package gxj.study.leetCode.hard.no913;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/4 13:55
 * @description
 */
public class No913 {

    /**
     * 游戏终态（dp初始状态）：
     * 1. 老鼠在洞里，老鼠赢。
     * 2. 猫鼠重合，猫赢。
     * 3. 老鼠或猫步数超过n，平局。推理见下方
     * <p>
     * 可推理终态（差一步）
     * 1. 如果老鼠走，老鼠只能(全称量词)走到猫的位置，猫赢。
     * 2. 如果猫走，猫能够(特称量词)走到老鼠位置，猫赢。
     * 3. 如果老鼠走，老鼠能够(特称量词)走到洞位置，老鼠赢。
     * 4. 如果老鼠或猫走到重复的位置，平。
     * 说明：
     * - 如果某个位置是某一方胜利位置，那它的下一步有可证实的同类型胜利位置。以此形成一个最短链路（不存在自环，若存在自环，自环一定是冗余的），直到以上三种基本型。
     * - 如果a位置的胜利依赖b，b位置的胜利依赖a，形成循环依赖，因为前置条件无法证实，所以形成悖论。所以循环依赖（回路）只存在平局。
     * - 因为猫当前位置的胜率取决于下一步的最高胜率，所以当前位置胜率不会高于临近位置的最高胜率（与最高相同），换句话说，猫可以使走的每一步，胜率都不低于当前胜率(上一步)。
     * 同理，鼠走的每一步，也可以使胜率不低于上一步胜率。因为猫鼠胜率是零和博弈，所以猫鼠按最优策略执行，一定会使胜率不变，即必胜状态一定只会导向必胜状态，必败状态也一
     * 定会导向必败状态，平局状态也一定会导向平局状态。胜利状态的最短路径一定是无环的，平局因为无法结束，所以一定是有环的。
     * 综上所述：我们要确定平局的终态，就要找到平局和胜负路径的差别。（猫或鼠的）胜负路径是无环路径，长度是有限的，不会超过棋局总节点n；平局路径是有环的，长度是无限的，会超过棋局总节点n。
     * 所以只要判断猫+鼠的路径总数超过2n即为平局。
     * <p>
     * 状态转移方程：
     * 1. 如果老鼠走，老鼠能够(特称量词)走到老鼠赢位置(nextStep)，老鼠赢。
     * 2. 如果老鼠走，老鼠只能(全称量词)走到猫赢位置(nextStep)，猫赢。
     * 3. 如果老鼠走，老鼠只能走到猫赢或平局位置，平局。
     * 4. 如果猫走，猫能够(特称量词)走到猫赢位置(nextStep)，猫赢。
     * 5. 如果猫走，猫只能(全称量词)走到老鼠赢位置(nextStep)，老鼠赢。
     * 6. 如果猫走，猫只能走到老鼠赢或平局位置，平局。
     */

    public final static boolean CAT_TURN = true;
    public final static boolean MOUSE_TURN = false;

    public static Map<String, Cache> cache = new HashMap<>();
    public static Cache[][][] optArrayCache;
    public static int[][][] arrayCache;
    public static String activeCache;
    public final static String MAP_CACHE = "MAP";
    public final static String OPT_ARRAY_CACHE = "OPT_ARRAY";
    public final static String OFFICIAL_ARRAY_CACHE = "ARRAY";

    public final static int CAT_WIN = 2;
    public final static int MOUSE_WIN = 1;
    public final static int DRAW = 0;
    public final static int UNKNOW = -1;

    public final static int HOLE_POS = 0;
    public final static int INIT_CAT_POS = 2;
    public final static int INIT_MOUSE_POS = 1;

    private int graphSize;
    private int[][] localGraph;

    private int nextcount;

    class Cache {
        public Integer minTurn;
        public Integer status;
    }

    private int markAndGetStepStatus(int catPos, int mousePos, int turnCount) {
        Integer stepStatus = getCache(catPos, mousePos, turnCount);
        if (stepStatus == null) {
            stepStatus = getStepStatus(catPos, mousePos, turnCount);
            putCache(catPos, mousePos, stepStatus, turnCount);
        }
        return stepStatus;
    }

    private void putCache(int catPos, int mousePos, Integer stepStatus, int turnCount) {
        switch (activeCache) {
            case MAP_CACHE:
                String cacheKey = getCacheKeyLikeRedis(catPos, mousePos, turnCount);
                Cache myCache = new Cache();
                myCache.minTurn = turnCount;
                myCache.status = stepStatus;
                cache.put(cacheKey, myCache);
                return;
            case OFFICIAL_ARRAY_CACHE:
                if (turnCount >= 2 * graphSize) {
                    //cache数组越界
                    return;
                }
                arrayCache[catPos][mousePos][turnCount] = stepStatus;
                return;
            case OPT_ARRAY_CACHE:
                Cache myOptCache = new Cache();
                myOptCache.minTurn = turnCount;
                myOptCache.status = stepStatus;
                //猫鼠行动是不同的，用turn%2区分
                optArrayCache[catPos][mousePos][turnCount % 2] = myOptCache;
            default:
                return;
        }
    }

    private Integer getCache(int catPos, int mousePos, int turn) {
        switch (activeCache) {
            case MAP_CACHE:
                String cacheKey = getCacheKeyLikeRedis(catPos, mousePos, turn);
                Cache myCache = cache.get(cacheKey);
                return readStatusFromCache(myCache, turn);
            case OFFICIAL_ARRAY_CACHE:
                if (turn >= 2 * graphSize) {
                    //cache数组越界
                    return null;
                }
                int status = arrayCache[catPos][mousePos][turn];
                if (status < 0) {
                    //-1为初始化值，适配返回null
                    return null;
                }
                return status;
            case OPT_ARRAY_CACHE:
                //猫鼠行动是不同的，用turn%2区分
                Cache myOptCache = optArrayCache[catPos][mousePos][turn % 2];
                return readStatusFromCache(myOptCache, turn);
            default:
                return null;
        }
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

    private int getStepStatus(int catPos, int mousePos, int turnCount) {
        nextcount++;
        //1. 终态
        if (turnCount > 2 * graphSize) {
            return DRAW;
        }
        if (mousePos == HOLE_POS) {
            return MOUSE_WIN;
        }
        if (catPos == mousePos) {
            return CAT_WIN;
        }
        boolean isCatTurn = whoseTurn(turnCount) == CAT_TURN;
        if (isCatTurn) {
            //1. 终态
            if (canGoTo(catPos, mousePos)) {
                return CAT_WIN;
            }
            //2. 转移方程
            int[] nextStepsStatus = getAllNextSteps(catPos, mousePos, turnCount);
            if (anyIs(nextStepsStatus, CAT_WIN)) {
                return CAT_WIN;
            }
            if (anyIs(nextStepsStatus, DRAW)) {
                return DRAW;
            }
            if (allIs(nextStepsStatus, MOUSE_WIN)) {
                return MOUSE_WIN;
            }
        } else {
            //mouse turn
            //1. 终态
            if (canGoTo(mousePos, HOLE_POS)) {
                return MOUSE_WIN;
            }
            if (allGoTo(mousePos, catPos)) {
                return CAT_WIN;
            }
            //2. 转移方程
            int[] nextStepsStatus = getAllNextSteps(catPos, mousePos, turnCount);
            if (anyIs(nextStepsStatus, MOUSE_WIN)) {
                return MOUSE_WIN;
            }
            if (anyIs(nextStepsStatus, DRAW)) {
                return DRAW;
            }
            if (allIs(nextStepsStatus, CAT_WIN)) {
                return CAT_WIN;
            }
        }
        //剩余情况一定符合转移方程的3或6。
        return UNKNOW;
    }

    private boolean anyIs(int[] nextStepsStatus, int targetStatus) {
        for (int status : nextStepsStatus) {
            if (status == targetStatus)
                return true;
        }
        return false;
    }

    private boolean allIs(int[] nextStepsStatus, int targetStatus) {
        for (int status : nextStepsStatus) {
            if (status != targetStatus)
                return false;
        }
        return true;
    }

    private int[] getAllNextSteps(int catPos, int mousePos, int turnCount) {
        if (whoseTurn(turnCount) == CAT_TURN) {
            int[] nextCatPos = Arrays.stream(localGraph[catPos]).filter(o -> o != 0).toArray();
            int[] nextStepsStatus = new int[nextCatPos.length];
            for (int i = 0; i < nextStepsStatus.length; i++) {
                nextStepsStatus[i] = markAndGetStepStatus(nextCatPos[i], mousePos, turnCount + 1);
            }
            return nextStepsStatus;
        } else {
            int[] nextMousePos = localGraph[mousePos];
            int[] nextStepsStatus = new int[nextMousePos.length];
            for (int i = 0; i < nextStepsStatus.length; i++) {
                nextStepsStatus[i] = markAndGetStepStatus(catPos, nextMousePos[i], turnCount + 1);
            }
            return nextStepsStatus;
        }
    }

    public String getCacheKeyLikeRedis(int catPos, int mousePos, int turnCount) {
        String whoseTurn = whoseTurn(turnCount) == CAT_TURN ? ":catMove" : ":mouseMove";
        return "cat:" + catPos + ":mouse:" + mousePos + whoseTurn;
    }

    private boolean whoseTurn(int turnCount) {
        //鼠先走，所以奇数是鼠的回合，偶数是猫的回合
        return turnCount % 2 == 1 ? MOUSE_TURN : CAT_TURN;
    }

    private boolean canGoTo(int from, int to) {
        for (Integer integer : localGraph[from]) {
            if (integer == to)
                return true;
        }
        return false;
    }

    private boolean allGoTo(int from, int to) {
        for (Integer integer : localGraph[from]) {
            if (integer != to)
                return false;
        }
        return true;
    }


    public int catMouseGame(int[][] graph) {
        nextcount = 0;
        localGraph = graph;
        graphSize = graph.length;
        activeCache(OPT_ARRAY_CACHE);
        cleanCache();
        int firstTurn = 1;
        return markAndGetStepStatus(INIT_CAT_POS, INIT_MOUSE_POS, firstTurn);
    }

    private void activeCache(String cacheType) {
        activeCache = cacheType;
    }

    private void cleanCache() {
        //懒得写类了，可以抽象缓存适配器
        switch (activeCache) {
            case MAP_CACHE:
                cache.clear();
            case OFFICIAL_ARRAY_CACHE:
                arrayCache = new int[graphSize][graphSize][graphSize * 2];
                for (int i = 0; i < graphSize; i++) {
                    for (int j = 0; j < graphSize; j++) {
                        Arrays.fill(arrayCache[i][j], -1);
                    }
                }
            case OPT_ARRAY_CACHE:
                optArrayCache = new Cache[graphSize][graphSize][2];
            default:
                return;
        }
    }

    public static void main(String[] args) {
        //optArrayCache和mapCache都是优化过的，下方测试用例循环的复杂度比官方版本少一个0，但是总耗时仍然比官方答案高
        int[][] graph = new int[][]{{3, 4, 6, 7, 9, 15, 16, 18}, {4, 5, 8, 19}, {3, 4, 6, 9, 17, 18}, {0, 2, 11, 15}, {0, 1, 10, 6, 2, 12, 14, 16}, {1, 10, 7, 9, 15, 17, 18}, {0, 10, 4, 7, 9, 2, 11, 12, 13, 14, 15, 17, 19}, {0, 10, 5, 6, 9, 16, 17}, {1, 9, 14, 15, 16, 19}, {0, 10, 5, 6, 7, 8, 2, 11, 13, 15, 16, 17, 18}, {4, 5, 6, 7, 9, 18}, {3, 6, 9, 12, 19}, {4, 6, 11, 15, 17, 19}, {6, 9, 15, 17, 18, 19}, {4, 6, 8, 15, 19}, {0, 3, 5, 6, 8, 9, 12, 13, 14, 16, 19}, {0, 4, 7, 8, 9, 15, 17, 18, 19}, {5, 6, 7, 9, 2, 12, 13, 16}, {0, 10, 5, 9, 2, 13, 16}, {1, 6, 8, 11, 12, 13, 14, 15, 16}};
        No913 no913 = new No913();
        System.out.println(no913.catMouseGame(graph));
        System.out.println(no913.nextcount);
    }
}
