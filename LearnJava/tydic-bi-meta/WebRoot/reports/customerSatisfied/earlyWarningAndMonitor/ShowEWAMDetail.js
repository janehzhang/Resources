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
     map.vTypeId	=$("vTypeId").value;
     map.targetId	=$("targetId").value;
     map.judgeResultId	=$("judgeResultId").value;
     zoneCode=map.zoneCode;
     startTime= map.startTime;
     
     map.pageCount=  page.pageCount;    //每页显示多少条数
     map.currPageNum=page.currPageNum;//当前第几页
     dhx.showProgress("正在执行......");
     CustomerSatisfiedAction.getEWAMDetailData(map, function (res) {
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
function tableData(dataColumn,headColumn){
   
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
			    		               if(tempColumn == '操作' &&(dataColumn[i].检查反馈 == ' ' || dataColumn[i].检查反馈 == null))
			    		               {  
			    		            	   if(flag){
			    		            		   tableData +="<td><span style='float: center;'>"+dataColumn[i][tempColumn]+"</span>";
				    		                    tableData +="</td>"; 
				    		                    flag=true;
			    		            	   }else{
				    		                  	tableData +="<td><span style='float: center;'></span>";
				    		                  	tableData += "<a href='javascript:void(0)' onClick=\"feedbackResult('"+dataColumn[i].UNIQUE_VALUE+"')\">填写反馈</a>&nbsp;";
				    		                  	tableData +="</td>"; 
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

var feedbackResult=function(seq){
    //初始化新增弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("editWindow", 0, 0, 100, 200);
    var editWindow = dhxWindow.window("editWindow");
    editWindow.setModal(true);
    editWindow.stick();
    editWindow.setDimension(450, 300);
    editWindow.center();
    editWindow.setPosition(editWindow.getPosition()[0]-100,editWindow.getPosition()[1]-80);
    editWindow.denyResize();
    editWindow.denyPark();
    editWindow.button("minmax1").hide();
    editWindow.button("park").hide();
    editWindow.button("stick").hide();
    editWindow.button("sticked").hide();
    editWindow.setText("异常情况反馈填写");
    editWindow.keepInViewport(true);
    editWindow.show();
    /**
     * 填写内容
     */
    var editFormData=[
                       {type:"block",offsetTop:15,list:[                           
             	            {type:"input",offsetLeft:30,rows:5,label:"<span style='color: red'>*</span> 反馈内容： ",inputWidth: 370,name:"staffName",validate:"NotEmpty,MaxLength[600]"},
             	            {type:"newcolumn"}             	            
             	        ]},
             	        {type:"block",offsetTop:15,list:[
             	            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:160},
             	            {type:"newcolumn"},
             	            {type:"button",label:"关闭",name:"close",value:"关闭",offsetLeft:10}
             	        ]}
    ];
    //建立表单。
    var editForm = editWindow.attachForm(editFormData);

    //新增表单事件处理
    editForm.attachEvent("onButtonClick", function(id) {
        var validateAdd=function(){
        	var data = editForm.getFormData();
	    	if(data.staffName==null||data.staffName==''){
	    	    alert('核查反馈信息不能为空！');
	    	    return false;
	    	}
	    	return true;
        }
        if (id == "save") {
        	var data= editForm.getFormData();
        	if(validateAdd()){
    	        dhx.confirm("保存后将无法再次修改，是否确定所填写的反馈信息无误？", function(r){
    	            if(r){
    	            	var map=new Object();
    	            	
    	            	map.uniqueValue = seq;
    	            	map.feedBack = data.staffName;
    	            	
//    	          	  	map.id=id;
//    	          	  	map.url=startTime+"_"+zoneCode;
    	          	  	map.startTime=startTime;
    	          	  	map.executor='sys';
    	          	  	map.ewamFlag='1';

    	          	  	CustomerSatisfiedAction.updateEwamFeedback(map, function (res) {
    	          		  //dhx.alert("提交中，请稍侯");
    	          		   dhx.closeProgress();
    	          		   if (res == null) {
    	          				dhx.alert("填写失败,请稍后重试!");
    	          				return;
    	          		   }
    	          		   dhx.alert("修改成功！");
	                       editWindow.close();
	                       queryData();
    	                });
    	            }
    	        })
    	    }
        }
        if(id == "close"){
        	editWindow.close();
            dhxWindow.unload();
        }
    });
   
}

dhx.ready(indexInit);