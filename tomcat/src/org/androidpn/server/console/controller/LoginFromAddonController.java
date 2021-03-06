package org.androidpn.server.console.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.util.ApnUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class LoginFromAddonController extends MultiActionController {
	private UserService userService;

	protected final Log logger = LogFactory.getLog(getClass());
	
	public LoginFromAddonController(){
		userService = ServiceLocator.getUserService();
	}
	
	public String send(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String results = "";
        String email = ServletRequestUtils.getStringParameter(request,"email");
        String password = ApnUtil.unescape(ServletRequestUtils.getStringParameter(request, "password"));
        User user = userService.getUserByEmail(email);
        if(null != user){
        	String pass = user.getPassword();
        	if(password.equals(pass)){
        		logger.debug("user exist");
        		results = "sucess/" + user.getUsername();
        	}else{
        		logger.debug("password is wrong");
        		results = "error1";
        	}
        }else{
        	logger.debug("can't find the user with email: " + email);
        	results = "error2";
        }
        
        if (results.startsWith("sucess"))
        {
		  String userName = user.getUsername();
          this.logger.debug("adding cookie-----" + "the user name is" + userName);
          Cookie cookie = new Cookie("apn_userId", userName);
          cookie.setPath("/");
          cookie.setMaxAge(31536000);
          response.addCookie(cookie);
          this.logger.debug("done-----");
        }
        
        response.getWriter().write(results);
		return null;
	}
}
