package org.androidpn.server.console.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
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
        String password = ServletRequestUtils.getStringParameter(request, "password");
        User user = userService.getUserByEmail(email);
        if(null != user){
        	String pass = user.getPassword();
        	if(password.equals(pass)){
        		logger.debug("user is exist");
        		results = "sucess";
        	}else{
        		logger.debug("password is wrong");
        		results = "error1";
        	}
        }else{
        	logger.debug("can't find the user: " + email);
        	results = "error2";
        }
        
        response.getWriter().write(results);
		return null;
	}
}
