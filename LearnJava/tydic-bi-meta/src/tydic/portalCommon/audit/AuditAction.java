package tydic.portalCommon.audit;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

//@Controller
//@RequestMapping("module/portal/audit/*")
//@SessionAttributes({"user","menu","auth","btns"})
@RemoteProxy
public class AuditAction {
	
	private AuditDAO auditDao;
	
	public AuditDAO getAuditDao() {
		return auditDao;
	}

	public void setAuditDao(AuditDAO auditDao) {
		this.auditDao = auditDao;
	}
	
	/**
	 * 取得审核范围
	 *
     * @param page
     * @return
	 */
	public List<Map<String, Object>> listScope(Page page) {
		return auditDao.listScopes(page);
	}
    /**
     * 取得审核信息List
     * @param page
     * @return
     */
	public List<Map<String, Object>> listAudits(Map<?,?> queryData,Page page) {
        String dataDate = Convert.toString(queryData.get("auditDate"));
    	String moduleAddres = Convert.toString(queryData.get("auditApply"));
    	String auditconclude = Convert.toString(queryData.get("auditStatus")==null?"":queryData.get("auditStatus"));
    	if(dataDate == null || "".equals(dataDate)){
    		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
    		dataDate = sdf.format(new Date());
    	}else{
            Date date = new Date();
            date.setTime(Long.parseLong(dataDate));
            dataDate =  new java.text.SimpleDateFormat("yyyyMMdd").format(date);
        }
        AuditPO auditPO = new AuditPO();
    	dataDate = dataDate.replace("-", "").replace("\\", "");
    	auditPO.setDataDate(dataDate);
    	auditPO.setAuditConclude(auditconclude);
    	List<Map<String, Object>> rtn = auditDao.listAuditsNew(auditPO);
		List<Map<String, Object>> moduleAddresList = auditDao.getTableSet();
    	if(rtn != null){
			for(int i = 0; i < rtn.size(); i++){
                //获取一条审核信息
				Map<String, Object> map = rtn.get(i);
				String scopeId = Convert.toString(map.get("SCOPE_ID"));
				String yy = this.getValue(scopeId, moduleAddresList);
				map.put("yy", yy);//设置应用
				String auditType = Convert.toString(map.get("AUDIT_TYPE"));
				String pd = "";//频度
				if("1".equals(auditType)){
					pd = "按日";
				}else if("2".equals(auditType)){
					pd = "按月";
				}else{
					pd="其他频度";
				}
				map.put("pd", pd);//设置频度
				String daDate = Convert.toString(map.get("DATA_DATE"));
				if(daDate.equals("")){
					map.put("datadate", dataDate);
				}
			}
			int length = rtn.size();
			for(int i = 0; i < length; i++){
                rtn.get(i).put("sjssgs", "省公司");
                String auid = Convert.toString(rtn.get(i).get("SCOPE_ID"))  + "," +  Convert.toString(rtn.get(i).get("DATA_DATE") + "," + Convert.toString(rtn.get(i).get("DATA_AREA")));//0000:省公司DATA_AREA代码
                rtn.get(i).put("auid", auid);
			}
			if(rtn!=null){
				List<Map<String, Object>> old = rtn;
				List<Map<String, Object>> newl = new ArrayList<Map<String, Object>>();
				for(int k = 0; k < old.size(); k ++){
					if(Convert.toString(old.get(k).get("auid")).trim().equals("")){
						//去除空的列表元素
					}else{
						newl.add(old.get(k));
					}
				}
				rtn = newl;
			}
			for(int i = 0; i < rtn.size(); i ++){
				rtn.get(i).put("rown", i);
				rtn.get(i).put("rn", i);
			}
		}
    	
    	return rtn;
    }
    /**
     * 获取审核应用类型
     * @return
     */
    public List<Map<String,Object>> queryApply(){
        return auditDao.getTableSet();
    }
    /**
     * 进行审核，修改或新增             注意：这里暂时只提供修改的方法
     * @param commitData
     * @return
     */
	public String commitAudit(Map<String, String> commitData) {
		String auid = commitData.get("auid");
		Map<String,Object>  currUser= SessionManager.getCurrentUser();
        String auditopinion = commitData.get("showopinion");  //审核意见
        String showopinion = commitData.get("auditnote");  //显示说明
        String auditconclude = commitData.get("auditconclude"); //是否通过,代码:0否、1是
		try {
			auid = URLDecoder.decode(auid, "UTF-8");
			auditopinion = URLDecoder.decode(auditopinion, "UTF-8");
			showopinion = URLDecoder.decode(showopinion, "UTF-8");
			auditconclude = URLDecoder.decode(auditconclude, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
		}
		boolean isUpdate = true;//由false改为true，每次操作都更新最大有效时间
		if("0".equals(auditconclude)){//不展现(更新最大有效时间)
			isUpdate = true;
			auditconclude = "0";
		}
		if("1".equals(auditconclude)){//展现(更新最大有效时间)
			isUpdate = true;
			auditconclude = "1";
		}
        String b = "";
		try{
            BaseDAO.beginTransaction();
			b = auditDao.updateAuditInfo(auid, auditopinion, showopinion, auditconclude,Integer.parseInt(currUser.get("userId").toString()),currUser.get("userNamecn").toString(), isUpdate);
            BaseDAO.commit();
		} catch (Exception e){
            Log.error("审核失败",e);
            BaseDAO.rollback();
		}
		return b;
	}
    /**
     * 根据ID取得List<Map<String, Object>>中对应的Text值
     * @param id
     * @param lm
     * @return
     */
    private String getValue(String id, List<Map<String, Object>> lm){
    	String rtn = "";
    	for(int i = 0; i < lm.size(); i ++){
    		Map<String, Object> m = lm.get(i);
    		String s = Convert.toString(m.get("ID"));
    		if(s.equals(id)){
    			rtn = Convert.toString(m.get("TEXT"));
    			break;
    		}
    	}
    	if(rtn.equals("")){
    		System.out.println("没有对应ID:"+id+"的数据!");
    	}
    	return rtn;
    }
}

