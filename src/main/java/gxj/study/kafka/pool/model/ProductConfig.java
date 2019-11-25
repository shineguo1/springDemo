package gxj.study.kafka.pool.model;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * <ul>
 * <li>生产者配置类</li>
 * <li>added:新增线程池功能</li>
 * <li>User: weiwei Date:16/5/11 <li>
 * <li>updated: weiwei Date:16/5/24 <li>
 * </ul>
 */
public class ProductConfig extends GenericObjectPoolConfig {

    public ProductConfig() {
        // defaults to make your life with connection pool easier :)
        setMinIdle(10);
        setMaxIdle(30);
        setMaxTotal(100);
    }

    /**
     * 主机/端口列表,用于建立kafka集群的初始连接,动态发现集群中所有broker地址.
     * 格式: host1:port1,host2:port2,....
     */
    String bootstrapServers;

    /**
     * ACK = 0，   如果设置为零，那么生产者不会等待来自服务器的任何确认。该消息将被立即添加到套接字缓冲区，并认为发送。
     * ACK = 1     这将意味着leader写入确认，但不包含副本的确认。
     * ACK= all    leader和副本节点的确认。
     */
    String acks = "1";

    /**
     * 如果请求失败，生产者会自动重试，我们指定是0次，如果启用多次，则会有重复消息的可能性。
     */
    String retries = "0";

    /**
     * 生产者保存每个分区未发送消息的缓冲。这些缓冲的大小是通过 batch.size 配置指定的。
     * 值较大的话将会产生更多的批。但是需要更多的内存（通常每个活动分区都有缓冲区）。
     */
    String batchSize = "16384";

    /**
     * 默认缓冲可以立即发送，即使有额外未使用的缓冲空间，但是，如果你想减少请求的数量，
     * 可以设置 linger.ms 大于0。这将指示生产者发送请求之前等待一段时间，希望更多的消息填补到同一个批次。
     * 这类似于TCP的算法，例如上面的代码段，可能100条消息在一个请求发送，
     * 因为我们设置了linger(逗留)时间为1毫秒，然后，如果我们没有填满缓冲区，
     * 这个设置将增加1毫秒的延迟请求来等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，
     * 不管 linger.ms=0，然后，设置比0大，将会有更少的，更有效的请求，在最大负荷时少量的延迟的成本。
     */
    String lingerMs = "1";

    /**
     * 控制生产者可用内存缓冲的总量，如果消息发送速度比他们快可以传输到服务器的快，
     * 将会耗尽这个缓冲区空间。当缓冲区空间耗尽，其他发送调用将被阻塞，如果不想任何阻塞，
     * 你可以设置block.on.buffer.full=false，但是这将会导致发送调用异常。
     */
    String bufferMemory = "33554432";

    /**
     * 消息传输已key:value键值对的格式进行传输
     * <p>
     * key序列化格式类型
     */
    String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * 消息传输已key:value键值对的格式进行传输
     * <p>
     * Value序列化格式类型
     */
    String valueSerializer = "gxj.study.kafka.pool.serializer.MessageJsonSerializer";


    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(String lingerMs) {
        this.lingerMs = lingerMs;
    }

    public String getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(String bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }
}
