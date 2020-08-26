package basicusage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sparkstart.Environment;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class HeaderUsageTest {

/*

HttpURLConnection is pretty basic and doesn't have any semantic support for
HTTP headers.

Headers are added with the `setRequestProperty` method.

 */

    @Test
    void canAddHeaderToRequestHeader() throws IOException, URISyntaxException, InterruptedException {

        final HttpClient client = HttpClient.newBuilder().
                                    proxy(ProxySelector.getDefault()).build();

        final URI uri = new URI(Environment.getBaseUri() + "/reflect");

        final HttpRequest.Builder request = HttpRequest.newBuilder().
                uri(uri);

        request.method("GET", HttpRequest.BodyPublishers.ofString(""));

        request.header("X-HEADER", "my header value");

        final HttpResponse<String> response =
                client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Assertions.assertTrue(response.body().contains("X-HEADER: my header value"));
    }

    @Test
    void canAddDuplicateHeadersToRequest() throws IOException, InterruptedException, URISyntaxException {

        final HttpClient client = HttpClient.newBuilder().
                proxy(ProxySelector.getDefault()).build();

        final URI uri = new URI(Environment.getBaseUri() + "/reflect");

        final HttpRequest.Builder request = HttpRequest.newBuilder().
                uri(uri);

        request.method("GET", HttpRequest.BodyPublishers.ofString(""));

        request.header("X-HEADER", "my header value");
        request.header("X-HEADER", "my other header value");

        final HttpResponse<String> response =
                client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Assertions.assertTrue(response.body().contains("X-HEADER: my header value"));
        Assertions.assertTrue(response.body().contains("X-HEADER: my other header value"));
    }

    @Test
    void canSendBasicAuthHeader() throws IOException, InterruptedException, URISyntaxException {

        final HttpClient client = HttpClient.newBuilder().
                proxy(ProxySelector.getDefault()).build();

        final URI uri = new URI(Environment.getBaseUri() + "/reflect");

        final HttpRequest.Builder request = HttpRequest.newBuilder().
                uri(uri);

        request.method("GET", HttpRequest.BodyPublishers.ofString(""));

        final String base64encoded = Base64.getEncoder().
                encodeToString("admin:password".getBytes());
        request.header("Authorization", "Basic " + base64encoded);


        final HttpResponse<String> response =
                client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Assertions.assertTrue(response.body().
                contains("Authorization: Basic " + base64encoded));
    }
}
