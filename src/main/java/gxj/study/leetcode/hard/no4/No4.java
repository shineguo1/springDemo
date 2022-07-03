package gxj.study.leetcode.hard.no4;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/20 14:03
 * @description
 */
public class No4 {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        boolean isOdd = (len1 + len2) % 2 == 1;

        //因为nusm1、nums2都是升序，用二分查找中位数位置
        if (isOdd) {
            //第一个数标记为1
            int mid = (len1 + len2 + 1) / 2;
            return findMedian(nums1, nums2, 0, len1 - 1, 0, len2 - 1, mid);
        } else {
            //第一个数标记为1
            int midl = (len1 + len2) / 2;
            int midr = (len1 + len2 + 1) / 2;
            return (findMedian(nums1, nums2, 0, len1 - 1, 0, len2 - 1, midl) + findMedian(nums1, nums2, 0, len1 - 1, 0, len2 - 1, midr)) / 2.0;
        }
    }

    /**
     * 二分
     *
     * @param nums1  数组1
     * @param nums2  数组1
     * @param start1 数组1 起始位置
     * @param end1   数组1 结束位置
     * @param start2 数组2 起始位置
     * @param end2   数组2 结束位置
     * @param target 第target小的数（从1开始计数）
     * @return
     */
    private int findMedian(int[] nums1, int[] nums2, int start1, int end1, int start2, int end2, int target) {
        System.out.println(start1 + " " + end1 + " " + start2 + " " + end2 + " " + target);
        int half1 = (start1 + end1) / 2;
        int half2 = (start2 + end2) / 2;
        /*===递归结束情况===*/
        //最小数一定是两个数组最小数中小的那个
        if (target == 1) {
            return Math.min(nums1[start1], nums2[start2]);
        }
        //最大数一定是两个数组最大数中大的那个
        if (target == (end1 - start1 + 1) + (end2 - start2 + 1)) {
            return Math.max(nums1[end1], nums2[end2]);
        }
        //有一个数组长度变成1了，死循环

        //可以连成升序数组
        if (nums1[end1] <= nums2[start2]) {
            return getKLargeNum(nums1, nums2, start1, end1, start2, target);
        }
        if (nums2[end2] <= nums1[start1]) {
            return getKLargeNum(nums2, nums1, start2, end2, start1, target);
        }

        /*===递归情况===*/
        if (nums1[half1] <= nums2[half2] && target <= (half1 + 1)) {
            //比最小半边的小，target一定在2个数组的小半边的合集中。
            return findMedian(nums1, nums2, start1, half1, start2, half2, target);
        } else if (nums2[half2] <= nums1[half1] && target <= (half2 + 1)) {
            //比最小半边的小，target一定在2个数组的小半边的合集中。
            return findMedian(nums1, nums2, start1, half1, start2, half2, target);
        }
        //比最小的半边大，比2个小半边加起来小，一定不在最大的半边里（因为大半边的数一定大于2个小半边的任意数，即序号一定超过2个小半边数量之和
        else if (target <= (half1 + half2 + 2)) {
            return findMedian(nums1, nums2, start1, end1, start2, half2, target);
        }
        //比2个小半边加起来都大，肯定不在最小的半边里
        else if (target > (half1 + half2 + 2)) {
            return findMedian(nums1, nums2, half1 + 1, end1, start2, end2, target - (half1 + 1));
        }
        //不可能的情况
        return -1;
    }

    /**
     * 要求littleNums最大数比largeNums最小数小，查找第k大的数
     */
    private int getKLargeNum(int[] littleNums, int[] largeNums, int littleStart, int littleEnd, int largeStart, int k) {
        if (k <= (littleEnd - littleStart + 1)) {
            int offset = k - 1;
            return littleNums[littleStart + offset];
        } else {
            int offset = k - (littleEnd - littleStart + 1) - 1;
            return largeNums[largeStart + offset];
        }
    }

    public static void main(String[] args) {
        System.out.println(new No4().findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
    }
}
