var meta = meta || new Object();
meta.table= new Object();

meta.table.DataTable=new function(tableId,columns,hidCols)
{
	this.tableId=tableId;	//	唯一标识ID
	this.tableInnerHtml='';	//	
	this.bindData=null;		//	绑定数据
	this.desFlag=0;			//	是否销毁绑定数据
	this.div=null;			//	绑定层
	this.pageingFlag=true;	//	分页标识，是否显示分页条
	this.sorting=true;		//	表头是否排序
	this.Page={
		currPageNum:1, 		//当前页号
		allRowCount:0,		//	总记录数
		allPageCount:0,			//	总页面数
		currPageRowCount:20 	//当前页面记录数
		};
	this.Sort={
		orderColMap:{},			//	排序字段方向
		orderCols:[],			//	排序字段列表
		reserveCount:3 			//	保留排序字段数
		};
	this.columnNames=columns;	//	中文显示名称
	this.columnCols=[];			//	数组保持显示顺序
	this.showCol={};			//	显示标识
	if(columns)
	{
		for(var col in columns)
		{
			this.columnNames[this.columnNames.length]=col;
			this.showCol[col]=true;
		}
	}
	this.setHiddenCols(hidCols);
}
meta.table.DataTable.prototype.enabledSorting=function(flag)
{
	this.sorting=flag;
}
meta.table.DataTable.prototype.enabledSorting=function(flag)
{
	this.PageingFlag=flag;
}
meta.table.DataTable.prototype.setHiddenCols=function(hidCols)
{
	if(typeof hidCols=="string")
	{
		var cols=hidCols.split(",");
		for(var i=0;i<cols.length;i++)
			this.showCol[cols[i]]=false;
	}
	else if(typeof hidCols=="object" && hidCols.join)
	{
		for(var i=0;i<hidCols.length;i++)
			this.showCol[hidCols[i]]=false;
	}
}
meta.table.DataTable.prototype.bind=function(div)
{
	div.style.overflow="auto";
	this.div=div;
}
//callFun(i) i column index
meta.table.DataTable.prototype.buildTableHeadStr=function(callFun)
{
	
}
//callFun(i,rowData)
meta.table.DataTable.prototype.buildTableRowStr=function(callFun)
{
	
}
//callFun(i,j,rowData)
meta.table.DataTable.prototype.buildTableRowCellStr=function(callFun)
{
	
}
/**
 * 
 * @param {Object} columns  列名map  字段名：中文名
 * @param {Object} arrayData
 * @param {Object} RowCounts
 * @memberOf {TypeName} 
 */
meta.table.DataTable.prototype.bindData=function(columns,arrayData,RowCounts)
{
	this.bindData=arrayData;
	if(desFlag)this.desFlag=desFlag;
}
meta.table.DataTable.prototype.buildT=function()
{
	
}
