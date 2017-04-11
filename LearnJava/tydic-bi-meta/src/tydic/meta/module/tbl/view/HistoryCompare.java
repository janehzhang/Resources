package tydic.meta.module.tbl.view;

import tydic.frame.common.utils.Convert;
import tydic.meta.module.tbl.TblCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 刘斌
 * Date: 11-12-28
 * Time: 上午11:03
 * 历史版本比较
 */
public class HistoryCompare {
    /**
     * 差异类型常量定义：
     * 差异类型包括1：字段新增，2 字段删除，3字段类型不同，4字段可为空差异，5主键差异，6默认值差异，7列中文名差异，8列注释差异，9字段修改
     */
    public final static int DIFF_TYPE_ADD_COLUMN=1;
    public final static int DIFF_TYPE_DELETE_COLUMN=2;
    public final static int DIFF_TYPE_DATATYPE=3;
    public final static int DIFF_TYPE_NULLABLE=4;
    public final static int DIFF_TYPE_PRIMARYKEY=5;
    public final static int DIFF_TYPE_DEFAULTVAL=6;
    public final static int DIFF_TYPE_COLNAMECN=7;
    public final static int DIFF_TYPE_COLCOMM=8;
    public final static int DIFF_TYPE_UPDATE_COLUMN=9;

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
    public static List<Map<String,Object>> diffCompare(List<Map<String,Object>> tableConfigColumns,
            List<Map<String,Object>> instColumns){
        //先将表类字段和实例字段转换为关联数组形式，以COL_NAME作为键值。便于后面匹配
        Map<String,Map<String,Object>> configMapColumns=new HashMap<String, Map<String, Object>>();
        for(Map<String,Object> column:tableConfigColumns){
            configMapColumns.put(Convert.toString(column.get("COL_ID")).toUpperCase(),column);
        }
        Map<String,Map<String,Object>> instMapColumns=new HashMap<String, Map<String, Object>>();
        for(Map<String,Object> column:instColumns){
            instMapColumns.put(Convert.toString(column.get("COL_ID")).toUpperCase(),column);
        }
        //构造返回结果。
        List<Map<String,Object>> result=new ArrayList<Map<String, Object>>();
        //差异数
        int diffConunt=0;
        for(Map.Entry<String,Map<String,Object>> entry:configMapColumns.entrySet()){
            Map<String,Object> itemResult=new HashMap<String, Object>();
            String columnId=entry.getKey();
            Map<String,Object> configColumn=entry.getValue();
            //加入表类配置列基本信息。
            itemResult.put("CONFIG_COLUMN_NAME",configColumn.get("COL_NAME"));
            itemResult.put("CONFIG_COLUMN_ID",columnId);
            itemResult.put("CONFIG_DATA_TYPE", TblCommon.buildDataType(Convert.toString(configColumn.get("DATA_TYPE")),
                    Integer.parseInt(configColumn.get("COL_SIZE").toString()), Integer.parseInt(configColumn.get("COL_PREC").toString())));
            itemResult.put("CONFIG_COL_NULLABLED",Integer.parseInt(configColumn.get("COL_NULLABLED").toString()));
            itemResult.put("CONFIG_DEFAULT_VAL",Convert.toString(configColumn.get("DEFAULT_VAL")));
            itemResult.put("CONFIG_IS_PRIMARY",Integer.parseInt(configColumn.get("IS_PRIMARY").toString()));
            itemResult.put("CONFIG_COL_NAMECN",Convert.toString(configColumn.get("COL_NAME_CN")));
            //判断实例列中是否存在此列名
            if(instMapColumns.containsKey(columnId)){
                Map<String,Object> instColumn=instMapColumns.get(columnId);
                itemResult.put("INST_COLUMN_NAME",Convert.toString(instColumn.get("COL_NAME")));
                itemResult.put("INST_DATA_TYPE", TblCommon.buildDataType(Convert.toString(instColumn.get("DATA_TYPE")),
                		Integer.parseInt(instColumn.get("COL_SIZE").toString()),Integer.parseInt(instColumn.get("COL_PREC").toString())));
                itemResult.put("INST_COL_NULLABLED",Integer.parseInt(instColumn.get("COL_NULLABLED").toString()));
                itemResult.put("INST_DEFAULT_VAL",Convert.toString(instColumn.get("DEFAULT_VAL")));
                itemResult.put("INST_IS_PRIMARY",Integer.parseInt(instColumn.get("IS_PRIMARY").toString()));
                itemResult.put("INST_COL_NAMECN",Convert.toString(instColumn.get("COL_NAME_CN")));
                String diff="";
                //数据类型差异比较
                if(!configColumn.get("DATA_TYPE").toString()
                        .equalsIgnoreCase(instColumn.get("DATA_TYPE").toString())&&!instColumn.get("DATA_TYPE").toString().equalsIgnoreCase("DATE")){
                    //数据类型差异
                    diff+=DIFF_TYPE_DATATYPE;
                }else{//比较长度和精度
                    if(!instColumn.get("DATA_TYPE").toString().equalsIgnoreCase("DATE")&&!(Integer.parseInt(instColumn.get("COL_SIZE").toString())==Integer.parseInt(configColumn.get("COL_SIZE").toString())
                         &&Integer.parseInt(instColumn.get("COL_PREC").toString())==Integer.parseInt((configColumn.get("COL_PREC")).toString()))){
                        diff+=DIFF_TYPE_DATATYPE;
                    }
                }
                //是否为主键比对
                if(Integer.parseInt(instColumn.get("IS_PRIMARY").toString())!=Integer.parseInt(configColumn.get("IS_PRIMARY").toString())){
                    diff+=diff.equals("")?DIFF_TYPE_PRIMARYKEY:(","+DIFF_TYPE_PRIMARYKEY);
                }
                //是否为空比对
                if(Integer.parseInt(instColumn.get("COL_NULLABLED").toString())!=Integer.parseInt(configColumn.get("COL_NULLABLED").toString())){
                    diff+=diff.equals("")?DIFF_TYPE_NULLABLE:(","+DIFF_TYPE_NULLABLE);
                }
                //默认值比对
                if(!Convert.toString(instColumn.get("DEFAULT_VAL")).equals(Convert.toString(configColumn.get("DEFAULT_VAL")))){
                    diff+=diff.equals("")?DIFF_TYPE_DEFAULTVAL:(","+DIFF_TYPE_DEFAULTVAL);
                }
                // 列中文名差异 COL_NAME_CN
                if(!Convert.toString(instColumn.get("COL_NAME_CN")).equals(Convert.toString(configColumn.get("COL_NAME_CN")))){
                    diff+=diff.equals("")?DIFF_TYPE_COLNAMECN:(","+DIFF_TYPE_COLNAMECN);
                }
                // 列注释差异 COL_BUS_COMMENT
                if(!Convert.toString(instColumn.get("COL_BUS_COMMENT")).equals(Convert.toString(configColumn.get("COL_BUS_COMMENT")))){
                    diff+=diff.equals("")?DIFF_TYPE_COLCOMM:(","+DIFF_TYPE_COLCOMM);
                }
                // 字段修改差异 COL_NAME
                if(!Convert.toString(instColumn.get("COL_NAME")).equals(Convert.toString(configColumn.get("COL_NAME")))){
                    diff+=diff.equals("")?DIFF_TYPE_UPDATE_COLUMN:(","+DIFF_TYPE_UPDATE_COLUMN);
                }
                itemResult.put("DIFF",diff);
                //判断是否存在差异
                if(!diff.equals("")){
                    diffConunt++;
                }
                //删除已经匹配的字段。
                instMapColumns.remove(columnId);
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
                String columnId=entry.getKey();
                Map<String,Object> itemResult=new HashMap<String, Object>();
                Map<String,Object> instColumn=instMapColumns.get(columnId);
                itemResult.put("INST_COLUMN_NAME",Convert.toString(instColumn.get("COL_NAME")));
                itemResult.put("INST_DATA_TYPE", TblCommon.buildDataType(Convert.toString(instColumn.get("DATA_TYPE")),
                		Integer.parseInt(instColumn.get("COL_SIZE").toString()),Integer.parseInt(instColumn.get("COL_PREC").toString())));
                itemResult.put("INST_COL_NULLABLED",Integer.parseInt(instColumn.get("COL_NULLABLED").toString()));
                itemResult.put("INST_DEFAULT_VAL",Convert.toString(instColumn.get("DEFAULT_VAL")));
                itemResult.put("INST_IS_PRIMARY",Integer.parseInt(instColumn.get("IS_PRIMARY").toString()));
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

}
