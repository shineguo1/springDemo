package gxj.study.leetcode.medium.no71;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/6 11:10
 * @description
 */
public class No71 {

    private Deque<String> dirStack;
    public final static String UPPER = "..";
    public final static String CURRENT = ".";

    public String cd(String path) {
        String[] dirs = path.split("/");
        for (String dir : dirs) {
            receiveSimpleDir(dir);
        }
        return peekPath();
    }

    public String peekPath() {
        String path = "";
        for (String s : dirStack) {
            path = "/" + s + path;
        }
        return path.equals("") ? "/" : path;
    }

    private void receiveSimpleDir(String dir) {
        if (dir == null || dir.equals("") || dir.equals(CURRENT)) {
            //当前路径即当前目录
            return;
        } else if (dir.equals(UPPER)) {
            //返回上层
            goUpper();
        } else {
            //进入下层
            goInto(dir);
        }
    }

    /**
     * 进入下层：stack储存子目录。
     */
    private void goInto(String dir) {
        dirStack.push(dir);
    }

    /**
     * 返回上层：如果stack有目录，删除一级；如果stack无目录，已返回到顶层目录。
     */
    private void goUpper() {
        if (!dirStack.isEmpty()) {
            dirStack.pop();
        }
    }

    public String simplifyPath(String path) {
        dirStack = new LinkedList<>();
        cd(path);
        return peekPath();
    }
}
