package gxj.study.leetcode.hard.no913;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/4 13:55
 * @description
 */
@Slf4j
public class No913OfficialOpt {
    static final int MOUSE_WIN = 1;
    static final int CAT_WIN = 2;
    static final int DRAW = 0;
    int n;
    int[][] graph;
    Cache[][][] dp;

    int nextcount;

    public int catMouseGame(int[][] graph) {
        this.nextcount = 0;
        this.n = graph.length;
        this.graph = graph;
        this.dp = new Cache[n][n][2];
        return getResult(1, 2, 0);
    }

    public int getResult(int mouse, int cat, int turns) {
        if (turns == n * 2) {
            return DRAW;
        }
        if (readStatusFromCache(dp[mouse][cat][turns%2],turns) == null) {
            int status;
            if (mouse == 0) {
                status = MOUSE_WIN;
            } else if (cat == mouse) {
                status = CAT_WIN;
            } else {
                status = getNextResult(mouse, cat, turns);
            }
            Cache cache = new Cache();
            cache.minTurn = turns;
            cache.status = status;
            dp[mouse][cat][turns%2] = cache;
        }
        return dp[mouse][cat][turns%2].status;
    }

    public int getNextResult(int mouse, int cat, int turns) {

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

    class Cache {
        public Integer minTurn;
        public Integer status;
    }

    private Integer readStatusFromCache(Cache myCache, int turn) {
        if (myCache == null || myCache.minTurn == null) {
            return null;
        }
        if (myCache.minTurn > turn && myCache.status == DRAW) {
            //无效缓存：turn变小了，可能会算出胜负关系
            return null;
        }
        return myCache.status;
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
        No913OfficialOpt no913 = new No913OfficialOpt();
        System.out.println(no913.catMouseGame(graph));
//        no913.debug(1);
        System.out.println(no913.nextcount);
    }
}
