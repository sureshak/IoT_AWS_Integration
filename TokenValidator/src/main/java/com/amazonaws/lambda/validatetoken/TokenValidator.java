package com.amazonaws.lambda.validatetoken;

import java.util.HashMap;

import org.json.JSONObject;

import com.amazonaws.lambda.service.TokenValidatorService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenValidator implements RequestHandler<Object, HashMap<String, Object>> {

    @Override
    public HashMap<String, Object> handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        HashMap<String, Object> response=new HashMap<String, Object>();
        try{
        	TokenValidatorService tokenValidatorService=new TokenValidatorService();           
            context.getLogger().log("input is :"+input.toString());
            ObjectMapper objectMapper=new ObjectMapper(); 
            //JSONObject parameters = new JSONObject(objectMapper.writeValueAsString(input));
            JSONObject parameters = new JSONObject(input.toString()); 
            //context.getLogger().log("token value :"+parameters.getInt("token"));
            response=tokenValidatorService.checkToken(parameters.getInt("Id"), context);
        }catch(Exception ex){
        	System.out.println(ex.getMessage());
        }
        
        // TODO: implement your handler
        return response;
    }

}
