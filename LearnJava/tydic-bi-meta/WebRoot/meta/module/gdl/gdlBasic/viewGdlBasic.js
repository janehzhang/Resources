/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       addGdlBasic.js
 *Description：
 *       指标管理-查看基础指标
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-06-15
 ********************************************************/

var gdlData = null;//指标信息

function pageInit() {
    dhx.showProgress("请求数据中");
	GdlBasicMagAction.queryGdlInfoById(gdlId,gdlVersion,function(data){
        dhx.closeProgress();
        gdlData = data;
		$('gdl_code').innerHTML = data["GDL_CODE"]?data["GDL_CODE"]:"&nbsp;";		//指标编码
		$('gdl_name').innerHTML = data["GDL_NAME"]?data["GDL_NAME"]:"&nbsp;";		//指标名称
		$('gdl_type').innerHTML = getNameByTypeValue("GDL_TYPE",data["GDL_TYPE"]);		//指标类型
		$('gdl_state').innerHTML = getNameByTypeValue("GDL_STATE",data["GDL_STATE"]);	//指标状态
		$('gdl_unit').innerHTML = data["GDL_UNIT"]?data["GDL_UNIT"]:"&nbsp;";		//单位
		$('gdl_group').innerHTML = data["GDL_GROUP_NAME"]?data["GDL_GROUP_NAME"]:"&nbsp;";//指标分类名
		$('num_frommat').innerHTML = data["NUM_FORMAT"]?getNameByTypeValue("GDL_NUMFORMAT",data["NUM_FORMAT"]):"无";		//默认展示格式
		$('gdl_src_table_name').innerHTML = data["GDL_SRC_TABLE_NAME"]?data["GDL_SRC_TABLE_NAME"]:"&nbsp;";	//来源表类
		$('gdl_src_col').innerHTML = data["GDL_SRC_COL"]?data["GDL_SRC_COL"]:"&nbsp;";	//来源表类
		$('user_namecn').innerHTML = data["USER_NAMECN"]?data["USER_NAMECN"]:"&nbsp;";	//创建人
		$('create_time').innerHTML = data["CREATE_TIME"]?data["CREATE_TIME"]:"&nbsp;";	//创建时间
		$('gdl_bus_desc').innerHTML = data["GDL_BUS_DESC"]?data["GDL_BUS_DESC"]:"&nbsp;";	//业务解释
		
		setDimGrid(data["DIM_DATA"]);	//设置支持维度

        attachObjEvent($('colseWin'),"onclick",function(){	//关闭按钮
            window.parent.closeTab("基础指标("+gdlData['GDL_CODE']+")");
        });
	});
}

//生成Grid数据
function setDimGrid(dimData){
	if(dimData){
		for(var i=0;i<dimData.length; i++){
		    var tab = document.getElementById("dimtermTable");
		    var tr = document.createElement("TR");
		    tab.appendChild(tr);
		    
		    //第一列
		    var td1 = document.createElement("TD");
		    td1.innerHTML = "";
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
		    var td3Value = "";
			if(dimData[i].GDL_ID){
				td3Value = getNameByTypeValue("GDL_GROUP_METHOD",dimData[i]["GROUP_METHOD"]);
			}else{
				td3Value = "不支持";
			}
		    td3.innerHTML = td3Value;
		    tr.appendChild(td3);
		    td3.style.textAlign = "left";
		    td3.style.paddingLeft = "5px";
		}
	}
}

dhx.ready(pageInit);