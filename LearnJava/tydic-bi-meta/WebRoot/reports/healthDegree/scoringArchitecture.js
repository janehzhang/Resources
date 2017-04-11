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

var userInit=function(){
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.dateTime   =$("dateTime").value;
    dhx.showProgress("正在执行......");
    HealthDegreeAction.getScoringArchitecture_Pg(map, function (res) {
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='100%' height='250px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
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
	var tableHead="<tr>";
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
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<10;i++)//22
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<headColumn.length;j++)
		    		      {
		    		                 var tempColumn=headColumn[j]; 
		    		                 if(tempColumn.indexOf('_')==-1)
				    		         {  
		    		            	    tableData +="<td align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
					    		        excelData += dataColumn[i][tempColumn]+"}";  
				    		         } 
		    		      }  
		    tableData +="</tr>";
		    excelData +="]";
	       }
	    	   tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<3;j++)
		    		      {
		    		                 var tempColumn=headColumn[j]; 
		    		                 if(tempColumn.indexOf('_')==-1)
				    		         {  
			    		            	    tableData +="<td align='center'>"+dataColumn[10][tempColumn]+"</td>"; 
						    		        excelData += dataColumn[10][tempColumn]+"}";  
				    		         } 
		    		      }  
		    		     tableData +="<td align='center' rowspan='5'>"+dataColumn[10][headColumn[3]]+"</td>"; 
			    		 excelData += dataColumn[10][headColumn[3]]+"}";
		    tableData +="</tr>";
		    excelData +="]";
	       for(var i=11;i<=14;i++)
	       {
	    	   tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<3;j++)
		    		      {
		    		                 var tempColumn=headColumn[j]; 
		    		                 if(tempColumn.indexOf('_')==-1)
				    		         {  
		    		            	    tableData +="<td align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
					    		        excelData += dataColumn[i][tempColumn]+"}";  
				    		         } 
		    		      }  
		    		     excelData += dataColumn[10][headColumn[3]]+"}";
		    		     //excelData +="]";
		    tableData +="</tr>";
		    excelData +="]";
	       }
	    	   tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<3;j++)
		    		      {
		    		                 var tempColumn=headColumn[j]; 
		    		                 if(tempColumn.indexOf('_')==-1)
				    		         {  
		    		                	 tableData +="<td align='center'>"+dataColumn[15][tempColumn]+"</td>"; 
						    		     excelData += dataColumn[15][tempColumn]+"}";  
				    		         } 
		    		      }
		    		     tableData +="<td align='center' rowspan='3'>"+dataColumn[15][headColumn[3]]+"</td>"; 
			    		 excelData += dataColumn[15][headColumn[3]]+"}";
		    tableData +="</tr>";
		    excelData +="]";
		    for(var i=16;i<=17;i++)
		       {
		    	   tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
			    		     for(var j=0;j<3;j++)
			    		      {
			    		                 var tempColumn=headColumn[j]; 
			    		                 if(tempColumn.indexOf('_')==-1)
					    		         {  
			    		            	    tableData +="<td align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
						    		        excelData += dataColumn[i][tempColumn]+"}";  
					    		         } 
			    		      }
			    		     excelData += dataColumn[10][headColumn[3]]+"}";
			    tableData +="</tr>";
			    excelData +="]";
		       }
	       for(var i=18;i<dataColumn.length;i++)
	       {
	    	   tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<headColumn.length;j++)
		    		      {
		    		                 var tempColumn=headColumn[j]; 
		    		                 if(tempColumn.indexOf('_')==-1)
				    		         {  
		    		            	    tableData +="<td align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
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