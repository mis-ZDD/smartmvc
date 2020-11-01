package demo.controller;

import core.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

public class LoginController {
    @RequestMapping("/toLogin.do")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login.do")
    public String login(HttpServletRequest request){
        System.out.println("LoginController's login()");
        String username = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        System.out.println("username:" + username + " pwd:" + pwd);
        if("Sally".equals(username) && "1234".equals(pwd)){
            //登录成功,重定向到欢迎页面
            return "redirect:welcome.do";
        }else{
            //登录失败，转发到登录页面
            request.setAttribute("login_failed","用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("/welcome.do")
    public String welcome(){
        return "welcome";
    }
}
