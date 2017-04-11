package tydic.meta.module.dim.dimCode.search;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author tanwc
 * @description  维度值编码查询DAO <br>
 * @date 2012-05-22
 */
public class DimCodeSearchDAO extends MetaBaseDAO{
	
	/**
	 * 查询维度编码值
	 * @param tableName
	 * @param preName
	 * @param parId	
	 * @param dimTypeId
	 * @param keyword
	 * @param dataSourceId
	 * @return
	 */
	public List<Map<String,Object>> queryDimCode(String tableName,String tableOwner,String preName,long parId,long dimTypeId,String keyword,int dataSourceId) {
		String sql = 	"SELECT A.*, DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN "+
					  	"FROM "+tableOwner+"."+tableName+" A "+
					  	"LEFT JOIN (SELECT "+preName+"_PAR_ID, COUNT(1) CNT "+
					  	"            	FROM "+tableOwner+"."+tableName+" "+
					  	"          	GROUP BY "+preName+"_PAR_ID) C "+
					  	"ON A."+preName+"_ID = C."+preName+"_PAR_ID "+
					  	"WHERE A."+preName+"_PAR_ID = "+parId+"  ";
		if(dimTypeId != -1) {
			sql += "AND A.DIM_TYPE_ID="+dimTypeId+"";
		}
		if(!StringUtils.isEmpty(keyword)) {
			sql += "AND (A."+preName+"_CODE LIKE '%"+keyword+"%' ";
			sql += "OR A."+preName+"_NAME LIKE '%"+keyword+"%' ";
			sql += "OR A."+preName+"_DESC LIKE '%"+keyword+"%') ";
		}
		return getDataAccess(dataSourceId).queryForList(sql);
	}
	/**
	 * 根据关键字查询维度编码值
	 * @param tableName
	 * @param preName
	 * @param dimTypeId
	 * @param keyword
	 * @param dataSourceId
	 * @return
	 */
	public List<Map<String,Object>> queryDimCodeByKeyword(String tableName,String tableOwner,String preName,long dimTypeId,String keyword,int dataSourceId) {
		String sql = 	"SELECT A.*, DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN "+
		"FROM "+tableOwner+"."+tableName+" A "+
		"LEFT JOIN (SELECT "+preName+"_PAR_ID, COUNT(1) CNT "+
		"            	FROM "+tableOwner+"."+tableName+" "+
		"          	GROUP BY "+preName+"_PAR_ID) C "+
		"ON A."+preName+"_ID = C."+preName+"_PAR_ID "+
		"WHERE A.DIM_TYPE_ID="+dimTypeId+"  ";
		if(!StringUtils.isEmpty(keyword)) {
			sql += "AND (A."+preName+"_CODE LIKE '%"+keyword+"%' ";
			sql += "OR A."+preName+"_NAME LIKE '%"+keyword+"%' ";
			sql += "OR A."+preName+"_DESC LIKE '%"+keyword+"%') ";
		}
		return getDataAccess(dataSourceId).queryForList(sql);
	}
	
}
