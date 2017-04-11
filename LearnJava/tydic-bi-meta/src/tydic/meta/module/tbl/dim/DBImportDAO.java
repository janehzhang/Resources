package tydic.meta.module.tbl.dim;

import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.IRowMapper;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @date 2012-5-8
 * @description 导入数据库维表数据
 *
 */
public class DBImportDAO extends MetaBaseDAO {

    /**
     * 查询目标数据源目标表列信息最大值
     * @return
     */
    public Map<String, Object> queryForMax(List<Map<String, Object>> rowDatas, String tableName, String tableOwner, int dataSourceId,final List<Map<String, Object>> dyColData){
        String sql = "select ";
        for(Map<String, Object> m:rowDatas){
            //横表列
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("codeCol") + ")) as " + m.get("codeCol") + ", ";
            }
            if(!Convert.toString(m.get("nameCol")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("nameCol") + ")) as " + m.get("nameCol") + ", ";
            }
            if(!Convert.toString(m.get("descCol")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("descCol") + ")) as " + m.get("descCol") + ", ";
            }
            //纵表列
            if(!Convert.toString(m.get("verNameCol")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("verNameCol") + ")) as " + m.get("verNameCol") + ", ";
            }
            if(!Convert.toString(m.get("verParCodeCol")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("verParCodeCol") + ")) as " + m.get("verParCodeCol") + ", ";
            }
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("verCodeCol") + ")) as " + m.get("verCodeCol") + ", ";
            }
            if(!Convert.toString(m.get("verDescCol")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("verDescCol") + ")) as " + m.get("verDescCol") + ", ";
            }
        }
        for(Map<String, Object> m:dyColData){
            if(!Convert.toString(m.get("dyColDb")).equals("")){
                sql = sql + "MAX(LENGTH(T." + m.get("dyColDb") + ")) as " + m.get("dyColDb") + ", ";
            }
        }

        if(sql.endsWith(", ")){
            sql = sql.substring(0, sql.length() - 2);
        }
        sql = sql + " FROM " + tableOwner + "." +tableName + " T ";
        return getDataAccess(dataSourceId).queryForMap(sql);
    }

    /**
     * 创建临时表
     * @param maxValues
     * @param tmpTableName
     */
    public void createTmpTable(Map<String, Object> maxValues, String tmpTableName, String tmpTableOwner, int tmpDataSourceId){
        String sql = "create table "+tmpTableOwner+"."+tmpTableName+" as (select ";
        for(String dataKey : maxValues.keySet()){
            String tmp = "";
            for(int i = 0; i < Convert.toInt(maxValues.get(dataKey), 0)*2 && i<4000; i ++){
                tmp = tmp + "1";
            }
            sql = sql + "'"+tmp+"' " + dataKey + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " from dual where 1=0)";
        getDataAccess(tmpDataSourceId).execUpdate(sql);
    }

    /**
     * 删除临时表
     * @param tableName
     */
    public void dropTmpTable(String tableName, String tmpTableOwner, int tmpDataSourceId){
        try{
            String sql = "drop table " + tmpTableOwner + "." + tableName;
            getDataAccess(tmpDataSourceId).execUpdate(sql);
        }catch (Exception e){

        }
    }

    /**
     * 清空维表实体表数据
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     */
    public void emptyTable(String tableName, String tableOwner, int dataSourceId, int dimType){
        String sql = "DELETE FROM " + tableOwner + "." + tableName + " WHERE DIM_TYPE_ID = " + dimType;
        getDataAccess(dataSourceId).execUpdate(sql);
    }

    /**
     * 将目标数据库表数据导入到本数据库表中
     * @return
     */
    public boolean insertDataByDB(final List<Map<String, Object>> rowDatas, String tableName,
                                  String tableOwner, int dataSourceId, final String tmpTableName,
                                  final String tmpTableOwner, final int tmpDataSourceId, final List<Map<String, Object>> dyColData){
        String sql = "select ";
        for(Map<String, Object> m:rowDatas){
            //横表信息
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + "T." + m.get("codeCol") + " as " + m.get("codeCol") + ", ";
            }
            if(!Convert.toString(m.get("nameCol")).equals("")){
                sql = sql + "T." + m.get("nameCol") + " as " + m.get("nameCol") + ", ";
            }
            if(!Convert.toString(m.get("descCol")).equals("")){
                sql = sql + "T." + m.get("descCol") + " as " + m.get("descCol") + ", ";
            }
            //纵表信息
            if(!Convert.toString(m.get("verNameCol")).equals("")){
                sql = sql + "T." + m.get("verNameCol") + " as " + m.get("verNameCol") + ", ";
            }
            if(!Convert.toString(m.get("verParCodeCol")).equals("")){
                sql = sql + "T." + m.get("verParCodeCol") + " as " + m.get("verParCodeCol") + ", ";
            }
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + "T." + m.get("verCodeCol") + " as " + m.get("verCodeCol") + ", ";
            }
            if(!Convert.toString(m.get("verDescCol")).equals("")){
                sql = sql + "T." + m.get("verDescCol") + " as " + m.get("verDescCol") + ", ";
            }
        }
        for(Map<String, Object> m:dyColData){
            if(!Convert.toString(m.get("dyColDb")).equals("")){
                sql = sql + "T." + m.get("dyColDb") + " as " + m.get("dyColDb") + ", ";
            }
        }
        if(sql.endsWith(", ")){
            sql = sql.substring(0, sql.length() - 2);
        }
        sql = sql + " FROM " + tableOwner + "." +tableName + " T ";
        getDataAccess(dataSourceId).queryByRowMapper(sql, new IRowMapper<Object>() {
            public Object convert(ResultSet rs) {
                int colCount = 0;
                String insert = "insert into "+tmpTableOwner+"."+tmpTableName+" (";
                for(Map<String, Object> m:rowDatas){
                    //横表信息
                    if(!Convert.toString(m.get("codeCol")).equals("")){
                        insert += Convert.toString(m.get("codeCol")) + ",";
                        colCount ++;
                    }
                    if(!Convert.toString(m.get("nameCol")).equals("")){
                        insert += Convert.toString(m.get("nameCol")) + ",";
                        colCount ++;
                    }
                    if(!Convert.toString(m.get("descCol")).equals("")){
                        insert += Convert.toString(m.get("descCol")) + ",";
                        colCount ++;
                    }
                    // 纵表信息
                    if(!Convert.toString(m.get("verNameCol")).equals("")){
                        insert += Convert.toString(m.get("verNameCol")) + ",";
                        colCount ++;
                    }
                    if(!Convert.toString(m.get("verParCodeCol")).equals("")){
                        insert += Convert.toString(m.get("verParCodeCol")) + ",";
                        colCount ++;
                    }
                    if(!Convert.toString(m.get("verCodeCol")).equals("")){
                        insert += Convert.toString(m.get("verCodeCol")) + ",";
                        colCount ++;
                    }
                    if(!Convert.toString(m.get("verDescCol")).equals("")){
                        insert += Convert.toString(m.get("verDescCol")) + ",";
                        colCount ++;
                    }
                }
                for(Map<String, Object> m:dyColData){
                    if(!Convert.toString(m.get("dyColDb")).equals("")){
                        insert += Convert.toString(m.get("dyColDb")) + ",";
                        colCount ++;
                    }
                }
                if(insert.endsWith(",")){
                    insert = insert.substring(0, insert.length() - 1);
                }
                insert += ") values(";
                for(int i = 0; i < colCount; i++){
                    insert += "?";
                    if(i != colCount - 1){
                        insert += ",";
                    }
                }
                insert += ")";

                try {
                    List<Object[]> param = new ArrayList<Object[]>();
                    int commitNum = 1000;
                    while(rs.next()){
                        List<Object> oneParam = new ArrayList<Object>();
                        for(Map<String, Object> m:rowDatas){
                            //横表信息
                            if(!Convert.toString(m.get("codeCol")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("codeCol"))));
                            }
                            if(!Convert.toString(m.get("nameCol")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("nameCol"))));
                            }
                            if(!Convert.toString(m.get("descCol")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("descCol"))));
                            }
                            // 纵表信息
                            if(!Convert.toString(m.get("verNameCol")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("verNameCol"))));
                            }
                            if(!Convert.toString(m.get("verParCodeCol")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("verParCodeCol"))));
                            }
                            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("verCodeCol"))));
                            }
                            if(!Convert.toString(m.get("verDescCol")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("verDescCol"))));
                            }
                        }
                        for(Map<String, Object> m:dyColData){
                            if(!Convert.toString(m.get("dyColDb")).equals("")){
                                oneParam.add(rs.getObject(Convert.toString(m.get("dyColDb"))));
                            }
                        }
                        param.add(oneParam.toArray());
                    }
                    Object[] objs = param.toArray();
                    Object[][] paraO = new Object[objs.length][];
                    for(int i = 0; i < paraO.length; i++){
                        paraO[i] = (Object[])objs[i];
                    }
                    getDataAccess(tmpDataSourceId).execUpdateBatch(insert, paraO);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return false;
    }

    /**
     * 从临时表数据中导入维表数据
     * SQL 样式：
     * 最高层级：
     * INSERT INTO DIM.D_SUIT_TYPE_NEW
	(TYPE_NEW_ID,
	 DIM_TYPE_ID,
	 DIM_TABLE_ID,
	 STATE,
	 DIM_LEVEL,
	 MOD_FLAG,
	 ORDER_ID,
	 TYPE_NEW_PAR_ID,
	 TYPE_NEW_CODE,
	 TYPE_NEW_NAME,
	 TYPE_NEW_DESC,
	 TYPE_NEW_PAR_CODE)
	SELECT SEQ_DIM_DATA_ID.NEXTVAL,
				 2381,
				 3546,
				 1,
				 1,
				 1,
				 0,
				 NVL(B.TYPE_NEW_ID, 0),
				 A.LEVEL3_CODE,
				 NVL(A.LEVEL3_NAME, ' ') LEVEL3_NAME,
				 A.CODE_DESC,
				 NVL(B.TYPE_NEW_CODE, 0)
	FROM   (SELECT MAX(0),
								 '  ' AS CODE_DESC,
								 LEVEL3_CODE LEVEL3_CODE,
								 MAX(LEVEL3_NAME) LEVEL3_NAME
					FROM   D_SUIT_TYPE_NEW_BAK
					WHERE  LEVEL3_CODE IS NOT NULL
					GROUP  BY LEVEL3_CODE) A
	LEFT   JOIN DIM.D_SUIT_TYPE_NEW B
	ON     1 = 0;
     *
     *
     * 非最高层级：
     *
     * NSERT INTO DIM.D_SUIT_TYPE_NEW
	(TYPE_NEW_ID,
	 DIM_TYPE_ID,
	 DIM_TABLE_ID,
	 STATE,
	 DIM_LEVEL,
	 MOD_FLAG,
	 ORDER_ID,
	 TYPE_NEW_PAR_ID,
	 TYPE_NEW_CODE,
	 TYPE_NEW_NAME,
	 TYPE_NEW_DESC,
	 TYPE_NEW_PAR_CODE)
	SELECT SEQ_DIM_DATA_ID.NEXTVAL,
				 2381,
				 3546,
				 1,
				 3,
				 1,
				 0,
				 NVL(B.TYPE_NEW_ID, 0),
				 A.SUIT_TYPE_CODE,
				 NVL(A.SUIT_TYPE_NAME, ' ') SUIT_TYPE_NAME,
				 A.CODE_DESC,
				 NVL(B.TYPE_NEW_CODE, 0)
	FROM   (SELECT MAX(LEVEL2_CODE) LEVEL2_CODE,
								 '  ' AS CODE_DESC,
								 SUIT_TYPE_CODE SUIT_TYPE_CODE,
								 MAX(SUIT_TYPE_NAME) SUIT_TYPE_NAME
					FROM   D_SUIT_TYPE_NEW_BAK
					WHERE  SUIT_TYPE_CODE IS NOT NULL
					GROUP  BY LEVEL3_CODE, LEVEL2_CODE, SUIT_TYPE_CODE) A
	LEFT   JOIN DIM.D_SUIT_TYPE_NEW B
	ON     A.LEVEL2_CODE = B.TYPE_NEW_CODE AND A.DIM_TYPE_ID = ?
	AND    DIM_LEVEL = 2;
     *
     */
    public void insertLevelDimData(List<Map<String, Object>> colData, Map<String, Object> rowData,
                                    int tableId, String tableName,String tmpTableName,
                                    int dimType, String tableOwner, int dataSourceId,
                                    int level,String dimPrefix,final List<Map<String, Object>> rowDatas,
                                    int codeType, final List<Map<String, Object>> dyColData){
        //拼装INSERT 语句
        String sql = "insert into " +tableOwner+ "." + tableName + "(";
        for(Map<String, Object> col:colData){
            sql = sql + Convert.toString(col.get("COL_NAME")) + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ") SELECT ";
        int order = 1;
        for(Map<String, Object> col:colData){
            String colName = Convert.toString(col.get("COL_NAME"));
            if(colName.equals("DIM_TABLE_ID")){//维度表类ID
                sql = sql + tableId + ",";
            }else if(colName.equals("DIM_TYPE_ID")){//维度分组类型
                sql = sql + dimType + ",";
            }else if(colName.equals("DIM_LEVEL")){//维层级
                sql = sql + level + ",";
            }else if(colName.equals("MOD_FLAG")){//修改标示
                sql = sql + "1,";
            }else if(colName.equals("ORDER_ID")){//排序序号
                sql = sql + order + ",";
                order ++;
            }else if(colName.equals("STATE")){//状态
                sql = sql + "1,";
            }else if(colName.equals(dimPrefix+"_ID")){//维度ID值
                sql = sql + "SEQ_DIM_DATA_ID.NEXTVAL,";
            }else if(colName.equals(dimPrefix+"_PAR_ID")){//维度父ID
                sql = sql + "NVL(B."+dimPrefix+"_ID, 0),";
            }else if(colName.equals(dimPrefix+"_PAR_CODE")){//维度父CODE
                sql = sql + "NVL(B."+dimPrefix+"_CODE, 0),";
            }else if(colName.equals(dimPrefix+"_CODE")){//维度编码
                if(!Convert.toString(rowData.get("codeCol")).equals("")){
                    sql = sql + "A."+Convert.toString(rowData.get("codeCol")) + ",";
                }else{
                    sql = sql + "'',";
                }
            }else if(colName.equals(dimPrefix+"_NAME")){//维度名称
                if(!Convert.toString(rowData.get("nameCol")).equals("")){  //NVL(A.LEVEL3_NAME, ' ') LEVEL3_NAME,
                    sql = sql + "NVL(TRIM(A."+Convert.toString(rowData.get("nameCol"))+"), ' ') "+Convert.toString(rowData.get("nameCol"))+",";
                }else{
                    sql = sql + "'',";
                }
            }else if(colName.equals(dimPrefix+"_DESC")){//维度描述
                if(!Convert.toString(rowData.get("descCol")).equals("")){
                    sql = sql + "A."+Convert.toString(rowData.get("descCol")) + ",";
                }else{
                    sql = sql + "A.CODE_DESC,";
                }
            }else{// 维表动态字段
                boolean match = false;
                for(Map<String, Object> m:dyColData){
                    if(colName.equals(Convert.toString(m.get("dyCol")))
                            &&!Convert.toString(m.get("dyColDb")).equals("")){
                        sql = sql + "A."+Convert.toString(m.get("dyColDb")) + ",";
                        match = true;
                        break;
                    }
                }
                if(!match){
                    sql = sql + "'',";
                }
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " FROM   (SELECT ";
        if(level <= 1){//当前为最高维层级
            sql = sql + "MAX(0),";
            if(!Convert.toString(rowData.get("descCol")).equals("")){
                sql = sql + "MAX(" + Convert.toString(rowData.get("descCol")) + ") AS " +Convert.toString(rowData.get("descCol")) + ",";
            }else{
                sql = sql + "'  ' AS CODE_DESC,";
            }
            if(codeType == 1){//不追加CODE
                sql = sql + "TRIM(" + Convert.toString(rowData.get("codeCol")) + ") " + Convert.toString(rowData.get("codeCol")) + ",";
            }else{
                sql = sql + "MAX(TRIM(" + Convert.toString(rowData.get("codeCol")) + ")) " + Convert.toString(rowData.get("codeCol")) + ",";
            }
            // 维表动态字段
            for(Map<String, Object> m:dyColData){
                if(!Convert.toString(m.get("dyColDb")).equals("")){
                    sql = sql + " MAX(TRIM(" + Convert.toString(m.get("dyColDb")) + ")) " + Convert.toString(m.get("dyColDb")) + ",";
                }
            }
            sql = sql + " MAX(TRIM(" + Convert.toString(rowData.get("nameCol")) + ")) " + Convert.toString(rowData.get("nameCol"));

            sql = sql + " FROM " + tableOwner + "." + tmpTableName;
            sql = sql + " WHERE  "+Convert.toString(rowData.get("codeCol"))
                    +" IS NOT NULL GROUP  BY TRIM("+Convert.toString(rowData.get("codeCol"))+")) A " +
                    " LEFT   JOIN " +tableOwner+ "." + tableName + " B " +
                    " ON     1 = 0 ";
        }else{//当前非最高维层级
            Map<String, Object> fatherRow = rowDatas.get(level-2);
            if(codeType == 1){//不追加CODE
                sql = sql + " MAX(TRIM(" + Convert.toString(fatherRow.get("codeCol")) + ")) " + Convert.toString(fatherRow.get("codeCol")) + ",";
            }else{
                for(int i = 0; i < level-1; i++){
                    Map<String, Object> m = rowDatas.get(i);
                    sql = sql + " MAX(TRIM("+Convert.toString(m.get("codeCol"))+")) || ";
                }
                sql = sql.substring(0, sql.length()-3);
                sql = sql + Convert.toString(fatherRow.get("codeCol") + ",");
            }
            if(!Convert.toString(rowData.get("descCol")).equals("")){
                sql = sql + "MAX(" + Convert.toString(rowData.get("descCol")) + ") AS " +Convert.toString(rowData.get("descCol")) + ",";
            }else{
                sql = sql + "'  ' AS CODE_DESC,";
            }
            if(codeType == 1){//不追加CODE
                sql = sql + "TRIM(" + Convert.toString(rowData.get("codeCol")) + ") " + Convert.toString(rowData.get("codeCol")) + ",";
            }else{//追加CODE
                for(int i = 0; i < level; i++){
                    Map<String, Object> m = rowDatas.get(i);
                    sql = sql + "MAX(TRIM("+Convert.toString(m.get("codeCol"))+")) || ";
                }
                sql = sql.substring(0, sql.length()-3);
                sql = sql + Convert.toString(rowData.get("codeCol") + ",");
            }
            // 维表动态字段
            for(Map<String, Object> m:dyColData){
                if(!Convert.toString(m.get("dyColDb")).equals("")){
                    sql = sql + " MAX(TRIM(" + Convert.toString(m.get("dyColDb")) + ")) " + Convert.toString(m.get("dyColDb")) + ",";
                }
            }
            sql = sql + " MAX(TRIM(" + Convert.toString(rowData.get("nameCol")) + ")) " + Convert.toString(rowData.get("nameCol"));
            sql = sql + " FROM " + tableOwner + "." + tmpTableName;
            sql = sql + " WHERE  "+Convert.toString(rowData.get("codeCol")) + " IS NOT NULL GROUP  BY ";
            for(int i = 0; i < level; i++){
                Map<String, Object> m = rowDatas.get(i);
                sql = sql + "TRIM(" + Convert.toString(m.get("codeCol"))+"),";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql = sql + ") A LEFT JOIN " +tableOwner+ "." + tableName
                    + " B ON A."+Convert.toString(fatherRow.get("codeCol"))
                    + " = B."+dimPrefix+"_CODE"+" AND B.DIM_TYPE_ID = "+dimType+" " +
                    " AND DIM_LEVEL = "+(level-1);
        }
        getDataAccess(dataSourceId).execUpdate(sql);
    }

    /**
     * 查询纵表层级
     * @return
     */
    public int queryVerLevel(String tableName, String tableOwner, String verCodeCol,
                             String verParCodeCol, String verRootVal, int dataSourceId){
        if(!"".equals(verRootVal)){
            String sql = "select MAX(LEVEL) from "+tableOwner+"."+tableName+" t " +
                    "connect by t."+verParCodeCol+"= prior t."+verCodeCol+"  start with "+verParCodeCol+" = ?";
            return getDataAccess(dataSourceId).queryForIntByNvl(sql,0, verRootVal);
        }else{
            String sql = "select MAX(LEVEL) from "+tableOwner+"."+tableName+" t " +
                    "connect by t."+verParCodeCol+"= prior t."+verCodeCol+"  start with "+verParCodeCol+" IS NULL";
            return getDataAccess(dataSourceId).queryForIntByNvl(sql,0);
        }
    }

    /**
     * 导入纵表数据
     */
    public void insertVerDimData(List<Map<String, Object>> colData, Map<String, Object> rowData,
                                 int tableId, String tableName,String tmpTableName,
                                 int dimType, String tableOwner, int dataSourceId,
                                 int level,String dimPrefix,final List<Map<String, Object>> dyColData){
        //拼装INSERT 语句
        String sql = "insert into " +tableOwner+ "." + tableName + "(";
        for(Map<String, Object> col:colData){
            sql = sql + Convert.toString(col.get("COL_NAME")) + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ") SELECT ";
        int order = 1;
        for(Map<String, Object> col:colData){
            String colName = Convert.toString(col.get("COL_NAME"));
            if(colName.equals("DIM_TABLE_ID")){//维度表类ID
                sql = sql + tableId + ",";
            }else if(colName.equals("DIM_TYPE_ID")){//维度分组类型
                sql = sql + dimType + ",";
            }else if(colName.equals("DIM_LEVEL")){//维层级
                sql = sql + level + ",";
            }else if(colName.equals("MOD_FLAG")){//修改标示
                sql = sql + "1,";
            }else if(colName.equals("ORDER_ID")){//排序序号
                sql = sql + order + ",";
                order ++;
            }else if(colName.equals("STATE")){//状态
                sql = sql + "1,";
            }else if(colName.equals(dimPrefix+"_ID")){//维度ID值
                sql = sql + "SEQ_DIM_DATA_ID.NEXTVAL,";
            }else if(colName.equals(dimPrefix+"_PAR_ID")){//维度父ID
                sql = sql + "CASE WHEN C."+dimPrefix+"_ID"+" IS NULL THEN 0 ELSE C."+dimPrefix+"_ID"+" END,";
            }else if(colName.equals(dimPrefix+"_PAR_CODE")){//维度父CODE
                sql = sql + "NVL(C."+dimPrefix+"_CODE"+", '0'),";
            }else if(colName.equals(dimPrefix+"_CODE")){//维度编码
                if(!Convert.toString(rowData.get("verCodeCol")).equals("")){
                    sql = sql + "TRIM(A."+Convert.toString(rowData.get("verCodeCol"))+"),";
                }else{
                    sql = sql + "'',";
                }
            }else if(colName.equals(dimPrefix+"_NAME")){//维度名称
                if(!Convert.toString(rowData.get("verNameCol")).equals("")){
                    sql =sql + "NVL(TRIM(A."+Convert.toString(rowData.get("verNameCol"))+"), ' '),";
                }else{
                    sql = sql + "'',";
                }
            }else if(colName.equals(dimPrefix+"_DESC")){//维度描述
                if(!Convert.toString(rowData.get("verDescCol")).equals("")){
                    sql = sql + "A."+Convert.toString(rowData.get("verDescCol"))+",";
                }else{
                    sql = sql + "'',";
                }
            }else{
                // 动态字段
                boolean match = false;
                for(Map<String, Object> m:dyColData){
                    if(colName.equals(Convert.toString(m.get("dyCol")))
                            &&!Convert.toString(m.get("dyColDb")).equals("")){
                        sql = sql + "A."+Convert.toString(m.get("dyColDb")) + ",";
                        match = true;
                        break;
                    }
                }
                if(!match){
                    sql = sql + "'',";
                }
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " FROM   (SELECT * FROM " + tableOwner + "." + tmpTableName + " T " +
                " WHERE  LEVEL = "+level+" CONNECT BY TRIM(T."+Convert.toString(rowData.get("verParCodeCol"))+") = PRIOR TRIM(T."
                +Convert.toString(rowData.get("verCodeCol"))+") " ;
        if(!Convert.toString(rowData.get("verRootVal")).equals("")){
            sql = sql + " START  WITH TRIM("+Convert.toString(rowData.get("verParCodeCol"))+") = '"+Convert.toString(rowData.get("verRootVal"))+"') A " ;
        }else{
            sql = sql + " START  WITH TRIM("+Convert.toString(rowData.get("verParCodeCol"))+") IS NULL) A " ;
        }
        sql = sql + " LEFT   JOIN "+tableOwner+"."+tableName+" C " +
                " ON     TRIM(A."+Convert.toString(rowData.get("verParCodeCol"))+") = TRIM(C."+dimPrefix+"_CODE"+")";
        if(level>1){
            sql = sql + " AND c.dim_type_id = "+dimType;
        }

        getDataAccess(dataSourceId).execUpdate(sql);
    }

    /**
     * 从临时表和维度实体表中取出编码相同的记录
     * @param rowDatas 界面上选择的列信息
     * @param tmpTableName 临时表表名
     * @param tmpDataSourceId 临时表所在数据源，跟维度实体表所在数据源一致
     * @param tmpTableOwner 临时表所属用户，跟维度实体表所属用户一致
     * @param tableInstName 维度实体表表名
     * @param dimPrefix 维度列名前缀
     * @param dimType 归并类型
     * @return
     */
    public List<Map<String, Object>> queryDupData(final List<Map<String, Object>> rowDatas, final String tmpTableName,
                                                  final int tmpDataSourceId, final String tmpTableOwner, String tableInstName,
                                                  String dimPrefix, int dimType){
        String sql = "SELECT ";

        sql = sql + "a." + dimPrefix + "_NAME, a." + dimPrefix + "_CODE,";
        for(Map<String, Object> m:rowDatas){
            if(!Convert.toString(m.get("nameCol")).equals("")&&!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + "b." + Convert.toString(m.get("nameCol")) + "," + "b." + Convert.toString(m.get("codeCol")) + ",";
            }
            if(!Convert.toString(m.get("verCodeCol")).equals("")
                    &&!Convert.toString(m.get("verNameCol")).equals("")&&!Convert.toString(m.get("verParCodeCol")).equals("")){
                sql = sql + "b." + Convert.toString(m.get("verNameCol")) + "," + "b." + Convert.toString(m.get("verCodeCol")) + ","
                    + "b." + Convert.toString(m.get("verParCodeCol")) + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " from " + tmpTableOwner + "." + tableInstName + " a, " + tmpTableOwner + "." + tmpTableName
                + " b where a.dim_type_id = "+dimType+" AND (";
        for(Map<String, Object> m:rowDatas){
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + " trim(a." + dimPrefix + "_CODE) = trim(b." + Convert.toString(m.get("codeCol")) + ") or";
            }
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + " trim(a." + dimPrefix + "_CODE) = trim(b." + Convert.toString(m.get("verCodeCol")) + ") or";
            }
        }
        sql = sql.substring(0, sql.length() - 2);
        sql = sql + ") ORDER BY a."+dimPrefix+"_name";
        return getDataAccess(tmpDataSourceId).queryForList(sql);
    }

    /**
     * 判断临时表和维度实体表是否存在重复编码数据
     * @param rowDatas 界面上选择的列信息
     * @param tmpTableName 临时表表名
     * @param tmpDataSourceId 临时表所在数据源，跟维度实体表所在数据源一致
     * @param tmpTableOwner 临时表所属用户，跟维度实体表所属用户一致
     * @param tableInstName 维度实体表表名
     * @param dimPrefix 维度列名前缀
     * @param dimType 归并类型
     * @return
     */
    public boolean haveDupCode(final List<Map<String, Object>> rowDatas, final String tmpTableName,
                                                  final int tmpDataSourceId, final String tmpTableOwner, String tableInstName,
                                                  String dimPrefix, int dimType){
        String sql = "SELECT COUNT(1) from " + tmpTableOwner + "." + tableInstName + " a, " + tmpTableOwner + "." + tmpTableName
                + " b where a.dim_type_id = "+dimType+" AND (";
        for(Map<String, Object> m:rowDatas){
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + " TRIM(a." + dimPrefix + "_CODE) = TRIM(b." + Convert.toString(m.get("codeCol")) + ") or";
            }
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + " TRIM(a." + dimPrefix + "_CODE) = TRIM(b." + Convert.toString(m.get("verCodeCol")) + ") or";
            }
        }
        sql = sql.substring(0, sql.length() - 2);
        sql = sql + ") ORDER BY a."+dimPrefix+"_name";
        return getDataAccess(tmpDataSourceId).queryForIntByNvl(sql,0)>0;
    }

    /**
     * 检查纵表是否有断链的节点
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @param verCodeCol
     * @param verParCodeCol
     * @param verRootVal
     * @return
     */
    public boolean haveDisLinked(String tableName, String tableOwner, int dataSourceId,
                                 String verCodeCol, String verParCodeCol, String verRootVal){
        String sql = "select count(1) from "+tableOwner+"."+tableName+" where "+verParCodeCol+
                " not in (select "+verCodeCol+" from "+tableOwner+"."+tableName+") AND ";
        if(verRootVal.equals("")){
            sql = sql + verParCodeCol + " is not null";
        }else{
            sql = sql + verParCodeCol + " <> '"+verRootVal+"'";
        }
        return getDataAccess(dataSourceId).queryForIntByNvl(sql,0)>0;
    }

    /**
     * 查询纵表中断链的数据
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @param verNameCol
     * @param verCodeCol
     * @param verParCodeCol
     * @param verRootVal
     * @return
     */
    public List<Map<String, Object>> queryDisLinked(String tableName, String tableOwner, int dataSourceId,
                                 String verNameCol, String verCodeCol, String verParCodeCol, String verRootVal){
        String sql = "select " + verNameCol + ","+verCodeCol+","+verParCodeCol+" from "+tableOwner+"."+tableName+" where "+verParCodeCol+
                " not in (select "+verCodeCol+" from "+tableOwner+"."+tableName+") AND ";
        if(verRootVal.equals("")){
            sql = sql + verParCodeCol + " is not null";
        }else{
            sql = sql + verParCodeCol + " <> '"+verRootVal+"'";
        }
        return getDataAccess(dataSourceId).queryForList(sql);
    }

    /**
     * 判断源表中是否存在重复编码
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @param rowDatas
     * @return
     */
    public boolean haveDupCodeOfSoucre(String tableName, String tableOwner, int dataSourceId, List<Map<String, Object>> rowDatas){
        String sql = "SELECT COUNT(1) FROM (SELECT COUNT(1) AS cnt,";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + Convert.toString(m.get("codeCol")) + ",";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + Convert.toString(m.get("verCodeCol")) + ",";
            }

        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " from " + tableOwner + "." + tableName + " GROUP BY ";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + Convert.toString(m.get("codeCol")) + ",";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + Convert.toString(m.get("verCodeCol")) + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + ") A WHERE a.cnt > 1";
        return getDataAccess(dataSourceId).queryForIntByNvl(sql,0)>0;
    }

    /**
     * 查询源横表或者纵表存在的重复编码
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @param rowDatas
     * @return
     */
    public List<Map<String, Object>> queryDupCodeOfSoucre(String tableName, String tableOwner, int dataSourceId, List<Map<String, Object>> rowDatas, Page page){
        String sql = "SELECT ";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("codeCol")) + ",";
            }
            if(!Convert.toString(m.get("nameCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("nameCol")) + ",";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("verCodeCol")) + ",";
            }
            if(!Convert.toString(m.get("verNameCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("verNameCol")) + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " FROM " + tableOwner + "." + tableName + " t INNER JOIN ( SELECT ";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + Convert.toString(m.get("codeCol")) + ",";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + Convert.toString(m.get("verCodeCol")) + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " FROM (SELECT COUNT(1) AS cnt,";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + Convert.toString(m.get("codeCol")) + ",";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + Convert.toString(m.get("verCodeCol")) + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " FROM " + tableOwner + "." + tableName + " GROUP BY ";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + Convert.toString(m.get("codeCol")) + ",";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + Convert.toString(m.get("verCodeCol")) + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + ") A WHERE a.cnt > 1) B ON ";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + "NVL(t." + Convert.toString(m.get("codeCol")) + ",0) = "
                        + "NVL(b." + Convert.toString(m.get("codeCol")) + ",0) AND ";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + "NVL(t." + Convert.toString(m.get("verCodeCol")) + ",0) = "
                        + "NVL(b." + Convert.toString(m.get("verCodeCol")) + ",0) AND ";
            }
        }
        sql = sql.substring(0, sql.length()-4);
        sql = sql + " ORDER BY ";
        for(Map<String, Object> m:rowDatas){
            //横表
            if(!Convert.toString(m.get("codeCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("codeCol")) + ",";
            }
            if(!Convert.toString(m.get("nameCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("nameCol")) + ",";
            }
            //纵表
            if(!Convert.toString(m.get("verCodeCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("verCodeCol")) + ",";
            }
            if(!Convert.toString(m.get("verNameCol")).equals("")){
                sql = sql + " t." + Convert.toString(m.get("verNameCol")) + ",";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        if(page!=null){
        	sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess(dataSourceId).queryForList(sql);
    }

    /**
     * 检查选择的动态字段列里有无空值
     * @param dataSourceId
     * @param tableOwner
     * @param tableName
     * @param cols
     * @return
     */
    public boolean haveNullValue(int dataSourceId, String tableOwner, String tableName, String cols){
        String sql = "select count(1) from " + tableOwner + "." + tableName + " where 1=2 ";
        String col[] = cols.split(",");
        for(int i=0; i<col.length; i++){
            sql = sql + " OR " + col[i] + " is null ";
        }
        return getDataAccess(dataSourceId).queryForIntByNvl(sql,0)>0;
    }

}
