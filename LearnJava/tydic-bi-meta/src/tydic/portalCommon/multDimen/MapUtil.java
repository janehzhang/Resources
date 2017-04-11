package tydic.portalCommon.multDimen;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	
	private static Map<String, String> map = new HashMap<String, String>();
	
	static{
		//map的存储规则(<key>命名规则  窗口编号_名称_对应的sql id , <value> sql拼接字符串:条件替换名称)
		map.put("1_pianqu_11", " and b.zone_par_code = :{map_screen}");//下拉片区  数据字典
		map.put("1_pianqu_2", " and b.zone_par_code = :{map_screen}");//下拉片区  数据字典
		map.put("1_pianqu_3", " and b.zone_par_code = :{map_screen}");//下拉片区  数据字典
		map.put("1_pianqu_4", " and b.zone_par_code = :{map_screen}");//下拉片区  数据字典
		map.put("1_pianqu_51", " and b.zone_par_code = :{map_screen}");//下拉片区  数据字典
		map.put("1_pianqu_52", " and b.zone_par_code = :{map_screen}");//下拉片区  数据字典

//		map.put("2_djxz_2", " and c.cmpl_prod_type_code like :{prod_screen}");//点击产品维度下钻 的条件  数据字典	未优化SQL前所使用的条件，20160726105000
		map.put("2_djxz_2", " and a.cmpl_prod_type_id like :{prod_screen}");//点击产品维度下钻 的条件  数据字典     已优化SQL后所使用的条件，20160726105000优化
		map.put("2_drill_3", " and a.CMPL_PROD_TYPE_ID like :{prod_screen}");//点击产品维度下钻 联动 产品维度的条件  数据字典	
		
//		map.put("3_djxz_2", " and c.cmpl_busi_type_code like :{busi_screen}");//点击表象维度下钻 的条件  数据字典     未优化SQL前所使用的条件，20160726105000	
		map.put("3_djxz_2", " and a.CMPL_BUSINESS_TYPE_ID like :{busi_screen}");//点击表象维度下钻 的条件  数据字典    已优化SQL后所使用的条件，20160726105000优化	
		map.put("3_drill_3", " and a.CMPL_BUSINESS_TYPE_ID like :{busi_screen}");//点击表象维度下钻 联动 表象维度的条件  数据字典	
		
		map.put("4_khsx_41", " and a.serv_lev in :{cust_screen}");//客户属性  数据字典
		map.put("4_khsx_42", " and a.is_4g_disc in :{cust_screen}");//客户属性  数据字典
		map.put("4_khsx_43", " and a.bhgq_type in :{cust_screen}");//客户属性  数据字典
		map.put("4_khsx_44", " and a.payment_id in :{cust_screen}");//客户属性  数据字典
		map.put("4_khsx_45", " and a.optical_fiber_type in :{cust_screen}");//客户属性  数据字典
		map.put("4_khsx_46", " and a.is_intelligence in :{cust_screen}");//客户属性  数据字典
		map.put("4_khsx_47", " and a.SERV_GRP_TYPE in :{cust_screen}");//客户属性  数据字典
		map.put("4_khsx_48", " and a.DIVIDE_MARKET_6_NAME in :{cust_screen}");//客户属性  数据字典
		
	}
	
	public static String getValue(String key){
		return map.get(key);
	}
	
	public static Map<String, String> initMap(){
		Map<String, String> scMap = new HashMap<String, String>();
		scMap.put("{parent_id}", null);// 点击下钻 添加条件
		scMap.put("{map_level}", null); // 城市级别  1 省内， 2片区，3地市，4县级，5营服中心
		scMap.put("{map_screen}", null); //城市的关联的id
		scMap.put("{dr_level}", null); //下钻的级别
		scMap.put("{prod_screen}", null);//产品维度联动条件
		scMap.put("{busi_screen}", null);//表象维度联动条件
		scMap.put("{cust_screen}", null);//客户属性联动条件
		scMap.put("{accs_screen}", null);//考核维度联动条件
		return scMap;
	}
	
	
	
}
