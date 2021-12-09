package gxj.study.util;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/28 16:18
 * @description
 */
public class Counter {

    private static final Counter instance = new Counter();
    private static AtomicInteger count = new AtomicInteger(0);

    public static int getCount() {
        return count.incrementAndGet();
    }


    public int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        return quickSort(nums, 0, n - 1, k);
    }

    private int quickSort(int[] nums, int l, int r, int k) {
        if (l >= r) return nums[l];
        int i = l - 1, j = r + 1, x = nums[l + r >> 1];
        while (i < j) {
            // 求第k大元素，把模板的 < 改成 > 就行了。
            do i ++; while (nums[i] > x);
            do j --; while (nums[j] < x);
            if (i < j) {
                int t = nums[i];
                nums[i] = nums[j];
                nums[j] = t;
            }
        }
        int sl = j - l + 1;
        System.out.println(sl);
        if (k <= sl) return quickSort(nums, l, j, k);
        return quickSort(nums, j + 1, r, k - sl);
    }


    public static void main(String[] args) {
        int k = 3;
        int[] arr = new int[]{0};
        instance.findKthLargest(new int[]{-1,2,0}, 2);
    }
}
