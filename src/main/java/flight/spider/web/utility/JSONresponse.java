package flight.spider.web.utility;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import flight.spider.web.model.ReturnValue;

@Component
public class JSONresponse {

	public JSONresponse() {
		// TODO Auto-generated constructor stub
	}
	
	public void Print(HttpServletRequest request,HttpServletResponse response,String text) throws IOException {
		String callback = request.getParameter("callback");
		response.setContentType("text/json; charset=UTF-8");
		if (StringUtils.hasText(callback)) {
			if (text.startsWith("{")) {
				response.getWriter().print(callback + "(" + text + ")");
			} else {
				response.getWriter().print(
						callback + "(\"" + text.replace("\"", "\\\"") + "\")");
			}
		} else {
			response.getWriter().print(text);
		}
	}

	public void PrintError(HttpServletRequest request,HttpServletResponse response,String err) throws IOException {
		ReturnValue<String> retValue = new ReturnValue<String>();
		retValue.setStatus(false);
		retValue.setError(err);
		Print(request,response,retValue.toJson().toString());

	}

	public <T> void PrintData(HttpServletRequest request,HttpServletResponse response,T data) throws IOException {
		ReturnValue<T> retValue = new ReturnValue<T>();
		retValue.setStatus(true);
		retValue.setData(data);
		Print(request,response,retValue.toJson().toString());

	}

	public void PrintStatus(HttpServletRequest request,HttpServletResponse response,boolean status) throws IOException {
		ReturnValue<String> retValue = new ReturnValue<String>();
		retValue.setStatus(status);
		Print(request,response,retValue.toJson().toString());
	}
	
}
