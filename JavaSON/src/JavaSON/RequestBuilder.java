package JavaSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RequestBuilder {
	//REGEX of url
	private String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	//URL
	private String URL = null;
	//Charset
	private final String charset = "UTF-8"; 
	
	private Map<String, String> additionalAttributes = new HashMap<String,String>();
	/*
	 * Constructor passing a URL
	 */
	public RequestBuilder(String URL) {
		this.URL = URL;
	}
	/*
	 * Empty Constructor
	 */
	public RequestBuilder() {
		super();
	}
	
	/*
	 * Getters and setters for required attributes
	 */
	public void setURL(String url) {
		this.URL = url;
	}
	public String getURL() {
		return URL;
	}
	public String getCharset() {
		return charset;
	}
	public void addAttribute(String attribute, String value) {
		additionalAttributes.putIfAbsent(attribute, value);
	}
	public void removeAttribute(String attribute) {
		additionalAttributes.remove(attribute);
	}
	public void changeAttribute(String attribute, String newValue) {
		if(!additionalAttributes.containsKey(attribute)) {
			System.out.println("Attribute \"" + attribute + "\" does not exist");
			return;
		}
		additionalAttributes.replace(attribute, newValue);
	}
	public String getCustomAttributes() {
		String aa="";
		for (Entry<String, String> i : additionalAttributes.entrySet()) {
			aa+=(i.getKey() + " : " + i.getValue() + " ");
		}
		return aa;
	}
	private boolean canBuild() {
		return (URL != null && URL.matches(urlRegex));
	}
	
	public String buildRequest() {
		if(!canBuild()) {
			System.out.println("Please set a valid URL before building a request");
			return null;
		}
		String attributes = "";
		for(Entry<String,String> i : additionalAttributes.entrySet()) {
			if(i.getValue().contains(" ")) {
				attributes+=i.getKey() + "="+i.getValue().replace(" ", "%20") + "&";
			}else {
				attributes += i.getKey() + "=" +i.getValue() + "&";
			}
			
		}
		if (attributes.length()>1) {
			attributes = attributes.substring(0,attributes.length() - 1);
			return URL + "?" + attributes;
		}
		return URL;
	}
	/*
	 * A handy method that allows you to turn your miles to meters
	 */
	public static double milesToMeters(int miles) {
		return (miles*1609.34);
	}
	
	
}
