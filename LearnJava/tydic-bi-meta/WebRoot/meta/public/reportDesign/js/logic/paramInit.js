var startdt=new Date();

dhtmlXCalendarObject.prototype.langData["zh"] = {
    dateformat: '%Y%m%d',//2011-08
    monthesFNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthesSNames: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
    daysFNames: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
    daysSNames: ["日", "一", "二", "三", "四", "五", "六"],
    weekstart: 7
}
//设计器生成的配置初始化修改报表
function rptModificationInit(res)
{
	clearVarVal(reportConfig,toJsonFilter);
	reportConfig=eval("rpt="+res[1]);;
	reportConfig.REPORT_ID=rptId;
	if(debug)
	{
		var dt2=new Date();
		var m=(dt2.getHours()-startdt.getHours())*60+dt2.getMinutes()-startdt.getMinutes();
		var s=m*60+dt2.getSeconds()-startdt.getSeconds();
		var mm=s*1000+dt2.getMilliseconds()-startdt.getMilliseconds();
		var st=(mm/1000);
		Debug("完成配置读取用时："+st + "s");
	}
	for(var term in reportConfig.termCfms.value)
	{
		var rptTerm=reportConfig.termCfms.value[term];
		if(typeof rptTerm.defaultValue.value=="string")
			rptTerm.defaultValue.value=rptTerm.defaultValue.value.split(",");
		if(rptTerm.termType.value=="2")
		{
			var dt2=new Date();
			var d=parseInt(rptTerm.defaultValue.value[0]);
			if(!d)d=-1;
			dt2.setDate(dt2.getDate()+d);
			rptTerm.defaultValue.value[0]=dt2.getFullYear()+"-"+(dt2.getMonth()+1)+"-"+dt2.getDate();
		}
	}
	var tabs=getAllTabs();
	for(var i=0;i<tabs.length;i++)
	{
		var rptTab=tabs[i];
		rptTab.table.currentRowCounts && delete rptTab.table.currentRowCounts;
		rptTab.table.currentPageCount && delete rptTab.table.currentPageCount;
		rptTab.table.currentPageNum && delete rptTab.table.currentPageNum;
		rptTab.table.orderDircetion && delete rptTab.table.orderDircetion;
		rptTab.table.orderByColumn && delete rptTab.table.orderByColumn;
	}	
	parDataInited=true;
	return;
	rptParamCfg.REPORT_ID=rptId;
	clearVarVal(reportConfig.rptTitle);
	reportConfig.rptTitle=rptParamCfg.rptTitle;
	rptParamCfg.rptTitle=null;
	//报表条件对象
	clearVarVal(reportConfig.termNames);
	reportConfig.termNames=rptParamCfg.termNames;
	rptParamCfg.termNames=null;
	//数据源集合
	clearVarVal(reportConfig.dataCfms);
	reportConfig.dataCfms=rptParamCfg.dataCfms;
	rptParamCfg.dataCfms=null;
	//条件集合
	clearVarVal(reportConfig.termCfms);
	reportConfig.termCfms=rptParamCfg.termCfms;
	rptParamCfg.termCfms=null;
	
	reportConfig.termCount=rptParamCfg.termCount;
	reportConfig.maxDataCount=rptParamCfg.maxDataCount;
	reportConfig.maxDataCount=rptParamCfg.maxDataCount;
	
	reportConfig.reportDesc.value=rptParamCfg.reportDesc.value;	
	reportConfig.styleCssText.value=rptParamCfg.styleCssText.value;
	reportConfig.termRowSize.value=rptParamCfg.termRowSize.value;
	//报表模块
	clearVarVal(reportConfig.modules);
	reportConfig.modules=rptParamCfg.modules;
	rptParamCfg.modules=null;
	//报表模块页面
	
	/*
	 
  	modules:{type:"list",label:"模块列表",value:[],vp:"array",text:"moduleName",explain:"报表全局的模块名称列表"},		//	模块		reportModule()
 
	*/
}
//模型模式进入设计器
function rptModelInit(res)
{
	var modRptCfm=res[1];
	reportConfig.rptTitle.id.value=rptTitleConerId;
	reportConfig.rptTitle.font.size.value=16;
	reportConfig.rptTitle.height.value=30;
	reportConfig.rptTitle.title.value=modRptCfm[0].REPORT_NAME;
	reportConfig.reportDesc.value=modRptCfm[0].REPORT_NOTE;
	reportConfig.REPORT_ID=modRptCfm[0].REPORT_ID;
	if(modRptCfm[0].USER_ID!=reportConfig.USER_ID)	//引用复制
		reportConfig.REPORT_ID=0;	//	新增
	
	//添加条件
	var termLen=1;
	for(var i=0;i<modRptCfm[1].length;i++)
	{
		var term=modRptCfm[1][i];
		var rptTerm=new reportTerm();
		rptTerm.tabId="termData"+termLen++;
		reportConfig.termCfms.value[rptTerm.tabId]=rptTerm;
		reportConfig.termCount++;

		rptTerm.termName.value=term.LABEL_NAME;
		rptTerm.dataSrcId.value=term.DATA_SOURCE_ID;
		if(term.COL_DATATYPE=="NUMBER")	//	数据类型
			rptTerm.valueType.value=0;
		else
			rptTerm.valueType.value=1;
		rptTerm.valueColName.value=term.COL_NAME;
		rptTerm.textColName.value=term.COL_NAME_CN;
		rptTerm.DIM_TABLE_ID=term.DIM_TABLE_ID;
		rptTerm.DIM_TYPE_ID=term.DIM_TYPE_ID;
		rptTerm.DIM_TABLE_NAME=term.DIM_TABLE_NAME;
		if(term.DIM_LEVELS)
		rptTerm.dimDataLevels.value=term.DIM_LEVELS.split(",");
		rptTerm.showLength.value=100;	//	长度
		rptTerm.defaultValue.value=term.QUERY_DEFAULT.split(",");
		rptTerm.srcType.value=1;		//	查询数据来源类型
 		rptTerm.data.value="";
		
		var dimMaxLevel=rptTerm.dimDataLevels.value[rptTerm.dimDataLevels.value.length-1];
		if(term.DIM_LEVELS && dimMaxLevel)
			rptTerm.dimDataLevels.dimMaxLevel=dimMaxLevel;
		var dimPriStr=term.TABLE_DIM_PREFIX.toUpperCase();
		var dimOrderStr=" order_id,"+dimPriStr+"_CODE";
		var dimFilterStr=(term.DIM_CODE_FILTER?" and "+dimPriStr+"_CODE not in ("+term.DIM_CODE_FILTER+	")":"");
		var dt2=new Date();
		switch(term.QUERY_CONTROL)
		{
		case 1://时间维度
			if(term.DIM_LEVELS=="3")//日期
			{
				rptTerm.termType.value=2;//日期控件
				var d=rptTerm.defaultValue.value[0]?parseInt(rptTerm.defaultValue.value[0]):-1;
				dt2.setDate(dt2.getDate()+d);
				var mm=(dt2.getMonth()+1);
				mm=mm>9?mm:"0"+mm;
				var dd=dt2.getDate();
				dd=dd>9?dd:"0"+dd;
				rptTerm.defaultValue.value[0]=dt2.getFullYear()+"-"+mm+"-"+dd;
//				rptTerm.data.value=modRptCfm[0].effectDataSql;
			}
			else	//	判断是否审核 
			{
				rptTerm.termType.value=0;	//	下拉框
				var rowNum=rptTerm.defaultValue.value[1]?parseInt(rptTerm.defaultValue.value[1]):24;
				if(term.DIM_LEVELS=="2")	//	月份
				{
					var d=(rptTerm.defaultValue.value[0]!=null&&rptTerm.defaultValue.value[0]!=undefined&&rptTerm.defaultValue.value[0]!="")?parseInt(rptTerm.defaultValue.value[0]):-1;
					dt2.setMonth(dt2.getMonth()+d);
					var mm=(dt2.getMonth()+1);
					mm=mm>9?mm:"0"+mm;
					rptTerm.defaultValue.value[0]=d;
					rptTerm.defaultValue.value[1]=dt2.getFullYear()+""+mm;
					rptTerm.defaultValue.value[2]=dt2.getFullYear()+"年"+(dt2.getMonth()+1)+"月";
					rptTerm.data.value= "select * from(select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME from "+term.TABLE_NAME+
							" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr +" and dim_level=2 ";
					rptTerm.data.value+=" and "+dimPriStr+"_CODE<=to_char(add_months(sysdate,'"+rptTerm.defaultValue.value[0]+"' ),'yyyymm')";
					rptTerm.data.value+="  order by 1 desc,"+dimOrderStr+" ) where rownum<="+rowNum;	//最近一年
				}
				else
				{
					var d=(rptTerm.defaultValue.value[0]!=null&&rptTerm.defaultValue.value[0]!=undefined&&rptTerm.defaultValue.value[0]!="")?parseInt(rptTerm.defaultValue.value[0]):-1;
					dt2.setFullYear(dt2.getFullYear()+d);
					var mm=(dt2.getMonth()+1);
					mm=mm>9?mm:"0"+mm;
					rptTerm.defaultValue.value[0]=d;
					rptTerm.defaultValue.value[1]=dt2.getFullYear();
					rptTerm.defaultValue.value[2]=dt2.getFullYear()+"年";
					rptTerm.data.value= "select * from(select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME from "+term.TABLE_NAME+
							" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr +" and dim_level=1 ";
					rptTerm.data.value+=" and "+dimPriStr+"_CODE<=to_char(add_months(sysdate , '"+rptTerm.defaultValue.value[0]*12+"'),'yyyymm')";
					rptTerm.data.value+=" order by 1 desc,"+dimOrderStr+" ) where rownum<="+rowNum;	//最近一年
				}
			}
			break;	
		case 2:	//时间维度 间隔	//需要复制一个条件
			var rptTerm2=new reportTerm();
			rptTerm.valueColName.value=term.COL_NAME+"1";
			rptTerm2.valueColName.value=term.COL_NAME+"2";
			rptTerm2.tabId="termData"+termLen++;;
			reportConfig.termCfms.value[rptTerm2.tabId]=rptTerm2;
			reportConfig.termCount++;
			rptTerm2.parTermName.value=rptTerm.tabId.value;
			rptTerm2.dataSrcId.value=rptTerm.dataSrcId.value;
			rptTerm2.valueType.value=rptTerm.valueType.value;
			rptTerm2.textColName.value=rptTerm.textColName.value="";
			rptTerm2.textColName.value=rptTerm.textColName.value;
			rptTerm2.DIM_TABLE_ID=rptTerm.DIM_TABLE_ID;
			rptTerm2.DIM_TYPE_ID=rptTerm.DIM_TYPE_ID;
			rptTerm2.DIM_TABLE_NAME=rptTerm.DIM_TABLE_NAME;
			rptTerm2.dimDataLevels.value=rptTerm.dimDataLevels.value;
			rptTerm2.showLength.value=rptTerm.showLength.value;	//	长度
			rptTerm2.srcType.value=rptTerm.srcType.value;		//	查询数据来源类型
			rptTerm2.defaultValue.value[0]=rptTerm.defaultValue.value[1];
			if(term.DIM_LEVELS=="3")//日期区间
			{
				rptTerm.termType.value=2;//日期控件
				rptTerm2.termType.value=2;//日期控件
				rptTerm.termName.value="开始日期";
				rptTerm2.termName.value="结束日期";
				var d=(rptTerm.defaultValue.value[0]!=null&&rptTerm.defaultValue.value[0]!=undefined&&rptTerm.defaultValue.value[0]!="")?parseInt(rptTerm.defaultValue.value[0]):-1;
				dt2.setDate(dt2.getDate()+d);
				var mm=(dt2.getMonth()+1);
				mm=mm>9?mm:"0"+mm;
				var dd=dt2.getDate();
				dd=dd>9?dd:"0"+dd;
				rptTerm.defaultValue.value[0]=dt2.getFullYear()+"-"+mm+"-"+dd;
				d=rptTerm2.defaultValue.value[0]?parseInt(rptTerm2.defaultValue.value[0]):-1;
				dt2=new Date();
				dt2.setDate(dt2.getDate()+d);
				mm=(dt2.getMonth()+1);
				mm=mm>9?mm:"0"+mm;
				dd=dt2.getDate();
				dd=dd>9?dd:"0"+dd;
				rptTerm2.defaultValue.value[0]=dt2.getFullYear()+"-"+mm+"-"+dd;
				rptTerm.defaultValue.value.length=1;
//				rptTerm.data.value=modRptCfm[0].effectDataSql;	//查询有效时间区间
//				rptTerm2.data.value=modRptCfm[0].effectDataSql;
			}
			else if(term.DIM_LEVELS=="2")//日期区间
			{
				var rowNum=rptTerm.defaultValue.value[2]?parseInt(rptTerm.defaultValue.value[2]):24;
				rptTerm.termName.value="开始月份";
				var d=(rptTerm.defaultValue.value[0]!=null&&rptTerm.defaultValue.value[0]!=undefined&&rptTerm.defaultValue.value[0]!="")?parseInt(rptTerm.defaultValue.value[0]):-2;
				dt2.setMonth(dt2.getMonth()+d);
				var mm=(dt2.getMonth()+1);
				mm=mm>9?mm:"0"+mm;
				rptTerm.defaultValue.value[0]=dt2.getFullYear()+""+mm;
				rptTerm.defaultValue.value[1]=dt2.getFullYear()+"年"+(dt2.getMonth()+1)+"月";
				rptTerm.data.value= "select * from(select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME from "+term.TABLE_NAME+
						" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr +" and dim_level=2 ";
				rptTerm.data.value+=" and "+dimPriStr+"_CODE<="+rptTerm.defaultValue.value[0];
				rptTerm.data.value+=" order by 1 desc,"+dimOrderStr+" ) where rownum<="+rowNum;
				rptTerm.defaultValue.value.length=2;
				rptTerm2.termName.value="结束月份";
				dt2=new Date();
				d=rptTerm2.defaultValue.value[0]?parseInt(rptTerm2.defaultValue.value[0]):-1;
				dt2.setMonth(dt2.getMonth()+d);
				mm=(dt2.getMonth()+1);
				mm=mm>9?mm:"0"+mm;
				rptTerm2.defaultValue.value[0]=dt2.getFullYear()+""+mm;
				rptTerm2.defaultValue.value[1]=dt2.getFullYear()+"年"+(dt2.getMonth()+1)+"月";
				rptTerm2.data.value= "select * from(select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME from "+term.TABLE_NAME+
						" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr +" and dim_level=2 ";
				rptTerm2.data.value+=" and "+dimPriStr+"_CODE<="+rptTerm2.defaultValue.value[0];
				rptTerm2.data.value+=" order by 1 desc,"+dimOrderStr+" ) where rownum<="+rowNum;
				rptTerm2.defaultValue.value.length=2;
				rptTerm.defaultValue.value.length=2;
			}
			else if(term.DIM_LEVELS=="1")//日期区间
			{
				rptTerm.termName.value="开始年份";
				
				var rowNum=rptTerm.defaultValue.value[2]?parseInt(rptTerm.defaultValue.value[2]):12;
				var d=(rptTerm.defaultValue.value[0]!=null&&rptTerm.defaultValue.value[0]!=undefined&&rptTerm.defaultValue.value[0]!="")?parseInt(rptTerm.defaultValue.value[0]):-1;
				dt2.setMonth(dt2.getMonth()+d);
				var mm=(dt2.getMonth()+1);
				mm=mm>9?mm:"0"+mm;
				rptTerm.defaultValue.value[0]=dt2.getFullYear()+""+mm;
				rptTerm.defaultValue.value[1]=dt2.getFullYear()+"年"+(dt2.getMonth()+1)+"月";
				rptTerm.data.value= "select * from(select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME from "+term.TABLE_NAME+
						" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr +" and dim_level=1 ";
				rptTerm.data.value+=" and "+dimPriStr+"_CODE<="+rptTerm.defaultValue.value[0];
				rptTerm.data.value+=" order by 1 desc,"+dimOrderStr+" ) where rownum<="+rowNum;
				rptTerm.defaultValue.value.length=2;
				rptTerm2.termName.value="结束年份";
				d=(rptTerm.defaultValue.value[0]!=null&&rptTerm.defaultValue.value[0]!=undefined&&rptTerm.defaultValue.value[0]!="")?parseInt(rptTerm2.defaultValue.value[0]):-1;
				dt2.setMonth(dt2.getMonth()+d);
				mm=(dt2.getMonth()+1);
				mm=mm>9?mm:"0"+mm;
				rptTerm2.defaultValue.value[0]=dt2.getFullYear()+""+mm;
				rptTerm2.defaultValue.value[1]=dt2.getFullYear()+"年"+(dt2.getMonth()+1)+"月";
				rptTerm2.data.value= "select * from(select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME from "+term.TABLE_NAME+
						" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr +" and dim_level=1 ";
				rptTerm2.data.value+=" and "+dimPriStr+"_CODE<="+rptTerm.defaultValue.value[0];
				rptTerm2.data.value+=" order by 1 desc,"+dimOrderStr+" ) where rownum<="+rowNum;
				rptTerm2.defaultValue.value.length=2;
				rptTerm.defaultValue.value.length=2;
			}
		 	break;
		case 5:
			rptTerm.data.value = term.DIM_CODE_FILTER;
			rptTerm.termType.value=5;
			break;
		case 3:	//地域维度 
		case 4:	//其它维度
		default://兼容非法设置
			var treeLevel=1;
			if(term.QUERY_CONTROL==3)
			{
				if(rptTerm.defaultValue.value[0] && rptTerm.defaultValue.value[0].indexOf("{USER_ZONE}")>=0)
					rptTerm.defaultValue.value[0]=user_zone;
				if(rptTerm.defaultValue.value[1] && rptTerm.defaultValue.value[1].indexOf("{USER_ZONE}")>=0)
					rptTerm.defaultValue.value[1]=user_zone;
				treeLevel=2;	//	层级大于2
			}
			if(term.DIM_LEVELS.split(",").length>1 || rptTerm.dimDataLevels.value[0]>treeLevel)//层级大于1
			{
				rptTerm.termType.value=1;//下拉树
//				term.DIM_ROW_COUNTS=10;
				if((term.DIM_ROW_COUNTS>=0 && term.DIM_ROW_COUNTS<500) ||dimMaxLevel <=3)	//	一次性读取
				{
					if(term.HAVE_PAR_CODE)
					{
						rptTerm.data.value= "select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME,"+dimPriStr+"_PAR_CODE,DIM_LEVEL from "+term.TABLE_NAME+
							" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr ;
						rptTerm.data.value+=" and dim_level<="+dimMaxLevel+" order by 3,"+dimOrderStr;
					}
					else
					{
						rptTerm.data.value="select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME,"+
							"nvl((select "+dimPriStr+"_CODE from "+term.TABLE_NAME+" a where a."+dimPriStr+
							"_ID=t."+dimPriStr+"_par_id),0) "+dimPriStr+"_par_code,DIM_LEVEL from "+term.TABLE_NAME+
							" t where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr;
						rptTerm.data.value+=" and dim_level <="+dimMaxLevel+" order by 3,"+dimOrderStr;
					}
				}
				else	//异步加载
				{
					if(term.HAVE_PAR_CODE)
					{
						rptTerm.data.value= "select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME,DIM_LEVEL from "+term.TABLE_NAME+
							" where "+dimPriStr+"_par_id =0 and  dim_type_id="+term.DIM_TYPE_ID;
						rptTerm.data.value+=+"; select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME,DIM_LEVEL from "+term.TABLE_NAME+
							" where "+dimPriStr+"_PAR_CODE ='{"+term.COL_NAME+"}' and dim_type_id="+term.DIM_TYPE_ID ;
						rptTerm.data.value+=+" and dim_level <="+dimMaxLevel+" order by "+dimOrderStr;
					}
					else
					{
						rptTerm.data.value= "select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME,DIM_LEVEL from "+term.TABLE_NAME+
							" where "+dimPriStr+"_par_id =0 and dim_type_id="+term.DIM_TYPE_ID ;
						rptTerm.data.value+="; select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME,DIM_LEVEL from "+term.TABLE_NAME+
							" where "+dimPriStr+"_par_id =(select "+dimPriStr+"_id from "+term.TABLE_NAME+
							" a where a."+dimPriStr+"_code='{"+term.COL_NAME+"}' and dim_type_id="+term.DIM_TYPE_ID ;
						rptTerm.data.value+=" and dim_level <="+dimMaxLevel+") and dim_level <="+dimMaxLevel+" order by "+dimOrderStr;
					}

				}
			}
			else
			{
				rptTerm.srcType.value=1;		//	查询数据来源类型
				rptTerm.defaultValue.value[2]=rptTerm.defaultValue.value[1];
				rptTerm.defaultValue.value[1]=rptTerm.defaultValue.value[0];
				rptTerm.defaultValue.value[0]=-1;
				rptTerm.data.value="select "+dimPriStr+"_CODE,"+dimPriStr+"_NAME from "+term.TABLE_NAME+
						" where dim_type_id="+term.DIM_TYPE_ID+dimFilterStr;
				rptTerm.data.value+=" and dim_level="+term.DIM_LEVELS+" order by "+dimOrderStr;
			}
			break;
		}
	}
	//构造数据源
	var rptData=new rptSrcDataCfm();
 	rptData.tabId="srcData1";
	reportConfig.dataCfms.value[rptData.tabId]=rptData;
 	rptData.dataName.value=modRptCfm[0].TABLE_NAME_CN;
	reportConfig.dataCount++;
	rptData.dataSrcType.value=initType;
	rptData.dataSrcId.value=modRptCfm[0].DATA_SOURCE_ID;
	rptData.TABLE_ID=modRptCfm[0].TABLE_ID;
	rptData.mainDataTab.value=modRptCfm[0].TABLE_NAME;
	rptData.IS_LISTING=modRptCfm[0].IS_LISTING;
	//构造模块
	AddRptModel();	//	自动添加了一页面
	var rptTab=getAllTabs();//构造Tab
	rptTab=rptTab[0];
	
	for(var termId in reportConfig.termCfms.value)
	{
		var term=reportConfig.termCfms.value[termId];
		term.parent=rptTab.tabId;
		rptTab.termNames.value[rptTab.termNames.value.length]={};
		rptTab.termNames.value[rptTab.termNames.value.length-1].value=termId;
		rptTab.termNames.value[rptTab.termNames.value.length-1].termName=term.termName.value;
	}
	var dimCols=[];
	var outIdMap={};
	for(var i=0;i<modRptCfm[2].length;i++)
	{
		var col=modRptCfm[2][i];
		var dCol=rptData.colAttrs.value[i]=new columnAttr();
		dCol.name.value=col.SELECT_NAME;
		dCol.nameCn.value=col.COLUMN_NAME;
		dCol.busType.value=col.COL_BUS_TYPE;//0 1 2 
		dCol.dbType.value=(col.COL_DATATYPE=="NUMBER"?0:1);
		dCol.indexExegesis.value=col.COL_BUS_COMMENT;
		dCol.dimCodeTransSql.value=col.TRANS_CODE;
		dCol.DIM_TABLE_ID=col.DIM_TABLE_ID;
		dCol.DIM_TYPE_ID=col.DIM_TYPE_ID;
		dCol.DIM_DATA_LEVELS=col.DIM_LEVELS.split(",");
		dCol.TABLE_ID=col.TABLE_ID;
		dCol.COL_ID=col.COL_ID;
		dCol.GROUP_METHOD=col.COLLECT_MOTHED;
		if(dCol.busType.value==0)dimCols[dimCols.length]=dCol.name.value;
		dCol.TOTAL_DISPLAY=col.TOTAL_DISPLAY;
		dCol.TOTAL_FLAG=col.TOTAL_FLAG;
		if(dCol.dbType.value==0)dCol.foramt="";
		if(dCol.TOTAL_FLAG)	//列合计  需要判断 是否存在纵横转换，当单指标时才合计
			rptTab.table.totalType.value=1;
		dCol.srcIndex=i;
		var colCfm=new colConfig();
		rptTab.table.columns.value[dCol.name.value]=colCfm;
		colCfm.colName.value=dCol.name.value;
		colCfm.index=dCol.srcIndex;
		colCfm.totalTxt.value=dCol.TOTAL_DISPLAY;
		colCfm.colNameCn=dCol.nameCn;
		if(col.DUCK_FLAG && dCol.DIM_DATA_LEVELS.length>1)//钻取字段
			rptTab.table.duckColName.value=dCol.name.value;
		outIdMap[col.OUTPUT_ID]=dCol.name.value;
	}
	rptTab.dataSrcName.value=rptData.tabId;
	rptTab.table.rowUniteCols.value=dimCols;
	rptTab.table.colFreeze.value[0]=rptData.colAttrs.value[0].name.value;
	//联动需要传参，必需使用未编码转换的SQL
	rptData.dataSql.value=modRptCfm[0].REPORT_SQL;
	if(rptData.IS_LISTING)rptTab.table.autoPagination.value=50;
	if(!rptTab.table.dataTransRule.value)rptTab.table.dataTransRule.value=[];
	rptTab.table.dataTransRule.value[0]=modRptCfm[0].DATA_TRANS_DIMS?modRptCfm[0].DATA_TRANS_DIMS.split(","):"";
	rptTab.table.dataTransRule.value[1]=modRptCfm[0].DATA_TRANS_GDLS?modRptCfm[0].DATA_TRANS_GDLS.split(","):"";
	rptTab.table.dataTransRule.value[2]=modRptCfm[0].DATA_TRANS_GDL_COLNAME?modRptCfm[0].DATA_TRANS_GDL_COLNAME:"指标";
	if(rptTab.table.dataTransRule.value[0])
	{
		for(var i=0;i<rptTab.table.dataTransRule.value[0].length;i++)
			rptTab.table.dataTransRule.value[0][i]=outIdMap[rptTab.table.dataTransRule.value[0][i]];		
	}
	if(rptTab.table.dataTransRule.value[1])
	{
		for(var i=0;i<rptTab.table.dataTransRule.value[1].length;i++)
			rptTab.table.dataTransRule.value[1][i]=outIdMap[rptTab.table.dataTransRule.value[1][i]];		
	}
	rptTab.table.dataTransRule.transFlag=getDataTransFlag(rptTab,0);
	rptTab.table.dataTransRule.designerPriv=rptTab.table.dataTransRule.transFlag;
	rptTab.table.duckPartSql.value=modRptCfm[0].DIM_DUCK_TERM_SQL;
	parDataInited=true;
}
//返回是否使用数据横纵转换
function getDataTransFlag(rptTab,type)
{
	if(type==0)
	{
		if(!rptTab.table.dataTransRule || !rptTab.table.dataTransRule.value)return false;
		if(rptTab.table.dataTransRule.value.length<2)return false;
		if(!rptTab.table.dataTransRule.value[0] || !rptTab.table.dataTransRule.value[0].length || !rptTab.table.dataTransRule.value[0][0])return false;
		if(!rptTab.table.dataTransRule.value[1] || !rptTab.table.dataTransRule.value[1].length || !rptTab.table.dataTransRule.value[1][0])return false;
		return true;
	}
	return false;
}
//指标模式进入设计器
function rptGdlInit(res)
{
	parDataInited=true;
}
