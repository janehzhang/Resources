package tydic.meta.common;

import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.mapper.AbstractSingleRowMapper;
import tydic.meta.sys.code.CodeManager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @description 作用:回调类，在回调的时候查询系统缓存起来的码表信息，返回编码对应的名称。
 * @date 2012-02-28
 *
 */
public class CodeMapper extends AbstractSingleRowMapper<Map>{
	private static Class<Map> clazz =Map.class ;
    private CodeBean cols[];

    public CodeMapper(CodeBean cols[]){
    	super(clazz);
        this.cols = cols;
    }

	/* (non-Javadoc)
	 * @see tydic.frame.jdbc.mapper.AbstractSingleRowMapper#convertRow(java.sql.ResultSet)
	 */
	public Map<String, Object> convertRow(ResultSet resultset) {
		Map<String, Object> map = new HashMap<String, Object>();
        try {
            ResultSetMetaData resultSetMetaData = resultset.getMetaData();
            int colsCount = resultSetMetaData.getColumnCount(); // 取得结果集列数
            List<String> colsNames = new ArrayList<String>(); // 保存结果集列名
            for (int i = 1; i <= colsCount; i++) {
                colsNames.add(resultSetMetaData.getColumnName(i).toUpperCase());
			}
            Object[] colsName = colsNames.toArray();
            for (int i = 1; i <= colsCount; i++) {
                map.put(Convert.toString(colsName[i - 1]), resultset.getObject(i));
                for(CodeBean bean : cols){
                    if(Convert.toString(colsName[i - 1]).equalsIgnoreCase(bean.getColItem())){//查出匹配项
                        map.put(bean.getShowItem().toUpperCase(), CodeManager.getName(bean.getType(), Convert.toString(map.get(bean.getColItem()))));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return map;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
