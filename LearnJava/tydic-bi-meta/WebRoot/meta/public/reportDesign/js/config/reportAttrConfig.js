
var dataSourceList=[];
//报表数据源配置
function rptSrcDataCfm()
{
	this.type="rptSrcDataCfm";
	//报表数据源
//	this.dataId=0;
	this.tabId="";
	this.dataName={readonly:dataReadOnly,type:"txt",label:"数据源名称",value:"",explain:"数据源名称"},
	this.dataSrcType={readonly:dataReadOnly,type:"sel",readOnly:true,label:"来源类型",value:0,
					list:[{value:0,text:"直接SQL"},{value:1,text:"指标组合SQL"}
					,{value:2,text:"表配置传入SQL"},{value:3,text:"Web Service"},{value:4,text:"外部 URL"}],explain:"数据源来源类型"},			//来源类型 0:直接SQL 1:指标组合SQL 2：表输入SQL 3:web service
	this.dataSrcId={readonly:dataReadOnly,type:"sel",label:"来源数据库",value:"",list:dataSourceList,explain:"设置模块显示数据来源"};			//	数据源类型,  非0:数据源ID JS中数据源名称        0:URL 直接打开   
 	this.dataSql={readonly:dataReadOnly,type:"txt",label:"查询 SQL",value:"",explain:"数据源数据查询SQL"},		//主数据查询SQL
	this.mainDataTab={readonly:dataReadOnly,type:"txt",label:"主数据表",value:"",explain:"主数据来源表名称"},	//主数据来源表
	this.colAttrs={readonly:dataReadOnly,type:"list",label:"字段列表",value:[],vp:"array",text:"name",
			explain:"从SQL中解析字段名称,列属性[英文名,中文名,index,业务类型，数据类型] 业务类型： 0:属性， 1 维度，2指标"};		
			//从SQL中解析字段名称 列属性   colName:[中文名,index,业务类型，数据类型]  0:属性， 1 维度，2指标
	//	指标解释来源类型，0：指标解释，通过字段名称从指标表查询     1:指定指标解释内容
	this.indexExegesisContent={readonly:dataReadOnly,type:"mulTxt",label:"附加指标解释内容",value:"",
			explain:"格式：指标名称1:指标解释;指标名称2:指标解释; 名称和内容中不能有半角冒号':' 和分号';'"};//	指标解释        指标名称1:指标解释;指标名称2:指标解释;
	this.TABLE_ID=0;
	this.gdlExp={};
}
function getAllColNames(rptData)
{
	var colNames=[];
	for(var i=0;i<rptData.colAttrs.value.length;i++)
		colNames[i]=rptData.colAttrs.value[i].name.value;
	return colNames;
}
//字段属性配置
function columnAttr()
{
	this.srcIndex=-1;
	this.name={readonly:dataReadOnly,type:"txt",label:"字段名称",value:""};
	this.nameCn={readonly:dataReadOnly,type:"txt",label:"字段中文名称",value:""};
	this.busType={readonly:dataReadOnly,type:"sel",label:"业务类型",list:[{value:0,text:"维度"},{value:1,text:"指标"},{value:2,text:"属性标识"}],value:2};
	this.dbType={readonly:dataReadOnly,type:"sel",label:"数据类型",list:[{value:0,text:"数字"},{value:1,text:"字符"}],value:0};
	this.indexExegesis={readonly:dataReadOnly,type:"txt",label:"业务解释",value:""};//	指标解释        指标名称1:指标解释;指标名称2:指标解释;
	this.dimCodeTransSql={readonly:dataReadOnly,type:"txt",label:"维度编码转换",value:""};
	this.DIM_TABLE_ID=0;
	this.DIM_TYPE_ID=0;
	this.DIM_DATA_LEVELS=[];
	this.GDL_ID=0;
	this.TABLE_ID=0;
	this.COL_ID=0;
	this.GROUP_METHOD=0;
}
//获取数据源维度列信息 、 指标列信息
function getDataColNames(rptData,type)
{
	var res=[];
	if(typeof rptData =="string")
		rptData=reportConfig.dataCfms.value[rptData];
	if(!rptData)return res;
	var columns=rptData.colAttrs.value;
	if(type==-1)return columns;
	for(var i=0;i<columns.length;i++)
	{
		var col=columns[i];
		if(col.busType.value==type )	//	维度列 多维度层级数据 && col.dimDataLevels.value.length>=1
		res[res.length]=col;
	}
	return res;
}
function indexExegesis()
{
	this.name={type:"txt",label:"指标名称",value:""};
	this.gdlDesc={type:"txt",label:"业务解释",value:""};
	this.valueOf=function()
	{
		return this.name.value+":"+this.gdlDesc.value;
	}
}
function reportTerm()
{
	this.tabId="";
	this.type="reportTerm";
	this.termName={readonly:dataReadOnly,type:"txt",label:"名称标签",value:"",explain:"条件名称 显示标签，唯一标识"};
	this.parTermName={readonly:dataReadOnly,type:"list",label:"父级条件",value:"",explain:"父级条件名称，下拉框级联，联动依赖"};
	this.termType={readonly:dataReadOnly,type:"sel",label:"输入类型",value:0,
			list:[{value:0,text:"下拉框"},{value:1,text:"下拉树"},{value:2,text:"时间框"}],explain:"条件录入和展现类型"};
			//	条件类型 条件类型 0:下拉框  1：下拉树   2:时间选择器
	this.valueType={readonly:dataReadOnly,type:"sel",label:"值类型",value:1,
			list:[{value:0,text:"数字"},{value:1,text:"文本"}],explain:"条件值数值类型"};			//	0:数字   1字符
	this.valueColName={readonly:dataReadOnly,type:"txt",label:"值字段宏名称",value:"",explain:"英文名，用于数据查询时的宏条件替换，必需唯一"};		//	值字段名称  英文名，用于宏替换
	this.textColName={readonly:dataReadOnly,type:"txt",label:"标签字段宏名称",value:"",explain:"英文名，用于数据查询时的宏条件替换，必需唯一"};		//	文本字段名称  英文名，用于宏替换
	this.showLength={type:"num",label:"显示长度",value:"120",explain:"设置条件框显示长度"};		//	显示长度
	this.srcType={readonly:dataReadOnly,type:"sel",label:"来源类型",value:1,
			list:[{value:1,text:"SQL查询语句"},{value:0,text:"固定值列表"}],explain:"下拉框备选值来源类型"};				//	数据来源类型  
	this.data={readonly:dataReadOnly,type:"txt",label:"值来源或列表",value:"",explain:"一行一个选项 各字段值用\',\'分隔，字段顺序：value,text；"+
			"可执行的SQL查询语句,字段顺序：select value,text from tb,可以带依赖条件的宏变量，或者自身宏变量$"+
			"一行一个选项 各字段值用\',\'分隔，字段顺序：value,text,parValue；"+
			"可执行的SQL查询语句,字段顺序：select value,text,parValue from tb，可以带依赖条件的宏变量，或者自身宏变量,可以使用二个语句实现异步树加载例如：select val,txt from tab;select val,txt from tab where parVal={val} $"+
			"填最小值--最大值[min-max],例如：20120101-20120201"};			//	SQL或者值列表  
	this.dataSrcId={readonly:dataReadOnly,type:"sel",label:"数据库",value:"",list:[],explain:"数据来源数据库"};	//数据源ID
	this.defaultValue={type:"txt",label:"默认值",value:[],vp:"array",explain:"页面初始化时的条件默认值，不设置时：下拉框默认为第一个选项，时间框默认为当前时间"};		//	默认值或者默认选择值
	this.appendText={readonly:dataReadOnly,type:"txt",label:"附加选项标签",value:"",explain:"列表附加选项标签，例如：所有，全部"};		//	附加选项  在下拉框时有效
	this.appendValue={readonly:dataReadOnly,type:"txt",label:"附加选项值",value:"",explain:"列表附加选项值"};			//	附加选项  在下拉框时有效
	this.DIM_TABLE_ID=0;
	this.DIM_TYPE_ID=0;
	this.DIM_TABLE_NAME="";
	this.dimDataLevels={readonly:dataReadOnly,type:"list",label:"维度层级",value:[]};//维度表ID，层级列表
	
}
function reportModule()
{
	this.type="reportModule";
	this.modId="";
	this.moduleName={type:"txt",label:"模块标题",value:"",explain:"模块标题"};			//	模块名称
	//窗口属性
	this.window={
		type:"tree",
		top:{type:"num",label:"",value:0,explain:"设置窗口X轴位置,水平与左上角距离"},
		left:{type:"num",label:"",value:0,explain:"设置窗口Y轴位置,,垂直与左上角距离"},
		width:{type:"num",label:"",value:0,explain:"设置窗口宽度"},
		height:{type:"num",label:"",value:0,explain:"设置窗口高度"},
		resizable:{type:"chk",label:"",value:true,explain:"设置窗口是否可改变大小"},//true,
		isMovable:{type:"chk",label:"",value:false,explain:"是否允许拖动窗口位置"},//false,
		isModal:{type:"chk",label:"",value:false,explain:"是否设为模态窗口"},//false,
		stick:{type:"chk",label:"",value:false,explain:"是否突出显示窗口"},
		hideHeader:{type:"chk",label:"",value:false,explain:"是否隐藏窗口标题"},//false,	//控制
//		closeHide:{type:"chk",label:"",value:false,explain:"是否隐藏窗口右上方关闭按钮"},//false,	//控制
		minHide:{type:"chk",label:"",value:true,explain:"是否隐藏窗口右上方最小化按钮"},//true,		//控制
		maxHide:{type:"chk",label:"",value:true,explain:"是否隐藏窗口右上方最小化按钮"},//true,		//控制
		helpBt:{type:"chk",label:"",value:true,explain:"是否显示窗口右上方帮助按钮"},//false,		//帮助按钮   显示全部指标解释，备注等
		bgcolor:{type:"color",label:"",value:"#FFFFFF",explain:"模块页面默认背景色"},
		lockDim:{tyle:"chk",label:"锁定大小位置",value:false,explain:"在报表展现时是否锁定模块的大小和位置"},
		win:null,
		tabBar:null
	};
	this.setSelf=function()
	{
		var pos=this.window.win.getPosition();
		this.window.left.value=pos[0];
		this.window.top.value=pos[1];
		pos=this.window.win.getDimension();
 		this.window.width.value=pos[0];
		this.window.height.value=pos[1];
		this.moduleName.value=this.window.win.getText();
		this.window.resizable.value=this.window.win.isResizable();
		this.window.isMovable.value=this.window.win.isMovable();
		this.window.isModal.value=this.window.win.isModal();
		this.window.stick.value=this.window.win.isSticked();
//		this.window.closeHide.value=this.window.win.button("close").isHidden();
		this.window.minHide.value=this.window.win.button("park").isHidden();
		this.window.maxHide.value=this.window.win.button("minmax1").isHidden();
		this.window.helpBt.value=!(this.window.win.button("help").isHidden());
	};
	this.tabPannel={tyle:"chk",label:"是否多标签页",value:false,explain:"是否在模块内使用TAB标签页面"};
	this.tabCount=0;
//	this.tabPannel={type:"chk",label:"是否多标签页",value:false,explain:"是否在模块内使用TAB标签页面"};
this.tabs={
		type:"tree"
	};	//	reportModuleTab()
	
	this.initTabBar=function()
	{
		var tabLen=this.tabs.length;
	}
}
function paramConfig()
{
	name="";
	dbtype="";
}
function reportModuleTab()
{
	this.type="reportModuleTab";
	this.modId="";
	this.win=null;
	this.tabBar=null;
	this.tabId="";
	this.tabName={type:"txt",label:"TAB页名称",value:"",explain:"tab标签页名称"};
	this.termNames={type:"list",label:"条件名称列表",value:[],vp:"array",text:"termName",explain:"模块的查询条件名称列表"};			//模块查询条件名称列表
	this.termRowSize={type:"num",label:"一行显示条件数",value:0,explain:"设置在报表中一行显示的条件个数,默认0：全部一行显示"};			//全部一行
	this.dataSrcName={type:"sel",label:"数据源",value:"",list:[],explain:"设置模块显示数据来源"};		//  dataSrcId!=0 数据源ID  dataSrcId==0   url 地址     地图也封装成URL地址，并附加权限
	this.inParams={type:"txt",label:"输入参数",value:[],vp:"array",text:"name",explain:"输入参数名称列表,参数间使用逗号分隔"};			//输入参数列表 		配置参数依赖
	this.outParams={type:"list",label:"输出参数",value:[],vp:"array",text:"name",explain:"模块的输出参数列表，用于级联和钻取的输入参数 数据源为数据库时自动生成,为URL时填写回调函数及传递参数 "};	//输出参数列表 		dataSrcId!=0 自动生成  dataSrcType==0    回调函数及传递参数
	this.downLoadFlag={type:"chk",label:"下载标识",value:true,explain:"是否支持下载，是否允许浏览用户下载数据"};		//	是否支持下载
	this.linkRptId={type:"label",label:"钻取报表",value:0,explain:"钻取报表ID，默认为当前报表，及只有模块联动，不为0时则传入输出参数打开新报表"};			//	钻取报表ID 
	this.linkModelIds={type:"combobox",label:"联动模块",value:[],list:[],vp:"array",explain:"设置报表内模块联动关系，选择要与此模块联动的模块列表"};
			//	事件联动模块列表    	//当联动的模块只有一个并且在客户列表中不存在时，在当前模块打开联动模块，下标添加返回按钮
	this.linkOpenType={type:"sel",label:"钻取打开方式",value:1,text:"",list:[{value:0,text:"当前窗口"},{value:1,text:"弹出窗口"}],
						explain:"设置钻取报表的打开方式"};		//	0:当前窗口 1:弹出窗口 
	this.linkType={type:"sel",label:"链接方式",value:0,text:"",list:[{value:0,text:"无"},{value:1,text:"单元格"},{value:2,text:"表头"},{value:3,text:"行头"}],
						explain:"设置钻取报表打开的链接方式：<br/>单元格:每个数据单元格链接打开<br/>列头:表格表头链接打开<br/>行头:表格第一列数据行链接打开"};		//	联动类型 	 showType=0	0:单元格  1:列头  2:行头    showType=1 0:值点,1:图例 2:标签
	this.showFlag={readonly:dataReadOnly,type:"chk",label:"显示标识",value:true,explain:"设置为不显示时只能做为联动模块打开，开发人员可以添加隐藏模块用于块下钻"};			//	显示标识  开发人员可以添加隐藏模块用于块下钻
	this.showType={type:"sel",label:"显示类型",value:0,text:"",list:[{value:0,text:"表格"},{value:1,text:"图形"},{value:2,text:"表格+图形"}],explain:"设置模块TAB页数据展示类型"};;			//	0:表格,1:图表
	//表格属性
	this.table={
		type:"tree",
		collapse:false,
			columns:{readonly:dataReadOnly,type:"list",label:"显示字段属性配置",value:{},vp:"map",list:[],text:"colName",explain:"字段展现属性配置,包括背景色、格式、预警等"},		
			//colName:colConfig()  字段配置 
			dataTransRule:{readonly:dataReadOnly,type:"list",label:"横纵转换规则",value:[],vp:"array",
					explain:"数据转换规则 实现纵表到横表数据的转换,打横字段  转列字段 列名"},
			//合并规则
			rowUniteCols:{type:"combobox",label:"记录合并字段",value:[],list:[],explain:"设置数据行头合并列表，例如：month_no,latnName "},	//	行头合并列表  month_no,latnName    
			colUniteRule:{type:"list",label:"表头合并规则",value:"",list:[],explain:"设置数据列合并列表，例如：月份;日期;用户数:3G用户数,上年3G用户数;渗透率;增长:年累计增长,月累计增长,当日增长"},	
			//	列合并规则  2$ 月份;日期;用户数:3G用户数,上年3G用户数;渗透率;增长:年累计增长,月累计增长,当日增长
			//3$  月份;日期;
			//合计规则 
			totalType:{type:"sel",label:"数据合计规则",value:0,text:"",list:[{value:0,text:"不合计"},{value:1,text:"列合计"},{value:2,text:"行合计"}],
						explain:"设置钻取报表的打开方式"},
			colFreeze:{type:"combobox",label:"列冻结字段",value:[],list:[],explain:"设置数据列冻结字段列表，例如：month_no,latnName "},	//	列冻结字段  month_no,latnName    
			autoPagination:{type:"sel",label:"分页设置",value:0,list:[{value:0,text:"不分页"},{value:20,text:"每页20条"},{value:50,text:"每页50条"},{value:100,text:"每页100条"},{value:500,text:"每页500条"}],explain:"数据是否分页显示"},
//			colTotal:{type:"chk",label:"记录汇总",value:true,explain:"是否添加合计汇总行"},		//	COLLECT_FLAG 汇总标识 0不汇总  1汇总  多层级数据展现不支持汇总
			duckColName:{readonly:dataReadOnly,type:"sel",label:"树形钻取字段",value:"",text:"",list:[],
						explain:"树形钻取字段，必需是维度字段"},
			duckPartSql:{readonly:dataReadOnly,type:"mulTxt",rows:5,label:"钻取SQL条件",value:"",explain:"钻取条件SQL,用分号分隔 初始化数据SQL;钻取条件SQL,替换SQL语句中的宏{DIM_DUCK_TERM_SQL}"},
			width:{type:"txt",label:"表格宽度",value:"100%",explain:"设置展示表格宽度，百分比或者像素值"},//"100%",
			height:{type:"txt",label:"表格高度",value:"100%",explain:"设置展示表格的高度，百分比或者像素值"},
			backGroundColor:{type:"color",label:"网格线颜色",value:"#ADC7EF",explain:"设置表格背景色，网格线颜色"},//"#ADC7EF",			//	表格背景色，网格线颜色 
			tabHeadBackColor:{type:"color",label:"标题背景色",value:"#A2D4DA",explain:"表头背景色 标题行背景色"},
//					{type:"color",label:"二级背景色",value:"#E0E7F0",explain:"合并表头时下级标题行背景色"}],//"#E0E7F0,#ADC7EF",//	表头背景色 标题行背景色    合并表头则为  E0E7F0,ADC7EF
			tabHeadForeColor:{type:"color",label:"表头字体颜色",value:"#000000",explain:"设置表头前景色，字体颜色"},//"#000000",		//	表头前景色  文本颜色 
			rowBackColor:{type:"color",label:"表格行背景色",value:"#E4F0FC",explain:"数据行背景色"},//"#D3D5FF",			//	数据行背景色
			rowAlternantBackColor:{type:"color",label:"交替行背景色",value:"#FFFFFF",explain:"交替数据数据行背景色"},//"#E3E5FF",//	交替数据数据行背景色
			cssName:{type:"txt",label:"样式表",value:"",explain:"设置表格CSS样式  \"表,表头,行,交替行\" 四个CSS名称  "}//[],		//	自定义复杂CSS样式     表,行,交替行
	};
	//图表属性
	this.graph={
		type:"tree",
		collapse:true,
		columns:{type:"list",label:"显示字段属性配置",value:{},vp:"map",list:[],text:"colName",explain:"字段展现属性配置,包括背景色、格式、预警等"},		
		dataTransRule:{readonly:dataReadOnly,label:"数据转换规则",value:"",explain:"数据转换规则 实现纵表到横表数据的转换"},
			
		graphType:0,		//	图形类型   图形类型 1-9:饼图
							//,11-30:XY图 线|柱
							//,31-40:Meters and Gauges 仪表 计量
							//,41-50:Gantt图
							//,51-60:Area图
							//,61-70:Polar Charts 雷达图,
							//71-80:Zoomable and Scrollable Charts,
							//81-90:Realtime Charts
		graphParams:"" 		//	图形显示控制参数集合
			//图形预警先不考虑
	};
}
function tableHead()
{
	this.groups=[];		//	二维数组  一行标识一个合并表头  为空则与前面合并
	this.color=[];		//	多行标题背景色
}
//字段配置   列宽 背景色 预警规则
function colConfig()
{
	this.type="tree",
	this.index=0,
	this.colName={type:"label",label:"列名",value:0,explain:"字段名"},//字段列宽
	this.colNameCn={type:"label",label:"中文名",value:"",explain:"字段名"},//中文名称，与数据源做引用关联
	this.totalTxt={type:"combo",label:"列合计",value:"",list:[{value:"-",text:"-"}],explain:"列合计时显示的特殊文本"};//为空则执行sum
	this.width={type:"num",label:"列宽",value:0,explain:"设置列显示宽度"},//字段列宽
	this.backColor={type:"color",label:"背景色",value:"",explain:"设置列背景色"},	//	背景色
	//指标用下面的属性
	this.dataFormart={type:"sel",label:"值格式",value:"",text:"",list:[],vp:"array",explain:"字段格式化字符串  ###.00  0表示一定有，#可有可否"},//	字段格式化字符串  colName:"###.##",
	this.linkFlag={type:"chk",label:"链接标识",value:false,explain:"对应列是否可链接到指定报表，报表ID在表显示链接属性中设置"},
//	this.alertFlag={type:"chk",label:"预警标识",value:0,text:"",list:[{value:1,text:"前景色"},{value:2,text:"背景色"}],explain:"预警标识   1前景 2背景"},	//	预警标识   1背景 2前景
	this.alertTerms={type:"list",label:"字段预警属性",value:[],vp:"array",text:"",explain:" [['{aaa}>0','0xFFDeAE','alertFlags'],['{aaa}>0','0xFF0000','alertFlags']]"};	//	[[条件,颜色值],[条件,颜色值]]  
}
function colAlert()
{
	this.colAlertTerm={type:"txt",label:"条件",value:"",explain:"条件表达式"};//字段列宽
	this.foreColor={type:"color",label:"前景颜色",value:"",explain:"预警颜色"}//字段列宽
	this.backColor={type:"color",label:"背景颜色",value:"",explain:"预警颜色"}//字段列宽
	this.valueOf=function(){return "\""+this.colAlertTerm.value+"\",\""+this.foreColor.value+"\",\""+this.backColor.value+"\"";}
}
function txtFont()
{
	this.type="tree",
	this.size={type:"num",label:"大小",value:"12",explain:"设置字体显示大小"};
	this.color={type:"color",label:"颜色",value:"#020202",explain:"设置字体颜色"};
	this.bold={type:"chk",label:"粗体",value:"false",explain:"加粗字体"};
};
function reportTitle()
{	//报表标题
	this.label="报表标题";
	this.type="reportTitle";
	this.id={type:"label",label:"",value:"",explain:"此对象的唯一标识"}, 
	this.title={type:"txt",label:"报表标题",value:"报表标题",explain:"标题"},
	this.showFlag={type:"chk",label:"是否显示",value:true,explain:"设置报表标题是否显示"},
	this.height={type:"num",label:"高度",value:"30",explain:"设置标题栏高度"};
	this.align={type:"sel",value:"center",label:"水平对齐",list:[{value:"left",text:"左对齐"},
									{value:"center",text:"居中"},
									{value:"right",text:"右对齐"}],explain:"设置报表标题水平对齐方式"},
	this.bgcolor={type:"color",label:"背景颜色",value:"#FFFFFF",explain:"设置标题栏背景颜色"};
	this.font=new txtFont();
	this.cssName={type:"txt",label:"CSS样式名称",value:"",explain:"设置标题栏CSS样式名称"};
};
//报表元素各对象
var reportConfig={
	
	type:"reportConfig",
	rptTitle:new reportTitle(),
	termNames:{type:"list",label:"条件列表",value:[],vp:"array",text:"termName",explain:"报表全局的查询条件名称列表"},//模块查询条件名称列表,
	termRowSize:{type:"num",label:"一行显示条件数",value:0,explain:"设置在报表中一行显示的条件个数,默认0：全部一行显示"},			//全部一行
	modules:{type:"list",label:"模块列表",value:[],vp:"array",text:"moduleName",explain:"报表全局的模块名称列表"},		//	模块		reportModule()
	dataCfms:{type:"list",label:"数据源列表",value:{},vp:"map",text:"dataName",explain:"报表全局的数据源名称列表"},	//	数据源配置       dataName:rptSrcDataCfm()
	termCfms:{type:"list",label:"条件集合",value:{},vp:"map",text:"termName",explain:"报表全局的条件列表"},		//	报表查询条件 termName:reportTerm()
	termCount:0,
	dataCount:0,
	maxDataCount:5,
	reportDesc:{type:"mulTxt",label:"报表描述",value:"",rows:5,explain:"报表描述，搜索关键字。"},
	styleCssText:{type:"mulTxt",label:"样式表",value:"",rows:10,explain:"报表全局的样式表内容，各单独样式设置优先于此样式表格式。"},		//自定义复杂CSS样式
	REPORT_ID:rptId,	//0:新增
	USER_ID:userId
};
var filterKeys={
	myCalendar:true
	,win:true
	,rptData:true
	,combo:true
	,dhxTree:true
	,colWidth:true
	,colMap:true
//	,currentPageNum:true
};
function toJsonFilter(key, value)
{
	if(filterKeys[key])return null;
	if(typeof value === 'function')
	{
		value=value.toString().replace(/\/\/.*/g, '');//去掉函数内注释
		value=eval("value="+value);
	}
	if(value &&((value.hasOwnProperty && value.hasOwnProperty('tagName')) || value.tagName))return null;
	return value;
}
var dataFormatPattern=[",###","0.00%","##0.00",",###$",",###.00"];
 