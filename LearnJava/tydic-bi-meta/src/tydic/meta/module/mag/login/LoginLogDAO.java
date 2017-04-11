package tydic.meta.module.mag.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.web.session.User;
import tydic.meta.common.yhd.utils.Pager;


/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 登录日志记录DAO <br>
 * @date 2011-10-03
 */
public class LoginLogDAO extends MetaBaseDAO {

    /**
     * 登录时记录登录日志
     *
     * @param loginData
     * @return
     */
    public int insertLoginLog(User user){
        String sql = " INSERT INTO META_MAG_LOGIN_LOG ( "
                     + " LOG_ID, USER_ID,GROUP_ID, LOGIN_IP, LOGIN_MAC, "
                     + " LOGIN_DATE) VALUES ( "
                     + " ?,?,?,?,? ,TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'))";
        int pk = (int)queryForNextVal("SEQ_LOGIN_LOG_ID");
        Map<String,Object> userMap = user.getUserMap();
        Object[] params = {
            pk,MapUtils.getIntValue(userMap, "userId"),MapUtils.getIntValue(userMap, "groupId"),
            MapUtils.getString(userMap, "loginIp"),MapUtils.getString(userMap, "loginMac"),DateUtil.format(new Date(user.getLogInTime()), "yyyy-MM-dd HH:mm:ss")
        };
        getDataAccess().execUpdate(sql, params);
        return pk;
    }

    /**
     * session失效或者注销时记录日志
     *
     * @throws Exception
     */
    public void updateLoginOutTime(int logId){
        String sql = "UPDATE META_MAG_LOGIN_LOG SET LOGOFF_DATE=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') WHERE LOG_ID=?";
        getDataAccess().execUpdate(sql,DateUtil.format(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss"),logId);
    }

    /**
     * @author 熊小平
     * @description 访问排名
     * @param queryData
     *            查询时的过滤参数
     *@param  hideStations 需要隐藏不需要统计的岗位集
     * @param page
     *            分页参数
     * @return
     */
    public List<Map<String, Object>> queryLoginLog(Map<String, Object> queryData) {
    	//用户登录情况
        StringBuffer sql = new StringBuffer();
			sql.append("select u.user_namecn, ");
			//sql.append(" z.zone_name as show_zone_name,");
			 sql.append("CASE WHEN  Z.ZONE_PAR_ID > 10 THEN " +
					  	"(SELECT dz.zone_name FROM meta_dim_zone dz WHERE dz.zone_id = DECODE(z.ZONE_PAR_ID, '2', '200', z.ZONE_PAR_ID)) || '-' "+							  
					  	" ELSE '' END || z.zone_name AS SHOW_ZONE_NAME ,");
			sql.append(" u.dept_name,");
			sql.append(" u.station_name,");
			sql.append(" mr.role_name,");
			sql.append(" count(*) cnt");
			sql.append(" from meta_mag_login_log l");
			sql.append(" left join meta_mag_user u");
			sql.append(" on l.user_id = u.user_id");
			//sql.append(" left join meta_mag_user_dept d");
			//sql.append(" on u.dept_id = d.dept_id");
			//sql.append(" left join meta_mag_user_station s");
			//sql.append(" on u.station_id = s.station_id");
			sql.append(" left join meta_dim_zone z");
			sql.append(" on u.zone_id = z.zone_id");
			
			sql.append(" LEFT JOIN META_MAG_USER_ROLE UR");
			sql.append(" ON U.USER_ID = UR.USER_ID");
			sql.append(" LEFT JOIN META_MAG_ROLE MR");
			sql.append(" ON UR.ROLE_ID = MR.ROLE_ID");
			
			sql.append(" where l.login_date >= to_date('"+MapUtils.getString(queryData, "startTime", "")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
			sql.append(" and   l.login_date <  to_date('"+MapUtils.getString(queryData, "endTime",   "")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");   			
			String zoneCode = MapUtils.getString(queryData, "zoneCode",   "");
			sql.append(" and  z.zone_id in (select ZONE_id from META_DIM_ZONE start with ZONE_CODE = '"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)");
			//and u.admin_flag = 0
			sql.append(" and u.vip_flag = 0");
			sql.append(" AND U.VIP_FLAG = 0 AND U.STATE = 1 AND (UR.ROLE_ID <> '213762' AND UR.ROLE_ID <> '213763')");
			
			sql.append(" group by u.user_namecn,z.zone_name,u.dept_name,u.station_name, Z.ZONE_PAR_ID, mr.role_name");
			sql.append(" order by count(*) desc");
  
        return getDataAccess().queryForList(sql.toString());	

    }   

    /**
     * @author 熊小平
     * @description 查询某一用户详细访问信息
     * @param queryData
     *            查询时的过滤参数
     * @param page
     *            分页参数，为空表示不分页
     * @return
     */
    public List<Map<String, Object>> queryLoginLogByID(Map<String, Object> paramMap) {
    	//详情：
    	StringBuffer sql = new StringBuffer();
			sql.append("select u.user_namecn ,z.zone_name as show_zone_name,u.dept_name,u.station_name,l.login_ip," +
					"to_char(l.login_date,'yyyy-mm-dd hh24:mi:ss')  login_date," +
					"nvl(to_char(l.logoff_date,'yyyy-mm-dd hh24:mi:ss'),'-') logoff_date");
			sql.append(" from meta_mag_login_log l");
			sql.append(" left join meta_mag_user u");
			sql.append(" on l.user_id = u.user_id");
			//sql.append(" left join meta_mag_user_dept d");
			//sql.append(" on u.dept_id = d.dept_id");
			//sql.append(" left join meta_mag_user_station s");
			//sql.append(" on u.station_id = s.station_id");
			
			sql.append(" left join meta_dim_zone z");
			sql.append(" on u.zone_id = z.zone_id");
			
			sql.append(" where l.login_date >= to_date('"+MapUtils.getString(paramMap, "startTime", "")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
			sql.append(" and l.login_date <    to_date('"+MapUtils.getString(paramMap, "endTime",   "")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
			//and u.admin_flag = 0
			sql.append(" and u.vip_flag = 0");
			String userName = MapUtils.getString(paramMap, "userName",   "");
		if(!userName.equals(""))
		{		
				sql.append(" and u.user_namecn='"+userName+"'");
		}
		else {
			String zoneCode = MapUtils.getString(paramMap, "zoneCode",   "");
			if(zoneCode != "" && !zoneCode.equals("0000"))
			{
				sql.append(" AND z.zone_id in (select ZONE_id from META_DIM_ZONE start with ZONE_CODE = '"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)");
			  //sql.append(" and (z.zone_code='"+zoneCode+"' OR z.ZONE_PAR_CODE='"+zoneCode+"') ");
			}
		}
			sql.append(" order by l.login_date desc");
  return getDataAccess().queryForList(sql.toString());
    }
    /**
     * 获取用户访问报表
     * 
     * @param queryData 查询条件
     * @param hideStations 隐藏岗位
     * @return 查询结果
     */
    public List<Map<String ,Object>> queryLoginReport(Map<?, ?> queryData,String hideStations){
    	boolean adminFlag = false;
        if("true".equals(Convert.toString(queryData.get("adminFlag")))){
            adminFlag = true;
        }
        // 参数处理
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("SELECT A.ZONE_ID,B.ZONE_NAME,SUM(A.LOGIN_COUNT) SUM FROM ( " +
                "SELECT Z.ZONE_ID, USER_VISIT.LOGIN_COUNT LOGIN_COUNT " +
                "FROM META_DIM_ZONE Z,(SELECT COUNT(*) LOGIN_COUNT, " +
                "Z.ZONE_ID ZONE_ID,Z.ZONE_PAR_ID FROM META_MAG_LOGIN_LOG L " +
                "LEFT JOIN META_MAG_USER U ON L.USER_ID=U.USER_ID " +
                "LEFT JOIN META_DIM_ZONE Z ON U.ZONE_ID=Z.ZONE_ID WHERE 1=1 ");
        if (queryData != null) {
            if (queryData.get("startDate") != null) {
                try {
                    Date startDate = new Date();
                    startDate.setTime(Long.parseLong(queryData.get("startDate").toString()));
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    sql.append("AND L.LOGIN_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
                } catch (NumberFormatException e) {
                }
            }
            if (queryData.get("endDate") != null) {
                try {
                    Date endDate = new Date();
                    endDate.setTime(Long.parseLong(queryData.get("endDate").toString()));
                    endDate.setDate(endDate.getDate()+1);
                    endDate.setHours(0);
                    endDate.setMinutes(0);
                    endDate.setSeconds(0);
                    sql.append("AND L.LOGIN_DATE < TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                } catch (NumberFormatException e) {
                }
            }
        }
        if(hideStations!=null&&!hideStations.equals("")){
            sql.append(" AND U.STATION_ID NOT IN (");
            String[] stations=hideStations.contains(",")?hideStations.split(","):new String[]{hideStations};
            for(int i=0;i<stations.length;i++){
                sql.append(i==stations.length-1?stations[i]:(stations[i]+","));
            }
            sql.append(") ");
        }
        if(queryData != null && !Convert.toString(queryData.get("groupId")).equals("")&&
            !Convert.toString(queryData.get("groupId")).equalsIgnoreCase("null")){
             sql.append(" AND L.GROUP_ID=? ");
             params.add(Integer.parseInt(queryData.get("groupId").toString()));
         }
         if(!adminFlag){
             sql.append(" AND U.ADMIN_FLAG = 0 ");
         }
         sql.append("GROUP BY Z.ZONE_ID,Z.ZONE_PAR_ID) USER_VISIT " +
         		"WHERE Z.ZONE_ID = USER_VISIT.ZONE_ID AND " +
         		"(USER_VISIT.ZONE_PAR_ID=0 OR USER_VISIT.ZONE_PAR_ID=1) " +
         		"UNION ALL SELECT USER_VISIT.ZONE_PAR_ID ZONE_ID , " +
         		"USER_VISIT.LOGIN_COUNT LOGIN_COUNT FROM META_DIM_ZONE Z, " +
         		"(SELECT COUNT(*) LOGIN_COUNT, Z.ZONE_ID ZONE_ID,Z.ZONE_PAR_ID " +
         		"FROM META_MAG_LOGIN_LOG L LEFT JOIN META_MAG_USER U " +
         		"ON L.USER_ID=U.USER_ID LEFT JOIN META_DIM_ZONE Z " +
         		"ON U.ZONE_ID=Z.ZONE_ID WHERE 1=1");
         if (queryData != null) {
             if (queryData.get("startDate") != null) {
                 try {
                     Date startDate = new Date();
                     startDate.setTime(Long.parseLong(queryData.get("startDate").toString()));
                     startDate.setHours(0);
                     startDate.setMinutes(0);
                     startDate.setSeconds(0);
                     sql.append("AND L.Login_Date >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                     params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
                 } catch (NumberFormatException e) {
                 }
             }
             if (queryData.get("endDate") != null) {
                 try {
                     Date endDate = new Date();
                     endDate.setTime(Long.parseLong(queryData.get("endDate").toString()));
                     endDate.setDate(endDate.getDate()+1);
                     endDate.setHours(0);
                     endDate.setMinutes(0);
                     endDate.setSeconds(0);
                     sql.append("AND L.Login_Date < TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                     params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                 } catch (NumberFormatException e) {
                 }
             }
         }
         if(hideStations!=null&&!hideStations.equals("")){
             sql.append(" AND U.STATION_ID NOT IN (");
             String[] stations=hideStations.contains(",")?hideStations.split(","):new String[]{hideStations};
             for(int i=0;i<stations.length;i++){
                 sql.append(i==stations.length-1?stations[i]:(stations[i]+","));
             }
             sql.append(") ");
         }
         if(queryData != null && !Convert.toString(queryData.get("groupId")).equals("")&&
             !Convert.toString(queryData.get("groupId")).equalsIgnoreCase("null")){
              sql.append(" AND L.GROUP_ID=? ");
              params.add(Integer.parseInt(queryData.get("groupId").toString()));
          }
          if(!adminFlag){
              sql.append(" AND U.ADMIN_FLAG = 0 ");
          }
        sql.append("AND Z.ZONE_PAR_ID<>0 AND Z.ZONE_PAR_ID<>1 " +
        		"GROUP BY Z.ZONE_ID,Z.ZONE_PAR_ID) USER_VISIT " +
        		"WHERE Z.ZONE_ID = USER_VISIT.ZONE_ID ) A " +
        		"LEFT JOIN META_DIM_ZONE B ON A.ZONE_ID=B.ZONE_ID " +
        		"GROUP BY A.ZONE_ID,B.ZONE_NAME ORDER BY A.ZONE_ID");
        return getDataAccess().queryForList(sql.toString(),params.toArray());
    }
    /**
     * 获取指定菜单ID 的列表
     * 
     * @param str 菜单ID字符串 格式为：“10，11,12,13”
     * @return 查询结果
     */
    public List<Map<String,Object>> getMenuName(String str){
    	StringBuffer sql = new StringBuffer("SELECT MENU_NAME FROM META_MAG_MENU T WHERE T.MENU_ID IN ("+str+") ");
    	sql.append("ORDER BY T.MENU_ID ");
    	return getDataAccess().queryForList(sql.toString());
    }
    /**
     * 条件查询菜单访问统计
     * 
     * @param queryData 查询条件
     * @param hideStations 隐藏岗位
     * @param --menuId 菜单ID
     * @return 查询结果
     */
    public List<Map<String,Object>> queryMenuReport(Map<?, ?> queryData,String hideStations,
    		List<Map<String, Object>> listMenuId){
    	boolean adminFlag = false;
        if("true".equals(Convert.toString(queryData.get("adminFlag")))){
            adminFlag = true;
        }
        
        // 参数处理
        List<Object> params = new ArrayList<Object>();
    	StringBuffer Sql = new StringBuffer("SELECT A.ZONE_ID, SUM(A.MENUVISITCOUNT) MENUVISITCOUNT FROM ( " +
    			"SELECT Z.ZONE_ID, MENU_VISIT.MENU_VISIT_COUNT MENUVISITCOUNT " +
    			"FROM META_DIM_ZONE Z,(SELECT COUNT(*) MENU_VISIT_COUNT, Z.ZONE_ID ZONE_ID,Z.ZONE_PAR_ID " +
    			"FROM META_MAG_MENU_VISIT_LOG L LEFT JOIN META_MAG_USER U ON L.USER_ID=U.USER_ID " +
    			"LEFT JOIN META_DIM_ZONE Z ON U.ZONE_ID=Z.ZONE_ID WHERE 1=1 ");
    	if(listMenuId!=null){
    		Sql.append("AND L.MENU_ID IN ( ");
    		for(int i=0; i<listMenuId.size();i++){
    			Sql.append(listMenuId.get(i).get("MENU_ID"));
    			if(i!= listMenuId.size()-1){
    				Sql.append(",");
    			}
    		}
    		Sql.append(")");
    	}
    	if (queryData != null) {
            if (queryData.get("startDate") != null) {
                try {
                    Date startDate = new Date();
                    startDate.setTime(Long.parseLong(queryData.get("startDate").toString()));
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    Sql.append("AND L.VISIT_TIME >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
                } catch (NumberFormatException e) {
                }
            }
            if (queryData.get("endDate") != null) {
                try {
                    Date endDate = new Date();
                    endDate.setTime(Long.parseLong(queryData.get("endDate").toString()));
                    endDate.setDate(endDate.getDate()+1);
                    endDate.setHours(0);
                    endDate.setMinutes(0);
                    endDate.setSeconds(0);
                    Sql.append("AND L.VISIT_TIME < TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                } catch (NumberFormatException e) {
                }
            }
        }
    	if(hideStations!=null&&!hideStations.equals("")){
    		Sql.append(" AND U.STATION_ID NOT IN (");
            String[] stations=hideStations.contains(",")?hideStations.split(","):new String[]{hideStations};
            for(int i=0;i<stations.length;i++){
            	Sql.append(i==stations.length-1?stations[i]:(stations[i]+","));
            }
            Sql.append(") ");
        }
         if(!adminFlag){
        	 Sql.append(" AND U.ADMIN_FLAG = 0 ");
         }
        Sql.append("GROUP BY Z.ZONE_ID,Z.ZONE_PAR_ID) MENU_VISIT " +
        		"WHERE Z.ZONE_ID = MENU_VISIT.ZONE_ID AND (MENU_VISIT.ZONE_PAR_ID=0 OR MENU_VISIT.ZONE_PAR_ID=1) " +
        		"UNION ALL SELECT MENU_VISIT.ZONE_PAR_ID ZONE_ID , MENU_VISIT.MENU_VISIT_COUNT MENUVISITCOUNT " +
        		"FROM META_DIM_ZONE Z,(SELECT COUNT(*) MENU_VISIT_COUNT, Z.ZONE_ID ZONE_ID,Z.ZONE_PAR_ID " +
        		"FROM META_MAG_MENU_VISIT_LOG L LEFT JOIN META_MAG_USER U " +
        		"ON L.USER_ID=U.USER_ID LEFT JOIN META_DIM_ZONE Z ON U.ZONE_ID=Z.ZONE_ID WHERE 1=1 "); 
        if(listMenuId!=null){
    		Sql.append("AND L.MENU_ID IN ( ");
    		for(int i=0; i<listMenuId.size();i++){
    			Sql.append(listMenuId.get(i).get("MENU_ID"));
    			if(i!= listMenuId.size()-1){
    				Sql.append(",");
    			}
    		}
    		Sql.append(")");
    	}
    	if (queryData != null) {

            if (queryData.get("startDate") != null) {
                try {
                    Date startDate = new Date();
                    startDate.setTime(Long.parseLong(queryData.get("startDate").toString()));
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    Sql.append("AND L.VISIT_TIME >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
                } catch (NumberFormatException e) {
                }
            }
            if (queryData.get("endDate") != null) {
                try {
                    Date endDate = new Date();
                    endDate.setTime(Long.parseLong(queryData.get("endDate").toString()));
                    endDate.setDate(endDate.getDate()+1);
                    endDate.setHours(0);
                    endDate.setMinutes(0);
                    endDate.setSeconds(0);
                    Sql.append("AND L.VISIT_TIME < TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                } catch (NumberFormatException e) {
                }
            }
        }
    	if(hideStations!=null&&!hideStations.equals("")){
    		Sql.append(" AND U.STATION_ID NOT IN (");
            String[] stations=hideStations.contains(",")?hideStations.split(","):new String[]{hideStations};
            for(int i=0;i<stations.length;i++){
            	Sql.append(i==stations.length-1?stations[i]:(stations[i]+","));
            }
            Sql.append(") ");
        }
	     if(!adminFlag){
	    	 Sql.append(" AND U.ADMIN_FLAG = 0 ");
	     }  
        Sql.append("AND Z.ZONE_PAR_ID<>0 AND Z.ZONE_PAR_ID<>1 " +
        		"GROUP BY Z.ZONE_ID,Z.ZONE_PAR_ID) MENU_VISIT " +
        		"WHERE Z.ZONE_ID = MENU_VISIT.ZONE_ID) A GROUP BY A.ZONE_ID");
    	return getDataAccess().queryForList(Sql.toString(),params.toArray());
    }
    public List<Map<String, Object>> queryMenuList(String menuId){
    	StringBuffer sql = new StringBuffer("SELECT  T.MENU_ID " +
    			"FROM META_MAG_MENU T START WITH T.PARENT_ID = ? " +
    			"CONNECT BY PRIOR T.MENU_ID = T.PARENT_ID ");
    	// 参数处理
        List<Object> params = new ArrayList<Object>();
        params.add(menuId);
    	return getDataAccess().queryForList(sql.toString(), params.toArray());
    }
    /**
     *  add  @author yanhaidong  start
     */
    
   /* 
    * 获取菜单访问排行
    * 
    * add  @author  chenWei Guang
    * 
    * */
    
    public List<Map<String, Object>> getMenuVisitCount(Map<String, Object> paramMap) {
	    StringBuffer sql = new StringBuffer(); 
		  sql.append(" SELECT M.MENU_NAME, M.MENU_PATH,COUNT(*) CNT, M.MENU_ID");
		  sql.append(" FROM META_MAG_MENU_VISIT_LOG L");
		  sql.append(" LEFT JOIN META_MAG_USER U ON L.USER_ID = U.USER_ID");
		  sql.append(" LEFT JOIN META_DIM_ZONE Z ON U.ZONE_ID = Z.ZONE_ID");
		  sql.append(" LEFT JOIN (SELECT MENU_ID, MENU_NAME,");
		  sql.append(" SUBSTR(SYS_CONNECT_BY_PATH(MENU_NAME, '\\'), 2) MENU_PATH");
		  sql.append(" FROM META_MAG_MENU T");
		  sql.append("  WHERE CONNECT_BY_ISLEAF = 1");
		  
		  sql.append(" START WITH PARENT_ID = 0 CONNECT BY PRIOR MENU_ID = PARENT_ID) M");
		  sql.append(" ON L.MENU_ID = M.MENU_ID");
		  sql.append(" where l.visit_time >= to_date('"+MapUtils.getString(paramMap, "startTime", "")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
		  sql.append(" and l.visit_time   <  to_date('"+MapUtils.getString(paramMap, "endTime",   "")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
		
		String zoneCode = MapUtils.getString(paramMap, "zoneCode", "");
		if(zoneCode != "" && !zoneCode.equals("0000"))
		{
			sql.append(" and  z.zone_id in (select ZONE_id from META_DIM_ZONE start with ZONE_CODE = '"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)");
		  //sql.append(" and (z.zone_code='"+zoneCode+"' OR z.ZONE_PAR_CODE='"+zoneCode+"') ");
		}  
		  sql.append(" AND U.VIP_FLAG = 0 AND U.ADMIN_FLAG = 0 AND U.STATE = 1 AND M.MENU_NAME IS NOT NULL");
		  sql.append(" GROUP BY M.MENU_NAME, M.MENU_PATH, M.MENU_ID");
		  sql.append(" ORDER BY COUNT(*) DESC");
		return getDataAccess().queryForList(sql.toString());
}
    
    public List<Map<String, Object>>getUsersList(Map<String,Object>queryData,Pager page){
    	String staffName=queryData.get("staffName").toString()==null?"":queryData.get("staffName").toString();
    	String staffCode=queryData.get("staffCode").toString()==null?"":queryData.get("staffCode").toString();
    	String zoneCode=queryData.get("zoneCode").toString()==null?"0000":queryData.get("zoneCode").toString();
    	String treeCode=getTreeCodeById(zoneCode);
    	int startRow=page.getStartRow();
		int endRow=page.getEndRow();
    	StringBuffer sql=new StringBuffer();
    	sql.append("select CITY_NAME,NVL(AREA_NAME,'-') AREA_NAME,STAFF_NAME,STAFF_CODE,NVL(DEPT_NAME,'-')DEPT_NAME,SEQ from NOT_STATISFY_CHARGE_MANAGER a");
    	sql.append(" where state=1");
    	sql.append(" and a.ROWID IN (SELECT OUTER.rid FROM (SELECT ROWNUM RN, INNER_.rid FROM (select ROWID rid from NOT_STATISFY_CHARGE_MANAGER");
    	sql.append(" where state=1");
    	if(!"".equals(staffName)&&staffName!=""){
    		sql.append(" and STAFF_NAME like '%"+staffName+"%'");
    	}if(!"".equals(staffCode)&&staffCode!=""){
    		sql.append(" and STAFF_CODE like '%"+staffCode+"%'");
    	}if(!"0000".equals(zoneCode)&&zoneCode!="0000"){
    		sql.append(" and (CITY_ID like '"+treeCode+"%'");
    		sql.append(" or AREA_ID like '"+treeCode+"%'");
    		sql.append(" or DEPT_ID like '"+treeCode+"%')");
    	}
    	sql.append(" order by seq) INNER_) OUTER WHERE OUTER.RN >= "+startRow+" AND OUTER.RN <= "+endRow+") order by seq");
    	return getDataAccess().queryForList(sql.toString());
    } 
    
    public List<Map<String, Object>>getEwamUsersList(Map<String,Object>queryData,Pager page){
    	String staffName=queryData.get("staffName").toString()==null?"":queryData.get("staffName").toString();
    	String staffCode=queryData.get("staffCode").toString()==null?"":queryData.get("staffCode").toString();
    	String zoneCode=queryData.get("zoneCode").toString()==null?"0000":queryData.get("zoneCode").toString();
    	String treeCode=getTreeCodeById(zoneCode);
    	int startRow=page.getStartRow();
		int endRow=page.getEndRow();
    	StringBuffer sql=new StringBuffer();
    	sql.append("select CITY_NAME,NVL(AREA_NAME,'-') AREA_NAME,STAFF_NAME,STAFF_CODE,NVL(DEPT_NAME,'-')DEPT_NAME,SEQ from EWAM_CHARGE_MANAGER a");
    	sql.append(" where state=1");
    	sql.append(" and a.ROWID IN (SELECT OUTER.rid FROM (SELECT ROWNUM RN, INNER_.rid FROM (select ROWID rid from EWAM_CHARGE_MANAGER");
    	sql.append(" where state=1");
    	if(!"".equals(staffName)&&staffName!=""){
    		sql.append(" and STAFF_NAME like '%"+staffName+"%'");
    	}if(!"".equals(staffCode)&&staffCode!=""){
    		sql.append(" and STAFF_CODE like '%"+staffCode+"%'");
    	}if(!"0000".equals(zoneCode)&&zoneCode!="0000"){
    		sql.append(" and (CITY_ID like '"+treeCode+"%'");
    		sql.append(" or AREA_ID like '"+treeCode+"%'");
    		sql.append(" or DEPT_ID like '"+treeCode+"%')");
    	}
    	sql.append(" order by seq) INNER_) OUTER WHERE OUTER.RN >= "+startRow+" AND OUTER.RN <= "+endRow+") order by seq");
    	return getDataAccess().queryForList(sql.toString());
    } 
    
    public String getTreeCodeById(String zoneCode){
    	String sql="select treecode from meta_dim_zone_branch where zone_code='"+zoneCode+"'";
    	return getDataAccess().queryForString(sql);
    }
    public String getUsersCount(Map<String,Object>queryData){
    	String staffName=queryData.get("staffName").toString()==null?"":queryData.get("staffName").toString();
    	String staffCode=queryData.get("staffCode").toString()==null?"":queryData.get("staffCode").toString();
    	String zoneCode=queryData.get("zoneCode").toString()==null?"0000":queryData.get("zoneCode").toString();
    	String treeCode=getTreeCodeById(zoneCode);
    	StringBuffer sql=new StringBuffer();
    	sql.append("select count(1) from NOT_STATISFY_CHARGE_MANAGER");
    	sql.append(" where state=1");
    	if(!"".equals(staffName)&&staffName!=""){
    		sql.append(" and STAFF_NAME like '%"+staffName+"%'");
    	}if(!"".equals(staffCode)&&staffCode!=""){
    		sql.append(" and STAFF_CODE like '%"+staffCode+"%'");
    	}if(!"0000".equals(zoneCode)&&zoneCode!="0000"){
    		sql.append(" and (CITY_ID like '"+treeCode+"%'");
    		sql.append(" or AREA_ID like '"+treeCode+"%'");
    		sql.append(" or DEPT_ID like '"+treeCode+"%')");
    	}
    	return getDataAccess().queryForString(sql.toString());
    }
    public String getEwamUsersCount(Map<String,Object>queryData){
    	String staffName=queryData.get("staffName").toString()==null?"":queryData.get("staffName").toString();
    	String staffCode=queryData.get("staffCode").toString()==null?"":queryData.get("staffCode").toString();
    	String zoneCode=queryData.get("zoneCode").toString()==null?"0000":queryData.get("zoneCode").toString();
    	String treeCode=getTreeCodeById(zoneCode);
    	StringBuffer sql=new StringBuffer();
    	sql.append("select count(1) from EWAM_CHARGE_MANAGER");
    	sql.append(" where state=1");
    	if(!"".equals(staffName)&&staffName!=""){
    		sql.append(" and STAFF_NAME like '%"+staffName+"%'");
    	}if(!"".equals(staffCode)&&staffCode!=""){
    		sql.append(" and STAFF_CODE like '%"+staffCode+"%'");
    	}if(!"0000".equals(zoneCode)&&zoneCode!="0000"){
    		sql.append(" and (CITY_ID like '"+treeCode+"%'");
    		sql.append(" or AREA_ID like '"+treeCode+"%'");
    		sql.append(" or DEPT_ID like '"+treeCode+"%')");
    	}
    	return getDataAccess().queryForString(sql.toString());
    }
    public Map<String, Object> getUserById(String seq){
    	String sql="select B.ZONE_ID ZONE_CODE,B.DIM_LEVEL DIM_LEVEL,NVL(NVL(DEPT_NAME, AREA_NAME), CITY_NAME) ZONE_NAME," +
    			"STAFF_NAME,STAFF_CODE from NOT_STATISFY_CHARGE_MANAGER A,META_DIM_ZONE_BRANCH B  " +
    			"where NVL(NVL(A.DEPT_ID, A.AREA_ID), A.CITY_ID)=B.TREECODE AND seq='"+seq+"'";
    	return getDataAccess().queryForMap(sql.toString());
    }
    public Map<String, Object> getEwamUserById(String seq){
    	String sql="select B.ZONE_ID ZONE_CODE,B.DIM_LEVEL DIM_LEVEL,NVL(NVL(DEPT_NAME, AREA_NAME), CITY_NAME) ZONE_NAME," +
    			"STAFF_NAME,STAFF_CODE from EWAM_CHARGE_MANAGER A,META_DIM_ZONE_BRANCH B  " +
    			"where NVL(NVL(A.DEPT_ID, A.AREA_ID), A.CITY_ID)=B.TREECODE AND seq='"+seq+"'";
    	return getDataAccess().queryForMap(sql.toString());
    }
    public int deleteOaUser(String seq) throws Exception {
    	StringBuffer sql = new StringBuffer("DELETE FROM NOT_STATISFY_CHARGE_MANAGER WHERE SEQ ='"+seq+"'");
    	return getDataAccess().execUpdate(sql.toString());
    }
    public int deleteEwamUser(String seq) throws Exception {
    	StringBuffer sql = new StringBuffer("DELETE FROM EWAM_CHARGE_MANAGER WHERE SEQ ='"+seq+"'");
    	return getDataAccess().execUpdate(sql.toString());
    }
    public List<Map<String, Object>>getUsersByOa(String staffCode){
    	StringBuffer sql=new StringBuffer();
    	sql.append("select * from NOT_STATISFY_CHARGE_MANAGER a");
    	sql.append(" where state=1 and STAFF_CODE='"+staffCode+"'");
    	return getDataAccess().queryForList(sql.toString());
    } 
    public int insertUser(Map<String,Object> data) throws Exception {
    	 String sql = "INSERT INTO NOT_STATISFY_CHARGE_MANAGER(CITY_NAME,CITY_ID,AREA_NAME,AREA_ID,DEPT_NAME," +
    			"DEPT_ID,STAFF_NAME,STAFF_CODE,BUSI_CODE,STATE,BUSI_NAME,SEC_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    	 List<Object> proParams = new ArrayList<Object>(); 
    	 String zoneId=Convert.toString(data.get("zoneId")).trim();
    	 String cityName="";
    	 String cityId="";
    	 String areaName="";
    	 String areaId="";
    	 String deptName="";
    	 String deptId="";
    	 Map<String, Object> zoneMap =new  HashMap<String, Object>();
    	 String zoneDimLevel=getDimLevelById(zoneId);
    	 if(zoneDimLevel=="5"||"5".equals(zoneDimLevel)){//营销中心
    		 zoneMap=getLevel5ById(zoneId);   
    		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
        	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
        	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
        	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
        	 deptName=Convert.toString(zoneMap.get("DEPT_NAME"))==null?"":zoneMap.get("DEPT_NAME").toString();
        	 deptId=Convert.toString(zoneMap.get("DEPT_ID"))==null?"":zoneMap.get("DEPT_ID").toString();
    	 }else if(zoneDimLevel=="4"||"4".equals(zoneDimLevel)){//分公司
    		 zoneMap=getLevel4ById(zoneId);
    		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
        	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
        	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
        	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
        	 deptName="";
        	 deptId="";
    	 }else if(zoneDimLevel=="3"||"3".equals(zoneDimLevel)){//地市
    		 zoneMap=getLevel3ById(zoneId);
    		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
        	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
        	 areaName="";
        	 areaId="";
        	 deptName="";
        	 deptId="";
    	 }    	 
    	 proParams.add(cityName);
    	 proParams.add(cityId);
    	 proParams.add(areaName);
    	 proParams.add(areaId);
    	 proParams.add(deptName);
    	 proParams.add(deptId);
         if (data.containsKey("staffName")) proParams.add(data.get("staffName"));
         else proParams.add("");
         if (data.containsKey("staffCode")) proParams.add(data.get("staffCode"));
         else proParams.add("");
         proParams.add("11,12,13,14");//BUSI_CODE
         proParams.add("1");//STATE
         proParams.add("宽带装移机不满意,宽带修障不满意,宽带修障未修复,宽带装移不能正常使用");//BUSI_NAME
         proParams.add("");//SEC_TYPE
        return getDataAccess().execUpdate(sql, proParams.toArray());
    }
    public int insertEwamUser(Map<String,Object> data) throws Exception {
   	 String sql = "INSERT INTO EWAM_CHARGE_MANAGER(CITY_NAME,CITY_ID,AREA_NAME,AREA_ID,DEPT_NAME," +
   			"DEPT_ID,STAFF_NAME,STAFF_CODE,STATE,SEC_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?)";
   	 List<Object> proParams = new ArrayList<Object>(); 
   	 String zoneId=Convert.toString(data.get("zoneId")).trim();
   	 String cityName="";
   	 String cityId="";
   	 String areaName="";
   	 String areaId="";
   	 String deptName="";
   	 String deptId="";
   	 Map<String, Object> zoneMap =new  HashMap<String, Object>();
   	 String zoneDimLevel=getDimLevelById(zoneId);
   	 if(zoneDimLevel=="5"||"5".equals(zoneDimLevel)){//营销中心
   		 zoneMap=getLevel5ById(zoneId);   
   		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
       	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
       	 cityId=getCityTreecodeById(cityId);
       	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
       	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
       	 deptName=Convert.toString(zoneMap.get("DEPT_NAME"))==null?"":zoneMap.get("DEPT_NAME").toString();
       	 deptId=Convert.toString(zoneMap.get("DEPT_ID"))==null?"":zoneMap.get("DEPT_ID").toString();
   	 }else if(zoneDimLevel=="4"||"4".equals(zoneDimLevel)){//分公司
   		 zoneMap=getLevel4ById(zoneId);
   		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
       	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
       	 cityId=getCityTreecodeById(cityId);
       	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
       	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
       	 deptName="";
       	 deptId="";
   	 }else if(zoneDimLevel=="3"||"3".equals(zoneDimLevel)){//地市
   		 zoneMap=getLevel3ById(zoneId);
   		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
       	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
       	 cityId=getCityTreecodeById(cityId);
       	 areaName="";
       	 areaId="";
       	 deptName="";
       	 deptId="";
   	 }    	 
   	 proParams.add(cityName);
   	 proParams.add(cityId);
   	 proParams.add(areaName);
   	 proParams.add(areaId);
   	 proParams.add(deptName);
   	 proParams.add(deptId);
        if (data.containsKey("staffName")) proParams.add(data.get("staffName"));
        else proParams.add("");
        if (data.containsKey("staffCode")) proParams.add(data.get("staffCode"));
        else proParams.add("");
        proParams.add("1");//STATE
        proParams.add("");//SEC_TYPE
       return getDataAccess().execUpdate(sql, proParams.toArray());
   }
    public int updateUser(Map<String,Object> data) throws Exception {
   	 String sql = "UPDATE NOT_STATISFY_CHARGE_MANAGER SET CITY_NAME=?,CITY_ID=?,AREA_NAME=?,AREA_ID=?,DEPT_NAME=?," +
   			"DEPT_ID=?,STAFF_NAME=?,STAFF_CODE=?,BUSI_CODE=?,STATE=?,BUSI_NAME=?,SEC_TYPE=? WHERE SEQ=?";
   	 List<Object> proParams = new ArrayList<Object>(); 
   	 String zoneId=Convert.toString(data.get("zoneId")).trim();
   	 String cityName="";
   	 String cityId="";
   	 String areaName="";
   	 String areaId="";
   	 String deptName="";
   	 String deptId="";
   	 Map<String, Object> zoneMap =new  HashMap<String, Object>();
   	 String zoneDimLevel=getDimLevelById(zoneId);
   	 if(zoneDimLevel=="5"||"5".equals(zoneDimLevel)){//营销中心
   		 zoneMap=getLevel5ById(zoneId);   
   		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
       	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
       	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
       	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
       	 deptName=Convert.toString(zoneMap.get("DEPT_NAME"))==null?"":zoneMap.get("DEPT_NAME").toString();
       	 deptId=Convert.toString(zoneMap.get("DEPT_ID"))==null?"":zoneMap.get("DEPT_ID").toString();
   	 }else if(zoneDimLevel=="4"||"4".equals(zoneDimLevel)){//分公司
   		 zoneMap=getLevel4ById(zoneId);
   		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
       	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
       	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
       	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
       	 deptName="";
       	 deptId="";
   	 }else if(zoneDimLevel=="3"||"3".equals(zoneDimLevel)||zoneDimLevel=="2"||"2".equals(zoneDimLevel)||zoneDimLevel=="1"||"1".equals(zoneDimLevel)){//地市
   		 zoneMap=getLevel3ById(zoneId);
   		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
       	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
       	 areaName="";
       	 areaId="";
       	 deptName="";
       	 deptId="";
   	 }    	 
   	 proParams.add(cityName);
   	 proParams.add(cityId);
   	 proParams.add(areaName);
   	 proParams.add(areaId);
   	 proParams.add(deptName);
   	 proParams.add(deptId);
        if (data.containsKey("staffName")) proParams.add(data.get("staffName"));
        else proParams.add("");
        if (data.containsKey("staffCode")) proParams.add(data.get("staffCode"));
        else proParams.add("");
        proParams.add("11,12,13,14");//BUSI_CODE
        proParams.add("1");//STATE
        proParams.add("宽带装移机不满意,宽带修障不满意,宽带修障未修复,宽带装移不能正常使用");//BUSI_NAME
        proParams.add("");//SEC_TYPE
        proParams.add(Convert.toString(data.get("seq")).trim());//SEQ
       return getDataAccess().execUpdate(sql, proParams.toArray());
   }
    public int updateEwamUser(Map<String,Object> data) throws Exception {
      	 String sql = "UPDATE EWAM_CHARGE_MANAGER SET CITY_NAME=?,CITY_ID=?,AREA_NAME=?,AREA_ID=?,DEPT_NAME=?," +
      			"DEPT_ID=?,STAFF_NAME=?,STAFF_CODE=?,STATE=?,SEC_TYPE=? WHERE SEQ=?";
      	 List<Object> proParams = new ArrayList<Object>(); 
      	 String zoneId=Convert.toString(data.get("zoneId")).trim();
      	 String cityName="";
      	 String cityId="";
      	 String areaName="";
      	 String areaId="";
      	 String deptName="";
      	 String deptId="";
      	 Map<String, Object> zoneMap =new  HashMap<String, Object>();
      	 String zoneDimLevel=getDimLevelById(zoneId);
      	 if(zoneDimLevel=="5"||"5".equals(zoneDimLevel)){//营销中心
      		 zoneMap=getLevel5ById(zoneId);   
      		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
          	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
          	 cityId=getCityTreecodeById(cityId);
          	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
          	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
          	 deptName=Convert.toString(zoneMap.get("DEPT_NAME"))==null?"":zoneMap.get("DEPT_NAME").toString();
          	 deptId=Convert.toString(zoneMap.get("DEPT_ID"))==null?"":zoneMap.get("DEPT_ID").toString();
      	 }else if(zoneDimLevel=="4"||"4".equals(zoneDimLevel)){//分公司
      		 zoneMap=getLevel4ById(zoneId);
      		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
          	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
          	 cityId=getCityTreecodeById(cityId);
          	 areaName=Convert.toString(zoneMap.get("AREA_NAME"))==null?"":zoneMap.get("AREA_NAME").toString();
          	 areaId=Convert.toString(zoneMap.get("AREA_ID"))==null?"":zoneMap.get("AREA_ID").toString();
          	 deptName="";
          	 deptId="";
      	 }else if(zoneDimLevel=="3"||"3".equals(zoneDimLevel)||zoneDimLevel=="2"||"2".equals(zoneDimLevel)||zoneDimLevel=="1"||"1".equals(zoneDimLevel)){//地市
      		 zoneMap=getLevel3ById(zoneId);
      		 cityName=Convert.toString(zoneMap.get("CITY_NAME"))==null?"":zoneMap.get("CITY_NAME").toString();
          	 cityId=Convert.toString(zoneMap.get("CITY_ID"))==null?"":zoneMap.get("CITY_ID").toString();
          	 cityId=getCityTreecodeById(cityId);
          	 areaName="";
          	 areaId="";
          	 deptName="";
          	 deptId="";
      	 }    	 
      	 proParams.add(cityName);
      	 proParams.add(cityId);
      	 proParams.add(areaName);
      	 proParams.add(areaId);
      	 proParams.add(deptName);
      	 proParams.add(deptId);
           if (data.containsKey("staffName")) proParams.add(data.get("staffName"));
           else proParams.add("");
           if (data.containsKey("staffCode")) proParams.add(data.get("staffCode"));
           else proParams.add("");
           proParams.add("1");//STATE
           proParams.add("");//SEC_TYPE
           proParams.add(Convert.toString(data.get("seq")).trim());//SEQ
          return getDataAccess().execUpdate(sql, proParams.toArray());
      }
    public String getDimLevelById(String zoneId){
    	String sql="select dim_level from meta_dim_zone_branch where zone_id='"+zoneId+"'";
    	return getDataAccess().queryForString(sql);
    }
    public Map<String, Object>  getDimLevelByZoneId(String zoneId){
    	String sql="select dim_level from meta_dim_zone_branch where zone_id='"+zoneId+"'";
    	return getDataAccess().queryForMap(sql.toString());
    }
    public Map<String, Object>getLevel5ById(String zoneId){
    	String sql="select a1.zone_name CITY_NAME,a1.zone_code CITY_ID,a2.zone_name AREA_NAME," +
		"a2.treecode AREA_ID,a3.zone_name DEPT_NAME,a3.treecode DEPT_ID from meta_dim_zone_branch a1," +
		"meta_dim_zone_branch a2,meta_dim_zone_branch a3 where a1.zone_code= a2.zone_par_code " +
		"and a2.zone_code = a3.zone_par_code and a3.zone_id='"+zoneId+"'";
    	return getDataAccess().queryForMap(sql);
    }
    public Map<String, Object>getLevel4ById(String zoneId){
    	String sql="select a1.zone_name CITY_NAME,a1.zone_code CITY_ID,a2.zone_name AREA_NAME," +
		"a2.treecode AREA_ID,NULL DEPT_NAME,NULL DEPT_ID from meta_dim_zone_branch a1," +
		"meta_dim_zone_branch a2 where a1.zone_code= a2.zone_par_code " +
		"and a2.zone_id='"+zoneId+"'";
    	return getDataAccess().queryForMap(sql);
    }
    public Map<String, Object>getLevel3ById(String zoneId){
    	String sql="select a1.zone_name CITY_NAME,a1.zone_code CITY_ID,NULL AREA_NAME," +
		" NULL AREA_ID,NULL DEPT_NAME,NULL DEPT_ID from meta_dim_zone_branch a1 " +
		" where a1.zone_id='"+zoneId+"'";
    	return getDataAccess().queryForMap(sql);
    }
    public String getCityTreecodeById(String zoneCode){
    	String sql="select a1.treecode from meta_dim_zone_branch a1 " +
		" where a1.zone_code='"+zoneCode+"'";
    	return getDataAccess().queryForString(sql);
    }
	public List<Map<String, Object>> getMenuVisitDetail(Map<String, Object> paramMap) {
	    StringBuffer sql = new StringBuffer(); 
	    sql.append("select u.user_namecn,");
		  sql.append("CASE WHEN  Z.ZONE_PAR_ID > 10 THEN " +
				  	"(SELECT dz.zone_name FROM meta_dim_zone dz WHERE dz.zone_id = decode(z.zone_par_id,'2','200', z.zone_par_id)) || '-' "+							  
				  	" ELSE '' END || z.zone_name AS SHOW_ZONE_NAME ,");
		  sql.append(" u.dept_name,u.station_name,MR.Role_Name,M.menu_name, to_char(l.visit_time,'yyyy-mm-dd hh24:mi:ss') visit_time ");
		  sql.append(" from meta_mag_menu_visit_log l");
				  sql.append(" LEFT JOIN META_MAG_USER U ON L.USER_ID = U.USER_ID");
				  sql.append(" LEFT JOIN META_DIM_ZONE Z ON U.ZONE_ID = Z.ZONE_ID");
				  sql.append(" LEFT JOIN META_MAG_USER_ROLE UR ON U.USER_ID = UR.USER_ID");
				  sql.append(" LEFT JOIN META_MAG_ROLE MR ON UR.ROLE_ID = MR.ROLE_ID");
				  sql.append(" LEFT JOIN (SELECT MENU_ID, MENU_NAME,");
				  sql.append(" SUBSTR(SYS_CONNECT_BY_PATH(MENU_NAME, '\\'), 2) MENU_PATH");
				  sql.append(" FROM META_MAG_MENU T");
				  sql.append(" WHERE CONNECT_BY_ISLEAF = 1");
				  sql.append(" START WITH PARENT_ID = 0 CONNECT BY PRIOR MENU_ID = PARENT_ID) M");
				  sql.append(" ON L.MENU_ID = M.MENU_ID");
				  sql.append(" where l.visit_time >= to_date('"+MapUtils.getString(paramMap, "startTime", "")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
				  sql.append(" and l.visit_time   <=  to_date('"+MapUtils.getString(paramMap, "endTime",   "")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
				
				String zoneCode = MapUtils.getString(paramMap, "zoneCode",   "");
				if(zoneCode != "" && !zoneCode.equals("0000"))
				{
					sql.append(" AND z.zone_id in (select ZONE_id from META_DIM_ZONE start with ZONE_CODE = '"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)");
				  //sql.append(" and (z.zone_code='"+zoneCode+"' OR z.ZONE_PAR_CODE='"+zoneCode+"') ");
				}  
				sql.append(" AND U.VIP_FLAG = 0 AND U.ADMIN_FLAG = 0 AND U.STATE = 1 AND M.MENU_NAME IS NOT NULL AND (ur.role_id <>'213762' AND  ur.role_id <>'213763')");
				
				String menuId = MapUtils.getString(paramMap, "menuId",  "");
				if(menuId != null && !menuId.equals(""))
				{
					sql.append(" AND M.MENU_ID = " + menuId);
				}
				sql.append(" ORDER BY L.VISIT_TIME DESC ,M.MENU_ID");
	 return getDataAccess().queryForList(sql.toString());
}
    
    
    
	public List<Map<String, Object>> getUserVisitCount(Map<String, Object> paramMap) {
		    StringBuffer sql = new StringBuffer();
					  sql.append("select u.user_namecn,");
					  sql.append("CASE WHEN  Z.ZONE_PAR_ID > 10 THEN " +
							  	"(SELECT dz.zone_name FROM meta_dim_zone dz WHERE dz.zone_id = decode(z.zone_par_id,'2','200', z.zone_par_id)) || '-' "+							  
							  	" ELSE '' END || z.zone_name AS SHOW_ZONE_NAME ,");
					  sql.append(" u.dept_name,u.station_name, mr.role_name ,count(*) cnt");
					  sql.append(" from meta_mag_menu_visit_log l");
					  sql.append(" left join meta_mag_user u");
					  sql.append(" on l.user_id = u.user_id");
					  sql.append(" left join meta_dim_zone z");
					  sql.append(" on   u.zone_id = z.zone_id");
					  
					  sql.append(" LEFT JOIN META_MAG_USER_ROLE UR");
					  sql.append(" ON U.USER_ID = UR.USER_ID");
					  
					  sql.append(" LEFT JOIN META_MAG_ROLE MR");
					  sql.append(" ON UR.ROLE_ID = MR.ROLE_ID");
					  
					  sql.append(" left join (select menu_id,");
					  sql.append(" menu_name,");
					  sql.append(" substr(sys_connect_by_path(menu_name, '\\'),2) menu_path");
					  sql.append(" from meta_mag_menu t");
					  sql.append(" where connect_by_isleaf = 1");
					  sql.append(" start with parent_id = 0");
					  sql.append(" connect by prior menu_id = parent_id) m");
					  sql.append(" on l.menu_id = m.menu_id");
					  sql.append(" where l.visit_time >= to_date('"+MapUtils.getString(paramMap, "startTime", "")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
					  sql.append(" and l.visit_time   <  to_date('"+MapUtils.getString(paramMap, "endTime",   "")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
					
					String zoneCode = MapUtils.getString(paramMap, "zoneCode",   "");
					if(zoneCode != "" && !zoneCode.equals("0000"))
					{
						sql.append(" AND z.zone_id in (select ZONE_id from META_DIM_ZONE start with ZONE_CODE = '"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)");
					 // sql.append(" and (z.zone_code='"+zoneCode+"' OR z.ZONE_PAR_CODE='"+zoneCode+"') ");
					}  
					  sql.append(" and u.vip_flag = 0");
					  sql.append(" AND U.ADMIN_FLAG = 0 AND U.STATE = 1");
					  sql.append(" and m.menu_name is not null AND (ur.role_id <>'213762' AND  ur.role_id <>'213763')");
					  sql.append(" group by u.user_namecn,z.zone_name,u.dept_name,u.station_name, Z.ZONE_PAR_ID, mr.role_name ");
					  sql.append(" order by count(*) desc");
    	 return getDataAccess().queryForList(sql.toString());
	}
	
    public List<Map<String, Object>>  getUserVisitDetail(Map<String, Object> paramMap) {
		StringBuffer sql = new StringBuffer();
						sql.append("select u.user_namecn,");
						sql.append(" CASE WHEN  Z.ZONE_PAR_ID > 10 THEN " +
								  	" (SELECT dz.zone_name FROM meta_dim_zone dz WHERE dz.zone_id = decode(z.zone_par_id,'2','200', z.zone_par_id)) || '-' "+							  
								  	"  ELSE '' END || z.zone_name AS SHOW_ZONE_NAME ,");		
						sql.append(" u.dept_name,u.station_name,m.menu_name,m.menu_path,to_char(l.visit_time,'yyyy-mm-dd hh24:mi:ss') visit_time");
						sql.append(" from meta_mag_menu_visit_log l");
						sql.append(" left join meta_mag_user u");
						sql.append(" on l.user_id = u.user_id");
						//sql.append(" left join meta_mag_user_dept d");
						//sql.append(" on u.dept_id = d.dept_id");
						//sql.append(" left join meta_mag_user_station s");
						//sql.append(" on u.station_id = s.station_id");
						
						sql.append(" left join meta_dim_zone z");
						sql.append(" on   u.zone_id = z.zone_id");
						
						sql.append(" left join (select menu_id,");
						sql.append(" menu_name,");
						sql.append(" substr(sys_connect_by_path(menu_name, '\\'),2) menu_path");
						sql.append(" from meta_mag_menu t");
						sql.append(" where connect_by_isleaf = 1");
						sql.append(" start with parent_id = 0");
						sql.append(" connect by prior menu_id = parent_id) m");
						sql.append(" on l.menu_id = m.menu_id");
						sql.append(" where l.visit_time >= to_date('"+MapUtils.getString(paramMap, "startTime", "")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
						sql.append(" and l.visit_time <    to_date('"+MapUtils.getString(paramMap, "endTime",   "")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
						/**
						sql.append(" and u.admin_flag = 0");
						**/
						sql.append(" and u.vip_flag = 0");
					String userName = MapUtils.getString(paramMap, "userName",   "");
					if(!userName.equals(""))
					{	
						sql.append(" and u.user_namecn='"+userName+"'");
					}	
						sql.append(" and m.menu_name is not null");
						sql.append(" order by l.visit_time desc");
		return getDataAccess().queryForList(sql.toString());
	}
    public List<Map<String, Object>> getDeptIdByZoneId(String zoneId){
    	String sql="select * from NOT_STATISFY_CHARGE_MANAGER where dept_id=" +
    			"(select treecode from meta_dim_zone_branch where zone_id='"+zoneId+"') and state='1' ";
    	return getDataAccess().queryForList(sql);
    }
    public List<Map<String, Object>> getEwamDeptIdByZoneId(String zoneId){
    	String sql="select * from EWAM_CHARGE_MANAGER where dept_id=" +
    			"(select treecode from meta_dim_zone_branch where zone_id='"+zoneId+"') and state='1' ";
    	return getDataAccess().queryForList(sql);
    }
   /**
    *  add  @author yanhaidong  end
    */

}
