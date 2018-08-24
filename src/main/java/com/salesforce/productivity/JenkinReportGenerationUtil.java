package com.salesforce.productivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class JenkinReportGenerationUtil {
	
	public static void main(String[] args) {
		JenkinReportGenerationUtil obj = new JenkinReportGenerationUtil();
		System.out.println("Unique ID generated is: " + obj.generateUniqueKey());
	}

	public String generateUniqueKey() {
		String id = UUID.randomUUID().toString();
		return id;
	}
	
	public Boolean generateRandomVal() {
		int randomNum = ThreadLocalRandom.current().nextInt(1, 100);
		if(randomNum%2==0)
			System.out.println("True");
		else
			System.out.println("False");;
		return Boolean.TRUE;
	}

	public int canHitGoogle(){
		try {
			URL url = new URL("https://git.soma.salesforce.com");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
//			connection.connect();
//
			int code = 200; //connection.getResponseCode();
			System.out.println("Returning code: " + code);
			return code;
		} catch (Exception exp){
			return -1;
		}
	}
	
	public void unusedMethod() {
		System.out.println("Adding this method just to check the code coverage");
	}
}
 
 
