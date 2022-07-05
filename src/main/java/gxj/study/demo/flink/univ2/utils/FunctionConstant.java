package gxj.study.demo.flink.univ2.utils;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint24;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Collections;

/**
 * @author xinjie_guo
 * @date 2022/4/15 15:21
 */
public class FunctionConstant {

    public static final Function DECIMALS = new Function("decimals",
            Collections.emptyList(),
            Collections.singletonList(new TypeReference<Uint>() {
            }));

    public static final Function SYMBOL = new Function("symbol",
            Collections.emptyList(),
            Collections.singletonList(new TypeReference<Utf8String>() {
            }));

    public static final Function TOTAL_SUPPLY = new Function("totalSupply",
            Collections.emptyList(),
            Collections.singletonList(new TypeReference<Uint256>() {
            }));

    public static final Function FEE = new Function("fee",
            Collections.emptyList(),
            Collections.singletonList(new TypeReference<Uint24>() {
            }));

    public static final Function TOKEN0 = new Function("token0",
            Collections.emptyList(),
            Collections.singletonList(new TypeReference<Address>() {
            }));

    public static final Function TOKEN1 = new Function("token1",
            Collections.emptyList(),
            Collections.singletonList(new TypeReference<Address>() {
            }));

}
