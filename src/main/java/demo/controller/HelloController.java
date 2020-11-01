package demo.controller;

import core.annotation.RequestMapping;
import core.common.ModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理器类：负责处理业务逻辑
 */
@RequestMapping("/demo")
public class HelloController {
    @RequestMapping("/hello.do")
    public String hello(ModelMap mm){
        System.out.println("HelloController's hello()");
        mm.addAttribute("msg","hello modelmap");
        //返回视图名
        return "hello";
    }




}

