package com.g2academy.testcases.profile;

import com.g2academy.base.RequestConfig;
import com.g2academy.model.User;
import com.g2academy.utilities.SetDataToExcel;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class TC_EditUser extends RequestConfig {
    private User user = new User();
    private int testCaseIndex;
    private String[][] result = new String[100][16];

    @DataProvider(name="dataEditUser")
    Object[][] getDataFromExcel() throws IOException {
        return getDataProfileMenu("Edit User");
    }

    @BeforeClass
    public void beforeClass() {
        testCaseIndex = 1;
        result[0][0] = "description";
        result[0][1] = "fullName";
        result[0][2] = "phoneNumber";
        result[0][3] = "email";
        result[0][4] = "statusCodeRequest";
        result[0][5] = "responseBodyRequest";
        result[0][6] = "status";
    }

    @BeforeMethod
    public void beforeMethod() {
        user.setFullName("Zanuar Tri Romadon");
        user.setEmailAddress("testedituserbackend23@gmail.com");
        user.setPhoneNumber("+6281252930368");
        user.setPassword("Zanuar30@@");
        user.setConfirmPassword("Zanuar30@@");
        user.setPinNumber("123456");
        register(user);
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), 200);
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains("Please check your sms or email notification"));

        getOTPCode(user.getPhoneNumber());
        String generatedOtpCode = getResponse().jsonPath().getString("otpCode");
        Assert.assertTrue(getResponse().jsonPath().getBoolean("otpCodeStatus"));
        Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
        Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());

        sendOTPCodeRegister(user.getPhoneNumber(), generatedOtpCode, "true");
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), 200);
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains("Registration is success"));
        Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
        Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
        Assert.assertEquals(getResponse().jsonPath().getString("pinNumber"), user.getPinNumber());
        Assert.assertEquals(getResponse().jsonPath().getInt("saldoBalance"), 1000000);

        login(user);
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), 200);
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains("Login is success"));
        Assert.assertNotNull(getResponse().jsonPath().getString("token"));
        Assert.assertEquals(getResponse().jsonPath().getString("type"), "Bearer");
        Assert.assertEquals(getResponse().jsonPath().getString("fullName"), user.getFullName());
        Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
        Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
        Assert.assertEquals(getResponse().jsonPath().getInt("saldoBalance"), 1000000L);
    }

    @Test(dataProvider = "dataEditUser", timeOut = 30000)
    public void testEditUser(
            String description,
            String fullName,
            String phoneNumber,
            String email,
            String statusCodeRequest,
            String responseBodyRequest
    ) {
        result[testCaseIndex][0] = description;
        result[testCaseIndex][1] = fullName;
        result[testCaseIndex][2] = phoneNumber;
        result[testCaseIndex][3] = email;
        result[testCaseIndex][4] = statusCodeRequest;
        result[testCaseIndex][5] = responseBodyRequest;
        result[testCaseIndex][6] = "FAILED";

        editUser(user, fullName, phoneNumber, email);
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeRequest));
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyRequest));

        if (statusCodeRequest.equals("200")) {
            Assert.assertEquals(getResponse().jsonPath().getString("fullName"), fullName);
            Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), email);
            Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), phoneNumber);
        }

        deleteAccount(phoneNumber);
        result[testCaseIndex][6] = "SUCCESS";
    }

    @AfterMethod
    public void afterMethod() {
        testCaseIndex++;
        deleteAccount("+6281252930368");
    }

    @AfterClass
    public void afterClass() throws IOException {
        SetDataToExcel.write(result, "Edit User");
    }
}
