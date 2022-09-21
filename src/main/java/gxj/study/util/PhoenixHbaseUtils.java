package gxj.study.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/13 17:02
 */
public class PhoenixHbaseUtils {

    static {
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(queryTokenPrice("0x2260fac5e5542a773aa44fbcfedf7c193bc2c599", 15031463L));
        System.out.println(queryTokenPrice("0x2260fac5e5542a773aa44fbcfedf7c193bc2c599", 15021463L));
        System.out.println(queryTokenPrice("0x2260fac5e5542a773aa44fbcfedf7c193bc2c599", 12031463L));
    }


    public static BigDecimal queryTokenPrice(String tokenAddress, Long blockNo) throws SQLException {
        Connection connection = doConnection();
        String sql = "select \"derived_anchor\"  from  blockin_uniswap.t_base_tokenprice \n" +
                "where \"token_address\"  = '" + tokenAddress + "' \n" +
                "and to_number(\"current_block\") <= " + blockNo + " \n" +
                "order by to_number(\"current_block\") DESC\n" +
                "limit 1";

        long start = System.currentTimeMillis();
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        ResultSet rs = prepareStatement.executeQuery();
        System.out.println("use time:" + (System.currentTimeMillis() - start));
        BigDecimal price = null;
        if (rs.next()) {
            String string = rs.getString(1);
            if (StringUtils.isNotEmpty(string)) {
                price = new BigDecimal(string);
            }
        }
        rs.close();
        prepareStatement.close();
        connection.close();
        return price;
    }

    private static Connection doConnection() throws SQLException {
        //这里配置zookeeper地址，可单个，也可多个，可以是域名或者ip
        String url = "jdbc:phoenix:172.20.192.236:2181";
        Properties props = new Properties();
        props.put("phoenix.schema.isNamespaceMappingEnabled", "true");
        props.put("phoenix.schema.mapSystemTablesToNamespace", "true");
        return DriverManager.getConnection(url, props);
    }
}
