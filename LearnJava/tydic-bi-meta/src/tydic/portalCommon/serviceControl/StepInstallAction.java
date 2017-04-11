package tydic.portalCommon.serviceControl;


/**
 * 
 * @author yanhd
 * 
 */
public class StepInstallAction {

	private StepInstallDAO stepInstallDAO;

	//日
	public StepInstallBean getStepInstallDay(String dateTime,String indexType) {
		//System.out.print("getStepInstallDay--->>>>>>>>>>>>>>>>>>>>>: "+dateTime.replaceAll("-", ""));
		return stepInstallDAO.getStepInstallDay(dateTime.replaceAll("-", ""),indexType);
	}

	
	
   //月
	public StepInstallBean getStepInstallMon(String dateTime,String indexType) {
		return stepInstallDAO.getStepInstallMon(dateTime,indexType);
	}

  /****************************************************************************************/
	public void setStepInstallDAO(StepInstallDAO stepInstallDAO) {
		this.stepInstallDAO = stepInstallDAO;
	}

}
