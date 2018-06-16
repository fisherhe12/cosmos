package org.cosmos.admin.service.impl;

import org.cosmos.admin.domain.entity.User;
import org.cosmos.admin.mapper.UserMapper;
import org.cosmos.admin.mapper.UserQuery;
import org.cosmos.admin.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fisher
 * @date 2017-08-11
 */
@Service
@Transactional(rollbackFor = DataAccessException.class)
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void save(User user) {
        userMapper.insert(user);
    }

    @Override
    public List<User> findUserList(UserQuery userQuery) {
        return userMapper.selectUserList(userQuery);
    }

}
