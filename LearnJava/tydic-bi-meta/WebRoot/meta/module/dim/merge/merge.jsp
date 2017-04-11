<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-08--%>
<%--描述:编码归并--%>
<%@include file="../../../public/head.jsp" %>
<script type="text/javascript">
    var tableId='<%=request.getParameter("tableId")%>';
    var tableVersion='<%=request.getParameter("tableVersion")%>'
    var tableNameCn='<%=URLDecoder.decode(request.getParameter("tableNameCn"),"UTF-8")%>';
    var tableName='<%=request.getParameter("tableName")%>';

</script>
<head>
    <title>维护编码数据</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimMergeAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DBImportAction.js"></script>
    <script type="text/javascript" src="merge.js"></script>
    <script type="text/javascript" src="merge_dbImport.js"></script>
    <style type="text/css">
        .leftNode{
            text-align: right;
            padding: 0px;
            width: 10%;
            border: 1px #D0E5FF solid;
            background-color: #E3EFFF;
        }
        .rightCol{
            text-align: left;
            padding: 0px;
            width: 15%;
            border: 1px #D0E5FF solid;
        }
    </style>
</head>
<body>

<!--维护编码页面总体布局-->
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;table-layout: fixed;"   id="_layout">
    <tbody>
    <%--布局为两列--%>
    <tr style="height: 0px;display: none">
        <th style="height: 0px; width: 65%; "></th>
        <th style="height: 0px; width: 35% "></th>
    </tr>
    <%--工具条列--%>
    <tr style="height: 30px">
        <td style="height: 30px;">
            <div style="height: 100%;width: 98%;position:relative;padding: 0px" id="_toolbar"></div>
        </td>
        <td style="height: 30px;">
            <div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_codeToolbar"></div>
        </td>
    </tr>
    <%--显示区域grid--%>
    <tr >
        <%--维度编码信息显示区域--%>
        <td rowspan="3" id="_middleArea" style="vertical-align: top;">
            <div style="height: 100%;width: 100%;" id="_dimDataGrid"></div>
        </td>
        <%--归并类型编码--%>
        <td>
            <div style="height: 100%;width: 100%;" id="_dimTypeGrid"></div>
        </td>
<%--        <td>
            <div id="_debug" style="width:100%;height: 200px;overflow: auto;">

            </div>
        </td>--%>
    </tr>
    <tr style="height: 40px">
        <td style="height: 40px">
            <table style="table-layout: fixed;width: 100%">
                <tbody>
                <tr>
                    <td  align="right" style="width:4%"><div style="background-color: #64B201;height: 12px;width: 12px;"></div></td>
                    <td  align="left" style="width:12%">新增编码</td>
                    <td  align="right" style="width:4%"><div style="background-color: #63B8FF;height: 12px;width: 12px;"></div></td>
                    <td align="left"  style="width:12%" >归并编码</td>
                    <td  align="right" style="width:4%"><div style="background-color: #EEC900;height: 12px;width: 12px;"></div></td>
                    <td  align="left" style="width:12%">层级变更</td>
                    <td  align="right" style="width:4%"><div style="background-color: #7A378B;height: 12px;width: 12px;"></div></td>
                    <td align="left" style="width:12%">修改编码</td>
                    <td  align="right" style="width:4%"><div style="background-color: red;height: 12px;width: 12px;"></div></td>
                    <td align="left" style="width:12%">停用编码</td>
                    <td  align="right" style="width:4%"><div style="background-color: #F29FB5;height: 12px;width: 12px;"></div></td>
                    <td align="left" style="width:12%">启用编码</td>
                </tr>
                </tbody></table>
        </td>
    </tr>
    <%--按钮列--%>
    <tr style="height: 40px">
        <td style="height: 40px">
            <div style="height: 100%;width: 100%;text-align: center;vertical-align: middle;" id="_button"></div>
        </td>
    </tr>
    </tbody>
</table>
<div id="_template" style="display: none;">
    <%--归并类型编码模板HTML--%>
    <form action="" id="_columnDimForm_{template}" style="width:100%;height: auto;">
        <div style="width: 100%;height: 100%;">
            <%--表头DIV--%>
            <div style="width:100%;overflow-y: auto;" class="gridbox" id="_columnDimTableDiv_{template}">
                <div style="width: 100%;"
                     class="xhdr">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%;" class="hdr" id="_clumnDimHeadTable_{template}">
                        <tbody>
                        <tr style="height: 0px; ">
                            <%--定义表头列宽--%>
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 20%;"></th>
                            <th style="height: 0px; width: 28% "></th>
                            <th style="height: 0px; width: 28% "></th>
                            <th style="height: 0px; width: 12% "></th>
                        </tr>
                        <tr>
                            <td style="text-align: center; ">归并编号</td>
                            <td style="text-align: center; ">归并类型</td>
                            <td style="text-align: center; ">编码描述</td>
                            <td style="text-align: center; ">归并编码</td>
                            <td style="text-align: center; ">操作 </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div style="width: 100%;" class="objbox" id="_clumnDimContentDiv_{template}">
                    <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_clumnDimContentTable_{template}">
                        <tbody id="_clumDimContentBody_{template}">
                        <%--定义表体列宽--%>
                        <tr style="height: auto; ">
                            <th style="height: 0px; width: 12% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 28% "></th>
                            <th style="height: 0px; width: 28% "></th>
                            <th style="height: 0px; width: 12% "></th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </form>
</div>

<div id="_addCodeTemplate" style="display: none">
    <form action="" id="_addCodeForm_{template}" style="width:100%;height: 100%">
        <table cellpadding="0" cellspacing="0" style="width:100%;height: auto;table-layout: fixed;" class="" id="_addCodeTable_{template}">
            <tbody>
            <tr style="height: 0px; ">
                <th style="height: 0px; width: 20%"></th>
                <th style="height: 0px; width: 60%"></th>
                <th style="height: 0px; width: 20%"></th>
            </tr>
            <tr>
                <td class="leftCol" style="font-size:12px;">名称：</td>
                <td>
                    <input class="dhxlist_txt_textarea" name="itemName" id="_itemName_{template}" type="text" style="width: 98%;height: 100%">
                </td>
                <td><div style="color: red">*</div></td>
            </tr>
            <tr>
                <td class="leftCol" style="font-size:12px;">编码：</td>
                <td>
                    <input class="dhxlist_txt_textarea" name="itemCode" id="_itemCode_{template}" type="text" style="width: 98%;height: 100%">
                </td>
                <td><div style="color: red">*</div></td>
            </tr>
            <tr>
                <td class="leftCol" style="font-size:12px;">业务描述：</td>
                <td >
                    <textarea class="dhxlist_txt_textarea" name="itemDesc" id="_itemDesc_{template}" style="width:98%;height: 100%"
                              rows="3"></textarea>
                </td>
                <td>&nbsp;</td>
            </tr>
            </tbody>
        </table>
    </form>

</div>

<%--从数据中导入表信息查询--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 99%;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_importFromDb">
    <form action="" id="_queryTablesFromDb" onsubmit="return false;">
        <%--查询表单行--%>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 1px;width: 15% ">数据源：</td>
            <td style="text-align: left;padding: 1px;width: 35%">
                <select style="width: 80%;" name="dataSource" id="_tableDataSource">
                </select>
            </td>
            <td style="text-align: right;padding: 1px;width: 15%">所属用户：</td>
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
        <td colspan="4"  style="position: relative;height:70%">
            <div id="_queryTableGridFromDb" style="height: 100%;width:auto;position:relative;"></div>
        </td>
    </tr>
</table>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;" id="_nodeDetailInfo">
    <tr style="height: auto; display: none;">
        <th style="height: 0px; width: 10%"></th>
        <th style="height: 0px; width: 15%"></th>
        <th style="height: 0px; width: 10%"></th>
        <th style="height: 0px; width: 15%"></th>
        <th style="height: 0px; width: 10%"></th>
        <th style="height: 0px; width: 15%"></th>
    </tr>
    <tr style="height: 20%;width: 100%;">
        <td class="leftNode" >表类型：</td>
        <td class="rightCol" >
            <select id="tableType" style="width: 90%;" onchange="tableTypeChange(this)">
                <option value="1">横表</option>
                <option value="2">纵表</option>
            </select>
        </td>
        <td class="leftNode" >数据：</td>
        <td class="rightCol" >
            <select id="dataType" style="width: 90%;">
                <option value="1">保留当前数据</option>
                <option value="2">清空当前数据</option>
            </select>
        </td>
        <td class="leftNode" >编码：</td>
        <td class="rightCol" >
            <select id="codeType" title="是否追加CODE字段：比如第一层CODE 为01，第二层CODE为02，那么追加最后第二层CODE变为0102，依层递推。" style="width: 90%;">
                <option value="1">不追加</option>
                <option value="2">追加</option>
            </select>
        </td>
        <td class="leftNode" style="width: 15%;">动态字段：</td>
        <td class="rightCol" style="width: 10%;">
            <a style="padding-left: 5px;" href="#" onclick="dymaicColSet()">设置</a>
        </td>
    </tr>
    <tr  style="height: 80%;width: 100%;">
        <td colspan="8" style="height: 100%;width: 100%;">
            <div id='horTable' style="height: 100%; width:100%; ">
                <div style="width:100%" class="gridbox" id="_typeTable">
                    <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                         class="xhdr">
                        <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr">
                            <tbody>
                            <tr style="height: auto; ">
                                <%--定义表头列宽--%>
                                <th style="height: 0px; width: 15% "></th>
                                <th style="height: 0px; width: 25% "></th>
                                <th style="height: 0px; width: 25% "></th>
                                <th style="height: 0px; width: 25% "></th>
                                <th style="height: 0px; width: 10% "></th>
                            </tr>
                            <tr>
                                <td style="text-align: center; ">维层级</td>
                                <td style="text-align: center; ">编码字段</td>
                                <td style="text-align: center; ">名称字段</td>
                                <td style="text-align: center; ">描述字段</td>
                                <td style="text-align: center; "><img src="../../../resource/images/edit_add.png" title="增加" onclick="addTypeRow()" style="width:16px;height: 16px;cursor: pointer"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div style="width: 100%; height: 90px; overflow: auto;" class="objbox" >
                        <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_typeContentTable">

                            <tbody id="_typeContent">
                            <tr>
                            <th style="height: 0px; width: 15% "></th>
                            <th style="height: 0px; width: 25% "></th>
                            <th style="height: 0px; width: 25% "></th>
                            <th style="height: 0px; width: 25% "></th>
                            <th style="height: 0px; width: 10% "></th>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="_typeWindowButton" style="height: 30px"></div>
            </div>
            <div id='verTable' style="height: 100%; width:100%; display: none;">
                <div style="width:100%" class="gridbox" id="_verTable">
                    <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                         class="xhdr">
                        <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr">
                            <tbody>
                            <tr style="height: auto; ">
                                <%--定义表头列宽--%>
                                <th style="height: 0px; width: 20% "></th>
                                <th style="height: 0px; width: 20% "></th>
                                <th style="height: 0px; width: 20% "></th>
                                <th style="height: 0px; width: 20% "></th>
                                <th style="height: 0px; width: 20% "></th>
                            </tr>
                            <tr>
                                <td style="text-align: center; ">编码字段</td>
                                <td style="text-align: center; ">父编码</td>
                                <td style="text-align: center; ">名称字段</td>
                                <td style="text-align: center; ">描述字段</td>
                                <td style="text-align: center; ">根节点值</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div style="width: 100%; height: 90px; overflow: auto;" class="objbox" >
                        <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_verContentTable">

                            <tbody id="_verContent">
                            <%--定义表头列宽--%>
                            <tr>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            <th style="height: 0px; width: 20% "></th>
                            </tr>
                            <tr>
                                <td style="text-align: center; "><input type="text" name='verCodeCol' id="verCodeCol" readonly="readonly" style="width: 85%;"></td>
                                <td style="text-align: center; "><input type="text" name='verParCodeCol' id="verParCodeCol" readonly="readonly" style="width: 85%;"></td>
                                <td style="text-align: center; "><input type="text" name='verNameCol' id="verNameCol" readonly="readonly" style="width: 85%;"></td>
                                <td style="text-align: center; "><input type="text" name='verDescCol' id="verDescCol" style="width: 85%;"></td>
                                <td style="text-align: center; "><input type="text" name='verRootVal' id="verRootVal" style="width: 85%;"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div id="_verWindowButton" style="height: 30px"></div>
                </div>
            </div>
        </td>
    </tr>
</table>

<div id='dymaicColTable' style="height: 100%; width:100%; ">
    <div style="width:100%" class="gridbox" id="_dymaicColTable">
        <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
             class="xhdr">
            <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr">
                <tbody>
                <tr style="height: auto; ">
                    <%--定义表头列宽--%>
                    <th style="height: 0px; width: 50% "></th>
                    <th style="height: 0px; width: 50% "></th>
                </tr>
                <tr>
                    <td style="text-align: center; ">动态字段</td>
                    <td style="text-align: center; ">源表字段</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div style="width: 100%; height: 190px; overflow: auto;" class="objbox" >
            <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_dymaicColContentTable">
                <tbody id="_dymaicColContent">
                <tr>
                    <th style="height: 0px; width: 50% "></th>
                    <th style="height: 0px; width: 50% "></th>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div id="_dymaicColWindowButton" style="height: 30px"></div>
</div>
</body>
</html>