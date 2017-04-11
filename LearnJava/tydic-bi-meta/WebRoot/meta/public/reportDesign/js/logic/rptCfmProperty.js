function getObjTypeTitle(node)
{
	switch(node.type)
	{
	case "reportConfig":
		break;
	case "reportTitle":
		return "标题-";
	case "reportModule":
		return "模块-";
	case "reportModuleTab":		
		return "模块页-"+rptObjects[node.modId].moduleName.value+"-";
	}
	return "";
}
//添加对象到rptObjects          
//并且把对象的各属性值存入JS对象中
function addIds(node,id,text,foucs)
{
	if(!node.type)return;
	if(!rptComAttr[node.type])return;
	if(rptObjects[id])return;
	text=getObjTypeTitle(node)+text;
	rptObjects[id]=node;
	rptObjects[id].text=text;
	rptObjects[id].objId=id;
	var list=$("ids_list");
	list.options[list.length]=new Option(text,id);
	if(node.type=="reportModule")initModelTab(node);
	if(foucs)
	{
		list.selectedIndex=list.length-1;
		idlist_change();
	}
}
//从全局管理变量中删除对象ID
function deleteId(id)
{
	for(var sid in rptObjects)
	{
		if(sid==id)
		{
			rptObjects[id]=null;
			delete rptObjects[id];
		}
	}
	var list=$("ids_list");
	for(var i=0;i<list.length;i++)
	{
		if(list.options[i].value==id)
		{
			list.options.remove(i);
			if(list.selectedIndex>=list.length)
			{
				list.selectedIndex=0;
				idlist_change();
			}
			break;
		}
	}
}
function setSelectId(id)
{
	var list=$("ids_list");
	for(var i=0;i<list.length;i++)
	{
		if(list.options[i].value==id)
		{
			list.selectedIndex=i;
			break;
		}
	}
	idlist_change();
}
function updateListId(id,text)
{
	var list=$("ids_list");
	for(var i=0;i<list.length;i++)
	{
		if(list.options[i].value==id)
		{
			var pri=getObjTypeTitle(rptObjects[id]);
			list.options[i].text=pri+text;
			rptObjects[id].text=pri+text; 
			break;
		}
	}
}
//ID列表change事件
function idlist_change(force)
{
 	var list=$("ids_list");
	var id=list.value;
	if(!force && currentObj.id==id)return;
	currentObj.obj=rptObjects[id];
	currentObj.id=id;
	currentObj.type=currentObj.obj.type;
	bindPropertys();
	proList.scrollTop=0;
//window.setTimeout("	proList.scrollTop=0;",100);
}
//实现属性列表的生成
function bindPropertys()
{
	if(!currentObj.obj || !currentObj.id)return;
	if(!currentObj.obj.type)return;
	var table=$("property_table");
	var len=table.rows.length;
	for(var i=0;i<len;i++)
	{
		table.deleteRow();
	}
	var tr,td;
	var hashObj=currentObj.obj;
	for(key in hashObj)
	{
		if(key=="type")continue;
		if(typeof(hashObj[key])=="function")continue;
		if(!hashObj[key] || !hashObj[key].type)continue;
		tr=table.insertRow(table.rows.length);
		tr.id=key+"_tr";
		
		td=tr.insertCell(0);
		td.style.width="10px";
		td.align="right";
		td.bgColor="#d4d0c8";
		td.style.height="20px";
		var valP=hashObj[key].type;
		if(valP=="tree")
		{
			if(hashObj[key].collapse)
			{
				tr.display="none";
				td.innerHTML="<img src='images/Tplus.gif' id='"+key+"_img' border=0 onclick=\"change_trDisplay('"+key+"')\"/>";
			}
			else
			{
				td.innerHTML="<img src='images/Tminus.gif' id='"+key+"_img' border=0 onclick=\"change_trDisplay('"+key+"')\"/>";
			}
		}
		td=tr.insertCell(1);
		td.style.width="100px";
		td.noWrap=true;
		td.bgColor="#FFFFFF";
		//td.style.paddingLeft="5px";
		if(hashObj[key].label)
			td.innerHTML=hashObj[key].label;
		else
			td.innerHTML=key;
		td.id=key+"_name";
		if(valP=="tree")
		{
			td.ondblclick=bindPropertyValues;
		}
		else
		{
			td.onclick=bindPropertyValues;
		}
		td=tr.insertCell(2);
		td.style.width="100%";
		td.noWrap=true;
		td.bgColor="#FFFFFF";
		td.id=key+"_value";
		td.style.paddingLeft="5px";
		if(valP=="tree")
		{
			td.innerHTML="Tree";
			td.ondblclick=bindPropertyValues;
			td.obj="Tree";
			for(var cKey in hashObj[key])
			{
				if(cKey=="type")continue;
				if(typeof(hashObj[key][cKey])=="function")continue;
				if(!hashObj[key][cKey] || !hashObj[key][cKey].type)continue;
				tr=table.insertRow(table.rows.length);
				if(hashObj[key].collapse)tr.style.display="none";
				tr.id=key+"$"+cKey+"_tr";
				
				td=tr.insertCell(0);
				td.style.width="10px";
				td.align="right";
				td.bgColor="#d4d0c8";
				td.style.height="20px";

				td=tr.insertCell(1);
				td.style.width="40%";
				td.noWrap=true;
				td.bgColor="#FFFFFF";
				td.style.paddingLeft="20px";
				if(hashObj[key][cKey].label)
					td.innerHTML=hashObj[key][cKey].label;
				else
					td.innerHTML=cKey;
				td.id=key+"$"+cKey+"_name";
				td.onclick=bindPropertyValues;
				td=tr.insertCell(2);
				td.noWrap=true;
				td.bgColor="#FFFFFF";
				td.style.paddingLeft="5px";
				td.id=key+"$"+cKey+"_value";
				td.onclick=bindPropertyValues;
				if(hashObj[key][cKey].type=="sel")
				for(var i=0;i<hashObj[key][cKey].list.length;i++)
					if(hashObj[key].value==hashObj[key][cKey].list[i].value)
					{
						hashObj[key][cKey].text=hashObj[key][cKey].list[i].text;
						break;
					}
				if(hashObj[key][cKey].text)
					td.innerHTML=hashObj[key][cKey].text;
				else
					td.innerHTML=hashObj[key][cKey].value;
				if(propertySetType || initPropertyType[hashObj[key][cKey].type])
					bindPropertyValue([key,cKey],hashObj[key][cKey]);
			}
		}
		else
		{
			if(rptComAttr[valP])
			{
				td.onclick=openObjectWin;
				td.innerHTML="Object";
			}
			if(!rptComAttr[valP]) 
			{
				td.onclick=bindPropertyValues;	
				if(hashObj[key].type=="sel")
				for(var i=0;i<hashObj[key].list.length;i++)
				if(hashObj[key].value==hashObj[key].list[i].value)
				{
					hashObj[key].text=hashObj[key].list[i].text;
					break;
				}
				if(hashObj[key].text)
					td.innerHTML=hashObj[key].text;
				else
					td.innerHTML=hashObj[key].value;
				if(propertySetType || initPropertyType[hashObj[key].type])
					bindPropertyValue([key],hashObj[key]);
			}
		}
		//td.innerHTML=el[key];
	}
	if(currentObj.type=='reportModuleTab')
		setPrivTableShowType(currentObj.obj);
}
//展开子项
function change_trDisplay(key)
{
	var dis="none";
	var src="images/Tplus.gif";
	if($(key+"_tr").display=="none")
	{
		dis="";
		src="images/Tminus.gif";
	}
	$(key+"_tr").display=dis;
	$(key+"_img").src=src;
	for(cKey in currentObj.obj[key])
	{
		if($(key+"$"+cKey+"_tr"))
			$(key+"$"+cKey+"_tr").style.display=dis;
	}
}
//单击属性列表项事件,绑定属性值
function bindPropertyValues()
{
	var list=$("ids_list");
	var id=list.value;
	var hashObj=currentObj.obj;
	var key=event.srcElement.id.split("_")[0];
	var keys=key.split("$");
	var valueTd=$(key+"_value");
	var nameTd =$(key+"_name");
	if(currentObj.lastNode && currentObj.lastNode.id==nameTd.id)return;
	if(currentObj.lastNode)
	{
		currentObj.lastNode.bgColor="#FFFFFF";
		currentObj.lastNode.style.color="";
	}
	currentObj.lastNode=nameTd;
	nameTd.bgColor="#0a246a";
	nameTd.style.color="#FFFFFF";
	if(!valueTd.onclick && !valueTd.ondblclick)return;
	if(keys.length==1)
	{
		var hasChildNode=false;
		var se=event.srcElement.parentNode.parentNode;
		for(var i=0;i<se.childNodes.length;i++)
		{
			if(se.childNodes[i].id && se.childNodes[i].id.indexOf(key+"$")>-1)
			{
				hasChildNode=true;
				break;
			}
		}
		if(hasChildNode)
		{
			change_trDisplay(key);
		}
		else
		{
			if(propertySetType==0)bindPropertyValue(keys,hashObj[keys[0]]);
			changeIntro(keys);
		}
	}
	else
	{
		if(propertySetType==0)bindPropertyValue(keys,hashObj[keys[0]][keys[1]]);
		changeIntro(keys);
		/*
		var functions="{var keys='"+keys.join(",")+"';keys=keys.split(',');bindPropertyValue(keys);}";//调用修改值
		switchFunction(type,functions,true);
		functions="{var keys='"+keys.join(",")+"';keys=keys.split(',');changeIntro(keys);}";//显示属性说明
		switchFunction(type,functions,true);
		*/
	}
}
//绑定控件实现值修改 
function bindPropertyValue(keys,hashObj,force)
{
	var valueTd=$(keys.join("$")+"_value");
	var nameTd=$(keys.join("$")+"_name");
	var value=hashObj.value;
	var valueType=hashObj.type;
	var str="";
	var valId=keys.join("$")+"_pro_value";
	if(!force && valueTd.innerHTML.charAt()=="<")return;
	switch(valueType)
	{
	case "txt":
		valueTd.innerHTML="<input type='text'  style='width:100%;height:20px;' "+(hashObj.readonly?"readonly":"")+" class='inuptText' id='" 
				+valId+"' onblur='setPropertyValue(this)' onkeydown ='checkEnter(this)' title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'/>";
		$(valId).value=value;
		if(propertySetType==0)
		{
			$(valId).focus();
			$(valId).select();
		}
		break;
	case "num":
		str="<input style='width:100%;height:20px;' class='inuptText' "+(hashObj.readonly?"readonly":"")+" id='"
				+valId+"' onblur='setPropertyValue(this)' onkeydown ='checkEnter(this);onlynumber();'  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'/>";
		valueTd.innerHTML=str;
		$(valId).value=value;
		if(propertySetType==0)
		{
			$(valId).focus();
			$(valId).select();
		}
		break;
	case "sel":
		valueTd.innerHTML="<select style='width:100%;height:20px;' class='selectText' "+(hashObj.readonly?"disabled":"")+"  id='"+valId+"' onchange='setPropertyValue(this)'  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'></select>";
		if(hashObj.list.length)
		{
			var list=$(valId);
			for(var i=0;i<hashObj.list.length;i++)
			{
				list.options[list.length]=new Option(hashObj.list[i].text,hashObj.list[i].value);
				if(value==hashObj.list[i].value)
				{
					list.options[list.options.length-1].selected=true;
					hashObj.text=hashObj.list[i].text;
				}
			}
		}
		else
		{
			bindPropertyList(valueTd,keys,hashObj);
		}
		if(propertySetType==0)list.focus();
		break;
	case "mulTxt":
		if(!hashObj.rows)hashObj.rows=3;
		valueTd.innerHTML="<textarea type='text'  style='width:100%;' "+(hashObj.readonly?"readonly":"")+" class='inuptText' id='" 
				+valId+"' onblur='setPropertyValue(this)' rows="+hashObj.rows+"  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'></textarea>";
		$(valId).value=value;
		if(propertySetType==0)
		{
			$(valId).focus();
			$(valId).select();
		}
		break;
	case "color":
		valueTd.innerHTML="<span style='padding-left:5px;width:60px;background-color:"+value+"' id='"+valId+"' "+(hashObj.readonly?"":"onclick='colorChange(this)'")+"   title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'>"+value+"</span>";
		break;
	case "chk":
		valueTd.innerHTML="<input TYPE='checkbox' "+(hashObj.readonly?"disabled":"")+" id='"+valId+"' onclick='chkClick(this)' "+(value?"checked":"")+
				"/><label for='"+valId+"'  id='"+valId+"_label'  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'>"+(hashObj.label?hashObj.label:"")+"</label>";
		break;
	case "label":
		valueTd.innerHTML="<label  id='"+valId+"' onclick='labelClick(this)' sryle='width:100%;'  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'>"+value+"</label>";
		break;
	case "reportModuleTab":
		valueTd.innerHTML="<label  id='"+valId+"' onclick='onClick(\""+hashObj.tabId+"\")' sryle='width:100%;' >"+hashObj.tabName.value+"</label>";
		break;
	default:
		bindPropertyList(valueTd,keys,hashObj);
		break;
	}
}
//显示属性说明
function changeIntro(keys)
{
	var str="";
	if(keys.length==1)
	{
		str=currentObj.obj[keys[0]].explain;
		str=str?str:"";
	}
	else
	{
		str=currentObj.obj[keys[0]][keys[1]].explain;
		str=str?str:"";
	}
	if(debug)
		Debug(keys[keys.length-1]+"：<br>&nbsp;&nbsp;&nbsp;&nbsp;"+str);
	else
		$("property_intro").innerHTML=keys[keys.length-1]+"：<br>&nbsp;&nbsp;&nbsp;&nbsp;"+str;
}
//回写对象值
function setPropertyValue(obj)
{
	var valueTd=obj.parentNode;
	var keys=valueTd.id.split("_")[0];
	var nameTd=$(keys+"_name");
	var value=obj.value;
	keys=keys.split("$");
	var jsObj;
	if(obj.type=="num")
	{
		if(obj.value=="")obj.value="0";
	}
	if(keys.length==1)
	{
		jsObj=currentObj.obj[keys[0]];
		if(typeof obj.value =="string")	//	设置成空值需要单独设置
			jsObj.value=obj.value;
		if(rptComAttr[currentObj.type]==keys[0])
			updateListId(currentObj.id,obj.value);
	}
	else
	{
		jsObj=currentObj.obj[keys[0]][keys[1]];
		jsObj.value=obj.value;
	}
	if(!propertySetType)
	{
		if(obj.tagName=="SELECT")
		{
			if(obj.selectedIndex>=0)
				valueTd.innerHTML=obj.options[obj.selectedIndex].text;
		}
		else if(obj.tagName!="TEXTAREA")
			valueTd.innerHTML=jsObj.value;
	}
	if(keys[0]=="inParams")
		jsObj.value=jsObj.value.split(",");
	if(currentObj.type=='reportModuleTab' && keys[0]=='showType')	//	显示类型
	{
		setPrivTableShowType(currentObj.obj);
		
//		changeRptModelTab(currentObj.id,["showType"],rptObjects[currentObj.id]);
	}
	else if(currentObj.type=='reportModuleTab' && keys[0]=='dataSrcName')	//	数据源更改事件
	{
//		changeRptModelTab(currentObj.id,["dataSrcName"],rptObjects[currentObj.id]);
		//initModelTab(reportConfig.dataCfms.value[jsObj.value]);
	}
	Debug(keys+"===="+jsObj.value);
	bindRptProperty(currentObj.id,keys,currentObj.obj);
	return jsObj; //js存储对象
}
function setPrivTableShowType(rptTab)
{
	var tabId=rptTab.tabId;
	var showType=rptTab.showType.value;
	if(showType==0)	//只表格
	{//return;
		if(currentObj.type=="reportModuleTab")
		{
			if($("graph_tr") && $("graph_tr").display=="")
				change_trDisplay("graph");
			$("graph_tr").disabled=true;
			$("graph_name").disabled=true;
			$("graph_value").disabled=true;
			$("graph_img").disabled=true;
			$("table_tr").disabled=false;
			$("table_name").disabled=false;
			$("table_value").disabled=false;
			$("table_img").disabled=false;
		}
	}
	else if(showType==1)	//	只图形
	{
		if(currentObj.type=="reportModuleTab")
		{
			if($("table_tr") && $("table_tr").display=="")
				change_trDisplay("table");
			$("table_tr").disabled=true;
			$("table_name").disabled=true;
			$("table_value").disabled=true;
			$("table_img").disabled=true;
			$("graph_tr").disabled=false;
			$("graph_name").disabled=false;
			$("graph_value").disabled=false;
			$("graph_img").disabled=false;
		}
	}
	else if(showType==2)
	{
		if(currentObj.type=="reportModuleTab")
		{
			$("table_tr").disabled=false;
			$("table_name").disabled=false;
			$("table_value").disabled=false;
			$("graph_tr").disabled=false;
			$("graph_name").disabled=false;
			$("graph_value").disabled=false;
			$("table_img").disabled=false;
			$("graph_tr").disabled=false;
		}
	}
	moduleTabContentSize(rptTab.tabId);
}
//颜色框事件 
function colorChange(obj)
{
	var valueTd=obj.parentNode;
	var keys=valueTd.id.split("_")[0];
	var nameTd=$(keys+"_name");
	var value=obj.value;
	keys=keys.split("$");
	var rvalue=ShowDialog("color_picker.htm",obj.innerHTML,event.x-300,event.y-70,250,245)
//	var rvalue=ShowDialog("color_picker2.htm",obj.innerHTML,event.x-300,event.y-70,250,245)
	if(!rvalue)return;
	obj.innerHTML=rvalue;
	if(keys.length==1)
	{
		jsObj=currentObj.obj[keys[0]];
		jsObj.value=obj.innerHTML;
	}
	else
	{
		jsObj=currentObj.obj[keys[0]][keys[1]];
		jsObj.value=obj.innerHTML;
	}
	obj.style.backgroundColor=jsObj.value;
	bindRptProperty(currentObj.id,keys,currentObj.obj);
	
//	showProWin(initFun,callFun,null);
	
//	var cp = new dhtmlXColorPicker(null,null);
//	Debug(obj.offsetLeft+"__"+obj.offsetHeight);
//	cp.setPosition(event.x-280,event.y-100);
////	cp.linkTo(obj.id,obj.id);
//	cp.setColor(obj.innerHTML);
//   	cp.setImagePath("js/dhtmlx/imgs/");
//    cp.init();
//	cp.setOnSelectHandler(function(color){
//		obj.innerHTML=color;
//		if(keys.length==1)
//		{
//			jsObj=currentObj.obj[keys[0]];
//			jsObj.value=obj.innerHTML;
//		}
//		else
//		{
//			jsObj=currentObj.obj[keys[0]][keys[1]];
//			jsObj.value=obj.innerHTML;
//		}
//		obj.style.backgroundColor=jsObj.value;
////	 	bindRptProperty(currentObj.id,keys,currentObj.obj);
//		cp.close();
//		cp=null;
//	});
}
//复选框事件 
function chkClick(obj)
{
	var valueTd=obj.parentNode;
	var keys=valueTd.id.split("_")[0];
	var nameTd=$(keys+"_name");
	keys=keys.split("$");
	var jsObj;
	if(keys.length==1)
	{
		jsObj=currentObj.obj[keys[0]];
		jsObj.value=obj.checked;
	}
	else
	{
		jsObj=currentObj.obj[keys[0]][keys[1]];
		jsObj.value=obj.checked;
	}
	bindRptProperty(currentObj.id,keys,currentObj.obj);
	return jsObj; //js存储对象
}
//标签单击事件
function labelClick(obj)
{
	var valueTd=obj.parentNode;
	var keys=valueTd.id.split("_")[0];
	var nameTd=$(keys+"_name");
	var value=obj.innerHTML;
	keys=keys.split("$");
	switch(keys[0])
	{
		case "linkRptId":		//reportModuleTab	查询报表ID
			break;
	}
	Debug(keys+"="+value);
}

//		if(hashObj)
//		{
//			//根据类型调用
//			bindPropertyValue(keys,hashObj);
//			//var functions="{var keys='"+keys.join(",")+"';keys=keys.split(',');bindPropertyValue(keys);}";
//			//switchFunction(type,functions,true);
//			$("property_value").focus();
//		}
 
/*
	<tr>
		<td height=18 style='width:10px;' nowrap bgcolor='#d4d0c8'> </td>
		<td width='40%' align='left' nowrap bgcolor='#FFFFFF'> </td>
	 	<td width='60%' nowrap bgcolor='#FFFFFF'></td>
	</tr>
*/

