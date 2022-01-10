package gxj.study.leetcode.easy.no1185;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/1/6 15:40
 * @description
 */
public class No1185 {
    private String[] weekDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public String dayOfTheWeek(int day, int month, int year) {
        return implByLocalDate(day, month, year);
    }

    private String implByLocalDate(int day, int month, int year) {
        LocalDate localDate = LocalDate.of(year,month,day);
        //ordinal返回0-6，Monday是0
        //getValue返回1-7,Monday是7
        return weekDays[localDate.getDayOfWeek().getValue()%7];
    }

    private String implByDate(int day, int month, int year) {
        //返回0-6， Sunday=0
        int week = new Date(year - 1900, month - 1, day).getDay();
        //转换String
        return weekDays[week];
    }

    public static void main(String[] args) {
        System.out.println(new No1185().dayOfTheWeek(6,1,2022));
    }
}
