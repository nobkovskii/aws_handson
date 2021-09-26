package org.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.models.Apigateway;

import java.util.HashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Apigateway> {

    @Override
    public Apigateway handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> param = autoCast(input.get("queryStringParameters"));
        Map<String, String> text = new HashMap<>();
        text.put("value", param.get("input_text") + "-nyan");

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        Apigateway ag = new Apigateway(200, gson.toJson(text), header, false);

        return ag;
    }

    @SuppressWarnings("unchecked")
    private <T> T autoCast(Object obj) {
        T castObj = (T) obj;
        return castObj;
    }
}
