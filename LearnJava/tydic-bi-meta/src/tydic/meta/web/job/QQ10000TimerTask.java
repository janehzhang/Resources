
  
package tydic.meta.web.job;

import java.util.TimerTask;

import tydic.portalCommon.DateUtil;
import tydic.portalCommon.implData.ImplDataAction;


public class QQ10000TimerTask extends TimerTask{
	
	
	public QQ10000TimerTask(){
	
	}
	
	public void run() {
		String currDay = DateUtil.getFormatLastDay("yyyy-MM-dd");
		ImplDataAction service = new ImplDataAction();
		try {
			service.importCurrData(currDay);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("定时开始了............." + currDay);
	}
}
