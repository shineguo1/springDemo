package gxj.study.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏工具类
 * <p/>
 * <p>
 * </p>
 * User: 小乙  Date: 2019/08/07 ProjectName: oms
 */
public class EncryptStringUtil {

    private final static String SECRET_SIGN = "*";

    /**
     * 银行卡号脱敏
     *
     * @param bankCardNo 银行卡号
     * @return 脱敏银行卡号
     */
    public static String bankCardNo(String bankCardNo) {
        if (StringUtils.isBlank(bankCardNo)) {
            return "";
        } else if (bankCardNo.length() >= 14) {
            return encryptString(bankCardNo, 6, 4);
        } else {
            return encryptString(bankCardNo, bankCardNo.length() - 8, 4);
        }
    }

    /**
     * 邮箱脱敏
     *
     * @param email 邮箱
     * @return 脱敏邮箱
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 3)
            return email;
        else
            return StringUtils.rightPad(StringUtils.left(email, 3), index, "*")
                    .concat(StringUtils.mid(email, index, StringUtils.length(email)));
    }

    /**
     * 证件号脱敏
     *
     * @param idNo 证件号
     * @return 脱敏证件号
     */
    public static String idCardNo(String idNo) {

        return encryptString(idNo, 4, 4);
    }

    /**
     * 证件姓名脱敏
     *
     * @param idName 证件姓名
     * @return 脱敏证件姓名
     */
    public static String name(String idName) {
        if (StringUtils.isBlank(idName)) {
            return "";
        }
        return encryptStr(idName,1,0);
    }

    /**
     * 手机号码
     *
     * @param phone 银行预留手机号
     * @return 脱敏银行预留手机号
     */
    public static String phone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "";
        } else {
            return encryptString(phone, 3, 4);
        }
    }

    public static String address(String address) {
        return address;
    }

        /**
         * 信用卡有效期脱敏
         *
         * @param validDate 信用卡有效期
         * @return 脱敏信用卡有效期
         */
    public static String validDate(String validDate) {
        return encryptString(validDate, 1, 1);
    }

    /**
     * 屏蔽字符串中间字符,默认以 * 符号屏蔽
     *
     * @param str        原字符串
     * @param leftCount  保留左边位数
     * @param rightCount 保留右边位数
     * @return 屏蔽后的字符串
     */
    public static String encryptString(String str, int leftCount, int rightCount) {
        return encryptStr(str, leftCount, rightCount);
    }

    /**
     * 屏蔽字符串中间字符
     *
     * @param str        原字符串
     * @param leftCount  保留左边位数
     * @param rightCount 保留右边位数
     * @return 屏蔽后的字符串
     */
    public static String encryptStr(String str, int leftCount, int rightCount) {

        if (StringUtils.isBlank(str)) {
            return "";
        }
        StringBuilder cardNoEn = new StringBuilder();
        //字符串总长度
        int strLength = str.length();
        //请求保留左边位数 + 保留右边位数 长度
        int sumCount = leftCount + rightCount;
        if (strLength <= sumCount) {
            return str;
        }
        //取前leftCount位
        String cardNoLeft = str.substring(0, leftCount);
        //取后lightCount位
        String cardNoLight = str.substring(strLength - rightCount, strLength);
        //需要补充加密字符
        String strEn = String.format("%" + (strLength - sumCount) + "s", "").replaceAll("\\s", SECRET_SIGN);

        return cardNoEn.append(cardNoLeft).append(strEn).append(cardNoLight).toString();
    }

    /**
     * 是否需要重新加密
     *
     * @param str 加密字段
     * @return 结果
     */
    public static Boolean checkEncrypt(String str) {
        return StringUtils.isNotEmpty(str) && !str.contains(SECRET_SIGN);

    }
}
