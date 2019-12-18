package gxj.study.demo;

import org.assertj.core.util.Preconditions;
import org.junit.Test;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/12 9:19
 * @description
 */
public class DemoTest {

    @Test
    public void test(){
        String string = "abc";
        int minLength = 1;
        String padChar = "#";

        String ret  = "";
        Preconditions.checkNotNull(string);
        if(string.length() >= minLength) {
            ret =  string;
        } else {
            StringBuilder sb = new StringBuilder(minLength);

            for(int i = string.length(); i < minLength; ++i) {
                sb.append(padChar);
            }

            sb.append(string);
            ret =  sb.toString();
        }

        System.out.println(ret);
    }
}
