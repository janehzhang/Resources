package tydic.meta.module.gdl.rel;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.Page;
import tydic.meta.module.gdl.GdlDAO;
import tydic.meta.module.gdl.GdlTabRelDAO;
import tydic.meta.module.gdl.GdlTabRelTermDAO;
import tydic.meta.module.tbl.MetaTablesDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标和表类关系Action <br>
 * @date 2012-6-5
 */
public class GdlRelAction {

    private GdlRelDAO gdlRelDAO;
    private GdlDAO gdlDAO;
    private GdlTabRelDAO gdlTabRelDAO;
    private GdlTabRelTermDAO gdlTabRelTermDAO;
    private MetaTablesDAO metaTablesDAO;
    private GdlRelGdlDAO gdlRelGdlDAO;

    /**
     * 查询指标与表类关系流程图数据
     * data = {
        links : [                                                   //箭头数组
            {from:"tab001",to:"gdl001"},                            //from:箭头起点ID，to:箭头终点ID
            {from:"gdl001",to:"tab002"}
        ],
        nodes :[                                                    //节点数组
            {uid : "tab001", type : "node", text : "表001",level:0},//uid:ID，type：类型，text：显示文字，level：层级。。。
            {uid : "gdl001", type : "node", text : "指标001",level:1},
            {uid : "tab002", type : "node", text : "表002",level:1}
        ]
      }
     * @param gdlId
     * @return
     */
    public Map<String, Object> queryToFlowRelTable(int gdlId){
        //查询出所有有关联的表类信息
        List<Map<String, Object>> allRelTables = gdlRelDAO.queryRelTable(gdlId);

        Map<String, Object> flowData = new HashMap<String, Object>();
        //构造数据
        //link 数组
        List<Map<String, Object>> linkList = new ArrayList<Map<String, Object>>();
        //node 数组
        List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
        //指标基本信息
        Map<String, Object> gdlInfo = gdlDAO.queryGdlById(gdlId);
        //如果是基础指标，查出GDL_SRC_TABLE_ID 既直接数据来源
        int gdlSrcTableId = 0;
        if(Convert.toInt(gdlInfo.get("GDL_TYPE"))==0){//指标是基础指标
            gdlSrcTableId = Convert.toInt(gdlInfo.get("GDL_SRC_TABLE_ID"), 0);
        }

        Map<String, Object> parNode = new HashMap<String, Object>();
        //加入根节点
        parNode.put("uid","gdl_"+gdlId);//ID
        parNode.put("type","node");//类型：节点
        parNode.put("text",gdlInfo.get("GDL_NAME"));//文字描述（指标名称）
        parNode.put("tooltip",gdlInfo.get("GDL_BUS_DESC"));//描述 （指标解释）
        parNode.put("level",0);//层级
        parNode.put("color","0xDAEBF3");
        nodeList.add(parNode);

        //若表为直接数据来源，则箭头从表类打向指标，若关联表为间接数据来源，则箭头由指标打向表类
        for(Map<String, Object> relTable:allRelTables){
            //REL_TYPE为0表示该宽表为直接数据来源
            if(Convert.toString(relTable.get("REL_TYPE")).equals("0")){
                Map<String, Object> link = new HashMap<String, Object>();
                link.put("from", "tbl_"+Convert.toString(relTable.get("TABLE_ID")));
                link.put("to", "gdl_"+Convert.toString(relTable.get("GDL_ID")));
                link.put("text", Convert.toString(relTable.get("COL_NAME")));
                linkList.add(link);
                Map<String, Object> node = new HashMap<String, Object>();
                if(gdlSrcTableId!=0){
                    if(gdlSrcTableId == Convert.toInt(relTable.get("TABLE_ID"))){
                        node.put("color", "0x47760E");
                    }
                }
                node.put("uid", "tbl_" + Convert.toString(relTable.get("TABLE_ID")));
                node.put("type", "node");//类型：节点
                node.put("text", Convert.toString(relTable.get("TABLE_NAME")));//文字描述（表类名称）
                node.put("tooltip", Convert.toString(relTable.get("TABLE_NAME_CN")));//文字描述（表类中文名称）
                node.put("level", -1);
                nodeList.add(node);
            }else{//非直接数据来源宽表
                Map<String, Object> link = new HashMap<String, Object>();
                link.put("from", "tbl_"+Convert.toString(relTable.get("TABLE_ID")));
                link.put("to", "gdl_"+Convert.toString(relTable.get("GDL_ID")));
                link.put("text", Convert.toString(relTable.get("COL_NAME")));
                linkList.add(link);
                Map<String, Object> node = new HashMap<String, Object>();
                node.put("uid", "tbl_"+Convert.toString(relTable.get("TABLE_ID")));
                node.put("type","node");//类型：节点
                node.put("text",Convert.toString(relTable.get("TABLE_NAME")));//文字描述（表类名称）
                node.put("tooltip", Convert.toString(relTable.get("TABLE_NAME_CN")));//文字描述（表类中文名称）
                node.put("level", 1);
                nodeList.add(node);
            }
        }
        int gdlVersion = Convert.toInt(gdlInfo.get("GDL_VERSION"), 0);
        int gdlType = Convert.toInt(gdlInfo.get("GDL_TYPE"), 0);
        flowData.put("links", linkList);//关联的箭头
        // 过滤linkList
        flowData.put("nodes", nodeList);//关联的节点
        //源表ID
        flowData.put("gdlSrcTableId", gdlSrcTableId);
        flowData.put("gdlVersion", gdlVersion);
        flowData.put("gdlType", gdlType);
        flowData.put("gdlCode", gdlInfo.get("GDL_CODE"));
        return flowData;
    }

    /**
     * 查询指标对应的备选宽表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryLeftTable(Map<String,Object> queryData,Page page){

        return gdlRelDAO.queryLeftTable(queryData,page);
    }

    /**
     * 根据表类ID查询该表指标列信息
     * @param tableId
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryGdlColByTableId(int tableId, Page page){

        return gdlRelDAO.queryGdlColByTableId(tableId, page);
    }

    /**
     * 查询指标的绑定维度
     * @param gdlId
     * @return
     */
    public List<Map<String, Object>> queryBandDim(int gdlId,int tableId, Page page){

        return gdlRelDAO.queryGdlBindDimsByTableId(gdlId,tableId, page);
    }

    /**
     * 查询指标的支撑维度
     * @param gdlId
     * @return
     */
    public List<Map<String, Object>> querySupportDim(int gdlId,int tableId, Page page){

        return gdlRelDAO.querySupportDimsByTableId(gdlId,tableId, page);
    }

    /**
     * 插入指标——表类关联关系表
     * @param insData
     * @return
     */
    public String insertTableRel(Map<String, Object> insData){
        try{
            BaseDAO.beginTransaction();
            //当前指标ID
            int gdlId = Convert.toInt(insData.get("gdlId"), 0);
            int tableId = Convert.toInt(insData.get("tableId"), 0);
            int colId = Convert.toInt(insData.get("colId"), 0);
            //插入当前关系数据
            long gdlTblRelId = gdlTabRelDAO.queryForNextVal("SEQ_GDL_TBL_REL_ID");
            gdlTabRelDAO.insertRel(gdlTblRelId, gdlId, tableId,colId,0,0);
            //插入当前指标对应的绑定维度数据
            List<Map<String, Object>> basicRelTerms = gdlTabRelTermDAO.queryBasicDetail(gdlId);
            //TODO 1 直接添加的关系不需要条件
//            for(Map<String, Object> bMap : basicRelTerms){
//                Map<String, Object> insertData = new HashMap<String, Object>();
//                insertData.put("gdlId", gdlId);
//                insertData.put("dimTypeId", bMap.get("DIM_TYPE_ID"));
//                insertData.put("gdlTblRelId", gdlTblRelId);
//                insertData.put("dimTableId", bMap.get("DIM_TABLE_ID"));
//                insertData.put("dimLevel", bMap.get("DIM_LEVEL"));
//                insertData.put("dimColId", bMap.get("DIM_COL_ID"));
//                insertData.put("dimCode", bMap.get("DIM_CODE"));
//                insertData.put("orderId", bMap.get("ORDER_ID"));
//                gdlTabRelTermDAO.insertGdlTblRelTerm(insertData);
//            }

            //当前指标的所有子指标
            List<Map<String, Object>> subGdlIds = gdlRelDAO.queryAllChildGdlIds(gdlId);
            for(Map<String, Object> idMap : subGdlIds){
                int subGdlId = Convert.toInt(idMap.get("GDL_ID"));

                //循环添加子指标关系
                // 判断该子指标绑定维度集合是否是所选宽表支撑维度列的子集，是才维护
                if(gdlRelDAO.checkBindDim(subGdlId, tableId)){
                    //TODO 2子层级=当前层级+绑定纬度差
//                    int level = Convert.toInt(idMap.get("LV"));
                    List<Map<String, Object>> diffBandDims = gdlRelGdlDAO.queryDiffBandDim(gdlId, subGdlId);
                    long subGdlTblRelId = gdlTabRelDAO.queryForNextVal("SEQ_GDL_TBL_REL_ID");

                    gdlTabRelDAO.insertRel(subGdlTblRelId, subGdlId, tableId, colId, diffBandDims.size(), 1);
                    List<Map<String, Object>> subBasicRelTerms = gdlTabRelTermDAO.queryBasicDetail(subGdlId);
                    for(Map<String, Object> subMap : subBasicRelTerms){
                        boolean isDiff = false;
                        for(Map<String, Object> diffM : diffBandDims){
                            if(Convert.toString(diffM.get("DIM_TABLE_ID"))
                                    .equals(Convert.toString(subMap.get("DIM_TABLE_ID")))){
                                isDiff = true;
                            }
                        }
                        if(isDiff){//3添加条件时应只添加该子指标和当前指标绑定维度的差集 相当于子的DIM_TABLE_ID不在父的绑定维度中
                            Map<String, Object> insertData = new HashMap<String, Object>();
                            insertData.put("gdlId", subGdlId);
                            insertData.put("dimTypeId", subMap.get("DIM_TYPE_ID"));
                            insertData.put("gdlTblRelId", subGdlTblRelId);
                            insertData.put("dimTableId", subMap.get("DIM_TABLE_ID"));
                            insertData.put("dimLevel", subMap.get("DIM_LEVEL"));
                            insertData.put("dimColId", subMap.get("DIM_COL_ID"));
                            insertData.put("dimCode", subMap.get("DIM_CODE"));
                            insertData.put("orderId", subMap.get("ORDER_ID"));
                            gdlTabRelTermDAO.insertGdlTblRelTerm(insertData);
                        }
                    }
                }
            }

            BaseDAO.commit();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            BaseDAO.rollback();
            return e.getMessage();
        }
    }

    /**
     * 修改指标关联宽表关系信息
     * @param updateData
     * @return
     */
    public String updateTableRel(Map<String, Object> updateData){
        try{
            BaseDAO.beginTransaction();
            //当前指标ID
            int gdlId = Convert.toInt(updateData.get("gdlId"), 0);
            int tableId = Convert.toInt(updateData.get("tableId"), 0);
            int colId = Convert.toInt(updateData.get("colId"), 0);
            //先修改当前指标关系
            gdlTabRelDAO.updateByTableIdAndGdlId(colId, tableId, gdlId);
            //修改子指标和表类的关系
            //当前指标的所有子指标
            List<Map<String, Object>> subGdlIds = gdlRelDAO.queryAllChildGdlIds(gdlId);
            for(Map<String, Object> idMap : subGdlIds){
                int subGdlId = Convert.toInt(idMap.get("GDL_ID"));
                // 判断该子指标绑定维度集合是否是所选宽表支撑维度列的子集，是才维护
                if(gdlRelDAO.checkBindDim(subGdlId, tableId)){
                    gdlTabRelDAO.updateByTableIdAndGdlId(colId, tableId, subGdlId);
                }
            }
            BaseDAO.commit();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            BaseDAO.rollback();
            return e.getMessage();
        }
    }

    /**
     * 删除指标关联宽表关系信息
     * @param updateData
     * @return
     */
    public String deleteTableRel(Map<String, Object> updateData){
        try{
            BaseDAO.beginTransaction();
            //当前指标ID
            int gdlId = Convert.toInt(updateData.get("gdlId"), 0);
            int tableId = Convert.toInt(updateData.get("tableId"), 0);
//            int colId = Convert.toInt(updateData.get("colId"), 0);
            //先删除当前指标关系以及其对应的绑定维度关系
            long gdlTblRelId = gdlTabRelDAO.queryGdlTblRelIdByTableIdAndGldId(tableId, gdlId);
            gdlTabRelTermDAO.deleteByGdlTblRelId((int)gdlTblRelId);
            gdlTabRelDAO.deleteByTableIdAndGldId(tableId, gdlId);

            //当前指标的所有子指标
            List<Map<String, Object>> subGdlIds = gdlRelDAO.queryAllChildGdlIds(gdlId);
            for(Map<String, Object> idMap : subGdlIds){
                int subGdlId = Convert.toInt(idMap.get("GDL_ID"));
                // 判断该子指标绑定维度集合是否是所选宽表支撑维度列的子集，是才维护
                if(gdlRelDAO.checkBindDim(subGdlId, tableId)){
                    long subGdlTblRelId = gdlTabRelDAO.queryGdlTblRelIdByTableIdAndGldId(tableId, subGdlId);
                    gdlTabRelTermDAO.deleteByGdlTblRelId((int)subGdlTblRelId);
                    gdlTabRelDAO.deleteByTableIdAndGldId(tableId, subGdlId);
                }
            }
            BaseDAO.commit();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            BaseDAO.rollback();
            return e.getMessage();
        }
    }

    /**
     * 根据表类ID和指标ID取对应的唯一一条的指标-表类关系数据
     * @param tableId
     * @param gdlId
     * @return
     */
    public Map<String, Object> queryInfoByTableIdAndGdlId(int tableId, int gdlId){
        return gdlTabRelDAO.queryInfoByTableIdAndGdlId(tableId, gdlId);
    }

    /**
     * 拓扑图节点双击事件，返回该节点表的详细信息
     * @param nodeIdTxt
     * @return
     */
    public Map<String, Object> flowNodeDetail(String nodeIdTxt){
        String nodeId = nodeIdTxt.replace("tbl_", "");
        return metaTablesDAO.queryTableByTableId(Integer.parseInt(nodeId));
    }

    public void setGdlRelGdlDAO(GdlRelGdlDAO gdlRelGdlDAO) {
        this.gdlRelGdlDAO = gdlRelGdlDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setGdlTabRelTermDAO(GdlTabRelTermDAO gdlTabRelTermDAO) {
        this.gdlTabRelTermDAO = gdlTabRelTermDAO;
    }

    public void setGdlDAO(GdlDAO gdlDAO) {
        this.gdlDAO = gdlDAO;
    }

    public void setGdlRelDAO(GdlRelDAO gdlRelDAO) {
        this.gdlRelDAO = gdlRelDAO;
    }

    public void setGdlTabRelDAO(GdlTabRelDAO gdlTabRelDAO) {
        this.gdlTabRelDAO = gdlTabRelDAO;
    }
}
