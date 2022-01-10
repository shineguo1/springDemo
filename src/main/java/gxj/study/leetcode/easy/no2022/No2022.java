package gxj.study.leetcode.easy.no2022;

import java.util.Arrays;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/6 15:24
 * @description
 */
public class No2022 {
    /**
     * 1 <= original.length <= 5 * 10^4
     * 1 <= original[i] <= 10^5
     * 1 <= m, n <= 4 * 10^4
     * <p>
     * m*n在int范围内
     */
    public int[][] construct2DArray(int[] original, int m, int n) {
        if (original.length != m * n) {
            //大小不能恰好匹配，返回二维空数组
            return new int[0][0];
        }
        int[][] ints = new int[m][];
        for (int i = 0; i < m; i++) {
            //第i行数据区间 [i*n, i*n + n) (从第0行记)
            ints[i] = Arrays.copyOfRange(original, i*n, i*n + n);
        }
        return ints;
    }
}
