package core.common;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

/**
 * 该类采用了单例模式，用来管理系统的资源。
 *
 */
public class ResourceManager {
    private static ResourceManager rm;
    //要管理的资源
    private TemplateEngine engine;

    //对外界提供的获得资源的方法
    public TemplateEngine getEngine() {
        return engine;
    }
    private ResourceManager(ServletContext sctx){
        System.out.println("ResourceManager's constructor()");
        //在这儿创建资源(确保资源只创建一份)
        //创建解析器
        ServletContextTemplateResolver sctr =
                new ServletContextTemplateResolver(sctx);
        //给解析器设置一些特性
        sctr.setTemplateMode(TemplateMode.HTML);
        sctr.setPrefix("/WEB-INF/templates/");
        sctr.setSuffix(".html");
        sctr.setCharacterEncoding("utf-8");
        sctr.setCacheable(false);
        //创建模板引擎
        engine = new TemplateEngine();
        //给引擎设置相应的解析器
        engine.setTemplateResolver(sctr);
    }

    public synchronized static ResourceManager getInstance(ServletContext sctx){
        if(rm == null){
            rm = new ResourceManager(sctx);
        }
        return rm;
    }
}
