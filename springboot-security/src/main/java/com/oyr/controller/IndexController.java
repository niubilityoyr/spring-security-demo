package com.oyr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author ouyang
 * @Date 2018/12/11 14:35
 * @Version 1.0
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndex(){
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showAdmin(){
        return "/security/login";
    }


    @RequestMapping(value = "/loginError", method = RequestMethod.GET)
    public String loginError(){
        return "/security/loginError";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String error403(){
        return "/security/403";
    }

    @RequestMapping(value = "/logoutSuccess", method = RequestMethod.GET)
    public String logoutSuccess(){
        return "/security/logoutSuccess";
    }

    @RequestMapping(value = "/session/invalid", method = RequestMethod.GET)
    public String sessionInvalid(){
        return "/security/sessionInvalid";
    }

}
