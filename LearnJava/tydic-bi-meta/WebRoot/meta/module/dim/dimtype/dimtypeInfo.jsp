<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="../../../public/head.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title></title>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=DIM_TYPE_STATE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimTypeAction.js"></script>
    <script type="text/javascript" src="dimtypeInfo.js"></script>
    <script type="text/javascript">
        var tableId='<%=request.getParameter("tableId")%>';
    </script>
    <style type="text/css">
     .tdHeadStyle{
             height:22px;
             text-align:center;   
      }
      .trStyle{
        background-color:red;
      }
      table td{text-align:center}
    </style>
</head>
  <body style="width:100%;height:100%">
  <div id="main" style="height: 100%;width: 100%">
    <div id="dimtype" style="height: 100%;width: 100%; overflow-y:auto;" class="gridbox">
        <div id="typehead" style="height:11%;width:100%;" class="xhdr">
          <table cellpadding="0" cellspacing="0" style="width:100%;" class="hdr">
             <thead>
              <tr style="height: 0px; ">
                  <th style="height: 0px; width: 10% "></th>
                  <th style="height: 0px; width: 30%"></th>
                  <th style="height: 0px; width: 45% "></th>
                  <th style="height: 0px; width: 15% "></th>                 
                </tr>
                <tr>
                  <td style="text-align:center">归并类型编码</td>
                  <td style="text-align:center">归并名称</td>
                  <td style="text-align:center">归并描述</td>
                  <td style="text-align:center">操作</td>
                </tr>
             </thead>
          </table>
        </div><!--end head  -->
        <div id="typebody" style="width: 100%;height:89%;overflow-y:auto;" class="objbox">
          <table id='dimtypetable' cellpadding="0" cellspacing="0" style="width: 100%;" class="obj">
             <tbody>
              <tr style="height: 0px;">
                  <th style="height: 0px; width: 10% "></th>
                  <th style="height: 0px; width: 30% "></th>
                  <th style="height: 0px; width: 45% "></th>
                  <th style="height: 0px; width: 15% "></th>
                </tr>
             </tbody>
          </table>
        </div><!--end body  -->
    </div><!--end typeDiv  -->
    <div id="dimlevel" style="height:100%;width: 100%;" class="gridbox">
         <div id="levelhead" style="height:28px;width:100%;" class="xhdr">
           <table cellpadding="0" cellspacing="0" class="hdr" style="width:100%;">
             <thead>
              <tr style="height: 0px; ">
                  <th style="height: 0px; width: 24% "></th>
                  <th style="height: 0px; width: 43% "></th>
                  <th style="height: 0px; width: 30% "></th>
              </tr>
              <tr>
                  <td style="text-align:center">层次编码</td>
                  <td style="text-align:center">层次名称</td>
                  <td style="text-align:center">操作</td>
                </tr>
             </thead>
           </table>
         </div><!--end head-->
         <div id="levelbody" style="width: 100%;height:65%;overflow-x:hidden;overflow-y:scroll;" class="objbox">
          <table id='dimleveltable' cellpadding="0" cellspacing="0" style="width: 100%;height:auto;" class="obj">
             <tbody>
              <tr style="height: 0px;">
                  <th style="height: 0px; width: 25% "></th>
                  <th style="height: 0px; width: 45% "></th>
                  <th style="height: 0px; width: 30% "></th>
                </tr>
             </tbody>
          </table>
        </div><!--end body  -->
         <div id="_button" style="width: 100%;height:auto; margin-top:10px;"></div>
    </div>
  </div>
  </body>
</html>

