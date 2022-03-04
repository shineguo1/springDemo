package gxj.study.leetcode.hard.no1220;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/17 9:23
 * @description
 */
public class No1220 {
    /**
     * 按规则递归，缓存答案，快速返回。
     *　初始状态：ｎ=1 时，a、e、i、o、u都只有1种答案（自己本身）
     *  递归条件：后5个规则。
     *  所有测试用例可用共用相同的缓存答案。
     */
    private static final int MOD = 1000000007;
    //缓存满足长度为n，首字母为a、b、c、d、e的答案，数据规模n<=2w。答案为正整数(非0)。
    public static int[] a = new int[20001];
    public static int[] e = new int[20001];
    public static int[] i = new int[20001];
    public static int[] o = new int[20001];
    public static int[] u = new int[20001];

    static {
        //初始化，长度为1时，缓存答案1
        a[1] = 1;
        e[1] = 1;
        i[1] = 1;
        o[1] = 1;
        u[1] = 1;
    }


    public int countVowelPermutation(int n) {
        return numAdd(getA(n), getE(n), getI(n), getO(n), getU(n)) % (MOD);
    }

    private int getA(int n) {
        if (a[n] == 0) {
            a[n] = getE(n - 1);
        }
        return a[n];
    }

    private int getE(int n) {
        if (e[n] == 0) {
            e[n] = numAdd(getA(n - 1), getI(n - 1));
        }
        return e[n];
    }

    private int getI(int n) {
        if (i[n] == 0) {
            i[n] = numAdd(getA(n - 1), getE(n - 1), getO(n - 1), getU(n - 1));
        }
        return i[n];
    }

    private int getO(int n) {
        if (o[n] == 0) {
            o[n] = numAdd(getI(n - 1), getU(n - 1));
        }
        return o[n];
    }

    private int getU(int n) {
        if (u[n] == 0) {
            u[n] = getA(n - 1);
        }
        return u[n];
    }


    public static int numAdd(int... nums) {
        int ans = 0;
        for (int num : nums) {
            ans = (ans + num) % MOD;
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(new No1220().countVowelPermutation(17264));
        System.out.println(new No1220().countVowelPermutation(2));
        System.out.println(new No1220().countVowelPermutation(5));
    }
}
