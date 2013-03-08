
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
				document.getElementById("moz-cn-apn-user").reset();
				document.getElementById("moz-cn-apn-password").reset();
			}, false );
		}
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