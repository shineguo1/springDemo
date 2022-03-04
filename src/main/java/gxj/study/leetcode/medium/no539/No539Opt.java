package gxj.study.leetcode.medium.no539;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/18 9:40
 * @description
 */
public class No539Opt {

    private static final int ONE_DAY = 24 * 60;

    public int findMinDifference(List<String> timePoints) {
        //抽屉原理快速返回
        if(timePoints.size() > ONE_DAY) return 0;
        //排序
        int length = timePoints.size();
        int[] minutePoints = new int[length];
        for (int i = 0; i < timePoints.size(); i++) {
            minutePoints[i] = getMinutes(timePoints.get(i));
        }
        Arrays.sort(minutePoints);

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            min = Math.min(min, diff(minutePoints[(i + 1) % length], minutePoints[i]));
        }
        return min;
    }

    private int getMinutes(String time) {
        //使用字符计算
        return (time.charAt(0) - '0') * 600 + (time.charAt(1) - '0') * 60 + (time.charAt(3) - '0') * 10 + (time.charAt(4) - '0');
    }

    private int diff(int a, int b) {
        int diff = Math.abs(a - b);
        return diff > ONE_DAY / 2 ? ONE_DAY - diff : diff;
    }
}
