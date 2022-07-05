package gxj.study.demo.flink.univ2.utils;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;

import java.util.List;
import java.util.Objects;


/**
 * @author xinjie_guo
 * @date 2022/4/15 15:31
 */
@Component
@Slf4j
public class Web3jUtil {

    private static Web3j web3j = Web3jConfig.build("https://eth-mainnet.alchemyapi.io/v2/j_TSwVEuEBo2Hw1qZ7f6WK8CauPDs2Hs");

    /**
     * 调用合约方法，返回结果是复杂结构体
     *
     * @param function        函数
     * @param contractAddress 合约地址
     * @param block           区块
     */
    public static List<Type> callReadMethod(Function function, String contractAddress, DefaultBlockParameter block) {
        EthCall response = internalCallMethod(function, null, contractAddress, block);
        return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
    }

    /**
     * 调用合约方法，返回对象是简单类型
     *
     * @param function        函数
     * @param contractAddress 合约地址
     * @param block           区块
     */
    public static Object quickCallReadMethod(Function function, String contractAddress, DefaultBlockParameter block) {
        List<Type> responseParams = callReadMethod(function, contractAddress, block);
        return CollectionUtils.isEmpty(responseParams) ? null : responseParams.get(0).getValue();
    }

    public static boolean containsMethod(Function function, String contractAddress, DefaultBlockParameter block) {
        EthCall response = internalCallMethod(function, contractAddress, contractAddress, block);
        List<Type> decode = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        //合约有方法，调用成功
        boolean successResult = Objects.nonNull(response.getResult()) && CollectionUtils.isNotEmpty(decode);
        //合约有方法，方法代码内部报错
        boolean errorResult = Objects.nonNull(response.getError()) && Objects.nonNull(response.getError().getData());
        return successResult || errorResult;
    }


    private static EthCall internalCallMethod(Function function, String from, String to, DefaultBlockParameter block) {
        // encode the function
        String encodedFunction = FunctionEncoder.encode(function);

        /*
         * String from null(optional)
         * String to 合约地址
         * String data ABI
         */
        EthCall response;
        try {
            response = web3j.ethCall(Transaction.createEthCallTransaction(from, to, encodedFunction), block).send();
        } catch (Exception e) {
            log.error("合约调用异常，cause:{}", Throwables.getStackTraceAsString(e));
            throw new RuntimeException("合约调用异常");
        }
        return response;
    }
}
