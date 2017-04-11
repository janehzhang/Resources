package tydic.meta.module.dim.merge;

import tydic.meta.common.Common;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.TblConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description  维度编码归并DAO <br>
 * @date 2011-11-09
 */
public class DimMergeDAO extends MetaBaseDAO{
    /**
     *根据归并类型归并状态为所有的数据
     * @param dimType 归并类型
     * @param dimTableName 维度表名称
     * @param dynamicCols   动态字段集
     * @param dimTablePrefix 维度表前缀
     * @param keyWord 关键字。
     * @param lastlevelFlag 末级是否显示字段 0：不显示，1：显示。
     * @return
     */
    public List<Map<String,Object>> queryAllDimData(int dimType,String dimTableName
            ,List<String> dynamicCols,String dimTablePrefix,String tableOwner,String keyWord,int  lastlevelFlag,Map<String, Object> dbSource){
        StringBuffer queryCols=new StringBuffer(dimTablePrefix+"_ID,"+dimTablePrefix+"_PAR_ID, ");
        queryCols.append(dimTablePrefix+"_NAME,"+dimTablePrefix+"_CODE, ");
        queryCols.append(dimTablePrefix+"_DESC,STATE,DIM_LEVEL,ORDER_ID,DIM_TYPE_ID,MOD_FLAG ");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(","+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),","));
        }
        StringBuffer sql=new StringBuffer();
        sql.append("SELECT A.*,NVL(B.CNT,0) DIMTYPE_CNT,DECODE(NVL(C.CHILDCOUNT,0),0,0,1) AS CHILDREN FROM ( ");
        sql.append("SELECT "+queryCols.toString()+" FROM "+tableOwner+"."+dimTableName+" WHERE DIM_TYPE_ID="+dimType+" OR DIM_TYPE_ID=0 OR DIM_TYPE_ID IS NULL) A ");
        //外关联自身，查询归并数量。
        sql.append("LEFT JOIN (SELECT "+dimTablePrefix+"_CODE, DIM_TYPE_ID,COUNT(*) CNT FROM "+tableOwner+"."+dimTableName+" GROUP BY "+dimTablePrefix+"_CODE, DIM_TYPE_ID) B ");
        sql.append("ON A."+dimTablePrefix+"_CODE=B."+dimTablePrefix+"_CODE AND A.DIM_TYPE_ID=B.DIM_TYPE_ID AND B.DIM_TYPE_ID!=0 AND B.DIM_TYPE_ID IS NOT NULL ");
        sql.append("LEFT JOIN (SELECT "+dimTablePrefix+"_PAR_ID ,COUNT(*) CHILDCOUNT FROM "+tableOwner+"."+dimTableName+" WHERE MOD_FLAG="+DimConstant.DIM_MODE_FALG_SHOW
                +" GROUP BY "+dimTablePrefix+"_PAR_ID  ) C " +
                   "ON A."+dimTablePrefix+"_ID =C."+dimTablePrefix+"_PAR_ID  WHERE 1=1 ");
        if(lastlevelFlag== DimConstant.DIM_LAST_LEVEL_FLAG_UNDISPLAY){
            sql.append("AND MOD_FLAG="+DimConstant.DIM_MODE_FALG_SHOW);
        }
        if(keyWord!=null&&!keyWord.equals("")){//如果关键字不为NULL,添加嵌套SQL，过滤查询后数据
            sql.append("AND A."+dimTablePrefix+"_CODE LIKE "+SqlUtils.allLikeParam(keyWord)+" OR A."+dimTablePrefix+"_NAME LIKE "+SqlUtils.allLikeParam(keyWord)+" OR A. "
                       +dimTablePrefix+"_DESC LIKE "+SqlUtils.allLikeParam(keyWord));
        }else{//只查询根节点
            sql.append(" AND A."+dimTablePrefix+"_PAR_ID="+ Constant.DEFAULT_ROOT_PARENT);
        }
        sql.append(" ORDER BY A.ORDER_ID ASC");
//        DataAccess dataAccess = getDataAccess(dbSource.get("DATA_SOURCE_USER").toString(), dbSource.get("DATA_SOURCE_PASS").toString(), dbSource.get("DATA_SOURCE_RULE").toString(), Constant.ORACLE_DRIVER_STRING);
        return getDataAccess().queryForList(sql.toString());
    }

    /**
     * 查找尚未归并为指定类型的所有CODE集合,所有的父ID均写为0，无层级关系
     * @param dimType
     * @param dimTable
     * @param dynamicCols
     * @param dimTablePrefix
     * @param keyWord
     * @param lastlevelFlag 末级是否显示字段 0：不显示，1：显示。
     * @return
     */
    public List<Map<String,Object>> queryNotMergeDimData(int dimType,String dimTable,
            List<String> dynamicCols,String dimTablePrefix,String tableOwner,String keyWord,int  lastlevelFlag,Map<String,Object> dbSource){
        StringBuffer queryCols=new StringBuffer("A."+dimTablePrefix+"_ID,A."+dimTablePrefix+"_PAR_ID, ");
        queryCols.append("A."+dimTablePrefix+"_NAME,A."+dimTablePrefix+"_CODE, ");
        queryCols.append("A."+dimTablePrefix+"_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID,MOD_FLAG");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(",A."+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),",A."));
        }
        StringBuffer sql=new StringBuffer();
        sql.append("SELECT " +queryCols.toString()+",NVL(B.CNT,0) DIMTYPE_CNT,DECODE(NVL(D.CHILDCOUNT,0),0,0,1) AS CHILDREN FROM "+tableOwner+"."+dimTable+" A ");
        //外关联自身，查询归并数量。
        sql.append("LEFT JOIN (SELECT "+dimTablePrefix+"_CODE, DIM_TYPE_ID,COUNT(*) CNT FROM "+tableOwner+"."+dimTable+" GROUP BY "+dimTablePrefix+"_CODE, DIM_TYPE_ID) B ");
        sql.append("ON A."+dimTablePrefix+"_CODE=B."+dimTablePrefix+"_CODE AND A.DIM_TYPE_ID=B.DIM_TYPE_ID AND B.DIM_TYPE_ID!=0 AND B.DIM_TYPE_ID IS NOT NULL ");
        sql.append("LEFT JOIN (SELECT "+dimTablePrefix+"_PAR_ID ,COUNT(*) CHILDCOUNT FROM "+tableOwner+"."+dimTable+" " +
                "WHERE MOD_FLAG="+DimConstant.DIM_MODE_FALG_SHOW +" GROUP BY "+dimTablePrefix+"_PAR_ID  ) D " +
                   "ON A."+dimTablePrefix+"_ID =D."+dimTablePrefix+"_PAR_ID  ");
        sql.append(",(SELECT "+dimTablePrefix+"_CODE, "+dimTablePrefix+"_ID FROM "+tableOwner+"."
                   +dimTable+" T WHERE DIM_TYPE_ID!="+dimType+") C ");
        sql.append("WHERE A."+dimTablePrefix+"_ID=C."+dimTablePrefix+"_ID ");
        if(keyWord!=null&&!"".equals(keyWord)){
            sql.append("AND (A."+dimTablePrefix+"_CODE LIKE "+SqlUtils.allLikeParam(keyWord)+ " OR A."+dimTablePrefix+"_NAME LIKE "+SqlUtils.allLikeParam(keyWord)+ " OR A. "
                       +dimTablePrefix+"_DESC LIKE "+SqlUtils.allLikeParam(keyWord)+ ") ");
        }else{
            sql.append(" AND A."+dimTablePrefix+"_PAR_ID="+ Constant.DEFAULT_ROOT_PARENT);
        }
        if(lastlevelFlag== DimConstant.DIM_LAST_LEVEL_FLAG_UNDISPLAY){
            sql.append("AND MOD_FLAG="+DimConstant.DIM_MODE_FALG_SHOW);
        }
        sql.append(" ORDER BY A.ORDER_ID ASC");
        return getDataAccess().queryForList(sql.toString());
    }

    /**
     * 查询指定类型已归并的数据
     * @param dimType
     * @param dimTable
     * @param dynamicCols
     * @param dimTablePrefix
     * @param keyWord
     * @param lastlevelFlag 末级是否显示字段 0：不显示，1：显示。
     * @return
     */
    public List<Map<String,Object>> queryAlreadyMergeDimData(int dimType,String dimTable,
            List<String> dynamicCols,String dimTablePrefix,String tableUser,String keyWord,int  lastlevelFlag,Map<String, Object> dbSource){
        StringBuffer queryCols=new StringBuffer("A."+dimTablePrefix+"_ID,A."+dimTablePrefix+"_PAR_ID, ");
        queryCols.append("A."+dimTablePrefix+"_NAME,A."+dimTablePrefix+"_PAR_CODE,A."+dimTablePrefix+"_CODE, ");
        queryCols.append("A."+dimTablePrefix+"_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID,MOD_FLAG ");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(",A."+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),",A."));
        }
        String sql="SELECT "+queryCols.toString()+",NVL(B.CNT,0) DIMTYPE_CNT,DECODE(NVL(D.CHILDCOUNT,0),0,0,1) AS CHILDREN FROM "+tableUser+"."+dimTable+" A ";
        sql+="LEFT JOIN (SELECT "+dimTablePrefix+"_CODE,COUNT(*) CNT FROM "+tableUser+"."+dimTable+" GROUP BY "+dimTablePrefix+"_CODE) B ";
        sql+="ON A."+dimTablePrefix+"_CODE=B."+dimTablePrefix+"_CODE ";
        sql+="LEFT JOIN (SELECT "+dimTablePrefix+"_PAR_ID ,COUNT(*) CHILDCOUNT FROM "+tableUser+"."+dimTable+" WHERE MOD_FLAG="+DimConstant.DIM_MODE_FALG_SHOW
                +" GROUP BY "+dimTablePrefix+"_PAR_ID  ) D " +
             "ON A."+dimTablePrefix+"_ID =D."+dimTablePrefix+"_PAR_ID  ";

        sql +=" WHERE A.DIM_TYPE_ID="+dimType+" ";
        if(keyWord!=null&&!"".equals(keyWord)){
            sql+="AND (A."+dimTablePrefix+"_CODE LIKE "+SqlUtils.allLikeParam(keyWord)+ " OR "+dimTablePrefix+"_NAME LIKE "+SqlUtils.allLikeParam(keyWord)+ " OR "
                 +dimTablePrefix+"_DESC LIKE "+SqlUtils.allLikeParam(keyWord)+ ") ";
        }else{
            sql+=" AND A."+dimTablePrefix+"_PAR_ID="+ Constant.DEFAULT_ROOT_PARENT;
        }
        if(lastlevelFlag== DimConstant.DIM_LAST_LEVEL_FLAG_UNDISPLAY){
            sql+="AND MOD_FLAG="+DimConstant.DIM_MODE_FALG_SHOW;
        }
        sql+=" ORDER BY A.ORDER_ID ASC";
//        DataAccess dataAccess = getDataAccess(dbSource.get("DATA_SOURCE_USER").toString(), dbSource.get("DATA_SOURCE_PASS").toString(), dbSource.get("DATA_SOURCE_RULE").toString(), Constant.ORACLE_DRIVER_STRING);
        return getDataAccess().queryForList(sql.toString());
    }

    /**
     * 查询下级数据
     * @param tableName
     * @param dimTablePrefix
     * @param dynamicCols
     * @return
     */
    public List<Map<String,Object>>  querySub(String tableName,String dimTablePrefix,String tableOwner,List<String> dynamicCols,
            long parId){
        StringBuffer queryCols=new StringBuffer("A."+dimTablePrefix+"_ID,A."+dimTablePrefix+"_PAR_ID, ");
        queryCols.append("A."+dimTablePrefix+"_NAME,A."+dimTablePrefix+"_CODE, ");
        queryCols.append("A."+dimTablePrefix+"_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID ");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(",A."+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),",A."));
        }
        String sql="SELECT "+queryCols.toString()+",NVL(B.CNT,0) DIMTYPE_CNT,DECODE(NVL(D.CHILDCOUNT,0),0,0,1) AS CHILDREN FROM "+tableOwner+"."+tableName+" A ";
        sql+="LEFT JOIN (SELECT "+dimTablePrefix+"_CODE,COUNT(*) CNT FROM "+tableOwner+"."+tableName+" GROUP BY "+dimTablePrefix+"_CODE ) B ";
        sql+="ON A."+dimTablePrefix+"_CODE=B."+dimTablePrefix+"_CODE  ";
        sql+="LEFT JOIN (SELECT "+dimTablePrefix+"_PAR_ID ,COUNT(*) CHILDCOUNT FROM "+tableOwner+"."+tableName+" WHERE MOD_FLAG ="+DimConstant.DIM_MODE_FALG_SHOW
                +" GROUP BY "+dimTablePrefix+"_PAR_ID  ) D " +
             "ON A."+dimTablePrefix+"_ID =D."+dimTablePrefix+"_PAR_ID  ";
        sql+=" WHERE A." +dimTablePrefix+"_PAR_ID="+parId;
        sql+=" AND MOD_FLAG =1 ORDER BY A.ORDER_ID ASC";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据父ID查询其父ID下的所有子节点不包括父节点
     * @param tableName  表名称
     * @param dimTablePrefix  表前缀
     * @param dynamicCols  动态字段
     * @param parId  父节点
     * @return
     */
    public List<Map<String,Object>> queryAllSub(String tableName,String dimTablePrefix,String tableOwner,List<String> dynamicCols,long parId){
        StringBuffer queryCols=new StringBuffer("A."+dimTablePrefix+"_ID,A."+dimTablePrefix+"_PAR_ID, ");
        queryCols.append("A."+dimTablePrefix+"_NAME,A."+dimTablePrefix+"_CODE, ");
        queryCols.append("A."+dimTablePrefix+"_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID ");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(",A."+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),",A."));
        }
        String sql="SELECT "+queryCols.toString()+" FROM "+tableOwner+"."+tableName+" A CONNECT BY  "
                   +dimTablePrefix+"_PAR_ID=PRIOR A."+dimTablePrefix+"_ID START WITH A."+dimTablePrefix+"_PAR_ID=?";
        return getDataAccess().queryForList(sql,parId);
    }

    /**
     * 查询未通过审核的数据条数。
     * @param dimTablleId
     * @return
     */
    public int queryUnAuditedCount(int dimTablleId,int dimTypeId){
        Integer[] inObj=new Integer[]{DimConstant.DIM_LEVEL_CHANGE,
                DimConstant.DIM_CODE_ADD,DimConstant.DIM_CODE_ENABLE,
                DimConstant.DIM_CODE_STOP,DimConstant.DIM_TYPE_ADD,
                DimConstant.DIM_RECORD_CHANGE};
        if(dimTablleId!=0){
        	if(dimTypeId!=0){
        	  String sql="SELECT COUNT(*) FROM META_DIM_TAB_MOD_HIS WHERE DIM_TABLE_ID=?  AND DIM_TYPE_ID=? AND  SUBSTR(MOD_FLAG,0,1) IN "+ SqlUtils.inParamDeal(inObj)+" AND AUDIT_FLAG=?";
        	  return getDataAccess().queryForIntByNvl(sql,0,dimTablleId,dimTypeId,DimConstant.DIM_NOT_AUDIT);
        	}
        	if(dimTypeId==0){
              String sql="SELECT COUNT(*) FROM META_DIM_TAB_MOD_HIS WHERE DIM_TABLE_ID=? AND  SUBSTR(MOD_FLAG,0,1) IN "+ SqlUtils.inParamDeal(inObj)+" AND AUDIT_FLAG=?";
           	  return getDataAccess().queryForIntByNvl(sql,0,dimTablleId,DimConstant.DIM_NOT_AUDIT);
        	}
        }
		return -1;
    }

    /**
     * 对一批数据进行排序整理
     * @param orderData 键值表示要排序的ID，Value表示要排序的值
     */
    public void order(Map<String ,Long> orderData,String tableName,String dimTablePrefix,String tableOwner) throws Exception{
        String sql="UPDATE "+tableOwner+"."+tableName+" SET ORDER_ID=? WHERE "+dimTablePrefix+"_ID=?";
        Object[][] proParamses = new Object[orderData.size()][];
        int i=0;
        for(Map.Entry<String ,Long> entry:orderData.entrySet()){
            Object[] params = {entry.getValue(),entry.getKey()};
            proParamses[i++] = params;
        }
        getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).execUpdateBatch(sql, proParamses);
    }

    /**
     * 维表层级数据整理，或者错误纠正
     * @param tableName
     */
    public void dealLevel(String tableName,String dimTablePrefix,String tableOwner) throws Exception{
        String sql=" UPDATE "+tableOwner+"."+tableName+" T SET T.DIM_LEVEL=  NVL((SELECT L  FROM " +
                   "(SELECT "+dimTablePrefix+"_ID,LEVEL L FROM "+tableOwner+"."+tableName+" T CONNECT BY PRIOR T."+dimTablePrefix+"_ID= T."+dimTablePrefix+"_PAR_ID START WITH "+dimTablePrefix+"_PAR_ID=0) M " +
                   "WHERE T."+dimTablePrefix+"_ID=M."+dimTablePrefix+"_ID),0)";
        getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).execUpdate(sql);
    }

    /**
     * 对于某个节点编码，查询其所属的所有归并类型。
     * @param tableId
     * @param dimCode
     * @param tableName
     * @param tableDimPrefix
     * @return
     */
    public List<Map<String,Object>> queryDimTypesByCode(int tableId,String tableName,String tableDimPrefix,String tableOwner,String dimCode){
        String sql="SELECT DIM_TYPE_ID,DIM_TYPE_NAME,DIM_TYPE_DESC,DIM_TYPE_CODE," +
                   "DIM_TABLE_ID FROM META_DIM_TYPE WHERE DIM_TABLE_ID="+tableId+" AND DIM_TYPE_ID IN " +
                   "(SELECT DISTINCT DIM_TYPE_ID FROM "+tableOwner+"."+tableName+" WHERE "+tableDimPrefix+"_CODE=?) ";
        return getDataAccess().queryForList(sql,dimCode);
    }

    /**
     * 查找完整的维表完整的树形结构，其归并类型ID不能为NULL，状态为有效，归并类型同样为有效。
     * @return
     */
    public List<Map<String,Object>> queryCompleteTreeData(String tableName,String dimTablePrefix,String tableOwner,
            List<String> dynamicCols,int  lastlevelFlag,Map<String, Object> dbSource){
        StringBuffer queryCols=new StringBuffer("A."+dimTablePrefix+"_ID,A."+dimTablePrefix+"_PAR_ID, ");
        queryCols.append("A."+dimTablePrefix+"_NAME,A."+dimTablePrefix+"_CODE, A."+dimTablePrefix+"_PAR_CODE,");
        queryCols.append("A."+dimTablePrefix+"_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID ");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(",A."+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),",A."));
        }
        String sql="SELECT "+queryCols.toString()+" FROM "+tableOwner+"."+tableName+" A " ;
        //末级不显示过滤最末层级的数据
        if(lastlevelFlag== DimConstant.DIM_LAST_LEVEL_FLAG_UNDISPLAY){
            sql+="LEFT JOIN (SELECT "+dimTablePrefix+"_PAR_ID ,COUNT(*) CHILDCOUNT FROM "+tableOwner+"."+tableName+" GROUP BY "+dimTablePrefix+"_PAR_ID  ) D " +
                 "ON A."+dimTablePrefix+"_ID =D."+dimTablePrefix+"_PAR_ID  ";
        }
        sql+= "INNER JOIN "+MetaDataSourceDAO.getMetaOwner()+".META_DIM_TYPE B ON A.DIM_TYPE_ID=B.DIM_TYPE_ID " +
              "WHERE A.STATE="+ TblConstant.META_DIM_TYPE_STATE_VALID+" AND A.DIM_TYPE_ID IS NOT NULL AND B.DIM_TYPE_STATE="
              + TblConstant.META_DIM_TYPE_STATE_VALID ;
        if(lastlevelFlag== DimConstant.DIM_LAST_LEVEL_FLAG_UNDISPLAY){
            sql+="AND MOD_FLAG="+DimConstant.DIM_MODE_FALG_SHOW;
        }
        sql+=  " ORDER BY A.DIM_TYPE_ID" ;
//        DataAccess dataAccess = getDataAccess(dbSource.get("DATA_SOURCE_USER").toString(), dbSource.get("DATA_SOURCE_PASS").toString(), dbSource.get("DATA_SOURCE_RULE").toString(), Constant.ORACLE_DRIVER_STRING);
        return getDataAccess().queryForList(sql.toString());
    }
    /**
     * 根据编码查询所有归并类型的数据
     * @param code
     * @param tableName
     * @param dimTablePrefix
     * @return
     */
    public List<Map<String,Object>> queryDimDataByCode(String code,String tableName,String dimTablePrefix,String tableOwner,List<String> dynamicCols){
        StringBuffer queryCols=new StringBuffer("A."+dimTablePrefix+"_ID,A."+dimTablePrefix+"_PAR_ID, ");
        queryCols.append("A."+dimTablePrefix+"_NAME,A."+dimTablePrefix+"_PAR_CODE,A."+dimTablePrefix+"_CODE, ");
        queryCols.append("A."+dimTablePrefix+"_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID ");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(",A."+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),",A."));
        }
        String sql="SELECT "+queryCols.toString()+",NVL(B.CNT,0) DIMTYPE_CNT FROM "+tableOwner+"."+tableName+" A ";
        sql+="LEFT JOIN (SELECT "+dimTablePrefix+"_CODE, DIM_TYPE_ID,COUNT(*) CNT FROM "+tableOwner+"."+tableName+" GROUP BY "+dimTablePrefix+"_CODE, DIM_TYPE_ID) B ";
        sql+="ON A."+dimTablePrefix+"_CODE=B."+dimTablePrefix+"_CODE AND A.DIM_TYPE_ID=B.DIM_TYPE_ID ";
        sql+="WHERE A."+dimTablePrefix+"_CODE=?";
        return getDataAccess().queryForList(sql,code);

    }

    /**
     * 查询维表关联字段指定层级的数据。只查询CODE，NAME
     * @param dimTablePrefix
     * @param level
     * @param dimType
     * @return
     */
    public List<Map<String,Object>> queryRefDimData(String dimTablePrefix,String  tableOwner,
            int level,int dimType,String tableName){
        String sql="SELECT "+dimTablePrefix+"_CODE,"+dimTablePrefix+"_NAME FROM "+tableOwner+"."+tableName+" WHERE　DIM_TYPE_ID=? AND DIM_LEVEL=? ";
        return getDataAccess().queryForList(sql,new int[]{dimType,level});
    }

    /**
     * 根据编码查询该编码以及归并的所有归并类型，便于前台展示。
     * @param tableName
     * @param dimTablePrefix
     * @param code
     * @return
     */
    public List<Map<String,Object>>  queryCodeDimTypeInfo(String tableName,String dimTablePrefix,String tableOwner,String code){
        String sql="SELECT "+dimTablePrefix+"_ID ,"+dimTablePrefix+"_PAR_ID ,C."+dimTablePrefix+"_NAME   ITEM_PAR_NAME,"+dimTablePrefix+"_DESC ,"+dimTablePrefix+"_NAME ITEM_NAME, " +
                   "B.DIM_TYPE_NAME,B.DIM_TYPE_DESC,B.DIM_TYPE_CODE FROM "+tableOwner+"."+tableName+" A " +
                   "INNER JOIN META_DIM_TYPE B ON A.DIM_TYPE_ID=B.DIM_TYPE_ID " +
                   "LEFT JOIN "+tableOwner+"."+tableName+" C ON A."+dimTablePrefix+"_PAR_ID =C."+dimTablePrefix+"_ID  WHERE A."+dimTablePrefix+"_CODE =? ";
        return getDataAccess().queryForList(sql,code);
    }

    /**
     * 判断表在元数据管理库中是否存在。
     * @param tableName
     * @return
     * @throws Exception
     */
    public  boolean isExitsTable(String tableName,String tableUser){
        boolean flag = true;
        try{
            getDataAccess().queryForInt("SELECT 1 FROM ALL_TABLES  WHERE  TABLE_NAME = '"+tableName+"'"+" AND OWNER='"+tableUser+"'");

            //getDataAccess().queryForInt("SELECT 1 FROM ALL_TABLES  WHERE  TABLE_NAME = ? AND OWNER=?",tableName,tableUser);
        } catch(Exception e){
            flag=false;
        }
        return flag;
    }

    /**
     * 是否包含重复编码
     * @param code
     * @param id
     * @param tableName
     * @param dimTablePrefix
     * @param dynamicCols
     * @return
     */
    public List<Map<String,Object >> valiHasCode(String code,String id,String tableName,String dimTablePrefix,String tableOwner,List<String> dynamicCols){
        StringBuffer queryCols=new StringBuffer("A."+dimTablePrefix+"_ID,A."+dimTablePrefix+"_PAR_ID, ");
        queryCols.append("A."+dimTablePrefix+"_NAME,A."+dimTablePrefix+"_CODE, ");
        queryCols.append("A."+dimTablePrefix+"_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,A.DIM_TYPE_ID ");
        //动态字段
        if(dynamicCols!=null&&dynamicCols.size()>0){
            queryCols.append(",A."+ Common.join(dynamicCols.toArray(new String[dynamicCols.size()]),",A."));
        }
        String sql="SELECT "+queryCols.toString()+"FROM "+tableOwner+"."+tableName+" A ";
        sql+="WHERE A."+dimTablePrefix+"_CODE=? AND A."+dimTablePrefix+"_ID !=?";
        Object[] params = {code,id};
        return getDataAccess().queryForList(sql,params);
    }

    /**
     * 查询维度表的动态字段
     * @param tableName
     * @param tablePrefix
     * @param tableOwner
     * @return
     */
    public List<Map<String,Object>> quertColNameByTableName(String tableName,String tablePrefix,String tableOwner){
    	List<Map<String,Object>> colNameMap =new ArrayList<Map<String, Object>>();
    	Map<String,String> dimColNameMap = new HashMap<String,String>();
    	dimColNameMap.put(tablePrefix+"_ID", tablePrefix+"_ID");
    	dimColNameMap.put(tablePrefix+"_CODE", tablePrefix+"_CODE");
    	dimColNameMap.put(tablePrefix+"_NAME", tablePrefix+"_NAME");
    	dimColNameMap.put(tablePrefix+"_PAR_ID", tablePrefix+"_PAR_ID");
    	dimColNameMap.put(tablePrefix+"_DESC", tablePrefix+"_DESC");
    	dimColNameMap.put(tablePrefix+"_PAR_CODE", tablePrefix+"_PAR_CODE");
    	String  sql = "SELECT T.COLUMN_NAME FROM USER_COL_COMMENTS T WHERE T.TABLE_NAME=? AND T.COLUMN_NAME NOT IN ('DIM_TABLE_ID','DIM_TYPE_ID','STATE','DIM_LEVEL','MOD_FLAG','ORDER_ID')";
		List<Map<String,Object>> list = this.getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).queryForList(sql, tableName);
		if(list!=null&&list.size()!=0){
			for(int i = 0;i<list.size();i++){
				Map<String,Object> map = list.get(i);
				String colName = map.get("COLUMN_NAME").toString();
				if(!dimColNameMap.containsKey(colName)){
					colNameMap.add(map);
				}
			}
		}
		return colNameMap;
    }
    
    /***
     * 获取该维度表中已经有的code
     * */
    public Map<String,DimBean> queryCodeByDimTypeId(String tableName,String tablePrefix,String tableOwner,int typeId){
    	Map<String,DimBean> codeMap = new HashMap<String,DimBean>();
    	String sql="SELECT DISTINCT T."+tablePrefix+"_CODE ,T."+tablePrefix+"_ID,T.DIM_LEVEL FROM "+tableOwner+"."+tableName+" T WHERE T.DIM_TYPE_ID=?";
    	List<Map<String,Object>> codeList = this.getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).queryForList(sql,typeId);
    	if(codeList!=null&&codeList.size()!=0){
    		for(int i = 0;i<codeList.size();i++){
    			Map<String,Object> map = codeList.get(i);
    			String value = map.get(tablePrefix+"_CODE").toString();
    			DimBean dim = new DimBean();
    			dim.setItemId(Long.valueOf(map.get(tablePrefix+"_ID").toString()));
    			dim.setLevel(Integer.valueOf(map.get("DIM_LEVEL").toString()));
    			codeMap.put(value, dim);
    		}
    	}
		return codeMap;
    }
    
    /**
     * 获取归并类型的下某节点的完整路径
     * @author 王晶
     * @param tableName 表名
     * @param tableDimPrefix 维度前缀
     * @param dimCode 维度编码
     * @param typeId 归类类型Id
     * */
	public String queryPathNameByCode(String tableName, String tableDimPrefix,String tableOwner,String dimCode,int typeId){
		String sql = "SELECT reverse(substr(max(SYS_CONNECT_BY_PATH (to_char(reverse(T."+tableDimPrefix.toUpperCase()+"_NAME)), '>-')),3)) "+'"'+"ITEM_NAME"+'"'+" FROM "+ tableOwner+"."+tableName+" T WHERE T.DIM_TYPE_ID =? START WITH T."+tableDimPrefix.toUpperCase()+"_CODE= ? CONNECT BY PRIOR T."+tableDimPrefix.toUpperCase()+"_PAR_ID = T."+tableDimPrefix.toUpperCase()+"_ID";
		return this.getDataAccess().queryForString(sql, typeId,dimCode);
	}
}
