/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();

//当前系统的主页Path
var base = getBasePath();
//查询条件参数   
var userInit=function(){
    //执行查询数据
	queryData();
    queryData1();
    queryData2();
}

var queryData=function(){
    var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.dateType   ="1"; //周报
    map.field   ="SATISFY_LV"; 
    map.zoneCode   =$("zoneCode").value;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfied(map, function (res) {
    	$('chartdiv').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "190", "0", "0");
			    	 chart.setDataXML(res.barChartMap);
			         chart.render("chartdiv");
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='100%' height='200px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
      $('chartTable').innerHTML=tableStr;
    //  autoRowSpan($("mytbl"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var  tableHead="";
	var  excelHeader="";
tableHead+="<tr>"
				+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>触点</span></td>"
				+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>满意率</span></td>"
				+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>不满意率</span></td>"
		 +"</tr>"
		 +"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>本周</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>上周</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>本周</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>上周</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>";
		    tableHead+="</tr>";
	      for(var i=0;i<headColumn.length;i++)
	      {
	   	     if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    	  {
			     excelHeader += headColumn[i]+",";
	          }
	      }
    
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
				    		          if(tempColumn == '触点') 
				    		          {
				    		        	 tableData +="<td nowrap onclick=\"openMenuHref('"+dataColumn[i].触点_编码+"','1')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		          }
				    		          else
				    		          {
				    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";  
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
//查询月报
var queryData1=function(){
    var map=new Object();
    map.dateTime   =$("dateTime1").value;
    map.dateType   ="2"; //月报
    map.field   ="SATISFY_LV"; 
    map.zoneCode   =$("zoneCode").value;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfied(map, function (res) {
    	$('chartdiv1').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "190", "0", "0");
			    	 chart.setDataXML(res.barChartMap);
			         chart.render("chartdiv1");
					 buildTable1(res);
   });
} 
function buildTable1(data){
    tableStr ="<table id='tab1' width='100%' height='200px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader1(data.headColumn);
    tableStr+=tableData1(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
      $('chartTable1').innerHTML=tableStr;
    //  autoRowSpan($("mytbl"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader1(headColumn){
	var  tableHead="";
	var  excelHeader="";
tableHead+="<tr>"
				+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>触点</span></td>"
				+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>满意率</span></td>"
				+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>不满意率</span></td>"
		 +"</tr>"
		 +"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>本月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>上月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>本月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>上月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
		    tableHead+="</tr>";
	      for(var i=0;i<headColumn.length;i++)
	      {
	   	     if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    	  {
			     excelHeader += headColumn[i]+",";
	          }
	      }
    
    $("excelHeader1").value=excelHeader;//Excel表头
    return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData1(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
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
				    		          if(tempColumn == '触点')
				    		          {
				    		        	 tableData +="<td nowrap onclick=\"openMenuHref('"+dataColumn[i].触点_编码+"','2')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		          }
				    		          else
				    		          {
				    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";  
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
     $("excelData1").value= excelData;//Excel数据
return tableData;
}
//查询日报
var queryData2=function(){
    var map=new Object();
    map.dateTime   =$("dateTime2").value;
    map.dateType   ="0"; 
    map.field   ="SATISFY_LV"; 
    map.zoneCode   =$("zoneCode").value;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfied(map, function (res) {
    	$('chartdiv2').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "190", "0", "0");
			    	 chart.setDataXML(res.barChartMap);
			         chart.render("chartdiv2");
					 buildTable2(res);
   });
} 
function buildTable2(data){
    tableStr ="<table  id='tab1' width='100%' height='200px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader2(data.headColumn);
    tableStr+=tableData2(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
      $('chartTable2').innerHTML=tableStr;
    //  autoRowSpan($("mytbl"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader2(headColumn){
	var  tableHead="";
	var  excelHeader="";
tableHead+="<tr>"
				+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>触点</span></td>"
				+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>满意率</span></td>"
				+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>不满意率</span></td>"
		 +"</tr>"
		 +"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>当日</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>昨日</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当日</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>昨日</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
		    tableHead+="</tr>";
	      for(var i=0;i<headColumn.length;i++)
	      {
	   	     if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    	  {
			     excelHeader += headColumn[i]+",";
	          }
	      }
    
    $("excelHeader2").value=excelHeader;//Excel表头
    return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData2(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
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
				    		          if(tempColumn == '触点')
				    		          {
				    		        	 tableData +="<td nowrap onclick=\"openMenuHref('"+dataColumn[i].触点_编码+"','0')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		          }
				    		          else
				    		          {
				    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";  
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
     $("excelData2").value= excelData;//Excel数据
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
var openMenuHref=function(actType,dateType){
	  if(actType == "0"){//宽带新装
		  if(dateType=="0"){//日报
			  var url="/reports/customerSatisfied/broadbandNew/serviceSatisfied_Day.jsp?dateTime="+$("dateTime2").value+"&menuId=122019"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("122019"+actType, "宽带新装满意率日报", base+url, 'top');
		  }if(dateType=="1"){//周报
			  var url="/reports/customerSatisfied/broadbandNew/serviceSatisfied_Week.jsp?dateTime="+$("dateTime").value+"&menuId=122001"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("122001"+actType, "宽带新装满意率周报", base+url, 'top');
		  }if(dateType=="2"){//月报
			  var url="/reports/customerSatisfied/broadbandNew/serviceSatisfied_Mon.jsp?dateTime="+$("dateTime1").value+"&menuId=122018"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("122018"+actType, "宽带新装满意率月报", base+url, 'top');
		  }
	   }
	  if(actType == "1"){//宽带修障
		  if(dateType=="0"){//日报
			  var url="/reports/customerSatisfied/broadbandRepair/serviceSatisfied_Day.jsp?dateTime="+$("dateTime2").value+"&menuId=122023"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("122023"+actType, "宽带修障满意率日报", base+url, 'top');
		  }if(dateType=="1"){//周报
			  var url="/reports/customerSatisfied/broadbandRepair/serviceSatisfied_Week.jsp?dateTime="+$("dateTime").value+"&menuId=122009"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("122009"+actType, "宽带修障满意率周报", base+url, 'top');
		  }if(dateType=="2"){//月报
			  var url="/reports/customerSatisfied/broadbandRepair/serviceSatisfied_Mon.jsp?dateTime="+$("dateTime1").value+"&menuId=122022"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("122022"+actType, "宽带修障满意率月报", base+url, 'top');
		  } 
	   } 
	  if(actType == "2"){//营业厅服务 
		  if(dateType=="0"){//日报
			  var url="/reports/customerSatisfied/businessHallService/serviceSatisfied_Day.jsp?dateTime="+$("dateTime2").value+"&menuId=124009"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("124009"+actType, "营业厅服务满意率日报", base+url, 'top');
		  }if(dateType=="1"){//周报
			  var url="/reports/customerSatisfied/businessHallService/serviceSatisfied_Week.jsp?dateTime="+$("dateTime").value+"&menuId=124002"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("124002"+actType, "营业厅服务满意率周报", base+url, 'top');
		  }if(dateType=="2"){//月报
			  var url="/reports/customerSatisfied/businessHallService/serviceSatisfied_Mon.jsp?dateTime="+$("dateTime1").value+"&menuId=124007"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("124007"+actType, "营业厅服务满意率月报", base+url, 'top');
		  }  
	   }
	  if(actType == "3"){//投诉处理
		  if(dateType=="0"){//日报
			  var url="/reports/customerSatisfied/cmplDeal/serviceSatisfied_Day.jsp?dateTime="+$("dateTime2").value+"&menuId=124013"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("124013"+actType, "投诉处理满意率日报", base+url, 'top');
		  }if(dateType=="1"){//周报
			  var url="/reports/customerSatisfied/cmplDeal/serviceSatisfied_Week.jsp?dateTime="+$("dateTime").value+"&menuId=124005"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("124005"+actType, "投诉处理满意率周报", base+url, 'top');
		  }if(dateType=="2"){//月报
			  var url="/reports/customerSatisfied/cmplDeal/serviceSatisfied_Mon.jsp?dateTime="+$("dateTime1").value+"&menuId=124011"+"&zoneCode="+$("zoneCode").value;
		        return parent.openTreeTab("124011"+actType, "投诉处理满意率月报", base+url, 'top');
		  }
		  
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