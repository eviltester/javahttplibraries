package basicusage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sparkstart.Environment;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EveryVerbReflectedTest {

    /*
       Common gotcha's with HttpUrlConnection:

       - verbs must be uppercase i.e `con.setRequestMethod("POST");`
       - `PATCH` and `CONN
       ECT` are not supported, try the `X-HTTP-Method-Override` if the server supports it
       - body is an Inputstream so needs to be read once into a string and then can be 're-used'

     */
    static Stream verbList(){
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of("get"));
        args.add(Arguments.of("head"));
        args.add(Arguments.of("options"));
        args.add(Arguments.of("post"));
        args.add(Arguments.of("put"));
        args.add(Arguments.of("delete"));
        args.add(Arguments.of("trace"));

        // HttpURLConnection cannot send patch and connect
        //args.add(Arguments.of("patch"));
        //args.add(Arguments.of("connect"));
        return args.stream();
    }

    @ParameterizedTest(name = "verb sent reflected for {0}")
    @MethodSource("verbList")
    void verbReflectionTest(String verb) throws URISyntaxException, IOException, InterruptedException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(verb.toUpperCase());

        Assertions.assertEquals(200, con.getResponseCode());

        Assertions.assertEquals(verb.toUpperCase(), con.getHeaderField("X-VERB"));
    }

    static Stream unsupportedVerbList(){
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of("patch"));
        args.add(Arguments.of("connect"));
        return args.stream();
    }

    @ParameterizedTest(name = "unsupported verbs throw error {0}")
    @MethodSource("unsupportedVerbList")
    void verbUnsupported(String verb) throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        Assertions.assertThrows(ProtocolException.class,
                () -> con.setRequestMethod(verb.toUpperCase())
        );
    }

    /*
     have to use the override headers

     https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/06-Test_HTTP_Methods

     e.g.

     - `headers.put("X-HTTP-Method-Override", "CONNECT")`
     - `headers.put("X-HTTP-Method-Override", "PATCH");`

     and then POST to send these methods, but not all servers are configured to support this approach
     and there is a potential that this approach is a security risk

     - `con.setRequestMethod("POST");`

     Override headers can be:

     - X-HTTP-Method
     - X-HTTP-Method-Override
     - X-Method-Override

     Although the actual header used is dependent on the server.

    */


    @ParameterizedTest(name = "unsupported verbs should use override header throw error {0}")
    @MethodSource("unsupportedVerbList")
    void verbOverrideUnsupported(String verb) throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect/override");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("X-HTTP-Method-Override", verb);

        con.setRequestMethod("POST");

        Assertions.assertEquals(200, con.getResponseCode());

        Assertions.assertEquals(verb, con.getHeaderField("X-VERB"));
    }

}
