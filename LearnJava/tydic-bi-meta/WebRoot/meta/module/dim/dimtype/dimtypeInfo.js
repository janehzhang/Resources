/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        dimtype.js
 *Description：
 *        维度归并类型管理具体展示的JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        王晶
 *Finished：
 *       2011-11-07
 *Modified By：
 *
 *Modified Date:
 *
 *Modified Reasons:
 *
 ********************************************************/
/****************全局设置start*****************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();
var dimTypeTable = null; //归并类型的展示表格
var dimLevelTable = null; //类型下的层级的展示表格
var addTypeArray=null;
var updateTypeArray = null;
/****************全局设置end*****************************************/

/****************数据转换start***************************************************/
var typeConvertConfig ={
    idColumn:"dimTypeId",
    filterColumns:["dimTypeCode","dimTypeName","dimTypeDesc","dimTypeState"],
    userData:function(rowIndex,rowData){
        var userData ={};
        userData["tableId"]=rowData["dimTableId"];
        userData["typeId"]=rowData["dimTypeId"];
        return userData;
    }
}
var typeConverter = new dhtmxGridDataConverter(typeConvertConfig);
dwrCaller.addAutoAction("queryTypeByTableId",DimTypeAction.queryDimTypeByTableId,{
    dwrConfig:true,
    converter:typeConverter,
    async:false,
    isShowProcess:false
});
var levelConvertConfig ={
    idColumn:"dimLevel",
    filterColumns:["dimLevel","dimLevelName"],
    userData:function(rowIndex,rowData){
        var userData ={};
        userData["tableId"]=rowData["dimTableId"];
        userData["typeId"] = rowData["dimTypeId"];
        return userData;
    }
}
var levelConverter = new dhtmxGridDataConverter(levelConvertConfig);
dwrCaller.addAutoAction("querylevelByTypeId","DimTypeAction.queryDimLevelByTypeId",{
    dwrConfig:true,
    isShowProcess:false,
    async:false,
    converter:levelConverter
});
//更改状态的action
dwrCaller.addAutoAction("updateState","DimTypeAction.updateDimTypeState");
//保存数据的action
dwrCaller.addAutoAction("saveData","DimTypeAction.saveData");
/****************数据转换end***************************************************/

/****************公有方法start***************************************************/
//阻止冒泡事件的处理方法
function cancelBubble (event){
    var e = (event) ? event : window.event;
    if(e.preventDefault){
        e.preventDefault();
    }
    e.cancelBubble=true;
}
//去掉空格的方法
var trim = function(str){
    return str.replace(/(^\s*)|(\s*$)/g,"");
}
//清空表
var emptyTable = function(obj){
    if(obj!=null&&obj!=undefined){
        var tLen = obj.rows.length;
        var tBody = obj.tBodies[0];
        if(tBody!=null&&tBody!=undefined){
            for(var i= 1;i<tLen;i++){
                tBody.removeChild(tBody.lastChild);
            }
        }
    }
}
//获取row节点
function getParentRow(obj)
{
    var r = obj.parentNode;
    while(r!=null && r.tagName!="TR") {
        r = r.parentNode;
    }
    return r;
}
//删除元素
Array.prototype.removeByIndex = function(i){
      if (i < this.length && i >= 0) {
      var ret = this.slice(0, i).concat(this.slice(i + 1));
      this.length = 0;
      this.push.apply(this, ret);
      }
} 

/****************公有方法end*****************************************************/

/******************js方法start**********************************************************************/
   /**
    * 生成归并类型的表格
    * @parma: typeObj 归并类型的对象
    * @parma: rowIndex 列序号
    * @parma: flag 是否为新增
    */
  function creartTypeRow(typeObj,rowIndex,flag){
	  var row = document.createElement("tr");
	  dimTypeTable.tBodies[0].appendChild(row);
	  rowIndex = rowIndex+1;
	  row.onclick = function(){
		  changeBgColor(this,flag,typeObj.typeId);
	  }
	  if(typeObj!=null){
		  for(var i = 0 ;i<4;i++){
			  var cell= document.createElement("td");
			  row.appendChild(cell);
			  if(i==0){
				  cell.innerHTML=rowIndex;
			  }
			  if(i==1&&typeObj.dimTypeName!=null){
				  cell.innerHTML ="<div><input id='typeName"+rowIndex+"' type='text' value='"+typeObj.dimTypeName+"' style='width:180px;' onclick='cancelBubble()' onkeyup='addTypeRow(this,"+rowIndex+",true)' onfocus='cancelBubble();changeBgColor(this,"+flag+","+typeObj.typeId+")' onblur='cancelBubble();changeValue(this,"+rowIndex+","+flag+",-1,0)'/></div>";
			  }
			  if(i==1&&typeObj.dimTypeName==null){
				  cell.innerHTML ="<div><input id='typeName"+rowIndex+"' type='text' style='width:180px;' onclick='cancelBubble()' onkeyup='addTypeRow(this,"+rowIndex+",true)' onfocus='cancelBubble();changeBgColor(this,"+flag+","+typeObj.typeId+")' onblur='cancelBubble();changeValue(this,"+rowIndex+","+flag+",-1,0)'/></div>";
			  }
			  if(i==2&&typeObj.dimTypeDesc!=null){
                  cell.innerHTML ="<div><input id='typeDes"+rowIndex+"' type='text' value='"+typeObj.dimTypeDesc+"' style='width:380px;' onclick='cancelBubble()' onkeyup='addTypeRow(this,"+rowIndex+",true)' onfocus='cancelBubble();changeBgColor(this,"+flag+","+typeObj.typeId+")' onblur='cancelBubble();changeValue(this,"+rowIndex+","+flag+",-1,1)'/></div>";    
		      }
			  if(i==2&&typeObj.dimTypeDesc==null){
				   cell.innerHTML ="<div><input id='typeDes"+rowIndex+"' type='text'  style='width:380px;' onclick='cancelBubble()' onkeyup='addTypeRow(this,"+rowIndex+")' onfocus='cancelBubble();changeBgColor(this,"+flag+","+typeObj.typeId+")' onblur='cancelBubble();changeValue(this,"+rowIndex+","+flag+",-1,1)'/></div>";
			  }
			  if(i==3){
				  if(typeObj.state==0){
					 cell.innerHTML= "<a href='#'onclick='cancelBubble();changestate("+tableId+","+typeObj.typeId+",1,"+rowIndex+")'>"+getNameByTypeValue("DIM_TYPE_STATE",1)+"</a>";
				  }
				  if(typeObj.state==1){                 
                     cell.innerHTML= "<a href='#' onclick='cancelBubble();changestate("+tableId+","+typeObj.typeId+",0,"+rowIndex+")'>"+getNameByTypeValue("DIM_TYPE_STATE",0)+"</a>";
                  }
				  if(typeObj.state==null){
                	  cell.innerHTML= "<div>&nbsp;&nbsp;</div>";
                  }
	         }
        }
		dhtmlxValidation.addValidation(row,[
             {target:"typeName" + rowIndex,rule:"NotEmpty,MaxLength[32]"},
             {target:"typeDes" + rowIndex,rule:"MaxLength[512]"}
        ]);
	 }
 }
 /***
  * 新增一行归并类型
  * @parma obj 事件的节点
  * @parma rowIndex 列号
  */
  function addTypeRow(obj,rowIndex,flag){
	  if(obj!=null){
		  var nextTr = obj.parentNode.parentNode.parentNode.nextSibling;
		  if(nextTr==null){
			  var typeObj = {};
		      typeObj.dimTableId= tableId;
		      typeObj.typeId  = tableId+"|"+rowIndex;
		      typeObj.dimTypeName = null ;
		      typeObj.dimTypeDesc = null;
		      typeObj.dimTypeCode = addTypeArray.length+1;
		      typeObj.dimTypeState = 1;
		      typeObj.level = new Array();
		      addTypeArray.push(typeObj);
		      creartTypeRow(typeObj,rowIndex,flag);
	      }
		 var currTr = obj.parentNode.parentNode.parentNode;
		 if(currTr.lastChild.innerHTML=="<DIV>&nbsp;&nbsp;</DIV>"||currTr.lastChild.innerHTML=="<div>&nbsp;&nbsp;</div>"){
	        currTr.lastChild.innerHTML="<a href='#' onclick='cancelBubble();deleteRow(this,0,"+rowIndex+",0)'>删除该行</a>";
	     }
	 }else{
		  var typeObj = {};
		  typeObj.dimTableId= tableId;
		  typeObj.typeId  = tableId+"|"+rowIndex;
		  typeObj.dimTypeName = null ;
		  typeObj.dimTypeDesc = null;
		  typeObj.dimTypeState =1;
		  typeObj.dimTypeCode = addTypeArray.length+1;
		  typeObj.level = new Array();
		  addTypeArray.push(typeObj);
		  creartTypeRow(typeObj,rowIndex,flag);
	 }
  }
  /**
    * 生成归并类型层级的表格
    * @parma: obj 层级的对象
    * @parma: rowIndex 序号
    * @parma: levelArr 层级对象
    */
   function createLevelRow(obj,rowIndex,typeId,isAdd,typeIndex){
	      if(obj==null){
	    	  obj = {};
	    	  obj.tableId = tableId;
              obj.typeId =  typeId;
              obj.levelId = null;
              obj.levelName = null;
              obj.levelIsDelete = null;
              if(isAdd==false){
            	  updateTypeArray[typeIndex].level.push(obj);
              }else{
            	  addTypeArray[typeIndex].level.push(obj);
              }
	       }
			  var row = document.createElement("tr");
			  rowIndex = rowIndex+1;
	          dimLevelTable.tBodies[0].appendChild(row);
	          if(row.nextSibling!=null){
	        	  dimLevelTable.tBodies[0].removeChild(row.nextSibling);
	          }
	          for(var i=0;i<3;i++){
	        	  var cell = document.createElement('td');
	              row.appendChild(cell);
	              if(i==0){
	                cell.innerHTML = rowIndex;
	              }
	              if(i==1){
	            	  if(obj.levelName!=null){
	            		  cell.innerHTML ="<div><input id='levelName"+rowIndex+"' type='text' value='"+obj.levelName+"' style='width:180px;' onkeyup='insertLevelRow(this,"+rowIndex+","+typeId+","+isAdd+","+typeIndex+","+obj.levelIsDelete+")' onblur='cancelBubble();changeValue(this,"+rowIndex+","+isAdd+","+typeIndex+",0)'/></div>";
	            	  }else{
	            		  cell.innerHTML="<div><input id='levelName"+rowIndex+"' type='text'  value='' style='width:180px;' onkeyup='insertLevelRow(this,"+rowIndex+","+typeId+","+isAdd+","+typeIndex+","+obj.levelIsDelete+")' onblur='cancelBubble();changeValue(this,"+rowIndex+","+isAdd+","+typeIndex+",0)'/></div>";
	            	  }
	              }
	              if(i==2&&obj.levelIsDelete==false){
	            	 cell.innerHTML= "<div>&nbsp;&nbsp;</div>"; 
	              }
	              if(i==2&&obj.levelIsDelete){
	            	 cell.innerHTML="<a href='#' onclick='cancelBubble();deleteRow(this,1,"+typeIndex+","+rowIndex+")'>删除该行</a>"; 
	              }
	              if(i==2&&obj.levelIsDelete==null){
	            	 cell.innerHTML= "<div>&nbsp;&nbsp;</div>"; 
	            	 obj.levelIsDelete=true;
	              }
	          }
	          dhtmlxValidation.addValidation(row,[
                   {target:"levelName" + rowIndex,rule:"NotEmpty,MaxLength[18]"}
              ]);
   }
   /**
    * 插入归并类型层级的行
    * @parma: obj 触发的对象
    * @parma: rowIndex 序号
    * @parma: levelArr 层级对象
    */
   function  insertLevelRow(obj,rowIndex,typeId,isAdd,typeIndex,isDetele){
	    if(obj!=null&&obj.value!=null){
	       var nextTr = obj.parentNode.parentNode.parentNode.nextSibling;
	       if(nextTr==null){
	    	  createLevelRow(null,rowIndex,typeId,isAdd,typeIndex);
	       }
	       var currTr = obj.parentNode.parentNode.parentNode;
		   if((currTr.lastChild.innerHTML=="<DIV>&nbsp;&nbsp;</DIV>"||currTr.lastChild.innerHTML=="<div>&nbsp;&nbsp;</div>")&&(isDetele==true||isDetele==null)){
			  if(isAdd==false){
	            currTr.lastChild.innerHTML="<a href='#' onclick='cancelBubble();deleteRow(this,1,"+typeIndex+","+rowIndex+")'>删除该行</a>";
	          }
			  if(isAdd==true){
				currTr.lastChild.innerHTML="<a href='#' onclick='cancelBubble();deleteRow(this,0,"+typeIndex+","+rowIndex+")'>删除该行</a>";
			  }
	       }
	    }
   }
   /**
     * 点击或者生成归并类型的层级
     * @parma: levelArr 层级对象
     * @parma: typeId 类型的id
     */
  function showLevelTable(typeId,isAdd,typeIndex){
	  var levelArr = null;
	  if(isAdd==false){
		  levelArr = updateTypeArray[typeIndex].level;
	  }else{
		  levelArr = addTypeArray[typeIndex].level;
	  }
	  if(levelArr!=null){
		  if(levelArr.length==0){
			  obj = {};
	    	  obj.tableId = tableId;
              obj.typeId =  typeId;
              obj.levelId = null;
              obj.levelName = null;
              obj.levelIsDelete = null;
              levelArr.push(obj);
			  createLevelRow(obj,0,typeId,isAdd,typeIndex);
		  }else{
			  for(var i = 0;i<levelArr.length;i++){
				  var obj = levelArr[i];
				  if(obj!=null&&obj.levelName!=null){
				    createLevelRow(obj,i,typeId,isAdd,typeIndex);
				  }else{
					  obj=null;
					  levelArr.removeByIndex(i);
				  }
			  }
			  obj = {};
	    	  obj.tableId = tableId;
              obj.typeId =  typeId;
              obj.levelId = null;
              obj.levelName = null;
              obj.levelIsDelete = null;
              levelArr.push(obj);
			  createLevelRow(obj,levelArr.length-1,typeId,isAdd,typeIndex);
		  }
	  }
  }
  /**
     * 改变背景色,加载层级表格
     * @parma: 当前对象或者当前对象的tr对象
     * @parma: isAdd 是否新增的归并类型
   */
   function changeBgColor(obj,isAdd,typeId){
	    emptyTable(dimLevelTable);
	    var rows = dimTypeTable.getElementsByTagName('tr');
	    if(obj.tagName!="TR"){
	    	obj=obj.parentNode.parentNode.parentNode;
	    }
	     for(var rowLen = 0 ;rowLen<dimTypeTable.rows.length;rowLen++){
            rows[rowLen].style.backgroundColor="#fff";
        }
	    obj.style.backgroundColor="#FFE7B2";
	    var rowIndex = obj.cells[0].innerHTML;
	    if(isAdd==false){
	    	var levelArr = updateTypeArray[rowIndex-1].level;
	    	showLevelTable(typeId,isAdd,rowIndex-1);
	    }else{
	    	var existLen = updateTypeArray.length;
	    	var levelArr = addTypeArray[rowIndex-1-existLen].level;
	    	showLevelTable(typeId,isAdd,rowIndex-1-existLen);
	    }
   }
    /**
     * 改变值
     * @parma: 当前对象或者当前对象的tr对象
     * @parma: isAdd 是否新增的归并类型
   */
   function changeValue(obj,rowIndex,isAdd,typeIndex,flag){
	  if(isAdd==false){
		  if(typeIndex==-1&&flag==0){
			  updateTypeArray[rowIndex-1].dimTypeName=obj.value;
		  }
		  if(typeIndex==-1&&flag==1){
			  updateTypeArray[rowIndex-1].dimTypeDesc=obj.value;
		  }
		  if(typeIndex!=-1){
			  updateTypeArray[typeIndex].level[rowIndex-1].levelName=obj.value;
		  }
	  }else{
		  var existLen = updateTypeArray.length;
		  if(typeIndex==-1&&flag==0){
			  addTypeArray[rowIndex-1-existLen].dimTypeName=obj.value;
		  }
		  if(typeIndex==-1&&flag==1){
			   addTypeArray[rowIndex-1-existLen].dimTypeDesc=obj.value; 
		  }
		  if(typeIndex!=-1){
			  addTypeArray[typeIndex].level[rowIndex-1].levelName=obj.value;
		  }
	  }
   }
   
   function changestate(tableId,typeId,flag,rowIndex){
    cancelBubble();
    if(flag==0){
        dhx.confirm("您确定停用当前归并类型?",function(b){
            if(b){
                dwrCaller.executeAction("updateState",{tableId:tableId,typeId:typeId,flag:flag},function(data){
                    if(data){
                        dimTypeTable.rows[rowIndex].lastChild.innerHTML= "<a href='#' onclick='cancelBubble();changestate("+tableId+","+typeId+",1,"+rowIndex+")'>启用</a>";
                        changeBgColor(dimTypeTable.rows[rowIndex],false,typeId);
                        window.parent&&window.parent.loadDimTypeInfo&&window.parent.loadDimTypeInfo();
                    }
                })
            }
        })
    }
    if(flag==1){
        dhx.confirm("您确定启用当前归并类型?",function(b){
            if(b){
                dwrCaller.executeAction("updateState",{tableId:tableId,typeId:typeId,flag:flag},function(data){
                    if(data){
                        dhx.alert("操作成功");
                        dimTypeTable.rows[rowIndex].lastChild.innerHTML= "<a href='#'onclick='cancelBubble();changestate("+tableId+","+typeId+",0,"+rowIndex+")'>停用</a>";
                        changeBgColor(dimTypeTable.rows[rowIndex],false,typeId);
                        window.parent&&window.parent.loadDimTypeInfo&&window.parent.loadDimTypeInfo();
                    }
                })
            }
        })
    }
}
   
function deleteRow(obj,flag,typeIndex,levelIndex){
    cancelBubble();
    var tr = getParentRow(obj);
    if(tr){
        //tr.parentNode.removeChild(tr);
        tr.style.display="none";
    }
    if(flag==0&&levelIndex==0){ //删除新增的归并类型,包括归并类型下的层级都被删除
    	var existLen = updateTypeArray.length;
        addTypeArray[typeIndex-1-existLen]=null;
        emptyTable(dimLevelTable);
    }
    if(flag==0&&levelIndex!=0){ //删除新增的归并类型下层级的情况
        var levelArr = addTypeArray[typeIndex].level;
        levelArr[levelIndex-1]=null;
    }
    if(flag==1){  //删除原有的归并类型下新增的层级
        var levelArr = updateTypeArray[typeIndex].level;
        levelArr[levelIndex-1]=null;
    }
}
/******************js方法end**********************************************************************/

/****************加载数据start*************************************************************/
   /**
    * 查询当前维度表的归并类型
    * @parma: tableId 当前维度表的id
    */
   function queryDimType(tableId){
	   if(tableId==0){
		   dhx.alert("对不起,请先选择你要查询的维度表");
	   }else{
		    addTypeArray = new Array();
		    updateTypeArray = new Array();
		    dwrCaller.executeAction("queryTypeByTableId",{tableId:tableId},function(data){
		    	if(data!=null&&data!=undefined){
		    		var dataLen = data.rows.length;
		    		if(dataLen!=0){
                       for(var i = 0;i<dataLen;i++){
                    	   var userdata= data.rows[i].userdata;
                    	   var rowdata = data.rows[i].data;
                    	   var typeObj = {};
                    	   typeObj.dimTableId= userdata.tableId;
                    	   typeObj.typeId  = userdata.typeId;
                    	   typeObj.dimTypeName = rowdata[1] ;
                    	   typeObj.dimTypeDesc = rowdata[2];
                    	   typeObj.state = rowdata[3];
                    	   typeObj.level = new Array();
                    	   updateTypeArray.push(typeObj);
                    	   if(tableId!=0&&userdata.typeId!=0){
                    		    queryDimLevel(tableId,userdata.typeId,typeObj.level);
                    	   }
                       }
		    		}
		    	}// END DATA
		    }); //end ajax type
	   }
   }
    /**
    * 查询当前维度表的归并类型下的层级
    * @parma: tableId 当前维度表的id
    * @parma: typeId  归并类型的id
    * @parma: arrobj  数组用于装载层级对象
    */
   
   function queryDimLevel(tableId,typeId,arrobj){
	   dwrCaller.executeAction("querylevelByTypeId",{tableId:tableId,typeId:typeId},function(data){
		    if(data!=null&&data!=undefined){
              if(data.rows.length!=null&&data.rows.length!=0){
            	   for(var i = 0;i<data.rows.length;i++){
            		   var levelObj = {};
            		   var rowdata = data.rows[i].data;
            		   levelObj.tableId = tableId;
                       levelObj.typeId = typeId;
                       levelObj.levelId = rowdata[0];
                       levelObj.levelName = rowdata[1];
                       levelObj.levelIsDelete = false;
                       arrobj.push(levelObj);
            	   }
               }
            }
	   }); //end levelAjax;
   }
   
   /**
 * 提交数据之前进行验证,验证框架的验证方法
 **/
var validate = function(){
    var validateRes=true;
    var tableObj = dimTypeTable;
    for(var i = 1; i < tableObj.rows.length - 1; i++){
        validateRes=dhtmlxValidation.validate(tableObj.rows[i])&&validateRes;
    }
    return validateRes;
}
/**
 * 提交之前,验证输入类型与层级是否有错,用于提示信息
 */
var showValidateInfo = function(arrObj,isAdd){
    var isSuccess = true;
    if(arrObj!=null&&arrObj!=undefined){
        for(var i = 0 ;i<arrObj.length;i++){
            var obj = arrObj[i];
            if(obj!=null&&obj.dimTypeName!=null){
                var typeName = obj.dimTypeName;
                if(typeName!=null&&typeName.length>32){
                    dhx.alert("归并类型:"+typeName+"的名称长度超出了验证的长度");
                    return false;
                }
                var typeDes = obj.dimTypeDesc;
                if(typeDes!=null&&typeDes.length>512){
                    dhx.alert("归并类型:"+typeDes+"的描述长度超出了验证的长度");
                    return isSuccess&&false;
                }
                var levelArr = obj.level;
                if(levelArr!=null&&levelArr!=undefined){
                   if(levelArr.length==0&&isAdd==false){
                	  dhx.alert("归并类型"+typeName+"下必须有一个层级");
                      return isSuccess&&false;
                   }
                   else if(levelArr.length==1&&isAdd){
                	   dhx.alert("归并类型"+typeName+"下必须有一个层级");
                       return isSuccess&&false;
                   }
                   else if(levelArr.length>0&&isAdd==false){  //判断当前已有归并类型下已经有的层级的名称的长度
                	   var nullCount = 0 ;
                	   for(var j = 0 ;j<levelArr.length;j++){
                		   var levelObj = levelArr[j];
                		   if(levelObj!=null&&levelObj.levelName!=null&&levelObj.levelName.length>32){
                			    dhx.alert("归并类型"+typeName+"下的一个层级"+levelObj.levelName+"超过了字符长度");
                			    return isSuccess&&false;
                		   }
                		   if(levelObj==null){
                			   nullCount++;
                		   }
                	   }
                	   if(nullCount==levelArr.length){
                		   dhx.alert("归并类型"+typeName+"下必须有一个层级");
                           return isSuccess&&false;
                	   }
                   }
                   else if(levelArr.length>1&&isAdd){
                	   var nullCount = 1 ;
                	   for(var j = 0 ;j<levelArr.length;j++){
                		   var levelObj = levelArr[j];
                		   if(levelObj!=null&&levelObj.levelName!=null&&levelObj.levelName.length>32){
                			    dhx.alert("归并类型"+typeName+"下的一个层级"+levelObj.levelName+"超过了字符长度");
                			    return isSuccess&&false;
                		   }
                		   if(levelObj==null){
                			   nullCount++;
                		   }
                	   }
                	   if(nullCount==levelArr.length){
                		   dhx.alert("归并类型"+typeName+"下必须有一个层级");
                           return isSuccess&&false;
                	   }
                   }
                }
            }
        }
        return isSuccess&&true;
    }else
    {
        return false;
    }

}
var saveData = function(){
    if(showValidateInfo(addTypeArray,true)&&showValidateInfo(updateTypeArray,false)){
        dwrCaller.executeAction("saveData",{addType:addTypeArray,updateType:updateTypeArray},function(data){
            if(validate()&&data){
                dhx.alert("操作成功");
                emptyTable(dimTypeTable);
                queryDimType(tableId);
				    if(updateTypeArray!=null&&updateTypeArray.length!=0){
				    	for(var i = 0 ;i<updateTypeArray.length;i++){
				    		var obj = updateTypeArray[i];
				    		creartTypeRow(obj,i,false);
				    	}
				    	addTypeRow(null,updateTypeArray.length,true);
				    	var typeObj = updateTypeArray[0];
				    	changeBgColor(dimTypeTable.rows[1],false,typeObj.typeId);
				    }else{
				    	addTypeRow(null,0,true);
				    }
                //如果是嵌入到标签页中的修改，需要标签主题重新加载归并类型信息。
                window.parent&&window.parent.loadDimTypeInfo&&window.parent.loadDimTypeInfo();
            }else{
                dhx.alert("对不起,操作失败");
            }
        });
    }
}
/****************加载数据end**********************************************************/

/***********************************init函数start***************************************************************************/
var initPage = function(){
    dimTypeTable = document.getElementById("dimtypetable"); //归并类型的展示表格
    dimLevelTable = document.getElementById("dimleveltable"); //类型下的层级的展示表格
    var typeLayout = new dhtmlXLayoutObject("main", "2E");
    typeLayout.cells("a").hideHeader();
    typeLayout.cells("b").hideHeader();
    typeLayout.cells("a").setHeight(240);
    typeLayout.cells("a").attachObject("dimtype");
    typeLayout.cells("b").attachObject("dimlevel");
    typeLayout.hideSpliter();
    //设置CSS
    dhx.html.addCss($("dimtype"),global.css.gridTopDiv);
    dhx.html.addCss($("dimlevel"),global.css.gridTopDiv);
    var sumbit = Tools.getButtonNode("保存");
    $("_button").appendChild(sumbit);
    sumbit.style.marginLeft = (Math.round(($("_button").offsetWidth - 100) / 2)) + "px";
    sumbit.style.styleFloat = "right";
    sumbit.onclick = saveData;
    queryDimType(tableId);
    if(updateTypeArray!=null&&updateTypeArray.length!=0){
    	for(var i = 0 ;i<updateTypeArray.length;i++){
    		var obj = updateTypeArray[i];
    		creartTypeRow(obj,i,false);
    	}
    	addTypeRow(null,updateTypeArray.length,true);
    	var typeObj = updateTypeArray[0];
    	changeBgColor(dimTypeTable.rows[1],false,typeObj.typeId);
    }else{
    	addTypeRow(null,0,true);
    }
}
dhx.ready(initPage);