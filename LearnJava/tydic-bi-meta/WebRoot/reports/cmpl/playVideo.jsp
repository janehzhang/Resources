<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="tydic.meta.sys.code.CodeManager,tydic.frame.SystemVariable" %>
<%
		
		String serialNo=(String)request.getParameter("serialNo");
		String staffId=(String)request.getParameter("staffId");
		String cityId=(String)request.getParameter("cityId");
		String cityCSP=CodeManager.getValue("ZONE_CODE_CSP", cityId);
		String cspPath=SystemVariable.getString(cityCSP,"");
		cspPath +="?system=KFFX&serialNo="+serialNo+"&staffId=900101&cityId="+cityId; 
		System.out.println("录音播放Path: "+cspPath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>播放录像</title>
		<%@include file="../../portalCommon/public/head.jsp"%>
		<%@include file="../../portalCommon/public/include.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
	</head>
	<body>
	   <table style='margin:2px;border: 1px solid #87CEFF;' width='100%'  height='100%' border='0'  cellpadding='0px' cellspacing='0px'>
				<tr height="100%">
					<td  width='100%' valign="top" id="fontzoom" align="center" style="word-break: break-all; Width: fixed">
						 <object id="player" height="64" width="360" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6">
								<PARAM NAME="EnableContextMenu" VALUE="-1">
								<PARAM NAME="ShowDisplay" VALUE="-1">
								<PARAM NAME="ShowControls" VALUE="-1">
								<PARAM NAME="ShowPositionControls" VALUE="0">
								<PARAM NAME="ShowSelectionControls" VALUE="0">
								<PARAM NAME="EnablePositionControls" VALUE="-1">
								<PARAM NAME="EnableSelectionControls" VALUE="-1">
								<PARAM NAME="ShowTracker" VALUE="-1">
								<PARAM NAME="EnableTracker" VALUE="-1">
								<PARAM NAME="AllowHideDisplay" VALUE="-1">
								<PARAM NAME="AllowHideControls" VALUE="-1">
								<PARAM NAME="MovieWindowSize" VALUE="0">
								<PARAM NAME="FullScreenMode" VALUE="0">
								<PARAM NAME="MovieWindowWidth" VALUE="208">
								<PARAM NAME="MovieWindowHeight" VALUE="160">
								<PARAM NAME="AutoStart" VALUE="-1">
								<PARAM NAME="AutoRewind" VALUE="-1">
								<PARAM NAME="PlayCount" VALUE="1">
								<PARAM NAME="SelectionStart" VALUE="0">
								<PARAM NAME="SelectionEnd" VALUE="32767">
								<PARAM NAME="Appearance" VALUE="1">
								<PARAM NAME="BorderStyle" VALUE="1">
							    <param NAME="url"  value="<%=cspPath %>"> 
							 	<PARAM NAME="DisplayMode" VALUE="0">
							 	<PARAM NAME="volume" value="100"> 
							    <!--默认声音大小0%-100%,50则为50%--> 
								<PARAM NAME="AllowChangeDisplayMode" VALUE="-1">
								<PARAM NAME="DisplayForeColor" VALUE="16777215">
								<PARAM NAME="DisplayBackColor" VALUE="0">
						 </object> 
						<br>
						<br>
					    <a href="javascript:upload('<%=cspPath %>')">如果播放有异常，请您点击下载到本地播放！</a>
				    </td>
				</tr>
	   </table>	
	   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>	
  </body>
</html>
<script type="text/javascript">
var base = getBasePath();
var upload = function(path){
 	//var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ path;
	window.open(path,"hiddenFrame","");
}
</script>