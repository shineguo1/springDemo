package gxj.study.mapper;

import gxj.study.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/3/4 12:17
 * @description
 */
@Mapper
public interface UserMapper {

    int insert(String name);

    UserDTO select(String id);

}
