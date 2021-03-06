package com.oyr.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Create by 欧阳荣
 * 2018/12/16 15:39
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add() {
        return "add";
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    public String update() {
        return "update";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String delete() {
        return "delete";
    }
}
