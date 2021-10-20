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
"CREATE TABLE `T_COST_TRADE_PAYMENT` (\n" +
        "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
        "  `PAY_REQ_NO` varchar(32) DEFAULT NULL COMMENT '支付业务单号',\n" +
        "  `BATCH_NO` varchar(32) DEFAULT NULL COMMENT '转账批次号',\n" +
        "  `RELATION_NO` varchar(32) DEFAULT NULL COMMENT '关联号',\n" +
        "  `PAY_REQ_DATE` timestamp NULL DEFAULT NULL COMMENT '支付请求时间',\n" +
        "  `TRADE_TYPE` varchar(16) DEFAULT NULL COMMENT '转账类型',\n" +
        "  `CCY` varchar(16) DEFAULT NULL COMMENT '币种',\n" +
        "  `AMT` int(9) DEFAULT NULL COMMENT '转账金额',\n" +
        "  `STATUS` varchar(16) DEFAULT NULL COMMENT '状态',\n" +
        "  `EVENT_NO` varchar(32) DEFAULT NULL COMMENT '会计事件',\n" +
        "  `RESP_DATE` datetime DEFAULT NULL COMMENT '交易返回时间',\n" +
        "  `RESP_NO` varchar(32) DEFAULT NULL COMMENT '交易返回订单号',\n" +
        "  `ERROR_CODE` varchar(32) DEFAULT NULL COMMENT '错误码',\n" +
        "  `ERROR_MSG` varchar(128) DEFAULT NULL COMMENT '错误信息',\n" +
        "  `USABLE_FLAG` varchar(16) DEFAULT NULL COMMENT '可用标识',\n" +
        "  `DESCRIPTION` varchar(128) DEFAULT NULL COMMENT '描述',\n" +
        "  `CREATED_AT` timestamp NOT NULL COMMENT '建立日期',\n" +
        "  `CREATED_BY` varchar(32) NOT NULL COMMENT '创建用户',\n" +
        "  `UPDATED_AT` timestamp NOT NULL COMMENT '最后更新时间',\n" +
        "  `UPDATED_BY` varchar(32) NOT NULL COMMENT '最后更新用户',\n" +
        "  `BIZ_TYPE` varchar(32) DEFAULT NULL COMMENT '产品大类',\n" +
        "  `SUB_BIZ_TYPE` varchar(32) DEFAULT NULL COMMENT '产品细类',\n" +
        "  PRIMARY KEY (`ID`),\n" +
        "  KEY `INX_BATCH_NO` (`BATCH_NO`) USING BTREE,\n" +
        "  KEY `INX_CREATED_AT` (`CREATED_AT`),\n" +
        "  KEY `IDX_PAY_REQ_NO` (`PAY_REQ_NO`) USING BTREE\n" +
        ") ENGINE=InnoDB AUTO_INCREMENT=377218 DEFAULT CHARSET=utf8mb4 COMMENT='成本支付信息表';\n" +
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
