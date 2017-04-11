package tydic.meta.module.gdl.composite;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.module.gdl.GdlConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description  复合指标管理 Action
 * @date 12-3-29
 * -
 * @modify
 * @modifyDate -
 */
public class GdlCompositeMagAction {

    private GdlCompositeMagDAO compositeDAO;

    public void setCompositeDAO(GdlCompositeMagDAO compositeDAO) {
        this.compositeDAO = compositeDAO;
    }

    /**
     * 获取指标基本信息 以及支撑绑定维度
     * @param gdlId
     * @return
     */
    public Map<String,Object> getGdlInfo(int gdlId){
        Map<String,Object> gdlInfo = compositeDAO.queryGdlById(gdlId);
        if(gdlInfo!=null){
            gdlInfo.put("SUPPORT_DIMS",compositeDAO.getGdlSupportDimInfos(gdlId));//支撑维度
            gdlInfo.put("BIND_DIMS",compositeDAO.getGdlBindDims(gdlId,null));//绑定维度
            gdlInfo.put("GDL_GROUPINFO",compositeDAO.getGdlGroupIds(gdlId));
            gdlInfo.put("CAN_MODIFY",!compositeDAO.hasChildGdl(gdlId)); //有子不能修改
        }
        return gdlInfo;
    }

    /**
     * 查询指标信息
     * @param gdlId
     * @param gdlVersion
     * @return
     */
    public Map<String,Object> viewGdlInfo(int gdlId,int gdlVersion){
        Map<String,Object> gdlInfo = null;
        if(gdlVersion!=0){
            gdlInfo = compositeDAO.queryGdlByVersion(gdlId, gdlVersion);
        }else{
            gdlInfo = compositeDAO.queryGdlById(gdlId);
        }
        if(gdlInfo!=null){
            List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
            List<Map<String,Object>> list = compositeDAO.getGdlBindDims(gdlId,null);
            for (Map<String, Object> map : list) {
                String dimtabid = map.get("DIM_TABLE_ID").toString();
                String dimTypeId = map.get("DIM_TYPE_ID").toString();
                String code = map.get("DIM_CODE").toString();
                map.put("CODE_NAME", compositeDAO.getNameByCode(dimtabid, dimTypeId, code));
                listmap.add(map);
            }
            gdlInfo.put("SUPPORT_DIMS",compositeDAO.getGdlSupportDimInfos(gdlId));//支撑维度
            gdlInfo.put("BIND_DIMS",list);//绑定维度
            gdlInfo.put("GDL_GROUPINFO",compositeDAO.getGdlGroupIds(gdlId));
            gdlInfo.put("CAN_MODIFY",!compositeDAO.hasChildGdl(gdlId)); //有子不能修改
            gdlInfo.put("CODE_NAME", listmap);
        }
        return gdlInfo;
    }

    /**
     * 保存复合指标
     * @param data
     * @return
     */
    public Map<String,Object> saveCompositeGdl(Map<String,Object> data){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            int optFlag = MapUtils.getInteger(data,"OPT_FLAG",0);//0新增，1修改
            int gdlId = MapUtils.getInteger(data,"GDL_ID");
            String code = MapUtils.getString(data,"GDL_CODE");
            boolean saveFlag = true;
            if(compositeDAO.checkReGdlCode(code,optFlag==1?gdlId:0)){
                result.put("flag","codeExists");
                result.put("msg","编码重复!");
                saveFlag = false;
            }
            int baseId = compositeDAO.getBaseGdlIdByGdl(gdlId); //查询出基础指标
            if(saveFlag && compositeDAO.checkCompositeReGdl(data,baseId,(optFlag==0?0:gdlId))){
                result.put("flag","dimExists");
                result.put("msg","相关维度已发现重复指标!");
                saveFlag = false;
            }
            if(saveFlag){
                BaseDAO.beginTransaction();
                if(optFlag==0){  //添加
                    long pk = compositeDAO.queryForNextVal("SEQ_GDL_ID");
                    data.put("GDL_ID",pk);
                    data.put("GDL_STATE",GdlConstant.GDL_STATE_VALID);
                    int parVersion = MapUtils.getInteger(data,"GDL_VERSION");
                    data.put("GDL_VERSION",1);
                    data.put("GDL_TYPE", GdlConstant.GDL_TYPE_COMPOSITE);
                    compositeDAO.insertGDL(data);
                    compositeDAO.extendGdlMethod((int)pk,1,gdlId,parVersion);//继承父维度方法
                    String groups = MapUtils.getString(data,"GDL_GROUPINFO");
                    compositeDAO.addGdlGroup((int)pk,groups.split(","));//指标分类更新

                    Map<String,Object> bindDims = (Map<String,Object>)MapUtils.getMap(data,"BIND_DIMS");
                    compositeDAO.analysisGdlRel((int)pk,baseId,bindDims);//分析父指标关系
                    compositeDAO.extendParentGdlRel((int)pk,bindDims);//继承父表关系
                }else{  //修改
                    data.put("GDL_STATE",GdlConstant.GDL_STATE_VALID);
                    int preVersion = MapUtils.getInteger(data,"GDL_VERSION");
                    data.put("GDL_VERSION",preVersion+1);
                    data.put("GDL_TYPE", GdlConstant.GDL_TYPE_COMPOSITE);

                    compositeDAO.downLine(gdlId,preVersion);//设置前一版本为无效
                    compositeDAO.insertGDL(data);
                    compositeDAO.extendGdlMethod(gdlId,preVersion+1,gdlId,preVersion);//拷贝前一半的维度方法
                    String groups = MapUtils.getString(data,"GDL_GROUPINFO");
                    compositeDAO.addGdlGroup(gdlId,groups.split(",")); //指标分类更新

                    if(data.containsKey("BIND_DIMS")){//包含绑定维度信息，说明维度有更新
                        compositeDAO.deleteOldRel(gdlId);//删除原关系，重新分析确定关系

                        Map<String,Object> bindDims = (Map<String,Object>)MapUtils.getMap(data,"BIND_DIMS");
                        compositeDAO.analysisGdlRel(gdlId,baseId,bindDims);//分析父指标关系
                        compositeDAO.extendParentGdlRel(gdlId,bindDims);//继承父表关系
                    }
                }
                BaseDAO.commit();
            }
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("保存指标出错", e);
            result.put("flag","error");
            result.put("msg","");
        }
        return result;
    }

    /**
     * 查询复合指标列表，根据分类，关键字
     * @param data
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryCompositeByCond(Map<String,Object> data,Page page){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("gdlId","1");
        map.put("gdlCode","asdf");
        map.put("gdlType","1");
        map.put("gdlName","1erewewe");
        map.put("gdlUnit","ds");
        map.put("gdlCreateTime","1");
        map.put("gdlState","0");
        list.add(map);

        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("gdlId","2");
        map2.put("gdlCode","111");
        map2.put("gdlType","1");
        map2.put("gdlName","wwwww1");
        map2.put("gdlUnit","1");
        map2.put("gdlCreateTime","1");
        map2.put("gdlState","1");
        list.add(map2);
        return list;
    }

    /**
     * 查询有效指标 根据关键字
     * @param data 参数里面包含关键字参数和分页参数
     * @return
     */
    public List<Map<String,Object>> queryAvailableGdl(Map<String,Object> data){

        return null;
    }


}
