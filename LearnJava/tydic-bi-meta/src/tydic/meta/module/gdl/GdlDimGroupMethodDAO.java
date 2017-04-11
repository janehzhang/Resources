package tydic.meta.module.gdl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.MetaBaseDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 维度汇总方法
 * @date 12-6-6
 * -
 * @modify
 * @modifyDate -
 */
public class GdlDimGroupMethodDAO extends MetaBaseDAO{
	
	/**
	 * 批量新增指标和维度的关系汇总
	 * @param datas
	 * @return
	 * @author 李国民
	 */
	public int[] insertGdlDimGroupMethod(final List<Map<String,Object>> datas) {
		String sql = "INSERT INTO META_GDL_DIM_GROUP_METHOD(GDL_VERSION, GDL_ID," +
				" DIM_TABLE_ID, GROUP_METHOD) VALUES(?,?,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> data=datas.get(i);
                preparedStatement.setObject(1,data.get("GDL_VERSION")); 		//指标版本
                preparedStatement.setObject(2,data.get("GDL_ID"));   			//指标id
                preparedStatement.setObject(3,data.get("DIM_TABLE_ID")); 		//维度表id
                preparedStatement.setObject(4,data.get("GROUP_METHOD")); 		//指标汇总方法
            }
            public int batchSize(){
                return datas.size();
            }
        });
	}
}
