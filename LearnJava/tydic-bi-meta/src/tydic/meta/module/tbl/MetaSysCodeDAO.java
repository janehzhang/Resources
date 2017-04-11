package tydic.meta.module.tbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.sys.code.CodePO;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 熊小平
 * @date 2011-10-26
 * @description META_SYS_CODE操作DAO
 * 
 */
public class MetaSysCodeDAO extends MetaBaseDAO {
    /**
     * @description 根据类型查询系统代码
     * @param type
     * @return
     */
    public List<Map<String, Object>> querySysCodeByCodeType(String type) {
    	type = type.toLowerCase();
    	List<Map<String, String>> codes = CodeManager.getCodeList(type);
    	List<Map<String, Object>> rtn = new ArrayList<Map<String, Object>>();
        for(Map<String, String> m:codes){
            Map<String, Object> rtnM = new HashMap<String, Object>();
            rtnM.put("CODE_ITEM", m.get("value"));
            rtnM.put("CODE_NAME", m.get("name"));
            rtn.add(rtnM);
        }
        return rtn;
//        String sql = "SELECT T.CODE_ID,T.CODE_ITEM,T.CODE_NAME "
//                + "FROM META_SYS_CODE T "
//                + "WHERE UPPER(T.CODE_TYPE) = UPPER(?) "
//                + "ORDER BY T.ORDER_ID ";
//        return getDataAccess().queryForList(sql, type);
    }
    
    public List<Map<String, Object>> querySysCodeByPid(String type,int pId) {
    	List<Map<String, String>> codes = CodeManager.getCodeList(type);
    	List<Map<String, Object>> rtn = new ArrayList<Map<String, Object>>();
    	String sql = "";
    	if(pId == 0) {
    		for(Map<String, String> m:codes){
                Map<String, Object> rtnM = new HashMap<String, Object>();
                rtnM.put("CODE_ITEM", m.get("value"));
                rtnM.put("CODE_NAME", m.get("name"));
                rtn.add(rtnM);
            }
//    		sql = "SELECT T.CODE_ID,T.CODE_ITEM,T.CODE_NAME "
//                + "FROM META_SYS_CODE T "
//                + "WHERE UPPER(T.CODE_TYPE) = UPPER(?) "
//                + "ORDER BY T.ORDER_ID ";
    	}else {
    		String tmpId = getDataAccess().queryForString("SELECT T.TABLE_TYPE_ID from meta.meta_table_group T where t.table_group_id = "+pId);
    		for(Map<String, String> m:codes){
    			if(tmpId.equals(m.get("value"))){
	                Map<String, Object> rtnM = new HashMap<String, Object>();
	                rtnM.put("CODE_ITEM", m.get("value"));
	                rtnM.put("CODE_NAME", m.get("name"));
	                rtn.add(rtnM);
    			}
            }
//			sql = "SELECT T.CODE_ID,T.CODE_ITEM,T.CODE_NAME "
//		        + "FROM META_SYS_CODE T "
//		        + "WHERE UPPER(T.CODE_TYPE) = UPPER(?) "
//		        +"AND T.CODE_ITEM ='"+tmpId+"'";
    	}
    	return rtn;
//        return getDataAccess().queryForList(sql, type);
    }

    /**
     * @Title: getTableTypes 
     * @Description: 获取层次分类
     * @param type
     * @return Object[][]   
     * @throws
     */
    public Object[][] getTableTypes(String type){
    	 String sql = "SELECT T.CODE_ITEM,T.CODE_NAME,T.CODE_ID "
             + "FROM META_SYS_CODE T "
             + "WHERE UPPER(T.CODE_TYPE) = UPPER(?) "
             + "ORDER BY T.ORDER_ID ";
	     return getDataAccess().queryForDataTable(sql,type).rows;
    }
}
