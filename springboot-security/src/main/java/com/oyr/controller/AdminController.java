package com.oyr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2018/12/21.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showAdmin(){
        return "admin";
    }

}
