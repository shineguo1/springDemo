package gxj.study.mapper;

import gxj.study.BaseTest;
import gxj.study.model.UserDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2020/3/4.
 */
public class UserMapperTest extends BaseTest {
    @Autowired
    UserMapper mapper;

    @Test
    public void select() throws Exception {
        UserDTO user = mapper.select("1");
        System.out.println(user);

    }

}