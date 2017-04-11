<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  String dateTime=formatter.format(new Date());
  dateTime=DateUtil.lastDay(dateTime, -1);
  request.setAttribute("dateTime",dateTime);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>宽带故障申告全流程各环节管控（日报）</title>
<%@include file="../../public/head.jsp"%>
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/portalCommon/module/serviceControl/css/style.css">
	<script type="text/javascript"   src="<%=rootPath%>/dwr/interface/StepFaultAction.js"></script>
	<script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<div class="bd">
<div class="title"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/clock.png" />宽带故障申告全流程各环节管控（日报）</div>
<div class="blank10px"></div>
<div class="search">日期：<input id='dateTime' name='dateTime' value="${dateTime}" readonly="true"  class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})"/>
<td width="8%">宽带类型:</td>
											                  <td width="6%">
											                  		<select id="indexType" name="indexType">
											                          <option value="" >==请选择==</option>
											                          <option value="0">普通</option>
											                          <option value="1" >光纤</option> 
											                       </select>
											                  </td>
&nbsp;&nbsp;<input type='button'  id="queryBtn" name="queryBtn" onClick="queryData()" style="width:60px;" value="查  询" />
</div>
<div>
  <table width="944" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="418" height="48"><table width="418" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/title2_bg.jpg" class="f16">
        <tr>
          <td width="112" height="48" align="center" valign="middle">流程</td>
          <td width="306" align="center" valign="middle">工单量</td>
        </tr>
      </table></td>
      <td width="108">&nbsp;</td>
      <td width="418"><table width="418" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/title2_bg.jpg" class="f16">
        <tr>
          <td width="112" height="48" align="center" valign="middle">流程</td>
          <td width="306" align="center" valign="middle">工单量</td>
        </tr>
      </table></td>
      </tr>
  </table>
  <table width="944" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="418" rowspan="2"><table width="418" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px #dcdcdc dashed;">
        <tr>
          <td height="60"><div class="item"> 故障受理 </div></td>
          <td align="right" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
              <tr>
                <td width="110" height="52" valign="middle"><div align="center">故障申告量：</div></td>
                <td width="176" align="left">当日: <span id="cmpl_num"></span><br />
                  上月平均: <span id="cmpl_last_num"></span></td>
              </tr>
          </table></td>
        </tr>
      </table>
        <table width="418" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px #dcdcdc dashed;">
          <tr>
            <td height="60"><div class="item"> 预处理 </div></td>
            <td align="right" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
                <tr>
                  <td width="110" height="52" valign="middle"><div align="center">预处理率：</div></td>
                  <td width="176" align="left">当日: <span id="deal_num"></span><br />
                    上月平均: <span id="deal_last_num"></span></td>
                </tr>
            </table></td>
          </tr>
        </table>
        <table width="418" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px #dcdcdc dashed;">
          <tr>
            <td height="60"><div class="item"> 派单</div></td>
            <td align="right" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
                <tr>
                  <td width="110" height="52" valign="middle"><div align="center">派单量：</div></td>
                  <td width="176" align="left">当日: <span id="dispach_num"></span><br />
                    上月平均: <span id="dispach_last_num"></span></td>
                </tr>
            </table></td>
          </tr>
        </table>
        <table width="418" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="112" height="60"><div class="item">障碍修复</div></td>
            <td align="right" valign="middle">&nbsp;</td>
          </tr>
        </table>
        <table width="418" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="112" height="60"><div class="item">结束</div></td>
            <td align="right" valign="middle">&nbsp;</td>
          </tr>
        </table></td>
      <td width="108" height="180" align="center" valign="middle"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/yc.jpg" width="68" height="42" /></td>
      <td width="418" rowspan="2" valign="top"><table width="418" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px #dcdcdc dashed;">
        <tr>
          <td height="60"><div class="item"> 超时</div></td>
          <td align="right" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
              <tr>
                <td width="110" height="52" valign="middle"><div align="center">超时量：</div></td>
                <td width="176" align="left">当日: <span id="overtime_num"></span><br />
                  上月平均: <span id="overtime_last_num"></span></td>
              </tr>
          </table></td>
        </tr>
      </table>
        <table width="418" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px #dcdcdc dashed;">
          <tr>
            <td height="60"><div class="item">挂起</div></td>
            <td align="right" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
                <tr>
                  <td width="110" height="52" valign="middle"><div align="center">挂起量：</div></td>
                  <td width="176" align="left">当日: <span id="hangup_num"></span><br />
                    上月平均: <span id="hangup_last_num"></span></td>
                </tr>
            </table></td>
          </tr>
        </table>
        <table width="418" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px #dcdcdc dashed;">
          <tr>
            <td height="60"><div class="item">回访回退单</div></td>
            <td align="right" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
                <tr>
                  <td width="110" height="52" valign="middle"><div align="center">回访回退量：</div></td>
                  <td width="176" align="left">当日: <span id="return_num"></span><br />
                    上月平均: <span id="return_last_num"></span></td>
                </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
    <tr>
      <td height="120" align="center" valign="middle">&nbsp;</td>
    </tr>
  </table>
</div>
<div></div>
</div>
<script type="text/javascript">
function openTabSub(menu_name,url)
{
    var dateTime=document.getElementById('dateTime').value;
    url +='&date='+dateTime;
	return parent.openTabSub(menu_name,url,'top');
}
</script>
</body>
</html>
<script type="text/javascript" src="step_fault_day.js"></script>
