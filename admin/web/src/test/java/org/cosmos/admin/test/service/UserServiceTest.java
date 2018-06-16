package org.cosmos.admin.test.service;

import org.cosmos.admin.domain.entity.User;
import org.cosmos.admin.service.UserService;
import org.cosmos.admin.test.BaseSpringBootTest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 用户业务测试类
 *
 * @author fisher
 * @date 2018-01-19
 */
public class UserServiceTest extends BaseSpringBootTest {

    @Resource
    private UserService userService;

    @Test
    public void save(){
        User u = new User();
        u.setId(1);
        u.setName("码农");
        userService.save(u);
    }
}
