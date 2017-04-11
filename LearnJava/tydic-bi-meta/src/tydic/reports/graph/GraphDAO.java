package tydic.reports.graph;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

/**
 * @modify  qx
 * @modifyDate  2013-9-12
 */
public class GraphDAO extends MetaBaseDAO {
	
	//区域权限控制处理  changeZone:2:下两级 1:下一级  0:同级_网格
	public List<Map<String, Object>> getChangeZoneLevel(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode="".equals(Convert.toString(queryData.get("changeCode")))?"1":Convert.toString(queryData.get("changeCode"));
		String sql="select rownum,order_id,ZONE_CODE,ZONE_NAME,DIM_LEVEL from (";
		if("0".equals(changeCode)){
			sql+="select order_id,ZONE_CODE, ZONE_NAME,zone_par_code,DIM_LEVEL from META_DIM_ZONE_CUSTGRP " +
			"WHERE zone_par_code= (SELECT zone_par_code FROM META_DIM_ZONE_CUSTGRP WHERE ZONE_CODE = '"+zoneCode+"')" +
					" order by order_id";
		}else if("1".equals(changeCode)&&!zoneCode.equals("0000")){
		    sql+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from META_DIM_ZONE_CUSTGRP start with ZONE_CODE = '"+zoneCode+"' " +
				"connect by prior ZONE_CODE = zone_par_code)b  WHERE b.DIM_LEVEL = " +
				"(SELECT DIM_LEVEL+'"+changeCode+"' FROM META_DIM_ZONE_CUSTGRP WHERE ZONE_CODE ='"+zoneCode+"')" +
						" order by dim_level,order_id";
			
		}else
		{
			sql+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from META_DIM_ZONE_CUSTGRP)b  WHERE b.DIM_LEVEL = " +
			"(SELECT DIM_LEVEL+'"+changeCode+"' FROM META_DIM_ZONE_CUSTGRP WHERE ZONE_CODE ='"+zoneCode+"')" +
					" order by dim_level,order_id";
		}
		sql+=")where rownum<23";
		List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	    return list;
	}
	//渠道服务区域月、日、周报表区域权限控制处理  changeZone:2:下两级 1:下一级  0:同级_网格
	public List<Map<String, Object>> getChangeZoneLevel_channel(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode="".equals(Convert.toString(queryData.get("changeCode")))?"1":Convert.toString(queryData.get("changeCode"));
		String channelTypeCode="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String sqlTemp="select DIM_LEVEL from META_DIM_ZONE_CUSTGRP where zone_code='"+zoneCode+"'";
		String dimLevel=getDataAccess().queryForString(sqlTemp.toString());
		if("14001".equals(channelTypeCode)||"10".equals(channelTypeCode)||"1".equals(channelTypeCode)){//自营厅、传统渠道、所有类型
			if("4".equals(dimLevel)){//第四级
			   changeCode="0";
			}
		}else{
			if(Integer.parseInt(dimLevel)>=3){
				changeCode="0";
			}
		}	
		String sql="select rownum,order_id,ZONE_CODE,ZONE_NAME,DIM_LEVEL from (";
		if("0".equals(changeCode)){
			sql+="select order_id,ZONE_CODE, ZONE_NAME,zone_par_code,DIM_LEVEL from META_DIM_ZONE_CUSTGRP " +
			"WHERE zone_par_code= (SELECT zone_par_code FROM META_DIM_ZONE_CUSTGRP WHERE ZONE_CODE = '"+zoneCode+"')" +
					" order by order_id";
		}else if("1".equals(changeCode)&&!zoneCode.equals("0000")){
		    sql+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from META_DIM_ZONE_CUSTGRP start with ZONE_CODE = '"+zoneCode+"' " +
				"connect by prior ZONE_CODE = zone_par_code)b  WHERE b.DIM_LEVEL = " +
				"(SELECT DIM_LEVEL+'"+changeCode+"' FROM META_DIM_ZONE_CUSTGRP WHERE ZONE_CODE ='"+zoneCode+"')" +
						" order by dim_level,order_id";
			
		}else
		{
			sql+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from META_DIM_ZONE_CUSTGRP)b  WHERE b.DIM_LEVEL = " +
			"(SELECT DIM_LEVEL+'"+changeCode+"' FROM META_DIM_ZONE_CUSTGRP WHERE ZONE_CODE ='"+zoneCode+"')" +
					" order by dim_level,order_id";
		}
		sql+=")where rownum<23";
		List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	    return list;
	}
	//区域权限控制处理  changeZone:2:下两级 1:下一级  0:同级-分公司
	public List<Map<String, Object>> getChangeZoneLevelCom(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode="".equals(Convert.toString(queryData.get("changeCode")))?"1":Convert.toString(queryData.get("changeCode"));
		String sql="select rownum,order_id,ZONE_CODE,ZONE_NAME,DIM_LEVEL from (";
		if("0".equals(changeCode)){
			sql+="select order_id,ZONE_CODE, ZONE_NAME,zone_par_code,DIM_LEVEL from meta_dim_zone_branch " +
			"WHERE zone_par_code= (SELECT zone_par_code FROM meta_dim_zone_branch WHERE ZONE_CODE = '"+zoneCode+"')" +
					" order by order_id";
		}else{
		    sql+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from meta_dim_zone_branch start with ZONE_CODE = '"+zoneCode+"' " +
				"connect by prior ZONE_CODE = zone_par_code)b  WHERE b.DIM_LEVEL = " +
				"(SELECT DIM_LEVEL+'"+changeCode+"' FROM meta_dim_zone_branch WHERE ZONE_CODE ='"+zoneCode+"')" +
						" order by dim_level,order_id";
		}
		sql+=")where rownum<23";
		List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	    return list;
	}
	public List<Map<String, Object>> getChangeZoneLevelCom_Dg(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode="".equals(Convert.toString(queryData.get("changeCode")))?"1":Convert.toString(queryData.get("changeCode"));
		if(zoneCode.length()==3){
			changeCode="2";
		}
		String sql="select rownum,order_id,ZONE_CODE,ZONE_NAME,DIM_LEVEL from (";
		if("0".equals(changeCode)){
			sql+="select order_id,ZONE_CODE, ZONE_NAME,zone_par_code,DIM_LEVEL from meta_dim_zone_branch " +
			"WHERE zone_par_code= (SELECT zone_par_code FROM meta_dim_zone_branch WHERE ZONE_CODE = '"+zoneCode+"')" +
					" order by order_id";
		}else{
		    sql+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from meta_dim_zone_branch start with ZONE_CODE = '"+zoneCode+"' " +
				"connect by prior ZONE_CODE = zone_par_code)b  WHERE b.DIM_LEVEL = " +
				"(SELECT DIM_LEVEL+'"+changeCode+"' FROM meta_dim_zone_branch WHERE ZONE_CODE ='"+zoneCode+"')" +
						" order by dim_level,order_id";
		}
		sql+=")where rownum<23";
		List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	    return list;
	}
    //按月查询最大周
	public String getZoneLevel(String zoneCode) {
		String sqlTemp="select DIM_LEVEL from META_DIM_ZONE_CUSTGRP where zone_code='"+zoneCode+"'";
		String dimLevel=getDataAccess().queryForString(sqlTemp.toString());
		return dimLevel;
	}
}
