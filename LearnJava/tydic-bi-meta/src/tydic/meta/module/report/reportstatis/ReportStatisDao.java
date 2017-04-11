
/**   
 * @文件名: ReportStatisDao.java
 * @包 tydic.meta.module.report.reportstatis
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-2-21 下午05:28:08
 *  
 */
  
package tydic.meta.module.report.reportstatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportStatisDao   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-2-21 下午05:28:08   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportStatisDao extends MetaBaseDAO{
	private String day ="1";
	private String week ="2";
	private String month ="3";
	/**
	 * @Title: getAllRepConfigType 
	 * @Description: 获取所有报表分类信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getAllRepConfigType(){
		String sql = "select t.REPORT_TYPE_ID,t.REPORT_TYPE_NAME,t.PARENT_ID,t.REPORT_TYPE_ORDER from meta_rep_config_type t";
		return getDataAccess().queryForList(sql);
	}
	public List<Map<String,Object>> getQueryTables(Map<String,String> queryMessage,Map<String,Object> userMap){
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		//个人
		buffer.append(" select '"+userMap.get("userNamecn")+"' USER_NAMECN,b.USE_USER,nvl(a.creattypenum,0) CREATE_TYPE_NUM,nvl(b.readtypenum,0) READTYPENUM,nvl(b.opentypenum,0)"); 
		buffer.append(" OPENTYPENUM from(select "+userMap.get("userId")+" operate_user, count(1) creattypenum from META_REP_CONFIG_LOG a where a.OPERATE_TYPE = 11");
		buffer.append(" and a.operate_user = "+userMap.get("userId")+"");
		if(queryMessage == null){
			if(queryMessage.get("dateTime")!=null && !queryMessage.get("dateTime").toString().equals("")){
				if(day.equals(queryMessage.get("dateTime"))){
					buffer.append(" AND a.operate_time = sysdate");
				}else if(week.equals(queryMessage.get("dateTime"))){
					buffer.append(" AND a.operate_time >= trunc(next_day(sysdate-8,1)+1) and a.operate_time <= trunc(next_day(sysdate-8,1)+7)+1");
				}else if(month.equals(queryMessage.get("dateTime"))){
					buffer.append(" AND a.operate_time >= trunc(sysdate,'YYYY') and a.operate_time <= add_months(trunc(sysdate,'YYYY'),12)-1");
				}
            }
//			if(queryMessage.get("repTypeId")!=null && !queryMessage.get("repTypeId").toString().equals("")){
//				buffer.append(" AND  = ? ");
//                params.add(queryMessage.get("repTypeId"));
//            }
//			if(queryMessage.get("repName")!=null && !queryMessage.get("repName").toString().equals("")){
//				buffer.append(" AND  = ? ");
//                params.add(queryMessage.get("repName"));
//            }
		}
		buffer.append(" ) a,(select "+userMap.get("userId")+" use_user,sum(decode(b.operate_type, 21, 1,0)) readtypenum,");
		buffer.append(" sum(decode(b.operate_type, 31, 1,0)) opentypenum from META_REP_USE_LOG b where b.OPERATE_TYPE in (21, 31) and b.use_user = "+userMap.get("userId"));
		if(queryMessage == null){
			if(queryMessage.get("dateTime")!=null && !queryMessage.get("dateTime").toString().equals("")){
				if(day.equals(queryMessage.get("dateTime"))){
					buffer.append(" AND b.use_time = sysdate");
				}else if(week.equals(queryMessage.get("dateTime"))){
					buffer.append(" AND b.use_time >= trunc(next_day(sysdate-8,1)+1) and b.use_time <= trunc(next_day(sysdate-8,1)+7)+1");
				}else if(month.equals(queryMessage.get("dateTime"))){
					buffer.append(" AND b.use_time >= trunc(sysdate,'YYYY') and b.use_time <= add_months(trunc(sysdate,'YYYY'),12)-1");
				}
            }
		}
		buffer.append(" ) b where a.operate_user=b.use_user(+)");
		//部门
		buffer.append(" union all select b.DEPT_NAME,a.use_dept,a.creattypenum,a.readtypenum,a.opentypenum from");
		buffer.append(" (select a.use_dept,nvl(b.creattypenum,0) creattypenum,a.readtypenum,a.opentypenum from (select "+userMap.get("deptId")+" use_dept,");
		buffer.append(" sum(decode(b.operate_type, 21, 1,0)) readtypenum,sum(decode(b.operate_type, 31, 1,0)) opentypenum from META_REP_USE_LOG b");
		buffer.append(" where b.OPERATE_TYPE in (21, 31) and b.use_dept = "+userMap.get("deptId")+") a,(select "+userMap.get("deptId")+" operate_dept, count(1) creattypenum");
		buffer.append(" from META_REP_CONFIG_LOG a where a.OPERATE_TYPE = 11 and a.operate_dept="+userMap.get("deptId"));
		buffer.append(" ) b where a.use_dept(+)=b.operate_dept ) a,META_DIM_USER_DEPT b where a.use_dept=b.DEPT_CODE");
		//岗位
		buffer.append(" union all select b.STATION_NAME,a.use_station,a.creattypenum,a.readtypenum,a.opentypenum from                                  ");
		buffer.append(" (select a.use_station,nvl(b.creattypenum,0) creattypenum,a.readtypenum,a.opentypenum from (select "+userMap.get("stationId")+" use_station,");
		buffer.append(" sum(decode(b.operate_type, 21, 1,0)) readtypenum,sum(decode(b.operate_type, 31, 1,0)) opentypenum from META_REP_USE_LOG b");
		buffer.append(" where b.OPERATE_TYPE in (21, 31) and b.use_station = "+userMap.get("stationId")+") a,(select "+userMap.get("stationId")+" operate_station, count(1) creattypenum");
		buffer.append(" from META_REP_CONFIG_LOG a where a.OPERATE_TYPE = 11 and a.operate_station="+userMap.get("stationId")+") b where a.use_station(+)=b.operate_station)");
		buffer.append("  a,META_DIM_USER_STATION b where a.use_station=b.STATION_CODE");
		//部门最高
		buffer.append(" union all select b.DEPT_NAME||'订阅最高者：admin'  max_dept_user_name,a.use_user,a.creattypenum,a.readtypenum,a.opentypenum    ");
		buffer.append(" from(select a.use_dept,a.use_user,a.creattypenum,a.readtypenum,a.opentypenum from (                                            ");
		buffer.append(" select a.use_dept,a.use_user,nvl(b.creattypenum,0) creattypenum,a.readtypenum,a.opentypenum from(                              ");
		buffer.append(" select "+userMap.get("deptId")+" use_dept,b.use_user,sum(decode(b.operate_type, 21, 1,0)) readtypenum,sum(decode(b.operate_type, 31, 1,0))");
		buffer.append(" opentypenum from META_REP_USE_LOG b where b.OPERATE_TYPE in (21, 31) and b.use_dept = "+userMap.get("deptId")+" group by b.use_user) a ,(");
		buffer.append(" select "+userMap.get("deptId")+" operate_dept,a.operate_user,nvl(sum(a.OPERATE_TYPE),0) creattypenum from META_REP_CONFIG_LOG a where");
		buffer.append(" a.OPERATE_TYPE = 11 and a.operate_dept="+userMap.get("deptId")+" group by a.operate_user) b where a.use_dept=b.operate_dept(+)");
		buffer.append(" and a.use_user=b.operate_user(+) order by a.readtypenum desc,a.opentypenum desc,b.creattypenum desc) a where rownum<=1 ) a,");
		buffer.append(" META_DIM_USER_DEPT b where a.use_dept=b.DEPT_CODE");
		//岗位
		buffer.append(" union all select b.STATION_NAME||'订阅最高者：admin' max_station_user_name,a.use_user,a.creattypenum,a.readtypenum,");
		buffer.append(" a.opentypenum from(select a.use_station,a.use_user,a.creattypenum,a.readtypenum,a.opentypenum from");
		buffer.append(" (select a.use_station,a.use_user,nvl(b.creattypenum,0) creattypenum,a.readtypenum,a.opentypenum from(");
		buffer.append(" select "+userMap.get("stationId")+" use_station,b.use_user,sum(decode(b.operate_type, 21, 1,0)) readtypenum,");
		buffer.append(" sum(decode(b.operate_type, 31, 1,0)) opentypenum from META_REP_USE_LOG b where b.OPERATE_TYPE in (21, 31)");
		buffer.append(" and b.use_station = "+userMap.get("stationId")+" group by b.use_user) a ,(select "+userMap.get("stationId")+" operate_station,a.operate_user,                                      ");
		buffer.append(" nvl(sum(a.OPERATE_TYPE),0) creattypenum from META_REP_CONFIG_LOG a where a.OPERATE_TYPE = 11 and a.operate_station="+userMap.get("stationId"));
		buffer.append(" group by a.operate_user) b where a.use_station=b.operate_station(+) and a.use_user=b.operate_user(+)");
		buffer.append(" order by a.readtypenum desc,a.opentypenum desc,b.creattypenum desc");
		buffer.append(" ) a where rownum<=1) a, META_DIM_USER_STATION b where a.use_station=b.STATION_CODE");
		// repName=null, dateTime=1, repType=null,
		
		return getDataAccess().queryForList(buffer.toString(), params.toArray());
	}
	/**
	 * 
	 * @Title: getRepAppStaAnaMes 
	 * @Description:获取报表应用分析数据 
	 * @param queryMessage
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepAppStaAnaMes(Map<String,String> queryMessage){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select b.DEPT_NAME,a.use_dept,a.CREATETYPENUM,a.READTYPENUM,a.OPENTYPENUM from(select a.use_dept,nvl(b.creattypenum,0)"); 
		buffer.append(" creattypenum,a.readtypenum,a.opentypenum from (select b.use_dept,sum(decode(b.operate_type, 21, 1,0)) readtypenum,");
		buffer.append(" sum(decode(b.operate_type, 31, 1,0)) opentypenum from");
		if((queryMessage.get("repType") == null || "".equals(queryMessage.get("repType")) || "null".equals(queryMessage.get("repType")))
			&& (queryMessage.get("repName") == null || "".equals(queryMessage.get("repName")) || "null".equals(queryMessage.get("repName"))))
			buffer.append(" META_REP_USE_LOG b,");
		else{
			buffer.append(" (select b.* from META_REP_USE_LOG b,(");
			buffer.append(" select a.REPORT_ID from META_REP_CONFIG_REPORT a,META_REP_CONFIG_TYPE c");
			buffer.append(" where a.REPORT_TYPE_ID=c.REPORT_TYPE_ID");
			if(queryMessage.get("repName") != null && !"".equals(queryMessage.get("repName")) && !"null".equals(queryMessage.get("repName"))){
				buffer.append(" and a.REPORT_NAME like "+ SqlUtils.allLikeParam(queryMessage.get("repName").toString()));
			}
			if(queryMessage.get("repType") != null && !"".equals(queryMessage.get("repType")) && !"null".equals(queryMessage.get("repType"))){
				buffer.append(" and c.REPORT_TYPE_ID="+queryMessage.get("repType"));
			}
			buffer.append(" )a where b.REPORT_ID=a.REPORT_ID) b,");
		}
		buffer.append(" (select a.USER_ID,b.DIM_LEVEL from");
		buffer.append(" META_MAG_USER a,META_DIM_ZONE b where a.ZONE_ID=b.zone_id and b.DIM_LEVEL="+queryMessage.get("DIM_LEVEL")+") a");
		buffer.append(" where b.USE_USER=a.USER_ID and b.OPERATE_TYPE in (21, 31)");
		if("1".equals(queryMessage.get("dateTime")))//当天
			buffer.append(" and to_date(to_char(b.USE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+queryMessage.get("currentDay")+"','yyyy-MM-dd')");
		else if("2".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(b.USE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}else if("3".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(b.USE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}
		buffer.append(" group by b.use_dept) a,(select a.operate_dept, count(1) creattypenum from");
		if((queryMessage.get("repType") == null || "".equals(queryMessage.get("repType")) || "null".equals(queryMessage.get("repType")))
				&& (queryMessage.get("repName") == null || "".equals(queryMessage.get("repName")) || "null".equals(queryMessage.get("repName"))))
			buffer.append(" META_REP_CONFIG_LOG a,");
		else{
			buffer.append(" (select a.* from META_REP_CONFIG_LOG a,(");
			buffer.append(" select a.REPORT_ID from META_REP_CONFIG_REPORT a,META_REP_CONFIG_TYPE c");
			buffer.append(" where a.REPORT_TYPE_ID=c.REPORT_TYPE_ID");
			if(queryMessage.get("repName") != null && !"".equals(queryMessage.get("repName")) && !"null".equals(queryMessage.get("repName"))){
				buffer.append(" and a.REPORT_NAME like "+SqlUtils.allLikeParam(queryMessage.get("repName").toString()));
			}
			if(queryMessage.get("repType") != null && !"".equals(queryMessage.get("repType")) && !"null".equals(queryMessage.get("repType"))){
				buffer.append(" and c.REPORT_TYPE_ID="+queryMessage.get("repType"));
			}
			buffer.append(" )b where b.REPORT_ID=a.REPORT_ID) a,");
		}
		buffer.append(" (select a.USER_ID,b.DIM_LEVEL from META_MAG_USER a,META_DIM_ZONE b");
		buffer.append(" where a.ZONE_ID=b.zone_id and b.DIM_LEVEL="+queryMessage.get("DIM_LEVEL"));
		buffer.append(" ) b where a.OPERATE_USER=b.USER_ID and a.OPERATE_TYPE = 11");
		if("1".equals(queryMessage.get("dateTime")))//当天
			buffer.append(" and to_date(to_char(a.OPERATE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+queryMessage.get("currentDay")+"','yyyy-MM-dd')");
		else if("2".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(a.OPERATE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}else if("3".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(a.OPERATE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}
		buffer.append(" group by a.operate_dept");
		buffer.append(" ) b where a.use_dept=b.operate_dept(+)) a,META_DIM_USER_DEPT b where a.use_dept=b.DEPT_CODE");
		buffer.append(" union all select '合计' DEPT_NAME,0 use_dept,sum(a.creattypenum) creattypenum,sum(a.readtypenum) readtypenum,");
		buffer.append(" sum(a.opentypenum) opentypenum from(select a.use_dept,nvl(b.creattypenum,0) creattypenum,a.readtypenum,a.opentypenum");
		buffer.append(" from (select b.use_dept,sum(decode(b.operate_type, 21, 1,0)) readtypenum,");
		buffer.append(" sum(decode(b.operate_type, 31, 1,0)) opentypenum from");
		if((queryMessage.get("repType") == null || "".equals(queryMessage.get("repType")) || "null".equals(queryMessage.get("repType")))
				&& (queryMessage.get("repName") == null || "".equals(queryMessage.get("repName")) || "null".equals(queryMessage.get("repName"))))
				buffer.append(" META_REP_USE_LOG b,");
		else{
			buffer.append(" (select b.* from META_REP_USE_LOG b,(");
			buffer.append(" select a.REPORT_ID from META_REP_CONFIG_REPORT a,META_REP_CONFIG_TYPE c");
			buffer.append(" where a.REPORT_TYPE_ID=c.REPORT_TYPE_ID");
			if(queryMessage.get("repName") != null && !"".equals(queryMessage.get("repName")) && !"null".equals(queryMessage.get("repName"))){
				buffer.append(" and a.REPORT_NAME like "+SqlUtils.allLikeParam(queryMessage.get("repName").toString()));
			}
			if(queryMessage.get("repType") != null && !"".equals(queryMessage.get("repType")) && !"null".equals(queryMessage.get("repType"))){
				buffer.append(" and c.REPORT_TYPE_ID="+queryMessage.get("repType"));
			}
			buffer.append(" )a where b.REPORT_ID=a.REPORT_ID) b,");
		}
		buffer.append(" (select a.USER_ID,b.DIM_LEVEL from META_MAG_USER a,META_DIM_ZONE b where a.ZONE_ID=b.zone_id and b.DIM_LEVEL="+queryMessage.get("DIM_LEVEL")+") a");
		buffer.append(" where b.USE_USER=a.USER_ID and b.OPERATE_TYPE in (21, 31)");
		if("1".equals(queryMessage.get("dateTime")))//当天
			buffer.append(" and to_date(to_char(b.USE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+queryMessage.get("currentDay")+"','yyyy-MM-dd')");
		else if("2".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(b.USE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}else if("3".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(b.USE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}
		buffer.append(" group by b.use_dept) a,(select a.operate_dept, count(1) creattypenum from");
		if((queryMessage.get("repType") == null || "".equals(queryMessage.get("repType")) || "null".equals(queryMessage.get("repType")))
				&& (queryMessage.get("repName") == null || "".equals(queryMessage.get("repName")) || "null".equals(queryMessage.get("repName"))))
			buffer.append(" META_REP_CONFIG_LOG a,");
		else{
			buffer.append(" (select a.* from META_REP_CONFIG_LOG a,(");
			buffer.append(" select a.REPORT_ID from META_REP_CONFIG_REPORT a,META_REP_CONFIG_TYPE c");
			buffer.append(" where a.REPORT_TYPE_ID=c.REPORT_TYPE_ID");
			if(queryMessage.get("repName") != null && !"".equals(queryMessage.get("repName")) && !"null".equals(queryMessage.get("repName"))){
				buffer.append(" and a.REPORT_NAME like "+SqlUtils.allLikeParam(queryMessage.get("repName").toString()));
			}
			if(queryMessage.get("repType") != null && !"".equals(queryMessage.get("repType")) && !"null".equals(queryMessage.get("repType"))){
				buffer.append(" and c.REPORT_TYPE_ID="+queryMessage.get("repType"));
			}
			buffer.append(" )b where b.REPORT_ID=a.REPORT_ID) a,");
		}
		buffer.append(" (select a.USER_ID,b.DIM_LEVEL from META_MAG_USER a,META_DIM_ZONE b");
		buffer.append(" where a.ZONE_ID=b.zone_id and b.DIM_LEVEL="+queryMessage.get("DIM_LEVEL"));
		buffer.append(" ) b where a.OPERATE_USER=b.USER_ID and a.OPERATE_TYPE = 11");
		if("1".equals(queryMessage.get("dateTime")))//当天
			buffer.append(" and to_date(to_char(a.OPERATE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+queryMessage.get("currentDay")+"','yyyy-MM-dd')");
		else if("2".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(a.OPERATE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}else if("3".equals(queryMessage.get("dateTime"))){//本周
			buffer.append(" and (to_date(to_char(a.OPERATE_TIME,'yyyy-MM-dd'),'yyyy-MM-dd') between to_date('"+queryMessage.get("startday")+"','yyyy-MM-dd')");
			buffer.append(" and to_date('"+queryMessage.get("endday")+"','yyyy-MM-dd'))");
		}
		buffer.append(" group by a.operate_dept) b where a.use_dept=b.operate_dept(+)) a,META_DIM_USER_DEPT b");
		buffer.append(" where a.use_dept=b.DEPT_CODE");
		String pageSql = buffer.toString();
		pageSql += " order by b.DEPT_NAME";
		return getDataAccess().queryForList(pageSql);
	}
}
