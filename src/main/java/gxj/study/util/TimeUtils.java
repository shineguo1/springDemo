package gxj.study.util;


/**
 * 时间戳工具类
 *
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/8/1 11:27
 */
public class TimeUtils {
    /**
     * 自定义一小时的时间戳长度(millis)，便于demo构造测试数据
     */
    public static final int ONE_HOUR = 10000;

    /**
     * 上一个小时区间的截至时间
     * 如18:00, 属于[18:00,19:00),返回18:00
     */
    public static long getLastHour(long timestamp) {
        return timestamp / ONE_HOUR * ONE_HOUR;
    }

    /**
     * 当前时间所在小时区间的截至时间。
     * 数学描述：小时区间，左闭右开，返回区间右端点。如18:00属于[18:00,19:00)，返回19:00
     * 实际意义：汇总统计一个小时的数据，[18:00,19:00)区间的数据在19:00截至，所以18:00这样的整点也向上进一位，作为19:00截至的数据统计。
     */
    public static long getCurrentHour(long timestamp) {
        return getLastHour(timestamp) + ONE_HOUR;
    }

    /**
     * 下一个小时区间的截至时间
     */
    public static long getNextHour(long timestamp) {
        return getCurrentHour(timestamp) + ONE_HOUR;
    }

}
