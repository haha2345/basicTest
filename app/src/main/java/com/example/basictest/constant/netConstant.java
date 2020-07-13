package com.example.basictest.constant;

public class netConstant {
    private static String URL="http://172.16.10.11:8080/notarize/";

    private static String getVcodeURL=URL+"getSMSCode";
    private static String loginURL=URL+"mobile/login";
    private static String registerURL=URL+"mobile/register";
    private static String resetPwdURL=URL+"mobile/resetPwd";
    private static String noticeURL=URL+"system/notice/list";
    private static String bankURL=URL+"fqgz/coList/partList";

    public static String getBankURL() {
        return bankURL;
    }

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

    public static String getNoticeURL() {
        return noticeURL;
    }
}
