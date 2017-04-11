<%@ page language="java"  pageEncoding="UTF-8"%>
<table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
	<tr>
	     <td>
			 <table  width="1000"  border="0" cellpadding="0" cellspacing="0">
               <tr>
                  <td width="2%">月 份:</td>
                  <td width="10%">
                      <select id="dateTime" name="dateTime">
                        <% for(String month:months ){  %>
			              <option value="<%=month %>"><%=month %></option>
			            <%  }%> 
                      </select>
                  </td>
                  <td width="2%">区 域:</td>
                  <td width="10%">
					  <input type="hidden"  id="zoneCode"    value="<%=zoneCode%>"    name="zoneCode" />
					  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
                  </td>
                  <td nowrap width="30%">
                     <input type="button"    class="poster_btn" id="queryBtn"  name="queryBtn"    onclick="queryData();"        style="width:60px;"  value="查  询"/>
                     <input type="button"    class="poster_btn" id="impBtn"    name="impBtn"      onclick="impExcel();"         style="width:60px;"  value="导  出"/>
                     <!--<input type="button"    class='poster_btn' id='city'      name='city'        onclick="lookCity()"          style="width:60px;"  value="切换地市"/>  -->
                  </td>										                  
                </tr>
			 </table>			        
	       </td>
	 </tr>
</table>	
