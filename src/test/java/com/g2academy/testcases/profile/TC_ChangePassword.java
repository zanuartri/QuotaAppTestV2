package com.g2academy.testcases.profile;

import com.g2academy.base.*;
import com.g2academy.model.User;
import com.g2academy.utilities.SetDataToExcel;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class TC_ChangePassword extends RequestConfig {
    private User user = new User();
    private int testCaseIndex;
    private String[][] result = new String[100][16];

    @DataProvider(name="dataChangePassword")
    Object[][] getDataFromExcel() throws IOException {
        return getDataProfileMenu("Change Password");
    }

    @BeforeClass
    public void beforeClass() {
        testCaseIndex = 1;
        result[0][0] = "description";
        result[0][1] = "phoneNumber";
        result[0][2] = "email";
        result[0][3] = "statusCodeRequest";
        result[0][4] = "responseBodyRequest";
        result[0][5] = "verificationMethod";
        result[0][6] = "otpCode";
        result[0][7] = "statusOtpCode";
        result[0][8] = "token";
        result[0][9] = "statusCodeConfirmation";
        result[0][10] = "responseBodyConfirmation";
        result[0][11] = "newPassword";
        result[0][12] = "confirmNewPassword";
        result[0][13] = "statusCodeNewPassword";
        result[0][14] = "responseBodyNewPassword";
        result[0][15] = "status";

        user.setFullName("Zanuar Tri Romadon");
        user.setEmailAddress("testchangepasswordbackend23@gmail.com");
        user.setPhoneNumber("+6281252930366");
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

    @Test(dataProvider = "dataChangePassword", timeOut = 30000)
    public void testChangePassword(
            String description,
            String phoneNumber,
            String email,
            String statusCodeRequest,
            String responseBodyRequest,
            String verificationMethod,
            String otpCode,
            String statusOtpCode,
            String token,
            String statusCodeConfirmation,
            String responseBodyConfirmation,
            String newPassword,
            String confirmNewPassword,
            String statusCodeNewPassword,
            String responseBodyNewPassword
    ) {
        result[testCaseIndex][0] = description;
        result[testCaseIndex][1] = phoneNumber;
        result[testCaseIndex][2] = email;
        result[testCaseIndex][3] = statusCodeRequest;
        result[testCaseIndex][4] = responseBodyRequest;
        result[testCaseIndex][5] = verificationMethod;
        result[testCaseIndex][6] = otpCode;
        result[testCaseIndex][7] = statusOtpCode;
        result[testCaseIndex][8] = token;
        result[testCaseIndex][9] = statusCodeConfirmation;
        result[testCaseIndex][10] = responseBodyConfirmation;
        result[testCaseIndex][11] = newPassword;
        result[testCaseIndex][12] = confirmNewPassword;
        result[testCaseIndex][13] = statusCodeNewPassword;
        result[testCaseIndex][14] = responseBodyNewPassword;
        result[testCaseIndex][15] = "FAILED";

        user.setPhoneNumber(phoneNumber);
        user.setEmailAddress(email);
        resetPassword(user);
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeRequest));
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyRequest));

        if (verificationMethod.equals("OTP")) {
            if (otpCode.equals("TRUE")) {
                getOTPCode(user.getPhoneNumber());
                String generatedOtpCode = getResponse().jsonPath().getString("otpCode");
                Assert.assertTrue(getResponse().jsonPath().getBoolean("otpCodeStatus"));
                Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
                Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());

                sendOTPCodeForgotPassword(user.getPhoneNumber(), generatedOtpCode, statusOtpCode, newPassword, confirmNewPassword);
                Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeNewPassword));
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyNewPassword));
            } else {
                sendOTPCodeForgotPassword(user.getPhoneNumber(), otpCode, statusOtpCode, newPassword, confirmNewPassword);
                Assert.assertEquals(getResponse().jsonPath().get("status"), statusCodeNewPassword);
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyNewPassword));
            }
        } else if (verificationMethod.equals("TOKEN")) {
            if (token.equals("TRUE")) {
                getToken(user.getEmailAddress());
                String generatedToken = getResponse().jsonPath().getString("token");
                Assert.assertEquals(getResponse().jsonPath().getString("emailAddress"), user.getEmailAddress());
                Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
                Assert.assertTrue(getResponse().jsonPath().getBoolean("tokenStatus"));

                sendTokenForgotPassword(generatedToken);
                Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeConfirmation));
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyConfirmation));

                changePassword(user, newPassword, confirmNewPassword);
                Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeNewPassword));
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyNewPassword));
            } else {
                sendTokenForgotPassword(token);
                Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeConfirmation));
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyConfirmation));
            }
        }

        result[testCaseIndex][15] = "SUCCESS";
    }

    @AfterMethod
    public void afterMethod() {
        testCaseIndex++;
    }

    @AfterClass
    public void afterClass() throws IOException {
        deleteAccount("+6281252930366");
        SetDataToExcel.write(result, "Change Password");
    }
}
