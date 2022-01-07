package gxj.study.leetCode.normal.no390;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/6 17:28
 * @description
 */
public class No390 {

    /**
     * arr_0 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
     * arr_1 = [2, 4, 6, 8]
     * arr_2 = [2, 6]
     * arr_3 = [6]
     * <p>
     * arr_k通项 = head + gap * n
     * gap = 2^k
     * head = arr_k-1的a0 (k = 偶 且 arr_k-1.length = 偶)
     * 　　　= arr_k-1的a1 (k = 奇 ， k = 偶 且 arr_k-1.length = 奇)
     * arr_k.length = arr_k-1.length / 2 （取整）
     * 中止条件  ：arr_k.length / 2 = 1，
     */


    public int lastRemaining(int n) {
        if (n == 1) return 1;
        //初始值
        int head = 1, k = 0, gap = 1, length = n;

        /*
          长度大于1时，递推公式参数迭代。
         */
        while (length > 1) {
            /*
             从k递推到k+1
             如果k+1 = 偶，且 arr_k.length = 偶，head = head + gap * 0(首项)
             否则 head = head + gap * 1（第二项）
             */
            if ((k + 1) % 2 == 0 && length % 2 == 0) {
                //首相不变
            } else {
                //head = head + gap * 1（第二项）
                head = head + gap;
            }
            k++;
            gap = gap * 2;
            length = length / 2;
        }
        //返回通项 head + gap * n，首项n=0
        return head;
    }

    public static void main(String[] args) {
        System.out.println(new No390().lastRemaining(2));
        System.out.println(new No390().lastRemaining(4));
        System.out.println(new No390().lastRemaining(6));
        System.out.println(new No390().lastRemaining(9));
        System.out.println(new No390().lastRemaining(10));
    }
}
