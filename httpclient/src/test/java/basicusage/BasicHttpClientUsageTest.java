package basicusage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sparkstart.Environment;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BasicHttpClientUsageTest {

    @Test
    void basicUsageExample() throws URISyntaxException, IOException, InterruptedException {

        final HttpClient client = HttpClient.newBuilder().proxy(ProxySelector.getDefault()).build();

        final URI uri = new URI(Environment.getBaseUri() + "/reflect");
        // todo: create a 'good' reflect api that returns a json or xml object representing the request
        final HttpRequest.Builder request = HttpRequest.newBuilder().
                uri(uri);

        request.method("GET", HttpRequest.BodyPublishers.ofString(""));

        final HttpResponse<String> actualResponse =
                client.send(request.build(), HttpResponse.BodyHandlers.ofString());

        System.out.println(actualResponse.body());

        Assertions.assertTrue(actualResponse.body().contains("GET"));

    }

}
