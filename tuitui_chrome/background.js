// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
var username;
var tablink = "";
var message = "";
var fromMenu = false;
chrome.browserAction.onClicked.addListener(push)
function getCookies(domain, name, callback) {
    chrome.cookies.get({"url": domain, "name": name}, function(cookie) {
        if(callback) {
            if(!cookie)
            {
               callback(0)

            }
            else
            {
               if(cookie.value == "" || cookie.value.length < 10)
               {
                   callback(0)
               }
               else
               {
                   callback(cookie.value);
               }

            }
        }
    });
}

function push()
{
   getCookies("http://42.96.141.125", "apn_userId", function(id)
   {
      username = id;
      chrome.tabs.getSelected(null,function(tab) {
          if(!fromMenu)
          {
             tablink = tab.url;
          }
          else
          {
             fromMenu = false;
          }
          sendReq();
      });
   });

}
function sendReq()
{

   if(username == 0)
   {
      chrome.windows.create({'url': 'popup.html', 'type': 'popup', width: 300, height: 200}, function(window) {});
   }
   else
   {
      var title = "\u63A8\u63A8\u7684\u65B0\u6D88\u606F";
      var req = new XMLHttpRequest();
      var url= "http://42.96.141.125:8080/notificationFromWeb.do?action=send";
      req.open("POST",url,true);
      req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
      //alert(tablink);
      //tablink = tablink.replace("&","&amp;");
      //alert(tablink);
      tablink = encodeURIComponent(tablink);
      var content = 'title=' + title + '&message=' + message + '&uri=' + tablink + '&username=' + username;
      req.send(content);
   }
}
function onClickHandler(info, tab)
{
   if(info.menuItemId == "contextselection")
   {
      message = info.selectionText;
      tablink = "";
   }
   else if(info.menuItemId == "contextlink")
   {
      message = info.linkUrl;
      tablink = info.linkUrl;
   }
   else if(info.menuItemId == "contextimage")
   {
      tablink = info.srcUrl;
   }
   else
   {
      tablink = info.pageUrl;
      message = "";
   }
   fromMenu = true;
   push();   
};
chrome.contextMenus.onClicked.addListener(onClickHandler);

function createRightClickMenu()
{
   var contexts = ["page","selection","link", "image"];
   var titles = ["\u63A8\u9001\u7F51\u9875\u5730\u5740","\u63A8\u9001\u9009\u5B9A\u5185\u5BB9","\u63A8\u9001\u94FE\u63A5","\u63A8\u9001\u56FE\u7247"];
   for (var i = 0; i < contexts.length; i++)
   {
      var context = contexts[i];
      var title = titles[i];
      var id = chrome.contextMenus.create({"title": title, "contexts":[context], "id": "context" + context});
   }
}

chrome.runtime.onInstalled.addListener(function()
{
     createRightClickMenu();
});