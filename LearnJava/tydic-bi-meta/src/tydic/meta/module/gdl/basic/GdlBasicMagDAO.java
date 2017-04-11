package tydic.meta.module.gdl.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.gdl.GdlDAO;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;

/**
*
* @author 李国民
* @description 基础指标管理DAO
* @date 2012-06-05
*
*/
public class GdlBasicMagDAO extends GdlDAO {

	private final String TyepId = "6";		//基础宽表类型id
	
	/**
	 * 查询可以创建基础指标的表信息
	 * @param data {dataSourceId：数据源id， tableOwner：所属用户
	 * 				keywords：关键字
	 * 			   }
	 * @param page 分页
	 */
	public List<Map<String, Object>> queryTableInfoToGdl(Map<String, Object> data, Page page){
		String dataSourceId = MapUtils.getString(data, "DATA_SOURCE_ID");		//数据源id
		String tableOwner = MapUtils.getString(data, "TABLE_OWNER");			//所属用户
		String keywords =  MapUtils.getString(data, "KEYWORDS");			//关键字
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT T.TABLE_ID,T.TABLE_NAME,T.TABLE_NAME_CN,T.TABLE_GROUP_ID," +
				" G.TABLE_GROUP_NAME,T.TABLE_TYPE_ID,T.TABLE_OWNER,T.DATA_SOURCE_ID," +
				" (SELECT count(*) FROM META_GDL D" +
					" LEFT JOIN META_GDL_ALTER_HISTORY H ON D.GDL_ID = H.GDL_ID" +
					" AND D.GDL_VERSION = H.GDL_VERSION" +
					" WHERE D.GDL_SRC_TABLE_ID = T.TABLE_ID AND D.GDL_TYPE = 0 AND H.ALTER_TYPE=1" +
					" AND (D.GDL_STATE = 1 OR (D.GDL_ID NOT IN" +
						" (SELECT G.GDL_ID FROM META_GDL G WHERE G.GDL_STATE = 1 AND G.GDL_TYPE = 0" +
						" AND G.GDL_SRC_TABLE_ID = T.TABLE_ID) AND H.AUDIT_STATE <> 2)))  ||'/'||" +
					" (SELECT COUNT(*) FROM META_TABLE_COLS C WHERE C.TABLE_ID=T.TABLE_ID AND C.COL_BUS_TYPE=1)" +
					" AS GDL_NUMBER" +
				" FROM META_TABLES T" +
				" LEFT JOIN META_TABLE_GROUP G ON T.TABLE_GROUP_ID=G.TABLE_GROUP_ID" +
				" WHERE T.TABLE_STATE=1";
		sql += " AND T.TABLE_TYPE_ID ="+TyepId;
		if(dataSourceId!=null&&!dataSourceId.equals("")){
			sql += " AND T.DATA_SOURCE_ID=?";
			param.add(dataSourceId);
		}
		if(tableOwner!=null&&!tableOwner.equals("")){
			sql += " AND Upper(T.TABLE_OWNER)=?";
			param.add(tableOwner.toUpperCase());
		}
		if(keywords!=null&&!keywords.equals("")){
			sql += " AND (T.TABLE_NAME LIKE ? ESCAPE '/' OR T.TABLE_NAME_CN LIKE ? ESCAPE '/')";
    		param.add("%"+keywords.toUpperCase()+"%");
    		param.add("%"+keywords+"%");
		}
		sql += " ORDER BY T.TABLE_ID DESC";
        sql= SqlUtils.wrapPagingSql(sql, page);

        List<Map<String,Object>> rs = getDataAccess().queryForList(sql,param.toArray());
        //加入码表信息
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("TABLE_TYPE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE,MapUtils.getString(map,"TABLE_TYPE_ID")));
            }
        }
		return rs;
	}
	
	/**
	 * 查询对应表中支持的维度
	 * @param tableId 表类id
	 * @return
	 */
	public List<Map<String, Object>> queryDimColsByTableId(int tableId){
//		String sql = "SELECT T.COL_ID, T.COL_NAME, T.COL_NAME_CN, T.DIM_COL_ID," +
//				" T.DIM_TABLE_ID,D.TABLE_NAME AS DIM_TABLE_NAME" +
//				" FROM META_TABLE_COLS T" +
//				" LEFT JOIN META_DIM_TABLES D ON T.DIM_TABLE_ID = D.DIM_TABLE_ID" +
//				" WHERE T.TABLE_ID = ? AND T.COL_STATE = 1 AND T.COL_BUS_TYPE = 0" +
//				" ORDER BY T.COL_ID"; 
		String sql = "SELECT distinct T.DIM_COL_ID,T.DIM_TABLE_ID,D.TABLE_NAME AS DIM_TABLE_NAME," +
				" D.TABLE_NAME_CN AS DIM_TABLE_NAME_CN" +
				" FROM META_TABLE_COLS T" +
				" LEFT JOIN (SELECT * FROM META_TABLES D WHERE D.TABLE_STATE = 1) D " +
					" ON T.DIM_TABLE_ID = D.TABLE_ID" +
				" WHERE T.TABLE_ID = ? AND T.COL_STATE = 1 AND T.COL_BUS_TYPE = 0" +
				" ORDER BY T.DIM_TABLE_ID";
		List<Map<String,Object>> rs = getDataAccess().queryForList(sql,tableId);
		return rs;
	}

	/**
	 * 查询对应表中可以添加指标的列
	 * @param tableId 表类id
	 * @return
	 */
	public List<Map<String, Object>> queryGdlColsByTableId(int tableId){
		String sql = "SELECT T.COL_ID,T.TABLE_ID,T.COL_NAME,T.COL_NAME_CN,T.COL_DATATYPE,T.COL_SIZE,T.COL_PREC," +
				" T.TABLE_VERSION,B.TABLE_NAME,L.GDL_SRC_TABLE_NAME AS SRC_TABLE_NAME,L.GDL_ID,L.GDL_CODE,L.GDL_NAME" +
				" FROM META_TABLE_COLS T" +
				" LEFT JOIN META_TABLES B ON B.TABLE_ID=T.TABLE_ID AND B.TABLE_VERSION=T.TABLE_VERSION" +
				" LEFT JOIN (SELECT D.GDL_ID,D.GDL_CODE,D.GDL_NAME,D.GDL_SRC_TABLE_NAME," +
					" D.GDL_SRC_TABLE_ID,D.GDL_SRC_COL_ID" +
					" FROM META_GDL D" +
					" LEFT JOIN META_GDL_ALTER_HISTORY H ON D.GDL_ID = H.GDL_ID AND D.GDL_VERSION = H.GDL_VERSION" +
					" WHERE D.GDL_SRC_TABLE_ID = ? AND D.GDL_TYPE = 0 AND H.ALTER_TYPE=1" +
					" AND (D.GDL_STATE = 1 OR (D.GDL_ID NOT IN (" +
						" SELECT G.GDL_ID FROM META_GDL G WHERE G.GDL_STATE = 1" +
						" AND G.GDL_TYPE = 0 AND G.GDL_SRC_TABLE_ID = ?" +
					" ) AND H.AUDIT_STATE <> 2))) L ON T.TABLE_ID = L.GDL_SRC_TABLE_ID" +
					" AND T.COL_ID = L.GDL_SRC_COL_ID" +
				" WHERE T.TABLE_ID = ? AND T.COL_STATE = 1 AND T.COL_BUS_TYPE = 1"; 
		List<Map<String,Object>> rs = getDataAccess().queryForList(sql,tableId,tableId,tableId);
		return rs;
	}
	
    /**
     * 通过指标id及版本号得到指标详细信息(包括是否存在审核的用户信息)
     * @param gdlId 指标id
     * @param gdlVersion 指标版本号
     * @return
     */
    public Map<String, Object> queryGdlInfoById(int gdlId, int gdlVersion){
    	String sql = "SELECT T.GDL_ID,T.GDL_CODE,T.GDL_NAME,T.GDL_TYPE,T.GDL_SRC_TABLE_NAME," +
    			" T.GDL_SRC_TABLE_ID,T.GDL_SRC_COL,T.GDL_SRC_COL_ID,T.GDL_COL_NAME," +
    			" T.GDL_BUS_DESC,T.GDL_UNIT,T.USER_ID,U.USER_NAMECN,T.CREATE_TIME," +
    			" T.GDL_STATE,T.GDL_VERSION,T.NUM_FORMAT,H.GDL_ALTER_HISTOTY_ID" +
    			" FROM META_GDL T" +
    			" LEFT JOIN (SELECT * FROM META_GDL_ALTER_HISTORY A WHERE A.AUDIT_STATE=0 " +
    				" AND A.GDL_ID = ?) H ON T.GDL_ID=H.GDL_ID" +
    			" LEFT JOIN META_MAG_USER U ON T.USER_ID=U.USER_ID" +
    			" WHERE T.GDL_ID = ? AND T.GDL_VERSION = ?";
    	Map<String, Object> rs = getDataAccess().queryForMap(sql,gdlId,gdlId,gdlVersion);
    	return rs;
    }
    
    /**
     * 查询基础指标下面的可以支持的维度及已支持维度
     * @param data {GDL_ID：指标id ，GDL_VERSION：指标版本
     * 				GDL_SRC_TABLE_ID：表类id
     * 			   }
     * @return
     */
    public List<Map<String, Object>> queryDimData(Map<String, Object> data){
    	int gdlId = MapUtils.getIntValue(data, "GDL_ID");				//指标id
    	int gdlVersion = MapUtils.getIntValue(data, "GDL_VERSION");		//指标版本
    	int tableId = MapUtils.getIntValue(data, "GDL_SRC_TABLE_ID");	//表类id
//    	String sql = "SELECT C.COL_ID,C.COL_NAME,C.COL_NAME_CN,C.COL_BUS_COMMENT,C.DIM_COL_ID," +
//    			" C.DIM_TABLE_ID,D.TABLE_NAME AS DIM_TABLE_NAME,T.GDL_ID,T.GDL_VERSION,T.GROUP_METHOD" +
//    			" FROM META_TABLE_COLS C" +
//    			" LEFT JOIN META_DIM_TABLES D ON C.DIM_TABLE_ID = D.DIM_TABLE_ID" +
//    			" LEFT JOIN (SELECT * FROM META_GDL_DIM_GROUP_METHOD G" +
//    				" WHERE G.GDL_ID = ? AND G.GDL_VERSION = ?) T ON T.DIM_COL_ID = C.COL_ID" +
//    			" WHERE C.TABLE_ID = ? AND C.COL_STATE = 1 AND C.COL_BUS_TYPE = 0" +
//    			" ORDER BY C.COL_ID";
    	String sql = "SELECT distinct T.DIM_TABLE_ID,D.TABLE_NAME AS DIM_TABLE_NAME," +
    			" D.TABLE_NAME_CN AS DIM_TABLE_NAME_CN,N.GDL_ID,N.GDL_VERSION,N.GROUP_METHOD" +
    			" FROM META_TABLE_COLS T" +
    			" LEFT JOIN (SELECT * FROM META_TABLES D WHERE D.TABLE_STATE = 1) D " +
    				" ON T.DIM_TABLE_ID = D.TABLE_ID" +
    			" LEFT JOIN (SELECT * FROM META_GDL_DIM_GROUP_METHOD G" +
    				" WHERE G.GDL_ID = ? AND G.GDL_VERSION = ?) N ON N.DIM_TABLE_ID = T.DIM_TABLE_ID" +
    			" WHERE T.TABLE_ID = ?  AND T.COL_STATE = 1 AND T.COL_BUS_TYPE = 0" +
    			" ORDER BY T.DIM_TABLE_ID";
    	return getDataAccess().queryForList(sql,gdlId,gdlVersion,tableId);
    }
    
    /**
     * 通过指标id查询对应关联的分类信息
     * @param gdlId
     * @return
     */
    public List<Map<String, Object>> queryGroupByGdlId(int gdlId){
    	String sql = "SELECT T.GDL_GROUP_ID, G.GROUP_NAME" +
    			" FROM META_GDL_GROUP_REL T" +
    			" LEFT JOIN META_GDL_GROUP G ON T.GDL_GROUP_ID = G.GDL_GROUP_ID" +
    			" WHERE T.GDL_ID = ? ORDER BY G.ORDER_ID ASC";
    	return getDataAccess().queryForList(sql,gdlId);
    }
}
