package gxj.study.leetcode.hard.no1036;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/11 10:42
 * @description
 */
public class No1036Opt {
    /**
     * 数据规模10^6*10^6
     * <p>
     * 优化1.0：BFS全部遍历数据规模太大，考虑提前退出的办法。
     * 思路:1.从start和target开始BFS遍历，因为blocked数量有限（<=200个），只要能离开原点超过x距离，就不可能被blocked封锁住。
     * 　　接下来确定参数x的大小：blocked长度为n，所以包围面积最长内径距离不会超过n（贴角斜着），所以x=n
     * 2. 从start和target开始BFS遍历，如果在突破包围之前遇到了target，也能直接返回。
     */

    public final static int BLOCK = -1;
    public final static int ACCESS = 1;
    private static final int SIZE = 1000000;

    /**
     * 优化2.0：缓存map的key从String优化为long, 耗时从464ms提升到108ms
     */
    private Long getBoardKey(int x, int y) {
        return (long) x * SIZE + y;
    }

    public boolean isEscapePossible(int[][] blocked, int[] source, int[] target) {

        return isConnected(blocked, source, target) && isConnected(blocked, target, source);
    }

    /**
     * 是否连通：从source开始，是否能突破blocked包围（已证明：离开起点blocked.length的横向/纵向距离为充分条件）或source和target是否能直接证明连通。
     */
    private boolean isConnected(int[][] blocked, int[] source, int[] target) {
        Map<Long, Integer> board = new HashMap<>();
        //记录不可达方格
        for (int[] block : blocked) {
            board.put(getBoardKey(block[0], block[1]), BLOCK);
        }
        //记录起点可达
        board.put(getBoardKey(source[0], source[1]), ACCESS);
        //起点入队列
        Queue<int[]> queue = new LinkedList<>();
        queue.add(source);
        //BFS查找是否能突破禁止块最大范围（找到一个连通的方块，距离起点超过禁止块的数量，则起点一定不会被禁止块包围）
        while (queue.size() > 0) {
            int[] ints = queue.poll();
            //查找并遍历上下左右邻居
            List<int[]> neighbours = getNeighbours(ints);
            for (int[] neighbour : neighbours) {
                int x = neighbour[0];
                int y = neighbour[1];
                //标记可达,加入队列(广度优先遍历)
                if (!board.containsKey(getBoardKey(x, y))) {
                    board.put(getBoardKey(x, y), ACCESS);
                    queue.add(neighbour);
                    //到达目标,快速返回,目标不会block
                    boolean isConnected = Math.abs(x - source[0]) >= blocked.length || Math.abs(y - source[1]) >= blocked.length;
                    boolean isSameSide = x == target[0] && y == target[1];
                    if (isConnected || isSameSide) {
                        return true;
                    }
                }
                //已标记ACCESS和BLOCK的方格不用处理
            }
        }
        //BFS结束,未找到target,返回false
        return false;
    }

    private List<int[]> getNeighbours(int[] ints) {
        List<int[]> neighbours = new ArrayList<>();
        int x = ints[0];
        int y = ints[1];
        if (x - 1 >= 0) {
            neighbours.add(new int[]{x - 1, y});
        }
        if (x + 1 <= SIZE - 1) {
            neighbours.add(new int[]{x + 1, y});
        }
        if (y - 1 >= 0) {
            neighbours.add(new int[]{x, y - 1});
        }
        if (y + 1 <= SIZE - 1) {
            neighbours.add(new int[]{x, y + 1});
        }
        return neighbours;
    }

    public static void main(String[] args) {
        int[][] blocked = new int[][]{{0, 3}, {1, 0}, {1, 1}, {1, 2}, {1, 3}};
        int[] source = new int[]{0, 0};
        int[] target = new int[]{0, 2};
        System.out.println(new No1036Opt().isEscapePossible(blocked, source, target));
    }
}
