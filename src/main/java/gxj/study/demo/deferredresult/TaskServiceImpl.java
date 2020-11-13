package gxj.study.demo.deferredresult;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/9/14 13:58
 * @description
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Override
    public String execute() {
        try {
            Thread.sleep(5000);
            log.info("Slow task executed");
            return "Task finished";
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}