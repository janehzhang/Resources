package tydic.meta.common.term;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.module.gdl.GdlConstant;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description  维度树实现,需传入DIM_TABLE_ID,DIM_TYPE_ID,DIM_TABLE_NAME
 * @date 12-6-7
 * -
 * @modify
 * @modifyDate -
 */
public class DimTreeServiceImpl extends TermDataService{

    public Object[][] getData(DataAccess access, Map<String, Object> params, TermDataCall call) throws Exception {
        Map classPar = MapUtils.getMap(params,TermConstant.KEY_classRuleParams);
        if(classPar!=null){
            int termType = MapUtils.getInteger(params,TermConstant.KEY_termType);
            String tableName = MapUtils.getString(classPar, "DIM_TABLE_NAME");
            String prefix = MapUtils.getString(classPar,"TABLE_DIM_PREFIX");
            int typeId = MapUtils.getInteger(classPar,"DIM_TYPE_ID",0);
            if(tableName==null || prefix==null)return null;
            String sql = "SELECT ";
            sql += prefix+"_CODE,";
            sql += prefix+"_NAME,";
            sql += prefix+"_PAR_CODE,";
            sql += "(case " +
                    " when exists (select 1 " +
                    " from "+ GdlConstant.DIM_OWNER +tableName +
                    "  where "+prefix+"_PAR_ID = a."+prefix+"_ID AND DIM_TYPE_ID=?)" +
                    " then 1 else 0 end) hasChild,";
            sql += prefix+"_ID,";
            sql += prefix+"_PAR_ID,";
            sql += " DIM_TYPE_ID,";
            sql += " DIM_LEVEL ";
            sql += " FROM "+GdlConstant.DIM_OWNER + tableName + " a";
            sql += " WHERE STATE=1 " +
                    (termType==2?" AND DIM_LEVEL=1 ":"") +
                    " AND DIM_TYPE_ID=? " +
                    " ORDER BY ORDER_ID ";
            if(termType==2){
                String defValue = MapUtils.getString(params,TermConstant.KEY_defaultValue,"");
                if(!"".equals(defValue)){
                    sql  = getDimDefaultValuePathSql(params,access,classPar,sql);
                    call.coverTermAttribute(TermConstant.KEY_defValPathInited,true);
                    if(params.containsKey(TermConstant.KEY_defaultValuePath))
                        call.coverTermAttribute(TermConstant.KEY_defaultValuePath,params.get(TermConstant.KEY_defaultValuePath));
                }

                call.coverTermAttribute(TermConstant.KEY_dynload,true);
                call.coverTermAttribute(TermConstant.KEY_treeChildFlag,true);
            }
            return access.queryForArray(sql,false,typeId,typeId);
        }
        return null;
    }

    public Object[][] getChildData(DataAccess access,Map<String,Object> params,String parentID,TermDataCall call) throws Exception{
        Map classPar = MapUtils.getMap(params,TermConstant.KEY_classRuleParams);
        if(classPar!=null){
            String tableName = MapUtils.getString(classPar, "DIM_TABLE_NAME");
            String prefix = MapUtils.getString(classPar,"TABLE_DIM_PREFIX");
            int typeId = MapUtils.getInteger(classPar,"DIM_TYPE_ID",0);
            if(tableName==null || prefix==null)return null;
            String sql = "SELECT ";
            sql += prefix+"_CODE,";
            sql += prefix+"_NAME,";
            sql += prefix+"_PAR_CODE,";
            sql += "(case " +
                    " when exists (select 1 " +
                    " from "+ GdlConstant.DIM_OWNER +tableName +
                    "  where "+prefix+"_PAR_ID= a."+prefix+"_ID AND DIM_TYPE_ID=?)" +
                    " then 1 else 0 end) hasChild,";
            sql += " DIM_TYPE_ID,";
            sql += " DIM_LEVEL ";
            sql += " FROM "+GdlConstant.DIM_OWNER + tableName + " a";
            sql += " WHERE STATE=1 AND " +prefix+"_PAR_CODE=? " +
                    " AND DIM_TYPE_ID=? " +
                    " ORDER BY DIM_LEVEL,ORDER_ID ";
            return access.queryForArray(sql,false,typeId,parentID,typeId);
        }
        return null;
    }

    private static String getDimDefaultValuePathSql(Map<String, Object> termControl,DataAccess dataAccess,Map classPar,String execSql) throws Exception{
        String defValue = MapUtils.getString(termControl,TermConstant.KEY_defaultValue,"");
        String tableName = GdlConstant.DIM_OWNER + MapUtils.getString(classPar, "DIM_TABLE_NAME");
        String prefix = MapUtils.getString(classPar,"TABLE_DIM_PREFIX");
        int typeId = MapUtils.getInteger(classPar,"DIM_TYPE_ID",0);
        if(prefix!=null && !"".equals(defValue)){           //维度
            String dv = defValue;
            String valueCol = prefix+"_CODE";
            String textCol = prefix+"_NAME";
            String parCol = prefix+"_PAR_CODE";
            String id = prefix+"_ID";
            String pid = prefix + "_PAR_ID";
            if(!dv.contains("'")){
                dv = "'" + dv.replaceAll(",","','") + "'";
            }
            String childFlag = ",(case " +
                " when exists (select 1 " +
                        " from "+ tableName +
                        "  where "+prefix+"_PAR_ID = a."+prefix+"_ID AND DIM_TYPE_ID="+typeId+")" +
                        " then 1 else 0 end) hasChild";
            String dsql = "SELECT a."+valueCol+",a."+textCol+",a."+parCol+childFlag + ",a."+id+",a."+pid+",a.DIM_TYPE_ID,a.DIM_LEVEL " +
                        "FROM "+tableName+" a WHERE a.STATE=1 AND a.DIM_TYPE_ID="+typeId ;
            String dynLoadSql = dsql + " AND a."+parCol;
            dsql = "select distinct aaa."+parCol+",aaa.dim_level from ("+dsql+") aaa " +
                    "connect by prior aaa."+pid+"=aaa."+id+" start with " +
                    "aaa."+valueCol+" in("+dv+") order by aaa.dim_level ";
            Object[][] devR = dataAccess.queryForArray(dsql,false);
            if(devR.length==0)return execSql;

            Object[] dev = new Object[devR.length];
            String[] levelLen = new String[Convert.toInt(devR[devR.length - 1][1])];
            for (int i=0;i<devR.length;i++) {
                dev[i] = devR[i][0];
                if(levelLen[Convert.toInt(devR[i][1]) - 1]==null)
                    levelLen[Convert.toInt(devR[i][1]) - 1] = "";
                levelLen[Convert.toInt(devR[i][1]) - 1] +=  "'" + Convert.toString(devR[i][0]) +"'," ;
            }

            String sql_ = "select xy.* from("+execSql+") xy ";  //准备拼联合查询sql
            for(int i=0;i<levelLen.length;i++){
                if(levelLen[i]!=null){
                    levelLen[i] = levelLen[i].substring(0,levelLen[i].length()-1);
                    sql_ += " union ";
                    sql_ += " select x"+i+".* from(" + dynLoadSql + " IN("+levelLen[i]+") ORDER BY a.DIM_LEVEL,a.ORDER_ID) x"+i;
                }
            }
            termControl.put(TermConstant.KEY_defaultValuePath,dev);

            return sql_;
        }
        return execSql;
    }

}
