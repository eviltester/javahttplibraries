package application.http;

public class RequestHeader {

    final String name;
    final String value;

    public RequestHeader(final String header, final String headerValue) {
        this.name = header;
        this.value = headerValue;
    }
}
