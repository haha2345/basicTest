package com.example.basictest.utils;

import java.util.HashMap;
import java.util.Map;

public class ValueConvertUtil {
    /**
     * 将大写金额转换为字符串形式的阿拉伯数字。
     * 支持小于一万亿的数字，可精确到小数点后两位
     * 如果大写金额的格式错误，本函数不保证能范围正确的结果（废话..）
     */
    public static String formatAmount(String cnTraditionalNum) {
        //规范错误字符
        cnTraditionalNum = cnTraditionalNum
                .replaceAll("億", "亿")
                .replaceAll("萬", "万")
                .replaceAll("千", "仟")
                .replaceAll("任", "仟")
                .replaceAll("阡", "仟")
                .replaceAll("百", "佰")
                .replaceAll("陌", "佰")
                .replaceAll("元", "圆")
                .replaceAll("圓", "圆")
        //.replaceAll("廿", "贰拾")
        //.replaceAll("念", "贰拾")
        //.replaceAll("卅", "叁拾")
        //.replaceAll("卌", "肆拾")
        //.replaceAll("圩", "伍拾")
        //.replaceAll("圆", "陆拾")
        //.replaceAll("进", "柒拾")
        //.replaceAll("枯", "捌拾")
        //.replaceAll("枠", "玖拾")
        ;

        long result = 0;

        if (cnTraditionalNum.contains("亿")) {
            String yi = cnTraditionalNum.substring(0, cnTraditionalNum.indexOf("亿"));
            result += cnThousandNumberToArab(yi) * 100000000L;
            cnTraditionalNum = cnTraditionalNum.substring(cnTraditionalNum.indexOf("亿"));
        }

        if (cnTraditionalNum.contains("万")) {
            String wan = cnTraditionalNum.substring(0, cnTraditionalNum.indexOf("万"));
            result += cnThousandNumberToArab(wan) * 10000L;
            cnTraditionalNum = cnTraditionalNum.substring(cnTraditionalNum.indexOf("万"));
        }


        if (cnTraditionalNum.contains("圆")) {
            String wan = cnTraditionalNum.substring(0, cnTraditionalNum.indexOf("圆"));
            result += cnThousandNumberToArab(wan);
            cnTraditionalNum = cnTraditionalNum.substring(cnTraditionalNum.indexOf("圆"));
        }

        //小数点后
        String jiao = "0";
        String fen = "0";
        String resultStr = String.valueOf(result);
        if (cnTraditionalNum.contains("角")) {
            jiao = cnNumToArabicNumMap.get(cnTraditionalNum.substring(cnTraditionalNum.indexOf("角") - 1, cnTraditionalNum.indexOf("角"))).toString();
        }
        if (cnTraditionalNum.contains("分")) {
            fen = cnNumToArabicNumMap.get(cnTraditionalNum.substring(cnTraditionalNum.indexOf("分") - 1, cnTraditionalNum.indexOf("分"))).toString();
        }

        //resultStr += "." + jiao + fen;
        return resultStr;
    }

    private static Map<String, Integer> cnNumToArabicNumMap;

    static {
        cnNumToArabicNumMap = new HashMap<>();
        cnNumToArabicNumMap.put("零", 0);
        cnNumToArabicNumMap.put("壹", 1);
        cnNumToArabicNumMap.put("喜", 1);
        cnNumToArabicNumMap.put("登", 1);
        cnNumToArabicNumMap.put("豆", 1);
        cnNumToArabicNumMap.put("贰", 2);
        cnNumToArabicNumMap.put("貳", 2);
        cnNumToArabicNumMap.put("两", 2);
        cnNumToArabicNumMap.put("叁", 3);
        cnNumToArabicNumMap.put("参", 3);
        cnNumToArabicNumMap.put("兰", 3);
        cnNumToArabicNumMap.put("三", 3);
        cnNumToArabicNumMap.put("肆", 4);
        cnNumToArabicNumMap.put("伍", 5);
        cnNumToArabicNumMap.put("陆", 6);
        cnNumToArabicNumMap.put("陸", 6);
        cnNumToArabicNumMap.put("阿", 6);
        cnNumToArabicNumMap.put("陕", 6);
        cnNumToArabicNumMap.put("柒", 7);
        cnNumToArabicNumMap.put("洪", 7);
        cnNumToArabicNumMap.put("捌", 8);
        cnNumToArabicNumMap.put("器", 8);
        cnNumToArabicNumMap.put("玖", 9);
    }

    /**
     * 将大写数字转换为阿拉伯数字（数字小于一万）
     */
    private static long cnThousandNumberToArab(String number) {
        int result = 0;
        
        if (number.indexOf("拾") <= 0 
        || !cnNumToArabicNumMap.containsKey(String.valueOf(number.charAt(number.indexOf("拾") - 1)))) {
            number = number.replace("拾", "壹拾");
        }
        if (number.contains("仟")) {
            result += getArabicValue(number.charAt(number.indexOf("仟") - 1)) * 1000;
        }
        if (number.contains("佰")) {
            result += getArabicValue(number.charAt(number.indexOf("佰") - 1)) * 100;
        }
        if (number.contains("拾")) {
            result += getArabicValue(number.charAt(number.indexOf("拾") - 1)) * 10;
        }
        if (cnNumToArabicNumMap.containsKey(number.substring(number.length() - 1))) {
            result += cnNumToArabicNumMap.get(number.substring(number.length() - 1));
        }
        return result;
    }

    private static int getArabicValue(Character num) {
        Integer result = cnNumToArabicNumMap.get(num.toString());
        if (result == null)
            throw new IllegalArgumentException("不支持将该中文字符[" + num + "]转换为阿拉伯数字！");
        return result;
    }

}