/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       	termControl.js
 *Description：条件控件封装
 *
 *Dependent：
 *
 *Author:
 *        hans
 ********************************************************/

var meta = meta || new Object();
meta.term= new Object();
meta.maxInt=Math.pow(2,31);
if(!meta.uid)
meta.uid=function()
{
	if(dhx)return dhx.uid();
	return Math.random()*meta.maxInt+1;
}
meta.term.terms={};
/**
 * 条件控件对象
 * @param {Object} parentDiv  父对象，可以是容器或者input框
 * @param {Object} termName 条件名称，也是参数的KEY
 * @memberOf {TypeName} 
 */
meta.term.termControl=function(parentDiv,termName,valueChangeCall)
{
	this.termName="";//条件值名称,值宏变量名称
 	this.textName="";//文本宏变量名称
	this.termType=0;	//	0:文本框 1:下拉框 2:下拉树 3:日期控件 时间选择器
	this.parentTerm="";	//父级条件名称，下拉框级联，联动依赖
	this.valueType=1;	//条件值数值类型	0:数字   1字符串  2:日期对象
	this.termWidth=100;//条件框长度
	this.dataSrcType=0;//数据来源类型    1:SQL查询语句 0:固定值列表 
	this.dataRule=""//SQL或者值列表  
			/*根据this.dataSrcType 和 this.termType值不同，涵义不同
			 * 一行一个选项 各字段值用\',\'分隔，字段顺序：value,text； 
			 * 可执行的SQL查询语句,字段顺序：select value,text from tb,可以带依赖(父级)条件的宏变量(termName)，或者自身宏变量
			 * 一行一个选项 各字段值用\',\'分隔，字段顺序：value,text,parValue； 
			 * 可执行的SQL查询语句,字段顺序：select value,text,parValue from tb，可以带依赖条件的宏变量，或者自身宏变量
			 * 可以使用二个语句实现异步树加载例如：select val,txt from tab;select val,txt from tab where parVal={val} 
			 * 填最小值--最大值[min-max],例如：20120101-20120201 
			*/
	this.dataSrcId=0;//数据来源数据库  数据源ID
	this.appendData=null;//	附加选项值  在下拉框和下拉树时有效
	
	this.defaultValue=[];// 页面初始化时的条件默认值，不设置时：下拉框默认为第一个选项，时间框默认为当前时间 
	////////////////////////////////////////以下为维度获取数据,SQL则服务器端生成///////////////////////////////////////////////////////////
	this.initType=0;//1 维度表设置 
	this.dimTableId=0;
	this.dimTypeId=0;
	this.dimValueType=0;//维度数据类型 0:维度编码，1:维度ID
	this.dimDataLevels=[];//维度表ID，层级列表
	this.excludeValues=[];//需要排除的值	
	
	this.mulSelect=false;//是否支持多选 下拉框和树情况下 使用复选框支持	
	
	this.dynaLoad=false;//
	
	termName=termName.toUpperCase();
	this.inputObj=parentDiv;
	if(typeof parentDiv=="string")
		this.inputObj=$(parentDiv);
	if(!this.inputObj.id)this.inputObj.id=meta.uid();
	this.termName=termName;
	this.valueChangeCall=valueChangeCall;
}
meta.term.termControl.prototype.getConfig=function()
{
	var cfm={};
	cfm.termName         =this.termName         ;
	cfm.textName         =this.textName         ;
	cfm.termType         =this.termType         ;
	cfm.parentTerm       =this.parentTerm       ;
	cfm.valueType        =this.valueType        ;
	cfm.termWidth        =this.termWidth        ;
	cfm.dataSrcType      =this.dataSrcType      ;
	cfm.dataRule         =this.dataRule         ;
	cfm.dataSrcId        =this.dataSrcId        ;
	cfm.defaultValue     =this.defaultValue.join();
	cfm.initType         =this.initType         ;
	cfm.dimTableId       =this.dimTableId       ;
	cfm.dimTypeId        =this.dimTypeId        ;
	cfm.dimValueType     =this.dimValueType     ;
	cfm.dimDataLevels    =this.dimDataLevels    ;
	cfm.excludeValues 	 =this.excludeValues 	;
	cfm.mulSelect        =this.mulSelect        ;
	cfm.dynaLoad		=this.dynaLoad		;
	return cfm;
}

/**
 * 设置从维度获取数据填充下拉框或者下拉树，自动识别使用下拉框还是下拉树 同步还是异步加载
 * @param {Object} dimTableId  	维度表ID
 * @param {Object} dimTypeId	维度类型ID
 * @param {Object} dimTableName	维度表名称
 * @param {Object} dimDataLevels 需要的维度数据层级
 * @param {Object} excludeValues 需要排除的维度编码
 * @param {Object} dimValueType 维度数据类型 0:维度编码，1:维度ID
 */
meta.term.termControl.prototype.setDimRule=function(dimTableId,dimTypeId,defaultValue,dimDataLevels,excludeValues,dimValueType)
{
	this.initType=1;
	if(!dimTableId){alert("请设置正确的维度表ID");return;}
	if(!dimTypeId){alert("请设置正确的维度表ID");return;}
	this.dimTableId=dimTableId;
	this.dimTypeId=dimTypeId;
	if(defaultValue)
	{
		if(typeof defaultValue=="string" || typeof defaultValue=="number")
			this.defaultValue[0]=defaultValue;
		else if(typeof defaultValue=="object" && defaultValue.sort)
			this.defaultValue=defaultValue;
		else
			alert("请设置正确的维度条件默认值");
	}
	if(dimDataLevels)//维度表ID，层级列表
	{
		if(typeof dimDataLevels=="string" || typeof dimDataLevels=="number" )
			this.dimDataLevels[0]=parseInt(dimDataLevels);
		else if(typeof dimDataLevels=="object" && dimDataLevels.sort)
			this.dimDataLevels=dimDataLevels;
		else
			alert("请设置正确的维度条件数据层级列表");
	}
	if(excludeValues)//需要排除的值
	{
		if(typeof excludeValues=="string" || typeof defaultValue=="number")
			this.excludeValues[0]=excludeValues+"";
		else if(typeof excludeValues=="object" && excludeValues.sort)
			this.excludeValues=excludeValues;
		else
			alert("请设置正确的维度条件排除数据列表");
	}
	if(dimValueType)
		this.dimValueType=1;//维度数据类型 0:维度编码，1:维度ID
	else 
		this.dimValueType=0;//维度数据类型 0:维度编码，1:维度ID
	
	if(this.dimDataLevels.length>1 || this.dimDataLevels.length==0 
			|| parseInt(this.dimDataLevels[0])>1)//多层数据 必定为树   只有一层，且大于1 
	{
		this.termType=2;
	}
	else
	{
		this.termType=1;
	}
	if(dimTableId==1 && this.dimDataLevels[0] && parseInt(this.dimDataLevels[0])==3)	//时间维度且为日
		this.termType=3;
}
/**
 * 设置下拉框数据规则
 * @param {Object} termType
 * @param {Object} dataSrcType	数据来源类型    1:SQL查询语句 0:固定值列表 
 * @param {Object} dataRule	SQL或者值列表 select value,text from tb 或者  数组 字段顺序：value,text   可在后续列附加数据
 * @param {Object} dataSrcId 
 * @memberOf {TypeName} 
 */
meta.term.termControl.prototype.setListRule=function(dataSrcType,dataRule,dataSrcId,defaultValue)
{
	this.termType=1;
	if(dataSrcType)this.dataSrcType=dataSrcType;//数据来源类型    1:SQL查询语句 0:固定值列表 
	if(dataRule)this.dataRule=dataRule;//SQL或者值列表  
	if(dataSrcId)this.dataSrcId=dataSrcId;//数据来源数据库  数据源ID
	if(defaultValue)
	{
		if(typeof defaultValue=="string" || typeof defaultValue=="number")
			this.defaultValue[0]=defaultValue;
		else if(typeof defaultValue=="object" && defaultValue.sort)
			this.defaultValue=defaultValue;
		else
			alert("请设置正确的维度条件默认值");
	}
};
meta.term.termControl.prototype.setDateRule=function(dataSrcType,dataRule,dataSrcId,defaultValue)
{
	this.termType=3;
	if(dataSrcType)this.dataSrcType=dataSrcType;//数据来源类型    1:SQL查询语句 0:固定值列表 
	if(dataRule)this.dataRule=dataRule;//有效值区间列表或者   查询有效值区间 SQL 填最小值--最大值[min-max],例如：20120101-20120201  
	if(dataSrcId)this.dataSrcId=dataSrcId;//数据来源数据库  数据源ID
	if(defaultValue)
	{
		if(typeof defaultValue=="string" || typeof defaultValue=="number")
			this.defaultValue[0]=defaultValue;
		else if(typeof defaultValue=="object" && defaultValue.sort)
			this.defaultValue=defaultValue;
		else
			alert("请设置正确的维度条件默认值");
	}
};
meta.term.termControl.prototype.setTreeRule=function(dataSrcType,dataRule,dataSrcId,defaultValue)
{
	if(!dataRule)
	{
		alert("请设置正确的下拉树数据规则");
		return;
	}
	this.termType=2;
	if(dataSrcType)this.dataSrcType=dataSrcType;//数据来源类型    1:SQL查询语句 0:固定值列表 
		/**
		 * 二维数组 , 字段顺序：value,text,parValue； 
		 * SQL:
		 * 同步：select value,text,parValue from tb，可以带依赖条件的宏变量，或者自身宏变量
		 * 异步刷新:select val,txt from tab;select val,txt from tab where parVal={val} 
		 */
	if(typeof dataRule=="object" && dataRule.sort)
	{
		this.dynaLoad=true;
		this.dataRule=dataRule.join(";");
	}
	else if(typeof dataRule=="string")
	{
		if(dataRule.split(";").length>1)
			this.dynaLoad=true;
		this.dataRule=dataRule ;
	}
	else
	{
		alert("请设置正确的下拉树数据规则");
		return;
	}
	if(dataSrcId)this.dataSrcId=dataSrcId;//数据来源数据库  数据源ID
	if(defaultValue)
	{
		if(typeof defaultValue=="string" || typeof defaultValue=="number")
			this.defaultValue[0]=defaultValue;
		else if(typeof defaultValue=="object" && defaultValue.sort)
			this.defaultValue=defaultValue;
		else
			alert("请设置正确的维度条件默认值");
	}
};
//设置附加数据选项
meta.term.termControl.prototype.setAppendData=function(data)
{
	if(data)this.appendData=data;
}
//设置条件值
meta.term.termControl.prototype.setParentTerm=function(parentTermName)
{
	parentTermName=parentTermName.toUpperCase();
	if(meta.term.terms[parentTermName])
		this.parentTerm=parentTermName;
	else
		return false;
	return true;
}
meta.term.termControl.prototype.setValueChange=function(valueChangeCall)
{
	this.valueChangeCall=valueChangeCall;
}
/**
 * 单个使用时的初始化
 * @param {Object} callBackFun
 * @memberOf {TypeName} 
 */
meta.term.termControl.prototype.init=function(callBackFun)
{
	this.render();
	//请求服务器数据
	TermControl.getTermData(this,function(res)
	{
		//绑定对象值
		if(callBackFun)
		{
			var termVals=this.getValue();
			callBackFun(termVals);
		}
	});
}
/**
 * 绘制条件，生成对象：组合框、下拉树、日期控件等
 */
meta.term.termControl.prototype.render=function()
{
	if(!this.inputObj){alert("条件绑定对象为空，不能进行绑定绘制。");return false;}
	if(this.myCalendar)
	{
//		 this.myCalendar.i=[];
         tryEval(this.myCalendar.unload);
	}
	if(this.combo)
	{
		this.combo.clearAll(true);
		destructorDHMLX(this.combo);
		this.combo=null;delete this.combo;
	}
	if(this.selTree)
	{
		this.selTree.trBx.style.display="none";
		var trBx=rptTerm.selTree.trBx;
		this.selTree.destructor();//destructorDHMLX(this.selTree);
		this.selTree=null;
		if(trBx)document.body.removeChild(trBx);
	}
	if(this.termType!=1 && this.inputObj.tagName!="INPUT")	//下拉框使用combo实现
	{
		var el=$(this.inputObj.id+"_input");
		if(!el)
		{
			el= document.createElement("INPUT");
			el.id=this.inputObj.id+"_input";
		}
        el.termControl = this;
        el.type = "text";
        el.style.width = this.termWidth+"px";
        this.inputObj.appendChild(el);
        this.inputObj=el;
	}
	switch(this.termType)//0:文本框 1:下拉框 2:下拉树 3:日期控件 时间选择器
	{
	case 0:	//	文本框不需要做处理
		if(this.valueType==0)this.inputObj.onkeydown=onlynumber;
		else this.inputObj.onkeydown=null;
		this.inputObj.onchange=this.valueChange;
		break;
	case 1:
		this.combo=DHTMLXFactory.createCombo(this.inputObj.id,this.inputObj.id,this.inputObj.id+"_val",this.termWidth,this.mulSelect?'checkbox':null);
		//new dhtmlXCombo(this.inputObj.id,this.inputObj.id+"_val",this.termWidth,this.mulSelect?'checkbox':null);
		this.combo.enableOptionAutoWidth(true);
		this.combo.enableFilteringMode(true);
        this.combo.enableOptionAutoPositioning(true);
//		this.combo.setAutoOpenListWidth(true);
        this.combo.termControl=this;
		this.combo.mulSelect=this.mulSelect;
		this.combo.comboCheck=this.comboCheck;
		if(this.mulSelect)
		{
			this.combo.attachEvent("onCheck", function(value, state){
				if(this.getSelectedIndex()==-1 && state)
				{
					this.selIndex=true;
					this.selectOption(this.getIndexByValue(value),false,false);
				}
				this.comboCheck(this);
				return true;
			});
		}
		this.combo.attachEvent("onSelectionChange", function(){
			if(this.mulSelect)
			{
				var checked=this.getOption(this.getSelectedValue()).data()[2];
				if(!this.selIndex)//this.getSelectedIndex()!=this.selectedIndex && 
				{
					this.setChecked(this.getSelectedIndex(),!checked); 
					if(checked)
					{
						var checked_array = this.getChecked();
						if(checked_array.length)
					  	{
							this.selIndex=true;
							this.selectOption(this.getIndexByValue(checked_array[0]),false,false);
					  	}
					}
				}
				this.selIndex=false;
				this.comboCheck(this);
			}
			else
				this.termControl.valueChange();
			return true;
  		}); 
		break;
	case 2:	//	下拉树
		this.selTree=new meta.ui.selectTree(this.inputObj.id);
		this.selTree.termControl=this;
        this.selTree.enableSearch(true,true);
        this.selTree.enableAutoSize(true);
        this.selTree.enableMuiltselect(this.mulSelect);
        this.selTree.setDynaLoad(this.dynaLoad,meta.term.termControl.termTreeListFlash);
		break;
	case 3://	日期
		this.inputObj.readonly=true;
		this.myCalendar=DHTMLXFactory.createCalendar(this.inputObj.id,this.inputObj.id);
		this.myCalendar.setDateFormat("%Y-%m-%d");
		this.myCalendar.termControl=this;
		this.myCalendar.attachEvent("onClick",function(date){
		     this.termControl.valueChange();
		     return true;
			});
		break;
	}
	this.readered=true;
}
//组合框值设定
meta.term.termControl.prototype.comboCheck=function(combo)
{
	if(this.mulSelect)
	{
		var txt="";
		var checked_array =combo.getChecked();//alert(checked_array);
		for(var i=0;i<checked_array.length;i++)
		{
			if(i)txt+=",";
			txt+=combo.getOption(checked_array[i]).text;
		}
	 	combo.setComboText(txt);
	 	combo.termControl.valueChange();
	}
 }
/**
 * 绑定数据
 * @param {Object} data
 */
meta.term.termControl.prototype.bindData=function(data)
{
	if(!this.readered)this.render();
	switch(this.termType)
	{
		case 1://combo
			var valMap={};
			if(this.mulSelect)
			for(var i=0;i<this.defaultValue.length;i++)
				valMap[this.defaultValue[i][0]]=this.defaultValue[i][1]?this.defaultValue[i][1]:this.defaultValue[i][0];
			for(var i=0;i<data.length;i++)
			{
		 		this.combo.addOption(data[i][0]+"",data[i][1]+"");
		 		var op=this.combo.getOptionByIndex(i);
		 		op.optionData=data[i].clone();
		 		if(this.mulSelect)//支持多选，checkbox
		 		{
	 				if(valMap[data[i][0]+""])
						this.combo.setChecked(this.combo.getIndexByValue(data[i][0]),true); 
		 		}
		 		else
		 		{
		 			if(data[i][0]+""==this.defaultValue[0]+"")
		 				this.combo.selectOption(i,false,true);
		 		}
			}
			break;
		case 2://tree
			if(this.selTree.dataLoadType)
			{
//				this.selTree.tree.attachEvent("onDblClick", meta.term.termControl.termTreeListFlashDbl);
//				this.selTree.tree.attachEvent("onSelect",meta.term.termControl.termTreeListFlashSel);
//				this.selTree.tree.attachEvent("onClick", meta.term.termControl.termTreeListFlashClk);
//				this.selTree.tree.attachEvent("onOpenStart", meta.term.termControl.termTreeListFlash);
				
				this.selTree.tree.attachEvent("onDblClick", meta.term.termControl.termTreeListSelect);
				this.selTree.enableDynaLoad(true,meta.term.termControl.termTreeListFlash);
				
			}
			else
			{
				this.selTree.tree.attachEvent("onSelect",meta.term.termControl.termTreeListOpenItem);
				this.selTree.tree.attachEvent("onClick", meta.term.termControl.termTreeListOpenItem);
				this.selTree.tree.attachEvent("onDblClick", meta.term.termControl.termTreeListSelect);
			}
			this.bindTreeData(data);
			this.selectDefaultTreeListVal();
			break;
		case 3://date
			this.myCalendar.setDate(this.defaultValue[0]);
			if(data.length>1)//多行,值列表
			{
				
			}
			else	//有效值区间
			{
				var val1=(data[0] && data[0].sort) ?data[0][0]:data[0];
				var val2=(data[0] && data[0].sort) ?data[0][1]:data[1];
				if(val1.trim()=="" && val2.trim()=="")return;
				if(this.parentTerm && meta.term.terms[this.termName] && meta.term.terms[this.parentTerm] && meta.term.terms[this.parentTerm].termType==3) 
				{
					 var parVal=meta.term.terms[this.parentTerm].getValue();
					val1=val1.toUpperCase().replace("{"+this.parentTerm.toUpperCase()+"}",parVal);
					val2=val2.toUpperCase().replace("{"+this.parentTerm.toUpperCase()+"}",parVal);
				}
				//判断父级条件的值
				if(val1)val1=parseInt(val1);
				else	val1=0;
				if(val2)val2=parseInt(val2);
				else	val2=0;
				val1=formatNumDate(val1);
				val2=formatNumDate(val2);
				if((val1 && val2) && val1>val2)
				{
					alert("时间有效性配置错误,开始值必需小于结束值");
					return false;
				}
				this.myCalendar.setSensitiveRange(val1,val2);
			}
			break;
	}
}
//递归选择默认值
meta.term.termControl.prototype.selectDefaultTreeListVal=function ()
{
	this.mulSelect;
	this.selTree.tree.selectItem(this.defaultValue[0],true,false);//focusItem(itemId)

}
meta.term.termControl.prototype.valueChange=function()
{
	Debug(this.getValue()+"");
	
	this.termFlashCheck();//判断是否有依赖此条件的条件存在，存在则刷新
//		return true;
	if(this.valueChangeCall)
	{
		this.valueChangeCall(this.getValue());
	}
}
//	预览值改变依赖联动
meta.term.termControl.prototype.termFlashCheck=function ()
{
	if(!meta.term.terms[this.termName])return false;//未进行打包管理的条件
	for(var term in meta.term.terms)
	{
		if(meta.term.terms[term].parentTerm && meta.term.terms[term].parentTerm==this.termName)
		{
			//读取数据，刷新
			
			return true;
		}
	}
	return false;
}
/**
 * 获取输入对象 根据条件类型返回不同对象 0:文本框 1:combo 2:tree 3:calendar
 */
meta.term.termControl.prototype.getInputObj=function()
{
	switch(this.termType)
	{
	case 0:
		return this.inputObj;
	case 1://combo
		return this.combo;
	case 2://tree
		return this.selTree;
	case 3://
		return this.myCalendar;
	}
}
//设置返回值类型 0:数字   1字符串  2:日期对象
meta.term.termControl.prototype.setValueType=function(valueType)
{
	this.valueType=valueType?valueType:1;
};
//设置条件值
meta.term.termControl.prototype.setValue=function(value)
{
	
};
//获取条件值 附加值，多值返回数组
meta.term.termControl.prototype.getValue=function()
{
	switch(this.termType)
	{
	case 0:
		return this.inputObj.value;
	case 1://combo
		if(this.mulSelect)
		{
			var res=[];
			var checked_array =this.combo.getChecked();
			for(var i=0;i<checked_array.length;i++)
			{
				 var op=this.combo.getOption(checked_array[i]);
				 res[i]=op.optionData;
			}
			return res;
		}
		else
		{
			var index =this.combo.getSelectedIndex();
			if(index>=0)
			{
				var op=this.combo.getOptionByIndex(index);
				return op.optionData;
			}
			return null;
		}
	case 2://tree
		if(this.mulSelect)
		{
			return this.selTree.getCheckedValue();
		}
		else
		{
			return this.selTree.getItemValue(this.inputObj.getAttribute("code"));
		}
	case 3:
		return this.inputObj.value;
	}
	return this.defaultValue;
};
//设置输入框宽度
meta.term.termControl.prototype.setWidth=function(width)
{
	this.termWidth=parseInt(width);
	if(!this.termWidth)this.termWidth=100;
	this.inputObj.style.width=width+"px";
	if(this.termType==1 && this.combo)
		this.combo.setSize(this.termWidth);
};
//设置下拉树宽度
meta.term.termControl.prototype.setListWidth=function(width,height)
{
	this.listWidth = parseInt(width)?parseInt(width):100 ;
   	this.listHeight =parseInt(height)?parseInt(height):100 ;
	switch(this.termType)
	{
	case 1:
		if(this.combo)
		{
			this.combo.setOptionWidth(this.listWidth);
			this.combo.setOptionHeight(this.listHeight);
		}
		break;
	case 2:
		this.listWidth = parseInt(width)?parseInt(width):100 ;
   		this.listHeight =parseInt(height)?parseInt(height):150 ;
   		if(this.selTree)
			this.selTree.setListSize(this.listWidth,this.listHeight);
		break;
	}
};

//条件控件统一创建入口
meta.term.createTermControl=function(parentDiv,termName,valueChangeCall)
{
	termName=termName.toUpperCase();
	if(meta.term.terms[termName])return meta.term.terms[termName];
	return meta.term.terms[termName]=new meta.term.termControl(parentDiv,termName,valueChangeCall);
}
//获取单个控件
meta.term.getTermControl=function(termName)
{
	termName=termName.toUpperCase();
	if(meta.term.terms[termName])return meta.term.terms[termName];
	return null;
}
/**
 * 获取输入对象 根据条件类型返回不同对象 0:文本框 1:combo 2:tree 3:calendar
 * @param {Object} termName
 * @return {TypeName} 
 */
meta.term.getTermInputObj=function(termName)
{
	termName=termName.toUpperCase();
	if(meta.term.terms[termName])return meta.term.terms[termName].getInputObj();
	return null;
}
//设置单个条件控件的值
meta.term.setTermValue=function(termName,value)
{
	termName=termName.toUpperCase();
	if(!meta.term.terms[termName])return null;
	return meta.term.terms[termName].setValue(value);
}
//获取单个条件控件的值
meta.term.getTermValue=function(termName)
{
	termName=termName.toUpperCase();
	if(!meta.term.terms[termName])return null;
	return meta.term.terms[termName].getValue();
	
}
//获取所有条件控件的值
meta.term.getAllValue=function()
{
	var res={};
	for(var termName in meta.term.terms)
	{
		var val= meta.term.terms[termName].getValue();
		res[termName]=val;
	}
	return res;
}
/**
 * 统一初始化各条件控件对象，打包数据请求，
 * 	回调函数传入参数：各条件控件值map<termName,vlaue>,附加值map<termName,[附加值数组，下拉框和下拉树可能存在]>
 * @param {Object} callBackFun(termVals,termAppendVals) 
 * @return {TypeName} 是否初始化成功
 */
meta.term.init=function(callBackFun)
{
	try
	{
		var cfms=[];
		for(var termName in meta.term.terms)
		{
			meta.term.terms[termName].render();
			var index=cfms.length;
			cfms[index]=meta.term.terms[termName].getConfig();
			cfms[index].value=meta.term.terms[termName].getValue();
			if(cfms[index].dynaLoad)
			{
				cfms[index].dataRule=cfms[index].dataRule.split(";")[0];
			}
		}
		//请求服务器数据
		TermControl.getTermsData(cfms,function(res)
		{
			if(res==null || res=="null" || res[0]=="false")
			{
				alert("读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
				return false;
			}
			//绑定对象值
			for(var termName in meta.term.terms)
			{
				meta.term.terms[termName].bindData(res[termName]);
			}
			if(callBackFun)
			{
				var termVals=meta.term.getAllValue();
				callBackFun(termVals);
			}
		});
		return true;
	}
	catch(ex)
	{
		if(typeof ex=="string")
		{
			alert(ex);
		}
		else
		{
			var msg="";
			for(var key in ex)
				if(ex[key])msg+=key+":"+ex[key]+"\n";
			alert(msg);
		}
		return false;
	}
}
