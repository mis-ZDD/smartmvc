package core.common;

import core.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射处理器类：
 *  负责提供请求路径与处理器及方法的对应关系，比如
 *  请求路径为"/hello.do",则该请求应该由HelloController的hello方法来处理。
 */
public class HandlerMapping {
    /*
        map用于存放请求路径与处理器及方法的对应关系。
        Handler封装了处理器及方法。
     */
    private Map<String,Handler> map = new HashMap<>();

    /**
     * 依据请求路径返回Handler。
     */
    public Handler getHandler(String path){
        return map.get(path);
    }

    /**
     * 负责建立请求路径与处理器及方法的对应关系
     * @param beans 处理器实例组成的集合
     */
    public void process(List beans) {
        System.out.println("HandlerMapping's process()");
        for(Object obj : beans){
            //获得加在前面的注解@RequestMapping
            RequestMapping rm2 = obj.getClass().getAnnotation(RequestMapping.class);
            //判断类前面有没有加这个requestMapping
            String path2 = "";
            if (rm2!=null){
                //获得加在类前面的请求路径
                path2 = rm2.value();
            }
            //获得处理器的所有方法
            Method[] methods = obj.getClass().getDeclaredMethods();
            for(Method mh : methods){
                //获得加在处理器方法前的@RequestMapping注解
               RequestMapping rm =  mh.getAnnotation(RequestMapping.class);
               //允许处理器方法前不加@RequestMapping注解，所以，有可能获得null。
               if(rm != null){
                   //获得请求路径
                   String path = rm.value();
                   System.out.println("path:" + path);
                   //将处理器及其对应的方法对象封装到Handler
                   Handler handler = new Handler();
                   handler.setMh(mh);
                   handler.setObj(obj);
                   //将请求路径与处理器及方法的对应关系存放到map里面
                   map.put(path2+path,handler);
               }
            }
        }
        System.out.println("map:" + map);
    }
}

