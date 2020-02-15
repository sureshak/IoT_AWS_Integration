package com.amazonaws.lambda.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.amazonaws.services.lambda.runtime.Context;

public class SmsService {
	public String sendSms(Integer token, Context context) {
		try {
			// Construct data
			String apiKey = "apikey=" + "Ylw+TjHSlIQ-skFpRbeY57rhMHyQQt1ZHBW1f9vhGX";
			String message = "&message=" + "Authentication Token:"+token;
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + "919995586967";
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			context.getLogger().log("-------messgae sent------"+stringBuffer.toString());
			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			context.getLogger().log("-------messgae not sent------"+e.getMessage());
			return "Error "+e;
		}
	}
}
