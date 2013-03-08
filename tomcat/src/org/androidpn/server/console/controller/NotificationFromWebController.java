package org.androidpn.server.console.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class NotificationFromWebController extends MultiActionController {
	private NotificationManager notificationManager;
	protected final Log logger = LogFactory.getLog(getClass());


    public NotificationFromWebController() {
        notificationManager = new NotificationManager();
    }
	
    public String send(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String username = ServletRequestUtils.getStringParameter(request,
                "username");
        String title = ServletRequestUtils.getStringParameter(request, "title");
        String message = ServletRequestUtils.getStringParameter(request,
                "message");
        String uri = ServletRequestUtils.getStringParameter(request, "uri");

        String apiKey = Config.getString("apiKey", "");
        logger.debug("apiKey=" + apiKey);


        notificationManager.sendNotifcationToUser(apiKey, username, title,
                    message, uri);

        String results = "sucess";
        response.getWriter().write(results);
        
        return null;
        
    }
}
