package application;

import spark.Request;
import spark.Response;
import spark.Spark;

import static spark.Spark.*;

public class MyHttpMain {

    static public void main(final String[] args){

        int proxyport = 4567;

        for (String arg : args) {
            System.out.println("Args: " + arg);

            if (arg.startsWith("-port")) {
                String[] details = arg.split("=");
                if (details != null && details.length > 1) {
                    proxyport = Integer.parseInt(details[1].trim());
                    System.out.println("Will configure web server to use port " + proxyport);
                }
            }
        }


        Spark.port(proxyport);
        //Spark.staticFileLocation("/public");  // main/resources/public

        before((request, response) ->{
            System.out.println(request.url());
            response.header("X-VERB", request.requestMethod());
        });

        get("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        head("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        options("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        post("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        put("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        patch("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        delete("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        trace("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        connect("/reflect", (request, response) ->{
            return (outputRequest(request));
        });

        get("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        head("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        options("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        post("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        put("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        patch("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        delete("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        trace("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });

        connect("/reflect/override", (request, response) ->{
            return (outputRequestOverride(request, response));
        });
    }

    private static String outputRequestOverride(final Request request, final Response response) {

        //X-HTTP-Method
        //X-HTTP-Method-Override
        //X-Method-Override

        String[] overrideHeaders = {"X-HTTP-Method",
                                    "X-HTTP-Method-Override",
                                    "X-Method-Override"};
        String overrideVerb = "";

        for(String overrideHeaderName : overrideHeaders){
            if(request.headers(overrideHeaderName)!=null && request.headers(overrideHeaderName).trim().length()>0 ){
                overrideVerb = request.headers(overrideHeaderName);
                break;
            }
        }

        if(!overrideVerb.equals("")){
            response.raw().setHeader("X-VERB", overrideVerb);
        }

        return outputRequest(request, overrideVerb);
    }

    private static String outputRequest(final Request request) {
        return outputRequest(request, "");
    }

    private static String outputRequest(final Request request, String overrideVerb) {
        StringBuilder output = new StringBuilder();

        output.append(String.format("URL: %s%n", request.url()));
        if(overrideVerb.equals("")) {
            output.append(String.format("METHOD: %s%n", request.requestMethod()));
        }else{
            output.append(String.format("METHOD: %s%n", overrideVerb));
        }

        if(!request.headers().isEmpty()){
            output.append(String.format("Headers:%n"));
            for(String header: request.headers()){
                output.append(String.format("%s: %s%n", header, request.headers(header)));
            }
        }
        if(request.body()!=null && request.body().trim().length()>0){
            output.append(String.format("Body:%n"));
            output.append(String.format("%s%n", request.body()));
        }
        return output.toString();
    }
}
