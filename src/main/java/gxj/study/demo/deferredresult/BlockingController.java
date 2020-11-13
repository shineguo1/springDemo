package gxj.study.demo.deferredresult;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/9/14 14:00
 * @description
 */
@RestController
@Slf4j
public class BlockingController {
    private final TaskService taskService;

    @Autowired
    public BlockingController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     *  execute阻塞，顺序执行
     * @return
     */
    @RequestMapping(value = "/block", method = RequestMethod.GET, produces = "text/html")
    public String executeSlowTask() {
        log.info("Request received");
        String result = taskService.execute();
        log.info("Servlet thread released");

        return result;
    }

    /**
     * execute并发执行，不阻塞后续代码，等异步线程执行完成后，前端（postman）收到请求结果
     * @return
     */
    @RequestMapping(value = "/callable", method = RequestMethod.GET, produces = "text/html")
    public Callable<String> executeCallableTask() {
        log.info("Request received");
        Callable<String> callable = taskService::execute;
        log.info("Servlet thread released");
        log.info(JSON.toJSONString(callable));

        return callable;
    }

    /**
     * 同callable，但更自由，能制定返回结果和超时策略
     * @return
     */
    @RequestMapping(value = "/deferred", method = RequestMethod.GET, produces = "text/html")
    public DeferredResult<String> executeDeferredTask() {
        log.info("Request received");
        DeferredResult<String> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(taskService::execute)
                .whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        log.info("Servlet thread released");
        log.info(JSON.toJSONString(deferredResult));

        return deferredResult;
    }
}