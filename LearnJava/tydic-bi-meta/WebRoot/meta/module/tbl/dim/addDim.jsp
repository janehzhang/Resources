<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>维度表类新增</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=DATA_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="addDim.js"></script>
    <style type="text/css">
    	.formAnnotation{
    		 color:#FFA500;
    	}
    	#_tableBaseInfo{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			margin-top: 2px;
			border-bottom: #99b2c9 solid 1px;
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
		}
    </style>
</head>

<body style="height: 100%;width: 100%; ">
<%--申请表类表单--%>
<form action="" id="_baseInfoForm" style="width:100%;height: 100%;display: none">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 98%"
           class="dhtmlxInfoBarLabel formTable" id="_tableBaseInfo">
    	<tbody>
    		<tr>
    		  <td colspan="4" height="5%">
				<table style="width: 100%">
					<tr>
						<td id="icoTable" style="border: 0px;"></td>
						<td id="tableMeg" style="border: 0px; ">新增维度表类</td>
						<td width="89%" style="text-align: right;border:none;">
							<span onclick="importFromDatabase()" style="cursor: pointer;">
								<img src="<%=rootPath %>/meta/resource/images/dataBase.png" width="18px" height="18px"></img>
								从数据表中导入</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span onclick="importFormTable()" style="cursor: pointer;">
								<img src="<%=rootPath %>/meta/resource/images/tableImport.png" width="18px" height="18px"></img>
								从原数据表类复制</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
    		  </td>
    		</tr>
    		<tr>
    			<th width="15%">维度表类名称：</th>
    			<td width="35%">
    				<input name="tableName" id="_tableName" type="text" style="width: 155px;height:15px;line-height:15px;text-transform: uppercase"></input>
	            	<span style="color: red">*</span>
	                <span class="formAnnotation">注：表类的名称，可以带宏变量</span>
    			</td>
    			
    			<th>维度中文名称：</th>
    			<td>
    				<input name="tableNameCn" id="_tableNameCn" type="text" style="width: 155px;height:15px;line-height:15px;"></input>
	            	<span style="color: red">*</span>
	                <span class="formAnnotation">注：待申请表类的中文名</span>
    			</td>
    		</tr>
    		
    		<tr>
    			<th>业务类型：</th>
    			<td>
    				<input type="hidden" name="tableGroupId" id="_tableGroupId"></input>
                	<input name="tableGroup" id="_tableGroup" type="text" style="width: 155px;height:15px;line-height:15px;"></input>
                	<span style="color: red">*</span>
               		<span class="formAnnotation">注：表类的业务类型</span>
    			</td>
    			
    			<th>维度表前缀：</th>
    			<td>
    				<input name="tableDimPrefix" id="_tableDimPrefix" type="text" style="width: 155px;height:15px;line-height:15px;"></input>
    				<span style="color: red">*</span> 
    			</td>
    		</tr>
    		
    		<tr>
    			<th>数据源：</th>
    			<td>
    				<select style="height: 20px;width: 161px;" name="dataSourceId" onchange="dataSourceChange(this)"  id="_dataSource">
                    	<%--<option value="">--请选择--</option>--%>
               		</select>
               		<span style="color: red">*</span>
                	<span class="formAnnotation">注：表类所在的数据源</span>
    			</td>
    			<th>所属用户：</th>
    			<td>
    				<select onchange="ownerChange(this)" name="tableOwner" id="_tableOwner" style="width: 161px;">
                        <option value=""></option>
		  			</select>
		  			<span style="color: red">*</span>
                	<span class="formAnnotation"></span>
    				<!--
    				<input name="tableOwner" id="_tableOwner" type="text" style="width: 40%"></input>
    				-->
    			</td>
    		</tr>
    		
    		<tr>
    			<th>表空间：</th>
    			<td>
    				<select name="tableSpace" id="_tableSpace" style="width: 161px;">
		  			</select>
		  			<span style="color: red">*</span>
                	<span class="formAnnotation">注：表类所在的表空间</span>
		  			<!--
    				<input name="tableSpace" id="_tableSpace" type="text" style="width: 40%"></input>
    				-->
    			</td>
    			<th>末级是否显示：</th>
    			<td>
    				<input name="lastLevelFlag" id="_lastLevelFlag" type="checkbox"></input>
    			</td>
    		</tr>
    		
    		<tr>
    			<th>分区SQL：</th>
    			<td colspan="3">
    				<textarea name="partitionSql" id="_partitionSql" rows="2" style="width: 73%"></textarea>
    				 <span class="formAnnotation"> 注：建表所用的分区SQL</span>
    			</td>
    		</tr>
    		
    		<tr>
    			<th>注释：</th>
    			<td colspan="3">
    				 <textarea name="tableBusComment" id="_tableBusComment" rows="2" style="width: 73%"></textarea>
    			</td>
    		</tr>
    	</tbody>
    </table>
</form>
<%--编辑列表单--%>
<div style="height: 97%; width:100%; " id="_columnFormDiv">
    <form action="" id="_columnForm" style="width:100%;height: auto;" onsubmit="return false;">
        <%--表头DIV--%>
        <div style="width: 100%;height: 100%">
            <div style="width:100%;" class="gridbox" id="_columnTableDiv">
                <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                     class="xhdr">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr" id="_columnHeadTable">
                        <tbody>
                        <tr style="height: 0px; ">
                            <%--定义表头列宽--%>
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 6% "></th>
                            <th style="height: 0px; width: 6% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 16%"></th>
                            <th style="height: 0px; width: 8%"></th>
                            <th style="height: 0px; width: 8%; "></th>
                        </tr>
                        <tr>
                            <td style="text-align: center; line-height:22px;">名称<span style="color: red;">*</span></td>
                            <td style="text-align: center; line-height:22px;">列中文名</td>
                            <td style="text-align: center; line-height:22px;">类型<span style="color: red;">*</span></td>
                            <td style="text-align: center; line-height:22px;">主键</td>
                            <td style="text-align: center; line-height:22px; ">允许空</td>
                            <td style="text-align: center; line-height:22px;">默认值</td>
                            <td style="text-align: center; line-height:22px;"> 注释</td>
                            <td style="text-align: center; line-height:22px;">维度</td>
                            <td style="text-align: center; line-height:22px;">列属性</td>
                            <td style="text-align: center; line-height:22px;">操作
                            	 <img src="../../../resource/images/delete.png" alt="清空列信息" title="清空列信息"
                           		   style="width: 16px;height: 16px;" onclick="clearAllColumnData()"/>
                           		<div style="background-image: url('../../../resource/dhtmlx/imgs/dhxlayout_dhx_skyblue/dhxlayout_btns.gif');overflow:hidden;margin:0px;
                                  background-position: -32px 0;cursor: pointer;width: 16px;height: 16px;position: absolute;top:5px;right:0px;float: right;*top:-20px;" title="缩放" onclick="collapse(this,'_clumnContentDiv',$('_clumnContentDiv'))"></div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <%--表体内容--%>
                <div style="width: 100%;overflow-y: scroll; " class="objbox" id="_clumnContentDiv">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: auto; table-layout: fixed;" class="obj" id="_clumnContentTable">
                        <tbody id="_clumnContentBody">
                        <%--定义表体列宽--%>
                        <tr style="height: 0px; ">
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 6% "></th>
                            <th style="height: 0px; width: 6% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 16%"></th>
                            <th style="height: 0px; width: 8%"></th>
                            <th style="height: 0px; width: 8%; "></th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </form>
    <form action="" id="_columnDimForm" style="width:100%;height: auto;" onsubmit="return false;">
        <div style="width: 100%;height: 100%">
            <%--表头DIV--%>
            <div style="width:100%" class="gridbox" id="_columnDimTableDiv">
                <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                     class="xhdr">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr" id="_clumnDimHeadTable">
                        <tbody>
                        <tr style="height: auto; ">
                            <%--定义表头列宽--%>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 18% "></th>
                            <th style="height: 0px; width: 44% "></th>
                            <%--<th style="height: 0px; width: 8% "></th>--%>
                            <th style="height: 0px; width: 8% "></th>
                        </tr>
                        <tr>
                            <td style="text-align: center; line-height:22px;">拥有的归并类型名称</td>
                            <td style="text-align: center; line-height:22px;">编码<span style="color: red">*</span></td>
                            <td style="text-align: center; line-height:22px;">描述</td>
                            <td style="text-align: center; line-height:22px;">层级</td>
                            <%--<td style="text-align: center; ">末级是否映射</td>--%>
                            <td style="text-align: center; line-height:22px;">操作
                           		<img src="../../../resource/images/delete.png" alt="清空归并信息" title="清空归并信息"
                           		   style="cursor: pointer;width: 16px;height: 16px;position: relative;" onclick="clearConflationData()"></img>
                                <div style="background-image: url('../../../resource/dhtmlx/imgs/dhxlayout_dhx_skyblue/dhxlayout_btns.gif');
                                                         background-position: -32px 0;cursor: pointer;width: 16px;height: 16px;position: absolute;top:5px;right:0px;float: right;*top:-20px;" title="缩放" onclick="collapse(this,'_clumnDimContentDiv',$('_clumnContentDiv'))"></div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div style="width: 100%;  overflow-y: scroll;overflow-x: hidden;" class="objbox" id="_clumnDimContentDiv">
                    <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_clumnDimContentTable">
                        <tbody id="_clumDimContentBody">
                        <%--定义表体列宽--%>
                        <tr style="height: auto; ">
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            <th style="height: 0px; width: 18% "></th>
                            <th style="height: 0px; width: 44% "></th>
                            <%--<th style="height: 0px; width: 8% "></th>--%>
                            <th style="height: 0px; width: 8% "></th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    <%--提交按钮--%>
    <div style="height: 30px; width:100%; position: relative; border-top: #99b2c9 solid 1px;" id="_submit"></div>
    </form>
</div>
<%--选择维表页面布局--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_dimInfoTable">

    <form action="" id="_queryDimForm" onsubmit="return false;">
        <%--查询表单行--%>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 2px ">查询维表：</td>
            <td style="text-align: left;padding: 2px">
                <input type="hidden" name="tableGroupId" id="_queryDimId">
                <%--隐藏表类型ID--%>
                <input type="hidden" name="tableTypeId" value="2">
                <input class="dhxlist_txt_textarea" name="tableGroup" id="_queryDimName" type="text" style="padding-right: 0px;width: 100%">
            </td>
            <td style="text-align: right;padding: 2px">名称：</td>
            <td style="text-align: left;padding: 2px">
                <input class="dhxlist_txt_textarea" name="queryMessage" id="_queryDimTableName" type="text" style="width: 100%; ">
            </td>
            <td style="text-align: center;padding-left:20px; " id="_queryDimButton"></td>
        </tr>
    </form>
    <!--维度表查询grid-->
    <tr>
        <td colspan="5" style="width:100%;height: 90%;">
            <div id="_queryDimGrid" style="width:100%;height:100%;"> </div>
        </td>
    </tr>
</table>

<%--维度选择页面维表关联属性布局--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: auto;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_dimRefAttrTable">
    <form action="" id="_dimRefAttrForm" onsubmit="return false;">
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
            <%--窗口关闭确定按钮TD--%>
            <td colspan="4" style="text-align: center;" id="_dimInfoWindowButton">
            </td>
        </tr>
    </form>
</table>

<%--从元数据复制查询--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_importFromTable">
    <form action="" id="_queryTables" onsubmit="return false;">
        <%--查询表单行--%>
        <tr style="height: 10%">
            <td style="text-align: right;padding: 1px;width: 10%">业务类型：</td>
            <td style="text-align: left;padding: 1px;width: 35%">
                <input type="hidden" name="tableGroupId" id="_queryTableGroupId">
                <input class="dhxlist_txt_textarea" name="tableGroup" id="_queryTableGroup" type="text" style="padding-right: 0px;width: 80% ">
            </td>
            <td  style="text-align: right;padding: 1px;width: 10%">关键字：</td>
            <td style="text-align: left;padding: 1px;width: 10%">
                <input class="dhxlist_txt_textarea" name="keyWord" id="_tableKeyWord" type="text" style="width: 77% ">
            </td>
            <td id="_queryTableButton" style="text-align: center;width: 10%">
            </td>
        </tr>
    </form>
    <!--查询出的grid-->
    <tr>
        <td colspan="5" style="position: relative;height:90%">
            <div id="_queryTableGrid" style="height: 100%;width:auto;position:relative;"></div>
        </td>
    </tr>
</table>
<%--从数据中导入表信息查询--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_importFromDb">
    <form action="" id="_queryTablesFromDb" onsubmit="return false;">
        <%--查询表单行--%>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 1px;width: 15% ">数据源：</td>
            <td style="text-align: left;padding: 1px;width: 35%">
                <select style="width: 80%;" name="dataSource" id="_tableDataSource">
                </select>
            </td>
            <td style="text-align: right;padding: 1px;width: 15%">用户：</td>
            <td style="text-align: left;padding: 1px;width: 35%">
                <select style="width:80%;" name="owner" id="_owner">
                </select>
            </td>
        </tr>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 1px ">关键字：</td>
            <td style="text-align: left;padding: 1px">
                <input class="dhxlist_txt_textarea" name="keyWord" id="_tableKeyWordFromDb" type="text" style="width: 77%">
            </td>
            <td>&nbsp;</td>
            <td id="_queryTableButtonFromDb" style="text-align: center;">
            </td>
        </tr>
    </form>
    <!--查询出的grid-->
    <tr>
        <td colspan="4"  style="position: relative;height:80%">
            <div id="_queryTableGridFromDb" style="height: 100%;width:auto;position:relative;"></div>
        </td>
    </tr>
</table>

<div id="levelContext">
    <!-- 下方表格点击“层级”后的弹出框 -->
    <form action="" id="_levelForm_{index}" style="width:100%;height: 100%" onsubmit="return false;">
        <div style="height: 100%; width:100%; ">
            <div style="height: 20px">归并类型：<span id="_dimType_{index}" style="font-weight: bold;"></span></div>
            <div style="width:100%" class="gridbox" id="_LevelTable_{index}">
                <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                     class="xhdr">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr">
                        <tbody>
                        <tr style="height: auto; ">
                            <%--定义表头列宽--%>
                            <th style="height: 0px; width: 25% "></th>
                            <th style="height: 0px; width: 50% "></th>
                            <th style="height: 0px; width: 25% "></th>
                        </tr>
                        <tr>
                            <td style="text-align: center; ">层级编码</td>
                            <td style="text-align: center; ">层级名称</td>
                            <td style="text-align: center; ">操作</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div style="width: 100%; height: 270px; overflow: auto;" class="objbox" >
                    <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_levelContentTable_{index}">
                        <tbody id="_LevelContent_{index}">
                        <%--定义表头列宽--%>
                        <th style="height: 0px; width: 25% "></th>
                        <th style="height: 0px; width: 50% "></th>
                        <th style="height: 0px; width: 25% "></th>
                        </tbody>
                    </table>
                </div>
            </div>
            <div id="_levelWindowButton_{index}" style="height: 30px"></div>
        </div>
    </form>
</div>
</body>
</html>