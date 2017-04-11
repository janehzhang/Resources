
package tydic.meta.common.yhd.test;

import java.sql.ResultSet;
import java.util.Vector;

import tydic.meta.common.yhd.constant.ConstantStoreProc;
import tydic.meta.common.yhd.sql.RecordSet;
import tydic.meta.common.yhd.sql.SQLUtil;


public class TestManager {

	
	
	/*
	 * 功能说明：根据搜索条件获取列表信息
	 * 输入参数：
	 */
	public Vector getTestList(String v_startTime, String  v_endTime){
		boolean flag = false;
		Vector vt = new Vector();
		RecordSet set = RecordSet.getInstance();
		ResultSet rs = null;
		try{
			String storeName = ConstantStoreProc.RPT_FAULT__LIST;
			Vector param = new Vector();
			param.add(v_startTime);
			param.add(v_endTime);
	
			rs = set.executeQueryStore(storeName,param);
			while(rs.next()){
				/*Bank bank = new Bank();
				bank.setBankID(SQLUtil.safelyGetString(rs,"iBankId"));
				bank.setBankName(SQLUtil.safelyGetString(rs,"vchBankName"));
				bank.setBankType(SQLUtil.safelyGetString(rs,"iBankType"));
				bank.setBankTypeName(SQLUtil.safelyGetString(rs,"vchBankType"));
				bank.setBankKind(SQLUtil.safelyGetString(rs,"iBankKind"));
				bank.setBankKindName(SQLUtil.safelyGetString(rs,"vchBankKind"));
				bank.setCorporation(SQLUtil.safelyGetString(rs,"iCorporation"));
				bank.setCorporationName(SQLUtil.safelyGetString(rs,"vchCorporation"));
				bank.setBanker(SQLUtil.safelyGetString(rs,"vchBanker"));
				bank.setPhone(SQLUtil.safelyGetString(rs,"vchPhone"));
				bank.setBankAddress(SQLUtil.safelyGetString(rs,"vchAddress"));
				bank.setRemark(SQLUtil.safelyGetString(rs,"vchRemark"));
				vt.addElement(bank);*/
			}
		}catch(Exception se){
			se.printStackTrace();
		}finally{
			SQLUtil.safelyCloseResultSet(rs);
			set.close();
		}
		return vt;
	}
	
	
	
	
	/**
	 * 方法描述：
	 * @param: 
	 * @return: 
	 * @version: 1.0
	 * @author: yanhaidong
	 * @version: 2013-5-20 下午04:01:16
	 */
	public static void main(String[] args) {
		  TestManager dao=new TestManager();
		  dao.getTestList("20130420","20130422");
		
		/*try {
			System.out.println((DbUtil.getInstance().getConnection()).isClosed());
			
			  DbUtil.getInstance().closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
