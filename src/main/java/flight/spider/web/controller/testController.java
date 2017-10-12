package flight.spider.web.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import flight.spider.web.bean.user;
import flight.spider.web.service.userService;
import flight.spider.web.utility.JSONresponse;
import flight.spider.web.utility.securityUtil;

@Controller  
@RequestMapping("/theSpider")  
public class testController {  
	@Autowired 
	private JSONresponse jsonresponse;
	@Autowired 
	private userService userInfoService;
    @RequestMapping(value="/login",method=RequestMethod.POST)   
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,@RequestParam String username,
    		@RequestParam String password,@RequestParam String vcode,Model model) {
    	try {
	    	JSONObject obj = new JSONObject();
	    	HttpSession session = request.getSession();
	    	user userInfo=null;
	    	String encryptPwd = securityUtil.PasswordEncrypt(password);
	    	if(username == null || username.isEmpty())
	    		jsonresponse.PrintError(request, response, "用户名不能为空");
	    	else if(password == null || password.isEmpty())
	    		jsonresponse.PrintError(request, response, "密码不能为空");
	    	else if(!vcode.equalsIgnoreCase((String)session.getAttribute("validateCode"))){
	    		jsonresponse.PrintError(request, response, "验证码错误");
	    	}else {
	    		userInfo = userInfoService.getUserByUsername(username);
	    		if(userInfo==null){
	    	    		jsonresponse.PrintError(request, response, "用户名或密码错误");
	    		}else{
	    			if(userInfo.getPassword().equals(encryptPwd)){
	    				session.setAttribute("username", username);
			        	String redirectTo = (String) session.getAttribute("redirect");
			        	session.removeAttribute("redirect");
			        	if(redirectTo == null || redirectTo.length()==0)
			        		redirectTo="/";
							obj.put("redirectTo", redirectTo);
							jsonresponse.PrintData(request,response,obj);
	    			}else{
	    				jsonresponse.PrintError(request, response, "用户名或密码错误");
	    			}
	    		}
				
	    	}
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    @RequestMapping(value="/login",method=RequestMethod.GET)   
    public ModelAndView loginView(HttpServletRequest request,Model model) {
    	
    	HttpSession session = request.getSession();
    	if(session.getAttribute("username")!=null)
    		return new ModelAndView("redirect:/");
    	else
    		return new ModelAndView("index");
    }
} 