package gxj.study.demo.flink.univ2.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/29 13:53
 */
@Data
public class EventData {

    /**
     * 项目名称
     */
    private String project;

    /**
     * 元数据名称
     */
    private String name;
    /**
     * 区块时间（格式：yyyy-MM-dd  时区：UTC）
     * 说明：这个类在多处使用fastJson序列化，fastJson只能通过JSON.defaultTimeZone全局常量设置时区，
     * JSONField注解不能特殊地设置时区。如果隐蔽地修改了全局常量容易在其他地方引发问题且不易定位原因。
     * 因此干脆将这个字段设计成String类型。
     */
    private String pkDay;
    /**
     * 元数据内容
     */
    private JSONObject data;

}
