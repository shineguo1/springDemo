package gxj.study.leetcode.medium.no2029;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/20 10:20
 * @description
 */
public class No2029 {

    public boolean stoneGameIX(int[] stones) {
        int remainder[] = new int[3];
        for (int stone : stones) {
            remainder[stone % 3]++;
        }
        //如果只有3的倍数，那么先手一定输，此时3的倍数影响到了结果，特殊情况特殊处理。  -- 此情况被下面的判断包含
//        if(remainder[1] == 0 && remainder[2] == 0) return false;
        //3的倍数是奇数吗，任何时候取3的倍数都可以把处境反转给对方。所以3的倍数是奇数和偶数情况，答案是相反的。
        boolean isOdd = remainder[0] % 2 == 1;
        //下面研究3的倍数是偶数情况。此时3的倍数对结果不产生影响，当作0考虑。
        //取余情况一定是22121212... 或者 1121212121212...
        //由上可见，一定是第一种数连取2个，然后12轮流。
        //1. 能取完情况后手必胜。能取完情况包含stones只有1个，stones只有2个2+若干3或者2个1+若干3 -- 此情况被下面的判断包含
//        if((remainder[1] + remainder[2]) == 1 || (remainder[1] == 2 && remainder[2] == 0) || (remainder[1] == 0 && remainder[2] == 2)){            //默认后手赢，isOdd修正
//            return false;
//        }
        //2. 取不完情况 && 3的倍数是偶数情况：
        //  a. 有数量差异，先取少的，先手必胜
        //  b. 一样多情况，先手必胜
        //  c. 有一方为0，只能先取多的（数量为1取完，数量为2取完，数量>2先手会取到3的倍数）
        // 综上，偶数情况，1、2数量非空先手必胜，否则后手必胜
        if(!isOdd){
            return remainder[1] * remainder[2]  != 0;
        } else {
            //3. 取不完情况， && 3的倍数是奇数情况：
            //   如果棋子充足，先手先取数量多的，例如22121212，先手会先把1取完，然后3是奇数先后手反转，先手必胜
            //   如果棋子不充足，棋子会被取完，后手必胜
            //   综上，需要2比1数量多2以上或者1比2数量多2以上（如果恰好等于2，数字刚好取完），先手必胜；否则后手必胜。
            return Math.abs(remainder[2] - remainder[1]) >2 ;
        }
    }
}
