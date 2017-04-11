<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript">
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_REL_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MaintainRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/resource/js/flow.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="viewRef.js"></script>
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
            padding: 5px;
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
<div id="detail" style="height: 100%;width: 100%"></div>
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
<%--<div id="linkDetail">--%>
<%--<form action="" id="_linkDetail_{index}">--%>
    <%--<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%"--%>
           <%--class="" id="_linkDetailInfo_{index}">--%>
        <%--<tr style="height: auto; display: none;">--%>
            <%--<th style="height: 0px; width: 30%"></th>--%>
            <%--<th style="height: 0px; width: 70%"></th>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td class="leftDim" >表类1：</td>--%>
            <%--<td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >--%>
                <%--<div id="_tbName1_{index}"></div>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td class="leftDim" >列：</td>--%>
            <%--<td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >--%>
                <%--<div id="_colName1_{index}"></div>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td class="leftDim" >表类2：</td>--%>
            <%--<td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >--%>
                <%--<div id="_tbName2_{index}"></div>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td class="leftDim" >列：</td>--%>
            <%--<td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >--%>
                <%--<div id="_colName2_{index}"></div>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td class="leftDim" >关系类型：</td>--%>
            <%--<td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >--%>
                <%--<div id="_relType_{index}"></div>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td class="leftDim" style="height: 60px;" >关系说明：</td>--%>
            <%--<td style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;height: 60px;" >--%>
                <%--<div id="_relComm_{index}"></div>--%>
            <%--</td>--%>
        <%--</tr>--%>

    <%--</table>--%>
<%--</form>--%>
<%--</div>--%>
<div id="flow_div" style="height: 100%;width: auto;">
</div>
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
                <td colspan="2" style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;" >

                    <select id="_newRelType_{index}" style="width: 40%; ">
                        <%--<option value="">请选择</option>--%>
                        <%--<option value="1">一对一</option>--%>
                        <%--<option value="2">一对多</option>--%>
                        <%--<option value="3">多对一</option>--%>
                    </select>
                    <%--<div id="_relType_{index}"></div>--%>
                </td>
            </tr>
            <tr>
                <td class="leftDim" style="height: 60px;" >关系说明：</td>
                <td colspan="2" style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;height: 60px;" >
                    <%--<div id="_relComm_{index}"></div>--%>
                    <textarea rows="2" cols="" style="width: 90%; height: 90%;" id="_newRelComm_{index}"></textarea>
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
                        <th style="height: 0px; width: 50%; "></th>
                        <th style="height: 0px; width: 50%; "></th>
                    </tr>

                    <tr>
                        <td style="text-align: center; width: 50%; ">当前表列</td>
                        <td style="text-align: center; width: 50%;">被关联表列</td>
                    </tr>
                    </tbody>
                </table>
                <div style="height: 150px; overflow-y: scroll; overflow-x: hidden;width: 100%;" id="_newRelContentTable_Div_{index}">
                <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_newRelContentTable_{index}">
                    <tbody id="_newRelContentBody_{index}">
                    <tr style="height: 0px; ">
                        <th style="height: 0px; width: 50%"></th>
                        <th style="height: 0px; width: 50%"></th>
                    </tr>
                    </tbody>
                </table>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>