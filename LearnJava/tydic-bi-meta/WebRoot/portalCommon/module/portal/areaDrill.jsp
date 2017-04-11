<%@page language="java" import="java.util.*"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>辖区内容</title>
	    <%@include file="../../public/head.jsp" %>
	    <%@include file="../../public/include.jsp" %>
		<script type="text/javascript" src="<%=path %>/dwr/interface/PortalCommonCtrlr.js"></script>
		<script type="text/javascript" src="<%=path %>/dwr/interface/PortalInstructionAction.js"></script>
		<script type="text/javascript" src="<%=path %>/dwr/interface/MenuVisitLogAction.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/TableMouse.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/formatNumber.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/Drag.js"></script>
	    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
        <link   href="<%=path %>/css/fpage.css" rel='stylesheet' type='text/css'>
	    <style type="text/css">
	      .poster_btn {
				    cursor: pointer;
				    display: inline-block;
				    font-size: 12px;
				    height: auto;
				    line-height: 18px;
				    overflow: hidden;
				    right: 0;
				    padding-bottom: 1px;
				    margin:5px 5px 5px 5px;
				    border-radius: 2px;
				    color: black;
				    border: 1px solid #198EB4;
				    background-color: #D6E8FF;
				    background-position: 0 0 !important;
				}
	       .poster_btn1 {
				    cursor: pointer;
				    display: inline-block;
				    font-size: 12px;
				    height: auto;
				    overflow: hidden;
				    color: black;
				    border: 1px solid #198EB4;
				    background-color: #D6E8FF;
				    background-position: 0 0 !important;
				}			
			.tab_01{
				margin: 0 auto;
				border-top: #a9b8cd solid 1px;
				border-left: #a9b8cd solid 1px;
				text-align: center;
			}
		    .tab_01 tbody tr td{
				padding: 3px 4px;
				border-top: #FFF solid 1px;
				border-left: #FFF solid 1px;
				border-right: #a9b8cd solid 1px;
				border-bottom: #a9b8cd solid 1px;
			}			
	         #left{float: left; height: auto; width: 19.4%;}
			
			 #left_1{height:160px; width: 100%; border: 1px solid #87CEFF;}
			.u_1{width:99%;height:auto;magin: 0px;padding: 0px;float: left;display:inline;list-style:none;margin-left: 0px}
		    .li_1{list-style-position:outside;list-style-type:none;background-image:url(<%=path %>/images/u72_normal.gif);background-repeat: no-repeat;
		          background-position:0px; padding-left: 20px; margin-top: 5px}
		    
			 #left_2{height: 160px; width: 100%; border: 1px solid #87CEFF;margin-top:3px;}
		     .u_2{width:99%;height:auto;magin: 0px;padding: 0px;float: left;display:inline;list-style:none;margin-left: 0px}
		     .li_2{width:100%;list-style-position:outside;list-style-type:none;margin-top: 0px;display: inline;}
		     .li_2 span{color: blue; float: right;} 
			
			 #u156_line{width:100%;height:2px;background-image:url(<%=path %>/images/u87.png);background-repeat:no-repeat;}
			
		    #left_3{height: 220px; width: 100%; border: 1px solid #87CEFF;margin-top:3px; }
			#left_4{height: 100px; width: 100%; border: 1px solid #87CEFF;margin-top:3px; }
		   
		    #main{ float: right;  height: auto; width: 100%; border: 1px solid #87CEFF;}
		    
		    #chartdiv{border: 1px solid #87CEFF;}
		    .tips{width: 100%;border: 1px solid #87CEFF; }
	        .unl {
	            text-decoration: underline;
	            color: #0000FF;
	            cursor: pointer;
	        }
	        #chartTitle{
	         float: left;font:14px;
	         font-weight:bold;
	        }	    
		   #psButton{
		     float: right;
		     margin:0;
		     padding:0;
		    }
	    </style>
		<%
			
		%>
	</head>
<body>
 <div id="div_src"
	     style="position: absolute; width: 100%;height:auto!important; z-index: 1000; top: 0px; left: 0px;
				background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:visible;"
	     title="" class="dragBar">
	    <center style="color: red;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
 </div>

<form id="queryform" name="queryform">	
 	 <input type="hidden" id="tabId"     name="tabId" />
	 <input type="hidden" id="indexId"   name="indexId" />
	 <input type="hidden" id="zoneCode"  name="zoneCode" />
	 <input type="hidden" id="dataDate"  name="dataDate" />
	 <input type="hidden" id="indexName" name="indexName" />	 
 <div style="border:1px solid #87CEFF;width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
    <div id="main">
		      <table  width="100%" border="0"  cellpadding="0" cellspacing="0" background="<%=path %>/images/fpage_04.gif">
	                <tr height="25">
	                  <td>
                   	     <table  width="100%" border="0"  cellpadding="0" cellspacing="0">
	                        <td id="chartTitle"  nowrap align="left"></td>
	                        <td id="psButton"    nowrap align="right"> 
	                          <input type="button" id="impBtn" name="impBtn" class="poster_btn1"  value="导  出"  onclick="impExcel();"  style="width:60px;"/>
	                        </td>
                         </table>
	                  </td>
	                </tr>
	           </table>
	           <div id=chartdiv>图形初始化</div>
	           <div id="rpt_tabbar" style="width:100%; height:100%;">
	           
               </div> 
	 </div>
 </div>
</form>
<iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript">
//全局用户数据
var lurl=decodeURI(window.location.href);
lurl=lurl.substring(lurl.indexOf("?")+1);
var dataDate=getQuery("dataDate",lurl);
var tabId   =getQuery("tabId",lurl);
var indexId =getQuery("indexId",lurl);
var indexName=getQuery("indexName",lurl);	//数据时间
var zoneCode=getQuery("zoneCode",lurl);	
var tabName=getQuery("tabName",lurl);
	function impExcel(){
		       $("tabId").value=tabId;
		       $("indexId").value=indexId;
		       $("zoneCode").value=zoneCode;
		       $("dataDate").value=dataDate;
		       $("indexName").value=indexName;
	     	   var url = getBasePath()+"/portalCommon/module/portal/impExcel/areaDrill_imp_excel.jsp";
				  document.forms[0].method = "post";
				  document.forms[0].action=url;
				  document.forms[0].target="hiddenFrame";
				  document.forms[0].submit();
	}
</script>
<script type="text/javascript" src="areaDrill.js"></script>