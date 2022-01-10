package gxj.study.leetcode.medium.no89;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/10 11:34
 * @description
 */
public class No89 {


    public List<Integer> grayCode(int n) {
        //数学公式法
        List<Integer> list = new ArrayList<>();
        int power = 1<<n;
        for(int i = 0;i<power;i++){
            int num = (i >> 1) ^ i;
            list.add(num);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(new No89().grayCode(2));
    }
}
