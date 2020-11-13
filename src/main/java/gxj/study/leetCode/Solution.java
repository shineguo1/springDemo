package gxj.study.leetCode;


import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;
import java.util.stream.Collectors;

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {
    }

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}


public class Solution {

//    public int[][] insert(int[][] intervals, int[] newInterval) {
//        int len = intervals.length;
//        List<int[]> intervalsList = new ArrayList<>();
//        Collections.addAll(intervalsList,intervals);
//        for (int[] interval : intervalsList) {
//            if (interval[0] < newInterval[0]) {
//                if (interval[1] > newInterval[0]) {
//                    if (interval[1] >= newInterval[1]) return intervals;
//                    else {
//                        interval[1] = newInterval[1];
//                        newInterval = interval;
//                    }
//                }
//            }
//        }
////        for (int i = 1; i < intervals.length; i++) {
////            if()
////        }
//    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int x) {
        val = x;
        next = null;
    }
}