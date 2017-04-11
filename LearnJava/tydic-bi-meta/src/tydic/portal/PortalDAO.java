package tydic.portal;

import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.DataTable;
import tydic.meta.common.MetaBaseDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 移植记录：
 * 1，为保证编译通过，能注销的代码已经注销而不是删除
 * 2，所有static方法取消static属性
 * 3，方法中参数有DataAccess的，取消掉，改为用getDataAccess(sourceName)获取
 *
 * --刘斌
 */

public class PortalDAO extends MetaBaseDAO {

    private static final String sourceName	= SystemVariable.getString("db.poatal","config2");//数据源使用config2

    public Object[] getDataAudit(int menu_id, int tab_id, int index_type_id, String dateNo, String areaCode) {
        DataAccess access = getDataAccess(sourceName);
        try {
            String sql = "SELECT T.AUDIT_CONCLUDE AUDIT_STATE ,T.SHOW_OPINION MSG,t.audit_opinion EXP FROM  TB_B_DATA_AUDIT T,TB_B_DATA_SCOPE M"
                    +
                    " WHERE M.BUS_FLAG ='" + menu_id + "' AND M.MODULE_ADDRES='" + tab_id
                    + "' AND M.PAGE_ADDRES='" + index_type_id + "' " +
                    " AND T.DATA_DATE='" + dateNo + "' AND T.DATA_AREA='" + areaCode + "' AND T.SCOPE_ID=M.SCOPE_ID";
            Map<String, Object> re = access.queryForMap(sql);
            System.out.println("验证数据属性：" + sql);
            if (re != null && re.size() > 0) {
                return new Object[]{re.get("AUDIT_STATE"), re.get("MSG"), re.get("EXP")};
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public Object[] getAreaName() {

        String sql = "select t.zone_code,t.zone_name from meta_dim_zone t where t.dim_type_id=1 ";

        DataTable dt = new DataTable();

        try {
            dt = getDataAccess().queryForDataTable(sql);
            return dt.rows;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获取显示表单栏
     *
     * @return
     */
    public List<Map<String, Object>> getViewTabs(int menu_id, int define_id, int indexTPD)
            throws Exception {
        String sql = "SELECT T.TAB_ID,T.TAB_NAME,T.INDEX_TYPE_ID,T.RPT_TYPE,rolldown_layer,default_grid FROM DM_1_DATA_VIEW_TAB_SET T "
                + "WHERE T.MENU_ID = " + menu_id + " AND T.DEFINE_ID=" + define_id;
        if (indexTPD != 0)
            sql += " and t.index_type_id='" + indexTPD + "'";
        sql += " AND T.SHOW_FLAG = 1 ORDER BY ORDER_ID,TAB_ID ";
        System.out.println("TAB表配置：" + sql);
        return getDataAccess(sourceName).queryForList(sql);
    }

    /**
     * 读取指标列配置
     *
     * @param index_type_id
     * @param report_level_id
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getViewTabIndex(int index_type_id, int report_level_id)
            throws Exception {
        String sql = "SELECT T.COL_EN_NAME,NVL(T.COL_CN_NAME_NEW,T.COL_CN_NAME) COL_CN_NAME,T.INDEX_CD,T.INDEX_NAME,T.FLAG " +
                "FROM DM_1_DATA_VIEW_COL_SET T " +
                "WHERE T.INDEX_TYPE='" + index_type_id + "' AND T.REPORT_LEVEL_ID='" + report_level_id + "' " +
                "AND T.COL_AUTH_FLAG=1 ORDER BY T.SHOW_ORDER_ID ";
        System.out.println("TAB表指标：" + sql);
        return getDataAccess(sourceName).queryForList(sql);

    }

    /**
     * 获取菜单功能点数据审核状态和数据区间
     *
     * @param menu_id
     * @param tab_id
     * @param index_type_id
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getViewTabDataAudit(int menu_id, int tab_id, int index_type_id)
            throws Exception {
        String sql = " SELECT T.BUS_FLAG MENU_ID,T.MODULE_ADDRES TAB_ID,T.MIN_DATE MIN_DATENO," +
                " to_char(sysdate+to_number(T.MAX_DATE),'yyyymmdd') MAX_DATENO,T.MAX_EFFECT EFFECT_DATENO "
                + " FROM TB_B_DATA_SCOPE  T WHERE T.BUS_FLAG=" + menu_id
                + " AND T.MODULE_ADDRES='" + tab_id + "' AND T.PAGE_ADDRES='" + index_type_id + "' AND T.ADUIT_FLAG='1'";
        System.out.println("数据验证：" + sql);
        List<Map<String, Object>> tmp = getDataAccess(sourceName).queryForList(sql);
        if (tmp.size() == 0) {
            sql = "SELECT " + menu_id + " MENU_ID," + tab_id
                    + " TAB_ID, MIN(T.DATE_NO) MIN_DATENO,MAX(T.DATE_NO) MAX_DATENO,MAX(T.DATE_NO) EFFECT_DATENO "
                    + "FROM DM_2_DATA_VIEW_REPORT T WHERE T.INDEX_TYPE_ID='" + index_type_id + "' AND T.REPORT_LEVEL_ID='1'";
            System.out.println("数据验证：" + sql);
            tmp = getDataAccess(sourceName).queryForList(sql);
        }
        return tmp;
    }

    /**
     * 获取指标解释
     *
     * @param index_type_id
     * @param report_level_id
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getViewTabIndexExp(String index_type_id, String report_level_id)
            throws Exception {
        String sql = " SELECT DISTINCT  T.INDEX_CD,D.INDEX_NAME,D.INDEX_EXPRESS                                            " +
                " FROM TBAS.DM_1_DATA_VIEW_COL_SET T                                                                  " +
                " INNER JOIN DBP_DIM_DATA_INDEX D ON T.INDEX_CD=D.DATA_INDEX_CODE                      " +
                " WHERE T.INDEX_TYPE='" + index_type_id
                + "' AND T.REPORT_LEVEL_ID IN ('" + report_level_id + "','" + (Integer.parseInt(report_level_id) + 1) + "')       " +
                " AND T.COL_AUTH_FLAG=1                                                                               " +
                " UNION                                                                                               " +
                " SELECT DISTINCT NVL(T.COL_CN_NAME_NEW,T.COL_CN_NAME) CNNAME,D.INDEX_NAME,D.INDEX_EXPRESS            " +
                " FROM TBAS.DM_1_DATA_VIEW_COL_SET T                                                                  " +
                " INNER JOIN DBP_DIM_DATA_INDEX D ON NVL(T.COL_CN_NAME_NEW,T.COL_CN_NAME)=D.INDEX_NAME                " +
                " WHERE T.INDEX_TYPE=" + index_type_id
                + "  AND T.COL_AUTH_FLAG=1     ";
        System.out.println("指标解释：" + sql);
        return getDataAccess(sourceName).queryForList(sql);

    }

    /**
     * 获取列名
     *
     * @param indexType
     * @param reportLevelId
     * @param indexCd
     * @return
     */
    public List<Map<String, Object>> getRowName(String indexType, String reportLevelId, String indexCd) {
        StringBuffer sql = new StringBuffer("SELECT T.INDEX_TYPE INDEXTYPE," +
                " T.REPORT_LEVEL_ID REPORTLEVELID," +
                " T.INDEX_CD INDEXCD," +
                " T.INDEX_NAME INDEXNAME," +
                " T.COL_EN_NAME COLENNAME," +
                " T.COL_CN_NAME COLCNNAME," +
                " T.COL_CN_NAME_NEW COLCNNAMENEW," +
                " T.FLAG FLAG" +
                " FROM TBAS.DM_1_DATA_VIEW_COL_SET T WHERE 1=1 ");
        List params = new ArrayList();
        if (indexType != null && !"".equals(indexType)) {
            sql.append(" AND T.INDEX_TYPE = ? ");
            params.add(indexType);
        }
        if (reportLevelId != null && !"".equals(reportLevelId)) {
            sql.append(" AND T.REPORT_LEVEL_ID = ?");
            params.add(reportLevelId);
        }
        if (indexCd != null && !"".equals(indexCd)) {
            sql.append(" AND T.INDEX_CD = ?");
            params.add(indexCd);
        }
        sql.append(" ORDER BY TO_NUMBER(SUBSTR(COL_EN_NAME,6)) ASC ");
        DataAccess access = getDataAccess(sourceName);
        try {
            List<Map<String, Object>> rtn = access.queryForList(sql.toString(), params.toArray());
            return rtn;
        } catch (Exception e) {
            Log.error(null, e);
            return null;
        }
    }
    /**
     * 保存访问排名的角色
     */


    /**
     * 获取数据
     *
     * @param indexTypeId
     * @param reportLevelId
     * @param dateNo
     * @param indexCd
     * @param localCode
     * @param areaId
     * @return
     */
    public Object[][] getData(String indexTypeId, String reportLevelId, String dateNo, String indexCd,
                              String localCode, String areaId, String[] vals, int type) {
        DataAccess access = getDataAccess(sourceName);
        try {
            StringBuffer sql = new StringBuffer("SELECT " +
                    " t.LOCAL_CODE,t.area_id," +
                    "T.INDEX_CD INDEXCD," +
                    " T.INDEX_NAME INDEXNAME,"
            );

            if (vals != null && vals.length != 0) {
                for (int i = 0; i < vals.length; i++) {
                    sql.append(vals[i]);
                    sql.append(",");
                }
            } else {
                List<Map<String, Object>> valList = this.getViewTabIndex(indexTypeId==null||indexTypeId.equals("")?0:Integer.parseInt(indexTypeId),
                       reportLevelId==null||reportLevelId.equals("")?0:Integer.parseInt(reportLevelId));
                for (Iterator iterator = valList.iterator(); iterator.hasNext(); ) {
                    Map<String, Object> map = (Map<String, Object>) iterator.next();
                    sql.append(map.get("COL_EN_NAME"));
                    sql.append(",");
                }
            }

            sql.append(" T.GROUP_LINE_FLAG,T.IS_DOWN_LEVEL_ID ISDOWNLEVELID " +
                    " FROM TBAS.DM_2_DATA_VIEW_REPORT T WHERE T.FLAG = '1' ");
            List params = new ArrayList();
            if (indexTypeId != null && !"".equals(indexTypeId)) {
                sql.append(" AND T.INDEX_TYPE_ID = ? ");
                params.add(indexTypeId);
            }
            if (reportLevelId != null && !"".equals(reportLevelId)) {
                sql.append(" AND T.REPORT_LEVEL_ID = ? ");
                params.add(reportLevelId);
            }
            if (dateNo != null && !"".equals(dateNo)) {
                sql.append(" AND T.DATE_NO = ?");
                params.add(dateNo);
            }
            if (indexCd != null && !"".equals(indexCd)) {
                sql.append(" AND T.INDEX_CD = ? ");
                params.add(indexCd);
            }
            if (localCode != null && !"".equals(localCode)) {
                if (type == 2 && reportLevelId.equals("2")) {
                    sql.append(" AND T.LOCAL_CODE != ? ");
                } else {
                    sql.append(" AND T.LOCAL_CODE = ? ");
                }
                params.add(localCode);
            }
            if (areaId != null && !"".equals(areaId) && !"0".equals(areaId)) {
                sql.append(" AND T.AREA_ID = ? ");
                params.add(areaId);
            }
            sql.append(" ORDER BY T.ORDER_ID,TO_NUMBER(SUBSTR(T.INDEX_CD,10,2)) ASC ");

            String sqlTemp = sql.toString();
            for (int i = 0; i < params.size(); i++) {
                sqlTemp = sqlTemp.replaceFirst("\\?", params.get(i).toString());
            }
            System.out.println("：" + sqlTemp);

            DataTable dataTable = new DataTable();
            dataTable = getDataAccess(sourceName).queryForDataTable(sql.toString(), params.toArray());
            return dataTable.rows;
        } catch (Exception e) {
            Log.error(null, e);
            return null;
        }
    }

    public DataTable getAreaData(String fieldName, String indexTypeId, String REPORT_LEVEL_ID, String dateNo,
                                 String indexCd, String LOCAL_CODE) {

        String sql = "SELECT   value1," + fieldName + " FROM TBAS.DM_2_DATA_VIEW_REPORT T WHERE T.FLAG = '1' " +
                " AND T.INDEX_TYPE_ID = '" + indexTypeId + "'   AND T.REPORT_LEVEL_ID = '" + REPORT_LEVEL_ID
                + "' AND T.DATE_NO = '" + dateNo + "'  " +
                " and t.index_cd='" + indexCd;
        if (LOCAL_CODE != null && !LOCAL_CODE.equals("")) {
            if (LOCAL_CODE.equals("0000"))
                sql += "'  AND T.LOCAL_CODE != '0000'";
            else
                sql += "'  AND T.LOCAL_CODE = '" + LOCAL_CODE + "'";
        }
        sql += " ORDER BY T.ORDER_ID, TO_NUMBER(SUBSTR(T.INDEX_CD, 10, 2)) ASC";
        System.out.println("地域数据：" + sql);
        DataTable dt = new DataTable();
        try {
            dt = getDataAccess(sourceName).queryForDataTable(sql);
            return dt;
        } catch (Exception ed) {

        }
        return null;
    }

    /**
     * 获取统计图
     *
     * @param indexTypeId
     * @param localCode
     * @param areaId
     * @param indexCd
     * @param reportLevelId
     * @param minTime
     * @param maxTime
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getChart(String indexTypeId, String localCode, String areaId, String indexCd,
                                              String reportLevelId, String minTime, String maxTime, String fieldName) throws Exception {
        List<Map<String, Object>> valList = getViewTabIndexExp(indexTypeId, reportLevelId);
        StringBuffer sql = new StringBuffer("SELECT T.INDEX_TYPE_ID INDEXTYPEID," +
                " T.MONTH_NO MONTHNO," +
                " T.DATE_NO DATENO," +
                " T.LOCAL_CODE LOCALCODE," +
                " T.AREA_ID AREAID," +
                " T.INDEX_CD INDEXCD," +
                " T.INDEX_NAME INDEXNAME,"
        );
        sql.append(" NVL(");
        sql.append(fieldName);
        sql.append(" ,0)" + fieldName + ",");
        sql.append(" T.ORDER_ID ORDERID," +
                " T.REPORT_LEVEL_ID REPORTLEVELID," +
                " T.IS_DOWN_LEVEL_ID ISDOWNLEVELID," +
                " T.FLAG FALG FROM TBAS.DM_2_DATA_VIEW_REPORT T WHERE 1=1");

        List params = new ArrayList();
        if (indexTypeId != null && !"".equals(indexTypeId)) {
            sql.append(" AND T.INDEX_TYPE_ID = ? ");
            params.add(indexTypeId);
        }
        if (localCode != null && !"".equals(localCode)) {
            sql.append(" AND T.LOCAL_CODE = ? ");
            params.add(localCode);
        }
        if (areaId != null && !"".equals(areaId) && !"0".equals(areaId)) {
            sql.append(" AND T.AREA_ID = ? ");
            params.add(areaId);
        }
        if (indexCd != null && !"".equals(indexCd)) {
            sql.append(" AND T.INDEX_CD = ? ");
            params.add(indexCd);
        }
        if (reportLevelId != null && !"".equals(reportLevelId)) {
            sql.append(" AND T.REPORT_LEVEL_ID = ? ");
            params.add(reportLevelId);
        }
        if (minTime != null && !"".equals(minTime)) {
            sql.append(" AND T.DATE_NO >= ? ");
            params.add(minTime);
        }
        if (maxTime != null && !"".equals(maxTime)) {
            sql.append(" AND T.DATE_NO <= ? ");
            params.add(maxTime);
        }
        sql.append(" ORDER BY T.ORDER_ID ASC ");
        String sqlTemp = sql.toString();
        for (int i = 0; i < params.size(); i++) {
            sqlTemp = sqlTemp.replaceFirst("\\?", params.get(i).toString());
        }
        System.out.println("图表数据：" + sqlTemp);
        return getDataAccess(sourceName).queryForList(sql.toString(), params.toArray());
    }
    /**
     *
     */
    public List<Map<String, Object>> getChart1(String type,String dragon_channel_type, String bt_code, String localCode, String minTime, String maxTime) throws Exception {
        //List<Map<String, Object>> valList = getViewTabIndexExp(indexTypeId, reportLevelId);
        //判断是否是达到数
        boolean isArive = false;
        if(type.toString().equals("1")){
            isArive = true;
        }
        StringBuffer sqlBuf = new StringBuffer();
        List params = new ArrayList();
        if(isArive){
            sqlBuf.append("SELECT DATENO,VALUE2 FROM " +
                    "(SELECT DATE_NO DATENO, SUM(CN) OVER(ORDER BY DATE_NO) VALUE2 " +
                    "  FROM " +
                    "(SELECT DATE_NO,SUM(A.TERM_SUM) CN " +
                    "                  FROM DM_MATERIAL_SERV_D A " +
                    "               WHERE IF_IPHONE = 1 ");
//            if(minTime != null && !"".equals(minTime)){
//                sqlBuf.append("AND DATE_NO >= ? ");
//                params.add(minTime);
//            }
            if(maxTime != null && !"".equals(maxTime)){
                sqlBuf.append("AND Date_No <= ? ");
                params.add(maxTime);
            }
            if(dragon_channel_type != null && !"".equals(dragon_channel_type)){
                sqlBuf.append("and dragon_channel_type=? ");
                params.add(dragon_channel_type);
            }
            if(bt_code != null && !"".equals(bt_code)){
                sqlBuf.append("and bt_code=? ");
                params.add(bt_code);
            }
            sqlBuf.append(" GROUP BY DATE_NO)) WHERE 1=1 ");
             if(minTime != null && !"".equals(minTime)){
                sqlBuf.append("AND DATENO >= ? ");
                params.add(minTime);
            }
            if(maxTime != null && !"".equals(maxTime)){
                sqlBuf.append("AND DATENO <= ? ");
                params.add(maxTime);
            }
        }else {
            sqlBuf.append("select DATE_NO DATENO,sum(t.term_sum) Value2 " +
                    "from DM_MATERIAL_SERV_D t where t.if_iphone=1 ");
            if(minTime != null && !"".equals(minTime)){
                sqlBuf.append("AND T.DATE_NO >= ? ");
                params.add(minTime);
            }
            if(maxTime != null && !"".equals(maxTime)){
                sqlBuf.append("AND T.Date_No <= ? ");
                params.add(maxTime);
            }
            if(dragon_channel_type != null && !"".equals(dragon_channel_type)){
                sqlBuf.append("and t.dragon_channel_type=? ");
                params.add(dragon_channel_type);
            }
            if(bt_code != null && !"".equals(bt_code)){
                sqlBuf.append("and t.bt_code=? ");
                params.add(bt_code);
            }
            sqlBuf.append("group by t.date_no ");
        }
        System.out.println("新OA数据：" + sqlBuf.toString());
        return getDataAccess(sourceName).queryForList(sqlBuf.toString(), params.toArray());
    }

    /**
     * 获取公告 若有置顶公告 NOTICE_STATE = {1、精华 2、置顶
     * 3、置顶+精华}，则显示置顶公告；若有多个置顶公告，显示UPDATE_DATE为最新的
     *
     * @return
     */
    public List<Map<String, Object>> getNotice() {
        String sql = "select to_char(d.update_date,'yyyy-mm-dd hh24:mi:ss') update_date,d.notice_content from DBP_AM_NOTICE d  ORDER BY UPDATE_DATE DESC";
        System.out.println("getNotice.sql=" + sql); ///*where d.notice_lx=1 and NOTICE_STATE = 2 OR NOTICE_STATE = 38*/
        List<Map<String, Object>> a = getDataAccess(sourceName).queryForList(sql);
        System.out.println("getNotice().a=" + a);
        return a;
    }

    /**
     * 获取地图数据
     *
     * @param dateNo
     * @param indexCD
     * @param fieldName
     * @return
     * @throws Exception
     */
    public List getMapData(String dateNo, String indexTypeId, String indexCD, String fieldName)
            throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T.LOCAL_CODE,");
        sql.append(fieldName);
        sql.append(" FROM TBAS.DM_2_DATA_VIEW_REPORT T WHERE T.LOCAL_CODE!='0000' and T.DATE_NO=? AND T.INDEX_TYPE_ID=? AND T.INDEX_CD=? AND T.REPORT_LEVEL_ID=2 ORDER BY ");
        sql.append(fieldName);
        sql.append(" DESC");
        System.out.println("地图数据：" + sql);
        Object[] params = {dateNo,indexTypeId,indexCD};
        return getDataAccess(sourceName).queryForList(sql.toString(),params);
    }

    /**
     * 获取值班人员
     *
     * @return
     */
    public List getDuty() {
        String sql = "SELECT * FROM DIC_DBP.DBP_SYS_DUTY T WHERE T.STATE=1";
        return getDataAccess(sourceName).queryForList(sql);
    }
    
    /**
     * 获取最大有效时间
     *
     * @param indexType
     * @return
     */
    public String queryMaxDate(String indexTypeId){
    	String sql = "SELECT MAX_EFFECT FROM TB_B_DATA_SCOPE WHERE MODULE_ADDRES =?";
    	return getDataAccess(sourceName).queryForString(sql, indexTypeId);
    }
    
    
    
}
