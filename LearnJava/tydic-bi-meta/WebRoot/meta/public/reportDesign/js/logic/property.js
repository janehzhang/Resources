//属性窗口参数
var property_params={
	property_dragging:false,
	width:0,
	height:0,
	tx:0,
	ty:0,
	Handle:null,
	property_Vdragging:false,
	term_dragging:false,
	outObj:null,
	inObj:null
	
	};

function change_property_state()
{
	var isCheck;
	if(proConer.style.display!="")
	{
//		unhideObj(proConerId);
		proConer.style.display="";
		proMoveHandle.style.display="";
		isCheck=true;
	}
	else
	{
//		hideObj(proConerId);
		proConer.style.display="none";
		proMoveHandle.style.display="none";
		isCheck=false;
	}
	//菜单状态同步
	menu.setCheckboxState("view_pro_window",isCheck );
	//工具栏同步
	//mb.items[2].subMenu.items[1].parentMenu.invalidate();
	reSetControl();
	return;
}
function property_down()
{
	if(window.event.srcElement.id!=proMoveHandleId)return;
	//proMoveHandle.attachEvent("onmousemove",property_Change);
	//proMoveHandle.attachEvent("onmouseup",property_up);
	proMoveHandle.style.cursor="col-resize";
	document.body.style.cursor="col-resize";
	property_params.property_dragging=true;
	property_params.tx=window.event.clientX;
	property_params.Handle=proMoveHandle.cloneNode();
	proMoveHandle.parentNode.appendChild(property_params.Handle);
	property_params.Handle.style.position="absolute";
	property_params.Handle.style.left=proMoveHandle.offsetLeft;
	property_params.Handle.style.backgroundColor="#000000";
	property_params.Handle.id="DmoveHandle";
	property_params.Handle.style.zIndex =10;
	document.onselectstart=function(){return false;};
}
function property_move()
{
	if(!property_params.property_dragging)return;
	if(proConer.offsetWidth-(window.event.clientX-property_params.tx)<=450 && 
	proConer.offsetWidth-(window.event.clientX-property_params.tx)>=200)
	{
		property_params.width=window.event.clientX-property_params.tx;
		property_params.Handle.style.left=window.event.clientX-1;//property_params.width;
	}
}
function property_up()
{
	document.body.style.cursor="";
	if(!property_params.property_dragging)return;
	property_params.property_dragging=false;
	property_params.Handle.parentNode.removeChild(property_params.Handle);
	if(property_params.width==0)return;
	property_params.Handle=null; //
	proConer.style.width=proConer.offsetWidth-property_params.width;	
	reSetControl();
	property_params.width=0;
	Debug("property_up: "+proConer.offsetWidth+":"+property_params.width);
	//moveHandle.attachEvent("onmousemove",null);
	document.onselectstart=function(){return true;}; 
}
function property_Vdown()
{
	if(window.event.srcElement.id!=proMoveVHandleId)return;
	document.body.style.cursor="n-resize";
	property_params.property_Vdragging=true;
	property_params.ty=window.event.clientY;
	property_params.Handle=proMoveVHandle.cloneNode();
	//document.body.appendChild(property_params.Handle);
	proMoveVHandle.parentNode.appendChild(property_params.Handle);
	property_params.Handle.style.position="absolute";
	property_params.Handle.style.top=proMoveVHandle.offsetTop;
	property_params.Handle.style.backgroundColor="#000000";
	property_params.Handle.id="VmoveHandle";
	property_params.Handle.innerHTML="<img src='"+imageRoot+"images/tileback.gif'/>";
	property_params.Handle.style.zIndex =1100;
	document.onselectstart=function(){return false;};
}
function property_Vmove()
{
	if(!property_params.property_Vdragging)return;
	if(proIntro.offsetHeight-(window.event.clientY-property_params.ty)<=250 && 
	proIntro.offsetHeight-(window.event.clientY-property_params.ty)>=70)
	{
		property_params.width=window.event.clientY-property_params.ty;
		property_params.Handle.style.top=proMoveVHandle.offsetTop+property_params.width;
	}
}
function property_Vup()
{
	document.body.style.cursor="";
	if(!property_params.property_Vdragging)return;
	property_params.property_Vdragging=false;	
	property_params.Handle.parentNode.removeChild(property_params.Handle);
	if(property_params.width==0)return;
	property_params.Handle=null;
	proList.style.height=proList.offsetHeight+property_params.width;
	proIntro.style.height=proIntro.offsetHeight-property_params.width;
	proMoveVHandle.style.top=proList.offsetHeight+proList.offsetTop;
	document.onselectstart=function(){return true;}; 
	property_params.width=0;
}

//生成属性选项

