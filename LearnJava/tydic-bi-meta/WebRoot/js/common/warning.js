var indexCdColWaring;//预警集合
//预警
function loadIndexCdWarn(menuName){
  ReportConfigAction.getReportWarnList(menuName,{
        async:false,
        callback:function(mapList) {
            indexCdColWaring = mapList;
        }
    });
}
function addWaringStyle(dataValue,indexCd,col)
{
	//判断是否需要预警
	var isWaringFlag = "";
	    isWaringFlag = isWaring(indexCdColWaring,dataValue,indexCd,col);
	var fontStyle_s = "";//预警的颜色s
	var fontStyle_e = "";//预警的颜色e
	if(isWaringFlag != null && isWaringFlag != ""){
	    fontStyle_s = "<font style=\"background-color:red;\" title=\"预警:"+isWaringFlag+"\">&nbsp;&nbsp;";
	    fontStyle_e = "&nbsp;&nbsp;</font>";
	}
    return fontStyle_s+dataValue+fontStyle_e;
}
 //是否预警
var isWaring = function(indexArray,dataValue,indexCd,col){
    if(typeof indexArray == "undefined" || indexArray == undefined || indexArray == "undefined" || indexArray == null)
        return "";
    if(indexArray.length <= 0)
        return "";
    if(parseInt(dataValue) == "NaN"  && ( ""+dataValue.indexOf("%") > 0 || ""+dataValue.indexOf("‰")> 0 ) )
       return "";
    for(var r = 0; r < indexArray.length; r++){
    	var flag=false;
        var waringType = indexArray[r].WARING_TYPE;
        var value1 =     indexArray[r].WARING_VALUE;
        var value2 = "";
        if(waringType == "BTWEEN AND"){
                value2 = indexArray[r].WARING_VALUE2;
         }
        var columnName = indexArray[r].COLUMN_NAME;
        var indexName=   indexArray[r].INDEX_NAME;
     
      if(indexCd !=null && indexCd !="null" && indexCd !="")  //指标不为空
      {
        if(indexName==indexCd && columnName==col )
         {
            flag= judWaring(waringType,dataValue,value1,value2);
         }
         if(flag)
         {
            return waringType +"&nbsp;"+value1+"&nbsp;"+value2;
         }
      }
      else  //指标等于空
      {
        if(columnName==col )
         {
            flag= judWaring(waringType,dataValue,value1,value2);
         }
         if(flag)
         {
            return waringType +"&nbsp;"+value1+"&nbsp;"+value2;
         }
      }

    }
        return "";
}
var judWaring = function(waringType,dataValue,value,value2){
	dataValue=formatData(dataValue);
    if(waringType == ">="){
        if(1*dataValue >= 1*value)
            return true;
        else
            return false;
    }else if(waringType == "<="){
        if(1*dataValue <= 1*value)
            return true;
        else
            return false;
    }else if(waringType == "<"){
        if(1*dataValue < 1*value)
            return true;
        else
            return false;
    }else if(waringType == ">"){
        if(1*dataValue > 1*value)
            return true;
        else
            return false;
    }else if(waringType == "="){
        if(1*dataValue == 1*value)
            return true;
        else
            return false;
    }else if(waringType == "!="){
        if(1*dataValue != 1*value)
            return true;
        else
            return false;
    }else if(waringType == "BTWEEN AND"){
        if(1*dataValue <= 1*value2 && 1*dataValue >= 1*value)
            return true;
        else
            return false;
    }
}

/**
 *格式化数据
 * @param data
 */
function formatData(data)
{
	data=""+data;//转化字符串
	return 1*(data.replace("%","").replace("‰",""));
}