/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       dataExamineInit.js
 *Description：
 *       门户首页配置-数据审核初始化
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       刘弟伟
 *Date：
 *       2012-5-15
 ********************************************************/

/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

//查询报表名称
dwrCaller.addAutoAction("queryReportName", "ReportConfigAction.queryReportName");

//执行数据导入。
dwrCaller.addAutoAction("importData","ReportConfigAction.importData");

var dataExamineInit=function(){
    var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("数据审核初始化");
    topCell.setHeight(80);
    topCell.fixSize(true,true);

    var bottomCell = dhxLayout.cells("b");
    bottomCell.fixSize(true,true);
    bottomCell.hideHeader();

    var firstday = new Date();
    var today = new Date();
    firstday.setDate(1);
    var importform = topCell.attachForm(
            [
                {type:"settings",position: "label-left"},
                {type:"select",label:"报表名称：",name:"reportname",inputHeight:22,inputWidth: 120,options:[{value:"",text:"全部",selected:true}]},
                {type:"newcolumn"},
                {type:"calendar",label:"报告日期：", inputHeight:17,name:"startDate",dateFormat:"%Y-%m-%d",weekStart:"7",value:firstday,validate: "NotEmpty",readonly:true},
                {type:"newcolumn"},
                {type:"calendar",label:"至",name:"endDate",dateFormat:"%Y-%m-%d",weekStart:"7",value:today,inputHeight:17,validate: "NotEmpty",readonly:true},
                {type:"newcolumn"},
                {type:"select",label:"展示类型：",name:"Exhibitiontype",inputHeight:22,inputWidth: 120,options:[{value:"",text:"全部",selected:true}]},
                {type:"newcolumn"},
                {type:"button",name:"generatedata",value:"批量生成数据",offsetLeft : 0,offsetTop : 0}
            ]
    );
    dwrCaller.executeAction("queryReportName",function(data){
      var reportNameSelect= importform.getSelect("reportname");
        var reportNameCount=1;
         reportNameSelect.options[0]=new Option("全部","");
        
        for(var key in data){
            if(key!='length'){
                reportNameSelect.options[reportNameCount++]=new Option(data[key].TAB_NAME,data[key].TAB_ID);
            }
        }
        
    }) ;
    
     var ExhibitiontypeSelect= importform.getSelect("Exhibitiontype");
     ExhibitiontypeSelect.options[0] = new Option("不展示","0");
     ExhibitiontypeSelect.options[1] = new Option("展示","1");

    var startCalendar = importform.getCalendar("startDate");
    var endCalendar = importform.getCalendar("endDate");

    //将日历控件语言设置成中文
    startCalendar.loadUserLanguage('zh');
    endCalendar.loadUserLanguage('zh');

   // startCalendar.setInsensitiveRange(tomorrow,null);
   // endCalendar.setInsensitiveRange(tomorrow,null);
    
    importform.attachEvent("onButtonClick", function(id) {
    	if (id == "generatedata") 
    	{
    		dhx.confirm("执行此操作将自动删除该日期范围内已经存在的数据！",function(r){
            if(r)
            {
            	//批量生成数据。
            	ReportConfigAction.importData(importform.getFormData(),function(rs)
            	{
            		if (rs) 
            		{
		                dhx.alert("数据导入成功！");
		            } else 
		            {
		                dhx.alert("数据导入失败，请重试！");
		            }
            	});
            }
            });
        }
    });
}

dhx.ready(dataExamineInit);