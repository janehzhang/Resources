package tydic.meta.module.dim.merge;

import java.util.Map;
public class CodeBean {
   private Long itemId;
   private String  itemCode;
   private String  itemName;
   private String  itemDesc;
   private Long parId;
   private int  dimTableId;
   private String batchId;
   private int  dimTypeId;
   private int  dimLevel;
   private String  itemParCode;
   private String errorInfo;
   private int userId;
   public String getItemParCode() {
		return itemParCode;
	}
	public void setItemParCode(String itemParCode) {
		this.itemParCode = itemParCode;
	}
   private Map<String,Object> dynColMap;
   public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public int getDimTableId() {
		return dimTableId;
	}
	public void setDimTableId(int dimTableId) {
		this.dimTableId = dimTableId;
	}
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Long getParId() {
		return parId;
	}
	public void setParId(Long parId) {
		this.parId = parId;
	}
	public int getDimTypeId() {
		return dimTypeId;
	}
	public void setDimTypeId(int dimTypeId) {
		this.dimTypeId = dimTypeId;
	}
	public int getDimLevel() {
		return dimLevel;
	}
	public void setDimLevel(int dimLevel) {
		this.dimLevel = dimLevel;
	}
	public Map<String, Object> getDynColMap() {
		return dynColMap;
	}
	public void setDynColMap(Map<String, Object> dynColMap) {
		this.dynColMap = dynColMap;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
   public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
