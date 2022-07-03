package gxj.study.leetcode.hard.no1345;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/21 9:54
 * @description
 */
public class No1345 {


    public int minJumps(int[] arr) {
        //图规则：连通相邻节点和等值节点。

        //cache,index:数值，value：index数组
        HashMap<Integer, List<Integer>> cache = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (!cache.containsKey(arr[i])) {
                cache.put(arr[i], new ArrayList<>());
            }
            cache.get(arr[i]).add(i);
        }
        System.out.println(JSON.toJSONString(cache));

        //节省空间，就不构造图结构了。临节点算法：相邻边、cache内同一个key的其他节点
        //bfs：目标，查找到arr.length-1坐标，dist[],从原点到达目标位置最短步数
        Integer[] dist = new Integer[arr.length];
        int cur = 0;
        dist[0] = 0;
        int target = arr.length - 1;
        Queue<Integer> que = new LinkedList<>();
        que.add(cur);
        while (!que.isEmpty()) {
            cur = que.poll();
            List<Integer> neighbours = getNeighbours(cur, arr, cache, dist);
            int nextDist = dist[cur] + 1;
            for (Integer neighbour : neighbours) {
                //抵达目标节点，快速返回
                if (neighbour == target) {
                    System.out.println(JSON.toJSONString(dist));
                    return nextDist;
                }
                //抵达非目标节点，如果是新节点，缓存距离，入队继续遍历
                dist[neighbour] = nextDist;
                que.add(neighbour);
            }
        }
        //异常返回
        System.out.println(JSON.toJSONString(dist));
        return dist[target];
    }

    /**
     * 按规则计算cur的邻居节点
     * 规则：
     * 1.如果数组不越界，cur-1、cur+1是邻居节点。
     * 2.值相等的其他节点是邻居节点。
     */
    private List<Integer> getNeighbours(int cur, int[] arr, HashMap<Integer, List<Integer>> cache, Integer[] dist) {
        List<Integer> neighbours = new ArrayList<>();
        List<Integer> integers = cache.get(arr[cur]);
        if (integers != null) {
            for (Integer integer : integers) {
                if (integer != cur && dist[integer] == null) {
                    neighbours.add(integer);
                }
            }
            //关键一步，解决超时：因为能一步到达所有同值节点，所以以后不会再用到这个值的同值节点缓存了。因为当前步数一定是最小步数。
            cache.remove(arr[cur]);
        }
        if (cur - 1 >= 0 && dist[cur - 1] == null) {
            neighbours.add(cur - 1);
        }
        if (cur + 1 <= arr.length - 1 && dist[cur + 1] == null) {
            neighbours.add(cur + 1);
        }
        return neighbours;
    }

    public static void main(String[] args) {
        System.out.println(new No1345().minJumps(new int[]{7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 11}));
    }
}
