package tydic.meta.module.report.issue.ColType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;

public class ColTypeDAO extends MetaBaseDAO{
	
	
	public int addColType(String colTypeName, String parentId, int isShowIndex) {
        String sql = "INSERT INTO META_RPT_MODEL_COL_BUS_TYPE " + " (COL_TYPE_ID, COL_TYPE_NAME, PARENT_ID, COL_TYPE_ORDER, SHOW_INDEXPAGE_FLAG)"+
        " VALUES " + " (?, ?, ?, ? , ?) ";
        
        List<Object> proParams 	= new ArrayList<Object>();
        long pk = (long) queryForNextVal("SEQ_MAG_MENU_ID");
        String ORDER = "";
        proParams.add(pk);
        
        proParams.add(colTypeName);
        
        //如果查出来的数据时NULL的时候，默认为0
        Integer ORDER_ID = getDataAccess().queryForIntByNvl("SELECT MAX(COL_TYPE_ORDER) FROM META_RPT_MODEL_COL_BUS_TYPE WHERE PARENT_ID="+Integer.parseInt(parentId),0)+1;
        ORDER = ORDER_ID.toString();
        
        proParams.add(parentId);
        proParams.add(ORDER);
        proParams.add(isShowIndex);
        
        getDataAccess().execUpdate(sql, proParams.toArray());
        
        return (int)pk;
	}
	
	/**'
	 * 根据父级ID判断是否存在同名的字段分类
	 * @param colTypeName
	 * @param parentId
	 * @return	true or false 
	 */
	public boolean hasExist(String colTypeName, String parentId) {
        
		String sql="SELECT COUNT(*) FROM META_RPT_MODEL_COL_BUS_TYPE WHERE COL_TYPE_NAME='"+colTypeName+"' AND PARENT_ID='"+parentId+"'";
		int result =  getDataAccess().queryForInt(sql);
		if(result ==0)	return false;
		
        return true;
	}
	
    public Map<String,Object> queryColTypeById(int colTypeId){
        String sql="SELECT * FROM META_RPT_MODEL_COL_BUS_TYPE WHERE COL_TYPE_ID=? ORDER BY COL_TYPE_ORDER ASC ";
        return getDataAccess().queryForMap(sql, colTypeId);
    }

	public List<Map<String, Object>> getAllColType(Page page) {
        String sql="SELECT A.COL_TYPE_ID,A.COL_TYPE_NAME,A.PARENT_ID,A.COL_TYPE_ORDER,A.SHOW_INDEXPAGE_FLAG, DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN  FROM META_RPT_MODEL_COL_BUS_TYPE A ";
        
        sql +=  "LEFT JOIN (SELECT PARENT_ID,COUNT(1) "+
        		"CNT FROM META_RPT_MODEL_COL_BUS_TYPE GROUP BY PARENT_ID)"+
		        "C ON A.COL_TYPE_ID=C.PARENT_ID WHERE  "+
		        " A.PARENT_ID=0 ORDER BY A.COL_TYPE_ORDER ASC ";
		return getDataAccess().queryForList(sql);
	}
	// 查看该节点下是否有子节点
	public List<Map<String, Object>> getSubColType(Integer parentId) {
        String sql="SELECT A.COL_TYPE_ID,A.COL_TYPE_NAME,A.PARENT_ID,A.COL_TYPE_ORDER,A.SHOW_INDEXPAGE_FLAG, DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN  FROM META_RPT_MODEL_COL_BUS_TYPE A ";
        
        sql +=  "LEFT JOIN (SELECT PARENT_ID,COUNT(1) "+
        		"CNT FROM META_RPT_MODEL_COL_BUS_TYPE GROUP BY PARENT_ID)"+
		        "C ON A.COL_TYPE_ID=C.PARENT_ID WHERE  "+
		        " A.PARENT_ID='"+parentId+"' ORDER BY A.COL_TYPE_ORDER ASC ";
        
        return  getDataAccess().queryForList(sql);
	}
	// 查看该分类是否被发布模型引用
	public List<Map<String, Object>> isCite(Integer colTypeId) {
        String sql="select b.* from  META_RPT_MODEL_ISSUE_CONFIG a ,META_RPT_MODEL_ISSUE_COLS b where a.issue_id = b.issue_id and b.col_type_id in ("+colTypeId+")";
        return  getDataAccess().queryForList(sql);
	}

	public Object updateColType(String colTypeId,String colTypeName, int isShowIndex) {
		String sql="UPDATE META_RPT_MODEL_COL_BUS_TYPE SET COL_TYPE_NAME=?,SHOW_INDEXPAGE_FLAG=? WHERE COL_TYPE_ID=? ";
		 
	 	if(colTypeId.equals(null)) {
	 		throw  new IllegalStateException("主键ID传入失败，更新出错");
	 	}
		 
	 	List<Object> params=new ArrayList<Object>();
		params.add(colTypeName);
		params.add(isShowIndex);
		params.add(colTypeId);
		
		return  getDataAccess().execUpdate(sql,params.toArray());
	}

	public int deleteColType(int colTypeId) {
		return getDataAccess().execUpdate("DELETE FROM META_RPT_MODEL_COL_BUS_TYPE WHERE COL_TYPE_ID="+colTypeId);
	}

    public int[] updateBatchLevel(List<Map<String,Object>> levelDatas)throws Exception{
        String sql = "UPDATE META_RPT_MODEL_COL_BUS_TYPE SET PARENT_ID=? , COL_TYPE_ORDER=? " +" WHERE COL_TYPE_ID=? ";
        Object[][] proParamses=new Object[levelDatas.size()][3];
        int i=0;
        for(Map<String,Object> levelData:levelDatas){
        	proParamses[i][0] = (Integer.parseInt(levelData.get("parentId").toString()));
        	proParamses[i][1] = (Integer.parseInt(levelData.get("colTypeOrder").toString()));
        	proParamses[i][2] = (Integer.parseInt(levelData.get("colTypeId").toString()));
        	i++;
        }
        return getDataAccess().execUpdateBatch(sql, proParamses);
    }


    public int[] updateBatchOrder(Map<String,Long> orderDatas) throws Exception{
        String sql = "UPDATE META_RPT_MODEL_COL_BUS_TYPE SET  COL_TYPE_ORDER=? " +" WHERE COL_TYPE_ID=? ";
        Object[][] proParamses = new Object[orderDatas.size()][2];
        int i=0;
        for(Map.Entry<String,Long> orderData:orderDatas.entrySet()){
        	proParamses[i] = new Object[]{
        			orderData.getValue(),orderData.getKey()};
        	i++;
        }
        return getDataAccess().execUpdateBatch(sql,proParamses);
    }
    
    public List<Map<String, Object>> queryColType() {
        String sql = "SELECT T.COL_TYPE_ID,T.COL_TYPE_NAME,T.PARENT_ID,T.COL_TYPE_ORDER,T.SHOW_INDEXPAGE_FLAG " +
        		" FROM META_RPT_MODEL_COL_BUS_TYPE T ORDER BY T.COL_TYPE_ORDER ASC";
        return getDataAccess().queryForList(sql);
    }
	
}
