package com.g2academy.testcases.home.home;

import com.g2academy.base.RequestConfig;
import com.g2academy.model.User;
import com.g2academy.utilities.SetDataToExcel;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class TC_InternetDataPurchase extends RequestConfig {
    private User user = new User();
    private int testCaseIndex;
    private String[][] result = new String[200][18];

    @DataProvider(name="paketDataPurchase")
    Object[][] getDataFromExcel() throws IOException {
        return getDataPurchase("Paket Data Purchase");
    }

    @BeforeClass
    public void beforeClass() {
        testCaseIndex = 1;
        result[0][0] = "description";
        result[0][1] = "phoneNumber";
        result[0][2] = "phoneNumberForPaketData";
        result[0][3] = "provider";
        result[0][4] = "price";
        result[0][5] = "paketData";
        result[0][6] = "statusCodeRequest";
        result[0][7] = "responseBodyRequest";
        result[0][8] = "paymentMethod";
        result[0][9] = "phoneNumberForPayment";
        result[0][10] = "pinTransaction";
        result[0][11] = "virtualAccount";
        result[0][12] = "statusCodeConfirmation";
        result[0][13] = "responseBodyConfirmation";
        result[0][14] = "imageName";
        result[0][15] = "statusCodeInvoice";
        result[0][16] = "responseBodyInvoice";
        result[0][17] = "status";

        user.setFullName("Zanuar Tri Romadon");
        user.setEmailAddress("testpurchasebackend23@gmail.com");
        user.setPhoneNumber("+6281252930365");
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

    @Test(dataProvider = "paketDataPurchase", timeOut = 30000)
    public void testPaketDataPurchase(
            String description,
            String phoneNumber,
            String phoneNumberForPaketData,
            String provider,
            String price,
            String paketData,
            String statusCodeRequest,
            String responseBodyRequest,
            String paymentMethod,
            String phoneNumberForPayment,
            String pinTransaction,
            String virtualAccount,
            String statusCodeConfirmation,
            String responseBodyConfirmation,
            String imageName,
            String statusCodeInvoice,
            String responseBodyInvoice
    ) {
        result[testCaseIndex][0] = description;
        result[testCaseIndex][1] = phoneNumber;
        result[testCaseIndex][2] = phoneNumberForPaketData;
        result[testCaseIndex][3] = provider;
        result[testCaseIndex][4] = price;
        result[testCaseIndex][5] = paketData;
        result[testCaseIndex][6] = statusCodeRequest;
        result[testCaseIndex][7] = responseBodyRequest;
        result[testCaseIndex][8] = paymentMethod;
        result[testCaseIndex][9] = phoneNumberForPayment;
        result[testCaseIndex][10] = pinTransaction;
        result[testCaseIndex][11] = virtualAccount;
        result[testCaseIndex][12] = statusCodeConfirmation;
        result[testCaseIndex][13] = responseBodyConfirmation;
        result[testCaseIndex][14] = imageName;
        result[testCaseIndex][15] = statusCodeInvoice;
        result[testCaseIndex][16] = responseBodyInvoice;
        result[testCaseIndex][17] = "FAILED";

        user.setPhoneNumber(phoneNumber);
        purchasePaketData(user, phoneNumberForPaketData, provider, price, paketData);
        Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeRequest));
        Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyRequest));

        if (statusCodeRequest.equals("200")) {
            Assert.assertEquals(getResponse().jsonPath().getString("phoneNumberForInternetData"), phoneNumberForPaketData);
            Assert.assertEquals(getResponse().jsonPath().getString("phoneNumber"), user.getPhoneNumber());
            Assert.assertEquals(getResponse().jsonPath().getString("providerName"), provider);
            Assert.assertEquals(getResponse().jsonPath().getString("internetData"), paketData);
            Assert.assertEquals(getResponse().jsonPath().getString("price"), price);
        }

        if (paymentMethod.equals("QWALLET")) {
            user.setPhoneNumber(phoneNumberForPayment);
            user.setPinNumber(pinTransaction);
            payWithQWallet(user);
            Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeConfirmation));
            Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyConfirmation));

            if (statusCodeConfirmation.equals("200")) {
                Assert.assertEquals(getResponse().jsonPath().getString("namaUser"), user.getFullName());
                Assert.assertEquals(getResponse().jsonPath().getString("nomorTeleponUser"), user.getPhoneNumber());
                Assert.assertEquals(getResponse().jsonPath().getString("nomorPaketData"), phoneNumberForPaketData);
                Assert.assertEquals(getResponse().jsonPath().getString("namaProvider"), provider);
                Assert.assertEquals(getResponse().jsonPath().getString("paketData"), paketData);
                Assert.assertEquals(getResponse().jsonPath().getString("harga"), price);
                Assert.assertEquals(getResponse().jsonPath().getString("pembayaranMelalui"), "E-Walet");
                Assert.assertTrue(getResponse().jsonPath().getBoolean("statusPembayaran"));
            }
        } else if (paymentMethod.equals("VA")) {
            user.setPhoneNumber(phoneNumberForPayment);
            user.setVirtualAccount(virtualAccount);
            payWithVirtualAccount(user);
            Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeConfirmation));
            Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyConfirmation));

            if (!imageName.equals("-")) {
                uploadInvoice("resources/image/DANA.jpg");
                Assert.assertEquals(getResponse().jsonPath().getInt("status"), Integer.parseInt(statusCodeInvoice));
                Assert.assertTrue(getResponse().jsonPath().getString("message").contains(responseBodyInvoice));
            }
        }

        result[testCaseIndex][17] = "SUCCESS";
    }

    @AfterMethod
    public void afterMethod() {
        testCaseIndex++;
    }

    @AfterClass
    public void afterClass() throws IOException {
        deleteAccount("+6281252930365");
        SetDataToExcel.write(result, "Paket Data Purchase");
    }
}
