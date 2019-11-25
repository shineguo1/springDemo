package gxj.study.kafka.pool.handle;

/**
 * <ul>
 * <li>业务处理接口类</li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */
public interface BizHandleInterface<O> {

    /**
     * 业务处理接口
     */
    public void doBiz(O obj);
}
