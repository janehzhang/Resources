package tydic.meta.module.dim.mapping;

import java.util.*;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.TblConstant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 维度编码映射DAO <br>
 * @date 2011-11-16
 */
public class DimMappingDAO extends MetaBaseDAO {

    /**
     * 返回一个归并类型的完整数据的总数量
     *
     * @param tableName      维度表名
     * @param dimTablePrefix 表前缀
     * @param tableUser      表用户
     * @param dimTypeId      归并类型
     * @param lastLevelFalg  末级是否显示，1显示，0不显示
     * @return
     */
    public int queryDimDataCount(String tableName, String dimTablePrefix,
                                 String tableUser, int dimTypeId, int lastLevelFalg) {
        String sql = "SELECT count(*) as DIMCOUNT FROM " + tableUser + "." + tableName + " A " + (lastLevelFalg == 0 ?
                ("LEFT JOIN (SELECT " + dimTablePrefix + "_PAR_ID ,COUNT(*) CNT FROM " + tableName + " WHERE STATE=" +
                        TblConstant.META_DIM_TYPE_STATE_VALID + " GROUP BY " + dimTablePrefix + "_PAR_ID  ) B ON A." + dimTablePrefix + "_ID =B." + dimTablePrefix + "_PAR_ID  ") : "") +
                "WHERE A.STATE=" + TblConstant.META_DIM_TYPE_STATE_VALID + " ";
        if (dimTypeId != 0) {
            sql += " AND A.DIM_TYPE_ID =" + dimTypeId + " ";
        }
        sql += (lastLevelFalg == 0 ? " AND A.MOD_FLAG = 1 " : "" + " ORDER BY A." + dimTablePrefix + "_CODE");
        Map<String, Object> rs = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).queryForMap(sql);
        return Integer.parseInt(rs.get("DIMCOUNT").toString());
    }

    /**
     * 查询一个归并类型的完整树形数据。
     *
     * @param tableName
     * @param dimTablePrefix
     * @param dimTypeId      归并类型
     * @param lastLevelFalg  末级是否显示 1 显示，0 不显示
     * @param
     * @return
     */
    public List<Map<String, Object>> queryDimTreeData(String tableName, String dimTablePrefix,
                                                      String tableUser, int dimTypeId, int lastLevelFalg) {
        StringBuffer queryCols = new StringBuffer("A." + dimTablePrefix + "_ID AS ID,A." + dimTablePrefix + "_PAR_ID AS PAR_ID, ");
        queryCols.append("A." + dimTablePrefix + "_NAME AS NAME,A." + dimTablePrefix + "_CODE AS CODE, ");
        queryCols.append("A." + dimTablePrefix + "_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID ");
        String sql = "SELECT " + queryCols.toString() + " FROM " + tableUser + "." + tableName + " A " + (lastLevelFalg == 0 ?
                ("LEFT JOIN (SELECT " + dimTablePrefix + "_PAR_ID ,COUNT(*) CNT FROM " + tableUser + "." + tableName + " WHERE STATE=" +
                        TblConstant.META_DIM_TYPE_STATE_VALID + " GROUP BY " + dimTablePrefix + "_PAR_ID  ) B ON A." + dimTablePrefix + "_ID =B." + dimTablePrefix + "_PAR_ID  ") : "") +
                "WHERE A.STATE=" + TblConstant.META_DIM_TYPE_STATE_VALID + " AND A.DIM_TYPE_ID =" + dimTypeId + " "
                + (lastLevelFalg == 0 ? " AND A.MOD_FLAG = 1 " : "" + " ORDER BY A." + dimTablePrefix + "_CODE");
        return getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).queryForList(sql);
    }

    /**
     * 得到归并类型树某一层的数据（此方法为数据量过大，异步加载时使用）
     *
     * @param tableName
     * @param dimTablePrefix
     * @param tableUser
     * @param dimTypeId
     * @param lastLevelFalg
     * @return
     */
    public List<Map<String, Object>> getDimTreeData(String tableName, String dimTablePrefix,
                                                    String tableUser, int dimTypeId, int lastLevelFalg, long parId) {
        StringBuffer queryCols = new StringBuffer("A." + dimTablePrefix + "_ID AS ID,A." + dimTablePrefix + "_PAR_ID AS PAR_ID, ");
        queryCols.append("A." + dimTablePrefix + "_NAME AS NAME,A." + dimTablePrefix + "_CODE AS CODE,A." + dimTablePrefix + "_DESC,");
        queryCols.append("A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID,DECODE(NVL(B.CNT, 0), 0, 0, 1) as CHILDREN ");
        String sql = "SELECT " + queryCols.toString() + " FROM " + tableUser + "." + tableName + " A " +
                " LEFT JOIN (SELECT Z." + dimTablePrefix + "_PAR_ID ,COUNT(*) CNT FROM " + tableUser + "." + tableName + " Z" +
                " WHERE STATE=" + TblConstant.META_DIM_TYPE_STATE_VALID + " AND Z.DIM_TYPE_ID=" + dimTypeId +
                " AND Z.MOD_FLAG = 1 GROUP BY " + dimTablePrefix + "_PAR_ID  ) B " +
                " ON A." + dimTablePrefix + "_ID =B." + dimTablePrefix + "_PAR_ID  " +
                " WHERE A.STATE=" + TblConstant.META_DIM_TYPE_STATE_VALID + " AND A.DIM_TYPE_ID =" + dimTypeId + " " +
                " AND A." + dimTablePrefix + "_PAR_ID = " + parId +
                (lastLevelFalg == 0 ? " AND A.MOD_FLAG = 1 " : "" + " ORDER BY A." + dimTablePrefix + "_CODE");
        return getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).queryForList(sql);
    }

    /**
     * 查询未审核映射数
     *
     * @param dimTableInfo { tableId：维度表id  }
     * @return
     */
    public int queryNoAuditCount(Map<String, Object> dimTableInfo) {
        int tableId = MapUtils.getIntValue(dimTableInfo, "dimTableId");
        String sql = "SELECT COUNT(*) AS AUDIT_COUNT FROM META_DIM_TAB_MOD_HIS T" +
                " WHERE T.DIM_TABLE_ID = ? AND T.MOD_FLAG IN (2, 8, 9) AND T.AUDIT_FLAG = 0";
        Map<String, Object> rs = getDataAccess().queryForMap(sql, tableId);
        return Integer.parseInt(rs.get("AUDIT_COUNT").toString());
    }

    /**
     * 查询未映射和已映射数
     *
     * @param dimTableInfo { intTableOwner：接口表用户，areaIntTableName：接口表名
     *                     tableId：维度表id
     *                     }
     * @param isMapping    是否映射(true为映射，false为未映射)
     * @return
     */
    public int queryMappCount(Map<String, Object> dimTableInfo, boolean isMapping) {
        String intTableOwner = MapUtils.getString(dimTableInfo, "intTableOwner");
        String areaIntTableName = MapUtils.getString(dimTableInfo, "areaIntTableName");
        int tableId = MapUtils.getIntValue(dimTableInfo, "dimTableId");
        String sql = "select count(*) as MAPP_COUNT from (" +
                "SELECT SUBSTR(SYS_CONNECT_BY_PATH(SRC_NAME, '->'), 3) SRC_NAME,A.SRC_CODE " +
                "FROM " + intTableOwner + "." + areaIntTableName + " A LEFT JOIN (SELECT SRC_PAR_CODE, COUNT(*) CNT" +
                " FROM " + intTableOwner + "." + areaIntTableName + " GROUP BY SRC_PAR_CODE) B ON A.SRC_CODE = B.SRC_PAR_CODE" +
                " WHERE NVL(B.CNT, 0) = 0 START WITH A.SRC_PAR_CODE IS NULL" +
                " CONNECT BY A.SRC_PAR_CODE = PRIOR A.SRC_CODE) A" +
                " LEFT JOIN (SELECT * FROM META_DIM_MAPP P WHERE P.DIM_TABLE_ID=?) M ON A.SRC_CODE = M.SRC_CODE";
        if (isMapping) {
            sql += " WHERE M.SRC_CODE IS NOT NULL";
        } else {
            sql += " WHERE M.SRC_CODE IS NULL";
        }
        Map<String, Object> rs = getDataAccess().queryForMap(sql, tableId);
        return Integer.parseInt(rs.get("MAPP_COUNT").toString());
    }

    /**
     * 查询维度表的映射信息。
     *
     * @param dimTableInfo     维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param areaIntTableName 维度接口表表名
     * @param queryMessage     查询信息，包含键值 keyWord:关键字，dimTypeId:归并类型ID，system:系统ID。
     * @return
     */
    public List<Map<String, Object>> queryDimMapping(Map<String, Object> dimTableInfo, Map<String, Object> queryMessage,
                                                     boolean isMapping, Page page, int flag) {
        String areaIntTableName = MapUtils.getString(dimTableInfo, "areaIntTableName");
        int tableId = Integer.parseInt(dimTableInfo.get("dimTableId").toString());
        String tableName = Convert.toString(dimTableInfo.get("tableName"));
        String dimTablePrefix = Convert.toString(dimTableInfo.get("tableDimPrefix"));
        String keyWord = Convert.toString(queryMessage.get("keyWord"));
        int system = Integer.parseInt(queryMessage.get("system").toString());
        String tableOwner = Convert.toString(dimTableInfo.get("tableOwner"));
        String intTableOwner = Convert.toString(dimTableInfo.get("intTableOwner"));
        String sql = "SELECT A.SRC_NAME,A.SRC_CODE,E." + dimTablePrefix + "_CODE CODE,E." + dimTablePrefix + "_NAME NAME,E.PAR_CODE,E.PAR_NAME, " +
                "CASE WHEN E." + dimTablePrefix + "_CODE  IS NULL THEN 0 ELSE 1 END ISMAPPING,E." + dimTablePrefix + "_ID  ITEM_ID FROM " +
                "(SELECT DISTINCT SUBSTR(SYS_CONNECT_BY_PATH( SRC_NAME, '->' ),3) SRC_NAME,A.SRC_CODE " +
                "FROM " + intTableOwner + "." + areaIntTableName + " A " +
                "LEFT JOIN (SELECT SRC_PAR_CODE ,COUNT(*) CNT FROM " + intTableOwner + "." + areaIntTableName + " GROUP BY SRC_PAR_CODE) B " +
                " ON A.SRC_CODE=B.SRC_PAR_CODE WHERE NVL(B.CNT,0)=0 " +
                //关键字查询
                ((keyWord != null && !keyWord.equals("")) ? "AND (A.SRC_CODE LIKE " + SqlUtils.allLikeParam(keyWord) + " OR A.SRC_NAME LIKE " + SqlUtils.allLikeParam(keyWord) + ") " : "") +
                " START WITH A.SRC_PAR_CODE IS NULL " +
                " CONNECT BY  A.SRC_PAR_CODE = PRIOR A.SRC_CODE) A  LEFT JOIN " +
                "(SELECT MAX(B." + dimTablePrefix + "_ID) AS " + dimTablePrefix + "_ID ,MAX(B." + dimTablePrefix + "_NAME) AS " + dimTablePrefix + "_NAME," +
                "MAX(B." + dimTablePrefix + "_CODE) AS " + dimTablePrefix + "_CODE,MAX(C." + dimTablePrefix + "_ID) AS PAR_ID," +
                "MAX(C." + dimTablePrefix + "_CODE) AS PAR_CODE,MAX(C." + dimTablePrefix + "_NAME) AS PAR_NAME,D.SRC_CODE " +
                "FROM " + tableOwner + "." + tableName + " B " +
                "LEFT JOIN " + tableOwner + "." + tableName + " C ON B." + dimTablePrefix + "_PAR_ID =C." + dimTablePrefix + "_ID  " +
                "LEFT JOIN META_DIM_MAPP D ON B." + dimTablePrefix + "_CODE =D.ITEM_CODE AND D.SYS_ID=" + system + " AND D.DIM_TABLE_ID=" + tableId +
                " WHERE D.SRC_CODE IS NOT NULL GROUP BY D.SRC_CODE" +
                ") E ON A.SRC_CODE=E.SRC_CODE ";
        if (flag != 1) { //为1为查询所有，不等于1是判断是查询映射还是非映射
            if (isMapping) {//已映射
                sql += "WHERE E." + dimTablePrefix + "_CODE  IS NOT NULL ";
            } else {//非映射
                sql += "WHERE E." + dimTablePrefix + "_CODE  IS NULL ";
            }
        }
        sql += "ORDER BY A.SRC_CODE ";
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询尚未审核的信息
     *
     * @param dimTableInfo     维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param queryMessage     查询信息，包含键值 keyWord:关键字，dimTypeId:归并类型ID，system:系统ID。
     * @param areaIntTableName 维度接口表表名
     * @param isAudit          是否审核。
     * @return
     */
    public List<Map<String, Object>> queryDimMappingByHis(Map<String, Object> dimTableInfo, Map<String, Object> queryMessage, Page page) {
        String areaIntTableName = MapUtils.getString(dimTableInfo, "areaIntTableName");
        int tableId = Integer.parseInt(dimTableInfo.get("dimTableId").toString());
        Object keyWord = queryMessage.get("keyWord");
        int system = Integer.parseInt(queryMessage.get("system").toString());
        String intTableOwner = Convert.toString(dimTableInfo.get("intTableOwner"));
        //查询未审核的数据
        String sql = "SELECT B.SRC_CODE,B.SRC_NAME,M.ITEM_CODE CODE,M.ITEM_NAME NAME," +
                " decode(M.ITEM_CODE,'',0,1) as ISMAPPING,A.HIS_ID " +
                " FROM META_DIM_TAB_MOD_HIS A" +
                " LEFT JOIN META_DIM_MAPP M ON A.SRC_CODE=M.SRC_CODE " +
                " LEFT JOIN " + intTableOwner + "." + areaIntTableName + " B ON A.SRC_CODE=B.SRC_CODE  " +
                " WHERE A.DIM_TABLE_ID=" + tableId + " AND A.SRC_SYS_ID=" + system + " AND A.AUDIT_FLAG=0";
        if (keyWord != null && !keyWord.toString().equals("")) {
            sql += "AND (A.SRC_CODE LIKE " + SqlUtils.allLikeParam(keyWord.toString()) + " OR A.SRC_NAME LIKE " + SqlUtils.allLikeParam(keyWord.toString()) + ") ";
        }
        sql += " ORDER BY A.SRC_CODE ";
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询未通过审核的数据条数。
     *
     * @param dimTablleId
     * @return
     */
    public int queryUnAuditedCount(int dimTablleId, int sysId) {
        Integer[] inObj = new Integer[]{DimConstant.DIM_MAPP_ADD,
                DimConstant.DIM_MAPP_DELETE, DimConstant.DIM_MAPP_UPDATE};
        String sql = null;
        if (dimTablleId != 0) {
            if (sysId != 0) {
                sql = "SELECT COUNT(*) FROM META_DIM_TAB_MOD_HIS WHERE DIM_TABLE_ID=? AND SRC_SYS_ID=? AND MOD_FLAG IN " + SqlUtils.inParamDeal(inObj) + " AND AUDIT_FLAG=? ";
                return getDataAccess().queryForIntByNvl(sql, 0, dimTablleId, sysId, DimConstant.DIM_NOT_AUDIT);
            }
            if (sysId == 0) {
                sql = "SELECT COUNT(*) FROM META_DIM_TAB_MOD_HIS WHERE DIM_TABLE_ID=? AND MOD_FLAG IN " + SqlUtils.inParamDeal(inObj) + " AND AUDIT_FLAG=? ";
                return getDataAccess().queryForIntByNvl(sql, 0, dimTablleId, DimConstant.DIM_NOT_AUDIT);
            }
        }
        return -1;
    }

    /**
     * 选择的当前维度表中,那些映射数据是在正在被审核的
     *
     * @return 正在被审核的数据 MAP<String,Object> key:当前数据的源系统编码  value: 源系统编码值
     * @prama tableId 当前维度表的id
     */
    public List<Map<String, Object>> queryMappAuditData(int tableId, int sysId) {
        Integer[] inObj = new Integer[]{DimConstant.DIM_MAPP_ADD,
                DimConstant.DIM_MAPP_DELETE, DimConstant.DIM_MAPP_UPDATE};
        String sql = "SELECT ITEM_ID,ITEM_CODE,SRC_CODE FROM META_DIM_TAB_MOD_HIS WHERE DIM_TABLE_ID=? AND SRC_SYS_ID=? AND MOD_FLAG IN " + SqlUtils.inParamDeal(inObj) + " AND AUDIT_FLAG=? ";
        return getDataAccess().queryForList(sql, tableId, sysId, DimConstant.DIM_NOT_AUDIT);
    }

    /**
     * 查询当前映射表中已经有的当前系统和当前维度表已经有的映射
     */
    public List<Map<String, Object>> queryMappDataByCondition(int sysId, int dimTableId) {
        String sql = "SELECT M.ITEM_CODE FROM META_DIM_MAPP M WHERE M.SYS_ID=? AND M.DIM_TABLE_ID=?";
        return this.getDataAccess().queryForList(sql, sysId, dimTableId);
    }

    /**
     * 查询code和typeId 查询当前code的itemId
     */
    public List<Map<String, Object>> queryDataByCode(String tableName, String prefix, String tableOwner, int dimTypeId) {
        List<Map<String, Object>> codeList = null;
        String sql = "SELECT T." + prefix + "_ID," + prefix + "_CODE,T.DIM_TYPE_ID,T.DIM_LEVEL FROM " + tableOwner + "." + tableName + " T";
        codeList = this.getDataAccess().queryForList(sql);
        return codeList;
    }

    /**
     * 查询当前维度表中维度记录的最大层级
     */
    public int queryMaxLevelByCondition(String tableName, String tableOwner) {
        int level = 0;
        String sql = "SELECT MAX(DIM_LEVEL) FROM " + tableOwner + "." + tableName;
        String str = this.getDataAccess().queryForString(sql);
        if (str != null) {
            level = Convert.toInt(str);
        }
        return level;
    }

    /**
     * 查询当前的表中的itemName和itemCode用于生成excel文件的模板
     */
    public List<Map<String, Object>> queryDimCodeByTableName(String tableName, String tablePrefix, String tableOwner, int sysId, int tableId) {
//    	int  level = this.queryMaxLevelByCondition(tableName, tableOwner);
        String sql = "SELECT DISTINCT T." + tablePrefix + "_NAME,T." + tablePrefix + "_CODE,T." + tablePrefix + "_ID FROM " + tableOwner + "." + tableName + " T " +
                "  LEFT JOIN (SELECT  " + tablePrefix + "_PAR_ID, COUNT(*) CNT" +
                "  FROM " + tableOwner + "." + tableName +
                "  WHERE MOD_FLAG = 1 " +
                "   GROUP BY " + tablePrefix + "_PAR_ID) M ON M." + tablePrefix + "_PAR_ID = T." + tablePrefix + "_ID WHERE T.STATE =1 AND T.MOD_FLAG=1 AND T." + tablePrefix + "_CODE " +
                "NOT IN (SELECT M.ITEM_CODE FROM "+ MetaDataSourceDAO.getMetaOwner()+".META_DIM_MAPP M WHERE M.SYS_ID=? AND M.DIM_TABLE_ID=?) AND NVL(M.CNT,0)=0 ";
        List<Map<String, Object>> datas = this.getDataAccess().queryForList(sql, sysId, tableId);
/*        if (datas != null && datas.size() > 0) {
            Set<String> unModData = this.queryUnModFlagCode(tableName, tablePrefix, tableOwner, sysId, tableId);
            if (unModData != null && unModData.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                   Map<String,Object> data=datas.get(i);
                   if(unModData.contains(MapUtils.getString(data,tablePrefix+"_CODE"))){
                       datas.remove(i--);
                   }
                }
            }
        }*/
        return datas;
    }

    /**
     * 查询末级不显示的已经映射的编码
     *
     * @param tableName
     * @param tablePrefix
     * @param tableOwner
     * @param sysId
     * @param tableId
     * @return
     */
    public Set<String> queryUnModFlagCode(String tableName, String tablePrefix, String tableOwner, int sysId, int tableId) {
        String sql = "SELECT DISTINCT T." + tablePrefix + "_PAR_CODE " +
                "  FROM " + tableOwner + "." + tableName + " T WHERE T." + tablePrefix + "_CODE  IN " +
                "        (SELECT M.ITEM_CODE FROM META.META_DIM_MAPP M WHERE M.SYS_ID = ? " +
                "                            AND M.DIM_TABLE_ID = ? ) " +
                "     AND t.MOD_FLAG=0";
        List<Map<String, Object>> datas = getDataAccess().queryForList(sql, sysId, tableId);
        if (datas != null && datas.size() > 0) {
            Set<String> rs = new HashSet<String>();
            for (Map<String, Object> data : datas) {
                rs.add(MapUtils.getString(data, tablePrefix + "_PAR_CODE"));
            }
            return rs;
        } else {
            return null;
        }
    }

    /**
     * 查询接口表可被映射的所有编码
     *
     * @param dataSourceId
     * @param tableOwner
     * @param tableName
     * @return
     */
    public Set<String> queryIntRelCode(int dataSourceId, String tableOwner, String tableName) {
        String sql = "SELECT  A.SRC_CODE   FROM " + tableOwner + "." + tableName + " A  LEFT JOIN (SELECT SRC_PAR_CODE, COUNT(*) CNT " +
                " FROM " + tableOwner + "." + tableName + " GROUP BY SRC_PAR_CODE) B " +
                " ON A.SRC_CODE = B.SRC_PAR_CODE " +
                " WHERE NVL(B.CNT, 0) = 0 ";
        Set<String> set = new HashSet<String>();
        Object[][] data = getDataAccess(dataSourceId).queryForArray(sql, false);
        if (data != null && data.length > 0) {
            for (Object[] tempData : data) {
                set.add(tempData[0].toString());
            }
        }
        return set;
    }

}
