<%@ page import="java.net.URLDecoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title>查看基础指标</title>
	<%@include file="../../../public/header.jsp"%>
	<script type="text/javascript"
		src="<%=rootPath%>/meta/public/code.jsp?types=GDL_GROUP_METHOD,GDL_NUMFORMAT,GDL_TYPE,GDL_STATE">
	</script>
	<script type="text/javascript"
		src="<%=rootPath%>/dwr/interface/GdlBasicMagAction.js">
	</script>
	<script type="text/javascript" src="viewGdlBasic.js"></script>
	<script type="text/javascript">
		var gdlId=<%=request.getParameter("gdlId")%>;				//指标id
		var gdlVersion=<%=request.getParameter("gdlVersion")%>;		//指标版本
	</script>
	<style type="text/css">
		#_tableBaseInfo {
			border-left: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			margin: 2px auto;
			border-bottom: #99b2c9 solid 1px;
			width: 92%;
		}
		
		.tableBaseInfoValue {
			border-top: #99b2c9 solid 1px;
			border-right: #99b2c9 solid 1px;
			text-align: left;
			padding-left: 2px;
			line-height: 15px;
			height: 24px;
			background-color: #ffffff;
		}
		
		.tableBaseInfoTitle {
			white-space: nowrap;
			text-align: right;
			font-weight: normal;
			background-color: #E2EFFF;
			border-top: #99b2c9 solid 1px;
			border-right: #99b2c9 solid 1px;
			padding: 0px;
			margin: 0px;
			height: 24px;
		}
		
		#icoTable {
			background: url('<%=rootPath%>/meta/resource/images/ico-h.gif');
			background-position: -50px 0;
			width: 28px;
			height: 24px;
			float: left;
			text-align: right;
		}
		
		#tableMeg {
			font-weight: bold;
			padding-top: 6px;
			font-size: 14px;
		}
		
		#dimtermTable {
			width: 90%;
		}
		
		#dimtermTable tbody tr td {
			height: 20px;
		}
	</style>
</head>
<body>
	<div id="pageContent"
		style="position: absolute; top: 0px; left: 0px; right: 0px; bottom: 0px; overflow-x: hidden; overflow-y: auto;">
		<div id="gdlInfo" style="width: 100%;">
			<table>
				<tr>
					<td id="icoTable"></td>
					<td id="tableMeg">
						基本信息
					</td>
				</tr>
			</table>
			<div id="tableInfo"
				style="width: 100%; padding-top: 5px; padding-bottom: 5px;">
				<table cellpadding="0" cellspacing="0" id="_tableBaseInfo">
					<tr>
						<td class="tableBaseInfoTitle" width="15%">
							编码：
						</td>
						<td class="tableBaseInfoValue" width="35%">
							<div id="gdl_code">
								&nbsp;
							</div>
						</td>
						<td class="tableBaseInfoTitle" width="15%">
							名称：
						</td>
						<td class="tableBaseInfoValue" width="35%">
							<div id="gdl_name">
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td class="tableBaseInfoTitle">
							类型：
						</td>
						<td class="tableBaseInfoValue">
							<div id="gdl_type">
								&nbsp;
							</div>
						</td>
						<td class="tableBaseInfoTitle">
							状态：
						</td>
						<td class="tableBaseInfoValue">
							<div id="gdl_state">
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td class="tableBaseInfoTitle">
							单位：
						</td>
						<td class="tableBaseInfoValue">
							<div id="gdl_unit">
								&nbsp;
							</div>
						</td>
						<td class="tableBaseInfoTitle">
							指标分类：
						</td>
						<td class="tableBaseInfoValue">
							<div id="gdl_group">
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td class="tableBaseInfoTitle">
							默认展示格式：
						</td>
						<td class="tableBaseInfoValue" colspan="3">
							<div id="num_frommat">
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td class="tableBaseInfoTitle">
							来源表类：
						</td>
						<td class="tableBaseInfoValue">
							<div id="gdl_src_table_name">
								&nbsp;
							</div>
						</td>
						<td class="tableBaseInfoTitle">
							来源列名：
						</td>
						<td class="tableBaseInfoValue" style="text-align: left; padding: 1px; width: 35%">
							<div id="gdl_src_col">
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td class="tableBaseInfoTitle">
							创建人：
						</td>
						<td class="tableBaseInfoValue">
							<div id="user_namecn">
								&nbsp;
							</div>
						</td>
						<td class="tableBaseInfoTitle">
							创建时间：
						</td>
						<td class="tableBaseInfoValue" style="text-align: left; padding: 1px; width: 35%">
							<div id="create_time">
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td class="tableBaseInfoTitle" style="min-height: 25px;">
							业务解释：
						</td>
						<td class="tableBaseInfoValue" colspan="3">
							<div id="gdl_bus_desc">
								&nbsp;
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div id="dimData" style="width: 100%;">
			<table>
				<tr>
					<td id="icoTable"></td>
					<td id="tableMeg">
						维度分组计算方法
					</td>
				</tr>
			</table>
			<div id="main"
				style="width: 70%; min-height: 270px; padding-left: 120px;">
				<div id="tableInfo" style="width: 100%;padding-top: 5px;">
					<table cellpadding="0" cellspacing="1" class='MetaFormTable'
						id="dimtermTable">
						<tr>
							<td style="width: 4%;"></td>
							<td style="width:41%; text-align: center;">
								<span style="font-weight: bold;">维度名</span>
							</td>
							<td style="width:55%; text-align: center;">
								<span style="font-weight: bold;">分组计算方法</span>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div id="buttonDiv"
			style="width: 100%; text-align: center; height: 20px;">
			<input name="" id="colseWin" type="button" value="关闭"
				class="btn_2" />
		</div>
	</div>
</body>
</html>