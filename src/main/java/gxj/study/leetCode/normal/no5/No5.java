package gxj.study.leetCode.normal.no5;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/7 16:14
 * @description
 */
@Slf4j
public class No5 {

    class MaxDesc {
        int maxLength = 1;
        int maxBegin = 1;
    }

    /**
     * 初始状态：
     * P(i,i)  =true
     * P(i,i+1) = (Si == Si+1)
     * 动态转移方程：
     * P(i,j) = P(i+1,j−1)∧(Si==Sj)
     */

    public String longestPalindrome(String s) {
        char[] chars = s.toCharArray();
        //因为只需要找到最长回文串，所以只需要 1.缓存截至当前为止最长回文串就行， 2.动态转移方程有固定规律，不需要缓存所有dp答案
        //从字符串最左往右遍历，依次以(1,1),(1,2),(2,2),(2,3)为回文中心遍历。每个回文中心的转移方程递推计算与其他回文中心的dp答案无关。
        if (chars.length == 0) return "";
        if (chars.length == 1) return s;
        //对最大回文串的描述，默认为s第一个字符，长度为1
        MaxDesc desc = new MaxDesc();
        int length = chars.length;
        for (int i = 0; i < length; i++) {
            //每个坐标都能做回文中心，一共有length次，当前为第i次，用这个方法计算当前次数的回文中间位置。
            int centerIndex = getCenterIndex(length, i);
            /*
             如果截至当前最大回文串长度已经超过了当前回文中心到最左端或最右端距离的两倍(可达最长距离)，
             回文中心往两边迭代长度只会越来会小，不会超过maxLength，可以直接退出循环。
             就测试用例而言, 提前退出大约节省了一半的耗时.
            */
            if (desc.maxLength > (centerIndex + 1) * 2 || desc.maxLength > ((length - 1) - centerIndex) * 2 + 1) {
                break;
            }
            //单字符回文中心(i,i)情况
            int left, right;
            left = centerIndex - 1;
            right = centerIndex + 1;
            //往两边扩散（递归到以centerIndex为中心的最大回文串）
            loop(chars, left, right, desc);
            //双字符回文中心(i,i+1)情况
            left = centerIndex;
            right = centerIndex + 1;
            //往两边扩散（递归到以centerIndex为中心的最大回文串）
            loop(chars, left, right, desc);
        }
        return s.substring(desc.maxBegin, desc.maxBegin + desc.maxLength);
    }

    private void loop(char[] chars, int left, int right, MaxDesc desc) {
        int length = chars.length;
        while (left >= 0 && right < length && chars[left] == chars[right]) {
            //是回文
            int thisLength = right - left + 1;
            if (thisLength > desc.maxLength) {
                desc.maxLength = thisLength;
                desc.maxBegin = left;
            }
            left--;
            right++;
        }
    }

    /**
     * 从中间位置开始向两边重新编号，获取第ordinal新编号位置的坐标
     */
    private int getCenterIndex(int length, int ordinal) {
        //length奇数：正中心；偶数：中心偏左。
        int center = (length - 1) / 2;
        //ordinal偶数为中心往左，奇数为中心往右。 ...4 2 0 1 3... ;  ...2 0 1 3...;
        boolean left = ordinal % 2 == 0;
        return left ? center - ordinal / 2 : center + (ordinal + 1) / 2;
    }

    public static void main(String[] args) {
        System.out.println(new No5().longestPalindrome("abba"));
    }

}
