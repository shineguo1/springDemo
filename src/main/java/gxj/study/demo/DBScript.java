package gxj.study.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/31 11:34
 * @description
 */
public class DBScript {
    public static void main(String[] args) {
        String[] prefixes = getPrefixsForTest().split("\\n");
        Map<String, Integer> sequenceNos = new HashMap<>(prefixes.length);
        for (String prefix : prefixes) {
            sequenceNos.put(prefix, 0);
        }
        StringBuilder builder = new StringBuilder();
        sequenceNos.forEach(
                (key, num) -> builder.append("SET @" + key.replaceAll("-", "") + " = 0;\n")
        );
        builder.append("SELECT id, CONTRACT_NO,\n" +
                "case  CONCAT( \"XYKJ\", \"-\", SUBSTR(CREATED_AT, 3, 2), SUBSTR(CREATED_AT, 6, 2), \"-\" )\n");
        sequenceNos.forEach(
                (key, num) -> builder.append("when '" + key + "' then '" + getNewContractNo(key.replaceAll("-", "")) + "'\n")
        );
        builder.append("when 'XYKJ-1601-' then '233'\n");
        builder.append("end as NEW_CONTRACT_NO\n" +
                "FROM T_DATA_LEDGER_INFO \n" +
                "ORDER BY id ");
        System.out.println(builder.toString());
    }

    private static String getNewContractNo(String variableName) {
        return "CONCAT( \"XYKJ\", \"-\", SUBSTR(CREATED_AT, 3, 2), SUBSTR(CREATED_AT, 6, 2), \"-\" , RIGHT (CONCAT('0000', @" + variableName + " := @" + variableName + " + 1), 4))";
    }

    private static String getPrefixs() {
        return "XYKJ-1809-\n" +
                "XYKJ-1810-\n" +
                "XYKJ-1811-\n" +
                "XYKJ-1812-\n" +
                "XYKJ-1901-\n" +
                "XYKJ-1902-\n" +
                "XYKJ-1903-\n" +
                "XYKJ-1904-\n" +
                "XYKJ-1905-\n" +
                "XYKJ-1906-\n" +
                "XYKJ-1907-\n" +
                "XYKJ-1804-\n" +
                "XYKJ-1601-\n" +
                "XYKJ-1701-\n" +
                "XYKJ-1801-\n" +
                "XYKJ-1805-\n" +
                "XYKJ-1806-\n" +
                "XYKJ-1807-\n" +
                "XYKJ-1808-\n" +
                "XYKJ-1908-\n" +
                "XYKJ-1909-\n" +
                "XYKJ-1910-\n" +
                "XYKJ-1911-\n" +
                "XYKJ-1912-\n" +
                "XYKJ-2001-\n" +
                "XYKJ-2002-\n" +
                "XYKJ-2003-\n" +
                "XYKJ-2004-\n" +
                "XYKJ-2005-\n" +
                "XYKJ-2006-\n" +
                "XYKJ-2007-";
    }

    private static String getPrefixsForTest() {
        return "XYKJ-1601-\n" +
                "XYKJ-1701-\n" +
                "XYKJ-1801-\n" +
                "XYKJ-1805-\n" +
                "XYKJ-1806-\n" +
                "XYKJ-1904-\n" +
                "XYKJ-1910-\n";
    }
}
