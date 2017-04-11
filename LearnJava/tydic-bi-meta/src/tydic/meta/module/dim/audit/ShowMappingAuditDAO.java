package tydic.meta.module.dim.audit;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;

/**
 * 
 * @author 李国民
 * @description 审核编码映射DAO <br>
 * @date 2012-02-08
 */
public class ShowMappingAuditDAO extends MetaBaseDAO{

  	/**
  	 * 通过批次号和维度表id查询审核后的映射信息
  	 * @param batchId 批次号
  	 * @param dimTableId 维度表id
  	 * @return 审核后的映射信息
  	 */
      public List<Map<String, Object>> queryMapping(String batchId, int dimTableId, Page page){
    	  String sql = "SELECT  T.*" +
      			" FROM META_DIM_TAB_MOD_HIS T WHERE T.BATCH_ID=? AND T.DIM_TABLE_ID=? ORDER BY T.HIS_ID";
    	  DataAccess dataAccess = getDataAccess();
          //分页包装
          if(page!=null){
          	sql= SqlUtils.wrapPagingSql(sql, page);
          }
          List<Map<String,Object>> rs= dataAccess.queryForList(sql,batchId,dimTableId);
          //加入码表信息
          if(rs!=null&&rs.size()>0){
              for(Map<String,Object> map:rs){
                  map.put("FLAG", CodeManager.getName(TblConstant.DIM_APPLY_TYPE,MapUtils.getString(map,"MOD_FLAG")));
              }
          }
          return rs;
      }
      
}
