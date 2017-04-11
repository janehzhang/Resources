package tydic.meta.module.gdl.calc;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.gdl.GdlDAO;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class GdlCalcDAO extends GdlDAO{

    /**
     * 分析计算指标表达式
     * @param exprStr
     * @return 返回各个组成指标
     *          key  指标ID
     *          value 指标详细信息map
     */
    public Map<Integer,Map<String,Object>> analysisExpr(String exprStr){
        if(exprStr!=null && !"".equals(exprStr)){
            Matcher mcher = Pattern.compile("\\{(\\d+)\\}").matcher(exprStr);
            Set<Integer> gdlIds = new HashSet<Integer>();
            while(mcher.find()){
                String str = mcher.group(1);
                gdlIds.add(Convert.toInt(str));
            }
            Map<Integer,Map<String,Object>> map = new HashMap<Integer,Map<String, Object>>();
            for(Integer gdlId : gdlIds){
                Map<String,Object> gdl = queryGdlById(gdlId);
                gdl.put("BIND_DIMS",getGdlBindDims(gdlId,null));
                map.put(gdlId,gdl);
            }
            return map;
        }
        return null;
    }

    /**
     * 检测验证计算指标
     * @param exprStr
     * @param gdlId 需要排除的指标ID
     * @return
     */
    public boolean checkRelCalcGdl(String exprStr,int gdlId){
        String sql = "SELECT COUNT(1) FROM META_GDL WHERE GDL_CALC_EXPR=? AND GDL_ID!=?";
        return getDataAccess().queryForInt(sql,exprStr,gdlId)>=1;
    }



}
