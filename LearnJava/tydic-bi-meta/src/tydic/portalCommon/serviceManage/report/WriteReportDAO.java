package tydic.portalCommon.serviceManage.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 * 
 * @author yhd
 *
 */
public class WriteReportDAO extends MetaBaseDAO {
	  /**
	    * 列表
	    * @param queryData
	    * @param page
	    * @return
	    */
  public List<Map<String,Object>> queryWriteReports(Map<String,Object> queryData, Page page){
    	   Object monthId = queryData.get("monthId");
           Object title = queryData.get("title");
           String createId=MapUtils.getString(queryData, "createId", null);
           StringBuffer sql = new StringBuffer("select a.ID, a.MONTHID MONTH_ID, a.TITLE, a.STATUS, a.MAKEID MAKE_ID,(case when  SYSDATE >= b.endtime  then 'true' else 'false' end) end_time " +
           		" from meta_portal_writeReport a , meta_portal_makeReport b  where  a.makeid=b.id and a.createid='"+createId+"'"); 
        List<Object> params=new ArrayList<Object>();
        if(monthId != null && !"".equals(monthId)){
            sql.append(" AND a.monthId=?");
            params.add(Convert.toString(monthId));
        }
        if(title != null && !"".equals(title)){
            sql.append(" AND a.TITLE LIKE  "+SqlUtils.allLikeParam(Convert.toString(title).trim()));
        }
        String pageSql=sql.toString();
        pageSql = pageSql + " ORDER by a.STATUS,a.monthId DESC";
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        return getDataAccess().queryForList(pageSql, params.toArray());
    }
  
  
  
   public List<Map<String, Object>> queryMakeReports(Page page) {
		StringBuffer sql = new StringBuffer("SELECT ID,MONTHID MONTH_ID,TITLE FROM meta_portal_makeReport WHERE 1=1 "); 
        String pageSql=sql.toString();
        pageSql = pageSql + " ORDER by monthId DESC";
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        return getDataAccess().queryForList(pageSql);
	}

   public Map<String, Object> getMakeReportById(String id) {
		String sql = "SELECT t.* FROM  meta_portal_makereport t WHERE t.ID = ?";
		return getDataAccess().queryForMap(sql, new Object[]{id});
	}
 
 public boolean insertReport(Map<String, Object> map) {
	 String sql = "INSERT INTO  meta_portal_writeReport(ID,MAKEID,MONTHID,TITLE,GZLD,GZLID,GZJY,ZGQK,CREATEID,CREATENAME,CREATETIME,STATUS,MAKEUSERID,MAKEUSERNAME)"
                            +" VALUES(?,?,?,?,?,?,?,?,?,?,SYSDATE,'0',?,?)";
		Object[] param = {
		queryForNextVal("seq_app_id"),
		MapUtils.getString(map, "makeId", null),
		MapUtils.getString(map, "monthId", null),
		MapUtils.getString(map, "title", null),
		MapUtils.getString(map, "gzld", null),
		MapUtils.getString(map, "gzlid"),
		MapUtils.getString(map, "gzjy", null),
		MapUtils.getString(map, "zgqk", null),
		MapUtils.getString(map, "createId", null),
		MapUtils.getString(map, "createName", null),
		MapUtils.getString(map, "makeUserId", null),
		MapUtils.getString(map, "makeUserName", null)
		};
		return getDataAccess().execNoQuerySql(sql, param);
    }
 
 public boolean deleteReport(String idStr) {
		   if (idStr != null &&  !"".equals(idStr)) {
	           StringBuffer sql = new StringBuffer("DELETE FROM meta_portal_writeReport WHERE ID IN ("+idStr+")");
	           return getDataAccess().execUpdate(sql.toString()) != -1;
	       } else {
	           return false;
	       }
	}
  /**
   *  审阅报告  add yanhd
   * 
   */
  
  public List<Map<String, Object>> queryVettintReports(Map<String, Object> queryData, Page page) {
		Object monthId = queryData.get("monthId");
		Object title = queryData.get("title");
		String createId = MapUtils.getString(queryData, "createId", null);
	   Object zoneId= queryData.get("zoneId");
		StringBuffer sql = new StringBuffer("select a.id, a.monthid MONTH_ID,c.zone_name,a.title,a.GZLD,a.GZLID,a.GZJY,a.ZGQK,a.createName CREATE_NAME, a.status, a.MAKEID MAKE_ID " +
				" from meta_portal_writeReport a, meta_mag_user b, meta_dim_zone c where a.createid = b.user_id " +
				" and b.zone_id = c.zone_id and a.status <> 0 and a.makeuserid='"+ createId + "'");
		List<Object> params = new ArrayList<Object>();
		if (monthId != null && !"".equals(monthId)) {
			sql.append(" AND a.monthId=?");
			params.add(Convert.toString(monthId));
		}
		if (title != null && !"".equals(title)) {
			sql.append(" AND a.TITLE LIKE  "
					+ SqlUtils.allLikeParam(Convert.toString(title).trim()));
		}
		if (zoneId != null && !"".equals(zoneId)) {
		     if ("1".equals(Convert.toString(zoneId))) {
				sql.append(" AND (c.zone_par_id=?  or c.zone_id=?)");
				params.add(Convert.toString(zoneId));
				params.add(Convert.toString(zoneId));
			} else {
				sql.append(" AND c.zone_id in(" + zoneId + ")");
				//params.add(Convert.toString(zoneId));
			}
		}		
		String pageSql = sql.toString();
		pageSql = pageSql + " ORDER by a.STATUS,a.monthId DESC";
		// 分页包装
		if (page != null) {
			pageSql = SqlUtils.wrapPagingSql(pageSql, page);
		}
		return getDataAccess().queryForList(pageSql, params.toArray());
	}
	
  
  //查看报告
 public List<Map<String, Object>> queryLookReports(Map<String, Object> queryData, Page page) {
	 	   Object monthId = queryData.get("monthId");
	       Object title = queryData.get("title");
	       Object zoneId= queryData.get("zoneId");
	       
		   StringBuffer sql = new StringBuffer(" SELECT a.id,a.monthid MONTH_ID,c.zone_name,a.title,a.gzld,a.gzlid,a.gzjy,a.zgqk,a.createName CREATE_NAME, a.status " +
		   		" FROM meta_portal_writeReport a, meta_mag_user b, meta_dim_zone c " +
		   		" where a.createid = b.user_id and b.zone_id = c.zone_id and status=2"); 
		        List<Object> params=new ArrayList<Object>();
			    if(monthId != null && !"".equals(monthId)){
			        sql.append(" AND a.monthId=?");
			        params.add(Convert.toString(monthId));
			    }
			    if(title != null && !"".equals(title)){
			        sql.append(" AND a.TITLE LIKE  "+SqlUtils.allLikeParam(Convert.toString(title).trim()));
			    }
				if (zoneId != null && !"".equals(zoneId)) {
				     if ("1".equals(Convert.toString(zoneId))) {
						sql.append(" AND (c.zone_par_id=?  or c.zone_id=?)");
						params.add(Convert.toString(zoneId));
						params.add(Convert.toString(zoneId));
					} else {
						sql.append(" AND c.zone_id in(" + zoneId + ")");
						//params.add(Convert.toString(zoneId));
					}
				}
			    String pageSql=sql.toString();
			    pageSql = pageSql + " ORDER by a.monthId DESC";
			    //分页包装
			    if(page!=null){
			        pageSql= SqlUtils.wrapPagingSql(pageSql, page);
			    }
			    return getDataAccess().queryForList(pageSql, params.toArray()); 	  
	}
  

   public Map<String, Object> getWriteReportById(String id) {
		String sql = "SELECT ID,MAKEID,MONTHID,TITLE,GZLD,GZLID,GZJY,ZGQK,CREATEID,CREATENAME," +
				"TO_CHAR(CREATETIME,'yyyy-mm-dd hh24:mi:ss') CREATETIME,AFFIXADDRESS,AFFIXNAME,STATUS,GUID FROM meta_portal_writeReport  WHERE ID = ?";
		return getDataAccess().queryForMap(sql, new Object[]{id});
		
	}
  
   public boolean deleteReportByMakeId(String makeId) {
	   StringBuffer sql = new StringBuffer("DELETE FROM meta_portal_writeReport WHERE MAKEID='"+makeId+"'");
       return getDataAccess().execUpdate(sql.toString()) != -1;
	}

	/**
	 * 更新表
	 * 
	 * @param map
	 * @return
	 */
	public boolean updateWriteReport(Map<String, Object> map) {
		String sql = "update meta_portal_writeReport a set a.gzld=?, a.gzlid=?, a.gzjy=? , a.zgqk=?, a.status=?,a.GUID=?,a.AFFIXADDRESS=?,a.AFFIXNAME=? where a.id=?";
		Object[] param = { 
				MapUtils.getString(map, "gzld", null),
				MapUtils.getString(map, "gzlid", null),
				MapUtils.getString(map, "gzjy", null),
				MapUtils.getString(map, "zgqk", null),
				MapUtils.getString(map, "status", null),
				MapUtils.getString(map, "GUID", null),
				MapUtils.getString(map, "iconUrl", null),
				MapUtils.getString(map, "affixName", null),
				MapUtils.getString(map, "id", null) 
			};
		return getDataAccess().execNoQuerySql(sql, param);
	}
   public Map<String, Object> queryMakeReportById(String id, String makeId) {
		String sql = " select b.*,a.gzld  gzld_value,a.gzlid  gzlid_value,a.gzjy  gzjy_value,a.zgqk zgqk_value,a.affixaddress,a.affixname from meta_portal_writeReport a" +
				     ",meta_portal_makereport b where a.makeid=b.id  and a.id=? and a.makeid=?";
        return getDataAccess().queryForMap(sql, new Object[]{id,makeId});
    }
    /**
     * 同意
     * @param map
     * @return
     */
	public boolean vettingWriteReport(Map<String, Object> map) {
		String sql = "update meta_portal_writeReport a set a.agree=?,a.status=? where a.id=?";
		Object[] param = { MapUtils.getString(map, "agree", null),
				MapUtils.getString(map, "status", null),
				MapUtils.getString(map, "id", null) };
		return getDataAccess().execNoQuerySql(sql, param);
	}
   
   //导出报表
   public List<Map<String, Object>> getListMapData(Map<String, Object> queryData) {
		 	   Object monthId = queryData.get("monthId");
		       Object zoneId=   queryData.get("zoneId");
			   StringBuffer sql = new StringBuffer("SELECT a.monthid,a.title,a.GZLD,a.GZLID,a.GZJY,a.ZGQK,c.zone_name,a.createName " +
			   		" FROM meta_portal_writeReport a, meta_mag_user b, meta_dim_zone c " +
			   		" where a.createid = b.user_id and b.zone_id = c.zone_id and status=2"); 
			      
			   List<Object> params=new ArrayList<Object>();
			        if(monthId != null && !"".equals(monthId)){
				        sql.append(" AND a.monthId=?");
				        params.add(Convert.toString(monthId));
				    }
					if (zoneId != null && !"".equals(zoneId)) {
					     if ("1".equals(Convert.toString(zoneId))) {
							sql.append(" AND (c.zone_par_id=?  or c.zone_id=?)");
							params.add(Convert.toString(zoneId));
							params.add(Convert.toString(zoneId));
						} else {
							sql.append(" AND c.zone_id in(" + zoneId + ")");
						}
					}
				   sql.append(" ORDER by a.monthId DESC");
				   return getDataAccess().queryForList(sql.toString(), params.toArray()); 	  
    }
}
