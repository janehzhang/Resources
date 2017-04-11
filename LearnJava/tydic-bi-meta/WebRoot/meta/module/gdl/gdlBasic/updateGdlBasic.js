/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       addGdlBasic.js
 *Description：
 *       指标管理-修改基础指标
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-06-12
 ********************************************************/

var groupTree = null;		//分类树
var gdlGroupMethod = "";	//维度计算方式
var gdlNumformat = null;	//默认展示格式
var gdlData = null;
function pageInit() {
	gdlGroupMethod = getCodeByType("GDL_GROUP_METHOD");		//初始化维度计算方式
	gdlNumformat = getCodeByType("GDL_NUMFORMAT");	//初始化默认展示格式
	//初始化默认展示格式下拉列表
	for(var m=0;m<gdlNumformat.length;m++){
		var option=new Option(gdlNumformat[m].name,gdlNumformat[m].value);
  		$('num_frommat').options[m]=option;
	}
    dhx.showProgress("请求数据中");
	GdlBasicMagAction.queryGdlInfoById(gdlId,gdlVersion,function(data){
        dhx.closeProgress();
        gdlData = data;
        
        //如果存在值，表示该指标存在未审核
    	if(data["GDL_ALTER_HISTOTY_ID"]){
    		/*dhx.*/alert("该指标已经存在审核中，目前无法修改！请先审核后再修改！");
    		commitUpdate.disabled="disabled";		//禁用保存按钮
    	}
        
		$('gdl_code').value = data["GDL_CODE"]?data["GDL_CODE"]:"";		//指标编码
		$('gdl_name').value = data["GDL_NAME"]?data["GDL_NAME"]:"";		//指标名称
		$('gdl_type').innerHTML = getNameByTypeValue("GDL_TYPE",data["GDL_TYPE"]);		//指标类型
		$('gdl_state').innerHTML = getNameByTypeValue("GDL_STATE",data["GDL_STATE"]);	//指标状态
		$('gdl_unit').value = data["GDL_UNIT"]?data["GDL_UNIT"]:"";		//单位
		$('gdl_group').value = data["GDL_GROUP_NAME"]?data["GDL_GROUP_NAME"]:"";//指标分类名
		$('gdl_group').setAttribute("groupIds",data["GDL_GROUP_IDS"]?data["GDL_GROUP_IDS"]:"");	//保存分类ids
		$('num_frommat').value = data["NUM_FORMAT"]?data["NUM_FORMAT"]:"";		//默认展示格式
		$('gdl_src_table_name').innerHTML = data["GDL_SRC_TABLE_NAME"]?data["GDL_SRC_TABLE_NAME"]:"";	//来源表类
		$('gdl_src_col').innerHTML = data["GDL_SRC_COL"]?data["GDL_SRC_COL"]:"&nbsp;";	//来源表类
		$('user_namecn').innerHTML = data["USER_NAMECN"]?data["USER_NAMECN"]:"&nbsp;";	//创建人
		$('create_time').innerHTML = data["CREATE_TIME"]?data["CREATE_TIME"]:"&nbsp;";	//创建时间
		$('gdl_bus_desc').value = data["GDL_BUS_DESC"]?data["GDL_BUS_DESC"]:"";	//业务解释
		
		setDimGrid(data["DIM_DATA"]);	//设置支持维度
		initGroupTree();		//初始化分类树
	});
	
	createValidate();		//添加验证信息
	
    attachObjEvent($('commitUpdate'),"onclick",function(){	//保存修改
        //验证信息
        if(dhtmlxValidation.validate("_tableBaseInfo")) {
    		updateGDL();
        }
    });
}

function updateGDL(){
	var isOnlyUpdateGroup = true;	//是否只修改分类信息
	var isUpdateCheck = false;		//判断是否修改了信息
	var gdl_code = $('gdl_code').value;			//指标编码
	var gdl_name = $('gdl_name').value;			//指标名称
	var gdl_unit = $('gdl_unit').value;			//单位
	var gdl_group_ids = $('gdl_group').getAttribute("groupIds");	//指标分类id
	var num_frommat = $('num_frommat').value;		//默认展示格式
	var gdl_bus_desc = $('gdl_bus_desc').value;		//业务解释
	
	var dimData = gdlData["DIM_DATA"];
	var dimArray = new Array();
	for(var i=0;i<dimData.length; i++){
		if($("CHECK_BOX_"+dimData[i].DIM_TABLE_ID).checked){
			var dimMethod = $("DIM_"+dimData[i].DIM_TABLE_ID).value;		//维度支持方式
			var addDimData = dhx.extend({},dimData[i]);  //需要添加的维度列(方法第一个参数，继承第二个参数所有属性)
			addDimData["GROUP_METHOD"] = dimMethod;		//设置该维度支撑方式
			dimArray.push(addDimData);	//添加该维度	
			if(!dimData[i].GDL_ID||dimData[i].GROUP_METHOD!=dimMethod){
				//当该维度不存在，或者值不同时，表示做了修改操作
				isUpdateCheck = true;
				isOnlyUpdateGroup = false;
			}
		}else{
			if(dimData[i].GDL_ID){
				//如果该维度为初始存在，但现在没有选中时。表示做了修改操作
				isUpdateCheck = true;
				isOnlyUpdateGroup = false;
			}
		}
	}
	/**********验证是否修改了信息**********/
	if(gdl_code != gdlData["GDL_CODE"]){		//检验指标编码
		isUpdateCheck = true;
		isOnlyUpdateGroup = false;
	}
	if(gdl_name != gdlData["GDL_NAME"]){		//检验指标名称
		isUpdateCheck = true;
		isOnlyUpdateGroup = false;
	}
	if(gdl_unit != gdlData["GDL_UNIT"]){		//检验指标单位
		isUpdateCheck = true;
		isOnlyUpdateGroup = false;
	}
	if(gdl_group_ids != gdlData["GDL_GROUP_IDS"]){		//检验指标分类id
		isUpdateCheck = true;
	}
	if(num_frommat != gdlData["NUM_FORMAT"]){		//检验展示格式
		isUpdateCheck = true;
		isOnlyUpdateGroup = false;
	}
	if(gdl_bus_desc != gdlData["GDL_BUS_DESC"]){		//检验指标业务解释
		isUpdateCheck = true;
		isOnlyUpdateGroup = false;
	}
	
	if(!isUpdateCheck){
    	/*dhx.*/alert("你未做任何操作，无需修改！");
		return;
	}
	
	/**********验证是否修改了信息**********/
	
    //验证指标编码是否存在
	GdlBasicMagAction.checkGdlCode(gdl_code,gdlData["GDL_ID"],function(rs){
		if(rs){
        	/*dhx.*/alert('指标编码'+gdl_code+'已经存在，请重新填写！');
        	return;
		}else{
	        var updateGDL = dhx.extend({},gdlData);
			if(isOnlyUpdateGroup){
				updateGDL["ONLY_UPDATE_GROUP"] = 1;
			}
			updateGDL["GDL_CODE"] = gdl_code;
			updateGDL["GDL_NAME"] = gdl_name;
			updateGDL["GDL_UNIT"] = gdl_unit;
			updateGDL["GDL_GROUP_IDS"] = gdl_group_ids;
			updateGDL["NUM_FORMAT"] = num_frommat;
			updateGDL["GDL_BUS_DESC"] = gdl_bus_desc;
			updateGDL["DIM_INFO"] = dimArray;	//设置关联维度
			GdlBasicMagAction.updateGdlBasic(updateGDL,function(rs){
			   	if(rs){
			    	if(isOnlyUpdateGroup){
		        		/*dhx.*/alert('修改成功！');
			    	}else{
		        		/*dhx.*/alert('提交成功！');
			    		commitUpdate.disabled="disabled";		//禁用保存按钮
			    	}
			   	}else{
		        	/*dhx.*/alert('提交失败！请联系管理员！');
			   	}
			});
		}
	});
}

//添加验证信息
function createValidate(){
    dhtmlxValidation.addValidation($("_tableBaseInfo"), [
        {target:"gdl_code",rule:"NotEmpty,MaxLength[12]"},
        {target:"gdl_name",rule:"NotEmpty,MaxLength[64]"},
        {target:"gdl_unit",rule:"NotEmpty,MaxLength[20]"},
        {target:"gdl_group",rule:"NotEmpty" },
        {target:"gdl_bus_desc",rule:"NotEmpty,MaxLength[1000]"}
    ])
}

//生成Grid数据
function setDimGrid(dimData){
	if(dimData){
		for(var i=0;i<dimData.length; i++){
		    var tab = document.getElementById("dimtermTable");
		    var tr = document.createElement("TR");
		    tab.appendChild(tr);
		    
		    //复选框
		    var ischecked = "";
		    if(dimData[i].GDL_ID){	//如果存在值，表示为已经支持的维度
		    	ischecked = "checked='checked'";
		    }
		    var td1 = document.createElement("TD");
		    td1.innerHTML = "<input type='checkbox' id='"+("CHECK_BOX_"+dimData[i].DIM_TABLE_ID)+"' "+ischecked+"/>";
		    tr.appendChild(td1);
		    td1.style.textAlign = "center";
		    
		    //名称
		    var td2 = document.createElement("TD");
		    td2.innerHTML = dimData[i]["DIM_TABLE_NAME_CN"]+"：";
		    tr.appendChild(td2);
		    td2.style.textAlign = "right";
		    td2.style.paddingRight = "5px";
		    
		    //支持维度
		    var td3 = document.createElement("TD");
		    str = "<select id='"+("DIM_"+dimData[i].DIM_TABLE_ID)+"'>";
			for(var m=0;m<gdlGroupMethod.length;m++){
				var isSelected = "";
				if(dimData[i].GROUP_METHOD&&dimData[i].GROUP_METHOD==gdlGroupMethod[m].value){
					isSelected = "selected='selected'"
				}
				str += "<option value='"+gdlGroupMethod[m].value+"' "+isSelected+">"+gdlGroupMethod[m].name+"</option>";
			}
			str += "</select>";
		    td3.innerHTML = str;
		    tr.appendChild(td3);
		    td3.style.textAlign = "left";
		    td3.style.paddingLeft = "5px";
		}
	}
}

var groupData = null;	//分类数据
//初始分类树
function initGroupTree(){
	if(!groupTree){
		groupTree = new meta.ui.selectTree("gdl_group",300,220);
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
		
		//为input框绑定点击事件，初始化值
		attachObjEvent($("gdl_group"),"onclick",function(){
			//取消所有选中的值
			var childs = groupTree.tree.getAllCheckedBranches();	//得到所有选中的值
            if(childs){
                var childIds = childs.toString().split(",");
                for(var j=0;j<childIds.length;j++){
                	groupTree.tree.setCheck(childIds[j],false);		//取消所有选中值
                }
            }
            //根据已经选中的值，选中tree的对应的值
			if($("gdl_group").getAttribute("groupIds")){
	    		var groupIds = $("gdl_group").getAttribute("groupIds").split(",");	//得到所有选中的值
	            for(var j=0;j<groupIds.length;j++){
	            	groupTree.tree.setCheck(groupTree._priFix+groupIds[j],true);	//设置对应input框选中值选中
	            }
			}
		});
	}
} 

dhx.ready(pageInit);