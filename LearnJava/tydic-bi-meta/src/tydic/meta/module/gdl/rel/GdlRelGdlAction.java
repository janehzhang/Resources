package tydic.meta.module.gdl.rel;

import tydic.frame.common.utils.Convert;
import tydic.meta.module.gdl.GdlDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标和指标关系Action <br>
 * @date 2012-6-13
 */
public class GdlRelGdlAction {
    private GdlRelGdlDAO gdlRelGdlDAO;
    private GdlDAO gdlDAO;

    /**
     * 查询指标与表类关系流程图数据
     * data = {
        links : [                                                   //箭头数组
            {from:"gdl001",to:"gdl001"},                            //from:箭头起点ID，to:箭头终点ID
            {from:"gdl001",to:"gdl002"}
        ],
        nodes :[                                                    //节点数组
            {uid : "gdl001", type : "node", text : "表001",level:0},//uid:ID，type：类型，text：显示文字，level：层级,color：节点颜色
            {uid : "gdl001", type : "node", text : "指标001",level:1},
            {uid : "gdl002", type : "node", text : "表002",level:1}
        ]
      }
     * @param gdlId
     * @return
     */
    public Map<String, Object> queryToFlowRelGdl(int gdlId){
        //子指标集合
        List<Map<String, Object>> childGdls = gdlRelGdlDAO.queryChildGdls(gdlId);
        //父指标集合
        List<Map<String, Object>> fatherGdls = gdlRelGdlDAO.queryFatherGdls(gdlId);

        Map<String, Object> typeMap = new HashMap<String, Object>();
        Map<String, Object> versionMap = new HashMap<String, Object>();
        Map<String, Object> codeMap = new HashMap<String, Object>();

        Map<String, Object> flowData = new HashMap<String, Object>();
        //构造数据
        //link 数组
        List<Map<String, Object>> linkList = new ArrayList<Map<String, Object>>();
        //node 数组
        List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
        //指标基本信息
        Map<String, Object> gdlInfo = gdlDAO.queryGdlById(gdlId);
        typeMap.put("gdl_"+gdlId, gdlInfo.get("GDL_TYPE"));
        versionMap.put("gdl_"+gdlId, gdlInfo.get("GDL_VERSION"));
        codeMap.put("gdl_"+gdlId, gdlInfo.get("GDL_CODE"));
        Map<String, Object> parNode = new HashMap<String, Object>();
        //加入根节点
        parNode.put("uid","gdl_"+gdlId);//ID
        parNode.put("type","node");//类型：节点
        parNode.put("text",gdlInfo.get("GDL_NAME"));//文字描述（指标名称）
        parNode.put("tooltip",gdlInfo.get("GDL_BUS_DESC"));//描述 （指标解释）
        parNode.put("level",0);//层级
        parNode.put("color","0xFF0000");
        nodeList.add(parNode);

        //设置子指标集合数据
        Map<String, Object> onlyOne = new HashMap<String, Object>();
        for(Map<String, Object> childM : childGdls){
            //设置子指标集合的连接数据
            Map<String, Object> link = new HashMap<String, Object>();
            link.put("from", "gdl_" + Convert.toString(childM.get("PAR_GDL_ID")));
            link.put("to", "gdl_" + Convert.toString(childM.get("GDL_ID")));
            // 设置线段的TEXT值
            List<Map<String, Object>> diffBandDims = gdlRelGdlDAO.queryDiffBandDim(Convert.toInt(childM.get("PAR_GDL_ID")),Convert.toInt(childM.get("GDL_ID")));
            //根据绑定的维度编码去实体维表查询编码对应的名称
            String linkText = "";
            for(Map<String, Object> oneBand : diffBandDims){
                String tableId = Convert.toString(oneBand.get("DIM_TABLE_ID"));
                String tableNameCn = Convert.toString(oneBand.get("TABLE_NAME_CN"));
                String dimTypeId = Convert.toString(oneBand.get("DIM_TYPE_ID"));
                String code = Convert.toString(oneBand.get("DIM_CODE"));
                String codeName = gdlDAO.getNameByCode(tableId, dimTypeId, code);
                linkText += tableNameCn + "：" + codeName + "，";
            }
            if(linkText.length()>0)
            linkText = linkText.substring(0, linkText.length() - 1);
            link.put("text",linkText);
            linkList.add(link);
            //设置子指标集合的节点数据
            Map<String, Object> node = new HashMap<String, Object>();
            node.put("uid", "gdl_" + Convert.toString(childM.get("GDL_ID")));
            node.put("type", "node");//类型：节点
            node.put("text", Convert.toString(childM.get("GDL_NAME")));
            node.put("tooltip", Convert.toString(childM.get("GDL_BUS_DESC")));
            node.put("level", Convert.toInt(childM.get("LV"), 0));
            if(Convert.toInt(childM.get("GDL_TYPE"))==1){//复合指标
                node.put("color","0xFFFF93");
            }else if(Convert.toInt(childM.get("GDL_TYPE"))==2){//计算指标
                node.put("color","0x00FFFF");
            }else if(Convert.toInt(childM.get("GDL_TYPE"))==0){//基础指标
                node.put("color","0xE4E4E4");
            }
            if(!onlyOne.containsKey("gdl_" + Convert.toString(childM.get("GDL_ID")))){
                onlyOne.put("gdl_" + Convert.toString(childM.get("GDL_ID")),true);
                nodeList.add(node);
                typeMap.put("gdl_" + Convert.toString(childM.get("GDL_ID")),
                        Convert.toString(childM.get("GDL_TYPE")));
                versionMap.put("gdl_" + Convert.toString(childM.get("GDL_ID")),
                        Convert.toString(childM.get("GDL_VERSION")));
                codeMap.put("gdl_" + Convert.toString(childM.get("GDL_ID")),
                        Convert.toString(childM.get("GDL_CODE")));
            }
        }

        //设置父指标集合数据
        for(Map<String, Object> parM : fatherGdls){
            //设置父指标集合的连接数据
            Map<String, Object> link = new HashMap<String, Object>();
            link.put("from", "gdl_" + Convert.toString(parM.get("PAR_GDL_ID")));
            link.put("to", "gdl_" + Convert.toString(parM.get("GDL_ID")));
            // 设置线段的TEXT值
            List<Map<String, Object>> diffBandDims = gdlRelGdlDAO.queryDiffBandDim(Convert.toInt(parM.get("PAR_GDL_ID")),Convert.toInt(parM.get("GDL_ID")));
            //根据绑定的维度编码去实体维表查询编码对应的名称
            String linkText = "";
            for(Map<String, Object> oneBand : diffBandDims){
                String tableId = Convert.toString(oneBand.get("DIM_TABLE_ID"));
                String tableNameCn = Convert.toString(oneBand.get("TABLE_NAME_CN"));
                String dimTypeId = Convert.toString(oneBand.get("DIM_TYPE_ID"));
                String code = Convert.toString(oneBand.get("DIM_CODE"));
                String codeName = gdlDAO.getNameByCode(tableId, dimTypeId, code);
                linkText += tableNameCn + "：" + codeName + "，";
            }
            if(linkText.length()>0)
            linkText = linkText.substring(0, linkText.length() - 1);
            link.put("text",linkText);

            linkList.add(link);
            //设置父指标集合的节点数据
            Map<String, Object> node = new HashMap<String, Object>();
            node.put("uid", "gdl_" + Convert.toString(parM.get("PAR_GDL_ID")));
            node.put("type", "node");//类型：节点
            node.put("text", Convert.toString(parM.get("GDL_NAME")));
            node.put("tooltip", Convert.toString(parM.get("GDL_BUS_DESC")));
            node.put("level", -Convert.toInt(parM.get("LV"), 0));
            if(Convert.toInt(parM.get("GDL_TYPE"))==1){//复合指标
                node.put("color","0xFFFF93");
            }else if(Convert.toInt(parM.get("GDL_TYPE"))==2){//计算指标
                node.put("color","0x00FFFF");
            }else if(Convert.toInt(parM.get("GDL_TYPE"))==0){//基础指标
                node.put("color","0xE4E4E4");
            }
            if(!onlyOne.containsKey("gdl_" + Convert.toString(parM.get("PAR_GDL_ID")))){
                onlyOne.put("gdl_" + Convert.toString(parM.get("PAR_GDL_ID")),true);
                typeMap.put("gdl_" + Convert.toString(parM.get("PAR_GDL_ID")), Convert.toString(parM.get("GDL_TYPE")));
                versionMap.put("gdl_" + Convert.toString(parM.get("PAR_GDL_ID")), Convert.toString(parM.get("GDL_VERSION")));
                codeMap.put("gdl_" + Convert.toString(parM.get("PAR_GDL_ID")), Convert.toString(parM.get("GDL_CODE")));
                nodeList.add(node);
            }
        }
        flowData.put("versionMap", versionMap);//指标版本数据
        flowData.put("typeMap", typeMap);//指标类型数据
        flowData.put("codeMap", codeMap);//指标类型数据
        flowData.put("links", linkList);//关联的箭头
        flowData.put("nodes", nodeList);//关联的节点
        return flowData;
    }

    public void setGdlDAO(GdlDAO gdlDAO) {
        this.gdlDAO = gdlDAO;
    }

    public void setGdlRelGdlDAO(GdlRelGdlDAO gdlRelGdlDAO) {
        this.gdlRelGdlDAO = gdlRelGdlDAO;
    }
}
