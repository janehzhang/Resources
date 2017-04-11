package tydic.meta.module.tbl.diff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.module.tbl.TblCommon;
import tydic.meta.module.tbl.apply.TableApplyAction;
/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 表类差异分析算法类，主要提供公共的核心算法 <br>
 * @date 2011-11-25
 */
public class DiffAnysis{

    /**
     * 差异类型常量定义：
     * 差异类型包括1：字段新增，2 字段删除，3字段类型不同，4字段可为空差异，5主键差异，6默认值差异
     */
    public final static int DIFF_TYPE_ADD_COLUMN=1;
    public final static int DIFF_TYPE_DELETE_COLUMN=2;
    public final static int DIFF_TYPE_DATATYPE=3;
    public final static int DIFF_TYPE_NULLABLE=4;
    public final static int DIFF_TYPE_PRIMARYKEY=5;
    public final static int DIFF_TYPE_DEFAULTVAL=6;
    public final static int DIFF_TYPE_UPDATE_COLUMN=7;

    /**
     * 根据表类配置的列信息和实例表列信息进行差异比较，此差异分析只针对列进行分析。
     * 具体比较列下列信息，1、主键差异。2、列数据类型差异。比较具体数据类型与长度和精度
     * 3、是否为空比较，4、默认值比较。
     * 比较思想是以列名大小写不敏感进行匹配，如果表类配置的列名无法在实例表中存在，则此列算作新增
     * 如果实例表中的列无法在表类配置中找到，则此列算作删除。
     * @param tableConfigColumns 表类配置的列信息，至少包含以下键值
     * COLUMN_NAME,DATA_TYPE,COL_SIZE,COL_PREC,COL_NULLABLED,DEFAULT_VAL,IS_PRIMARY
     * @param instColumns  实例表列信息,至少包含以下键值
     * COLUMN_NAME,DATA_TYPE,COL_SIZE,COL_PREC,COL_NULLABLED,DEFAULT_VAL,IS_PRIMARY
     * @return 返回一个LIST,此list包含表类和列所有基本配置信息。每一列包含如下键值
     * 分别包括表类列配置信息  CONFIG_COLUMN_NAME,CONFIG_DATA_TYPE,CONFIG_COL_NULLABLED,CONFIG_DEFAULT_VAL,CONFIG_IS_PRIMARY，CONFIG_COL_ID
     * 其中 CONFIG_DATA_TYPE 已格式化为最终数据类型，如NUMBER(7,2);
     *         实例表配置信息  INST_COLUMN_NAME,INST_DATA_TYPE,INST_COL_NULLABLED,INST_DEFAULT_VAL,INST_IS_PRIMARY
     * 其中 INST_DATA_TYPE 已格式化为最终数据类型，如NUMBER(7,2);
     * 另有键值：DIFF 表示差异信息，差异类型包括1：字段新增，2 字段删除，3字段类型不同，4字段可为空差异，5主键差异，6默认值差异
     * 同时存在多种差异以“，”号分隔表示，如3,4表示字段类型不同且字段是否为空也存在差异。
     * 返回的最后一列表示差异统计，包含键值
     * DIFFCOUNT:差异数，DIFFCOUNT=0表示不存在差异。
     */
    public static  List<Map<String,Object>> diffCompare(List<Map<String,Object>> tableConfigColumns,
            List<Map<String,Object>> instColumns){
    
        //先将表类字段和实例字段转换为关联数组形式，以COL_NAME作为键值。便于后面匹配
//        Map<String,Map<String,Object>> configMapColumns=new HashMap<String, Map<String, Object>>();
//        for(Map<String,Object> column:tableConfigColumns){
//            configMapColumns.put(Convert.toString(column.get("COL_NAME")).toUpperCase(),column);
//        }
        Map<String,Map<String,Object>> instMapColumns=new HashMap<String, Map<String, Object>>();
        for(Map<String,Object> column:instColumns){
            instMapColumns.put(Convert.toString(column.get("COL_NAME")).toUpperCase().trim(),column);
        }
        //构造返回结果。
        List<Map<String,Object>> result=new ArrayList<Map<String, Object>>();
        //差异数
        int diffConunt=0;
        for(Map<String,Object> configColumn:tableConfigColumns){	
            Map<String,Object> itemResult=new HashMap<String, Object>();
            //以COL_NAME作为键值。便于匹配
            String columnName=Convert.toString(configColumn.get("COL_NAME")).toUpperCase();
            //加入表类配置列基本信息。

            itemResult.put("CONFIG_COLUMN_NAME",columnName);
            itemResult.put("CONFIG_COLUMN_NAME_CN",configColumn.get("COL_NAME_CN"));
            itemResult.put("CONFIG_COLUMN_ID",configColumn.get("COL_ID"));
            itemResult.put("CONFIG_DATA_TYPE", TblCommon.buildDataType(Convert.toString(configColumn.get("DATA_TYPE")),
                    parseInt(configColumn.get("COL_SIZE"))==null?0:parseInt(configColumn.get("COL_SIZE")),
                    parseInt(configColumn.get("COL_PREC"))==null?0:parseInt(configColumn.get("COL_PREC"))));
            itemResult.put("CONFIG_COL_NULLABLED",parseInt((configColumn.get("COL_NULLABLED"))));
            itemResult.put("CONFIG_DEFAULT_VAL",Convert.toString(configColumn.get("DEFAULT_VAL")));
            itemResult.put("CONFIG_IS_PRIMARY",parseInt((configColumn.get("IS_PRIMARY"))));
            //判断实例列中是否存在此列名
            if(instMapColumns.containsKey(columnName)){
            	Map<String,Object> instColumn=instMapColumns.get(columnName);
                if(instColumn.get("COL_PREC")==null){
                    instColumn.put("COL_PREC",0);
                }
                if(instColumn.get("COL_SIZE")==null){
                    instColumn.put("COL_SIZE",0);
                }
            	String dataType = TblCommon.buildDataType(Convert.toString(instColumn.get("DATA_TYPE")),
                        parseInt(instColumn.get("COL_SIZE"))==null?0:parseInt(instColumn.get("COL_SIZE")),
                        parseInt(instColumn.get("COL_PREC"))==null?0:parseInt(instColumn.get("COL_PREC")));
                if(dataType.equals("NUMBER(22)")){
                	dataType = "NUMBER";
                	instColumn.remove("COL_SIZE");
                	instColumn.put("COL_SIZE","0");
                }
                itemResult.put("INST_COLUMN_NAME",columnName);
                itemResult.put("INST_DATA_TYPE", dataType);
                itemResult.put("INST_COL_NULLABLED",parseInt(instColumn.get("COL_NULLABLED")));
                // 对
                String defaultVal = Convert.toString(instColumn.get("DEFAULT_VAL")).trim();
                if(defaultVal.indexOf("'") != -1){
                	defaultVal = defaultVal.substring(1, defaultVal.length()-1);
                }
                itemResult.put("INST_DEFAULT_VAL",defaultVal);
                itemResult.put("INST_IS_PRIMARY",parseInt(instColumn.get("IS_PRIMARY")));
                String diff="";
               
                //数据类型差异比较
                if(!configColumn.get("DATA_TYPE").toString().equalsIgnoreCase(instColumn.get("DATA_TYPE").toString())){
                    //数据类型差异
                    diff+=DIFF_TYPE_DATATYPE;
                }else{//比较长度和精度
                    if(!instColumn.get("DATA_TYPE").toString().equalsIgnoreCase("DATE")){//不为DATE类型
                        if(!Convert.toString(instColumn.get("COL_SIZE")).equals(Convert.toString(configColumn.get("COL_SIZE")))){//长度不相等
                            diff+=DIFF_TYPE_DATATYPE;

                        }else if(!parseInt(instColumn.get("COL_PREC")).equals(parseInt(configColumn.get("COL_PREC"))==null?"0":parseInt(configColumn.get("COL_PREC")))){//精度不相等
                            diff+=DIFF_TYPE_DATATYPE;
                        }
                    }
                }
                //是否为主键比对
                if(parseInt(instColumn.get("IS_PRIMARY"))!=parseInt(configColumn.get("IS_PRIMARY"))){
                    diff+=diff.equals("")?DIFF_TYPE_PRIMARYKEY:(","+DIFF_TYPE_PRIMARYKEY);
                }
                //是否为空比对
                if(parseInt(instColumn.get("COL_NULLABLED"))!=parseInt(configColumn.get("COL_NULLABLED"))){
                    diff+=diff.equals("")?DIFF_TYPE_NULLABLE:(","+DIFF_TYPE_NULLABLE);
                }
                //默认值比对
                String dv = Convert.toString(instColumn.get("DEFAULT_VAL")).trim();
                if(dv.indexOf("'") != -1){
                	dv = dv.substring(1, dv.length()-1);
                }
                if(!dv.equals(Convert.toString(configColumn.get("DEFAULT_VAL")).trim())){
                    diff+=diff.equals("")?DIFF_TYPE_DEFAULTVAL:(","+DIFF_TYPE_DEFAULTVAL);
                }
                //字段修改差异 COL_NAME
                if(!Convert.toString(instColumn.get("COL_NAME")).equals(Convert.toString(configColumn.get("COL_NAME")))){
                    diff+=diff.equals("")?DIFF_TYPE_UPDATE_COLUMN:(","+DIFF_TYPE_UPDATE_COLUMN);
                }
                itemResult.put("DIFF",diff);
                //判断是否存在差异
                if(!diff.equals("")){
                    diffConunt++;
                }
                //删除已经匹配的字段。
                instMapColumns.remove(columnName);
            }else{
                //不存在则表示此列为新增列
                itemResult.put("INST_COLUMN_NAME","--");
                itemResult.put("INST_DATA_TYPE", "--");
                itemResult.put("INST_COL_NULLABLED","--");
                itemResult.put("INST_DEFAULT_VAL","--");
                itemResult.put("INST_IS_PRIMARY","--");
                itemResult.put("DIFF",Convert.toString(DIFF_TYPE_ADD_COLUMN));
                diffConunt++;
            }
            result.add(itemResult);
        }
        //如果实例表中还存在字段，则计算为删除的字段。
        if(instMapColumns.size()>0){
            for(Map.Entry<String,Map<String,Object>> entry:instMapColumns.entrySet()){
                String columnName=entry.getKey();
                Map<String,Object> itemResult=new HashMap<String, Object>();
                Map<String,Object> instColumn=instMapColumns.get(columnName);
                itemResult.put("INST_COLUMN_NAME",columnName);
                String dataType = Convert.toString(instColumn.get("DATA_TYPE"));
                int colSize = parseInt(instColumn.get("COL_SIZE"))==null? 0 : parseInt(instColumn.get("COL_SIZE"));
                int colPrec = parseInt(instColumn.get("COL_PREC"))==null? 0 : parseInt(instColumn.get("COL_PREC"));

                itemResult.put("INST_DATA_TYPE", TblCommon.buildDataType(dataType,colSize,colPrec));
                itemResult.put("INST_COL_NULLABLED",parseInt(instColumn.get("COL_NULLABLED")));
                itemResult.put("INST_DEFAULT_VAL",parseInt(instColumn.get("DEFAULT_VAL")));
                itemResult.put("INST_IS_PRIMARY",parseInt(instColumn.get("IS_PRIMARY")));
                itemResult.put("DIFF",Convert.toString(DIFF_TYPE_DELETE_COLUMN));
                itemResult.put("CONFIG_COLUMN_NAME","--");
                itemResult.put("CONFIG_DATA_TYPE", "--");
                itemResult.put("CONFIG_COL_NULLABLED","--");
                itemResult.put("CONFIG_DEFAULT_VAL","--");
                itemResult.put("CONFIG_IS_PRIMARY","--");
                diffConunt++;
                result.add(itemResult);
            }
        }
        //加入差异数字段。
        Map<String,Object> diffInfo=new HashMap<String, Object>();
        diffInfo.put("DIFF_COUNT",diffConunt);
        result.add(diffInfo);
        return  result;
    }
    
    /**
     * 判断是两个表列字段否存在差异。
     * @param tableConfigColumns
     * @param instColumns
     * @return 返回false为不存在差异，返回true为存在差异
     */
    public static boolean isDiffCompare(List<Map<String,Object>> tableConfigColumns,
            List<Map<String,Object>> instColumns){
    	boolean diffCheck = false;		//是否存在差异，flase为不存在，true为存在差异
    	
        Map<String,Map<String,Object>> instMapColumns=new HashMap<String, Map<String, Object>>();
        for(Map<String,Object> column:instColumns){
            instMapColumns.put(Convert.toString(column.get("COL_NAME")).toUpperCase().trim(),column);
        }
        for(Map<String,Object> configColumn:tableConfigColumns){	
            Map<String,Object> itemResult=new HashMap<String, Object>();
            //以COL_NAME作为键值。便于匹配
            String columnName=Convert.toString(configColumn.get("COL_NAME")).toUpperCase();
            //加入表类配置列基本信息。

            itemResult.put("CONFIG_COLUMN_NAME",columnName);
            itemResult.put("CONFIG_COLUMN_NAME_CN",configColumn.get("COL_NAME_CN"));
            itemResult.put("CONFIG_COLUMN_ID",configColumn.get("COL_ID"));
            itemResult.put("CONFIG_DATA_TYPE", TblCommon.buildDataType(Convert.toString(configColumn.get("DATA_TYPE")),
                    parseInt(configColumn.get("COL_SIZE"))==null?0:parseInt(configColumn.get("COL_SIZE")),
                    parseInt(configColumn.get("COL_PREC"))==null?0:parseInt(configColumn.get("COL_PREC"))));
            itemResult.put("CONFIG_COL_NULLABLED",parseInt((configColumn.get("COL_NULLABLED"))));
            itemResult.put("CONFIG_DEFAULT_VAL",Convert.toString(configColumn.get("DEFAULT_VAL")));
            itemResult.put("CONFIG_IS_PRIMARY",parseInt((configColumn.get("IS_PRIMARY"))));
            //判断实例列中是否存在此列名
            if(instMapColumns.containsKey(columnName)){
            	Map<String,Object> instColumn=instMapColumns.get(columnName);
                if(instColumn.get("COL_PREC")==null){
                    instColumn.put("COL_PREC",0);
                }
                if(instColumn.get("COL_SIZE")==null){
                    instColumn.put("COL_SIZE",0);
                }
            	String dataType = TblCommon.buildDataType(Convert.toString(instColumn.get("DATA_TYPE")),
                        parseInt(instColumn.get("COL_SIZE"))==null?0:parseInt(instColumn.get("COL_SIZE")),
                        parseInt(instColumn.get("COL_PREC"))==null?0:parseInt(instColumn.get("COL_PREC")));
                if(dataType.equals("NUMBER(22)")){
                	dataType = "NUMBER";
                	instColumn.remove("COL_SIZE");
                	instColumn.put("COL_SIZE","0");
                }
                itemResult.put("INST_COLUMN_NAME",columnName);
                itemResult.put("INST_DATA_TYPE", dataType);
                itemResult.put("INST_COL_NULLABLED",parseInt(instColumn.get("COL_NULLABLED")));
                // 对
                String defaultVal = Convert.toString(instColumn.get("DEFAULT_VAL")).trim();
                if(defaultVal.indexOf("'") != -1){
                	defaultVal = defaultVal.substring(1, defaultVal.length()-1);
                }
                itemResult.put("INST_DEFAULT_VAL",defaultVal);
                itemResult.put("INST_IS_PRIMARY",parseInt(instColumn.get("IS_PRIMARY")));
                
                //数据类型差异比较
                if(!configColumn.get("DATA_TYPE").toString().equalsIgnoreCase(instColumn.get("DATA_TYPE").toString())&&!instColumn.get("DATA_TYPE").toString().equalsIgnoreCase("DATE")){
                    //数据类型差异
                	diffCheck=true;
                	break;
                }else{//比较长度和精度
                    if(!instColumn.get("DATA_TYPE").toString().equalsIgnoreCase("DATE")){//不为DATE类型
                        if(!Convert.toString(instColumn.get("COL_SIZE")).equals(Convert.toString(configColumn.get("COL_SIZE")))){//长度不相等
                        	diffCheck=true;
                        	break;

                        }else if(!parseInt(instColumn.get("COL_PREC")).equals(parseInt(configColumn.get("COL_PREC"))==null?"0":parseInt(configColumn.get("COL_PREC")))){//精度不相等
                        	diffCheck=true;
                        	break;
                        }
                    }
                }
                //是否为主键比对
                if(parseInt(instColumn.get("IS_PRIMARY"))!=parseInt(configColumn.get("IS_PRIMARY"))){
                	diffCheck=true;
                	break;
                }
                //是否为空比对
                if(parseInt(instColumn.get("COL_NULLABLED"))!=parseInt(configColumn.get("COL_NULLABLED"))){
                	diffCheck=true;
                	break;
                }
                //默认值比对
                String dv = Convert.toString(instColumn.get("DEFAULT_VAL")).trim();
                if(dv.indexOf("'") != -1){
                	dv = dv.substring(1, dv.length()-1);
                }
                if(!dv.equals(Convert.toString(configColumn.get("DEFAULT_VAL")).trim())){
                	diffCheck=true;
                	break;
                }
                // 字段修改差异 COL_NAME
                if(!Convert.toString(instColumn.get("COL_NAME")).equals(Convert.toString(configColumn.get("COL_NAME")))){
                	diffCheck=true;
                	break;
                }
                //删除已经匹配的字段。
                instMapColumns.remove(columnName);
            }else{
            	diffCheck=true;
            	break;
            }
        }
        //如果实例表中还存在字段，则计算为删除的字段。
        if(instMapColumns.size()>0){
        	diffCheck=true;
        }        
        return  diffCheck;
    }

    /**
     * 生成同步数据与表结构建表SQL，基本思想是根据配置新建一个临时表，拷贝数据至临时表中，修改原来的表名为备份表，
     * 修改临时表表名为新表
     * @param tableConfig 表类配置信息，此处至少需要与表结构相关的键值包括分区SQL PARTITION_SQL、TABLE_BUS_COMMENT
     * @param tableColumnsConfig 表类列配置信息,至少包含以下键值
     * COLUMN_NAME,DATA_TYPE,COL_SIZE,COL_PREC,COL_NULLABLED,DEFAULT_VAL,IS_PRIMARY,COL_ID
     * @param columnDiffData 列差异映射数据，包含三个键值：
     * modifyColumn:[{configColumnId:1,instColumnName:""}...],
     * addColumn:[...];//记录新增的列Id
     * deleteColumn:[...];//记录删除的实例表列名
     * columnMapping:[{configColumnId:1,instColumnName:""}...]://记录其列映射（从配置到实例之间的列映射）
     * @return  返回3个长度的数组，0位表示同步结构与数据SQL集合。1位表示生成的主要操作临时表表名，2位表示生成
     * 的备份临时表表名。
     */
    public static String[] synchronousStructDataSql(Map<String, Object> tableConfig,
            List<Map<String,Object>> tableColumnsConfig,Map<String,Object> columnDiffData,String tableName,String tableOwner){
        //生成备份TableName
        String tempTableName= "TEMP_"+System.currentTimeMillis();
        StringBuffer sqls=new StringBuffer();
        //获取建表SQl
        Map<String,Object> data = new HashMap<String, Object>();
        Map<String, Object> tableData = new HashMap<String, Object>();
        tableData.put("partitionSql", tableConfig.get("PARTITION_SQL"));
        tableData.put("tableBusComment", tableConfig.get("TABLE_BUS_COMMENT"));
        tableData.put("tableName", tempTableName);
        tableData.put("tableVersion", tableConfig.get("TABLE_VERSION"));
        tableData.put("tableOwner", tableOwner);
        data.put("tableData", tableData);
        List<Map<String,Object>> columnDatas = new ArrayList<Map<String, Object>>();
        //列ID与列名映射。
        Map<Integer,String> idNameMapping=new HashMap<Integer, String>();
        Map<Integer,Map<String,Object>> idColumnMapping=new HashMap<Integer, Map<String,Object>>();
        for(int i = 0; i < tableColumnsConfig.size(); i++){
            Map<String, Object> columnData = new HashMap<String, Object>();
            columnData.put("colBusComment", tableColumnsConfig.get(i).get("COL_BUS_COMMENT"));
            columnData.put("colDatatype", tableColumnsConfig.get(i).get("DATA_TYPE"));
            columnData.put("colName", tableColumnsConfig.get(i).get("COL_NAME"));
            columnData.put("colNullabled", tableColumnsConfig.get(i).get("COL_NULLABLED"));
            columnData.put("colPrec", tableColumnsConfig.get(i).get("COL_PREC"));
            columnData.put("colSize", tableColumnsConfig.get(i).get("COL_SIZE"));
            columnData.put("defaultVal", tableColumnsConfig.get(i).get("DEFAULT_VAL"));
            columnData.put("isPrimary", tableColumnsConfig.get(i).get("IS_PRIMARY"));
            idNameMapping.put(parseInt(tableColumnsConfig.get(i).get("COL_ID"))
                    ,Convert.toString(tableColumnsConfig.get(i).get("COL_NAME")));
            idColumnMapping.put(parseInt(tableColumnsConfig.get(i).get("COL_ID")),tableColumnsConfig.get(i));
            columnDatas.add(columnData);
        }
        data.put("columnDatas",columnDatas);
        TableApplyAction obj = new TableApplyAction();
        sqls.append(obj.generateSql(data));

        //第二步，insert into语句
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> columnMappings= (List<Map<String,Object>>)columnDiffData.get("columnMapping");
        String sqlInsert="INSERT /*+append*/ INTO "+tableOwner + "." +tempTableName+" (_insertColumn!key1)<br/>  SELECT _fromColumn!key2 FROM "+tableOwner + "." +tableName+";<br/>";
        String insertColumn="";
        String fromColumn="";
        for(int i=0;i<columnMappings.size();i++){
            insertColumn+=idNameMapping.get(parseInt(columnMappings.get(i).get("configColumnId")))
                          +(i==columnMappings.size()-1?"":",");
            fromColumn+=columnMappings.get(i).get("instColumnName")+(i==columnMappings.size()-1?"":",");
            idColumnMapping.remove(parseInt(columnMappings.get(i).get("configColumnId")));
        }
        //剩余的字段如果是不能为NULL，且没有默认值，设置一个默认值进去。
        for(Map.Entry<Integer,Map<String,Object>> entry:idColumnMapping.entrySet()){
            Map<String,Object> column=entry.getValue();
            if(parseInt(column.get("COL_NULLABLED"))==0
               &&(column.get("DEFAULT_VAL")==null||column.get("DEFAULT_VAL").equals(""))){
                insertColumn+=","+column.get("COL_NAME");
                fromColumn+=","+(TblCommon.isNumber(Convert.toString(column.get("DATA_TYPE")))?0:"' '");
            }
        }
        sqlInsert=sqlInsert.replace("_insertColumn!key1", insertColumn).replace("_fromColumn!key2", fromColumn);
        sqls.append(sqlInsert);
        String tempTableName1= "TEMP_"+(System.currentTimeMillis()+1);
        //第三步，修改原表名为备份表
        sqls.append("ALTER TABLE "+tableOwner + "." +tableName+" RENAME TO "+tempTableName1+ tableConfig.get("TABLE_VERSION")+";<br/>");
        sqls.append("ALTER TABLE "+tableOwner + "." +tempTableName+" RENAME TO "+tableName+";<br/>");
        sqls.append("DROP TABLE "+tableOwner + "." +tempTableName1+ tableConfig.get("TABLE_VERSION")+";");
        return new String[]{sqls.toString(),tableOwner + "."+tempTableName,
                tableOwner + "."+tempTableName1+ tableConfig.get("TABLE_VERSION")};
    }

    /**
     * 将空转0
     * @param obj
     * @return
     */
    private static Integer parseInt(Object obj){
        try{
            return Integer.parseInt(obj.toString());
        }catch (Exception e){
            return 0;
        }    
    }
    
}