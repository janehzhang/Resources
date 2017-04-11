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
	loadZoneTreeChkBox(zoneCode,queryform);
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.startTime   =$("dateTime").value;
    map.zoneCode   =$("zoneCode").value;
    
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getItanTass(map, function (res) {
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='4350px' height='180px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),0,1);
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var excelHeader="";
	var tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:80px'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:80px'><span class='title'>分公司</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>宽带装维月均满意率、月均参与率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>实体渠道（营业厅+微信）月均满意率、月均参与率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>10000号月均满意率、月均参与率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>号百月均满意率、月均参与率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:800px'><span class='title'>网掌厅满意率月均满意率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>宽带装维当月满意率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>实体渠道（营业厅+微信）当月满意率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>10000号当月满意率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6' style='width:750px'><span class='title'>号百当月满意率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4' style='width:800px'><span class='title'>网掌厅满意率当月满意率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='7' style='width:550px'><span class='title'>实体渠道（评价器）</span></td>"
		+"</tr>"
		+"<tr>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>满意率月均值（90%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >满意率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>满意率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >满意率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>满意率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >满意率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>满意率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >满意率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>参与率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>网厅满意率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >满意率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"	
		
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>掌厅满意率月均值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title' >满意率目标值</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>与目标差距</span></td>"
		
		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>邀请量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>回复量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月满意率</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>满意率环比</span></td>"		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月参与率</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>参与率环比</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>邀请量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>回复量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月满意率（98%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>满意率环比</span></td>"		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月参与率（80%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>参与率环比</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>邀请量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>回复量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月满意率（95.0%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>满意率环比</span></td>"		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月参与率（50%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>参与率环比</span></td>"
		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>邀请量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>回复量</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月满意率（96.5%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>满意率环比</span></td>"		
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>当月参与率（70%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>参与率环比</span></td>"
		
		
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>当月网厅满意率（98%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>满意率环比</span></td>"
					
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>当月掌厅满意率（98%）</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>满意率环比</span></td>"
		
		
		
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>当月应测评总数</span></td>"		
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>当月邀请客户数</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>当月使用率</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title' >当月测评有效样本数</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>当月测评参与率</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>月均参与率</span></td>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>当月满意率</span></td>"
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
		    		            	   }else{
						    		       tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";
					    		           excelData += dataColumn[i][tempColumn]+"}";
		    		            	   }
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