//	存在数据横纵转换时的滚动条冻结表头和列
function moduleTabScrollForTanrsData(tabId,obj)
{
	try
	{
	}
	catch(ex)
	{
		
	}
}
function showTabCover(tabId,type,msg)
{
	var cover=$(tabId+'_estopCover');
	if(!cover)return;
	var top=(cover.offsetHeight-150)/2;
	var left=(cover.offsetWidth-150)/2;
	cover.innerHTML="<table border=0  style='position:absolute; top:"+top+"px;left:"+left+
			"px;width:150px;height:100px;background-color:#EEEEEE;' align=left><tr>"
			+"<td width=150px ><img border=0 src='images/loading.gif' style='width:150px;height:150px;' align=absmiddle /> </td>"
			+"</tr></table>";//<td style='padding-left:15px;' id='waitDivSpan'>"+(msg?msg:"数据加载中 ")+"</td>
	if(dhtmlx._isIE)
	{
		cover.style.backgroundColor="#EEEEEE";
		cover.style.filter="alpha(opacity=50)";
	}
	if(type==0)
		cover.style.zIndex =99999999;
	else
		cover.style.zIndex=-1111;
}
//读取模块显示Tab页数据
function mbuildTabData(tabId,resetPage)
{

	//构造虚拟数据
 
}
//读取模块显示Tab页数据
function readTabData(tabId,resetPage)
{
	if(scriptRunPage && !realTimeRefreshData)//设计器的非实时 
	{
		return mbuildTabData(tabId);
	}
	var rptTab=getAllTabs(tabId);
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	var dataSrcId=rptData.dataSrcId.value;
	var sql=rptData.dataSql.value;
	var params=getMacroParams(3,rptTab);	//	条件值

	for(var i=0;i<rptTab.inParams.value.length;i++)//需要添加页面初始输入参数
		params[rptTab.inParams.value.name]=rptTab.inParams.value.value;
	var pageCfm=[privRowCount,"0",""];// int rowNUm,int curPage,String orderBy
	if(rptTab.table.autoPagination && rptTab.table.autoPagination.value>0)
	{
		if(resetPage)
			rptTab.table.currentPageNum=1;
		pageCfm[0]=rptTab.table.autoPagination.value;
		if(rptTab.table.currentPageNum && rptTab.table.currentPageNum>0)
			pageCfm[1]=rptTab.table.currentPageNum;
		else
			pageCfm[1]=rptTab.table.currentPageNum=1;
	}
	pageCfm[2]=getTableOrderbyStr(rptTab);
	if(rptTab.table.duckPartSql.value)
	{
		var duckTermSqls=rptTab.table.duckPartSql.value.split(";");
		sql=sql.replace("{DIM_DUCK_TERM_SQL}",duckTermSqls[0]);
	}
	else
	{
		sql=sql.replace("{DIM_DUCK_TERM_SQL}","");
	}
	var dsqls=[];
	dsqls[0]=sql;
	for(var i=0;i<rptData.colAttrs.value.length;i++)
	{
		var col=rptData.colAttrs.value[i];
		if(col.dimCodeTransSql.value)
		{
			if(col.dimCodeTransSql.value.trim().substr(0 ,6).toUpperCase()=="SELECT")
				dsqls[dsqls.length]=col.name.value+":"+col.dimCodeTransSql.value;
		}
	}
//	qryDataParam(String dataSrcName, String[] dsqls, Hashtable<String, String> params, int type
//			, HashMap<String, Object> columns, List<Object> dataTransRule, String[] pageCfm)
    var termAttrs=getTermAttrsMap(rptTab,params);//条件属性,到服务端二次判断权限
	var dataSrcAttrs=getDataSrcMap(rptData);//数据源信息
	if(rptTab.table.duckColName.value)
		dataSrcAttrs.colAttrs[rptTab.table.duckColName.value].duckFlag="true";
	var dataTransRule=getDataTransRuleList(rptTab);
	showTabCover(tabId,0);
	ReportDesignerAction.qryDataParam(dataSrcId,dsqls,params,termAttrs ,dataSrcAttrs,dataTransRule, pageCfm,function(res){
		var obj = res;
		clearVarVal(dataTransRule);
		clearVarVal(dataSrcAttrs); 
		clearVarVal(termAttrs); 
		showTabCover(tabId,1);
		if(!res || res[0]=="false")
		{
			if(res && res[1].indexOf("ORA-01013: user requested cancel")>=0)
			{
				alert("由于执行SQL超时，请点击右上角EXCEL导出数据进行查看，谢谢！ERROR Message="+res[1]);
				if(scriptRunPage)realTimeRefreshData=false;
			}
			else
			{
				alert("刷新加载错误："+(res?res[1]:res));
			}
			return;
		}
		//clearVarVal(rptTab.rptData);
		if(!res[2])res[2]={};//编码转换
		for(var i=0;i<rptData.colAttrs.value.length;i++)
		{
			var col=rptData.colAttrs.value[i];
			if(col.dimCodeTransSql.value)
			{
				if(col.dimCodeTransSql.value.trim().substr(0 ,6).toUpperCase()!="SELECT")
				{
					res[2][col.name.value]=getDimTransCode(col.dimCodeTransSql.value);
				}
			}
		}
		rptTab.rptData=res;	//	保存数据
		buildPagination(rptTab);
		if(scriptRunPage){
			bindTabDataPriv(rptTab.tabId,[],rptTab);
		}else{
			bindTabDataPriv(rptTab);	//	绑定下拉框预览数据
		}
	});
}
//	单表绑定表格数据
function bindTabData(rptTab,res)
{
	var tabId=rptTab.tabId;

	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	table=$(tabId+'_privTable_head');
	if(rptTab.table.dataTransRule.transFlag &&(scriptRunPage==0 || rptTab.table.dataTransRule.designerPriv)) //横纵转换
	{
		buildTransData(rptTab,rptData,res);
	}
	else
	{
		var colNames= rptTab.table.colNames.clone();
		var selCols=rptTab.table.columns.value;
		var colAttrs=[];
		var colPros=[];
		var colDisplayAttrs=[];
		var colTotal=[];
		if(table && cmpObj(table.colNames,rptTab.table.colNames))
		{
			colAttrs=table.colAttrs;
			colPros=table.colPros;
			colDisplayAttrs=table.colDisplayAttrs;
			colTotal=table.colTotal;
			for(var i=0;i<colNames.length;i++)
			{
				colTotal[i]=colPros[i].totalTxt.value?colPros[i].totalTxt.value:0;
				if(colAttrs[i].busType.value!=1)
					colTotal[i]=colTotal[i]?colTotal[i]:"-";
				colDisplayAttrs[i]="";
				if(parseInt(colPros[i].width.value))colDisplayAttrs[i]+="width:"+colPros[i].width.value+"px;";
				if(colPros[i].backColor.value)colDisplayAttrs[i]+="background-color:"+colPros[i].backColor.value+";";
			}
		}
		else
		{
			//清除数据
			if(table && table.colNames)
			{
				if(table.colAttrs)table.colAttrs.length=0;
				if(table.colPros)table.colPros.length=0;
				clearVarVal(table.colNames);
				clearVarVal(table.colDisplayAttrs);
				clearVarVal(table.colTotal);
			}
			for(var i=0;i<colNames.length;i++)
			{
				var colName=colNames[i];
				colAttrs[i]=rptData.colMap[colName];
				colPros[i]=selCols[colName];
				colTotal[i]=colPros[i].totalTxt.value?colPros[i].totalTxt.value:0;
				if(colAttrs[i].busType.value!=1)
					colTotal[i]=colTotal[i]?colTotal[i]:"-";
				colDisplayAttrs[i]="";
				if(colAttrs[i].rowUnited)colAttrs[i].rowUnited=false;
				if(parseInt(colPros[i].width.value))colDisplayAttrs[i]+="width:"+colPros[i].width.value+"px;";
				if(colPros[i].backColor.value)colDisplayAttrs[i]+="background-color:"+colPros[i].backColor.value+";";
				for (var n = 0; n < rptTab.table.rowUniteCols.value.length; n++)
				{
					if(colName==rptTab.table.rowUniteCols.value[n])
					{
						colAttrs[i].rowUnited=true;
						break;
					}
				}
			}
		}
		buildShowTable(rptTab,res,colNames,colAttrs,colPros,colDisplayAttrs,colTotal);
	}
}
//横纵转换数据显示，构造字段属性
function buildTransData(rptTab,rptData,resData)
{
	var tabId=rptTab.tabId;
	var colums=resData[0];
	var rows=resData[1];
	var transCode=resData[2];
	var colNames=getAllColNames(rptData);
	var groupCol=rptTab.table.colNames.clone();
	var gdlCols=getDataColNames(rptData,1);
	var gdlMap={};
	for(var i=0;i<gdlCols.length;i++)gdlMap[gdlCols[i].name.value]=true;
	for(var i=0;i<groupCol.length;i++)
	{
		if(gdlMap[groupCol[i]])//排除指标
		{
			groupCol.remove(i);
			i--;
			continue;
		}
		/**
		for(var n=0;n<rptTab.table.dataTransRule.value[0].length;n++)//排除转换维度
		{
			if(groupCol[i]==rptTab.table.dataTransRule.value[0][n])
			{
				groupCol.remove(i);
				i--;
				break;
			}
		}
		**/
	}

//	var table=$(tabId+'_privTable_head');
//	if(table && table.transData)
//	{
//		buildTransDataAttr(rptTab,rptData,resData,table.transData);
//	}
//	else
//	{
        //实时从服务器获取转换后数据，避免点击转换按钮无任何反应

		//服务器端转换
		ReportDesignerAction.transData(colNames,rows,groupCol,rptTab.table.dataTransRule.value[0], rptTab.table.dataTransRule.value[1],
		function(res)
		{
			if(res==null || res[0]=="false")
			{
				alert("转换数据发生错误："+(res[1]?res[1]:""));
				return;
			}
			res[0][res[2]]=rptTab.table.dataTransRule.value[2];//转换列名称
			buildTransDataAttr(rptTab,rptData,resData,res);
		});
//	}
	
}
function buildTransDataAttr(rptTab,rptData,srcData,transRes)
{
	var tabId=rptTab.tabId;
	var colums=srcData[0];//转换前的字段列
	var rows=srcData[1];//转换前的数据行
	var transCode=srcData[2];//转换前维度编码转换
	var table=$(tabId+'_privTable_head');
	var srcColNames= rptTab.table.colNames;
	var selCols=rptTab.table.columns.value;
	var colAttrs=[];
	var colPros=[];
	var colDisplayAttrs=[];
	var colTotal=[];
	//需要重新构造记录和表列头
	var colNames=transRes[0];
	var resData=[];
	//排序
	sortTableData(rptTab,colums,rows);
	if(table && cmpObj(table.colNames,colNames) && table.transData)
	{
		colAttrs=table.colAttrs;
		colPros=table.colPros;
		colDisplayAttrs=table.colDisplayAttrs;
		colTotal=table.colTotal;
		for(var i=0;i<colNames.length;i++)
		{
			colTotal[i]=colPros[i].totalTxt.value?colPros[i].totalTxt.value:0;
			if(colAttrs[i].busType.value!=1)
				colTotal[i]=colTotal[i]?colTotal[i]:"-";
			if(parseInt(colPros[i].width.value))colDisplayAttrs[i]+="width:"+colPros[i].width.value+"px;";
			if(colPros[i].backColor.value)colDisplayAttrs[i]+="background-color:"+colPros[i].backColor.value+";";
		}
		resData=table.transData;
		resData[1]=transRes[1];
	}
	else
	{
		//清除数据
		if(table && table.colNames)
		{
			//清除数据
			if(cmpObj(table.colNames,rptTab.table.colNames))
			{
				if(table.colAttrs)table.colAttrs.length=0;
				if(table.colPros)table.colPros.length=0;
				clearVarVal(table.colDisplayAttrs);
				clearVarVal(table.colTotal);
				clearVarVal(table.colNames);
				clearVarVal(table.transData);
				
			}
			else
			{
				clearVarVal(table.colNames);
				clearVarVal(table.colAttrs);
				clearVarVal(table.colPros);
				clearVarVal(table.colDisplayAttrs);
				clearVarVal(table.colTotal);
				table.colAttrs=null;
				clearVarVal(table.transData);
				table.transData=null;
			}
		}
		//resData,colNames,colAttrs,colPros,colDisplayAttrs,colTotal
		resData[0]=colNames;
		resData[1]=transRes[1];
		resData[2]=transCode;//需要添加表头编码转换
		if(rptTab.table.totalType.value==1)	//	行变列
			rptTab.table.totalType.value=2;
		else if(rptTab.table.totalType.value==2)	//	列变行
			rptTab.table.totalType.value=1;
		
		var dglColName=colNames[transRes[2]];
		if(rptTab.table.dataTransRule.value[1].length>=1)		//指标需要编码转换将列名转换成中文
		{
			transCode[dglColName]={};
			var gdls=rptTab.table.dataTransRule.value[1];
			for(var i=0;i<gdls.length;i++)
			{
				var colAttr=rptData.colMap[gdls[i]];
				var gdlName=colAttr.nameCn.value?colAttr.nameCn.value:gdls[i];	//	从字段配置中获取指标名称
				transCode[dglColName][gdls[i]]=gdlName;
			}
		}
		var dimColName=rptTab.table.dataTransRule.value[0][0];//当前只支持单维度
		var selCols=rptTab.table.columns.value;
		for(var i=0;i<colNames.length;i++)//构造转换后，新列属性
		{
			if(selCols[colNames[i]])//存在 ，表示是原始列对象 不会存在维度编码与列名相同的情况
			{
				colPros[i]=clone(selCols[colNames[i]]);	//拷贝对象,方便统一删除
				colAttrs[i]=clone(rptData.colMap[colNames[i]]);	//拷贝对象,方便统一删除
				colAttrs[i].srcIndex=i;
			}
			else //转换后的生成维度列  维度打横后的字段列   维度列 需要与指标列配置结合设置属性
			{
				colAttrs[i]=new columnAttr();
				colPros[i]=new colConfig();
				colAttrs[i].srcIndex=i;
				colAttrs[i].name.value=colNames[i];//存在维度编码转换,使用生成 colAttr方式转换
				if(transCode[dimColName] && transCode[dimColName][colNames[i]])
					colAttrs[i].nameCn.value=transCode[dimColName][colNames[i]];
				colPros[i].colName.value=colNames[i];
				colPros[i].index=colAttrs[i].srcIndex;
				colPros[i].colNameCn.value=colAttrs[i].nameCn.value;
			 	if(colNames[i]==dglColName)	//	转换后的生成维度列
				{
			 		colPros[i].totalTxt.value="-";
			 		colAttrs[i].busType.value=0;	//打横后成维度了
				}
				else	//合计设置成打横列的属性
				{
					colPros[i].totalTxt.value=selCols[dimColName].totalTxt.value;
					colAttrs[i].busType.value=1;	//打横后成指标了
				}
			}
			colDisplayAttrs[i]="";
			if(colAttrs[i].rowUnited)colAttrs[i].rowUnited=false;
			if(parseInt(colPros[i].width.value))colDisplayAttrs[i]+="width:"+colPros[i].width.value+"px;";
			if(colPros[i].backColor.value)colDisplayAttrs[i]+="background-color:"+colPros[i].backColor.value+";";
			for (var n = 0; n < rptTab.table.rowUniteCols.value.length; n++)
			{
				if((colNames[i]==rptTab.table.rowUniteCols.value[n])	//设置了行合并
				 || (colNames[i]==dglColName && rptTab.table.rowUniteCols.value[n]==dimColName))//指标列取原维度列值
				{
					colAttrs[i].rowUnited=true;
					break;
				}
			}
			colTotal[i]=colPros[i].totalTxt.value?colPros[i].totalTxt.value:0;
			if(colAttrs[i].busType.value!=1)//指标列才允许行合计   列合计直接使用指标标识确定
				colTotal[i]=colTotal[i]?colTotal[i]:"-";
		}
		if(rptTab.table.dataTransRule.value[1].length==1)
		{
			for(var i=0;i<colNames.length;i++)
				if(colNames[i]==dglColName)//生成的维度列 如果是单指标转换则不显示,需要删除
			{
			/**
				clearVarVal(colPros[i]);
				//colPros[i].remove(i);
				colPros.remove(i);
				clearVarVal(colAttrs[i]);
				colAttrs.remove(i);
				colDisplayAttrs.remove(i);
 				colTotal.remove(i);
 				colNames.remove(i);
 				break;
 				**/
			}
		}
		if(transCode[dimColName])	//	清除打横后的维度转换数据
		{
			var colTrans=transCode[dimColName];
			clearVarVal(colTrans);
			transCode[dimColName]=null;
			delete transCode[dimColName];
		}
	}
	buildShowTable(rptTab,resData,colNames,colAttrs,colPros,colDisplayAttrs,colTotal);
	table=$(tabId+'_privTable_head');
	table.transData=resData;
}
function sortTableData(rptTab,colums,rows)
{
	if((!rptTab.table.colFreeze.value || rptTab.table.colFreeze.value.length==0) 
			&& (!rptTab.table.orderColMap || !rptTab.table.orderCols))return ;
	var orderCols=rptTab.table.colFreeze.value.clone();
	orderCols=orderCols.concat(rptTab.table.orderCols);
	//	进行数据排序
}
function buildShowTable(rptTab,resData,colNames,colAttrs,colPros,colDisplayAttrs,colTotal)
{
	var sortFlag=getSortFalg(rptTab);
	var duckColName=rptTab.table.duckColName.value;
	if(duckColName)sortFlag=false;
	var colums=resData[0];
	var rows=resData[1];
	var transCode=resData[2];
	var tabId=rptTab.tabId;
	var cssName=formatTabCss(rptTab);
	var headDiv=$(tabId+'_tableHeadDiv');
	var strHtml='<table '+(cssName[0]?cssName[0]:' cellpadding="0" cellspacing="0" border="0" ')+
			' align=center class="'+rptTab.tabId.replace("@","")+'_tableBorder '+cssName[0]
			+'" id="'+tabId+'_privTable_head" style="background-color:'+rptTab.table.backGroundColor.value+';">';	//表,表头,行,交替行
	strHtml+="<THEAD><tr  id='"+tabId+"_ShowTableHead' class='"+cssName[1]+" FixedTitleRow ' style='position:relative;background-color:"+rptTab.table.tabHeadBackColor.value+";color:"
			+rptTab.table.tabHeadForeColor.value+";'>";
	if(rptTab.linkType.value==2 || sortFlag)
		loadCssText(".headLink:link {	COLOR: "+rptTab.table.tabHeadForeColor.value+";}.headLink:visited {	COLOR:  "+rptTab.table.tabHeadForeColor.value+";}.headLink:hover {	COLOR: "+rptTab.table.tabHeadForeColor.value+";}");
	if(isIE)loadCssText("."+rptTab.tabId.replace("@","")+"_tableBorder td,th{border-bottom:1px solid "+rptTab.table.backGroundColor.value+"; border-right:1px solid "+rptTab.table.backGroundColor.value+";}");
	if(!rptTab.table.colFreeze.map)
	{
		var colFreeze=rptTab.table.colFreeze.value;
		rptTab.table.colFreeze.map={};
		var selColIndex={};
		for(var c=0;c<colNames.length;c++)selColIndex[colNames[c]]=c;
		for(var c=0;c<colFreeze.length;c++)
		{
			rptTab.table.colFreeze.map[colFreeze[c]]=[];
			rptTab.table.colFreeze.map[colFreeze[c]][0]=c+1;
			rptTab.table.colFreeze.map[colFreeze[c]][1]=selColIndex[colFreeze[c]];
		}
		clearVarVal(selColIndex);
	}
	for(var i=0;i<colNames.length;i++)
	{
		var colName=colNames[i];
		var colAttr=colAttrs[i];
		var colPro=colPros[i];
		var colDisplayAttr=colDisplayAttrs[i];
		var colNameCn=colAttr.nameCn.value?colAttr.nameCn.value:colName;
		//先判断是否存在表头链接
		if(rptTab.linkType.value==2)// 表头
		{//打开报表 刷新模块   行头可能存在编码转换
			colNameCn="<a href='javascript:void(0);' class='headLink' onclick='openLinkRpt(\""+tabId+"\",\""+colName+"\");return false;'>"+colNameCn+"</a>";
		}
		else if(sortFlag)
		{
			colNameCn="<a href='javascript:void(0);' class='headLink' onclick='tabTableSorting(\""+tabId+"\",\""+colName+"\",this);return false;'>"+colNameCn+"</a>";
			if(rptTab.table.orderColMap && rptTab.table.orderColMap[colName])
			{
				if(rptTab.table.orderCols[0]==colName)
				{
					if(rptTab.table.orderColMap[colName]=="desc")
						colNameCn+="<span style='color:#FF0000'>↓</span>";
					else
						colNameCn+="<span style='color:#FF0000'>↑</span>";
				}
				else
				{
					if(rptTab.table.orderColMap[colName]=="desc")
						colNameCn+="<span style='color:#999999'>↓</span>";
					else
						colNameCn+="<span style='color:#999999'>↑</span>";
				}
			}
		}
		strHtml+="<th nowrap align=center id='"+tabId+"_headTd_"+i+"' class='paddinLeftRight5px "+(isIE && rptTab.table.colFreeze.map[colName]?"FixedTitleColumn":"")+"' style='position:relative;"+
				colDisplayAttr+"'>"+colNameCn+"</th>";
	}
 	if(rptTab.table.totalType.value==2)//行合计，需要加一列
 		strHtml+="<td nowrap align=center id='"+tabId+"_headTd_"+colNames.length+"' class='paddinLeftRight5px' style='"+colDisplayAttr+"'>合 计</td>";
	strHtml+="</tr></THEAD><tbody>";
	for( var r=0;r<rows.length;r++)
	{
		strHtml+="<tr  class='"+(r%2==0?cssName[2]:cssName[3])+"' style='background-color:"+
				(r%2==0?rptTab.table.rowBackColor.value:rptTab.table.rowAlternantBackColor.value)+
				"'  onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutR(this)'";	//tabHeadBackColor
		var dataTotal=0;
		var rowCol0Data="";
		for(var i=0;i<colNames.length;i++)
		{
			var si=colAttrs[i].srcIndex;
			var data=rows[r][si];
			var sameVals = 1;
			var duckStr="";
			if(data==null || data=="null")data="-";
			if(i==0)
				strHtml+=" id='"+tabId+"_"+r+","+data+"' >";//添加Code
			if(i==0)rowCol0Data=data;
			if (colAttrs[i].rowUnited)
			{
				if (r > 0 && rows[r-1][si]==data)
					continue;
				for (var n = r+1; n < rows.length; n++)
				{
					if (rows[n][si]==data)
						sameVals++;
					else
						break;
				}
			}
			var numVal=parseFloat(data);
			numVal=numVal==numVal?numVal:0;
			if(rptTab.table.totalType.value==2 && colAttrs[i].busType.value==1)dataTotal+=numVal;
			if(rptTab.table.totalType.value==1 && !colPros[i].totalTxt.value && colAttrs[i].busType.value==1)//列合计
				colTotal[i]+=numVal;
			var style="";
			var tdAttr="";
			if(colAttrs[i].busType.value==1)style+="text-align:right;";
			if(colPros[i].alertTerms.value.length>0)style=alertPrivDataCol(rows[r],colPros[i],colAttrs[i]);	//	一行数据用于预警计算
			if(colPros[i].dataFormart.value)data=formatPrivDataCol(data,colPros[i].dataFormart.value);
			if (sameVals > 1)tdAttr += " rowSpan=" + sameVals;
			
			if(transCode[colNames[i]])
				data=transCode[colNames[i]][data]?transCode[colNames[i]][data]:data;
			//if(rptTab.linkRptId.value!=0 || rptTab.linkModelIds.value.length || rptTab.linkType.value) 
			if(rptTab.linkType.value==1)//单元格
			{
				data="<a href='javascript:void(0);' class='bottom' onclick='openLinkRpt(\""+tabId+"\",\""+
					colNames[i]+"\",\""+rows[r][si]+"\",\""+//当前单元格值
					colNames[0]+"\",\""+rowCol0Data+//行头值
					"\");return false;'>"+data+"</a>";
			}
			else if(rptTab.linkType.value==3 && i==0)//行头
			{
				data="<a href='javascript:void(0);' class='bottom' onclick='openLinkRpt(\""+tabId+"\",\""+
					colNames[0]+"\",\""+rowCol0Data+//行头值
					"\");return false;'>"+data+"</a>";
			}
			if(i==0 && duckColName)//生成钻取连接
			{
				var indentLevel=1;
				var DUCK_LEVEL=0;
				if(duckInitLevel==1)DUCK_LEVEL=1;
				var haveDuck=true;
				if(resData[3])//返回了字段数据层级
				{
					DUCK_LEVEL=resData[3][colNames[i]];
					if(colAttrs[i].DIM_DATA_LEVELS.length && DUCK_LEVEL>=colAttrs[i].DIM_DATA_LEVELS[colAttrs[i].DIM_DATA_LEVELS.length] )
						haveDuck=false;
				}
				else
				{
					if(DUCK_LEVEL>=colAttrs[i].DIM_DATA_LEVELS.length)
						haveDuck=false;
				}
				if(haveDuck && resData[4] && !resData[4][rowCol0Data])//返回了下级标识,且无权限
				{
					haveDuck=false;
				}
				if(haveDuck)
					duckStr="<span id='"+tabId+"_plusMins_"+r+"' class='plusMinus' indentLevel="+indentLevel+" DUCK_LEVEL="+DUCK_LEVEL+" class='hand' onclick='duckTabData(this,\""+tabId+"\",\""+colNames[i]+"\",\""+rowCol0Data+"\")'>+</span>&nbsp;&nbsp;";
				else
					duckStr="<span class='plusMinus' indentLevel="+indentLevel+" DUCK_LEVEL="+DUCK_LEVEL+" ></span>&nbsp;&nbsp;";
 			}
			strHtml+="<td id='"+tabId+"_rowTd_"+r+"_"+i+"' "+tdAttr+(i==0?" nowrap ":"")+"  class='paddinLeftRight5px "+
						(isIE && rptTab.table.colFreeze.map[colNames[i]]?"FixedDataColumn":"")+"' style='"+colDisplayAttrs[i]+
						style+"'>"+duckStr+data+"&nbsp;</td>";
		}
		if(rptTab.table.totalType.value==2)//行合计，需要加一列
		{
			strHtml+="<td id='"+tabId+"_rowTd_"+r+"_"+colNames.length+"' class='paddinLeftRight5px' style='text-align:right;'>&nbsp;"+
					formatPrivDataCol(dataTotal,",###.##")+"</td>";
		}
		strHtml+="</tr>";
	}
	if(rptTab.table.totalType.value==1)//添加一行
	{
		strHtml+="<tr  class='"+(rows.length%2==0?cssName[2]:cssName[3])+"' id='"+tabId+"_"+rows.length+"_total' style='background-color:"+
				(rows.length%2==0?rptTab.table.rowBackColor.value:rptTab.table.rowAlternantBackColor.value)+
				"'  onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutR(this)'>";
		for(var i=0;i<colNames.length;i++)
		{
			var style="text-align:center;";
			if(colAttrs[i].busType.value==1)style="text-align:right;";
			if(typeof colTotal[i]=="number")
				colTotal[i]=formatPrivDataCol(colTotal[i],",###.##");
			strHtml+="<td id='"+tabId+"_rowTd_"+rows.length+"_"+i+"' "+tdAttr+"  class='paddinLeftRight5px "+(isIE && rptTab.table.colFreeze.map[colNames[i]]?"FixedDataColumn":"")+"' style='"+colDisplayAttrs[i]+
					style+"'>&nbsp;"+colTotal[i]+"&nbsp;</td>";
		}
		strHtml+="</tr>";
	}
	strHtml+="</tbody></table>";
	var tableDiv=$(tabId+'_tableDiv');
	var stop=tableDiv.scrollTop;
	var sleft=tableDiv.scrollLeft;
	headDiv.innerHTML=strHtml; 
	tableDiv.scrollTop=1;//stop;
	tableDiv.scrollTop=stop;
	tableDiv.scrollLeft=sleft;
	for(var i=0;i<colNames.length;i++)
		$(tabId+"_headTd_"+i).style.width=$(tabId+"_headTd_"+i).offsetWidth+10+"px";
	var width=0;
	for(var i=0;i<colNames.length;i++)
	{
		width+=$(tabId+"_headTd_"+i).offsetWidth;
		$(tabId+"_headTd_"+i).style.width=$(tabId+"_headTd_"+i).offsetWidth+"px";
	}
	var table=$(tabId+'_privTable_head');
	if(width < $(tabId+'_tableDiv').offsetWidth)
	{
		var headDiv=$(tabId+'_tableHeadDiv');
		headDiv.style.width="100%";
		table.style.width="100%";
		for(var i=0;i<colNames.length;i++)
			$(tabId+"_headTd_"+i).style.width=$(tabId+"_headTd_"+i).offsetWidth+"px";
	}
	$(tabId+'_tableRowDiv').style.display="none";
	if(!table.colNames || table.colNames!=colNames)
	{
		if(table.colAttrs)
			table.colAttrs.length=0;
		if(table.colPros)
			table.colPros.length=0;
		if(table.colDisplayAttrs)
			table.table.colDisplayAttrs.length=0;
		if(table.colTotal)table.colTotal.length=0;
		if(!table.colWidth)table.colWidth={};
		for(var i=0;i<colNames.length;i++)
			table.colWidth[colNames[i]]=$(tabId+"_headTd_"+i).offsetWidth;
		
		table.colNames=colNames;
		table.colAttrs=colAttrs;
		table.colPros=colPros;
		table.colDisplayAttrs=colDisplayAttrs;
		table.colTotal=colTotal;
	}
	if(haveDuck && duckInitLevel==0 && rows.length==1)//钻取一级
	{
		duckTabData($(tabId+"_plusMins_0"),tabId,colNames[0], rows[0][colAttrs[0].srcIndex]+"");
	}
}
function closeDuckTable(obj,tabId,type)
{
	var table=$(tabId+'_privTable_head');
	if(type==0)//隐藏子行
	{
		var tmpTable=$(tabId+"___tmpTable_");
		if(!tmpTable)
		{
			tmpTable=document.createElement("table");
		}
		obj.setAttribute("open","false");
		obj.innerHTML="+";
		for(var i=0;i<obj.subRowIds.length;i++)
		{
			obj.subRowIds[i].style.display="none";
			var sobj=obj.subRowIds[i].cells[0].getElementsByTagName("SPAN")
			if(sobj)
				sobj=sobj[0];
			if(sobj && sobj.subRowIds)
				closeDuckTable(sobj,tabId,type);
			tmpTable.insertRow(tmpTable.rows.length).replaceNode(obj.subRowIds[i]);
		}
	}
	else
	{
		var tmpTable=$(tabId+"___tmpTable_");
		var sindex=1;
		var sTabRow=obj.parentNode.parentNode;
		sindex=sTabRow.rowIndex;
		var td=sTabRow.cells[0];
		if(td.rowSpan)
			sindex=parseInt(td.rowSpan)+sindex;
		else
			sindex+=1;
		//显示子行
		if(obj.subRowIds && obj.subRowIds.length)
		{
			obj.setAttribute("open","true");
			obj.innerHTML="-";
			for(var i=0;i<obj.subRowIds.length;i++)
			{
				obj.subRowIds[i].style.display="";
				Debug(table.rows.length);
				table.insertRow(sindex++).replaceNode(obj.subRowIds[i]);
			}
		}
		else
		{
			obj.innerHTML="";
		}
	}
}
function duckTabData(obj,tabId,duckColName,duckCode)
{
	if(obj.getAttribute("init") && obj.getAttribute("init")=="true")
	{
		if(obj.getAttribute("open") && obj.getAttribute("open")=="true")
		{
			if(obj && obj.subRowIds)
			closeDuckTable(obj,tabId,0);
		}
		else
		{
			closeDuckTable(obj,tabId,1);
		}
	}
	else
	{
		if(obj.getAttribute("init")=="loading")
		{
			alert("请等待上一次加载");
			return;
		}
		readDuckRptData(obj,tabId,duckColName,duckCode);
	}
}
//读取钻取数据
function readDuckRptData(obj,tabId,duckColName,duckCode)
{
	obj.setAttribute("init","loading");
	var rptTab=getAllTabs(tabId);
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	var dataSrcId=rptData.dataSrcId.value;
	var sql=rptData.dataSql.value;
	var params=getMacroParams(3,rptTab);	//	条件值
	for(var i=0;i<rptTab.inParams.value.length;i++)//需要添加页面初始输入参数
		params[rptTab.inParams.value.name]=rptTab.inParams.value.value;
	params[duckColName]=duckCode;
	var pageCfm=[privRowCount,"0",""];// int rowNUm,int curPage,String orderBy
	var duckTermSqls=rptTab.table.duckPartSql.value.split(";");
	if(duckTermSqls.length!=2)
	{
		alert("钻取SQl条件配置错误 ，请参考格式说明\n 初始化条件;钻取条件 例：and ZONE_CODE='0000';AND ZONE_CODE IN (select zone_code from meta_dim_zone t where t.zone_par_code='{zone_code}' and t.dim_type_id=4)");
		return;
	}
	sql=sql.replace("{DIM_DUCK_TERM_SQL}",duckTermSqls[1]);
	var dsqls=[];
	dsqls[0]=sql;
	for(var i=0;i<rptData.colAttrs.value.length;i++)
	{
		var col=rptData.colAttrs.value[i];
		if(col.dimCodeTransSql.value)
		{
			if(col.dimCodeTransSql.value.trim().substr(0 ,6).toUpperCase()=="SELECT")
				dsqls[dsqls.length]=col.name.value+":"+col.dimCodeTransSql.value;
		}
	}
  	var termAttrs=getTermAttrsMap(rptTab,params);//条件属性,到服务端二次判断权限
	var dataSrcAttrs=getDataSrcMap(rptData);//数据源信息
	dataSrcAttrs.colAttrs=getDataSrcColsMap(rptData);
	if(rptTab.table.duckColName.value)
		dataSrcAttrs.colAttrs[rptTab.table.duckColName.value].duckFlag="true";
	
	var dataTransRule=getDataTransRuleList(rptTab);
	ReportDesignerAction.qryDataParam(dataSrcId,dsqls,params,termAttrs,dataSrcAttrs,dataTransRule,null,function(res){
		clearVarVal(dataTransRule);
		clearVarVal(dataSrcAttrs); 
		clearVarVal(termAttrs); 
		if(!res || res[0]=="false")
		{
			obj.setAttribute("init","false");
			if(res && res[1].indexOf("ORA-01013: user requested cancel")>=0)
			{
				alert("由于执行SQL超时，请点击右上角EXCEL导出数据进行查看，谢谢!ERROR Message="+res[1]);
				if(scriptRunPage)realTimeRefreshData=false;
			}
			else
			{
				alert("刷新加载错误："+(res?res[1]:res));
			}
			return;
		}
		if(!res[2])res[2]={};//编码转换
		for(var i=0;i<rptData.colAttrs.value.length;i++)
		{
			var col=rptData.colAttrs.value[i];
			if(col.dimCodeTransSql.value)
			{
				if(col.dimCodeTransSql.value.trim().substr(0 ,6).toUpperCase()!="SELECT")
				{
					res[2][col.name.value]=getDimTransCode(col.dimCodeTransSql.value);
				}
			}
		}
		bindTabDataDuckPriv(obj,rptTab,res);	//	绑定下拉框预览数据
	});
}
function bindTabDataDuckPriv(obj,rptTab,resData)
{
	var colums=resData[0];
	var rows=resData[1];
	if(rows.length==0)
	{
		obj.innerHTML="";
		obj.setAttribute("init","true");
		return;
	}
	var transCode=resData[2];
	var tabId=rptTab.tabId;
	var table=$(tabId+'_privTable_head');
	var cssName=formatTabCss(rptTab);
	var colNames=table.colNames;
	var colAttrs=table.colAttrs;
	var colPros=table.colPros;
	var colDisplayAttrs=table.colDisplayAttrs;
	var colTotal=table.colTotal;
	for(var i=0;i<colTotal.length;i++)
	{
		colTotal[i]=colPros[i].totalTxt.value?colPros[i].totalTxt.value:0;
		if(colAttrs[i].busType.value!=1)
			colTotal[i]=colTotal[i]?colTotal[i]:"-";
	}
	var sindex=1;
	var sTabRow=obj.parentNode.parentNode;
	var DUCK_LEVEL=parseInt(obj.getAttribute("DUCK_LEVEL"))+1;
	var indentLevel=parseInt(obj.getAttribute("indentLevel"))+1;
	var rowLength=table.rows.length;
//	for(var i=0;i<rowLength;i++)
//	{
//		if(sTabRow==table.rows[i])
//		{
//			var td=sTabRow.cells[0];
//			if(sTabRow.cells.length==colNames.length && td.rowSpan)
//				sindex=parseInt(td.rowSpan)+i;
//			else
//				sindex=i;
//			break;
//		}
//	}
	sindex=sTabRow.rowIndex;
	var td=sTabRow.cells[0];
	if(td.rowSpan)
		sindex=parseInt(td.rowSpan)+sindex;
	else
		sindex+=1;
//	Debug(sindex);
	var indentStr="";
	for(var i =1;i<indentLevel;i++)
		indentStr+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	var subRowIds=[];
	for( var r=0;r<rows.length;r++)
	{
		var tr=table.insertRow(sindex);
		tr.setAttribute("sindex",sindex++);
		tr.className=(sindex%2==0?cssName[2]:cssName[3]);
		tr.style.backgroundColor=(sindex%2==0?rptTab.table.rowBackColor.value:rptTab.table.rowAlternantBackColor.value);
		var dataTotal=0;
		var rowCol0Data="";
		var colLen=0;
		for(var i=0;i<colNames.length;i++)
		{
			var si=colAttrs[i].srcIndex;
			var data=rows[r][si];
			var sameVals = 1;
			var duckStr="";
			if(i==0)
			{
				tr.id=tabId+"_"+(rowLength+r)+","+data;//添加Code
				subRowIds[subRowIds.length]=tr;
			}
			if(i==0)rowCol0Data=data;
			if (colAttrs[i].rowUnited)
			{
				if (r > 0 && rows[r-1][si]==data)
					continue;
				for (var n = r+1; n < rows.length; n++)
				{
					if (rows[n][si]==data)
						sameVals++;
					else
						break;
				}
			}
			var td=tr.insertCell(colLen++);
			td.noWrap=true;
			if(rptTab.table.totalType.value==2 && colAttrs[i].busType.value==1)dataTotal+=parseFloat(data);
			if(rptTab.table.totalType.value==1 && !colPros[i].totalTxt.value && colAttrs[i].busType.value==1)//列合计
				colTotal[i]+=parseFloat(data);
			if(colAttrs[i].busType.value==1)td.style.textAlign="right";
			if(colPros[i].alertTerms.value.length>0)alertPrivDataCol(rows[r],colPros[i],colAttrs[i],td);	//	一行数据用于预警计算
			if(colPros[i].dataFormart.value)data=formatPrivDataCol(data,colPros[i].dataFormart.value);
			if (sameVals > 1)td.rowSpan=sameVals;
			
			if(transCode[colNames[i]])
				data=transCode[colNames[i]][data]?transCode[colNames[i]][data]:data;
			//if(rptTab.linkRptId.value!=0 || rptTab.linkModelIds.value.length || rptTab.linkType.value) 
			if(rptTab.linkType.value==1)//单元格
			{
				data="<a href='javascript:void(0);' class='bottom' onclick='openLinkRpt(\""+tabId+"\",\""+
					colNames[i]+"\",\""+rows[r][si]+"\",\""+//当前单元格值
					colNames[0]+"\",\""+rowCol0Data+//行头值
					"\");return false;'>"+data+"</a>";
			}
			else if(rptTab.linkType.value==3 && i==0)//行头
			{
				data="<a href='javascript:void(0);' class='bottom' onclick='openLinkRpt(\""+tabId+"\",\""+
					colNames[0]+"\",\""+rowCol0Data+//行头值
					"\");return false;'>"+data+"</a>";
			}
			if(i==0)//生成钻取连接
			{
				var haveDuck=true;
				if(resData[3])//返回了字段数据层级
				{
					DUCK_LEVEL=resData[3][colNames[i]];
					if(colAttrs[i].DIM_DATA_LEVELS.length && DUCK_LEVEL>=colAttrs[i].DIM_DATA_LEVELS[colAttrs[i].DIM_DATA_LEVELS.length] )
						haveDuck=false;
				}
				else
				{
					if(DUCK_LEVEL>=colAttrs[i].DIM_DATA_LEVELS.length)
						haveDuck=false;
				}
				if(haveDuck && resData[4] && !resData[4][rowCol0Data])//返回了下级标识,且无权限
				{
					haveDuck=false;
				}
				if(haveDuck)
					duckStr=indentStr+"<span class='plusMinus' indentLevel="+indentLevel+" DUCK_LEVEL="+DUCK_LEVEL+" class='hand' onclick='duckTabData(this,\""+tabId+"\",\""+colNames[i]+"\",\""+rowCol0Data+"\")'>+</span>&nbsp;&nbsp;";
				else
					duckStr=indentStr+"<span class='plusMinus' indentLevel="+indentLevel+" DUCK_LEVEL="+DUCK_LEVEL+" ></span>&nbsp;&nbsp;";
			}
			td.id=tabId+"_rowTd_"+(rowLength+r)+"_"+i;
			td.className="paddinLeftRight5px";
			td.innerHTML=duckStr+data;
//			strHtml+="<td    style='"+colDisplayAttrs[i]+
		}
		tr.onmouseover=function(){tr_onmouseover(event.srcElement);};
		tr.onmouseout=function(){tr_onmouseoutR(event.srcElement);};
		if(rptTab.table.totalType.value==2)//行合计，需要加一列
		{
			var td=tr.insertCell(colNames.length);
			td.id=tabId+"_rowTd_"+r+"_"+colNames.length;
			td.className="paddinLeftRight5px";
			td.style.textAlign="right";
			td.innerHTML=formatPrivDataCol(dataTotal,",###.##");
		}
	}
	if(rptTab.table.totalType.value==1)//添加一行
	{
		var tr=table.insertRow(sindex);
		tr.setAttribute("sindex",sindex++);
		subRowIds[subRowIds.length]=tr;
		tr.id=tabId+"_"+(rowLength+rows.length)+"_total";
		tr.className=(sindex%2==0?cssName[2]:cssName[3]);
		tr.style.backgroundColor=(sindex%2==0?rptTab.table.rowBackColor.value:rptTab.table.rowAlternantBackColor.value);
		tr.onmouseover=function(){tr_onmouseover(event.srcElement);};
		tr.onmouseout=function(){tr_onmouseoutR(event.srcElement);};
		for(var i=0;i<colNames.length;i++)
		{
			var td=tr.insertCell(i);
			td.style.textAlign="center";
			if(colAttrs[i].busType.value==1)
				td.style.textAlign="right";
			if(typeof colTotal[i]=="number")
				colTotal[i]=formatPrivDataCol(colTotal[i],",###.##");
			td.id=tabId+"_rowTd_"+(rowLength+rows.length)+"_"+i;
			td.className='paddinLeftRight5px';
			td.innerHTML=colTotal[i];
//			style='"+colDisplayAttrs[i]+
		}
	}
	for(var i=0;i<colNames.length;i++)
		$(tabId+"_headTd_"+i).style.width=table.colWidth[colNames[i]];
	obj.subRowIds=subRowIds;
	obj.setAttribute("init","true");
	obj.setAttribute("open","true");
	obj.innerHTML="-";
}
function formatPrivDataCol(data,formart)
{
	if(formart)
	{
		try
		{
			return data=formatNumber(data,{pattern:formart});
		}
		catch(ex)
		{
			return data;	
		}
	}
	else
	{
		return data;
	}
}
function alertPrivDataCol(row,colPro,colAttr)
{
	return "";
}
//图形展示
function bindGraphPriv(rptTab)
{
	var tabId=rptTab.tabId;
	var graphDiv=$(tabId+'_graphDiv');
	var stop=graphDiv.scrollTop;
	var sleft=graphDiv.scrollLeft;
	graphDiv.scrollTop=stop;
	graphDiv.scrollLeft=sleft;//图形
	return "";
}
//打开关联报表或者刷新关联模块列表
function openLinkRpt(tabId,colName,value)
{
	
}

