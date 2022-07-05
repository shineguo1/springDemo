package gxj.study.demo.flink.univ2.utils;

import com.google.common.collect.Lists;
import lombok.Data;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.utils.Numeric;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/30 18:21
 */
public class BatchUtils {

    //eth地址
    private final static String MULTICONTRACT = "0x5BA1e12693Dc8F9c48aAD8770482f4739bEeD696";
    private final static String AGGREGATE = "aggregate";

    public static List<Object>  batch(List<BatchReq> reqList, DefaultBlockParameter block) {

        List<ContractPayLoad> contractCallInfos = reqList.stream()
                .map(o->new ContractPayLoad(new Address(o.getAddress()), FunctionEncoder.encode(o.getFunction())))
                .collect(Collectors.toList());

        List<Type> inputParams = Collections.singletonList(new DynamicArray(ContractPayLoad.class, contractCallInfos));


        List<TypeReference<?>> outputParams = Arrays.asList(new TypeReference<Uint256>() {
        }, new TypeReference<DynamicArray<DynamicBytes>>() {
        });

        Function aggregateFunction = new Function(AGGREGATE, inputParams, outputParams);
        List<Type> results = Web3jUtil.callReadMethod(aggregateFunction, MULTICONTRACT, block);

        List<Object> ret = Lists.newArrayList();
        if (results != null && results.size() > 0) {
            List<DynamicBytes> bytes = (List<DynamicBytes>) results.get(1).getValue();
            for (int i = 0; i < reqList.size(); i++) {
                Function function = reqList.get(i).getFunction();
                Type functionResult = FunctionReturnDecoder.decode(Numeric.toHexString(bytes.get(i).getValue()),
                        function.getOutputParameters()).get(0);
//                System.out.println(function.getName() + " : " + functionResult.getValue());
                ret.add(functionResult.getValue());
            }
        }
        return ret;
    }


    @Data
    private static class ContractPayLoad extends DynamicStruct {

        private Address target;

        private String callData;

        ContractPayLoad(Address target, String callData) {
            super(target, new DynamicBytes(Numeric.hexStringToByteArray(callData)));
            this.target = target;
            this.callData = callData;
        }
    }

}