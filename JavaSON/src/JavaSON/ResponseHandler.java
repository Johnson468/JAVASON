package JavaSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseHandler {
	String request = null;
	String charset = null;
	InputStream response = null;
	URLConnection connection = null;
	StringBuilder sb = null;
	JSONObject jObj = null;
	JSONArray jArray = null;
	Map<String,LinkedList<String>> resultMap = null;
	/*
	 * Set up the request and the connection object
	 */
	public ResponseHandler(String request, String charset) {
		this.request = request;
		this.charset = charset;
		sb = new StringBuilder();
		try {
			connection = new URL(request).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StringBuilder getRequest() {
		try {
			response = connection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response==null) {
			return null;
		}
		Scanner responseScanner = new Scanner(response);
		while (responseScanner.hasNextLine()) {
			sb.append(responseScanner.nextLine());
		}
		responseScanner.close();
		return sb;
	}
	/*
	 * sb = the request
	 * Target = the part of the JSON response the user wants
	 * Values = Values the user wants
	 */
	public Map<String,LinkedList<String>> getResults(StringBuilder sb, String target, String[] values) throws JSONException {
		LinkedList<String> results = new LinkedList<String>();
		LinkedList<String>[] valuesArray = new LinkedList[values.length];
		for (int x =0; x < valuesArray.length; x++) {
			valuesArray[x] = new LinkedList<String>();
		}
		int currNum = 0;
		try {
			jObj = new JSONObject(sb.toString());
			//Key is the part of the JSON to get the info from
			jArray = jObj.getJSONArray(target);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(jArray == null || target == null || values.length == 0) {
			System.out.println("An error has occured");
			return null;
		}
		resultMap = new HashMap<String,LinkedList<String>>();
		//For each item in the array
		for (int i = 0; i < jArray.length(); i++) {
			/*
			 * Put each item the user wants to get into an array of lists that corresponds with a value
			 */
			for(int x = 0; x < values.length; x++) {
				if(jArray.getJSONObject(i).get(values[x]) != null){
				//results.add(jArray.getJSONObject(i).get(values[x]).toString());
				valuesArray[x].add(jArray.getJSONObject(i).get(values[x]).toString());
				//currNum = x;
				}
			}

		}
		appendMap(values,valuesArray);
		return resultMap;
	}
	
	private void appendMap(String[] keys, LinkedList[] items) {
		/*
		 * Add the linked lists to the map using the array of values the user wants
		 * As the keys
		 */
		for (int x =0; x < items.length;x++) {
			resultMap.put(keys[x], items[x]);
		}
	}
	
	public void printResults() {
		for (Entry<String, LinkedList<String>> i : resultMap.entrySet()) {
			System.out.println(i.getKey() + " : " + i.getValue());
		}
	}
	
	
}
