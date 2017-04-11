package tydic.reports.customerSatisfied;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;
import tydic.meta.common.yhd.utils.Pager;
import tydic.portalCommon.DateUtil;
import tydic.reports.CommonUtils.CommonUtils;

public class CustomerSatisfiedDAO extends MetaBaseDAO {
	
	/**
	 * 返回不满意工单负责人及去负责营服中心编码
	 * @param zoneCode
	 * @return
	 */
	public List<Map<String,Object>> getChargeManagers(){
		String sql="select city_id,area_id,dept_id,dept_name,staff_code,staff_name,busi_code,SEC_TYPE,busi_name from meta_user.not_statisfy_charge_manager where state='1' ";
		List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
		return list;
	}
	
	public List<Map<String,Object>> getChargeManagersEWAM(){
		String sql="select city_id,city_name,area_id,area_name,dept_id,dept_name,staff_code,staff_name from meta_user.EWAM_CHARGE_MANAGER where state='1' ";
		List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
		return list;
	}
	
	
	//宽带新装5大区
	public List<Map<String, Object>> getBarEveryType_5Zone(String zoneCode){
		String sql="select ZONE_CODE,zone_name from META_DIM_ZONE where level = '2'" +
				" start with ZONE_CODE ='"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code";      
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//按表查询有数据的最大日期_客户满意度监测触点
	public String getMaxDate_Enter(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(V_DATE) from CS_VISIT_RESULT where date_type='0'");
		return getDataAccess().queryForString(buffer.toString());
	}
	//按表查询有数据的最大日期_满意度评测总体情况
	public String getMaxDate_EnterSum(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(V_DATE) from CS_VISIT_KDZYJ_AREA where date_type='0'");
		return getDataAccess().queryForString(buffer.toString());
	}
	
	
	public String getMaxDate_EWAM(String tableName){ //预警监控传最大日期
		StringBuffer buffer = new StringBuffer();
		String tabStr="";
		String sql="";
		tabStr=tableName;
		sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		buffer.append(sql);
		return getDataAccess().queryForString(buffer.toString());
	}
	
	
	//按表查询有数据的最大日期_满意度评测总体情况
	public String getMaxDate_EnterSum(String vTypeId){
		StringBuffer buffer = new StringBuffer();
		String tabStr="";
		String sql="";
		if("0".equals(vTypeId)){
			tabStr="CS_VISIT_KDZYJ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("1".equals(vTypeId)){
			tabStr="CS_VISIT_KDXZ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("2".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("5".equals(vTypeId)){
			tabStr="CS_VISIT_10000_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("6".equals(vTypeId)){
			tabStr="CS_VISIT_WT_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("7".equals(vTypeId)){
			tabStr="CS_VISIT_ZT_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("8".equals(vTypeId)){
			tabStr="CS_VISIT_HB_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("9".equals(vTypeId)){
			tabStr="CS_VISIT_ISHOP_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("10".equals(vTypeId)){
			tabStr="CS_VISIT_DX_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("11".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("12".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("13".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA_1";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("14".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA_1";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("20".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA_OTHER";
			sql="select max(v_date) from CS_VISIT_BUSI_AREA_OTHER where date_type='0' ";
		}if("17".equals(vTypeId)){
			tabStr="CS_VISIT_TS_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("30".equals(vTypeId)){
			tabStr="CS_VISIT_SHQD_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("31".equals(vTypeId)){
			tabStr="CS_VISIT_ZYQD_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		}if("300".equals(vTypeId)){
			  tabStr="CS_VISIT_WX_AREA";
			  sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
	    }if("18".equals(vTypeId)){
			  tabStr="CS_VISIT_VIP_AREA";
			  sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
	    }
		
		buffer.append(sql);
		return getDataAccess().queryForString(buffer.toString());
	}
	
	public String getMaxDate_EWAMSum(String tableName){
		StringBuffer buffer = new StringBuffer();
		String tabStr="";
		String sql="";
		tabStr=tableName;
		sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		buffer.append(sql);
		return getDataAccess().queryForString(buffer.toString());
	}
	
	//按表查询有数据的最大日期_宽带修障、新装
	public String getMaxDate_EveryType(String vTypeId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(V_DATE) from CS_VISIT_RESULT where date_type='0' and V_TYPE_ID='"+vTypeId+"'");
		return getDataAccess().queryForString(buffer.toString());
	}
	//按表查询有数据的最大日期_满意度不满意度原因top
	public String getMaxDate_NoSatisfied(String vTypeId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(V_DATE) from CS_VISIT_RESULT_MAIN where date_type='0' and V_TYPE_ID='"+vTypeId+"'");
		return getDataAccess().queryForString(buffer.toString());
	}
	//按表查询有数据的最大日期_满意度评测总体情况
	public String getMaxDate_ResultSum(String vDateType){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(V_DATE) from CS_VISIT_RESULT_MAIN where date_type='"+vDateType+"'");
		return getDataAccess().queryForString(buffer.toString());
	}
	//按表查询有数据的最大日期_宽带修障、新装_修改版
	public String getMaxDate_EveryTypeMod(String vTypeId){
		String tabStr="";
		  if("0".equals(vTypeId)||"1".equals(vTypeId)){
			  tabStr="CS_VISIT_ZW_CUSTLEVEL";
		  }if("2".equals(vTypeId)){
			  tabStr="CS_VISIT_BUSI_CUSTLEVEL";
		  }if("5".equals(vTypeId)){
			  tabStr="CS_VISIT_10000_CUSTLEVEL";
		  }if("6".equals(vTypeId)){
			  tabStr="CS_VISIT_WT_CUSTLEVEL";
		  }if("7".equals(vTypeId)){
			  tabStr="CS_VISIT_ZT_CUSTLEVEL";
		  }if("8".equals(vTypeId)){
			  tabStr="CS_VISIT_HB_CUSTLEVEL";
		  }if("9".equals(vTypeId)){
			  tabStr="CS_VISIT_ISHOP_CUSTLEVEL";
		  }if("10".equals(vTypeId)){
			  tabStr="CS_VISIT_DX_CUSTLEVEL";
		  }if("20".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_AREA_OTHER";
		  }if("17".equals(vTypeId)){
			  tabStr="CS_VISIT_TS_CUSTLEVEL";
		  }
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(V_DATE) from "+tabStr+" where date_type='0' and V_TYPE_ID='"+vTypeId+"'");
		return getDataAccess().queryForString(buffer.toString());
	}
	//按表查询有数据的最大日期_宽带修障、新装_按维度取数修改版
	public String getMaxDate_EveryTypeDemension(String vTypeId,String demension){
		String tabStr="";
		 if("0".equals(vTypeId)){
				tabStr="CS_VISIT_KDZYJ_AREA";
			}if("1".equals(vTypeId)){
				tabStr="CS_VISIT_KDXZ_AREA";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_AREA";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_AREA";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_AREA";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_AREA";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_AREA";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_AREA";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_AREA";
			}if("17".equals(vTypeId)){
				tabStr="CS_VISIT_TS_AREA";
			}if("30".equals(vTypeId)){
				tabStr="CS_VISIT_SHQD_AREA";
			}if("31".equals(vTypeId)){
				tabStr="CS_VISIT_ZYQD_AREA";
			}if("300".equals(vTypeId)){
				tabStr="CS_VISIT_WX_AREA";
		    }if("18".equals(vTypeId)){
				tabStr="CS_VISIT_VIP_AREA";
		    }
			
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(V_DATE) from "+tabStr+" where date_type='0' and V_TYPE_ID='"+vTypeId+"'");
		return getDataAccess().queryForString(buffer.toString());
	}
	//宽带新装21地市循环
	public List<Map<String, Object>> getBarEveryType_21Zone(String zoneCode){
		String sql="select ZONE_CODE,zone_name from META_DIM_ZONE where level = '3'" +
		" start with ZONE_CODE ='0000' connect by prior ZONE_CODE = zone_par_code"; 
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//按照查询周查询月和周
	public Map<String, Object> queryMonthAndWeek(String WeekMon) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_ID,WEEK_ID from (select distinct MONTH_ID,WEEK_ID from CS_VISIT_RESULT where V_DATE=? and DATE_TYPE='1') order by WEEK_ID desc ");
		return getDataAccess().queryForMap(buffer.toString(), WeekMon);
	}
	//按照触点id动态获取时间列表
	public Map<String, Object> getDateListByIndex(Map<String, Object> queryData) {
		String dateType=MapUtils.getString(queryData, "dateType", null);
		String vTypeId=MapUtils.getString(queryData, "vTypeId", null);
		StringBuffer buffer = new StringBuffer();
		String tabStr="";
		if("0".equals(vTypeId)){
			tabStr="CS_VISIT_KDZYJ_AREA";
		}if("1".equals(vTypeId)){
			tabStr="CS_VISIT_KDXZ_AREA";
		}if("2".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA";
		}if("5".equals(vTypeId)){
			tabStr="CS_VISIT_10000_AREA";
		}if("6".equals(vTypeId)){
			tabStr="CS_VISIT_WT_AREA";
		}if("7".equals(vTypeId)){
			tabStr="CS_VISIT_ZT_AREA";
		}if("8".equals(vTypeId)){
			tabStr="CS_VISIT_HB_AREA";
		}if("9".equals(vTypeId)){
			tabStr="CS_VISIT_ISHOP_AREA";
		}if("10".equals(vTypeId)){
			tabStr="CS_VISIT_DX_AREA";
		}if("20".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA_OTHER";
		}if("17".equals(vTypeId)){
			tabStr="CS_VISIT_TS_AREA ";
		}if("30".equals(vTypeId)){
			tabStr="CS_VISIT_SHQD_AREA";
		}if("31".equals(vTypeId)){
			tabStr="CS_VISIT_ZYQD_AREA";
		}if("300".equals(vTypeId)){
			tabStr="CS_VISIT_WX_AREA";
	    }if("18".equals(vTypeId)){
			tabStr="CS_VISIT_VIP_AREA";
	    }
		buffer.append("select distinct V_DATE as DATE_NO from "+tabStr+" where DATE_TYPE='"+dateType+"' " +
				" order by V_DATE desc");
		return getDataAccess().queryForMap(buffer.toString());
	}
	//按照查询周查询月和周_修改版
	public Map<String, Object> queryMonthAndWeekMod(Map<String, Object> queryData) {
		String WeekMon=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
		String actType=MapUtils.getString(queryData, "actType", null);
		  String tabStr="";
		  if("0".equals(actType)||"1".equals(actType)){
			  tabStr="CS_VISIT_ZW_CUSTLEVEL";
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_CUSTLEVEL";
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_CUSTLEVEL";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_CUSTLEVEL";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_CUSTLEVEL";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_CUSTLEVEL";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_CUSTLEVEL";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_CUSTLEVEL";
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_CUSTLEVEL";
		  }
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_ID,WEEK_ID from (select distinct MONTH_ID,WEEK_ID from "+tabStr+" where V_DATE=? and DATE_TYPE='1') order by WEEK_ID desc ");
		return getDataAccess().queryForMap(buffer.toString(), WeekMon);
	}
	//按照查询周查询月和周_修改版
	public Map<String, Object> queryMonthAndWeekSum(Map<String, Object> queryData) {
		String WeekMon=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");
		String actType=MapUtils.getString(queryData, "actType", null);
		  String tabStr="";
		  if("0".equals(actType)){
			  tabStr="CS_VISIT_KDZYJ_AREA";
		  }if("1".equals(actType)){
			  tabStr="CS_VISIT_KDXZ_AREA";
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA";
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_AREA";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_AREA";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_AREA";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_AREA";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_AREA";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_AREA";
		  }if("11".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA";
		  }if("12".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA";
		  }if("13".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA_1";
		  }if("14".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA_1";	  
		  }if("20".equals(actType)){
				tabStr="CS_VISIT_BUSI_AREA_OTHER";
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_AREA ";
		  }if("30".equals(actType)){
				tabStr="CS_VISIT_SHQD_AREA";
		  }if("31".equals(actType)){
				tabStr="CS_VISIT_ZYQD_AREA";
		  }if("300".equals(actType)){
			  tabStr="CS_VISIT_WX_AREA";
	      }if("18".equals(actType)){
			  tabStr="CS_VISIT_VIP_AREA";
	      }
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_ID,WEEK_ID from (select distinct MONTH_ID,WEEK_ID from "+tabStr+" where V_DATE=? and DATE_TYPE='1') order by WEEK_ID desc ");
		return getDataAccess().queryForMap(buffer.toString(), WeekMon);
	}
	//按月查询最大周
	public String queryMaxWeek(String WeekMon) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(WEEK_ID) from CS_VISIT_RESULT where MONTH_ID=? ");
		return getDataAccess().queryForString(buffer.toString(), WeekMon);
	}
	//满意度详细报表周,月列表
	public List<Map<String, Object>>getDateTimeList_EveryType(String dateType,String vTypeId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct V_DATE as DATE_NO from CS_VISIT_RESULT where DATE_TYPE='"+dateType+"' and V_TYPE_ID='"+vTypeId+"'" +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//满意度详细报表周,月列表_修改版
	public List<Map<String, Object>>getDateTimeList_EveryTypeMod(String dateType,String vTypeId){
		StringBuffer buffer = new StringBuffer();
		String tabStr="";
		  if("0".equals(vTypeId)||"1".equals(vTypeId)){
			  tabStr="CS_VISIT_ZW_CUSTLEVEL";
		  }if("2".equals(vTypeId)){
			  tabStr="CS_VISIT_BUSI_CUSTLEVEL";
		  }if("5".equals(vTypeId)){
			  tabStr="CS_VISIT_10000_CUSTLEVEL";
		  }if("6".equals(vTypeId)){
			  tabStr="CS_VISIT_WT_CUSTLEVEL";
		  }if("7".equals(vTypeId)){
			  tabStr="CS_VISIT_ZT_CUSTLEVEL";
		  }if("8".equals(vTypeId)){
			  tabStr="CS_VISIT_HB_CUSTLEVEL";
		  }if("9".equals(vTypeId)){
			  tabStr="CS_VISIT_ISHOP_CUSTLEVEL";
		  }if("10".equals(vTypeId)){
			  tabStr="CS_VISIT_DX_CUSTLEVEL";
		  }if("17".equals(vTypeId)){
			  tabStr="CS_VISIT_TS_CUSTLEVEL";
		  }
		buffer.append("select distinct V_DATE as DATE_NO from "+tabStr+" where DATE_TYPE='"+dateType+"' and V_TYPE_ID='"+vTypeId+"'" +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//满意度详细报表周,月列表_按维度取数修改版
	public List<Map<String, Object>>getDateTimeList_EveryTypeDemension(String dateType,String vTypeId,String demension){
		StringBuffer buffer = new StringBuffer();
		String tabStr="";
		/*if("0".equals(demension)){//客户在网时长
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_ONLINE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_ONLINE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_ONLINE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_ONLINE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_ONLINE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_ONLINE";
			}	
		}if("1".equals(demension)){//受理渠道
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CHANNEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CHANNEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CHANNEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CHANNEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CHANNEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CHANNEL";
			}	
		}if("2".equals(demension)){//客户群长
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CUSTGROUP";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CUSTGROUP";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CUSTGROUP";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CUSTGROUP";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CUSTGROUP";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CUSTGROUP";
			}
		}if("3".equals(demension)){//客户等级
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CUSTLEVEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CUSTLEVEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CUSTLEVEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CUSTLEVEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CUSTLEVEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CUSTLEVEL";
			}	
		}if("4".equals(demension)){//ARPU值
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_ARPULEVEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_ARPULEVEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_ARPULEVEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_ARPULEVEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_ARPULEVEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_ARPULEVEL";
			}
		}if("5".equals(demension)){//套餐类型
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CDMATYPE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CDMATYPE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CDMATYPE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CDMATYPE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CDMATYPE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CDMATYPE";
			}
		}if("6".equals(demension)){//服务人员
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_DUTYOPTR";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_DUTYOPTR";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_DUTYOPTR";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_DUTYOPTR";
			}
		}if("7".equals(demension)){//测评方式
			if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_TMETHOD";
			}
			if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_TMETHOD";
			}
		}if("8".equals(demension)){//业务类型
			if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_BUSINESS";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_BUSINESS";
			}
		}if("9".equals(demension)){//产品类型
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_PRODTYPE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_PRODTYPE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_PRODTYPE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_PRODTYPE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_PRODTYPE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_PRODTYPE";
			}	
		}if("10".equals(demension)){//终端类型
			if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_TERMINALTYPE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_TERMINALTYPE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_TERMINALTYPE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_TERMINALTYPE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_TERMINALTYPE";
			}	
		}*/
		 if("0".equals(vTypeId)){
			  tabStr="CS_VISIT_KDZYJ_AREA";
		  }if("1".equals(vTypeId)){
			  tabStr="CS_VISIT_KDXZ_AREA";
		  }if("2".equals(vTypeId)){
			  tabStr="CS_VISIT_BUSI_AREA";
		  }if("5".equals(vTypeId)){
			  tabStr="CS_VISIT_10000_AREA";
		  }if("6".equals(vTypeId)){
			  tabStr="CS_VISIT_WT_AREA";
		  }if("7".equals(vTypeId)){
			  tabStr="CS_VISIT_ZT_AREA";
		  }if("8".equals(vTypeId)){
			  tabStr="CS_VISIT_HB_AREA";
		  }if("9".equals(vTypeId)){
			  tabStr="CS_VISIT_ISHOP_AREA";
		  }if("10".equals(vTypeId)){
			  tabStr="CS_VISIT_DX_AREA";
		  }if("17".equals(vTypeId)){
			  tabStr="CS_VISIT_TS_AREA ";
		  }if("30".equals(vTypeId)){
				tabStr="CS_VISIT_SHQD_AREA";
		  }if("31".equals(vTypeId)){
				tabStr="CS_VISIT_ZYQD_AREA";
		  }if("300".equals(vTypeId)){
			  tabStr="CS_VISIT_WX_AREA";
	      }if("18".equals(vTypeId)){
			  tabStr="CS_VISIT_VIP_AREA";
	      }
		buffer.append("select distinct V_DATE as DATE_NO from "+tabStr+" where DATE_TYPE='"+dateType+"' and V_TYPE_ID='"+vTypeId+"'" +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//满意度评测总体情况周,月列表
	public List<Map<String, Object>>getDateTimeList_ResultSum(String dateType){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct V_DATE as DATE_NO from CS_VISIT_RESULT_MAIN where DATE_TYPE='"+dateType+"'" +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//满意度详细报表_最大周，月
	public String getMaxWeek_EveryType(String dateType,String vTypeId) {
		String sql = "select rownum,V_DATE from (select distinct V_DATE from CS_VISIT_RESULT " +
		          "where V_DATE is not null and DATE_TYPE='"+dateType+"' and V_TYPE_ID='"+vTypeId+"' order by V_DATE desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//满意度详细报表_最大周，月_修改版
	public String getMaxWeek_EveryTypeMod(String dateType,String vTypeId) {
		String tabStr="";
		  if("0".equals(vTypeId)||"1".equals(vTypeId)){
			  tabStr="CS_VISIT_ZW_CUSTLEVEL";
		  }if("2".equals(vTypeId)){
			  tabStr="CS_VISIT_BUSI_CUSTLEVEL";
		  }if("5".equals(vTypeId)){
			  tabStr="CS_VISIT_10000_CUSTLEVEL";
		  }if("6".equals(vTypeId)){
			  tabStr="CS_VISIT_WT_CUSTLEVEL";
		  }if("7".equals(vTypeId)){
			  tabStr="CS_VISIT_ZT_CUSTLEVEL";
		  }if("8".equals(vTypeId)){
			  tabStr="CS_VISIT_HB_CUSTLEVEL";
		  }if("9".equals(vTypeId)){
			  tabStr="CS_VISIT_ISHOP_CUSTLEVEL";
		  }if("10".equals(vTypeId)){
			  tabStr="CS_VISIT_DX_CUSTLEVEL";
		  }if("17".equals(vTypeId)){
			  tabStr="CS_VISIT_TS_CUSTLEVEL";
		  }
		String sql = "select rownum,V_DATE from (select distinct V_DATE from "+tabStr +
		          " where V_DATE is not null and DATE_TYPE='"+dateType+"' and V_TYPE_ID='"+vTypeId+"' order by V_DATE desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//满意度详细报表_最大周，月_按维度取数修改版
	public String getMaxWeek_EveryTypeDemension(String dateType,String vTypeId,String demension) {
		String tabStr="";
		/*if("0".equals(demension)){//客户在网时长
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_ONLINE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_ONLINE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_ONLINE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_ONLINE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_ONLINE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_ONLINE";
			}	
		}if("1".equals(demension)){//受理渠道
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CHANNEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CHANNEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CHANNEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CHANNEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CHANNEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CHANNEL";
			}	
		}if("2".equals(demension)){//客户群长
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CUSTGROUP";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CUSTGROUP";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CUSTGROUP";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CUSTGROUP";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CUSTGROUP";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CUSTGROUP";
			}
		}if("3".equals(demension)){//客户等级
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CUSTLEVEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CUSTLEVEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CUSTLEVEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CUSTLEVEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CUSTLEVEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CUSTLEVEL";
			}	
		}if("4".equals(demension)){//ARPU值
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_ARPULEVEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_ARPULEVEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_ARPULEVEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_ARPULEVEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_ARPULEVEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_ARPULEVEL";
			}
		}if("5".equals(demension)){//套餐类型
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CDMATYPE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CDMATYPE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CDMATYPE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CDMATYPE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CDMATYPE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CDMATYPE";
			}
		}if("6".equals(demension)){//服务人员
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_DUTYOPTR";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_DUTYOPTR";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_DUTYOPTR";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_DUTYOPTR";
			}
		}if("7".equals(demension)){//测评方式
			if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_TMETHOD";
			}
			if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_TMETHOD";
			}
		}if("8".equals(demension)){//业务类型
			if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_BUSINESS";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_BUSINESS";
			}
		}if("9".equals(demension)){//产品类型
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_PRODTYPE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_PRODTYPE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_PRODTYPE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_PRODTYPE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_PRODTYPE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_PRODTYPE";
			}	
		}if("10".equals(demension)){//终端类型
			if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_TERMINALTYPE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_TERMINALT";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_TERMINALTYPE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_TERMINALTYPE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_TERMINALTYPE";
			}	
		}*/
		    if("0".equals(vTypeId)){
				tabStr="CS_VISIT_KDZYJ_AREA";
			}if("1".equals(vTypeId)){
				tabStr="CS_VISIT_KDXZ_AREA";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_AREA";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_AREA";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_AREA";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_AREA";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_AREA";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_AREA";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_AREA";
			}if("17".equals(vTypeId)){
				tabStr="CS_VISIT_TS_AREA ";
			}if("30".equals(vTypeId)){
				tabStr="CS_VISIT_SHQD_AREA";
			}if("31".equals(vTypeId)){
				tabStr="CS_VISIT_ZYQD_AREA";
			}if("300".equals(vTypeId)){
				tabStr="CS_VISIT_WX_AREA";
		    }if("18".equals(vTypeId)){
				tabStr="CS_VISIT_VIP_AREA";
		    }
		/*String sql = "select rownum,V_DATE from (select distinct V_DATE from "+tabStr +
		          " where V_DATE is not null and DATE_TYPE='"+dateType+"' and V_TYPE_ID='"+vTypeId+"' order by V_DATE desc) where rownum=1";*/
			String sql="select  max(V_DATE) V_DATE from "+tabStr+" where V_DATE is not null and DATE_TYPE='"+dateType+"' and V_TYPE_ID='"+vTypeId+"'";
		return getDataAccess().queryForString(sql);
	}
	//客户满意率报表_最大周，月
	public String getMaxDateTime_CustomerSatisfied(String dateType) {
		String sql = "select rownum,V_DATE from (select distinct V_DATE from CS_VISIT_RESULT " +
		          "where V_DATE is not null and DATE_TYPE='"+dateType+"' order by V_DATE desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//满意度评测总体情况_最大周，月
	public String getMaxDateTime_CustomerSatisfiedSum(String dateType) {
		String sql = "select rownum,V_DATE from (select distinct V_DATE from CS_VISIT_KDZYJ_AREA " +
		          "where V_DATE is not null and DATE_TYPE='"+dateType+"' order by V_DATE desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//满意度总体情况最大日期
	public String getNewDay(String tabStr) {
		String sql = "select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual ";
		return getDataAccess().queryForString(sql);
	}
	//满意度评测总体情况_最大周，月
	public String getMaxDateTime_CustomerSatisfiedSum(String dateType,String vTypeId) {
		String tabStr="";
		Integer tempNum=0;
		String sql="";
		if("1".equals(dateType)){
			tempNum=17;
		}else if("2".equals(dateType)){
			tempNum=8;
		}
		if("0".equals(vTypeId)){
			tabStr="CS_VISIT_KDZYJ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("1".equals(vTypeId)){
			tabStr="CS_VISIT_KDXZ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("2".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("5".equals(vTypeId)){
			tabStr="CS_VISIT_10000_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("51".equals(vTypeId)){
			tabStr="CS_VISIT_10000_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("6".equals(vTypeId)){
			tabStr="CS_VISIT_WT_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("7".equals(vTypeId)){
			tabStr="CS_VISIT_ZT_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("8".equals(vTypeId)){
			tabStr="CS_VISIT_HB_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("9".equals(vTypeId)){
			tabStr="CS_VISIT_ISHOP_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("10".equals(vTypeId)){
			tabStr="CS_VISIT_DX_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("11".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("12".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("13".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA_1";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("14".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA_1";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if(vTypeId.equals("20")){ //表示非实体营业
			tabStr="CS_VISIT_BUSI_AREA_OTHER";
			sql="select max(v_date) from CS_VISIT_BUSI_AREA_OTHER where date_type='"+dateType+"'";
		}if("17".equals(vTypeId)){
			tabStr="CS_VISIT_TS_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("30".equals(vTypeId)){
			tabStr="CS_VISIT_SHQD_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("31".equals(vTypeId)){
			tabStr="CS_VISIT_ZYQD_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("300".equals(vTypeId)){
			tabStr="CS_VISIT_WX_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("18".equals(vTypeId)){
			tabStr="CS_VISIT_VIP_AREA";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}if("19".equals(vTypeId)){
			tabStr="CS_VISIT_EWAM_RESULT";
			sql="select GET_MAX_PARTITION_DATE('"+tabStr+"',"+tempNum+") from dual ";
		}
		return getDataAccess().queryForString(sql);
	}
	//满意度评测总体情况_最大周，月
	public String getMaxDateTime_CustomerSatisfiedDetails(String dateType,String vTypeId) {
		String tabStr="";
		if("0".equals(vTypeId)){
			tabStr="CS_VISIT_KDZYJ_AREA";
		}if("1".equals(vTypeId)){
			tabStr="CS_VISIT_KDXZ_AREA";
		}if("2".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA";
		}if("5".equals(vTypeId)){
			tabStr="CS_VISIT_10000_AREA";
		}if("6".equals(vTypeId)){
			tabStr="CS_VISIT_WT_AREA";
		}if("7".equals(vTypeId)){
			tabStr="CS_VISIT_ZT_AREA";
		}if("8".equals(vTypeId)){
			tabStr="CS_VISIT_HB_AREA";
		}if("9".equals(vTypeId)){
			tabStr="CS_VISIT_ISHOP_AREA";
		}if("10".equals(vTypeId)){
			tabStr="CS_VISIT_DX_AREA";
		}if("20".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA_OTHER";
		}if("17".equals(vTypeId)){
			tabStr="CS_VISIT_TS_AREA";
		}if("30".equals(vTypeId)){
			tabStr="CS_VISIT_SHQD_AREA";
		}if("31".equals(vTypeId)){
			tabStr="CS_VISIT_ZYQD_AREA";
		}if("300".equals(vTypeId)){
			tabStr="CS_VISIT_WX_AREA";
		}if("18".equals(vTypeId)){
			tabStr="CS_VISIT_VIP_AREA";
		}
		String sql = "select rownum,V_DATE from (select distinct V_DATE from "+tabStr +
		          "where V_DATE is not null and DATE_TYPE='"+dateType+"' order by V_DATE desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}//客户满意度触点列表
	public List<Map<String, Object>> getVTypeList() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct V_TYPE_ID,V_TYPE_NAME from CS_VISIT_RESULT_MAIN " +
				" order by case when V_TYPE_ID='2' then 1 when V_TYPE_ID='5' then 2 when V_TYPE_ID='8' then 3 when V_TYPE_ID='6' then 4 when V_TYPE_ID='7' then 5 " +
	    		"when V_TYPE_ID='0' then 6  when V_TYPE_ID='1' then 7 end");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getUserSubCode(Map<String, Object> queryData){
		String userCode=MapUtils.getString(queryData, "userCode", null);
		StringBuffer buffer = new StringBuffer();
		buffer.append("select zone_id,ZONE_CODE,DIM_LEVEL,ORDER_ID from meta_dim_zone_branch start with ZONE_CODE ='"+userCode+"' " +
						"connect by prior ZONE_CODE = zone_par_code");
		return getDataAccess().queryForList(buffer.toString());
	}


	/**
	 * 根据负责人所在营销服务中心推送不满意清单
	 * @param queryData
	 * @return
	 */
	public List<Map<String,Object>>getMessList(Map<String,Object>queryData,String dept_id,String busi_code,String sec_type){
		String startTime=MapUtils.getString(queryData, "startTime",null).replaceAll("-", "");
		String endTime=MapUtils.getString(queryData, "endTime",null).replaceAll("-", "");
		String zoneCode=MapUtils.getString(queryData, "zoneCode","0000");
		String orderId=MapUtils.getString(queryData, "orderId","");
		sec_type=sec_type==null||"".equals(sec_type)?"null":sec_type;
		StringBuffer buffer=new StringBuffer();
		buffer.append("select distinct UNIQUE_VALUE,BRANCH_ID from CS_RESULT_NOSATIS_TS a,(select zone_code from meta_dim_zone_branch start with " +
				"zone_code='0000' connect by prior zone_code=zone_par_code)b where a.region_id=b.zone_code and v_type_id='"+busi_code+"' and a.branch_treecode like '%"+dept_id+"%' " + // and a.branch_treecode like '%"+dept_id+"%'
				" and ("+sec_type+" is null or IS_CORRECT='"+sec_type+"') and DAY_ID>='"+startTime+"' and DAY_ID<='"+endTime+"'");
		if(!"".equals(orderId)&&orderId!=""){
			buffer.append(" and UNIQUE_VALUE='"+orderId+"'");
      	}
		return getDataAccess().queryForList(buffer.toString());
	}
	
	/**
	 * 根据负责人所在营销服务中心推送清单
	 * @param queryData
	 * @return
	 */
	public List<Map<String,Object>>getEwamMessList(Map<String,Object>queryData,String dept_id){
		String startTime=MapUtils.getString(queryData, "startTime",null).replaceAll("-", "");
		String endTime=MapUtils.getString(queryData, "endTime",null).replaceAll("-", "");
//		String zoneCode=MapUtils.getString(queryData, "zoneCode","0000");
//		String orderId=MapUtils.getString(queryData, "orderId","");
		//sec_type=sec_type==null||"".equals(sec_type)?"null":sec_type;
		StringBuffer buffer=new StringBuffer();
		buffer.append("select * from meta_user.CS_VISIT_EWAM_TS a where a.v_date >= '"
				+startTime+"' and a.v_date <='"
				+endTime+"' and a.branch_treecode = '"
				+dept_id+"'"
				+"and a.push_flag is null and rownum <= 1");
//		if(!"".equals(orderId)&&orderId!=""){
//			buffer.append(" and UNIQUE_VALUE='"+orderId+"'");
//      	}
		return getDataAccess().queryForList(buffer.toString());
	}
	
	public String getEwamMessCount(Map<String,Object>queryData,String dept_id){
		String startTime=MapUtils.getString(queryData, "startTime",null).replaceAll("-", "");
		String endTime=MapUtils.getString(queryData, "endTime",null).replaceAll("-", "");
//		String zoneCode=MapUtils.getString(queryData, "zoneCode","0000");
//		String orderId=MapUtils.getString(queryData, "orderId","");
		//sec_type=sec_type==null||"".equals(sec_type)?"null":sec_type;
		StringBuffer buffer=new StringBuffer();
		buffer.append("select count(1) from meta_user.CS_VISIT_EWAM_TS a where a.v_date >= '"
				+startTime+"' and a.v_date <='"
				+endTime+"' and a.branch_treecode = '"
				+dept_id+"'");

//		if(!"".equals(orderId)&&orderId!=""){
//			buffer.append(" and UNIQUE_VALUE='"+orderId+"'");
//      	}
		return getDataAccess().queryForString(buffer.toString());
	}

	/**
	 * 更新推送时间和计划完成时间
	 * @param create_time
	 */
	public void updatePushDate(String dataTime,String create_time){
		try {
		Date curr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create_time);
		String tomorror= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(curr.getTime() + 1 * 24 * 60 * 60 * 1000));  
		
		String sql=" update CS_RESULT_NOSATIS_TS set push_date='"+create_time+"',plan_date='"+tomorror+"' " +
				"where day_id='"+dataTime+"'";

		getDataAccess().execUpdate(sql);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新推送时间和计划完成时间
	 * @param create_time
	 */
	public void updateEwamPushDate(String dataTime,String create_time){
		try {
		Date curr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create_time);
		String tomorror= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(curr.getTime() + 1 * 24 * 60 * 60 * 1000));  
		
		String sql=" update CS_VISIT_EWAM_TS set push_date='"+create_time+"',plan_date='"+tomorror+"' " +
				"where v_date='"+dataTime+"'";

		getDataAccess().execUpdate(sql);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//客户满意率监测报表_周，月列表
	public List<Map<String, Object>> getDateTimeList(String dateType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct V_DATE as DATE_NO from CS_VISIT_RESULT where DATE_TYPE='"+dateType+"' " +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//满意度评测总体情况_周，月列表
	public List<Map<String, Object>> getDateTimeListSum(String dateType) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select distinct V_DATE as DATE_NO from CS_VISIT_VIP_AREA where DATE_TYPE='"+dateType+"' " +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//满意度评测总体情况_周，月列表
	public List<Map<String, Object>> getDateListSum(String dateType) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select distinct V_DATE as DATE_NO from CS_VISIT_ZWIVR_NOSATIS_SUM where DATE_TYPE='"+dateType+"' " +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getDateListSum10000(String dateType) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select distinct V_DATE as DATE_NO from CS_VISIT_10000_NOSATIS where DATE_TYPE='"+dateType+"' " +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getDateTimeList10000(String dateType) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select distinct V_DATE as DATE_NO from CS_VISIT_10000_AREA where DATE_TYPE='"+dateType+"' " +
				" order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//满意度评测详情_周，月列表
	public List<Map<String, Object>> getDateTimeListSum(String dateType,String vTypeId) {
		String tabStr="";
		String sql="";
		Integer tempNum=0;
		if("1".equals(dateType)){
			tempNum=17;
		}else if("2".equals(dateType)){
			tempNum=6;
		}
		if("0".equals(vTypeId)){
			tabStr="CS_VISIT_KDZYJ_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
					"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("1".equals(vTypeId)){
			tabStr="CS_VISIT_KDXZ_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("2".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("5".equals(vTypeId)){
			tabStr="CS_VISIT_10000_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("6".equals(vTypeId)){
			tabStr="CS_VISIT_WT_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("7".equals(vTypeId)){
			tabStr="CS_VISIT_ZT_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("8".equals(vTypeId)){
			tabStr="CS_VISIT_HB_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("9".equals(vTypeId)){
			tabStr="CS_VISIT_ISHOP_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("10".equals(vTypeId)){
			tabStr="CS_VISIT_DX_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("11".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("12".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("13".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA_1";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("14".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA_1";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}
		if("20".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA_OTHER";
			sql="select distinct v_date DATE_NO from CS_VISIT_BUSI_AREA_OTHER where date_type='"+dateType+"' order by v_date desc";
		}if("17".equals(vTypeId)){
			tabStr="CS_VISIT_TS_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("30".equals(vTypeId)){
			tabStr="CS_VISIT_SHQD_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("31".equals(vTypeId)){
			tabStr="CS_VISIT_ZYQD_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("300".equals(vTypeId)){
			tabStr="CS_VISIT_WX_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}if("18".equals(vTypeId)){
			tabStr="CS_VISIT_VIP_AREA";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}
		if("19".equals(vTypeId)){
			tabStr="CS_VISIT_EWAM_RESULT";
			sql="select distinct substr(partition_name, 2) DATE_NO from user_Segments " +
			"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by DATE_NO desc ";
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		return getDataAccess().queryForList(buffer.toString());
	}
	   //客户满意率监测报表_表格数据
	public Map<String, Object> getCustomerSatisfied_Week(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_RATIO;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "zoneCode", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //满意度评测总体情况_表格数据
	public Map<String, Object> getCustomerSatisfied_Sum(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String isIshop = Convert.toString(queryData.get("isIshop"));
		String storeName="";
		if(isIshop.equals("1")||isIshop=="1"){
			storeName = ConstantStoreProc.RPT_VISIT_SATISFY_ALL_TT;
		}else{
			storeName = ConstantStoreProc.RPT_VISIT_SATISFY_ALL;
		}
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				"1",
				"1",
			    "15",
			    new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	//客服部关键指标满意率报表
	public Map<String, Object> getCustKeyIndex_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_KEY_DATA_LISTING;
		Object[] params = {
				MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	
	//满意度评测详情总体报表
	public Map<String, Object> getCustomerSatisfiedDetails_Sum(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName =null;
		Object[] params = null;
		BigDecimal sqlSeq;
		if("20".equals(queryData.get("actType"))){
			 storeName = ConstantStoreProc.RPT_VISIT_SATISFY_AREA_OTHER;
			  params = new Object[]{ MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
						MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(queryData, "zoneCode", null),
						MapUtils.getString(queryData, "dateType", null),
						"2",
						"1",
					    "115",
					    new DBOutParameter(OracleTypes.INTEGER),
					    new DBOutParameter(OracleTypes.NUMBER),
						new DBOutParameter(OracleTypes.CURSOR)
			  			};
		}else {
			if("ishop1".equals(queryData.get("rptId")))storeName = "RPT_ISHOP_SATISFY_AREA";
			else if("ivrzw".equals(queryData.get("rptId")))storeName = "RPT_IVR_ZW_SATISFY_AREA";
			else storeName = "RPT_VISIT_SATISFY_AREA_TT";
			params = new Object[]{ MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				    MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),
					MapUtils.getString(queryData, "dateType", null),
					//MapUtils.getString(queryData, "typeId",   null),
					MapUtils.getString(queryData, "actType", null),
					MapUtils.getString(queryData, "rptId", null),
					"1",
				    "115",
				    new DBOutParameter(OracleTypes.INTEGER),
				    new DBOutParameter(OracleTypes.NUMBER),
				    new DBOutParameter(OracleTypes.CURSOR)
		  			};
		}
		 
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			sqlSeq=  (BigDecimal) statement.getObject(len-1);
			String sqlStr= CommonUtils.clobToString(getDataAccess().queryForObject("select LOGDESC from SYS_PUB_LOG_TEST where seq=?",Clob.class, sqlSeq));

			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
			mapList.put("sqlStr", sqlStr);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	public Map<String, Object> getCustomerSatisfiedDetails_Sum_Dg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_AREA_DG;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				"1",
			    "115",
			    new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);

			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	//客户满意度监测柱状图
	public List<Map<String, Object>> getBarCustomerSatisfied(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		String dateType =Convert.toString(queryData.get("dateType"));
		String zoneCode =Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql = new StringBuffer("select a.V_TYPE_ID,a.V_TYPE_NAME,decode(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num " +
				"when v_type_id<>'3' then v_succ_num-satisfy_num end)+sum(satisfy_num),0,0,round((sum(a.SATISFY_NUM)/(sum(case when v_type_id='3'and is_satisfy='1'" +
				" then v_succ_num when v_type_id<>'3' then v_succ_num-satisfy_num end)+sum(satisfy_num)) )*100,2))SATISFY_LV" +
				" from CS_VISIT_RESULT a,(select zone_id,ZONE_CODE,DIM_LEVEL,ORDER_ID from META_DIM_ZONE start with ZONE_CODE ='"+zoneCode+"' " +
						"connect by prior ZONE_CODE = zone_par_code)b where a.REGION_ID = b.ZONE_CODE and a.V_DATE='"+dateTime+"' and DATE_TYPE='"+dateType+"'");
	    sql.append(" group by a.V_TYPE_ID,a.V_TYPE_NAME ");
	    sql.append(" order by a.V_TYPE_ID");
        List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	    return list;
	}
	//满意度评测总体情况柱状图
	public List<Map<String, Object>> getBarCustomerSatisfiedSum(Map<String, Object> queryData){
		String startDate = Convert.toString(queryData.get("startDate")).replaceAll("-", "");
		String endDate = Convert.toString(queryData.get("endDate")).replaceAll("-", "");
		String dateType =Convert.toString(queryData.get("dateType"));
		String zoneCode =Convert.toString(queryData.get("zoneCode"));
		String isIshop =Convert.toString(queryData.get("isIshop"));
		String tempStr="";
		if(isIshop.equals("1")||isIshop=="1"){
			tempStr=" union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_ISHOP_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  " +
						"and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' " +
								"group by a.V_TYPE_ID,a.V_TYPE_NAME" +
			 " union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_DX_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  " +
						"and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' " +
								"group by a.V_TYPE_ID,a.V_TYPE_NAME";
		}else{
			tempStr=" union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_ISHOP_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' group by a.V_TYPE_ID,a.V_TYPE_NAME";
		}
		StringBuffer sql = new StringBuffer("select a.V_TYPE_ID,a.V_TYPE_NAME,DECODE(v_succ_num,0,0," +
				"round(v_satisfy_num/v_succ_num*100,2)) SATISFY_LV from (select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num)  v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num "+
				" from CS_VISIT_IVRZYJ_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"' and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' group by a.V_TYPE_ID,a.V_TYPE_NAME" +
								" union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_IVRXZ_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' group by a.V_TYPE_ID,a.V_TYPE_NAME" +
						" union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_10000_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' group by a.V_TYPE_ID,a.V_TYPE_NAME" +
						" union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_WT_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' group by a.V_TYPE_ID,a.V_TYPE_NAME" +
						" union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_ZT_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' group by a.V_TYPE_ID,a.V_TYPE_NAME" +
						" union all select a.V_TYPE_ID,a.V_TYPE_NAME,sum(v_succ_num) v_succ_num, sum(very_satisfy_num+satisfy_num)  v_satisfy_num " +
				" from CS_VISIT_HB_AREA a,meta_dim_zone_branch b where a.REGION_ID = b.ZONE_CODE and ZONE_CODE = '"+zoneCode+"'  and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' and DATE_TYPE='"+dateType+"' group by a.V_TYPE_ID,a.V_TYPE_NAME"+tempStr+")a ");
	    sql.append(" order by case when V_TYPE_ID='10' then 1 when V_TYPE_ID='2' then 2 when V_TYPE_ID='9' then 3 when V_TYPE_ID='11' then 4  when V_TYPE_ID='12' then 5 when V_TYPE_ID='5' then 6 when V_TYPE_ID='8' then 7 when V_TYPE_ID='6' then 8 when V_TYPE_ID='7' then 9 end ");
        List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	    return list;
	}
	//客户满意度监测_上周(月，日)数据
	public  Map<String,Object> getChartData_CustomerSatisfied(String dateTime, Map<String,Object> map) {
		String vTypeId = Convert.toString(map.get("vTypeId"));
		String dateType =Convert.toString(map.get("dateType"));
		String zoneCode =Convert.toString(map.get("zoneCode"));
		StringBuffer sql = new StringBuffer("select decode(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id<>'3' then " +
				"v_succ_num-satisfy_num end)+sum(satisfy_num),0,0,round((sum(a.SATISFY_NUM)/(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id<>'3'" +
				" then v_succ_num-satisfy_num end)+sum(satisfy_num)) )*100,2))SATISFY_LV" +
				" from CS_VISIT_RESULT a,(select zone_id,ZONE_CODE,DIM_LEVEL,ORDER_ID from META_DIM_ZONE start with ZONE_CODE ='"+zoneCode+"' " +
						"connect by prior ZONE_CODE = zone_par_code)b where a.REGION_ID = b.ZONE_CODE and a.V_DATE='"+dateTime+"' and a.V_TYPE_ID='"+vTypeId+"' and DATE_TYPE='"+dateType+"'");
	  return getDataAccess().queryForMap(sql.toString());
  } 
	//满意度评测总体情况_上周(月，日)数据
	public  Map<String,Object> getChartData_CustomerSatisfiedSum(String startDate,String endDate, Map<String,Object> map) {
		String vTypeId = Convert.toString(map.get("vTypeId"));
		String tabStr="";
		if("0".equals(vTypeId)){
			tabStr="CS_VISIT_KDZYJ_AREA";
		}if("1".equals(vTypeId)){
			tabStr="CS_VISIT_KDXZ_AREA";
		}if("2".equals(vTypeId)){
			tabStr="CS_VISIT_BUSI_AREA";
		}if("5".equals(vTypeId)){
			tabStr="CS_VISIT_10000_AREA";
		}if("6".equals(vTypeId)){
			tabStr="CS_VISIT_WT_AREA";
		}if("7".equals(vTypeId)){
			tabStr="CS_VISIT_ZT_AREA";
		}if("8".equals(vTypeId)){
			tabStr="CS_VISIT_HB_AREA";
		}if("9".equals(vTypeId)){
			tabStr="CS_VISIT_ISHOP_AREA";
		}if("10".equals(vTypeId)){
			tabStr="CS_VISIT_DX_AREA";
		}if("11".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA";
		}if("12".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA";
		}if("13".equals(vTypeId)){
			tabStr="CS_VISIT_IVRZYJ_AREA_1";
		}if("14".equals(vTypeId)){
			tabStr="CS_VISIT_IVRXZ_AREA_1";		
		}if("17".equals(vTypeId)){
			tabStr="CS_VISIT_TS_AREA";		
		}if("30".equals(vTypeId)){
			tabStr="CS_VISIT_SHQD_AREA";
		}if("31".equals(vTypeId)){
			tabStr="CS_VISIT_ZYQD_AREA";
		}if("300".equals(vTypeId)){
			tabStr="CS_VISIT_WX_AREA";
		}if("18".equals(vTypeId)){
			tabStr="CS_VISIT_VIP_AREA";
		}
		String dateType =Convert.toString(map.get("dateType"));
		String zoneCode =Convert.toString(map.get("zoneCode"));
		StringBuffer sql = new StringBuffer("select DECODE(sum(v_succ_num),0,0," +
				"round((sum(very_satisfy_num+satisfy_num)/sum(v_succ_num) )*100,2)) SATISFY_LV" +
				" from "+tabStr+" a where a.REGION_ID = '"+zoneCode+"' and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' " +
						" and a.V_TYPE_ID='"+vTypeId+"' and DATE_TYPE='"+dateType+"'");
	  return getDataAccess().queryForMap(sql.toString());
  }
	   //客户满意度（各触点）报表_表格数据
	public Map<String, Object> getEveryType_Week(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_VISIT_SATISFY_LEVEL;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //客户满意度（各触点）报表_表格数据_修改版
	public Map<String, Object> getEveryType_WeekMod(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		String actType=MapUtils.getString(queryData, "actType", null);//判断是装维还是营业厅:0宽带新装，1宽带修障，2，营业厅服务，3投诉处理，4电子渠道
		if("0".equals(actType)||"1".equals(actType)){
			storeName=ConstantStoreProc.RPT_VISIT_SATISFY_AREA_ZW;//装维
		}if("2".equals(actType)){
			storeName=ConstantStoreProc.RPT_VISIT_SATISFY_AREA_BUSI;//营业厅
		}if("5".equals(actType)){
			storeName=ConstantStoreProc.RPT_VISIT_SATISFY_AREA_10000;//10000号
		}if("6".equals(actType)){
			storeName=ConstantStoreProc.RPT_VISIT_SATISFY_AREA_WT;//网厅
		}if("7".equals(actType)){
			storeName=ConstantStoreProc.RPT_VISIT_SATISFY_AREA_ZT;//掌厅
		}if("8".equals(actType)){
			storeName=ConstantStoreProc.RPT_VISIT_SATISFY_AREA_HB;//号百
		}
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //客户满意度（各触点）报表_表格数据_维度修改版
	public Map<String, Object> getEveryType_WeekDemension(Map<String, Object> queryData, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		String actType=MapUtils.getString(queryData, "actType", null);//判断是装维还是营业厅:0宽带新装，1宽带修障，2，营业厅服务，3投诉处理，4电子渠道,5 10000号，6网厅,7掌厅
		String demension=MapUtils.getString(queryData, "demension", null);//判断是维度:0：客户在网时长 1：受理渠道 2：客户群长 3：客户等级 4：ARPU值    5:套餐类型 6:装维员(营业员)。7：测评方式 8：业务类型
			if("0".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_ON_TIME;
			}if("1".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_CHANNEL;
			}if("2".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_CUSTGROUP;
			}if("3".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_CUSTLEVEL;
			}if("4".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_ARPU;
			}if("5".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_CDMATYPE;
			}if("6".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_DUTYOR;
			}if("7".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_TMETHOD;
			}if("8".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_BUSINESS;
			}if("9".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_PRODTYPE;
			}if("10".equals(demension)){
				storeName=ConstantStoreProc.RPT_VISIT_SATISFY_TERMINAL;
			}
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //客户满意度（总表_表格数据
	public Map<String, Object> getResultSum(Map<String, Object> queryData, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_VISIT_SATISFY_MAIN;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //不满意原因周报
	public Map<String, Object> getNosatisfied_ResultSum(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_VISIT_SATISFY_DIS_REA_WEEK;
		Object[] params = {
				MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //装维IVR不满意原因周报
	public Map<String, Object> getNosatisfiedZWIVR_ResultSum(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_VISIT_NO_SATISFY_WEEK;
		Object[] params = {
				MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	
	public Map<String, Object> getNoSatisfyResult(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_VISIT_DIS_SATISFY_WEEK;
		Object[] params = {
				MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endTime", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //满意度测评结果周报（各触点）
	public Map<String, Object> getServicesatisfiedResult(Map<String, Object> queryData, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_VISIT_SATISFY_DIS_RES_WEEK;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	//客户满意度（各触点）报表_表格数据总条数_维度修改版
	public Integer getDataCount(Map<String, Object> paramMap) {
		String vTypeId=MapUtils.getString(paramMap, "actType", null);//判断是装维还是营业厅:0宽带新装，1宽带修障，2，营业厅服务，3投诉处理，4电子渠道
		String demension=MapUtils.getString(paramMap, "demension", null);//判断是维度:0：客户在网时长 1：受理渠道 2：客户群长 3：客户等级 4：ARPU值
		String zoneCode =Convert.toString(paramMap.get("zoneCode"));
		String dateTime = Convert.toString(paramMap.get("dateTime")).replaceAll("-", "");
		String tabStr="";
		if("0".equals(demension)){//客户在网时长
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_ONLINE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_ONLINE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_ONLINE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_ONLINE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_ONLINE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_ONLINE";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_ONLINE";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_ONLINE";
			}	
		}if("1".equals(demension)){//受理渠道
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CHANNEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CHANNEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CHANNEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CHANNEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CHANNEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CHANNEL";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_CHANNEL";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_CHANNEL";
			}		
		}if("2".equals(demension)){//客户群长
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CUSTGROUP";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CUSTGROUP";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CUSTGROUP";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CUSTGROUP";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CUSTGROUP";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CUSTGROUP";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_CUSTGROUP";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_CUSTGROUP";
			}
		}if("3".equals(demension)){//客户等级
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CUSTLEVEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CUSTLEVEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CUSTLEVEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CUSTLEVEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CUSTLEVEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CUSTLEVEL";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_CUSTLEVEL";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_CUSTLEVEL";
			}if("17".equals(vTypeId)){
				tabStr="CS_VISIT_TS_CUSTLEVEL";
			  }	
		}if("4".equals(demension)){//ARPU值
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_ARPULEVEL";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_ARPULEVEL";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_ARPULEVEL";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_ARPULEVEL";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_ARPULEVEL";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_ARPULEVEL";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_ARPULEVEL";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_ARPULEVEL";
			}	
		}if("5".equals(demension)){//套餐类型
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_CDMATYPE";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_CDMATYPE";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_CDMATYPE";
			}if("6".equals(vTypeId)){
				tabStr="CS_VISIT_WT_CDMATYPE";
			}if("7".equals(vTypeId)){
				tabStr="CS_VISIT_ZT_CDMATYPE";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_CDMATYPE";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_CDMATYPE";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_CDMATYPE";
			}	
		}if("6".equals(demension)){//服务人员
			if("0".equals(vTypeId)||"1".equals(vTypeId)){
				tabStr="CS_VISIT_ZW_DUTYOPTR";
			}if("2".equals(vTypeId)){
				tabStr="CS_VISIT_BUSI_DUTYOPTR";
			}if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_DUTYOPTR";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_DUTYOPTR";
			}if("9".equals(vTypeId)){
				tabStr="CS_VISIT_ISHOP_DUTYOPTR";
			}if("10".equals(vTypeId)){
				tabStr="CS_VISIT_DX_DUTYOPTR";
			}
		}if("7".equals(demension)){//测评方式
			if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_TMETHOD";
			}
		}if("8".equals(demension)){//业务类型
			if("5".equals(vTypeId)){
				tabStr="CS_VISIT_10000_BUSINESS";
			}if("8".equals(vTypeId)){
				tabStr="CS_VISIT_HB_BUSINESS";
			}
		}
		    StringBuffer sql=new StringBuffer();
		    sql.append("SELECT COUNT(*) FROM "+tabStr+" f WHERE 1=1");
	        sql.append(" AND f.region_treecode like (select treecode from meta_dim_zone z where zone_code ='"+zoneCode+"')||'%' ");
	        sql.append(" AND month_id='"+dateTime+"'");
         return getDataAccess().queryForInt(sql.toString());
}	
	//各触点报表_图
	public  Map<String,Object> getChartData_EveryTypeWeek(String week,String month, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
	      StringBuffer sql = new StringBuffer("select" +
	      		" sum(a.v_num)V_NUM,sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num else v_succ_num-satisfy_num end)+sum(satisfy_num) V_SUCC_NUM," +
	      		"decode(sum(a.v_num),0,0,round((sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num))/sum(a.v_num)*100,2))V_SUCC_NUM_LV," +
	      		"sum(a.SATISFY_NUM) SATISFY_NUM," +
	      		"decode(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num),0,0,round(sum(a.SATISFY_NUM)/(sum(case when v_type_id='3'and" +
	      		" is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num))*100,2))SATISFY_NUM_LV,(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then" +
	      		" v_succ_num-satisfy_num end)) NO_SATISFY_NUM," +
	      		"decode(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num),0,0,round((sum(case when v_type_id='3'and is_satisfy='1' then " +
	      		"v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end))/(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then" +
	      		" v_succ_num-satisfy_num end)+sum(satisfy_num))*100,2))NO_SATISFY_NUM_LV "
	              +" from  CS_VISIT_RESULT a," +
	              		" (select ZONE_CODE from META_DIM_ZONE start with ZONE_CODE ='"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)b" +
	              	" where a.region_id=b.zone_code and a.DATE_TYPE ='"+dateType+"' and a.V_TYPE_ID='"+actType+"' and a.MONTH_ID='"+month+"'" +
	              			" and a.week_ID='"+week+"'");
	  return getDataAccess().queryForMap(sql.toString());
  }
	//各触点报表_图_修改版
	public  Map<String,Object> getChartData_EveryTypeWeekMod(String week,String month, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
		  String tabStr="";
		  if("0".equals(actType)||"1".equals(actType)){
			  tabStr="CS_VISIT_ZW_CUSTLEVEL";
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_CUSTLEVEL";
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_CUSTLEVEL";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_CUSTLEVEL";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_CUSTLEVEL";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_CUSTLEVEL";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_CUSTLEVEL";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_CUSTLEVEL";
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_CUSTLEVEL";
		  }
	      StringBuffer sql = new StringBuffer("select" +
	      		" sum(a.V_SUCC_NUM)V_SUCC_NUM,to_char(decode(sum(a.V_NUM),0,0,round(sum(a.V_SUCC_NUM)/sum(a.V_NUM)*100,2)))V_SUCC_NUM_LV,sum(a.very_satisfy_num)v_satisfy_num,sum(a.satisfy_num)satisfy_num,sum(no_satisfy_num)no_satisfy_num,sum(a.very_satisfy_num)+sum(a.satisfy_num)satisfy," +
	      		" case when to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00')='' then '0'  else " +
	      		" to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00') end satisfy_lv" +
	              " from "+tabStr+" a," +
	              		" (select ZONE_CODE from META_DIM_ZONE start with ZONE_CODE ='"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)b" +
	              	" where a.region_id=b.zone_code and a.DATE_TYPE ='"+dateType+"' and a.V_TYPE_ID='"+actType+"' and a.MONTH_ID='"+month+"'" +
	              			" and a.week_ID='"+week+"'");
	  return getDataAccess().queryForMap(sql.toString());
  }
	//满意度详情周报
	public  Map<String,Object> getChartData_EveryTypeWeekSum(String week,String month, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
		  String rptId=MapUtils.getString(map, "rptId", null);
		  String tabStr="";
		  if("0".equals(actType)){
			  tabStr="CS_VISIT_KDZYJ_AREA";
		  }if("1".equals(actType)){
			  tabStr="CS_VISIT_KDXZ_AREA";
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA";
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_AREA";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_AREA";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_AREA";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_AREA";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_AREA";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_AREA";
		  }if("11".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA";
		  }if("12".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA";
		  }if("13".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA_1";
		  }if("14".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA_1";	  
		  }if("20".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA_OTHER";
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_AREA";
		  }if("30".equals(actType)){
			  tabStr="CS_VISIT_SHQD_AREA";
		  }if("31".equals(actType)){
			  tabStr="CS_VISIT_ZYQD_AREA";
		  }if("300".equals(actType)){
			  tabStr="CS_VISIT_WX_AREA";
		  }if("18".equals(actType)){
			  tabStr="CS_VISIT_VIP_AREA";
		  }
	      StringBuffer sql = new StringBuffer("select" +
	      		" sum(a.V_SUCC_NUM)V_SUCC_NUM,to_char(decode(sum(a.V_NUM),0,0,round(sum(a.V_SUCC_NUM)/sum(a.V_NUM)*100,2)))V_SUCC_NUM_LV,sum(a.very_satisfy_num)v_satisfy_num,sum(a.satisfy_num)satisfy_num,sum(no_satisfy_num)no_satisfy_num,sum(a.very_satisfy_num)+sum(a.satisfy_num)satisfy," +
	      		" case when to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00')='' then '0'  else " +
	      		" to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00') end satisfy_lv" +
	              " from "+tabStr+" a,meta_dim_zone_branch c " +
	              " where a.region_id = c.ZONE_CODE(+) and ZONE_CODE ='"+zoneCode+"' " +
	              	" and a.DATE_TYPE ='"+dateType+"'");
	      if("20".equals(actType)){
	    	  sql.append(" and a.V_TYPE_ID='2' and a.channel_type_id not in('1000','1100')");
	      }
	      if("13".equals(actType)||"14".equals(actType)){
	    	  if( "1".equals(rptId)){
   	    		  sql.append(" AND ACCESSMODE_NAME IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤')"); 
   	    	  }
   	    	  if( "2".equals(rptId)){
   	    		  sql.append("AND ACCESSMODE_NAME  IN('FTTH光纤接入方式','FTTO')"); 
   	    	  }
   	    	  if( "3".equals(rptId)){
   	    		  sql.append("AND PRE_ACCESSMODE_CODE IS NOT NULL and ACCESSMODE_NAME  IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤' )"); 
   	    	  }
   	    	  if( "4".equals(rptId)){
   	    		  sql.append("AND PRE_ACCESSMODE_CODE IS NULL AND ACCESSMODE_NAME  IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤' )"); 
   	    	  }
	      }
	      else {
			sql.append("and a.V_TYPE_ID='"+actType+"'");
		 }
	      sql.append(" and a.MONTH_ID='"+month+"' and a.week_ID='"+week+"'");
	  return getDataAccess().queryForMap(sql.toString());
}
	//各触点报表_按时间取数
      public  Map<String,Object> getChartData_EveryType(String dateTime, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
	      StringBuffer sql = new StringBuffer("select" +
	    		  " sum(a.v_num)V_NUM,sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num else v_succ_num-satisfy_num end)+sum(satisfy_num) V_SUCC_NUM," +
	    		  "decode(sum(a.v_num),0,0,round((sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num))/sum(a.v_num)*100,2))V_SUCC_NUM_LV," +
	    		  "sum(a.SATISFY_NUM) SATISFY_NUM," +
		      		"decode(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num),0,0,round(sum(a.SATISFY_NUM)/(sum(case when v_type_id='3'and is_satisfy='1'" +
		      		" then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num))*100,2))SATISFY_NUM_LV,(sum(case when v_type_id='3' and is_satisfy='1' then" +
		      		" v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)) NO_SATISFY_NUM," +
		      		"decode(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end)+sum(satisfy_num),0,0,round((sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num" +
		      		" when v_type_id <> '3' then v_succ_num-satisfy_num end))/(sum(case when v_type_id='3'and is_satisfy='1' then v_succ_num when v_type_id <> '3' then v_succ_num-satisfy_num end) + sum(satisfy_num))*100,2))NO_SATISFY_NUM_LV "
	              +" from  CS_VISIT_RESULT a," +
	              " (select ZONE_CODE from META_DIM_ZONE start with ZONE_CODE ='"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)b" +
	              	" where a.region_id=b.zone_code and DATE_TYPE ='"+dateType+"' and V_TYPE_ID='"+actType+"' " +
	              	" and a.V_DATE='"+dateTime+"'");
	  return getDataAccess().queryForMap(sql.toString());
  }
  	//各触点报表_按时间取数_修改版
      public  Map<String,Object> getChartData_EveryTypeMod(String dateTime, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
		  String tabStr="";
		  if("0".equals(actType)||"1".equals(actType)){
			  tabStr="CS_VISIT_ZW_CUSTLEVEL";//装维
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_CUSTLEVEL";//营业厅
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_CUSTLEVEL";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_CUSTLEVEL";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_CUSTLEVEL";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_CUSTLEVEL";
		  }
	      StringBuffer sql = new StringBuffer("select" +
	    		  " sum(a.V_SUCC_NUM)V_SUCC_NUM,to_char(decode(sum(a.V_NUM),0,0,round(sum(a.V_SUCC_NUM)/sum(a.V_NUM)*100,2)))V_SUCC_NUM_LV,sum(a.very_satisfy_num)v_satisfy_num,sum(a.satisfy_num)satisfy_num,sum(no_satisfy_num)no_satisfy_num,sum(a.very_satisfy_num)+sum(a.satisfy_num)satisfy," +
		      		" case when to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00')='' then '0'  else " +
		      		" to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00') end satisfy_lv" +
		      		" from "+tabStr+" a," +
	              " (select ZONE_CODE from META_DIM_ZONE start with ZONE_CODE ='"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)b" +
	              	" where a.region_id=b.zone_code and DATE_TYPE ='"+dateType+"' and V_TYPE_ID='"+actType+"' " +
	              	" and a.V_DATE='"+dateTime+"'");
	  return getDataAccess().queryForMap(sql.toString());
  }
  	//满意度测评详情图形数据获取
      public  Map<String,Object> getChartData_EveryTypeSum(String startDate,String endDate, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
		  String tabStr="";
		  if("0".equals(actType)){
			  tabStr="CS_VISIT_KDZYJ_AREA";//装维
		  }if("1".equals(actType)){
			  tabStr="CS_VISIT_KDXZ_AREA";//装维
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA";//营业厅
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_AREA";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_AREA";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_AREA";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_AREA";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_AREA";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_AREA";
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_AREA";
		  }if("30".equals(actType)){
			  tabStr="CS_VISIT_SHQD_AREA";
		  }if("31".equals(actType)){
			  tabStr="CS_VISIT_ZYQD_AREA";
		  }if("300".equals(actType)){
			  tabStr="CS_VISIT_WX_AREA";
		  }if("18".equals(actType)){
			  tabStr="CS_VISIT_VIP_AREA";
		  }
	      StringBuffer sql = new StringBuffer("select" +
	    		  " sum(a.V_SUCC_NUM)V_SUCC_NUM,case when to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00')='' " +
	    		  "then '0' else  to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00') end  V_SUCC_NUM_LV," +
	    		  "sum(a.very_satisfy_num)v_satisfy_num,sum(a.satisfy_num)satisfy_num,sum(no_satisfy_num)no_satisfy_num,sum(a.very_satisfy_num)+sum(a.satisfy_num)satisfy," +
		      		" case when to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00')='' then '0'  else " +
		      		" to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00') end satisfy_lv" +
		      		" from "+tabStr+" a,meta_dim_zone_branch c " +
			     	" where a.region_id = c.ZONE_CODE(+) and ZONE_CODE ='"+zoneCode+"' " +
	              	" and DATE_TYPE ='"+dateType+"' and V_TYPE_ID='"+actType+"' " +
	              	" and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"'");
	  return getDataAccess().queryForMap(sql.toString());
  } 
    	//满意度测评详情图形数据获取
      public  List<Map<String, Object>> getChartData_EveryTypeSumLine(String startDate,String endDate, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
		  String rptId=MapUtils.getString(map, "rptId", null);
		  String tabStr="";
		  if("0".equals(actType)){
			  tabStr="CS_VISIT_KDZYJ_AREA";//装维
		  }if("1".equals(actType)){
			  tabStr="CS_VISIT_KDXZ_AREA";//装维
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA";//营业厅
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_AREA";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_AREA";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_AREA";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_AREA";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_AREA";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_AREA";
		  }if("11".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA";
		  }if("12".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA";
		  }if("13".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA_1";
		  }if("14".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA_1";
		  }if("20".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA_OTHER";//营业厅非实体
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_AREA";//投诉
		  }if("30".equals(actType)){
			  tabStr="CS_VISIT_SHQD_AREA";
		  }if("31".equals(actType)){
			  tabStr="CS_VISIT_ZYQD_AREA";
		  }if("300".equals(actType)){
			  tabStr="CS_VISIT_WX_AREA";
		  }if("18".equals(actType)){
			  tabStr="CS_VISIT_VIP_AREA";
		  }
	      StringBuffer sql = new StringBuffer("select * from (select * from meta_dim_datetype where date_id >= '"+startDate+"' and date_id <= '"+endDate+"' " +
	      		" and date_type = '"+dateType+"') f left join(" +
	      		"select V_DATE,V_SUCC_NUM,V_SUCC_NUM_LV,v_satisfy_num,satisfy_num,no_satisfy_num,satisfy,satisfy_lv from (" +
	    		  "select V_DATE,sum(a.V_SUCC_NUM)V_SUCC_NUM,case when to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00')='' " +
	    		  "then '0' else  to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00') end  V_SUCC_NUM_LV," +
	    		  "sum(a.very_satisfy_num)v_satisfy_num,sum(a.satisfy_num)satisfy_num,sum(no_satisfy_num)no_satisfy_num,sum(a.very_satisfy_num)+sum(a.satisfy_num)satisfy," +
		      		" case when to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00')='' then '0'  else " +
		      		" to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00') end satisfy_lv" +
		      		" from "+tabStr+" a" +
			     	" where  region_id ='"+zoneCode+"' " +
	              	" and DATE_TYPE ='"+dateType+"'" +
	              	" and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"'");
				   if("20".equals(actType)){
			        	sql.append(" and V_TYPE_ID='9'  and channel_type_id not in('1000','1100')");
			        }
				   if("13".equals(actType)||"14".equals(actType)){
		       	    	  if( "1".equals(rptId)){
		       	    		  sql.append(" AND ACCESSMODE_NAME IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤')"); 
		       	    	  }
		       	    	  if( "2".equals(rptId)){
		       	    		  sql.append("AND ACCESSMODE_NAME  IN('FTTH光纤接入方式','FTTO')"); 
		       	    	  }
		       	    	  if( "3".equals(rptId)){
		       	    		  sql.append("AND PRE_ACCESSMODE_CODE in('FTTB(光纤到楼)','独有LAN','FTTB+LAN','独有电话线','共享宽带线路','已有电话线')and ACCESSMODE_NAME  IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤' )"); 
		       	    	  }
		       	    	  if( "4".equals(rptId)){
		       	    		  sql.append("AND PRE_ACCESSMODE_CODE IS NULL AND ACCESSMODE_NAME  IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤' )"); 
		       	    	  }
		       	      }
				   else {
						sql.append("and V_TYPE_ID='"+actType+"' ");
					}
	      		sql.append(" group by V_DATE)a)g on f.date_id=g.v_date order by date_id ");
	      			
	  return getDataAccess().queryForList(sql.toString());
  } 
    //满意度测评详情图形数据获取
      public  List<Map<String, Object>> getChartData_EveryTypeSumByDate(String startDate,String endDate, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
		  String rptId=MapUtils.getString(map, "rptId", null);
		  String changeCode="".equals(Convert.toString(map.get("changeCode")))?"1":Convert.toString(map.get("changeCode"));
		  String tabStr="";
		  if("0".equals(actType)){
			  tabStr="CS_VISIT_KDZYJ_AREA";//装维
		  }if("1".equals(actType)){
			  tabStr="CS_VISIT_KDXZ_AREA";//装维
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA";//营业厅评价器
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_AREA";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_AREA";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_AREA";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_AREA";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_AREA";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_AREA";
		  }if("11".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA";
		  }if("12".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA";
		  }if("13".equals(actType)){
			  tabStr="CS_VISIT_IVRZYJ_AREA_1";
		  }if("14".equals(actType)){
			  tabStr="CS_VISIT_IVRXZ_AREA_1";
		  }if("20".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA_OTHER";//营业厅非实体
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_AREA";//投诉
		  }if("30".equals(actType)){
			  tabStr="CS_VISIT_SHQD_AREA";
		  }if("31".equals(actType)){
			  tabStr="CS_VISIT_ZYQD_AREA";
		  }if("300".equals(actType)){
			  tabStr="CS_VISIT_WX_AREA";
		  }if("18".equals(actType)){
			  tabStr="CS_VISIT_VIP_AREA";
		  }
		  String sql1="select rownum,order_id,ZONE_CODE,ZONE_NAME,DIM_LEVEL from (";
			if("0".equals(changeCode)){
				sql1+="select order_id,ZONE_CODE, ZONE_NAME,zone_par_code,DIM_LEVEL from meta_dim_zone_branch " +
				"WHERE zone_par_code= (SELECT zone_par_code FROM meta_dim_zone_branch WHERE ZONE_CODE = '"+zoneCode+"')" +
						" order by order_id";
			}
			
			else{
			    sql1+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from meta_dim_zone_branch start with ZONE_CODE = '"+zoneCode+"' " +
					"connect by prior ZONE_CODE = zone_par_code)b  WHERE b.DIM_LEVEL = " +
					"(SELECT DIM_LEVEL+'"+changeCode+"' FROM meta_dim_zone_branch WHERE ZONE_CODE ='"+zoneCode+"')" +
							" order by dim_level,order_id";
			}
			sql1+=")where rownum<23";
			
	      StringBuffer sql = new StringBuffer("select REGION_ID,REGION_NAME," +
	    		  "sum(a.V_SUCC_NUM)V_SUCC_NUM,case when to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00')='' " +
	    		  "then '0' else  to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00') end  V_SUCC_NUM_LV," +
	    		  "sum(a.very_satisfy_num)v_satisfy_num,sum(a.satisfy_num)satisfy_num,sum(no_satisfy_num)no_satisfy_num,sum(a.very_satisfy_num)+sum(a.satisfy_num)satisfy," +
		      		" case when to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00')='' then '0'  else " +
		      		" to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00') end satisfy_lv" +
		      		" from "+tabStr+" a,(select ZONE_CODE,order_id,dim_level from ("+sql1+") )b where a.REGION_ID = b.ZONE_CODE " +
	              	" and DATE_TYPE ='"+dateType+"'" +
	              	" and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"' ");
	              	if("20".equals(actType)){
	              		sql.append(" and V_TYPE_ID='2'  and channel_type_id not in('1000','1100')");
	              	}
	              	 if("13".equals(actType)||"14".equals(actType)){
	       	    	  if( "1".equals(rptId)){
	       	    		  sql.append(" AND ACCESSMODE_NAME IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤')"); 
	       	    	  }
	       	    	  if( "2".equals(rptId)){
	       	    		  sql.append("AND ACCESSMODE_NAME  IN('FTTH光纤接入方式','FTTO')"); 
	       	    	  }
	       	    	  if( "3".equals(rptId)){
	       	    		  sql.append("AND PRE_ACCESSMODE_CODE in('FTTB(光纤到楼)','独有LAN','FTTB+LAN','独有电话线','共享宽带线路','已有电话线')and ACCESSMODE_NAME  IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤' )"); 
	       	    	  }
	       	    	  if( "4".equals(rptId)){
	       	    		  sql.append("AND PRE_ACCESSMODE_CODE IS NULL AND ACCESSMODE_NAME  IN('FTTH(光纤到户)','FTTO(企业光纤拨号)','光纤' )"); 
	       	    	  }
	       	      }
	              	else {
						sql.append("and V_TYPE_ID='"+actType+"' ");
					}
	              	 sql.append(" group by REGION_ID,REGION_NAME,order_id order by  b.order_id");
	  return getDataAccess().queryForList(sql.toString());
  }
      
      public Map<String, Object> getOaSatisfiedSum(Map<String, Object>  map) {
  		Map<String, Object> mapList =new  HashMap<String, Object>();
  		String storeName="";
  		Object[] params =null;
  			 storeName = "PRT_EAIC_OA_PUSH_RESULT";
  			 params = new Object[]{ MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
  					    MapUtils.getString(map, "endDate", null).replaceAll("-", ""),
  						MapUtils.getString(map, "zoneCode", null),
  						//page.getStartRow(),
  						//page.getEndRow(),
  						MapUtils.getString(map, "typeId", null),
  					    new DBOutParameter(OracleTypes.INTEGER),
  						new DBOutParameter(OracleTypes.CURSOR)};
  		 
  		int len = params.length;
  		String sql = convertStore(storeName, len);
  		ResultSet rs = null;
  		try {
  			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
  			mapList.put("allPageCount", statement.getInt(len-1));
  			rs = (ResultSet) statement.getObject(len);
  			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
  			dataColumn = rsToMaps(rs);
  			ResultSetMetaData rsm = rs.getMetaData();
  			String[]  headColumn = new String[rsm.getColumnCount()];
  			for (int i = 0; i < headColumn.length; i++) {
  				headColumn[i]=rsm.getColumnLabel(i+ 1);
  			}
  			mapList.put("headColumn", headColumn);
  			mapList.put("dataColumn", dataColumn);
  		} catch (SQLException e) {
  			e.printStackTrace();
  		} finally {
  			super.getDataAccess().close(rs);
  		}
  		return mapList;	
  	}
      
   public  List<Map<String, Object>> getChartData_EveryTypeSumByDate_Dg(String startDate,String endDate, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String actType=MapUtils.getString(map, "actType", null);
		  String dateType=MapUtils.getString(map, "dateType", null);
		  String changeCode="".equals(Convert.toString(map.get("changeCode")))?"1":Convert.toString(map.get("changeCode"));
		  String tabStr="";
		  if("0".equals(actType)){
			  tabStr="CS_VISIT_KDZYJ_AREA";//装维
		  }if("1".equals(actType)){
			  tabStr="CS_VISIT_KDXZ_AREA";//装维
		  }if("2".equals(actType)){
			  tabStr="CS_VISIT_BUSI_AREA";//营业厅
		  }if("5".equals(actType)){
			  tabStr="CS_VISIT_10000_AREA";
		  }if("6".equals(actType)){
			  tabStr="CS_VISIT_WT_AREA";
		  }if("7".equals(actType)){
			  tabStr="CS_VISIT_ZT_AREA";
		  }if("8".equals(actType)){
			  tabStr="CS_VISIT_HB_AREA";
		  }if("9".equals(actType)){
			  tabStr="CS_VISIT_ISHOP_AREA";
		  }if("10".equals(actType)){
			  tabStr="CS_VISIT_DX_AREA";
		  }if("17".equals(actType)){
			  tabStr="CS_VISIT_TS_AREA";
		  }if("30".equals(actType)){
			  tabStr="CS_VISIT_SHQD_AREA";
		  }if("31".equals(actType)){
			  tabStr="CS_VISIT_ZYQD_AREA";
		  }if("300".equals(actType)){
			  tabStr="CS_VISIT_WX_AREA";
		  }if("18".equals(actType)){
			  tabStr="CS_VISIT_VIP_AREA";
		  }
		  if(zoneCode.length()==3){
			  changeCode="2";
		  }
		  String sql1="select rownum,order_id,ZONE_CODE,ZONE_NAME,DIM_LEVEL from (";
			if("0".equals(changeCode)){
				sql1+="select order_id,ZONE_CODE, ZONE_NAME,zone_par_code,DIM_LEVEL from meta_dim_zone_branch " +
				"WHERE zone_par_code= (SELECT zone_par_code FROM meta_dim_zone_branch WHERE ZONE_CODE = '"+zoneCode+"')" +
						" order by order_id";
			}else{
			    sql1+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from meta_dim_zone_branch start with ZONE_CODE = '"+zoneCode+"' " +
					"connect by prior ZONE_CODE = zone_par_code)b  WHERE b.DIM_LEVEL = " +
					"(SELECT DIM_LEVEL+'"+changeCode+"' FROM meta_dim_zone_branch WHERE ZONE_CODE ='"+zoneCode+"')" +
							" order by dim_level,order_id";
			}
			sql1+=")where rownum<23";
			
	      StringBuffer sql = new StringBuffer("select REGION_ID,REGION_NAME," +
	    		  "sum(a.V_SUCC_NUM)V_SUCC_NUM,case when to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00')='' " +
	    		  "then '0' else  to_char(DECODE(sum(v_num),0,0,round((sum(v_succ_num)/sum(v_num) )*100,2)),'FM99990.00') end  V_SUCC_NUM_LV," +
	    		  "sum(a.very_satisfy_num)v_satisfy_num,sum(a.satisfy_num)satisfy_num,sum(no_satisfy_num)no_satisfy_num,sum(a.very_satisfy_num)+sum(a.satisfy_num)satisfy," +
		      		" case when to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00')='' then '0'  else " +
		      		" to_char(DECODE(sum(a.v_succ_num),0,0,round(((sum(very_satisfy_num)+sum(satisfy_num))/sum(a.v_succ_num) )*100,2)),'FM99990.00') end satisfy_lv" +
		      		" from "+tabStr+" a,(select ZONE_CODE,order_id,dim_level from ("+sql1+") )b where a.REGION_ID = b.ZONE_CODE " +
	              	" and DATE_TYPE ='"+dateType+"' and V_TYPE_ID='"+actType+"' " +
	              	" and a.V_DATE>='"+startDate+"' and a.V_DATE<='"+endDate+"'" +
	              			" group by REGION_ID,REGION_NAME,order_id order by  b.order_id");
	  return getDataAccess().queryForList(sql.toString());
}
	   //不满意原因TOP报表表格数据
	public Map<String, Object> getNOSatisfiedReason_Week(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_REASON;	
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	   //不满意原因TOP报表表格数据_最新
	public Map<String, Object> getNOSatisfiedReason(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = null;
		Object[] params = null;
		if ("20".equals(queryData.get("actType"))) {
			storeName = ConstantStoreProc.RPT_VISIT_SATISFY_DIS_REA_0; //执行营业厅非实体
			 params = new Object[]{
						MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
						MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(queryData, "zoneCode", null),
						MapUtils.getString(queryData, "dateType", null),
						"2",
						"1",
						"15",
						new DBOutParameter(OracleTypes.INTEGER),
						new DBOutParameter(OracleTypes.CURSOR)};
		}else {
			storeName = ConstantStoreProc.RPT_VISIT_SATISFY_DIS_REASON;	
			 params = new Object[]{
						MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
						MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(queryData, "zoneCode", null),
						MapUtils.getString(queryData, "dateType", null),
						MapUtils.getString(queryData, "actType", null),
						"1",
						"15",
						new DBOutParameter(OracleTypes.INTEGER),
						new DBOutParameter(OracleTypes.CURSOR)};
		}
		 
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	//不满意原因TOP列表
	public List<Map<String, Object>> getEveryNoSatisfiedReason(Map<String, Object> queryData){
		 String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
	     String dateTime =       MapUtils.getString(queryData, "dateTime",null).replaceAll("-", "");
	     String dateType =       MapUtils.getString(queryData, "dateType",null);
	     String actType =       MapUtils.getString(queryData, "actType",null);
		String sql="select (sum(V_SUCC_NUM)-sum(SATISFY_NUM))NO_SATISFY_NUM,ERR_REASON_ID,ERR_REASON_NAME from CS_VISIT_RESULT a," +
		 " (select ZONE_CODE from META_DIM_ZONE start with ZONE_CODE ='"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code)b" 
				+" where a.region_id=b.zone_code and a.V_TYPE_ID='"+actType+"' and a.date_type='"+dateType+"'" +
						" and V_DATE='"+dateTime+"' and IS_SATISFY='1' group by ERR_REASON_ID,ERR_REASON_NAME  order by NO_SATISFY_NUM desc";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//满意度不满意原因TOP饼图列表
	public List<Map<String, Object>> getNoSatisfiedReasonPieList(Map<String, Object> queryData){
		String zoneCode = MapUtils.getString(queryData, "zoneCode", null);
		String reasonCode = MapUtils.getString(queryData, "reasonCode", null);
		String startDate = Convert.toString(queryData.get("startDate"))
				.replaceAll("-", "");
		String endDate = Convert.toString(queryData.get("endDate")).replaceAll(
				"-", "");
		String dateType = MapUtils.getString(queryData, "dateType", null);
		String actType = MapUtils.getString(queryData, "actType", null);
		String tabStr = "";
		String sql = "";
		if ("0".equals(actType) || "1".equals(actType)) {
			tabStr = "CS_VISIT_ZW_CUSTLEVEL";// 装维
		}
		if ("2".equals(actType)) {
			tabStr = "CS_VISIT_BUSI_CUSTLEVEL";// 营业厅
		}if ("10".equals(actType)) {
			tabStr = "CS_VISIT_BUSI_CUSTLEVEL_DX";// 营业厅
		}
		if("20".equals(actType)){
			tabStr ="CS_VISIT_BUSI_AREA_OTHER"; //营业厅非实体
		}
		if ("5".equals(actType)) {
			tabStr = "CS_VISIT_10000_CUSTLEVEL";
		}
		if ("6".equals(actType)) {
			tabStr = "CS_VISIT_WT_CUSTLEVEL";
		}
		if ("7".equals(actType)) {
			tabStr = "CS_VISIT_ZT_CUSTLEVEL";
		}
		if ("8".equals(actType)) {
			tabStr = "CS_VISIT_HB_CUSTLEVEL";
		}
		if ("11".equals(actType) || "12".equals(actType)) {
			tabStr = "CS_VISIT_ZWIVR_CUSTLEVEL";// 装维
		}
		if ("17".equals(actType)) {
			tabStr = "CS_VISIT_TS_CUSTLEVEL";// 投诉
		}if ("18".equals(actType)) {
			tabStr = "CS_VISIT_RESULT_VIP";// 投诉
		}
		if ("5".equals(actType)) {
			boolean isRootLeaf = MapUtils.getBoolean(queryData, "isRootLeaf", null);
			if(isRootLeaf){
				sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID,ERR_REASON_NAME from "
					+ tabStr
					+ " a,"
					+ "(select TYPE_ID,dim_level from META_DM.D_V_JSFH_10000_DICT start with TYPE_ID = '"
					+ reasonCode
					+ "' connect by prior TYPE_ID = TYPE_PAR_ID ) d "
					+ "where a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%'  " +
							"and a.ERR_REASON_ID=d.type_id "
					+ " and a.V_TYPE_ID='"
					+ actType
					+ "' and a.date_type='"
					+ dateType
					+ "'"
					+ " and V_DATE>='"
					+ startDate
					+ "' and V_DATE<='"
					+ endDate
					+ "' and IS_SATISFY='1' AND ERR_REASON_ID in('0','1','2','3','4','5','6','7','8','9') " +
							"group by ERR_REASON_ID,ERR_REASON_NAME  order by NO_SATISFY_NUM desc";
			}else{
				sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID_2,ERR_REASON_NAME_2 from "
					+ tabStr
					+ " a,"
					+ "(select TYPE_ID,dim_level from META_DM.D_V_JSFH_10000_DICT start with TYPE_ID = '"
					+ reasonCode
					+ "' connect by prior TYPE_ID = TYPE_PAR_ID ) d "
					+ "where  a.ERR_REASON_ID_2=d.type_id " +
					" and a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%' "					
					+ " and a.V_TYPE_ID='"
					+ actType
					+ "' and a.date_type='"
					+ dateType
					+ "'"
					+ " and V_DATE>='"
					+ startDate
					+ "' and V_DATE<='"
					+ endDate
					+ "' and IS_SATISFY='1' AND ERR_REASON_ID in('0','1','2','3','4','5','6','7','8','9') " +
							"group by ERR_REASON_ID_2,ERR_REASON_NAME_2  order by NO_SATISFY_NUM desc";
			}
		}/* else if ("11".equals(actType)) {
			boolean isRootLeaf = MapUtils.getBoolean(queryData, "isRootLeaf", null);
			if(isRootLeaf){
				sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID,ERR_REASON_NAME from "
					+ tabStr
					+ " a,"
					+ "(select TYPE_ID,dim_level from META_DM.D_V_JSFH_IVRZYJ_DICT start with TYPE_ID = '"
					+ reasonCode
					+ "' connect by prior TYPE_ID = TYPE_PAR_ID ) d "
					+ "where a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%'  " +
							"and a.ERR_REASON_ID=d.type_id "
					+ " and a.V_TYPE_ID='"
					+ actType
					+ "' and a.date_type='"
					+ dateType
					+ "'"
					+ " and V_DATE>='"
					+ startDate
					+ "' and V_DATE<='"
					+ endDate
					+ "' and IS_SATISFY='1' and ERR_REASON_ID is not null  " +
							"group by ERR_REASON_ID,ERR_REASON_NAME  order by NO_SATISFY_NUM desc";
			}else{
				sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID2,ERR_REASON_NAME2 from "
					+ tabStr
					+ " a,"
					+ "(select TYPE_ID,dim_level from META_DM.D_V_JSFH_IVRZYJ_DICT start with TYPE_ID = '"
					+ reasonCode
					+ "' connect by prior TYPE_ID = TYPE_PAR_ID ) d "
					+ "where  a.ERR_REASON_ID2=d.type_id " +
					" and a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%' "					
					+ " and a.V_TYPE_ID='"
					+ actType
					+ "' and a.date_type='"
					+ dateType
					+ "'"
					+ " and V_DATE>='"
					+ startDate
					+ "' and V_DATE<='"
					+ endDate
					+ "' and IS_SATISFY='1' and ERR_REASON_ID is not null  " +
							"group by ERR_REASON_ID2,ERR_REASON_NAME2  order by NO_SATISFY_NUM desc";
			}
		}*//*else if ("12".equals(actType)) {
			boolean isRootLeaf = MapUtils.getBoolean(queryData, "isRootLeaf", null);
			if(isRootLeaf){
				sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID,ERR_REASON_NAME from "
					+ tabStr
					+ " a,"
					+ "(select TYPE_ID,dim_level from META_DM.D_V_JSFH_IVRXZ_DICT start with TYPE_ID = '"
					+ reasonCode
					+ "' connect by prior TYPE_ID = TYPE_PAR_ID ) d "
					+ "where a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%'  " +
							"and a.ERR_REASON_ID=d.type_id "
					+ " and a.V_TYPE_ID='"
					+ actType
					+ "' and a.date_type='"
					+ dateType
					+ "'"
					+ " and V_DATE>='"
					+ startDate
					+ "' and V_DATE<='"
					+ endDate
					+ "' and IS_SATISFY='1' and ERR_REASON_ID is not null  " +
							"group by ERR_REASON_ID,ERR_REASON_NAME  order by NO_SATISFY_NUM desc";
			}else{
				sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID2,ERR_REASON_NAME2 from "
					+ tabStr
					+ " a,"
					+ "(select TYPE_ID,dim_level from META_DM.D_V_JSFH_IVRXZ_DICT start with TYPE_ID = '"
					+ reasonCode
					+ "' connect by prior TYPE_ID = TYPE_PAR_ID ) d "
					+ "where  a.ERR_REASON_ID2=d.type_id " +
					" and a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%' "					
					+ " and a.V_TYPE_ID='"
					+ actType
					+ "' and a.date_type='"
					+ dateType
					+ "'"
					+ " and V_DATE>='"
					+ startDate
					+ "' and V_DATE<='"
					+ endDate
					+ "' and IS_SATISFY='1' and ERR_REASON_ID is not null  " +
							"group by ERR_REASON_ID2,ERR_REASON_NAME2  order by NO_SATISFY_NUM desc";
			}
		}*/
		else if("20".equals(actType)) {
			sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID,ERR_REASON_NAME from "
					+ tabStr
					+ " a  "
					+" where a.REGION_TREECODE = (select treecode from meta_dim_zone where ZONE_CODE = '"+zoneCode+"') "
					+ " and a.V_TYPE_ID='2' and a.date_type='"
					+ dateType
					+ "'"
					+ " and V_DATE>='"
					+ startDate
					+ "' and V_DATE<='"
					+ endDate
					+ "'and  no_satisfy_num >0 and  channel_type_id not in('1000','1100') AND ERR_REASON_ID in('0','1','2','3','4','5','6','7','8','9') " +
							"group by ERR_REASON_ID,ERR_REASON_NAME  order by NO_SATISFY_NUM desc";
		}else if("18".equals(actType)) {
			sql = "select (sum(no_correct_num)+sum(normal_num)+sum(no_satisfy_num)) NO_SATISFY_NUM,ERR_REASON_ID,ERR_REASON_NAME from "
				+ tabStr
				+ " a "
				+ "where a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%'  "
				+ " and a.V_TYPE_ID='"
				+ actType
				+ "' and a.date_type='"
				+ dateType
				+ "'"
				+ " and V_DATE>='"
				+ startDate
				+ "' and V_DATE<='"
				+ endDate
				+ "' and err_reason_id<>'-999' group by ERR_REASON_ID,ERR_REASON_NAME  order by NO_SATISFY_NUM desc";
		}else {
			sql = "select sum(no_satisfy_num)NO_SATISFY_NUM,ERR_REASON_ID,ERR_REASON_NAME from "
				+ tabStr
				+ " a "
				+ "where a.BRANCH_TREECODE like  (select treecode from meta_dim_zone_branch where ZONE_CODE = '"+zoneCode+"') || '%'  "
				+ " and a.V_TYPE_ID='"
				+ actType
				+ "' and a.date_type='"
				+ dateType
				+ "'"
				+ " and V_DATE>='"
				+ startDate
				+ "' and V_DATE<='"
				+ endDate
				+ "' and IS_SATISFY='1' AND ERR_REASON_ID in('0','1','2','3','4','5','6','7','8','9') group by ERR_REASON_ID,ERR_REASON_NAME  order by NO_SATISFY_NUM desc";
		}

		List<Map<String, Object>> list = getDataAccess().queryForList(
				sql.toString());
		return list;
	}
	public boolean getIsRootLeaf(String reasonId) {
		String sql ="select type_par_id from META_DM.D_V_JSFH_10000_DICT where type_id='"+reasonId+"' ";
		String result= getDataAccess().queryForString(sql);
		return "-1".equals(result)?true:false;
	}
	public boolean getIsLeaf(String reasonId) {
		String sql ="select type_id from META_DM.D_V_JSFH_10000_DICT where type_par_id='"+reasonId+"' ";
		String result= getDataAccess().queryForString(sql);
		return result==null?true:false;
	}
	//不满意清单推送
	public Map<String, Object> getNosatisDetailData(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_NO_SATISFY_DETALL;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "orderId",  null),
				MapUtils.getString(map, "busiCode",  null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	
	//预警监控清单推送
	public Map<String, Object> getEWAMDetailData(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_EWAM_TS_DETAIL;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "vTypeId",  null),
				MapUtils.getString(map, "targetId",  null),
				MapUtils.getString(map, "judgeResultId",  null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	
	//预警监控反馈日志
	public Map<String, Object> getEWAMDetailLog(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_EWAM_FB_LOG;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "vTypeId",  null),
				MapUtils.getString(map, "targetId",  null),
				//MapUtils.getString(map, "judgeResultId",  null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	
	public boolean updateVisitResultIVR(Map<String, Object> param) {
		String finishDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    StringBuffer sql = new StringBuffer("UPDATE meta_user.CS_RESULT_NOSATIS_TS  SET finish_Date='"+finishDate+"', check_stat=?,handle_stat=?,memo=? WHERE UNIQUE_VALUE =? ");
	    Object[] params = {
	    		 MapUtils.getString(param,  "checkStat", null) ,MapUtils.getString(param,  "handleStat", null),MapUtils.getString(param,  "memo", null),
	    		 MapUtils.getString(param,  "id",   null)
	    		          };
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	
	public boolean updateEwamFeedback(Map<String, Object> param) {
		String finishDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    StringBuffer sql = new StringBuffer("UPDATE meta_user.CS_VISIT_EWAM_TS  SET finish_Date='"+finishDate+"', CHECK_FEEDBACK=? WHERE UNIQUE_VALUE =? ");
	    Object[] params = {
	    		 MapUtils.getString(param,  "feedBack", null) ,
	    		 MapUtils.getString(param,  "uniqueValue",   null)
	    		          };
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	
	//延迟测评
	public Map<String, Object> getsmsDelay(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_VISIT_SMSDELAY_AREA;;
		Object[] params = {
				MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	
	//延迟测评清单
		public Map<String, Object> getsmsDelayList(Map<String, Object> queryData, Pager page) {
			Map<String, Object> mapList =new  HashMap<String, Object>();
			String storeName=ConstantStoreProc.RPT_VISIT_SMSDELAY_DETALL;;
			Object[] params = {
					MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode",  null),
					MapUtils.getString(queryData, "indexType", null),
					MapUtils.getString(queryData, "satisType",  null),
				    page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
			int len = params.length;
			String sql = convertStore(storeName, len);
			ResultSet rs = null;
			try {
				CallableStatement statement = getDataAccess().execQueryCall(sql,params);
				rs = (ResultSet) statement.getObject(len);
				mapList.put("allPageCount", statement.getInt(len-1));
				List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
				dataColumn = rsToMaps(rs);
				ResultSetMetaData rsm = rs.getMetaData();
				String[]  headColumn = new String[rsm.getColumnCount()];
				for (int i = 0; i < headColumn.length; i++) {
					headColumn[i]=rsm.getColumnLabel(i+ 1);
				}
				mapList.put("headColumn", headColumn);
				mapList.put("dataColumn", dataColumn);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				super.getDataAccess().close(rs);
			}
			return mapList;
		}
		
		//政企满意度清单
		public Map<String, Object> getzqSatisfyList(Map<String, Object> queryData, Pager page) {
			Map<String, Object> mapList =new  HashMap<String, Object>();
			String storeName=ConstantStoreProc.RPT_VISIT_ZQMYD_DETALL;;
			Object[] params = {
					MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode",  null),
					MapUtils.getString(queryData, "visitChannel", null),
					//MapUtils.getString(queryData, "satisType",  null),
				    page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
			int len = params.length;
			String sql = convertStore(storeName, len);
			ResultSet rs = null;
			try {
				CallableStatement statement = getDataAccess().execQueryCall(sql,params);
				rs = (ResultSet) statement.getObject(len);
				mapList.put("allPageCount", statement.getInt(len-1));
				List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
				dataColumn = rsToMaps(rs);
				ResultSetMetaData rsm = rs.getMetaData();
				String[]  headColumn = new String[rsm.getColumnCount()];
				for (int i = 0; i < headColumn.length; i++) {
					headColumn[i]=rsm.getColumnLabel(i+ 1);
				}
				mapList.put("headColumn", headColumn);
				mapList.put("dataColumn", dataColumn);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				super.getDataAccess().close(rs);
			}
			return mapList;
		}
		//政企满意度
		public Map<String, Object> getzqMYDDetails(Map<String, Object> queryData) {
			Map<String, Object> mapList =new  HashMap<String, Object>();
			String storeName = "";
			if("1".equals(queryData.get("flag")))
				storeName=ConstantStoreProc.RPT_VISIT_ZQMYD_AREA;
			else if("2".equals(queryData.get("flag")))
				storeName=ConstantStoreProc.RPT_VISIT_ZQMYD_AREA_WEEK;
			else storeName=ConstantStoreProc.RPT_VISIT_ZQMYD_UNSATISFY;
			Object[] params = {
					MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),
					MapUtils.getString(queryData, "flag", null),
					new DBOutParameter(OracleTypes.CURSOR)};
			int len = params.length;
			String sql = convertStore(storeName, len);
			ResultSet rs = null;
			try {
				CallableStatement statement = getDataAccess().execQueryCall(sql,params);
				rs = (ResultSet) statement.getObject(len);
				List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
				dataColumn = rsToMaps(rs);
				ResultSetMetaData rsm = rs.getMetaData();
				String[]  headColumn = new String[rsm.getColumnCount()];
				for (int i = 0; i < headColumn.length; i++) {
					headColumn[i]=rsm.getColumnLabel(i+ 1);
				}
				mapList.put("headColumn", headColumn);
				mapList.put("dataColumn", dataColumn);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				super.getDataAccess().close(rs);
			}
			return mapList;
		}
		
		
	public Map<String, Object> getItanTass(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.PRO_CS_TB_ITANT_ASS_NTQLITY_OT;
		Object[] params = {
				MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
	
	public void pushFlag(String uniqueValue){
		String sql=" update CS_RESULT_NOSATIS_TS set push_flag='1' where UNIQUE_VALUE=?";
		getDataAccess().execUpdate(sql,uniqueValue);
	}
	
	public void ewamPushFlag(String uniqueValue){
		String sql=" update CS_VISIT_EWAM_TS set push_flag='1' where UNIQUE_VALUE=?";
		getDataAccess().execUpdate(sql,uniqueValue);
	}
	
	//即时测评报表最大日期
	public String getMaxMonth_NTQLITY() {
		String sql = "select max(V_DATE) as DATE_NO from CS_TB_ITANT_ASS_NTQLITY_ALL order by V_DATE desc";
		return getDataAccess().queryForString(sql);
	}
	//即时测评报表月列表
	public List<Map<String, Object>> getDateTimeList_NTQLITY() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select  distinct V_DATE as DATE_NO from CS_TB_ITANT_ASS_NTQLITY_ALL order by V_DATE desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//延迟测评报表月列表
	public List<Map<String, Object>> getDateTimeList_smsDelay() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select  distinct MON_ID as DATE_NO from CS_SMSDELAY_AREA_RESULT order by MON_ID desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//延迟测评报表月列表
	public List<Map<String, Object>> getDateTimeListForAll(String tableName, String field) {
		StringBuffer buffer = new StringBuffer();		
		buffer.append("select replace(week,'_','~') as date_no from (select substr(partition_name,2) as week from user_tab_partitions t where table_name = 'CS_ZQMYD_AREA_RESULT' and length(substr(partition_name,2))=17 order by partition_name desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//渠道服务偏好挖掘最大日期
	public String getMaxMonth_ChannelCS() {
		String sql = "select max(day_id) as DATE_NO from CS_CHANNEL_VIEW_CUST_ALL_LOC order by day_id desc";
		return getDataAccess().queryForString(sql);
	}
	//渠道服务偏好挖掘月列表
	public List<Map<String, Object>> getDateTimeList_ChannelCS() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select  distinct day_id as DATE_NO from CS_CHANNEL_VIEW_CUST_ALL_LOC order by day_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	
	
	//渠道服务偏好挖掘最大日期
	public String getMaxMonth_ChannelSj() {
		String sql = "select max(day_id) as DATE_NO from CS_CHANNEL_VIEW_CUST_SJ_LOC order by day_id desc";
		return getDataAccess().queryForString(sql);
	}
	//渠道服务偏好挖掘月列表
	public List<Map<String, Object>> getDateTimeList_ChannelSj() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select  distinct day_id as DATE_NO from CS_CHANNEL_VIEW_CUST_SJ_LOC order by day_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	

	/***
	 * 存储过程 渠道服务偏好挖掘（服务）
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelCustService(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PRO_CS_CHANNEL_QT_CUST_ALL;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "iChannelId", null),
				MapUtils.getString(queryData, "iCustCode", null),
				MapUtils.getString(queryData, "state", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		
		return mapList;
		
	}
	
  public boolean OAPushCount( Date curDate){
	  String curDateTime=new SimpleDateFormat("yyyy-MM-dd").format(curDate);
	  String logSql="select count(process_limited) from CS_MAIN_SERVICE_PROBLEM  where process_limited='"+DateUtil.lastDay(curDateTime, -1).replaceAll("-", "")+"'";
	  long logCnt=getDataAccess().queryForLong(logSql, null);
	  
	  String recordSql="select count(day_id) from CS_RESULT_NOSATIS_TS where day_id='"+DateUtil.lastDay(curDateTime, -1).replaceAll("-", "")+"'";
	  long recordCnt=getDataAccess().queryForLong(recordSql, null);
	  
	  return logCnt==0&&recordCnt>0;
  }
  
  public boolean EwamOAPushCount( Date curDate){
	  String curDateTime=new SimpleDateFormat("yyyy-MM-dd").format(curDate);
	  String logSql="select count(process_limited) from CS_MAIN_SERVICE_PROBLEM  where process_limited='"+DateUtil.lastDay(curDateTime, -1).replaceAll("-", "")+"'"
	  			   +" and theme like '%满意度测评%'";
	  long logCnt=getDataAccess().queryForLong(logSql, null);
	  
	  String recordSql="select count(v_date) from CS_VISIT_EWAM_TS where v_date='"+DateUtil.lastDay(curDateTime, -1).replaceAll("-", "")+"'";
	  //String recordSql="select count(v_date) from CS_VISIT_EWAM_TS where v_date='20160603'"; //调试固定日期，后面再更改
	  long recordCnt=getDataAccess().queryForLong(recordSql, null);
	  
	  return logCnt==0&&recordCnt>0;
  }
	
	/***
	 * 存储过程 渠道服务偏好挖掘（渠道）
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelCustQd(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PRO_CS_CHANNEL_QTFW_CUST_ALL;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "iCustCode", null),
				MapUtils.getString(queryData, "state", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		
		return mapList;
		
	}

	/***
	 * 存储过程 渠道服务偏好挖掘（渠道）
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelCustSj(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PRO_CS_CHANNEL_QTFW_CUST_SJ;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "ichannelservid", null),
				MapUtils.getString(queryData, "iCustCode", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		
		return mapList;
		
	}
	public List<Map<String, Object>>getNoSatisfyInstall(Map<String,Object>queryData){
    	String startTime=queryData.get("startTime").toString()==null?"":queryData.get("startTime").toString();
    	String dateType=queryData.get("dateType").toString()==null?"":queryData.get("dateType").toString();
    	String actType=queryData.get("actType").toString()==null?"11":queryData.get("actType").toString();
    	StringBuffer sql=new StringBuffer();
    	sql.append("select a.zone_name, min(b.INSTALL_NAME) INSTALL_NAME, max(NO_SATISFY_NUM) NO_SATISFY_NUM from meta_dim_zone a left join (select b.* " +
    			"from (select region_id, max(NO_SATISFY_NUM) NO_SATISFY_NUM from (select region_id,INSTALL_NAME,SUM(NO_SATISFY_NUM) NO_SATISFY_NUM " +
    			"from CS_VISIT_ZWIVR_NOSATIS_SUM a,meta_dim_zone b where v_date='"+startTime+"' and a.region_id = b.zone_code and " +
    			"b.dim_level = '3' and v_type_id = '"+actType+"'  and date_type='"+dateType+"' GROUP BY INSTALL_ACTOR,INSTALL_NAME,region_id,order_id order by order_id)" +
    			"group by region_id) a,(select region_id,INSTALL_NAME,SUM(NO_SATISFY_NUM) NO_SATISFY_NUM from CS_VISIT_ZWIVR_NOSATIS_SUM a," +
    			" meta_dim_zone b where v_date='"+startTime+"' and a.region_id = b.zone_code and b.dim_level = '3' and v_type_id " +
    			"='"+actType+"'  and date_type='"+dateType+"' GROUP BY INSTALL_ACTOR,INSTALL_NAME,region_id,order_id order by order_id) b  where a.NO_SATISFY_NUM = " +
    			"b.NO_SATISFY_NUM and a.region_id = b.region_id) b on a.zone_code = b.region_id where a.dim_level = 3 group by a.zone_name, " +
    			"a.order_id order by a.order_id");
    	return getDataAccess().queryForList(sql.toString());
    }
	
}