package tydic.meta.module.tbl.examine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.meta.common.Common;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.MetaTableInstDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.module.tbl.TblCommon;
import tydic.meta.module.tbl.apply.TableApplyDAO;

/**
 * Copyrights @ 2011，Tianyuan DIC Information Co., Ltd. All rights reserved.
 * 表类的查询与审核
 * @author 王晶
 * @date 2011-11-01
 */
public class TableExamineAction {

	private TableExamineDAO tableExamineDAO;
	private MetaTablesDAO metaTableDao;
    private MetaTableColsDAO metaTableColsDAO;
    private MetaTableInstDAO metaTableInstDAO;	
    private MetaDataSourceDAO metaDataSourceDAO;
    private TableApplyDAO tableApplyDAO;
    
	/**
	 * 按页面输入的值查找对应的表
	 * @param queryData 查询的map 包括了业务类型的名称 层次分类的名称 状态 关键字
	 * @param page 分页的对象
	 * @author 王晶
	 * */
	public List<Map<String,Object>> queryTablesByCondition(Map<String,Object> queryData,Page page){
		if(page==null){
			page= new Page(0,20);
		}
		List<Map<String,Object>> resultList = tableExamineDAO.queryTablesByCondition(queryData, page);
		return resultList;
	}
	public boolean insertTableState(Map<String,Object> map){
		int tableId = 0 ;
		int tableVersion = -1;
		boolean flag = false;
		Object tableIdObj  = map.get("tableId");
		if(tableIdObj!=null){
			tableId = Integer.valueOf(tableIdObj.toString());
		}
		Object tableVersionObj  = map.get("tableVersion");
		if(tableVersionObj!=null){
			tableVersion = Integer.valueOf(tableVersionObj.toString());
		}
		try {
			BaseDAO.beginTransaction();
			int count =  tableExamineDAO.insertTableState(map);
			if(count==1){
	    		Object flagObj = map.get("resuleflag");
	    		int resuleflag = 0;
	    		if(flagObj!=null){
	    			resuleflag = Integer.valueOf(flagObj.toString());
	    		}
	    		if(resuleflag==1){		//如果标识为1标识审核通过。
				    metaTableDao.updateValidTable(tableId);
					int inValidCount = metaTableDao.updateTableVersionVaild(tableId, tableVersion);
                    // 以前版本列信息设为无效，当前版本列信息设为有效
                    metaTableColsDAO.invalidTableCols(tableId);
                    metaTableColsDAO.updateMetaTableColsByTableIdAndVersion(tableId, tableVersion, 1);
					if(inValidCount==1){
						flag =  true;
					}
	    		}else{
	    			flag = true;
	    		}
			}
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 动态生成建表sql的私有方法
	 * @param tableId 表的id
	 * @param tableVersion 表类的版本
	 * @param columnDatas List 表类的列信息
	 * @return String 建表的sql
	 * */
    private String  generateSql(int tableId,int tableVersion,List<Map<String,Object>> columnDatas,String tableName){
    	String createSql = null;
    	StringBuffer sb = new StringBuffer();
    	Map<String, Object> tableData = metaTableDao.queryTablesByIdAndVersion(tableId,tableVersion);
    	if(tableName!=null&&!tableName.equals("")){			//如果是测试sql时，把表明替换为测试表名
    		tableData.put("TABLE_NAME", tableName);
    	}
    	if(columnDatas!=null&&columnDatas.size()!=0){
    		sb.append("CREATE TABLE ");
            //加入所属用户信息 例如： meta.testtable
            String tableOwner = Convert.toString(tableData.get("TABLE_OWNER"));
            if(!tableOwner.equals("")){
                sb.append(tableOwner + ".");
            }
    		sb.append(tableData.get("TABLE_NAME").toString());
    		sb.append(" ( <br/>");
    		int count=0;
            String columnComment="";//列注释。
            List<String> primaryKeys=new ArrayList<String>();//生成主键字段。
            if(columnDatas!=null&&columnDatas.size()>0){
                for(Map<String,Object> column:columnDatas){
                    boolean  isNumber=TblCommon.isNumber(String.valueOf(column.get("colDatatype")));
                    sb.append(column.get("colName")+" ");
                    //添加数据类型
                    sb.append(TblCommon.buildDataType(String.valueOf(column.get("colDatatype")),
                            Integer.parseInt(column.get("colSize").toString()),Common.parseInt(column.get("colPrec"))==null?0:Common.parseInt(column.get("colPrec")))+" ");
                    //添加默认值。
                    if(column.get("defaultVal")!=null&&
                       !column.get("defaultVal").toString().equals("")){
                        sb.append("DEFAULT "+(isNumber||column.get("defaultVal").toString().equalsIgnoreCase("SYSDATE")?"":"'")
                                         +column.get("defaultVal")+(isNumber||column.get("defaultVal").toString().equalsIgnoreCase("SYSDATE")?" ":"' "));
                    }
                    //添加是否为空处理。
                    if(Integer.parseInt(column.get("colNullabled").toString())==0){
                        sb.append("NOT NULL ");
                    }
                    //是否为主键
                    if(Integer.parseInt(column.get("isPrimary").toString())==1){
                        primaryKeys.add(column.get("colName").toString());
//                        createSql.append("PRIMARY KEY ");
                    }
                    //加入分隔逗号
                    if(count++!=columnDatas.size()-1){
                        sb.append(",");
                    }
                    sb.append("<br/>");
                    //列注释
                    if(column.get("colBusComment")!=null&&
                       !column.get("colBusComment").toString().equals("")){
                        columnComment+="COMMENT ON COLUMN ";
                        //加入所属用户信息 例如： meta.testtable
                        if(!tableOwner.equals("")){
                            columnComment+= tableOwner + ".";
                        }
                        columnComment+= tableData.get("TABLE_NAME")+"."
                                       +column.get("colName")+" IS '"+ column.get("colBusComment")
                                       +"';<br/>";
                    }
                }
            }
            sb.append(") ");
            //分区SQL
            if(tableData.get("PARTITION_SQL")!=null
               &&!tableData.get("PARTITION_SQL").toString().equals("")){
                sb.append("<br/>"+tableData.get("PARTITION_SQL")+"<br/>");
            }
            //建表SQL完成。
            sb.append(";<br/>");
            //添加表注释
            if(tableData.get("TABLE_BUS_COMMENT")!=null
               &&!tableData.get("TABLE_BUS_COMMENT").toString().equals("")){
                sb.append("COMMENT ON TABLE ");
                //加入所属用户信息 例如： meta.testtable
                if(!tableOwner.equals("")){
                    sb.append(tableOwner + ".");
                }
                sb.append(tableData.get("TABLE_NAME")+" IS '"
                                 +tableData.get("TABLE_BUS_COMMENT")+"';<br/>");
            }
            //行级注释
            sb.append(columnComment);
            //主键SQL
            if(primaryKeys.size()>0){
                sb.append("ALTER TABLE ");
                //加入所属用户信息 例如： meta.testtable
                if(!tableOwner.equals("")){
                    sb.append(tableOwner + ".");
                }
                sb.append(tableData.get("TABLE_NAME")+"  ADD PRIMARY KEY ("
                                 +Common.join(primaryKeys.toArray(new String[primaryKeys.size()]),",")+");<br/>");
            }
            //返回最终生成的SQL
            createSql =  TblCommon.sqlMarcoReplace(sb.toString()).toUpperCase();
    	}
		return createSql;
    	
    }
	/**
	 * 查询当前表类的列的信息并生成建表sql,用于审核之后的弹出建表sql的窗口显示建表sql并在页面上提供执行当前建表sql的按钮
	 * @param tableId 表的id
	 * @param tableVersion 表类的版本
	 * */
	public String getCreateTableSql(int tableId,int tableVersion){
    	List<Map<String,Object>> columnDatas = null;
		String createSql = null;
		if(tableId!=0&&tableVersion!=0){
			List<Map<String, Object>> cols = metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion, null);
			columnDatas = new ArrayList<Map<String, Object>>();
		      for(int i = 0; i < cols.size(); i++){
		            Map<String, Object> columnData = new HashMap<String, Object>();
		            columnData.put("colBusComment", cols.get(i).get("COL_BUS_COMMENT"));
		            columnData.put("colDatatype", cols.get(i).get("DATA_TYPE"));
		            columnData.put("colName", cols.get(i).get("COL_NAME"));
		            columnData.put("colNullabled", cols.get(i).get("COL_NULLABLED"));
		            columnData.put("colPrec", cols.get(i).get("COL_PREC"));
		            columnData.put("colSize", cols.get(i).get("COL_SIZE"));
		            columnData.put("defaultVal", cols.get(i).get("DEFAULT_VAL"));
		            columnData.put("dimColId", cols.get(i).get("DIM_COL_ID"));
		            columnData.put("isPrimary", cols.get(i).get("IS_PRIMARY"));
		            columnData.put("tableVersion", cols.get(i).get("TABLE_VERSION"));
		            columnDatas.add(columnData);
		    }
		      createSql =  generateSql(tableId,tableVersion,columnDatas,null);
		}
		return createSql;
      
	}
	/**
	 * 判断当前在执行环境中和实例表中是否存在当前表的数据
	 * @throws Exception 
	 * */
	public boolean validataTable(Map<String,Object>map,String tableName,String tableOwener){
		boolean flag = false;
		String dataSourceId = null;
		
		Object urlObj = map.get("DATA_SOURCE_ID");
		if(urlObj!=null){
			dataSourceId  = urlObj.toString();
		}
		try {
			flag = TblCommon.isExitsTable(dataSourceId,tableName,tableOwener);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;		
	}
	/**
	 * 判断当前表类是否存在实例表
	 * */
	 public boolean queryTableInstanceByTableInfo(int tableId,String tableName){
		 return metaTableInstDAO.queryTableInstanceByTableInfo(tableId, tableName);
	 }
	 /**
	  * 新增实例表
	  * */
	 public int insertInsTableByInfo(Map<String,Object>map){
		int  count =-1;
		try {
			BaseDAO.beginTransaction();
			count =  metaTableInstDAO.insertInsTableByInfo(map);
			BaseDAO.commit();
		} catch (Exception e) {
			 BaseDAO.rollback();
	         Log.error(null,e);
			e.printStackTrace();
		}
		return count;
	 }

	 /**
	  * 表类审核时，辅助检查情况
	  * @param tableId
	  * @param tableVersion
	  * @return 返回验证的信息，比如验证项，检查值，说明，检查结果
	  */
	public List<Map<String,Object>> validate(int tableId,int tableVersion){
        List<Map<String, Object>> cols = metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion, null);
    	List<Map<String,Object>> columnDatas = new ArrayList<Map<String, Object>>();
        int keyCount=0;
	    for(int i = 0; i < cols.size(); i++){
            Map<String, Object> columnData = new HashMap<String, Object>();
            columnData.put("colBusComment", cols.get(i).get("COL_BUS_COMMENT"));
            columnData.put("colDatatype", cols.get(i).get("DATA_TYPE"));
            columnData.put("colName", cols.get(i).get("COL_NAME"));
            columnData.put("colNullabled", cols.get(i).get("COL_NULLABLED"));
            columnData.put("colPrec", cols.get(i).get("COL_PREC"));
            columnData.put("colSize", cols.get(i).get("COL_SIZE"));
            columnData.put("defaultVal", cols.get(i).get("DEFAULT_VAL"));
            columnData.put("dimColId", cols.get(i).get("DIM_COL_ID"));
            columnData.put("isPrimary", cols.get(i).get("IS_PRIMARY"));
            columnData.put("tableVersion", cols.get(i).get("TABLE_VERSION"));
            columnDatas.add(columnData);
	    	if(Integer.parseInt(cols.get(i).get("IS_PRIMARY").toString())==1){			//判断是否为主键
	    		keyCount++;
	    	}
	    }
        List<Map<String,Object>> validateResult=new ArrayList<Map<String,Object>>();	//辅助检查返回结果
        Map<String,Object> validateTemp=new HashMap<String, Object>();	//辅助检查第一项
        validateTemp.put("checkItem","主键验证");
        validateTemp.put("checkValue",keyCount);
        validateTemp.put("desc","主键个数应大于一个且小于三个。");
        validateTemp.put("res",keyCount>0&&keyCount<=3);
        validateResult.add(validateTemp);

        validateTemp=new HashMap<String, Object>();	 //辅助检查第二项
        validateTemp.put("checkItem","建表SQL验证");
        validateTemp.put("checkValue","验证成功！");
        validateTemp.put("desc","实时验证生成的建表SQL语句");
        validateTemp.put("res",true);
        try{
        	Map<String, Object> tableData = metaTableDao.queryTablesByIdAndVersion(tableId,tableVersion);
            //查询数据源
//            Map<String,Object> dataSource=metaDataSourceDAO.queryDataSourceById(
//                    Integer.parseInt(tableData.get("DATA_SOURCE_ID").toString()));
            String tableName="TEMP_"+System.currentTimeMillis();	 //测试tableName
            String tableOwner=Convert.toString(tableData.get("TABLE_OWNER"));
            String sql = generateSql(tableId,tableVersion,columnDatas,tableName);
            List<String> sqls=new ArrayList<String>();

            sql = sql.replaceAll("<BR/>", "");
            sql = sql.substring(0, sql.length()-1);
            String sqlData[] = sql.split(";");
            for(int i=0;i<sqlData.length;i++){
                sqls.add(sqlData[i]);
            }
        	tableApplyDAO.testCreatetSql(sqls,tableData.get("DATA_SOURCE_ID").toString(),tableName,tableOwner);

        }catch(Exception e){
            if(e instanceof SQLException){
                SQLException sqlException=(SQLException)e;
                validateTemp.put("checkValue","SQLSTATE:"+sqlException.getSQLState()+";"
                                              +"ERRORCODE:"+sqlException.getErrorCode()+";MESSAGE:"+sqlException.getMessage());
            }else{
                validateTemp.put("checkValue", e.getMessage());
            }
            validateTemp.put("res",false);
        }
        validateResult.add(validateTemp);
		return validateResult;
	}
	 

	 /**
	  * 
	  * */
	public boolean queryAduitState(int tableId,int tableVersion){
		boolean bool = false;
		bool = tableExamineDAO.queryAduitState(tableId,tableVersion);
		return bool;
	}

    /**
     * 更新实例表版本为最新版本
     * @param tableId
     * @param tableVersion
     */
    public void updateInstByTableId(int tableId,int tableVersion){
        metaTableInstDAO.updateInstByTableId(tableId, tableVersion);
    }


	public void setTableExamineDAO(TableExamineDAO tableExamineDAO) {
		this.tableExamineDAO = tableExamineDAO;
	}
	public void setMetaTableDao(MetaTablesDAO metaTableDao) {
		this.metaTableDao = metaTableDao;
	}
	public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
		this.metaTableColsDAO = metaTableColsDAO;
	}
	public void setMetaTableInstDAO(MetaTableInstDAO metaTableInstDAO) {
		this.metaTableInstDAO = metaTableInstDAO;
	}
    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO){
        this.metaDataSourceDAO = metaDataSourceDAO;
    }    
    public void setTableApplyDAO(TableApplyDAO tableApplyDAO){
        this.tableApplyDAO = tableApplyDAO;
    }
}
