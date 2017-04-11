/**
 * @文件名: ImportBatchTableAction.java
 * @包 tydic.meta.module.tbl.imptab
 * @描述:
 * @author wuxl@tydic.com
 * @创建日期 Jan 13, 2012 9:40:48 AM
 *
 */

package tydic.meta.module.tbl.imptab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Common;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.*;
import tydic.meta.module.tbl.apply.TableApplyDAO;
import tydic.meta.web.session.SessionManager;
import uk.ltd.getahead.dwr.WebContextFactory;


/**
 * 项目名称：tydic-bi-meta
 * 类名称：ImportBatchTableAction
 * 类描述：
 * 创建人：wuxl@tydic.com
 * 创建时间：Jan 13, 2012 9:40:48 AM
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class ImportBatchTableAction {
    private ImportTableDAO importTableDAO;
    private MetaDataSourceDAO metaDataSourceDAO;
    private MetaTablesDAO metaTablesDAO;
    private TableApplyDAO tableApplyDAO;
    private MetaSysCodeDAO metaSysCodeDAO;
    private ImportBatchTableDAO batchTableDAO;
    private MetaTableRelDAO metaTableRelDAO;
    private TblPartitonDAO tblPartitonDAO;

    public MetaTableRelDAO getMetaTableRelDAO() {
        return metaTableRelDAO;
    }

    public void setMetaTableRelDAO(MetaTableRelDAO metaTableRelDAO) {
        this.metaTableRelDAO = metaTableRelDAO;
    }

    public ImportBatchTableDAO getBatchTableDAO() {
        return batchTableDAO;
    }

    public void setTblPartitonDAO(TblPartitonDAO tblPartitonDAO) {
        this.tblPartitonDAO = tblPartitonDAO;
    }

    public void setBatchTableDAO(ImportBatchTableDAO batchTableDAO) {
        this.batchTableDAO = batchTableDAO;
    }

    public MetaSysCodeDAO getMetaSysCodeDAO() {
        return metaSysCodeDAO;
    }

    public void setMetaSysCodeDAO(MetaSysCodeDAO metaSysCodeDAO) {
        this.metaSysCodeDAO = metaSysCodeDAO;
    }

    public TableApplyDAO getTableApplyDAO() {
        return tableApplyDAO;
    }

    public void setTableApplyDAO(TableApplyDAO tableApplyDAO) {
        this.tableApplyDAO = tableApplyDAO;
    }

    public MetaTablesDAO getMetaTablesDAO() {
        return metaTablesDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    /**
     * @param queryMessage
     * @return List<Map<String,Object>>
     * @throws
     * @Title: getQueryTables
     * @Description: 获取数据源表信息
     */
    public List<Map<String, Object>> getQueryTables(Map<String, String> queryMessage, Page page) {
        String typeValue = MapUtils.getString(queryMessage, "typeValue");
        List<Map<String, Object>> tablesList = new ArrayList<Map<String, Object>>();
        if (typeValue.equals("search")) {
            int dataSourceID = 0;
            if (queryMessage.get("dataSource") != null && !"".equals(queryMessage.get("dataSource")) && !"null".equals(queryMessage.get("dataSource"))) {
                dataSourceID = Integer.parseInt(queryMessage.get("dataSource"));
            }
            Map<String, Object> dataSourceMap = metaTablesDAO.getUserNameByDataSourceId(dataSourceID);
            /*获取数据源表数据*/
            tablesList = metaTablesDAO.getTablesByOwner(dataSourceMap, queryMessage.get("owner"), queryMessage.get("keyWord").toUpperCase(), dataSourceID, page);
            //查询出分区SQL
            for (Map<String, Object> temp : tablesList) {
                String partitioned = MapUtils.getString(temp, "PARTITIONED");
                if (StringUtils.isNotEmpty(partitioned)&&partitioned.equals("YES")) {
                    //如果有分区，查询出分区SQL
                    temp.put("PARTITIONSQL", tblPartitonDAO.buildPartionSql(MapUtils.getString(temp, "PRATABLENAME")
                            , MapUtils.getString(queryMessage, "owner"), dataSourceID));
                }
            }
        } else if (typeValue.equals("upload")) {
            HttpSession session = WebContextFactory.get().getSession();
            List<Map<String, Object>> importTablesList = (ArrayList<Map<String, Object>>) session.getAttribute("importTablesList");
            for (int i = page.getPosStart(); i < importTablesList.size(); i++) {
                if (page.getPosStart() + page.getCount() == i) {
                    break;
                }
                Map<String, Object> map = importTablesList.get(i);
                Map<String, Object> tablesMap = new HashMap<String, Object>();
                tablesMap.put("RN_", i + 1);
                tablesMap.put("PRATABLENAME", map.get("partablename"));
                tablesMap.put("TABLETYPENAME", map.get("tablename"));
                tablesMap.put("TABLEBUSCOMMENT", map.get("tablebuscomment"));
                tablesMap.put("TOTAL_COUNT_", importTablesList.size());
                tablesList.add(tablesMap);
            }
        }
        return tablesList;
    }

    /**
     * @param dataSourceID
     * @return String[]
     * @throws
     * @Title: getTableSpaces
     * @Description: 获取数据源表空间
     */
    public String[] getTableSpaces(String dataSourceID) {
        int dataSource_Id = 0;
        if (dataSourceID != null && !"".equals(dataSourceID) && !"null".equals(dataSourceID))
            dataSource_Id = Integer.valueOf((dataSourceID));
        return metaTablesDAO.getTableSpaces(dataSource_Id);
    }

    /**
     * @return Object[][]
     * @throws
     * @Title: getTableTypes
     * @Description: 获取属性分类
     */
    public Object[][] getTableTypes() {
        return batchTableDAO.getTableTypes(TblConstant.META_SYS_CODE_TABLE_TYPE);
    }

    /**
     * @param map
     * @return String
     * @throws ServletException
     * @throws IOException
     * @throws
     * @Title: getTableColumns
     * @Description: 获取表列字段信息
     */
    @SuppressWarnings("unchecked")
    public String getTableColumns(Map<String, String> map) throws ServletException, IOException {
//		int dataSourceId = 0;
//		if(map.get("dataSourceId") != null && !"".equals(map.get("dataSourceId")) && !"null".equals(map.get("dataSourceId"))){
//			dataSourceId = Integer.valueOf((map.get("dataSourceId")));
//		}
//		Map<String,Object> dataSourceMap = metaTablesDAO.getUserNameByDataSourceId(dataSourceId);

        String typeValue = MapUtils.getString(map, "typeValue");
        List<Map<String, Object>> colList = new ArrayList<Map<String, Object>>();
        if (typeValue.equals("search")) {
            colList = tableApplyDAO.queryDbTableColumns(map.get("dataSourceId").toString(), map.get("owner"), map.get("tableName"));
        } else if (typeValue.equals("upload")) {
            HttpSession session = WebContextFactory.get().getSession();
            List<Map<String, Object>> importTablesList = (ArrayList<Map<String, Object>>) session.getAttribute("importTablesList");
            for (int i = 0; i < importTablesList.size(); i++) {
                Map<String, Object> tablesMap = importTablesList.get(i);
                if (MapUtils.getString(map, "tableName").equals(MapUtils.getString(tablesMap, "partablename"))) {
                    List<Map<String, Object>> colmnsList = (ArrayList<Map<String, Object>>) tablesMap.get("columns");
                    for (int j = 0; j < colmnsList.size(); j++) {
                        Map<String, Object> colMap = colmnsList.get(j);
                        colList.add(colMap);
                    }
                    break;
                }
            }
        }
        //获取类型
        List<Map<String, Object>> colDatatypeList = metaSysCodeDAO.querySysCodeByCodeType(TblConstant.META_SYS_CODE_DATA_TYPE);
        WebContext webContext = WebContextFactory.get();
        //父表的行号
        String rowIndex = map.get("rowIndex");
        webContext.getHttpServletRequest().setAttribute("rowIndex", rowIndex);

        webContext.getHttpServletRequest().setAttribute("colList", colList);
        webContext.getHttpServletRequest().setAttribute("colDatatypeList", colDatatypeList);
        return webContext.forwardToString("/meta/module/tbl/import/subBatchImport.jsp");
    }

    public boolean validateTabName(String tableName, String owner) {
        return batchTableDAO.validateTabName(tableName, owner);
    }

    /**
     * @param datas
     * @return boolean
     * @throws
     * @Title: saveAllMes
     * @Description:信息保存
     */
    public boolean saveAllMes(List<Map<String, Object>> datas, String typeValue) {
        try {
            BaseDAO.beginTransaction();
            //新增META_TABLES信息
            List<Map<String, Long>> tableIdList = batchTableDAO.saveMetaTablesBatch(datas);

            List<Map<String, Object>> userTabRelList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> m : datas) {
                long tableId = 0;
                //新增列信息
                List<Map<String, Object>> colList = new ArrayList<Map<String, Object>>();
                ;
                List getList = null;
                String tableName = String.valueOf(m.get("tableName"));
                tableId = getTableId(tableName, tableIdList);
                String type = "";

//				int rowIndex = Integer.valueOf(String.valueOf((m.get("rowIndex"))));//行号
                if (m.containsKey("colDetail")) {
                    //列信息
                    getList = (List) m.get("colDetail");
                    type = "Y";
                } else {//查询列信息 
//					long dataSourceId = 0;
//					if(m.get("dataSourceId") != null && !"".equals(m.get("dataSourceId")) && !"null".equals(m.get("dataSourceId"))){
//						dataSourceId = Long.valueOf(String.valueOf(m.get("dataSourceId")));
//					}
//					Map<String,Object> dataSourceMap = metaTablesDAO.getUserNameByDataSourceId(Integer.valueOf(String.valueOf(dataSourceId)));
                    type = "N";
                    if (typeValue.equals("search")) {
                        colList = tableApplyDAO.queryDbTableColumns(m.get("dataSourceId").toString(), m.get("owner").toString(), m.get("praTableName").toString());
                    } else if (typeValue.equals("upload")) {
                        HttpSession session = WebContextFactory.get().getSession();
                        List<Map<String, Object>> importTablesList = (ArrayList<Map<String, Object>>) session.getAttribute("importTablesList");
                        for (int i = 0; i < importTablesList.size(); i++) {
                            Map<String, Object> tablesMap = importTablesList.get(i);
                            if (MapUtils.getString(m, "praTableName").equals(MapUtils.getString(tablesMap, "partablename"))) {
                                List<Map<String, Object>> colmnsList = (ArrayList<Map<String, Object>>) tablesMap.get("columns");
                                for (int j = 0; j < colmnsList.size(); j++) {
                                    Map<String, Object> colMap = colmnsList.get(j);
                                    colList.add(colMap);
                                }
                                break;
                            }
                        }
                    }
                }
                if (("N".equals(type) || (getList != null && getList.size() > 0)) && tableId > 0) {
                    if ("Y".equals(type)) {
                        for (int l = 0; l < getList.size(); l++) {
                            colList = (List<Map<String, Object>>) getList.get(l);
                        }
                    }
                    if (colList != null && colList.size() > 0) {
//						//保存列信息
                        List<Map<String, Long>> colIdList = batchTableDAO.saveColMesBatch(colList, type, tableId);

                        /*维度列关联信息*/
                        //根据新增表的ID查询表类字段信息
                        for (int i = 0; i < colList.size(); i++) {
                            //根据ID查询其关联的维度列
                            int dimTableId = Common.parseInt(colList.get(i).get("dimTableId")) == null ? 0 : Common.parseInt(colList.get(i).get("dimTableId"));
                            int dimColId = Common.parseInt(colList.get(i).get("dimColId")) == null ? 0 : Common.parseInt(colList.get(i).get("dimColId"));
                            long colId = getColId(String.valueOf(colList.get(i).get("colName")), colIdList);
                            if (dimTableId > 0 && dimColId > 0 && colId > 0) {
                                // 保存表类关系信息
                                Map<String, Object> insertData = new HashMap<String, Object>();
                                insertData.put("tableId1ColIds", colId);
                                insertData.put("tableId2ColIds", dimColId);
                                insertData.put("tableRelDesc", "");
                                insertData.put("tableRelType", 1);
                                insertData.put("tableId1", tableId);
                                insertData.put("tableId2", Common.parseInt(colList.get(i).get("dimTableId")));
                                if (!metaTableRelDAO.checkExist(insertData)) {
                                    metaTableRelDAO.insertMetaTableRel(insertData);
                                }
                            }
                        }
                    }
                }

                //新增描述用户与表的关系META_MAG_USER_TAB_REL
                if (tableId > 0) {
                    Map<String, Object> userTabRel = new HashMap<String, Object>();
                    userTabRel.put("userId", SessionManager.getCurrentUserID());
                    userTabRel.put("tableId", tableId);
                    userTabRel.put("relType", TblConstant.USER_TAB_REL_TYPE_APPLY);
                    userTabRel.put("tableState", 1);
                    userTabRel.put("tableVersion", 1);
                    userTabRel.put("tableName", tableName);
                    userTabRelList.add(userTabRel);
                }
                if (typeValue.equals("search")) {        //当为实体表查询时，简历实体表关系
                    /*新增实体表*/
                    if (tableId > 0) {
                        String praTableName = (m.get("praTableName") + "").toUpperCase();
                        Map<String, Object> tabInstMap = new HashMap<String, Object>();
                        tabInstMap.put("tableId", tableId);
                        tabInstMap.put("tableVersion", 1);
                        tabInstMap.put("tableName", praTableName);
                        tabInstMap.put("tableRecords", 0);
                        tabInstMap.put("tableOwner", m.get("owner"));
                        //owner、表名，获取实体表的表空间
                        long dataSourceId = Long.valueOf(String.valueOf(m.get("dataSourceId")));
                        Map<String, Object> dataSourceMap = metaTablesDAO.getUserNameByDataSourceId(Integer.valueOf(String.valueOf(dataSourceId)));
                        Map<String, Object> tabSpaceMap = batchTableDAO.getTabSpace(praTableName, dataSourceMap, String.valueOf(m.get("owner")));
                        tabInstMap.put("tableSpace", tabSpaceMap.get("TABLESPACE"));
                        tabInstMap.put("state", 1);
                        batchTableDAO.saveTabInst(tabInstMap);
                    }
                }
            }
            if (userTabRelList != null && userTabRelList.size() > 0) {
                batchTableDAO.saveUserTabRelBatch(userTabRelList);
            }
            BaseDAO.commit();
            return true;
        } catch (Exception e) {
            BaseDAO.rollback();
            Log.error(null, e);
            return false;
        }
    }

    List<Map<String, Object>> getColList(Object[] obj) {
        List<Map<String, Object>> colList = null;
        for (Object o : obj) {
            if (o != null) {
                colList = (List<Map<String, Object>>) o;
                break;
            }
        }
        return colList;
    }

    long getColId(String colName, List<Map<String, Long>> colIdList) {
        long colId = 0;
        for (Map<String, Long> map : colIdList) {
            for (Entry<String, Long> entry : map.entrySet()) {
                String key = entry.getKey();
                long value = entry.getValue();
                if (key.equals(colName.toUpperCase())) {
                    colId = value;
                    break;
                }
            }
        }
        return colId;
    }

    long getTableId(String tableName, List<Map<String, Long>> tableIdList) {
        long tableId = 0;
        for (Map<String, Long> map : tableIdList) {
            for (Entry<String, Long> entry : map.entrySet()) {
                String key = entry.getKey();
                long value = entry.getValue();
                if (key.equals(tableName.toUpperCase())) {
                    tableId = value;
                    break;
                }
            }
        }
        return tableId;
    }

    public int getCountsByTableName(String tableName) {
        return metaTablesDAO.getCountsByTableName(tableName);
    }

    public ImportTableDAO getImportTableDAO() {
        return importTableDAO;
    }

    public void setImportTableDAO(ImportTableDAO importTableDAO) {
        this.importTableDAO = importTableDAO;
    }

    public MetaDataSourceDAO getMetaDataSourceDAO() {
        return metaDataSourceDAO;
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
        this.metaDataSourceDAO = metaDataSourceDAO;
    }
}
