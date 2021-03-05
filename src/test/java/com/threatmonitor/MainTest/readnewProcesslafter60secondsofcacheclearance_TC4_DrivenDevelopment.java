package com.threatmonitor.MainTest;

import java.io.FileReader;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import com.google.gson.Gson;
import com.threatmonitor.main.dataDrivenfunctions;

public class readnewProcesslafter60secondsofcacheclearance_TC4_DrivenDevelopment {


	/*
	 * TestScript Summary- Identifying a new process on the system which is captured after 60 seconds from the previous session
	 *
	 * Expectations- When system finds a new process codes everytime, it should update the process details in the cache and store it in the database
	 *
	 * Static value of process- codes taken upto 5 numbers
	 *
	 * Results- When there is any new process identified from the existing process after 60 seconds, the function will store the process details in the memory
	 *
	 */


	@Test
	public void readnewProcesslessthan60seconds() {

		try {

			//calling Emulate process code functions to generate given number of process to capture
			dataDrivenfunctions.GenerateProcess(5);

			TimeUnit.SECONDS.sleep(61);

			//Json parser declaration
			JSONParser jsonParser = new JSONParser();

			//File reader class to load the JSON file .i.e reading existing captured process details
			FileReader reader = new FileReader(System.getProperty("user.dir")+"//src//main//resources//processCodefile.json");

			//decalring Obj for JsonParser
			Object obj = jsonParser.parse(reader);

			//JSONString to json file read responses
			String jsonInString = new Gson().toJson(obj);

			//JSON Object to store existing JSON file process codes
			JSONObject ProcessExistingjson = new JSONObject(jsonInString);

			//declaring counter variable
			int counter=0;

			//Delare and define the extends process to strore the newly generated process codes
			JSONObject ExtendProcess=dataDrivenfunctions.generateProcess(5);

			//Declaring iterator to read for each new process codes
			Iterator<String> keys= ExtendProcess.keys();

			//Looping starts
			while(keys.hasNext()) {

				//Store each process code in a local variable
				String newprocessCode=keys.next();

				//Check if new process code is present in the existing running process
				if(ProcessExistingjson.has(newprocessCode)) {

					System.out.println("No capturing is required");
				}else {

					System.out.println("Update to the existing file");

					//existing process file will be updated with new process codes
					ProcessExistingjson.put(newprocessCode, ExtendProcess.get(newprocessCode));

					//counter increased for teh condition
					counter++;

				}
			}

			//If counter has any change then Update teh JSON file with new process updates otherwise no updates required
			if(counter>0) {
				System.out.println("Json Update is required");
				dataDrivenfunctions.JsonFileUpdation(ProcessExistingjson);
			}else {
				System.out.println("Json Update is not required as there is no new process");
			}

		}catch(Exception e) {

			e.printStackTrace();

		}

	}
}

//End of Test

