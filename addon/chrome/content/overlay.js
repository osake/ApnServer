var MozCnApn = {
		el: function(id) {
			if (typeof id == 'string')
				return document.getElementById(id);
			else
				return id;
		},
		get panel() {
			delete this.panel;
			return this.panel = this.el("moz-cn-apn-login-popup");	
		},

		get toolbarbutton() {
			delete this.toolbarbutton;
			if(this.el('moz_cn_apn'))
				return this.toolbarbutton = this.el('moz_cn_apn');
		},

		show_panel: function() {
			if (this.panel.state === "open" || this.panel.state === "showing") {
				this.panel.hidePopup();
			}
			this.panel.openPopup(this.toolbarbutton, "after_end", -15, 0, false, false);
		},

		show_success: function() {
			this.el("moz-cn-apn-login-messp").style.display = "none";
			this.el("moz-cn-apn-login-submit").style.display = "none";
			this.el("moz-cn-apn-login-messs").style.display = "block";
			window.setTimeout(function() {
				MozCnApn.close_panel();
				MozCnApn.input_recover();
			}, 1500);
		},

		close_panel: function() {
			this.el("moz-cn-apn-user-require").style.display = "none";
			this.el("moz-cn-apn-password-require").style.display = "none";
			if (this.panel.state === "open" || this.panel.state === "showing") {
				this.panel.hidePopup();
			}
		},

		send_request:function() {
			var email = this.el("moz-cn-apn-user").value;
			var password = this.el("moz-cn-apn-password").value;
			var content = 'email=' + email + '&password=' + password;
			var url = "http://42.96.141.125:8080/login.do?action=send";
			var request = Components.classes["@mozilla.org/xmlextras/xmlhttprequest;1"].createInstance(Components.interfaces.nsIXMLHttpRequest);
			var stringsBundle = document.getElementById("string-bundle");
			request.onload = function(aEvent) {
				var array = this.responseText.split("/");
				var returnFlag = array[0];
				var username = array[1];
				if(returnFlag == "sucess"){
					docCookies.setItem("apn_userId", username);
					MozCnApn.show_success();
				}else {
					MozCnApn.input_recover();
					var tag = document.getElementById("moz-cn-apn-login-info");	 
					if(this.responseText == "error1"){
						var error1String = stringsBundle.getString('login_submit_fail1');
						tag.setAttribute("value", error1String);
					}else if(this.responseText == "error2"){
						var error2String = stringsBundle.getString('login_submit_fail2');
						tag.setAttribute("value", error2String);
					}else{
						var error3String = stringsBundle.getString('login_submit_fail3');
						tag.setAttribute("value", error3String);
					}
					MozCnApn.el("moz-cn-apn-login-messp").style.display = "none";
					MozCnApn.el("moz-cn-apn-login-messf").style.display = "block";
				} 
			};
			request.onerror = function(aEvent) {
				MozCnApn.input_recover();
				var tag = document.getElementById("moz-cn-apn-login-info");	
				var errorString = stringsBundle.getString('login_submit_fail3');
				tag.setAttribute("value", errorString);
				MozCnApn.el("moz-cn-apn-login-messp").style.display = "none";
				MozCnApn.el("moz-cn-apn-login-messf").style.display = "block";
			};
			request.open("POST", url, true);
			request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			request.send(content);
		},
		
		send_request2:function(title, message, uri, username) {
			var content = 'title=' + title + '&message=' + message + '&uri=' + uri + '&username=' + username;
			var url = "http://42.96.141.125:8080/notificationFromWeb.do?action=send";
			var request = Components.classes["@mozilla.org/xmlextras/xmlhttprequest;1"].createInstance(Components.interfaces.nsIXMLHttpRequest);
			request.onload = function(aEvent) {
				//do nothing
			};
			request.onerror = function(aEvent) {
				//do nothing
			};
			request.open("POST", url, true);
			request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			request.send(content);
		},

		input_disable:function() {
			this.el("moz-cn-apn-user").disabled = true;
			this.el("moz-cn-apn-password").disabled = true;
		},

		input_recover:function() {
			this.el("moz-cn-apn-user").disabled = false;
			this.el("moz-cn-apn-password").disabled = false;
		},
		
		login_submit:function() {
			if(this.el("moz-cn-apn-user").value == ''){
				this.el("moz-cn-apn-user-require").style.display = "block";
			}else if(this.el("moz-cn-apn-password").value == ''){
				this.el("moz-cn-apn-password-require").style.display = "block";
			}else {
				this.input_disable();
				this.el("moz-cn-apn-user-require").style.display = "none";
				this.el("moz-cn-apn-password-require").style.display = "none";
				this.el("moz-cn-apn-login-submit").style.display = "none";
				this.el("moz-cn-apn-login-messp").style.display = "block";
				this.send_request();
			}
		},
		
		handle_event:function(){
			var login = docCookies.loginCheck();
			if(login == 1){
				var stringsBundle = document.getElementById("string-bundle");
				var title = stringsBundle.getString('apn_send_url_title');
				var username = docCookies.getItem("apn_userId");
				var uri = gBrowser.contentDocument.location;
				MozCnApn.send_request2(title, '', uri, username);
			}else{
				MozCnApn.show_panel();
			}
		},
		
		resubmit:function() {
			if(this.el("moz-cn-apn-user").value == ''){
				this.el("moz-cn-apn-user-require").style.display = "block";
			}else if(this.el("moz-cn-apn-password").value == ''){
				this.el("moz-cn-apn-password-require").style.display = "block";
			}else {
				this.input_disable();
				this.el("moz-cn-apn-user-require").style.display = "none";
				this.el("moz-cn-apn-password-require").style.display = "none";
				this.el("moz-cn-apn-login-messf").style.display = "none";
				this.el("moz-cn-apn-login-messp").style.display = "block";
				this.send_request();
			}
		},
		
		exit:function() {
			docCookies.removeItem("apn_userId");
		}
		
		
};

var docCookies = {
		makeURI: function(str) {
		    return Components.classes["@mozilla.org/network/io-service;1"]
		                     .getService(Components.interfaces.nsIIOService)
		                     .newURI(str, null, null);
		},
		
		getItem: function (skey) {
			Services.prefs.setIntPref("network.cookie.cookieBehavior", 0);
		    var serv =   Components.classes["@mozilla.org/cookieService;1"]
		                           .getService(Components.interfaces.nsICookieService);
		    var uri = makeURI("http://apncloud.com/");
			var cookie = serv.getCookieString(uri, null);
			if (cookie != null && cookie != ""){
				var CArray = cookie.split("=");
				if(CArray[0] == skey){
					return CArray[1];
				}
			}
			return null;
		},
		
		setItem: function (sKey, sValue) {
			Services.prefs.setIntPref("network.cookie.cookieBehavior", 0);
		    var serv =   Components.classes["@mozilla.org/cookieService;1"]
		                           .getService(Components.interfaces.nsICookieService);
		    var uri = makeURI("http://apncloud.com/");
		    serv.setCookieString(uri, null, sKey+ "=" + sValue +"; path=/; domain=apncloud.com; expires=Sun, 31-Dec-2022 16:00:00 GMT;", null);
		},
		  
		 removeItem: function (sKey) {
			 var cookieManager = Components.classes["@mozilla.org/cookiemanager;1"]
			 							   .getService(Components.interfaces.nsICookieManager);
			 cookieManager.remove(".apncloud.com", sKey, "/", false);
		 },
		  
		 loginCheck: function(){
			 if(null != docCookies.getItem("apn_userId")){
				 if('' != docCookies.getItem("apn_userId")){
					 return 1;
				 }else{
				  return 0;
				 }
			 }else{
				  return 0;
			 }
		 }
};

var ApnClipper = {
		
		onLinkDDCheck: function(onLink, target){
			if(onLink){
				if(target.tagName.toLowerCase() != 'a'){
                    target = target.parentNode;
                    if(target.tagName.toLowerCase() != 'a'){
                        return 0;
                    }{
                    	return 1;
                    }
                }{
                	return 1;
                }
			}{
				return 0;
			}
		},
		
		pushUrl: function() {
			var login = docCookies.loginCheck();
			if(login == 1){
				var stringsBundle = document.getElementById("string-bundle");
				var title = stringsBundle.getString('apn_send_url_title');
				var username = docCookies.getItem("apn_userId");
				var uri = gBrowser.contentDocument.location;
				MozCnApn.send_request2(title, '', uri, username);
			}else{
				MozCnApn.show_panel();
			}
		},
		
		pushContent: function() {
			var login = docCookies.loginCheck();
			var stringsBundle = document.getElementById("string-bundle");
			var title = stringsBundle.getString('apn_send_url_title');
			var selection = "";
			var uri = "";
			var username = docCookies.getItem("apn_userId");
			if(login == 1){
				isContentSelected = gContextMenu.isContentSelected;
	            onLink = gContextMenu.onLink;
	            if( onLink && isContentSelected){
	            	target = gContextMenu.target;
	            	var flag = ApnClipper.onLinkDDCheck(onLink, target);
	            	if(flag == 1){
	            		uri = target.href;
	            		selection = gBrowser.contentDocument.getSelection();
	            	}else{
	            		selection = gBrowser.contentDocument.getSelection();
	            	}
	            }else if(onLink){
	            	target = gContextMenu.target;
	            	var flag = ApnClipper.onLinkDDCheck(onLink, target);
	            	if(flag == 1){
	            		uri = target.href;
	            		selection = target.title || target.text || target.href;
	            	}
	            }else if(isContentSelected){
	            	selection = gBrowser.contentDocument.getSelection();
	            }
				MozCnApn.send_request2(title, selection, uri, username);
			}else{
				MozCnApn.show_panel();
			}
		}
};