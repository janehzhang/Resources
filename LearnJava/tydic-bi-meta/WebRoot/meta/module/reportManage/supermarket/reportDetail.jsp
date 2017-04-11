<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="../../../public/head.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
String reportId = request.getParameter("reportId");	 //报表id
String showTypeId = "0";		//是否显示头信息(1为是，0为否)
%>
<head>
    <title>报表详情</title>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=PERORT_SEND_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/RepSupermarketAction.js"></script>
    <script type="text/javascript" src="reportDetail.js"></script>
	<script type="text/javascript" src="favCommon.js"></script>
	<script type="text/javascript">
     	var reportId='<%=request.getParameter("reportId")%>';	//报表id
	</script>
	<link href="baobiao.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        .showReportDetail{
        	height: 238px;
        	width: 100%;
        	padding: 0px;
        	margin: 0px;
          }
        .showReport{
        	height:450px;
        	width: 100%;
        	padding: 0px;
        	margin: 0px;
        }
    	.tableBaseInfo{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			margin-top: 2px;
			border-bottom: #99b2c9 solid 1px;
		}
		.tableTd{
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px; 
			text-align: left; 
			padding-left: 2px; 
			line-height: 24px;
			background-color: #ffffff; 
		}
		.tableTh { 
			white-space: nowrap; 
			text-align: right; 
			font-weight: normal; 
			background-color: #E2EFFF; 
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px;  
			padding: 0px; 
			margin: 0px;
			line-height: 24px;
		}
		.classa{
			margin-right: 20px;
		}
		a:hover {
			color: blue;;
		}
    </style>
</head>
  <body style="width:100%;height:100%;">
  <div id="container" style="height: 99%;width: 100%">
    <div class="showReportDetail" id="showReportDetail">
      <table border="1" cellpadding="0" cellspacing="0" width="100%" class="tableBaseInfo">
	    <tr>
	      <th class="tableTh" colspan="4" style="text-align:left;">
			    <div style="height: 30px;line-height: 30px; text-align: right;">
			       <a class="classa" id="rptFav">收藏报表</a>
			       <a class="classa">修改报表</a>
			       <a class="classa">复制并修改报表</a>
			    </div>
		  </th>
	    </tr>
      	<tr>
      		<th class="tableTh" width="20%">报表名称：</th>
      		<td class="tableTd" width="30%"><span id="reportName"></span></td>
      		<th class="tableTh" width="20%">报表分类：</th>
      		<td class="tableTd" width="30%"><span id="reportTypeName"></span></td>
      	</tr>
      	<tr>
      		<th class="tableTh">创建人：</th>
      		<td class="tableTd"><span id="userNameCn"></span></td>
      		<th class="tableTh" >创建部门：</th>
      		<td class="tableTd"><span id="deptName"></span></td>
      	</tr>
      	<tr>
      		<th class="tableTh">生效时间：</th>
      		<td class="tableTd"><span id="startDate"></span></td>
      		<th class="tableTh" >有效时间：</th>
      		<td class="tableTd"><span id="showEffectTime"></span></td>
      	</tr>
      	<tr>
      		<th class="tableTh">关键字：</th>
      		<td class="tableTd" colspan="3"><span id="reportKeyword"></span></td>
      	</tr>
      	<tr>
      		<th class="tableTh">报表说明：</th>
      		<td class="tableTd" colspan="3">
    			<textarea id="reportNote" rows="3" style="width: 70%" readonly="readonly"></textarea>
    		</td>
      	</tr>
      </table>
    </div>
    <div id="showReport" class="showReport">
	 
    </div>
     <div class="favDiv" id="favDiv"></div>
     <div class="subDiv" id="subDiv"></div>
   </div>
  </body>
</html>

