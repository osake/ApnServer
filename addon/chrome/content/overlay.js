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
				if(this.responseText == "sucess"){
					docCookies.setItem("apn_userId", email);
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
		
		send_request2:function(title, message, uri, email) {
			var content = 'title=' + title + '&message=' + message + '&uri=' + uri + '&email=' + email;
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
				var email = docCookies.getItem("apn_userId");
				var uri = gBrowser.contentDocument.location;
				MozCnApn.send_request2(title, '', uri, email);
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
		getItem: function (sKey) {
			if (!sKey || !this.hasItem(sKey)) { return null; }
		    return unescape(document.cookie.replace(new RegExp("(?:^|.*;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*((?:[^;](?!;))*[^;]?).*"), "$1"));
		},
		
		setItem: function (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
		    if (!sKey || /^(?:expires|max\-age|path|domain|secure)$/i.test(sKey)) { return; }
		    var sExpires = "";
		    if (vEnd) {
		      switch (vEnd.constructor) {
		        case Number:
		          sExpires = vEnd === Infinity ? "; expires=Tue, 19 Jan 2038 03:14:07 GMT" : "; max-age=" + vEnd;
		          break;
		        case String:
		          sExpires = "; expires=" + vEnd;
		          break;
		        case Date:
		          sExpires = "; expires=" + vEnd.toGMTString();
		          break;
		      }
		    }
		    document.cookie = escape(sKey) + "=" + escape(sValue) + sExpires + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "") + (bSecure ? "; secure" : "");
		  },
		  
		  removeItem: function (sKey, sPath) {
		    if (!sKey || !this.hasItem(sKey)) { return; }
		    document.cookie = escape(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + (sPath ? "; path=" + sPath : "");
		  },
		  
		  hasItem: function (sKey) {
		    return (new RegExp("(?:^|;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
		  },
		  
		  keys: /* optional method: you can safely remove it! */ function () {
		    var aKeys = document.cookie.replace(/((?:^|\s*;)[^\=]+)(?=;|$)|^\s*|\s*(?:\=[^;]*)?(?:\1|$)/g, "").split(/\s*(?:\=[^;]*)?;\s*/);
		    for (var nIdx = 0; nIdx < aKeys.length; nIdx++) { aKeys[nIdx] = unescape(aKeys[nIdx]); }
		    return aKeys;
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
		
		pushUrl: function() {
			var login = docCookies.loginCheck();
			if(login == 1){
				var stringsBundle = document.getElementById("string-bundle");
				var title = stringsBundle.getString('apn_send_url_title');
				var email = docCookies.getItem("apn_userId");
				var uri = gBrowser.contentDocument.location;
				MozCnApn.send_request2(title, '', uri, email);
			}else{
				MozCnApn.show_panel();
			}
		},
		
		pushContent: function() {
			var login = docCookies.loginCheck();
			if(login == 1){
				var stringsBundle = document.getElementById("string-bundle");
				var title = stringsBundle.getString('apn_send_url_title');
				var selection = gBrowser.contentDocument.getSelection();
				var email = docCookies.getItem("apn_userId");
				MozCnApn.send_request2(title, selection, '', email);
			}else{
				MozCnApn.show_panel();
			}
		}
};