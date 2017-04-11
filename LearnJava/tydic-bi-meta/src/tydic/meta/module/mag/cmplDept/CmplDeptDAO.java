package tydic.meta.module.mag.cmplDept;

import tydic.frame.BaseDAO;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2013，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 该类是部门查询DAO类，用于连接数据库操作，供ZoneAction调用
 * @modifyDate  2013-07-04
 */
public class CmplDeptDAO extends BaseDAO {
    /**
     * 查询子业务类型
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubDeptCode(String parentCode){
        String select = "SELECT A.CMPL_DEPT_NAME,A.CMPL_DEPT_CODE,"
        +" A.CMPL_DEPT_PAR_CODE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM tbas_dm.d_v_cmpl_dept_condition A"
        +" LEFT JOIN (SELECT CMPL_DEPT_PAR_CODE, COUNT(1) CNT"
        +" FROM tbas_dm.d_v_cmpl_dept_condition"
        +" GROUP BY CMPL_DEPT_PAR_CODE) C"
        +" ON A.CMPL_DEPT_CODE = C.CMPL_DEPT_PAR_CODE"
        +" WHERE A.CMPL_DEPT_PAR_CODE = ?"
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
    public List<Map<String,Object>> queryDeptByPathCode(String beginId, String endId){	
 	   String sql = "SELECT A.CMPL_DEPT_NAME,A.CMPL_DEPT_CODE,"
        +" A.CMPL_DEPT_PAR_CODE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM tbas_dm.d_v_cmpl_dept_condition A"
        +" LEFT JOIN (SELECT CMPL_DEPT_PAR_CODE, COUNT(1) CNT"
        +" FROM tbas_dm.d_v_cmpl_dept_condition"
        +" GROUP BY CMPL_DEPT_PAR_CODE) C"
        +" ON A.CMPL_DEPT_CODE = C.CMPL_DEPT_PAR_CODE"
 		+ " WHERE A.CMPL_DEPT_CODE = ?";
	    Object[] proParams ={endId};
	   return getDataAccess().queryForList(sql, proParams);
    }
}
