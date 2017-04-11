package tydic.meta.module.tbl.examine;
/***
 * 表类审核相关操作的dao
 * */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;

public class TableExamineDAO extends BaseDAO {

	/**
	 * 按页面输入的值查找对应的表
	 * @param queryData 查询的map 包括了业务类型的名称 tableGroupId 层次分类的名称 tableTypeId 状态 stateRel 关键字keyWord
	 * @param page 分页的对象
	 * @author 王晶
	 * */
	public List<Map<String,Object>> queryTablesByCondition(Map<String,Object> queryData,Page page){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT T.TABLE_NAME, T.TABLE_NAME_CN, T.TABLE_OWNER, T.TABLE_BUS_COMMENT, T.TABLE_SPACE,R.TABLE_VERSION,");
        sb.append("T.TABLE_STATE, T.TABLE_ID, T.TABLE_GROUP_ID, T.DATA_SOURCE_ID, G.TABLE_GROUP_ID GROUPID,G.TABLE_GROUP_NAME,B.USER_NAMECN, ");
//      sb.append("C.CODE_ITEM,C.CODE_ID,C.CODE_NAME,D.DATA_SOURCE_NAME,D.DATA_SOURCE_ID SOURCE_ID,R.REL_ID,R.REL_TYPE,R.STATE_MARK,to_char(R.STATE_DATE,'yyyy-mm-dd HH24:MI:SS') STATE_DATE FROM META_MAG_USER_TAB_REL R " );
        sb.append("                  T.TABLE_TYPE_ID,D.DATA_SOURCE_NAME,D.DATA_SOURCE_ID SOURCE_ID,R.REL_ID,R.REL_TYPE,R.STATE_MARK,to_char(R.STATE_DATE,'yyyy-mm-dd HH24:MI:SS') STATE_DATE FROM META_MAG_USER_TAB_REL R " );
        sb.append("INNER JOIN META_TABLES T ON R.TABLE_ID = T.TABLE_ID AND R.TABLE_VERSION  = T.TABLE_VERSION AND R.TABLE_VERSION IS NOT NULL ");
        sb.append("LEFT JOIN META_TABLE_GROUP G ON T.TABLE_GROUP_ID = G.TABLE_GROUP_ID ");
//        sb.append("LEFT JOIN META_SYS_CODE C ON T.TABLE_TYPE_ID=C.CODE_ITEM AND UPPER(C.CODE_TYPE)='"+TblConstant.META_SYS_CODE_TABLE_TYPE+"' ");
        sb.append("LEFT JOIN META_DATA_SOURCE D ON T.DATA_SOURCE_ID = D.DATA_SOURCE_ID ");
        sb.append("LEFT JOIN META_MAG_USER B ON R.USER_ID = B.USER_ID ");
        //sb.append("WHERE R.REL_TYPE<>2 AND T.TABLE_TYPE_ID<>2");
        //sb.append("AND ((R.TABLE_ID , R.TABLE_VERSION) NOT IN (SELECT TABLE_ID,TABLE_VERSION FROM META_MAG_USER_TAB_REL WHERE REL_TYPE = 4)) ");
        sb.append("WHERE ((R.TABLE_ID , R.TABLE_VERSION) NOT IN (SELECT UTR.TABLE_ID,UTR.TABLE_VERSION FROM META_MAG_USER_TAB_REL UTR INNER JOIN META_TABLES MT ON UTR.TABLE_ID = MT.TABLE_ID AND UTR.TABLE_VERSION  = MT.TABLE_VERSION AND UTR.TABLE_VERSION IS NOT NULL WHERE UTR.REL_TYPE = 2 AND MT.TABLE_TYPE_ID=2))");
//        sb.append("WHERE UPPER(C.CODE_TYPE)='"+TblConstant.META_SYS_CODE_TABLE_TYPE+"'");
//		sb.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
//                "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
//                "C.CODE_NAME,D.DATA_SOURCE_NAME,A.TABLE_VERSION,F.TABLE_DIM_LEVEL FROM META_TABLES A " +
//                "LEFT JOIN META_MAG_USER_TAB_REL R ON A.TABLE_ID = R.TABLE_ID AND A.TABLE_VERSION  = R.TABLE_VERSION "+
//                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
//                "LEFT JOIN META_SYS_CODE C ON B.TABLE_TYPE_ID=C.CODE_ITEM  " +
//                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
//                //关联维度表，获取其维度信息
//                "LEFT JOIN META_DIM_TABLES F ON A.TABLE_ID=F.DIM_TABLE_ID "+
//                //寻找有效版本
//                ",(SELECT TABLE_NAME,CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END " +
//                " VALID_VERSION  FROM " +
//                "(SELECT TABLE_NAME,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0)  " +
//                "VALID_VERSION FROM META_TABLES GROUP BY TABLE_NAME,TABLE_VERSION,TABLE_STATE)  GROUP BY TABLE_NAME) E "+
//                "WHERE UPPER(C.CODE_TYPE)='"+TblConstant.META_SYS_CODE_TABLE_TYPE+ "' "+
//                "AND A.TABLE_NAME=E.TABLE_NAME AND A.TABLE_VERSION=E.VALID_VERSION " );
        List<Object>  params = new ArrayList<Object>();
		if(queryData!=null&&queryData.size()!=0){
			 int groupId = Integer.valueOf(queryData.get("tableGroupId").toString());
			 if(groupId!=0){
			  sb.append(" AND T.TABLE_GROUP_ID = ?");
			  params.add(groupId);
			 }
			 int typeId = Integer.valueOf(queryData.get("tableTypeId").toString());
			 if(typeId!=-1){
			  //sb.append(" AND T.TABLE_TYPE_ID = ?");
			  sb.append(" AND T.TABLE_TYPE_ID = ?");
			  params.add(typeId);
			 }
			 int stateId = Integer.valueOf(queryData.get("stateRel").toString());
			 if(stateId!=-1){
				 if(stateId==3){
			        sb.append(" AND R.REL_TYPE <?");
			        sb.append(" AND NOT EXISTS (SELECT 1 FROM META_MAG_USER_TAB_REL UTR WHERE UTR.REL_TYPE>=4 AND UTR.LAST_REL_ID=R.REL_ID ) ");
				 }
				 if(stateId==4){
					 sb.append(" AND R.REL_TYPE =?");
				 }
				 if(stateId==5){
					    sb.append(" AND R.REL_TYPE =?");
				 }
			  params.add(stateId);
			 }
			 String keyword = null;
			 Object keywordObj = queryData.get("keyWord");
			 if (keywordObj!=null){
				 keyword = keywordObj.toString();
			 }
			 if(keyword!=null&&!keyword.equals("")){
				 sb.append(" AND (Upper(T.TABLE_NAME) LIKE UPPER(?) OR ");
	             sb.append("UPPER(T.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(T.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
	                params.add("%" + keyword + "%");
	                params.add("%" + keyword + "%");
	                params.add("%" + keyword + "%");
			 }
		}//end if dataMap
		sb.append(" ORDER BY R.STATE_DATE DESC");
		String pageSql = sb.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rtn = getDataAccess().queryForList(pageSql, params.toArray());
        //加入码表信息
        if(rtn!=null&&rtn.size()>0){
            for(Map<String,Object> map:rtn){
                map.put("CODE_NAME", CodeManager.getName("TABLE_TYPE", MapUtils.getString(map, "TABLE_TYPE_ID")));
                map.put("CODE_ITEM", MapUtils.getString(map, "TABLE_TYPE_ID"));
            }
        }
        return rtn;
	}
	/**
	 * 审核通过与不通过 的处理方法
	 * @param map 包含了flag int 通过还是拒绝的标志 result String 审核意见  user int 当前登录用户的id tableId int 当前表类的id
	 * @throws Exception 
	 * */
     public int insertTableState(Map<String,Object>map) throws Exception{
    	 List<Object>  params = new ArrayList<Object>();
    	 if(map!=null&&map.size()!=0){
    		 Object flagObj = map.get("resuleflag");
    		 int flag = -1;
    		 if(flagObj!=null){
    			 flag = Integer.valueOf(flagObj.toString());
    		 }
    		 int userId = 0;
    		 Object userObj = map.get("user");
    		 if(userObj!=null){
    			 userId = Integer.valueOf(userObj.toString());
    		 }
    		 int tableId = 0 ;
    		 Object tableObj = map.get("tableId");
    		 if(tableObj!=null){
    			 tableId  =Integer.valueOf(tableObj.toString());
    		 }
    		 int relType= -1;
    		 Object relTypeObj = map.get("reltype");
    		 if(relTypeObj!=null){
    			 relType = Integer.valueOf(relTypeObj.toString());
    		 }
    		 Object resultObj = map.get("result");
    		 String result = null;
    		 if(resultObj!=null){
    			 result = resultObj.toString().trim();
    		 }
    		 Object tableNameObj = map.get("tableName").toString();
    		 String tableName = null;
    		 if(tableNameObj!=null){
    			 tableName = tableNameObj.toString().trim();
    		 }
    		 Object tableVersionObj = map.get("tableVersion");
    		 int tableVersion=-1;
    		 if(tableVersionObj!=null){
    			 tableVersion = Integer.valueOf(tableVersionObj.toString());
    		 }
    		 int lastRelId = 0;
    		 Object lastRelIdObj = map.get("lastRelId");
    		 if(lastRelIdObj!=null){
    			 lastRelId = Integer.parseInt(lastRelIdObj.toString());
    		 }
    		 if(flag!=-1&&tableId!=0&&userId!=0&&relType!=-1&&lastRelId!=0){
    			 //String updateSql = "UPDATE META_MAG_USER_TAB_REL T SET T.STATE_MARK='"+result+"' WHERE T.TABLE_ID="+tableId+" AND T.TABLE_VERSION="+tableVersion;
    			 //int count = getDataAccess().execUpdate(updateSql);
    			// if(count==1){
	    			 String sql = "INSERT INTO META_MAG_USER_TAB_REL (REL_ID,USER_ID,TABLE_NAME,STATE_DATE,REL_TYPE,STATE_MARK,TABLE_ID,TABLE_VERSION,LAST_REL_ID) VALUES(SEQ_TAB_REL_ID.NEXTVAL,?,?,SYSDATE,?,?,?,?,?)";
	    			 params.add(userId);
	    			 params.add(tableName);
	    			 if(flag==1){
	    			  params.add(4);
	    			 }
	    			 if(flag==0){
		    			  params.add(5);
		    		 }
	    			 params.add(result);
	    			 params.add(tableId);
	    			 params.add(tableVersion);
	    			 params.add(lastRelId);
	    			 return  getDataAccess().execUpdate(sql,params.toArray());
    			// }
    			 
    	     }
    	 }
		return 0;
     }
     /**
      * 
      * */
     public boolean queryAduitState(int tableId,int tableVersion){
    	 Object[]  params = {tableId, tableVersion};
    	 String sql ="SELECT T.REL_TYPE FROM META_MAG_USER_TAB_REL T WHERE T.TABLE_ID=? AND T.TABLE_VERSION=?";
    	 Object obj  = this.getDataAccess().queryForObject(sql, Integer.class, params);
    	 if(obj!=null){
    		 if(Integer.parseInt(Convert.toString(obj))==4){
    			 return true;
    		 }
    	 }
		return false;
     }
		
	 
}
