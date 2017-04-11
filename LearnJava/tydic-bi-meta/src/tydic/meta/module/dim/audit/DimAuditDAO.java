package tydic.meta.module.dim.audit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.common.Common;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.tbl.TblCommon;
import tydic.meta.module.tbl.TblConstant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description 维度表类审核DAO
 * @date: 12-1-31
 * @time: 上午9:55
 */
public class DimAuditDAO extends MetaBaseDAO {
    /**
     * 按照输入条件查询数据
     *
     * @param map<String,Object>,包含审核状态
     * @return List 包含了页面需要展示的数据外,还包含了表类的id和类型的id
     */
    public List<Map<String, Object>> queryDimTableByCondition(Map<String, Object> map, Page page) {
        if (map != null && map.size() != 0) {
            StringBuffer sqlBuffer = new StringBuffer("SELECT DISTINCT DECODE(AUDITNUMB,0,'所有未审核',ALLNUMB,'已审核完成','部分已审核('||AUDITNUMB||'/'||ALLNUMB||')') AUDIT_RES," +
                    "  DECODE(AUDITNUMB,0,'1',ALLNUMB,'2','3') AUDIT_FLAG," +
                    "  DD.*,B.TABLE_NAME,B.TABLE_NAME_CN TABLE_NAMECN,B.TABLE_OWNER,C.USER_NAMECN,A.TABLE_DIM_PREFIX FROM ( SELECT   A.DIM_TABLE_ID, A.USER_ID, " +
                    "  TO_CHAR(A.MOD_DATE,'YYYY-MM-DD HH24:MI:SS') MOD_DATE, A.BATCH_ID, CASE A.MOD_FLAG  WHEN  '2'  THEN '2' WHEN  '8' THEN '2' WHEN '9' " +
                    "  THEN '2' ELSE  '1' END AS CODE_ITEM,SUM(DECODE(A.AUDIT_FLAG,1,1,2,1,0)) AUDITNUMB,COUNT(1) ALLNUMB" +
                    "  FROM META_DIM_TAB_MOD_HIS A  WHERE A.MOD_FLAG <> '0' GROUP BY A.DIM_TABLE_ID, A.USER_ID, A.MOD_DATE, A.BATCH_ID," +
                    "  CASE A.MOD_FLAG WHEN '2' THEN '2' WHEN '8' THEN '2' WHEN '9' THEN '2' ELSE '1' END  ) DD," +
                    "  META_DIM_TABLES A, META_TABLES B,META_MAG_USER C WHERE A.DIM_TABLE_ID = DD.DIM_TABLE_ID " +
                    "  AND A.DIM_TABLE_ID = B.TABLE_ID AND DD.USER_ID = C.USER_ID ");
            List<Object> params = new ArrayList<Object>();
            if (map.containsKey("auditState") && map.get("auditState") != null && !map.get("auditState").toString().trim().equals("")) {
                if (map.get("auditState").toString().trim().equals("1")) {
                    sqlBuffer.append(" AND AUDITNUMB = 0");
                } else if (map.get("auditState").toString().trim().equals("2")) {
                    sqlBuffer.append(" AND AUDITNUMB = ALLNUMB");
                } else if (map.get("auditState").toString().trim().equals("3")) {
                    sqlBuffer.append(" AND AUDITNUMB < ALLNUMB AND AUDITNUMB > 0");
                }
            }
            if (map.containsKey("auditStyle") && map.get("auditStyle") != null && !map.get("auditStyle").toString().equals("")) {
                //编码归并申请查询
                sqlBuffer.append(" AND CODE_ITEM = ? ");
                params.add(map.get("auditStyle") == null ? null : Integer.parseInt(map.get("auditStyle").toString()));
            }
            //申请人查询
            if (map.containsKey("auditApplyer") && map.get("auditApplyer") != null && !map.get("auditApplyer").toString().trim().equals("")) {
                sqlBuffer.append(" AND USER_NAMECN LIKE  " + SqlUtils.allLikeParam(map.get("auditApplyer").toString()));
//                params.add("%"+map.get("auditApplyer").toString()+"%");
            }
            sqlBuffer.append(" ORDER BY MOD_DATE DESC");
            String sql = sqlBuffer.toString();
            if (page != null) {
                sql = SqlUtils.wrapPagingSql(sql, page);
            }
            return getDataAccess().queryForList(sql, params.toArray());
        }
        return null;
    }

//    public List<Map<String,Object>> queryDimTree(String tableName){
//        String sql = "SELECT T.ZONE_ID,T.ZONE_PAR_ID,T.ZONE_CODE,T.ZONE_NAME,T.ZONE_DESC, " +
//                     "T.DIM_TYPE_ID,T.STATE,T.DIM_LEVEL,T.MOD_FLAG,T.ORDER_ID FROM META_DIM_ZONE T WHERE T.STATE=1 ";
//        return getDataAccess().queryForList(sql);
//    }


    //根据dimTableId 获取维度表信息
    public Map<String, Object> queryDimTableInfo(int dimTableId) {
        String sql = "SELECT T.TABLE_NAME,T.DATA_SOURCE_ID,T.TABLE_DIM_LEVEL, " +
                "T.TABLE_DIM_PREFIX,T.DIM_PAR_KEY_COL_ID,T.DIM_PAR_KEY_COL_ID, " +
                "T.DIM_TABLE_ID,T.LAST_LEVEL_FLAG from META_DIM_TABLES T WHERE T.DIM_TABLE_ID=" + dimTableId;
        return getDataAccess().queryForMap(sql);
    }

    /**
     * 查询一个归并类型的完整树形数据。
     * HIS==1 历史表数据，即修改后的数据
     * HIS==2 历史表数据，修改后数据，且为新增类的编码
     * HIS==3 原生数据信息，修改了几次便有几条
     * HIS==4 修改后节点一次递归到其根节点的数据
     *
     * @param tableName
     * @param dimTablePrefix
     * @param dimTypeId      归并类型
     * @param
     * @return
     */
    public List<Map<String, Object>> queryDimTreeData(String tableName, String dimTablePrefix, String tableOwner, int dimTypeId, String batchId) {
        StringBuffer queryCols = new StringBuffer("A." + dimTablePrefix + "_ID,A." + dimTablePrefix + "_PAR_CODE,A." + dimTablePrefix + "_PAR_ID, '' PATH_NAME, ");
        queryCols.append("A." + dimTablePrefix + "_NAME,A." + dimTablePrefix + "_CODE, ");
        queryCols.append("A." + dimTablePrefix + "_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL, C.MOD_FLAG,A.ORDER_ID, 3 HIS, HIS_ID,'' DIM_TYPE_ID  ");
        String sql = "SELECT A.ITEM_ID " + dimTablePrefix + "_ID,A.ITEM_PAR_CODE " + dimTablePrefix + "_PAR_CODE, A.ITEM_PAR_ID " + dimTablePrefix + "_PAR_ID,  '' PATH_NAME,  A.ITEM_NAME  " + dimTablePrefix + "_NAME," +
                " A.ITEM_CODE  " + dimTablePrefix + "_CODE,  A.ITEM_DESC  " + dimTablePrefix + "_DESC,  A.DIM_TYPE_ID,  A.STATE,  TO_NUMBER(A.DIM_LEVEL) DIM_LEVEL," +
                " A.MOD_FLAG, A.ORDER_ID,  1 HIS,HIS_ID,'' DIM_TYPE_ID  FROM META_DIM_TAB_MOD_HIS A  WHERE A.BATCH_ID = ? AND A.DIM_TYPE_ID = " + dimTypeId +
                " AND A.AUDIT_FLAG = 0 UNION  SELECT A.ITEM_ID " + dimTablePrefix + "_ID, A.ITEM_PAR_CODE " + dimTablePrefix + "_PAR_CODE, A.ITEM_PAR_ID " + dimTablePrefix + "_PAR_ID, '' PATH_NAME,  '----' " + dimTablePrefix + "_NAME," +
                " '----' " + dimTablePrefix + "_CODE,  '----' " + dimTablePrefix + "_DECS,  A.DIM_TYPE_ID,  A.STATE,  TO_NUMBER(A.DIM_LEVEL) DIM_LEVEL," +
                " A.MOD_FLAG,  A.ORDER_ID,   2 HIS,HIS_ID,'' DIM_TYPE_ID  FROM META_DIM_TAB_MOD_HIS A  WHERE A.BATCH_ID = ? AND A.AUDIT_FLAG = 0 AND A.DIM_TYPE_ID = " + dimTypeId +
                " AND A.MOD_FLAG in ('4','6') UNION SELECT " + queryCols + " FROM " + tableOwner + "." + tableName + " A,META_DIM_TAB_MOD_HIS C WHERE A." + dimTablePrefix + "_ID = C.ITEM_ID  AND C.BATCH_ID = ? AND A.DIM_TYPE_ID = " + dimTypeId +
                " AND C.AUDIT_FLAG = 0 " +
                " UNION SELECT DISTINCT A." + dimTablePrefix + "_ID,A." + dimTablePrefix + "_PAR_CODE,A." + dimTablePrefix + "_PAR_ID,  SUBSTR(sys_connect_by_path(A." + dimTablePrefix + "_NAME,'->'),3) PATH_NAME, NVL((SELECT B.ITEM_NAME  FROM META_DIM_TAB_MOD_HIS B WHERE B.ITEM_ID =" +
                " A." + dimTablePrefix + "_ID AND B.BATCH_ID = " + batchId + " AND B.MOD_FLAG != '0'), A." + dimTablePrefix + "_NAME),A." + dimTablePrefix + "_CODE, A." + dimTablePrefix + "_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL ,'-1' MOD_FLAG, A.ORDER_ID,4 HIS,0 HIS_ID, '' DIM_TYPE_ID FROM " +
                tableOwner + "." + tableName + " A  START WITH A." + dimTablePrefix + "_ID IN (SELECT A.ITEM_PAR_ID  FROM META_DIM_TAB_MOD_HIS A WHERE A.BATCH_ID = ? AND A.AUDIT_FLAG = 0 AND A.DIM_TYPE_ID = " + dimTypeId + ")  CONNECT BY PRIOR " +
                " A." + dimTablePrefix + "_PAR_ID = A." + dimTablePrefix + "_ID" +
                " ORDER BY " + dimTablePrefix + "_ID,HIS DESC";
        List<Object> params = new ArrayList<Object>();
        params.add(batchId);
        params.add(batchId);
        params.add(batchId);
        params.add(batchId);
        return getDataAccess().queryForList(sql, params.toArray());
    }


    /**
     * 查询归并编码审核后的数据
     *
     * @param tableName
     * @param dimTablePrefix
     * @param dimTypeId      归并类型
     * @param
     * @return
     */
    public List<Map<String, Object>> queryDimInfoData(String tableName, String dimTablePrefix, String tableOwner, int dimTypeId, String batchId) {
        StringBuffer queryCols = new StringBuffer("A." + dimTablePrefix + "_ID,A." + dimTablePrefix + "_PAR_ID, '' PATH_NAME,TO_CHAR(A.AUDIT_FLAG) AUDIT_FLAG,  A.MOD_MARK,  B.USER_NAMECN, ");
        queryCols.append("A." + dimTablePrefix + "_NAME,A." + dimTablePrefix + "_CODE, ");
        queryCols.append("A." + dimTablePrefix + "_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL, C.MOD_FLAG,A.ORDER_ID, '3' HIS, HIS_ID  ");
        String sql = "SELECT A.ITEM_ID " + dimTablePrefix + "_ID, A.ITEM_PAR_ID " + dimTablePrefix + "_PAR_ID,  '' PATH_NAME,TO_CHAR(A.AUDIT_FLAG) AUDIT_FLAG,  A.MOD_MARK,  B.USER_NAMECN,  A.ITEM_NAME  " + dimTablePrefix + "_NAME," +
                " A.ITEM_CODE  " + dimTablePrefix + "_CODE,  A.ITEM_DESC  " + dimTablePrefix + "_DESC,  A.DIM_TYPE_ID,  A.STATE,  TO_NUMBER(A.DIM_LEVEL) DIM_LEVEL," +
                " A.MOD_FLAG,  A.ORDER_ID,  DECODE(A.MOD_FLAG,'0','2','1') HIS,HIS_ID  FROM META_DIM_TAB_MOD_HIS A,META_MAG_USER B  WHERE A.BATCH_ID = ? AND A.AUDIT_USER_ID = B.USER_ID(+) AND A.DIM_TYPE_ID = " + dimTypeId +
                " AND A.AUDIT_FLAG <> 0 UNION  SELECT A.ITEM_ID " + dimTablePrefix + "_ID,  A.ITEM_PAR_ID " + dimTablePrefix + "_PAR_ID, '' PATH_NAME,'' AUDIT_FLAG,  A.MOD_MARK, B.USER_NAMECN,  '----' " + dimTablePrefix + "_NAME," +
                " '----' " + dimTablePrefix + "_CODE,  '----' " + dimTablePrefix + "_DECS,  A.DIM_TYPE_ID,  A.STATE,  TO_NUMBER(A.DIM_LEVEL) DIM_LEVEL," +
                " A.MOD_FLAG,  A.ORDER_ID,   '2' HIS,HIS_ID  FROM META_DIM_TAB_MOD_HIS A,META_MAG_USER B  WHERE A.BATCH_ID = ? AND A.AUDIT_USER_ID = B.USER_ID AND A.AUDIT_FLAG <> 0 AND A.DIM_TYPE_ID = " + dimTypeId +
                " AND SUBSTR(A.MOD_FLAG,0,1) NOT  in ('1','3','0')  UNION SELECT DISTINCT A." + dimTablePrefix + "_ID,A." + dimTablePrefix + "_PAR_ID,  SUBSTR(sys_connect_by_path(A." + dimTablePrefix + "_NAME,'->'),3) PATH_NAME,'' AUDIT_FLAG,  '' MOD_MARK, ''  USER_NAMECN, NVL((SELECT B.ITEM_NAME  FROM META_DIM_TAB_MOD_HIS B WHERE B.ITEM_ID =" +
                " A." + dimTablePrefix + "_ID AND B.BATCH_ID = " + batchId + " AND B.MOD_FLAG != '0'), A." + dimTablePrefix + "_NAME),A." + dimTablePrefix + "_CODE, A." + dimTablePrefix + "_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL ,'-1' MOD_FLAG, A.ORDER_ID,'4' HIS,0 HIS_ID FROM " +
                tableOwner + "." + tableName + " A  START WITH A." + dimTablePrefix + "_ID IN (SELECT A.ITEM_PAR_ID  FROM META_DIM_TAB_MOD_HIS A WHERE A.BATCH_ID = ? AND A.AUDIT_FLAG <> 0 AND A.MOD_FLAG <> '0'  AND A.DIM_TYPE_ID = " + dimTypeId + ")  CONNECT BY PRIOR " +
                " A." + dimTablePrefix + "_PAR_ID = A." + dimTablePrefix + "_ID" +
                " ORDER BY " + dimTablePrefix + "_ID,HIS DESC";
        List<Object> params = new ArrayList<Object>();
        params.add(batchId);
        params.add(batchId);
        params.add(batchId);
        return getDataAccess().queryForList(sql, params.toArray());
    }


    /**
     * 查询编码节点。
     *
     * @param tableName
     * @param dimTablePrefix
     * @param dimTypeId      归并类型
     * @param
     * @return
     */
    public List<Map<String, Object>> queryDimPathData(String tableName, String dimTablePrefix, String tableOwner, int dimTypeId, String batchId) {
        StringBuffer queryCols = new StringBuffer("A." + dimTablePrefix + "_ID,A." + dimTablePrefix + "_PAR_ID,");
        queryCols.append("A." + dimTablePrefix + "_NAME,A." + dimTablePrefix + "_CODE, ");
        queryCols.append("A." + dimTablePrefix + "_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL, '-1'  MOD_FLAG,A.ORDER_ID, 3 HIS, 0 HIS_ID  ");
        String sql = "SELECT  DD.*,SUBSTR(sys_connect_by_path(" + dimTablePrefix + "_NAME, '->'), 3)  PATH_NAME FROM (" +
                "SELECT DISTINCT " + queryCols + " FROM " + tableOwner + "." + tableName + " A START WITH A." + dimTablePrefix + "_ID IN (SELECT C.ITEM_ID FROM META_DIM_TAB_MOD_HIS C" +
                " WHERE C.BATCH_ID = " + batchId + " AND C.AUDIT_FLAG = 0  AND C.DIM_TYPE_ID = " + dimTypeId + ") CONNECT BY PRIOR A." + dimTablePrefix + "_PAR_ID = A." + dimTablePrefix + "_ID) DD" +
                " START WITH " + dimTablePrefix + "_PAR_ID =0 CONNECT BY PRIOR " + dimTablePrefix + "_ID = " + dimTablePrefix + "_PAR_ID";

        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询审核后的编码节点。
     */
    public List<Map<String, Object>> queryDimPathInfo(String tableName, String dimTablePrefix, String tableOwner, int dimTypeId, String batchId) {
        StringBuffer queryCols = new StringBuffer("A." + dimTablePrefix + "_ID,A." + dimTablePrefix + "_PAR_ID,");
        queryCols.append("A." + dimTablePrefix + "_NAME,A." + dimTablePrefix + "_CODE, ");
        queryCols.append("A." + dimTablePrefix + "_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL, '-1'  MOD_FLAG,A.ORDER_ID, 3 HIS, 0 HIS_ID  ");
        String sql = "SELECT  DD.*,SUBSTR(sys_connect_by_path(" + dimTablePrefix + "_NAME, '->'), 3)  PATH_NAME FROM (" +
                "SELECT DISTINCT " + queryCols + " FROM " + tableOwner + "." + tableName + " A START WITH A." + dimTablePrefix + "_ID IN (SELECT C.ITEM_ID FROM META_DIM_TAB_MOD_HIS C" +
                " WHERE C.BATCH_ID = " + batchId + " AND C.AUDIT_FLAG <> 0  AND C.DIM_TYPE_ID = " + dimTypeId + ") CONNECT BY PRIOR A." + dimTablePrefix + "_PAR_ID = A." + dimTablePrefix + "_ID) DD" +
                " START WITH " + dimTablePrefix + "_PAR_ID =0 CONNECT BY PRIOR " + dimTablePrefix + "_ID = " + dimTablePrefix + "_PAR_ID";

        return getDataAccess().queryForList(sql);
    }


    /**
     * 获取表的动态字段的值
     */
    public List<Map<String, Object>> queryDimCols(String tableName, String dimTablePrefix, String tableOwner, String batchId
            , int dimTypeId, String hisdyColums, List<String> dycols) {
        String dimdyColums = "";
        for (int i = 1; i <= dycols.size(); i++) {
            if (i == dycols.size()) {
                hisdyColums += "COL" + i;
                dimdyColums += "TO_CHAR(B." + dycols.get(i - 1) + ") " + dycols.get(i - 1);
                // adddyColums += "'----'";
            } else {
                hisdyColums += "COL" + i + ",";
                dimdyColums += "TO_CHAR(B." + dycols.get(i - 1) + ") " + dycols.get(i - 1) + ",";
                //   adddyColums += "'----',";
            }
        }
        String sql = "SELECT A.ITEM_ID,'DIM' DATA_TYPE," + dimdyColums + " FROM " + tableOwner + "." + tableName + " B,META_DIM_TAB_MOD_HIS A WHERE " +
                " A.BATCH_ID = " + batchId + "  AND A.ITEM_ID =" + dimTablePrefix + "_ID UNION  " +
                " SELECT A.ITEM_ID,'HIS' DATA_TYPE," + hisdyColums + " FROM META_DIM_TAB_MOD_HIS A WHERE A.DIM_TYPE_ID = " + dimTypeId + " AND A.AUDIT_FLAG = 0" +
                " AND A.BATCH_ID = " + batchId;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 更新维度记录表中维度的基本记录(除动态字段的以外的所有字段)
     *
     * @param map<String,Object> 包含维度表中的基本字段(P代表维度表的前缀)
     *                           PId维度记录的id PParId 维度记录的父Id
     *                           PCode 记录的code PName 维度记录的名称
     *                           PDesc 记录的描述    dimTypeId 归并类型的id
     *                           state 记录是否有效  dimLevel 归并类型下的层级
     * @param tableName          维度表的名称
     * @param prefix             维度表的前缀
     */
    public int updateDimBaseRecord(Map<String, Object> map, String tableName, String tableOwner, String prefix) {
        StringBuffer sb = new StringBuffer();
        String sql = null;
        if (tableName != null && prefix != null) {
            sb.append(" UPDATE " + tableOwner + "." + tableName + " T SET ");
            sb.append(prefix + "_PAR_ID=?, ");
            sb.append(prefix + "_CODE=?, ");
            sb.append(prefix + "_NAME=?, ");
            sb.append(prefix + "_DESC=?, DIM_TYPE_ID=?, STATE=?, DIM_LEVEL=?,DIM_TABLE_ID=? WHERE " + prefix + "_ID=?");
            sql = sb.toString();
            Object[] proParams = new Object[]{
                    map.get("itemParId"), map.get("itemCode"), map.get("itemName"), map.get("itemDesc"), map.get("dimTypeId"), map.get("state"),
                    map.get("dimLevel"), map.get("dimTableId"), map.get("itemId")};
            DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
            return dataAccess.execUpdate(sql, proParams);
        }
        return 0;
    }

    /**
     * 更改维度表中的动态字段或者其他以key:value存在的,key并不明确的记录
     *
     * @param Map<String,Object> key为数据库中的字段 value为更改之后的值
     * @param tableName          当前维度表的名称
     * @param PKName             维度表的主键名称或者能确定维度的记录的标示
     * @param ItemId             维度表标示的值
     *                           *
     */
    public int updateDimDynRecord(Map<String, Object> map, String tableName, String tableOwner, String PKName, long itemId) {
        if (map != null && map.size() != 0) {
            List<Object> proParams = new ArrayList<Object>();
            StringBuffer sb = new StringBuffer();
            sb.append(" UPDATE " + tableOwner + "." + tableName + " T SET ");
            Set<String> keySet = map.keySet();
            if (keySet != null && !keySet.isEmpty()) {
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next() + "=?, ");
                    proParams.add(map.get(iterator.next()));
                }
            }
            String sql = sb.toString();
            int len = sql.length();
            sql = sql.substring(0, len - 1);
            sql += " WHERE T." + PKName + "=" + itemId;
            DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
            return dataAccess.execUpdate(sql, proParams);
        }
        return 0;
    }

    /**
     * 包含维度表中的基本字段(P代表维度表的前缀)
     *
     * @param data           PId维度记录的id PParId 维度记录的父Id
     *                       PCode 记录的code PName 维度记录的名称
     *                       PDesc 记录的描述    dimTypeId 归并类型的id
     *                       state 记录是否有效  dimLevel 归并类型下的层级以及动态字段。
     * @param tableName      维表名称
     * @param prefix         维表前缀
     * @param dymaicColNames 动态字段集合
     * @author:张伟
     */
    public void updataAllDimTableData(Map<String, Object> data, String tableName, String prefix, String tableOwner, List<String> dymaicColNames) {
        StringBuffer sb = new StringBuffer();
        String sql = null;
        List params = null;
        if (tableName != null && prefix != null) {
            Object[] proParams = new Object[]{
                    data.get("itemParId"), data.get("itemCode"), data.get("itemName"), data.get("itemParCode"), data.get("itemDesc"), data.get("dimTypeId"), data.get("state"),
                    data.get("dimLevel"), data.get("dimTableId")};
            params = new ArrayList(Arrays.asList(proParams));
            sb.append(" UPDATE " + tableOwner + "." + tableName + " T SET ");
            sb.append(prefix + "_PAR_ID=?, ");
            sb.append(prefix + "_CODE=?, ");
            sb.append(prefix + "_NAME=?, ");
            sb.append(prefix + "_PAR_CODE=?,");
            sb.append(prefix + "_DESC=?, DIM_TYPE_ID=?, STATE=?, DIM_LEVEL=? ,DIM_TABLE_ID=? ");
            if (dymaicColNames != null && dymaicColNames.size() > 0) {
                for (String col : dymaicColNames) {
                    sb.append("," + col + "=? ");
                    params.add(data.get(Common.tranColumnToJavaName(col)));
                }
            }
            sb.append(" WHERE " + prefix + "_ID=?");
            params.add(data.get("itemId"));
            sql = sb.toString();
            DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
            dataAccess.execUpdate(sql, params.toArray());
        }
    }


    /**
     * 对于同一个归并编码是否已经有审核记录
     *
     * @param batchId        批次号
     * @param itemId         维度表ID值
     * @param tableName      表名
     * @param prefix         前缀
     * @param dymaicColNames 动态字段集
     * @author 杨苏维
     */
    public int hasAuditList(String batchId, long itemId) {

        String sql = "SELECT COUNT(1) FROM META_DIM_TAB_MOD_HIS A WHERE A.BATCH_ID = ?" +
                " AND A.ITEM_ID = ? AND A.MOD_FLAG = '0'";

        return getDataAccess().queryForInt(sql, batchId, itemId);

    }


    /**
     * 审核之后，对于维度层次修改维度记录修改类型，记录其修改前的维度表数据，便于查看
     *
     * @param batchId        批次号
     * @param itemId         维度表ID值
     * @param tableName      表名
     * @param prefix         前缀
     * @param dymaicColNames 动态字段集
     * @author 张伟
     */
    public void insertHisDataAfterAudit(String batchId, long dimTableId,
                                        long itemId, String tableName, String prefix, String tableOwner,
                                        List<String> dymaicColNames) {
        if (hasAuditList(batchId, itemId) > 0) {

        } else {
            String sql = "INSERT INTO META_DIM_TAB_MOD_HIS(ITEM_ID,ITEM_NAME,ITEM_PAR_ID,ITEM_CODE,DIM_TYPE_ID,DIM_LEVEL,MOD_FLAG, "
                    + "DIM_TABLE_ID,ITEM_DESC,ORDER_ID, STATE,HIS_ID,AUDIT_FLAG,BATCH_ID";
            if (dymaicColNames != null && dymaicColNames.size() > 0) {
                int count = 1;
                for (String col : dymaicColNames) {
                    sql += (",COL" + (count++));
                }
            }
            sql += ") SELECT " + prefix + "_ID," + prefix + "_NAME," + prefix + "_PAR_ID," + prefix + "_CODE,DIM_TYPE_ID,DIM_LEVEL" + ","
                    + DimConstant.DIM_MODE_FALG + ",?, " + "" + prefix + "_DESC,ORDER_ID,STATE,SEQ_DIM_TAB_MOD_HIS_ID.NEXTVAL,"
                    + DimConstant.DIM_HAS_AUDIT_PASS + ",?";
            if (dymaicColNames != null && dymaicColNames.size() > 0) {
                for (String col : dymaicColNames) {
                    sql += (",'colName:" + col + ",value:'||" + col);
                }
            }
            sql += " FROM " + tableOwner + "." + tableName + " WHERE " + prefix + "_ID=? ";
            getDataAccess().execUpdate(sql, dimTableId, batchId, itemId);

        }
    }


    /**
     * 数据插入当前维度表
     *
     * @param dimTableName   维度表名称
     * @param dimTablePrefix 维度表前缀
     * @param itemId         维度节点的itemId
     * @param parId          维度节点的父Id
     * @param itemCode       维度节点的code
     * @param itemName       维度节点的名称
     * @param itemDesc       维度节点的描述
     * @param typeId         维度节点的归并类型id
     * @param levelId        维度节点归并类型层级的id
     * @param orderId        排序id
     */
    public int insertDimData(String dimTableName, String dimTablePrefix, String tableOwner, long itemId, Map<String, Object> dataMap, List<String> dymaicColNames) {
        StringBuffer sb = new StringBuffer();
        List params = null;
        String dynValue = ","; //生成动态字段的"?"号
        Object[] proParams = new Object[]{itemId,
                dataMap.get("itemParId"), dataMap.get("itemCode"), dataMap.get("itemName"), dataMap.get("dimTypeId"), dataMap.get("state"),
                dataMap.get("dimLevel"), dataMap.get("dimTableId"), dataMap.get("orderId"), dataMap.get("itemParCode")};
        params = new ArrayList(Arrays.asList(proParams));
        sb.append("INSERT INTO " + tableOwner + "." + dimTableName + "(");
        sb.append(dimTablePrefix + "_ID,");
        sb.append(dimTablePrefix + "_PAR_ID,");
        sb.append(dimTablePrefix + "_CODE,");
        sb.append(dimTablePrefix + "_NAME,");
        sb.append("DIM_TYPE_ID,STATE,DIM_LEVEL,DIM_TABLE_ID,MOD_FLAG,ORDER_ID,");
        sb.append(dimTablePrefix + "_PAR_CODE");
        if (dataMap.get("itemDesc") != null) {
            sb.append("," + dimTablePrefix + "_DESC");
            params.add(dataMap.get("itemDesc"));
            dynValue += "?,";
        }
        if (dymaicColNames != null && dymaicColNames.size() > 0) {
            for (String col : dymaicColNames) {
                Object value = dataMap.get(Common.tranColumnToJavaName(col));
                if (value != null) {
                    sb.append("," + col);
                    params.add(dataMap.get(Common.tranColumnToJavaName(col)));
                    dynValue += "?,";
                }
            }
        }
        sb.append(") VALUES(?,?,?,?,?,?,?,?,1,?,?"); //固定字段的值插入
        int len = dynValue.length();
        dynValue = dynValue.substring(0, len - 1);
        sb.append(dynValue + ")");
        String sql = sb.toString();
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql, params.toArray());

    }

    /**
     * 数据插入当前维度表(审核映射时使用)
     *
     * @param dimTableName   维度表名称
     * @param dimTablePrefix 维度表前缀
     * @param proParams（参数集） {
     * @param itemId         维度节点的itemId
     * @param parId          维度节点的父Id
     * @param itemCode       维度节点的code
     * @param itemName       维度节点的名称
     * @param itemDesc       维度节点的描述
     * @param typeId         维度节点的归并类型id
     * @param tableId        维度表表id
     * @param levelId        维度节点归并类型层级的id
     * @param orderId        排序id
     *                       }
     * @author 李国民
     */
    public int insertDimDataForMappAudit(String dimTableName, String dimTablePrefix, String tableOwner, Object[] proParams, List<Map<String,Object>> nameList) {
        StringBuffer sb = new StringBuffer();
        String dynValue = ","; //生成动态字段的"?"号
        sb.append("INSERT INTO " + tableOwner + "." + dimTableName + "(");
        sb.append(dimTablePrefix + "_ID,");
        sb.append(dimTablePrefix + "_PAR_ID,");
        sb.append(dimTablePrefix + "_CODE,");
        sb.append(dimTablePrefix + "_PAR_CODE,");
        sb.append(dimTablePrefix + "_NAME,");
        sb.append(dimTablePrefix + "_DESC,DIM_TYPE_ID,DIM_TABLE_ID,STATE,DIM_LEVEL,MOD_FLAG,ORDER_ID");
        if (nameList != null && nameList.size() > 0) {
            for (Map<String,Object> col : nameList) {
                sb.append("," + MapUtils.getString(col,"COL_NAME"));
                String  dataType=MapUtils.getString(col,"DATA_TYPE");
                dynValue+= (dataType.equalsIgnoreCase("DATE")?"SYSDATE":(TblCommon.isNumber(dataType)?0:"' '"))+",";
            }
        }
        sb.append(") VALUES(?,?,?,?,?,?,?,?,1,?,0,?");//固定字段的值插入
        int len = dynValue.length();
        dynValue = dynValue.substring(0, len - 1);
        sb.append(dynValue + ")");
        String sql = sb.toString();
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql, proParams);

    }

    /**
     * 删除维度表中的特定数据
     *
     * @param dimTableName   维度表的名字
     * @param dimTablePrefix 维度表的前缀
     * @param itemCode       当前节点的code
     * @param typeId         维度节点归并类型的id
     */
    public int deleteDimData(String dimTableName, String dimTablePrefix, String tableOwner, String itemCode, int typeId) {
        String sql = "DELETE FROM " + tableOwner + "." + dimTableName + " T WHERE T." + dimTablePrefix + "_CODE=? AND DIM_TYPE_ID=?";
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql, itemCode, typeId);
    }

    /**
     * 删除维度表中的特定数据（末级显示时使用）
     *
     * @param dimTableName   维度表的名字
     * @param dimTablePrefix 维度表的前缀
     * @param itemCode       当前节点的code
     * @param typeId         维度节点归并类型的id
     */
    public int deleteDimData(String dimTableName, String dimTablePrefix, String tableOwner, String itemCode) {
        String sql = "DELETE FROM " + tableOwner + "." + dimTableName + " T WHERE T." + dimTablePrefix + "_CODE=?";
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql, itemCode);
    }

    /**
     * 查询某批次改动涉及到的归并类型
     *
     * @param batchId 批次号
     * @param isAudit 是否已经审核，如果为true表示查询通过审核的所有归并类型，如果是false表示查询某批次改动涉及
     *                到的所有归并类型
     * @return
     * @author 张伟
     */
    public List<Map<String, Object>> queryDimTypeOnModify(String batchId, boolean isAudit) {
        String sql = "SELECT A.DIM_TYPE_ID,DIM_TYPE_NAME,DIM_TYPE_DESC,DIM_TYPE_CODE FROM META_DIM_TYPE A WHERE DIM_TYPE_ID IN " +
                "(SELECT DISTINCT DIM_TYPE_ID FROM META_DIM_TAB_MOD_HIS WHERE BATCH_ID=?  ";
        //查询通过审核的归并类型
        if (isAudit) {
            sql += "AND MOD_FLAG!='" + DimConstant.DIM_NOT_AUDIT + "'";
        }
        sql += ")";
        return getDataAccess().queryForList(sql, batchId);
    }

    /**
     * 更新当前维度表中的某一个字节点的子节点
     *
     * @param dimTableName   维度表的名字
     * @param dimTablePrefix 维度表的前缀
     * @param parNewId       新生成的itemId,用于更新新插入中的维度节点中的父Id
     * @param parId          维度记录中原来的父id,新生成的为负数
     * @param typeId         归并类型的id,只更新该归并下的id
     */
    public int updateDimDataByParId(String dimTableName, String dimTablePrefix, String tableOwner, long parNewId, long parId, int typeId) {
        String sql = "UPDATE " + tableOwner + "." + dimTableName + " SET " + dimTablePrefix + "_PAR_ID=? WHERE " + dimTablePrefix + "_PAR_ID=? AND DIM_TYPE_ID=?";
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql, parNewId, parId, typeId);
    }

    /**
     * 通过父id删除子节点
     *
     * @param dimTableName   维度表的名称
     * @param dimTablePrefix 维度表的前缀
     * @param parId          父id
     */
    public int deleteDimDataByParId(String dimTableName, String dimTablePrefix, String tableOwner, long parId) {
        String sql = "DELETE FROM " + tableOwner + "." + dimTableName + " T WHERE T." + dimTablePrefix + "_PAR_ID=" + parId;
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql);
    }

    /**
     * 更改当前节点的有效无效的状态
     *
     * @param dimTableName   维度表的名称
     * @param dimTablePrefix 维度表的前缀
     * @param itemId         节点的id
     * @param state          启用或者停用的标示
     */
    public int updateDimStateByItemId(String dimTableName, String dimTablePrefix, String tableOwner, long itemId, int state) {
        String sql = "UPDATE " + tableOwner + "." + dimTableName + " SET STATE=? WHERE " + dimTablePrefix + "_ID=?";
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql, state, itemId);
    }

    /**
     * 更改当前节点下子节点的状态
     *
     * @param dimTableName   维度表的名称
     * @param dimTablePrefix 维度表的前缀
     * @param itemId         节点的id
     * @param state          启用或者停用的标示
     */
    public int updateDimStateByParId(String dimTableName, String dimTablePrefix, String tableOwner, long parId, int state) {
        String sql = "UPDATE " + tableOwner + "." + dimTableName + " SET STATE=? WHERE " + dimTablePrefix + "_PAR_ID=?";
        DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
        return dataAccess.execUpdate(sql, state, parId);
    }

    /**
     * 查找当前节点下子节点的code
     *
     * @param dimTableName   维度表的名称
     * @param dimTablePrefix 维度表的前缀
     * @param itemId         节点的id
     * @param state          启用或者停用的标示
     */
    public List<Map<String, Object>> queryDimCodeByParId(String dimTableName, String dimTablePrefix, String tableOwner, long parId) {
        String sql = "SELECT " + dimTablePrefix + "_CODE FORM " + tableOwner + "." + dimTableName + " WHERE " + dimTablePrefix + "_PAR_ID=?";
        DataAccess dataAccess = getDataAccess();
        return dataAccess.queryForList(sql, parId);
    }

    /**
     * 更新某节点下所有父code
     *
     * @param dimTableName   维度表名称
     * @param dimTablePrefix 维度表前缀
     * @param tableOwner     表用户
     * @param oldParCode     原来的code
     * @param newParCode     现在的code
     * @param typeId         归并类型的id
     * @return
     */
    public int updateParCodeByItemCode(String dimTableName, String dimTablePrefix, String tableOwner, String oldParCode, String newParCode, int typeId) {
        String sql = " UPDATE " + tableOwner + "." + dimTableName + " T SET T." + dimTablePrefix + "_PAR_CODE=? WHERE T." + dimTablePrefix + "_PAR_CODE=? AND T.DIM_TYPE_ID=?";
        return this.getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).execUpdate(sql, newParCode, oldParCode, typeId);
    }

    /**
     * 更新某节点下所有父code
     *
     * @param dimTableName   维度表名称
     * @param dimTablePrefix 维度表前缀
     * @param tableOwner     表用户
     * @param newParCode     现在的code
     * @param itemId         节点的id
     * @return
     */
    public int updateParCodeByItemId(String dimTableName, String dimTablePrefix, String tableOwner, String newParCode, long itemId) {
        String sql = " UPDATE " + tableOwner + "." + dimTableName + " T SET T." + dimTablePrefix + "_PAR_CODE=? WHERE T." + dimTablePrefix + "_ID=?";
        return this.getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).execUpdate(sql, newParCode, itemId);
    }

    /**
     * 查询当前节点在原表中的CODE值
     *
     * @param dimTableName   维度表名称
     * @param dimTablePrefix 维度表前缀
     * @param tableOwner     表用户
     * @param itemId         节点的id
     */
    public String queryItemCodeByItemId(String dimTableName, String dimTablePrefix, String tableOwner, long ItemId) {
        String sql = "SELECT T." + dimTablePrefix + "_CODE FROM " + tableOwner + "." + dimTableName + " T WHERE T." + dimTablePrefix + "_ID=?";
        return this.getDataAccess().queryForString(sql, ItemId);
    }

}
