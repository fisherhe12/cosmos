package org.cosmos.admin.controller;

import org.cosmos.admin.domain.entity.User;
import org.cosmos.admin.mapper.UserQuery;
import org.cosmos.admin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * org.cosmos.admin.controller
 *
 * @author fisher
 * @date 2017-12-28
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = {"list", ""})
    public String list(@ModelAttribute(value = "q") UserQuery userQuery, Model model) {
        List<User> userList = userService.findUserList(userQuery);
        model.addAttribute("userList", userList);
        return "/user";
    }

}
