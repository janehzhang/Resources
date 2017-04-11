package tydic.meta.module.dim;

import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.Common;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.module.dim.merge.CodeBean;
import tydic.meta.web.session.SessionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description  维度修改数据备份表DAO <br>
 * @date 2011-11-15
 */
public class MetaDimTabModHisDAO extends MetaBaseDAO{
    /**
     * 批量插入数据。
     * @param datas
     * @param dymaicCloCount 动态字段数目。
     * @return
     * @throws Exception
     */
    public int[] insertBatch(final List<Map<String,Object>> datas,final int dymaicCloCount,final int dimTableId) throws Exception{
        String sql = "INSERT INTO META_DIM_TAB_MOD_HIS(ITEM_ID,ITEM_NAME,ITEM_PAR_ID,ITEM_CODE,DIM_TYPE_ID, " +
                     "DIM_LEVEL,MOD_FLAG,MOD_MARK,MOD_DATE,USER_ID, " +
                     "DIM_TABLE_ID,SRC_CODE,SRC_NAME,ITEM_DESC,ORDER_ID, " +
                     "STATE,HIS_ID,AUDIT_FLAG,SRC_SYS_ID,BATCH_ID,ITEM_PAR_CODE";
        if(dymaicCloCount>0){
            for(int i=1;i<=dymaicCloCount;i++){
                sql+=",COL"+i;
            }
        }
        sql+=") VALUES(?,?,?,?,?,?,?,?,sysdate,? ,?,?,?,?,?, ?,SEQ_DIM_TAB_MOD_HIS_ID.NEXTVAL,?,?,?,?";
        if(dymaicCloCount>0){
            for(int i=1;i<=dymaicCloCount;i++){
                sql+=",?";
            }
        }
        sql+=")";
        return getDataAccess().execUpdateBatch(sql,new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> dataRow=datas.get(i);
                preparedStatement.setObject(1,dataRow.get("ITEM_ID"));
                preparedStatement.setObject(2,dataRow.get("ITEM_NAME"));
                preparedStatement.setObject(3,dataRow.get("ITEM_PAR_ID"));
                preparedStatement.setObject(4,dataRow.get("ITEM_CODE"));
                preparedStatement.setObject(5,dataRow.get("DIM_TYPE_ID"));
                preparedStatement.setObject(6,dataRow.get("DIM_LEVEL"));
                preparedStatement.setObject(7,dataRow.get("MOD_FLAG")==null||dataRow.get("MOD_FLAG").equals("")? 0 :
                        dataRow.get("MOD_FLAG").toString());
                preparedStatement.setObject(8,dataRow.get("MOD_MARK"));
                preparedStatement.setObject(9,dataRow.containsKey("USER_ID")?dataRow.get("USER_ID"):SessionManager.getCurrentUserID());
                preparedStatement.setObject(10,dimTableId);
                preparedStatement.setObject(11,dataRow.get("SRC_CODE"));
                preparedStatement.setObject(12,dataRow.get("SRC_NAME"));
                preparedStatement.setObject(13,dataRow.get("ITEM_DESC"));
                preparedStatement.setObject(14,dataRow.get("ORDER_ID"));
                preparedStatement.setObject(15,dataRow.get("STATE"));
                preparedStatement.setObject(16,dataRow.get("AUDIT_FLAG")==null||dataRow.get("AUDIT_FLAG").equals("")?DimConstant.DIM_NOT_AUDIT:dataRow.get("AUDIT_FLAG"));
                preparedStatement.setObject(17,dataRow.get("SRC_SYS_ID"));
                preparedStatement.setObject(18,dataRow.get("BATCH_ID"));
                preparedStatement.setObject(19,dataRow.get("ITEM_PAR_CODE"));
                if(dymaicCloCount>0){
                    for(int j=1;j<=dymaicCloCount;j++){
                        preparedStatement.setObject(19+j,dataRow.get("COL" + j));
                    }
                }
            }
            public int batchSize(){
                return datas.size();
            }
        });
    }

    /**
     * 批量插入数据。
     * @param datas
     * @param dymaicCloCount 动态字段数目。
     * @return
     * @throws Exception
     */
    public int[] insertBatchByBean(final List<CodeBean> datas,final int dymaicCloCount,final int dimTableId) throws Exception{
        String sql = "INSERT INTO META_DIM_TAB_MOD_HIS(ITEM_ID,ITEM_NAME,ITEM_PAR_ID,ITEM_CODE,DIM_TYPE_ID, " +
                     "DIM_LEVEL,MOD_FLAG,MOD_MARK,MOD_DATE,USER_ID, " +
                     "DIM_TABLE_ID,SRC_CODE,SRC_NAME,ITEM_DESC,ORDER_ID, " +
                     "STATE,HIS_ID,AUDIT_FLAG,SRC_SYS_ID,BATCH_ID,ITEM_PAR_CODE";
        if(dymaicCloCount>0){
            for(int i=1;i<=dymaicCloCount;i++){
                sql+=",COL"+i;
            }
        }
        sql+=") VALUES(?,?,?,?,? ,?,?,?,sysdate,? ,?,?,?,?,?, ?,SEQ_DIM_TAB_MOD_HIS_ID.NEXTVAL,?,?,?,?";
        if(dymaicCloCount>0){
            for(int i=1;i<=dymaicCloCount;i++){
                sql+=",?";
            }
        }
        sql+=")";
        return getDataAccess().execUpdateBatch(sql,new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                CodeBean cb = datas.get(i);
                preparedStatement.setObject(1,cb.getItemId());
                preparedStatement.setObject(2,cb.getItemName());
                preparedStatement.setObject(3,cb.getParId());
                preparedStatement.setObject(4,cb.getItemCode());
                preparedStatement.setObject(5,cb.getDimTypeId());
                preparedStatement.setObject(6,cb.getDimLevel());
                preparedStatement.setObject(7,DimConstant.DIM_CODE_ADD);
                preparedStatement.setObject(8,null);
                preparedStatement.setObject(9,cb.getUserId());
                preparedStatement.setObject(10,dimTableId);
                preparedStatement.setObject(11,null);
                preparedStatement.setObject(12,null);
                preparedStatement.setObject(13,cb.getItemDesc());
                preparedStatement.setObject(14,0);
                preparedStatement.setObject(15, Constant.META_ENABLE);
                preparedStatement.setObject(16,DimConstant.DIM_NOT_AUDIT);
                preparedStatement.setObject(17,null);
                preparedStatement.setObject(18,cb.getBatchId());
                preparedStatement.setObject(19,cb.getItemParCode());
                Map<String,Object> dynMap =  cb.getDynColMap();
                if(dymaicCloCount>0){
                    int count = 1;
                    Iterator<String> it = dynMap.keySet().iterator();
                    while(it.hasNext()){
                    	String key = it.next();
                    	preparedStatement.setObject(19+count,dynMap.get(key));
                    	count++;
                    }
                }
            }
            public int batchSize(){
                return datas.size();
            }
        });
    }

    /**
     * 批量删除历史表数据
     * @param ids
     */
    public void delelteByBatch(List<Long> ids) throws Exception{
        String sql="DELETE FROM META_DIM_TAB_MOD_HIS WHERE HIS_ID IN　( ";
        sql+= Common.join(ids.toArray(),",")+")";
        getDataAccess().execUpdate(sql);
    }
    
    /**
     * 更新维度审核操作历史表
     * @param itemId 维度节点的id
     * @param parId  当前节点维度表父Id
     * @param userId 审核人id
     * @param auditFlag 审核标示
     * @param auditMask 审核意见
     * @param hisId 操作历史表Id
     * */
    public int updateDimModHis(long itemId,long parId,int userId,int auditFlag,String auditMask,int hisId){
    	String sql ="UPDATE META_DIM_TAB_MOD_HIS SET ITEM_ID=?,ITEM_PAR_ID=?,AUDIT_FLAG=?,AUDIT_USER_ID=?," +
    			    "MOD_MARK=? WHERE HIS_ID=?";
    	Object[] proParams = new Object[]{itemId,parId,auditFlag,userId,auditMask,hisId};
    	DataAccess dataAccess = getDataAccess();
    	return dataAccess.execUpdate(sql,proParams);
    	
    }
    
    /**
     * 更新维度审核操作历史表（修改时）
     * @author 李国民
     * @param itemId 维度节点的id
     * @param parId  当前节点维度表父Id
     * @param userId 审核人id
     * @param auditFlag 审核标示
     * @param auditMask 审核意见
     * @param hisId 操作历史表Id
     * @param showItemCode 审批后查看显示的维度编码
     * @param showItemName 审批后查看显示的维度名称
     * */
    public int updateDimModHis(long itemId,long parId,int userId,int auditFlag,String auditMask,int hisId,String showItemCode,String showItemName){
    	String sql ="UPDATE META_DIM_TAB_MOD_HIS SET ITEM_ID=?,ITEM_PAR_ID=?,AUDIT_FLAG=?,AUDIT_USER_ID=?," +
    			    "MOD_MARK=?,ITEM_CODE=?,ITEM_NAME=? WHERE HIS_ID=?";
    	Object[] proParams = new Object[]{itemId,parId,auditFlag,userId,auditMask,showItemCode,showItemName,hisId};
    	return this.getDataAccess().execUpdate(sql,proParams);
    	
    }
    /**
     * 更新维度表中某一个节点下的所有子节点的一个字段
     * @param updateColName包含需要更改的列名称
     * @param parId 父ID
     * @param dataValue 更新之后的数据值
     * @param dataType 更新的字段的类型 1为int 2 为long
     * */
    public int updateValueByParId(String updateColName,long parId,String dataValue){
    	String sql ="UPDATE META_DIM_TAB_MOD_HIS T SET T."+updateColName+"=? WHERE T.ITEM_PAR_ID=?";
    	Object[] proParams = new Object[2];
    	proParams[0]= dataValue;
    	proParams[1]=parId;
    	DataAccess dataAccess = getDataAccess();
		return dataAccess.execUpdate(sql, proParams);
    }
    /**
     * 更新维度表中某一个节点下的所有子节点的几个字段
     * @param updateColNameMap 包含需要更改的列名称
     * @param parId 父ID
     * @param dataValue 更新之后的数据值
     * */
    public int updateBatchValueByParId(Map<String,Object>updateColNameMap,long parId){
    	String sql = null;
    	if(updateColNameMap!=null&&updateColNameMap.size()!=0){
    		StringBuffer sb = new StringBuffer();
    		sb.append("UPDATE META_DIM_TAB_MOD_HIS T SET ");
    		Set<String>keySet = updateColNameMap.keySet();
    		Iterator<String>keyIterator  = keySet.iterator();
    		while(keyIterator.hasNext()){
    			String key  = keyIterator.next();
    			String value = Convert.toString(updateColNameMap.get(key));
    			sb.append("T."+key+"="+value+",");
    		}
    		sql = sb.toString();
    		int len = sql.length();
    		sql=sql.substring(0, len-1);
    		sql+=" WHERE T.ITEM_PAR_ID="+parId;
    		DataAccess dataAccess = getDataAccess();
    		return dataAccess.execUpdate(sql);
    	}
		return -1;
    }

    /**
     * 判断当前历史操作表(META_DIM_TAB_MOD_HIS)的动态字段数有多少个
     * */
	public int quertDynColCount(){
		String  sql ="SELECT COUNT(t.COLUMN_NAME) FROM USER_COL_COMMENTS T WHERE T.TABLE_NAME='META_DIM_TAB_MOD_HIS'AND T.COLUMN_NAME LIKE 'COL%'";
		return this.getDataAccess().queryForInt(sql);
	}
    
}
