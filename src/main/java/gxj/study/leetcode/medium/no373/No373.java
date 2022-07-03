package gxj.study.leetcode.medium.no373;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/14 11:29
 * @description
 */
public class No373 {
    /**
     * 遍历所有可能，用大顶堆储存最小的K个数，TLE
     * 优化时间复杂度，数组嵌套循环无法避免，已经条件nums1和nums2是升序数组未使用，考虑快速返回
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        //大顶堆
        PriorityQueue<int[]> pairQue = new PriorityQueue<>((pair1,pair2)->sum(pair2)-sum(pair1));
        for (int i : nums1) {
            for (int j : nums2) {
                if (pairQue.size() >= k) {
                    int[] peek = pairQue.peek();
                    int[] newPair = {i, j};
                    if(sum(peek) > sum(newPair)){
                        pairQue.remove();
                        pairQue.add(newPair);
                    }
                }else {
                    pairQue.add(new int[]{i, j});
                }
            }
        }
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if(!pairQue.isEmpty()) {
                int[] remove = pairQue.poll();
                list.add(Arrays.asList(remove[0], remove[1]));
            }
        }
        return list;
    }

    private int sum(int[] pair){
        return pair[0]+pair[1];
    }

    public static void main(String[] args) {
        int[] nums1 = new int[]{1,1,2};
        int[] nums2 = new int[]{1,2,3};
        int k = 2;
        List<List<Integer>> o = new No373().kSmallestPairs(nums1,nums2,k);
        System.out.println(JSON.toJSONString(o));
    }
}
