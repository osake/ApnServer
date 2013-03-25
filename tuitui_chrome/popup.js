var req = new XMLHttpRequest();
function getCookies(domain, name, callback) {
    chrome.cookies.get({"url":domain, "name":name}, function(cookie) {
        if(callback) {
            if(!cookie)
            {
               callback(0)

            }
            else
            {
            	callback(cookie.value);
            }
        }
    });
}

function get()
{
   getCookies("http://42.96.141.125", "apn_userId", function(id) {
      alert(id)
   });
}

document.addEventListener('DOMContentLoaded', function() {
  document.getElementById('btn1').addEventListener('click',validate)
});

function validate()
{
	var username=document.getElementById("username").value;
	var pass=document.getElementById("password").value;
	if(username.value == "")
	{
	   document.getElementById("errormsg").innerHTML="邮箱地址不能为空";
		username.focus();
		return;
	}
	if(pass.value=="")
	{
		document.getElementById("errormsg").innerHTML="密码不能为空";
		pass.focus();
		return;
	}
   login(username, pass);
}

function login(email, password)
{
    var url = "http://42.96.141.125:8080/login.do?action=send";
    var content = 'email=' + email + '&password=' + password;
    req.onreadystatechange = handleRes;
    req.open("POST",url,true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.send(content);
}
function handleRes()
{

    if(req.readyState == 4 && req.status == 200)
    {
      if(req.responseText == "error1" || req.responseText == "error2")
      {
         document.getElementById("errormsg").innerHTML="用户名或密码错误";
      }
      else
      {
         window.opener=null;
         window.open('','_self');
         window.close();
      }
    }
}