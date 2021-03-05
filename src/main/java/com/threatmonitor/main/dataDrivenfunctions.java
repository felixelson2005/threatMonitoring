package com.threatmonitor.main;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class dataDrivenfunctions {

	//File writer to load,manage the files
	private static FileWriter filewriter;


	/*
	 *
	 * Assumption- This Function Call is happened to Trigger the monitor and Identify the running process on the System
	 *
	 * This method is used to Emulates the random records- process code as Key and date timestamp as value and store it in the JSONObject
	 *
	 *
	 * @Params - Expecting number of process to be emulated
	 *
	 * @Return - Expecting JSON object with Key as sample hashcode and value as datetime stamp
	 *
	 */
	public static JSONObject  generateProcess(int intnum) {

		JSONObject ProcessCreationjson = new JSONObject();

		try {

			int sum=5;

			for(int i=0;i<sum;i++) {

				//Random process code generation- Data for running process at the back end
				String randomNumber = String.valueOf((int) (Math.random() * 99999) + 11111);

				//capturing time stamp for each respective process codes
				String timeStamp = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(new Date());

				System.out.println(randomNumber+timeStamp);

				//Store the data in the temporary JSON Object
				ProcessCreationjson.put(randomNumber, timeStamp);

				//Passing system to get different timestamp for each process
				Thread.sleep(1000);

			}
		}catch(Exception e) {
			//Exception Handling
			e.printStackTrace();

		}

		System.out.println(ProcessCreationjson);

		return ProcessCreationjson;

	}

	/*
	 *This method calls 2 functions together- Emulates the process codes and Storing in the JSON file
	 *
	 */

	public static void GenerateProcess(int intnum) {
		try {

			JsonFileUpdation(generateProcess(intnum));
			
			System.out.println();

		}catch(Exception e) {

			//Exception Handling

			System.out.println(e);

		}

	}

	/*
	 *
	 * Assumption- This Function Call is happened to Store the response in the Cache memory for every 2 seconds in real time - Assuming JSON file as cache memory
	 *
	 * This method is used to Create or Update the JSON file in the project folder with captured data
	 *
	 */

	public static JSONObject JsonFileUpdation(JSONObject obj) throws IOException {

		JSONObject returnobj;

		try {
			returnobj=obj;
			//file to identify the JSON file
			filewriter = new FileWriter(System.getProperty("user.dir")+"//src//main//resources//processCodefile.json");

			//Write the data in the JSON file
			filewriter.write(obj.toString());

			//Closing the file
			filewriter.close();

			return returnobj;

		}catch(Exception e) {

			//Exception Handling
			e.printStackTrace();
			return null;
		}

	}

	/*
	 *
	 * This method is used to compare 2 processes
	 *
	 */

	public static boolean newprocessIdentified(String oldProcess,String newProcess) {

		boolean sameprocess=false;

		if(oldProcess.contentEquals(newProcess)) {
			System.out.println("same process is running");

			sameprocess=true;

		}else {

			sameprocess=false;

		}

		return sameprocess;

	}

	/*
	 *
	 * This method is used to compare teh timestamp between 2 dates
	 *
	 */

	public static boolean timeDifferbetween2dates(String oldptrack,String  newtrack) {
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		boolean dtimestampdifference=false;

		try {
			d1 = format.parse(oldptrack);
			d2 = format.parse(newtrack);

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");

			if(diffSeconds<=60&&diffMinutes==0&&diffHours==0&&diffDays==0) {
				dtimestampdifference=true;
			}

		} catch (Exception e) {
			dtimestampdifference=false;
			e.printStackTrace();
		}

		return dtimestampdifference;

	}




}
