package gxj.study.util.scala;
import  scala.Option;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/12 11:00
 */
public class ScalaUtils {

    public static <T> Option<T> none() {
        return Option.apply(null);
    }


}
