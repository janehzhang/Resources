package tydic.portalCommon.serviceManage.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 * 
 * @author yhd
 *
 */
public class MakeReportDAO extends MetaBaseDAO {
	  /**
	    * 列表
	    * @param queryData
	    * @param page
	    * @return
	    */
       public List<Map<String,Object>> queryReports(Map<?,?> queryData, Page page){
       String createId=MapUtils.getString(queryData, "createId", null);
    	StringBuffer sql = new StringBuffer("SELECT ID,MONTHID MONTH_ID,TITLE,STATUS FROM meta_portal_makeReport WHERE 1=1" +
    			" AND createid ='"+createId+"'"); 
        Object monthId = queryData.get("monthId");
        Object title = queryData.get("title");
        List<Object> params=new ArrayList<Object>();
        if(monthId != null && !"".equals(monthId)){
            sql.append(" AND monthId=?");
            params.add(Convert.toString(monthId));
        }
        if(title != null && !"".equals(title)){
            sql.append(" AND TITLE LIKE  "+SqlUtils.allLikeParam(Convert.toString(title).trim()));
        }
        String pageSql=sql.toString();
        
        pageSql = pageSql + " ORDER by STATUS,monthId DESC";
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        return getDataAccess().queryForList(pageSql, params.toArray());
    }

 public boolean insertReport(Map<String, Object> map) {
		String sql = "INSERT INTO  meta_portal_makeReport(id,monthId,title,gzld,gzlid,gzjy,zgqk,createId,createName,createTime,endTime,status,GUID)" +
				     " VALUES(?,?,?,?,?,?,?,?,?,SYSDATE,TO_DATE(?,'yyyy-mm-dd'),?,?)";
		Object[] param = {
				MapUtils.getString(map, "keyId", null),
				DateUtil.getDateforStampMon(MapUtils.getString(map, "monthId", null)),
				MapUtils.getString(map, "title", null),
				MapUtils.getString(map, "gzld", null),
				MapUtils.getString(map, "gzlid"),
				MapUtils.getString(map, "gzjy", null),
				MapUtils.getString(map, "zgqk", null),
				MapUtils.getString(map, "createId", null),
				MapUtils.getString(map, "createName", null),
			    DateUtil.getDateforStamp(MapUtils.getString(map, "endTime", null)),
			    MapUtils.getString(map, "status", null),
			    MapUtils.getString(map, "GUID", null)
		 };
		return getDataAccess().execNoQuerySql(sql,param);
	
	}
   public boolean  deleteReport(String id){
           StringBuffer sql = new StringBuffer("DELETE FROM meta_portal_makeReport WHERE ID ='"+id+"'");
           return getDataAccess().execUpdate(sql.toString()) != -1;
   } 
   
   public boolean updateReport(Map<String, Object> map) {
	    StringBuffer sql = new StringBuffer("Update meta_portal_makeReport t  set t.status=?  where id=?");
	    Object[] param = {MapUtils.getString(map, "status", null) , 
	    		          MapUtils.getString(map, "id", null)};
        return getDataAccess().execUpdate(sql.toString(),param) != -1;
	}
	
   public Map<String, Object> getMakeReportById(long id) {
		StringBuffer buffer = new StringBuffer("select * FROM  meta_portal_makeReport WHERE ID = ?");
		return getDataAccess().queryForMap(buffer.toString(), new Object[]{id});
	}
}
