package tydic.meta.module.gdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 指标dao
 * @date 12-6-2
 * -
 * @modify
 * @modifyDate -
 */
public class GdlDAO extends MetaBaseDAO{

    /**
     * 获取一个指标的支撑维度
     * @param gdlId
     * @return
     */
    public List<Map<String,Object>> getGdlSupportDims(int gdlId,Page page){
        //查询支撑维度SQL
        String supportSql = "select " +
                "b.DIM_TABLE_ID, " +
                "b.GROUP_METHOD, " +
                "(select TABLE_NAME_CN from META_TABLES where TABLE_ID = b.DIM_TABLE_ID " +
                "     and TABLE_STATE=1) DIM_NAME_CN " +
                "  from meta_gdl a, meta_gdl_dim_group_method b " +
                " where b.GDL_ID = a.GDL_ID " +
                "   and b.GDL_VERSION = a.GDL_VERSION " +
                "   and a.GDL_STATE =1 " +
                "   and b.GDL_ID = ?";
        if(page!=null){
            supportSql = supportSql + " ORDER BY b.DIM_TABLE_ID ";
            supportSql = SqlUtils.wrapPagingSql(supportSql,page);
        }
        
        List<Map<String,Object>> list = getDataAccess().queryForList(supportSql,gdlId);
        for(Map<String,Object> map : list){
            map.put("GROUP_METHOD_NAME", CodeManager.getName(GdlConstant.GDL_GROUP_METHOD_NAME, MapUtils.getString(map, "GROUP_METHOD")));
		}
        return list;
    }

    /**
     * 获取一个指标的绑定维度
     * @param gdlId
     * @return
     */
    public List<Map<String,Object>> getGdlBindDims(int gdlId,Page page){
        //查询绑定维度SQL
        String bindSql = "select b.DIM_COL_ID, " +
                "         b.DIM_TABLE_ID, " +
                "         b.DIM_TYPE_ID, " +
                "         c.DIM_TYPE_NAME, " +
                "         b.DIM_LEVEL, " +
                "         b.DIM_CODE," +
                "       (select TABLE_NAME_CN from META_TABLES where TABLE_ID = b.DIM_TABLE_ID " +
                "        and TABLE_STATE=1) DIM_NAME_CN " +
                "from meta_gdl_tbl_rel_term b,META_DIM_TYPE c " +
                "  where b.DIM_TYPE_ID=c.DIM_TYPE_ID AND b.GDL_TBL_REL_ID=0 " +
                "  and b.GDL_ID = ? ";
        bindSql += " ORDER BY b.ORDER_ID ";
        if(page!=null){
            bindSql = SqlUtils.wrapPagingSql(bindSql,page);
        }
        return getDataAccess().queryForList(bindSql,gdlId);
    }

    /**
     * 根据一个指标获取其支撑维度以及绑定维度
     * @param gdlId 指标ID，可以是基础指标，也可复合指标，基础指标时，绑定维度为空
     * @return 返回map：SUPPORT支撑，BIND绑定
     */
    public Map<String,Object> getGdlDims(int gdlId){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("SUPPORT",getGdlSupportDims(gdlId,null));
        map.put("BIND",getGdlBindDims(gdlId,null));
        return map;
    }

    /**
     * 向指标主表添加一条记录
     * @param gdl
     * @return
     */
    public int insertGDL(Map<String,Object> gdl){
        String sql = "INSERT INTO META_GDL(GDL_ID, GDL_CODE, GDL_NAME, GDL_TYPE, GDL_SRC_TABLE_NAME, " +
                "GDL_SRC_TABLE_ID, GDL_SRC_COL, GDL_SRC_COL_ID, GDL_COL_NAME, GDL_BUS_DESC," +
                "SRC_CALC_GROUP, GDL_CALC_EXPR, GDL_UNIT, USER_ID, CREATE_TIME," +
                "VALID_TIME, GDL_STATE, GDL_VERSION,NUM_FORMAT) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
        List<Object> params = new ArrayList<Object>();
        params.add(MapUtils.getString(gdl,"GDL_ID"));
        params.add(MapUtils.getString(gdl,"GDL_CODE"));
        params.add(MapUtils.getString(gdl,"GDL_NAME"));
        params.add(MapUtils.getString(gdl,"GDL_TYPE"));
        params.add(MapUtils.getString(gdl,"GDL_SRC_TABLE_NAME"));
        params.add(MapUtils.getString(gdl,"GDL_SRC_TABLE_ID"));
        params.add(MapUtils.getString(gdl,"GDL_SRC_COL"));
        params.add(MapUtils.getString(gdl,"GDL_SRC_COL_ID"));
        params.add(MapUtils.getString(gdl,"GDL_COL_NAME"));
        params.add(MapUtils.getString(gdl,"GDL_BUS_DESC"));
        params.add(MapUtils.getString(gdl,"SRC_CALC_GROUP"));
        params.add(MapUtils.getString(gdl,"GDL_CALC_EXPR"));
        params.add(MapUtils.getString(gdl,"GDL_UNIT"));
        params.add(SessionManager.getCurrentUserID());
        if(!"".equals(MapUtils.getString(gdl,"VALID_TIME",""))){
            params.add(MapUtils.getString(gdl,"VALID_TIME"));
        }else{
            params.add(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
        }
        params.add(MapUtils.getString(gdl,"GDL_STATE"));
        params.add(MapUtils.getString(gdl,"GDL_VERSION"));
        params.add(MapUtils.getString(gdl,"NUM_FORMAT"));
        return getDataAccess().execUpdate(sql,params.toArray());
    }

    /**
     * 检测指标编码的重复(修改时)
     * @param code
     * @param gdlId（排除，修改时传入自身id）
     * @return
     */
    public boolean checkReGdlCode(String code,int gdlId){
        String sql = "SELECT COUNT(1) FROM META_GDL WHERE GDL_CODE=? AND GDL_ID!=?";
        return getDataAccess().queryForInt(sql,code,gdlId)>=1;
    }
    
    /**
     * 检测指标编码的重复(新增时)
     * @param code
     * @return
     */
    public boolean checkReGdlCode(String code){
        String sql = "SELECT COUNT(1) FROM META_GDL WHERE GDL_CODE=?";
        return getDataAccess().queryForInt(sql,code)>=1;
    }

    /**
     * 获取一个指标的分类
     * @param gdlId
     * @return
     */
    public List<Map<String,Object>> getGdlGroupIds(int gdlId){
        String sql = "SELECT distinct a.GDL_GROUP_ID,B.GROUP_NAME FROM META_GDL_GROUP_REL a," +
                "META_GDL_GROUP b WHERE " +
                "a.GDL_GROUP_ID=b.GDL_GROUP_ID AND a.GDL_ID=? ";
        return getDataAccess().queryForList(sql,gdlId);
    }

    /**
     * 获取一个指标的直接子指标数
     * @param gdlId
     * @return
     */
    public boolean hasChildGdl(int gdlId){
        String sql = "SELECT COUNT(1) FROM META_GDL_REL A LEFT JOIN META_GDL B " +
                "ON A.GDL_ID=B.GDL_ID WHERE B.GDL_TYPE=? AND A.PAR_GDL_ID=?";
        return getDataAccess().queryForInt(sql,GdlConstant.GDL_TYPE_COMPOSITE,gdlId)>0;
    }

    /**
     * 获取一个指标的所有父，按层级排序，基础指标-->复合指标-->复合指标
     * @param gdlId
     * @return
     */
    public List<Map<String,Object>> getAllParentByGdl(int gdlId){
        String sql = "SELECT " +
                "x.GDL_ID, " +
                "x.GDL_NAME, " +
                "b.LV, " +
                "x.GDL_TYPE " +
                //...根据需要再加入其他字段
                "  FROM (select distinct a.PAR_GDL_ID, level lv " +
                "   from meta_gdl_rel a " +
                " CONNECT BY PRIOR a.PAR_GDL_ID = a.GDL_ID " +
                "  start with a.GDL_ID = ?) b " +
                "  left join meta_gdl x on x.GDL_ID = b.PAR_GDL_ID " +
                " WHERE x.GDL_STATE = 1 " +
                " ORDER BY LV DESC";
        return getDataAccess().queryForList(sql,gdlId);
    }

    /**
     * 根据指标ID查询有效指标信息
     * @param gdlId
     * @return
     */
    public Map<String, Object> queryGdlById(int gdlId){
        String sql = "SELECT a.GDL_ID, a.GDL_CODE, a.GDL_NAME, a.GDL_TYPE, a.GDL_SRC_TABLE_NAME, " +
                "a.GDL_SRC_TABLE_ID, a.GDL_SRC_COL, a.GDL_SRC_COL_ID, a.GDL_COL_NAME, a.GDL_BUS_DESC, " +
                "a.SRC_CALC_GROUP, a.GDL_CALC_EXPR, a.GDL_UNIT, a.USER_ID, " +
                "to_char(a.CREATE_TIME,'yyyy-mm-dd hh24:mi:ss') CREATE_TIME, " +
                "to_char(a.VALID_TIME,'yyyy-mm-dd hh24:mi:ss') VALID_TIME, " +
                " a.GDL_STATE, a.GDL_VERSION,a.NUM_FORMAT,b.USER_NAMECN "+
                "FROM META_GDL a,META_MAG_USER b WHERE a.USER_ID=b.USER_ID and a.GDL_STATE="+ GdlConstant.GDL_STATE_VALID +
                " AND a.GDL_ID=? ";
        Map<String, Object> map = getDataAccess().queryForMap(sql,gdlId);
        if(map!=null){
            map.put("GDL_TYPE_NAME", CodeManager.getName(GdlConstant.GDL_TYPE_CODE_NAME, MapUtils.getString(map, "GDL_TYPE")));
            map.put("GDL_STATE_NAME", CodeManager.getName(GdlConstant.GDL_STATE_CODE_NAME, MapUtils.getString(map, "GDL_STATE")));
            map.put("GDL_NUMFORMAT_NAME",CodeManager.getName(GdlConstant.GDL_NUMFORMAT_CODENAME,MapUtils.getString(map,"NUM_FORMAT")));
        }
        return map;
    }

    /**
     * 根据一个指标版本获取指标信息
     * @param gdlId
     * @param version
     * @return
     */
    public Map<String,Object> queryGdlByVersion(int gdlId,int version){
        String sql = "SELECT a.GDL_ID, a.GDL_CODE, a.GDL_NAME, a.GDL_TYPE, a.GDL_SRC_TABLE_NAME, " +
                "a.GDL_SRC_TABLE_ID, a.GDL_SRC_COL, a.GDL_SRC_COL_ID, a.GDL_COL_NAME, a.GDL_BUS_DESC, " +
                "a.SRC_CALC_GROUP, a.GDL_CALC_EXPR, a.GDL_UNIT, a.USER_ID, " +
                "to_char(a.CREATE_TIME,'yyyy-mm-dd hh24:mi:ss') CREATE_TIME, " +
                "to_char(a.VALID_TIME,'yyyy-mm-dd hh24:mi:ss') VALID_TIME, " +
                " a.GDL_STATE, a.GDL_VERSION,a.NUM_FORMAT,b.USER_NAMECN "+
                "FROM META_GDL a,META_MAG_USER b WHERE a.USER_ID=b.USER_ID and a.GDL_VERSION=?" +
                " AND a.GDL_ID=? ";
        Map<String, Object> map = getDataAccess().queryForMap(sql,version,gdlId);
        map.put("GDL_TYPE_NAME", CodeManager.getName(GdlConstant.GDL_TYPE_CODE_NAME, MapUtils.getString(map, "GDL_TYPE")));
        map.put("GDL_STATE_NAME", CodeManager.getName(GdlConstant.GDL_STATE_CODE_NAME, MapUtils.getString(map, "GDL_STATE")));
        map.put("GDL_NUMFORMAT_NAME",CodeManager.getName(GdlConstant.GDL_NUMFORMAT_CODENAME,MapUtils.getString(map,"NUM_FORMAT")));
        return map;
    }

    /**
     * 查询指标
     * @param data
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryGdl(Map<String,Object> data,Page page){
        String gdlType = MapUtils.getString(data,"GDL_TYPE");
        String gdlState = MapUtils.getString(data,"GDL_STATE");
        String gdlGroup = MapUtils.getString(data,"GDL_GROUP");
        String notType = MapUtils.getString(data,"NOT_GDL_TYPE");//要排除的类行
        String kwd = MapUtils.getString(data,"KEY_WORD");
        String columnSort = MapUtils.getString(data,"_COLUMN_SORT");

        String sql = "SELECT a.GDL_ID, a.GDL_CODE, a.GDL_NAME, a.GDL_TYPE, a.GDL_SRC_TABLE_NAME, " +
                "a.GDL_SRC_TABLE_ID, a.GDL_SRC_COL, a.GDL_SRC_COL_ID, a.GDL_COL_NAME, a.GDL_BUS_DESC, " +
                "a.SRC_CALC_GROUP, a.GDL_CALC_EXPR, a.GDL_UNIT, a.USER_ID,b.USER_NAMECN, " +
                "to_char(a.CREATE_TIME,'yyyy-mm-dd hh24:mi:ss') CREATE_TIME, " +
                "to_char(a.VALID_TIME,'yyyy-mm-dd hh24:mi:ss') VALID_TIME, " +
                " a.GDL_STATE, a.GDL_VERSION,a.NUM_FORMAT FROM META_GDL a " +
                " LEFT JOIN META_MAG_USER b ON a.USER_ID=b.USER_ID " ;
        if(gdlGroup!=null && !"".equals(gdlGroup)){
            sql += "LEFT JOIN META_GDL_GROUP_REL c ON a.GDL_ID=c.GDL_ID ";
        }
        sql += " WHERE 1=1 ";
        List<Object> param = new ArrayList<Object>();
        if(gdlType!=null && !"".equals(gdlType)){
            sql += " AND a.GDL_TYPE=? ";
            param.add(Convert.toInt(gdlType));
        }else{
            if(notType!=null && !"".equals(notType)){
                sql += " AND a.GDL_TYPE NOT IN"+SqlUtils.inParamDeal(notType.split(","));
            }
        }
        if("1".equals(gdlState)){  //查询线上指标
            sql += " AND a.GDL_STATE="+GdlConstant.GDL_STATE_VALID;
        }else if("3".equals(gdlState)){//查询线下指标
            sql += " AND a.GDL_STATE="+GdlConstant.GDL_STATE_DOWNLINE;
//            sql += " AND a.GDL_STATE="+GdlConstant.GDL_STATE_DOWNLINE +
//                   " AND a.GDL_VERSION =(SELECT MAX(x.GDL_VERSION) FROM META_GDL_ALTER_HISTORY x" +
//                    " WHERE x.GDL_ID=a.GDL_ID AND x.ALTER_TYPE="+GdlConstant.GDL_ALTER_TYPE_DOWN+") ";
        }else{//线上和线下
            sql+= " AND (a.GDL_STATE="+GdlConstant.GDL_STATE_VALID;
            sql+= " OR a.GDL_STATE="+GdlConstant.GDL_STATE_DOWNLINE+") ";
//            sql += " AND (a.GDL_STATE="+GdlConstant.GDL_STATE_VALID + " OR (" ;
//            sql += "a.GDL_STATE="+GdlConstant.GDL_STATE_INVALID;
//            sql += " AND NOT EXISTS(SELECT 1 FROM META_GDL x WHERE x.GDL_ID=a.GDL_ID AND x.GDL_STATE="+GdlConstant.GDL_STATE_VALID+")";
//            sql += " AND a.GDL_VERSION =(SELECT MAX(x.GDL_VERSION) FROM META_GDL_ALTER_HISTORY x" +
//                   " WHERE x.GDL_ID=a.GDL_ID AND x.ALTER_TYPE="+GdlConstant.GDL_ALTER_TYPE_DOWN+")";
//            sql += "))";
        }

        if(gdlGroup!=null && !"".equals(gdlGroup)){
            sql += " AND c.GDL_GROUP_ID IN"+SqlUtils.inParamDeal(gdlGroup.split(","));
        }
        if(kwd!=null && !"".equals(kwd)){
            if (!kwd.contains("%") && !kwd.contains("_")) {
                sql += "AND (Upper(A.GDL_NAME) LIKE UPPER(?) OR ";
                sql += "Upper(A.GDL_CODE) LIKE UPPER(?) OR ";
                sql += "UPPER(A.GDL_BUS_DESC) LIKE UPPER(?)) ";
                param.add("%" + kwd + "%");
                param.add("%" + kwd + "%");
                param.add("%" + kwd + "%");
            } else {
                kwd = kwd.replaceAll("_", "/_").replaceAll("%", "/%");
                sql += "AND (Upper(A.GDL_NAME) LIKE UPPER(?) ESCAPE '/' OR ";
                sql += "Upper(A.GDL_CODE) LIKE UPPER(?) ESCAPE '/' OR ";
                sql += "UPPER(A.GDL_BUS_DESC) LIKE UPPER(?) ESCAPE '/') ";
                param.add("%" + kwd + "%");
                param.add("%" + kwd + "%");
                param.add("%" + kwd + "%");
            }
        }
        if(columnSort!=null && !"".equals(columnSort)){
            sql += " ORDER BY "+columnSort;
        }else{
            sql += " ORDER BY a.VALID_TIME DESC ";
        }
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        List<Map<String,Object>> list = getDataAccess().queryForList(sql,param.toArray());
        for(Map<String,Object> map : list){
            map.put("GDL_TYPE_NAME", CodeManager.getName(GdlConstant.GDL_TYPE_CODE_NAME, MapUtils.getString(map, "GDL_TYPE")));
            map.put("GDL_STATE_NAME", CodeManager.getName(GdlConstant.GDL_STATE_CODE_NAME, MapUtils.getString(map, "GDL_STATE")));
            map.put("GDL_NUMFORMAT_NAME",CodeManager.getName(GdlConstant.GDL_NUMFORMAT_CODENAME,MapUtils.getString(map,"NUM_FORMAT")));
        }
        return list;
    }

    /**
     * 添加指标关系（实现方式为先删再加）
     * @param gdlId 指标ID
     * @param ids 分组ID
     * @return
     */
    public boolean addGdlGroup(int gdlId,String[] ids){
        String sql = "DELETE FROM META_GDL_GROUP_REL WHERE GDL_ID=?";
        String addSql = "INSERT INTO META_GDL_GROUP_REL(GDL_ID,GDL_GROUP_ID) VALUES(?,?)";
        getDataAccess().execUpdate(sql,gdlId);
        Object[][] params = new Object[ids.length][2];
        for(int i=0;i<ids.length;i++){
            params[i] = new Object[]{gdlId,Convert.toInt(ids[i])};
        }
        getDataAccess().execUpdateBatch(addSql,params);
        return true;
    }

    /**
     * 设置父关系
     * @param gdlId
     * @param ids
     * @return
     */
    public boolean setParentGdlRel(int gdlId,String[] ids){
        String sql = "DELETE FROM META_GDL_REL WHERE GDL_ID=?";
        String addSql = "INSERT INTO META_GDL_REL(GDL_ID,PAR_GDL_ID) VALUES(?,?)";
        getDataAccess().execUpdate(sql,gdlId);
        Object[][] params = new Object[ids.length][2];
        for(int i=0;i<ids.length;i++){
            params[i] = new Object[]{gdlId,Convert.toInt(ids[i])};
        }
        getDataAccess().execUpdateBatch(addSql,params);
        return true;
    }

    /**
     * 指标下线，可用于部分场合把指标设为无效
     * @param gdlId
     * @param version
     * @return
     */
    public int downLine(int gdlId,int version){
        String sql = "UPDATE META_GDL SET GDL_STATE="+GdlConstant.GDL_STATE_DOWNLINE +
                " WHERE GDL_ID=? AND GDL_STATE="+GdlConstant.GDL_STATE_VALID +
                " AND GDL_VERSION=?";
        return getDataAccess().execUpdate(sql,gdlId,version);
    }

    /**
     * 指标上线
     * @param gdlId
     * @param version
     * @return
     */
    public int upLine(int gdlId,int version){
        String sql = "UPDATE META_GDL SET GDL_STATE="+GdlConstant.GDL_STATE_VALID +
                " WHERE GDL_ID=? AND GDL_VERSION=? AND GDL_STATE="+GdlConstant.GDL_STATE_DOWNLINE;
        return getDataAccess().execUpdate(sql,gdlId,version);
    }

    /**
     * 根据指标集合查询出指标的基本信息
     *
     * @param gdlIds
     * @return
     */
    public List<Map<String,Object>> queryGdlInfos(List<Integer> gdlIds) {
        String sql = "SELECT GDL_ID, GDL_CODE, GDL_NAME, GDL_TYPE, GDL_SRC_TABLE_NAME" +
                ", GDL_SRC_TABLE_ID, GDL_SRC_COL, GDL_SRC_COL_ID, GDL_COL_NAME, GDL_BUS_DESC, " +
                "SRC_CALC_GROUP, GDL_CALC_EXPR,GDL_VERSION," +
                " NUM_FORMAT FROM META_GDL T WHERE T.GDL_STATE=" + GdlConstant.GDL_STATE_VALID
                + " AND T.GDL_ID IN " + SqlUtils.inParamDeal(gdlIds);
        return getDataAccess().queryForList(sql);
    }
    
    /**
     * 通过指标id得到最大版本号的下一个版本号
     * @return
     */
    public int queryNextVersion(int gdlId){
    	String sql = "SELECT (MAX(T.GDL_VERSION)+1) AS MAX_VERSION " +
    			" FROM META_GDL T WHERE T.GDL_ID=?";
    	return getDataAccess().queryForInt(sql, gdlId);
    }
    

	/**
	 * 通过维度表id，归并类型id,得到对应维度code的名称
	 * @param dimTableId 维度表id
	 * @param dimTypeId 归并类型id
	 * @param code 维度值code
	 * @return 维度code对应的名称
	 * @author 李国民
	 */
	public String getNameByCode(String dimTableId, String dimTypeId, String code){
		String name = "";
		if(!dimTableId.equals("")&&!dimTypeId.equals("")&&!code.equals("")){
			String sql = "SELECT I.TABLE_OWNER, T.DATA_SOURCE_ID," +
					" T.TABLE_NAME, T.TABLE_DIM_PREFIX" +
					" FROM META_DIM_TABLES T" +
					" LEFT JOIN META_TABLE_INST I ON T.DIM_TABLE_ID = I.TABLE_ID" +
					" WHERE I.STATE = 1 AND T.DIM_TABLE_ID = ?";
	        Map<String,Object> dimTableMap = getDataAccess().queryForMap(sql,dimTableId);
	        if(dimTableMap!=null&&dimTableMap.size()>0){
	        	String dimTableName =  MapUtils.getString(dimTableMap, "TABLE_NAME");			//维度表名
	        	String dimTablePrefix = MapUtils.getString(dimTableMap, "TABLE_DIM_PREFIX").toUpperCase();	//维度表前缀
	        	String tableOwner = MapUtils.getString(dimTableMap, "TABLE_OWNER");		//用户
	        	String dataSoruceId = MapUtils.getString(dimTableMap, "DATA_SOURCE_ID");	//数据源id
	        	
	        	sql = "SELECT T."+dimTablePrefix+"_NAME FROM "+tableOwner+"."+dimTableName+" T " +
	        			" WHERE T."+dimTablePrefix+"_CODE=? AND T.DIM_TYPE_ID=?";
		        Map<String,Object> codeNameMap = getDataAccess(dataSoruceId).queryForMap(sql,code,dimTypeId);
		        if(codeNameMap!=null&&codeNameMap.size()>0){
		        	name = MapUtils.getString(codeNameMap,(dimTablePrefix+"_NAME"),"");
		        }
	        }
		}
		return name.equals("")?code:name;
	}
    
}
