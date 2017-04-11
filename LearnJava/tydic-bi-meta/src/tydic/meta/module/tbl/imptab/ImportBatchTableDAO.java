
/**
 * @文件名: ImportBatchTableDAO.java
 * @包 tydic.meta.module.tbl.imptab
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 Jan 30, 2012 11:20:22 AM
 *
 */

package tydic.meta.module.tbl.imptab;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.sys.code.CodePO;


/**
 * 项目名称：tydic-bi-meta   
 * 类名称：ImportBatchTableDAO   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：Jan 30, 2012 11:20:22 AM   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version
 */

public class ImportBatchTableDAO extends MetaBaseDAO {
    public List<Map<String,Long>> saveMetaTablesBatch(final List<Map<String,Object>> datas){//datas不可能为空，下面没判断
        final List<Map<String, Long>> tableIdList = new ArrayList<Map<String, Long>>();
        //META_TABLES信息新增
        String sql = "INSERT INTO META_TABLES( " + "TABLE_ID, TABLE_NAME, TABLE_NAME_CN, TABLE_OWNER, TABLE_BUS_COMMENT, " +
                     "TABLE_STATE, TABLE_SPACE, TABLE_GROUP_ID, DATA_SOURCE_ID, TABLE_TYPE_ID, " +
                     "TABLE_VERSION, PARTITION_SQL) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";

        getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> m=datas.get(i);
                long tableId = queryForNextVal("SEQ_TAB_ID");
                Map<String, Long> tableIdMap = new HashMap<String, Long>();
                tableIdMap.put(String.valueOf(m.get("tableName")).toUpperCase(), tableId);
                tableIdList.add(tableIdMap);
                preparedStatement.setObject(1,tableId);
//                System.out.println("tableName="+(m.get("tableName")+"").toUpperCase());
                preparedStatement.setObject(2,(m.get("tableName")+"").toUpperCase());
//                System.out.println("tableTypeName="+(m.get("tableTypeName")+"").toUpperCase());
                preparedStatement.setObject(3,(m.get("tableTypeName")+"").toUpperCase());
//                System.out.println("owner="+(m.get("owner")+"").toUpperCase());
                preparedStatement.setObject(4,m.get("owner"));
//                System.out.println("tableBusComment="+(m.get("tableBusComment")+"").toUpperCase());
                preparedStatement.setObject(5,m.get("tableBusComment"));
                preparedStatement.setObject(6, TblConstant.META_TABLE_STATE_MODIFY);//导入的表类为待审核状态，state为2
//                System.out.println("tableSpace="+(m.get("tableSpace")+"").toUpperCase());
                preparedStatement.setObject(7,m.get("tableSpace"));
//                System.out.println("tableGroup="+(m.get("tableGroup")+"").toUpperCase());
                preparedStatement.setObject(8,m.get("tableGroup"));
//                System.out.println("dataSourceId="+(m.get("dataSourceId")+"").toUpperCase());
                preparedStatement.setObject(9,m.get("dataSourceId"));
//                System.out.println("tableType="+(m.get("tableType")+"").toUpperCase());
                preparedStatement.setObject(10,m.get("tableType"));
                preparedStatement.setObject(11,1);
//                System.out.println("partitionSql="+(m.get("partitionSql")+"").toUpperCase());
                preparedStatement.setObject(12,m.get("partitionSql"));
            }

            public int batchSize(){
                return datas.size();
            }
        });
        return tableIdList;
    }

    public List<Map<String,Long>> saveColMesBatch(final List<Map<String,Object>> colList,String type,final long tableId){
        String sql = "INSERT INTO META_TABLE_COLS( "
                     + "COL_ID,TABLE_ID,COL_NAME,COL_NAME_CN,COL_DATATYPE,COL_SIZE,"
                     + "COL_PREC,COL_BUS_COMMENT,COL_ORDER,COL_NULLABLED,COL_STATE,"
                     + "DEFAULT_VAL,DIM_TABLE_ID,COL_BUS_TYPE,DIM_LEVEL,DIM_COL_ID,"
                     + "IS_PRIMARY,DIM_TYPE_ID,TABLE_VERSION ) "
                     + "VALUES(?,?,?,?,?,?,   ?,?,?,?,?,   ?,?,?,?,?,   ?,?,?)";
        final List<Map<String, Long>> colIdList = new ArrayList<Map<String, Long>>();
        if("Y".equals(type)){//界面获取
            getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                    Map<String,Object> m = colList.get(i);
                    long colId = queryForNextVal("SEQ_TAB_COL_ID");
                    Map<String, Long> tableIdMap = new HashMap<String, Long>();
                    tableIdMap.put(String.valueOf(m.get("colName")).toUpperCase(), colId);
                    colIdList.add(tableIdMap);
                    preparedStatement.setObject(1,colId);
                    preparedStatement.setObject(2,tableId);
                    preparedStatement.setObject(3,m.get("colName"));
                    preparedStatement.setObject(4,m.get("colNameCn"));
                    preparedStatement.setObject(5,m.get("colDatatype"));
                    preparedStatement.setObject(6,m.get("colSize"));
                    preparedStatement.setObject(7,m.get("colPrec"));
                    preparedStatement.setObject(8,m.get("colBusComment"));
                    preparedStatement.setObject(9,i+1);
                    preparedStatement.setObject(10,m.get("colNullabled"));
                    preparedStatement.setObject(11,1);
                    preparedStatement.setObject(12,m.get("defaultVal"));
                    preparedStatement.setObject(13,m.get("dimTableId"));
                    preparedStatement.setObject(14,m.get("colBusType"));
                    preparedStatement.setObject(15,m.get("dimLevel"));
                    preparedStatement.setObject(16,m.get("dimColId"));
                    preparedStatement.setObject(17,m.get("isPrimary"));
                    preparedStatement.setObject(18,m.get("dimTypeId"));
                    preparedStatement.setObject(19,1);
                }

                public int batchSize(){
                    return colList.size();
                }
            });
        }else{//查询
            getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                    Map<String,Object> m = colList.get(i);
                    long colId = queryForNextVal("SEQ_TAB_COL_ID");
                    Map<String, Long> tableIdMap = new HashMap<String, Long>();
                    tableIdMap.put(String.valueOf(m.get("colName")).toUpperCase(), colId);
                    colIdList.add(tableIdMap);
                    preparedStatement.setObject(1,colId);
                    preparedStatement.setObject(2,tableId);
                    preparedStatement.setObject(3,m.get("COL_NAME"));
                    preparedStatement.setObject(4,"");
                    preparedStatement.setObject(5,m.get("COL_DATATYPE"));
                    preparedStatement.setObject(6,m.get("COL_SIZE"));
                    preparedStatement.setObject(7,m.get("COL_PREC"));
                    preparedStatement.setObject(8,m.get("COL_BUS_COMMENT"));
                    preparedStatement.setObject(9,i+1);
                    preparedStatement.setObject(10,m.get("COL_NULLABLED"));
                    preparedStatement.setObject(11,1);
                    preparedStatement.setObject(12,m.get("DEFAULT_VAL"));
                    preparedStatement.setObject(13,null);
                    preparedStatement.setObject(14,null);
                    preparedStatement.setObject(15,null);
                    preparedStatement.setObject(16,null);
                    preparedStatement.setObject(17,m.get("IS_PRIMARY"));
                    preparedStatement.setObject(18,null);
                    preparedStatement.setObject(19,1);
                }

                public int batchSize(){
                    return colList.size();
                }
            });
        }
        return colIdList;
    }
    public int[] saveUserTabRelBatch(final List<Map<String,Object>> userTabRelList){
        String sql = "INSERT INTO META_MAG_USER_TAB_REL("
                     + "REL_ID,USER_ID,TABLE_NAME,REL_TYPE,STATE_DATE,TABLE_ID,TABLE_VERSION,STATE_MARK) "
                     + "VALUES(?,?,?,?,sysdate,?,?,?)";
//        List<Object>  params = new ArrayList<Object>();
//        Map<String,Object> m = userTabRelList.get(0);
//        long relId = queryForNextVal("SEQ_TAB_REL_ID");
//        params.add(relId);
//        params.add(m.get("userId"));
//        params.add((m.get("tableName")+"").toUpperCase());
//        params.add(m.get("relType"));
//        params.add(m.get("tableId"));
//        params.add(m.get("tableVersion"));
//        if(m.containsKey("stateMark")){
//        	 params.add(m.get("stateMark"));
//        }else{
//        	 params.add(null);
//        }
//        return  getDataAccess().execUpdate(sql,params.toArray());
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> m = userTabRelList.get(i);
                long relId = queryForNextVal("SEQ_TAB_REL_ID");
                preparedStatement.setObject(1,relId);
                preparedStatement.setObject(2,m.get("userId"));
                preparedStatement.setObject(3,(m.get("tableName")+"").toUpperCase());
                preparedStatement.setObject(4,m.get("relType"));
                preparedStatement.setObject(5,m.get("tableId"));
                preparedStatement.setObject(6,m.get("tableVersion"));
                if(m.containsKey("stateMark")){
                	preparedStatement.setObject(7,m.get("stateMark"));
                }else{
                	preparedStatement.setObject(7,null);
                }
            }

            public int batchSize(){
                return userTabRelList.size();
            }
        });
    }
    public List<Map<String, Object>> getDimMesByTableIdAndVersion(long tableId,int tableVersion){
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT C.COL_ID, C.DIM_TABLE_ID, C.DIM_COL_ID FROM META_TABLE_COLS C WHERE C.DIM_TABLE_ID > 0 ");
        buffer.append("and C.DIM_COL_ID > 0 and C.TABLE_ID = ? AND C.TABLE_VERSION = ?");
        Object[] params = new Object[]{
        	tableId,tableVersion
        };
        return getDataAccess().queryForList(buffer.toString(), params);
    }
    public int[] saveDimMesBatch(final List<Map<String, Object>> dimList,final long tableId){
        String sql = "INSERT INTO META_TABLE_REL("
                     + "TABLE_REL_ID,TABLE_ID1_COL_IDS,TABLE_ID2_COL_IDS,TABLE_REL_DESC,TABLE_REL_TYPE, "
                     + "TABLE_ID1,TABLE_ID2) "
                     + "VALUES(SEQ_TAB_REL_ID.NEXTVAL,?,?,?,?,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> m = dimList.get(i);
                preparedStatement.setObject(1,m.get("colId"));
                preparedStatement.setObject(2,m.get("dimColId"));
                preparedStatement.setObject(3,"");
                preparedStatement.setObject(4,1);
                preparedStatement.setObject(5,tableId);
                preparedStatement.setObject(6,m.get("dimTableId"));
            }

            public int batchSize(){
                return dimList.size();
            }
        });
    }
    /**
     * @Title: getTableTypes
     * @Description: 获取属性分类除去维度表
     * @param type
     * @return Object[][]
     * @throws
     */
    public Object[][] getTableTypes(String type){
    	type = type.toLowerCase();
    	if(CodeManager.getCodes(type) != null && CodeManager.getCodes(type).length > 0){
    		int row = 0;
    		CodePO[] list = CodeManager.getCodes(type);
    		for(CodePO c : CodeManager.getCodes(type)){
        		if(!"2".equals(c.getCodeValue()) && !"3".equals(c.getCodeValue())){
	        		row++;
        		}
        	}
    		Object[][] obj = new Object[row][2];
        	int r = 0;
        	for(CodePO c : CodeManager.getCodes(type)){
        		if(!"2".equals(c.getCodeValue()) && !"3".equals(c.getCodeValue())){
	        		obj[r][1] = c.getCodeName();
	        		obj[r][0] = c.getCodeValue();
	        		r++;
        		}
        	}
        	return obj;
    	}else
    		return null;
//        String sql = "SELECT T.CODE_ITEM,T.CODE_NAME,T.CODE_ID "
//                     + "FROM META_SYS_CODE T "
//                     + "WHERE UPPER(T.CODE_TYPE) = UPPER(?) "
//                     +" AND T.CODE_ITEM NOT IN (2,3) "
//                     + "ORDER BY T.ORDER_ID ";
//        Object[] params = new Object[]{
//        		type
//        };
//        return getDataAccess().queryForArray(sql, params);
    }
    public Map<String, Object> getTabSpace(String tableName,Map<String, Object> map,String owner){
        StringBuffer buffer = new StringBuffer();
        buffer.append("select t.table_name as praTableName,t.tablespace_name as tableSpace from all_tables t where t.owner = '"+owner+"'");
        buffer.append(" and t.table_name='"+tableName+"'");
        return getDataAccess(Convert.toString(map.get("DATA_SOURCE_USER")),
                Convert.toString(map.get("DATA_SOURCE_PASS")),
                Convert.toString(map.get("DATA_SOURCE_RULE")),
                Constant.ORACLE_DRIVER_STRING).queryForMap(buffer.toString());
    }
    public int saveTabInst(Map<String, Object> map) throws Exception{
        String sql = "INSERT INTO META_TABLE_INST(TABLE_INST_ID,TABLE_NAME,TABLE_ID," +
                     "TABLE_SPACE,TABLE_OWNER,TABLE_VERSION,STATE,TABLE_DATE) " +
                     "VALUES (?,?,?,?,?,?,?,sysdate)";
        Object[] params = new Object[7];
        params[0] = queryForNextVal("SEQ_TAB_INST_ID");
        params[1] = (map.get("tableName")+"").toUpperCase();
        params[2] = map.get("tableId");
        params[3] = map.get("tableSpace");//
        params[4] = map.get("tableOwner");
        params[5] = map.get("tableVersion");
        params[6] = map.get("state");
        return getDataAccess().execUpdate(sql, params);
    }

    public boolean validateTabName(String tableName,String owner){
        String sql = "select t.table_name from META_TABLES t where t.table_name='"+tableName+"' and t.table_owner='"+owner+"'";
        List<Map<String, Object>> list = getDataAccess().queryForList(sql.toString());
        if(list.size() > 0)
            return false;
        else
            return true;
    }
}
