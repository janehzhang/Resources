package tydic.meta.module;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Constant;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.MetaDimTablesDAO;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:维度常用工具方法
 * @date 2012-04-07
 */
public class DimUtils extends MetaBaseDAO {

    /**
     * 时间维度的表类ID值
     */
    public final static int TIME_DIM_TABLE_ID = 1;

    /**
     * 时间归并类型：年月日
     */
    public final static int TIME_DIM_TYPE_MONTH = 1;

    /**
     * 时间归并类型：年周日
     */
    public final static int TIME_DIM_TYPE_WEEK = 2;

    /**
     * 获取维度数据。
     *
     * @param dimTableId     维度表类ID，必须。
     * @param dimTypeId      维度归并类型ID，必须。
     * @param dimLevels      维度归并层次,如果有则以","号进行分割
     * @param dimFilterCodes 维度过滤字段
     * @return
     */
    public static List<Map<String, Object>> queryDimData(long dimTableId, long dimTypeId,
                                                         String dimLevels, String dimFilterCodes) {
        //根据表类ID查询维度信息
        MetaDimTablesDAO metaDimTablesDAO = new MetaDimTablesDAO();
        DimUtils dimUtils = new DimUtils();
    	try {
	        //查询维表信息
	        Map<String, Object> dimInfo = metaDimTablesDAO.queryDimTableInfo(dimTableId);
	        if (dimInfo != null && dimInfo.size() > 0) {
	            String tableName = MapUtils.getString(dimInfo, "TABLE_NAME");
	            String tableDimPrefix = MapUtils.getString(dimInfo, "TABLE_DIM_PREFIX");
	            String tableOwner = MapUtils.getString(dimInfo, "TABLE_OWNER");
	            String dataSoruceId = MapUtils.getString(dimInfo, "DATA_SOURCE_ID");
	            //构建查询维表SQL
	            String sql = "SELECT " + tableDimPrefix + "_ID AS ID," + tableDimPrefix
	                    + "_PAR_ID AS PAR_ID," + tableDimPrefix + "_CODE AS CODE," + tableDimPrefix + "_NAME AS NAME,DIM_LEVEL FROM " + tableOwner + "." + tableName + " ";
	            sql += "WHERE DIM_TYPE_ID=? AND STATE="+ Constant.META_ENABLE+" ";
	            //如果维度层次不为空
	            if (!StringUtils.isEmpty(dimLevels)) {
	                sql += "AND DIM_LEVEL IN " + SqlUtils.inParamDeal(dimLevels.split(",")) + " ";
	            }
	            //如果维度过滤编码不为空，加入维度编码过滤限制
	            if (!StringUtils.isEmpty(dimFilterCodes)) {
	                sql += "AND " + tableDimPrefix + "_CODE NOT IN " + SqlUtils.inParamDeal(dimFilterCodes.split(",")) + " ";
	            }
	            int minLevel = findMinLevel(dimLevels);
	            //如果是时间维度，返回日期值小于当前日期
	            if (dimTableId == TIME_DIM_TABLE_ID) {
	                if (StringUtils.isNotEmpty(dimLevels)) {
	                    String[] levels = dimLevels.split(",");
	                    int count = 0;
	                    for (String level : levels) {
	                        String format = level.equals("1") ? "yyyy" : (level.equals("2") ? "yyyyMM" : "yyyyMMdd");
	                        String time = DateUtil.getCurrentDay(format);
	                        if (TIME_DIM_TYPE_WEEK == dimTypeId) {
	                            Calendar c = Calendar.getInstance();
	                            int week = c.get(Calendar.WEEK_OF_YEAR);//当前年的周
	                            int year = c.get(Calendar.YEAR);//当前年
	                            time = year + "" + week;
	                        }
	                        sql += (count == 0 ? " AND (" : "");
	                        sql += "(" + tableDimPrefix + "_CODE<='" + time + "' AND DIM_LEVEL=" + level + ") ";
	                        sql += (count++ == levels.length - 1 ? ") " : " OR ");
	                    }
	                }
	            }
	            if (dimTableId == TIME_DIM_TABLE_ID) {
	                //按照时间倒排序
	                sql += "ORDER BY " + tableDimPrefix + "_CODE DESC";
	            } else {
	                sql += " ORDER BY ORDER_ID DESC";
	            }
	            List<Map<String, Object>> dimDatas = dimUtils.getDataAccess(dataSoruceId).queryForList(sql, dimTypeId);
	            return dimDatas;
	        } else {
	            return null;
	        }
        }catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }finally {
			dimUtils.close();
            metaDimTablesDAO.close();
        }
    }

    /**
     * 获取维度数据。
     *
     * @param dimTableId 维度表类ID，必须。
     * @param dimTypeId  维度归并类型ID，必须。
     * @param codes      维度CODES
     * @return
     */
    public static List<Map<String, Object>> queryDimDataByCodes(long dimTableId, long dimTypeId, String codes) {
        //根据表类ID查询维度信息
        MetaDimTablesDAO metaDimTablesDAO = new MetaDimTablesDAO();
        //查询维表信息
        Map<String, Object> dimInfo = metaDimTablesDAO.queryDimTableInfo(dimTableId);
        if (dimInfo != null && dimInfo.size() > 0) {
            String tableName = MapUtils.getString(dimInfo, "TABLE_NAME");
            String tableDimPrefix = MapUtils.getString(dimInfo, "TABLE_DIM_PREFIX");
            String tableOwner = MapUtils.getString(dimInfo, "TABLE_OWNER");
            String dataSoruceId = MapUtils.getString(dimInfo, "DATA_SOURCE_ID");
            //构建查询维表SQL
            String sql = "SELECT " + tableDimPrefix + "_ID AS ID," + tableDimPrefix
                    + "_PAR_ID AS PAR_ID," + tableDimPrefix + "_CODE AS CODE," + tableDimPrefix + "_NAME AS NAME,DIM_LEVEL FROM " + tableOwner + "." + tableName + " ";
            sql += "WHERE DIM_TYPE_ID=? AND STATE="+ Constant.META_ENABLE+" ";
            //如果维度过滤编码不为空，加入维度编码过滤限制
            if (!StringUtils.isEmpty(codes)) {
                sql += "AND " + tableDimPrefix + "_CODE IN " + SqlUtils.inParamDeal(codes.split(",")) + " ";
            }
            if (dimTableId == TIME_DIM_TABLE_ID) {
                //按照时间倒排序
                sql += "ORDER BY " + tableDimPrefix + "_CODE DESC";
            } else {
                sql += " ORDER BY ORDER_ID DESC";
            }
            DimUtils dimUtils = new DimUtils();
            List<Map<String, Object>> dimDatas = dimUtils.getDataAccess(dataSoruceId).queryForList(sql, dimTypeId);
            dimUtils.close();
            metaDimTablesDAO.close();
            return dimDatas;
        } else {
            return null;
        }
    }

    /**
     * 异步加载某一节点下的子节点
     *
     * @param dimTableId
     * @param dimTypeId
     * @param parId
     * @param dimFilterCodes
     * @return
     */
    public static List<Map<String, Object>> queryChildDimData(long dimTableId, long dimTypeId, long parId
            , String dimFilterCodes, int maxLevel) {
        //根据表类ID查询维度信息
        MetaDimTablesDAO metaDimTablesDAO = new MetaDimTablesDAO();
        //查询维表信息
        Map<String, Object> dimInfo = metaDimTablesDAO.queryDimTableInfo(dimTableId);
        if (dimInfo != null && dimInfo.size() > 0) {
            String tableName = MapUtils.getString(dimInfo, "TABLE_NAME");
            String tableDimPrefix = MapUtils.getString(dimInfo, "TABLE_DIM_PREFIX");
            String tableOwner = MapUtils.getString(dimInfo, "TABLE_OWNER");
            String dataSoruceId = MapUtils.getString(dimInfo, "DATA_SOURCE_ID");
            //构建查询维表SQL
            String sql = "SELECT A." + tableDimPrefix + "_ID AS ID,A." + tableDimPrefix
                    + "_PAR_ID AS PAR_ID,A." + tableDimPrefix + "_CODE AS CODE,A." + tableDimPrefix + "_NAME AS NAME," +
                    (maxLevel == -1 ? "" : "CASE WHEN A.DIM_LEVEL=" + maxLevel + " THEN 0 ELSE ") +
                    " DECODE(NVL(B.CNT,0),0,0,1) " + (maxLevel == -1 ? "" : " END ") +
                    " CHILDREN, " +
                    "DIM_LEVEL FROM " + tableOwner + "." + tableName + " A ";
            sql += "LEFT JOIN (SELECT " + tableDimPrefix + "_PAR_ID,COUNT(*) CNT FROM " + tableOwner + "." + tableName + " GROUP BY " + tableDimPrefix + "_PAR_ID) B " +
                    "ON A." + tableDimPrefix + "_ID=B." + tableDimPrefix + "_PAR_ID ";
            sql += "WHERE DIM_TYPE_ID=? AND A." + tableDimPrefix + "_PAR_ID=? AND A.STATE="+ Constant.META_ENABLE+" ";
            //如果是时间维度，返回日期值小于当前日期
            if (dimTableId == TIME_DIM_TABLE_ID) {
                String time1 = "'" + DateUtil.getCurrentDay("yyyy") + "'";
                String time2 = "'" + DateUtil.getCurrentDay("yyyyMM") + "'";
                if (TIME_DIM_TYPE_WEEK == dimTypeId) {
                    Calendar c = Calendar.getInstance();
                    int week = c.get(Calendar.WEEK_OF_YEAR);//当前年的周
                    int year = c.get(Calendar.YEAR);//当前年
                    time2 = year + "" + week;
                }
                String time3 = "'" + DateUtil.getCurrentDay("yyyyMM") + "'";
                sql += "AND " + tableDimPrefix + "_CODE<=DECODE(DIM_LEVEL,1," + time1 + ",2," + time2 + ",3," + time3 + ") ";
            }
            //如果维度过滤编码不为空，加入维度编码过滤限制
            if (!StringUtils.isEmpty(dimFilterCodes)) {
                sql += "AND " + tableDimPrefix + "_CODE NOT IN " + SqlUtils.inParamDeal(dimFilterCodes.split(",")) + " ";
            }
            if (dimTableId == TIME_DIM_TABLE_ID) {
                //按照时间倒排序
                sql += "ORDER BY " + tableDimPrefix + "_CODE DESC";
            } else {
                sql += " ORDER BY ORDER_ID DESC";
            }
            DimUtils dimUtils = new DimUtils();
            List<Map<String, Object>> dimDatas = dimUtils.getDataAccess(dataSoruceId).queryForList(sql, dimTypeId, parId);
            dimUtils.close();
            metaDimTablesDAO.close();
            return dimDatas;
        } else {
            return null;
        }
    }

    /**
     * 对以","分隔的字符串寻找其最大的层次
     *
     * @param dimLevels
     * @return
     */
    public static int findMaxLevel(String dimLevels) {
        int maxLevel = -1;
        if (!StringUtils.isEmpty(dimLevels)) {
            String[] levels = dimLevels.split(",");
            for (String level : levels) {
                if (Integer.parseInt(level) > maxLevel) {
                    maxLevel = Integer.parseInt(level);
                }
            }
        }
        return maxLevel;
    }

    /**
     * 对以","分隔的字符串寻找其最大的层次
     *
     * @param dimLevels
     * @return
     */
    public static int findMinLevel(String dimLevels) {
        int minLevel = 100000;
        if (!StringUtils.isEmpty(dimLevels)) {
            String[] levels = dimLevels.split(",");
            for (String level : levels) {
                if (Integer.parseInt(level) < minLevel) {
                    minLevel = Integer.parseInt(level);
                }
            }
        }
        return minLevel;
    }
}
