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
   ""
                ;

        //删除第一行 create table
        if(sql.split("\n")[0].endsWith("(")){
            sql = sql.replace(sql.split("\n")[0],"");
        }
    }

    public void read() {
        String point = "";
        initSql();
        String[] sqls = sql.split(",\n");
        int line = 0;
        for (String s : sqls) {
            //过滤key
            if (s.trim().split(" ")[0].equals("KEY") || s.trim().split(" ")[0].equals("PRIMARY")) {
                continue;
            }
            //属性
            TableAttribute t = new TableAttribute();
            t.name = s.split("`")[1];
            if(s.contains("COMMENT")) {
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
                if(s1.trim().startsWith("'")){
                    t.defaultValue = s1.split("'")[1];
                }else {
                    t.defaultValue = s1.split(" ")[1];
                }
            } else {
                t.defaultValue = "";
            }
            t.print();
            line ++;
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
    String name="";
    /**
     * 字段说明
     */
    String desc="";
    /**
     * 默认值
     */
    String defaultValue="";
    /**
     * 类型
     */
    String type="";
    /**
     * 是否为空
     */
    String nullable="";

    public void print() {
        System.out.println(name + "\t" + desc + "\t" + defaultValue + "\t" + type + "\t" + nullable);
    }
}
