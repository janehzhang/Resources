function proGrid(w,h)
{
	this.type=0;	//	0:确定 取消   1:关闭
	this.modal=false;
	this.width=w?w+"px":"100%";
	this.height=h?h+"px":"100%";
	this.events={};
	this.parent=document.body;
	this.priId=this.uid();
	this.box=document.createElement("DIV");
	this.box.id="proGrid_Box"+this.priId;
	this._buildHTML();
	this.modBox=null;
	

	this.box.prototype=this;
}
proGrid.prototype=new dhtmlXGridObject;

proGrid.prototype.setDim=function(w,h)
{
	if(w)this.width=w+"px";
	if(h)this.height=h+"px";
	this.box.style.width=this.width;
	this.box.style.height=this.height;
};
proGrid.prototype.setButtonName=function(names)
{
	this.buttonNames=names;
}
proGrid.prototype.show=function()
{
	if(this.modal)
	{
		this.modBox.style.display="";
		this.box.style.display="";
	}
	else
	{
		this.box.style.display="";
	}
}
proGrid.prototype.hide=function()
{
	if(this.modal)
		this.modBox.style.display="none";
	else
		this.box.style.display="none";
};
proGrid.prototype._bind=function()
{
	if(this.modal)
	{
		this.modBox=document.createElement("DIV");
		this.modBox.style.zindex=999999999;
		this.modBox.className ="modcss";
		this.modBox.style.position="absolute";
		this.modBox.style.top=0;
		this.modBox.style.left=0;
		this.modBox.style.width=document.documentElement.offsetWidth+"px";
		this.modBox.style.height=document.documentElement.offsetHeight+"px";
		document.body.appendChild(this.modBox);
		this.modBox.appendChild(this.box);
		this.box.style.position="relative";
		this.box.style.top=(document.documentElement.clientHeight-this.box.offsetHeight)/2+"px";
		this.box.style.left=(document.documentElement.clientWidth-parseInt(this.width))/2+"px";
		this.box.style.width=this.width;

	}
	else
	{
		this.parent.appendChild(this.box);
		this.box.style.zindex=999999999;
		this.box.style.position="absolute";
		this.box.style.top=0;
		this.box.style.left=0;
		this.box.style.width=this.width;
		this.box.style.height=this.height;
	}
	
	var cont=$("proGrid_content_"+this.priId);
	this.init();//initialize grid
	this.attachToObject(cont);
	var ch=cont.childNodes(0);
	if(ch.offsetHeight> parseInt(this.height))
		ch.style.height="";
	ch=ch.childNodes(1);
	if(ch.offsetHeight> parseInt(this.height))
	ch.style.height="";
}
//onSubmit onCancel  
proGrid.prototype._buildHTML=function()
{
	var strHtml='<table cellpadding="1" cellspacing="1" border="0" width=100% bgcolor="#999999"><tr bgcolor="#FFFFFF">';
	strHtml+="<td id='proGrid_Con_"+this.priId+"' style='height:"+this.height+";'  ><div  id='proGrid_content_"+this.priId+"' style='overflow:auto;width:"+this.width+";height:"+this.height+";' ></div></td></tr>";
	strHtml+="<tr bgcolor='#FFFFFF'><td height=20px align=center><input value='确 定' type=button id='proGrid_submit_"+this.priId+"' onclick='proGrid.prototype.proGrid_onClick(\"onSubmit\",\""+this.box.id
			+"\")'/> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; ";
	strHtml+="<input value='取 消' type=button  id='proGrid_cancel_"+this.priId+"'  onclick='proGrid.prototype.proGrid_onClick(\"onCancel\",\""+this.box.id+"\")'/> ";
	strHtml+=" </td></tr></table>";
	this.box.innerHTML=strHtml;
	strHtml=null;
}
	
proGrid.prototype.proGrid_onClick=function(ev,proUid)
{
	var pro=$(proUid);
	if(!pro)return;
	var pro=pro.prototype;
	if(!pro)return;
	var res=true;
	switch(ev)
	{
	case "onSubmit":
		if(pro.onSubmit)res=pro.onSubmit(pro);
		break;
	case "onSubmit":
		if(pro.onCancel)res=pro.onCancel(pro);
		break;
	}
	if(res)pro.hide();
}
proGrid.prototype.display=function(obj,mod,type)
{
	if(obj)obj=$(obj);
	if(obj)this.parent=obj;
	this.modal=mod;
	this.type=type;
	this._bind();
	if(type)$("proGrid_submit_"+this.priId).style.display="none";
	else $("proGrid_submit_"+this.priId).style.display="";
};
proGrid.prototype.setPos=function(top,left)
{
	this.box.style.top=top+"px";
	this.box.style.left=left+"px";
	this.box.style.position="absolute";
};

//////////////////////////////test///////////////////////////////

//var mygrid = new proGrid(400,200);
//mygrid.setHeader("Column A, Column B");//set column names
//mygrid.setColAlign("right,left");//set column values align
//mygrid.setColTypes("ro,ed");//set column types
//mygrid.setColSorting("int,str");//set sorting
//mygrid.setSkin("dhx_skyblue");//set grid skin
//mygrid.setInitWidths("100,100");
//mygrid.display(null,1);
////onCancel
//mygrid.onSubmit=function(obj)
//{
//	alert(obj.box.id);
//	return false;
//}
//mygrid.setPos(200,100);
//mygrid.clearAll();
//
//var mygrid = new dhtmlXGridObject();
//	mygrid.setImagePath("./codebase/imgs/");//path to images required by grid
//	mygrid.setHeader("Column A, Column B");//set column names
//	mygrid.setInitWidths("100,250");//set column width in px
//	mygrid.setColAlign("right,left");//set column values align
//	mygrid.setColTypes("ro,ed");//set column types
	
	
	
