package gxj.study.leetcode.medium.no306;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/10 9:36
 * @description
 */
public class No306 {

    public boolean isAdditiveNumber(String num) {
        int length = num.length();
        //前2个加数确定，整个累加数字符串也能确定。穷举前两个加数的情况，回溯。
        //第一个加数不能超过字符串长度一半，不然必然大于和数。第二个加数坐标不能超过字符串长度三分之二，不然也必然大于和数。
        for (int i = 1; i <= length / 2; i++) {
            //第一个加数不会以0开头,除了0本身
            if (num.charAt(0) == '0' && i > 1) continue;
            for (int ii = i + 1; ii <= (length / 3 + 1) * 2 && ii <= length; ii++) {
                //第二个加数不会以0开头,除了0本身
                if (num.charAt(i) == '0' && ii - i > 1) continue;
                if (isAdditiveNumber(num, i, ii)) {
                    return true;
                }
            }
        }
        //尝试所有可能情况后仍未成功，无解
        return false;
    }

    private boolean isAdditiveNumber(String num, int firstEndIndex, int secondEndIndex) {
        int length = num.length();
        //和数数组越界（第二个加数占用到num的最后一个字符）
        boolean outOfIndex = secondEndIndex >= length;
        if (outOfIndex) return false;

        //数据规模 0 < num.length < 35, 35/3+1 = 12, 所以数最大长度12可以用long表示。
        long a = Long.valueOf(num.substring(0, firstEndIndex));
        long b = Long.valueOf(num.substring(firstEndIndex, secondEndIndex));
        for (int thirdEndIndex = secondEndIndex + 1; thirdEndIndex <= length; thirdEndIndex++) {
            //和数不会以0开头,除了0本身
            boolean beginWithZero = num.charAt(secondEndIndex) == '0' && thirdEndIndex - secondEndIndex > 1;
            if (beginWithZero) return false;

            long c = Long.valueOf(num.substring(secondEndIndex, thirdEndIndex));
            if (a + b == c) {
                //找到和数，不用继续循环了。
                if (thirdEndIndex >= length) {
                    //字符串遍历完了，整个字符串通过累加数验证
                    return true;
                } else {
                    //字符串未遍历完，快速返回递归结果
                    //新加数是原第二个加数，新字符串截掉了原第一个加数
                    int newFirstEndIndex = secondEndIndex - firstEndIndex;
                    //新加数是原和数，新字符串截掉了原第一个加数
                    int newSecondEndIndex = thirdEndIndex - firstEndIndex;
                    return isAdditiveNumber(num.substring(firstEndIndex), newFirstEndIndex, newSecondEndIndex);
                }
            } else if (a + b < c) {
                //不满足累加数。和数已经大于加数了，继续循环没有意义。快速返回false。
                return false;
            } else {
                //不满足累加数。和数太小，继续循环使和数多加一位。回溯。
                continue;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(new No306().isAdditiveNumber("112358"));
    }
}
