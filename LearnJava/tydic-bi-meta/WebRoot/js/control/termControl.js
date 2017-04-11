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
meta.uid=function(){
	if(dhx)return dhx.uid();
	return Math.random()*meta.maxInt+1;
};
/**
 * 静态工具函数，条件包生成器，条件包一般通过此生成，加入了资源回收
 * @class {TermReqFactory}
 */
var TermReqFactory = {
    reqs:{},

    /**
     * 创建一个请求包
     * @memberOf {TermReqFactory}
     * @param id 传入ID，如果已存在则直接返回
     */
    createTermReq:function(id){
        if(id==null || id==undefined){
            alert("请传入正确请求包ID");
            return null;
        }
        if(this.reqs[id])
            return this.reqs[id];
        this.reqs[id] = new meta.term(id);
        this.reqs[id].DHTML_TYPE = "termReqs";
        return this.reqs[id];
    },

    /**
     * 根据ID获取请求包
     * @memberOf {TermReqFactory}
     * @param id
     */
    getTermReq:function(id){
        if(id==null || id==undefined){
            alert("请传入正确请求包ID");
            return null;
        }
        if(this.reqs[id])
            return this.reqs[id];
        else
            alert("getTermReq \n未找到对应的请求包对象，请检查ID或是否已创建过!");
        return null;
    }
};
Destroy.addDestroyVar(TermReqFactory);

/**
 * 条件对象包(一组条件对象)
 * @class {meta.term}
 */
meta.term=function(id){
    this.id = id;
    this.terms={} ;
};
/**
 * 条件控件对象
 * @param {Object} parentDiv  父对象，可以是容器或者input框
 * @param {Object} termName 条件名称，也是参数的KEY
 * @memberOf {TypeName} 
 */
meta.term.metaDataSourceId = 0;//元数据源ID

/**
 * 一个条件对象构造方法
 * @class {meta.term.termControl}
 * @param parentDiv 父容器
 * @param termName 条件名（不区分大小写）
 * @param valueChangeCall 值改变的回调事件
 */
meta.term.termControl=function(parentDiv,termName,valueChangeCall){
	this.termName="";//条件值名称,值宏变量名称
 	this.textName="";//文本宏变量名称
	this.termType=0;	//	0:文本框 1:下拉框 2:下拉树 3:日期控件 时间选择器
	this.parentTerm="";	//父级条件名称，下拉框级联，联动依赖
	this.valueType=1;	//条件值数值类型	0:数字   1字符串  2:日期对象
	this.termWidth=140;//条件框长度
	this.dataSrcType=0;//数据来源类型    1:SQL查询语句 0:固定值列表 2 后台程序接口

    /** this.dataRule 注释：SQL 或者 值列表  或者 后台接口实现类全名
     * sql可带宏变量，一是条件和字段宏变量宏变量{termName}或{textVal}的形式，二是函数宏变量{fun:函数名}，函数必须返回复合sql语法的值
     *
     termType=0时：此字段无效；
     termType=1时：sql规则：select value,text,... from tb 至少两个字段位置顺序固定，where条件可带宏变量
                   值规则：二维数组(与sql列数要求一样)
     termType=2时：sql规则：select val,txt,parV,... from tb
                        至少三个字段位置固定,配合下拉树setTreeChildFiledFlag方法第四个字段可以是是否有子标识位
                              select a.val,a.txt,a.parV,
                                (case when  exists (select 1 from tb x where x.parV=a.val) then 1 else 0 end) hasChild,...
                              from tb a
                        异步sql规则：select val,txt,parV,... from tab;select val,txt,parV,... from tab where parV={val} 两个sql;分割
                   值规则：二维数组（与sql列数要求一样），无异步概念
     termType=3时：sql规则：select value from tb 只认第一个字段
                            值有3种格式：20110101有效值
                                         -20110201无效值
                                         20100202-20110202有效区间，20100202-0右开区间，0-20100202左开区间
                    值规则：一维数组（与sql列数要求一样）
     **/
    this.dataRule="";
    this.classRuleParams=null;//后台接口获取数据时，接口参数

 	this.dataSrcId=meta.term.metaDataSourceId;//数据来源数据库  数据源ID
	this.appendData=null;//	附加选项值  在下拉框和下拉树时有效
	
	this.defaultValue=[];// 页面初始化时的条件默认值，不设置时：下拉框默认为第一个选项，时间框默认为当前时间 
	////////////////////////////////////////以下为维度获取数据,SQL则服务器端生成///////////////////////////////////////////////////////////
	this.initType=0;//1 维度表设置  2码表设置

    this.codeType = "";//码表编码键
	this.dimTableId=0;
	this.dimTypeId=0;
	this.dimValueType=0;//维度数据类型 0:维度编码，1:维度ID
	this.dimDataLevels=[];//维度表ID，层级列表，必须从小到大的数字
    this.dimInitValues = [];  //初始值列表
    this.excludeValues=[];//需要排除的值


	
	this.mulSelect=false;//是否支持多选 下拉框和树情况下 使用复选框支持	
	this.dynload=false;//
	
	termName=termName.toUpperCase();
	this.inputObj=parentDiv;
	if(typeof parentDiv=="string")
		this.inputObj=$(parentDiv);
	if(!this.inputObj.id)this.inputObj.id=meta.uid();
	this.termName=termName;
	this.valueChangeCall=valueChangeCall;
    this.bindDataCall = function(){};
    this.bindDataBeforeCall = function(d){return d};
};

/**
 * 私有内置方法 获取一个条件的配置，此配置一般会被传回服务器
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.getConfig=function(){
	var cfm={};
	cfm.termName         =this.termName         ;
	cfm.textName         =this.textName         ;
	cfm.termType         =this.termType         ;
	cfm.parentTerm       =this.parentTerm      ;

	cfm.valueType        =this.valueType        ;
	cfm.termWidth        =this.termWidth        ;
	cfm.dataSrcType      =this.dataSrcType     ;
	cfm.dataRule         =(this.dataSrcType!=0)?this.dataRule:""         ;
	cfm.dataSrcId        =((this.initType==1) ? meta.term.metaDataSourceId : this.dataSrcId);
	cfm.defaultValue     =this.defaultValue.join(",");
    cfm.defValPathInited = this.defValPathInited;
    cfm.classRuleParams = this.classRuleParams;

    cfm.codeType         =this.codeType;
    cfm.codeInited       = this.codeInited;
    cfm.initType         =this.initType         ;
	cfm.dimTableId       =this.dimTableId       ;
	cfm.dimTypeId        =this.dimTypeId        ;
	cfm.dimValueType     =this.dimValueType     ;
	cfm.dimDataLevels    =this.dimDataLevels.join(",")    ;
	cfm.excludeValues 	 =this.excludeValues.join(",")	;
	cfm.dimInitValues 	 =this.dimInitValues.join(",")	;
	cfm.mulSelect        =this.mulSelect        ;
	cfm.dynload		=this.dynload		;
    cfm.treeChildFlag = this.treeChildFlag;
	return cfm;
};

/**
 * 设置单条记录绑定之前回调，可对单条数据进行处理加工，默认是原样返回
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.setBindBeforeCall = function(fun){
    this.bindDataBeforeCall = fun;
};
/**
 * 设置绑后回调
 * 所有数据绑定完成后的回调，默认是什么都不做
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.setBindDataCall = function(fun){
    this.bindDataCall = fun;
};
/**
 * 处理服务端附加数据回调接口
 * 此回调方法有两个参数，1是追加数据  2是条件对象
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.setServerAppendDataCall = function(fun){
    this.serverAppendDataCall = fun;
};

/**
 * 设置从后台接口类中获取数据
 * @memberOf {meta.term.termControl}
 * @param className 类全名,此类必须是条件请求提供的固定接口或抽象类的实现
 * @param termType 控件类型1下拉框  2下拉树
 * @param defaultValue 默认值
 * @param classParams 参数，可以是固定值对象，也可以是函数
 */
meta.term.termControl.prototype.setClassRule = function(className,termType,defaultValue,classParams){
    this.dataRule = className;
    this.dataSrcType = 2;
    this.termType = termType==1 ? 1 : 2;
    this.dataSrcId = meta.term.metaDataSourceId;
    if(defaultValue!=null && defaultValue!=undefined){
        if(typeof defaultValue=="string" || typeof defaultValue=="number" ){
            if(typeof defaultValue =="number")
                this.defaultValue[0] = defaultValue+"";
            else
                this.defaultValue = defaultValue.split(",");
        }else if(typeof defaultValue=="object" && defaultValue.sort)
            this.defaultValue=defaultValue;
        else
            alert("请设置正确的默认值");
    }
    this.classRuleParams = classParams;
};
/**
 * 设置SQL常量
 * @param className 类名称
 * @param filedName 字段名称
 * @param dataSrcId  Sql执行数据源
 * @param defaultValue 默认值
 */
meta.term.termControl.prototype.setClassConstantSql=function(className,filedName,dataSrcId,defaultValue){
    this.termType = 1;
    this.dataRule = "tydic.meta.common.term.TermDataConstantSqlServiceImpl";
    this.dataSrcType = 2;
    this.dataSrcId =dataSrcId||meta.term.metaDataSourceId;
    if(defaultValue!=null && defaultValue!=undefined){
        if(typeof defaultValue=="string" || typeof defaultValue=="number" ){
            if(typeof defaultValue =="number")
                this.defaultValue[0] = defaultValue+"";
            else
                this.defaultValue = defaultValue.split(",");
        }else if(typeof defaultValue=="object" && defaultValue.sort)
            this.defaultValue=defaultValue;
        else
            alert("请设置正确的默认值");
    }
    this.constantSql=[className,filedName];
};

/**
 * 设置从码表获取数据填充下拉框
 * @memberOf {meta.term.termControl}
 * @param codeType
 * @param defaultValue
 * @param excludeValues
 */
meta.term.termControl.prototype.setCodeRule=function(codeType,defaultValue,excludeValues){
    this.dataRule = "";
    this.dataSrcType = 0;  //先设置成固定值模式，尝试从客户端缓存中获取码表值，如果没找到，那么则重新请求后台获取
    this.dataSrcId = meta.term.metaDataSourceId;
    this.initType = 2;
    this.termType =1;//下拉框
    this.codeType = codeType;
    if(defaultValue!=null && defaultValue!=undefined){
        if(typeof defaultValue=="string" || typeof defaultValue=="number" ){
            if(typeof defaultValue =="number")
                this.defaultValue[0] = defaultValue+"";
            else
                this.defaultValue = defaultValue.split(",");
        }else if(typeof defaultValue=="object" && defaultValue.sort)
            this.defaultValue=defaultValue;
        else
            alert("请设置正确的默认值");
    }
    if(excludeValues)//需要排除的值
    {
        if(typeof excludeValues=="string" || typeof excludeValues=="number")
            if(typeof excludeValues =="number")
                this.excludeValues[0] = excludeValues+"";
            else
                this.excludeValues = excludeValues.split(",");
        else if(typeof excludeValues=="object" && excludeValues.sort)
            this.excludeValues=excludeValues;
        else
            alert("请设置正确的码表条件排除数据列表");
    }
};

/**
 * 设置从维度获取数据填充下拉框或者下拉树，自动识别使用下拉框还是下拉树 同步还是异步加载
 * @memberOf {meta.term.termControl}
 * @param {Object} dimTableId  	维度表ID
 * @param {Object} dimTypeId	维度类型ID
 * @param defaultValue 默认值
 * @param {Object} dimDataLevels 需要的维度数据层级
 * @param {Object} dimValueType 维度数据类型 0:维度编码，1:维度ID
 * @param {Object} dimInitValues 初始值
 * @param {Object} excludeValues 需要排除的维度编码
 */
meta.term.termControl.prototype.setDimRule=function(dimTableId,dimTypeId,defaultValue,dimDataLevels,dimValueType,dimInitValues,excludeValues){
    this.dataRule = "";//只要调用此方法，数据sql,就清空，在第一次进入后台初始时，由服务端拼好sql回填给他
    this.dataSrcType = 1;
    this.dataSrcId = meta.term.metaDataSourceId;
	this.initType=1;
	if(!dimTableId){alert("请设置正确的维度表ID");return;}
	if(!dimTypeId){alert("请设置正确的维度归并类型ID");return;}
	this.dimTableId=dimTableId;
	this.dimTypeId=dimTypeId;
    if(defaultValue!=null && defaultValue!=undefined){
        if(typeof defaultValue=="string" || typeof defaultValue=="number" ){
            if(typeof defaultValue =="number")
                this.defaultValue[0] = defaultValue+"";
            else
                this.defaultValue = defaultValue.split(",");
        }else if(typeof defaultValue=="object" && defaultValue.sort)
            this.defaultValue=defaultValue;
        else
            alert("请设置正确的默认值");
    }
	if(dimDataLevels)//维度表ID，层级列表
	{
		if(typeof dimDataLevels=="string" || typeof dimDataLevels=="number" ){
            if(typeof dimDataLevels =="number")
			    this.dimDataLevels[0] = dimDataLevels+"";
            else
                this.dimDataLevels = dimDataLevels.split(",");
        }else if(typeof dimDataLevels=="object" && dimDataLevels.sort)
			this.dimDataLevels=dimDataLevels;
        else
			alert("请设置正确的维度条件数据层级列表");

        for(var i=0;i<dimDataLevels.length;i++){
            if(parseInt(dimDataLevels[i]))
                dimDataLevels[i] = parseInt(dimDataLevels[i])+"";
            else{
                dimDataLevels = [];
                alert("请设置正确的层级，必须是数字");
            }
        }
        this.dimDataLevels.sort(function(a,b){return a-b});
	}
	if(excludeValues)//需要排除的值
	{
		if(typeof excludeValues=="string" || typeof excludeValues=="number")
            if(typeof excludeValues =="number")
                this.excludeValues[0] = excludeValues+"";
            else
                this.excludeValues = excludeValues.split(",");
		else if(typeof excludeValues=="object" && excludeValues.sort)
			this.excludeValues=excludeValues;
		else
			alert("请设置正确的维度条件排除数据列表");
	}
    if(dimInitValues)//初始值
    {
        if(typeof dimInitValues=="string" || typeof dimInitValues=="number")
            if(typeof dimInitValues =="number")
                this.dimInitValues[0] = dimInitValues+"";
            else
                this.dimInitValues = dimInitValues.split(",");
        else if(typeof dimInitValues=="object" && dimInitValues.sort)
            this.dimInitValues=dimInitValues;
        else
            alert("请设置正确的维度初始值数据列表");
    }

	if(dimValueType==1)
		this.dimValueType=1;//维度数据类型 0:维度编码，1:维度ID
	else 
		this.dimValueType=0;//维度数据类型 0:维度编码，1:维度ID
	
	if(this.dimDataLevels.length>1 || this.dimDataLevels.length==0 
			|| parseInt(this.dimDataLevels[0])>1)//多层数据 必定为树   只有一层，且大于1 
	{
		this.termType=2;
        if(dimTableId==2 && this.dimDataLevels.length==1 && parseInt(this.dimDataLevels[0])<=2)
            this.termType = 1;
	}
	else
	{
		this.termType=1;
	}
	if(dimTableId==1 && this.dimDataLevels[0] && parseInt(this.dimDataLevels[0])==3)	//时间维度且为日
		this.termType=3;
};
/**
 * 设置下拉框数据规则
 * @memberOf {meta.term.termControl}
 * @param {Object} dataSrcType	数据来源类型    1:SQL查询语句 0:固定值列表 
 * @param {Object} dataRule	SQL或者值列表 select value,text from tb 或者  数组 字段顺序：value,text   可在后续列附加数据 ，可在后续列附加数据，如果是数组，表示SQL为后台常量
 * 例:["tydic.meta.module.tbl.TblConstant","DATA_SOURCE_SQL"]  ,表示SQL来自于类 tydic.meta.module.tbl.TblConstant 的  DATA_SOURCE_SQL静态字段
 * @param {Object} defaultValue
 * @param {Object} dataSrcId 
 * @memberOf {TypeName} 
 */
meta.term.termControl.prototype.setListRule=function(dataSrcType,dataRule,defaultValue,dataSrcId){
	this.termType=1;
	if(dataSrcType)this.dataSrcType=dataSrcType;//数据来源类型    1:SQL查询语句 0:固定值列表
	if(dataRule)  this.dataRule=dataRule;//SQL或者值列表
	if(dataSrcId)this.dataSrcId=dataSrcId;//数据来源数据库  数据源ID
    if(defaultValue!=null && defaultValue!=undefined){
        if(typeof defaultValue=="string" || typeof defaultValue=="number" ){
            if(typeof defaultValue =="number")
                this.defaultValue[0] = defaultValue+"";
            else
                this.defaultValue = defaultValue.split(",");
        }else if(typeof defaultValue=="object" && defaultValue.sort)
            this.defaultValue=defaultValue;
        else
            alert("请设置正确的默认值");
    }
};
/**
 * 设置日期条件
 * @memberOf {meta.term.termControl}
 * @param dataSrcType 数据来源类型 0固定值，1SQL
 * @param dataRule
 * @param defaultValue
 * @param dataSrcId
 */
meta.term.termControl.prototype.setDateRule=function(dataSrcType,dataRule,defaultValue,dataSrcId){
	this.termType=3;
	if(dataSrcType)this.dataSrcType=dataSrcType;//数据来源类型    1:SQL查询语句 0:固定值列表 
	if(dataRule)this.dataRule=dataRule;
	if(dataSrcId)this.dataSrcId=dataSrcId;//数据来源数据库  数据源ID
    if(defaultValue!=null && defaultValue!=undefined){
        if(typeof defaultValue=="string" || typeof defaultValue=="number" ){
            if(typeof defaultValue =="number")
                this.defaultValue[0] = defaultValue+"";
            else
                this.defaultValue = defaultValue.split(",");
        }else if(typeof defaultValue=="object" && defaultValue.sort)
            this.defaultValue=defaultValue;
        else
            alert("请设置正确的默认值");
    }
};
/**
 * 设置下拉树数据规则
 * @memberOf {meta.term.termControl}
 * @param dataSrcType 数据来源类型 0固定值，1为SQL
 * @param dataRule
 *          sql规则：select val,txt,parV,... from tb
                    至少三个字段位置固定,配合下拉树setTreeChildFiledFlag方法第四个字段可以是是否有子标识位
                 select a.val,a.txt,a.parV,
                 (case when  exists (select 1 from tb x where x.parV=a.val) then 1 else 0 end) hasChild,...
                 from tb a
             异步sql规则：select val,txt,parV,... from tab;select val,txt,parV,... from tab where parV={val} 两个sql;分割
             值规则：二维数组（与sql列数要求一样），无异步概念
 * @param dataSrcId 数据源ID
 * @param defaultValue 默认值
 */
meta.term.termControl.prototype.setTreeRule=function(dataSrcType,dataRule,defaultValue,dataSrcId){
	if(!dataRule){
		alert("请设置正确的下拉树数据规则");
		return;
	}
	this.termType=2;
    //数据来源类型    1:SQL查询语句 0:固定值列表
	if(dataSrcType){
        this.dataSrcType=dataSrcType;
        if(typeof dataRule=="object" && dataRule.sort){
            if(data.length>1)this.dynload=true;
            this.dataRule=dataRule.join(";");
        }else if(typeof dataRule=="string"){
            if(dataRule.split(";").length>1)
                this.dynload=true;
            this.dataRule=dataRule ;
        }else{
            alert("请设置正确的下拉树数据规则");
            return;
        }
    }else{
        if(typeof dataRule=="object" && dataRule.sort && dataRule[0] && dataRule[0].sort){
            this.dataRule=dataRule;
        }else{
            alert("请设置正确的下拉树数据(必须是二维数组)");
            return;
        }
    }
	if(dataSrcId)this.dataSrcId=dataSrcId;//数据来源数据库  数据源ID
    if(defaultValue!=null && defaultValue!=undefined){
        if(typeof defaultValue=="string" || typeof defaultValue=="number" ){
            if(typeof defaultValue =="number")
                this.defaultValue[0] = defaultValue+"";
            else
                this.defaultValue = defaultValue.split(",");
        }else if(typeof defaultValue=="object" && defaultValue.sort)
            this.defaultValue=defaultValue;
        else
            alert("请设置正确的默认值");
    }
};
/**
 * 设置checkbox传递模式分四种：
 * @memberOf {meta.term.termControl}
 * 1，向下传递（子永远跟着父变）
 * 2，向上传递（勾选子时，依赖的父也勾选，取消子勾选父不变）
 * 3，双向传递（子永远跟着父变；勾选子时，依赖的父也勾选，取消子勾选父不变）
 * 4，父永远控制子的选中，但是子未勾全时，父为半勾选状态
 * @param {Object} flag
 * @memberOf {TypeName}
 */
meta.term.termControl.prototype.setTreeCheckboxFlag=function(flag){
	if(!this.mulSelect)return;
	if(!this.selTree)return;
	this.selTree.setCheckboxFlag(flag);
};
/**
 * 设置异步加载树数据时的是否存在子节点标识位 true: id,text,pid,flag  false: id,text,pid
 * @param {Object} flag
 * @memberOf {meta.term.termControl}
 * @memberOf {TypeName} 
 */
meta.term.termControl.prototype.setTreeChildFiledFlag=function(flag){
    this.treeChildFlag = !!flag;
	if(!this.selTree)return;
	this.selTree.enableChildField(this.treeChildFlag);
};

/**
 * 设置追加数据，比如往下拉框加入一个  全部，请选择选项
 * @param data 数据格式与对应控件要求的格式一样
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.setAppendData=function(data){
	if(data)this.appendData=data;
};
/**
 * 设置父条件，级联刷新实现
 * @memberOf {meta.term.termControl}
 * @param parentTermName 父条件或父条件名
 */
meta.term.termControl.prototype.setParentTerm=function(parentTermName){
    var ptermName = "";
    if(typeof(parentTermName)=="string")
	    ptermName = parentTermName.toUpperCase();
    else if(typeof(parentTermName)=="object")
        ptermName = parentTermName.termName;
	if(this.metaTerm.terms[ptermName])
		this.parentTerm=ptermName;
	else
		return false;
	return true;
};
/**
 * 设置单个条件的值改变回调
 * @memberOf {meta.term.termControl}
 * @param valueChangeCall
 */
meta.term.termControl.prototype.setValueChange=function(valueChangeCall){
	this.valueChangeCall=valueChangeCall;
};

/**
 * 私有方法
 * 替换函数或变量 宏变量
 * @memberOf {meta.term.termControl}
 * @param value
 */
meta.term.termControl.prototype.replaceMacroVar = function(value){
    var regExp = new RegExp("\\{(fun:[^}]*?)\\}","ig");
    var __tm = this;
    var txt = value.replace(regExp,function(x){
        var _x = x.substring(1,x.length-1).replace(new RegExp("fun:","ig"),"");
        try{
            if(window[_x]){
                if(typeof(window[_x])=="function")
                    return window[_x](__tm);
                else
                    return window[_x]+"";
            }
            return "";
        }catch(e){
            alert("传入的外部宏变量或函数 "+_x+" 运行出错!");
            return "";
        }
    });
    __tm = null;
    return txt;
};

/**
 * 单个使用时的初始化
 * @memberOf {meta.term.termControl}
 * @param {Object} callBackFun
 * @memberOf {TypeName} 
 */
meta.term.termControl.prototype.init=function(callBackFun){
	this.render();
    var thisCfm=this.getConfig();
    if(this.dataSrcType==0){
        this.bindData(thisCfm.dataRule);
        this.inited = true;
        return;
    }
    if(this.initType==2){
        if(window["getCodeArrayByRemoveValue"]){
            var codes = window["getCodeArrayByRemoveValue"](this.codeType,this.excludeValues);
            if(codes && codes.length>0){
                this.bindData(codes);
                this.inited = true;
                return;
            }else{
                this.dataSrcType = 2;
            }
        }else{
            this.dataSrcType = 2;
        }
    }
    thisCfm.dataRule=thisCfm.dataRule.split(";")[0];
    if(this.dataSrcType==1){
        thisCfm.dataRule = this.replaceMacroVar(thisCfm.dataRule);
    }
    if(thisCfm.dataSrcType==2){
        if(thisCfm.classRuleParams && typeof(thisCfm.classRuleParams)=="function"){
            thisCfm.classRuleParams = thisCfm.classRuleParams();
        }
    }
	thisCfm.value=this.getValue();
//    if(this.constantSql){
//        thisCfm.constantSql=this.constantSql;
//    }
	var term=this;
	TermControlAction.getTermData(thisCfm,function(res){
		if(res==null || res=="null" || res[0]=="false"){
			alert("读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
			return false;
		}
        term._dealServerAttribute(res);
		//绑定对象值
		term.bindData(res[1]);
        term.inited = true;
		if(callBackFun){
			var termVals=meta.term.getAllValue();
			callBackFun(termVals);
		}
        term = null;
	});
};
/**
 * 设置input框的回车事件,只有termType=0有效
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.setInputEnterCall = function(fun){
    this.inpuEnterCall = fun;
};
meta.term.termControl.prototype.enableReadonly = function(mode){
    this._readOnly = !!mode;
};
/**
 * 绘制条件，生成对象：组合框、下拉树、日期控件等  flag:强制重绘生成
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.render=function(flag){
	if(this.readered && !flag)return;
	this.readered=false;
	if(!this.inputObj){alert("条件绑定对象为空，不能进行绑定绘制。");return false;}
	if(this.myCalendar){
//		 this.myCalendar.i=[];
        Destroy.destructorDHMLX(this.myCalendar);
	}
	if(this.combo){
		this.combo.clearAll(true);
		Destroy.destructorDHMLX(this.combo);
		this.combo=null;delete this.combo;
	}
	if(this.selTree){
		this.selTree.destructor();//destructorDHMLX(this.selTree);
		this.selTree=null;
	}
	if(this.termType!=1 && this.termType!=2 && this.inputObj.tagName!="INPUT"){	//下拉框使用combo实现
	    var el = document.createElement("INPUT");
        el.termControl = this;
        el.type = "text";
        el.style.width = this.termWidth+"px";
        this.inputObj.appendChild(el);
        this.inputObj=el;
	}
	switch(this.termType) { //0:文本框 1:下拉框 2:下拉树 3:日期控件 时间选择器
        case 0:	//	文本框不需要做处理
            if(this.valueType==0)this.inputObj.onkeydown=onlynumber;
            else this.inputObj.onkeydown=function(e){
                e = e||window.event;
                if(e.keyCode==13){
                    if(this.onchange)this.onchange();
                }
            };
            this.inputObj.onchange=this.valueChange;
            this.inputObj.termControl = this;
            if(this.inpuEnterCall && typeof(this.inpuEnterCall)=="function"){
                this.inputObj.onkeyup = function(e){
                    e = e||window.event;
                    if(e.keyCode==13 && this.termControl){
                        this.termControl.inpuEnterCall(e);
                    }
                };
            }
            break;
        case 1:
            this.combo=DHTMLXFactory.createCombo(this.inputObj.id,this.inputObj.id,this.inputObj.id+"_val",this.termWidth,this.mulSelect?'checkbox':null);
            //new dhtmlXCombo(this.inputObj.id,this.inputObj.id+"_val",this.termWidth,this.mulSelect?'checkbox':null);
            if(this._readOnly){
                this.combo.readonly(true,true);
                this.combo.enableFilteringMode(false);
            }else{
                this.combo.enableFilteringMode(true);
            }
            this.combo.enableOptionAutoPositioning(true);
//    		this.combo.setAutoOpenListWidth(true);
            this.combo.termControl=this;
            if(this.mulSelect){
                this.combo.attachEvent("onCheck", function(value, state){
                    if(this.getSelectedIndex()==-1 && state){
                        this.selIndex=true;
                        this.selectOption(this.getIndexByValue(value),false,false);
                    }
                    this.termControl.comboCheck(this);
                    return true;
                });
            }
            this.combo.attachEvent("onSelectionChange", function(){
                if(this.termControl.mulSelect){
                    var checked=this.getOption(this.getSelectedValue()).data()[2];
                    if(!this.selIndex){//this.getSelectedIndex()!=this.selectedIndex &&
                        this.setChecked(this.getSelectedIndex(),!checked);
                        if(checked){
                            var checked_array = this.getChecked();
                            if(checked_array.length){
                                this.selIndex=true;
                                this.selectOption(this.getIndexByValue(checked_array[0]),false,false);
                            }
                        }
                    }
                    this.selIndex=false;
                    this.termControl.comboCheck(this);
                }else{
                    var v = this.getSelectedValue();
                    if(v!=this.termControl.inputObj.getAttribute("code")){
                        this.termControl.valueChange();
                    }
                    this.termControl.inputObj.setAttribute("code",v);
                }
                return true;
            });
            break;
        case 2:	//	下拉树
            this.selTree=new meta.ui.selectTree(this.inputObj.id);
            this.selTree.termControl=this;
            this.selTree.enableAutoSize(true);
            this.selTree.enableMuiltselect(this.mulSelect);
            this.selTree.setBindObjWidth(this.inputObj.id,this.termWidth);

            if(this.inputObj.tagName!="INPUT"){
                var el=$(this.inputObj.id+"_input");
                this.inputObj=el;
            }
            if(this._readOnly){
                this.inputObj.setAttribute("readonly",true);
            }else{
                this.selTree.enableSearch(true,true);
            }

            break;
        case 3://	日期
            this.inputObj.readonly=true;
            this.myCalendar=DHTMLXFactory.createCalendar(this.inputObj.id,this.inputObj.id);
            this.myCalendar.setDateFormat("%Y-%m-%d");
            this.myCalendar.termControl=this;
            this.myCalendar.attachEvent("onClick",function(date){
                var change_ = this.termControl.inputObj.getAttribute("code")==date;
                this.termControl.inputObj.setAttribute("code",date);
                if(!change_)
                    this.termControl.valueChange();
                return true;
            });
            break;
	}
	this.readered=true;
};
/**
 * 私有方法
 * 设置combo选择事件
 * @memberOf {meta.term.termControl}
 * @param combo
 * @param unCall
 */
meta.term.termControl.prototype.comboCheck=function(combo,unCall){
	if(this.mulSelect){
		var txt="";
		var checked_array =combo.getChecked();//alert(checked_array);
		for(var i=0;i<checked_array.length;i++){
			if(i)txt+=",";
			txt+=combo.getOption(checked_array[i]).text;
		}
	 	combo.setComboText(txt);
        if(!unCall)
	 	    combo.termControl.valueChange();
	}
 };
/**
 * 清空值
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.clearValue = function(){
    if(this.termType>1){
        this.inputObj.value = "";
        this.inputObj.setAttribute("code",null);
    }
    switch(this.termType){
        case 1:
            this.combo.clearAll(true);
            break;
        case 2:
            this.selTree.tree.deleteChildItems(this.selTree.tree.rootId);
            break;
        case 3:
            this.myCalendar.clearSensitiveRange();
            this.myCalendar.clearInsensitiveDays();
    }
};
/**
 * 绑定数据
 * @param {Object} data  从后台返回的数据都会通过此接口绑定到具体的条件上
 * data一般是二维数组
 * @param isfresh 是否由级联刷新引起的绑定
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.bindData=function(resdata,isfresh){
	if(!this.readered)this.render();
    var data= (this.dataSrcType==0 && this.initType==0) ? this.dataRule : resdata;
    if(this.termType==0)this.inputObj.value = this.defaultValue;
    this.setTreeChildFiledFlag(this.treeChildFlag);
    if(isfresh && this.termType>1){//下拉框实现无文本框
        this.inputObj.value = "";
        this.inputObj.setAttribute("code",null);
    }
    if(data==null || data==undefined)return;
	switch(this.termType){
        case 0: //文本框
            this.inputObj.value = this.defaultValue;
            break;
		case 1://combo
            if(isfresh)this.combo.clearAll(true);
			var valMap={};
			if(this.mulSelect){
                for(var i=0;i<this.defaultValue.length;i++){
                    valMap[this.defaultValue[i]]=this.defaultValue[i];
                }
            }
            if(this.appendData && this.appendData.sort){
                for(var i=this.appendData.length-1;i>=0;i--){
                    data.unshift(this.appendData[i]);
                }
            }
			for(var i=0;i<data.length;i++){
                var bd = data[i];
                if(bd==null ||bd==undefined)continue;
                bd = this.bindDataBeforeCall(bd);
		 		this.combo.addOption(bd[0]+"",bd[1]+"");
		 		var op=this.combo.getOptionByIndex(i);
		 		op.optionData=bd.clone();
		 		if(this.mulSelect) {//支持多选，checkbox
	 				if(valMap[bd[0]+""])
						this.combo.setChecked(this.combo.getIndexByValue(bd[0]),true);
		 		}else{
		 			if(bd[0]+""==this.defaultValue[0]+"")
		 				this.combo.selectOption(i,false,true);
		 		}
			}
            if(this.mulSelect)
                this.comboCheck(this.combo,true);
            else{
                this.inputObj.value = this.combo.getSelectedText();
                this.inputObj.setAttribute("code",this.combo.getSelectedValue());
            }
			break;
		case 2://tree
            if(isfresh){
                this.selTree.tree.deleteChildItems(this.selTree.tree.rootId);
            }else{
                if(this.dynload){
                    this.selTree.tree.attachEvent("onSelect",meta.term.termControl.termTreeListFlashSel);
                    this.selTree.tree.attachEvent("onClick", meta.term.termControl.termTreeListFlashClk);
                    this.selTree.tree.attachEvent("onOpenStart", meta.term.termControl.termTreeListFlash);
                    this.selTree.tree.attachEvent("onDblClick", meta.term.termControl.termTreeListFlashDbl);
                    if(this.mulSelect){
                        this.selTree.tree.attachEvent("onCheck", meta.term.termControl.termTreeCheck);
                    }
                    this.selTree.setDynload(this.dynload,function(id,tree){return true;});
                    this.selTree.enableItemDynLoad(this.selTree.tree.rootId,false);
//				this.selTree.setDynload(this.dynload,this.termTreeDynload);
                }else{
                    this.selTree.tree.attachEvent("onSelect",meta.term.termControl.termTreeListOpenItem);
                    this.selTree.tree.attachEvent("onClick", meta.term.termControl.termTreeListOpenItem);
                    this.selTree.tree.attachEvent("onDblClick", meta.term.termControl.termTreeListSelect);
                    if(this.mulSelect){
                        this.selTree.tree.attachEvent("onCheck", meta.term.termControl.termTreeCheck);
                    }
                }
            }
            if(this.appendData && this.appendData.sort){
                for(var i=this.appendData.length-1;i>=0;i--){
                    data.unshift(this.appendData[i]);
                }
            }
			this.bindTreeData(data);
			this.selectDefaultTreeListVal();
			break;
		case 3://date
            if(isfresh){
                this.myCalendar.clearSensitiveRange();
                this.myCalendar.clearInsensitiveDays();
            }
            var def = formatNumDate(this.defaultValue[0]);
            if(!def){
                def = new Date();
                def.setDate(def.getDate()-1);
            }
            this.myCalendar.setDate(def);
            this.inputObj.value = this.myCalendar.getDate(true);
            var minR = "";
            if(this.parentTerm && this.metaTerm.terms[this.parentTerm] && this.metaTerm.terms[this.parentTerm].termType==3){
                minR = this.metaTerm.terms[this.parentTerm].getValue().replaceAll("-","");
            }
            var maxR = "";
            for(var i=0;i<data.length;i++){
                var _date = this.bindDataBeforeCall(data[i]);
                if(_date.sort)  //二维数组
                    _date = _date[0];
                _date = _date.split("-");//尝试按 '-' 字符拆分前面表示最小值，后面表示最大值,0表示无穷
                var mind = _date[0]!=null&&_date[0]!=undefined ? _date[0] : "";
                var maxd = _date[1]!=null&&_date[1]!=undefined ? _date[1] : "";
                if(this.parentTerm
                    && this.metaTerm.terms[this.termName]
                    && this.metaTerm.terms[this.parentTerm]
                    && this.metaTerm.terms[this.parentTerm].termType==3){
                    var parVal=this.metaTerm.terms[this.parentTerm].getValue();
                    mind=mind.toUpperCase().replace("{"+this.parentTerm.toUpperCase()+"}",parVal);
                    maxd=maxd.toUpperCase().replace("{"+this.parentTerm.toUpperCase()+"}",parVal);
                }
                if(mind!="" && mind!="0" && maxd==""){  //mind有效值
                    mind = formatNumDate(mind);
                }else if(mind=="" && maxd!="" && maxd!="0"){ //maxd无效值
                    maxd = formatNumDate(maxd);
                    if(maxd)
                        this.myCalendar._rangeSet[new Date(maxd.getFullYear(),maxd.getMonth(),maxd.getDate(),0,0,0,0).getTime()] = true;
                }else if(mind=="0" && maxd!="" && maxd!="0"){//左开区间
                    if(maxR)
                        maxR = Math.min(maxR,maxd);
                    else
                        maxR = maxd;
                }else if(mind!="0" && mind!="" && maxd=="0"){ //右开区间
                    if(minR)
                        minR = Math.max(minR,mind);
                    else
                        minR = mind;
                }else if(mind!="" && mind!="0" && maxd!="" && maxd!="0"){
                    if(maxR)
                        maxR = Math.min(maxR,maxd);
                    else
                        maxR = maxd;
                    if(minR)
                        minR = Math.max(minR,mind);
                    else
                        minR = mind;
                }
            }
            minR = formatNumDate(minR);
            maxR = formatNumDate(maxR);
            if((minR && maxR) && minR>maxR){
                alert("时间有效性配置错误,开始值必需小于结束值");
                return false;
            }
            this.myCalendar.setSensitiveRange(minR, maxR);
	}
    this.bindDataCall(this);
    if(isfresh){
        this.valueChange();
    }
    this.inited = true;
};
/**
 * 私有方法
 * 设置树默认值
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.selectDefaultTreeListVal=function (){
    if(this.dynload && this.defaultValue.length>0 && this.defaultValuePath){ //异步加载默认值节点
        for(var i=0;i<this.defaultValuePath.length;i++){
            this.selTree.tree.setUserData(this.selTree._priFix+this.defaultValuePath[i],"refresh","true");
        }
    }
    if(this.mulSelect){
        for(var i=0;i<this.defaultValue.length;i++){
            this.selTree.tree.setCheck(this.selTree._priFix+this.defaultValue[i],true);
        }
    }
    this.selTree.tree.selectItem(this.selTree._priFix+this.defaultValue[0],true,false);//focusItem(itemId)
    meta.term.termControl.termTreeListSelect(this.selTree._priFix+this.defaultValue[0],this,true);
};
/**
 * 私有方法
 * 条件值改变时执行
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.valueChange=function(){
//	Debug(this.getValue()+"");
    if(!this.inited)return;

    if(this.valueChangeCall){
        var flag = this.valueChangeCall(this.getValue(),this);
        if(!flag)
            return;
    }

    if(!this.mulSelect)
	    this.termFlashCheck();//忽略多选，判断是否有依赖此条件的条件存在，存在则刷新
};
/**
 * 私有方法，级联刷新
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.termFlashCheck=function (){
	if(!this.metaTerm.terms[this.termName])return;//未进行打包管理的条件
    var cfms=[];
    var flashTerms = {};
    var thisVal=this.metaTerm.terms[this.termName].getValue();
    thisVal = thisVal || "";
    if(thisVal && thisVal.sort) thisVal=thisVal[0];
	for(var term in this.metaTerm.terms){
		if(this.metaTerm.terms[term].parentTerm && this.metaTerm.terms[term].parentTerm==this.termName && term!=this.termName){
            if(this.metaTerm.terms[term].initType==2){
                if(window["getCodeArrayByRemoveValue"]){
                    var codes = window["getCodeArrayByRemoveValue"](this.metaTerm.terms[term].codeType,
                        this.metaTerm.terms[term].excludeValues);
                    if(codes && codes.length>0){
                        this.metaTerm.terms[term].bindData(codes,true);
                        this.metaTerm.terms[term].codeInited = true;
                    }else{
                        this.metaTerm.terms[term].dataSrcType = 2;
                    }
                }else{
                    this.metaTerm.terms[term].dataSrcType = 2;
                }
            }
			//读取数据，刷新
            var cfg = this.metaTerm.terms[term].getConfig();
            if(!cfg.dataSrcType)continue;//条件数据来源非SQL，无法级联刷新
            if(this.metaTerm.terms[term].dynload){
                cfg.dataRule = cfg.dataRule.split(";")[0];
            }
            var hbl = "{"+this.termName.toUpperCase()+"}";
            if(cfg.dataSrcType!=2)
                cfg.dataRule = cfg.dataRule.toUpperCase().replace(new RegExp(hbl,"ig"),thisVal);
            if(cfg.dataSrcType==1){
                cfg.dataRule = this.metaTerm.terms[term].replaceMacroVar(cfg.dataRule);
            }
            if(cfg.dataSrcType==2){
                if(cfg.classRuleParams && typeof(cfg.classRuleParams)=="function"){
                    cfg.classRuleParams = cfg.classRuleParams();
                }
            }
            if(this.metaTerm.terms[term]){
                cfg.constantSql=this.metaTerm.terms[term].constantSql;
            }
            cfms[cfms.length]= cfg;
            flashTerms[term] = this.metaTerm.terms[term];  //将满足刷新的条件存起来
            this.metaTerm.terms[term].render();
		}
	}
    if(cfms.length==0)return;
    //请求服务器数据
    TermControlAction.getTermsData(cfms,function(res){
        if(res==null || res=="null" || res[0]=="false"){
            alert("级联读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
            return false;
        }
        //绑定对象值
        for(var termName in flashTerms){
            flashTerms[termName]._dealServerAttribute(res[termName]);
            if(!flashTerms[termName].codeInited && res[termName])
                flashTerms[termName].bindData(res[termName][1],true);
        }
        flashTerms = null;
    });
};

/**
 * 获取输入对象 根据条件类型返回不同对象 0:文本框 1:combo 2:tree 3:calendar
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.getInputObj=function(){
	switch(this.termType){
        case 0:
            return this.inputObj;
        case 1://combo
            return this.combo;
        case 2://tree
            return this.selTree;
        case 3://
            return this.myCalendar;
	}
};
/**
 * 设置返回值类型 0:数字   1字符串  2:日期对象
 * @memberOf {meta.term.termControl}
 */

meta.term.termControl.prototype.setValueType=function(valueType){
	this.valueType=valueType?valueType:1;
};
/**
 * 设置条件值
 * @memberOf {meta.term.termControl}
 * @param value
 */
meta.term.termControl.prototype.setValue=function(value){
    switch(this.termType){
        case 0:
            this.inputObj.value = value;
            break;
        case 1:
            if(value && value.sort && this.mulSelect){
                for(var x=0;x<value.length;x++){
                    var v = value[x];
                    if(!v.sort)v=[v];
                    this.combo.setCheckedByValue(v[x][0]);
                }
            }else if(value && value.sort && !this.mulSelect){
                for(var i=0;i<this.combo.optionsArr;i++){
                    if(this.combo.optionsArr[i].value = value[0]){
                        this.inputObj.value = value[1];
                        this.combo.selectOption(i);
                        break;
                    }
                }
            }
            break;
        case 2:
            if(value && value.sort && this.mulSelect){
                for(var x=0;x<value.length;x++){
                    var v = value[x];
                    if(!v.sort)v=[v];
                    this.selTree.setCheck(this.selTree._priFix + v[x][0],true);
                }
            }else if(value && value.sort && !this.mulSelect){
                this.inputObj.value = value[1];
                this.selTree.tree.selectItem(this.selTree._priFix+value[0]);
            }
            break;
        case 3:
            this.inputObj.value = value;
            this.myCalendar.setDate(formatNumDate(value));
            break;
    }
	
};
/**
 * 根据termType和是否多选模式，返回不同的数据结构
 * 0文本框：返回字符串
 * 1下拉框：多选二维数组，单选一维数组，没选null
 * 2下拉树：多选二维数组，单选一维数组，没选null
 * 3日历框：返回字符串
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.getValue=function(){
    if(!this.inited){//如果自身未被初始或条件包也未被初始，则返回默认值
        if(this.termType==0 || this.termType==3)
            return this.defaultValue.join(",");
        return this.defaultValue;
    }
	switch(this.termType){
        case 0:
            return this.inputObj.value;
        case 1://combo
            if(this.mulSelect){
                var res=[];
                var checked_array =this.combo.getChecked();
                for(var i=0;i<checked_array.length;i++){
                     var op=this.combo.getOption(checked_array[i]);
                     res[i]=op.optionData;
                }
                return res.length>0 ? res : null;
            }else{
                var index =this.combo.getSelectedIndex();
                if(index>=0){
                    var op=this.combo.getOptionByIndex(index);
                    return op.optionData;
                }
                return null;
            }
        case 2://tree
            if(this.mulSelect){
                return this.selTree.getCheckedValue();
            }else{
                var id = this.inputObj.getAttribute("code");
                if(id!=null && id!=undefined && id!="")
                    return this.selTree.getItemValue(id);
                return null;
            }
        case 3:
            return this.inputObj.value;
	}
	return this.defaultValue;
};
/**
 * 设置输入框宽度
 * @memberOf {meta.term.termControl}
 * @param width
 */
meta.term.termControl.prototype.setWidth=function(width){
	this.termWidth=parseInt(width);
	if(!this.termWidth)this.termWidth=100;
	this.inputObj.style.width=width+"px";
	if(this.termType==1 && this.combo)
		this.combo.setSize(this.termWidth);
};
/**
 * 设置下拉树浮动层宽度
 * @memberOf {meta.term.termControl}
 * @param width
 * @param height
 */
meta.term.termControl.prototype.setListWidth=function(width,height){
	this.listWidth = parseInt(width)?parseInt(width):100 ;
   	this.listHeight =parseInt(height)?parseInt(height):100 ;
	switch(this.termType){
	case 1:
		if(this.combo){
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

/**
 * 创建一个条件对象（重要）
 * @memberOf {meta.term}
 * @param parentDiv
 * @param termName
 * @param valueChangeCall
 */
meta.term.prototype.createTermControl=function(parentDiv,termName,valueChangeCall){
	termName=termName.toUpperCase();
	if(this.terms[termName])return this.terms[termName];
	this.terms[termName] = new meta.term.termControl(parentDiv,termName,valueChangeCall);
    this.terms[termName].metaTerm = this;
    return this.terms[termName];
};
/**
 * 获取单个控件
 * @memberOf {meta.term}
 * @param termName
 */
meta.term.prototype.getTermControl=function(termName){
	termName=termName.toUpperCase();
	if(this.terms[termName])return this.terms[termName];
	return null;
};
/**
 * 获取输入对象 根据条件类型返回不同对象 0:文本框 1:combo 2:tree 3:calendar
 * @param {Object} termName
 * @memberOf {meta.term}
 * @return {TypeName} 
 */
meta.term.prototype.getTermInputObj=function(termName){
	termName=termName.toUpperCase();
	if(this.terms[termName])return this.terms[termName].getInputObj();
	return null;
};
/**
 * 设置单个条件值
 * @memberOf {meta.term}
 * @param termName
 * @param value
 */
meta.term.prototype.setTermValue=function(termName,value){
	termName=termName.toUpperCase();
	if(!this.terms[termName])return null;
	return this.terms[termName].setValue(value);
};
/**
 * 获取单个条件值
 * @memberOf {meta.term}
 * @param termName
 */
meta.term.prototype.getTermValue=function(termName){
	termName=termName.toUpperCase();
	if(!this.terms[termName])return null;
	return this.terms[termName].getValue();
	
};
/**
 * 清空控件
 */
meta.term.prototype.clearValue = function(){
    for(var tm in this.terms){
        this.terms[tm].clearValue();
    }
};

/**
 * 获取所有条件值
 * @memberOf {meta.term}
 */
meta.term.prototype.getAllValue=function(){
	var res={};
	for(var termName in this.terms){
		var val= this.terms[termName].getValue();
		res[termName]=val;
	}
	return res;
};
/**
 * 获取单个条件值  简单格式，单选字符串，多选"，"分割的字符串
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.getKeyValue = function(){
    var val = this.getValue();
    if(this.termType==1 || this.termType==2){
        val = val||[];
        if(this.mulSelect){
            for(var i=0;i<val.length;i++){
                val[i] = val[i][0];
            }
            val = val.join(",");
        }else{
            val = (val[0]!=null && val[0]!=undefined) ? val[0] : "";
        }
    }
    return val;
};
/**
 * 获取所有条件控件的值（简单格式）
 * @memberOf {meta.term}
 */
meta.term.prototype.getKeyValue=function(){
    var res={};
    for(var termName in this.terms){
        res[termName]=this.terms[termName].getKeyValue();
    }
    return res;
};
/**
 * 绘制条件框
 * @memberOf {meta.term}
 * @param termName
 */
meta.term.prototype.render=function(termName){
	if(termName && this.terms[termName])
		return this.terms[termName].render();
	for(var termName in this.terms){
		this.terms[termName].render();
	}
};
/**
 * 统一初始化各条件控件对象，打包数据请求，
 * 	回调函数传入参数：各条件控件值map<termName,vlaue>,附加值map<termName,[附加值数组，下拉框和下拉树可能存在]>
 * @param {Object} callBackFun(termVals,termAppendVals) 
 * @return {TypeName} 是否初始化成功
 * @memberOf {meta.term}
 */
meta.term.prototype.init=function(callBackFun){
	try{
		var cfms=[];
		for(var termName in this.terms){
			this.terms[termName].render();
//            if(this.terms[termName].dataSrcType==0){
//                this.terms[termName].init();
//                continue;
//            }
            if(this.terms[termName].initType==2){
                if(window["getCodeArrayByRemoveValue"]){
                    var codes = window["getCodeArrayByRemoveValue"](this.terms[termName].codeType,this.terms[termName].excludeValues);
                    if(codes && codes.length>0){
                        this.terms[termName].bindData(codes);
                        this.terms[termName].codeInited = true;
                        this.terms[termName].inited = true;
                    }else{
                        this.terms[termName].dataSrcType = 2;
                    }
                }else{
                    this.terms[termName].dataSrcType = 2;
                }
            }
			var index=cfms.length;
            var cfg = this.terms[termName].getConfig();
            cfg.dataRule=cfg.dataRule.split(";")[0];
            if(cfg.dataSrcType==1){
                cfg.dataRule = this.terms[termName].replaceMacroVar(cfg.dataRule);
            }
            if(cfg.dataSrcType==2){
                if(cfg.classRuleParams && typeof(cfg.classRuleParams)=="function"){
                    cfg.classRuleParams = cfg.classRuleParams();
                }
            }

            cfg.value=this.terms[termName].getValue();
            if(cfg.value && cfg.value.sort)
                cfg.value = cfg.value.join(",");
            if(this.terms[termName].constantSql){
                cfg.constantSql=this.terms[termName].constantSql;
            }
			cfms[index]=cfg;
		}
        if(cfms.length==0)return true;
		//请求服务器数据
        var thatTerm = this;
		TermControlAction.getTermsData(cfms,function(res){
			if(res==null || res=="null" || res[0]=="false"){
				alert("读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
				return false;
			}
			//绑定对象值
			for(var termName in thatTerm.terms){
                thatTerm.terms[termName]._dealServerAttribute(res[termName]);
                if(!thatTerm.terms[termName].codeInited && res[termName])
                    thatTerm.terms[termName].bindData(res[termName][1]);
                else if(thatTerm.terms[termName].dataSrcType==0 && thatTerm.terms[termName].dataRule)
                    thatTerm.terms[termName].bindData(thatTerm.terms[termName].dataRule);
                thatTerm.terms[termName].inited = true;
			}
            thatTerm.inited = true;
            if(callBackFun){
                var termVals=thatTerm.getAllValue();
                callBackFun(termVals);
            }

            thatTerm = null;
		});
		return true;
	}catch(ex){
		if(typeof ex=="string"){
			alert(ex);
		}else{
			var msg="";
			for(var key in ex)
				if(ex[key])msg+=key+":"+ex[key]+"\n";
			alert(msg);
		}
		return false;
	}
};
/**
 * 单个条件销毁方法
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype.destructor = function(){
    if(this.myCalendar){
        Destroy.destructorDHMLX(this.myCalendar);
        this.myCalendar = null;
        delete this.myCalendar;
    }
    if(this.combo){
        this.combo.clearAll(true);
        Destroy.destructorDHMLX(this.combo);
        this.combo=null;
        delete this.combo;
    }
    if(this.selTree){
        this.selTree.destructor();
        this.selTree=null;
        delete this.selTree;
    }
    this.metaTerm = null;
    delete this.metaTerm;
};
/**
 * 销毁一个请求包内所有条件
 * @memberOf {meta.term}
 */
meta.term.prototype.destructor = function(){
    for(var termName in this.terms){
        if(this.terms[termName])
            this.terms[termName].destructor();
    }
};
/**
 * 私有方法
 * 绑定树数据
 * @memberOf {meta.term.termControl}
 * @param data
 * @param rootId
 */
meta.term.termControl.prototype.bindTreeData=function(data,rootId){
    if(data.length==0)return;
    if(this.dynload==0)
        this.selTree.clearData();
    var selId=this.selTree.tree.getSelectedItemId();
    if(selId)selId=selId.split("_")[2];
    var lastNodeId="";
    this.selTree.appendData(data,this.bindDataBeforeCall);
    if(!this.dynload)
        this.selTree.tree.closeAllItems(rootId?rootId:this.selTree.tree.rootId);
    this.selTree.inited=true;
};
/**
 * 私有方法，打开树节点
 * @memberOf {meta.term.termControl}
 * @param itemId
 */
meta.term.termControl.termTreeListOpenItem=function (itemId){
    var selTree=this.selTree;
    var term=selTree.termControl;
    selTree.tree.openItem(itemId);
    return true;
};
/**
 * 私有方法
 * 条件 下拉树 单击异步刷新
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.termTreeListFlashClk=function (id,id2){
//    Debug("onClick");
    var selTree=this.selTree;
    var term=selTree.termControl;
    return meta.term.termControl.termTreeListFlash(id,"onClick",term);
};
/**
 * 私有方法
 * 条件 下拉树 选择异步刷新
 * @memberOf {meta.term.termControl}
 * @param id
 */
meta.term.termControl.termTreeListFlashSel=function (id){
//    Debug("onSelect");
    var selTree=this.selTree;
    var term=selTree.termControl;
    return meta.term.termControl.termTreeListFlash(id,"onSelect",term);
};
/**
 * 私有方法
 * 条件 下拉树 双击异步刷新
 * @memberOf {meta.term.termControl}
 * @param id
 */
meta.term.termControl.termTreeListFlashDbl=function (id){
//    Debug("onDblClick");
    var selTree=this.selTree;
    var term=selTree.termControl;
    return meta.term.termControl.termTreeListFlash(id,"onDblClick",term);
};
/**
 * 条件 下拉树 异步刷新
 * @param id
 * @param type
 * @param term
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.termTreeListFlash=function (id,type,term){
    if(!term)term=this.selTree.termControl;
    if(!term.dynload)return true;
    var selTree=term.selTree;
    if(term.mulSelect && type=="onDblClick"){
        meta.term.termControl.termTreeListSelect(id,term);
        term.selTree.tree.closeItem(id);
    }
    var itm = selTree.tree._globalIdStorageFind(id);
    if(!itm)return;
    if(itm.childsCount){
        selTree.tree.setUserData(id,"refresh","true");
    }
    if(selTree.tree.getUserData(id,"refresh")){
        if(type=="onDblClick" && !term.mulSelect){
            meta.term.termControl.termTreeListSelect(id,term);
            term.selTree.tree.closeItem(id);
        }
        return true;
    }
//    if(term.selTree.tree.hasChildren(id))return true;
    if(id==term.selTree.tree.rootId)return;
//    Debug("type:"+type+"   onOpenStart id="+id);
    selTree.tree.setUserData(id,"refresh","true");
    var thisCfm=term.getConfig();
    if(thisCfm.dataSrcType==1){
        thisCfm.dataRule=thisCfm.dataRule.split(";")[1];
        if(!thisCfm.dataRule)return;
        thisCfm.dataRule=thisCfm.dataRule.replace(new RegExp("{"+thisCfm.termName+"}","ig"),(id+"").replace(selTree._priFix,""));
        thisCfm.dataRule = term.replaceMacroVar(thisCfm.dataRule);
    }else if(thisCfm.dataSrcType==2){
        if(thisCfm.classRuleParams && typeof(thisCfm.classRuleParams)=="function"){
            thisCfm.classRuleParams = thisCfm.classRuleParams();
        }
        thisCfm.parentID = (id+"").replace(selTree._priFix,"");  //父ID
    }
//    thisCfm.value=term.getValue();
    TermControlAction.getTermData(thisCfm,function(res){ //异步刷新数据
        term.selTree.tree.openItem(id);
        term.selTree.tree.closeItem(id);
        if(res==null || res=="null" || res[0]=="false"){
            alert("读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
            return false;
        }
//        term._dealServerAttribute(res);
        term.bindTreeData(res[1],id);
        term.selTree.tree.openItem(id);
        term.bindDataCall(term);
    });
};
/**
 * 私有方法，处理后台附加数据和回填属性
 * res一般直接来自服务端返回数据是一个至少包含2个，之多包含4个元素的数组，其中：
 * res[0] 是执行成功标识，一般能调用此方法,其值都为'true'
 * res[1] 具体数据，其值是二维数组
 * res[2] 服务器端需要回填给客户端条件的改变属性值，为map对象
 * res[3] 动态树条件特有，存放默认值的Path，动态加载时会按照其Path依次初始数据
 * @memberOf {meta.term.termControl}
 */
meta.term.termControl.prototype._dealServerAttribute = function(res){
    if(res && res.sort && res.length>=3){
        var attrs_ = res[2];//暂定为map，其键合前台传入后台的config对象的键一样对应
        dhx.extend(this,attrs_,true);//覆盖对象属性（实现回填)
        if(res.length==4){
            var appendData = res[4];
            if(appendData && this.serverAppendDataCall && typeof(this.serverAppendDataCall)=="function"){
                this.serverAppendDataCall(appendData,this);
            }
        }
    }
};
/**
 * 选择下拉树值
 * @memberOf {meta.term.termControl}
 * @param id
 * @param term
 * @param unCall
 */
meta.term.termControl.termTreeListSelect=function (id,term,unCall){
    if(!term)term=this.selTree.termControl;
    if(!(term instanceof meta.term.termControl))term=term.selTree.termControl;
    var selTree=term.selTree;
    if(term.mulSelect){
        if(!term.inputObj.checkIds)term.inputObj.checkIds={};
        selTree.tree.setCheck(id,!term.inputObj.checkIds[id]);
        meta.term.termControl.termTreeCheck(id,!term.inputObj.checkIds[id],term,unCall);
    }else{
        var val=selTree.tree.getSelectedItemText();
        term.inputObj.value=val;//selTree.getItemValue(id);
        var change_ = term.inputObj.getAttribute("code")==id;
        term.inputObj.setAttribute("code",id);
        selTree.box.style.display = "none";
        if(!change_ && !unCall)
            term.valueChange();
    }
    return false;
};
/**
 * 下拉树选择刷新
 * @memberOf {meta.term.termControl}
 * @param id
 * @param state
 * @param term
 * @param unCall
 */
meta.term.termControl.termTreeCheck =function(id,state,term,unCall){
    if(!term)term=this.selTree.termControl;
    if(term.dynload)meta.term.termControl.termTreeListFlash(id,"",term);
    if(!term.inputObj.checkIds)
        term.inputObj.checkIds={};
    Destroy.clearVarVal(term.inputObj.checkIds);
    var selTree=term.selTree;
    var checkIdStr=selTree.tree.getAllChecked();
    if(!checkIdStr){
        term.inputObj.value="";
        return;
    }
    var change_ = term.inputObj.getAttribute("code")==checkIdStr;
    term.inputObj.setAttribute("code",checkIdStr);
    checkIdStr=checkIdStr.split(",");
    term.inputObj.value="";
    for(var i=0;i<checkIdStr.length;i++){
        term.inputObj.checkIds[checkIdStr[i]]=selTree.tree.getItemText(checkIdStr[i]);
        if(term.inputObj.value)
            term.inputObj.value+=","+term.inputObj.checkIds[checkIdStr[i]];
        else
            term.inputObj.value = term.inputObj.checkIds[checkIdStr[i]];
    }
    if(!change_ && !unCall)
        term.valueChange();
};