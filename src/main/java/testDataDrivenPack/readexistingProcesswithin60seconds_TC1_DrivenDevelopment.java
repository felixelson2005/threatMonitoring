package testDataDrivenPack;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import com.google.gson.Gson;
import com.threatmonitor.server.main.dataDrivenfunctions;

public class readexistingProcesswithin60seconds_TC1_DrivenDevelopment {

	@Test
	public void readexistingProcesswithin60seconds() {


		/*
		 * TestScript Summary- Identifying an existing running process on the system which is captured within 60 seconds from the previous session
		 *
		 * Expectations- When system finds a existing running process codes everytime, it should Discard the process details in the cache but counter will be incremented
		 *
		 * Static value of process- codes taken upto 5 numbers
		 *
		 * Results- When System identifies the existing running process in teh memory within 60 seconds, system discard to add it in file
		 *
		 * Fyi- Capture can be identified by TimeStamp of each process codes
		 *
		 */


		try {

			//calling Emulate process code functions to generate given number of process to capture
			dataDrivenfunctions.GenerateProcess(5);

			//Json parser declaration
			JSONParser jsonParser = new JSONParser();

			//File reader class to load the JSON file .i.e reading existing captured process details
			FileReader reader = new FileReader(System.getProperty("user.dir")+"//src//main//resources//processCodefile.json");

			//decalring Obj for JsonParser
			Object obj = jsonParser.parse(reader);

			//JSONString to json file read responses
			String jsonInString = new Gson().toJson(obj);

			//JSON Object to store existing JSON file process codes
			JSONObject mJSONObject = new JSONObject(jsonInString);

			//declaring counter variable
			JSONObject ProcessExistingjson = mJSONObject;

			System.out.println();

			//declaring JSOn object
			JSONObject ExtendProcess=new JSONObject();

			//Declaring iterator to read for each new existing codes
			Iterator<String> keys= ProcessExistingjson.keys();

			int changecount=0;

			//Looping starts
			while(keys.hasNext()) {

				//Store each process code in a local variable
				String processcode=keys.next();

				//capture the timestamp of newly captured existing process
				String newtimeStamp = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(new Date());

				//Existing running process captured timestamp
				String oldtimestamp=ProcessExistingjson.getString(processcode);

				//Newly captured existing process for teh data validation
				ExtendProcess.put(processcode, newtimeStamp);

				//If newly captured existied running process has differences of 60 seconds then timestamp will be updated in the memory
				if(dataDrivenfunctions.timeDifferbetween2dates(oldtimestamp,newtimeStamp)) {
					int counter=0;
					System.out.println("Process tracked less than in 60 seconds");
					System.out.println("Not updating the file with process and stamp");
					System.out.println("Counter value is"+counter++);


				}else {
					System.out.println("Process tracked more than in 60 seconds");
					ProcessExistingjson.put(processcode, newtimeStamp);
					changecount++;
				}

			}

			//When there is a change in teh process timestamp, update the JSON file
			if(changecount>0) {

				dataDrivenfunctions.JsonFileUpdation(ProcessExistingjson);

			}else {

				System.out.println("No updates made");


			}
		}catch(Exception e) {

			e.printStackTrace();

		}

	}

}

