package gxj.study.leetcode.medium.no8;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/18 10:23
 * @description
 */
public class No8 {
    /**
     * 逐字读取char，注意Integer.MAX_VALUE和MIN_VALUE的边界值问题。
     * 自动机
     */

    public int myAtoi(String s) {
        char[] chars = s.toCharArray();
        boolean isPre = true;
        int sign = 1;
        int value = 0;
        for (char c : chars) {
            //处理前缀
            if (isPre) {
                if (c == ' ') {
                    continue;
                } else if (c == '+') {
                    isPre = false;
                } else if (c == '-') {
                    isPre = false;
                    sign = -1;
                } else if (c >= '0' && c <= '9') {
                    isPre = false;
                    value = sign *(c - '0');
                } else {
                    return 0;
                }
            }
            //处理中间
            else {
                //非数(舍弃后缀)
                if (c < '0' || c > '9') {
                    return value;
                }
                //数
                else {
                    int cVal = c - '0';
                    if (sign == 1 && (Integer.MAX_VALUE-cVal)/10 < value){
                        return Integer.MAX_VALUE;
                    }
                    if(sign == -1 && (Integer.MIN_VALUE + cVal)/10 > value){
                        return Integer.MIN_VALUE;
                    }
                    value = value * 10 + sign * cVal;
                }
            }
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println(new No8().myAtoi("  0000000000012345678"));
    }
}
