package application.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InternalRequest {

    String verb;
    String url;
    List<RequestHeader> headers;
    String body;

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setVerb(final String requestMethod) {
        this.verb = requestMethod;
    }

    public void addHeader(final String header, final String headerValue) {
        if(headers==null){
            headers = new ArrayList<>();
        }
        headers.add(new RequestHeader(header, headerValue));
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public String getVerb() {
        return verb;
    }

    public List<RequestHeader> getHeaders() {
        if(headers==null){
            return new ArrayList<>();
        }

        return headers;
    }

    public String getBody() {
        return body;
    }
}
