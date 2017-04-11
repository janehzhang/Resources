package tydic.portalCommon.multDimen;

import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;

public class MultDimenAnalyDAO  extends MetaBaseDAO{
	
	public List<Map<String, Object>> getData(String timeType, int type, String parent){
		//a.day_id >= '{start_time}' and a.day_id <='{end_time}
		String startTime = "";
		String endTime = "";
		
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		String sql = this.buildSql(type);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		if(type == 2){
			if(parent != null){
				sql = sql.replace("{parent_id}", parent);
			}else{
				sql = sql.replace("{parent_id}", "-1");
			}
		}else if(type == 3){
			if(parent != null){
				sql = sql.replace("{parent_id}", parent);
			}else{
				sql = sql.replace("{parent_id}", "1");
			}
		}
		sql = sql.replace("{screen}", "");
		sql = sql.replace("{map_screen}", "");
		sql = sql.replace("{city_level}", "1");
		sql = sql.replace("{level}", "1");
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	public List<Map<String, Object>> getAllData(String timeType, int type, String parent, int level){
		//a.day_id >= '{start_time}' and a.day_id <='{end_time}
		String startTime = "";
		String endTime = "";
		
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		String sql = this.buildSql(type);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		if(parent != null){
			sql = sql.replace("{parent_id}", parent);
		}
		if(type == 2 || type == 21){
			if(parent != null){
				sql = sql.replace("{parent_id}", parent);
			}else{
				sql = sql.replace("{parent_id}", "-1");
			}
		}else if(type == 3 || type == 31){
			if(parent != null){
				sql = sql.replace("{parent_id}", parent);
			}else{
				sql = sql.replace("{parent_id}", "1");
			}
		}
		if(level == 0){
			sql = sql.replace("{level}", "1");
		}else{
			sql = sql.replace("{level}", level+"");
		}
		
		sql = sql.replace("{screen}", "");
		sql = sql.replace("{map_screen}", "");
		sql = sql.replace("{city_level}", "1");
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	public List<Map<String, Object>> getMapLDData(String timeType, int type, String parent, String screen, String cityLevel){
		//a.day_id >= '{start_time}' and a.day_id <='{end_time}
		String startTime = "";
		String endTime = "";
		
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		String sql = this.buildSql(type);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		if(parent != null){
			sql = sql.replace("{parent_id}", parent);
		}
		sql = sql.replace("{screen}", "");
		sql = sql.replace("{map_screen}", screen);
		sql = sql.replace("{city_level}", cityLevel+"");
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	public List<Map<String, Object>> getPieData(String timeType, int type, String screen, String ldScreen, String cityLevel, String mapScreen){
		//a.day_id >= '{start_time}' and a.day_id <='{end_time}
		String startTime = "";
		String endTime = "";
		
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		String sql = this.buildSql(type);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		if(screen != null){
			sql = sql.replace("{screen}", screen);
		}else{
			sql = sql.replace("{screen}", "");
		}
		if(ldScreen != null){
			sql = sql.replace("{ld_screen}", ldScreen);
		}else{
			sql = sql.replace("{ld_screen}", "");
		}
		
		if(cityLevel == null && mapScreen== null){
			sql = sql.replace("{map_screen}", "");
			sql = sql.replace("{city_level}", "1");
		}else{
			sql = sql.replace("{map_screen}", mapScreen);
			sql = sql.replace("{city_level}", cityLevel+"");
		}
		
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	public List<Map<String, Object>> getLDData(String timeType, int type, String screen){
		//a.day_id >= '{start_time}' and a.day_id <='{end_time}
		String startTime = "";
		String endTime = "";
		
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		String sql = this.buildSql(type);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		
		sql = sql.replace("{parent_id}", "");
		
		if(screen != null){
			sql = sql.replace("{screen}", screen);
		}else{
			sql = sql.replace("{screen}", "");
		}
		sql = sql.replace("{map_screen}", "");
		sql = sql.replace("{city_level}", "1");
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	public List<Map<String, Object>> getDrLDData(String timeType, int type, String parentId, int level, String cityLevel, String mapScreen){
		String startTime = "";
		String endTime = "";
		String screen = "";
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		
		String sql = this.buildSql(type);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		if(type == 22){
			screen  = " and a.CMPL_BUSINESS_TYPE_ID like '" + parentId + "%'";   
			sql = sql.replace("{parentId}", "-1").replace("{level}", String.valueOf(level)).replace("{screen}", screen);
		}else if(type == 32){
			screen  = " and a.CMPL_PROD_TYPE_ID like '" + parentId + "%'";
			sql = sql.replace("{parentId}", "1").replace("{level}", String.valueOf(level)).replace("{screen}", screen);
		}
		if(cityLevel == null && mapScreen== null){
			sql = sql.replace("{map_screen}", "");
			sql = sql.replace("{city_level}", "1");
		}else{
			sql = sql.replace("{map_screen}", mapScreen);
			sql = sql.replace("{city_level}", cityLevel+"");
		}
		
		
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	public List<Map<String, Object>> getDrillData(String timeType, int type, String parentId, int level){
		String startTime = "";
		String endTime = "";
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		
		String sql = this.buildSql(type);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		sql = sql.replace("{parentId}", parentId).replace("{level}", String.valueOf(level)).replace("{screen}", "");
		sql = sql.replace("{map_screen}", "");
		sql = sql.replace("{city_level}", "1");
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	public List<Map<String, Object>> getMapData(String timeType, String parentId){
		String startTime = "";
		String endTime = "";
		
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		String sql = this.b11_sql.replace("{par_id}", parentId);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	public List<Map<String, Object>> getCountyData(String timeType, String code){
		String startTime = "";
		String endTime = "";
		
		String dateTime = buildDate(timeType);
		if(dateTime.indexOf("_")>0){
			String[] ds = dateTime.split("_");
			startTime = ds[0].trim();
			endTime = ds[1].trim();
		}else{
			startTime = dateTime.trim();
			endTime = dateTime.trim();
		}
		String sql = this.b13_sql.replace("{code}", code);
		sql = sql.replace("{start_time}", startTime).replace("{end_time}", endTime);
		sql = sql.replace("{map_screen}", "");
		sql = sql.replace("{city_level}", "1");
		List<Map<String, Object>>  map = getDataAccess().queryForList(sql);
		return map;
		
	}
	
	private String buildDate(String timeType){
		DateUtil dUtil = new DateUtil();
		String dateTime = dUtil.getyd();
		if(timeType.equals("yest")){
			dateTime = dUtil.getyd();
		}else if(timeType.equals("week")){
			dateTime = this.getWeekDate();
		}else if(timeType.equals("mon")){
			dateTime = dUtil.getPreviousMonthFirst() + "_" + dUtil.getPreviousMonthEnd();
		}
		return dateTime;
	}
	
	private String buildSql(int type){
		String sql = "";
		switch (type) {
		case 1:
			sql = this.b1_sql_map;//第一幅图表  地图sql
			break;
		case 11:
			sql = this.b11_sql;  //第一幅图表  下拉片区
			break;
		case 12:
			sql = this.b12_sql; //第一幅图表 按地市
			break;
		case 13:
			sql = this.b13_sql; //第一幅图表 县级数据
			break;
		case 2:
			sql = this.b2_sql;  //第二幅图表 产品投诉 一级产品 top5
			break;
		case 21:
			sql = this.b2_all_sql;  //第二幅图表 产品投诉 一级产品 all
			break;
		case 22:
			sql = this.b22_sql;	//第二幅图表 产品投诉  下钻sql
			break;	
		case 3:
			sql = this.b3_sql;  //第三幅图表 表象投诉 一级表象 top5
			break;
		case 31:
			sql = this.b3_all_sql;  //第三幅图表 表象投诉 一级表象 all
			break;
		case 32:
			sql = this.b32_sql;  //第三幅图表 产品投诉 表象投诉 下钻sql
			break;	
		case 41:
			sql = this.b41_sql; //第四幅图表 饼图
			break;
		case 42:
			sql = this.b42_sql;
			break;
		case 43:
			sql = this.b43_sql;
			break;
		case 44:
			sql = this.b44_sql;
			break;
		case 45:
			sql = this.b45_sql;
			break;
		case 46:
			sql = this.b46_sql;
			break;
		case 47:
			sql = this.b47_sql;
			break;
		case 48:
			sql = this.b48_sql;
			break;
		case 51:
			sql = this.b51_sql;  //考核维度 按省公司
			break;
		case 511:
			sql = this.b511_sql; //考核维度  联动数据sql
			break;
		case 52:
			sql = this.b52_sql;	 //考核维度 按地市
			break;
		case 521:
			sql = this.b521_sql; //考核维度  联动数据sql
			break;
		default:
			break;
		}
		return sql;
	}
	
	private String getWeekDate(){
		String sql = "select GET_MAX_PARTITION_DATE('CS_VISIT_VIP_AREA',17) WEEK from dual";
		List<Map<String, Object>>  list = getDataAccess().queryForList(sql);
		if(list.isEmpty()){
			return "";
		}
		return String.valueOf(list.get(0).get("WEEK"));
	}
	
	private String b1_sql_map = "select b.zone_qx NAME,  "+
			"       b.zone_qx_code CODE,  "+
			"         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM,  "+
			"       max(a.NUM1) TOTAL_NUM,  "+
			"       trim(to_char(decode(nvl(max(a.num1),0),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')||'') RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') LOOP_RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			"                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			"              0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') SAME_RATIO  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d  "+
			"          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('2')  "+
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {screen}  "+
			"  group by b.zone_qx,  "+
			"         b.zone_qx_code  "+
			"order by zone_qx_code  ";
	private String b2_sql =  "select * from ( "+
			 " SELECT aa.CMPL_PROD_TYPE_CODE CODE,  "+
			 "       aa.CMPL_PROD_TYPE_NAME NAME，  "+
			 "       aa.COMPLAIN_NUM COMPLAIN_NUM,  "+
			 "       aa.LOOP_RATIO,  "+
			 "       aa.SAME_RATIO,  "+
			 "       bb.COMPLAIN_NUM TOTAL_NUM,  "+
			 " trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')) RATIO  "+
			 " FROM (select c.CMPL_PROD_TYPE_CODE ,  "+
			 "       c.CMPL_PROD_TYPE_NAME ,  "+
			 "       sum(a.COMPLAIN_NUM)  COMPLAIN_NUM,  "+
			 "       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO,  "+
			 "       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			 "                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			 "              0,DECODE(max(NUM1),0,0,  "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO  "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			 "       meta_dim_zone_custgrp b,  "+
			 "        (select CMPL_PROD_TYPE_CODE,CMPL_PROD_TYPE_NAME,t.dim_level  "+
			 "         "+
			 "          from tbas_dm.d_v_cmpl_prod_condition t   "+
			 "         start with CMPL_PROD_TYPE_CODE = nvl('{parent_id}','-1')  "+
			 "        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE  ) c  "+
			 "          "+
			 " where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			 "  and a.region_id = b.zone_code  "+
		 	 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			 "  and b.dim_level in ('{city_level}') "+
		 	 "  {map_screen} " + 
			 "  and c.dim_level in ({level})  "+
			 "  and a.cmpl_prod_type_id like c.cmpl_prod_type_code||'%'  "+
			 "  {screen} " +
			 "  group by c.CMPL_PROD_TYPE_CODE,  "+
			 "           c.CMPL_PROD_TYPE_NAME) aa,  "+
			 " (select         "+
			 "       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			 "       meta_dim_zone_custgrp b    "+
			 "        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			 "  and a.region_id = b.zone_code  "+
			 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			 "  and b.dim_level in ('{city_level}') " +
			 "  {map_screen} " +
			 "  ) bb    "+
			 "        order by aa.COMPLAIN_NUM desc     "+
			 "  ) where  rownum <= 5  ";
	
	private String b2_all_sql = " SELECT aa.CMPL_PROD_TYPE_CODE CODE,  "+
			 "       aa.CMPL_PROD_TYPE_NAME NAME，  "+
			 "       aa.COMPLAIN_NUM COMPLAIN_NUM,  "+
			 "       aa.LOOP_RATIO,  "+
			 "       aa.SAME_RATIO,  "+
			 "       bb.COMPLAIN_NUM TOTAL_NUM,  "+
			 " trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')) RATIO  "+
			 " FROM (select c.CMPL_PROD_TYPE_CODE ,  "+
			 "       c.CMPL_PROD_TYPE_NAME ,  "+
			 "       sum(a.COMPLAIN_NUM)  COMPLAIN_NUM,  "+
			 "       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO,  "+
			 "       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			 "                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			 "              0,DECODE(max(NUM1),0,0,  "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO  "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			 "       meta_dim_zone_custgrp b,  "+
			 "        (select CMPL_PROD_TYPE_CODE,CMPL_PROD_TYPE_NAME,t.dim_level  "+
			 "         "+
			 "          from tbas_dm.d_v_cmpl_prod_condition t   "+
			 "         start with CMPL_PROD_TYPE_CODE = nvl('{parent_id}','-1')  "+
			 "        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE  ) c  "+
			 "          "+
			 " where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			 "  and a.region_id = b.zone_code  "+
		 	 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			 "  and b.dim_level in ('1') "+
			 "  and c.dim_level in ({level})  "+
			 "  and a.cmpl_prod_type_id like c.cmpl_prod_type_code||'%'  "+
			 "  group by c.CMPL_PROD_TYPE_CODE,  "+
			 "           c.CMPL_PROD_TYPE_NAME) aa,  "+
			 " (select         "+
			 "       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			 "       meta_dim_zone_custgrp b    "+
			 "        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			 "  and a.region_id = b.zone_code  "+
			 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			 "  and b.dim_level in ('1')  "+
			 "  ) bb    "+
			 "        order by aa.COMPLAIN_NUM desc     ";
	
	private String b3_sql =  "select * from ( "+
			"SELECT aa.CODE ,aa.NAME ,aa.COMPLAIN_NUM ,aa.LOOP_RATIO ,aa.SAME_RATIO ,bb.TOTAL_NUM,  "+
			" trim(to_char(decode(bb.TOTAL_NUM,0,0,round(aa.COMPLAIN_NUM / bb.TOTAL_NUM * 100, 2)),'FM99990.00')) RATIO  "+
			"FROM (select c.cmpl_busi_type_code CODE,  "+
			"       c.cmpl_busi_type_name NAME,  "+
			"       sum(a.COMPLAIN_NUM)  COMPLAIN_NUM,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			"                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			"              0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"        (select cmpl_busi_type_code,cmpl_busi_type_name,t.dim_level  "+
			"         "+
			"          from tbas_dm.d_v_cmpl_business_condition t  "+
			"         start with cmpl_busi_type_code = nvl('{parent_id}','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code  ) c  "+
			"          "+
			" where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') " +
			"  {map_screen} " +
			"  and c.dim_level in ({level})  "+
			"  and a.Cmpl_Business_Type_Id like c.cmpl_busi_type_code||'%'  "+
			"  {screen} " +
			"  group by c.cmpl_busi_type_code,  "+
			"           c.cmpl_busi_type_name) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) TOTAL_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b    "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}' "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') " +
			"  {map_screen} " +
			"  ) bb    "+
			"   order by aa.COMPLAIN_NUM desc  "+
			"  ) where  rownum <= 5  ";

	private String b3_all_sql ="SELECT aa.CODE ,aa.NAME ,aa.COMPLAIN_NUM ,aa.LOOP_RATIO ,aa.SAME_RATIO ,bb.TOTAL_NUM,  "+
			" trim(to_char(decode(bb.TOTAL_NUM,0,0,round(aa.COMPLAIN_NUM / bb.TOTAL_NUM * 100, 2)),'FM99990.00')) RATIO  "+
			"FROM (select c.cmpl_busi_type_code CODE,  "+
			"       c.cmpl_busi_type_name NAME,  "+
			"       sum(a.COMPLAIN_NUM)  COMPLAIN_NUM,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			"                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			"              0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"        (select cmpl_busi_type_code,cmpl_busi_type_name,t.dim_level  "+
			"         "+
			"          from tbas_dm.d_v_cmpl_business_condition t  "+
			"         start with cmpl_busi_type_code = nvl('{parent_id}','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code  ) c  "+
			"          "+
			" where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('1')  "+
			"  and c.dim_level in ({level})  "+
			"  and a.Cmpl_Business_Type_Id like c.cmpl_busi_type_code||'%'  "+
			"  group by c.cmpl_busi_type_code,  "+
			"           c.cmpl_busi_type_name) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) TOTAL_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b    "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('1')  "+
			"  ) bb    "+
			"   order by aa.COMPLAIN_NUM desc  ";
	
	private String b51_sql =  " select * from (  "+
			 " select A.Duty_Group_Province_Name NAME, "+
			 "       sum(a.COMPLAIN_NUM) COMPLAIN_NUM, "+
			 "       max(a.NUM1) TOTAL_NUM, "+
			 "       trim(to_char(decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')) RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(HB_NUM1),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(TB_NUM1),0,0, "+
			 "                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0, "+
			 "              0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a, "+
			 "       meta_dim_zone_custgrp b, "+
			 "       (select CMPL_PROD_TYPE_CODE,CMPL_PROD_TYPE_NAME,t.dim_level "+
			 "        "+
			 "          from tbas_dm.d_v_cmpl_prod_condition t  "+
			 "         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1') "+
			 "        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE  ) c, "+
			 "       (select cmpl_busi_type_code "+
			 "          from tbas_dm.d_v_cmpl_business_condition "+
			 "         start with cmpl_busi_type_code = nvl('1','1') "+
			 "        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d "+
			 "         "+
			 "        where a.day_id >= '{start_time}' and a.day_id <='{end_time}' "+
			 "  and a.region_id = b.zone_code "+
			 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000') "+
			 "  and b.dim_level in ('1') "+
			 "  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE "+
			 "  AND A.Duty_Group_Province_Name NOT LIKE '%不考核%' "+
			 "  and a.cmpl_business_type_id = d.cmpl_busi_type_code "+
			 "  {screen} " +
			 "  group by A.Duty_Group_Province_Name "+
			 "  order by sum(a.COMPLAIN_NUM) desc "+
			 "	) where  rownum <= 5 ";
	
	private String b511_sql =  " select * from (  "+
			 " select A.Duty_Group_Province_Name NAME, "+
			 "       sum(a.COMPLAIN_NUM) COMPLAIN_NUM, "+
			 "       max(a.NUM1) TOTAL_NUM, "+
			 "       trim(to_char(decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')) RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(HB_NUM1),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(TB_NUM1),0,0, "+
			 "                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0, "+
			 "              0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a, "+
			 "       meta_dim_zone_custgrp b, "+
			 "       (select CMPL_PROD_TYPE_CODE,CMPL_PROD_TYPE_NAME,t.dim_level "+
			 "        "+
			 "          from tbas_dm.d_v_cmpl_prod_condition t  "+
			 "         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1') "+
			 "        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE  ) c, "+
			 "       (select cmpl_busi_type_code "+
			 "          from tbas_dm.d_v_cmpl_business_condition "+
			 "         start with cmpl_busi_type_code = nvl('1','1') "+
			 "        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d "+
			 "         "+
			 "        where a.day_id >= '{start_time}' and a.day_id <='{end_time}' "+
			 "  and a.region_id = b.zone_code "+
			 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000') "+
			 "  and b.dim_level in ('{city_level}') "+
		 	 "  {map_screen} " + 
			 "  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE "+
			 "  AND A.Duty_Group_Province_Name NOT LIKE '%不考核%' "+
			 "  and a.cmpl_business_type_id = d.cmpl_busi_type_code "+
			 "  {screen}"+
			 "  group by A.Duty_Group_Province_Name "+
			 "  order by sum(a.COMPLAIN_NUM) desc "+
			 "	) where  rownum <= 5 ";
	
	private String b521_sql =  " select * from (    "+
			 "	select  "+
			 "         A.REGION_NAME NAME, "+
			 "        "+
			 "       sum(a.COMPLAIN_NUM) COMPLAIN_NUM, "+
			 "       max(a.NUM1) TOTAL_NUM, "+
			 "       trim(to_char(decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')) RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(HB_NUM1),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(TB_NUM1),0,0, "+
			 "                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0, "+
			 "              0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a, "+
			 "       meta_dim_zone_custgrp b, "+
			 "       (select CMPL_PROD_TYPE_CODE,CMPL_PROD_TYPE_NAME,t.dim_level "+
			 "        "+
			 "          from tbas_dm.d_v_cmpl_prod_condition t  "+
			 "         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1') "+
			 "        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE  ) c, "+
			 "       (select cmpl_busi_type_code "+
			 "          from tbas_dm.d_v_cmpl_business_condition "+
			 "         start with cmpl_busi_type_code = nvl('1','1') "+
			 "        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d "+
			 "         "+
			 "        where a.day_id >= '{start_time}' and a.day_id <='{end_time}' "+
			 "  and a.region_id = b.zone_code "+
			 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000') "+
			 "  and b.dim_level in ('3') "+
			 "  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE "+
			 "  AND A.DUTY_GROUP_CITY_NAME NOT LIKE '%不考核%' "+
			 "  and a.cmpl_business_type_id = d.cmpl_busi_type_code "+
			 "  and d.cmpl_busi_type_code = '{parent_id}'"+
			 "  {screen} " +
			 "  group by  "+
			 "         A.REGION_NAME "+
			 "         order by sum(a.COMPLAIN_NUM) desc "+
			 "  ) where  rownum <= 5 ";

	private String b52_sql =  " select * from (    "+
			 "	select  "+
			 "         A.REGION_NAME NAME, "+
			 "        "+
			 "       sum(a.COMPLAIN_NUM) COMPLAIN_NUM, "+
			 "       max(a.NUM1) TOTAL_NUM, "+
			 "       trim(to_char(decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')) RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(HB_NUM1),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')) LOOP_RATIO, "+
			 "       trim(to_char(DECODE(DECODE(max(TB_NUM1),0,0, "+
			 "                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0, "+
			 "              0,DECODE(max(NUM1),0,0, "+
			 "                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')) SAME_RATIO "+
			 "       from CS_SERV_CMPL_ANALYSIS_DAY a, "+
			 "       meta_dim_zone_custgrp b, "+
			 "       (select CMPL_PROD_TYPE_CODE,CMPL_PROD_TYPE_NAME,t.dim_level "+
			 "        "+
			 "          from tbas_dm.d_v_cmpl_prod_condition t  "+
			 "         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1') "+
			 "        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE  ) c, "+
			 "       (select cmpl_busi_type_code "+
			 "          from tbas_dm.d_v_cmpl_business_condition "+
			 "         start with cmpl_busi_type_code = nvl('1','1') "+
			 "        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d "+
			 "         "+
			 "        where a.day_id >= '{start_time}' and a.day_id <='{end_time}' "+
			 "  and a.region_id = b.zone_code "+
			 "  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000') "+
			 "  and b.dim_level in ('3') "+
			 "  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE "+
			 "  AND A.DUTY_GROUP_CITY_NAME NOT LIKE '%不考核%' "+
			 "  and a.cmpl_business_type_id = d.cmpl_busi_type_code "+
			 "  {screen} " +
			 "  group by  "+
			 "         A.REGION_NAME "+
			 "         order by sum(a.COMPLAIN_NUM) desc "+
			 "  ) where  rownum <= 5 ";
	
	private String b42_sql = "select aa.is_4g_disc Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.is_4g_disc,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"   from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			" connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.is_4g_disc ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb   "+
			"	where aa.is_4g_disc is not null " + 
			"		 {screen} ";
	
	private String b44_sql = "select aa.payment_id Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.payment_id,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.payment_id ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb  "+
			"	where aa.payment_id is not null  " + 
			"		 {screen} ";
	
	private String b46_sql = "select aa.is_intelligence Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.is_intelligence,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.is_intelligence ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb  "+
			"	where aa.is_intelligence in ('0','1')  " + 
			"		 {screen} ";

	private String b41_sql = "select aa.serv_lev Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.serv_lev,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.serv_lev ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb " +
			"  where aa.serv_lev is not null " + 
			"		 {screen} ";

	
	private String b48_sql = "select aa.DIVIDE_MARKET_6_NAME Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.DIVIDE_MARKET_6_NAME,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.DIVIDE_MARKET_6_NAME ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb  "+
			"	where aa.DIVIDE_MARKET_6_NAME is not null " + 
			"		 {screen} ";
	
	
	private String b47_sql = "select aa.SERV_GRP_TYPE Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.SERV_GRP_TYPE,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.SERV_GRP_TYPE ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb  "+
			"	where aa.SERV_GRP_TYPE in ('政企', '公众')  " + 
			"		 {screen} ";
	
	private String b45_sql = "select aa.optical_fiber_type Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.optical_fiber_type,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.optical_fiber_type ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb  "+
			"	where aa.optical_fiber_type in(0,1)  " + 
			"		 {screen} ";
	
	private String b43_sql = "select aa.bhgq_type Y_OR_N,aa.COMPLAIN_NUM COMPLAIN_NUM, bb.COMPLAIN_NUM TOTAL_NUM，  "+
			" trim(to_char(decode(bb.COMPLAIN_NUM,0,0,round(aa.COMPLAIN_NUM / bb.COMPLAIN_NUM * 100, 2)),'FM99990.00')||'%') RATIO  "+
			" from (select a.bhgq_type,         "+
			"       sum(a.COMPLAIN_NUM)    COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  {ld_screen}  "+
			"  group by a.bhgq_type ) aa,(select         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code" +
			"  {ld_screen}  "+
			") bb  " +
			"  where aa.bhgq_type is not null " + 
			"		 {screen} ";
	
	private String b22_sql =  "select * from ( "+
			" select c.CMPL_PROD_TYPE_CODE CODE,  "+
			"       c.CMPL_PROD_TYPE_NAME NAME,  "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"        (select CMPL_PROD_TYPE_CODE,CMPL_PROD_TYPE_NAME,t.dim_level  "+
			"         "+
			"          from tbas_dm.d_v_cmpl_prod_condition t   "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('{parentId}','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE  ) c  "+
			"          "+
			" where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and c.dim_level in ({level})  "+
			"  and a.cmpl_prod_type_id like c.cmpl_prod_type_code||'%'  "+
			"  {screen} " +
			"  group by c.CMPL_PROD_TYPE_CODE,  "+
			"           c.CMPL_PROD_TYPE_NAME  " +
			" order by sum(a.COMPLAIN_NUM) desc "+
			"  ) where  rownum <= 5 ";
	
	private String b32_sql =  "select * from ( "+
			" select c.CMPL_BUSI_TYPE_CODE CODE, "+
			"       c.CMPL_BUSI_TYPE_NAME NAME, "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a, "+
			"       meta_dim_zone_custgrp b, "+
			"        (select CMPL_BUSI_TYPE_CODE,CMPL_BUSI_TYPE_NAME,t.dim_level "+
			"        "+
			"          from tbas_dm.d_v_cmpl_business_condition t  "+
			"         start with CMPL_BUSI_TYPE_CODE = nvl('{parentId}','1') "+
			"        connect by prior  CMPL_BUSI_TYPE_CODE=CMPL_BUSI_TYPE_PAR_CODE  ) c "+
			"         "+
			" where a.day_id >= '{start_time}' and a.day_id <='{end_time}' "+
			"  and a.region_id = b.zone_code "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000') "+
			"  and b.dim_level in ('{city_level}') "+
		 	"  {map_screen} " + 
			"  and c.dim_level in ({level}) "+
			"  and A.CMPL_BUSINESS_TYPE_ID like c.CMPL_BUSI_TYPE_CODE||'%' "+
			"  {screen} " +
			"  group by c.CMPL_BUSI_TYPE_CODE, "+
			"           c.CMPL_BUSI_TYPE_NAME "+
			" order by sum(a.COMPLAIN_NUM) desc "+
			"  ) where  rownum <= 5 ";
	
	private String map_sql = "select b.zone_qx NAME,  "+
			"       b.zone_qx_code CODE,  "+
			"         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM,  "+
			"       max(a.NUM1) TOTAL_NUM,  "+
			"       trim(to_char(decode(nvl(max(a.num1),0),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')||'‰') RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') LOOP_RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			"                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			"              0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') SAME_RATIO  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d  "+
			"          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('2')-- 广东省展示3级，其它展示两级  "+
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  group by b.zone_qx,  "+
			"         b.zone_qx_code  "+
			"order by zone_qx_code  ";

	private String b11_sql = "select b.zone_name NAME,    "+
			"       b.zone_code CODE,  "+
			"         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM,  "+
			"       max(a.NUM1) TOTAL_NUM,  "+
			"       trim(to_char(decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')||'‰') RATIA,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') LOOP_RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			"                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			"              0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') SAME_RATIO  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d  "+
			"          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('3')  "+
			"  and b.zone_par_id={par_id}   "+
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  group by b.zone_name,  "+
			"         b.zone_code  "+
			"order by COMPLAIN_NUM desc  ";
	
	private String b12_sql = "select b.ZONE_NAME NAME,  "+
			"       b.ZONE_CODE CODE,  "+
			"         "+
			"       sum(a.COMPLAIN_NUM) COMPLAIN_NUM,  "+
			"       max(a.NUM1) TOTAL_NUM,  "+
			"       trim(to_char(decode(nvl(max(a.num1),0),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')||'‰') RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') LOOP_RATIO,  "+
			"       trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0,  "+
			"                     SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0,  "+
			"              0,DECODE(max(NUM1),0,0,  "+
			"                     ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') SAME_RATIO  "+
			"       from CS_SERV_CMPL_ANALYSIS_DAY a,  "+
			"       meta_dim_zone_custgrp b,  "+
			"       (select CMPL_PROD_TYPE_CODE  "+
			"          from tbas_dm.d_v_cmpl_prod_condition  "+
			"         start with CMPL_PROD_TYPE_CODE = nvl('-1','-1')  "+
			"        connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c,  "+
			"       (select cmpl_busi_type_code  "+
			"          from tbas_dm.d_v_cmpl_business_condition  "+
			"         start with cmpl_busi_type_code = nvl('1','1')  "+
			"        connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d  "+
			"          "+
			"        where a.day_id >= '{start_time}' and a.day_id <='{end_time}'  "+
			"  and a.region_id = b.zone_code  "+
			"  and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000')  "+
			"  and b.dim_level in ('3') "+
			"  and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE  "+
			"  and a.cmpl_business_type_id = d.cmpl_busi_type_code  "+
			"  group by b.ZONE_NAME,  "+
			"         b.zone_code  "+
			"order by sum(a.COMPLAIN_NUM) desc ";
	
	private String b13_sql = "select * from ( "+
			" select b.zone_name NAME,  "+
			"        b.zone_code CODE, "+
			"        sum(a.COMPLAIN_NUM) COMPLAIN_NUM, "+
			"        max(a.NUM1) TOTAL_NUM, "+
			"        trim(to_char(decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM) / max(a.NUM1) * 1000, 2)),'FM99990.00')||'‰') 投诉率, "+
			"        trim(to_char(DECODE(DECODE(nvl(max(HB_NUM1),0),0,0,SUM(HB_COMPLAIN_NUM) / max(HB_NUM1)),0,0,DECODE(max(NUM1),0,0, "+
			"                      ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(HB_COMPLAIN_NUM) /max(HB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') 环比, "+
			"        trim(to_char(DECODE(DECODE(nvl(max(TB_NUM1),0),0,0, "+
			"                      SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)), 0, "+
			"               0,DECODE(max(NUM1),0,0, "+
			"                      ROUND(((SUM(COMPLAIN_NUM) / max(NUM1)) /(SUM(TB_COMPLAIN_NUM) / max(TB_NUM1)) - 1) * 100,2))),'FM99990.00')||'%') 同比 "+
			"        from CS_SERV_CMPL_ANALYSIS_DAY a, "+
			"        meta_dim_zone_custgrp b, "+
			"        (select CMPL_PROD_TYPE_CODE "+
			"           from tbas_dm.d_v_cmpl_prod_condition "+
			"          start with CMPL_PROD_TYPE_CODE = nvl('-1','-1') "+
			"         connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c, "+
			"        (select cmpl_busi_type_code "+
			"           from tbas_dm.d_v_cmpl_business_condition "+
			"          start with cmpl_busi_type_code = nvl('1','1') "+
			"         connect by prior cmpl_busi_type_code = cmpl_busi_type_par_code) d "+
			"          "+
			"         where a.day_id >= '{start_time}' and a.day_id <='{end_time}' "+
			"   and a.region_id = b.zone_code "+
			"   and b.treecode like (select treecode||'%' from meta_dim_zone_custgrp where zone_code = '0000') "+
			"   and b.dim_level in ('4') "+
			"   and b.zone_par_code={code} "+
			"   and a.cmpl_prod_type_id = c.CMPL_PROD_TYPE_CODE "+
			"   and a.cmpl_business_type_id = d.cmpl_busi_type_code "+
			"   group by b.zone_name, "+
			"          b.zone_code "+
			" order by COMPLAIN_NUM  desc" +
			"  ) where  rownum <= 5 ";
}
