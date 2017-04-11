package tydic.meta.module.tbl.dim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 熊久亮
 * @date 2011-12-21
 * @description 维度表字段对比
 *
 */
public class DimTableContrast{
	/**
	 * 修改表结构
	 * @param difference 差异类型 1、增加表字段结构。2、修改表的基本结构
	 * @param dimColsData	修改前列信息
	 * @param newDimColsData 修改后列信息
	 */
	public List<Map<String, Object>> modifyTableStructure(List<Map<String, Object>> dimColsData,
			List<Map<String, Object>> newDimColsData) {
		Map<String, Map<String, Object>> oldDimTableCols = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> column : dimColsData) 
		{
			oldDimTableCols.put(Convert.toString(column.get("COL_NAME")).toUpperCase(), column);
		}
		Map<String, Map<String, Object>> newDimTableCols = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> column : newDimColsData)
		{
			newDimTableCols.put(Convert.toString(column.get("COL_NAME")).toUpperCase(), column);
		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Map.Entry<String, Map<String, Object>> entry : newDimTableCols.entrySet()) {
			String columnName = entry.getKey();
			Map<String, Object> configColumn = entry.getValue();
			// 用新数据的KEY到以前数据中取值，取不到说明是新增字段。
			if (oldDimTableCols.containsKey(columnName) == false) {
				resultList.add(configColumn);
			}
		}
		
		return resultList;
	}
	
	/**
	 * 差异字段对比，找出新增字段
	 * @param dimColsData 修改前列信息
	 * @param newDimColsData 修改后列信息
	 * @return 返回找出差异字段集合
	 */
	public static List<Map<String, Object>> dimTableDifference(Map<String, Map<String, Object>> oldDimTableCols,
			Map<String, Map<String, Object>> newDimTableCols){
		//保存差异结果
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Map.Entry<String, Map<String, Object>> entry : newDimTableCols.entrySet()) {
			String columnName = entry.getKey();
			Map<String, Object> configColumn = entry.getValue();
			// 用新数据的KEY到以前数据中取值，取不到说明是新增字段。
			if (oldDimTableCols.containsKey(columnName) == false) {
				resultList.add(configColumn);
			}
		}
		
		return resultList;
	}
}
