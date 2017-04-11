// JavaScript Document

var jsReady=false,swfReady=false;

function isReady()
{
	return jsReady;
}
 
function setSwfIsReady(m,c)
{
	swfReady=true;
	getSWF("myFlash").fun(m,c);
}

function reSetSwfIsReady()
{
	swfReady=true;
	getSWF("myFlash").refun();
}

function pageInit()
{
     jsReady=true;
}

function getSWF(movieName)
{
  if (navigator.appName.indexOf("Microsoft") != -1)
  {
		return window.document[movieName];
  }
  else
  {
   		return document[movieName];
  }
}

window.onload = pageInit;