package tydic.portalCommon.serviceControl;   
  
public class StringUtil {
	public static Integer getLastMon(String currentMon){
		String tempStr=currentMon.substring(4, currentMon.length());
		///System.out.println("tempStr: "+tempStr);
		Integer retValue=0;
		if("01".equals(tempStr)){
			retValue=Integer.parseInt(currentMon)-89;
		}else{
			retValue=Integer.parseInt(currentMon)-1;
		}
		return retValue;
	}
	
	public static void main(String[] args) {
		System.out.println(getLastMon("201303"));
	}
}
