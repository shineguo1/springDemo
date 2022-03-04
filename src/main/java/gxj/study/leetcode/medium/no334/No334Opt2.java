package gxj.study.leetcode.medium.no334;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/12 9:37
 * @description
 */
public class No334Opt2 {

    /**
     * 从左往右遍历，先找到第一个连续的递增二元组(a,b)，然后依次读取后续数字
     * 1. 如果读到数字 c < b，则将二元组修正为(a,c)，因为(a,c)严格优于(a,b)
     * 2. 如果读到数字 c < a，则另外缓存二元组(c,?)。 因为假设读到数字d，c < a < d <b， 则二元组(c,d)将严格优于(a,d)；若找到数字d，d > b，则找到三元组(a,b,d),缓存c弃用。
     * 什么叫严格优于：以(1.)为例子，截至到c为止，仅存在递增二元组，不存在递增三元组，且最小的递增二元组是(a,c), 即将c之前的数字序列看作黑盒，输出的最佳解是(a,c)，所以(a,c)严格优于(a,b)
     * 状态:
     * 1. a == null, 令a = num;
     * 2. a != null && b == null, 令 a = num while a > num; 令 b = num while a < num;
     * 3. a != null && b != null && cache == null, find answer while num > b; 令 b = num while b > num > a; 令 cache = num while a > num;
     * 4. a != null && b != null && cache != nul , find answer while num > b; 令 a = cache, b = num while b > num > c; 令 cache = num while c > num;
     */

    public boolean increasingTriplet(int[] nums) {
        Integer smallestFirst = null;
        Integer smallestSecond = null;
        for (int num : nums) {
            if(smallestSecond != null && num > smallestSecond){
                return true;
            }
             else if(smallestFirst == null || num < smallestFirst){
                smallestFirst = num;
            }
             else if( num > smallestFirst && (smallestSecond == null || num < smallestSecond)){
                smallestSecond = num;
            }
        }
        return false;
    }


}
