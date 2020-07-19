package com.example.basictest.constant;

public class netConstant {
    private static String URL="http://i305k85088.qicp.vip:36422/notarize/";

    private static String getVcodeURL=URL+"getSMSCode";
    private static String loginURL=URL+"mobile/login";
    private static String registerURL=URL+"mobile/register";
    private static String resetPwdURL=URL+"mobile/resetPwd";
    private static String noticeURL=URL+"system/notice/list";
    private static String bankURL=URL+"fqgz/coList/partList";
    private static String applyURL=URL+"fqgz/ca/apply";
    private static String uploadEContractURL=URL+"fqgz/userFile/uploadEContract";
    private static String checkSMSCodeURL=URL+"checkSMSCode";
    private static String model0x12URL=URL+"fqgz/ca/mode0x12";
    private static String personalListURL=URL+"fqgz/caseList/personalList";




    public static String getPersonalListURL() {
        return personalListURL;
    }
    public static String getCheckSMSCodeURL() {
        return checkSMSCodeURL;
    }
    public static String getModel0x12URL() {
        return model0x12URL;
    }
    public static String getApplyURL() {
        return applyURL;
    }
    public static String getUploadEContractURL() {
        return uploadEContractURL;
    }
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
