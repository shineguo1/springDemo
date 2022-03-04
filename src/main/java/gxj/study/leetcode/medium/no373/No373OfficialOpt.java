package gxj.study.leetcode.medium.no373;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class No373OfficialOpt {

    /**
     * 【尝试对标答做优化，减少了循环和入队次数，但增加了缓存和hash操作，耗时没有明显变化】
     * <p>
     * 遍历所有可能，用大顶堆储存最小的K个数，TLE
     * 优化时间复杂度，数组嵌套循环无法避免，已经条件nums1和nums2是升序数组未使用，考虑快速返回
     * <p>
     * nums1和nums2可以构成nums1.length*nums2.length的矩阵，每个元素都是一个数对
     * 贪心：最小的数对一定在(1,1)。
     * 第二小数对一定在(1,1)的右边和下面中产生。
     * <p>
     * 规则：
     * 假如(i,j)是候选队列中最小数，取出(i,j)，将(i+1,j)、(i,j+1)加入候选队列。
     * 假如(i,j)在候选队列中，(ii,jj) （ii>=i, jj>=j,等号至多只有一个成立）不能加入候选队列。
     * 将候选数对在矩阵中标记出来，一定满足楼梯形结构
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(k, (o1, o2) -> {
            return nums1[o1[0]] + nums2[o1[1]] - nums1[o2[0]] - nums2[o2[1]];
        });
        List<List<Integer>> ans = new ArrayList<>();
        int m = nums1.length;
        int n = nums2.length;
        int loopCount = 1;
        int offerCount = 1;
        //初始化最小数所在的列
        pq.offer(new int[]{0, 0});
        Map<Integer, Integer> cols = new HashMap<>();
        cols.put(0, 0);
        Map<Integer, Integer> rows = new HashMap<>();
        rows.put(0, 0);
        while (k-- > 0 && !pq.isEmpty()) {
            loopCount++;
            int[] idxPair = pq.poll();
            int x = idxPair[0];
            int y = idxPair[1];
            addToList(nums1[x], nums2[y], ans);
            //把(x+1,y)补进候选队列
            if (x + 1 < m) {
                // y = 0可以补
                // 对所有在候选队列中的(x+1, ?)，y'是最大的纵坐标。如果y'+1<=y, 则(x+1, y'+1)可以补
                // 即满足 在矩阵中(x+1,y)上方(包含)且未加入过候选队列的第一个方格。
                Integer colMax = cols.get(x + 1);
                if (colMax == null) {
                    offerCount++;
                    putIntoQue(pq, cols, rows, x + 1, 0);
                } else if (colMax + 1 < n) {
                    //找到x+1列被标记的最大数对(x+1, colMax)
                    //如果(x+1, colMax) 位于 (x+1,y) 上方（包含重合），则将(x+1, colMax)加入候选队列
                    if (y >= colMax + 1) {
                        offerCount++;
                        putIntoQue(pq, cols, rows, x + 1, colMax + 1);
                    }
                }
            }
            //把(x,y+1)补进候选队列
            if (y + 1 < n) {
                //与上述相仿
                Integer rowMax = rows.get(y + 1);
                if (rowMax == null) {
                    offerCount++;
                    putIntoQue(pq, cols, rows, 0, y + 1);
                } else if (rowMax + 1 < m) {
                    if (x >= rowMax + 1) {
                        offerCount++;
                        putIntoQue(pq, cols, rows, rowMax + 1, y + 1);
                    }
                }
            }
        }
        System.out.println(this.getClass().getSimpleName() + " loopCount:" + loopCount + " offerCount:" + offerCount);
        return ans;
    }

    private void putIntoQue(PriorityQueue<int[]> pq, Map<Integer, Integer> cols, Map<Integer, Integer> rows, int x, int y) {
        pq.offer(new int[]{x, y});
        rows.put(y, x);
        cols.put(x, y);
    }

    private void addToList(int e, int e1, List<List<Integer>> ans) {
        List<Integer> list = new ArrayList<>();
        list.add(e);
        list.add(e1);
        ans.add(list);
    }

    public static void main(String[] args) {
        int[] nums1 = new int[]{3, 22, 35, 56, 76};
        int[] nums2 = new int[]{3, 22, 35, 56, 76};
        int k = 25;
        List<List<Integer>> o3 = new No373OfficialOpt().kSmallestPairs(nums1, nums2, k);
    }


}
