package basicusage;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sparkstart.Environment;


import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class HeaderUsageTest {

    @BeforeAll
    static void logRequests(){
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter());
    }

    @Test
    void canAddHeaderToRequestHeader() throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");

        String body = RestAssured.given().
                header("X-HEADER", "my header value").
                when().get(url).
                andReturn().body().prettyPrint();

        System.out.println(body);

        Assertions.assertTrue(body.contains("X-HEADER: my header value"));
    }

    @Test
    void canNotAddDuplicateHeadersToRequest() throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");

        String body = RestAssured.given().
                header("X-HEADER",
                        "my header value").
                header("X-HEADER",
                        "my other header value").
                when().get(url).
                andReturn().body().prettyPrint();

        System.out.println(body);

        Assertions.assertTrue(body.contains("X-HEADER: my header value"));
        Assertions.assertTrue(body.contains("X-HEADER: my other header value"));
    }

    @Test
    void canSendBasicAuthHeader() throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");

        // use pre-emptive auth to send with header
        String body = RestAssured.given().
                auth().preemptive().
                    basic("admin", "password").
                when().get(url).
                andReturn().body().prettyPrint();

        System.out.println(body);

        final String base64encoded = Base64.getEncoder().
                encodeToString("admin:password".getBytes());

        Assertions.assertTrue(body.contains("Authorization: Basic " + base64encoded));
    }
}
