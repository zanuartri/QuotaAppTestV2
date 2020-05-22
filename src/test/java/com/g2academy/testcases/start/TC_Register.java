package com.g2academy.testcases.start;

import com.g2academy.base.RequestConfig;
import com.g2academy.model.User;
import com.g2academy.utilities.SetDataToExcel;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class TC_Register extends RequestConfig {
    private User user = new User();
    private int testCaseIndex;
    private String[][] result = new String[200][16];

    @DataProvider(name="dataRegister")
    Object[][] getDataFromExcel() throws IOException {
        return getDataLoginMenu("Register");
    }

    @BeforeClass
    public void beforeClass() {
        testCaseIndex = 1;
        result[0][0] = "description";
        result[0][1] = "pinTransaction";
        result[0][2] = "fullName";
        result[0][3] = "email";
        result[0][4] = "phoneNumber";
        result[0][5] = "password";
        result[0][6] = "confirmPassword";
        result[0][7] = "statusCodeRequest";
        result[0][8] = "responseBodyRequest";
        result[0][9] = "verificationMethod";
        result[0][10] = "otpCode";
        result[0][11] = "statusOtpCode";
        result[0][12] = "token";
        result[0][13] = "statusCodeConfirmation";
        result[0][14] = "responseBodyConfirmation";
        result[0][15] = "status";
    }

    @Test(dataProvider = "dataRegister", timeOut = 30000)
    public void testRegister(
            String description,
            String pinTransaction,
            String fullName,
            String email,
            String phoneNumber,
            String password,
            String confirmPassword,
            String statusCodeRequest,
            String responseBodyRequest,
            String verificationMethod,
            String otpCode,
            String statusOtpCode,
            String token,
            String statusCodeConfirmation,
            String responseBodyConfirmation
    ) {
        result[testCaseIndex][0] = description;
        result[testCaseIndex][1] = pinTransaction;
        result[testCaseIndex][2] = fullName;
        result[testCaseIndex][3] = email;
        result[testCaseIndex][4] = phoneNumber;
        result[testCaseIndex][5] = password;
        result[testCaseIndex][6] = confirmPassword;
        result[testCaseIndex][7] = statusCodeRequest;
        result[testCaseIndex][8] = responseBodyRequest;
        result[testCaseIndex][9] = verificationMethod;
        result[testCaseIndex][10] = otpCode;
        result[testCaseIndex][11] = statusOtpCode;
        result[testCaseIndex][12] = token;
        result[testCaseIndex][13] = statusCodeConfirmation;
        result[testCaseIndex][14] = responseBodyConfirmation;
        result[testCaseIndex][15] = "FAILED";

        user.setFullName(fullName);
        user.setEmailAddress(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        user.setPinNumber(pinTransaction);
        register(user);
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeRequest));
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyRequest));

        if (verificationMethod.equals("OTP")) {
            if (otpCode.equals("TRUE")) {
                getOTPCode(user.getPhoneNumber());
                String generatedOtpCode = getResponse().jsonPath().getString("otpCode");
                Assert.assertTrue(getResponse().jsonPath().getBoolean("otpCodeStatus"));
                Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
                Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());

                sendOTPCodeRegister(user.getPhoneNumber(), generatedOtpCode, statusOtpCode);
                Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeConfirmation));
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyConfirmation));

                if (statusOtpCode.equals("true")) {
                    Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
                    Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
                    Assert.assertEquals(getResponse().jsonPath().getString("pinNumber"), user.getPinNumber());
                    Assert.assertEquals(getResponse().jsonPath().getInt("saldoBalance"), 1000000);
                }
            } else {
                sendOTPCodeRegister(user.getPhoneNumber(), otpCode, statusOtpCode);
                Assert.assertEquals(getResponse().jsonPath().get("status"), statusCodeConfirmation);
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyConfirmation));
            }
        } else if (verificationMethod.equals("TOKEN")) {
            if (token.equals("TRUE")) {
                getToken(user.getEmailAddress());
                String generatedToken = getResponse().jsonPath().getString("token");
                Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
                Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
                Assert.assertTrue(getResponse().jsonPath().getBoolean("tokenStatus"));

                sendTokenRegister(generatedToken);
                Assert.assertEquals(getResponse().jsonPath().get("status"), statusCodeConfirmation);
                Assert.assertEquals(getResponse().jsonPath().getString("message"), responseBodyConfirmation);
                Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
                Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
                Assert.assertEquals(getResponse().jsonPath().getString("pinNumber"), user.getPinNumber());
                Assert.assertEquals(getResponse().jsonPath().getInt("saldoBalance"), 1000000);
            } else {
                sendTokenRegister(token);
                Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeConfirmation));
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyConfirmation));
            }
        }

        result[testCaseIndex][15] = "SUCCESS";
    }

    @AfterMethod
    public void afterMethod() {
        deleteAccount(user.getPhoneNumber());
        System.out.println(getResponse().getBody().asString());
        testCaseIndex++;
    }

    @AfterClass
    public void afterClass() throws IOException {
        SetDataToExcel.write(result, "Register");
    }
}