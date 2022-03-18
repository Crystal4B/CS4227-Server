package dev.platinum.hotel.interceptors;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class idAuthenticationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//System.out.println("username: "+request.getParameter("username"));
		//System.out.println("password: "+request.getParameter("password"));


		/*StringBuilder builder = new StringBuilder();
		try (BufferedReader in = request.getReader()) {
			char[] buf = new char[4096];
			for (int len; (len = in.read(buf)) > 0; )
				builder.append(buf, 0, len);
		} catch (IllegalStateException e) {
		}

		Enumeration en = request.getParameterNames();
		String str = "";
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);
			str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue);
		}
		if (str.length() > 0)
			str = str.substring(1);

		String requestBody = builder.toString();
		System.out.println(requestBody);
		check = true;
		/*
		String str = "password: \\\""; //12 characters

		int start = requestBody.indexOf(str);
		int end = requestBody.indexOf("\\",start+12);
		System.out.println(requestBody.substring(start,end));

		/*System.out.println(request);
		System.out.println(response);
		System.out.println(handler);*/
		return true;//unless true stops execution on client
		//System.out.print(handler.id);*/
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		//perform operations before sending the response to the client
		//System.out.print("postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
		//perform operations after completing the request and response
		//System.out.print("afterCompletion");
	}
}