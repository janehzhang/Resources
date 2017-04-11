/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        category.js
 *Description：
 *        指标审核页面JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        刘弟伟
 *Finished：
 *       2012-6-06
 *Modified By：
 *
 *Modified Date:
 *
 *Modified Reasons:
 *
 ********************************************************/
/*******************全局变量设置start***********************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var gdlGroupInfo = {};

//页面初始化函数
var pageInit = function(){
    //表类管理标签页应该触发时应该刷新。
    window.parent.addTabRefreshOnActiveByName("指标管理");
        dhx.showProgress("请求数据中");
        GdlExamineAction.getAlertExamineInfo(gdlId,gdlVersion,function(data){
        dhx.closeProgress();
        if(data){
            TermReqFactory.createTermReq(1);//创建一个请求包
            initDIMInfos(data["SUPPORT_DIMS"]);
            
            initGDLBaseInfo(data);
            dhx.showProgress("请求数据中");
            TermReqFactory.getTermReq(1).init(function(val){
                dhx.closeProgress();
            });
            var audit1 = document.getElementById("audit1");
            attachObjEvent(audit1,"onclick",function(){
            	$('effective_time1').disabled = '';
            	$('effective_time2').disabled = '';
            });
             var audit2 = document.getElementById("audit2");
            attachObjEvent(audit2,"onclick",function(){
            	$('effective_time1').disabled = true;
            	$('effective_time2').disabled = true;
            	$('valid_time').disabled = true; 
            });
             var effective_time1 = document.getElementById("effective_time1");
            attachObjEvent(effective_time1,"onclick",function(){
            	$('valid_time').disabled = true;
            });
            var effective_time2 = document.getElementById("effective_time2");
            attachObjEvent(effective_time2,"onclick",function(){
            	$('valid_time').disabled = ''; 
            });
            var newBtn = document.getElementById("newBtn");
            attachObjEvent(newBtn,"onclick",function(){
            	saveGdlInfo();
            });
        }else{
            alert("未找到指标，或指标已被下线!");
        }  
    });
       
     /**
 * 初始维度信息
 * @param support 支撑
 * @param beforsupport
 */
function initDIMInfos(support){
    if(support){
        for(var i=0;i<support.length;i++){
            createDimTr(support[i]);
        }
    }
}
        
//用支撑维度数据，创建一个
function createDimTr(data){
    var tab = document.getElementById("dimtermTable");
    var tr = document.createElement("TR");
    tab.appendChild(tr);
    var th = document.createElement("TD");
    th.innerHTML = data["OLD_DIM_NAME_CN"] == null?data["NEW_DIM_NAME_CN"]:data["OLD_DIM_NAME_CN"];
    tr.appendChild(th);
    th.style.textAlign = "right";
    var td1 = document.createElement("TD");
    td1.innerHTML = data["OLD_GROUP_METHOD_NAME"] == null?"":data["OLD_GROUP_METHOD_NAME"];
    tr.appendChild(td1);
    var td2 = document.createElement("TD");
    var td2val = data["NEW_GROUP_METHOD_NAME"] == null?"":data["NEW_GROUP_METHOD_NAME"];
    if(data["NEW_GROUP_METHOD_NAME"] != data["OLD_GROUP_METHOD_NAME"]){
        td2.innerHTML = "<span style='color:orange'>"+td2val+"</span>";
    }else{
    	td2.innerHTML = td2val;
    }
    tr.appendChild(td2); 
}
        
   //初始指标基本信息
function initGDLBaseInfo(data){
    if(data){
        $("gdl_id").value = data["GDL_ID"]==null?"": data["GDL_ID"];
        $("gdl_version").value = data["GDL_VERSION"]==null?"": data["GDL_VERSION"];
        $("create_user").innerHTML = data["USER_NAMECN"]==null?"":data["USER_NAMECN"];
        $("create_time").innerHTML = data["CREATE_TIME"]==null?"":data["CREATE_TIME"];
        $("gdl_src_table_name").innerHTML = data["CREATE_TIME"]==null?"":data["CREATE_TIME"];
        $("create_time").innerHTML = data["CREATE_TIME"]==null?"":data["CREATE_TIME"];
        $("gdl_src_table_name").innerHTML = data["GDL_SRC_TABLE_NAME"]==null?"":data["GDL_SRC_TABLE_NAME"];
        $("gdl_col_name").innerHTML = data["GDL_COL_NAME"]==null?"":data["GDL_COL_NAME"];
        $("gdl_code").innerHTML = data["GDL_CODE"]==null?"": data["GDL_CODE"];
        $("gdl_name").innerHTML = data["GDL_NAME"]==null?"": data["GDL_NAME"];
        $("gdl_unit").innerHTML = data["GDL_UNIT"]==null?"": data["GDL_UNIT"];
        $("gdl_bus_desc").innerHTML = data["GDL_BUS_DESC"]==null?";": data["GDL_BUS_DESC"].replace(/\n/g,"<br>");
        var beforData = data["GDL_ALERT_BOFOR_INFO"];
        $("alter_version").value = beforData["GDL_VERSION"]==null?"": beforData["GDL_VERSION"];
        $("apply_user").innerHTML = beforData["USER_NAMECN"]==null?"": beforData["USER_NAMECN"];
        $("apply_time").innerHTML = beforData["CREATE_TIME"]==null?"": beforData["CREATE_TIME"];
        $("gdl_code_befor").innerHTML = beforData["GDL_CODE"]==null?"": beforData["GDL_CODE"];
        $("gdl_name_befor").innerHTML = beforData["GDL_NAME"]==null?"": beforData["GDL_NAME"];
        $("gdl_unit_befor").innerHTML = beforData["GDL_UNIT"]==null?"": beforData["GDL_UNIT"];
        $("gdl_bus_desc_befor").innerHTML = beforData["GDL_BUS_DESC"]==null?"": beforData["GDL_BUS_DESC"].replace(/\n/g,"<br>");
        
        if(data["GDL_CODE"] != beforData["GDL_CODE"]){
        	$("gdl_code").style.color = '#EEB422';
        }
        if(data["GDL_NAME"] != beforData["GDL_NAME"]){
        	$("gdl_name").style.color = '#EEB422';
        }
        if(data["GDL_UNIT"] != beforData["GDL_UNIT"]){
        	$("gdl_unit").style.color = '#EEB422';
        }
        if(data["GDL_BUS_DESC"] != beforData["GDL_BUS_DESC"]){
        	$("gdl_bus_desc").style.color = '#EEB422';
        }
        
        var gdlGroup = document.getElementById("gdlGroup");
        var gdlginfos = data["GDL_GROUPINFO"];
        var groupname = [];
        if(gdlginfos){
            for(var i=0;i<gdlginfos.length;i++){
                groupname[i] = gdlginfos[i]["GROUP_NAME"]; 
            }
       }
        $("gdlGroup").innerHTML = groupname.join();
        
        var valid_timeTerm = TermReqFactory.getTermReq(1).createTermControl("valid_time","VALID_TIME");
        valid_timeTerm.setDateRule();
        valid_timeTerm.render();
        valid_timeTerm.myCalendar.showTime();
        valid_timeTerm.myCalendar.setDateFormat("%Y-%m-%d %H:%i:%s");
        valid_timeTerm.myCalendar.setSensitiveRange(new Date());

    }
}

   //保存提交
function saveGdlInfo(){
	var checkValidate = true;
    var tableData = Tools.getFormValues("examine_Form");
    var audit = Tools.getCheckedRadio('audit').value;
    var effectivetime = Tools.getCheckedRadio("effectivetime").value;
    tableData["audit"] = audit;
    tableData["effectivetime"] = effectivetime;
    var validTime = TermReqFactory.getTermReq(1).getTermControl("VALID_TIME").getKeyValue();
    tableData["VALID_TIME"] = validTime;
    var  checkDate = function(){
		var date = new Date();
		var date_time = validTime.split(" "); 
		dateval = date_time[0];
		timeval = date_time[1];
		
		date_arr = dateval.split("-");
		var year = parseInt(date_arr[0]);
		var month = parseInt(date_arr[1])-1;
		var day = parseInt(date_arr[2]);
		
		time_arr = timeval.split(":");
		var HH = parseInt(time_arr[0]);
		var ii = parseInt(time_arr[1]);
		var ss = parseInt(time_arr[2]);
		var date1 = new Date(year,month,day,HH,ii,ss);
		if(date.valueOf() > date1.valueOf()){
		 	alert("延时生效时间不得小于当前时间！");
		 	checkValidate = false;	//验证失败
		}
     }
   
    if(audit ==1 && effectivetime == 2){
    	if(!isDateTime(validTime)){
    		alert('请输入正确的延时生效日期时间！');
			checkValidate = false;	//验证失败
    	}
    	checkDate();
    }
    if($("audit_opinion").value==""){
		alert('请输审核意见！');
		checkValidate = false;	//验证失败
    }
    if(checkValidate){
    	dhx.showProgress("数据审核中");
    GdlExamineAction.alertExamine(tableData,function(data){  
    dhx.closeProgress();
	if(data == '1'){
        	dhx.alert("操作成功",function(){
        	window.parent.closeTab("指标审核");
        });
        
        }else{
        	dhx.alert("操作失败",function(){
        		window.parent.closeTab("指标审核");
        	});
        }
    });
    }
}
	  
}//end pageInit
dhx.ready(pageInit);