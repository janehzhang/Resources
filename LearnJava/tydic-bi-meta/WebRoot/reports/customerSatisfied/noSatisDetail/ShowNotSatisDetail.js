/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var page=null;

/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCodeGrid");
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

var indexInit=function(){
	 page=Page.getInstance();
	 page.init();
     
     //执行查询数据
     excuteInitData();
}
var zoneCode="";
var startTime="";
var excuteInitData=function(){
     var map=new Object();
     map.zoneCode    =$("zoneCode").value==""?zoneCode:$("zoneCode").value;
     map.startTime   =$("startTime").value;
     map.endTime     =$("endTime").value;
     map.orderId=$("orderId").value;
     map.busiCode	=$("busiCode").value;
     zoneCode=map.zoneCode;
     startTime= map.startTime;
     
     map.pageCount=  page.pageCount;    //每页显示多少条数
     map.currPageNum=page.currPageNum;//当前第几页
     dhx.showProgress("正在执行......");
     CustomerSatisfiedAction.getNosatisDetailData(map, function (res) {
    	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
					 buildTable(res);
    });
}
function buildTable(data){
	     tableStr ="<table  width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
           tableStr+=tableHeader(data.headColumn);
		   tableStr+=tableData(data.dataColumn,data.headColumn);
        tableStr +="</table>"; 
         $('dataTable').innerHTML=tableStr;
         $("totalCount").value=data.allPageCount;
         page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
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
function tableData(dataColumn,headColumn){//11,12,13,14 宽带装移机不满意,宽带修障不满意,宽带修障未修复,宽带装移机不能正常使用
   //宽带装移机不满意
   //核查情况
   var checkStat_select_html11="<select name='checkStat'><option value=''>--请选择--</option><option value='用户反馈属实'>用户反馈属实</option><option value='用户反馈失实'>用户反馈失实</option><option value='非本区域管辖范围工单'>非本区域管辖范围工单</option></select>";  
   //处理情况
   var handleStat_select_html11="<select name='handleStat'><option value=''>--请选择--</option><option value='已对装机人员进行考核扣罚'>已对装机人员进行考核扣罚</option><option value='已对装机人员进行警示教育'>已对装机人员进行警示教育</option><option value='装机人员无责'>装机人员无责</option><option value='请系统根据工单实际管理单位重新推送工单待办'>请系统根据工单实际管理单位重新推送工单待办</option></select>";
  //宽带修障不满意
   var checkStat_select_html12="<select name='checkStat'><option value=''>--请选择--</option><option value='用户反馈属实'>用户反馈属实</option><option value='用户反馈失实'>用户反馈失实</option><option value='非本区域管辖范围工单'>非本区域管辖范围工单</option></select>";  
   var handleStat_select_html12="<select name='handleStat'><option value=''>--请选择--</option><option value='已对修障人员进行考核扣罚'>已对修障人员进行考核扣罚</option><option value='已对修障人员进行警示教育'>已对修障人员进行警示教育</option><option value='修障人员无责'>修障人员无责</option><option value='请系统根据工单实际管理单位重新推送工单待办'>请系统根据工单实际管理单位重新推送工单待办</option></select>";
  
   //宽带修障未修复
   var checkStat_select_html13="<select name='checkStat'><option value=''>--请选择--</option><option value='修障人员虚假回单'>修障人员虚假回单</option><option value='修障人员技能问题'>修障人员技能问题</option><option value='用户原因'>用户原因</option><option value='非本区域管辖范围工单'>非本区域管辖范围工单</option></select>";
   var handleStat_select_html13="<select name='handleStat'><option value=''>--请选择--</option><option value='已督促解决问题，对修障人员进行考核扣罚'>已督促解决问题，对修障人员进行考核扣罚</option><option value='已督促解决问题，对修障人员进行警示教育'>已督促解决问题，对修障人员进行警示教育</option><option value='已督促解决问题，对修障人员进行技能提升'>已督促解决问题，对修障人员进行技能提升</option><option value='已跟用户沟通，用户自行处理'>已跟用户沟通，用户自行处理</option><option value='请系统根据工单实际管理单位重新推送工单待办'>请系统根据工单实际管理单位重新推送工单待办</option></select>";
  //宽带装移机不能正常使用
   var checkStat_select_html14="<select name='checkStat'><option value=''>--请选择--</option><option value='装机人员虚假回单'>装机人员虚假回单</option><option value='装机人员技能问题'>装机人员技能问题</option><option value='用户原因'>用户原因</option><option value='非本区域管辖范围工单'>非本区域管辖范围工单</option></select>";
   var handleStat_select_html14="<select name='handleStat'><option value=''>--请选择--</option><option value='已督促解决问题，对装机人员进行考核扣罚'>已督促解决问题，对装机人员进行考核扣罚</option><option value='已督促解决问题，对装机人员进行警示教育'>已督促解决问题，对装机人员进行警示教育</option><option value='已督促解决问题，对装机人员进行技能提升'>已督促解决问题，对装机人员进行技能提升</option><option value='已跟用户沟通，用户自行处理'>已跟用户沟通，用户自行处理</option><option value='请系统根据工单实际管理单位重新推送工单待办'>请系统根据工单实际管理单位重新推送工单待办</option></select>";

   switch (typeId) {
	   case 11:
			checkStat_select_html=checkStat_select_html11;
			handleStat_select_html=handleStat_select_html11;
			break;
	   case 12:
		    checkStat_select_html=checkStat_select_html12;
			handleStat_select_html=handleStat_select_html12;
			break;
		case 13:
			checkStat_select_html=checkStat_select_html13;
			handleStat_select_html=handleStat_select_html13;
			break;
		case 14:
			checkStat_select_html=checkStat_select_html14;
			handleStat_select_html=handleStat_select_html14;
			break;
		default:
			break;
   		}
   
   var memo_input_html="<input type='text' maxlength='80' style='width: 200px' name='memo'/>";
   var  excelData="";
	    var  tableData="";
	    var flag=false;
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
			    		               if(tempColumn == '核查情况')
			    		               {
			    		                    dataColumn[i][tempColumn]= dataColumn[i][tempColumn].replaceAll('&nbsp;','');
			    		            	   if(dataColumn[i][tempColumn]!='') {
			    		            		   tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>";
				    		                    tableData +="</td>"; 
				    		                    flag=true;
			    		            	   }else{
			    		            		   tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>"+checkStat_select_html;
				    		                    tableData +="</td>"; 
				    		                    flag=false;
			    		            	   }
			    		               }
			    		               
			    		               else  if(tempColumn == '处理情况')
			    		               {
				    		            	   dataColumn[i][tempColumn]= dataColumn[i][tempColumn].replaceAll('&nbsp;','');
				    		            	   if(dataColumn[i][tempColumn]!='') {
				    		            		   tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>";
					    		                    tableData +="</td>"; 
					    		                    flag=true;
				    		            	   }else{
				    		            		   tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>"+handleStat_select_html;
					    		                    tableData +="</td>"; 
					    		                    flag=false;
				    		            	   }
				    		               }
			    		               else  if(tempColumn == '备注')
			    		               {
				    		            	   dataColumn[i][tempColumn]= dataColumn[i][tempColumn].replaceAll('&nbsp;','');
				    		            	   if(flag) {
				    		            		   tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>";
					    		                    tableData +="</td>"; 
					    		                    flag=true;
				    		            	   }else{
				    		            		   tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>"+memo_input_html;
					    		                    tableData +="</td>"; 
					    		                    flag=false;
				    		            	   }
				    		               }
			    		               //备注
			    		               else  if(tempColumn == '操作')
			    		               {  
			    		            	   if(flag){
			    		            		   tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>";
				    		                    tableData +="</td>"; 
				    		                    flag=true;
			    		            	   }else{
				    		                  	tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>";
				    		                  	tableData += "<input type=\"button\"  onclick=\"fillVisitResult('"+dataColumn[i].外呼工单号+"',this)\" class=\"poster_btn\"    name=\"queryBtn\" value=\"保 存\" style=\"width:60px;\" />";
				    		                  	tableData +="</td>"; 
			    		            	   }
				    		               }
			    		               else
			    		               {
			    		            	    tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";    
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
	if(isEffectDate())
   {
	  page.resetPage();
	  excuteInitData();
   }
   else
   {
	   alert("不能选择跨月！");	
   }
}
//消息推送
var sendMess=function(){
	var map=new Object()
	map.zoneCode=$("zoneCode").value==null?"0000":$("zoneCode").value;
	map.startTime=$("startTime").value;
	map.endTime=$("endTime").value;
	map.orderId=$("orderId").value;
	dhx.showProgress("正在执行......");
	CustomerSatisfiedAction.sendNoSatisDetailData(map,function(res){
		dhx.closeProgress();
		if(res==false){
			dhx.alert("查询失败，请稍后重试!");
			return;
		}else{
			dhx.alert("消息推送成功!");
			return;
		}
	});
}
//比较两个日期
var isEffectDate=function()
{
	var startTime=$("startTime").value;
	startTime=startTime.substr(5,2);
	var endTime=  $("endTime").value;
	endTime=endTime.substr(5,2);
	if(startTime==endTime){
	   return true;
	}else{
	   return false;
	}
}
var fillVisitResult=function(id,e)
{
	//confirm("保存后将不能修改，是否保存？");
	var checkStat="";
	var handleStat="";
	var memo="";
	
	checkStat=e.parentNode.previousSibling.previousSibling.previousSibling.lastChild.value;
	handleStat=e.parentNode.previousSibling.previousSibling.lastChild.value;
	memo=e.parentNode.previousSibling.lastChild.value;
	if(checkStat==''){
		alert('请选择核查结果');
		return;
	}
	if(handleStat==''){
		alert('请填写处理结果');
		return;
	}
		
 
  if(confirm("保存后将不能再次修改，是否保存？")){
	  var map=new Object();

	  map.id=id;
	  map.checkStat=checkStat;
	  map.handleStat=handleStat;
	  map.memo=memo;
	  map.url=startTime+"_"+zoneCode;
	  map.startTime=startTime;
	  map.executor='sys';
	  
	  CustomerSatisfiedAction.updateVisitResultIVR(map, function (res) {
		  //dhx.alert("提交中，请稍侯");
		   dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
				      history.go(0);
        });
  }
}

function finish(guid){
	var map=new Object();
	map.guid=guid;
	 if(confirm("结束待办后将无法再次查看，是否结束？")){
	CustomerSatisfiedAction.finishEAIC(map, function (res) {
		  //dhx.alert("提交中，请稍侯");
		   dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }else if(res==1){
				        	dhx.alert("待办已删除");
				        }
				      window.close();
      });
	 }
}

dhx.ready(indexInit);