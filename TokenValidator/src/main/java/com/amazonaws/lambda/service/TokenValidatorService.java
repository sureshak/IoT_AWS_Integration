package com.amazonaws.lambda.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
 


public class TokenValidatorService {
	
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    static String tableName = "ProductCatalog";
	
	public HashMap<String, Object> checkToken(int input,Context context){
		HashMap<String, Object> response= new HashMap<String, Object>();
		try{
			
			String tableName = "TokenManagement";
			Table table = dynamoDB.getTable(tableName); 
	        ItemCollection<ScanOutcome> items = table.scan(null, null, null);
	        System.out.println("Scan of " + tableName + " for items with a price less than 100.");
	        Iterator<Item> iterator = items.iterator();
	        context.getLogger().log("Iterator : "+iterator);
	        while (iterator.hasNext()) {
	        	String responseObject=iterator.next().toJSONPretty();
	        	try {
					JSONObject StringToJson=new JSONObject(responseObject);
					int token = StringToJson.getInt("Id");
					context.getLogger().log("token and input compare : "+token+" & "+input);
					if(token == input){
						response.put("phase", Integer.parseInt(StringToJson.get("Phase").toString()));
						response.put("status", "success");
			        	response.put("message", "Token authenticated successfully");
			        	response.put("code", 200);
						return response;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            //System.out.println(iterator.next().toJSONPretty());
	        } 
				response.put("status", "failure");
	        	response.put("message", "Token authentication failed");
	        	response.put("code", 400);
	        	return response;
    
			} catch (DynamoDbException e) {
				response.put("status", "failure");
	        	response.put("message", e.getMessage());
	        	response.put("code", 400); 
	        	return response;
			}
	}
}
