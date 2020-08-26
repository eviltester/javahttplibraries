package application.http;

public class InternalRequestFormat {
    private final InternalRequest request;

    public InternalRequestFormat(final InternalRequest request) {
        this.request = request;
    }

    public String asText() {

        StringBuilder output = new StringBuilder();

        output.append(String.format("URL: %s\n", request.getUrl()));
        output.append(String.format("METHOD: %s\n", request.getVerb()));

        if(!request.getHeaders().isEmpty()){
            output.append(String.format("Headers:\n"));
            for(RequestHeader header: request.getHeaders()){
                output.append(String.format("%s: %s\n", header.name, header.value));
            }
        }
        if(request.getBody()!=null && request.getBody().trim().length()>0){
            output.append(String.format("Body:\n"));
            output.append(String.format("%s\n", request.getBody()));
        }
        return output.toString();
    }
}
