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
        GdlExamineAction.getAlertExamineInfo_View(gdlId,gdlVersion,function(data){
        dhx.closeProgress();
        if(data){
            TermReqFactory.createTermReq(1);//创建一个请求包
            initDIMInfos(data["SUPPORT_DIMS"]);
            initGDLBaseInfo(data);
            dhx.showProgress("请求数据中");
            TermReqFactory.getTermReq(1).init(function(val){
                dhx.closeProgress();
            });
             var newBtn = document.getElementById("Back");
             attachObjEvent(newBtn,"onclick",function(){
            	window.parent.closeTab("全息视图");  
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
        $("create_user").innerHTML = data["USER_NAMECN"]==null?"":data["USER_NAMECN"];
        $("create_time").innerHTML = data["CREATE_TIME"]==null?"":data["CREATE_TIME"];
        $("gdl_src_table_name").innerHTML = data["CREATE_TIME"]==null?"":data["CREATE_TIME"];
        $("create_time").innerHTML = data["CREATE_TIME"]==null?"":data["CREATE_TIME"];
        $("gdl_src_table_name").innerHTML = data["GDL_SRC_TABLE_NAME"]==null?"":data["GDL_SRC_TABLE_NAME"];
        $("gdl_col_name").innerHTML = data["GDL_COL_NAME"]==null?"":data["GDL_COL_NAME"];
        $("gdl_code").innerHTML = data["GDL_CODE"]==null?"": data["GDL_CODE"];
        $("gdl_name").innerHTML = data["GDL_NAME"]==null?"": data["GDL_NAME"];
        $("gdl_unit").innerHTML = data["GDL_UNIT"]==null?"": data["GDL_UNIT"];
        $("gdl_bus_desc").innerHTML = data["GDL_BUS_DESC"]==null?"": data["GDL_BUS_DESC"].replace(/\n/g,"<br>");
        var beforData = data["GDL_ALERT_BOFOR_INFO"];
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
        $("audit_result").innerHTML  = data["AUDIT_STATE_NAME"]==null?"":data["AUDIT_STATE_NAME"];
        $("audit_opinion").innerHTML = data["AUDIT_OPINION"]==null?"":data["AUDIT_OPINION"];

    }
}
        
}//end pageInit
dhx.ready(pageInit);