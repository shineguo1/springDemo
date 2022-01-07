package gxj.study.leetCode.hard.no913;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/4 13:55
 * @description
 */
@Slf4j
public class No913Official {
    static final int MOUSE_WIN = 1;
    static final int CAT_WIN = 2;
    static final int DRAW = 0;
    int n;
    int[][] graph;
    int[][][] dp;

    int nextcount;

    public int catMouseGame(int[][] graph) {
        this.nextcount = 0;
        this.n = graph.length;
        this.graph = graph;
        this.dp = new int[n][n][n * 2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        return getResult(1, 2, 0);
    }

    public int getResult(int mouse, int cat, int turns) {
        if (turns == n * 2) {
            return DRAW;
        }
        if (dp[mouse][cat][turns] < 0) {
            if (mouse == 0) {
                dp[mouse][cat][turns] = MOUSE_WIN;
            } else if (cat == mouse) {
                dp[mouse][cat][turns] = CAT_WIN;
            } else {
                dp[mouse][cat][turns] = getNextResult(mouse, cat, turns);
            }
        }
        return dp[mouse][cat][turns];
    }

    public int getNextResult(int mouse, int cat, int turns) {
        nextcount++;

        boolean isMouseMove = turns % 2 == 0;
        boolean isCatMove = !isMouseMove;

        int curMove = isMouseMove ? mouse : cat;
        int defaultResult = isMouseMove ? CAT_WIN : MOUSE_WIN;
        int result = defaultResult;
        int[] nextNodes = graph[curMove];
        for (int next : nextNodes) {
            if (isCatMove && next == 0) {
                continue;
            }
            int nextMouse = isMouseMove ? next : mouse;
            int nextCat = isCatMove ? next : cat;
            int nextResult = getResult(nextMouse, nextCat, turns + 1);
            if (nextResult != defaultResult) {
                result = nextResult;
                if (result != DRAW) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 用于查找胜利路径
     */
    private void debug(int expectedResult) {
        System.out.println(dp[1][2][0]);
        List<String> list = Lists.newArrayList();
        for (int mouse = 0; mouse < dp.length; mouse++) {
            for (int cat = 0; cat < dp[mouse].length; cat++) {
                for (int turn = 0; turn < dp[mouse][cat].length; turn++) {
                    if (dp[mouse][cat][turn] == expectedResult) {
                        list.add(String.format(" turn:%d#, cat:%d, mouse:%d", turn, cat, mouse));
                    }
                }
            }
        }
        list.sort(String::compareTo);
        list.forEach(System.out::println);
    }

    public static void main(String[] args) {
        int[][] graph = new int[][]{
                {3, 4, 6, 7, 9, 15, 16, 18},
                {4, 5, 8, 19},
                {3, 4, 6, 9, 17, 18},
                {0, 2, 11, 15},
                {0, 1, 10, 6, 2, 12, 14, 16},
                {1, 10, 7, 9, 15, 17, 18},
                {0, 10, 4, 7, 9, 2, 11, 12, 13, 14, 15, 17, 19},
                {0, 10, 5, 6, 9, 16, 17},
                {1, 9, 14, 15, 16, 19},
                {0, 10, 5, 6, 7, 8, 2, 11, 13, 15, 16, 17, 18},
                {4, 5, 6, 7, 9, 18},
                {3, 6, 9, 12, 19},
                {4, 6, 11, 15, 17, 19},
                {6, 9, 15, 17, 18, 19},
                {4, 6, 8, 15, 19},
                {0, 3, 5, 6, 8, 9, 12, 13, 14, 16, 19},
                {0, 4, 7, 8, 9, 15, 17, 18, 19},
                {5, 6, 7, 9, 2, 12, 13, 16},
                {0, 10, 5, 9, 2, 13, 16},
                {1, 6, 8, 11, 12, 13, 14, 15, 16}
        };
        System.out.println(JSON.toJSONString(graph));
        No913Official no913 = new No913Official();
        System.out.println(no913.catMouseGame(graph));
//        no913.debug(1);
        System.out.println(no913.nextcount);
    }
}
