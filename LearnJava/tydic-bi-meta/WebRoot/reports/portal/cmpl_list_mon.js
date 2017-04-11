/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var globProdTypeTree = null;
var globCmplBusiTypeTree = null;

//查询条件参数
//查询系统参数
var startDate = null;
var endDate = null;

var startDate=$('startDate').value;
var endDate=$('endDate').value;
var zoneId =   $('zoneCode').value;
var prodTypeId =   $('prodTypeCode').value;
var cmplBusiTypeId =   $('cmplBusiTypeCode').value;
var indId =   $('indId').value;

var pageSize = 15;//每页大小
var pageIndex =0;//开始页


var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
        	formData.field="COMPLAIN_NUM";
        	formData.startDate=$("startDate").value; 
            formData.endDate=$("endDate").value;
            formData.indId=$("indId").value;
            formData.pageIndex=pageIndex;
            formData.pageSize=pageSize;
            formData.zoneId=$("zoneCode").value          ==""?zoneCode:$("zoneCode").value;
            formData.prodTypeId=$('prodTypeCode').value  ==""?prodTypeCode:$("prodTypeCode").value;
            formData.cmplBusiTypeId=$('cmplBusiTypeCode').value==""?cmplBusiTypeCode:$("cmplBusiTypeCode").value;
            return formData;
        }
        }
    ]);
  
dwrCaller.addAutoAction("getListCmplReport_Mon","ReportsMonAction.getListCmplReport_Mon",loadParam);


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

//业务类型树
dwrCaller.addAutoAction("loadProdTypeTree","ProdTypeAction.queryProdTypeByPath");
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
});

//投诉现象树
dwrCaller.addAutoAction("loadCmplBusiTypeTree","CmplBusiTypeAction.queryCmplBusiTypeByPath");
var cmplBusiTypeConverter=dhx.extend({idColumn:"cmplBusiTypeCode",pidColumn:"cmplBusiTypeParCode",
    textColumn:"cmplBusiTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadCmplBusiTypeTree",cmplBusiTypeConverter);
//树动态加载Action
dwrCaller.addAction("querySubCmplBusiType",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},cmplBusiTypeConverter,false);
    CmplBusiTypeAction.querySubCmplBusiType(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //加载业务类型树
     loadProdTypeTree(prodTypeCode,queryform);
    //加载投诉现象树
     loadCmplBusiTypeTree(cmplBusiTypeCode,queryform);
     //执行查询数据
     excuteInitData(pageIndex);
}


var excuteInitData=function(page_index){
	pageIndex=page_index;
	//pageSize=page_size;
	dwrCaller.executeAction("getListCmplReport_Mon",function(data){
		$('chartTable').innerHTML='';
		var tableStr="<table width='2350px' id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			  +"<tr>"
			    +"<td bgcolor='#cde5fd'><strong>工单流水号</strong></td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>产品类型</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>服务类别</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>工单来源</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>工单类别</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>申告号码</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>申告地市</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>客户名称</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>联系电话1</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>联系电话2</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>受理时间</td>"
			   // +"<td bgcolor='#cde5fd' style='width:60px'>受理班组ID</td>"
			    //+"<td bgcolor='#cde5fd' style='width:100px'>受理班组(简称)</td>"
			    +"<td bgcolor='#cde5fd' style='width:100px'>受理班组</td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:100px'>受理操作员编号</td>"
			    +"<td bgcolor='#cde5fd' style='width:100px'>归档操作员编号</td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:90px'>归档时间</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>归档类型 </td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>工单历时</td>"
			    +"<td bgcolor='#cde5fd' style='width:90px'>工单超时时长</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>超时时间</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>重复次数</td>"
			   // +"<td bgcolor='#cde5fd' style='width:120px'>分公司责任班组ID</td>"
			   // +"<td bgcolor='#cde5fd' style='width:110px'>分公司责任班组</td>"
			    +"<td bgcolor='#cde5fd' style='width:120px'>分公司责任班组</td>"
			    +"<td bgcolor='#cde5fd' style='width:200px'>定性内容</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>回访满意度</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>派外系统流水号</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>服务等级</td>"
			 +"</tr>";
    	if(data&&data.list.length>0){	
       for(var i=0;i<data.list.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
             +"<td>"+data.list[i].BILL_ID+"</td>"
             +"<td>"+data.list[i].PROD_TYPE_NAME+"</td>"
             +"<td>"+data.list[i].CMPL_BUSINESS_TYPE_NAME+"</td>"
             +"<td>"+data.list[i].CMPL_CHANNEL_NAME+"</td>"
             +"<td>"+data.list[i].CMPL_BILL_TYPE_NAME+"</td>"
             +"<td>"+data.list[i].CUST_NBR+"</td>"
             +"<td>"+data.list[i].ZONE_NAME+"</td>"
             +"<td>"+data.list[i].CUST_NAME+"</td>"
             +"<td>"+data.list[i].CUST_LINK_NBR+"</td>"
             +"<td>"+data.list[i].CUST_LINK_NBR2+"</td>"
             +"<td>"+data.list[i].HANDLE_DATE+"</td>"
            // +"<td>"+data.list[i].HANDLE_DEPT_ID+"</td>"
             //+"<td>"+data.list[i].HANDLE_DEPT_NAME+"</td>"
             +"<td>"+data.list[i].HANDLE_DEPT_NAME_DESC+"</td>"
             
             +"<td>"+data.list[i].HANDLE_OPTR_ID+"</td>"
             +"<td>"+data.list[i].ARCHIVE_OPTR_ID+"</td>"
             
             +"<td>"+data.list[i].ARCHIVE_DATE+"</td>"
             +"<td>"+data.list[i].FILE_TYPE_NAME+"</td>"
             +"<td>"+data.list[i].BILL_TIME_COST+"</td>"
             +"<td>"+data.list[i].BILL_TIMEOUT_COST+"</td>"
             +"<td>"+data.list[i].TIMEOUT_DURATION+"</td>"
             +"<td>"+data.list[i].REPEAT_NUM+"</td>"
             //+"<td>"+data.list[i].DUTY_DEPT_ID+"</td>"
             //+"<td>"+data.list[i].DUTY_DEPT_NAME+"</td>"
             +"<td>"+data.list[i].DUTY_DEPT_NAME_DESC+"</td>"
             +"<td>"+data.list[i].ARCHIVE_DESC+"</td>"
             +"<td>"+data.list[i].SATISFY_NAME+"</td>"
             +"<td>"+data.list[i].CALL_SEQ+"</td>"
             +"<td>"+data.list[i].CUST_LEVEL_NAME+"</td>"
		    +"</tr>";
         }
              $('chartTable').innerHTML=tableStr;
              generatePageBar("excuteInitData",data);
	      }else{
	    	  $('pageTag').innerHTML='';
	    	  $('chartTable').innerHTML='<br/>没找到任何数据';
	      }
      });
	
}
//查询
var queryData=function(){
	excuteInitData(pageIndex);
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
 * 生成分页工具条
 * 
 * @param callback_name
 *  分页函数名
 * @param data
 *  数据
 */
function generatePageBar(callback_name, data) {
	$('pageTag').innerHTML='';
	bar="<span style='color:#000000'>每页显示</span><select name='pageSize' id='pageSize' onchange='onSelect1();'>" 
	bar +="<option value='15' "; bar+=(data.pageSize==15)?'selected':''; bar +=">15</option>";
    bar +="<option value='20' "; bar+=(data.pageSize==20)?'selected':''; bar +=">20</option>";
    bar +="<option value='30' "; bar+=(data.pageSize==30)?'selected':''; bar +=">30</option>";
    bar +="<option value='40' "; bar+=(data.pageSize==40)?'selected':''; bar +=">40</option>";
    bar +="<option value='50' "; bar+=(data.pageSize==50)?'selected':''; bar +=">50</option>";
    bar +="</select>条记录，";
	bar += "<span style='color:#000000'>共" + data.totalSize + "条记录，共"
	+ data.pageCount + "页，当前第 " + (data.pageNo + 1) + "页</span>&nbsp";
	if (data.pageCount > 0) {
	if (data.pageNo == 0) {
	bar += "<font color='gray'>首页</font>&nbsp";
	} else {
	bar += "<a href='javascript:void(0);' onclick='" + callback_name
	+ "(0);'>首页</a>&nbsp";
	bar += "<a href='javascript:void(0);' onclick='" + callback_name
	+ "("+(data.pageNo - 1)+ ");'>上一页</a>&nbsp";
	}
	if (data.pageCount - 1 == data.pageNo) {
	bar += "<a href='javascript:void(0);'><font color='gray'>下一页</font></a>&nbsp";
	bar += "<a href='javascript:void(0);'><font color='gray'>尾页</font></a>&nbsp";
	} else {
	bar += "<a href='javascript:void(0);' onclick='" + callback_name
	+ "("+ (data.pageNo + 1)+");'>下一页</a>&nbsp";
	bar += "<a href='javascript:void(0);' onclick='" + callback_name
	+ "("+ (data.pageCount - 1)+");'>尾页</a>&nbsp";
	}
	bar+="<span style='color:#000000'>跳转到第</span><input style='height:15px;width:40px' id='pageIndex' name='pageIndex'value='"+(data.pageNo + 1)+"' >页<input style='height:27px;width:37px' type='button' value='GO' onclick='onSelect("+data.pageCount+");'/>";
	}
	$('pageTag').innerHTML=bar;
}
function  onSelect(size){
	  pageIndex=$("pageIndex").value-1;
	  pageSize=$("pageSize").value;
	  if(pageIndex>=0&pageIndex<size){
	     excuteInitData(pageIndex); 
	  }else{
		  alert("输入跳转页号有误，请重新输入");
	  }
	}
function  onSelect1(data){
	  pageSize=$("pageSize").value;
	  excuteInitData(0);
	}

dhx.ready(indexInit);