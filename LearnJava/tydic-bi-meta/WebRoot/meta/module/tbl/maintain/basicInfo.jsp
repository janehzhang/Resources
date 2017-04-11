<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript">
        var tableName='<%=URLDecoder.decode(request.getParameter("tableName"),"UTF-8")%>';
        var focus='<%=request.getParameter("focus")%>';
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>';
        var isDimTable='<%=request.getParameter("isDimTable")%>';
        var myApp='<%=request.getParameter("myApp")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=DATA_TYPE,TABLE_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MaintainRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableFusionChartsAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/resource/js/flow.js"></script>
    <script type="text/javascript" src="<%=rootPath %>/portal/resource/js/FusionCharts.js"></script>
    <script type="text/javascript" src="<%=rootPath %>/dwr/interface/MaintainRelAction.js"></script>
    <script type="text/javascript" src="basicInfo.js"></script>
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
<body style="height: 100%;width: 100%">

<div id="detail" style="height: 100%;width: 100%"></div>
<div style="height: 100%; width:100%; " id="_relFormDiv">
    <form action="" id="_relForm" style="width:100%;height: auto" onsubmit="return false;">
        <div style="width: 100%;height: 100%">
            <div style="width:100%" class="gridbox" id="_relTableDiv">
                <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                     class="xhdr">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr" id="_relColumnHeadTable">
                        <tbody>
                        <tr style="height: 0px; ">
                            <%--定义表头列宽--%>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 9% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 22% "></th>
                            <th style="height: 0px; width: 9% "></th>
                        </tr>

                        <tr>
                            <td style="text-align: center; ">列名</td>
                            <td style="text-align: center; ">关联类型</td>
                            <td style="text-align: center; ">关联表类名</td>
                            <td style="text-align: center; ">关联列名</td>
                            <td style="text-align: center; ">说明</td>
                            <td style="text-align: center; ">操作
                                <img src="../../../resource/images/delete.png" alt="清空所有"
                                     style="cursor: pointer;width: 16px;height: 16px;position: relative;" onclick="clearAllColumnData()"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <%--表体内容--%>
                <div style="width: 100%;  overflow-y: scroll;" class="objbox" id="_relContentDiv">
                    <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_relContentTable">
                        <tbody id="_relContentBody">
                        <tr style="height: 0px; ">
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 9% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 22% "></th>
                            <th style="height: 0px; width: 9% "></th>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </form>
</div>
<form action="" id="_charForm" onsubmit="return false;">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%"
           class="" id="_charTable">
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 90%"></th>
            <th style="height: 0px; width: 10%"></th>
        </tr>

        <tr>
            <td style="width: 90%;">
                <div id="chartdiv"></div>
            </td>
            <td style="width: 10%;">
                <div id="chartRight"></div>
            </td>
        </tr>
    </table>
</form>
<form action="" id="updataTableBase_Form" onsubmit="return false;" >
	<table style="height: 10%;width: 100%">
		<tr>
			<td id="icoTable"></td>
			<td id="tableMeg">修改表类基本信息</td>
			<td width="89%" style="text-align: right">
				<%--<span onclick="openSystemTable()">--%>
					<%--<img src="<%=rootPath %>/meta/resource/images/dataBase.png" width="18px" height="18px"></img>--%>
					<%--从数据表中导入</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
				<%--<span onclick="openMetaTable()">--%>
					<%--<img src="<%=rootPath %>/meta/resource/images/tableImport.png" width="18px" height="18px"></img>--%>
					<%--从原数据表类复制</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
			</td>
		</tr>
	</table>
	
	<table cellpadding="0" cellspacing="0" id="_tableBaseInfo" style="height:120px;">
		<tr>
			<th width="10%">表类名称：</th>
			<td width="40%">
				<input id="tableName" readonly="readonly" disabled="disabled" style="width: 193px"></input>
				<label class="labelMeg">*</label>
	            <label class="pass-desc">注：表类的名称，可以带宏变量 </label>
            </td>
            <th>中文名称：</th>
            <td>
            	<input type="text" id="table_name_cn" style="width: 193px;"/>
            	<label class="labelMeg">*</label>
                <label class="pass-desc">注：待修改表类中文名</label>
            </td>
		</tr>
		
		<tr>
			<th>层次分类：</th>
			<td>
				<select id="table_attType" class="table_input" onchange="changeAttType()" style="width: 198px;" disabled="true">
                    <option value="">----请选择----</option>
                </select>
                <label class="labelMeg">*</label>
                <label class="pass-desc">注：表类层次层次分类 </label>
			</td>
			
			<th>业务类型：</th>
			<td>
				<input type="text" id="table_operType" style="width: 193px;">
				<label class="labelMeg">*</label>
                <label class="pass-desc">注：表类业务类型 </label>
                <input type="hidden" id="tableGroupId"/>
			</td>
		</tr>
		
		<tr>
			<th>数据源：</th>
			<td>
				<input id="data_source" style="width: 193px;" readonly="readonly" disabled="disabled"/>
				<label class="labelMeg">*</label>
                <label class="pass-desc">注：表类所在用户数据源</label>
                <input id="data_sourceHidden" type="hidden"/>
			</td>
            <th>所属用户：</th>
            <td style="text-align: left;padding: 1px;width: 35%">
                <select name="tableOwner" id="_tableOwner" style="width: 193px;">
                </select>
            </td>

		</tr>
		
		<tr>
            <th>表空间：</th>
            <td colspan="3">
                <select name="tableSpace" id="_tableSpace" style="width: 193px;">
                </select>
            </td>
		</tr>
		
		<tr>
			<th>注释：</th>
			<td>
				<textarea name="tableBusComment" id="_tableBusComment" rows="2" cols="100" style="width: 85%;"></textarea>
			</td>
            <th>分区SQL：</th>
			<td>
				<textarea name="partitionSql" id="_partitionSql" rows="2" cols="100" style="width: 85%;"></textarea>
			</td>
		</tr>
	</table>
   
</form>
<div style="height: 60%; width:100%;" id="_columnFormDiv">
    <form action="" id="_columnForm" style="width:100%;height: 100%">
        <%--表头DIV--%>
        <div style="width: 100%;height: 100%">
            <div style="width:100%" class="gridbox" id="_columnTableDiv">
                <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                     class="xhdr">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%;table-layout: fixed;" class="hdr" id="_columnHeadTable">
                        <tbody>
                        <tr style="height: 0px; ">
                            <%--定义表头列宽--%>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 15% "></th>
                            <th style="height: 0px; width: 15%"></th>
                            <th style="height: 0px; width: 10%"></th>
                        </tr>
                        <tr>
                            <td style="text-align: center; line-height:22px;">名称<span style="color: red;">*</span></td>
                            <td style="text-align: center; line-height:22px;">列中文名</td>
                            <td style="text-align: center; line-height:22px;">类型<span style="color: red;">*</span></td>
                            <td style="text-align: center; line-height:22px;">主键</td>
                            <td style="text-align: center; line-height:22px;">默认值</td>
                            <td style="text-align: center; line-height:22px;">允许空</td>
                            <td style="text-align: center; line-height:22px;">注释</td>
                            <td style="text-align: center; line-height:22px;">维度</td>
                            <td style="text-align: center; line-height:22px;">操作</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <%--表体内容--%>
                <div style="width: 100%;  overflow-y: scroll;" class="objbox" id="_clumnContentDiv">
                    <table cellpadding="0" cellspacing="0" style=" width: auto%;height: auto;table-layout: fixed;" class="obj" id="_clumnContentTable">
                        <tbody id="_clumnContentBody">
                        <%--定义表体列宽--%>
                        <tr style="height: 0px; ">
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 15% "></th>
                            <th style="height: 0px; width: 15%"></th>
                            <th style="height: 0px; width: 10%"></th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <%--提交按钮--%>
    <div style="height: 30px; width:100%; position: relative;margin-top: 60px;" id="_submit"></div>
    </form>
</div>

<!-- 维表 -->
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;"
           class="dhtmlxInfoBarLabel formTable" id="_dimInfoTable">

    <form action="" id="_queryDimForm" onsubmit="return false;">
        <tr style="height: 0px;">
            <th style="height: 0px; width: 15% "></th>
            <th style="height: 0px; width: 25% "></th>
            <th style="height: 0px; width: 15% "></th>
            <th style="height: 0px; width: 25% "></th>
            <th style="height: 0px; width: 20% "></th>
        </tr>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 2px ">查询维表：</td>
            <td style="text-align: left;padding: 2px">
                <input type="hidden" name="tableGroupId" id="_queryDimId" />
                <input type="hidden" name="tableTypeId" value="2" />
                <input class="dhxlist_txt_textarea" name="tableGroup" id="_queryDimName" type="text" style="padding-right: 0px;width: 100%" />
            </td>
            <td style="text-align: right;padding: 2px">名称：</td>
            <td style="text-align: left;padding: 2px">
                <input class="dhxlist_txt_textarea" name="queryMessage" id="_queryDimTableName" type="text" style="width: 100%;"/>
            </td>
            <td style="text-align: center;padding-left:20px; " id="_queryDimButton"></td>
        </tr>
        </form>
        <tr>
            <td colspan="5" style="width:100%;height:90%;">
                <div id="_queryDimGrid" style="width:100%;height:100%;"> </div>
            </td>
        </tr>
    </table>


<!-- 从元数据中查询 -->
<form action="" id="_queryTables" onsubmit="return false;">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;"
           class="dhtmlxInfoBarLabel formTable" id="_importFromTable">
        <%--查询表单行--%>
        <tr style="height: 10%">
            <td style="text-align: right;padding: 1px;width: 10%">业务类型：</td>
            <td style="text-align: left;padding: 1px;width: 35%">
                <input type="hidden" name="tableGroupId" id="_queryTableGroupId" />
                <input class="dhxlist_txt_textarea" name="tableGroup" id="_queryTableGroup" type="text" style="padding-right: 0px;width: 80% " />
            </td>
            <td  style="text-align: right;padding: 1px;width: 10%">关键字：</td>
            <td style="text-align: left;padding: 1px;width: 10%">
                <input class="dhxlist_txt_textarea" name="keyWord" id="_tableKeyWord" type="text" style="width: 80% "/>
            </td>
            <td id="_queryTableButton" style="text-align: center;width: 10%">
            </td>
        </tr>
        <!--查询出的grid-->
        <tr>
            <td colspan="5" style="position: relative;height:90%">
                <div id="_queryTableGrid" style="height: 100%;width:100%;position:relative;"></div>
            </td>
        </tr>
    </table>
</form>

<form action="" id="_dimRefAttrForm" onsubmit="return false;">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: auto;position: relative;"
           class="dhtmlxInfoBarLabel formTable" id="_dimRefAttrTable">
        <tr style="height: 0px; ">
            <th style="height: 0px; width: 20% "></th>
            <th style="height: 0px; width: 30% "></th>
            <th style="height: 0px; width: 20% "></th>
            <th style="height: 0px; width: 30% "></th>
        </tr>
        <tr>
            <td  style="text-align: right;padding: 5px "><span style="color: red">*</span>关联字段：</td>
            <td style="text-align: left;padding: 5px">
                <select style="width: 100%;" name="refColumn" id="_refColumn">
                </select>
            </td>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td  style="text-align: right;padding: 5px "><span style="color: red">*</span>维度归并类型：</td>
            <td style="text-align: left;padding: 5px">
                <select style="width: 100%;" name="dimType" id="_dimType">
                </select>
            </td>
            <td style="text-align: right;padding: 5px"><span style="color: red">*</span>维层级：</td>
            <td style="text-align: left;padding: 5px">
                <select style="width: 100%;" name="selectLevel" id="_selectLevel">
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="4" style="text-align: center;" id="_dimInfoWindowButton">
            </td>
        </tr>
    </table>
</form>
</body>
</html>