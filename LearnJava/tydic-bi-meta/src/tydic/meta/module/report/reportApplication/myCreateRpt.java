package tydic.meta.module.report.reportApplication;

import java.util.List;
import java.util.Map;

import tydic.meta.web.session.SessionManager;

public class myCreateRpt {
      
	MyCreateRptDAO myCreateRptDAO;
	
	public List<Map<String,Object>> queryUserCreateRpt(){
		int userId = SessionManager.getCurrentUserID();
		return myCreateRptDAO.queryUserCreateRpt(userId);
	}
}
