package gxj.study.leetcode.medium.no382;

import com.alibaba.fastjson.JSON;
import gxj.study.leetcode.medium.no373.No373;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/17 10:12
 * @description
 */
public class No382 {
    /**
     * 蓄水池抽样算法
     * 遍历链表，每遍历一位，就有相当的概率替换答案。遍整个链表后，剩下的答案就是以对应概率选中这个答案的情况。
     * 这个算法用来解决数据规模极大，内存不够的情况，只需遍历一次链表，O(1)空间复杂度。
     */


    ListNode head;
    Random random;

    public No382(ListNode head) {
        this.head = head;
        this.random = new Random();
    }

    public int getRandom() {
        int ans = head.val;
        int size = 1;
        ListNode cur = head;
        while (cur.next != null) {
            size++;
            cur = cur.next;
            //第size数被选中的概率是 size分之一，即与 random size规模，出现0概率相同。
            boolean isPick = random.nextInt(size) == 0;
            if (isPick) {
                ans = cur.val;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] vals = {10, 100, 100, 20, 20, 100};
        ListNode head = new ListNode(vals);
        No382 obj = new No382(head);
        Map<Integer, Integer> ans = new HashMap<>();
        //初始化每种数据出现0次。
        Arrays.stream(vals).forEach(v -> ans.put(v, 0));
        //随机10000次，记录每种数字出现次数
        for (int i = 0; i < 10000; i++) {
            int random = obj.getRandom();
            ans.put(random, ans.get(random) + 1);
        }
        System.out.println(JSON.toJSONString(ans));

    }
/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(head);
 * int param_1 = obj.getRandom();
 */


}
