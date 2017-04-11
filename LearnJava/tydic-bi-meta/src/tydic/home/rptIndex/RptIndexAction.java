package tydic.home.rptIndex;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Order;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

import java.math.BigDecimal;
import java.util.*;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 报表首页Action
 * @date 12-4-9
 * -
 * @modify
 * @modifyDate -
 */
public class RptIndexAction {

    private RptIndexDAO rptIndexDAO;

    /**
     * 获取我创建的报表
     * @param num 获取数量，num=0时，取系统设置的一默认值
     * @return
     */
    public List<Map<String,Object>> queryMyCreateRpt(int num){
        Page page = new Page(0,num!=0?num:RptIndexConstant.MY_CREATE);
        int userId = SessionManager.getCurrentUserID();
        return rptIndexDAO.queryMyCreateRpt(userId, page);
    }

    /**
     * 获取我收藏的报表
     * @param num 获取数量，num=0时，取系统设置的一默认值
     * @return
     */
    public List<Map<String,Object>> queryMyFavRpt(int num){
        Page page = new Page(0,num!=0?num:RptIndexConstant.MY_FAV);
        int userId = SessionManager.getCurrentUserID();
        return rptIndexDAO.queryMyFavRpt(userId, page);
    }

    /**
     * 获取我订阅的报表
     * @param num 获取数量，num=0时，取系统设置的一默认值
     * @return
     */
    public List<Map<String,Object>> queryMyPushRpt(int num){
        Page page = new Page(0,num!=0?num:RptIndexConstant.MY_PUSH);
        int userId = SessionManager.getCurrentUserID();
        return rptIndexDAO.queryMyPushRpt(userId, page);
    }

    /**
     * 获取公共报表
     * @param num 获取数量，num=0时，取系统设置的一默认值
     * @return
     */
    public List<Map<String,Object>> queryPublicRpt(int num){
        Page page = new Page(0,num!=0?num:RptIndexConstant.PUBLIC);
        return rptIndexDAO.queryPublicRpt(page);
    }

    /**
     * 获取别人共享给我的报表
     * @param num 获取数量，num=0时，取系统设置的一默认值
     * @return
     */
    public List<Map<String,Object>> querySharingRpt(int num){
        Page page = new Page(0,num!=0?num:RptIndexConstant.SHARING);
        int userId = SessionManager.getCurrentUserID();
        return rptIndexDAO.querySharingRpt(userId, page);
    }

    /**
     * 根据关键字搜索模型模型字段 (用于搜索框 onkeyup事件时调用)
     * @param kwd
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> queryFields(String kwd){
        List<Map<String,Object>> datas = rptIndexDAO.queryModelFieldByKwd(kwd);
        Map<String,Map<String,Object>> fieldMap = new HashMap<String,Map<String,Object>>();
        for(Map<String,Object> map : datas){
            String key = MapUtils.getString(map,"COL_ALIAS");
            String issueId = MapUtils.getString(map,"ISSUE_ID");
            String colId = MapUtils.getString(map,"COL_ID");
            if(fieldMap.containsKey(key)){
                Map<String,Object> field = fieldMap.get(key);
                Map<String,Object> tabm = (Map<String,Object>) MapUtils.getMap(field,"issues");
                tabm.put(issueId,issueId);
                Map<String,Object> colm = (Map<String,Object>) MapUtils.getMap(field,"cols");
                colm.put(colId,colId);
            }else{
                Map<String,Object> field = new HashMap<String,Object>();
                field.put("fieldName",key);//字段名

                Map<String,Object> tabm = new HashMap<String, Object>();
                tabm.put(issueId,issueId);
                field.put("issues",tabm);  //存放此字段在那些模型存在

                Map<String,Object> colm = new HashMap<String, Object>();
                colm.put(colId,colId);
                field.put("cols",colm);  //存放此字段在对应那些列

                field.put("cnt",MapUtils.getString(map,"CNT"));//存放字段被模型应用次数
                fieldMap.put(key,field);
            }
        }
        List<Map<String,Object>> fieldList = new ArrayList<Map<String, Object>>(fieldMap.values());
        Collections.sort(fieldList,new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                int cnt1 = MapUtils.getInteger(map1,"cnt");
                int cnt2 = MapUtils.getInteger(map2,"cnt");
                return cnt1-cnt2;
            }
        });
        return fieldList;
    }

    /**
     * 根据关键字或指标过滤的模型ID集搜索匹配模型
     * @param kwd
     * @param issues
     * @param seld
     * @param orderField
     * @param orderMode
     * @param posStart
     * @param pageSize
     * @return
     */
    public List<Map<String,Object>> queryModels(String kwd,String[] issues,int seld,String orderField,String orderMode,int posStart,int pageSize){
        if(seld>0 && issues.length==0){
            return new ArrayList<Map<String,Object>>();
        }
        Page page = new Page(posStart,pageSize);
        List<Map<String,Object>> datas = rptIndexDAO.queryModels(kwd, issues, new Order(orderMode,orderField), page);
        return datas;
    }

    /**
     * 根据字段搜索匹配相似报表
     * @param colIds 字段
     * @param fieldNum 选择的字段数
     * @param num 获取数量，当num=0时，取系统设置的一默认值
     * @return
     */
    public List<Map<String,Object>> queryApproximateRptByField(String[] colIds,int fieldNum,int num){
        Page page = new Page(0,num!=0?num:RptIndexConstant.APPROXIMATE_RPT);
        List<Map<String,Object>> datas = rptIndexDAO.queryApproximateRptByCols(colIds,fieldNum,page,true);
        for(int i=0;i<datas.size();){
            Map<String,Object> rpt = datas.get(i);
            double p = MapUtils.getDouble(rpt,"APPROXIMATE");
            if(p>0){
                BigDecimal bd = new BigDecimal(p);
                bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
                rpt.put("APPROXIMATE_P",bd+"%");
                i++;
            }else{
                datas.remove(i);
            }
        }
        return datas;
    }

    /**
     * 首页可展示的大分类
     * @return
     */
    public List<Map<String,Object>> queryBusTypeByShowIndex(){
        return rptIndexDAO.queryBusTypeByShowIndex(false);
    }

    /**
     * 根据一个分类大类 查找其子类和下面的字段（属于孙子的字段需要递归到子）
     * @param parentId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> querySubTypeAndFieldsByParent(int parentId){
        List<Map<String,Object>> datas = rptIndexDAO.querySubType(parentId);
        datas.add(rptIndexDAO.getBusType(parentId)); //将父类放入其中
        for(int i=0;i<datas.size();i++){
            Map<String,Object> type = datas.get(i);
            Integer typeId = MapUtils.getInteger(type,"COL_TYPE_ID");
            String typeName = MapUtils.getString(type,"COL_TYPE_NAME");

            //分别初始每个分类下的所有字段
            List<Map<String,Object>> allFields = rptIndexDAO.queryFieldsByBusType(typeId,parentId!=typeId);
            Map<String,Map<String,Object>> fieldMap = new HashMap<String,Map<String,Object>>();
            for(Map<String,Object> map : allFields){
                String key = MapUtils.getString(map,"COL_ALIAS");
                String issueId = MapUtils.getString(map,"ISSUE_ID");
                String colId = MapUtils.getString(map,"COL_ID");
                if(fieldMap.containsKey(key)){
                    Map<String,Object> field = fieldMap.get(key);
                    Map<String,Object> tabm = (Map<String,Object>) MapUtils.getMap(field,"issues");
                    tabm.put(issueId,issueId);
                    Map<String,Object> colm = (Map<String,Object>) MapUtils.getMap(field,"cols");
                    colm.put(colId,colId);
                }else{
                    Map<String,Object> field = new HashMap<String,Object>();
                    field.put("fieldName",key);//字段名

                    Map<String,Object> tabm = new HashMap<String, Object>();
                    tabm.put(issueId,issueId);
                    field.put("issues",tabm);  //存放此字段在那些模型存在

                    Map<String,Object> colm = new HashMap<String, Object>();
                    colm.put(colId,colId);
                    field.put("cols",colm);  //存放此字段在对应那些列

                    field.put("cnt",MapUtils.getString(map,"CNT"));//存放字段被模型应用次数
                    fieldMap.put(key,field);
                }
            }
            List<Map<String,Object>> fieldList = new ArrayList<Map<String, Object>>(fieldMap.values());
            Collections.sort(fieldList,new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                    int cnt1 = MapUtils.getInteger(map1,"cnt");
                    int cnt2 = MapUtils.getInteger(map2,"cnt");
                    return cnt1-cnt2;
                }
            });

            //
            if(typeId==parentId && datas.size()>1){
                if(fieldList.size()>0){
                    type.put("typeId",typeId); //类型ID
                    type.put("typeName","其他");//只有父类型名称
                    type.put("fieldList",fieldList);//包含字段列表
                }else{
                    datas.remove(i);
                }
            }else{
                type.put("typeId",typeId); //类型ID
                type.put("typeName",typeName);
                type.put("fieldList",fieldList);//包含字段列表
            }
        }
        return datas;
    }

    public void setRptIndexDAO(RptIndexDAO rptIndexDAO) {
        this.rptIndexDAO = rptIndexDAO;
    }

}
