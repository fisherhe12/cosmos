package org.cosmos.admin.test.dao;

import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import org.cosmos.admin.domain.entity.User;
import org.cosmos.admin.mapper.UserMapper;
import org.cosmos.admin.mapper.UserQuery;
import org.cosmos.admin.test.BaseSpringBootTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 * @author fisher
 * @date 2017-08-11
 */
public class UserMapperTest extends BaseSpringBootTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void insert() throws Exception {
        User user = new User();
        user.setAccount("fisher");
        user.setName("fisher");
        user.setEmail("fisherhe@sina.com");
        user.setGender("男");
        user.setPassword("880212");
        userMapper.insert(user);


    }

    @Test
    public void query() {
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void update() {
        User u = new User();
        u.setId(1);
        u.setName("渔夫");
        userMapper.updateById(u);
    }

    @Test
    public void findByPage() {
        PageHelper.startPage(1,2);
        UserQuery userQuery=new UserQuery();
        userQuery.setName("渔夫");
        List<User> users = userMapper.selectUserList(userQuery);
        System.out.println(PageHelper.getTotal());
        System.out.println(PageHelper.freeTotal());
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void getByName() {
        User example = new User();
        example.setActive(true);
        User user = userMapper.selectOne(example);
        System.out.println(user);
    }

    @Test
    public void deleteLogic(){
        userMapper.deleteById(1);
    }
    @Test
    public void searchByCondition(){

    }
}
