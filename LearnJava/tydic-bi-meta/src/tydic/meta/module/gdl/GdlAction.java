package tydic.meta.module.gdl;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 指标管理查询页面Action，以及提供给外部模块页面的一些Action
 * @date 12-6-2
 * -
 * @modify
 * @modifyDate -
 */
public class GdlAction {
    
    private GdlDAO gdlDAO;
    private GdlGroupDAO groupDAO;
    private GdlAlterHisDAO hisDAO;

    public void setGdlDAO(GdlDAO gdlDAO) {
        this.gdlDAO = gdlDAO;
    }

    public void setGroupDAO(GdlGroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public void setHisDAO(GdlAlterHisDAO hisDAO) {
        this.hisDAO = hisDAO;
    }

    /**
     * 查询指标
     * @param data
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryGdl(Map<String,Object> data,Page page){
        return gdlDAO.queryGdl(data,page);
    }

    /**
     * 上线下线
     * @param gdlId 指标ID
     * @param state 0下线，1上线
     * @param version 版本
     * @return
     */
    public int upOrDownGdl(int gdlId,int state,int version){
        int ret = 0;
        try{
            BaseDAO.beginTransaction();
            if(state==1){
                gdlDAO.upLine(gdlId,version);
                Map<String,Object> his = new HashMap<String,Object>();
                his.put("GDL_ID",gdlId);
                his.put("GDL_VERSION",version);
                his.put("ALTER_TYPE",GdlConstant.GDL_ALTER_TYPE_UP);
                hisDAO.insertGdlAlterHis(his);
            }else{
                gdlDAO.downLine(gdlId,version);
                Map<String,Object> his = new HashMap<String,Object>();
                his.put("GDL_ID",gdlId);
                his.put("GDL_VERSION",version);
                his.put("ALTER_TYPE",GdlConstant.GDL_ALTER_TYPE_DOWN);
                hisDAO.insertGdlAlterHis(his);
            }
            ret = 1;
            BaseDAO.commit();
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("指标上下线报错:"+e);
            ret = -1;
        }
        return ret;
    }
    
}
