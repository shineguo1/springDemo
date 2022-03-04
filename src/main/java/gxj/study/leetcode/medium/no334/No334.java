package gxj.study.leetcode.medium.no334;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/12 9:37
 * @description
 */
public class No334 {

    /**
     * 从左往右遍历，先找到第一个连续的递增二元组(a,b)，然后依次读取后续数字
     * 1. 如果读到数字 c < b，则将二元组修正为(a,c)，因为(a,c)严格优于(a,b)
     * 2. 如果读到数字 c < a，则另外缓存二元组(c,?)。 因为假设读到数字d，c < a < d <b， 则二元组(c,d)将严格优于(a,d)；若找到数字d，d > b，则找到三元组(a,b,d),缓存c弃用。
     * 什么叫严格优于：以(1.)为例子，截至到c为止，仅存在递增二元组，不存在递增三元组，且最小的递增二元组是(a,c), 即将c之前的数字序列看作黑盒，输出的最佳解是(a,c)，所以(a,c)严格优于(a,b)
     */

    public boolean increasingTriplet(int[] nums) {
        Integer[] ans = new Integer[2];
        Integer cache = null;

        for (int num : nums) {
            if(ans[1] != null && ans[1] < num){
                //找到三元组，快速返回
                return true;
            }
            //初始化
            if (ans[0] == null) {
                ans[0] = num;
            }else if (cache != null && cache < num) {
                //如果已缓存可能更优情况，检查规则2，如果满足，替换最优解。
                ans[0] = cache;
                ans[1] = num;
                cache = null;
            } else if (num > ans[0]) {
                //num大于a, 检查规则1
                if (ans[1] == null || ans[1] > num) {
                    ans[1] = num;
                }
            } else if (num < ans[0]) {
                if (ans[1] == null) {
                    //未找到二元组，将最小数字替换成num
                    ans[0] = num;
                } else {
                    cache = num;
                }
            }
        }
        return false;
    }
}
