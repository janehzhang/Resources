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
     <title>宽带装移机端到端全流程各环节管控（月报）</title>
     <link href="css/style.css" rel="stylesheet" type="text/css" />
	 <%@include file="../../public/head.jsp"%>
	 <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/StepInstallAction.js"></script>
	 <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<div class="bd">
  <div class="title"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/clock.png" /><a href="javascript:openTreeTab('1_mon','宽带装维全流程管控月报
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=128')" style=" font-size:20px;font-weight:bolder;text-decoration:none;color:black">宽带装移机端到端全流程各环节管控（月报）</a></div>
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
           <td width="8%">宽带类型:</td>
											                  <td width="6%">
											                  		<select id="indexType" name="indexType">
											                          <option value="" >==请选择==</option>
											                          <option value="0">普通</option>
											                          <option value="1" >光纤</option>
											                       </select>
											                  </td>
          <input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" />
  </div>
<div class="menu">
    <span style="width:110px;text-align:center;line-height:50px;"><a style="text-align:center;line-height:40px;" href="javascript:openTreeTab('2_mon','装移_受理月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=109')">受理订单</a></span>
    <span style="width:570px;text-align:center;line-height:40px;">环节平均历时</span>
    <span style="width:140px;text-align:center;line-height:40px;">环节及时成功率</span>
  
 </div>
<div align="center"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/num.jpg" /></div>
<div class="line"></div>


<div>
<table width="944" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td rowspan="2" width="112" align="center" valign="middle">
      <div class="item">
        <a href="javascript:openTreeTab('3_mon','装移_收费环节月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=110')">收费环节</a>
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
    <td rowspan="2" width="122" align="center">时限要求：<br>0-24小时</td>
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
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=111')">资源配置环节</a> </div></td>
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
      <td rowspan="2" width="122" align="center">时限要求：<br/>0-4小时</td>
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
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=112')">数据施工环节</a> </div></td>
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
      <td rowspan="2"  width="122" align="center">时限要求：<br />
        0-4小时</td>
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
      <td  rowspan="2" width="112" align="center" valign="middle"><div class="item"> <a href="javascript:openTreeTab('6_mon','装移_外线施工月报表
','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=113')">外线施工环节</a> </div></td>
      <td width="560" valign="middle">
        <div class="tt">
    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
      <tr>
        <td  id="widthNum_3"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
        
        </td>
        <td width="100%">&nbsp;</td>
      </tr>
    </table>

  </div>
      
      </td>
      <td  rowspan="2" align="center" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
        <tr>
          <td width="110" height="52" valign="middle"><div align="center">外线施工成<br />
            功归档率：</div></td>
          <td width="176">当月: <span id="num_3"></span><br />
                                                                      上月: <span id="avg_3"></span></td>
        </tr>
      </table></td>
      </tr>
       <tr>
          <td width="560"  valign="middle">
             <div class="tt">
			      <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
			        <tr>
			          <td id="widthAvg_3"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
			          
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
                                          上月平均: <span  id="avg_4"></span></td>
    <td width="416" valign="middle" class="tip">图例说明：(单位：小时)&nbsp;&nbsp;<img src="<%=rootPath%>/portalCommon/module/serviceControl/images/blue.jpg" align="absmiddle" />当月时间&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=rootPath%>/portalCommon/module/serviceControl/images/green.jpg" align="absmiddle" />上月时间</td>
    <td width="140">当月: <span id="num_5"></span><br/>
                                                    上月: <span  id="avg_5"></span></td>
    <td width="122">&nbsp;</td>
  </tr>
</table>
<div class="blank30px"></div>
<div>
<table width="944" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="113"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/yc_l.jpg" width="113" height="32" /></td>
    <td width="811" background="<%=rootPath%>/portalCommon/module/serviceControl/images/yc_bg.jpg">&nbsp;</td>
    <td width="20"><img src="<%=rootPath%>/portalCommon/module/serviceControl/images/yc_r.jpg" width="20" height="32" /></td>
  </tr>
</table>
</div>
<div class="blank10px"></div>
<div class="line"></div>
<div>
<table width="944" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td rowspan="2" width="112" align="center" valign="middle">
      <div class="item2">
        <div class="link1"><a href="javascript:openTreeTab('7_mon','缓装库月报','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=120')">缓装库</a></div>
        <div class="link2"><a href="javascript:openTreeTab('8_mon','缓装原因月报','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=121')">缓装原因</a></div>    
      </div>
    </td>
    <td width="566" valign="middle">
			 <div class="tt">
			    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
			      <tr>
			        <td  id="orderExcepiton_num1"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
			        
			        </td>
			        <td width="100%">&nbsp;</td>
			      </tr>
			    </table>
			  </div>
    </td>
    <td rowspan="2" align="center" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
      <tr>
        <td width="110" height="52" valign="middle"><div align="center">缓装率：</div></td>
        <td width="176">当月: <span id="orderExcepiton_num2"></span><br />
                                                               上月: <span  id="orderExcepiton_avg2"></span></td>
      </tr>
    </table></td>
    </tr>
    <tr>
          <td width="566" valign="middle">
           <div class="tt">
			       <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
			        <tr>
			          <td id="orderExcepiton_avg1"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
			          
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
      <td rowspan="2" width="112" align="center" valign="middle">
         <div class="item2">
           <div class="link1"><a href="javascript:openTreeTab('9_mon','待装库月报','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=122')">待装库</a></div>
           <div class="link2"><a href="javascript:openTreeTab('10_mon','待装原因月报','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=123')">待装原因</a></div>    
         </div>
      </td>
      <td width="566" valign="middle">
	  <div class="tt">
	    <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
	      <tr>
	        <td  id="orderExcepiton_num3"   height="10"  bgcolor="#0099ff" style="padding-left:4px;">
	        
	        </td>
	        <td width="100%">&nbsp;</td>
	      </tr>
	    </table>
	   </div>
      </td>
      <td rowspan="2" align="center" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
        <tr>
          <td width="110" height="52" valign="middle"><div align="center">待装率：</div></td>
          <td width="176">当月: <span   id="orderExcepiton_num4"></span><br />
                                                                        上月: <span  id="orderExcepiton_avg4"></span></td>
        </tr>
      </table></td>
    </tr>
    <tr>
       <td width="566" valign="middle">
         <div class="tt">  
		      <table width="550" border="0" align="left" cellpadding="0" cellspacing="0" style="margin:0 0 0 3px;">
		        <tr>
		          <td id="orderExcepiton_avg3"  height="10"  bgcolor="#8fbe57" style="padding-left:4px;">
		          
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
      <td width="112" align="center" valign="middle"><div class="item"> <a href="javascript:openTreeTab('11_mon','撤单月报','http://132.121.165.45:8081/tydic_generateReportsV1/initReport.do?systemId=1&reportId=119')">撤单</a> </div></td>
      <td width="566" height="66" valign="middle">&nbsp;</td>
      <td align="center" valign="middle"><table width="275" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/portalCommon/module/serviceControl/images/iten_r.jpg">
        <tr>
          <td width="110" height="52" valign="middle"><div align="center">撤单率：</div></td>
          <td width="176">当月: <span  id="orderExcepiton_num5"></span><br />
                                                                  上月: <span   id="orderExcepiton_avg5"></span></td>
        </tr>
      </table></td>
    </tr>
  </table>
</div>
<div class="line"></div>
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
<script type="text/javascript" src="step_install_mon.js"></script>