package com.amazonaws.lambda.service;

import java.util.HashMap; 
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import com.amazonaws.services.lambda.runtime.Context;

public class TokenGeneratorService {
	
	
	public  HashMap<String, Object> tokenOperator(int createTokeninput,Context context){
		HashMap<String , Object> response= new HashMap<String, Object>();
		try{
			//Token Generator method
			Integer token=createToken(context);	
			
			if(token != null ){			
				//Send the token as SMS to operator
				SmsService smsService=new SmsService();
				JSONObject reply=new JSONObject(smsService.sendSms(token,context));
				//JSONObject reply=new JSONObject("{'balance':8,'batch_id':1076662352,'cost':1,'num_messages':1,'message':{'num_parts':1,'sender':'TXTLCL','content':'Authentication Token:654192'},'receipt_url':'qweqwe','custom':'sadasd','messages':[{'id':'11494634449','recipient':919995586967}],'status':'success'}");
				context.getLogger().log("reply from SMS Service : "+reply.toString());
				if(reply.get("status").equals("success") || reply.get("status") == "success"){
					//save to dynamoDB
					String tableName = "TokenManagement";
					String key = "TokenId";
					String keyVal = String.valueOf(token);
					String phaseKey = "Phase";
					String phaseValue = String.valueOf(createTokeninput); 

					// Create the DynamoDbClient object
					Region region = Region.US_EAST_1;
					DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

					HashMap<String,AttributeValue> itemValues = new HashMap<String,AttributeValue>();

					// Add content to the table
					itemValues.put(key, AttributeValue.builder().n(keyVal).build());
					itemValues.put(phaseKey, AttributeValue.builder().n(phaseValue).build());

					// Create a PutItemRequest object
					PutItemRequest request = PutItemRequest.builder()
					        .tableName(tableName)
					        .item(itemValues)
					        .build();
					try {
					    ddb.putItem(request);
					    context.getLogger().log("Table updated successfully");
					    System.out.println(tableName +" was successfully updated");
					    response.put("token", token);
					    response.put("status", "success");
					    response.put("code", 200);
					    response.put("message", "token generation and messaging operation successfull.");
					    return response;
					} catch (ResourceNotFoundException e) {
						response.put("status", "failure");
					    response.put("code", 400);
					    response.put("message", "The table "+tableName+" can't be found");
					    return response;
					} catch (DynamoDbException e) {
						response.put("status", "failure");
					    response.put("code", 400);
					    response.put("message", e.getMessage());
					    return response;
					}
				}else{
					JSONArray msgResponce=reply.getJSONArray("errors"); 
					response.put("status", reply.get("status"));
					response.put("code", msgResponce.getJSONObject(0).get("code"));
					response.put("message",msgResponce.getJSONObject(0).get("message"));
					return response;
				}
			}else{ 
				response.put("status","Failure" );
				response.put("message", "Error in token generation.");
				response.put("code",400);
				return response;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response.put("message", ex.getMessage());
			response.put("status", "failure");
			response.put("code",400 );
		}
		return response;
	} 
	
	public Integer createToken(Context context){
		Integer rand_int1=null;
		try{
			Random rand = new Random(); 	  
	        // Generate random integers in range 0 to 999999
	        rand_int1 = rand.nextInt(1000000); 
	  
	        // Print random integers 
	        context.getLogger().log("Random Integers: "+rand_int1); 
		}catch(Exception ex){
			rand_int1=null;
		}
		return rand_int1;
	}
	
}
