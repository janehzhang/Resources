package tydic.portal.audit;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
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
public class AuditCtrlr {
	
	private AuditDao auditDao;
	
	public AuditDao getAuditDao() {
		return auditDao;
	}

	public void setAuditDao(AuditDao auditDao) {
		this.auditDao = auditDao;
	}
	
	/**
	 * 取得审核范围
	 *
     * @param page
     * @return
	 */
	@RemoteMethod
//	@RequestMapping(value = "/listScope.do", method = RequestMethod.POST )
	public 
//	@ResponseBody
    List<Map<String, Object>> listScope(Page page) {
		return auditDao.listScopes(page);
	}
	
	/**
	 * 进入审核范围添加页面
	 * @return
	 */
    @RemoteMethod
//    @RequestMapping(value = "/showAdd.do", method = RequestMethod.POST)
    public String addInit(){
    	return "forward:../portal/audit/ScopeAdd.jsp";
    }
    
    /**
     * 取得审核信息List
     * @param page
     * @return
     */
    @RemoteMethod
//	@RequestMapping(value = "/listAudits.do", method = RequestMethod.POST )
	public 
//	@ResponseBody
//	List<Map<String, Object>> listAudits(AuditPO auditPO, Page page, WebRequest request, @ModelAttribute("user") UserPO user, ModelMap model) {
    List<Map<String, Object>> listAudits(Map<?,?> queryData,Page page) {
        String dataDate = Convert.toString(queryData.get("auditDate"));
    	String busFlag_ = Convert.toString(queryData.get("auditStyle"));
    	String moduleAddres = Convert.toString(queryData.get("auditApply"));
    	String effectState = Convert.toString(queryData.get("effectState")==null?"":queryData.get("effectState"));
    	String audittype = Convert.toString(queryData.get("auditState"));
    	String auditconclude = Convert.toString(queryData.get("auditStatus")==null?"":queryData.get("auditStatus"));
    	if(dataDate == null || "".equals(dataDate)){
    		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
    		dataDate = sdf.format(new Date());
//    		java.util.Calendar cal = java.util.Calendar.getInstance();
//    		try {
//    			// 取得前一天的日期
//    			cal.setTime(sdf.parse(dataDate));
//    			cal.add(cal.DATE, -1);
//    			dataDate = sdf.format(cal.getTime());
//    		}catch (Exception e){
//    		}
    	}else{
            Date date = new Date();
            date.setTime(Long.parseLong(dataDate));
            dataDate =  new java.text.SimpleDateFormat("yyyyMMdd").format(date);
        }
        AuditPO auditPO = new AuditPO();
    	dataDate = dataDate.replace("-", "").replace("\\", "");
    	auditPO.setDataDate(dataDate);
    	auditPO.setBusFlag(busFlag_);
    	auditPO.setModuleAddres(moduleAddres);
    	auditPO.setEffectState(effectState);
    	auditPO.setAudittype(audittype);
    	auditPO.setAuditConclude(auditconclude);
//    	page.setRowCount(100);
//    	List<Map<String, Object>> rtn = auditDao.listAudits(auditPO, page);
    	List<Map<String, Object>> rtn = auditDao.listAuditsNew(auditPO);
		List<Map<String, Object>> busFlagList = auditDao.getTableType();
		List<Map<String, Object>> moduleAddresList = auditDao.getTableSet();
    	
    	if(rtn != null){
			
			for(int i = 0; i < rtn.size(); i++){
				Map<String, Object> m = rtn.get(i);
				String busFlag = Convert.toString(m.get("BUSFLAG"));
				String yy = "";//应用
				String moduleaddres = Convert.toString(m.get("MODULEADDRES"));
				yy = this.getValue(busFlag, busFlagList) + " -> " + this.getValue(moduleaddres, moduleAddresList);
				m.put("yy", yy);//设置应用
				String auditType = Convert.toString(m.get("AUDITTYPE"));
				String pd = "";//频度
				if("11".equals(auditType)){
					pd = "按日";
				}else if("22".equals(auditType)){
					pd = "按月";
				}else{
					pd="其他频度";
				}
				m.put("pd", pd);//设置频度
				String daDate = Convert.toString(m.get("DATADATE"));
				if(daDate.equals("")){
					m.put("datadate", dataDate);
				}
			}
			int length = rtn.size();
			for(int i = 0; i < length; i++){
				if(Convert.toString(rtn.get(i).get("AUDITPROP")).trim().equals("22")){//22:审核模式为各分公司
					List<Map<String, Object>> companys = auditDao.getChildCompany();
					
					for(int j = 0; j < companys.size(); j ++){
						Map<String, Object> m = rtn.get(i);
						Map<String, Object> m1 = new HashMap<String, Object>();
						m1.put("sjssgs", companys.get(j).get("NAME"));
						m1.put("pd", m.get("pd"));
						m1.put("datadate", m.get("datadate"));
						m1.put("auditopinion", m.get("auditopinion"));
						m1.put("shwtdj", m.get("shwtdj"));
						m1.put("showopinion", m.get("showopinion"));
						m1.put("auditconclude", m.get("auditconclude"));
						m1.put("auditnote", m.get("auditnote"));
						m1.put("yy", m.get("yy"));
						String auid = Convert.toString(m.get("scopeid"))  + "," +  Convert.toString(m.get("datadate")  + "," +  Convert.toString(companys.get(j).get("ID")));
						m1.put("auid", auid);
						rtn.add(m1);
					}
				}else //11审核模式为省公司
//					if(Convert.ToString(l_m.get(i).get("auditprop")).equals("11"))
					{
					rtn.get(i).put("sjssgs", "省公司");
					String auid = Convert.toString(rtn.get(i).get("SCOPEID"))  + "," +  Convert.toString(rtn.get(i).get("DATADATE") + "," + Convert.toString(rtn.get(i).get("DATAAREA")));//0000:省公司DATA_AREA代码
					rtn.get(i).put("auid", auid);
				}
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
	 * 进入审核页面
	 * @return
	 */
	@RemoteMethod
//	@RequestMapping(value = "/auditInit.do", method = RequestMethod.GET)
	public String auditInit(
//            ModelMap model
    ) {
		
//		List<Map<String, Object>> busFlagList = auditDao.getTableType();  单独出来queryStyle（）
//		List<Map<String, Object>> moduleAddresList = auditDao.getTableSet();  单独出来见queryApply（）
//		model.addAttribute("busFlagList", busFlagList);
//		model.addAttribute("moduleAddresList", moduleAddresList);
		return "forward:../audit/AuditList.jsp";
	}

    /**
     * 获取审核类型
     * @return
     */
    public List<Map<String ,Object>> queryStyle(){
        return auditDao.getTableType();
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
    @RemoteMethod
//	@RequestMapping(value = "/commitAudit.do", method = RequestMethod.POST)
	public
//    @ResponseBody
//	boolean commitAudit(HttpServletRequest request, HttpSession session) {
    String commitAudit(Map<String, String> commitData) {
		String auid = commitData.get("auid");
//		String shwtdj = request.getParameter("shwtdj");
		
//		String auditopinion = request.getParameter("auditopinion");//审核意见
//		String showopinion = request.getParameter("showopinion");//显示说明
//		String auditconclude = request.getParameter("auditconclude");//是否通过,代码:0否、1是
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
//		DataAccess access = new DataAccess();
        String b = "";
		try{
//			access.OpenConnWithContent("ORACLE", sourceName);
			b = auditDao.updateAuditInfo(auid, auditopinion, showopinion, auditconclude,Integer.parseInt(currUser.get("userId").toString()),
                    Integer.parseInt(currUser.get("deptId").toString()),currUser.get("userNamecn").toString(), isUpdate);
		} catch (Exception e){
			e.printStackTrace();
		}
//        finally {
//			access.Dispose();
//		}
//		String finish = request.getParameter("finish");
//		if("yes".equals(finish)){
//			return true;
//		}
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

