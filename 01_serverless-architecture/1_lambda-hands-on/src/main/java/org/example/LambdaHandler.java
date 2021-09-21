package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String,Object>,Map<String,Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String ,Object> json = new HashMap<>();
        json.put("statusCode","200");
        json.put("body","test");
        json.put("input",input);
        json.put("context",context);
        return json;
    }
}
