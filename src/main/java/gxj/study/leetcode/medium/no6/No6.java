package gxj.study.leetcode.medium.no6;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/7 10:22
 * @description
 */
public class No6 {
    /**
     * 数字123代表字符串的坐标（从1开始计数），n代表一共n行，k代表第k行通向。
     * 　每一行遵循　奇数项，空格数1，偶数项，空格数2　如此循环。第一行和最后一行奇数项和偶数项合二为一。
     * 注：以下通项不满足n=1情况，第一行的公差当n=1，2，3，4...时为1，2，4，6，8....，当n=1和非1时为分段函数。n=1时单独处理。
     * 1  1+2(n-1)  1+2*2(n-1)
     * 2  2+2(n-2)  2+2(n-2)+2*(2-1)
     * <p>
     * k  k+2(n-k) k+2(n-k)+2(k-1)=k+2(n-1)  奇(2i-1)：k+(i-1)*2(n-k)+(i-1)*2(k-1), 偶(2i)：k+i*2(n-k)+(i-1)*2(k-1)
     * <p>
     * 通项公式(ａ_ki代表第k行,第i项，上面的i等于这里的(i+1)/2_奇，i/2_偶, blank_ki表示第k行第i次出现)：
     * 数　ａ_ki = k+(i-1)*(n-k)+(i-1)(k-1)=k+(i-1)(n-1) (i=奇)
     * 　　　　　= k+    i*(n-k)+(i-2)(k-1)=2-k+i*(n-1) (i=偶)
     */
    public String convert(String s, int numRows) {
        /* n = 1 单独处理 */
        if(numRows == 1) return s;

        /* n > 1 套用通项公式 */
        char[] chars = s.toCharArray();
        int length = chars.length;
        int n = numRows;
        StringBuilder sb = new StringBuilder();
        for (int k = 1; k <= n; k++) {
            int i = 1, lastIndex = -1;
            int index = calculateOrdinal(k, i, n) - 1;
            /*
            每行不超过length个(非均匀分布在每行,极端情况只有一行,为length个)
            每行的元素index不超过数据源长度(每行集合通项公式的约束条件)
            */
            while (index < length && i<=length) {
                if (index != lastIndex) {
                    sb.append(chars[index]);
                    System.out.println(String.format("i:%d,k:%d,n:%d,index:%d", i, k, n, index));

                }
                lastIndex = index;
                index = calculateOrdinal(k, i, n) - 1;
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * @return 字符序号，从1开始
     */
    private int calculateOrdinal(int k, int i, int n) {
        /*
       　ａ_ki = k+(i-1)*(n-k)+(i-1)(k-1)=k+(i-1)(n-1) (i=奇)
       　    　= k+    i*(n-k)+(i-2)(k-1)=2-k+i*(n-1) (i=偶)
         */
        return i % 2 == 1 ? k + (i - 1) * (n - 1)
                : 2 - k + i * (n - 1);
    }

    public static void main(String[] args) {
        System.out.println(new No6().convert("A", 1));
    }
}
