package gxj.study.leetCode.simple.no1576;

import java.util.Arrays;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/6 14:50
 * @description
 */
public class No1576 {

    public String modifyString(String s) {
        Character preChar = null;
        Character nextChar = null;
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i + 1 < chars.length) {
                nextChar = chars[i + 1];
            } else {
                nextChar = null;
            }
            if (c != '?') {
                preChar = c;
                continue;
            }
            c = handle(preChar, nextChar);
            chars[i] = preChar = c;
        }
        return new String(chars);
    }

    private char handle(Character preChar, Character nextChar) {
        //因为一个字母最多只有2个相邻字母，所以使用3个字母就能解决问题
        if (!equals(preChar,'a') && !equals(nextChar,'a')) {
            return 'a';
        } else if (!equals(preChar,'b')&& !equals(nextChar,'b')) {
            return 'b';
        } else {
            return 'c';
        }
    }

    private boolean equals(Character c1, Character c2){
        boolean asset = c1 != null && c1.equals(c2);
        boolean assetWhileDoubleNull = c1 == null && c2 == null;
        return asset || assetWhileDoubleNull;
    }
}
