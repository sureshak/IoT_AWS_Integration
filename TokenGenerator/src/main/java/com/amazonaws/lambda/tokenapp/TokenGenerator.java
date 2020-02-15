package com.amazonaws.lambda.tokenapp;

import java.util.HashMap;

import org.json.JSONObject;

import com.amazonaws.lambda.service.TokenGeneratorService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenGenerator implements RequestHandler<Object, HashMap<String, Object>> {

    @Override
    public HashMap<String, Object> handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        HashMap<String , Object> response=new HashMap<String, Object>();
        try{
        	TokenGeneratorService tokenGeneratorService=new TokenGeneratorService();
            context.getLogger().log("input is :"+input.toString());
            ObjectMapper objectMapper=new ObjectMapper(); 
            JSONObject parameters = new JSONObject(objectMapper.writeValueAsString(input));
            response=tokenGeneratorService.tokenOperator(parameters.getInt("phase"),context);
        }catch(Exception ex){
        	response.put("status", "failure");
        	response.put("message", ex.getMessage());
        	response.put("code", 400);
        }
        
        return response;
    }

}
