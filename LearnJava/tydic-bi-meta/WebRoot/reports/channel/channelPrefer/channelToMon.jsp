<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.Date,tydic.reports.complain.monitorDay.CmplIndexDAO" %>
<%
  CmplIndexDAO dao = new CmplIndexDAO();
  String maxMon=dao.getMaxMonth("CS_BROADBAND_MAINT_MON");
  String dateTime =(String)request.getParameter("dateTime")==null?maxMon:(String)request.getParameter("dateTime");
  List<Map<String, Object>> monList = dao.getMonList("CS_BROADBAND_MAINT_MON");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <title>渠道迁转（月报）</title>
     <link href="css/style.css" rel="stylesheet" type="text/css" />
	  <%@include file="../../../portalCommon/public/head.jsp"%>
	 <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/StepInstallAction.js"></script>
	 <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<div class="bd">
  <div class="title"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/clock.png" /><a href="javascript:openTreeTab('1_mon','渠道迁转（月报）','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=128')" style=" font-size:20px;font-weight:bolder;text-decoration:none;color:black">渠道迁转（月报）</a></div>
  <div class="search"> &nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"  name="dateTime" style="width:90px;">
           <%
           	for (Map<String, Object> key : monList) {
           	if(dateTime.equals(key.get("MONTH_ID"))){
           %>
           <option value="<%=key.get("MONTH_ID")%>" selected><%=key.get("MONTH_ID").toString()%></option>
           <%
              }
           else{
            %>  
           <option value="<%=key.get("MONTH_ID")%>"><%=key.get("MONTH_ID").toString()%></option>
            <%
            }
            }
            %>    
          </select>&nbsp;&nbsp;&nbsp;	
          <input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" />
  </div>
<div class="menu">
    <span style="width:110px;text-align:center;line-height:50px;"><a style="text-align:center;line-height:40px;" href="javascript:openTreeTab('2_mon','装移_受理月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=109')">迁转渠道</a></span>
    <span style="width:570px;text-align:center;line-height:40px;">用户量</span>
    <span style="width:140px;text-align:center;line-height:40px;">服务量</span>
  
 </div>
<div align="center"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/num.jpg" /></div>
<div class="line"></div>


<div>
<table width="944" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td rowspan="2" width="112" align="center" valign="middle">
      <div class="item">
        <a href="javascript:openTreeTab('3_mon','装移_收费环节月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=110')">网厅</a>
      </div>
    </td>
    <td width="566" valign="middle">
			  <div class="tt">
			    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0"   style="margin:0 0 0 3px;">
			      <tr>
			        <td  id="widthNum_0"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
			        
			        </td>
			        <td width="100%">&nbsp;</td>
			      </tr>
			    </table>
			  </div>
    </td>
    <td rowspan="2" width="140">当月: <span   id="num_0"></span><br/>
                                                    上月: <span   id="avg_0"></span></td>
   </tr>
   <tr>
        <td width="566" valign="middle">
           <div class="tt">
		       <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
		        <tr>
		          <td id="widthAvg_0"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
		          
		          </td>
		          <td width="100%">&nbsp;</td>
		        </tr>
		      </table>        
            </div>
         </td>
   </tr>
 </table>
</div>


<div class="line"></div>
<div>
  <table width="944" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td  rowspan="2" width="112" align="center" valign="middle"><div class="item"> <a href="javascript:openTreeTab('4_mon','装移_资源配置月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=111')">WAP厅</a> </div></td>
      <td width="566" valign="middle">
      
			  <div class="tt">
			    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
			      <tr>
			        <td  id="widthNum_1"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
			        
			        </td>
			        <td width="100%">&nbsp;</td>
			      </tr>
			    </table>
			  </div>
      
      </td>
      <td rowspan="2" width="140">当月: <span id="num_1"></span><br />
                                                         上月: <span  id="avg_1"></span></td>
    </tr>
    <tr>
           <td width="566" valign="middle">
			  <div class="tt">
			  	   <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
			        <tr>
			          <td id="widthAvg_1"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
			          
			          </td>
			          <td width="100%">&nbsp;</td>
			        </tr>
			      </table>
              </div>
           </td>
    </tr>
  </table>
</div>
<div class="line"></div>
<div>
  <table width="944" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td rowspan="2"  width="112" align="center" valign="middle"><div class="item"> <a href="javascript:openTreeTab('5_mon','装移_数据施工环节月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=112')">客户端</a> </div></td>
      <td width="566" valign="middle">
      
  <div class="tt">
    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
      <tr>
        <td  id="widthNum_2"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
        
        </td>
        <td width="100%">&nbsp;</td>
      </tr>
    </table>
  </div>
      
      </td>
      <td rowspan="2"  width="140">当月: <span  id="num_2"></span><br />
                                                          上月: <span  id="avg_2"></span></td>
    </tr>
    <tr>
      <td width="566" valign="middle">
			  <div class="tt">
			      <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
				        <tr>
				          <td id="widthAvg_2"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
				          
				          </td>
				          <td width="100%">&nbsp;</td>
				        </tr>
				   </table>  
			  </div>
	   </td>
    </tr>
  </table>
</div>
<div class="line"></div>
<div>
  <table width="944" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td rowspan="2"  width="112" align="center" valign="middle"><div class="item"> <a href="javascript:openTreeTab('5_mon','装移_数据施工环节月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=112')">自助终端</a> </div></td>
      <td width="566" valign="middle">
      
  <div class="tt">
    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
      <tr>
        <td  id="widthNum_2"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
        
        </td>
        <td width="100%">&nbsp;</td>
      </tr>
    </table>
  </div>
      
      </td>
      <td rowspan="2"  width="140">当月: <span  id="num_2"></span><br />
                                                          上月: <span  id="avg_2"></span></td>
    </tr>
    <tr>
      <td width="566" valign="middle">
			  <div class="tt">
			      <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
				        <tr>
				          <td id="widthAvg_2"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
				          
				          </td>
				          <td width="100%">&nbsp;</td>
				        </tr>
				   </table>  
			  </div>
	   </td>
    </tr>
  </table>
</div>
<div class="line"></div>
<div>
  <table width="944" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td rowspan="2"  width="112" align="center" valign="middle"><div class="item"> <a href="javascript:openTreeTab('5_mon','装移_数据施工环节月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=112')">微信</a> </div></td>
      <td width="566" valign="middle">
      
  <div class="tt">
    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
      <tr>
        <td  id="widthNum_2"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
        
        </td>
        <td width="100%">&nbsp;</td>
      </tr>
    </table>
  </div>
      
      </td>
      <td rowspan="2"  width="140">当月: <span  id="num_2"></span><br />
                                                          上月: <span  id="avg_2"></span></td>
    </tr>
    <tr>
      <td width="566" valign="middle">
			  <div class="tt">
			      <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
				        <tr>
				          <td id="widthAvg_2"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
				          
				          </td>
				          <td width="100%">&nbsp;</td>
				        </tr>
				   </table>  
			  </div>
	   </td>
    </tr>
  </table>
</div>
<div class="line"></div>
<div class="blank5px"></div>
<table width="944" border="0" cellpadding="0" cellspacing="0" bgcolor="#dde5ec">
  <tr>
    <td width="112" height="50" align="right" valign="middle" class="f18">合计：&nbsp; </td>
    <td width="150">当月: <span  id="num_4"></span><br/>
                                                    上月: <span  id="avg_4"></span></td>
    <td width="416" valign="middle" class="tip">图例说明：(单位：人)&nbsp;&nbsp;<img src="<%=rootPath%>/portalCommon/module/serviceControl/images/blue.jpg" align="absmiddle" />当月&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=rootPath%>/portalCommon/module/serviceControl/images/green.jpg" align="absmiddle" />上月</td>
    <td width="140">当月: <span id="num_5"></span><br/>
                                                    上月: <span  id="avg_5"></span></td>
  </tr>
</table>
</div>
</body>
<script type="text/javascript">
var zoneInfo = top.getSessionAttribute('zoneInfo');
function openTreeTab(menuId,menuName,url)
{
    var dateTime=document.getElementById('dateTime').value;
    url +='&date='+dateTime+'&areaCode='+zoneInfo.zoneCode;
    menuId=menuId.split("_")[0];
	return parent.openTreeTab(menuId,menuName,url,'top');
}
</script>
</html>
<script type="text/javascript" src="channelToMon.js"></script>