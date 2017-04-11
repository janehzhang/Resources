/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var globZoneTree = null;
var page=null;

//当前系统的主页Path
var base = getBasePath();
//查询条件参数
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
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
});
/** Tree head **/

var userInit=function(){
	page=Page.getInstance();
	page.init();
	loadIndexCdWarn(menuName);
	//1.加载地域树 
    loadZoneTreeChkBox(zoneCode,queryform);
    //执行查询数据
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.startDate   =$("startDate").value;
    map.endDate   =$("endDate").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
    map.dateType   ="0";//日报
    map.actType   =$("vTypeId").value;//触点
    map.demension   =$("demension").value;//维度
    
    map.pageCount=  page.pageCount;    //每页显示多少条数
    map.currPageNum=page.currPageNum;//当前第几页
    
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfied_everyTypeDemension(map, function (res) {
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    var actType   =$("vTypeId").value;
	var demension   =$("demension").value;
    if(actType=='8'){//号百
	    if(demension=='8'){//业务类型
	    	document.getElementById("pageDiv").style.display="none";
	    }else{
	    	 $("totalCount").value=data.allPageCount;
	    	 page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
	    }
    }else{
    	 $("totalCount").value=data.allPageCount;
    	 page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
    }  
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var  excelHeader="";
	var tableHead="";
	var firstCol="";
	var tempId   =$("vTypeId").value;//触点
	var demension   =$("demension").value;//维度
	
	if(demension=='0'){
		firstCol="在网时长";
	}if(demension=='1'){
		firstCol="受理渠道";
	}if(demension=='2'){
		firstCol="客户群";
	}if(demension=='3'){
		firstCol="客户等级";
	}if(demension=='4'){
		firstCol="ARPU值";
	}if(demension=='5'){
		firstCol="套餐名称";
	}if(demension=='6'){
		if(tempId=='2'){
			firstCol="营业员";
		}if(tempId=='0'||tempId=='1'){
			firstCol="装维员";
		}if(tempId=='5'||tempId=='6'||tempId=='7'||tempId=='8'){
			firstCol="话务员";
		}if(tempId=='9'||tempId=='10'){
			firstCol="服务人员工号";
		}
	}if(demension=='7'){
		firstCol="测评方式";
	}if(demension=='8'){
		firstCol="业务类型";
	}
	if(tempId=='0'){
		tableHead="<tr>"
			+"<td style='width:100px' bgcolor='#cde5fd' rowspan='2'><span class='title'>"+firstCol+"</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:200px'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='1'){
		tableHead="<tr>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:100px'><span class='title'>"+firstCol+"</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:200px'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='2'||tempId=='9'||tempId=='10'){
		tableHead="<tr>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:100px'><span class='title'>"+firstCol+"</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:200px'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='5'){//10000号
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>"+firstCol+"</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='4' style='width:150px'><span class='title'>总体评价</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:200px'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>对客户代表不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>对其他不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='6'){//网厅
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>"+firstCol+"</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:200px'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='7'){//掌厅
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>"+firstCol+"</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:200px'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='8'){//号百
		if(demension=='8'){//业务类型
			tableHead="<tr>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>话务中心</span></td>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>区域</span></td>"
				+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>"+firstCol+"</span></td>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>测评方式</span></td>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>邀请客户数</span></td>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>测评有效样本数</span></td>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>测评参与率</span></td>"
				+"<td nowrap bgcolor='#cde5fd' colspan='4' style='width:20%'><span class='title'>总体评价</span></td>"
				+"<td bgcolor='#cde5fd' colspan='4' style='width:20%'><span class='title'>测评满意率</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%' rowspan='2'><span class='title'>测评满意数</span></td>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>不满意原因提及率</span></td>"
				+"<td bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>不满意原因提及数</span></td>"
				+"</tr>"
				+"<tr>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>非常满意</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>对客服代表不满意</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>对其他不满意</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>当期</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>环比</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>同比</span></td>"
				+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>平均</span></td>";
		}else{
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:6%'><span class='title'>"+firstCol+"</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:6%'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:6%'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:6%'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='4' style='width:24%'><span class='title'>总体评价</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:24%'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:6%'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:6%'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>满意</span></td>"
			//+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>对客服代表不满意</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>对其他不满意</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:6%'><span class='title'>平均</span></td>";
		}
	}
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
				    		              tableData +="<td align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";   
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
	page.resetPage();
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