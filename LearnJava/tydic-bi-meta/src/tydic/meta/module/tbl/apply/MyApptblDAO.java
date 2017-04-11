package tydic.meta.module.tbl.apply;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 杨苏维
 * @description 我的表类申请DAO <br>
 * @date 2012-1-16
 */
public class MyApptblDAO extends MetaBaseDAO {
	
	
	/**
	 * @author 杨苏维
	 * 根据业务类型查询所属分类
	 */
    public List<Map<String, Object>> queryTableGroup(int tableType) {

        String sql = "SELECT T.TABLE_GROUP_ID,T.TABLE_GROUP_NAME,T.PAR_GROUP_ID "
                + "FROM META_TABLE_GROUP T WHERE TABLE_TYPE_ID=" +  tableType;
    	if(tableType == -1){
    		sql = "SELECT T.TABLE_GROUP_ID,T.TABLE_GROUP_NAME,T.PAR_GROUP_ID FROM META_TABLE_GROUP T";
    	}
        return getDataAccess().queryForList(sql);
    }
    
	/**
	 * @author 杨苏维
	 * 查询用户申请的表类
	 */
    public List<Map<String,Object>> queryTablesByCondition(Map<String,Object> queryData,Page page){
		StringBuffer sb = new StringBuffer("SELECT NVL(BB.REL_ID,AA.REL_ID) REL_ID,NVL(BB.LAST_REL_ID,AA.LAST_REL_ID) LAST_REL_ID,AA.TABLE_NAME, AA.TABLE_TYPE_ID,AA.TABLE_NAME_CN,AA.TABLE_OWNER, AA.TABLE_BUS_COMMENT, AA.TABLE_STATE," +
				" AA.TABLE_SPACE,  AA.TABLE_VERSION, AA.TABLE_ID, AA.TABLE_GROUP_ID, AA.SOURCE_ID, AA.GROUPID, AA.TABLE_GROUP_NAME," +
				" AA.USER_NAMECN,AA.DATA_SOURCE_NAME, AA.STATE_MARK, APP_DATE, NVL(BB.REL_TYPE,AA.REL_TYPE) REL_TYPE,BB.STATE_DATE" +
				" FROM (SELECT A.TABLE_NAME,  A.TABLE_TYPE_ID,B.USER_ID, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_STATE," +
				" A.TABLE_SPACE, A.TABLE_VERSION, A.TABLE_ID,  A.TABLE_GROUP_ID, A.DATA_SOURCE_ID SOURCE_ID, E.TABLE_GROUP_ID GROUPID," +
				" E.TABLE_GROUP_NAME,  C.USER_NAMECN,D.DATA_SOURCE_NAME, B.STATE_MARK,B.REL_TYPE,B.REL_ID,B.LAST_REL_ID,TO_CHAR(B.STATE_DATE, 'yyyy-mm-dd HH24:MI:SS') APP_DATE" +
				" FROM META_TABLES A,META_MAG_USER_TAB_REL B,META_MAG_USER C, META_DATA_SOURCE D, META_TABLE_GROUP E" +
				" WHERE A.TABLE_ID = B.TABLE_ID  AND B.USER_ID = C.USER_ID" +
				" AND A.DATA_SOURCE_ID = D.DATA_SOURCE_ID  AND A.TABLE_GROUP_ID = E.TABLE_GROUP_ID" +
				" AND A.TABLE_VERSION = B.TABLE_VERSION  AND A.DATA_SOURCE_ID = D.DATA_SOURCE_ID" +
				" AND B.LAST_REL_ID IS NULL AND B.REL_TYPE NOT IN (6,7)) AA,  (SELECT A.TABLE_NAME,B.USER_ID, A.TABLE_TYPE_ID,  A.TABLE_NAME_CN, A.TABLE_OWNER," +
				" A.TABLE_BUS_COMMENT,  A.TABLE_STATE, A.TABLE_SPACE, A.TABLE_VERSION,  A.TABLE_ID, A.TABLE_GROUP_ID, " +
				" A.DATA_SOURCE_ID SOURCE_ID,  B.REL_ID, B.LAST_REL_ID, B.REL_TYPE, TO_CHAR(B.STATE_DATE, 'yyyy-mm-dd HH24:MI:SS') STATE_DATE" +
				" FROM META_TABLES A, META_MAG_USER_TAB_REL B   WHERE A.TABLE_ID = B.TABLE_ID  AND A.TABLE_VERSION = B.TABLE_VERSION" +
				" AND B.LAST_REL_ID IS NOT NULL) BB where AA.TABLE_ID = BB.TABLE_ID(+)  AND AA.TABLE_VERSION = BB.TABLE_VERSION(+) ");
                List params = new ArrayList();
		if(queryData!=null&&queryData.size()!=0){
			 if(queryData.get("tableGroupId") != null && !queryData.get("tableGroupId").equals("")){
				 
				 int groupId = Integer.valueOf(queryData.get("tableGroupId").toString());
				  sb.append(" AND AA.TABLE_GROUP_ID = ?");
				  params.add(groupId);
			 }
			 if(queryData.get("tableTypeId") != null && !queryData.get("tableTypeId").equals("")){
			   int typeId = Integer.valueOf(queryData.get("tableTypeId").toString());
			   if(typeId!=-1){
			    //sb.append(" AND T.TABLE_TYPE_ID = ?");
			    sb.append(" AND AA.TABLE_TYPE_ID = ?");
				  params.add(typeId);
			   }
			 }
			 if(queryData.get("tableState") != null && !queryData.get("tableState").equals("")){
			 int stateId = Integer.valueOf(queryData.get("tableState").toString());
			   if(stateId!=-1){
				 if(stateId==3){
			        sb.append(" AND  NVL(BB.REL_TYPE,AA.REL_TYPE) <?");
			        sb.append(" AND NOT EXISTS (SELECT 1 FROM META_MAG_USER_TAB_REL UTR WHERE UTR.REL_TYPE>=4 AND UTR.LAST_REL_ID= NVL(BB.REL_TYPE,AA.REL_TYPE) ) ");
				 }
				 if(stateId==4){
					 sb.append(" AND  NVL(BB.REL_TYPE,AA.REL_TYPE) =?");
				 }
				 if(stateId==5){
					    sb.append(" AND  NVL(BB.REL_TYPE,AA.REL_TYPE) =?");
				 }
				  params.add(stateId);
			   }
			 }
			 String keyword = null;
			 Object keywordObj = queryData.get("keyWord");
			 if (keywordObj!=null && !keywordObj.equals("")){
				 keyword = keywordObj.toString();
			 }
			 if(keyword!=null&&keyword!=""){
				 sb.append(" AND (Upper(AA.TABLE_NAME) LIKE UPPER(?) OR ");
	             sb.append("UPPER(AA.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(AA.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
				  params.add("%"+keyword+"%");
				  params.add("%"+keyword+"%");
				  params.add("%"+keyword+"%");	
			 }
			 if(queryData.get("userId") != null && !queryData.get("userId").equals("")){
				 int userId = Integer.valueOf(queryData.get("userId").toString());
				 sb.append(" AND AA.USER_ID = ?");
				  params.add(userId);	
			 }
		}//end if dataMap
		sb.append(" ORDER BY APP_DATE DESC");
		String pageSql = sb.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        return getDataAccess().queryForList(pageSql, params.toArray());
	}
    
	
    
    
    /**
     * 判断某个表名是否在数据库中存在。
     * @param matchs
     * @return
     */
    public boolean  isExistsMatchTables(String matchs, int dataSourceId,String tableOwner,int tableId){
        String sql =
                "SELECT A.TABLE_ID, A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT " +
                        "FROM META_TABLES A WHERE UPPER(A.TABLE_NAME) = UPPER(?) AND A.DATA_SOURCE_ID="+dataSourceId +" AND UPPER(A.TABLE_OWNER) = UPPER('"+tableOwner+"')" +
                        		" AND A.TABLE_ID !=" + tableId+" AND A.TABLE_STATE <> "+ TblConstant.META_TABLE_STATE_MODIFY;
        return getDataAccess().queryForList(sql,matchs).size()>0?true:false;
    }
    
    
    /**
     * 重新发起申请查询表的列信息
     * 
     * 
     */
    public List<Map<String, Object>> queryMetaTableColsByTableId(int tableId,int tableVersion){
    	StringBuffer sql = new StringBuffer("SELECT * FROM META_TABLE_COLS A WHERE  A.COL_STATE = 1");
    	sql.append(" AND A.TABLE_ID = ?");
        Object params[] = new Object[1];
    	params[0]=tableId;
    	String pageSql = sql.toString();
    	return getDataAccess().queryForList(pageSql, params);
    }
    
    
    
	

}
