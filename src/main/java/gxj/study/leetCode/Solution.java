package gxj.study.leetCode;


import com.alibaba.fastjson.JSON;

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

    public int uniquePaths(int m, int n) {
        //可以向右走n-1步，可以向下走m-1步
        //等价于将向下走的步数插入向右走的步数之间（包括两端）
        //排列组合
        int x = n - 1;
        int y = m - 1;
        if (m == 0 || n == 0) return 0;
        return calculate(x + y, y);

    }

    int calculate(int x, int count) {
        int ret = 1;
        int diff = 1;
        while (count-- > 0) {
            ret *= x;
            ret /= diff;
            x--;
            diff++;
        }
        return ret;
    }

    public static void main(String[] args) {
//        int i = new Solution().uniquePaths(10, 10);
        System.out.println(new Solution().calculate(18, 9));
        Double a = 0.0;
        a.intValue();
    }
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