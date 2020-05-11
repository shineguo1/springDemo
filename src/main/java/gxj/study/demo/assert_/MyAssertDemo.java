package gxj.study.demo.assert_;


/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/1/10 11:41
 * @description
 */
public class MyAssertDemo {
    /**
     * 《重构》10.6 引入断言
     *
     * VM 默认关闭断言
     * VM Option 加命令-ea开启断言
     * 开启断言情况下，断言为false等同于抛出异常（java.lang.AssertionError），会中断程序。
     * 在非调试环境，可以关闭断言，忽视异常，让程序继续执行。
     *
     */
    public static void main(String[] args) {
        System.out.println("sentence 1");
        assert 1==1;
        assert  "a"=="c";
        //下面语句在关闭断言下会打印，在开启断言下不会执行。
        System.out.println("sentence 3");
    }

}
