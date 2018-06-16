package org.cosmos.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 登录控制器
 *
 * @author fisher
 * @date 2017-12-27
 */
@Controller
public class LoginController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home() {

        return "/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {

        return "/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin() {

        return "/login";
    }
}
