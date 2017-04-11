<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>表类全息视图</title>
    <script type="text/javascript">
        var tableName='<%=URLDecoder.decode(request.getParameter("tableName"),"UTF-8")%>';
        var focus='<%=request.getParameter("focus")%>';
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>';
        var isDimTable='<%=request.getParameter("isDimTable")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableFusionChartsAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/resource/js/flow.js"></script>
    <script type="text/javascript" src="<%=rootPath %>/portal/resource/js/FusionCharts.js"></script>
    <script type="text/javascript" src="viewDetail.js"></script>
    <style type="text/css">
		.but {
			background-image:
				url("<%=rootPath%>/portal/resource/images/buttont_bg001.gif");
		}
		
		#icoTable {
			background: url('<%=rootPath%>/meta/resource/images/ico-h.gif');
			background-position: -50px 0;
			width: 28px;
			height: 24px;
			float: left;
		}
		
		#tableMeg {
			font-weight: bold;
			text-align: left;
		}
		
		#_tableBaseInfo{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			border-bottom: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			width: 100%;
			height: 80%;
		}
		#_tableBaseInfo tbody tr td {
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px;  
			text-align: left; 
			padding: 2px; 
			line-height: 20px; 
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
			border-left: 0px;
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

<body style="height: 100%;width: 100%;overflow: auto;">
<div id="detail" style="height: 100%;width: 100%"></div>
<form action="" id="_baseInfoForm" style="width:100%;height:100%;display: none;">
	<table style="height: 10%">
		<tr>
			<td id="icoTable"></td>
			<td id="tableMeg">表类基本信息</td>
		</tr>
	</table>
    <table cellpadding="0" cellspacing="0" border="0" id="_tableBaseInfo">
    	<tbody>
		    <tr>
		      <th width="15%">申请人：</th>
		      <td width="25%">
	      		<div id="_applyUser">&nbsp;</div>
		      </td>
		      
		       <th width="15%">申请时间：</th>
	           <td width="25%">
	           	<div id="_applyTime">&nbsp;</div>
	           </td>
		    </tr>
		    
		    <tr>
		      <th>审核人：</th>
		      <td>
		      	<div id="_auditUser">&nbsp;</div>
		      </td>
		      <th>审核时间：</th>
	          <td>
	            <div id="_auditDate">&nbsp;</div>
	          </td>
		    </tr>
		    <tr style="height: 40px;">
		      <th>审核意见：</th>
		      <td colspan="3"><div id="_auditMark" style="height: 12px; width: 1000px;">&nbsp;</div></td>
		      <%--<th>&nbsp;</th>--%>
	          <%--<td>&nbsp;</td>--%>
		    </tr>
		    <tr>
		      <th>表类名称：</th>
		      <td><div id="_tableName">&nbsp;</div></td>
	          <th>中文名称：</th>
              <td>
                <div id="_tableNamecn">&nbsp;</div>
              </td>
		    </tr>
		    
		    <tr>
		      <th>层次分类：</th>
		      <td><div id="_codeName">&nbsp;</div></td>
		    <th>业务类型：</th>
            <td>
                <div id="_tableGroupName">&nbsp;</div>
            </td>
            </tr>
		    <%--<tr>--%>
		      <%--<th>业务类型：</th>--%>
		      <%--<td><div id="_tableGroupName">&nbsp;</div></td>--%>
		      <%--<th>审核意见：</th>--%>
	          <%--<td>--%>
	             <%--<div id="_auditContext">&nbsp;</div>--%>
	          <%--</td>--%>
		    <%--</tr>--%>
		    

            <tr style="height: 40px;">
		      <th>分区SQL：</th>
		      <td colspan="3" ><div id="_partitionSql" style="height: 38px; width: 1000px;overflow: auto;">&nbsp;</div></td>
		      <%--<th>&nbsp;</th>--%>
	          <%--<td>&nbsp;</td>--%>
		    </tr>
            <tr style="height: 40px;">
		      <th>注释：</th>
		      <td colspan="3"><div id="_tableBusComment" style="height: 12px; width: 1000px;">&nbsp;</div></td>
		      <%--<th>&nbsp;</th>--%>
	          <%--<td>&nbsp;</td>--%>
		    </tr>
		  </tbody>
    	
    </table>
</form>
<div id="_linkDimInfoForm"  style="height: auto; width: auto; overflow: auto;display: none; ">
    <form action="" >

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
                <td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >
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
                <td class="leftDim" >维层级：</td>
                <td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="_dimLevel"></div>
                </td>
            </tr>
        </table>

    </form>
</div>
<form action="" style="display: none;" id="_buttonForm">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%"
           class="dhtmlxInfoBarLabel formTable" id="_basciButton">
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 82%"></th>
            <th style="height: 0px; width: 8%"></th>
            <th style="height: 0px; width: 10%"></th>

        </tr>
        <tr>
        	<!--  
            <td style="width: 82%">&nbsp;</td>
            <td style="width: 8%;padding: 0px;" align="left"><input type="button" style="background-color: #99CCFF;" value="申请调整" onclick="javascript:goModify()"></td>
            <td style="width: 10%;padding: 0px;" align="left"><input type="button" style="background-color: #99CCFF;" value="导出建表SQL" onclick="javascript:generalSql()"></td>
        	-->
        	<div style="height: 30px; width:100%; position: relative;top:20px;" id="_submit"></div>
        </tr>
    </table>
</form>
<form action="" style="display: none;" id="_generalSqlForm">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;"
           class="dhtmlxInfoBarLabel formTable" id="_generalSqlButton">
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 80%"></th>
        </tr>
        <tr>
            <td style="height: 12px;text-align: center;padding: 0px;width: 10%;font-size: 12px; font-weight: bold;">
                建表SQL
            </td>
        </tr>
        <tr>
            <td style="padding:2px; width: 80%;padding-top: 0px;">
               <!--  <textarea style="width: 100%; height: 100%" rows="16" cols="" id="_generalSql"></textarea> -->
                <div id="_generalSql" style="width:377px; height: 230px;overflow: auto;"></div>
            </td>
        </tr>
    </table>
</form>
<form action="" id="_tableRelView" style="width:100%;height: 100%;display: none;">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%"
           class="" id="_tableRelInfo">
        <tbody>
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 70%"></th>
            <th style="height: 0px; width: 30%"></th>
        </tr>
        <tr style="height: auto;">
            <td colspan="2" style="height: 15px;text-align: center;padding: 0px;width: 10%;font-size: 14px; font-weight: bold;background-color: #CEE8FF;">
                表类关系查询
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>