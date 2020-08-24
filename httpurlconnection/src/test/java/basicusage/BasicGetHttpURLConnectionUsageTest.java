package basicusage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sparkstart.Environment;

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

        String body = getResponseBody(con);
        System.out.println(body);

        Assertions.assertTrue(body.contains("METHOD: GET"));
    }

    private String getResponseBody(HttpURLConnection con) {
        BufferedReader in=null;

        // https://stackoverflow.com/questions/24707506/httpurlconnection-how-to-read-payload-of-400-response
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        }catch(Exception e){
            // handle 400 exception messages
            InputStream stream = con.getErrorStream();
            if(stream!=null) {
                in = new BufferedReader(
                        new InputStreamReader(stream));
            }
        }

        String inputLine;
        StringBuffer responseBody = new StringBuffer();

        try{
            if(in!=null) {
                while ((inputLine = in.readLine()) != null) {
                    responseBody.append(inputLine);
                }
                in.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return responseBody.toString();
    }

}
