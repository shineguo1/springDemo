package gxj.study.leetcode.medium.no382;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/17 10:14
 * @description
 */
public class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    ListNode(int... vals){
        ListNode cur = null;
        for (int i = 0; i < vals.length; i++) {
            if(i == 0){
                cur = this;
                cur.val = vals[i];
            } else {
                cur.next = new ListNode(vals[i]);
                cur = cur.next;
            }
        }
    }
}
