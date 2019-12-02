package gxj.study.demo.kafka.pool.model;

/**
 * <ul>
 * <li>消费者配置类</li>
 * </ul>
 */
public class ConsumerConfig {

    /**
     * 主机/端口列表,用于建立kafka集群的初始连接,动态发现集群中所有broker地址.
     * 格式: host1:port1,host2:port2,....
     */
    private String bootstrapServers;

    /**
     * 队列和发布-订阅式。 队列的处理方式是 一组消费者从服务器读取消息，一条消息只有其中的一个消费者来处理。
     * 在发布-订阅模型中，消息被广播给所有的消费者，接收到消息的消费者都可以处理此消息。
     * Kafka为这两种模型提供了单一的消费者抽象模型： 消费者组 （consumer group）。
     * 消费者用一个消费者组名标记自己。 一个发布在Topic上消息被分发给此消费者组中的一个消费者。
     * 假如所有的消费者都在一个组中，那么这就变成了queue模型。
     * 假如所有的消费者都在不同的组中，那么就完全变成了发布-订阅模型。
     */
    private String groupId;

    /**
     * 如果为true消费者的偏移量会在后台定期提交。
     */
    private String enableAutoCommit = "false";

    /**
     * 消费者offset提交到zookeeper的频率（毫秒）。
     */
    private String autoCommitIntervalMs;

    /**
     * broker使用心跳机制自动检测测试组中失败的进程。消费者定期会自动ping集群，
     * 让集群知道它是活着的，如果消费者在一段时间内停止心跳时间超过session.timeout.ms，
     * 那么将被视为死亡，其分区被分配到另一个进程。
     */
    private String sessionTimeoutMs;

    /**
     * 消息传输已key:value键值对的格式进行传输
     * <p>
     * 解析key序列化格式类型
     */
    private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

    /**
     * 消息传输key:value键值对的格式进行传输
     * <p>
     * 解析Value序列化格式类型
     */
    private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(String enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public String getAutoCommitIntervalMs() {
        return autoCommitIntervalMs;
    }

    public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }

    public String getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(String sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

}
