package tydic.reports.CommonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import oracle.sql.CLOB;

/***
 * 公共的操作类
 * @author 我爱家乡
 *
 */
public class CommonUtils {
	/***
	 * 表名的映射
	 */
	private static Map<String,Object> TABLE_NAME = new HashMap<String, Object>();
	
	/**
	 * 存储过程映射
	 */
	private static Map<String,Object> PROCEDURE_NAME = new HashMap<String, Object>();
	
	
	static{
		TABLE_NAME.put("cxhw", "CS_CHANNEL_DISTRI_QUERY");//查询话务
		TABLE_NAME.put("zxhw", "CS_CHANNEL_DISTRI_CONSULT");//咨询话务
		TABLE_NAME.put("blhw", "CS_CHANNEL_DISTRI_DEAL");//办理话务
		TABLE_NAME.put("bzhw", "CS_CHANNEL_DISTRI_ERROR");//报障话务
		TABLE_NAME.put("tshw", "CS_CHANNEL_DISTRI_COMPLAIN");//投诉话务
		TABLE_NAME.put("gzxd", "CS_CHANNEL_DISTRI_FUALT");//故障下单
		TABLE_NAME.put("gzlTsxd", "CS_CHANNEL_DISTRI_CMPL");//工作流投诉下单
		TABLE_NAME.put("gzlXqxd", "CS_CHANNEL_DISTRI_REQ");//工作流需求下单
		
		PROCEDURE_NAME.put("cxhw", "RPT_CS_CHANNEL_DISTRI_QUERY");//查询话务
		PROCEDURE_NAME.put("zxhw", "RPT_CS_CHANNEL_DISTRI_CONSULT");//咨询话务
		PROCEDURE_NAME.put("blhw", "RPT_CS_CHANNEL_DISTRI_DEAL");//办理话务
		PROCEDURE_NAME.put("bzhw", "RPT_CS_CHANNEL_DISTRI_ERROR");//报障话务
		PROCEDURE_NAME.put("tshw", "RPT_CS_CHANNEL_DISTRI_COMPLAIN");//投诉话务
		PROCEDURE_NAME.put("gzxd", "RPT_CS_CHANNEL_DISTRI_FUALT");//故障下单
		PROCEDURE_NAME.put("gzlTsxd", "RPT_CS_CHANNEL_DISTRI_CMPL");//工作了投诉
		PROCEDURE_NAME.put("gzlXqxd", "RPT_CS_CHANNEL_DISTRI_REQ");//工作流需求下单
		
		
	}
	//存储过程的配置
	public static final String RPT_CS_CHANNEL_PREF_VIEW ="RPT_CS_CHANNEL_PREF_VIEW";//渠道服务偏好视图
	
	//存储过程的配置
	public static final String RPT_CS_CHANNEL_PREF_VIEW_USER ="RPT_CS_CHANNEL_PREF_VIEW_USER";//渠道服务偏好视图用户级
	
	public static final String RPT_CS_CHANNEL_ONE_PRO="RPT_CS_CHANNEL_ONE_PRO";//区域渠道服务一级报表存储过程
	
	public static final String RPT_CS_CHANNEL_SECOND_PRO="RPT_CS_CHANNEL_SECOND_PRO";//区域渠道服务二级报表
	
	public static final String RPT_CS_CHANNEL_THRID_PRO="RPT_CS_CHANNEL_THRID_PRO";//区域渠道服务三级报表
	
	public static final String RPT_CS_CHANNEL_THRID_TR_PRO="RPT_CS_CHANNEL_THRID_TR_PRO";//区域渠道服务三级报表
	
	public static final String RPT_CS_CHANNEL_CUST_VIEW="RPT_CS_CHANNEL_CUST_VIEW";//区域渠道服务等级偏好视图
	
	public static final String RPT_CS_CHANNEL_CUST_GROUP_VIEW="RPT_CS_CHANNEL_CUST_GROUP_VIEW";// 战略群偏好视图
	
	public static final String RPT_CS_CHANNEL_CUST_BRAND_VIEW="RPT_CS_CHANNEL_CUST_BRAND_VIEW";// 客户品牌好视图
	
	public static final String RPT_CS_CHANNEL_CUST_ARPU_VIEW="RPT_CS_CHANNEL_CUST_ARPU_VIEW";// 用户价值渠道服务偏好
	
	public static final String RPT_CS_CHANNEL_CUST_TIME_VIEW="RPT_CS_CHANNEL_CUST_TIME_VIEW";// 在网时长渠道服务偏好 
	
	public static final String RPT_CS_CHANNEL_CUST_PAY_VIEW="RPT_CS_CHANNEL_CUST_PAY_VIEW";//  付费方式渠道服务偏好
	
	public static final String RPT_CS_CHANNEL_CUST_TERM_VIEW="RPT_CS_CHANNEL_CUST_TERM_VIEW";// 产品类型渠道服务偏好 

	public static final String RPT_CS_CHANNEL_MARKET_VIEW="RPT_CS_CHANNEL_MARKET_VIEW";// 六大市场分析 
	
	public static final String PRO_SYSTEM_APPLY="PRO_SYSTEM_APPLY";//系统表监控
	
	public static final String PRO_SYSTEM_INTERFACE="PRO_SYSTEM_INTERFACE";//系统后台接口表监控
	
	public static final String PRO_CS_CHANNEL_QT_CUST_ALL="PRO_CS_CHANNEL_QT_CUST_ALL";// 渠道服务偏好挖掘（服务）
	
	public static final String PRO_CS_CHANNEL_QTFW_CUST_ALL="PRO_CS_CHANNEL_QTFW_CUST_ALL";// 渠道服务偏好挖掘（渠道）
	
	public static final String PRO_CS_CHANNEL_QTFW_CUST_SJ="PRO_CS_CHANNEL_QTFW_CUST_SJ";// 渠道服务偏好挖掘（时机）

	public static final String RPT_CS_CHANNEL_SERV_SUM="RPT_CS_CHANNEL_SERV_SUM";//偏好应用-服务偏好 整体
	
	public static final String PTR_CS_CHANNEL_SEVER_SIX="PTR_CS_CHANNEL_SEVER_SIX";//偏好应用-服务偏好 六大细分市场&在网时长&用户价值
	
	public static final String PTR_CS_CHANNEL_SEVER_TERMINAL="PTR_CS_CHANNEL_SEVER_TERMINAL";//偏好应用-服务偏好 产品类型
	
	public static final String PTR_CS_CHANNEL_SEVER_CUSTLEVEL="PTR_CS_CHANNEL_SEVER_CUSTLEVEL";//偏好应用-服务偏好 客户等级
	
	public static final String PTR_CS_CHANNEL_SEVER_PAYMENT="PTR_CS_CHANNEL_SEVER_PAYMENT";//偏好应用-服务偏好 付费方式
	
	public static final String RPT_CS_CHANNEL_SUM="RPT_CS_CHANNEL_SUM";//偏好应用-渠道偏好 整体
	
	public static final String RPT_CS_CHANNEL_SIX="RPT_CS_CHANNEL_SIX";//偏好应用-渠道偏好细分市场&在网时长&用户价值
	
	public static final String PTR_CS_CHANNEL_TERMINAL="PTR_CS_CHANNEL_TERMINAL";//偏好应用-渠道偏好   产品类型&客户等级

	public static final String PTR_CS_CHANNEL_PAYMENT="PTR_CS_CHANNEL_PAYMENT";//偏好应用-渠道偏好   付费方式
	
	public static final String MKF_YDXYGL_MONTH_PRO="MKF_YDXYGL_MONTH_PRO";//移动后付费信用管理月报表
	
	public static final String MKF_CreditComplaint_PRO="MKF_CreditComplaint_PRO";//移动后付费信用投诉表

	public static final String MKF_CreditManageKB_PRO="MKF_CreditManageKB_PRO";//移动后付费信用管理宽表
	
	public static final String MKF_YDXYGL_LSSX_PRO="MKF_YDXYGL_LSSX_PRO";//移动后付费临时授信月报表
	
	public static final String MKF_YDXYGL_HOLIDAYCREDIT_PRO="MKF_YDXYGL_HOLIDAYCREDIT_PRO";//移动后付费节假日无限信用效果监控报表
	
	public static final String MKF_LSSX_MONITORKB_PRO="MKF_LSSX_MONITORKB_PRO";//移动后付费临时授信监控管理宽表
	/***
	 * 获取表名
	 * @param key
	 * @return
	 */
	public static String getTableName(String key){
		
		return CommonUtils.TABLE_NAME.get(key).toString();
	}
	/***
	 * 获取存储过程
	 * @param key
	 * @return
	 */
	public static String getProName(String key){
		
		return CommonUtils.PROCEDURE_NAME.get(key).toString();
	}
	
	public static String clobToString(Clob sqlStr) throws SQLException, IOException { 

		String reString = ""; 
		Reader is = sqlStr.getCharacterStream();// 得到流 
		BufferedReader br = new BufferedReader(is); 
		String s = br.readLine(); 
		StringBuffer sb = new StringBuffer(); 
		while (s != null) {// 执行循环将字符串全部取出付值给 StringBuffer由StringBuffer转成STRING 
		sb.append(s); 
		s = br.readLine(); 
		} 
		reString = sb.toString(); 
		return reString; 
		} 
}
