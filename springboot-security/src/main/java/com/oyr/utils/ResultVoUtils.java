package com.oyr.utils;

import com.oyr.vo.ResultVo;

/**
 * ResultVo工具类
 */
public class ResultVoUtils {

    // 成功，不带参
    public static ResultVo ok() {
        Object object = null;
        return ok(object);
    }

    // 成功，带参数
    public static ResultVo ok(Object data) {
        return ok("success", data);
    }

    // 成功，带参数
    public static ResultVo ok(String msg) {
        return ok(msg, null);
    }

    // 成功，带参数
    public static ResultVo ok(String msg, Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode("200");
        resultVo.setMsg(msg);
        resultVo.setData(data);
        return resultVo;
    }

    // error
    public static ResultVo error(String code, String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        return resultVo;
    }

    // error
    public static ResultVo error(String code, String msg, Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        resultVo.setData(data);
        return resultVo;
    }
}