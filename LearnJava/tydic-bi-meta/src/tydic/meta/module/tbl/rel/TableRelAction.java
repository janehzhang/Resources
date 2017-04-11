package tydic.meta.module.tbl.rel;

import java.util.*;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.MetaTableRelDAO;
import tydic.meta.module.tbl.MetaTablesDAO;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 熊小平 刘斌
 * @date 2011-11-1
 * @description 表类关系控制类;，查询表类关系，以供全息视图调用
 * 
 */
public class TableRelAction {
    /**
     * 数据库操作类
     */
    private MetaTableRelDAO metaTabelRelDAO;
    private MetaTablesDAO metaTablesDAO;
    private MetaTableColsDAO metaTableColsDAO;

    /**
     * 查询表类关系
     * 
     * @param queryData 参数里只包含tableId参数，根据tableId查询该表类的关系
     * @return
     */
    public List<Map<String, Object>> queryTableRels(Map<String, Object> queryData,Page page) {
        if(page == null){
            page = new Page(0,20);
        }
        if (queryData != null) {
            if (queryData.get("tableId") != null
                    && !queryData.get("tableId").toString().equals("")) {
                List<Map<String, Object>> allRel = metaTabelRelDAO.queryDetailByTableId(Integer.parseInt(queryData.get("tableId").toString()), null);
                for(int i=0; i<allRel.size(); i++){
                    Map info = allRel.get(i);
                    if(Integer.parseInt(info.get("TABLE_ID1").toString())!=Integer.parseInt(queryData.get("tableId").toString())){
                        if(info.get("TABLE_REL_TYPE").toString().equals("2"))
                            info.put("TABLE_REL_TYPE", 3);
                        info.put("TABLE_ID2",info.get("TABLE_ID1"));
                        info.put("TABLE_ID1", Integer.parseInt(queryData.get("tableId").toString()));
                        String tempName=info.get("T1NAME").toString();
                        info.put("T1NAME", info.get("T2NAME"));
                        info.put("T2NAME", tempName);
                        String temNamecn=info.get("T1NAMECN").toString();
                        info.put("T1NAMECN", info.get("T2NAMECN"));
                        info.put("T2NAMECN", temNamecn);
                        String colStr=info.get("TABLE_ID1_COL_IDS").toString();
                        info.put("TABLE_ID1_COL_IDS", info.get("TABLE_ID2_COL_IDS"));
                        info.put("TABLE_ID2_COL_IDS", colStr);
                    }
                }
                //整合列信息
                for(int i = 0; i < allRel.size(); i++){
                    Map<String, Object> infoMap = allRel.get(i);
                    //表1的有效版本
                    int tb1Version = metaTablesDAO.queryValidVersion(Integer.parseInt(infoMap.get("TABLE_ID1").toString()));
                    //表2的有效版本
                    int tb2Version = metaTablesDAO.queryValidVersion(Integer.parseInt(infoMap.get("TABLE_ID2").toString()));

                    //列1字符串
                    String colIdStr1 = Convert.toString(infoMap.get("TABLE_ID1_COL_IDS"));
                    String colIds1[] = colIdStr1.split(",");
                    String col1Str = "";
                    for(int j = 0; j < colIds1.length; j++){
                        int colId = Integer.parseInt(colIds1[j]);
                        Map<String, Object> colInfo1 = metaTableColsDAO.queryColByColId(colId, tb1Version);
//                col1Str = col1Str + colInfo1.get("COL_NAME")+"："+colInfo1.get("COL_NAME_CN")+"，";
                        col1Str = col1Str + colInfo1.get("COL_NAME")+",";
                        if(j == colIds1.length - 1 && !"".equals(col1Str)){
                            col1Str = col1Str.substring(0,col1Str.length()-1);
                        }

                    }
                    infoMap.put("COLINFO1", col1Str);

                    //列2字符串
                    String colIdStr2 = Convert.toString(infoMap.get("TABLE_ID2_COL_IDS"));
                    String colIds2[] = colIdStr2.split(",");
                    String col2Str = "";
                    for(int j = 0; j < colIds2.length; j++){
                        int colId = Integer.parseInt(colIds2[j]);
                        Map<String, Object> colInfo2 = metaTableColsDAO.queryColByColId(colId, tb2Version);
//                col2Str = col2Str + colInfo2.get("COL_NAME")+"："+colInfo2.get("COL_NAME_CN")+",";
                        col2Str = col2Str + colInfo2.get("COL_NAME")+",";
                        if(j == colIds2.length - 1 && !"".equals(col2Str)){
                            col2Str = col2Str.substring(0, col2Str.length() - 1);
                        }
                    }
                    infoMap.put("COLINFO2", col2Str);
                }
                return allRel;
            }
        }
        Log.warn("参数有误：talbeId没有或不为整数");
        return null;
    }

    /**
     * 查询表类关系，生成流程图数据
     * @param queryData
     * @return 返回的数据格式实例：
     * data = {
        links : [                                                   //箭头数组
            {from:"tab001",to:"tab002"},                            //from:箭头起点ID，to:箭头终点ID
            {from:"tab001",to:"tab003"}
        ],
        nodes :[                                                    //节点数组
            {uid : "tab001", type : "node", text : "表001",level:0},//uid:ID，type：类型，text：显示文字，level：层级。。。
            {uid : "tab002", type : "node", text : "表002",level:1},
            {uid : "tab003", type : "node", text : "表003",level:1}
        ]
    };
     * //增加节点为负数的情况
     */
    public Map<String, Object> queryToFlow(Map<String, Object> queryData){
        int tableId = Integer.parseInt(queryData.get("tableId").toString());//表ID
        int level = Integer.parseInt(queryData.get("level").toString());//层级
        if(level == 0){
            level = 1;
        }
        Map<String, Object> tableInfo = metaTablesDAO.queryTableByTableId(tableId, metaTablesDAO.queryValidVersion(tableId));

        Map<String, Object> flowData = new HashMap<String, Object>();
        //构造数据
        //link 数组
        List<Map<String, Object>> linkList = new ArrayList<Map<String, Object>>();
        //node 数组
        List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();

        Map<String, String> uidMap=new HashMap<String, String>();
        uidMap.put("TABLE_"+tableId,"TABLE_"+tableId);
        linkList = mergList(genarallinkListP(linkList,level,tableId), genarallinkListN(linkList,level,tableId));
        nodeList = mergList(genaralnodeListP(nodeList,level,tableId,uidMap,1), genaralnodeListN(nodeList,level,tableId,uidMap,1));

        //过滤重复节点
        Map<String, String> filter = new HashMap<String, String>();
        List<Map<String, Object>> newNodes = new ArrayList<Map<String, Object>>();
        for(int i=0; i<nodeList.size(); i++){
            Map<String, Object> itr = nodeList.get(i);
            String uids=itr.get("uid").toString();
            String levels=itr.get("level").toString();
            if(filter.containsKey(uids+"_"+levels)){

            }else{
                newNodes.add(itr);
                filter.put(uids+"_"+levels,"");
            }
        }

        Map<String, Object> parNode = new HashMap<String, Object>();
        //加入根节点
        parNode.put("uid","TABLE_"+tableId);//ID
        parNode.put("type","node");//类型：节点
        parNode.put("text",tableInfo.get("TABLE_NAME"));//文字描述
        parNode.put("tooltip",tableInfo.get("TABLE_NAME_CN"));//描述
        parNode.put("level",0);//层级
        newNodes.add(parNode);

        flowData.put("links", this.cleanLinkList(linkList, "TABLE_"+tableId));//关联的箭头
        // 过滤linkList
        flowData.put("nodes", newNodes);//关联的节点
        if(level>1){
            return flowDataInit(flowData, "TABLE_"+tableId);
        }else{
            return flowData;
        }
    }

    /**
     * 初始化节点数据 在节点层次大于一时调用
     * @param flowData
     * @return
     */
    private Map<String, Object> flowDataInit(Map<String, Object> flowData, String mainTblIdStr){
        List<Map<String, Object>> nodes = (List<Map<String, Object>>)flowData.get("nodes");
        List<Map<String, Object>> links = (List<Map<String, Object>>)flowData.get("links");

        int maxP = 0; //正极节点最大level
        int maxN = 0; //负极节点最大level
        String maxPIdStr = ""; //正极最大节点
        String maxNIdStr = ""; //负极最大节点

        for(int i = 0; i < nodes.size(); i++){
            Map<String, Object> node = nodes.get(i);
            if(Integer.parseInt(node.get("level").toString())>maxP){
                maxP = Integer.parseInt(node.get("level").toString());
                maxPIdStr = Convert.toString(node.get("uid"));
            }
            if(Integer.parseInt(node.get("level").toString())<maxN){
                maxN = Integer.parseInt(node.get("level").toString());
                maxNIdStr = Convert.toString(node.get("uid"));
            }
        }
        //处理负极最大节点
        List<String> wrongNodeN = new ArrayList<String>();
        for(int i=0; i < links.size(); i++){
            Map<String, Object> link = links.get(i);
            if(Convert.toString(link.get("from")).equals(maxNIdStr)){
                for(int j = 0; j < nodes.size(); j++){
                    Map<String, Object> node = nodes.get(j);
                    if(Convert.toString(node.get("uid")).equals(Convert.toString(link.get("to")))){
                        if(Integer.parseInt(node.get("level").toString())!=maxN++){
                            nodes.get(j).put("level", maxN);
                            wrongNodeN.add(Convert.toString(node.get("uid")));
                        }
                    }
                }
            }
        }
        //处理方向错误的线段
        for(int i=0; i < links.size(); i++){
            Map<String, Object> link = links.get(i);
            for(int j=0; j < wrongNodeN.size(); j++){
                if(Convert.toString(link.get("to")).equals(wrongNodeN.get(j))
                        && Convert.toString(link.get("from")).equals(mainTblIdStr)){
                    links.get(i).put("from", wrongNodeN.get(j));
                    links.get(i).put("to", mainTblIdStr);
                }
            }
        }
        return flowData;
    }


    /**
     * 清理LinkList中的冗余数据
     * @return
     */
    private List<Map<String, Object>> cleanLinkList(List<Map<String, Object>> linkList, String mainTableIdStr){
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < linkList.size(); i++){
            Map<String, Object> linkMap = linkList.get(i);
            String linkFrom = Convert.toString(linkMap.get("from"));
            String linkTo = Convert.toString(linkMap.get("to"));
            boolean canAdd = true;
            for(int j = 0; j < rtnList.size(); j++){
                Map<String, Object> tempMap = rtnList.get(j);
                String tempFrom = Convert.toString(tempMap.get("from"));
                String tempTo = Convert.toString(tempMap.get("to"));
                if(linkFrom.equals(tempFrom)&&linkTo.equals(tempTo)){
                    canAdd = false;
                    break;
                }
                //清理正反向的箭头，既{from:2,to:3},{from:3,to:2}，
                //清理掉 mainTableIdStr连过来的，即如果有双向箭头，始终让箭头方向是从主表射出
                if(tempFrom.equals(linkTo) && tempTo.equals(linkFrom)){
                    if(tempFrom.equals(mainTableIdStr)){
                        canAdd = false;
                        break;
                    }else{
                        tempMap.put("from", linkTo);
                        tempMap.put("to", linkFrom);
                        canAdd = false;
                        break;
                    }
                }
            }
            if(canAdd){
                rtnList.add(linkMap);
            }
        }
        return rtnList;
    }

    /**
     * 取得正向箭头
     * @param linkList
     * @param level
     * @return
     */
    private List<Map<String, Object>> genarallinkListP(List<Map<String, Object>> linkList, int level,int tableId){
        List<Map<String, Object>> initDataP = metaTabelRelDAO.queryInitFlowDataP(tableId);
        for(int i = 0; i < initDataP.size(); i++){
            Map<String, Object> itrData = initDataP.get(i);
            Map<String, Object> link = new HashMap<String, Object>();
            link.put("from", "TABLE_"+tableId);//箭头起始
            link.put("to", "TABLE_"+itrData.get("TABLE_ID2"));//箭头终止
            linkList.add(link);
            if(level > 1){
                linkList = mergList(linkList, genarallinkListP(linkList, level-1, Integer.parseInt(itrData.get("TABLE_ID2").toString())));
            }
        }
        return linkList;
    }

    /**
     * 取得负向箭头
     * @param linkList
     * @param level
     * @param tableId
     * @return
     */
    private List<Map<String, Object>> genarallinkListN(List<Map<String, Object>> linkList, int level,int tableId){
        List<Map<String, Object>> initDataN = metaTabelRelDAO.queryInitFlowDataN(tableId);
        for(int i = 0; i < initDataN.size(); i++){
            Map<String, Object> itrData = initDataN.get(i);
            Map<String, Object> link = new HashMap<String, Object>();
            link.put("to", "TABLE_"+tableId);//箭头起始
            link.put("from", "TABLE_"+itrData.get("TABLE_ID1"));//箭头终止
            linkList.add(link);
            if(level > 1){
                linkList = mergList(linkList, genarallinkListN(linkList, level-1, Integer.parseInt(itrData.get("TABLE_ID1").toString())));
            }
        }
        return linkList;
    }

    /**
     * 获取正向节点
     * @param nodeList
     * @param level
     * @param tableId
     * @return
     */
    private List<Map<String, Object>> genaralnodeListP(List<Map<String, Object>> nodeList, int level,int tableId,Map<String, String> uidMap, int start){
        List<Map<String, Object>> initDataP = metaTabelRelDAO.queryInitFlowDataP(tableId);
        for(int i = 0; i < initDataP.size(); i++){
            Map<String, Object> itrData = initDataP.get(i);
            Map<String, Object> node = new HashMap<String, Object>();
            node.put("uid","TABLE_"+itrData.get("TABLE_ID2"));//ID
            node.put("type","node");//类型：节点
            node.put("text",itrData.get("T2NAME"));//文字描述
            node.put("tooltip",itrData.get("T2NAMECN"));//描述
            node.put("level",start);//层级
            if(!uidMap.containsKey("TABLE_"+itrData.get("TABLE_ID2"))){
                nodeList.add(node);
                uidMap.put("TABLE_"+itrData.get("TABLE_ID2"),"TABLE_"+itrData.get("TABLE_ID2"));
            }
            if(level > start){
                nodeList = mergList(nodeList, genaralnodeListP(nodeList,level, Integer.parseInt(itrData.get("TABLE_ID2").toString()),uidMap,start+1) );
            }
        }
        return nodeList;
    }

    /**
     * 获取负向节点
     * @param nodeList
     * @param level
     * @param tableId
     * @return
     */
    private List<Map<String, Object>> genaralnodeListN(List<Map<String, Object>> nodeList, int level,int tableId,Map<String, String> uidMap,int start){
        List<Map<String, Object>> initDataN = metaTabelRelDAO.queryInitFlowDataN(tableId);
        for(int i = 0; i < initDataN.size(); i++){
            Map<String, Object> itrData = initDataN.get(i);
            Map<String, Object> node = new HashMap<String, Object>();
            node.put("uid","TABLE_"+itrData.get("TABLE_ID1"));//ID
            node.put("type","node");//类型：节点
            node.put("text",itrData.get("T1NAME"));//文字描述
            node.put("tooltip",itrData.get("T1NAMECN"));//描述
            node.put("level",-start);//层级(负数)
            if(!uidMap.containsKey("TABLE_"+itrData.get("TABLE_ID1"))){
                nodeList.add(node);
                uidMap.put("TABLE_"+itrData.get("TABLE_ID1"),"TABLE_"+itrData.get("TABLE_ID1"));
            }
            if(level > start){
                nodeList = mergList(nodeList, genaralnodeListN(nodeList,level, Integer.parseInt(itrData.get("TABLE_ID1").toString()),uidMap,start+1));
            }
        }
        return nodeList;
    }


    /**
     * 合并两个List成新的List
     * @param linkList2
     * @param linkList1
     * @return
     */
    private List<Map<String, Object>> mergList(List<Map<String, Object>> linkList2, List<Map<String, Object>> linkList1){
        List<Map<String, Object>> rtnl = new ArrayList<Map<String, Object>>();
        for(int i=0; i<linkList2.size(); i++){
            rtnl.add(linkList2.get(i));
        }
        for(int i=0; i<linkList1.size(); i++){
            rtnl.add(linkList1.get(i));
        }
        return rtnl;
    }


    /**
     * 拓扑图箭头双击事件，返回两个表的关联关系详细信息
     *
     * @param data
     * @return
     * 返回是列表形式的数据结构，结构示例如下：
     * [
        {
            COLINFO1=asdsa：某某列，阿斯达说的：某某列2，。。。,
            TABLE_REL_DESC=sfdafd,
            TABLE_ID1_COL_IDS=1,
            TABLE_ID2_COL_IDS=2,
            COLINFO2=asdsa：某某列，阿斯达说的：某某列2，。。。,
            T1NAME=test_{yyyymm},
            T1NAMECN=测试表,
            TABLE_REL_TYPE=2,
            TABLE_REL_ID=3,
            T2NAME=test_{YYYYMMDD},
            TABLE_ID2=3,
            TABLE_ID1=1,
            T2NAMECN=测试表
        }
       ]
     */
    public List<Map<String, Object>> flowLinkDetail(Map<String, Object> data){
        int tb1Id = Integer.parseInt(Convert.toString(data.get("idFrom")).replace("TABLE_",""));//起始表ID
        int tb2Id = Integer.parseInt(Convert.toString(data.get("idTo")).replace("TABLE_",""));//目标表ID
        //表1的有效版本
        int tb1Version = metaTablesDAO.queryValidVersion(tb1Id);
        //表2的有效版本
        int tb2Version = metaTablesDAO.queryValidVersion(tb2Id);
        //整合列信息
        List<Map<String, Object>> tblRelInfo = metaTabelRelDAO.queryDetailsByTbl1Tb2(tb1Id, tb2Id, tb1Version, tb2Version);
        // 加上反向连接的数据
        List<Map<String, Object>> tblRelInfoN = metaTabelRelDAO.queryDetailsByTbl1Tb2(tb2Id, tb1Id, tb2Version, tb1Version);
        //将反向箭头处理为正向显示，既页面上Table1Id始终是当前表，关联关系类型2转3
        for(int i=0; i<tblRelInfoN.size(); i++){
            Map info = tblRelInfoN.get(i);
            if(Integer.parseInt(info.get("TABLE_ID1").toString())!=tb1Id){
                if(info.get("TABLE_REL_TYPE").toString().equals("2"))
                    info.put("TABLE_REL_TYPE", 3);
                info.put("TABLE_ID2",info.get("TABLE_ID1"));
                info.put("TABLE_ID1", tb1Id);
                String tempName=Convert.toString(info.get("T1NAME"));
                info.put("T1NAME", Convert.toString(info.get("T2NAME")));
                info.put("T2NAME", tempName);
                String temNamecn=Convert.toString(info.get("T1NAMECN"));
                info.put("T1NAMECN", Convert.toString(info.get("T2NAMECN")));
                info.put("T2NAMECN", temNamecn);
                String colStr=Convert.toString(info.get("TABLE_ID1_COL_IDS"));
                info.put("TABLE_ID1_COL_IDS", Convert.toString(info.get("TABLE_ID2_COL_IDS")));
                info.put("TABLE_ID2_COL_IDS", colStr);
                String tmpVersion=Convert.toString(info.get("T1VERSION").toString());
                info.put("T1VERSION", Convert.toString(info.get("T2VERSION")));
                info.put("T2VERSION", tmpVersion);
            }
        }

        tblRelInfo = this.mergList(tblRelInfo, tblRelInfoN);

        for(int i = 0; i < tblRelInfo.size(); i++){
            Map<String, Object> infoMap = tblRelInfo.get(i);
            //列1字符串
            String colIdStr1 = Convert.toString(infoMap.get("TABLE_ID1_COL_IDS"));
            String colIds1[] = colIdStr1.split(",");
            String col1Str = "";
            String col1Type = "";//列的类型
            for(int j = 0; j < colIds1.length; j++){
                int colId = Integer.parseInt(colIds1[j]);
                Map<String, Object> colInfo1 = metaTableColsDAO.queryColByColId(colId, Integer.parseInt(infoMap.get("T1VERSION").toString()));
//                col1Str = col1Str + colInfo1.get("COL_NAME")+"："+colInfo1.get("COL_NAME_CN")+",";
                col1Str = col1Str + colInfo1.get("COL_NAME")+",";
                col1Type = col1Type + colInfo1.get("COL_DATATYPE") + ",";
                if(j == colIds1.length - 1){
                    col1Str = col1Str.substring(0,col1Str.length()-1);
                    col1Type = col1Type.substring(0,col1Type.length()-1);
                }

            }
            infoMap.put("COLINFO1", col1Str);
            infoMap.put("COLTYPE1", col1Type);

            //列2字符串
            String colIdStr2 = Convert.toString(infoMap.get("TABLE_ID2_COL_IDS"));
            String colIds2[] = colIdStr2.split(",");
            String col2Str = "";
            String col2Type = "";
            for(int j = 0; j < colIds2.length; j++){
                int colId = Integer.parseInt(colIds2[j]);
                Map<String, Object> colInfo2 = metaTableColsDAO.queryColByColId(colId, Integer.parseInt(infoMap.get("T2VERSION").toString()));
//                col2Str = col2Str + colInfo2.get("COL_NAME")+"："+colInfo2.get("COL_NAME_CN")+",";
                col2Str = col2Str + colInfo2.get("COL_NAME")+",";
                col2Type = col2Type + colInfo2.get("COL_DATATYPE")+",";
                if(j == colIds2.length - 1 && !"".equals(col2Str)){
                    col2Str = col2Str.substring(0, col2Str.length() - 1);
                    col2Type = col2Type.substring(0, col2Type.length() - 1);
                }
            }
            infoMap.put("COLINFO2", col2Str);
            infoMap.put("COLTYPE2", col2Type);
        }

        // 加入判断是否是维度关联列
        List<Map<String, Object>> dimRel = metaTabelRelDAO.queryForDimRel(tb1Id, tb2Id);

        for(int i = 0; i < tblRelInfo.size(); i++){
            Map<String, Object> infoMap = tblRelInfo.get(i);
            String dimRelStr = "";

            String[] id1S = Convert.toString(infoMap.get("TABLE_ID1_COL_IDS")).split(",");
            String[] id2S = Convert.toString(infoMap.get("TABLE_ID2_COL_IDS")).split(",");
            int max = id1S.length > id2S.length ? id2S.length : id1S.length;
            for(int k = 0; k < max; k++){
                boolean isDim = false;
                for(int j = 0; j < dimRel.size(); j++){

                    Map<String, Object> dimRInfo = dimRel.get(j);
                    String dimCol1 = Convert.toString(dimRInfo.get("COL_ID"));
                    String dimCol2 = Convert.toString(dimRInfo.get("DIM_COL_ID"));
                    if((dimCol1.equals(id1S[k])&&dimCol2.equals(id2S[k]))
                            ||(dimCol2.equals(id1S[k])&&dimCol1.equals(id2S[k]))){
                        isDim = true;
                    }
                }
                if(isDim){
                    dimRelStr = dimRelStr + "true" + ",";
                }else{
                    dimRelStr = dimRelStr + "false" + ",";
                }
                if(k == max - 1){
                    dimRelStr = dimRelStr.substring(0, dimRelStr.length() - 1);
                }
            }
            infoMap.put("dimRel", dimRelStr);

        }
        return tblRelInfo;
    }

    /**
     * 拓扑图节点双击事件，返回该节点表的详细信息
     * @param nodeIdTxt
     * @return
     */
    public Map<String, Object> flowNodeDetail(String nodeIdTxt){
        String nodeId = nodeIdTxt.replace("TABLE_", "");
        return metaTablesDAO.queryTableByTableId(Integer.parseInt(nodeId), metaTablesDAO.queryValidVersion(Integer.parseInt(nodeId)));
    }

    /**
     * 根据表Id取关联关系信息
     * 传给前台表格展现
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryRelForGrid(int tableId){
        List<Map<String, Object>> allRel = metaTabelRelDAO.queryDetailByTableId(tableId, null);
        for(int i=0; i<allRel.size(); i++){
            Map info = allRel.get(i);
            if(Integer.parseInt(info.get("TABLE_ID1").toString())!=tableId){
                if(info.get("TABLE_REL_TYPE").toString().equals("2"))
                    info.put("TABLE_REL_TYPE", 3);
                info.put("TABLE_ID2",info.get("TABLE_ID1"));
                info.put("TABLE_ID1", tableId);
                String tempName=info.get("T1NAME").toString();
                info.put("T1NAME", info.get("T2NAME"));
                info.put("T2NAME", tempName);
                String temNamecn=info.get("T1NAMECN").toString();
                info.put("T1NAMECN", info.get("T2NAMECN"));
                info.put("T2NAMECN", temNamecn);
                String colStr=Convert.toString(info.get("TABLE_ID1_COL_IDS"));
                info.put("TABLE_ID1_COL_IDS", info.get("TABLE_ID2_COL_IDS"));
                info.put("TABLE_ID2_COL_IDS", colStr);
            }
        }
        //整合列信息
        for(int i = 0; i < allRel.size(); i++){
            Map<String, Object> infoMap = allRel.get(i);
            //表1的有效版本
            int tb1Version = metaTablesDAO.queryValidVersion(Integer.parseInt(infoMap.get("TABLE_ID1").toString()));
            //表2的有效版本
            int tb2Version = metaTablesDAO.queryValidVersion(Integer.parseInt(infoMap.get("TABLE_ID2").toString()));
            //列1字符串
            String colIdStr1 = Convert.toString(infoMap.get("TABLE_ID1_COL_IDS"));
            String colIds1[] = colIdStr1.split(",");
            String col1Str = "";
            String col1Tpye = "";//列1数据类型
            for(int j = 0; j < colIds1.length; j++){
                int colId = Integer.parseInt(colIds1[j]);
                Map<String, Object> colInfo1 = metaTableColsDAO.queryColByColId(colId, tb1Version);

//                col1Str = col1Str + colInfo1.get("COL_NAME")+"："+colInfo1.get("COL_NAME_CN")+"，";
                col1Str = col1Str + colInfo1.get("COL_NAME")+",";
                col1Tpye = col1Tpye + colInfo1.get("COL_DATATYPE")+",";
                if(j == colIds1.length - 1){
                    col1Str = col1Str.substring(0,col1Str.length()-1);
                    col1Tpye = col1Tpye.substring(0,col1Tpye.length() - 1);
                }

            }
            infoMap.put("COLINFO1", col1Str);
            infoMap.put("COLTYPE1", col1Tpye);

            //列2字符串
            String colIdStr2 = Convert.toString(infoMap.get("TABLE_ID2_COL_IDS"));
            String colIds2[] = colIdStr2.split(",");
            String col2Str = "";
            String col2Type = "";
            for(int j = 0; j < colIds2.length; j++){
                int colId = Integer.parseInt(colIds2[j]);
                Map<String, Object> colInfo2 = metaTableColsDAO.queryColByColId(colId, tb2Version);
//                col2Str = col2Str + colInfo2.get("COL_NAME")+"："+colInfo2.get("COL_NAME_CN")+",";
                col2Str = col2Str + colInfo2.get("COL_NAME")+",";
                col2Type = col2Type + colInfo2.get("COL_DATATYPE")+",";
                if(j == colIds2.length - 1 && !"".equals(col2Str)){
                    col2Str = col2Str.substring(0, col2Str.length() - 1);
                    col2Type = col2Type.substring(0, col2Type.length() - 1);
                }
            }
            infoMap.put("COLINFO2", col2Str);
            infoMap.put("COLTYPE2", col2Type);
        }
        return allRel;
    }

    /**
     * 页面左侧待选表类查询方法
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryLeftTable(Map<String,Object> queryData,Page page){
        int tableId = Convert.toInt(queryData.get("excludTableId"), 0);
        if(page == null){
            page = new Page(0,20);
        }
        List<Map<String,Object>> allTables = metaTablesDAO.queryMetaTablesMatain(queryData, page);
        Integer[] relIds = metaTabelRelDAO.queryExistRelTableId(tableId);
        for(Map<String,Object> m:allTables){
            for(Integer relTableId:relIds){
                if(Convert.toInt(m.get("TABLE_ID"),0) == relTableId){
                    m.put("related",true);
                }
            }
        }
        return allTables;
    }

    /**
     * 判断两表是否有维度列关联
     * @param table1Id
     * @param table2Id
     * @return
     */
    public boolean isRelDim(int table1Id, int table2Id){
        return metaTabelRelDAO.queryForDimRel(table1Id, table2Id).size()>0;
    }

    // setter
    public void setMetaTabelRelDAO(MetaTableRelDAO metaTabelRelDAO) {
        this.metaTabelRelDAO = metaTabelRelDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
        this.metaTableColsDAO = metaTableColsDAO;
    }
}
