package gxj.study.demo.flink.univ2.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/5 10:01
 */
public class BlockChainUtils {

    private static Pattern ZERO_ADDRESS = Pattern.compile("(0x)?0*");

    public static boolean isAddressEmpty(String address){
        return StringUtils.isEmpty(address) || ZERO_ADDRESS.matcher(address).matches();
    }

}
