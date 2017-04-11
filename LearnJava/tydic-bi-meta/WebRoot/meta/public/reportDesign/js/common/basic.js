function $() 
{ 
  var elements = new Array();  
  for (var i = 0; i < arguments.length; i++) 
  { 
    var element = arguments[i]; 
    if (typeof element == 'string') 
      element = getObjectById(element); 
    if (arguments.length == 1) 
      return element; 
    elements.push(element); 
  } 
  return elements; 
 }
function getObjectById(id)
{
    if (typeof(id) != "string" || id == "") return null;    
    if (document.getElementById) return document.getElementById(id);
    if (document.all) return document.all(id);
    try {return eval(id);} catch(e){ return null;}
} 
function length(obj)
{
	var l=0;
	for(var i in obj)l++;
	return l;
}
Array.prototype.valueOf=function()
{
	var res="";
	for(var i=0;i<this.length;i++)
	{
		if(i)
			res+=","+(this[i] && typeof this[i]=="object" && this[i].valueOf ?this[i].valueOf():this[i]);
		else
			res=(this[i] && typeof this[i]=="object" && this[i].valueOf ?this[i].valueOf():this[i]);
	}
	return res;
}

//删除数组中的一项 i:索引号
Array.prototype.remove=function(i)
{
//	return this.splice(i,1);
	if(i<0)return null;
	if(this.length<=i)return null;
	if(i==0)
	{
		return this.shift();
	}
	else if(i==this.length-1)
	{
		return this.pop();
	}	
	var result=this[i];
	for(var j=i;j<this.length-1;j++)
	{
		this[j]=this[j+1];
	}
	this.length-=1;
	return result;
}
//删除数组中的一项 i:索引号
Array.prototype.swap=function(s,d)
{
	var t=this[s];
	this[s]=this[d];
	this[d]=t;
}
Array.prototype.clone=function()
{
	var cl=[];
	for(var i=0;i<this.length;i++)
	if(typeof cl[i]=="object" && cl[i].clone && typeof cl[i].clone=="function")
		cl[i]=this[i].clone();
	else
		cl[i]=this[i];
	return cl;
}
function cmpObj(obj1,obj2)
{
	if(typeof obj1!=typeof obj2)
		return false;
	switch(typeof obj1)
	{
	case "boolean":
	case "string":
	case "number":
		return obj1==obj2;
	case "object":
		if(obj1.sort)
		{
			if(obj1.length!=obj2.length)return false;
			for(var i=0;i<obj1.length;i++)
				if(cmpObj(obj1[i],obj2[i])==false)return false;
			return true;
		}
		else
		{
			for(var i in obj1)
				if(cmpObj(obj1[i],obj2[i])==false)return false;
			for(var i in obj2)
				if(cmpObj(obj1[i],obj2[i])==false)return false;
			return true;
		}
	}
}

//数据拷贝
function clone(obj)
{
	switch(typeof obj)
	{
	case "boolean":
	case "string":
	case "number":
		return obj;
	case "object":
		if(obj.sort)
		{
			var res=[];
			for(var i=0;i<obj.length;i++)
				res[i]=clone(obj[i]);
			return res;//obj.clone();
		}
		else
		{
			var res=new Object();
			for(var i in obj)
			{
				res[i]=clone(obj[i]);
			}
			return res;
		}
	}
	return null;
}
function SymError(e)
{
	window.status=(e);
  	return true;
}
function attachObjEvent(obj,ev,fun)
{
	if(obj.attachEvent)
	{
		obj.attachEvent(ev,fun);
		return;
	}
	if(obj.addEventListener)
	{
		obj.addEventListener(ev.substr(2),fun,false);
		return;
	}
	obj[ev]=fun;
}
function detachObjEvent(obj,ev,fun)
{
	if(obj.attachEvent)
	{
		obj.detachEvent(ev,fun);
		return;
	}
	if(obj.addEventListener)
	{
		obj.removeEventListener(ev.substr(2),fun,false);
		return;
	}
	obj[ev]=null;
}
function unload(){clearVarVal(destroyData,function(key,val){
	if(typeof val=="object" && val.tagName)return false;else return true;
});}
function clearVarVal(data,filter)
{
	try
	{
		if(destructorDHMLX && data.DHTML_TYPE)
		{
			return destructorDHMLX(data);
		}
		for(var m in data)
		{
            if(destructorDHMLX && data[m].DHTML_TYPE)
            {
                return destructorDHMLX(data[m]);
            }
			if(filter && !filter(m,data[m]))
			{
				data[m]=null;
				delete data[m];
				continue;
			}
			if(typeof data[m] =="object"&& data[m] && data[m]!=data)
				clearVarVal(data[m],filter);
			data[m]=null;
			delete data[m];
		}
		data=undefined;
	}
	catch(ex){}
}
//加载报表CSS样式
function loadCssText(cssCode)
{
	if(cssCode)
	{
		var sty=$("userDefineCss");
		if(!sty)
		{
			sty=document.createElement("style");
			sty.id="userDefineCss";
			sty.setAttribute("type","text/css");
			var headElement=document.getElementsByTagName("head")[0];
			headElement.appendChild(sty);
		}
		if(dhtmlx._isIE)
		{//ie
			var t=cssCode.match(/opacity:(\d?\.\d+);/);//增加自动转换透明度功能，用户只需输入W3C的透明样式，它会自动转换成IE的透明滤镜
			if(t!=null)
				cssCode=cssCode.replace(t[0],"filter:alpha(opacity="+parseFloat(t[1])*100+")");
			sty.styleSheet.cssText+=cssCode;
			Debug && Debug("sty.styleSheet.cssText==\""+sty.styleSheet.cssText+"\"");
		}
		else if(document.getBoxObjectFor)
		{
			sty.innerHTML+=cssCode;//火狐支持直接innerHTML添加样式表字串
		}
		else
		{
			sty.appendChild(document.createTextNode(cssCode));
		}
//		addSheet(obj.styleCssText.value);
//		sty.childNodes	{...}	DispDOMChildrenCollection
//		sty.insertAdjacentHTML(0,obj.styleCssText.value);
	}
}
function KeyDown() 
{  //(event.keyCode==8)  || //屏蔽退格删除键
    if((window.event.altKey) && ((window.event.keyCode == 37) || (window.event.keyCode == 39)))
	{
		alert('不准你使用ALT+方向键前进或后退网页！');
		event.returnValue = false;
	}
	if((event.keyCode == 116) || (event.keyCode == 112) || (event.ctrlKey && event.keyCode == 82))
	{
		event.keyCode = 0;
		event.returnValue = false;
	}
	if ((event.ctrlKey)&&(event.keyCode==78))  
	event.returnValue=false;
	if ((event.shiftKey)&&(event.keyCode==121)) 
	event.returnValue=false;
	if(window.event.srcElement.tagName.toUpperCase()  == 'A' && window.event.shiftKey) 
		window.event.returnValue = false;                            
    if((window.event.altKey)&&(window.event.keyCode==115))             
    { 
		window.showModelessDialog('about:blank','','dialogWidth:1px;dialogheight:1px');
		return false;
	}
}
function contextmenu()
{
  return false;
}
var destroyData=[];
function addDestroyVar(data)
{
	destroyData[destroyData.length]=data;
}
attachObjEvent(window,"onunload",unload);
attachObjEvent(window,"onerror",SymError);
function tryEval()
{
	var al;
	var args=[];
	for(var i=0;i<arguments.length;i++)
		args[i]=arguments[i]; 
	try
	{
		fun=args.shift();
		al=args.shift();
		fun(args);
		return true;
	}
	catch(ex)
	{
		if(al && typeof ex=="string")alert(ex);
		else if(al)
		{
			var msg="";
			for(var a in ex)
				msg+=a+":"+ex[a]+"\n";
			alert(msg);
		}
		return false;
	}
}
//attachObjEvent( window.document,"onkeydown",KeyDown);
//attachObjEvent( window.document,"oncontextmenu",contextmenu);
//
