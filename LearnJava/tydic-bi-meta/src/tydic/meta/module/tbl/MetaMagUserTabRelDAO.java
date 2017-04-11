package tydic.meta.module.tbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 *
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @date 2011-10-27
 * @description META_MAG_USER_TAB_REL操作DAO
 *
 */
public class MetaMagUserTabRelDAO extends MetaBaseDAO {

    /**
     * @description 往META_MAG_USER_TAB_REL表中插入单条记录
     * @param data map形式的参数，其key为相应字段的驼峰格式命名
     * @return true插入成功，false插入失败
     */
    public boolean insertMetaMagUserTable(Map<String, Object> data) throws Exception{
        String sql = "INSERT INTO META_MAG_USER_TAB_REL("
                     + "REL_ID,USER_ID,TABLE_NAME,REL_TYPE,STATE_DATE,TABLE_ID,TABLE_VERSION,STATE_MARK,TABLE_STATE) "
                     + "VALUES(?,?,?,?,sysdate,?,?,?,?)";
        List params = new ArrayList();
        long pk = super.queryForNextVal("SEQ_TAB_REL_ID");
        params.add(pk);
        Object userId = data.get("userId");
        if(userId == null){
            //非空约束
            return false;
        }else{
            params.add(userId);
        }

        String tableName = Convert.toString(data.get("tableName")).toUpperCase();
        params.add(tableName);

        Object relType = data.get("relType");
        if(relType==null){
            //非空约束
            return false;
        }else{
            params.add(relType);
        }

        // 状态时间取系统时间
//        Date stateDate = new Date();
//        params.addValue(stateDate);

        Object tableId = data.get("tableId");
        if(tableId == null){
            //非空约束
            return false;
        }else{
            params.add(tableId);
        }
        params.add(data.get("tableVersion"));
        params.add(data.get("stateMark"));
        params.add(data.get("tableState"));
        return getDataAccess().execUpdate(sql, params.toArray())>0;
    }
    /**
     * 根据表ID取得表类变动历史
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryByTableId(int tableId, Page page){
        String sql = "SELECT  A.USER_ID AS APPLYUSER, " +
                     " U1.USER_NAMECN AS APPLYUSERNAME, " +
                     " A.TABLE_VERSION AS APPLYTYPE, " +
                     " A.REL_TYPE, " +
                     " TO_CHAR(A.STATE_DATE, 'yyyy-MM-dd hh24:mi:ss') AS APPLYDATE, " +
                     " A.TABLE_ID, " +
                     " A.TABLE_VERSION, " +
                     " T.TABLE_STATE, " +
                     " A.USER_ID AS AUDITUSER, " +
                     " U1.USER_NAMECN AS AUDITUSERNAME, " +
                     " TO_CHAR(A.STATE_DATE, 'yyyy-MM-dd hh24:mi:ss') AS AUDITDATE " +
                     " FROM META_MAG_USER_TAB_REL A " +
                     " LEFT JOIN META_MAG_USER U1 ON A.USER_ID = U1.USER_ID " +
                     " LEFT JOIN META_TABLES T ON A.TABLE_ID=T.TABLE_ID AND A.TABLE_VERSION=T.TABLE_VERSION" +
                     " WHERE A.REL_TYPE >= 4 AND    A.TABLE_ID = " + tableId +
                     " ORDER  BY A.TABLE_VERSION DESC";
        //分页包装
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据表ID，表版本取得表的申请信息、审核信息
     * @param tableId
     * @param tableVersion
     * @return
     */
    public Map<String, Object> queryDetailByTableIdTableVersion(int tableId, int tableVersion){
        String sql = "SELECT A.USER_ID, TABLE_NAME, REL_TYPE, TO_CHAR(STATE_DATE,'YYYY-MM-DD HH24:MI:SS') STATE_DATE, TABLE_ID, " +
                     " TABLE_STATE, TABLE_VERSION, STATE_MARK, B.USER_NAMECN FROM META_MAG_USER_TAB_REL A " +
                     " LEFT JOIN META_MAG_USER B ON A.USER_ID = B.USER_ID " +
                     " WHERE TABLE_ID="+tableId+" AND TABLE_VERSION="+tableVersion;
        List<Map<String, Object>> list = getDataAccess().queryForList(sql);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        for(int i=0; i < list.size(); i ++){
            int relType = -1;
            try{
                relType = Integer.parseInt(Convert.toString(list.get(i).get("REL_TYPE")));
            }catch(Exception e){}
            if(relType != -1){
                if(relType == TblConstant.USER_TAB_REL_TYPE_APPLY){//申请信息
                    rtnMap.put("applyUser", list.get(i).get("USER_NAMECN"));
                    rtnMap.put("applyDate", list.get(i).get("STATE_DATE"));
                }else if(relType == TblConstant.USER_TAB_REL_TYPE_MODIFY){	//修改信息
                    rtnMap.put("applyUser", list.get(i).get("USER_NAMECN"));
                    rtnMap.put("applyDate", list.get(i).get("STATE_DATE"));
                }
                else if(relType == TblConstant.USER_TAB_REL_TYPE_PASS){//审核信息
                    rtnMap.put("auditUser", list.get(i).get("USER_NAMECN"));
                    rtnMap.put("auditDate", list.get(i).get("STATE_DATE"));
                    rtnMap.put("auditMark", list.get(i).get("STATE_MARK"));
                    rtnMap.put("tableState", list.get(i).get("TABLE_STATE"));
                }
            }
        }
        return rtnMap;
    }
    /**
     * @Title: queryRelTypeByTableId
     * @Description: 根据表ID和版本号获取审核标识
     * 				 注：现在只考虑0状态（申请状态）
     * @param tableId
     * @param tableVersion
     * @return Map<String,Object>   
     * @throws
     */
    public List<Map<String, Object>> queryRelTypeByTableId(long tableId,int tableVersion){
        StringBuffer buffer = new StringBuffer();
        buffer.append("select REL_TYPE,REL_ID,LAST_REL_ID from meta_mag_user_tab_rel where table_id = ? and table_version=?");
        return getDataAccess().queryForList(buffer.toString(), tableId,tableVersion);
    }
    
    /**
     * 判断当前表是否在审核中
     * @param tableId 表类Id
     * @param tableVersion 表类版本
     * */
    public int isAudit(int tableId,int tableVersion){
    	String  sql = "SELECT COUNT(T.TABLE_ID) FROM  META_MAG_USER_TAB_REL T WHERE T.TABLE_ID=? AND T.TABLE_VERSION=?";
    	return  this.getDataAccess().queryForInt(sql, tableId,tableVersion);
    }
}
