package tydic.meta.module.mag.reason;

import java.util.List;
import java.util.Map;
/**
满意度测评不满意原因10000号
 */
public class ReasonAction {
    /**
     * 数据库操作类
     */
    private ReasonDAO reasonDAO;
    
	
	public ReasonDAO getReasonDAO() {
		return reasonDAO;
	}
	public void setReasonDAO(ReasonDAO reasonDAO) {
		this.reasonDAO = reasonDAO;
	}
	public List<Map<String,Object>> queryReasonByPathCode(String beginId,String endId){
        return reasonDAO.queryReasonByPathCode(beginId, endId);
    }
	public List<Map<String,Object>> queryIVRZYJReasonByPathCode(String beginId,String endId){
        return reasonDAO.queryIVRZYJReasonByPathCode(beginId, endId);
    }
	public List<Map<String,Object>> queryIVRXZReasonByPathCode(String beginId,String endId){
        return reasonDAO.queryIVRXZReasonByPathCode(beginId, endId);
    }
    public List<Map<String,Object>> querySubReasonCode(String parentCode){
        return reasonDAO.querySubReasonCode(parentCode);
    }
    public List<Map<String,Object>> querySubIVRZYJReasonCode(String parentCode){
        return reasonDAO.querySubIVRZYJReasonCode(parentCode);
    }
    public List<Map<String,Object>> querySubIVRXZReasonCode(String parentCode){
        return reasonDAO.querySubIVRXZReasonCode(parentCode);
    }
}
