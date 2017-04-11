<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-5-7
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/meta/public/component/components.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/interface/CmpInitAction.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/interface/ComponentAction.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/meta/public/component/componentCommon.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/meta/public/component/tablesSelectGrid.js"></script>
<style type="text/css">
    span.qcond{
        width:185px;
        display:block;
        float:left;
        margin:0 0 2px 0;
    }
    span.qcond span{
        width:60px;
        float:left;
        text-align: right;
        line-height:20px;
        padding:2px 0;
    }
    span.qcond input{
        width:120px;
        height:16px;
        float:left;
        text-align: left;
    }
    span.qcond select{
        width:124px;
        height:22px;
        float:left;
        text-align: left;
    }
    .qbtn{
        cursor: pointer;
        display: inline-block;
        font-size: 12px;
        height: 22px;
        line-height: 18px;
        overflow: hidden;
        top: 5px;
        right: 0;
        padding-bottom: 1px;
        margin:5px 5px 5px 5px;
        border-radius: 2px;
        color: black;
        border: 1px solid #198EB4;
        background-color: #D6E8FF;
        background-position: 0 0 !important;
    }
    span.chooseInp{
        width:13px;
        height:13px;
        padding:1px;
        margin:0;
        cursor:pointer;
        border:1px solid #0076A3;
        background:url(../../resource/images/arrow_down.gif) 2px 1px no-repeat;
    }
    span.inputspan{
        border:1px #A4BED4 solid ;
        display:block;
    }
</style>
<%--弹出选择框布局模板--%>
<div id="_SelectGrid" class="selectPop" style="display:none;">
    <div style="overflow:hidden;position:relative;height:100%;width:100%;">
        <div id="gh_{id}" style="width:100%;display:none;min-height:30px;position:relative;padding:5px;border-bottom:1px solid #A4BED4;">
            <div id="gqc_{id}" style="float:left;height:100%;">
                <%--<span><label>条件1</label><select id="sel1" style="width:120px;"><option>请选择</option></select></span>--%>
                <%--<span><label>条件2</label><input type="text" id="txt1" style="width:120px;"></span>--%>
            </div>
            <span style="position:absolute;bottom:10px;right:40px;"><input id="gquy_{id}" title="点击查询" class="qbtn" type="button" value="查询"></span>
        </div>
        <div id="gc_{id}" style="min-height:150px;overflow:hidden;"></div>
        <div id="gf_{id}" style="width:100%;display:none;border-top:1px solid #A4BED4;"></div>
        <div id="gb_{id}" style="text-align:center;padding:10px 0 7px 0;border-top:1px solid #A4BED4;">
            <input id="gsmt_{id}" class="qbtn" title="确定选择" type="button" value="确定">
            <input id="gcel_{id}" class="qbtn" title="取消选择" type="button" value="取消">
        </div>
    </div>
</div>
<%--弹出选择树布局模板--%>
<div id="_SelectTree" class="selectPop" style="display:none;">
    <div style="overflow-y:auto;position:relative;">
        <div id="th_{id}" style="width:100%;display:none;position:relative;">
            <div style="float:left;height:100%;width:80%;">
                <%--<span><label>条件1</label><select id="sel1" style="width:120px;"><option>请选择</option></select></span>--%>
                <%--<span><label>条件2</label><input type="text" id="txt1" style="width:120px;"></span>--%>
            </div>
            <div style="float:right;height:100%;width:20%;">
                <span style="position:absolute;bottom:10px;right:30px;"><input id="tquy_{id}" type="button" value="查询"></span>
            </div>
        </div>
        <div id="tc_{id}" style="min-height:150px;"></div>
        <div id="tf_{id}" style="width:100%;display:none"></div>
        <div id="tb_{id}" style="text-align:center">
            <span><input id="tsmt_{id}" type="button" value="确定"></span>
            <span><input id="tcel_{id}" type="button" value="取消"></span>
        </div>
    </div>
</div>