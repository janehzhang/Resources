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
var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?'0000':$("zoneCode").value;
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
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
})

var userInit=function(){
	//indexExp();
	loadZoneTreeChkBox(zoneCode,queryform);
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.startTime   =$("dateTime").value;
    map.zoneCode   =$("zoneCode").value;
    map.flag = "1";
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getzqMYDDetails(map, function (res) {
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='100%' height='180px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    //autoRowSpan($("tab1"),0,1);
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var excelHeader="";
	var tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:60px'><span class='title'>片区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:60px'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
		//+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>收到短信客户数</span></td>"
		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:60px'><span class='title'>测评有效样本量</span></td>"
		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:60px'><span class='title'>测评参与率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2' style='width:150px'><span class='title'>第一联系人准确情况</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2' style='width:150px'><span class='title'>总体评价数量</span></td>"
		//+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:60px'><span class='title'>测评满意数</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5' style='width:360px'><span class='title'>测评满意率</span></td>"
//		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:60px'><span class='title'>不满意数量</span></td>"
//		+"<td bgcolor='#cde5fd' colspan='2' style='width:120px'><span class='title'>套餐解释不清</span></td>"
//		+"<td bgcolor='#cde5fd' colspan='2' style='width:120px'><span class='title'>业务办理差错</span></td>"
//		+"<td bgcolor='#cde5fd' colspan='2' style='width:120px'><span class='title'>其他原因</span></td>"
		+"</tr>"
		+"<tr>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>联系人准确数</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >联系人准确率</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>满意数</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >不满意数</span></td>"
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>不满意，套餐解释不清</span></td>"
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>不满意，业务办理差错</span></td>"
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>不满意，其他原因</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>满意率</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>环比</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>同比</span></td>"
		//+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>平均</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>片区排名</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>全省排名</span></td>"
		
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>提及数</span></td>"
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >提及率</span></td>"
//		
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>提及数</span></td>"
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>提及率</span></td>"
//		
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>提及数</span></td>"
//		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>提及率</span></td>"
        +"</tr>";
	
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
		    		            	   if((dataColumn[i][tempColumn])=="%"){
		    		            			   tableData +="<td>-</td>";
		    		            			   excelData += "-}";
		    		            	   }/*else if(tempColumn == '分公司' && i!=0)
			    		        	 {
		    		            		   if(dataColumn[i].层级_ID<'6'){
					    		        	     tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
					    		        	  }else{
					    		        		  tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
					    		        	  }   
			    		        	 }*/else if(tempColumn == '邀请数' && dataColumn[i].邀请数 != '-' )
			    		        	 {
			    		        		 /*if(userCodeData.indexOf("]"+dataColumn[i].分公司_ID+"]")==-1){
			    		        			 tableData +="<td nowrap>"+dataColumn[i][tempColumn]+"</td>"; 
			    		        		 }else{*/
			    		        		    tableData +="<td nowrap onclick=\"drillList('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		        		 //}
			    		        	 } else{
				    		        	 tableData +="<td nowrap>"+dataColumn[i][tempColumn]+"</td>"; 
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

//政企满意度钻取
var drillList=function(zoneName,zoneCode){
	var dateTime = $('dateTime').value;
	//var endDate = $('endDate').value;
	
	var url="/portalCommon/module/procedure/visitDetail/zq/zqSatisfyList.jsp?dateTime="+dateTime+"&zoneCode="+zoneCode+"&transmitName="+"政企满意度清单["+zoneName+"]";
	//alert(base+url);
	return parent.openTreeTab("123006"+zoneCode,"政企满意度清单["+zoneName+"]",base+url,'top');
	
}

//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var tempIndexId='22';
	 var explainData="";
	 var map=new Object(); 
	 map.rptId=tempIndexId;
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

//查询
var queryData=function(){
	excuteInitData();
} 
//第一列合并
var autoRowSpan=function(tb,row,col){//0,16
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
dhx.ready(userInit);