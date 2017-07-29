package com.sikefeng.tongxuelu.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatchUtils {

    /**
     * 手机号是否合法验证
     * 中国移动134,135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,187,188
     * 中国联通130,131,132,152,155,156,176,185,186 中国电信133,153,177,180,181,189
     * 170
     */
    public static boolean checkMobile(String mobile) {
        String regex = "^1(3[0-9]|4[7]|5[012356789]|7[0678]|8[012356789])\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        return m.find();
    }

    /**
     * 判断邮箱格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 检验是否为银行卡号
     *
     * @param bankNum
     * @return
     */
    public static boolean isBankNum(String bankNum) {
        char bit = checkCode(bankNum.substring(0, bankNum.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankNum.charAt(bankNum.length() - 1) == bit;

    }

    public static char checkCode(String noCheckCode) {
        if (noCheckCode == null || noCheckCode.trim().length() == 0
                || !noCheckCode.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = noCheckCode.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 保留小数点后几位
     * @param sum 原值
     * @param digit 位数
     * @return
     */
    public static BigDecimal getscale(double sum,int digit){
        BigDecimal bg = new BigDecimal(sum);
        BigDecimal f = bg.setScale(digit, BigDecimal.ROUND_HALF_UP);
        return f;
    }


}
