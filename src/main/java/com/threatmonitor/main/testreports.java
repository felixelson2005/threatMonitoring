package com.threatmonitor.main;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;

public class testreports {
	
	public ExtentReports reportgenerator() {
		ExtentReports  extent = new ExtentReports (System.getProperty("user.dir") +"//TestOuputFiles//STMExtentReport.html", true);
		
		extent
        .addSystemInfo("Host Name", "Threat Monitor -test Driven software")
        .addSystemInfo("Environment", "Local")
        .addSystemInfo("User Name", "admin");
		
		extent.loadConfig(new File(System.getProperty("user.dir")+"//TestOuputFiles//extent-config.xml"));
		
		return extent;

	}

}
