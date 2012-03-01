package org.jonnys.jsontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class ParseJSONActivity extends Activity {
	
/** Called when the activity is first created. */
	
	private TextView jsonText;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);		

		// create TextView to show JSON data
		jsonText = (TextView) findViewById(R.id.txtView1);
		
		//to make the TextView scrollable
		jsonText.setMovementMethod(new ScrollingMovementMethod());
		
		// download JSON data from a specific uri and store in a string
		//String jsonData = downloadJSONData(
				//"http://api.worldbank.org/country?per_page=10&region=WLD&lendingtype=IDX&format=json");
		
		getWorldBankData("http://api.worldbank.org/countries/JAM/indicators/AG.LND.AGRI.ZS?per_page=20&date=2000:2012&format=json", 0, 0);
	
//		JSONArray jsonArray = getJSONData(
//				//"http://api.worldbank.org/countries/JAM/indicators/AG.LND.AGRI.ZS?per_page=20&date=2000:2012&format=json"); //WorldBank
//				//"http://api.ids.ac.uk/openapi/eldis/search/documents/?q=jamaica");  //IDS authentication needed
//				"http://api.bing.net/json.aspx?Appid=9AF9ECDB61C76C398C8904F43FE3AB1D44F58AA4&query=agriculture%20reports%20in%20jamaica&sources=web"); //BING
//				
//		try {
//			
//			//JSONArray jsonArray = new JSONArray(jsonData);
//			
//			jsonText.append("Number of entries " + String.valueOf(jsonArray.length()));
//			jsonText.append("\n-------------------------------------\n");
//			
//			// for specific feed
//			JSONObject jsonObject = jsonArray.getJSONObject(0);
//			jsonText.append("Total: " +jsonObject.getString("total"));
//			
//			int numofObjects = Integer.parseInt(jsonObject.getString("total"));
//			
//			if (numofObjects > 0){
//				jsonArray = jsonArray.getJSONArray(1);
//			}
//			
//			for (int i = 0; i < numofObjects; i++) {
//				JSONObject jsonInnerObject = jsonArray.getJSONObject(i);
//				
//				jsonText.append(jsonInnerObject.toString()); 
//				jsonText.append("\n--------------------------------------\n\n");
//				
//				JSONArray names = jsonInnerObject.names();
//				
//				for(int x = 0; x < names.length(); x++){
//					
//					if(jsonInnerObject.optJSONObject(names.getString(x)) == null){
//						jsonText.append("\n" + names.getString(x) + ": " + jsonInnerObject.getString(names.getString(x)));
//					}else{
//						jsonText.append("\n" + names.getString(x) + ": ");
//						jsonText.append("\n\t" +jsonInnerObject.optJSONObject(names.getString(x)).toString());
//					}
//				}
//				
//				jsonText.append("\n--------------------------------------\n");
//				jsonText.append("--------------------------------------\n\n");
//			}					
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			jsonText.append(e.toString());
//			jsonText.append("\n--------------------------------------\n\n");
//		}
	}
	
	

	public String downloadJSONData(String uri) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		
		//convert response to string
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(ParseJSONActivity.class.toString(), "Failed to download file");
				jsonText.append(ParseJSONActivity.class.toString());
				jsonText.append("Failed to download file");
				jsonText.append("\n-----------------------\n");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			jsonText.append(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			jsonText.append(e.toString());
		}
		return builder.toString();
	}
	
	/* Use the ANdroid data type content value
	 * Indicators
	 * Countries
	 * Search_Parameters (
	 * 
	 * API is constant that represents the three API's
	 * */
		
	public void getWorldBankData(String uri, int API, int grpahType)
	{
		JSONArray jsonArray = getJSONData(uri);
				//"http://api.worldbank.org/countries/JAM/indicators/AG.LND.AGRI.ZS?per_page=20&date=2000:2012&format=json"); //WorldBank
				
		try {			
					
			
			//jsonText.append("Number of entries " + String.valueOf(jsonArray.length()));
			jsonText.append("\n-------------------------------------\n");
			
			// To get the total number of results resent by the query
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			jsonText.append("Total: " +jsonObject.getString("total"));
			
			int numofObjects = Integer.parseInt(jsonObject.getString("total"));
			
			// validation to get the actual results from the api
			if (numofObjects > 0){
				jsonArray = jsonArray.getJSONArray(1);
			}
			
			/// itereate through the list of the results and parse the data
			for (int i = 0; i < numofObjects; i++) {
				JSONObject jsonInnerObject = jsonArray.getJSONObject(i);
				
				jsonText.append(jsonInnerObject.toString()); 
				jsonText.append("\n--------------------------------------\n\n");
				
				// get object names
				JSONArray names = jsonInnerObject.names();
				jsonText.append("\n--------------------------------------\n\n");
				jsonText.append(names.toString());
				
				// iterate through the names to get the particular value for the object
				for(int x = 0; x < names.length(); x++){
					
					if(jsonInnerObject.optJSONObject(names.getString(x)) == null){
						jsonText.append("\n" + names.getString(x) + ": " + jsonInnerObject.getString(names.getString(x)));
					}else{
						jsonText.append("\n" + names.getString(x) + ": ");
						jsonText.append("\n\t" +jsonInnerObject.optJSONObject(names.getString(x)).toString());
					}
				}
				
				jsonText.append("\n--------------------------------------\n");
				jsonText.append("--------------------------------------\n\n");
			}					
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonText.append(e.toString());
			jsonText.append("\n--------------------------------------\n\n");
		}		
	}
	
	
	public void getBingData(String uri, int API, int grpahType)
	{
		JSONArray jsonArray = getJSONData(uri);
				
		try {	
						
			// To get the total number of results resent by the query
			JSONObject jsonObject = jsonArray.getJSONObject(1);
			//jsonText.append("Total: " +jsonObject.getString("Total"));
			jsonText.append("Total: " +jsonObject.getInt("Total"));
			
			
			//int numofObjects = Integer.parseInt(jsonObject.getString("total"));
			int numofObjects = jsonObject.getInt("Total");
			
			// validation to get the actual results from the API
			if (numofObjects > 0){
				jsonArray = jsonArray.getJSONArray(1);
			}
			
			/// itereate through the list of the results and parse the data
			for (int i = 0; i < numofObjects; i++) {
				JSONObject jsonInnerObject = jsonArray.getJSONObject(i);
				
				jsonText.append(jsonInnerObject.toString()); 
				jsonText.append("\n--------------------------------------\n\n");
				
				// get object names
				JSONArray names = jsonInnerObject.names();
				jsonText.append(names.toString());
				jsonText.append("\n--------------------------------------\n\n");
				
				// iterate through the names to get the particular value for the object
				for(int x = 0; x < names.length(); x++){
					
					if(jsonInnerObject.optJSONObject(names.getString(x)) == null){
						jsonText.append("\n" + names.getString(x) + ": " + jsonInnerObject.getString(names.getString(x)));
					}else{
						jsonText.append("\n" + names.getString(x) + ": ");
						jsonText.append("\n\t" +jsonInnerObject.optJSONObject(names.getString(x)).toString());
					}
					jsonText.append("\n--------------------------------------\n");
				}				
				
				jsonText.append("--------------------------------------\n\n");
			}					
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonText.append(e.toString());
			jsonText.append("\n--------------------------------------\n\n");
		}		
	}
	
	
	
	// download JSON data in JSON Array format
	public JSONArray getJSONData(String uri) 	{
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		
		//HttP
		//httpGet.setParams(params)
		
		//TODO Implement Authorization for the IDS API
		// Store JSON Array
		JSONArray jArray = null;
		
		//convert response to string

		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				//Log.e(ParseJSON.class.toString(), "Failed to download file");
				jsonText.append(ParseJSONActivity.class.toString());
				jsonText.append("Failed to download file\n");
				jsonText.append("\n-----------------------\n");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			jsonText.append(e.toString());
			jsonText.append("\n-----------------------\n");
		} catch (IOException e) {
			e.printStackTrace();
			jsonText.append(e.toString());
			jsonText.append("\n-----------------------\n");
			
		}
		
		//try parse the string to a JSON object
		try{
	        	jArray = new JSONArray(builder.toString());
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
			jsonText.append("Error parsing data "+e.toString());
			jsonText.append("-----------------------\n");
		}

		return jArray;		
	}
	
}