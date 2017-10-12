package flight.spider.web.interceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class loginInterceptor implements HandlerInterceptor{
	/** 
     * Handler执行完成之后调用这个方法 
     */  
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception exc)  
            throws Exception {  
          
    }  
  
    /** 
     * Handler执行之后，ModelAndView返回之前调用这个方法 
     */  
    public void postHandle(HttpServletRequest request, HttpServletResponse response,  
            Object handler, ModelAndView modelAndView) throws Exception {  
    }  
  
    /** 
     * Handler执行之前调用这个方法 
     */  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,  
            Object handler) throws Exception {  
        //获取请求的URL  
        String url = request.getRequestURI();
        //URL:login.jsp是公开的;这个demo是除了login.jsp是可以公开访问的，其它的URL都进行拦截控制  
        //获取Session  
        HttpSession session = request.getSession();  
        String username = (String)session.getAttribute("username");  
        if(username != null){  
            return true;  
        }
        
        if (request.getHeader("x-requested-with")!= null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){//如果是ajax请求响应头会有x-requested-with 
            ServletOutputStream out = response.getOutputStream();
            out.print("unlogin");//返回给前端页面的未登陆标识
            out.flush();
            out.close();
            return false;
        }
        if(session.getAttribute("redirect") == null)
        	session.setAttribute("redirect", url);
        //不符合条件的，跳转到登录界面  
        response.sendRedirect("/theSpider/login");
        return false;  
    }  
}
