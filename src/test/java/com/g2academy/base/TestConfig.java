package com.g2academy.base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.logging.Logger;

public class TestConfig {
	private static RequestSpecification httpRequest;
	private static Response response;
	private static Logger logger;

	public static RequestSpecification getHttpRequest() {
		return httpRequest;
	}

	public static void setHttpRequest(RequestSpecification httpRequest) {
		TestConfig.httpRequest = httpRequest;
	}

	public static Response getResponse() {
		return response;
	}

	public static void setResponse(Response response) {
		TestConfig.response = response;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TestConfig.logger = logger;
	}

	@BeforeTest
	public void setup() {
		logger = Logger.getLogger("QuotaAppRestAPI");
		PropertyConfigurator.configure("Log4j.properties");
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 7474;
		httpRequest = RestAssured.given();
	}

	@AfterTest
	public void teardown() {
		// do some code

	}
}
