package org.example.models;

import java.util.Map;

public class Apigateway {
    private int statusCode;
    private String body;
    private Map<String, String> headers = null;
    private boolean isBase64Encoded;

    public Apigateway(int sc, String b, boolean e) {
        this.statusCode = sc;
        this.body = b;
        this.isBase64Encoded = e;
    }

    public Apigateway(int sc, String b, Map<String, String> h, boolean e) {
        this.statusCode = sc;
        this.body = b;
        this.headers = h;
        this.isBase64Encoded = e;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public boolean getIsBase64Encoded() {
        return isBase64Encoded;
    }
}
