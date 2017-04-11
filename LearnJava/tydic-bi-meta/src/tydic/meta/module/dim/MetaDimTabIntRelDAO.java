package tydic.meta.module.dim;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 维度接口表DAO <br>
 * @date 2011-11-16
 */
public class MetaDimTabIntRelDAO extends MetaBaseDAO{

    /**
     * 查询在接口表中注册了的系统信息。
     * @return
     */
    public List<Map<String,Object>> querySystem(int tableId){
        String sql="SELECT A.SYS_ID,C.TABLE_OWNER,DATA_SOURCE_ID,INT_TAB_ID,INT_TAB_NAME,DIM_TABLE_ID " +
                   "SRC_TAB_NAME,DATA_MAPP_SQL,DATA_MAPP_MARK,USER_ID,B.SYS_NAME SYS_NAME FROM META_DIM_TAB_INT_REL A "+
                   "LEFT JOIN META_SYS B ON A.SYS_ID=B.SYS_ID "+
                   "LEFT JOIN META_TABLES C ON A.INT_TAB_ID=C.TABLE_ID "+
                   "WHERE A.DIM_TABLE_ID="+tableId;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据表类Id和系统ID查询接口表信息。
     * @param sysId
     * @param tableId
     * @return
     */
    public List<Map<String,Object>> queryBySytemAndDim(Integer sysId,Integer tableId,Page page){
        String sql="SELECT A.SYS_ID,A.DATA_SOURCE_ID,A.INT_TAB_ID,A.INT_TAB_NAME,A.DIM_TABLE_ID,C.TABLE_OWNER,D.TABLE_OWNER INT_TABLE_OWNER, " +
                   "A.SRC_TAB_NAME,A.DATA_MAPP_SQL,A.DATA_MAPP_MARK,A.USER_ID,B.SYS_NAME FROM META_DIM_TAB_INT_REL A "+
                   "LEFT JOIN META_SYS B ON A.SYS_ID=B.SYS_ID " +
                   "LEFT JOIN META_TABLES C ON A.DIM_TABLE_ID = C.TABLE_ID AND C.TABLE_STATE =1 "+
                   "LEFT JOIN META_TABLES D ON A.INT_TAB_ID = D.TABLE_ID AND D.TABLE_STATE =1 WHERE 1=1 ";
        if(tableId!=null){
            sql+="AND A.DIM_TABLE_ID="+tableId+" ";
        }
        if(sysId!=null){
            sql+="AND A.SYS_ID="+sysId+" ";
        }
        sql+=" ORDER BY SYS_ID,DIM_TABLE_ID ";
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询所有的维度映射信息。
     * @param dimTableInfo 维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param keyWord
     * @param sysId
     * @return
     */
//    public  List<Map<String,Object>> queryAllMapping(Map<String,Object> dimTableInfo,String keyWord,int sysId,int dimTypeId){
//        int tableId= Convert.toInt(dimTableInfo.get("dimTableId"));
//        String tableName=Convert.toString(dimTableInfo.get("tableName"));
//        String tableDimPrefix=Convert.toString(dimTableInfo.get("tableDimPrefix"));
//        String idColumn=tableDimPrefix+"_ID";
//        String parColumn=tableDimPrefix+"_PAR_ID";
//        String codeColumn=tableDimPrefix+"_CODE";
//        String nameColumn=tableDimPrefix+"_NAME";
//        String sql="SELECT A.SRC_CODE,A.SRC_NAME,A.SRC_PAR_CODE, "
//                   +"D."+idColumn+",D."+parColumn+",D."+codeColumn+",D."+nameColumn+",D.DIM_LEVEL, "
//                   +"E."+codeColumn+" PAR_CODE,E."+nameColumn+" PAR_NAME FROM META_DIM_CRM_AREA_INT A " +
//                   "LEFT JOIN (SELECT B."+idColumn+",B."+parColumn+",B."+codeColumn+",B."+nameColumn+",B.DIM_LEVEL " +
//                   ",C.SRC_CODE,C.SRC_NAME FROM META_DIM_ZONE B  " +
//                   "lEFT JOIN META_DIM_ZONE E ON B.ZONE_PAR_ID=E.ZONE_ID " +
//                   "LEFT JOIN META_DIM_ZONE_MAPP C ON B."+codeColumn+"=C."+codeColumn+" AND C.SYS_ID=" +sysId+" "  +
//                   " WHERE B.DIM_TYPE_ID="+dimTypeId+")  D ON A.SRC_CODE=D.SRC_CODE " ;
//        if(keyWord!=null||"".equals(keyWord)){
//            sql+=" AND A.SRC_CODE LIKE '%"+keyWord+"'%' AND A.SRC_CODE LIKE '%"+keyWord+"%'";
//        }
//        return getDataAccess().queryForList(sql);
//    }

}
