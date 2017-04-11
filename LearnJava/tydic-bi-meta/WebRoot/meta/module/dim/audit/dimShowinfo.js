/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       dimauditinfo.js
 *Description：
 *       审核明细js
 *Dependent：
 *       head.jsp dhtmlx.js dwr
 *Author:
 *       程钰
 *Date：
 *       12-1-31
 ********************************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();
var mygrid = null;
var treeData = [];
var popWindow = null;
var initColData = ["ID","PAR_ID","CODE","NAME","DESC","DIM_TYPE_ID","STATE","DIM_LEVEL","MOD_FLAG","ORDER_ID","DIM_TABLE_ID"];
var modifyStyle = {
    4:"color:#64B201;",
    6:"color:#63B8FF",
    3:"color:#EEC900",
    1:"color:#7A378B",
    5:"color:red",
    clear:"font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;" ,
    cancelStopUse:"color: #F29FB5;"
}
/**
 * 维表字段信息
 */
var dymaicColData=null;
var columnDatas=[];
var dymaicColNames=[];
var dyColfilterName="";
var initColData = ["ID","PAR_ID","CODE","NAME","DESC","DIM_TYPE_ID","STATE","DIM_LEVEL","MOD_FLAG","ORDER_ID","DIM_TABLE_ID"];
//归并类型数据
var dimTypeInfo={};
var path={};

/**
 * dimAudit树Data转换器定义
 */
var dimTreeDataConfig = {
    isDycload:false,
    isFormatColumn:false,
    userData:function(rowIndex, rowData){
        return {rowData:rowData};
    },
    getNodeText:function(rowIndex, rowData){
        return  rowData[(tableDimPrefix+"_CODE").toUpperCase()]+"--"+rowData[this.textColumn];
    },
    //否打开节点（默认不打开）
    isOpen:function(){
        return true;
    },
    onBeforeConverted:function(data){
        if(data){

            var list = [];
            var modList = [];
            var hasName=false;
            for (var i = 0;i<data.length;i++){
                if(data[i]["HIS"] != 2 && data[i]["HIS"] != 3){
                	for(var j = 0;j<list.length;j++){
                		if(list[j][tableDimPrefix.toUpperCase()+"_ID"] == data[i][tableDimPrefix.toUpperCase()+"_ID"]){
                			hasName = true;
                			list[j]["MOD_FLAG"] = list[j]["MOD_FLAG"] == -1?data[i]["MOD_FLAG"]:list[j]["MOD_FLAG"];
                		}else{
                			hasName =false;
                		}
                	}
                	if(!hasName){
                		list.push(data[i])
                	}
                	
                    
                }
            }
        }
        return list;
    }
    ,afterCoverted:function(data){
        if(data&&data.length>0){
            for(var i = 0; i < data.length; i ++){
                path[data[i].id]=(path[data[i]._pid_temp_]?(path[data[i]._pid_temp_]+"-->"):"")
                        +data[i].userdata[0].content[tableDimPrefix.toUpperCase()+"_NAME"];
                arguments.callee.call(this,data[i].item);
            }
        }
    }

}

var dimGridDataConfig = {
    isFormatColumn:false,
    filterColumns:["MOD_FLAG","USER_NAMECN","AUDIT_FLAG","PATH_NAME",tableDimPrefix.toUpperCase()+"_CODE",tableDimPrefix.toUpperCase()+"_NAME",tableDimPrefix.toUpperCase()+"_DESC"],
    onBeforeConverted:function(data){
        if(data){
            var list = [];
            for(var i = 0;i<data.length;i++){
                if(data[i]["HIS"] != 4){
                    list.push(data[i]);
                }
            }
        }
        return list;
    },
    cellDataFormat:function(rowIndex,columnIndex,columnName,cellValue,rowData){
    	var mapping={
    		1:"修改编码",
    		3:"层次变更",
    		4:"新增编码",
    		5:"禁用编码",
    		6:"归并编码",
    		7:"启用编码"
    	}
         if(columnName == 'MOD_FLAG'){
        	 return (rowData.MOD_FLAG+"").replace(/(\d)/g,function($1,$2){
        		 return mapping[$2];
        	 });
        }else if(columnName == "AUDIT_FLAG"){
        	if(rowData["AUDIT_FLAG"] == 1){
        		return "通过";
        	}else if(rowData["AUDIT_FLAG"] == 2){
        		return "不通过";
        	}else{
        		return "----";
        	}
        }else{
            if(columnName=="PATH_NAME"&&rowData['HIS']==1){//找到改变之后的编码路径
                cellValue=path[rowData[tableDimPrefix.toUpperCase()+"_ID"]];
            }
            if(rowData['HIS']!=1){ //原生数据设置样式
                return {style:"color: #808080;font-style: italic;text-decoration: line-through",value:cellValue};
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    userData:function(rowIndex ,rowData){
        return rowData;
    },
    getRowID:function(rowIndex,rowData){
        if(rowData.HIS!=1){ //原生维表数据ID。
            return rowData[tableDimPrefix.toUpperCase()+"_ID"];
        }else{
            //历史表中数据列RowID
            return (rowData[tableDimPrefix.toUpperCase()+"_ID"]+"")+1;
        }
    }
}
var dimDataConverter = new dhtmxGridDataConverter(dimGridDataConfig);

var dimTreeDataConverter=new dhtmxTreeDataConverter(dimTreeDataConfig);

//初始化grid
var initPage = function(){
    //var audit
    var typeLayout = new dhtmlXLayoutObject("container", "2U");
    var leftLayout = new dhtmlXLayoutObject(typeLayout.cells("a"), "2E");
    var rightLayout = new dhtmlXLayoutObject(typeLayout.cells("b"), "2E");
    typeLayout.cells("b").hideHeader();
    typeLayout.cells("a").setWidth(300);
    typeLayout.cells("a").fixSize(false, true);
    typeLayout.hideConcentrate();
    typeLayout.hideSpliter();
    leftLayout.cells("a").setText("<div style='margin-top: -3px'><span style='font-weight: bolder;'>" +
                                  "归并类型：</span><select id='_dimType' style='width: 200px;height: 20px'></select></div>");
    leftLayout.cells("a").setHeight(600);
    leftLayout.cells("a").fixSize(false,false);
    leftLayout.cells("b").hideHeader();
    leftLayout.hideConcentrate();
    leftLayout.hideSpliter();
    //查询归并类型数据
    DimAuditNewAction.queryDimTypeOnModify(batchId,false,{
        callback:function(data){
            var dimTypeSelect=$("_dimType");
            dimTypeInfo={};
            data=new columnNameConverter().convert(data);
            for(var i = 0; i < data.length; i++){
                dimTypeInfo[data[i].dimTypeId] = data[i];
                dimTypeSelect.options[i]=new Option(data[i].dimTypeName,data[i].dimTypeId);
            }
            dimTypeInfo.length = data.length;
        },
        async:false
    });

    rightLayout.cells("a").hideHeader();
    rightLayout.cells("b").hideHeader();
    rightLayout.cells("a").setHeight(document.body.offsetHeight*8/10);
    rightLayout.hideConcentrate();
    rightLayout.hideSpliter();
    /**
     * 查询维度表列信息，分析其动态字段。
     */
    DimAuditNewAction.queryColDatas(dimTableId,{
        callback:function(data){
            columnDatas=new columnNameConverter().convert(data);
            dymaicColData = dynamicCols(tableDimPrefix, columnDatas);
            for(var i = 0; i < dymaicColData.length; i++){
                dymaicColNames.push(dymaicColData[i].colName);
                //dyColfilterName += Tools.tranColumnToJavaName(dymaicColData[i].colName);
                dimDataConverter.filterColumns.push(dymaicColData[i].colName.toUpperCase());
            }
        },
        async:false
    });

    if(tableDimPrefix){
        dimTreeDataConverter.setIdColumn((tableDimPrefix + "_ID").toUpperCase());
        dimTreeDataConverter.setPidColumn((tableDimPrefix + "_PAR_ID").toUpperCase());
        dimTreeDataConverter.setTextColumn((tableDimPrefix + "_NAME").toUpperCase());
    }
    dimTreeDataConverter.addItemConfig = function(rowIndex, rowData){
        var rs;
        if(rowData.MOD_FLAG){//说明此数据有做改动
            rs={style:modifyStyle[(rowData.MOD_FLAG).toString().split(",")[0]]}
        } else{
            rs= Tools.EmptyObject;
        }
        return rs;
    }
    var tree = leftLayout.cells("a").attachTree();
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableSmartCheckboxes(false);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.enableCheckBoxes(false);

    dwrCaller.addAction("queryDimTreeData",function(afterCall, param){
        DimAuditNewAction.queryDimInfoData(tableName,tableDimPrefix,tableOwner,dwr.util.getValue("_dimType"),batchId,dymaicColNames,function(data){
            treeData[1] =	data;
            treeData[2] = data;
            mygrid.clearAll();
            var childs=tree.getAllSubItems(0);
            if(childs){
                childs = childs+",";
                var childIds=childs.split(",");
                for(var i=0;i<childIds.length;i++){
                    tree.deleteItem(childIds[0]);
                }
            }
            tree.loadJSONObject(dimTreeDataConverter.convert(treeData[1]));
            mygrid.parse(dimDataConverter.convert(treeData[2]),"json");
        })
    })
    dwrCaller.addDataConverter("queryDimTreeData",dimTreeDataConverter);
    leftLayout.cells("b").attachObject('content');
    var header="操作类型,审核人,审核结果,父编码,编码,编码名称,编码描述";
    var alginStr = "center,center,center,center,center,center,center";
    var colTypeStr = "ro,ro,ro,ro,ro,ro,ro";
    var colSortStr = "na,na,na,na,na,na,na";
    var initP="8,10,10,16,16,20,15";
    for(var i = 0; i < dymaicColData.length; i++){
        //动态显示列：如果中文名为空时，显示其字段名称。
        if(dymaicColData[i].colNameCn){
            header += ","+dymaicColData[i].colNameCn;
        }
        else{
            header +=","+ dymaicColData[i].colName;
        }
        alginStr += ",left";
        colTypeStr += ",ro";
        colSortStr += ",na";
        initP+=",10";
    }
    mygrid = rightLayout.cells("a").attachGrid();
    mygrid.setHeader(header);
    mygrid.setInitWidthsP(initP);
    mygrid.setHeaderAlign(alginStr);
    mygrid.setColAlign(alginStr);
    mygrid.setColTypes(colTypeStr);
    mygrid.setColSorting(colSortStr);
    mygrid.init();
    dwrCaller.executeAction("queryDimTreeData");
    mygrid._erspan=true;
    mygrid.attachEvent("onXLE",function(grid_obj,count){
        for(var i=0;i<mygrid.getRowsNum();i+=2){
            mygrid.setRowspan(mygrid.getRowId(i),1,2);
            mygrid.setRowspan(mygrid.getRowId(i),0,2);
        }
    });
    mygrid.enableMultiselect(true);
//    mygrid.entBox.style.marginTop = "-5px";
    //为归并类型添加事件
    $("_dimType").onchange=function(){
        dwrCaller.executeAction("queryDimTreeData");
    }
    rightLayout.cells("b").attachObject($("_dimTableForm"));
    //表头checkbox与数据中表头checkbox之间的联动。
//    $("_headCheckBox").onchange=function(){
//        var state=dwr.util.getValue("_headCheckBox");
//        var selectBox=document.getElementsByName("selectBox");
//        for(var i=0;i<selectBox.length;i++){
//            selectBox[i].checked=state;
//        }
//    }
    var getModMark = function(id){
        	for(var i = 0;i< treeData[2].length;i++){
        		if(treeData[2][i][tableDimPrefix.toUpperCase()+"_ID"] == id && treeData[2][i]["HIS"] == 1){
        			return treeData[2][i]["MOD_MARK"];
        		}else  if(treeData[2][i]["HIS"] == 4 && treeData[2][i][tableDimPrefix.toUpperCase()+"_ID"] == id){
        			return "";
        		}
        	}
        };
    
    //树与Grid之间的联动
    tree.attachEvent("onClick", function(id){
        mygrid.clearSelection();
        mygrid.selectRowById(id,true);
        mygrid.selectRowById(id+""+1,true);
        $("_auditMark").value = getModMark(id);
    });
    
    mygrid.attachEvent("onRowSelect", function(id,ind){
        var hisFlag=mygrid.getRowData(id).userdata.HIS;
        if(hisFlag==1){
            id=(id+"").substr(0,(id+"").length-1);
            mygrid.selectRowById(id,true);
             $("_auditMark").value = getModMark(id);
        }else{
            mygrid.selectRowById(id+""+1,true);
            $("_auditMark").value = getModMark(id);
        }
        tree.clearSelection();
        tree.selectItem(id);
        tree.focusItem(id);
    });


}
/**
 * 返回动态字段列
 */
var dynamicCols = function(prefix, columnDatas){
    var dynamicColData = [];
    for(var j = 0; j < columnDatas.length; j++){
        var columnName = columnDatas[j].colName;
        var isInitCol = false;//是否是系统固定列
        for(var i = 0; i < initColData.length; i++){
            if(i < 5){
                if((prefix + "_" + initColData[i]).toUpperCase() == columnName.toUpperCase()){
                    isInitCol = true;
                    break;
                }
            } else{
                if(initColData[i].toUpperCase() == columnName.toUpperCase()){
                    isInitCol = true;
                    break;
                }
            }
        }
        if(!isInitCol){
            dynamicColData.push(columnDatas[j]);
        }
    }
    return dynamicColData;
}

dhx.ready(initPage);
