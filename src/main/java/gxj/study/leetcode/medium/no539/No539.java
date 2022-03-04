package gxj.study.leetcode.medium.no539;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/18 9:40
 * @description
 */
public class No539 {

    private static final int ONE_DAY = 24 * 60;

    public int findMinDifference(List<String> timePoints) {
        Integer[] minutePoints = timePoints.stream().map(this::getMinutes).toArray(Integer[]::new);
        Arrays.sort(minutePoints);
        int length = minutePoints.length;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            min = Math.min(min, diff(minutePoints[(i + 1) % length], minutePoints[i]));
        }
        return min;
    }

    private int getMinutes(String time) {
        String[] split = time.split(":");
        return Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
    }

    private int diff(int a, int b) {
        int diff = Math.abs(a - b);
        return diff > ONE_DAY / 2 ? ONE_DAY - diff : diff;
    }
}
