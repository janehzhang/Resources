package tydic.reports.query;

import tydic.frame.BaseDAO;
import java.util.List;
import java.util.Map;

/**
 * @modify  qx
 * @modifyDate  2013-4-15
 */
public class ProdTypeDAO extends BaseDAO {
    /**
     * 查询子业务类型
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubProdType(String parentCode){
        String select = "SELECT A.CMPL_PROD_TYPE_NAME,A.CMPL_PROD_TYPE_CODE,"
        +" A.CMPL_PROD_TYPE_PAR_CODE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM tbas_dm.d_v_cmpl_prod_condition A"
        +" LEFT JOIN (SELECT CMPL_PROD_TYPE_PAR_CODE, COUNT(1) CNT"
        +" FROM tbas_dm.d_v_cmpl_prod_condition"
        +" GROUP BY CMPL_PROD_TYPE_PAR_CODE) C"
        +" ON A.CMPL_PROD_TYPE_CODE = C.CMPL_PROD_TYPE_PAR_CODE"
        +" WHERE A.CMPL_PROD_TYPE_PAR_CODE = ?"
        +" ORDER BY dim_level, ORDER_ID ASC";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
   
    /**
     * 加载从起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据。
     *@author qx
     * @param beginId 起始ID
     * @param endId   结束ID
     * @return
     */
    public List<Map<String,Object>> queryProdTypeByBeginEndPath(String beginId, String endId){	
 	   String sql = "SELECT A.CMPL_PROD_TYPE_NAME,A.CMPL_PROD_TYPE_CODE,A.CMPL_PROD_TYPE_PAR_CODE,"
 		  + " A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
 		 + " FROM tbas_dm.d_v_cmpl_prod_condition A"
 		+ " LEFT JOIN (SELECT CMPL_PROD_TYPE_PAR_CODE, COUNT(1) CNT"
 		+ " FROM tbas_dm.d_v_cmpl_prod_condition"
 		+ " GROUP BY CMPL_PROD_TYPE_PAR_CODE) C"
 		+ " ON A.CMPL_PROD_TYPE_CODE = C.CMPL_PROD_TYPE_PAR_CODE"
 		+ " WHERE A.CMPL_PROD_TYPE_CODE = ?";
	    Object[] proParams ={endId};
	   return getDataAccess().queryForList(sql, proParams);
    }
}
