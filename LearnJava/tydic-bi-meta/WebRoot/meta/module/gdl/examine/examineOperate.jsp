<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=GDL_NUMFORMAT"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlExamineAction.js"></script>
     <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlBasicMagAction.js"></script>
    <script type="text/javascript" src="examineOperate.js"></script>
    <script type="text/javascript">
     var gdlId = '<%=request.getParameter("gdlId")%>';
     var gdlVersion = '<%=request.getParameter("gdlVersion")%>';
    </script>
    
       <style type="text/css">
        #_tableBaseInfo{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			margin-top: 2px;
			border-bottom: #99b2c9 solid 1px;
			width: 100%;
		}
		#_tableBaseInfo tbody tr td {
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px; 
			text-align: left; 
			padding-left: 2px; 
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
			padding: 0px; 
			margin: 0px;
		}
        .but{
            background-image: url("<%=rootPath%>/portal/resource/images/buttont_bg001.gif");
        }
        .lable_name{
            text-align: right;
        }
        .table_input{
            width: 200px;
            height: 20px;
        }
        .labelMeg{
            color: red;
        }
        .pass-desc {
            color:#FFA500;
        }
        #icoTable {
			background: url('<%=rootPath%>/meta/resource/images/ico-h.gif');
			background-position: -50px 0;
			width: 28px;
			height: 24px;
			float: left;
			text-align: right;
		}
		#tableMeg{
			font-weight: bold;
			padding-top:6px;
		}
    </style>
</head>
  <body style="width:100%;height:100%"">
  <div id="pageContent"
		style="position: absolute; top: 0px; left: 0px; right: 0px; bottom: 0px; overflow-x: hidden; overflow-y: auto;">
  <form action="" id="examine_Form" onsubmit="return false;" >
    <div id="main" style="width:70%;height:100%;padding:0 50px;overflow: auto;">
      <div id="title" style="width:100%;height:1%;text-align:center;font-weight: bold;font-size:20px;padding-top:5px;"></div>
      <div id="tableInfo" style="width:100%;">
      <table>
		<tr>
			<td id="icoTable"></td>
			<td id="tableMeg">基本信息</td>
		</tr>
	  </table>
      <table cellpadding="0" cellspacing="0" id="_tableBaseInfo" style="height:120px;">
		<tr>
		    <input type="hidden" id="gdl_id" style="width: 193px"></input>
			<input type="hidden" id="gdl_version" style="width: 193px"></input>
			<th width="15%"><span style='color:red'>*</span>编码：</th>
			<td width="35%"><input id="gdl_code" style="width: 193px"></input></td>
            <th><span style='color:red'>*</span>名称：</th>
            <td><input type="text" id="gdl_name" style="width: 193px;"/></td>
		</tr>
		<tr>
			<th>类型：</th>
			<td style="height: 20px"><div id="gdl_type">&nbsp;</div></td>
			<th>状态：</th>
			<td><div id="gdl_state">&nbsp;</div></td>
		</tr>
		<tr>
			<th><span style='color:red'>*</span>单位：</th>
			<td><input id="gdl_unit" style="width: 193px;"/></td>
            <th><span style='color:red'>*</span><%=I18nManager.getItemText(menuId,"GDL_CODE","指标分类")%>：</th>
            <td style="text-align: left;padding: 1px;width: 35%"><input type="text" id="gdlGroup"></td>
		</tr>
		<tr>
			<th>来源表类：</th>
			<td style="height: 20px"><div id="gdl_src_table_name">&nbsp;</div></td>
            <th>来源列名：</th>
            <td style="text-align: left;padding: 1px;width: 35%"><div id="gdl_col_name"></div></td>
		</tr>
		<tr>
			<th>创建人：</th>
			<td style="height: 20px"><div id="user_namecn"></div></td>
            <th>创建时间：</th>
            <td style="text-align: left;padding: 1px;width: 35%"><div id="create_time"></div></td>
		</tr>
		<tr>
            <th><span style='color:red'>*</span>业务解释：</th>
            <td colspan="3">
                <textarea name="gdl_bus_desc" id="gdl_bus_desc" rows="2" cols="100" style="width: 85%;"></textarea>
            </td>
		</tr>
	</table>
	<table>
		<tr>
			<td id="icoTable"></td>
			<td id="tableMeg">维度分组计算方法</td>
		</tr>
	</table>
    <div id="main">
      <div id="title" style="width:100%;height:1%;text-align:center;font-weight: bold;font-size:20px;padding-top:5px;"></div>
      <div id="tableInfo" >
      <table cellpadding="0" cellspacing="1" class='MetaFormTable'>
      <tbody id="dimtermTable" >
		<tr>
			<td width="15%" style="text-align:center;"><span style="font-weight: bold;">维度名</span></td>
            <td style="text-align:center;"><span style="font-weight: bold;">分组计算方法</span></td>
		</tr>
	  </tbody>
	</table>
      </div>
    </div>
     <table>
		<tr>
			<td id="icoTable"></td>
			<td id="tableMeg">审核信息</td>
		</tr>
	</table>
     <div id="title" style="width:100%;height:1%;text-align:center;font-weight: bold;font-size:20px;padding-top:5px;"></div>
      <div id="tableInfo" style="width:100%;">
      <table cellpadding="0" cellspacing="0" id="_tableBaseInfo" style="height:120px;">
		<tr>
			<th width="15%">审核结论：</th>
			<td width="85%" >
				<INPUT type=radio name="audit"  id="audit1" value="1" CHECKED/>通&nbsp;&nbsp;&nbsp;过&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<INPUT type=radio name="audit"  id="audit2" value="2"  />驳&nbsp;&nbsp;&nbsp;回
            </td>
		</tr>
		<tr>
			<th width="10%">生效时间：</th> 
			<td width="90%" >
				<INPUT type=radio name="effectivetime" id="effective_time1" value="1" CHECKED/>立即生效&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<INPUT type=radio name="effectivetime" id="effective_time2" value="2" />延时生效:
				<INPUT type=calendar id="valid_time" disabled = true />
				<span style='color:red'>*</span>输入格式：yyyy-mm-dd hh24:mi:ss
            </td>
		</tr>
	    <tr>
			<th width="10%">审核意见：</th>
			<td width="90%">
				 <textarea name="audit_opinion" id="audit_opinion" rows="12" cols="100" style="width: 85%;"></textarea>
            </td>
		</tr>
		
		<tr>
            <td colspan="4" style="text-align: center;"> 
                <input name="" id="newBtn" type="button" value="<%=I18nManager.getItemText(menuId,"SAVE_BTN","审核")%>" class="btn_2" />
            </td>
        </tr>
	
	</table>
      </div>
    </div>
      </div> 
      </form>
      </div>
  </body>
</html>