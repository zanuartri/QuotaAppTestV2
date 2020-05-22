package com.g2academy.base;

import com.g2academy.model.User;
import com.g2academy.utilities.GetDataFromExcel;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class RequestConfig extends TestConfig {
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String FORGOT_PASSWORD_PATH = "/api/auth/forgot-password";
    private static final String INTERNET_DATA_LIST_PATH = "/api/provider/cek-paket";
    private static final String INTERNET_DATA_PURCHASE_PATH  = "/api/purchase/choice";
    private static final String TRANSACTION_HISTORY_PATH = "/api/purchase/history";
    private static final String Q_WALLET_PATH = "/api/purchase/q-wallet";
    private static final String VIRTUAL_ACCOUNT_PATH = "/api/purchase/virtual-account";
    private static final String UPLOAD_INVOICE_PATH = "/api/upload-photo/invoice";
    private static final String EDIT_PERSONAL_INFORMATION_PATH = "/api/auth/edit-user";
    private static final String REQUEST_RESET_PASSWORD_PATH = "/api/auth/reset-password-in-application";
    private static final String CHANGE_PASSWORD_PATH = "/api/auth/change-password";
    private static final String LOGOUT_PATH = "/api/auth/logout";
    private static final String DELETE_ACCOUNT_PATH = "/api/auth/delete-user";
    private static final String SEND_OTP_REGISTER_PATH = "/api/auth/phone-number-verification-register";
    private static final String SEND_OTP_FORGOT_PASSWORD_PATH = "/api/auth/phone-number-verification-password";
    private static final String GET_OTP_PATH = "/api/auth/qa-get-otp";
    private static final String SEND_EMAIL_REGISTER_PATH = "/api/auth/email-verification-register";
    private static final String SEND_EMAIL_FORGOT_PASSWORD_PATH = "/api/auth/email-verification-password";
    private static final String GET_EMAIL_PATH = "/api/auth/qa-get-token";

    public void getRequest(String path) {
        setResponse(getHttpRequest().request(Method.GET, path));
    }

    public void postRequest(JSONObject requestParams, String path) {
        setResponse(getHttpRequest()
                .body(requestParams.toJSONString())
                .header("Content-Type", "application/json")
                .request(Method.POST, path));
    }

    public void putRequest(JSONObject requestParams, String path) {
        setResponse(getHttpRequest()
                .body(requestParams.toJSONString())
                .header("Content-Type", "application/json")
                .request(Method.PUT, path));
    }

    public void deleteRequest(String path) {
        setResponse(getHttpRequest().request(Method.DELETE, path));
    }

    public void uploadImageRequest(String imageName, String path) {
        File file = new File(imageName);
        setResponse(getHttpRequest()
                .header("content-type", "multipart/form-data")
                .multiPart(file)
                .when()
                .post(path));
        setHttpRequest(RestAssured.given());
    }

    public void login(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("phoneNumber", user.getPhoneNumber());
        requestParams.put("password", user.getPassword());
        postRequest(requestParams, LOGIN_PATH);
    }

    public void register(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("fullName", user.getFullName());
        requestParams.put("emailAddress", user.getEmailAddress());
        requestParams.put("phoneNumber", user.getPhoneNumber());
        requestParams.put("password", user.getPassword());
        requestParams.put("confirmPassword", user.getConfirmPassword());
        requestParams.put("pinNumber", user.getPinNumber());
        postRequest(requestParams, REGISTER_PATH);
    }

    public void forgotPassword(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("phoneNumber", user.getPhoneNumber());
        requestParams.put("emailAddress", user.getEmailAddress());
        postRequest(requestParams, FORGOT_PASSWORD_PATH);
    }

    public void getPaketDataList(String phoneNumber) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("phoneNumber", phoneNumber);
        postRequest(requestParams, INTERNET_DATA_LIST_PATH);
    }

    public void purchasePaketData(User user, String nomorPaketData, String provider, String price, String paketData) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("phoneNumber", user.getPhoneNumber());
        requestParams.put("phoneNumberForInternetData", nomorPaketData);
        requestParams.put("providerName", provider);
        requestParams.put("price", price);
        requestParams.put("internetData", paketData);
        postRequest(requestParams, INTERNET_DATA_PURCHASE_PATH);
    }

    public void getPaketDataHistory(String phoneNumber) {
        getRequest(TRANSACTION_HISTORY_PATH + "/" + phoneNumber);
    }

    public void payWithQWallet(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("phoneNumber", user.getPhoneNumber());
        requestParams.put("pinNumber", user.getPinNumber());
        postRequest(requestParams, Q_WALLET_PATH);
    }

    public void payWithVirtualAccount(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("phoneNumber", user.getPhoneNumber());
        requestParams.put("virtualAccount", user.getVirtualAccount());
        postRequest(requestParams, VIRTUAL_ACCOUNT_PATH);
    }

    public void uploadInvoice(String path) {
        uploadImageRequest(path, UPLOAD_INVOICE_PATH);
    }

    public void editUser(User user, String fullName, String phoneNumber, String emailAddress) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("fullName", fullName);
        requestParams.put("emailAddress", emailAddress);
        requestParams.put("phoneNumber", phoneNumber);
        putRequest(requestParams, EDIT_PERSONAL_INFORMATION_PATH + "/" + user.getPhoneNumber());
    }

    public void resetPassword(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("emailAddress", user.getEmailAddress());
        requestParams.put("phoneNumber", user.getPhoneNumber());
        postRequest(requestParams, REQUEST_RESET_PASSWORD_PATH);
    }

    public void changePassword(User user, String newPassword, String confirmPassword) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("emailAddress", user.getEmailAddress());
        requestParams.put("newPassword", newPassword);
        requestParams.put("confirmPassword", confirmPassword);
        putRequest(requestParams, CHANGE_PASSWORD_PATH);
    }

    public void logout(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("phoneNumber", user.getPhoneNumber());
        requestParams.put("password", user.getPassword());
        postRequest(requestParams, LOGOUT_PATH);
    }

    public void deleteAccount(String phoneNumber) {
        deleteRequest(DELETE_ACCOUNT_PATH + "/" + phoneNumber);
    }

    public void getOTPCode(String phoneNumber) {
        getRequest(GET_OTP_PATH + "/" + phoneNumber);
    }

    public void sendOTPCodeRegister(String phoneNumber, String otpCode, String statusOtpCode) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("otpCode", otpCode);
        requestParams.put("otpCodeStatus", statusOtpCode);
        postRequest(requestParams, SEND_OTP_REGISTER_PATH + "/" + phoneNumber);
    }

    public void sendOTPCodeForgotPassword(String phoneNumber, String otpCode, String statusOtpCode, String newPassword, String confirmPassword) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("otpCode", otpCode);
        requestParams.put("statusOtpCode", statusOtpCode);
        requestParams.put("newPassword", newPassword);
        requestParams.put("confirmPassword", confirmPassword);
        putRequest(requestParams, SEND_OTP_FORGOT_PASSWORD_PATH + "/" + phoneNumber);
    }

    public void getToken(String email) {
        getRequest(GET_EMAIL_PATH + "/" + email);
    }

    public void sendTokenRegister(String token) {
        getRequest(SEND_EMAIL_REGISTER_PATH + "/" + token);
    }

    public void sendTokenForgotPassword(String token) {
        getRequest(SEND_EMAIL_FORGOT_PASSWORD_PATH + "/" + token);
    }

    public Object[][] getDataLoginMenu(String sheetName) throws IOException {
        GetDataFromExcel getDataFromExcel = new GetDataFromExcel();
        return getDataFromExcel.getDataExcel("resources/DataLoginMenu.xlsx", sheetName);
    }

    public Object[][] getDataProfileMenu(String sheetName) throws IOException {
        GetDataFromExcel getDataFromExcel = new GetDataFromExcel();
        return getDataFromExcel.getDataExcel("resources/DataProfileMenu.xlsx", sheetName);
    }

    public Object[][] getDataPurchase(String sheetName) throws IOException {
        GetDataFromExcel getDataFromExcel = new GetDataFromExcel();
        return getDataFromExcel.getDataExcel("resources/DataPurchase.xlsx", sheetName);
    }
}
