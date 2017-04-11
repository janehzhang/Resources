/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       addGdlBasic.js
 *Description：
 *       指标管理-创建基础指标
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-06-07
 ********************************************************/

var dimData = null;		//维度数据
var gdlData = null;		//指标数据
var dataTable = null;	//数据表
var dataTable2 = null;	//数据表2
var groupTree = null;	//分类树
var validateId = [];	//需要添加验证的id行
var gdlDimCalculation = null;	//维度计算方式
var gdlNumformat = null;	//默认展示格式
var addGdlArray = null;	//添加的指标列
//初始界面
function pageInit() {
	gdlDimCalculation = getCodeByType("GDL_GROUP_METHOD");		//初始化维度计算方式
	gdlNumformat = getCodeByType("GDL_NUMFORMAT");	//初始化默认展示格式
    dhx.showProgress("请求数据中");
    GdlBasicMagAction.queryColsByTableId(tableId,function(data){
        dhx.closeProgress();
        gdlData = data.gdlData;
        dimData = data.dimData;
		dataTableInit(); //初始数据表格  
		dataTableValidateId(); //绑定验证信息 
		
		dataTable2Init();	//初始化grid表格2
		
		$('dataGridDiv2').style.display = "none";
		$('buttonDiv2').style.display = "none";
    });
    
    /**************添加按钮事件*****************/
    attachObjEvent($('nextStep'),"onclick",function(){	//下一步按钮
    	var gdlCodeArray = new Array();
    	addGdlArray = new Array();
		var checkValidate = true;		//验证信息
        var isCheckedOne = false;		//是否选中一条
        var allRows = dataTable.grid.getAllRowIds().split(',').unique();
        for(var i=0;i<allRows.length;i++) {
        	var tableColId = dataTable.getUserData(allRows[i],"COL_ID");	//得到列id
        	if(!$('ADD_SELECT_'+allRows[i])) {	//如果不存在复选框，表示该列为已经添加指标
       			continue;
       		}
            if(!$('ADD_SELECT_'+allRows[i]).checked){	//当没选中时，跳过
                continue;
            }
            isCheckedOne = true;
	        //验证信息
	        if(!dhtmlxValidation.validate(("GDL_NAME_"+tableColId))||!dhtmlxValidation.validate(("GDL_CODE_"+tableColId))) {
				checkValidate = false;	//验证失败
	        	break;
		    }
	        
	        /********判断指标编码是否重复***********/
	        var isExits = false;		//是否存在重复
	        var gdlCode = $("GDL_CODE_"+tableColId).value;
			for(var j=0;j<gdlCodeArray.length;j++){
				if(gdlCodeArray[j]["GDL_CODE"] == gdlCode){
					isExits = true;
					break;
				}
			}
			if(isExits){
				checkValidate = false;	//验证失败
        		dhx.alert('指标编码 '+gdlCode+' 重复，请重新填写！');
				break;
			}
			
	        gdlCodeArray.push({"GDL_CODE":gdlCode});		//把值存进去
	        /********判断指标编码是否重复***********/
	        
	        var data = dataTable.getUserData(allRows[i]);
	        data["GDL_NAME"] = $("GDL_NAME_"+tableColId).value;		//指标名
	        data["GDL_CODE"] = $("GDL_CODE_"+tableColId).value;		//指标编码
	        
	        var dimArray = new Array();
			for(var j=0;j<dimData.length;j++){
				var dimMethod = $("DIM_"+tableColId+"_"+dimData[j].DIM_TABLE_ID).value;		//维度支持方式
				if(dimMethod != ""){
					var addDimData = dhx.extend({},dimData[j]);  //需要添加的维度列(方法第一个参数，继承第二个参数所有属性)
					addDimData["GROUP_METHOD"] = dimMethod;		//设置该维度支撑方式
					dimArray.push(addDimData);	//添加该维度	
				}
			}
			data["DIM_INFO"] = dimArray;	//设置关联维度
            addGdlArray.push(data);	//保存新选择的字段，以便新增
        }
        if(!isCheckedOne){
        	dhx.alert('请至少选择一条数据！');
        	return;
        }
        
        //验证指标编码是否存在
		GdlBasicMagAction.checkGdlCodeByAdd(gdlCodeArray,function(checkData){
			if(checkData["IS_EXITS"]){
	        	dhx.alert('指标编码 '+checkData["GDL_CODE"]+' 已经存在，请重新填写！');
	        	return;
			}else{
		        if(checkValidate){
					$('dataGridDiv').style.display = "none";
					$('buttonDiv').style.display = "none";
					$('dataGridDiv2').style.display = "";
					$('buttonDiv2').style.display = "";
			    	dataTable2.bindData(addGdlArray);	//绑定数据
			    	initGroupTree(addGdlArray);		//加载分类树数据
		        }
			}
		});
    });
    attachObjEvent($('upStep'),"onclick",function(){	//上一步按钮
		$('dataGridDiv').style.display = "";
		$('buttonDiv').style.display = "";
		$('dataGridDiv2').style.display = "none";
		$('buttonDiv2').style.display = "none";
    });
    attachObjEvent($('submitAudit'),"onclick",function(){	//提交审核按钮
    	addGdlArray = new Array();		//提交信息
    	var checkValidate = true;		//验证信息
        var allRows = dataTable2.grid.getAllRowIds().split(',').unique();
        for(var i=0;i<allRows.length;i++) {
        	var tableColId = dataTable.getUserData(allRows[i],"COL_ID");	//得到列id
	        var gdlGroupIdS = $("GDL_GROUP_"+tableColId).getAttribute("groupIds");	//指标分类ids
	        var gdlUnit = $("GDL_UNIT_"+tableColId).value;					//指标单位
	        var gdlBusDesc = $("GDL_BUS_DESC_"+tableColId).value;			//指标业务解释
	        var numFormat = $("NUM_FORMAT_"+tableColId).value;				//指标默认展示方式

	        if(gdlGroupIdS==""){
        		dhx.alert('请选择指标分类！');
        		checkValidate = false;	//验证失败
	        	break;
	        }
	        if(gdlUnit==""){
        		dhx.alert('请输入单位！');
        		checkValidate = false;	//验证失败
	        	break;
	        }
	        if(gdlBusDesc==""){
        		dhx.alert('请输入业务解释！');
        		checkValidate = false;	//验证失败
	        	break;
	        }
	        
	        var data = dataTable.getUserData(allRows[i]);
	        data["GDL_GROUP_IDS"] = gdlGroupIdS;
	        data["GDL_UNIT"] = gdlUnit;
	        data["GDL_BUS_DESC"] = gdlBusDesc;
	        data["NUM_FORMAT"] = numFormat;
            addGdlArray.push(data);	//保存新选择的字段，以便新增
        }
        if(checkValidate){
    		dhx.showProgress("数据提交中");
	   		GdlBasicMagAction.insertGdlBasic(addGdlArray,function(rs){
				dhx.closeProgress();
	   			if(rs){
        			dhx.alert('提交成功！');
        			//审核通过后，刷新数据
				    GdlBasicMagAction.queryColsByTableId(tableId,function(data){
				        gdlData = data.gdlData;
			        	dimData = data.dimData;
	    				validateId = [];				//清空待验证数据
	    				dataTable.bindData(gdlData);	//绑定数据
	    				dataTableValidateId();			//绑定验证信息
						$('dataGridDiv').style.display = "";
						$('buttonDiv').style.display = "";
						$('dataGridDiv2').style.display = "none";
						$('buttonDiv2').style.display = "none";
			        });
	   			}else{
        			dhx.alert('提交失败！请联系管理员！');
	   			}
			});
        }
    });
    /**************添加按钮事件*****************/
    
	
}

//初始数据grid
function dataTableInit(){
	/*****************动态grid列开始**********************/
	var cols = {
        CHECK_BOX:"选择",
        COL_NAME:"列名",
        GDL_NAME:"指标名称",
        GDL_CODE:"指标编码"
    };
	var headerStr = "<input type='checkbox' id='allCheckBox' checked='checked' onClick='changeSelect(this)' />," +
		"列名,指标名称<span style='color:red;'>*</span>,指标编码<span style='color:red;'>*</span>";		//第一行头信息
	var headerAlign= ["vertical-align:middle;","text-align:center;vertical-align:middle;font-weight:bold;",
		"text-align:center;vertical-align:middle;font-weight:bold;","text-align:center;vertical-align:middle;font-weight:bold;"];		//第一行头信息位置方式
	var attachHeaderStr = "#rspan,#rspan,#rspan,#rspan"; //第二行头信息
	var attachHeaderAlign= ["text-align:center;font-weight:bold;","text-align:center;font-weight:bold;",
		"text-align:center;font-weight:bold;","text-align:center;font-weight:bold;"];				//第二行头信息位置方式
	var colsValueName = "COL_ID,COL_NAME,GDL_NAME,GDL_CODE";	//绑定对应变量值
	//遍历维度列，添加维度动态列信息
	for(var i=0;i<dimData.length;i++){
		cols["DIM_"+dimData[i].DIM_TABLE_ID] = dimData[i].DIM_TABLE_NAME_CN;	
		if(i==0){
			headerStr += ",维度分组计算方式";
			headerAlign.push("text-align:center;font-weight:bold;");
		}else{
			headerStr += ",#cspan";
		}
		attachHeaderStr += ","+dimData[i].DIM_TABLE_NAME_CN;
		attachHeaderAlign.push("text-align:center;font-weight:bold;");
	}
	/*****************动态grid列结束**********************/
	
	var dtatGridDiv = document.getElementById("dataGridDiv");
    var pageContent = document.getElementById("pageContent");
    var buttonDiv = document.getElementById("buttonDiv");
    dtatGridDiv.style.height = pageContent.offsetHeight-buttonDiv.offsetHeight + "px";
    dataTable = new meta.ui.DataTable("dataGridDiv",false);//第二个参数表示是否是表格树
    dataTable.setColumns(cols,colsValueName);
    dataTable.setRowIdForField("COL_ID","COL_ID");
    dataTable.setPaging(false);//分页
    dataTable.setSorting(false);
    dataTable.render();//绘制函数，一些set方法必须在绘制函数之前，绘制函数之后内置的源生dhtmlxGrid对象被初始
    
    dataTable.grid.setEditable(false);		//设置grid不可编辑
    dataTable.grid.setHeader(headerStr,null,headerAlign);
	dataTable.grid.attachHeader(attachHeaderStr,attachHeaderAlign);
    dataTable.setGridColumnCfg(0,{align:'center'});		//设置第一列复选款位置居中
    /***************动态计算宽度********************/
    var sumNumber = 62;		//总宽度百分比，剩余3%作为复选框位置
    var avlWidths = parseInt(sumNumber/dimData.length);		//平均宽度，去掉余数取整
    var widthsP = "";		//分配百分比
    var widht = 0;
    for(var i=0;i<dimData.length;i++){
    	if(i!=0){
    		widthsP += ","+avlWidths;
    	}
    }
    //如果宽度比大于所有平均宽度之和，表示存在差数，添加差数都最后一个百分比数上
    if(sumNumber>(avlWidths*dimData.length)){
    	avlWidths += sumNumber-(avlWidths*dimData.length);
    }
    widthsP += ","+avlWidths;
    dataTable.grid.setInitWidthsP("3,13,11,11"+widthsP);			//列宽度
    /***************动态计算宽度********************/
    //绑定值时调用方法
    dataTable.setFormatCellCall(function(rid,cid,data,colId){
        var tableColId = dataTable.getUserData(rid,"COL_ID");	//得到列id
        var gdlId = dataTable.getUserData(rid,"GDL_ID");	//得到指标id
    	//为维度值绑定下拉列表
		for(var i=0;i<dimData.length;i++){
	    	if(colId == ("DIM_"+dimData[i].DIM_TABLE_ID)){
	    		var str = "";
	    		if(!gdlId){	//当gdlId不存在时，表示未添加指标，
	    			str += "<select onclick='cancelBubble()' id='"+("DIM_"+tableColId+"_"+dimData[i].DIM_TABLE_ID)+"'>";
					for(var m=0;m<gdlDimCalculation.length;m++){
						str += "<option value='"+gdlDimCalculation[m].value+"'>"+gdlDimCalculation[m].name+"</option>";
					}
					str += "<option value=''>不支持</option></select>";
	    		}
	    		return str;
	    		break;
	    	}
		}
    	if(colId == "CHECK_BOX"){	//复选框
    		if(gdlId){	//存在指标id时，表示该列已经添加指标
    			return '';
    		}else{
    			return '<input type="checkbox" value="'+tableColId+'" id="ADD_SELECT_'+tableColId+'" name="selectCheck" checked="checked" />';
    		}
    	}
    	if(colId == "GDL_NAME"){	//指标名称
    		if(gdlId){
        		return data[cid];
    		}else{
    			validateId.push(tableColId);	//添加到验证的id行
    			
    			var colNameCn = dataTable.getUserData(rid,"COL_NAME_CN");		//列中文名
    			str = "<input onclick='cancelBubble()' type='text' id='GDL_NAME_"+tableColId+"' value='"+(colNameCn?colNameCn:'')+"' style='width:141px;' />";
    			return str;
    		}
    	}
    	if(colId == "GDL_CODE"){	//指标编码
    		if(gdlId){
        		return data[cid];
    		}else{
    			str = "<input onclick='cancelBubble()' type='text' id='GDL_CODE_"+tableColId+"' value='' style='width:141px;' />";
    			return str;
    		}
    	}
        return data[cid];
    });

    dataTable.bindData(gdlData);	//绑定数据
    return dataTable;
}

//添加验证信息
function dataTableValidateId(){
	for(var i=0;i<validateId.length;i++){
    	dhtmlxValidation.addValidation($('GDL_NAME_'+validateId[i]),"NotEmpty,MaxLength[64]");
    	dhtmlxValidation.addValidation($('GDL_CODE_'+validateId[i]),"NotEmpty,MaxLength[12]");
	}
}

//初始数据grid
function dataTable2Init(){
	var dtatGridDiv = document.getElementById("dataGridDiv2");
    var pageContent = document.getElementById("pageContent");
    var buttonDiv = document.getElementById("buttonDiv2");
    dtatGridDiv.style.height = pageContent.offsetHeight-buttonDiv.offsetHeight + "px";
    dataTable2 = new meta.ui.DataTable("dataGridDiv2",false);//第二个参数表示是否是表格树
    dataTable2.setColumns({
        GDL_NAME:"指标名称",
        GDL_CODE:"指标编码",
        COL_NAME:"列名",
        GDL_GROUP:"指标分类<span style='color:red;'>*</span>",
        COL_UNIT:"单位<span style='color:red;'>*</span>",
        GDL_BUS_DESC:"业务解释<span style='color:red;'>*</span>",
        NUM_FORMAT:"默认展示格式"
    },"GDL_NAME,GDL_CODE,COL_NAME");
    dataTable2.setRowIdForField("COL_ID","COL_ID");
    dataTable2.setPaging(false);//分页
    dataTable2.setSorting(false);
    dataTable2.render();//绘制函数，一些set方法必须在绘制函数之前，绘制函数之后内置的源生dhtmlxGrid对象被初始
    
    dataTable2.grid.setEditable(false);		//设置grid不可编辑
    dataTable2.setFormatCellCall(function(rid,cid,data,colId){
        var tableColId = dataTable.getUserData(rid,"COL_ID");	//得到列id
		if(colId == "GDL_GROUP"){	//指标分类
			return "<input onclick='cancelBubble()' readOnly='readOnly' type='text' id='GDL_GROUP_"+tableColId+"' value='' groupIds='' />";
		}
		if(colId == "COL_UNIT"){	//指标单位
			return "<input onclick='cancelBubble()' type='text' id='GDL_UNIT_"+tableColId+"' value='' />";
		}
		if(colId == "GDL_BUS_DESC"){	//业务解释
			return "<input onclick='cancelBubble()' type='text' id='GDL_BUS_DESC_"+tableColId+"' value='' />";
		}
		if(colId == "NUM_FORMAT"){	//默认展示格式
			var str = "<select onclick='cancelBubble()' id='"+("NUM_FORMAT_"+tableColId)+"'>";
			for(var m=0;m<gdlNumformat.length;m++){
				str += "<option value='"+gdlNumformat[m].value+"'>"+gdlNumformat[m].name+"</option>";
			}
			str += "</select>";
			return str;
		}
    	return data[cid];
    });

    return dataTable2;
}

var groupData = null;	//分类数据
//初始分类树
function initGroupTree(arr){
	var orjArr = [];		//需要绑定的input框对象集合
	//得到所有需要绑定input框对象id
	for(var i=0;i<arr.length;i++){
		orjArr[orjArr.length]=("GDL_GROUP_"+arr[i]["COL_ID"]);
		//为input框绑定点击事件，初始化值
		attachObjEvent($("GDL_GROUP_"+arr[i]["COL_ID"]),"onclick",function(){
			//取消所有选中的值
			var groupObj = groupTree.bindInput;	//得到当前对象
			var childs = groupTree.tree.getAllCheckedBranches();	//得到所有选中的值
            if(childs){
                var childIds = childs.toString().split(",");
                for(var j=0;j<childIds.length;j++){
                	groupTree.tree.setCheck(childIds[j],false);		//取消所有选中值
                }
            }
            //根据已经选中的值，选中tree的对应的值
			if(groupObj.getAttribute("groupIds")){
	    		var groupIds = groupObj.getAttribute("groupIds").split(",");	//得到所有选中的值
	            for(var j=0;j<groupIds.length;j++){
	            	groupTree.tree.setCheck(groupTree._priFix+groupIds[j],true);	//设置对应input框选中值选中
	            }
			}
		});
	}
	groupTree = new meta.ui.selectTree(orjArr,300,220);
	groupTree.enableMuiltselect(true,function(d){
        return d[2]!="0";//根分类不允许选择
    });
	groupTree.enableAutoSize(true);
	
	//设置tree选中事件
	groupTree.tree.attachEvent("onCheck",function(itemid,state){
		var groupObj = groupTree.bindInput;		//得到点击对象
		var allCheck = groupTree.getCheckedValue();	//得到所有选中值
		var groupIds = "";		//分类ids
		var groupText = "";		//分类名
		if(allCheck){
			for(var i=0;i<allCheck.length;i++){
				if(i==0){
					groupIds += allCheck[i][0];
					groupText += allCheck[i][1];
				}else{
					groupIds += ","+allCheck[i][0];
					groupText += ","+allCheck[i][1];
				}
            }
		}
		groupObj.value = groupText;		//设置input框显示值
		groupObj.setAttribute("groupIds",groupIds); //设置后台存入值
	});
	
	if(groupData){//如果已经存在查询过的数据，直接绑定
        groupTree.appendData(groupData);	
	}else{
		//初始绑定数据
		dhx.showProgress("数据请求中");
		GdlBasicMagAction.quertAllData(function(data){
		    dhx.closeProgress();
		    groupData = data;
            groupTree.appendData(data);
		});
	}
} 

//全选，全不选效果
function changeSelect(obj){
	var checkBoxs = document.getElementsByName("selectCheck");
	for(var i=0; i<checkBoxs.length; i++){
		checkBoxs[i].checked = obj.checked;
	}
}

//取消grid上输入框需要点击两次才能光标定位的问题
function cancelBubble(e) {
    e = e || window.event;
    if (!e)return false;
    if (e.preventDefault) {
        e.preventDefault();
    }
    e.cancelBubble = true;
    return false;
}

dhx.ready(pageInit);