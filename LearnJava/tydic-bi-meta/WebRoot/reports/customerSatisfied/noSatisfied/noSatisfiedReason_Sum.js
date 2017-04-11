/**
 * 页面初始化。
 */

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var globZoneTree = null;
var globReasonTree = null;

//当前系统的主页Path
var base = getBasePath();
//查询条件参数
var zoneCode  =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var zoneCode2 =   $('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value;
var zoneCode1 =   $('zoneCode1').value==""?userInfo['localCode']:$("zoneCode1").value;

var vTypeId=$("vTypeId").value;
var vTypeId1=$("vTypeId1").value;
var vTypeId2=$("vTypeId2").value;

if (vTypeId == '5'||vTypeId == '11'||vTypeId == '12') {
	var reasonCode = $('reasonCode').value;
}
if (vTypeId2 == '5'||vTypeId == '11'||vTypeId == '12') {
	var reasonCode2 = $('reasonCode2').value;
}
if (vTypeId1 == '5'||vTypeId == '11'||vTypeId == '12') {
	var reasonCode1 = $('reasonCode1').value;
}

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZoneCode(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

/** Tree head **/
if (vTypeId1 == '5'||vTypeId=='5'||vTypeId2=='5'){
	dwrCaller.addAutoAction("loadReasonTree","ReasonAction.queryReasonByPathCode");
	var reasonConverter=dhx.extend({idColumn:"typeId",pidColumn:"typeParId",
	    textColumn:"typeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadReasonTree",reasonConverter);
	dwrCaller.addAction("querySubReasonCode",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},reasonConverter,false);
	    ReasonAction.querySubReasonCode(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
}else if (vTypeId1 == '11'||vTypeId=='11'||vTypeId2=='11'){
	dwrCaller.addAutoAction("loadReasonTree","ReasonAction.queryIVRZYJReasonByPathCode");
	var reasonConverter=dhx.extend({idColumn:"typeId",pidColumn:"typeParId",
	    textColumn:"typeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadReasonTree",reasonConverter);
	dwrCaller.addAction("querySubReasonCode",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},reasonConverter,false);
	    ReasonAction.querySubIVRZYJReasonCode(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
}else if(vTypeId1 == '12'||vTypeId=='12'||vTypeId2=='12'){
	dwrCaller.addAutoAction("loadReasonTree","ReasonAction.queryIVRXZReasonByPathCode");
	var reasonConverter=dhx.extend({idColumn:"typeId",pidColumn:"typeParId",
	    textColumn:"typeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadReasonTree",reasonConverter);
	dwrCaller.addAction("querySubReasonCode",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},reasonConverter,false);
	    ReasonAction.querySubIVRXZReasonCode(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
}
/** Tree head **/
var userInit=function(){
	clickTab();
	//1.加载地域树 
	loadZoneTreeChkBox(zoneCode,queryform);
    loadZoneTreeChkBox2(zoneCode2,queryform);
    loadZoneTreeChkBox1(zoneCode1,queryform);
}
function clickTab(){
	var obj= this;
	var clickTab=$("clickTab").value;
	if(clickTab=="tab1"){//月报
		obj.id="tab1"
		changeTab1(obj);
	}else if(clickTab=="tab2"){//周
		obj.id="tab2"
		changeTab2(obj);
	}else if(clickTab=="tab3"){//日
		obj.id="tab3"
		changeTab3(obj);
	}else{
		obj.id="tab3"
		changeTab3(obj);
	}
}
//月
function changeTab1(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	if (vTypeId == '5') {
	   loadReasonTreeChkBox1(reasonCode1, queryform);
	}
	window.setTimeout("queryData1()",700);
	getUserSubCode();	
}
//周
function changeTab2(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	if (vTypeId == '5'||vTypeId == '11'||vTypeId == '12') {
	    loadReasonTreeChkBox(reasonCode, queryform);
	}
	window.setTimeout("queryData()",700);	
	getUserSubCode();	
}
//日
function changeTab3(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	if (vTypeId == '5'||vTypeId == '11'||vTypeId == '12') {
	    loadReasonTreeChkBox2(reasonCode2, queryform);
	}
	window.setTimeout("queryData2()",700);	
	getUserSubCode();
}

//周报
var queryData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
    var map=new Object();
    map.startDate =$("dateTime").value;
    map.endDate   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?zoneCode:$("zoneCode").value;
    if (vTypeId == '5'||vTypeId == '11'||vTypeId == '12') {
    map.reasonCode=$("reasonCode").value;
    }
    map.dateType   ="1";//日期类型--周报
    map.actType   =$("vTypeId").value; //触点名称
    map.field   ="NO_SATISFY_NUM";//不满意量
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getNOSatisfiedReason(map, function (res) {
    	$('chartdiv').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid, "560", "280", "0", "0");
			         chart.setDataXML(res.pieChartMap);
			         chart.render("chartdiv");
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table  id='tab01' width='560px' height='280'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    //autoRowSpan($("tab01"),1,1);
    autoRowSpan($("tab01"),1,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
var  tableHead="<tr>";
var  excelHeader="";
      for(var i=0;i<headColumn.length;i++)
      {
       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    {
	       tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
	       excelHeader += headColumn[i]+",";
        }
      }
    tableHead+="</tr>";
    $("excelHeader").value=excelHeader;//Excel表头
return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var zoneCode=$("zoneCode").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<headColumn.length;j++)
		    		      {
		    		       var tempColumn=headColumn[j]; 
		    		           if(tempColumn.indexOf('_')==-1)
					    		  {
		    		        	   	 if(tempColumn == '不满意量')
				    		           {
		    		        	   		if(userCodeData.indexOf("]"+zoneCode+"]")==-1){
		    		        	   			tableData +="<td nowrap align='center' >"+dataColumn[i][tempColumn]+"</td>"; 
		    		        	   		}else{
		    		        	   			tableData +="<td nowrap align='center' onclick=\"openMenuHref('','"+dataColumn[i].原因_编码+"','"+dataColumn[i].不满意二级原因_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; //
		    		        	   		} 
				    		           }
		    		        	   	   else
				    		           {
				    		        	tableData +="<td nowrap align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		           }
				    		          excelData += dataColumn[i][tempColumn]+"}";
			    		          }
		    		      }  
		    tableData +="</tr>";
		    excelData +="]";
	       }
	    }
        else
       {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
return tableData;
}
//月报
var queryData1=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
    var map=new Object();
    map.startDate =$("dateTime1").value;
    map.endDate   =$("dateTime1").value;
    map.zoneCode=$('zoneCode1').value==""?zoneCode:$("zoneCode1").value;
    if (vTypeId == '5'||vTypeId == '11'||vTypeId == '12') {
    map.reasonCode=$("reasonCode1").value;
    }
    map.dateType   ="2";//日期类型--月报
    map.actType   =$("vTypeId1").value; //触点名称
    map.field   ="NO_SATISFY_NUM";//不满意量
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getNOSatisfiedReason(map, function (res) {
    	$('chartdiv1').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid, "560", "280", "0", "0");
			         chart.setDataXML(res.pieChartMap);
			         chart.render("chartdiv1");
					 buildTable1(res);
   });	
}
function buildTable1(data){
    tableStr ="<table  id='tab01' width='560px' height='280'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader1(data.headColumn);
    tableStr+=tableData1(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable1').innerHTML=tableStr;
    //autoRowSpan($("tab01"),1,1);
    autoRowSpan($("tab01"),1,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader1(headColumn){
var  tableHead="<tr>";
var  excelHeader="";
      for(var i=0;i<headColumn.length;i++)
      {
       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    {
	       tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
	       excelHeader += headColumn[i]+",";
        }
      }
    tableHead+="</tr>";
    $("excelHeader").value=excelHeader;//Excel表头
return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData1(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var zoneCode=$("zoneCode1").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<headColumn.length;j++)
		    		      {
		    		       var tempColumn=headColumn[j]; 
		    		           if(tempColumn.indexOf('_')==-1)
					    		  {
		    		        	   	 if(tempColumn == '不满意量')
				    		           {
		    		        	   		if(userCodeData.indexOf("]"+zoneCode+"]")==-1){
		    		        	   			tableData +="<td nowrap align='center' >"+dataColumn[i][tempColumn]+"</td>"; 
		    		        	   		}else{
		    		        	   			tableData +="<td nowrap align='center' onclick=\"openMenuHref1('','"+dataColumn[i].原因_编码+"','"+dataColumn[i].不满意二级原因_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; //
		    		        	   		}
				    		           }
		    		        	   	   else
				    		           {
				    		        	tableData +="<td nowrap align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		           }
				    		          excelData += dataColumn[i][tempColumn]+"}";
			    		          }
		    		      }  
		    tableData +="</tr>";
		    excelData +="]";
	       }
	    }
        else
       {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
return tableData;
}
//日报
var queryData2=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
    var map=new Object();
    map.startDate =$("startDate").value;
    map.endDate   =$("endDate").value;
    map.zoneCode=$('zoneCode2').value==""?zoneCode:$("zoneCode2").value;
    if (vTypeId == '5'||vTypeId == '11'||vTypeId == '12') {
    map.reasonCode=$("reasonCode2").value;
    }
    map.dateType   ="0";//日期类型--日报
    map.actType   =$("vTypeId2").value; //触点名称
    map.field   ="NO_SATISFY_NUM";//不满意量
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getNOSatisfiedReason(map, function (res) {
    	$('chartdiv2').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid, "560", "280", "0", "0");
			         chart.setDataXML(res.pieChartMap);
			         chart.render("chartdiv2");
					 buildTable2(res);
   });	
}
function buildTable2(data){
    tableStr ="<table  id='tab01' width='560px' height='280'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader2(data.headColumn);
    tableStr+=tableData2(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable2').innerHTML=tableStr;
    //autoRowSpan($("tab01"),1,1);
    autoRowSpan($("tab01"),1,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader2(headColumn){
var  tableHead="<tr>";
var  excelHeader="";
      for(var i=0;i<headColumn.length;i++)
      {
       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    {
	       tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
	       excelHeader += headColumn[i]+",";
        }
      }
    tableHead+="</tr>";
    $("excelHeader").value=excelHeader;//Excel表头
return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData2(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var zoneCode=$("zoneCode2").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;
  //alert(userCodeData);
  //alert(zoneCode);
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<headColumn.length;j++)
		    		      {
		    		       var tempColumn=headColumn[j]; 
		    		           if(tempColumn.indexOf('_')==-1)
					    		  {
		    		        	   	 if(tempColumn == '不满意量')
				    		           {
		    		        	   		if(userCodeData.indexOf("]"+zoneCode+"]")==-1){
		    		        	   			tableData +="<td nowrap align='center' >"+dataColumn[i][tempColumn]+"</td>"; 
		    		        	   		}else{
		    		        	   			tableData +="<td nowrap align='center' onclick=\"openMenuHref2('','"+dataColumn[i].原因_编码+"','"+dataColumn[i].不满意二级原因_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; //
		    		        	   		}
				    		           }
		    		        	   	   else
				    		           {
				    		        	tableData +="<td nowrap align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		           }
				    		          excelData += dataColumn[i][tempColumn]+"}";
			    		          }
		    		      }  
		    tableData +="</tr>";
		    excelData +="]";
	       }
	    }
        else
       {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
return tableData;
}
//第一列合并
var autoRowSpan=function(tb,row,col){
    var lastValue="";
    var value="";
    var pos=1;
    for(var i=row;i<tb.rows.length;i++)
    {
        value = tb.rows[i].cells[col].innerText;
        if(lastValue == value)
        {
            tb.rows[i].deleteCell(col);
            tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
            pos++;
        }else{
            lastValue = value;
            pos=1;
        }
    }
}

/**
 *  打开链接菜单
 * 
 */
//周
var openMenuHref=function(indexId,reasonId,reasonId2){
	  var zoneCode=$('zoneCode').value;
	  var zoneName=$('zone').value;
	  var dateTime=$("dateTime").value;
	  //var vTypeName=$("vTypeId").options[$("vTypeId").selectedIndex].text;
	  var startTime=dateTime.substr(0,dateTime.indexOf("~"));
	  startTime =startTime.substr(0,4)+"-"+startTime.substr(4,2)+"-"+startTime.substr(6,2) ;
	  var endTime=dateTime.substr(dateTime.indexOf("~")+1,dateTime.lenght);
	  endTime =endTime.substr(0,4)+"-"+endTime.substr(4,2)+"-"+endTime.substr(6,2) ;
	  var vTypeId=$("vTypeId").value;
	  var url;
	  var tab_name="";
	  if(vTypeId=='0'){//0宽带新装
		  tab_name="宽带装移机";
		 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='1'){//1宽带修障
		  tab_name="宽带修障";
		 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='2'){//2营业厅服务
		  tab_name="实体渠道";
		 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId;
	  }if(vTypeId=='20'){
		  tab_name ="非实体渠道";
	  	url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		    "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId;
	  }if(vTypeId=='3'){//3投诉处理
		  tab_name="投诉处理";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='4'){//4电子渠道(无)
		  tab_name="电子渠道";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='5'){//10000号
		  tab_name="10000号";
		 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='6'){//6网厅
		  tab_name="网厅";
		 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='7'){//7掌厅
		  tab_name="掌厅";
		 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='8'){//8号百
		  tab_name="号百";
		 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='10'){//实体渠道短信
		  tab_name="实体渠道短信";
		  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId+"&testMethod=1";
	  }if(vTypeId=='11'){//实体渠道短信
		  tab_name="IVR装移机";
		  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&notSatisType1="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='12'){//实体渠道短信
		  tab_name="IVR修障";
		  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&notSatisType1="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='17'){//17投诉
		  tab_name="投诉";
		  url="/portalCommon/module/procedure/visitDetail/sts/ts_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }    
	 return parent.openTreeTab("124017"+zoneCode+indexId+vTypeId+reasonId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//月
var openMenuHref1=function(indexId,reasonId,reasonId2){
	  var zoneCode=$('zoneCode1').value;
	  var zoneName=$('zone1').value;
	  var dateTime=$("dateTime1").value;
	  //var vTypeName=$("vTypeId1").options[$("vTypeId1").selectedIndex].text;
	  var year= dateTime.substring(0,4);
	  var month=dateTime.substring(4,6);
	  var startTime=year+"-"+month+"-01";
	  var endTime=year+"-"+month+"-"+getLastDay(year,month);
	  var vTypeId=$("vTypeId1").value;
	  var url;
	  var tab_name="";
	  if(vTypeId=='0'){//0宽带新装
		  tab_name="宽带装移机";
		 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='1'){//1宽带修障
		  tab_name="宽带修障";
		 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='2'){//2营业厅服务
		  tab_name="实体渠道";
		 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId;
	  }if(vTypeId=='20'){
		  tab_name ="非实体渠道";
	  	url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		    "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId;
	  }if(vTypeId=='3'){//3投诉处理
		  tab_name="投诉处理";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='4'){//4电子渠道(无)
		  tab_name="电子渠道";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='5'){//10000号
		  tab_name="10000号";
		 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='6'){//6网厅
		  tab_name="网厅";
		 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='7'){//7掌厅
		  tab_name="掌厅";
		 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='8'){//8号百
		  tab_name="号百";
		 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='10'){//实体渠道短信
		  tab_name="实体渠道短信";
		  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId+"&testMethod=1";
	  }if(vTypeId=='11'){//实体渠道短信
		  tab_name="IVR装移机";
		  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&notSatisType1="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='12'){//实体渠道短信
		  tab_name="IVR修障";
		  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&notSatisType1="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }   
	 return parent.openTreeTab("124017"+zoneCode+indexId+vTypeId+reasonId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//日
var openMenuHref2=function(indexId,reasonId,reasonId2){
	  var zoneCode=$('zoneCode2').value;
	  var zoneName=$('zone2').value;
	  var  startTime=$("startDate").value;//时间
	  var  endTime=$("endDate").value;//时间
	  //var vTypeName=$("vTypeId2").options[$("vTypeId2").selectedIndex].text;
	  var startTime=startTime;
	  var endTime =endTime;
	  var vTypeId=$("vTypeId2").value;
	  var tab_name="";
	  var url;
	  if(vTypeId=='0'){//0宽带新装
		  tab_name="宽带装移机";
		 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='1'){//1宽带修障
		  tab_name="宽带修障";
		 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='2'){//2营业厅服务
		  tab_name="实体渠道";
		 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId;
	  }if(vTypeId=='20'){
		  tab_name ="非实体渠道";
	  	url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		    "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId;
	  }if(vTypeId=='3'){//3投诉处理
		  tab_name="投诉处理";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='4'){//4电子渠道(无)
		  tab_name="电子渠道";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='5'){//10000号
		  tab_name="10000号";
		 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='6'){//6网厅
		  tab_name="网厅";
		 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='7'){//7掌厅
		  tab_name="掌厅";
		 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='8'){//8号百
		  tab_name="号百";
		 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  }if(vTypeId=='10'){//实体渠道短信
		  tab_name="实体渠道短信";
		  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId+"&actType="+vTypeId+"&testMethod=1";
	  }if(vTypeId=='11'){//实体渠道短信
		  tab_name="IVR装移机";
		  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&notSatisType1="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='12'){//实体渠道短信
		  tab_name="IVR修障";
		  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&notSatisType1="+reasonId+"&indexId="+indexId+"&notSatisType2="+reasonId2;
	  }if(vTypeId=='17'){//17投诉
		  tab_name="投诉";
			 url="/portalCommon/module/procedure/visitDetail/sts/ts_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&indexId="+indexId;
	  } 
	 return parent.openTreeTab("124017"+zoneCode+indexId+vTypeId+reasonId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//获得某月的最后一天
function getLastDay(year,month) {
	var new_year = year;
   //取当前的年份
	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）
  if(month>12) {
	new_month -=12; //月份减
	new_year++; //年份增
	}
	var new_date = new
	Date(new_year,new_month,1); //取当年当月中的第一天
    return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
}
function exportImage(){
    var cid=$("cid").value;
    var charts = getChartFromId("ChartId1_"+cid);   //生成的FusionCharts图表本身的标识
    charts.exportChart(); 
}
//月
function exportExcel1(){
	    var  dateTime=$("dateTime1").options[$("dateTime1").selectedIndex].text;//时间
        var  zone=$("zone1").value;//区域
        //var  vTypeId=$("vTypeId1").options[$("vTypeId1").selectedIndex].text;//触点
        var queryCond="月份："+dateTime+"    区域："+zone;
	    $("excelCondition").value=queryCond; 
	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneChart.jsp";
		document.forms[0].method = "post";
		document.forms[0].action=url;
		document.forms[0].target="hiddenFrame";
		document.forms[0].submit();
}
//周
function exportExcel(){
	var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
    var  zone=$("zone").value;//区域
    //var  vTypeId=$("vTypeId").options[$("vTypeId").selectedIndex].text;//触点
    var queryCond="周期："+dateTime+"    区域："+zone;
    $("excelCondition").value=queryCond; 
    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneChart.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//日
function exportExcel2(){
	var  startTime=$("startDate").value;//时间
	var  endTime=$("endDate").value;//时间
    var  zone=$("zone2").value;//区域
   // var  vTypeId=$("vTypeId2").options[$("vTypeId2").selectedIndex].text;//触点
    var queryCond="日期从："+startTime+"   至："+endTime+"    区域："+zone;
	$("excelCondition").value=queryCond; 
    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneChart.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//所有子节点
function getUserSubCode(){
	 var userCodeData="]";
	 var map=new Object(); 
	 map.userCode=$("userCode").value;
	 CustomerSatisfiedAction.getUserSubCode(map, {callback:function (data) {
         if(data != null){
    		 if(data&&data.userCodeList.length>0){	
    			 for(var i=0;i<data.userCodeList.length;i++){
    				 userCodeData+=data.userCodeList[i].ZONE_CODE+"]";
    			 }
    		 } 
	         }
         $("userCodeData").value= userCodeData;
	   }
	}); 
}
function chartChange(reasonId,dateType,isRoot){
	if(dateType=='0'){//日
		if(isRoot=='1'){
			$('reasonCode2').value=reasonId;
		}else{
			$('reasonCode2').value='0000';
		}
		loadReasonTreeChkBox2($('reasonCode2').value, queryform);
		queryData2();
	}else if(dateType=='1'){//周
		if(isRoot=='1'){
			$('reasonCode').value=reasonId;
		}else{
			$('reasonCode').value='0000';
		}
		loadReasonTreeChkBox($('reasonCode').value, queryform);
		queryData();	
	}else if(dateType=='2'){//月
		if(isRoot=='1'){
			$('reasonCode1').value=reasonId;
		}else{
			$('reasonCode1').value='0000';
		}
		loadReasonTreeChkBox1($('reasonCode1').value, queryform);
		queryData1();
	}
}
/**
 * 
 * @param myObject
 * @return
 */
function dump_obj(myObject) {  
   var s = "";  
   for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
/** end **/
dhx.ready(userInit);