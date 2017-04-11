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
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;

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

//渠道类型加载

    dwrCaller.addAutoAction("loadChannelTypeTreePart","NewTwoChannelAction.queryChannelTypePathPart");
	var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
	    textColumn:"channelTypeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
	dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
	    NewTwoChannelAction.querySubChannelTypePart_new(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});


var userInit=function(){
	loadZoneTreeChkBox(zoneCode,queryform);
	loadChannelTypeTreePart(channelTypeCode,queryform);
    excuteInitData();
}

var excuteInitData=function(){
	
	 var map=new Object();
	 
	 map.dateTime=$("dateTime").value;
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	     map.channelTypeCode = ""?"1":$("channelTypeCode").value;
		 map.ichannelservid=$('ichannelservid').value; 
		 map.iCustCode =$("iCustCode").value;		 
	     dhx.showProgress("正在执行......");
	   CustomerSatisfiedAction.channelCustSj(map, function (res) {
     	   dhx.closeProgress();
 			 if (res == null) {
 				   dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				     return;
 			 }else{
 				 buildTable(res);
 			 }
    });
    
}
function buildTable(data){
    tableStr ="<table id='tab1' width='100%' height='180px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	

    var serv_name="";
    if ($("iCustCode").value=="1")
    {
    	serv_name="时机";
    }
    else if ($("iCustCode").value=="2")
    {
    		serv_name="客户分群";		
    }
    else if ($("iCustCode").value=="3")
    {
    		serv_name="客户子群类型";		
    }
    else if ($("iCustCode").value=="4")
    {
    		serv_name="客户等级";	
    }
    else if ($("iCustCode").value=="5")
    {
    		serv_name="出账收入";		
    }
    else if ($("iCustCode").value=="6")
    {
    		serv_name="在网时长";	
    }
    else if ($("iCustCode").value=="7")
    {
    		serv_name="产品类型";	
    }
    else if ($("iCustCode").value=="8")
    {
    		serv_name="用户品牌";	
    }
    else if ($("iCustCode").value=="9")
    {
    		serv_name="付费方式";		
    }	
    else 
    {
    	serv_name="细分市场";	
    }
	
	
	var excelHeader="";
	var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' style='width:80px'><span class='title'>"+serv_name+"</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>0-7时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>7-8时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>8-9时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title' >9-10时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>10-11时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>11-12时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>12-13时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>13-14时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>14-15时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>15-16时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>16-17时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>17-18时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>18-19时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>19-20时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>20-24时</span></td>"
		+"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>服务总量占比</span></td>"	
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