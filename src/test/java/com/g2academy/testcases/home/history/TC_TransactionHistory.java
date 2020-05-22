package com.g2academy.testcases.home.history;

import com.g2academy.base.RequestConfig;
import com.g2academy.model.User;
import com.g2academy.utilities.SetDataToExcel;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class TC_TransactionHistory extends RequestConfig {
    private User user = new User();
    private int testCaseIndex;
    private String[][] result = new String[50][5];

    @DataProvider(name="paketDataHistory")
    Object[][] getDataFromExcel() throws IOException {
        return getDataPurchase("Paket Data History");
    }

    @BeforeClass
    public void beforeClass() {
        testCaseIndex = 1;
        result[0][0] = "description";
        result[0][1] = "phone number";
        result[0][2] = "statusCodeRequest";
        result[0][3] = "responseBodyRequest";
        result[0][4] = "status";

        user.setFullName("Zanuar Tri Romadon");
        user.setEmailAddress("testhistorybackend25@gmail.com");
        user.setPhoneNumber("+6281252930353");
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

        purchasePaketData(user, user.getPhoneNumber(), "Simpati", "27000", "Paket-Internet-1GB");
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), 200);
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains("successfully"));
        Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
        Assert.assertEquals(getResponse().jsonPath().getString("phoneNumberForInternetData"), user.getPhoneNumber());
        Assert.assertEquals(getResponse().jsonPath().getString("providerName"), "Simpati");
        Assert.assertEquals(getResponse().jsonPath().getString("internetData"), "Paket-Internet-1GB");
        Assert.assertEquals(getResponse().jsonPath().getString("price"), "27000");

        payWithQWallet(user);
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), 200);
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains("Transaction successfully"));
        Assert.assertEquals(getResponse().jsonPath().getString("namaUser"), user.getFullName());
        Assert.assertEquals(getResponse().jsonPath().getString("nomorTeleponUser"), user.getPhoneNumber());
        Assert.assertEquals(getResponse().jsonPath().getString("nomorPaketData"), user.getPhoneNumber());
        Assert.assertEquals(getResponse().jsonPath().getString("namaProvider"), "Simpati");
        Assert.assertEquals(getResponse().jsonPath().getString("paketData"), "Paket-Internet-1GB");
        Assert.assertEquals(getResponse().jsonPath().getString("harga"), "27000");
        Assert.assertEquals(getResponse().jsonPath().getString("pembayaranMelalui"), "E-Walet");
        Assert.assertTrue(getResponse().jsonPath().getBoolean("statusPembayaran"));

    }

    @Test(dataProvider = "paketDataHistory", timeOut = 30000)
    public void testPaketDataHistory(
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

        getPaketDataHistory(phoneNumber);
        Assert.assertEquals(getResponse().getStatusCode(), Integer.parseInt(statusCodeRequest));

        if (statusCodeRequest.equals("200")) {
            Assert.assertTrue(getResponse().jsonPath().getString("history").contains(user.getFullName()));
            Assert.assertTrue(getResponse().jsonPath().getString("history").contains(user.getPhoneNumber()));
            Assert.assertTrue(getResponse().jsonPath().getString("history").contains("Simpati"));
            Assert.assertTrue(getResponse().jsonPath().getString("history").contains("Paket-Internet-1GB"));
            Assert.assertTrue(getResponse().jsonPath().getString("history").contains("27000"));
            Assert.assertTrue(getResponse().jsonPath().getString("history").contains("E-Walet"));
            Assert.assertTrue(getResponse().jsonPath().getString("history").contains(responseBodyRequest));
        } else {
            Assert.assertEquals(getResponse().jsonPath().getString("message"), responseBodyRequest);
        }

        result[testCaseIndex][4] = "SUCCESS";
    }

    @AfterMethod
    public void afterMethod() {
        testCaseIndex++;
    }

    @AfterClass
    public void afterClass() throws IOException {
        deleteAccount("+6281252930353");
        SetDataToExcel.write(result, "Paket Data History");
    }
}
