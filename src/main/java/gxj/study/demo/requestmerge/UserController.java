package gxj.study.demo.requestmerge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/3 11:04
 * @description
 */
@Controller
public class UserController {
    @Autowired
    private UserServiceImpl userService = new UserServiceImpl();

    private LinkedBlockingDeque<MergeReq<User>> taskQue = new LinkedBlockingDeque<>();

    @PostConstruct
    public void initSchedule() {
        ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledExecutor.scheduleWithFixedDelay(new SubmitTask(), 1000, 10, TimeUnit.MILLISECONDS);
    }

    public User doQuery(int id) {
//        System.out.println("query id:" + id);
        MergeReq<User> req = new MergeReq<>();
        User user = new User();
        user.setId(id);
        req.setData(user);
        req.setMethod("query");
        req.setPrimaryKey(user.getId());
        CompletableFuture<User> future = new CompletableFuture<>();
        req.setFuture(future);
        //上锁是为了保证在定时任务批量处理请求时，暂停往队列中加入请求（等待锁释放）
        synchronized (taskQue) {
            taskQue.add(req);
        }
        //利用future.join的特性，在此阻塞，直到future拿到值才会返回。
        return future.join();

    }

    private class SubmitTask implements Runnable {

        @Override
        public void run() {
            //按方法分类 <方法名，合并的请求集合>
            Map<String, List<MergeReq>> reqs = new HashMap<>(8);
            /*
            *上锁是因为保证执行while循环时，taskQue不会被改变(不会加进新的数据）；
            * 若不加sync锁，两次poll操作之间时，taskQue的ReentrantLock是释放的,此时允许新的请求加入队列，可能会导致队列无限扩展处理不完。
            * 可以看见，加锁时taskQue.size和批量处理条数(reqs)是一致的，不加锁时taskQue.size小于批量处理条数(demo中只有一个query请求，所以两次打印的值应当是完全相同的）
            */
            synchronized (taskQue) {
                if (taskQue.size() != 0) {
                    System.out.println("taskQue.size:" + taskQue.size() + "条");
                }
                //把队列里的请求按方法名分类，加入reqs
                while (!taskQue.isEmpty()) {
                    MergeReq<User> el = taskQue.poll();
                    String method = el.getMethod();
                    if (!reqs.containsKey(method)) {
                        reqs.put(method, new ArrayList<>());
                    }
                    reqs.get(method).add(el);
                }
            }
            //查询结果集，按方法分类 <方法名，批量查询结果集Map>
            Map<String, Map> results = new HashMap<>(8);
            //根据方法名依次执行（合并请求）批量查询
            reqs.forEach((method, list) -> {
                //Map<单条查询条件唯一索引，值>
                Result<Map> result = userService.doMergeReq(method, list);
                results.put(method, result.getData());
            });
            //执行future.complete把查询结果写回对应的请求。
            reqs.forEach((method, list) -> {
                //Map<单条查询条件唯一索引，值>
                Map resultMap = results.get(method);
                list.forEach(req -> {
                    CompletableFuture future = req.getFuture();
                    //req.primaryKey应当与serviceImpl返回的<单条查询条件唯一索引>一致
                    future.complete(resultMap.get(req.getPrimaryKey()));
                });
            });
        }
    }
}
