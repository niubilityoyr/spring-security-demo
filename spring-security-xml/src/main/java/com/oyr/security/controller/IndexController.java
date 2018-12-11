package com.oyr.security.controller;

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

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String showAdmin(){
        return "admin";
    }

}
