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
 * @author 李国民
 * @description 指标与指标分类关系DAO
 * @date 2012-06-12
 * -
 * @modify
 * @modifyDate -
 */
public class GdlGroupRelDAO extends MetaBaseDAO{

	/**
	 * 批量添加指标与指标分类的关系数据
	 * @param datas
	 * @return
	 * @author 李国民
	 */
	public int[] insertGdlDimGroupMethod(final List<Map<String,Object>> datas) {
		String sql = "INSERT INTO META_GDL_GROUP_REL(GDL_ID, GDL_GROUP_ID) VALUES(?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> data=datas.get(i);
                preparedStatement.setObject(1,data.get("GDL_ID"));   			//指标id
                preparedStatement.setObject(2,data.get("GDL_GROUP_ID")); 		//指标分类id
            }
            public int batchSize(){
                return datas.size();
            }
        });
	}
	
	/**
	 * 根据指标id删除指标与指标分类的关系
	 * @param gdlId 指标id
	 * @author 李国民
	 */
	public void deleteGdlDimGroupMethod(int gdlId){
		String sql = "DELETE FROM META_GDL_GROUP_REL T WHERE T.GDL_ID=?";
		getDataAccess().execUpdate(sql,gdlId);
	}
}
