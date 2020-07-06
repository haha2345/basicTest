package com.example.basictest.constant;

public class netConstant {
    private static String URL="http://172.16.10.11:8080/notarize/";

    private static String getVcodeURL=URL+"getSMSCode";
    private static String loginURL=URL+"mobile/login";
    private static String registerURL=URL+"mobile/register";
    private static String resetPwdURL=URL+"mobile/resetPwd";

    public static String getGetVcodeURL() {
        return getVcodeURL;
    }

    public static String getLoginURL() {
        return loginURL;
    }

    public static String getRegisterURL() {
        return registerURL;
    }

    public static String getResetPwdURL() {
        return resetPwdURL;
    }
}
