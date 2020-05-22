package com.g2academy.testcases.home.home;

import com.g2academy.base.RequestConfig;
import com.g2academy.utilities.SetDataToExcel;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class TC_InternetDataList extends RequestConfig {
    private int testCaseIndex;
    private String[][] result = new String[50][5];

    @DataProvider(name="paketDataList")
    Object[][] getDataFromExcel() throws IOException {
        return getDataPurchase("Paket Data List");
    }

    @BeforeClass
    public void beforeClass() {
        testCaseIndex = 1;
        result[0][0] = "description";
        result[0][1] = "phone number";
        result[0][2] = "statusCodeRequest";
        result[0][3] = "responseBodyRequest";
        result[0][4] = "status";
    }

    @Test(dataProvider = "paketDataList", timeOut = 30000)
    public void testPaketDataList(
            String description,
            String phoneNumber,
            String statusCodeRequest,
            String responseBodyRequest
    ) {
        result[testCaseIndex][0] = description;
        result[testCaseIndex][1] = phoneNumber;
        result[testCaseIndex][2] = statusCodeRequest;
        result[testCaseIndex][3] = responseBodyRequest;
        result[testCaseIndex][4] = "FAILED";

        getPaketDataList(phoneNumber);
        Assert.assertEquals(getResponse().getStatusCode(), Integer.parseInt(statusCodeRequest));

        if (statusCodeRequest.equals("200")) {
            Assert.assertTrue(getResponse().jsonPath().getString("paketData").contains(responseBodyRequest));
        }

        result[testCaseIndex][4] = "SUCCESS";
    }

    @AfterMethod
    public void afterMethod() {
        testCaseIndex++;
    }

    @AfterClass
    public void afterClass() throws IOException {
        SetDataToExcel.write(result, "Paket Data List");
    }
}
