package org.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.models.Apigateway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Apigateway> {
    private static final String CONNECTION_STRING = "jdbc:mysql://<RDS-エンドポイント>:3306/rds_test";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    @Override
    public Apigateway handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> param = autoCast(input.get("queryStringParameters"));
        String input_text = param.get("input_text");
        String output_text = input_text + "-nyan";

        Map<String, String> text = new HashMap<>();
        text.put("value", output_text);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD)) {
            String update_sql = "INSERT INTO test(input_text, output_text) VALUES(\'" + input_text + "\', \'" + output_text + "\')";
            setInitialSessionState(conn,update_sql);
        } catch (SQLException e) {
            System.err.println("Exception!!");
            System.err.println(e.getMessage());
        }

        Apigateway ag = new Apigateway(200, gson.toJson(text), header, false);

        return ag;
    }

    @SuppressWarnings("unchecked")
    private <T> T autoCast(Object obj) {
        T castObj = (T) obj;
        return castObj;
    }

    private static void setInitialSessionState(Connection conn,String sql) throws SQLException {
        try (Statement stmt1 = conn.createStatement()) {
            stmt1.executeUpdate(sql);
        }
    }
}
