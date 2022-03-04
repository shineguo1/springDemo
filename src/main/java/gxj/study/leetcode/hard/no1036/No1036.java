package gxj.study.leetcode.hard.no1036;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/11 10:42
 * @description
 */
public class No1036 {
    /**
     * 数据规模10^6*10^6
     * <p>
     * 思路:从start开始上下左右遍历可达坐标,再从上下左右遍历它们的上下左右可达坐标. 直到找到source
     * 结果: 数据规模太大, TLE、内存超出限制。需要优化。
     */

    public final static int BLOCK = -1;
    public final static int INIT = 0;
    public final static int ACCESS = 1;
    private static final int SIZE = 1000000;


    public boolean isEscapePossible(int[][] blocked, int[] source, int[] target) {
        int[][] board = new int[SIZE][SIZE];
        //记录不可达方格
        for (int[] block : blocked) {
            board[block[0]][block[1]] = BLOCK;
        }
        //记录起点可达
        board[source[0]][source[1]] = ACCESS;
        //起点入队列
        Queue<int[]> queue = new LinkedList<>();
        queue.add(source);
        while (queue.size() > 0) {
            int[] ints = queue.poll();
            //查找并遍历上下左右邻居
            List<int[]> neighbours = getNeighbours(ints);
            for (int[] neighbour : neighbours) {
                int x = neighbour[0];
                int y = neighbour[1];
                //标记可达,加入队列(广度优先遍历)
                if (board[x][y] == INIT) {
                    board[x][y] = ACCESS;
                    queue.add(neighbour);
                }
                //到达目标,快速返回,目标不会block
                if (x == target[0] && y == target[1]) {
                    return true;
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
        if (x - 1 >= 0 && y - 1 >= 0) {
            neighbours.add(new int[]{x - 1, y - 1});
        }
        if (x - 1 >= 0 && y + 1 <= SIZE - 1) {
            neighbours.add(new int[]{x - 1, y + 1});
        }
        if (x + 1 <= SIZE - 1 && y - 1 >= 0) {
            neighbours.add(new int[]{x + 1, y - 1});
        }
        if (x + 1 <= SIZE - 1 && y + 1 <= SIZE - 1) {
            neighbours.add(new int[]{x + 1, y + 1});
        }
        return neighbours;
    }

}
