package gxj.study.demo.requestmerge;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/3 14:25
 * @description
 */
@Service
public class UserServiceImpl {

    /*
    * 模拟查询结果
     */
    public List<User> queryAll(List<Integer> ids){
        System.out.println("批量查询:"+ids.size()+"条");
        List<User> results = new ArrayList<>();
        //mock results
        for (int i=1;i<=5;i++){
            User user = new User();
            user.setId(i);
            user.setName("user"+i);
            results.add(user);
        }
        return results;
    }

    /*
     * 以下为请求合并相关
     */

    //全策略缓存
    private Map<String,BaseMergeReqStrategy> mergeReqStrategyCache = new HashMap<>();
    //缓存初始化
    {
        mergeReqStrategyCache.put("query",new Query());
    }

    /*
    * 合并的请求统一入口
    * 合并请求应该是把多次单一查询合并为一次批量查询，所以应当返回list
     */
    public Result<Map> doMergeReq(String key, List<MergeReq> list){
        System.out.println("doQuery:"+key+list.toString());
        BaseMergeReqStrategy m = mergeReqStrategyCache.get(key);
//        System.out.println("doQuery:"+m);
        return m.doReq(list);
    }

    abstract class BaseMergeReqStrategy {
        Result<Map> doReq(List<MergeReq> list){
            return null;
        }

    }

    private class Query extends BaseMergeReqStrategy {
        @Override
        Result<Map> doReq( List<MergeReq> list){
            List<Integer> ids = new ArrayList<>();
            list.forEach(mergeReq -> ids.add(((User) mergeReq.getData()).getId()));
            List<User> users = queryAll(ids);
            Map<Integer,User> map = new HashMap<>(8);
            users.forEach(user->map.put(user.getId(),user));
            Result<Map> result = new Result<>();
            result.setData(map);
            return result;
        }
    }
}
