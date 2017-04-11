<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
	<head>
		<title>首页内容</title>

		<%@include file="../../public/head.jsp"%>
		<%@include file="../../public/include.jsp"%>
		<script type="text/javascript" src="<%=path %>/dwr/interface/PortalCtrlr.js"></script>
		<script type="text/javascript" src="<%=path %>/dwr/interface/MenuVisitLogAction.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/TableMouse.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/formatNumber.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/Drag.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/FusionCharts.js"></script>
		<link href="images/tab.css" rel="stylesheet" type="text/css" />

		<link type="text/css" rel="stylesheet" href="<%=path %>/portal/resource/css/base_portal.css">
		<style type="text/css">
			.bt{
				background-image: url(./images/bt_bg.jpg);
			 	text-align: middle;
				padding-top: 3px;
				border:0;
			}
			.groupLine
			{
			background-image:url(images/line_bot.gif);
			}
			.pad5
			{
				padding-left:5px;
				padding-right:5px;
			}
			.pad10
			{
				padding-left:10px;
				padding-right:10px;
			}
			DIV.Head_TD{	
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:#D0E8FF;
				font-weight:bold;
				color: #000000;	
			}
			.Head_TD 
			{	
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:#D0E8FF;
				font-weight:bold;
				color: #000000;	
			}
			.Row_TD
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
			    background-color:#ffffff;
			}
			DIV.Row_TD
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
			    background-color:#FAFBFF;
			}
			.Alt_Row_TD{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:White;
			}
			DIV.Alt_Row_TD
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:White;
			}
			.red
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #FF0000;
			}
			.unl
			{
				text-decoration: underline;
				color:#0000FF;
				cursor:pointer;
			}
				.data_grid {
			        border-collapse:collapse;
			        border-spacing:0;
			        border: 1px solid #eee;
			    }
			    .data_grid td{
			        border-color: #eee;
			    }
			    .data_grid th{
			        border-color: #eee;
			        text-align: center;
			    }
			body
			{
				overflow-y:auto;
				overflow-x:hidden;
				background-color: #fff;
			}
		</style>
	</head>
	<body   >
		<div id="div_src"
			style="position: absolute; width: 10%;  z-index: 1000; top: 0px; left: 0px;
			background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;" 
			 title="" class="dragBar" >
         <center style="color:#0000FF;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;" >数据加载中，请稍等....</center>
  </div>
		<table border="0" width=100% height=100 id="table_rpt" border="0"   cellspacing="1" cellpadding="1" style="background-color: #fff;">
			<tr>
				<td bgcolor="#D9E6F7" valign=top
					style="padding-left: 15px; vertical-align: top;">
				</td>
			</tr>

			<tr>
				<td bgcolor="#D9E6F7" height=200px
					style="padding-top: 0px; vertical-align: top;" id=rpt_tabbar_td>
					<div id="rpt_tabbar" style="height: 200px;"></div>
				</td>
			</tr>
							</tr>
<tr><td height="25px" style="padding-left:10px;background-image: url(./images/index_title_bg.jpg);" id="index_title" width=100% >

</td></tr>				<tr>
				<td bgcolor="#D9E6F7" height=100>
					<div id=chartdiv></div>
				</td>
			</tr>

			<tr>
				<td bgcolor='#fffff'>
					<div id="dateSelTd"
						style="position: relative; top: 0px, left : 0px; width: 100%; height: 20px; z-index: 9999;"
						align="right">
						时间段选择：
						<input name="chartDateRadio" type=radio value="1" checked
							id="chartDateRadio1" onClick="chartDateChg()" />
						<label for="chartDateRadio1">
							一个月
						</label>
						<input name="chartDateRadio" type=radio value="2"
							id="chartDateRadio2" onClick="chartDateChg()" />
						<label for="chartDateRadio2">
							二个月
						</label>
						<input name="chartDateRadio" type=radio value="3"
							id="chartDateRadio3" onClick="chartDateChg()" />
						<label for="chartDateRadio3">
							三个月
						</label>
					</div>
				</td>
			</tr>

		</table>

	</body>
</html>
  
<script type="text/javascript">
		//全局用户数据
		var lurl=decodeURI(window.location.href);
		lurl=lurl.substring(lurl.indexOf("?")+1);
		var dataLocalCode=getQuery("localCode",lurl);
		var dataIndexTypeId=getQuery("indexTypeId",lurl);
		var dataAreaId=getQuery("areaId",lurl);
		var dataDateNo=getQuery("dateNo",lurl);	//数据时间
		var dataIndexCd=getQuery("indexCd",lurl);	//数据时间
		 
		var dataIndexName=getQuery("indexName",lurl);
		var report_level_id=getQuery("report_level_id",lurl);
		var indexFormat=parseInt(getQuery("indexFormat",lurl));
		var userIndexMenuId=1;
		var user_define_id=0;
		var moduleMenu={};	//menu_name:{TAB_ID:5,TAB_NAME:'XX',url:'xxx',dataDateNo:xx,dataLocalCode:xx,dataAreaId:xx,}
		var loadSucceed=false;
		var chartFieldName=""; //字段名
		var chartIndexCd="";//图形字段名
		var chartRadio="1";//时间连续类型
		var init_rpt_level_id=1;	//业务属性特定的
		var currentTab="";
		var tabHeight,tabWidth;
		if(window.parent)
		{
			tabHeight=window.parent.tabHeight;
			tabWidth=window.parent.tabWidth;
		}
		else
		{
		 	tabHeight=$("div_src").offsetHeight+30;
			tabWidth=$("div_src").offsetWidth;
			$("div_src").style.display="none";
			$("div_src").style.zIndex="-1";
		}
		$("rpt_tabbar").style.width=(tabWidth-20)+"px"; 
		
		function writeLog(menuId)
		{
			MenuVisitLogAction.writeMenuLog({userId:userInfo['userId'],menuId:menuId});
		}
		function page_init()
		{
			//获取当前页面所需要的数据 tab页定义，时间区间  
			PortalCtrlr.getViewTabs(userIndexMenuId,user_define_id,init_rpt_level_id,dataIndexTypeId,function(res)
			{
				if(res==null || res.length==0)
					alert("获取数据失败，或者未配置对应的菜单项。");
				if(res[0].length==0)
					alert("未配置表单项");
				for(var i=0;i<res[0].length;i++)
				{
					moduleMenu[res[0][i]["TAB_NAME"]]=res[0][i];
					moduleMenu[res[0][i]["TAB_NAME"]]["dataDateNo"]="";
					moduleMenu[res[0][i]["TAB_NAME"]]["dataLocalCode"]="";
					moduleMenu[res[0][i]["TAB_NAME"]]["dataAreaId"]="";
					moduleMenu[res[0][i]["TAB_NAME"]]["rpt_index"]=res[1][i];//指标列表
					moduleMenu[res[0][i]["TAB_NAME"]]["rpt_index_exp"]=res[2][i];//指标解释
				}
				//var tabHeight,tabWidth;//rpt_tabbar_td
				for(var key in moduleMenu)
			    {
			    	currentTab=key;
			    	
					var ik=$("rpt_tabbar");
					moduleMenu[currentTab]["div_id"]=ik.id;
					ik.innerHTML="<center>数据加载中，请稍等....<center>";
					moduleMenu[key]["div_id"]=ik.id;
					ik.innerHTML=key;
					break;
			    }
				$("div_src").style.display="none";
				$("div_src").style.Zindex="-1";
		  		checkDataFresh();
		 	});
		}
		function resetTabHeight()
		{
			var divId=moduleMenu[currentTab]["div_id"];	//	获取容器
			var div=$(divId);
			div.style.overflowY="scroll";//overflow-y: scroll;
			div.style.overflowX="auto";//hidden
		
			var tabId=moduleMenu[currentTab]["TAB_ID"];
			var ht=$('rpt_table_'+tabId).rows*21+30;
			if(ht<220)
				ht=220;
			if(ht>300)
				ht=300;
			div.style.height=ht+"px";
			$("rpt_tabbar").style.height=ht+"px";
		}
		//检查条件是变化并刷新数据 
		function checkDataFresh()
		{
			if(loadSucceed && dataLocalCode==moduleMenu[currentTab]["dataLocalCode"] &&
				dataDateNo==moduleMenu[currentTab]["dataDateNo"] &&
				dataAreaId==moduleMenu[currentTab]["dataAreaId"]) // 条件发生变化，需要刷新
			{
				resetTabHeight();
				return;
			}
			loadSucceed=false;
				
			var po={};//ReportPO
			po.indexTypeId=moduleMenu[currentTab].INDEX_TYPE_ID;
			po.localCode=dataLocalCode;
			po.areaId=dataAreaId;
			po.indexCd=dataIndexCd;
			po.reportLevelId=report_level_id;
			po.dateNo=dataDateNo;
			var rptIndexs=moduleMenu[currentTab]["rpt_index"];
			var cols=[];
			for(var i=0;i<rptIndexs.length;i++)
			{
				cols[i]=rptIndexs[i]["COL_EN_NAME"];
			}
			//alert("cols:"+cols);
			var divId=moduleMenu[currentTab]["div_id"];
			var div=$(divId);
			div.innerHTML="<center>数据加载中，请稍等....<center>";
			//writeLog(moduleMenu[currentTab].TAB_ID);
			PortalCtrlr.getTableData(po,cols,2,function(res){
				if(res==null)
				{
					alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
					return;
				}
				moduleMenu[currentTab]["dataDateNo"]=dataDateNo;
				moduleMenu[currentTab]["dataLocalCode"]=dataLocalCode;
				moduleMenu[currentTab]["dataAreaId"]=dataAreaId;
				currentTab=currentTab;
				buildGrid(res);
				loadSucceed=true;
			});
			openArea(0,0);
		}
		function buildGrid(data)
		{
		    //alert("data[i][start]6666=");
			var divId=moduleMenu[currentTab]["div_id"];	//	获取容器
			var div=$(divId);
			var rptIndexs=moduleMenu[currentTab]["rpt_index"];//COL_EN_NAME COL_CN_NAME T.INDEX_CD,T.INDEX_NAME,T.FLAG 
			var str="";
			var tabId=moduleMenu[currentTab]["TAB_ID"];
			var rptType=moduleMenu[currentTab]["RPT_TYPE"];
			var rptIndexs=moduleMenu[currentTab]["rpt_index"];
			str='<table id=rpt_table_'+tabId+' rows='+data.length+'  border="0" style="background-color:#eeeeee;border-spacing:1px;border-collapse: separate;" cellspacing="1" cellpadding="1" width='+
			(div.offsetWidth-20)+'px bgcolor="#eeeeee" class="data_grid"><thead calss=fixedHeader ><tr class=Head_TD >';
			for(var i=0;i<rptIndexs.length;i++)
			{
				var COL_EN_NAME=rptIndexs[i]["COL_EN_NAME"];
				str+="<th height='23px' class='pad5' align='center'   "+
					" width='"+parseInt(100/rptIndexs.length)+"%' id='rpt_"+tabId+"_head_"+i;
				var tdStyle="";
				if(currentTab=="过网用户" && i==5)
				{
					tdStyle+="border-left: 1px solid #75b8cf;";
				}
				if(rptType==1)
					str+="' YName='DIM_NAME' YValue='"+rptIndexs[i]["COL_CN_NAME"]+"' dim_name='"+rptIndexs[i]["COL_CN_NAME"]+"' ";
				else
					str+="' YName='INDEX_CD' YValue='"+rptIndexs[i]["INDEX_CD"]+"' dim_name='"+rptIndexs[i]["COL_CN_NAME"]+"' ";
				str+=" style='"+tdStyle+"'  >"+rptIndexs[i]["COL_CN_NAME"]+"</th>";
			}
			if(report_level_id< moduleMenu[currentTab].ROLLDOWN_LAYER)
			{
				str+="<th class='pad5' nowrap algin='center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>";
			}
			str+="</tr></thead>";
			for(var i=0;i<data.length;i++)
			{
				var groupLine="";//(data[i][data[0].length-2]==1)?"groupLine":"";
				if(i%2==0)
					str+="<tr class='Row_TD "+groupLine+"' onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutR(this)' onclick='tr_onclick(this)'>";
				else
					str+="<tr class='Alt_Row_TD "+groupLine+"' onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutA(this)' onclick='tr_onclick(this)'>";
				var start=4;
				if(rptType==1)start=4;else start=3;
				for(var j=start;j<data[0].length-2;j++)
				{
					//alert("data[i][start]000000="+data[i][start]);
				
					var index=j-4;
					index=(index>0?index:0);
					var COL_EN_NAME="";
					str+="<td id='rpt_"+tabId+"_"+i+"_"+index+"' calss='pad10' nowrap onclick='openArea("+i+","+index+")' ";
					var tdStyle="";
					if(currentTab=="过网用户" && j==5+start)
					{
						tdStyle+="border-left: 1px solid #75b8cf;";
					}
					if(j==3 || j==4)
					{
						str+=" align=left  style='padding-left:15px;' local_code='"+data[i][parseInt(report_level_id)-2]+"' index_name='"+data[i][j]+"' ";
						/* //不需要动态算类型
						if(rptType==1)
						{
							str+="XName='INDEX_CD' XValue='"+data[i][0]+"' ";
							COL_EN_NAME="";
						}
						else
						{
							str+="XName='DIM_NAME' XValue='"+data[i][j]+"' ";
						}*/
						str+=" >"+ formatDataView(data[i][j],null)+"</td>";
					}
					else
					{
						str+=" align=right  style='padding-right:10px;"+tdStyle+"' ";
						str+=" >"+ formatDataView(data[i][j],rptIndexs[index],data[i][start])+"</td>";
						//alert("data[i][start]="+data[i][j]);
					}
					if(rptType!=0 && j==3)j++;
				}
				if(report_level_id< moduleMenu[currentTab].ROLLDOWN_LAYER)
				{
					str+="<td nowrap align='center' onclick='openArea("+i+","+rptIndexs.length+
					   ")' id='rpt_"+tabId+"_"+rptIndexs.length+"' class=unl  >辖区</td>";
				}
				str+="</tr>";
			}
			str+="</table>";
			div.innerHTML=str;
			$("index_title").innerHTML=currentTab+"->"+dataIndexName+"->各地域";
		 	resetTabHeight(currentTab);
			delete str;
		}
		var ORTD;
		function setActiveTD(row,col)
		{
			if(ORTD)
			{
				ORTD.style.backgroundColor="";
				ORTD.style.color="";
			}
			if(event && event.srcElement)
			{
				ORTD=event.srcElement;
				ORTD.style.backgroundColor = "highlight";
				ORTD.style.color = "highlighttext";
			}
			
			if(col==0)
			{
				return;
			}
			var rptIndexs=moduleMenu[currentTab]["rpt_index"];
			if(col==rptIndexs.length)return;
			var tabId=moduleMenu[currentTab].TAB_ID;
			var rptType=moduleMenu[currentTab].RPT_TYPE;
			var rd=$("rpt_"+tabId+"_"+row+"_0");
			var hd=$("rpt_"+tabId+"_head_"+col);
			var rname=rd.getAttribute("index_name").replaceAll("\\#",'').replaceAll("\\$",'');
			var dname=getAttr(hd,"YValue").replaceAll("\\#",'').replaceAll("\\$",'');
			var title="";
			if(rname.indexOf("（")>0)
				rname=rname.substring(0,rname.indexOf("（"));
			else if(rname.indexOf("(")>0)
				rname=rname.substring(0,rname.indexOf("("));
			if(dname.indexOf("（")>0)
				dname=dname.substring(0,dname.indexOf("（"));
			else if(dname.indexOf("(")>0)
				dname=dname.substring(0,dname.indexOf("("));
			if(rptType==1)
			{
				title=dname+"->"+rname;
			}
			else
			{
				title=rname+"->"+dname;
			}
			$("index_title").innerHTML=currentTab+"->"+dataIndexName+"->"+title;
		}
		//单元格单击事件 
		function openArea(row,col)
		{
			//alert(row+"---"+col);
			if(col==0)
			{
				fchart(true);//获取当前级地域指标数据
				return;
			}
			setActiveTD(row,col);
			var rptIndexs=moduleMenu[currentTab]["rpt_index"];
			var tabId=moduleMenu[currentTab].TAB_ID;
			var rd=$("rpt_"+tabId+"_"+row+"_0");
			if(col==rptIndexs.length)	//	辖区钻取
			{
				var rl=parseInt(report_level_id)+1;
				var indexCd=rd.getAttribute("local_code");
				var indexName=rd.getAttribute("index_name").replaceAll("\\#",'').replaceAll("\\$",'') + dataIndexName;
				var url="/portal/module/portal/areaDrill.jsp?dateNo="+dataDateNo+"&indexTypeId="+moduleMenu[currentTab].INDEX_TYPE_ID
					+"&indexCd="+dataIndexCd+"&indexName="+indexName+"&report_level_id="+rl;
				if(report_level_id==2)
				{
					url+="&localCode="+indexCd;//+"&areaId="+dataAreaId;
				}
				else
				{
					url+="&areaId="+indexCd;//+"&areaId="+dataAreaId;
					url+="&localCode="+dataLocalCode;//+"&areaId="+dataAreaId;
				}
				url=encodeURI(url);
				openTab(tabId+dataIndexCd.toString().substring(dataIndexCd.toString().length-1,dataIndexCd.toString().length)+"_"+rl+"_"+indexCd,indexName,url,0);
				
			}
			else	//	图形刷新
			{
				var hd=$("rpt_"+tabId+"_head_"+col);
				chartFieldName=rptIndexs[col].COL_EN_NAME;
				//alert(hd.xval);
				//alert(hd.getAttribute("YName")+"_ r _"+hd.getAttribute("YValue"));
				if(hd.getAttribute("YName")=='DIM_NAME')
				{
					chartIndexCd=rd.getAttribute("index_cd");
				}
				else
				{
					chartIndexCd=hd.getAttribute("YValue");
				}
				var indexCd=rd.getAttribute("local_code");
				if(report_level_id==2)
				{
					dataLocalCode=indexCd;//+"&areaId="+dataAreaId;
					dataAreaId="";
				}
				else
				{
					dataAreaId=indexCd;
				}
				var indexName=rd.getAttribute("index_name").replaceAll("\\#",'').replaceAll("\\$",'');
				var url="/portal/module/portal/areaDrill.jsp?dateNo="+dataDateNo+"&indexTypeId="+moduleMenu[currentTab].INDEX_TYPE_ID
					+"&indexCd="+dataIndexCd+"&indexName="+indexName+"&report_level_id="+rl;
				chartIndexCd=dataIndexCd;
				chartRadio=1;
				$("chartDateRadio1").checked=true;
				fchart();
				//获取数据并设置图例：		
			}
		}
		function fchart(init)
		{
			var indexTypeId=moduleMenu[currentTab].INDEX_TYPE_ID;
			var reportLevelId=report_level_id;
			var localCode=dataLocalCode;
			var areaId=dataAreaId;
			if(init)
			{
				$("dateSelTd").display="none";
				//String[] fieldName, String indexTypeId, String REPORT_LEVEL_ID, String dateNo,String indexCd, String LOCAL_CODE
				var rptIndexs=moduleMenu[currentTab]["rpt_index"];
				PortalCtrlr.getAreaData(rptIndexs[1]["COL_EN_NAME"],indexTypeId,reportLevelId,dataDateNo,dataIndexCd,localCode,function(res){
					if(res==null)
					{
						alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
						return;
					}
					$("chartdiv").style.width=($("rpt_tabbar").offsetWidth-20)+"px";
					$("chartdiv").style.height="200px";
					var chart = new FusionCharts("<%=path%>/portal/resource/swf/FusionCharts/MSColumn2D.swf", "ChartId", ($("rpt_tabbar").offsetWidth-20)+"", "200");
					//chart.setDataURL("Data/MSLine.xml");
					chart.setDataXML(res); 
					chart.render("chartdiv");
				});
				return;
			}
			$("dateSelTd").display="";
			// String indexTypeId,String reportLevelId,String indexCd,
			// String areaId,String localCode,String fieldName,int radio,String dateNo
			
			PortalCtrlr.getTableChart(indexTypeId,reportLevelId,chartIndexCd,dataLocalCode,dataAreaId,chartFieldName,chartRadio,dataDateNo,function(res)
			{
				if(!res)
				{
					alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
					return;
				}
				var w=$("rpt_tabbar").offsetWidth-20;
				$("chartdiv").style.width=w+"px";
				$("chartdiv").style.height="200px";
				var chart = new FusionCharts("<%=path%>/portal/resource/swf/FusionCharts/MSLine.swf", "ChartId", w+"", "200");
				//chart.setDataURL("Data/MSLine.xml");	
				chart.setDataXML(res[0]); 
				chart.render("chartdiv");
			});
		} 
		function formatDataView(data,colAttr,rowName)
		{
			if(data==null)
				return "-";
			//alert(typeof data);
			//#$我的e家
			var styleData;
			if(colAttr)
			{
				//re = new RegExp('\\#',"g");
				//data=data.replace(re,'&nbsp;&nbsp;&nbsp;&nbsp;');
				if(data=='-88888888')
					styleData='-';
				else
				{
					styleData="dataNum";
					var fl=parseInt(data)<0;
					if(fl)
					{
						styleData="<span class=red >dataNum</span>";
						data=data*-1;
					}
					var formats="#,###.##";
					
					data=formatNumber(data,{pattern:formats});
					if(indexFormat || colAttr["COL_CN_NAME"].indexOf("\\%")>0 || rowName.indexOf("\\%")>0 )
					{
						data+="%";
					}
					if(fl)data="-"+data;
					styleData=styleData.replace("dataNum",data);
				}
			}
			else
			{
				re = new RegExp('\\#',"g");
				var fl=data.indexOf("\\$")>0;
				styleData=data.replace(re,'&nbsp;&nbsp;&nbsp;&nbsp;');
				if(fl)
					styleData=styleData.replace("\\$","<img src='./images/jianhao.gif' border=0 value=1 onclick='changCol()' />");
				
			}
			return styleData;
		}
		function changCol()
		{return;
			var el=event.srcElement;
			if(el.value==1)
			{
				el.value=0;
				el.src='./images/jiahao.gif';
			}
			else
			{
				el.value=1;
				el.src='./images/jianhao.gif';
			}
		}
		function tipHit(flag)
		{
			var div_src=$("div_src");
			var el=event.srcElement;
			if(el && el.id=="div_src" && event.button)
			{
				event.returnValue=false;
				return;
			}
			if(flag )
			{
				div_src.style.display="none";
				div_src.style.zIndex="-1";
		 		return;
			}
			if(el.id=="div_src")
				return;
			var index_exp=moduleMenu[currentTab]["rpt_index_exp"];
			var indexName="";
			var indexNum=-1;
		
			if(el.id.indexOf("_head")>0)
			{
				indexName=el.getAttribute("dim_name");
			}
			else
			{
				indexName=el.getAttribute("index_name");
			}
			if(!indexName)return;
			var _indexName=indexName.replaceAll("\\#","").replaceAll("\\$","").trim();
			
			if(_indexName.indexOf("(")>0 || _indexName.indexOf("（")>0)
			{
				if(_indexName.indexOf("(")>0)
					_indexName=_indexName.substring(0,_indexName.indexOf("(")).trim();
				else
					_indexName=_indexName.substring(0,_indexName.indexOf("（")).trim();
			}
			for(var i=0;i<index_exp.length;i++)
			{
				if(index_exp[i]["INDEX_NAME"]==indexName)
				{
					indexNum=i;
				}
				else if(index_exp[i]["INDEX_NAME"]==_indexName)
				{
					indexName=_indexName;indexNum=i;
				}
			}
			if(indexNum==-1)	return;
			div_src.innerHTML=index_exp[indexNum]["INDEX_NAME"]+":<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
					HTMLEncode(index_exp[indexNum]["INDEX_EXPRESS"]);
		
			//显示指标解释
			div_src.style.display="";
			div_src.style.width="400px";
			div_src.style.height="200px";
			div_src.style.zIndex="1000";
			div_src.style.oveflowY="scroll";
			div_src.style.oveflowX="hidden";
			//div_src.style.left=event.x;
			//div_src.style.top=event.y;
			setdiv("div_src");
			if(el.id!="div_src")
			{
				//div_src.style.left=parseInt(div_src.style.left)+el.offsetWidth;
			}
			// t.index_cd,d.index_name,d.index_express 
		}
		function chartDateChg()
		{
			if(chartRadio!=event.srcElement.value)
			{
				chartRadio=event.srcElement.value;
				fchart();
			}
		}
		    
		window.onload = function ()
		{
		    page_init();
		}
		function openTab(menu_id,menu_name,url,type,formname)
		{
			return parent.openMenu(menu_name,url,'top');
		}
</script>
    
