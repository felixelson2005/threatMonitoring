package JavaAPITestDesignPack;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

/*
 * 
 * Request class- Declaring Variables
 */

public class RestRequest {
	
	public RequestMethod RequestMethod;
	public String Endpoint;
	public String Content;
	public String AuthorizationType;
	public String ContentType;
	public String userID;
	public String Password;	
	public List<Header> Headers=new ArrayList<Header>();
	public boolean FaultExpected=false;
	
	public RestRequest() {
		
		AuthorizationType="Basic";
		userID="Admin";
		Password="Admin";
		ContentType="application/json";
		
	}
	

}
