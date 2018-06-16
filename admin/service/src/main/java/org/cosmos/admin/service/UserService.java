package org.cosmos.admin.service;

import org.cosmos.admin.domain.entity.User;
import org.cosmos.admin.mapper.UserQuery;

import java.util.List;

/**
 * 用户业务接口
 *
 * @author fisher
 * @date 2017-08-11
 */
public interface UserService {

    /**
     * 保存用户
     *
     * @param user 用户对象
     */
    void save(User user);

    /**
     * 查询用户列表
     *
     * @param userQuery 用户查询对象
     * @return 用户列表
     */
    List<User> findUserList(UserQuery userQuery);
}
