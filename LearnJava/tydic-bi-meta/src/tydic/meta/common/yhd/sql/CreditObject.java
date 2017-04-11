/*
 * Created on 2005-10-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tydic.meta.common.yhd.sql;
import java.util.*;
/**
 * @author aps-tlm
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreditObject {
	private Vector vectName = new Vector();
	private Vector vectValue = new Vector();
	/**
	 * @return Returns the columnName.
	 */
	public String getColumnValue(String columnName) {
		int offset = vectName.indexOf(columnName);
		if (offset >= 0){
			//return (String)vectValue.elementAt(offset);
			String value = (String)vectValue.elementAt(offset);
			if (value == null) {
				return "";
			} else {
				return value.trim();
			}
		}else{
			if (columnName.substring(0,6).equals("NVALUE") || columnName.substring(0,6).equals("IVALUE")){
				return "0";
			}else{
				return "";
			}
		}
	}
	/**
	 * @param columnName The columnName to set.
	 */
	public void setColumnName(String columnName) {
		vectName.add(columnName);
	}
	/**
	 * @return Returns the columnValue.
	 */
	public String getColumnValue(int offset) {
		if (offset >= 0 && offset < vectValue.size()){
			//return (String)vectValue.elementAt(offset);
			String value = (String)vectValue.elementAt(offset);
			if (value == null) {
				return "";
			} else {
				return value.trim();
			}
		}else{
			return "";
		}
	}
	/**
	 * @param columnValue The columnValue to set.
	 */
	public void setColumnValue(String columnValue) {
		vectValue.add(columnValue);
	}
	/**
	 * 获得对象所对应记录的字段数
	 */
	public int size(){
		return vectValue.size();
	}
}
