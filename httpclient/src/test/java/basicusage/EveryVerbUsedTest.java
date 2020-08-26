package basicusage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sparkstart.Environment;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EveryVerbUsedTest {

    static Stream verbList(){
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of("get"));
        args.add(Arguments.of("head"));
        args.add(Arguments.of("options"));
        args.add(Arguments.of("post"));
        args.add(Arguments.of("put"));
        args.add(Arguments.of("patch"));
        args.add(Arguments.of("delete"));
        args.add(Arguments.of("trace"));
        args.add(Arguments.of("connect"));
        return args.stream();
    }

    @ParameterizedTest(name = "verb sent reflected for {0}")
    @MethodSource("verbList")
    void verbReflectionTest(String verb) throws URISyntaxException, IOException, InterruptedException {
        final HttpClient client = HttpClient.newBuilder().proxy(ProxySelector.getDefault()).build();

        final URI uri = new URI(Environment.getBaseUri() + "/reflect");

        final HttpRequest.Builder request =
                HttpRequest.newBuilder().
                    uri(uri).
                    method(verb, HttpRequest.BodyPublishers.ofString(""));

        final HttpResponse<String> response =
                client.send(request.build(), HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(verb, response.headers().
                                        firstValue("X-VERB").
                                        orElse(""));

    }

}
