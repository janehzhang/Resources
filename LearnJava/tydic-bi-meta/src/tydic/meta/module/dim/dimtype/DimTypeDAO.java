package tydic.meta.module.dim.dimtype;

import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
/**
 * 维表归并类型管理DAO
 * 
 * */
public class DimTypeDAO extends BaseDAO  {

	/**
	 * 查询查询所有的维表
	 * @author 王晶
	 * */
	public List<Map<String,Object>> queryDimTable(){
		String sql = "SELECT D.DIM_TABLE_ID,D.TABLE_NAME,T.TABLE_STATE FROM META_DIM_TABLES D LEFT JOIN META_TABLES T ON D.DIM_TABLE_ID=T.TABLE_ID";
		       sql+=" WHERE T.TABLE_STATE=1";
		return getDataAccess().queryForList(sql);
	}
	
    /**
     * 查询表中具体的维表类型
     * @author 王晶
     * @param tableId 具体表的id,页面传入
     * */
	public List<Map<String,Object>> queryDimTypeByTableId(int tableId){
		String sql="SELECT DIM_TYPE_ID,DIM_TYPE_NAME,DIM_TYPE_DESC,DIM_TABLE_ID,DIM_TYPE_CODE,DIM_TYPE_STATE ";
			   sql+="FROM META_DIM_TYPE ";
			   sql+="WHERE DIM_TABLE_ID="+tableId;
			   sql+=" ORDER BY DIM_TYPE_ID";
	   return getDataAccess().queryForList(sql);
	}
	
	 /**
     * 查询表中具体的维表类型下的层级关系
     * @author 王晶
     * @param tableId 具体表的id,页面传入
     * */
	public List<Map<String,Object>> queryDimLevelByTypeId(int tableId,int typeId){
		String sql="SELECT DIM_TYPE_ID,DIM_TABLE_ID,DIM_LEVEL,DIM_LEVEL_NAME ";
               sql+="FROM META_DIM_LEVEL WHERE DIM_TYPE_ID="+typeId+" AND DIM_TABLE_ID="+tableId+" ORDER BY DIM_LEVEL";
	   return getDataAccess().queryForList(sql);
	}
	/**
	 * 更新归并类型的状态
	 * @param tableId 表类的id
	 * @param typeId 归并类型的id
	 * */
	public boolean updateDimTypeState(int tableId,int typeId,int flag){
		String sql = null;
		if(flag!=-1){
		 if(flag==0){
		 sql ="UPDATE META_DIM_TYPE SET DIM_TYPE_STATE =0 WHERE DIM_TYPE_ID="+typeId+" AND DIM_TABLE_ID="+tableId;
		 }
		 if(flag==1){
	     sql ="UPDATE META_DIM_TYPE SET DIM_TYPE_STATE =1 WHERE DIM_TYPE_ID="+typeId+" AND DIM_TABLE_ID="+tableId;
		 }
		 return getDataAccess().execNoQuerySql(sql);
		}
		return false;
	}
	/**
	 * 查询当前某维度表中归并类型的最大值,用于自动生成code
	 * @param tableId 维度表的id
	 * @param 最大的值
	 * */
	public String queryMaxDimCodeByTableId(int tableId){
		String sql = "SELECT MAX(DIM_TYPE_CODE) TYPE_CODE FROM META_DIM_TYPE WHERE DIM_TABLE_ID="+tableId;
		return getDataAccess().queryForString(sql);
	}
	/**
	 * 查询当前某维度表中归并类型下的层级的最大值
	 * @param tableId 维度表的id typeId 某类型的id
	 * @param 最大的值
	 * */
	public int queryMaxLevelCodeByCondition(int tableId,long typeId){
		String sql = "SELECT NVL(MAX(DIM_LEVEL),0) TYPE_CODE FROM META_DIM_LEVEL WHERE DIM_TABLE_ID="+tableId+" AND DIM_TYPE_ID="+typeId;
		return getDataAccess().queryForInt(sql);
	}
	/**
	 * 查找当前表最大的id并加1
	 * */
    public int  queryMaxTypeId(int tableId){
    	String sql = "SELECT NVL(MAX(DIM_TYPE_ID),0) TYPE_CODE FROM META_DIM_TYPE WHERE DIM_TABLE_ID="+tableId;
		return getDataAccess().queryForInt(sql)+1;
    }
	/**
	 * 添加归并类型
	 * @param list<map> 包含tableId,类型名称,类型描述,是否末级显示,类型编码是自动生成的,状态默认为启用
	 * */
	public boolean insertDimType(int tableId,int typeId,String typeName,String typeDesc){
		        boolean flag = true;
		        Object[] proParams = new Object[5];
					if(tableId!=0){
						StringBuffer sb = new StringBuffer();
						sb.append("INSERT INTO META_DIM_TYPE(DIM_TYPE_ID,DIM_TABLE_ID,DIM_TYPE_NAME,DIM_TYPE_DESC,DIM_TYPE_CODE,");
						sb.append("DIM_TYPE_STATE) VALUES(");
						sb.append("?,?,?,?,?,1)");
						proParams[0]=typeId;
						proParams[1]=tableId;
						proParams[2]=typeName;
						proParams[3]=typeDesc;
						String typeCode = this.queryMaxDimCodeByTableId(tableId);
					    if(typeCode==null){
					    	proParams[4]="1";
					    }else{
					    	int typeCodeint = Integer.valueOf(typeCode)+1;
					    	proParams[4]=String.valueOf(typeCodeint);
					    }
					    String sql = sb.toString();
					    flag = getDataAccess().execNoQuerySql(sql,proParams);
					}
		            return flag;
	        }
	
	/**
	 * 添加某个归并类型下的层次
	 * @param list<map> 包含表类型的id 归并类型的id 层级的名称  层级code是自动生成
	 * */
	public boolean insertDimTypeLevel(int tableId,long typeId,String levelName){
		boolean flag = false;
		         Object[] proParams = new Object[4];
				 if(tableId!=0&&typeId!=0){
					String sql = "INSERT INTO META_DIM_LEVEL(DIM_TABLE_ID,DIM_TYPE_ID,DIM_LEVEL,DIM_LEVEL_NAME) VALUES(?,?,?,?)";
					proParams[0]=tableId;
					proParams[1]=typeId;
					int levelCode = queryMaxLevelCodeByCondition(tableId,typeId);
					if(levelCode==0){
						proParams[2]=1;	
					}else{
						proParams[2]=levelCode+1;	
					}
					proParams[3]=levelName;
					flag = this.getDataAccess().execNoQuerySql(sql, proParams);
		       }
		return flag;
	}
	/**
	 * 更新归并类型
	 * @param List<map> 包含的类型只包含了可以更改的类型名称,类型描述,是否展示
	 * @throws Exception 
	 * */
	public int updateDimType(int tableId,int typeId,String typeName,String typeDesc) throws Exception{
		
				StringBuffer sb = new StringBuffer();
				sb.append("UPDATE META_DIM_TYPE SET ");
				if(tableId!=0&&typeId!=0){
					  if(typeName!=null){
						  sb.append("DIM_TYPE_NAME="+"'"+typeName+"',");
					  }
					  if(typeDesc!=null){
							sb.append(" DIM_TYPE_DESC="+"'"+typeDesc+"',");
						}else{
							sb.append(" DIM_TYPE_DESC="+"' "+"',");
						}
					}
					int len = sb.length();
					String sql = sb.substring(0, len-1);
					sql = sql +" WHERE DIM_TYPE_ID="+typeId+" AND DIM_TABLE_ID="+tableId;
					return this.getDataAccess().execUpdate(sql);
	}
	
	/**
	 * 删除归并类型下的层级
	 * @param List<map> 包含的类型只包含了可以更改的层次名称
	 * @throws Exception 
	 * */
	public int deleteDimLevel(int tableId,int typeId) throws Exception{
		    int deleteCount = -1;
				if(tableId!=0&&typeId!=0){
					String deleteSql ="DELETE FROM META_DIM_LEVEL T WHERE T.DIM_TABLE_ID="+tableId+" AND T.DIM_TYPE_ID="+typeId;
					deleteCount =  this.getDataAccess().execUpdate(deleteSql); //先删除所有的层级
			    }
			return deleteCount;
	}
					
}
