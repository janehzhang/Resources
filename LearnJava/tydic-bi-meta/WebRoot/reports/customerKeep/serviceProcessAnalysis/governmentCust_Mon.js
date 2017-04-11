/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var parameters=$("parameters").value;

if(user['zoneId']!='1') { //不是广东省  （钻取权限控制）
	var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	}
}
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath1");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone1(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/** Tree head **/
//业务类型树
/*dwrCaller.addAutoAction("loadProdTypeTree","ProdTypeAction.queryProdTypeByPath");
var prodTypeConverter=dhx.extend({idColumn:"cmplProdTypeCode",pidColumn:"cmplProdTypeParCode",
    textColumn:"cmplProdTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadProdTypeTree",prodTypeConverter);
//树动态加载Action
dwrCaller.addAction("querySubProdType",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},prodTypeConverter,false);
    ProdTypeAction.querySubProdType(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});*/
var indexInit=function(){
    //加载预警信息
    //loadIndexCdWarn(menuName);
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
   //加载业务类型树
   //  loadProdTypeTree(prodTypeCode, queryform);
     //执行查询数据
     excuteInitData();
     indexExp();
}


var excuteInitData=function(){
	 var map=new Object();
     map.dateTime     =$("dateTime").value;
     map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;

     //map.field=$('selectCol').value;
    // map.rptIndex="mobileKeep";
     zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     
     map.reportId="30016";
     map.parameters=parameters; 
     map.values= $("dateTime").value.replaceAll("-", "")+"$"+zoneCode;
     
     dhx.showProgress("正在执行......");
     ServiceReActiveAction.getCustKeep_pg(map, function (res) {
     	//$('chartdiv1').innerHTML='';
     	//$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				    // chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "100%", "200", "0", "0");
 			    	// chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "100%", "200", "0", "0");
 			        // chart1.setDataXML(res.barChartMap);
 			        // chart2.setDataXML(res.lineChartMap);
 			       //  chart1.render("chartdiv1");
 			        // chart2.render("chartdiv2"); 
 					 buildTable(res);
 					// $("city").value='切换地市'
    });	
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),4,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
var  excelHeader="";
var  tableHead="<tr>"
	+"<td nowrap bgcolor='#cde5fd' style='width:80px' rowspan='3'><span class='title'>大区</span></td>"
	+"<td nowrap bgcolor='#cde5fd' style='width:60px' rowspan='3'><span class='title'>分公司</span></td>"
	+"<td nowrap bgcolor='#cde5fd'  style='width:250px' colspan='5'><span class='title'>政企属性用户</span></td>"
	+"<td nowrap bgcolor='#cde5fd'  style='width:160px' colspan='2'><span class='title'>VPN用户</span></td>"
	+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='10'><span class='title'>行业应用用户</span></td>"
	+"<td nowrap bgcolor='#cde5fd'  style='width:180px' colspan='2'><span class='title'>VPN及行业应用双重身份用户</span></td>"
	+"<td nowrap bgcolor='#cde5fd'  style='width:250px' colspan='5'><span class='title'>用户总量</span></td>"
	+"</tr><tr>"
	+"<td bgcolor='#cde5fd'  rowspan='2'><span class='title'>固话</span></td>"
	+"<td bgcolor='#cde5fd'  rowspan='2'><span class='title'>宽带</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>移动</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>其它</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>合计</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>VPN政企用户</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>VPN非政企用户</span></td>"
	+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>行业应用政企用户</span></td>"
	+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>行业应用非政企用户</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>VPN&行业应用政企用户</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>VPN&行业应用非政企用户</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>固话</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>宽带</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>移动</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>其它</span></td>"
	+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>合计</span></td>"
	+"</tr><tr>"
	+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>其它</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>合计</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>其它</span></td>"
	+"<td bgcolor='#cde5fd' ><span class='title'>合计</span></td>";
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
		    		            	   if(tempColumn == '分公司' && i!=0)
			    		        	 {
		    		            		  // if(dataColumn[i].区域_层级<'6'){
					    		        	     tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";	 
					    		        	 // }else{
					    		        		//  tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
					    		        	//  }
		    		            		  // tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].地市_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	   
			    		        	 }else{
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
//查询
var queryData=function(){
	excuteInitData();
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
//钻取
var drillArea=function(zoneName,zoneCode)
{	
	var url="/reports/customerKeep/serviceProcessAnalysis/governmentCust_Mon.jsp?dateTime="+$("dateTime").value+"&zoneCode="
	+zoneCode;
	return parent.openTreeTab("127002"+zoneCode,"政企用户分布月报表"+"["+zoneName+"]",base+url,'top');	
}
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var rptId="40"
     var map=new Object(); 
	 map.rptId=rptId;
	 CmplIndexAction.getIndexExplain(map, {callback:function (data) {
         if(data != null){
        	 var tableStr="<table width='100%' border='0'  cellpadding='0' cellspacing='0'>";
    		 if(data&&data.expList.length>0){	
    			 for(var i=0;i<data.expList.length;i++){
    				 tableStr =tableStr+
    		         "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
    		           +"<td align=\"left\"><font style='font-weight:bold;' >&nbsp;"+data.expList[i].INDEX_NAME+"  :  "+"</font>"
    		           +data.expList[i].INDEX_EXPLAIN+"</td>"   
    		           +"</tr>";
    				 explainData+=data.expList[i].INDEX_NAME+"  :  "+ data.expList[i].INDEX_EXPLAIN+"]";
    			 }
    			 tableStr +="</table>"; 
    			 $("explain").value=explainData;
    			 $('index_exp').innerHTML=tableStr;
    		 } 
	         }
	   }
	});
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
dhx.ready(indexInit);