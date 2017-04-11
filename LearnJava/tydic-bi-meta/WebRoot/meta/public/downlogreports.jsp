<%-- Set the content type header with the JSP directive --%>
<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.meta.module.mag.login.LoginReportAction" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="tydic.meta.module.mag.login.LoginReportDAO" %>
<%@ page import="java.util.HashMap" %>

<% response.setHeader("Content-Disposition", "attachment; filename=\"visitLog.xls\""); %>
<%
   LoginReportDAO loginReportDAO = new LoginReportDAO();
   String zoneId = request.getParameter("zoneId");
   String startDate = request.getParameter("startDate");
   String endDate = request.getParameter("endDate");
   String menuId = request.getParameter("menuId");
   Map<String,Object> param = new HashMap<String, Object>();
   param.put("startDate",startDate);
   param.put("endDate",endDate);
   String [] menuIdArr = menuId.split(",");
   String menu = request.getParameter("menuName").toString();
   String [] menuName = new String(menu.getBytes("ISO8859-1"),"UTF-8").split(",");
   List<Map<String,Object>> userData =loginReportDAO.querySubZone(Integer.parseInt(zoneId),menuId,param);
%>
<%-- Set the content disposition header --%>

       <html>
            <head><title></title></head>
            <body>
                <table>
                    <th>
                        <% for(int m=0; m<menuName.length; m++){  %>
                            <td>
                                <%=menuName[m]%>
                            </td>
                         <%   }
                         %>
                    </th>
                <% for(int i = 0; i < userData.size(); i++){ %>
                  <tr>
                    <%
                        Map<String,Object> result = userData.get(i);
                        for(int j=0; j<menuName.length; j++){
                        %>
                      <td>
                        <% if(j==0){out.print(result.get("ZONE_NAME"));}
                            else if(j==1){out.print(result.get("LOGIN_NUM"));}
                            else{
                                out.print(result.get("VISIT_NUM"+menuIdArr[j-2]));
                            }
                        %>
                      </td>
                    <% } %>
                  </tr>
                <% }
                    loginReportDAO.close();
                    loginReportDAO=null;
                %>
                </table>



            </body>
       </html>


