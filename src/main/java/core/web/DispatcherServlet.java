package core.web;

import core.common.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DispatcherServlet",urlPatterns = "*.do",
        loadOnStartup =1 )
public class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;
    private ThymeleafViewResolver thymeleafViewResolver;

    @Override
    /**
     * 1.读取配置文件中的处理器类名。
     * 2.将处理器实例化。
     * 3.将处理器实例交给映射处理器(HandlerMapping)来处理。
     */
    public void init() throws ServletException {
        System.out.println("DispatcherServlet's init()");
        SAXReader reader = new SAXReader();
        //读取配置文件名
        String configLocation = getInitParameter("configLocation");
        InputStream in = getClass().getClassLoader().
                getResourceAsStream(configLocation);
        try {
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();
            //beans用于存放处理器实例
            List beans = new ArrayList();
            for(Element ele : elements){
                //获得处理器类名
                String className = ele.attributeValue("class");
                System.out.println("className:" + className);
                //将处理器实例化
                Object obj = Class.forName(className).newInstance();
                beans.add(obj);
            }
            System.out.println("beans:" + beans);
            //将处理器实例交给映射处理器来处理
            handlerMapping = new HandlerMapping();
            handlerMapping.process(beans);
            //创建视图解析器实例
            thymeleafViewResolver = new ThymeleafViewResolver();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    protected void service(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("DispatcherServlet's service()");
        request.setCharacterEncoding("utf-8");
        //获得请求路径
        String path = request.getServletPath();
        System.out.println("path:" + path);
        //依据请求路径找到对应的处理器来处理
        Handler handler = handlerMapping.getHandler(path);
        System.out.println("handler:" + handler);
        if(handler == null){
            System.out.println("请求路径为:" + path + " 的请求没有对应的处理器");
            response.sendError(404);
            return;
        }
        //利用Handler来获得处理器及对应的Method对象
        Object obj = handler.getObj();
        Method mh = handler.getMh();
        //viewName是视图名
        String viewName = "";
        //ModelMap用于处理器向视图传递数据
        ModelMap mm = new ModelMap();
        try {
            //先获得处理器方法的参数类型信息
            Class[] types = mh.getParameterTypes();
            //rv是处理器方法的返回值
            Object rv = null;
            if(types.length == 0){
                //调用不带参的处理器方法
                rv = mh.invoke(obj);
            }else{
                //params用于存放实际参数值
                Object[] params = new Object[types.length];
                //依据参数类型进行相应的赋值
                for(int i = 0; i < types.length; i ++){
                    if(types[i] == HttpServletRequest.class){
                        params[i] = request;
                    }
                    if(types[i] == HttpServletResponse.class){
                        params[i] = response;
                    }
                    if(types[i] == ModelMap.class){
                        params[i] = mm;
                    }
                    //暂时只支持在处理器方法里面使用request,response,ModelMap
                }
                //调用带参的处理器方法
                rv = mh.invoke(obj,params);
            }
            //获得视图名
            viewName = rv.toString();
            System.out.println("viewName:" + viewName);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        //调用视图解析器来处理视图名
        thymeleafViewResolver.processViewName(viewName,mm,request,response);

    }


}
