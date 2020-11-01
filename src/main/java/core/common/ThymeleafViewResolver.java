package core.common;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 视图解析器：
 *    采用thymeleaf技术处理视图名
 */
public class ThymeleafViewResolver {

    /**
     * 处理视图名的方法
     */
    public void processViewName(String viewName,ModelMap mm,
                                HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {
       /*
            处理视图名，如果视图名是以"redirect:"开头，则重定向，
            否则转发。
         */
        if(viewName.startsWith("redirect:")){
            //生成重定向地址
            String redirectPath = request.getContextPath() + "/" +
                    viewName.substring("redirect:".length());
            //重定向
            response.sendRedirect(redirectPath);
        }else{
            //通过ResourceManager来获得TemplateEngine
            TemplateEngine engine =
                    ResourceManager.getInstance(request.getServletContext()).getEngine();
            //创建web上下文（提供数据）
            WebContext ctx = new WebContext(
                    request,response,request.getServletContext());
            //将ModelMap上绑订的数据取出来，放到ctx里
            for(Map.Entry<String,Object> entry : mm.getMap().entrySet()){
                String name = entry.getKey();
                Object value = entry.getValue();
                ctx.setVariable(name,value);
            }
            //设置MIME类型
            response.setContentType("text/html;charset=utf-8");
            //调用引擎的方法来处理模板
            engine.process(viewName,ctx,response.getWriter());
        }
    }
}
