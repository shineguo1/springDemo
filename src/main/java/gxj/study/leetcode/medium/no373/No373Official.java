package gxj.study.leetcode.medium.no373;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 作者：LeetCode-Solution
 * 链接：https://leetcode-cn.com/problems/find-k-pairs-with-smallest-sums/solution/cha-zhao-he-zui-xiao-de-kdui-shu-zi-by-l-z526/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class No373Official {

    /**
     * 遍历所有可能，用大顶堆储存最小的K个数，TLE
     * 优化时间复杂度，数组嵌套循环无法避免，已经条件nums1和nums2是升序数组未使用，考虑快速返回
     *
     * nums1和nums2可以构成nums1.length*nums2.length的矩阵，每个元素都是一个数对
     * 类比归并排序。最小的数对一定在第一列。将最小数对取出，将那一行后一个数对补到第一位，第二小的数对一定在新的第一列产生，以此类推。
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(k, (o1, o2) -> {
            return nums1[o1[0]] + nums2[o1[1]] - nums1[o2[0]] - nums2[o2[1]];
        });
        List<List<Integer>> ans = new ArrayList<>();
        int m = nums1.length;
        int n = nums2.length;
        int loopCount = 0;
        int offerCount = 0;
        //初始化最小数所在的列
        for (int i = 0; i < Math.min(m, k); i++) {
            loopCount++;
            offerCount++;
            pq.offer(new int[]{i, 0});
        }
        while (k-- > 0 && !pq.isEmpty()) {
            int[] idxPair = pq.poll();
            List<Integer> list = new ArrayList<>();
            list.add(nums1[idxPair[0]]);
            list.add(nums2[idxPair[1]]);
            ans.add(list);
            //第idx行是最小数，把这一行后一个数补进队列。
            if (idxPair[1] + 1 < n) {
                offerCount++;
                pq.offer(new int[]{idxPair[0], idxPair[1] + 1});
            }
            loopCount++;
        }
        System.out.println(this.getClass().getSimpleName() + " loopCount:" + loopCount + " offerCount:" + offerCount);
        return ans;
    }

}
