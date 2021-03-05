package JavaAPITestDesignPack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.json.simple.JSONArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;


/*
 * This Class calls to request webservice, Store response, manipulate the response via Json methods 
 * 
 * 
 * 
 */


public class javaWebservice {
	
	
	/*
	 * This Method is used to call the Rest webservice and return string reponses
	 * @Param- RequestData 
	 * 
	 * 
	 */
	
	public RestResponse callRestWebservice(RestRequest requestData) {
		
		RestResponse responseData=new RestResponse();
		responseData.Request=requestData.Content;
		
	try {
		
		//Declare the URL connection
		HttpURLConnection conn=null;
		
		//Initializing the URL connection
		URL restserviceurl=new URL(requestData.Endpoint);
		
		conn=(HttpURLConnection) restserviceurl.openConnection();
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		conn.setRequestMethod(requestData.RequestMethod.toString());
		
		//Set the request property - content Type
		if(requestData.ContentType!=null&& !requestData.ContentType.trim().equalsIgnoreCase("")) {
			conn.setRequestProperty("Content-Type", requestData.ContentType);
		}
		
		//Set the request property - Authorization type
		if(requestData.AuthorizationType.equalsIgnoreCase("BASIC")) {
			
			String userPass=requestData.userID+ ":"+requestData.Password;
			String basicAuth="Basic"+new String(new Base64().encode(userPass.getBytes()));
			
			conn.setRequestProperty("Authorization",basicAuth);
		}
		
		//Define the headers
		if(requestData.Headers.size()!=0) {
			for(Header header:requestData.Headers) {
				conn.setRequestProperty(header.getName().trim(), header.getValue().trim());
			}
		}
		
		//Convert the request content to bytes for request call
		if(!requestData.Content.equalsIgnoreCase("")) {
			OutputStream Os=conn.getOutputStream();
			Os.write(requestData.Content.getBytes());
			Os.flush();			
		}
		
		//Get the connection call and store responses code
		int responseCOde=conn.getResponseCode();
		
		System.out.println("Get Repsonse code "+responseCOde);
		
		//Buffered reader to read the byte ode values into string
		try {
			
			BufferedReader br=null;
			
			if(responseCOde==200) {
				br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else {
				br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			String output="";
			String ResponseContent="";
			
			while((output=br.readLine())!=null) {
				
				ResponseContent=ResponseContent+output+'\n';
					
			}
			
			responseData.connectionStatus=Integer.toString(conn.getResponseCode());
			
			responseData.Response=FormatContent(ResponseContent);
			responseData.Request=FormatContent(responseData.Request);
			conn.disconnect();
		}catch(Exception e) {
			
		}		
		
		
	}catch(Exception e) {
		
	}
	//return the response content formated
	return responseData;
	
	}
	
		
	/*
	 * This Method is used to fromat the reponse content
	 * @Param- Response content
	 * 
	 * 
	 */
	
	public String FormatContent(String content) {
		String formattedContent = "";
		formattedContent = FormatJson(content);
		return formattedContent;
		}
	
	/*
	 * This Method is used to format the JSON response content
	 * @Param- Response content
	 * 
	 * 
	 */
	
	public String FormatJson(String jsonContent) {
		String formattedJson = jsonContent;

		try {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		@SuppressWarnings("deprecation")
		JsonParser jsonParser = new JsonParser();
		@SuppressWarnings("deprecation")
		JsonElement jsonElement = jsonParser.parse(jsonContent);
		formattedJson = gson.toJson(jsonElement);
		} catch (Exception ex) {

		}
		return formattedJson;
		}

	
	/*
	 * This Method is get the values(more than one value from Json responses string)
	 * @Param- Response content,String Json path
	 * 
	 * 
	 */
	
	public List<String> GetValuesFromJSON(String json, String jsonPathString) {


		List<String> jsonValues = new ArrayList<String>();

		try {
		DocumentContext docCtx = JsonPath.parse(json);
		JsonPath jsonPath = JsonPath.compile(jsonPathString);
		Object jsonObject = docCtx.read(jsonPath);

		if (jsonObject == null) {

		} else {
		if (jsonObject instanceof JSONArray) {
		JSONArray JA = docCtx.read(jsonPath);
		for (int i = 0; i < JA.size(); i++) {
		jsonValues.add(JA.get(i).toString());
		}
		} else if (jsonObject instanceof String) {
		String jsonObjectString = jsonObject.toString().replace("[", "").replace("]", "");
		jsonValues.add(jsonObjectString);
		} else if (jsonObject instanceof LinkedHashMap) {
		jsonValues.add(jsonObject.toString());
		}

		}
		} catch (Exception ex) {
		ex.printStackTrace();
		}
		return jsonValues;
		}
	
	/*
	 * This Method is used to get the value a single value based on the index
	 * @Param- Response content,String Json path, index
	 * 
	 * 
	 */
	
	public String GetValueFromJSON(String json, String jsonPath, int index) {


		String jsonValue = "";
		List<String> result = GetValuesFromJSON(json, jsonPath);

		if (result.size() > index) {
		jsonValue = result.get(index).toString();
		} else {
			}
		return jsonValue;
		}



}
