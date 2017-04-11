<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-21--%>
<%--描述:查看维度表表类信息--%>
<%@include file="../../../public/head.jsp" %>
<head>
    <title>查看维度表类</title>
    <script type="text/javascript">
        var tableId=<%=request.getParameter("tableId")%>;
        var tableVersion=<%=request.getParameter("tableVersion")%>;
    </script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimTypeAction.js"></script>
    <script type="text/javascript" src="viewDim.js"></script>
    <style type="text/css">
        .leftCol{
            text-align: right;
            padding: 0px;
            width: 15%;
            border-left-color:#99b2c9;
            border-left-style:solid;
            border-left-width:1px;
            background-color: #E2EFFF; 
            border-top-color:#99b2c9;
            border-top-style:solid;
            border-top-width:1px;
            background-color: #E2EFFF;
            height: 20px;
            line-height: 15px;
            /*font-size: 13px;*/
        }
        .rowsleftCol{
            text-align: right;
            padding: 0px;
            width: 15%;           
            border-left-color:#99b2c9;
            border-left-style:solid;
            border-left-width:1px;
            background-color: #E2EFFF; 
            border-top-color:#99b2c9;
            border-top-style:solid;
            border-top-width:1px;
            background-color: #E2EFFF;
            height: 40px;
            line-height: 15px;
            /*font-size: 13px;*/
        }
        .centerCol{
            text-align: left;
            padding: 0px;
            width: 30%;
            border-left-color:#99b2c9;
            border-left-style:solid;
            border-left-width:1px;       
            border-top-color:#99b2c9;
            border-top-style:solid;
            border-top-width:1px;
            height: 20px;
            line-height: 15px;
            /*font-size: 13px;*/
        }
        .rightCol{
            text-align: left;
            padding: 0px;
            width: 40%;
            border-left-color:#99b2c9;
            border-left-style:solid;
            border-left-width:1px;
            border-top-color:#99b2c9;
            border-top-style:solid;
            border-top-width:1px;
            /*background-color: #FAFAFA;*/
            height: 20px;
            line-height: 15px;
            /*font-size: 13px;*/
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
        #_linkDimInfo{
        	font-size:12px;
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
<body style="overflow-y: auto;overflow-x: hidden;">
<form action="" id="_baseInfoForm" style="width:100%;height: 100%">
	<table style="height:10%; border:0px;">
					<tbody><tr>
						<td id="icoTable"></td>
						<td id="tableMeg">表类基本信息</td>
					</tr>
				</tbody></table>
    <table cellpadding="0" cellspacing="0" style="width:100%;"
           class="" id="_tableBaseInfo">
        <tbody>
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 10%"></th>
            <th style="height: 0px; width: 40%"></th>
            <th style="height: 0px; width: 10%"></th>
            <th style="height: 0px; width: 40%"></th>
        </tr>


        <tr>
            <td class="leftCol" style="font-size:12px;">表类数据源：</td>
            <td class="centerCol" >
                <div id="_tableDataSource" style="height:20px;"></div>
            </td>
            <td class="leftCol" style="font-size:12px;">申请人：</td>
            <td class="rightCol" >
                <div id="_applyUser" style="height:20px;"></div>
            </td>
        </tr>
        <tr>
            <td class="leftCol" style="font-size:12px;">表类名称：</td>
            <td class="centerCol" >
                <div id="_tableName" style="height:20px;"></div>
            </td>
            <td class="leftCol" style="font-size:12px;">申请时间：</td>
            <td class="rightCol" >
                <div id="_applyTime" style="height:20px;"></div>
            </td>

        </tr>
        <tr>
            <td class="leftCol" style="font-size:12px;">维度层次：</td>
            <td class="centerCol" >
                <div id="_dimMaxLevel" style="height:20px;"></div>
            </td>
            <td class="leftCol" style="font-size:12px;">审核人：</td>
            <td class="rightCol" >
                <div id="_auditUser" style="height:20px;"></div>
            </td>

        </tr>
        <tr>
            <td class="leftCol" style="font-size:12px;">业务类型：</td>
            <td class="centerCol" >
                <div id="_tableGroupName" style="height:20px;"></div>
            </td>
            <td class="leftCol" style="font-size:12px;">表类状态：</td>
            <td class="rightCol" >
                <div id="_tableState" style="height:20px;"></div>
            </td>

        </tr>

        <tr>
            <td class="leftCol" style="font-size:12px;">维度前缀：</td>
            <td class="centerCol" >
                <div id="_dimTablePrefix" style="height:20px;"></div>
            </td>
            <td class="leftCol" style="font-size:12px;">是否末级显示：</td>
            <td class="rightCol" >
                <div id="_lastLevelFlag" style="height:20px;"></div>
            </td>
        </tr>
        <tr>
            <td class="rowsleftCol" >表类备注：</td>
            <td class="centerCol" colspan="3">
                <div id="_tableBusComment"></div>
            </td>
        </tr>
        <tr>
            <td class="rowsleftCol" style="font-size:12px; border-bottom-style:solid;border-bottom-color:#99b2c9;border-bottom-width:1px;">审核意见：</td>
            <td class="centerCol"  colspan="3" style="font-size:12px; border-bottom-style:solid;border-bottom-color:#99b2c9;border-bottom-width:1px;">
                <div id="_auditContext"  style="height:20px;"></div>
            </td>
        </tr>


        </tbody>
    </table>
</form>
<div id="_linkDimInfoForm"  style="height: 180px; width: auto; overflow: auto;display:none; ">
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

</body>
</html>
