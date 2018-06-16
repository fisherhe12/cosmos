package org.cosmos.admin.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.cosmos.admin.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 系统用户Mapper
 *
 * @author fisher
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 条件查询用户列表
     *
     * @param userQuery 用户查询对象
     * @return 用户列表
     */
    List<User> selectUserList( UserQuery userQuery);


}
