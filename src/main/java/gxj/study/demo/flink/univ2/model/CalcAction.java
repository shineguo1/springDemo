package gxj.study.demo.flink.univ2.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 17:31
 */
@Data
public class CalcAction {

    /**
     * 所属链ID
     */
    private String chainId;
    /**
     * 项目协议
     */
    private String project;
    /**
     * 池子地址
     */
    private String poolAddress;
    /**
     * 上链Gas费
     */
    private BigDecimal gas;
    /**
     * 区块时间
     */
    private Long currentTimestamp;
    /**
     * 区块号
     */
    private Long blockNo;
    /**
     * 交易哈希值
     */
    private String txHash;
    /**
     * 事务索引
     */
    private Integer transactionIndex;
    /**
     * 交易序号
     */
    private Long nonce;
    /**
     * 事件类型
     */
    private String actionType;
    /**
     * 日志索引
     */
    private Integer logIndex;
    /**
     * 交易发送者,from
     */
    private String sender;
    /**
     * 交易拥有者,to
     */
    private String owner;
    /**
     * token0的地址
     */
    private String token0Address;
    /**
     * token1的地址
     */
    private String token1Address;
    /**
     * token0精度
     */
    private Integer decimals0;
    /**
     * token1精度
     */
    private Integer decimals1;
    /**
     * token0数量
     */
    private BigDecimal amount0;
    /**
     * token1数量
     */
    private BigDecimal amount1;
    /**
     * 铸造/销毁的流动性代币数量}
     */
    private BigDecimal liquidity;

    public CalcAction(Pair pair) {
        this.token0Address = pair.getToken0();
        this.token1Address = pair.getToken1();
        this.decimals0 = pair.getDecimals0();
        this.decimals1 = pair.getDecimals1();
    }

    public void setCommonFields(EventData eventData) {
        if (Objects.isNull(eventData) || Objects.isNull(eventData.getData())) {
            return;
        }
        JSONObject data = eventData.getData();
        this.setChainId(data.getString("chain"));
        this.setProject(data.getString("project"));
        this.setPoolAddress(data.getString("pairAddress"));
        this.setGas(data.getBigDecimal("gas").divide(new BigDecimal(Math.pow(10, 18)), 18, RoundingMode.HALF_UP));
        this.setCurrentTimestamp(data.getLong("currentTimestamp") * 1000);
        this.setBlockNo(data.getLong("blockNo"));
        this.setTxHash(data.getString("txHash"));
        this.setTransactionIndex(data.getInteger("transactionIndex"));
        this.setNonce(data.getLong("nonce"));


    }
}