/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;

var titileInfo="越级投诉率->当月值";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo;

/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
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

//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}
var changeCode = $("changeCode").value;

var indexInit=function(){
     //1.加载地域树 
     loadZoneTreeChkBox(tZoneCode,queryform);
     getUserSubCode();
      //执行查询数据
      excuteInitData(changeCode);
}

var excuteInitData=function(changeCode){
     var map=new Object();
     var zoneCode     =  $("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     var dateTime     = ($("dateTime").value).replaceAll("-", "");
     map.zoneCode     =zoneCode;
     map.dateTime     =dateTime;
     map.changeCode = changeCode;
     //查询数据表格
     selectTable(map);
      //查询图形
     selectChart(map);
     
     
}

/**
 * 查询数据表格
 * @param {Object} map
 * 
 */
var selectTable=function(map)
{
     dhx.showProgress("正在执行......");
     CmplSpanMonAction.getTableData(map, function (res) {
    	              dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
					    buildTable(res);
    });
}

/**
 *  构建表格
 * @param {Object} data
 */
function buildTable(data){
	     tableStr ="<table id='mytbl_1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
           tableStr+= tableHeader(data.headColumn);
		   tableStr+= tableData(data.dataColumn, data.headColumn);
         tableStr +="</table>"; 
         $('dataTable').innerHTML=tableStr;
         autoRowSpan($("mytbl_1"),1,0);//合并单元格
}

/**
 *  构造表头
 * @param {Object} data
 */
function tableHeader(headColumn){
	var  excelHeader="";
	var  tableHead="<tr>";
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
	  var  excelData="";
	   var tableData="";
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
				    		    	  if(tempColumn == '越级投诉量')
				    		          {
				    		    		  if(userCodeData.indexOf("]"+dataColumn[i].分公司_编码+"]")==-1){
				    		    			  tableData +="<td nowrap align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		    		  }else{
				    		        	      tableData +="<td nowrap align='center' onclick=\"openMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
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

var selectChart=function(map)
{
    //构建曲线图
	bulidLineChart(map);
	//构建柱状图
	bulidBarChart(map);
}

/**
 * 构建曲线图
 * @param {Object} map
 */
var bulidLineChart=function(map)
{
	$("cid").value=Math.random();
	var cid=$("cid").value;
	dhx.showProgress("正在执行......");
    CmplSpanMonAction.bulidLineChart(map, {callback:function (res) {
    	        dhx.closeProgress();
    	        if(res != null && res.XML != null){
	          		   var chart = new FusionCharts(base+"/js/Charts/MSLine.swf", "chartdiv_1"+cid, "100%", "200","0", "0");
					   chart.setDataXML(res.XML);
			           chart.render("chartdiv_line");
		         }else{
		        	  $('chartdiv_line').innerHTML="<center>没有图形显示</center>";
		         }
		   }
		});
}
/**
 * 构建柱状图
 * @param {Object} map
 */
var bulidBarChart=function(map)
{   
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	dhx.showProgress("正在执行......");
    CmplSpanMonAction.bulidBarChart(map, {callback:function (res) {
    	dhx.closeProgress();
    	         if(res != null && res.XML != null){
	          		   var chart = new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "chartdiv_2"+cid1, "100%", "200","0", "0");
					   chart.setDataXML(res.XML);
			           chart.render("chartdiv_bar");
		         }else{
		        	   $('chartdiv_bar').innerHTML="<center>没有图形显示</center>";
		         }
    	         $("div_src").style.display = "none";
    	         $("div_src").style.zindex = "-1";
		   }
		});
}

var lookCity = function (){
	 var obj = event.srcElement;
	 var map=new Object();
     map.zoneCode     =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     map.dateTime     =($("dateTime").value).replaceAll("-", "");
     tZoneCode=$("zoneCode").value;
  	 if(tZoneCode!='0000') { 
			  if(obj.value=='横向对比'){//地市
				  obj.value='返回';
				  map.changeCode="0";//地市同级
				  $("changeCode").value='0';
			  }else if(obj.value=='返回'){//地市
				  obj.value='横向对比';
				  map.changeCode="1";//地市下一级
				  $("changeCode").value='1';
			  }
		}else{
		     if(obj.value=='横向对比'){//省公司下一级
				  obj.value='返回';
				  map.changeCode="1";//省公司下一级
				$("changeCode").value='1';
			  }else if(obj.value=='返回'){//省公司下两级
				  obj.value='横向对比';
				  map.changeCode="2";//省公司下两级
				$("changeCode").value='2';
			  }
		}
              bulidBarChart(map);
}


//查询
var queryData=function(){
	changeCode=getChangeCode(); //by qx
	excuteInitData(changeCode);
}
var openMenuHref=function(zoneName,zoneCode){
	   var dateTime=$("dateTime").value;
	   var url="/portalCommon/module/procedure/ts/skip/list/cmplSpanDetail.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&transmitName="+menuName+"清单["+zoneName+"]";
	   return parent.openTreeTab("128017"+zoneCode, menuName+"清单"+"["+zoneName+"]", base+url,'top');
}
//合并单元格
var autoRowSpan=function(tb,row,col){
  for (var i=col; i >-1; i--)//列
   {
	    var lastValue="";
        var value="";
        var pos=1;
        for(var j=row;j<tb.rows.length;j++) //行
        {
            value = tb.rows[j].cells[i].innerText;
            if(lastValue == value)
            {
                tb.rows[j].deleteCell(i);
                tb.rows[j-pos].cells[i].rowSpan = tb.rows[j-pos].cells[i].rowSpan+1;
                pos++;
            }else{
                lastValue = value;
                pos=1;
            }
        }
   }
 }
function exportExcel(){
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//文本框
    var  zone=$("zone").value;//文本框
    var  queryCond="月 份："+dateTime +" 区域："+zone;
    $("excelCondition").value=queryCond;   
  	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_chart.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts1 = getChartFromId("chartdiv_1"+cid);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("chartdiv_2"+cid1);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
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
//获取changeCode
function getChangeCode(){
	 var changeCode="";
	 tZoneCode=$("zoneCode").value;
	if(tZoneCode!="0000"){
		 $("changeCode").value="1";
		 changeCode="1";
	}else{
	    $("changeCode").value="2";
	    changeCode="2";
	}
	$("city").value='横向对比';
	return changeCode;
}
dhx.ready(indexInit);