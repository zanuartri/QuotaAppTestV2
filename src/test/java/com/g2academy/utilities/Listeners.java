package com.g2academy.utilities;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Listeners extends TestListenerAdapter {
	private ExtentHtmlReporter htmlReporter;
	private ExtentReports extent;
	private ExtentTest test;
	
	public void onStart(ITestContext textContext) {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/QuotaAppReport.html");
		htmlReporter.config().setDocumentTitle("Automation Report");
		htmlReporter.config().setReportName("QuotaApp API Testing Report");
		htmlReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host name", "localhost");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("user", "zanuar");
	}
	
	public void onTestSuccess(ITestResult result) {
		test = extent.createTest(result.getName());
		test.log(Status.PASS, "Test Case PASSED : " + result.getName());
	}
	
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getName());
		test.log(Status.FAIL, "Test Case FAILED : " + result.getName());
		test.log(Status.FAIL, "Test Case FAILED : " + result.getThrowable());
	}
	
	public void onFinish(ITestContext testContext) {
		extent.flush();
	}
}
