
/**   
 * @文件名: DataOfflineDao.java
 * @包 tydic.meta.module.report.dataOffline
 * @描述: TODO
 * @author wxlcdut@163.com
 * @创建日期 2012-3-14 下午06:11:50
 *  
 */
  
package tydic.meta.module.report.dataOffline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.sys.code.CodeManager;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：DataOfflineDao   
 * 类描述：   
 * 创建人：wuxlcdut@gmail.com 
 * 创建时间：2012-3-14 下午06:11:50   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class DataOfflineDao extends MetaBaseDAO{
	
	public List<Map<String,Object>> getDataModelMes(Map<String,Object> data,Page page){
		List<Object> params = new ArrayList<Object>();
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT A.*,B.TABLE_GROUP_NAME FROM (SELECT A.*,B.DATA_SOURCE_NAME FROM (");
		buffer.append(" SELECT A.DATA_SOURCE_ID,A.TABLE_ID,A.TABLE_NAME,A.TABLE_NAME_CN,");
		buffer.append(" A.TABLE_OWNER,A.TABLE_TYPE_ID,A.TABLE_GROUP_ID,");
		buffer.append(" B.ISSUE_ID, B.TABLE_ALIAS, B.ISSUE_STATE");
		buffer.append(" FROM META_TABLES A, META_RPT_MODEL_ISSUE_CONFIG B");
		buffer.append(" WHERE A.TABLE_ID = B.TABLE_ID AND A.TABLE_STATE=1");
		//模糊条件
		if(data.get("keyword") != null && !"".equals(""+data.get("keyword"))){
			buffer.append(" AND (A.TABLE_NAME LIKE ? ESCAPE '/' OR A.TABLE_NAME_CN LIKE ? ESCAPE '/' OR A.TABLE_BUS_COMMENT LIKE ? ESCAPE '/' OR B.TABLE_ALIAS LIKE ?)");
			String keyWord = (data.get("keyword")+"").toUpperCase();
			params.add(SqlUtils.allLikeBindParam(keyWord));
			params.add(SqlUtils.allLikeBindParam(keyWord));
			params.add(SqlUtils.allLikeBindParam(keyWord));
			params.add(SqlUtils.allLikeBindParam(keyWord));
		}
		//数据源ID
		if(data.get("dataSourceId") != null && !"-1".equals(""+data.get("dataSourceId"))){
			buffer.append(" AND A.DATA_SOURCE_ID=?");
			params.add(data.get("dataSourceId"));
		}
		//用户 
		if(data.get("tableOwner") != null && !"".equals(""+data.get("tableOwner"))){
			buffer.append(" AND A.TABLE_OWNER=?");
			params.add(data.get("tableOwner"));
		}
		//层次分类
		if(data.get("tableTypeId") != null && !"-1".equals(""+data.get("tableTypeId"))){
			buffer.append(" AND A.TABLE_TYPE_ID=?");
			params.add(data.get("tableTypeId"));
		}
		//模型状态
		if(data.get("issueState") != null){
			if(!"-1".equals(data.get("issueState"))){
				buffer.append(" AND B.ISSUE_STATE=?");
				params.add(data.get("issueState"));
			}else{
				buffer.append(" AND B.ISSUE_STATE IN (0,1)");
			}
		}
		buffer.append(" ) A,META_DATA_SOURCE B");
		buffer.append(" WHERE A.DATA_SOURCE_ID=B.DATA_SOURCE_ID");
		buffer.append(" ) A,META_TABLE_GROUP B");
		buffer.append(" WHERE A.TABLE_GROUP_ID = B.TABLE_GROUP_ID");
		//业务类型
		if(data.get("tableGroupId") != null && !"".equals(""+data.get("tableGroupId"))){
			buffer.append(" AND B.TABLE_GROUP_ID=?");
			params.add(data.get("tableGroupId"));
		}
		buffer.append(" ORDER BY A.ISSUE_STATE DESC,A.TABLE_ID DESC");
		String sql = buffer.toString();
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
		List<Map<String,Object>> list = getDataAccess().queryForList(sql,params.toArray());
		
		return list;
	}
	/**
	 * @Title: getModelMes 
	 * @Description: 获取数据模型信息
	 * @param tableId
	 * @param issueId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getModelMes(String tableId,String issueId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT A.DATA_SOURCE_NAME,A.TABLE_NAME,A.TABLE_NAME_CN,B.TABLE_ALIAS,B.AUDIT_TYPE,B.MAX_DATE,B.TABLE_KEYWORD,");
		buffer.append(" B.ISSUE_STATE,B.ISSUE_NOTE,A.TABLE_OWNER,B.DATA_AUDIT_TYPE FROM (");
		buffer.append(" SELECT A.DATA_SOURCE_NAME,B.TABLE_NAME,B.TABLE_NAME_CN,B.TABLE_ID,B.TABLE_OWNER FROM");
		buffer.append(" META_DATA_SOURCE A,META_TABLES B WHERE A.DATA_SOURCE_ID=B.DATA_SOURCE_ID");
		buffer.append(" AND B.TABLE_ID=?");
		buffer.append(" ) A,(SELECT B.TABLE_ID,B.TABLE_ALIAS,B.AUDIT_TYPE,C.AUDIT_TYPE DATA_AUDIT_TYPE,C.MAX_DATE,B.TABLE_KEYWORD,B.ISSUE_STATE,B.ISSUE_NOTE FROM");
		buffer.append(" META_RPT_MODEL_ISSUE_CONFIG B,META_RPT_DATA_AUDIT_CFG C");
		buffer.append(" WHERE B.ISSUE_ID=C.ISSUE_ID");
		buffer.append(" AND B.ISSUE_ID=?");
		buffer.append(" ) B WHERE A.TABLE_ID=B.TABLE_ID");
		Map<String,Object> rs = getDataAccess().queryForMap(buffer.toString(),new Object[]{tableId,issueId});
        //加入码表信息
        if(rs != null && rs.size() > 0){
        	if("22".equals(""+rs.get("DATA_AUDIT_TYPE")))
        		rs.put("MAX_DATE","前一月");
        	else
        		rs.put("MAX_DATE", CodeManager.getName(ReportConstant.REPORT_PRT_AGREED,MapUtils.getString(rs,"MAX_DATE")));
        	rs.put("DATA_AUDIT_TYPE", CodeManager.getName(ReportConstant.REPORT_DATA_CYCLE,MapUtils.getString(rs,"DATA_AUDIT_TYPE")));
        	
        }
		return rs;
	}
	/**
	 * @Title: getColMes 
	 * @Description: 根据模型ID获取字段信息、维度信息
	 * @param issueId
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getColMes(String issueId){
		List<Map<String,Object>> rsList = new ArrayList<Map<String,Object>>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.*,B.COL_TYPE_NAME FROM (");
		buffer.append(" SELECT A.COL_ID,B.COL_NAME,A.COL_ALIAS,A.DIM_LEVELS,A.DIM_CODES,A.COL_TYPE_ID,A.COL_BUS_TYPE");
		buffer.append(" FROM META_RPT_MODEL_ISSUE_COLS A,");
		buffer.append(" META_TABLE_COLS B");
		buffer.append(" WHERE A.COL_ID=B.COL_ID AND A.ISSUE_ID=? AND B.COL_STATE=1 ORDER BY A.COLUMN_ID");
		buffer.append(" ) A,META_RPT_MODEL_COL_BUS_TYPE B WHERE A.COL_TYPE_ID=B.COL_TYPE_ID(+)");
		List<Map<String,Object>> list = getDataAccess().queryForList(buffer.toString(),new Object[]{issueId});
		if(list != null && list.size() > 0){
			for(Map<String,Object> m : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("COL_NAME", m.get("COL_NAME"));
				map.put("COL_ALIAS", m.get("COL_ALIAS"));
				map.put("COL_TYPE_NAME", m.get("COL_TYPE_NAME"));
				map.put("COL_BUS_TYPE", m.get("COL_BUS_TYPE"));
				if("0".equals(String.valueOf(m.get("COL_BUS_TYPE")))){
					buffer = null;
					buffer = new StringBuffer();
					buffer.append(" SELECT A.DIM_TABLE_ID,A.TABLE_NAME,A.TABLE_DIM_PREFIX,B.DIM_TYPE_ID FROM META_DIM_TABLES A,META_TABLE_COLS B");
					buffer.append(" WHERE A.DIM_TABLE_ID=B.DIM_TABLE_ID AND B.COL_ID=?");
					Map<String,Object> om = getDataAccess().queryForMap(buffer.toString(),new Object[]{m.get("COL_ID")});
					
					buffer = null;
					buffer = new StringBuffer();
					buffer.append("SELECT T.DIM_LEVEL_NAME FROM META_DIM_LEVEL T WHERE T.DIM_LEVEL IN ("+m.get("DIM_LEVELS")+")");
					buffer.append(" AND T.DIM_TABLE_ID=? AND T.DIM_TYPE_ID=?");
					buffer.append(" ORDER BY T.DIM_LEVEL");
					List<Map<String,Object>> dimList = getDataAccess().queryForList(buffer.toString(),new Object[]{om.get("DIM_TABLE_ID"),om.get("DIM_TYPE_ID")});
					String dimNames = "";
					if(dimList != null && dimList.size() > 0){
						int count = 1;
						for(Map<String,Object> mo : dimList){
							if(count == dimList.size())
								dimNames += mo.get("DIM_LEVEL_NAME")+"";
							else
								dimNames += mo.get("DIM_LEVEL_NAME")+",";
							count++;
						}
					}
					map.put("DIM_LEVELS", dimNames);
					
					//获取维度值
					if(m.get("DIM_CODES") != null && !"null".equals(String.valueOf(m.get("DIM_CODES")))){
						String value = "";
						String colName = om.get("TABLE_DIM_PREFIX")+"_NAME";
						String whereCol = om.get("TABLE_DIM_PREFIX")+"_ID";
						buffer = null;
						buffer = new StringBuffer();
						buffer.append("SELECT "+colName+" FROM "+om.get("TABLE_NAME")+" WHERE "+whereCol+" IN ("+m.get("DIM_CODES")+")");
						List<Map<String,Object>> valueList = getDataAccess().queryForList(buffer.toString());
						if(valueList != null && valueList.size() > 0){
							int count = 1;
							for(Map<String,Object> vm : valueList){
								if(count == valueList.size())
									value += vm.get(colName)+"";
								else
									value += vm.get(colName)+",";
								count++;
							}
						}
						map.put("DIM_CODES", value);
					}
				}else if("2".equals(""+m.get("COL_BUS_TYPE")) && m.get("DIM_CODES") != null && !"null".equals(String.valueOf(m.get("DIM_CODES")))){
					map.put("DIM_CODES", m.get("DIM_CODES"));
				}
				rsList.add(map);
			}
		}
		return rsList.size()==0?null:rsList;
	}
	
	/**
	 * @Title: getReportMes 
	 * @Description: 根据模型ID，获取引用报表信息
	 * @param issueId
	 * @param page
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>>getReportMes(String issueId,Page page){
		List<Map<String,Object>> rsList = new ArrayList<Map<String,Object>>();
		int operateType = ReportConstant.PEPORT_OPERATE_TYPE_ADD;
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT B.DEPT_NAME CJ_DEPT_NAME,B.STATION_NAME CJ_STATION_NAME,B.USER_NAMECN CJ_USER_NAMECN,A.* FROM"); 
		buffer.append(" (SELECT A.REPORT_NAME,A.REPORT_STATE,B.* FROM ( ");
		buffer.append(" SELECT T.REPORT_ID,T.REPORT_NAME,T.REPORT_STATE FROM META_RPT_TAB_REPORT_CFG T WHERE T.ISSUE_ID=?");
		buffer.append(" ) A,(SELECT T.USER_DEPT_ID,T.USER_STATION_ID,T.USER_ID,T.REPORT_ID,T.OPERATE_TIME FROM META_RPT_CONFIG_LOG T");
		buffer.append(" WHERE T.OPERATE_TYPE="+operateType+") B");
		buffer.append(" WHERE A.REPORT_ID=B.REPORT_ID) A,");
		buffer.append(" (SELECT A.*,S.STATION_NAME FROM (");
		buffer.append(" SELECT D.DEPT_NAME,U.DEPT_ID,U.STATION_ID,U.USER_ID,U.USER_NAMECN FROM");
		buffer.append(" META_DIM_USER_DEPT D,META_MAG_USER U WHERE D.DEPT_CODE=U.DEPT_ID");
		buffer.append(" ) A,META_DIM_USER_STATION S WHERE A.STATION_ID=S.STATION_CODE");
		buffer.append(" ) B WHERE A.USER_ID=B.USER_ID AND A.USER_DEPT_ID=B.DEPT_ID AND A.USER_STATION_ID=B.STATION_ID");
		buffer.append(" ORDER BY A.REPORT_ID DESC");
		String sql = buffer.toString();
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
		List<Map<String,Object>> list = getDataAccess().queryForList(sql,new Object[]{issueId});
		if(list != null && list.size() > 0){
			for(Map<String,Object> m : list){
				Map<String,Object> map = new HashMap<String,Object>();
				//日期转换
				if(m.get("OPERATE_TIME") != null && !"null".equals(""+m.get("OPERATE_TIME")) && !"".equals(""+m.get("OPERATE_TIME")))
					m.put("OPERATE_TIME", DateUtil.getParamDay(m.get("OPERATE_TIME")+"", "yyyy-MM-dd HH:mm:ss"));
				//封装创建人信息
				String createPeoMes = ""+m.get("CJ_DEPT_NAME")+m.get("CJ_STATION_NAME")+m.get("CJ_USER_NAMECN");
				map.put("CREATEPEOMES", createPeoMes);
				//加报表名称
				map.put("REPORT_NAME", m.get("REPORT_NAME"));
				//加操作时间
				map.put("OPERATE_TIME", m.get("OPERATE_TIME"));
				map.put("REPORT_STATE", m.get("REPORT_STATE"));
//				//获取订阅人信息：根据报表ID
//				buffer = null;
//				buffer = new StringBuffer();
//				buffer.append(" SELECT B.DEPT_NAME DY_DEPT_NAME,B.STATION_NAME DY_STATION_NAME,B.USER_NAMECN DY_USER_NAMECN FROM(");
//				buffer.append(" SELECT T.USER_ID,T.USE_DEPT_ID,T.USE_STATION_ID,T.REPORT_ID FROM META_RPT_USE_LOG T WHERE");
//				buffer.append(" T.OPERATE_TYPE=21 AND T.REPORT_ID=?) A,");
//				buffer.append(" (SELECT A.*,S.STATION_NAME FROM (");
//				buffer.append(" SELECT D.DEPT_NAME,U.DEPT_ID,U.STATION_ID,U.USER_ID,U.USER_NAMECN FROM");
//				buffer.append(" META_MAG_USER_DEPT D,META_MAG_USER U WHERE D.DEPT_ID=U.DEPT_ID");
//				buffer.append(" ) A,META_MAG_USER_STATION S WHERE A.STATION_ID=S.STATION_ID");
//				buffer.append(" ) B WHERE A.USER_ID=B.USER_ID AND A.USE_DEPT_ID=B.DEPT_ID AND A.USE_STATION_ID=B.STATION_ID");
//				List<Map<String,Object>> valueList = getDataAccess().queryForList(buffer.toString(),new Object[]{m.get("REPORT_ID")});
//				String dy_value = "";
//				if(valueList != null && valueList.size() > 0){
//					int count = 1;
//					for(Map<String,Object> vm : valueList){
//						String value = ""+vm.get("DY_DEPT_NAME")+vm.get("DY_STATION_NAME")+vm.get("DY_USER_NAMECN");
//						if(count == valueList.size())
//							value += "";
//						else
//							value += ",";
//						dy_value += value;
//						count++;
//					}
//				}
//				map.put("DYPEOMES", dy_value);
				rsList.add(map);
			}
		}
		return rsList.size()==0?null:rsList;
	}
	/**
	 * 
	 * @Title: updateModel 
	 * @Description: 更新标识
	 * @param issueId
	 * @return boolean   
	 * @throws
	 */
	public boolean updateModel(String issueId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_RPT_MODEL_ISSUE_CONFIG SET ISSUE_STATE=0 WHERE ISSUE_ID=?");
		return getDataAccess().execNoQuerySql(buffer.toString(),new Object[]{issueId});
	}
	/**
	 * @Title: insertModelLog 
	 * @Description: 新增模型日志信息
	 * @param issueId
	 * @param userMap
	 * @return boolean   
	 * @throws
	 */
	public boolean insertModelLog(String issueId,Map<String,Object> userMap){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" INSERT INTO META_RPT_MODEL_ISSUE_LOG(LOG_ID,USER_ID,ISSUE_ID,USER_ZONE_ID,");
		buffer.append(" USER_DEPT_ID,USER_STATION_ID,ISSUE_OPERATE,OPERATE_OPINION,ISSUE_TIME)");
		buffer.append(" VALUES(?,?,?,?,?, ?,?,?,sysdate)");
		Object[] obj = new Object[]{
				queryForNextVal("SEQ_RPT_MODEL_ISSUE_LOG_ID"),
				userMap.get("userId"),
				issueId,
				userMap.get("zoneId"),
				userMap.get("deptId"),
				userMap.get("stationId"),
				13,
				null
				};
		return getDataAccess().execNoQuerySql(buffer.toString(), obj);
	}
}
