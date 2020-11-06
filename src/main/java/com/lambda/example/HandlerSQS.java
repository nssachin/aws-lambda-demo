package com.lambda.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class HandlerSQS implements RequestHandler<SQSEvent, String> {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(SQSEvent sqsEvent, Context context) {
        LambdaLogger logger = context.getLogger();
        String response = "200 OK";
        sqsEvent.getRecords().forEach(message -> {
            logger.log("Message Body: "+ message.getBody());
            JsonObject jsonObject = gson.fromJson(message.getBody(), JsonObject.class);
            JsonElement contents = jsonObject.get("Contents");
            logger.log("Message Content " + contents);
        });
        return response;
    }
}
