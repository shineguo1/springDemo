package gxj.study.leetcode.easy.no1614;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/7 9:33
 * @description
 */
public class No1614 {

    public int maxDepth(String s) {
        Deque<Character> stack = new LinkedList<>();
        int maxLength = 0;
        for (char c : s.toCharArray()) {
            if('(' == c){
                stack.push(c);
                maxLength = Math.max(maxLength,stack.size());
            } else {
                if(')' == c ){
                    Character poll = stack.poll();
                    if(poll == null){
                        //无效括号字符串
                        return -1;
                    }
                }
            }
        }
        return maxLength;
    }

}
