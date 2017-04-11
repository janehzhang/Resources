<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="../../../public/head.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableExamineAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DiffAction.js"></script>
    <script type="text/javascript">
     var tableId='<%=request.getParameter("tableId")%>';
     var stateDate='<%=request.getParameter("date")%>';
     var relType='<%=request.getParameter("relType")%>';
     var tableVersion ='<%=request.getParameter("tableVersion")%>';
     var lastRelId = '<%=request.getParameter("lastRelId")%>';
     var closeMenuId='<%=request.getParameter("menuId")%>';
     var myApp='<%=request.getParameter("myApp")%>';
      function showInfo(){
         var type = document.getElementById("reltype");
         if(relType==0){
             type.innerHTML="申请";
         }
         if(relType==1){
             type.innerHTML="建立";
         }
         if(relType==2){
             type.innerHTML="修改";
         }
         if(relType==3){
             type.innerHTML="维护";
         }
         if(tableVersion==1){
        	 type.innerHTML="新增";
         }
         else{
        	 type.innerHTML="调整";
         }
       }
    </script>
    <script type="text/javascript" src="examineOperate.js"></script>
    <style type="text/css">
     .leftDim{
            text-align: right;
            padding: 5px;
            width: 30%;
            border: 1px #D0E5FF solid;
        }
      .excuBtn{
        padding-left:150px;  
       }
      .buttonStyle  {
           padding-left:600px;
           margin-top:0px;
      }
      .resultButton{
          margin-top:8px;
          padding-left:220px;
      }
	 .but {
			background-image:
				url("<%=rootPath%>/portal/resource/images/buttont_bg001.gif");
		}
	  #_tableBaseInfo{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			border-bottom: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			width: 100%;
			height: 30%;
		}
		#_tableBaseInfo tbody tr td {
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px;  
			text-align: left; 
			padding: 2px; 
			line-height: 15px; 
			background-color: #ffffff; 
			}
		#_tableBaseInfo tbody tr th { 
			white-space: nowrap; 
			text-align: right; 
			font-weight: normal; 
			background-color: #E2EFFF; 
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px; 
			padding: 2px; 
			border-left: 0px
		}
		#_tableBaseInfo tbody td div { 
			text-align: left; 
			font-weight: normal; 
			border: #fff solid 1px; 
			height: 15px; 
			border-left: 0px
		}
		.commnentStyle th{
		    white-space: nowrap; 
		    padding-top:0px;
			font-weight: 12px; 
			background-color: #E2EFFF; 
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px; 
			height:30px;
			border-left: 0px
		 }
		 .commnentStyle td{
		    border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px;  
			height:30px;
			background-color: #ffffff; 
		 }
       .leftDim{
            text-align: right;
            padding: 5px;
            width: 30%;
            border: 1px #D0E5FF solid;
            background-color: #E9F5FE;
        }
        .leftNode{
            text-align: right;
            padding: 5px;
            width: 15%;
            border: 1px #D0E5FF solid;
            background-color: #E9F5FE;
        }
    </style>
</head>
  <body style="width:100%;height:100%" onload="showInfo();">
    <div id="main" style="width:100%;height:100%">
      <div id="title" style="width:100%;height:1%;text-align:center;font-weight: bold;font-size:20px;padding-top:5px;"></div>
      <div id="tableInfo" style="width:100%;">
        <table cellpadding="0" cellspacing="0" border="0" id="_tableBaseInfo">
    	 <tbody>
		    <tr>
		    <th width="15%">申请人：</th>
	           <td width="35%">
	           	<div id="_applyUser"></div>
	           </td>
	           
	           <th width="15%">申请时间：</th>
	          <td width="35%">
	            <div id="_applyTime"></div>
	          </td>
		    </tr>
		    <tr>
			    <th>操作类型：</th>
              <td>
                <div id="reltype"></div>
              </td>
               <th>状态：</th>
                <td>
                <div id="_auditState">待审核</div>
               </td>
		    </tr>    
		    <tr>	      		
	      		 <th>表类名称：</th>
		      <td>
		      	<div id="_tableName"></div>
		      </td>
		      <th>中文名称：</th>
		      <td><div id="_tableNamecn"></div></td>
		    </tr>
		    
		    <tr>
		      <th>层次分类：</th>
		      <td><div id="_codeName"></div></td>
		      <th>业务类型：</th>
		      <td><div id="_tableGroupName"></div></td>
		   
            </tr>
		    <tr>
		     <th width="10%">数据源：</th>
		      	<td width="30%">
   					<div id="_tableDataSource"></div>
	      		</td>
            
		      <th>所属用户：</th>
	          <td>
	             <div id="_tableOwner"></div>
	          </td>
		    </tr>
		    
		    <tr class="commnentStyle">
		      <th>注释：</th>
		       <td colspan=3 ><div id="_tableBusComment" style="height:30px;margin-top:0px;"></div></td>
		    </tr>
		     <tr class="commnentStyle">
		      <th>分区SQL：</th>
		       <td colspan=3 ><div id="_tableSQL" style="height:30px;margin-top:0px;overflow: auto;"></div></td>
		    </tr>
		  </tbody>
    </table>
      </div>
      <div id="tableColumnInfo" style="height:48%;width:100%;margin-top:0px;"></div>
      <div id="buttonDiv" style="height:3%;width:100%;text-align:center;"></div>
    </div>
    <div id="dimInfo">
     <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%"
               class="dhtmlxInfoBarLabel formTable" id="_linkDimInfo">
            <tr style="height: auto; display: none;">
                <th style="height: 0px; width: 30%"></th>
                <th style="height: 0px; width: 70%"></th>
            </tr>
            <tr>
                <td class="leftDim" >表名：</td>
                <td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="_dimTableName"></div>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >中文名：</td>
                <td style="text-align: left;padding: 5px;wi dth: 70%;border: 1px #D0E5FF solid;" >
                    <div id="_dimTableNameCn"></div>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >注释：</td>
                <td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="_dimTableBusComment"></div>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >关联字段：</td>
                <td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="_dimColName"></div>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >维度归并类型：</td>
                <td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="_dimType"></div>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >维度层级：</td>
                <td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="_dimLevel"></div>
                </td>
            </tr>
        </table>
   </div>
      <div id="lookupDiv"></div>
  </body>
</html>