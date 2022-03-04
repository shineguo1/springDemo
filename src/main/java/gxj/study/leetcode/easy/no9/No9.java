package gxj.study.leetcode.easy.no9;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/12 11:09
 * @description
 */
public class No9 {
    /**
     * 判断回文数，至少一次遍历；双指针算法只需一次遍历；所以双指针算法已满足最优复杂度。
     * 数x转换成字符串，从两端开始往中间遍历，检查每一对字符是否相同，快速返回。
     */
    public boolean isPalindrome(int x) {
        String num = String.valueOf(x);
        int left = 0;
        int right = num.length() - 1;
        while (left < right) {
            if (num.charAt(left) != num.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
