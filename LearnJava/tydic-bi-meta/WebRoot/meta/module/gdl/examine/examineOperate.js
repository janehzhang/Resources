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
        GdlExamineAction.getExamineInfoByGdlId(gdlId,gdlVersion,function(data){
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
		        saveGdlInfo(data['SUPPORT_DIMS']);
            });
        }else{
            alert("未找到指标，或指标已被下线!");
        }  
    });
       
     /**
 * 初始维度信息
 * @param support 支撑
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
    th.innerHTML = data["DIM_NAME_CN"];
    tr.appendChild(th);
    th.style.textAlign = "right";

    var td = document.createElement("TD");
    tr.appendChild(td);
    var dimterm = TermReqFactory.getTermReq(1).createTermControl(td,"DIM_"+data["DIM_TABLE_ID"]);
    dimterm.setAppendData([["","不支撑"]])
    if(data["GROUP_METHOD"] != null){
    	dimterm.setCodeRule("GDL_GROUP_METHOD",data["GROUP_METHOD"]);
    }else{
    	dimterm.setCodeRule("GDL_GROUP_METHOD","");
    }
    dimterm.enableReadonly(true);
    
}
        
   //初始指标基本信息
function initGDLBaseInfo(data){
    if(data){
        $("gdl_id").value = data["GDL_ID"]==null?"": data["GDL_ID"];
        $("gdl_code").value = data["GDL_CODE"]==null?"": data["GDL_CODE"];
        $("gdl_name").value = data["GDL_NAME"]==null?"": data["GDL_NAME"];
        $("gdl_type").innerHTML = data["GDL_TYPE_NAME"]==null?"":data["GDL_TYPE_NAME"];
        $("gdl_state").innerHTML = data["AUDIT_STATE_NAME"]==null?"":data["AUDIT_STATE_NAME"];
        $("gdl_unit").value = data["GDL_UNIT"]==null?"": data["GDL_UNIT"];
        $("gdl_src_table_name").innerHTML = data["GDL_SRC_TABLE_NAME"]==null?"":data["GDL_SRC_TABLE_NAME"];
        $("gdl_col_name").innerHTML = data["GDL_COL_NAME"]==null?"":data["GDL_COL_NAME"];
        $("user_namecn").innerHTML = data["USER_NAMECN"]==null?"":data["USER_NAMECN"];
        $("create_time").innerHTML = data["CREATE_TIME"]==null?"":data["CREATE_TIME"];
        $("gdl_bus_desc").value = data["GDL_BUS_DESC"]==null?"": data["GDL_BUS_DESC"].replace(/\n/g,"<br>");;
        $("gdl_version").value = data["GDL_VERSION"]==null?"": data["GDL_VERSION"];
        var gdlGroup = document.getElementById("gdlGroup");
        var gdlginfos = data["GDL_GROUPINFO"];
        if(gdlginfos){
            for(var i=0;i<gdlginfos.length;i++){
                gdlGroupInfo[gdlginfos[i]["GDL_GROUP_ID"]] = 1;
            }
        }
        var groupTerm = TermReqFactory.getTermReq(1).createTermControl("gdlGroup","GDL_GROUP");
        groupTerm.setClassRule("tydic.meta.module.gdl.group.GdlGroupTreeImpl",2);
        groupTerm.mulSelect = true;
        groupTerm.setWidth(290);
        groupTerm.render();
        groupTerm.selTree.enableMuiltselect(true,function(d){
            return d[2]!="0";//根分类不允许选择
        });
        groupTerm.setBindDataCall(function(t){
            var selTree=t.selTree;
            t.selTree.tree.openAllItems(selTree.tree.rootId);
            for(var key in gdlGroupInfo){
                selTree.tree.setCheck(selTree._priFix+key,true);
            }
            var checkIdStr=selTree.tree.getAllChecked();
            if(checkIdStr=="")return;
            t.inputObj.setAttribute("code",checkIdStr);
            checkIdStr=checkIdStr.split(",");
            t.inputObj.value="";
            for(var i=0;i<checkIdStr.length;i++){
                t.inputObj.checkIds[checkIdStr[i]]=selTree.tree.getItemText(checkIdStr[i]);
                if(t.inputObj.value)
                    t.inputObj.value+=","+t.inputObj.checkIds[checkIdStr[i]];
                else
                    t.inputObj.value = t.inputObj.checkIds[checkIdStr[i]];
            }
        });
        
        var valid_timeTerm = TermReqFactory.getTermReq(1).createTermControl("valid_time","VALID_TIME");
        valid_timeTerm.setDateRule();
        valid_timeTerm.render();
        valid_timeTerm.myCalendar.showTime();
        valid_timeTerm.myCalendar.setDateFormat("%Y-%m-%d %H:%i:%s");
        valid_timeTerm.myCalendar.setSensitiveRange(new Date());
    }
}
        
   //保存提交
function saveGdlInfo(supos){
	var checkValidate = true;		//验证信息
    var tableData = Tools.getFormValues("examine_Form");
    var audit = Tools.getCheckedRadio('audit').value;
    var effectivetime = Tools.getCheckedRadio("effectivetime").value;
    tableData["audit"] = audit;
    tableData["effectivetime"] = effectivetime;
    var dimMethods = [];
    for(var i=0;i<supos.length;i++){
    	var methodTerm = TermReqFactory.getTermReq(1).getTermControl("DIM_"+supos[i]['DIM_TABLE_ID']);
    	dimMethods[i] = supos[i];
    	dimMethods[i]['GROUP_METHOD'] = methodTerm.getKeyValue();
    }
    var gdlGroup = TermReqFactory.getTermReq(1).getTermControl("GDL_GROUP").getKeyValue();
    tableData["GDL_GROUPINFO"] = gdlGroup;
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
    var gdl_code = $("gdl_code").value;
    if(gdl_code == ""){
		alert('请输入编码！');
		checkValidate = false;	//验证失败
		return;
    }
    if($("gdl_name").value == ""){
		alert('请输入名称！');
		checkValidate = false;	//验证失败
    }
    if(gdlGroup == ""){
        alert('请选择指标分类！');
        checkValidate = false;	//验证失败
	        }
    if($("gdl_unit").value == ""){
		alert('请输入单位！');
		checkValidate = false;	//验证失败
    }
    if($("gdl_bus_desc").value == ""){
		alert('请输入业务解释！');
		checkValidate = false;	//验证失败
    }
    if(audit ==1 && effectivetime == 2){
    	if(!isDateTime(validTime)){
    		alert('请输入正确的延时生效日期时间！');
			checkValidate = false;	//验证失败
    	}
    	checkDate();
    }
    if($("audit_opinion").value == ""){
		alert('请输审核意见！');
		checkValidate = false;	//验证失败
    }
	if(checkValidate){
		 //验证指标编码是否存在
	     GdlBasicMagAction.checkGdlCode(gdl_code,gdlId,function(rs){
         if(rs){
	           /*dhx.*/alert('指标编码'+gdl_code+'已经存在，请重新填写！');
		 		return;
         }else{
			    dhx.showProgress("数据审核中");
				GdlExamineAction.addExamine(tableData,dimMethods,function(data){  
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
	 });
    }
}
	  
}//end pageInit
dhx.ready(pageInit);