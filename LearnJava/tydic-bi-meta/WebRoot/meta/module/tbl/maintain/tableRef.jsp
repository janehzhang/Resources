<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript">
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>';
        var tableName='<%=request.getParameter("tableName")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,TABLE_REL_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MaintainRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/resource/js/flow.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="tableRef.js"></script>
    <script type="text/javascript" src="tableRefSet.js"></script>
    <style type="text/css">
        .leftCol{
            text-align: right;
            padding: 0px;
            width: 10%;
            border: 1px #D0E5FF solid;
            background-color: #FEFEFF;
            height: 10px;
            line-height: 0px;
            font-size: 11px;
        }
        .centerCol{
            text-align: left;
            padding: 0px;
            width: 45%;
            border: 1px #D0E5FF solid;
            height: 10px;
            line-height: 0px;
            font-size: 11px;
        }
        .rightCol{
            text-align: left;
            padding: 0px;
            width: 45%;
            border: 1px #D0E5FF solid;
            background-color: #FAFAFA;
            height: 10px;
            line-height: 0px;
            font-size: 11px;
        }
        .leftDim{
            text-align: right;
            padding: 2px;
            width: 30%;
            border: 1px #D0E5FF solid;
            background-color: #E9F5FE;
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

        .leftNode{
            text-align: right;
            padding: 0px;
            width: 15%;
            border: 1px #D0E5FF solid;
            background-color: #E9F5FE;
        }
    </style>
</head>
<body style="height: 100%;width: 100%">
<%--从元数据复制查询--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_importFromTableRel">
    <form action="" id="_queryTablesRel">
        <%--查询表单行--%>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 1px; width: 15%">层次分类：</td>
            <td style="text-align: left;padding: 1px;width:35%" >
                <select style="width: 80% " id="_querytableTypeRel" name="tableTypeId">
                    <option value="">--请选择--</option>
                </select>
            </td>
            <td style="text-align: right;padding: 1px;width: 15%">业务类型：</td>
            <td style="text-align: left;padding: 1px;width: 35%">
                <input type="hidden" name="tableGroupId" id="_queryTableGroupIdRel">
                <input class="dhxlist_txt_textarea" name="tableGroup" id="_queryTableGroupRel" type="text" style="padding-right: 0px;width: 80% ">
            </td>
        </tr>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 1px ">关键字：</td>
            <td style="text-align: left;padding: 1px">
                <input class="dhxlist_txt_textarea" name="keyWord" id="_tableKeyWordRel" type="text" style="width: 80% ">
            </td>
            <td>&nbsp;</td>
            <td id="_queryTableButtonRel" style="text-align: center;">
            </td>
        </tr>
    </form>
    <!--查询出的grid-->
    <tr>
        <td colspan="4" style="position: relative;height:80%">
            <div id="_queryTableGridRel" style="height: 100%;width:100%;position:relative;"></div>
        </td>
    </tr>
</table>
<div id="detail" style="height: 100%;width: 100%"></div>
<div style="height: 100%; width:100%; " id="_relFormDiv">
    <form action="" id="_relForm" style="width:100%;height: 100%;">
        <div style="width:100%;height: 100%;" class="gridbox" id="_relTableDiv">
            <table cellpadding="0" cellspacing="0" style="width: 100%; table-layout: fixed;" class="hdr" id="_relColumnHeadTable">
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
                    <td style="text-align: center; ">关联表类名</td>
                    <td style="text-align: center; ">关联类型</td>
                    <td style="text-align: center; ">列名</td>
                    <td style="text-align: center; ">关联列名</td>
                    <td style="text-align: center; ">说明</td>
                    <td style="text-align: center; "><div id="_headOperate_"></div></td>
                </tr>
                </tbody>
            </table>
            <%--表体内容--%>
            <div style="width: 100%;height: 100%;  overflow-y: scroll;" class="objbox" id="_relContentDiv">
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
    </form>
</div>
<div id="flow_div" style="height: 100%;width: auto;"></div>
<form action="" id="_nodeDetail">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%"
           class="" id="_nodeDetailInfo">
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 15%"></th>
            <th style="height: 0px; width: 35%"></th>
            <th style="height: 0px; width: 15%"></th>
            <th style="height: 0px; width: 35%"></th>
        </tr>
        <tr>
            <td class="leftNode" >表类名称：</td>
            <td style="text-align: left;padding: 0px;width: 35%;border: 1px #D0E5FF solid;" >
                <div id="_nodeTableName"></div>
            </td>
            <td class="leftNode" >中文名称：</td>
            <td style="text-align: left;padding: 0px;width: 35%;border: 1px #D0E5FF solid;" >
                <div id="_nodeTableNameCn"></div>
            </td>
        </tr>
        <tr>
            <td class="leftNode" >业务类型：</td>
            <td style="text-align: left;padding: 0px;width: 35%;border: 1px #D0E5FF solid;" >
                <div id="_nodeTableGroupName"></div>
            </td>
            <td class="leftNode" >层次分类：</td>
            <td style="text-align: left;padding: 0px;width: 35%;border: 1px #D0E5FF solid;" >
                <div id="_nodeTableCodeName"></div>
            </td>
        </tr>
        <tr>
            <td class="leftNode" >数据源：</td>
            <td style="text-align: left;padding: 0px;width: 35%;border: 1px #D0E5FF solid;" >
                <div id="_nodeTableDataSourceName"></div>
            </td>
            <td class="leftNode" >所属用户：</td>
            <td style="text-align: left;padding: 0px;width: 35%;border: 1px #D0E5FF solid;" >
                <div id="_tableOwner"></div>
            </td>
        </tr>
        <tr>
            <td colspan="4" align="center" style="background-color: #E9F5FE;"><div id="_goToNewView"></div></td>
        </tr>
    </table>
</form>
<div id="newLinkDetail">
    <form action="" id="_newLinkDetail_{index}">
        <table cellpadding="0" cellspacing="0" style="width:100%;"
               class="" id="_newLinkDetailInfo_{index}">
            <tr style="height: auto; display: none;">
                <th style="height: 0px; width: 30%"></th>
                <th style="height: 0px; width: 35%"></th>
                <th style="height: 0px; width: 35%"></th>
            </tr>
            <tr>
                <td class="leftDim" >关系类型：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >

                    <select id="_newRelType_{index}" style="width: 30%; ">
                    </select>
                    <%--<div id="_relType_{index}"></div>--%>
                </td>
            </tr>
            <tr>
                <td class="leftDim" style="height: 40px;" >关系说明：</td>
                <td colspan="2" style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;height: 40px;" >
                    <%--<div id="_relComm_{index}"></div>--%>
                    <textarea rows="2" cols="" style="width: 90%; height: 40px;" id="_newRelComm_{index}"></textarea>
                    <input type="hidden" id="_dimRel_{index}" value="">
                </td>
            </tr>
        </table>
        <div class="gridbox" id="_newRelTableDiv_{index}" >
            <%--表体内容--%>
            <div style="width: 100%;" class="objbox" id="_newRelContentDiv_{index}">
                <%--表头内容--%>
                <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr" id="_newRelColumnHeadTable_{index}">
                    <tbody>
                    <tr style="height: 0px; ">
                        <%--定义表头列宽--%>
                        <th style="height: 0px; width: 40% "></th>
                        <th style="height: 0px; width: 40% "></th>
                        <th style="height: 0px; width: 20% "></th>
                    </tr>

                    <tr>
                        <td style="text-align: center; ">当前表列</td>
                        <td style="text-align: center; ">被关联表列</td>
                        <td style="text-align: center; "><div id="_headOperate_{index}"></div></td>
                    </tr>
                    </tbody>
                </table>
                <div style="height: 150px; overflow-y: scroll; overflow-x: hidden" id="_newRelContentTable_Div_{index}">
                <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_newRelContentTable_{index}">
                    <tbody id="_newRelContentBody_{index}">
                    <tr style="height: 0px; ">
                        <th style="height: 0px; width: 40%"></th>
                        <th style="height: 0px; width: 40%"></th>
                        <th style="height: 0px; width: 20%"></th>
                    </tr>
                    </tbody>
                </table>
                </div>
            </div>
        </div>
        <table cellpadding="0" cellspacing="0" style="width:100%;">
            <tr>
                <td colspan="2" align="center"><div id="_newConfirm_{index}" style="border: solid 1px #A4BED4;"></div></td>

            </tr>
        </table>

    </form>
</div>

<div id="newLinkDetail_Grid">
    <form action="" id="_newLinkDetail_Grid_{index}">
        <div class="gridbox" id="_newRelTableDiv_Grid_{index}" >
            <%--表体内容--%>
            <div style="width: 100%;" class="objbox" id="_newRelContentDiv_Grid_{index}">
                <%--表头内容--%>
                <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr" id="_newRelColumnHeadTable_{index}">
                    <tbody>
                    <tr style="height: 0px; ">
                        <%--定义表头列宽--%>
                        <th style="height: 0px; width: 40% "></th>
                        <th style="height: 0px; width: 40% "></th>
                        <th style="height: 0px; width: 20% "></th>
                    </tr>

                    <tr>
                        <td style="text-align: center; "><div id="_CurrentCol_{index}"></div></td>
                        <td style="text-align: center; ">被关联表列</td>
                        <td style="text-align: center; "><div id="_headOperate_Grid_{index}"></div></td>
                    </tr>
                    </tbody>
                </table>
                <div style="height: 125px; overflow-y: scroll; overflow-x: hidden; width: 100%;" id="_newRelContentTable_Div_{index}">
                <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_newRelContentTable_Grid_{index}">
                    <tbody id="_newRelContentBody_Grid_{index}">
                    <tr style="height: 0px; ">
                        <th style="height: 0px; width: 40%"></th>
                        <th style="height: 0px; width: 40%"></th>
                        <th style="height: 0px; width: 20%"></th>
                    </tr>
                    </tbody>
                </table>
                </div>
            </div>
        </div>
        <table cellpadding="0" cellspacing="0" style="width:100%;">
            <tr>
                <td colspan="2" align="center"><div id="_newConfirm_Grid_{index}"></div></td>

            </tr>
        </table>

    </form>
</div>

</body>
</html>