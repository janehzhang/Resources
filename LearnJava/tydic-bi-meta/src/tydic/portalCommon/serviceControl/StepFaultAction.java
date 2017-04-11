package tydic.portalCommon.serviceControl;


/**
 * 
 * @author quanxia
 * 
 */
public class StepFaultAction {

	private StepFaultDAO stepFaultDAO;

	public StepFaultDAO getStepFaultDAO() {
		return stepFaultDAO;
	}

	public void setStepFaultDAO(StepFaultDAO stepFaultDAO) {
		this.stepFaultDAO = stepFaultDAO;
	}

	//日
	public StepFaultBean getStepFaultDay(String dateTime,String indexType) {
		return stepFaultDAO.getStepFaultDay(dateTime.replaceAll("-", ""),indexType);
	}
	
   //月
	public StepFaultBean getStepFaultlMon(String dateTime,String indexType) {
		return stepFaultDAO.getStepFaultMon(dateTime,indexType);
	}

}
