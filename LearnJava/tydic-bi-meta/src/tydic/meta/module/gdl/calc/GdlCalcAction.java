package tydic.meta.module.gdl.calc;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.gdl.GdlConstant;
import tydic.meta.module.gdl.GdlGroupDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description
 * @date 12-6-11
 * -
 * @modify
 * @modifyDate -
 */
public class GdlCalcAction {

    private GdlCalcDAO calcDAO ;
    private GdlGroupDAO groupDAO;

    public void setCalcDAO(GdlCalcDAO calcDAO) {
        this.calcDAO = calcDAO;
    }

    public void setGroupDAO(GdlGroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    /**
     * 获取一个计算指标的信息
     * @param gdlId
     * @return
     */
    public Map<String,Object> getGdlInfo(int gdlId){
        Map<String,Object> gdl = calcDAO.queryGdlById(gdlId);
        String exprStr = MapUtils.getString(gdl,"GDL_CALC_EXPR");
        Map<Integer,Map<String,Object>> exprGdls = calcDAO.analysisExpr(exprStr);
        gdl.put("EXPR_GDLS",exprGdls);
        gdl.put("GDL_GROUPINFO",calcDAO.getGdlGroupIds(gdlId));
        return gdl;
    }

    /**
     * 查看指标信息
     * @param gdlId
     * @param version
     * @return
     */
    public Map<String,Object> viewGdlInfo(int gdlId,int version){
        Map<String,Object> gdl = calcDAO.queryGdlByVersion(gdlId,version);
        String exprStr = MapUtils.getString(gdl,"GDL_CALC_EXPR");
        Map<Integer,Map<String,Object>> exprGdls = calcDAO.analysisExpr(exprStr);
        gdl.put("EXPR_GDLS",exprGdls);
        gdl.put("GDL_GROUPINFO",calcDAO.getGdlGroupIds(gdlId));
        return gdl;
    }

    /**
     * 指标树
     * @param data 包含关键字，或父ID
     * @return
     */
    public Map<String,Object> queryGdlTree(Map<String,Object> data){
        String keyWord = MapUtils.getString(data, "keyWord", "");
        int parentId = MapUtils.getInteger(data,"parentId",0);
        List<Map<String,Object>> gps = groupDAO.queryGroup(parentId,keyWord);
        Map<String,Object> d = new HashMap<String,Object>();//条件容器
        d.put("GDL_GROUP",parentId);
        d.put("GDL_STATE", GdlConstant.GDL_STATE_VALID);
        d.put("NOT_GDL_TYPE",GdlConstant.GDL_TYPE_EXPR);//排除计算指标
        List<Map<String,Object>> gdls = calcDAO.queryGdl(d,null);
        for(Map<String,Object> gdl : gdls){
            gdl.put("BIND_DIMS",calcDAO.getGdlBindDims(MapUtils.getInteger(gdl,"GDL_ID"),null));
        }

        d.clear();//清空作为数据容器
        d.put("GROUPS",gps);
        d.put("GDLS",gdls);
        return d;
    }

    /**
     * 保存计算指标
     * @param data
     * @return
     */
    public Map<String,Object> saveCalcGdl(Map<String,Object> data){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            int gdlId = MapUtils.getInteger(data,"GDL_ID",0);
            String code = MapUtils.getString(data,"GDL_CODE");
            String exprStr = MapUtils.getString(data,"GDL_CALC_EXPR");
            boolean saveFlag = true;
            if(calcDAO.checkReGdlCode(code,gdlId!=0?gdlId:0)){
                result.put("flag","codeExists");
                result.put("msg","编码重复!");
                saveFlag = false;
            }
            if(saveFlag && gdlId!=0 && calcDAO.checkRelCalcGdl(exprStr,gdlId!=0?gdlId:0)){
                result.put("flag","calcExists");
                result.put("msg","发现有相同计算表达式的指标!");
                saveFlag = false;
            }
            if(saveFlag){
                BaseDAO.beginTransaction();
                if(gdlId==0){
                    //添加
                    long pk = calcDAO.queryForNextVal("SEQ_GDL_ID");
                    data.put("GDL_ID",pk);
                    data.put("GDL_STATE",GdlConstant.GDL_STATE_VALID);
                    data.put("GDL_VERSION",1);
                    data.put("GDL_TYPE", GdlConstant.GDL_TYPE_EXPR);
                    calcDAO.insertGDL(data);

                    String groups = MapUtils.getString(data,"GDL_GROUPINFO");
                    calcDAO.addGdlGroup((int)pk,groups.split(","));//指标分类更新

                    String parGdls = MapUtils.getString(data,"PARENT_GDLS","");
                    calcDAO.setParentGdlRel((int)pk,parGdls.split(","));//设置父指标
                }else{
                    //修改
                    data.put("GDL_STATE",GdlConstant.GDL_STATE_VALID);
                    int preVersion = MapUtils.getInteger(data,"GDL_VERSION");
                    data.put("GDL_VERSION",preVersion+1);
                    data.put("GDL_TYPE", GdlConstant.GDL_TYPE_EXPR);

                    calcDAO.downLine(gdlId,preVersion);//设置前一版本为无效
                    calcDAO.insertGDL(data);
                    String groups = MapUtils.getString(data,"GDL_GROUPINFO");
                    calcDAO.addGdlGroup(gdlId,groups.split(",")); //指标分类更新

                    String parGdls = MapUtils.getString(data,"PARENT_GDLS","");
                    calcDAO.setParentGdlRel(gdlId,parGdls.split(","));

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


}
