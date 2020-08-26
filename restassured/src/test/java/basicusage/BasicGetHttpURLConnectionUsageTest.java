package basicusage;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sparkstart.Environment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class BasicGetHttpURLConnectionUsageTest {

    @Test
    void canGetReflectEndPoint() throws URISyntaxException, IOException, InterruptedException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");

        String body = RestAssured.get(url).
                andReturn().body().prettyPrint();

        System.out.println(body);

        Assertions.assertTrue(body.contains("METHOD: GET"));
    }

}
