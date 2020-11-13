package gxj.study.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/25 17:16
 * @description
 */
public class ReadTableStructure {
    private String sql = "";
    private List<TableAttribute> list = new ArrayList<>();


    public void initSql() {
        sql =
"CREATE TABLE `T_MONITOR_WHITE_MEMBER_INFO` (\n" +
        "  `ID` int(11) NOT NULL AUTO_INCREMENT,\n" +
        "  `WHITE_MEMBER_NO` varchar(32) DEFAULT NULL COMMENT '商户白名单编号',\n" +
        "  `MEMBER_ID` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商户ID',\n" +
        "  `MEMBER_NAME` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商户名称',\n" +
        "  `WHITE_TYPE` varchar(8) CHARACTER SET utf8 DEFAULT NULL COMMENT '白名单类型 WARN',\n" +
        "  `SELLER_IS_NOT_EMAIL` varchar(8) CHARACTER SET utf8 DEFAULT NULL COMMENT '销售不发邮件  YES NO',\n" +
        "  `SELLER_IS_NOT_MOBILE` varchar(8) CHARACTER SET utf8 DEFAULT NULL COMMENT '销售不发短信',\n" +
        "  `BEGIN_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',\n" +
        "  `END_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',\n" +
        "  `DELETE_FLAG` varchar(16) DEFAULT 'NORMAL' COMMENT '删除标识',\n" +
        "  `CREATED_AT` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
        "  `CREATED_BY` varchar(32) DEFAULT '' COMMENT '创建人',\n" +
        "  `UPDATED_AT` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
        "  `UPDATED_BY` varchar(32) DEFAULT '' COMMENT '更新人',\n" +
        "  PRIMARY KEY (`ID`),\n" +
        "  KEY `MEMBER_ID_INDEX` (`MEMBER_ID`)\n" +
        ") ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COMMENT='告警商户白名单配置信息表';\n" +
        "\n";
        ;

        //删除第一行 create table
        if (sql.split("\n")[0].endsWith("(")) {
            sql = sql.replace(sql.split("\n")[0], "");
        }
    }

    public void read() {
        String point = "";
        initSql();
        String[] sqls = sql.split(",\n");
        int line = 0;
        for (String s : sqls) {
            //过滤key
            if (s.trim().startsWith("KEY") || s.trim().startsWith("PRIMARY KEY") || s.trim().startsWith("UNIQUE KEY") || s.trim().startsWith("FULLTEXT KEY")) {
                continue;
            }
            //属性
            TableAttribute t = new TableAttribute();
            t.name = s.split("`")[1];
            if (s.contains("COMMENT")) {
                t.desc = s.split("COMMENT")[1].split("'")[1];
            }
            t.type = s.trim().split(" ")[1];
            //处理比如decimal(16,空格6)的特殊情况
            if (t.type.endsWith(",")) {
                t.type += " " + s.trim().split(" ")[2];
            }
            if (s.contains("NOT NULL")) {
                t.nullable = "";
            } else {
                t.nullable = "是";
            }
            if (s.contains("DEFAULT")) {
                String s1 = s.split("DEFAULT")[1];
                if (s1.trim().startsWith("'")) {
                    t.defaultValue = s1.split("'")[1];
                } else {
                    t.defaultValue = s1.split(" ")[1];
                }
            } else {
                t.defaultValue = "";
            }
            t.print();
            line++;
        }
        System.out.println(line + "行");

    }

    public static void main(String[] args) {
        new ReadTableStructure().read();
    }


}

@Data
class TableAttribute {
    /**
     * 字段名
     */
    String name = "";
    /**
     * 字段说明
     */
    String desc = "";
    /**
     * 默认值
     */
    String defaultValue = "";
    /**
     * 类型
     */
    String type = "";
    /**
     * 是否为空
     */
    String nullable = "";

    public void print() {
        System.out.println(name + "\t" + desc + "\t" + defaultValue + "\t" + type + "\t" + nullable);
    }
}
