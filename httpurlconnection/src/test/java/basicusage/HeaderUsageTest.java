package basicusage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sparkstart.Environment;
import support.HttpUrlConnectionSupport;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class HeaderUsageTest {

/*

HttpURLConnection is pretty basic and doesn't have any semantic support for
HTTP headers.

Headers are added with the `setRequestProperty` method.

When adding multiple headers of same name - only one is added, and it is the last one.
 */

    @Test
    void canAddHeaderToRequestHeader() throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("X-HEADER", "my header value");

        String body = new HttpUrlConnectionSupport().getResponseBody(con);
        System.out.println(body);

        Assertions.assertTrue(body.contains("X-HEADER: my header value"));
    }

    @Test
    void canNotAddDuplicateHeadersToRequest() throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("X-HEADER", "my header value");
        con.setRequestProperty("X-HEADER", "my other header value");

        String body = new HttpUrlConnectionSupport().getResponseBody(con);
        System.out.println(body);

        // can only send one version of each header
        Assertions.assertFalse(body.contains("X-HEADER: my header value"));
        Assertions.assertTrue(body.contains("X-HEADER: my other header value"));
    }

    @Test
    void canSendBasicAuthHeader() throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        final String base64encoded = Base64.getEncoder().
                                    encodeToString("admin:password".getBytes());

        con.setRequestProperty("Authorization", "Basic " + base64encoded);


        String body = new HttpUrlConnectionSupport().getResponseBody(con);
        System.out.println(body);

        Assertions.assertTrue(body.contains("Authorization: Basic " + base64encoded));
    }
}
