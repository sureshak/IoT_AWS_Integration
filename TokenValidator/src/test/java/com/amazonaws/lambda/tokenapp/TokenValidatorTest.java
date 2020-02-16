package com.amazonaws.lambda.tokenapp;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.lambda.validatetoken.TokenValidator;
import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class TokenValidatorTest {

    private static Object input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
    	HashMap<String,Object> test=new HashMap<String, Object>();
    	test.put("Id", 102939);
    	test.put("phase", 1);
        input = test;
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testTokenValidator() {
        TokenValidator handler = new TokenValidator();
        Context ctx = createContext();

        HashMap<String , Object> output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        Assert.assertEquals("Hello from Lambda!", output);
    }
}
