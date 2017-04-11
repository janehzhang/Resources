<%@ page import="java.net.URLDecoder" %>
<%@ page language="java" import="java.util.*"  pageEncoding="utf-8"%>
<%
	List<Map<String, Object>> colList = (List<Map<String, Object>>)request.getAttribute("colList");
	List<Map<String,Object>> colDatatypeList = (List<Map<String,Object>>)request.getAttribute("colDatatypeList");
	int dataColSize = 0;
	if(colList != null && colList.size() > 0)
		dataColSize = colList.size();
	
	String rowIndex = (String)request.getAttribute("rowIndex");
%>

<%--编辑列表单--%>
<form action="" id="_columnForm" style="width:100%;height: 100%">
    <div style="height: 100%; width:100%; ">
        <%--表头DIV--%>
        <div style="width:100%;" class="gridbox" id="_columnTableDiv">
            <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                 class="xhdr">
                <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr" id="_columnHeadTable">
                    <tbody>
                    <tr style="height: 0px; ">
                        <%--定义表头列宽--%>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                    </tr>
                    <tr>
                        <td style="text-align: center; line-height:22px;" nowrap>名称<span style="color: red;">*</span></td>
                        <td style="text-align: center; line-height:22px;" nowrap>列中文名</td>
                        <td style="text-align: center; line-height:22px;" nowrap>类型<span style="color: red;">*</span></td>
                        <td style="text-align: center; line-height:22px;" nowrap>主键</td>
                        <td style="text-align: center; line-height:22px;" nowrap>允许空</td>
                        <td style="text-align: center; line-height:22px;" nowrap>默认值</td>
                        <td style="text-align: center; line-height:22px;" nowrap>注释</td>
                        <td style="text-align: center; line-height:22px;" nowrap>维度</td>
                        <td style="text-align: center; line-height:22px;" nowrap>操作
                         <img src="../../../resource/images/delete.png" alt="清空所有" title="清空所有" style="cursor: pointer;width: 16px;height: 16px;position: relative;" onclick="clearAllColumnData(true,<%=rowIndex%>)">
                    </tr>
                    </tbody>
                </table>
            </div>
            <%--表体内容--%>
            <div style="width: 100%;  overflow-y:auto;overflow-x: hidden" class="objbox" id="_clumnContentDiv<%=rowIndex%>">
                <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_clumnContentTable<%=rowIndex%>">
                    <tbody id="_clumnContentBody<%=rowIndex%>">
                    <%--定义表体列宽--%>
                    <tr style="height: 0px; ">
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                        <th style="height: 0px; " nowrap></th>
                    </tr>
                    <%--</tbody>--%>
                    <%
                    	if(dataColSize > 0){
                    		for(int r = 0; r < dataColSize; r++){
                    			out.println("<tr id='_columnRow"+rowIndex+(r+1)+"'>");
                    			Map<String, Object> m = colList.get(r);
                    			for(int c = 0; c < 9; c++){
                    				out.println("<td style='text-align:center;' nowrap>");
	                    			if(c == 0){
	                    				out.println("<input classname='dhxlist_txt_textarea' style='width: 90%; height: 15px; line-height: 15px; text-transform: uppercase; ' type='text' name='colName"+rowIndex+(r+1)+"' id='_colName"+rowIndex+(r+1)+"' value='"+m.get("COL_NAME")+"'/>");
	                    			}else if(c == 1){//列中文名
	                    				String colBusComment = "";
	                    				if(m.get("COL_NAME_CN") != null && !"null".equals(m.get("COL_NAME_CN"))){
	                    					colBusComment = m.get("COL_NAME_CN")+"";
	                    				}else if(m.get("COL_BUS_COMMENT") != null && !"null".equals(m.get("COL_BUS_COMMENT"))){
	                    					colBusComment = m.get("COL_BUS_COMMENT")+"";
	                    				}
	                    				if(colBusComment.length() > 32)
	                    					colBusComment = colBusComment.substring(0,32);
	                    				out.println("<input type='text' classname='dhxlist_txt_textarea' style='width: 90%;height:15px;line-height:15px;' name='colNameCn"+rowIndex+(r+1)+"' id='_colNameCn"+rowIndex+(r+1)+"' value='"+colBusComment+"'/>");
	                    			}else if(c == 2){//类型--select
	                    				out.println("<select id='_colDatatype"+rowIndex+(r+1)+"' style='width:100px;height:20px'>");
	                    				if(colDatatypeList != null && colDatatypeList.size() > 0){
                							out.println("<option value='"+m.get("COL_DATATYPE")+"' selected>"+m.get("COL_DATATYPE")+"</option>");
	                    					for(Map<String,Object> map : colDatatypeList){
	                    						out.println("<option value='"+map.get("CODE_NAME")+"'>"+map.get("CODE_NAME")+"</option>");
	                    					}
	                    				}else
	                    					out.println("<option value='"+m.get("DATA_TYPE")+"' selected>"+m.get("COL_DATATYPE")+"</option>");
	                    				out.println("</select>");
	                    			}else if(c == 3){//是否是主键--checkbox
	                    				out.println("<div id='_isPrimaryDiv"+rowIndex+(r+1)+"'>");
	                    				if("1".equals(String.valueOf(m.get("IS_PRIMARY")))){
	                    					out.println("<input type='checkbox' style='width: 90%;' name='isPrimary"+rowIndex+(r+1)+"' id='_isPrimary"+rowIndex+(r+1)+"' value='1' checked onclick=\"showColNullAbled("+rowIndex+","+(r+1)+",this);\">");
	                    				}else
	                    					out.println("<input type='checkbox' style='width: 90%;' name='isPrimary"+rowIndex+(r+1)+"' id='_isPrimary"+rowIndex+(r+1)+"' value='0' onclick=\"showColNullAbled("+rowIndex+","+(r+1)+",this);\">");
	                    				out.println("</div>");
	                    			}else if(c == 4){//允许为空--checkbox
	                    				out.println("<div id='_colNullabledDiv"+rowIndex+(r+1)+"'>");
	                    				if("1".equals(String.valueOf(m.get("IS_PRIMARY"))) || !"1".equals(String.valueOf(m.get("COL_NULLABLED")))){
	                    					out.println("<input type='checkbox' style='width: 90%;' name='colNullabled"+rowIndex+(r+1)+"' id='_colNullabled"+rowIndex+(r+1)+"' value='0' onclick=\"isPrimary("+rowIndex+","+(r+1)+",this);\">");
	                    				}else
	                    					out.println("<input type='checkbox' style='width: 90%;' name='colNullabled"+rowIndex+(r+1)+"' id='_colNullabled"+rowIndex+(r+1)+"' value='1' checked onclick=\"isPrimary("+rowIndex+","+(r+1)+",this);\">");
	                    				out.println("</div>");
	                    			}else if(c == 5){//默认值
	                    				String defaultVal = "";
	                    				if(m.get("DEFAULT_VAL") != null && !"null".equals(m.get("DEFAULT_VAL")))
	                    					defaultVal = m.get("DEFAULT_VAL")+"";
	                    				out.println("<input type='text' classname='dhxlist_txt_textarea' style='width: 90%;height:15px;line-height:15px;' name='defaultVal"+rowIndex+(r+1)+"' id='_defaultVal"+rowIndex+(r+1)+"' value='"+defaultVal+"'>");
	                    			}else if(c == 6){//注释
	                    				String colBusComment = "";
	                    				if(m.get("COL_BUS_COMMENT") != null && !"null".equals(m.get("COL_BUS_COMMENT")))
	                    					colBusComment = m.get("COL_BUS_COMMENT")+"";
	                    				if(colBusComment.length() > 1000)
	                    					colBusComment = colBusComment.substring(0,1000);
	                    				out.println("<textarea classname='dhxlist_txt_textarea' name='colBusComment"+rowIndex+(r+1)+"' id='_colBusComment"+rowIndex+(r+1)+"'>"+colBusComment+"</textarea>");
	                    			}else if(c == 7){//维度
	                    				out.println("<input type='text' classname='dhxlist_txt_textarea' name='dimInfo"+rowIndex+(r+1)+"' id='_dimInfo"+rowIndex+(r+1)+"' style='width:70%' value='' readOnly onclick=\"getDimInfo("+rowIndex+","+(r+1)+");\">");
	                    				out.println("<img src='../../../resource/images/cancel.png' style='width:12px;height:12px;' title='清除维度信息' onclick=\"clearComment("+rowIndex+","+(r+1)+");\">");
	                    				//新增维度有关的hidden隐藏域
	                    				out.println("<input type='hidden' name='colBusType"+rowIndex+(r+1)+"' id='_colBusType"+rowIndex+(r+1)+"' value='1'/>"); 
	                    				out.println("<input type='hidden' name='dimLevel"+rowIndex+(r+1)+"' id='_dimLevel"+rowIndex+(r+1)+"' value=''/>"); 
	                    				out.println("<input type='hidden' name='dimColId"+rowIndex+(r+1)+"' id='_dimColId"+rowIndex+(r+1)+"' value=''/>"); 
	                    				out.println("<input type='hidden' name='dimTableId"+rowIndex+(r+1)+"' id='_dimTableId"+rowIndex+(r+1)+"' value=''/>"); 
	                    				out.println("<input type='hidden' name='dimTypeId"+rowIndex+(r+1)+"' id='_dimTypeId"+rowIndex+(r+1)+"' value=''/>");
	                    			}else{//操作
	                    				out.println("<img src='../../../resource/images/cancel.png' alt='删除' onclick=\"removeRow("+rowIndex+","+(r+1)+")\" style='width:16px;height: 16px;cursor: pointer'>");
	                    				//out.println("<img src='../../../resource/images/move_up.png' id='_moveUp"+(r+1)+"' alt='上移' onclick=\"moveUp(this,'_moveUp','_moveDown')\" style='width:16px;height: 16px;margin-left: 5px;cursor: pointer;'>");
	                    				//out.println("<img src='../../../resource/images/move_down.png' id='_moveDown"+(r+1)+"' alt='下移' onclick=\"moveDown(this,'_moveUp','_moveDown')\" style='width:16px;height: 16px;margin-left: 5px;cursor: pointer;'>");
	                    			}
	                    			out.println("</td>");
                    			}
                    			
                    			out.println("</tr>");
                    		}
                    	}
                    	/*增加一空行*/
                    	out.println("<tr id='_columnRow"+rowIndex+(dataColSize+1)+"'>");
                    	//第一列增加：自动增加行事件
                    	out.println("<td style='text-align:center;' nowrap>");
                    	out.println("<input classname='dhxlist_txt_textarea' style='width: 90%; height: 15px; line-height: 15px; text-transform: uppercase;' type='text' name='colName"+rowIndex+(dataColSize+1)+"' id='_colName"+rowIndex+(dataColSize+1)+"' value='' onchange=\"addColumnRow("+rowIndex+","+(dataColSize+1)+",true);\"/>");
                    	out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
                    	out.println("<input classname='dhxlist_txt_textarea' style='width: 90%;height:15px;line-height:15px;' type='text' name='colNameCn"+rowIndex+(dataColSize+1)+"' id='_colNameCn"+rowIndex+(dataColSize+1)+"' value=''/>");
                    	out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
                    	out.println("<select id='_colDatatype"+rowIndex+(dataColSize+1)+"' style='width:100px;height:20px'>");
	                    if(colDatatypeList != null && colDatatypeList.size() > 0){
	                    	for(Map<String,Object> map : colDatatypeList){
	                    		out.println("<option value='"+map.get("CODE_NAME")+"'>"+map.get("CODE_NAME")+"</option>");
	                    	}
	                    }else
	                    	out.println("<option value='NUMBER' selected>NUMBER</option>");
	                    out.println("</select>");
	                    out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
	                    out.println("<div id='_isPrimaryDiv"+rowIndex+(dataColSize+1)+"'>");
	                    out.println("<input style='width: 90%;' type='checkbox' name='isPrimary"+rowIndex+(dataColSize+1)+"' id='_isPrimary"+rowIndex+(dataColSize+1)+"' value='0' onclick=\"showColNullAbled("+rowIndex+","+(dataColSize+1)+",this);\">");
	                    out.println("</div>");
	                    out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
                    	out.println("<div id='_colNullabledDiv"+rowIndex+(dataColSize+1)+"'>");
	                    out.println("<input style='width: 90%;' type='checkbox' name='colNullabled"+rowIndex+(dataColSize+1)+"' id='_colNullabled"+rowIndex+(dataColSize+1)+"' value='0' onclick=\"isPrimary("+rowIndex+","+(dataColSize+1)+",this);\">");
	                    out.println("</div>");
	                    out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
	                    out.println("<input classname='dhxlist_txt_textarea' style='width: 90%;height:15px;line-height:15px;' type='text' name='defaultVal"+rowIndex+(dataColSize+1)+"' id='_defaultVal"+rowIndex+(dataColSize+1)+"' value=''>");
	                    out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
                    	out.println("<textarea classname='dhxlist_txt_textarea' name='colBusComment"+rowIndex+(dataColSize+1)+"' id='_colBusComment"+rowIndex+(dataColSize+1)+"'></textarea>");
                    	out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
                    	out.println("<input classname='dhxlist_txt_textarea' style='width:70%' type='text' name='dimInfo"+rowIndex+(dataColSize+1)+"' id='_dimInfo"+rowIndex+(dataColSize+1)+"' value='' readOnly onclick=\"getDimInfo("+rowIndex+","+(dataColSize+1)+");\">");
                    	out.println("<img src='../../../resource/images/cancel.png' style='width:12px;height:12px;' title='清除维度信息' onclick=\"clearComment("+rowIndex+","+(dataColSize+1)+");\">");
                    	//新增维度有关的hidden隐藏域
	                    out.println("<input type='hidden' name='colBusType"+rowIndex+(dataColSize+1)+"' id='_colBusType"+rowIndex+(dataColSize+1)+"' value='1'/>"); 
	                    out.println("<input type='hidden' name='dimLevel"+rowIndex+(dataColSize+1)+"' id='_dimLevel"+rowIndex+(dataColSize+1)+"' value=''/>"); 
	                    out.println("<input type='hidden' name='dimColId"+rowIndex+(dataColSize+1)+"' id='_dimColId"+rowIndex+(dataColSize+1)+"' value=''/>"); 
	                    out.println("<input type='hidden' name='dimTableId"+rowIndex+(dataColSize+1)+"' id='_dimTableId"+rowIndex+(dataColSize+1)+"' value=''/>"); 
	                    out.println("<input type='hidden' name='dimTypeId"+rowIndex+(dataColSize+1)+"' id='_dimTypeId"+rowIndex+(dataColSize+1)+"' value=''/>");
                    	out.println("</td>");
                    	out.println("<td style='text-align:center;' nowrap>");
                    	out.println("<img src='../../../resource/images/cancel.png' alt='删除' onclick=\"removeRow("+rowIndex+","+(dataColSize+1)+")\" style='width:16px;height: 16px;cursor: pointer'>");
	                    //out.println("<img src='../../../resource/images/move_up.png' id='_moveUp"+(dataColSize+1)+"' alt='上移' onclick=\"moveUp(this,'_moveUp','_moveDown')\" style='width:16px;height: 16px;margin-left: 5px;cursor: pointer;'>");
	                    //out.println("<img src='../../../resource/images/move_down.png' id='_moveDown"+(dataColSize+1)+"' alt='下移' onclick=\"moveDown(this,'_moveUp','_moveDown')\" style='width:16px;height: 16px;margin-left: 5px;cursor: pointer;'>");
                    	out.println("</td>");
                    	out.println("</tr>");
                     %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
var rowIndex = 1*<%=rowIndex%>;
dataRowSizes[rowIndex] = 1*<%=dataColSize%>;
$("_clumnContentDiv"+rowIndex).style.height=$("_clumnContentTable"+rowIndex).offsetHeight+"px";
subInit(rowIndex,1*dataRowSizes[rowIndex]+1);
//alert(dataTypeCombo[rowIndex][4].DOMelem_input);
<%
for(int i = 1; i <= dataColSize+1; i++){
	//out.println("alert($('_columnRow"+rowIndex+i+"'));");
	out.println("dhtmlxValidation.addValidation($('_columnRow"+rowIndex+i+"'), [");
	out.println("{target:\"_colName"+rowIndex+i+"\",rule:\"NotEmpty,MaxLength[32],ValidAplhaNumeric,ValidByCallBack[columnNameCheck&"+rowIndex+"&"+i+"]\"},");
	out.println("{target:\"_colNameCn"+rowIndex+i+"\",rule:\"MaxLength[32]\"},");
	out.println("{target:dataTypeCombo["+rowIndex+"]["+i+"].DOMelem_input,rule:\"NotEmpty,ValidByCallBack[dataTypeValidate]\"},");
	out.println("{target:\"_colBusComment"+rowIndex+i+"\",rule:\"MaxLength[1000]\"},");
	out.println("{target:\"_defaultVal"+rowIndex+i+"\",rule:\"ValidByCallBack[defaultValValidate&"+rowIndex+"&"+i+"]\"}");
	out.println("]);");
}
 %>
</script>