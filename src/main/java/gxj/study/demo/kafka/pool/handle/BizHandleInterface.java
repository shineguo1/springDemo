package gxj.study.demo.kafka.pool.handle;

/**
 * <ul>
 * <li>业务处理接口类</li>
 * </ul>
 */
public interface BizHandleInterface<O> {

    /**
     * 业务处理接口
     */
    public void doBiz(O obj);
}
