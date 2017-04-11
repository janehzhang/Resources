<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  <title>我创建的报表</title>
  <script type="text/javascript" src="myCreateRpt.js"></script>
  <style type="text/css">
       .container {width: 100%;margin: 0 auto;padding-top: 5px;margin-left:100px;zoom: 1;overflow-y:scroll;height:750px;}
	   .search {margin-bottom:20px;}
	   .search_input {width:600px;height:24px; margin: 0px;}
	   .my-link {height: 20px;}
       .my-link ul {margin:0 0 0 300px;}
       .my-link ul li {
		   list-style-type:none;
			float:left;
			margin-left:50px;
			font-size:12px;
		}
		.parhLog {margin: 5px 20px;}
		.rptList {margin: 5px 40px;}
		.rptName{font-size: 14px;}
		.item {width: 500px;}
		.rplTable {margin: 20px 0px;}
  </style>

  </head>
  
  <body>
    <div class="container">
      <div class="my-link">
			<ul>
				<li><a href="" id="" style="font-size: 14px;">报表订购</a></li>
				<li><a href="" id="" style="font-size: 14px;">指标订购</a></li>
			</ul>
	 </div>
     <div class="search" style="margin: 5px 10px;" >
			<span><input class="search_input" value="2012春促"/></span>
			<span><input type="button" value="搜索" class="search_submit" /></span>
	 </div>
	 <div class="parhLog"><a href="#">报表超市</a><span>->我创建的报表</span></div>
	 <div class="rptList">
	   <table cellpadding="0" cellspacing="0" style="border:0;" class="rplTable">
	     <tr>
          <td>
            <span class="rptName">2012春促日报</span> <span style="margin-left: 50px"><a href="#">删除</a></span>
          </td>
          </tr>
          <tr>
          <td>
            <div class="item">
             <span>日期、本地网、新增用户数、第二部手机新增用户数、3G手机新增用户数、智能终端新增用户数</span>
            </div>
          </td>	     
	     </tr>
	   </table>
	   <table cellpadding="0" cellspacing="0" style="border:0;" class="rplTable">
	     <tr>
          <td>
            <span class="rptName">2012春促日报</span> <span style="margin-left: 50px"><a href="#">删除</a></span>
          </td>
          </tr>
          <tr>
          <td>
            <div class="item">
             <span>日期、本地网、新增用户数、第二部手机新增用户数、3G手机新增用户数、智能终端新增用户数</span>
            </div>
          </td>	     
	     </tr>
	   </table>
	 </div>
    </div>
  </body>
</html>
