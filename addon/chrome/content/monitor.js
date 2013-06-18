
(function() {

	var ApnListener = {
		onLocationChange: function(webProgress, request, uri) {
			document.getElementById("moz-cn-apn-user").reset();
			document.getElementById("moz-cn-apn-password").reset();
		}
	};

	function installButton() {
		try{
			var firefoxnav = document.getElementById("nav-bar");
			var newSet = firefoxnav.currentSet + "";
			if(newSet.indexOf("moz_cn_apn") == -1){
				newSet = newSet + ",moz_cn_apn";
				firefoxnav.setAttribute("currentset", newSet);
				firefoxnav.currentSet = newSet;
				document.persist("nav-bar", "currentset");
				try{
					BrowserToolboxCustomizeDone(true);
				}catch(ex){}
			}
			setApnPref(true);
		} catch(e) {}
	}

	function Apn_init() {
		var navbar = document.getElementById("nav-bar");
		if (navbar != null){
			if (!getApnPref()) {
				installButton();
			}
			MozCnApn.panel.addEventListener("popupshown", function(e) {
				gBrowser.addProgressListener(ApnListener);
			}, false );
			MozCnApn.panel.addEventListener("popuphidden", function(e) {
				gBrowser.removeProgressListener(ApnListener);
				reset_button();
			}, false );
		}
		
		var contextMenu = document.getElementById("contentAreaContextMenu");
		if (contextMenu){
		   	contextMenu.addEventListener("popupshowing", initContextMenuShow, false);
		}
	}
	
	function initContextMenuShow(event) {
		var show1 = document.getElementById("apnclipper-contextmenu-locate");
		var show2 = document.getElementById("apnclipper-contextmenu-url");
		var show3 = document.getElementById("apnclipper-contextmenu-selection");
		var show4 = document.getElementById("apnclipper-contextmenu-duplicate");
		show1.hidden = (gContextMenu.isContentSelected || gContextMenu.onLink || gContextMenu.onImage);
		show2.hidden = !((gContextMenu.onLink && !(gContextMenu.isContentSelected && gContextMenu.onLink)) || gContextMenu.onImage);
		show3.hidden = !(gContextMenu.isContentSelected &&  !(gContextMenu.isContentSelected && gContextMenu.onLink));
		show4.hidden = !(gContextMenu.isContentSelected && gContextMenu.onLink);
    }
	
	function reset_button() {
		document.getElementById("moz-cn-apn-login-messp").style.display = "none";
		document.getElementById("moz-cn-apn-login-messf").style.display = "none";
		document.getElementById("moz-cn-apn-login-messs").style.display = "none";
		document.getElementById("moz-cn-apn-login-submit").style.display = "block";
		document.getElementById("moz-cn-apn-user").reset();
		document.getElementById("moz-cn-apn-password").reset();
	}

	function getApnPref() {
		return Application.prefs.getValue("extensions.apn@lwen.com.installed", false);
	}

	function setApnPref(val) {
		try{
			Application.prefs.setValue("extensions.apn@lwen.com.installed", val);
		}catch(e){}
	}
	
	window.addEventListener("load", Apn_init, false);

})();