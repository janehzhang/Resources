package tydic.portalCommon.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import tydic.frame.SystemVariable;

public class AccCheckedItemBean {
	
	private String ReqCode;
	private String ActionCode;
	private String SysCode;
	private String SysPwd;
	private String ServiceCode;
	private String CityId = "";
	private String ReqTime;
	private String Version;
	private String STAFF_ID = "";
	private String SUBSTAFF_ID;
	private String STAFF_PASSWD;
	private String IP_ADDR;
	private String NET_IP;
	private String QRY_TYPE;
	private String ACT_CODE;
	private String AD_SYN;
	
	private List<AccCheckedItemBean> AccCheckedItem=new ArrayList<AccCheckedItemBean>();

	public String getReqCode() {
		return ReqCode;
	}

	public void setReqCode(String reqCode) {
		ReqCode = reqCode;
	}

	public String getActionCode() {
		return ActionCode;
	}

	public void setActionCode(String actionCode) {
		ActionCode = actionCode;
	}

	public String getSysCode() {
		return SysCode;
	}

	public void setSysCode(String sysCode) {
		SysCode = sysCode;
	}

	public String getSysPwd() {
		return SysPwd;
	}

	public void setSysPwd(String sysPwd) {
		SysPwd = sysPwd;
	}

	public String getServiceCode() {
		return ServiceCode;
	}

	public void setServiceCode(String serviceCode) {
		ServiceCode = serviceCode;
	}

	public String getCityId() {
		return CityId;
	}

	public void setCityId(String cityId) {
		CityId = cityId;
	}

	public String getReqTime() {
		return ReqTime;
	}

	public void setReqTime(String reqTime) {
		ReqTime = reqTime;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}
	
	public String getSUBSTAFF_ID() {
		return SUBSTAFF_ID;
	}

	public void setSUBSTAFF_ID(String sUBSTAFFID) {
		SUBSTAFF_ID = sUBSTAFFID;
	}


	public String getSTAFF_ID() {
		return STAFF_ID;
	}

	public void setSTAFF_ID(String sTAFFID) {
		STAFF_ID = sTAFFID;
	}

	public String getSTAFF_PASSWD() {
		return STAFF_PASSWD;
	}

	public void setSTAFF_PASSWD(String sTAFFPASSWD) {
		STAFF_PASSWD = sTAFFPASSWD;
	}

	public String getIP_ADDR() {
		return IP_ADDR;
	}

	public void setIP_ADDR(String iPADDR) {
		IP_ADDR = iPADDR;
	}

	public String getNET_IP() {
		return NET_IP;
	}

	public void setNET_IP(String nETIP) {
		NET_IP = nETIP;
	}

	public String getQRY_TYPE() {
		return QRY_TYPE;
	}

	public void setQRY_TYPE(String qRYTYPE) {
		QRY_TYPE = qRYTYPE;
	}

	public String getACT_CODE() {
		return ACT_CODE;
	}

	public void setACT_CODE(String aCTCODE) {
		ACT_CODE = aCTCODE;
	}

	public String getAD_SYN() {
		return AD_SYN;
	}

	public void setAD_SYN(String aDSYN) {
		AD_SYN = aDSYN;
	}

	public List<AccCheckedItemBean> getAccCheckedItem() {
		return AccCheckedItem;
	}

	public void setAccCheckedItem(List<AccCheckedItemBean> accCheckedItem) {
		AccCheckedItem = accCheckedItem;
	}
	
	
}
