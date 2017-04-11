package tydic.meta.module.dim.intRel;

import org.apache.poi.util.StringUtil;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.*;
import tydic.meta.module.dim.DimConstant;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:XXX <br>
 * @date 2011-11-19
 */
public class DimIntRelDAO extends MetaBaseDAO {

    /**
     * 查询映射状态为所有的接口映射值
     *
     * @param dimTableInfo 维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param queryMessage 查询信息，包含键值 keyWord:关键字，dimTypeId:归并类型ID，system:系统ID。
     * @return
     */
    public List<Map<String, Object>> queryAll(Map<String, Object> dimTableInfo, Map<String, Object> queryMessage, Integer parId) {
        int tableId = Integer.parseInt(dimTableInfo.get("dimTableId").toString());
        String tableName = Convert.toString(dimTableInfo.get("tableName"));
        String dimTablePrefix = Convert.toString(dimTableInfo.get("tableDimPrefix"));
        String tableOwner = Convert.toString(dimTableInfo.get("tableOwner"));
        String keyWord = Convert.toString(queryMessage.get("keyWord"));
        int dimTypeId = Integer.parseInt(queryMessage.get("dimTypeId").toString());
        int system = Integer.parseInt(queryMessage.get("system").toString());
        String sql = "SELECT A." + dimTablePrefix + "_ID ,A." + dimTablePrefix + "_PAR_ID ,A." + dimTablePrefix + "_CODE ,A." + dimTablePrefix + "_NAME ,A." + dimTablePrefix + "_DESC,A.DIM_LEVEL,A.MOD_FLAG," +
                "C.SRC_CODE,C.SRC_NAME,NVL(B.CNT,0) CHILDREN,NVL(D.CNT,0) NO_DISPLAY_COUNT,CASE WHEN SRC_CODE IS NULL AND B.CNT>0 THEN -1 " +
                "WHEN SRC_CODE IS NULL THEN 0 ELSE 1 END AS ISMAPPING FROM " + tableOwner + "." + tableName + " A " +
                "LEFT JOIN (SELECT " + dimTablePrefix + "_PAR_ID ,COUNT(*) CNT FROM " + tableOwner + "." + tableName +
                " WHERE MOD_FLAG= " + DimConstant.DIM_MODE_FALG_SHOW + " GROUP BY " + dimTablePrefix + "_PAR_ID ) " +
                "B ON A." + dimTablePrefix + "_ID =B." + dimTablePrefix + "_PAR_ID  " +
                " LEFT JOIN (SELECT " + dimTablePrefix + "_PAR_ID, COUNT(*) CNT " +
                " FROM " + tableOwner + "." + tableName + " WHERE MOD_FLAG= " + DimConstant.DIM_MODE_FALG + " GROUP BY " + dimTablePrefix + "_PAR_ID) D "
                + " ON A." + dimTablePrefix + "_ID =D." + dimTablePrefix + "_PAR_ID  " +
                "LEFT JOIN META_DIM_MAPP C ON A." + dimTablePrefix + "_CODE =C.ITEM_CODE AND C.SYS_ID=" + system + " AND C.DIM_TABLE_ID=" + tableId +
                " WHERE  A.DIM_TYPE_ID=" + dimTypeId + " AND MOD_FLAG= " + DimConstant.DIM_MODE_FALG_SHOW;
        if (parId != null) {
            keyWord = "";
        }
        if (keyWord != null && !keyWord.equals("")) {
            sql += " AND (A." + dimTablePrefix + "_CODE   LIKE "+SqlUtils.allLikeParam(keyWord) + " OR A." + dimTablePrefix + "_NAME LIKE "+SqlUtils.allLikeParam(keyWord)+") ";
        } else {
            if (parId == null) {
                parId = Constant.DEFAULT_ROOT_PARENT;
            }
            sql += " AND A." + dimTablePrefix + "_PAR_ID=" + parId;
        }
        sql += " ORDER BY A.DIM_LEVEL";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据父节点查询下面子节点的映射情况
     *
     * @param parId
     * @return
     */
    public List<Map<String, Object>> queryChildMapping(Map<String, Object> dimTableInfo, Map<String, Object> queryMessage, int parId) {
        int tableId = Integer.parseInt(dimTableInfo.get("dimTableId").toString());
        String tableName = Convert.toString(dimTableInfo.get("tableName"));
        String dimTablePrefix = Convert.toString(dimTableInfo.get("tableDimPrefix"));
        String tableOwner = Convert.toString(dimTableInfo.get("tableOwner"));
        int dimTypeId = Integer.parseInt(queryMessage.get("dimTypeId").toString());
        int system = Integer.parseInt(queryMessage.get("system").toString());
        String sql = "SELECT A." + dimTablePrefix + "_ID ,A." + dimTablePrefix + "_PAR_ID ,A." + dimTablePrefix + "_CODE ,A." + dimTablePrefix + "_NAME ,A." + dimTablePrefix + "_DESC,A.DIM_LEVEL,A.MOD_FLAG," +
                "C.SRC_CODE,C.SRC_NAME FROM " + tableOwner + "." + tableName + " A " +
                "LEFT JOIN META_DIM_MAPP C ON A." + dimTablePrefix + "_CODE =C.ITEM_CODE AND C.SYS_ID=" + system + " AND C.DIM_TABLE_ID=" + tableId +
                " WHERE  A.DIM_TYPE_ID=" + dimTypeId + " AND A." + dimTablePrefix + "_PAR_ID=" + parId;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询所有的未映射的编码
     *
     * @param dimTableInfo
     * @param queryMessage
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryUnMapping(Map<String, Object> dimTableInfo, Map<String, Object> queryMessage, Page page) {
        int tableId = Integer.parseInt(dimTableInfo.get("dimTableId").toString());
        String tableName = Convert.toString(dimTableInfo.get("tableName"));
        String dimTablePrefix = Convert.toString(dimTableInfo.get("tableDimPrefix"));
        String tableOwner = Convert.toString(dimTableInfo.get("tableOwner"));
        String keyWord = Convert.toString(queryMessage.get("keyWord"));
        int dimTypeId = Integer.parseInt(queryMessage.get("dimTypeId").toString());
        int system = Integer.parseInt(queryMessage.get("system").toString());
        String sql = "SELECT A." + dimTablePrefix + "_ID ,A." + dimTablePrefix + "_PAR_ID ,A." + dimTablePrefix + "_CODE ,A." + dimTablePrefix + "_NAME ,A." + dimTablePrefix + "_DESC,A.DIM_LEVEL,A.MOD_FLAG," +
                "C.SRC_CODE,C.SRC_NAME,B.CNT CHILD_COUNT,CASE WHEN SRC_CODE IS NULL AND B.CNT>0 THEN -1 " +
                "WHEN SRC_CODE IS NULL THEN 0 ELSE 1 END AS ISMAPPING FROM " + tableOwner + "." + tableName + " A " +
                "LEFT JOIN (SELECT " + dimTablePrefix + "_PAR_ID ,COUNT(*) CNT FROM " + tableOwner + "." + tableName + " GROUP BY " + dimTablePrefix + "_PAR_ID ) " +
                "B ON A." + dimTablePrefix + "_ID =B." + dimTablePrefix + "_PAR_ID  " +
                "LEFT JOIN META_DIM_MAPP C ON A." + dimTablePrefix + "_CODE =C.ITEM_CODE AND C.SYS_ID=" + system + " AND C.DIM_TABLE_ID=" + tableId +
                " WHERE  A.DIM_TYPE_ID=" + dimTypeId + " AND A.MOD_FLAG=" + DimConstant.DIM_MODE_FALG_SHOW
                + " AND SRC_CODE IS NULL AND NVL(B.CNT,0)=0 ";
        if (keyWord != null && !keyWord.equals("")) {
            sql += " AND (A." + dimTablePrefix + "_CODE   LIKE " +SqlUtils.allLikeParam(keyWord)+ " OR A." + dimTablePrefix + "_NAME LIKE "+SqlUtils.allLikeParam(keyWord)+") ";
        }
        sql += " ORDER BY A." + dimTablePrefix + "_ID";
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询未审核的数据
     *
     * @param dimTableInfo 维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param queryMessage 查询信息，包含键值 keyWord:关键字，dimTypeId:归并类型ID，system:系统ID。
     * @param isAudit      是否审核。
     * @return
     */
    public List<Map<String, Object>> queryByHis(Map<String, Object> dimTableInfo, Map<String, Object> queryMessage) {
        int tableId = Integer.parseInt(dimTableInfo.get("dimTableId").toString());
        String keyWord = Convert.toString(queryMessage.get("keyWord"));
        int dimTypeId = Integer.parseInt(queryMessage.get("dimTypeId").toString());
        String dimTablePrefix = Convert.toString(dimTableInfo.get("tableDimPrefix"));
        String tableOwner = Convert.toString(dimTableInfo.get("tableOwner"));
        int system = Integer.parseInt(queryMessage.get("system").toString());
        String sql = "SELECT M.SRC_CODE,M.SRC_NAME,C." + dimTablePrefix + "_ID ,C." + dimTablePrefix + "_PAR_ID ,C." + dimTablePrefix + "_CODE ," +
                " C." + dimTablePrefix + "_NAME ,C." + dimTablePrefix + "_DESC,C.DIM_LEVEL," +
                " decode(M.SRC_CODE,'',0,1) AS ISMAPPING,A.HIS_ID " +
                " FROM META_DIM_TAB_MOD_HIS  A " +
                " LEFT JOIN META_DIM_MAPP M ON A.SRC_CODE=M.SRC_CODE" +
                " LEFT JOIN " + tableOwner + "." + Convert.toString(dimTableInfo.get("tableName")) + " C ON A.ITEM_ID=C." + dimTablePrefix + "_ID " +
                " WHERE A.DIM_TYPE_ID=" + dimTypeId + " AND A.DIM_TABLE_ID=" + tableId + " AND A.SRC_SYS_ID=" + system + " AND A.AUDIT_FLAG= 0";
        if (StringUtils.isNotEmpty(keyWord)) {
            sql += "AND (A.SRC_CODE LIKE " +SqlUtils.allLikeParam(keyWord)+ " OR A.SRC_NAME LIKE "+SqlUtils.allLikeParam(keyWord)+") ";
        }
        return getDataAccess().queryForList(sql);
    }
    
    /**
     * 查询已映射的数据
     *
     * @param dimTableInfo 维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param queryMessage 查询信息，包含键值 keyWord:关键字，dimTypeId:归并类型ID，system:系统ID。
     * @param isAudit      是否审核。
     * @return
     */
    public List<Map<String, Object>> queryByHisIsAudit(Map<String, Object> dimTableInfo, Map<String, Object> queryMessage) {
        int tableId = Integer.parseInt(dimTableInfo.get("dimTableId").toString());
        String keyWord = Convert.toString(queryMessage.get("keyWord"));
        int dimTypeId = Integer.parseInt(queryMessage.get("dimTypeId").toString());
        String dimTablePrefix = Convert.toString(dimTableInfo.get("tableDimPrefix"));
        String tableOwner = Convert.toString(dimTableInfo.get("tableOwner"));
        int system = Integer.parseInt(queryMessage.get("system").toString());
        String sql = "SELECT M.SRC_CODE,M.SRC_NAME,C." + dimTablePrefix + "_ID ,C." + dimTablePrefix + "_PAR_ID ,C." + dimTablePrefix + "_CODE ," +
                " C." + dimTablePrefix + "_NAME ,C." + dimTablePrefix + "_DESC,C.DIM_LEVEL," +
                " decode(M.SRC_CODE,'',0,1) AS ISMAPPING,A.HIS_ID " +
                " FROM META_DIM_MAPP M " +
                " LEFT JOIN (select B.* from ( SELECT max(t.his_id) as his_id,t.src_code " +
	            	" from meta_dim_tab_mod_his t where t.DIM_TABLE_ID = " + tableId + " " +
	            	" and t.audit_flag = 1 and t.src_sys_id = " + system + " group by t.src_code) A" +
	            	" LEFT JOIN meta_dim_tab_mod_his B on B.his_id = A.his_id " +
            	" )A ON A.SRC_CODE=M.SRC_CODE" +
                " LEFT JOIN " + tableOwner + "." + Convert.toString(dimTableInfo.get("tableName")) + " C ON A.ITEM_ID=C." + dimTablePrefix + "_ID " +
                " WHERE A.DIM_TYPE_ID=" + dimTypeId + " AND A.DIM_TABLE_ID=" + tableId + " AND A.SRC_SYS_ID=" + system ;
        if (StringUtils.isNotEmpty(keyWord)) {
            sql += "AND (A.SRC_CODE LIKE " +SqlUtils.allLikeParam(keyWord)+ " OR A.SRC_NAME LIKE "+SqlUtils.allLikeParam(keyWord)+") ";
        }
        return getDataAccess().queryForList(sql);
    }
}
