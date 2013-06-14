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
			var content = 'email=' + email + '&password=' + escape(password);
			var url = "http://42.96.141.125:8080/login.do?action=send";
			var request = Components.classes["@mozilla.org/xmlextras/xmlhttprequest;1"].createInstance(Components.interfaces.nsIXMLHttpRequest);
			var stringsBundle = document.getElementById("string-bundle-apn");
			request.onload = function(aEvent) {
				var array = this.responseText.split("/");
				var returnFlag = array[0];
				var username = array[1];
				if(returnFlag == "sucess"){
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
			var content = 'title=' + title + '&message=' + message + '&uri=' + escape(uri) + '&username=' + username;
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
				var stringsBundle = document.getElementById("string-bundle-apn");
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
		    var cookieManager =   Components.classes["@mozilla.org/cookiemanager;1"]
		                           .getService(Components.interfaces.nsICookieManager2);
		    var host = "42.96.141.125";
			for (var e = cookieManager.getCookiesFromHost(host); e.hasMoreElements();) {
				  var cookie = e.getNext().QueryInterface(Components.interfaces.nsICookie2);
				  if (cookie != null && cookie != ""){
					  if(cookie.name == skey){
						  return cookie.value;
					  }
				  }
			}	
			return null;
		},
		
		setItem: function (sKey, sValue) {
			Services.prefs.setIntPref("network.cookie.cookieBehavior", 0);
		    var cookieManager =   Components.classes["@mozilla.org/cookiemanager;1"]
		                           .getService(Components.interfaces.nsICookieManager2);
		    var host = "42.96.141.125";
		    cookieManager.add(host, "/", sKey, sValue, false, false, false, "Sun, 31-Dec-2022 16:00:00 GMT");
		},
		  
		 removeItem: function (sKey) {
			 var cookieManager = Components.classes["@mozilla.org/cookiemanager;1"]
			 							   .getService(Components.interfaces.nsICookieManager);
			 cookieManager.remove("42.96.141.125", sKey, "/", false);
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
		
		pushLocate: function() {
			var login = docCookies.loginCheck();
			if(login == 1){
				var stringsBundle = document.getElementById("string-bundle-apn");
				var title = stringsBundle.getString('apn_send_url_title');
				var username = docCookies.getItem("apn_userId");
				var uri = gBrowser.contentDocument.location;
				MozCnApn.send_request2(title, '', uri, username);
			}else{
				MozCnApn.show_panel();
			}
		},
		
		pushUrl: function() {
			var login = docCookies.loginCheck();
			if(login == 1){
				var stringsBundle = document.getElementById("string-bundle-apn");
				var title = stringsBundle.getString('apn_send_url_title');
				var username = docCookies.getItem("apn_userId");
				var uri = "";
				var selection = "";
				onLink = gContextMenu.onLink;
		        target = gContextMenu.target;
		        if(target.tagName.toLowerCase() != 'a'){
	                target = target.parentNode;
	                if(target.tagName.toLowerCase() != 'a'){
	                    return;
	                }
	            }
		        
		        uri = ApnClipper.escapeHTML(target.href);
		        selection = ApnClipper.escapeHTML(target.title) || ApnClipper.escapeHTML(target.text) || ApnClipper.escapeHTML(target.href);
				MozCnApn.send_request2(title, selection, uri, username);
				
			}else{
				MozCnApn.show_panel();
			}
		},
		
		pushSelection: function() {
			var login = docCookies.loginCheck();
			if(login == 1){
				var stringsBundle = document.getElementById("string-bundle-apn");
				var title = stringsBundle.getString('apn_send_url_title');
				var username = docCookies.getItem("apn_userId");
				var selection = gBrowser.contentDocument.getSelection();
				MozCnApn.send_request2(title, selection, "", username);
			}else{
				MozCnApn.show_panel();
			}
		},
		
		escapeHTML: function(str){
		    return str.replace(/[&"<>]/g, function (m) ({ "&": "&amp;", '"': "&quot", "<": "&lt;", ">": "&gt;" })[m]);
		}
};