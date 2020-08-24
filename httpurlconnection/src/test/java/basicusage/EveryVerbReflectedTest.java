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

    // have to use the
    // e.g.
    // - headers.put("X-HTTP-Method-Override", "CONNECT")
    // - headers.put("X-HTTP-Method-Override", "PATCH");
    //
    // and then POST
    //
    // con.setRequestMethod("POST");
}
