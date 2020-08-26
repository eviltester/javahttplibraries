package basicusage;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sparkstart.Environment;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EveryVerbUsedTest {

    @BeforeAll
    static void logRequests(){
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter());
    }

    static Stream verbList(){
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of("get"));
        args.add(Arguments.of("head"));
        args.add(Arguments.of("options"));
        args.add(Arguments.of("post"));
        args.add(Arguments.of("put"));
        args.add(Arguments.of("delete"));
        args.add(Arguments.of("trace"));
        args.add(Arguments.of("patch"));
        args.add(Arguments.of("connect"));
        return args.stream();
    }

    @ParameterizedTest(name = "verb sent reflected for {0}")
    @MethodSource("verbList")
    void verbReflectionTest(String verb) throws URISyntaxException, IOException, InterruptedException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect");

        final Response response = RestAssured.when().
                        request(verb, url).andReturn();

        Assertions.assertEquals(200, response.getStatusCode());

        Assertions.assertEquals(verb.toUpperCase(),
                response.getHeader("X-VERB"));
    }

    // REST Assured supports these verbs
    static Stream oftenUnsupportedVerbList(){
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of("patch"));
        args.add(Arguments.of("connect"));
        return args.stream();
    }


    /*
     With REST Assured we don't have to use override headers
     but we can if we want to.

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
    @MethodSource("oftenUnsupportedVerbList")
    void verbOverrideUnsupported(String verb) throws IOException {

        final URL url = new URL(Environment.getBaseUri() + "/reflect/override");

        final Response response = RestAssured.given().
                header("X-HTTP-Method-Override", verb).when().
                post(url).andReturn();

        Assertions.assertEquals(200, response.getStatusCode());

        Assertions.assertEquals(verb, response.getHeader("X-VERB"));
    }

}
