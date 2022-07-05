package gxj.study.demo.flink.univ2.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.web3j.abi.datatypes.Function;

/**
 * call info
 *
 * @author xin deng  Date: 2022/4/18 ProjectName: mdps Version: 1.0
 */
@Data
@AllArgsConstructor
public class BatchReq {

    private String address;

    private Function function;

}
