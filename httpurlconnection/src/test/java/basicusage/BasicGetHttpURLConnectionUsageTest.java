package basicusage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sparkstart.Environment;
import support.HttpUrlConnectionSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class BasicGetHttpURLConnectionUsageTest {

    @Test
    void canGetReflectEndPoint() throws URISyntaxException, IOException, InterruptedException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int statusCode = con.getResponseCode();

        String body = new HttpUrlConnectionSupport().
                            getResponseBody(con);
        System.out.println(body);

        Assertions.assertTrue(body.contains("METHOD: GET"));
    }

}
