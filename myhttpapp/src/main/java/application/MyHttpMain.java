package application;

import spark.Request;
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
    }

    private static String outputRequest(final Request request) {
            StringBuilder output = new StringBuilder();

            output.append(String.format("URL: %s%n", request.url()));
            output.append(String.format("METHOD: %s%n", request.requestMethod()));
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
