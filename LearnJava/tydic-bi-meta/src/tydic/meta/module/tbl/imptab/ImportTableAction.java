package tydic.meta.module.tbl.imptab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.apply.TableApplyDAO;
import tydic.meta.module.tbl.diff.DiffAnysis;

/**
 *
 * @author 李国民
 * @date 2012-01-10
 * @description 表类管理-导入实体表Action
 *
 */
public class ImportTableAction {
	
    private ImportTableDAO importTableDAO;
    private MetaDataSourceDAO metaDataSourceDAO;
    private MetaTableColsDAO metaTableColsDAO;
    private TableApplyDAO tableApplyDAO;
    //时间宏变量规格
    private String [] macroVariable = {"{YY}","{YYYY}","{MM}","{DD}","{HH}","{MI}","{SS}","{M}","{D}",
    		"{YYYYMM}","{YYYYMMN}","{YYYYMMP}","{YYYYMMDD}","{YYYYMMDDP}","{N_YY}","{N_YYYY}","{N_MM}",
    		"{N_DD}","{N_HH}","{N_MI}","{N_SS}","{N_M}","{N_D}","{N_YYYYMM}","{N_YYYYMMN}","{N_YYYYMMP}",
    		"{N_YYYYMMDD}","{N_YYYYMMDDP}"};
    //地域宏变量
    private String LocalCode = "{LOCAL_CODE}";

    /**
     * 批量导入实体表
     * @param datas 需要导入的数据
     * @return
     */
    public boolean insertInstTable(List<Map<String,Object>> datas){
    	try{
    		importTableDAO.insertMetaMagUserTable(datas);
    	}catch(Exception e){
            Log.error(null,e);
            return false;
        }
    	return true;
    }
    
    /** 
     * 查询需要导入的实体表信息及对应的表类
     * @return 
     */
    public List<Map<String,Object>> queryDbTables(Map<String,String> queryMessage){
    	int dataSourceID = 0;
    	if(!queryMessage.get("dataSource").equals("")){
    		dataSourceID = Integer.parseInt(queryMessage.get("dataSource"));
    	}
        //得到已经添加表映射的数据
        List<Map<String, Object>> instList = importTableDAO.queryInstTables(queryMessage.get("owner"));
        //得到已经存在映射关系的表名集合
        List<String> tableNamesList = getTableNamesList(instList);
        //得到对应数据源中的宏表类
        List<Map<String, Object>> tablesList = importTableDAO.queryMetaTables(dataSourceID);
        //通过数据源查询对应实体表
        List<Map<String,Object>> dbList = importTableDAO.queryDbTables(dataSourceID,queryMessage.get("owner"),
        		queryMessage.get("keyWord").toUpperCase(),tableNamesList);
        //返回数据
        List<Map<String, Object>> outList = getOutList(dbList, tablesList,dataSourceID,queryMessage.get("owner"));
        return outList;
    }
    
    /**
     * 得到已经存在映射关系的表名查询集合
     * @param instList 映射表数据
     * @return
     */
    private List<String> getTableNamesList (List<Map<String, Object>> instList){
        List<String> tableNamesList = new ArrayList<String>();
        String tableNames = "";
        int count = 0; 			//计数器
        int sum = 1000;			//分段数
        boolean check = true;	//判断开关
        for(int i=0;i<instList.size();i++){	//通过循环得到已经存在的映射关系表名，拼装成查询条件
        	Map<String, Object> instMap = instList.get(i);
        	if(check){	//条件第一个，前面不加逗号
            	tableNames += "'"+instMap.get("TABLE_NAME")+"'";
        		check = false;
        	}else{
            	tableNames += ","+"'"+instMap.get("TABLE_NAME")+"'";
        	}
        	count++;
        	if(count==sum){		//当计数器等于分段数时，重新拼装
        		tableNamesList.add(tableNames);
        		tableNames = "";
        		count = 0;
        		check = true;
        	}
        }
        if(!check){		//当开关不为true时，把未添加的条件添加到集合中
        	tableNamesList.add(tableNames);
        }
        return tableNamesList;
    }
    
    /**
     * 返回匹配后的实体表集合
     * （包括表名带宏变量的匹配及表列的差异化分析）
     * @param dbList 匹配前的实体表集合
     * @param tablesList 带有宏的表类集合
     * @return
     */
    private List<Map<String, Object>> getOutList(List<Map<String, Object>> dbList, 
    			 List<Map<String, Object>> tablesList, int dataSourceID, String owner){ 
    	List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
    	int count = 0;			//计数器
    	int sum = 200; 			//每次显示最多条数
	    for (int i = 0; i < dbList.size(); i++) {
	    	Map<String, Object> dbMap = dbList.get(i);
	    	String dbTabelName = (String) dbMap.get("TABLE_NAME");	//实体表表名
	    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    	boolean diffCheck = false;	//是否存在差异（false为不存在，true为存在）
	    	boolean isCheck = true;		//开关，判断是否需要进行表列差异化比对	
			for (int j = 0; j < tablesList.size(); j++) {
				Map<String, Object> tablesMap = tablesList.get(j);
				String metaTablesName = (String) tablesMap.get("TABLE_NAME");	//带宏变量的表类表名
				//拼装值，用于前台选中后使用
				String metaTablesValue = tablesMap.get("TABLE_ID").toString()+";"
					+tablesMap.get("TABLE_VERSION").toString();
				if(metaTablesName!=null&&!metaTablesName.equals("")){
					Map<String,Object> map = validaName(metaTablesName, dbTabelName, metaTablesValue);		//验证匹配
					if(map != null){
						list.add(map);
						if(isCheck){ 
							//如果为第一次是差异化分析时，调用方法，进行差异化分析，得到是否存在差异
							Map<String, String> dataMap = new HashMap<String, String>();
							dataMap.put("dataSourceID", dataSourceID+"");
							dataMap.put("owner", owner.toString());
							dataMap.put("tableId", tablesMap.get("TABLE_ID").toString());
							dataMap.put("tableVersion", tablesMap.get("TABLE_VERSION").toString());
							dataMap.put("tableName", dbTabelName.toString());
							diffCheck = isDiffCheck(dataMap);
							isCheck = false;//如果该表类已经进行过一次差异化分析后，关闭开关
						}
					}
				}
			}
			if(list.size()>0){
				//当实体表表名存在匹配成功的表类时，添加该表类到返回结果
				count++;
				dbMap.put("META_TABLES", list);
				dbMap.put("DIFF_CHECK", diffCheck);
				outList.add(dbMap);
				if(count == sum){
					break;
				}
			}
		}        
	    return outList;
    }
    
    
    /**
     * 通过传入的值进行宏变量匹配，如果通过返回保存集合
     * @param metaTablesName	带宏变量的表类表名
     * @param dbTabelName		实体表表名
     * @param metaTablesValue	拼装值，用于前台选中后使用
     * @return
     */
    private Map<String,Object> validaName(String metaTablesName, String dbTabelName, String metaTablesValue){
    	Map<String,Object> map = null;	 //返回值
		boolean check = false;		//控制开关

		int localIndex = metaTablesName.indexOf(LocalCode);;	//地域宏变量下标
		int dateIndex  = -1;	//时间宏变量下标
		String dateCode = "";	//时间宏变量值
		if(localIndex != -1){	//当地域宏变量存在时，判断是否存在时间宏变量
			for(int m=0;m<macroVariable.length;m++){
				dateIndex = metaTablesName.indexOf(macroVariable[m].toString());
				if(dateIndex != -1){	//存在匹配的时间宏变量值
					dateCode = macroVariable[m].toString();
					break;
				}	
			}
		}
		if(localIndex != -1 && dateIndex != -1){
			//当两个都不为-1时，表示为同时存在地域及时间宏变量表类
			int fristIndex = localIndex>dateIndex?dateIndex:localIndex;		//第一个宏变量下标
			String tem [] = null;	//分割宏变量后的数组
			String temStr1 = "";	//变量1
			String temStr2 = "";	//变量2
			String temStr3 = "";	//变量3
			if(fristIndex == localIndex){	//地域宏变量在前，时间宏变量在后
				tem = metaTablesName.split(LocalCode.replace("{", "\\{").replace("}", "\\}"));
				temStr1 = tem[0];
				tem = tem[1].split(dateCode.replace("{", "\\{").replace("}", "\\}"));
			}else{	//时间宏变量在前，地域宏变量在后
				tem = metaTablesName.split(dateCode.replace("{", "\\{").replace("}", "\\}"));
				temStr1 = tem[0];
				tem = tem[1].split(LocalCode.replace("{", "\\{").replace("}", "\\}"));
			}
			if(tem.length==2){
				temStr2 = tem[0];
				temStr3 = tem[1];
			}else if(tem.length==1){
				temStr2 = tem[0];
			}
			//当在变量值都存在实体表中时，进行宏变量值匹配
			if(dbTabelName.indexOf(temStr1)!=-1&&dbTabelName.indexOf(temStr2)!=-1&&dbTabelName.indexOf(temStr3)!=-1){
				if(!temStr1.equals("")){	//如果变量1不为空，替换实体表中对应变量值
					dbTabelName = dbTabelName.replace(temStr1, "");
				}
				if(!temStr3.equals("")){	//如果变量3不为空，替换实体表中对应变量值
					dbTabelName = dbTabelName.replace(temStr3, "");
				}
				String localValue = "";		//实体表对应地域宏变量值
				String dateValue = "";		//实体表对应时间宏变量值
				if(!temStr2.equals("")){
					String tablesValue [] = dbTabelName.split(temStr2);
					if(tablesValue.length==2){
						if(fristIndex == localIndex){	//地域宏变量在前，时间宏变量在后
							localValue = tablesValue[0];
							dateValue = tablesValue[1];
						}else{	//时间宏变量在前，地域宏变量在后
							dateValue = tablesValue[0];
							localValue = tablesValue[1];
						}
						check = true;
					}
				}else{
					int dateLength = dateCode.replace("{", "").replace("}", "").replace("N", "").replace("P", "").replace("_", "").length();
					if(dbTabelName.length()>dateLength){	//对应实体表值至少应该大于时间宏变量长度
						if(fristIndex == localIndex){	//地域宏变量在前，时间宏变量在后
							localValue = dbTabelName.substring(0, dbTabelName.length()-dateLength);
							dateValue = dbTabelName.substring(dbTabelName.length()-dateLength, dbTabelName.length());
						}else{	//时间宏变量在前，地域宏变量在后
							dateValue = dbTabelName.substring(0, dateLength);
							localValue = dbTabelName.substring(dateLength, dbTabelName.length());
						}
						check = true;
					}
				}
				if(check){
					//传入表类宏变量及实体表宏变量值进行匹配
					check = isDataValida(LocalCode,localValue)&&isDataValida(dateCode,dateValue);
				}
			}
		}else{
			//只有一个宏变量时
			String metaValue = "";		//表类宏变量
			String tablesValue = "";	//实体表对应宏变量值
			int startInt = metaTablesName.indexOf("{");
			int endInt = metaTablesName.indexOf("}");
			int length = metaTablesName.length();
			if(startInt==0){	//当{为第一个字符时，宏变量在前
				String checkName = metaTablesName.substring(endInt+1, length);	//宏表类匹配值
				if(dbTabelName.indexOf(checkName)!=-1){	//如果匹配值被包含在实体表表名中，进入下一步匹配
					String name = dbTabelName.substring(dbTabelName.length()-
							checkName.length(), dbTabelName.length());
					if(checkName.equals(name)){
						check = true;
						metaValue = metaTablesName.substring(0,endInt+1);
						tablesValue = dbTabelName.substring(0,dbTabelName.length()-name.length());
					}
				}
			}else if(endInt==length-1){	//当{不为第一个字符， 且}为最后一个字符时，宏变量在末尾
				String checkName = metaTablesName.substring(0, startInt);	//宏表类匹配值
				if(dbTabelName.indexOf(checkName)!=-1){	//如果匹配值被包含在实体表表名中，进入下一步匹配
					String name = dbTabelName.substring(0, checkName.length());
					if(checkName.equals(name)){
						check = true;
						metaValue = metaTablesName.substring(startInt,metaTablesName.length());
						tablesValue = dbTabelName.substring(startInt,dbTabelName.length());
					}
				}
			}else{	//当以上两种情况都不成立时，则宏变量在中间
				String checkNameOne = metaTablesName.substring(0, startInt);	//宏表类匹配值(前)
				String checkNameTwo = metaTablesName.substring(endInt+1, length);	//宏表类匹配值(后)
				if(dbTabelName.indexOf(checkNameOne)!=-1&&dbTabelName.indexOf(checkNameTwo)!=-1){
					String nameOne = dbTabelName.substring(0, checkNameOne.length());	//实体表匹配值(前)
					String nameTwo = dbTabelName.substring(dbTabelName.length()-
							checkNameTwo.length(), dbTabelName.length());				//实体表匹配值(后)
					if(checkNameOne.equals(nameOne)&&checkNameTwo.equals(nameTwo)){
						check = true;
						metaValue = metaTablesName.substring(startInt,endInt+1);
						tablesValue = dbTabelName.substring(startInt,dbTabelName.length()-nameTwo.length());
					}
				}
			}
			if(check){
				//传入表类宏变量及实体表宏变量值进行匹配
				check = isDataValida(metaValue,tablesValue);
			}
		}
		if(check){
			//表类宏变量和实体表宏变量匹配成功后，添加该表类到实体表匹配成功列表中
			map = new HashMap<String,Object>();
			map.put("0", metaTablesValue);
			map.put("1", metaTablesName);
		}
    	return map;
    }
    
    
    /**
     * 表类宏变量及实体表宏变量值匹配方法
     * @param metaValue  表类宏变量
     * @param tablesValue 实体表宏变量值
     * @return 匹配成功 返回true，不成功返回false
     */
    private boolean isDataValida(String metaValue, String tablesValue){
		boolean check = false;		 //是否匹配成功
		if(metaValue.equals(LocalCode)){
			//如果该宏变量为地域宏变量，判断该对应实体表宏变量值是否存在。
			check = importTableDAO.isExistLocalCode(tablesValue);
		}else{		
			//否则为时间宏变量，进行匹配
			for(int m=0;m<macroVariable.length;m++){
				if(macroVariable[m].toString().equals(metaValue.toString())){
					try{
						//判断实体表宏变量值是否为数字，不为数字则抛出异常
						Integer.parseInt(tablesValue);	
						if(metaValue.indexOf("N_")!=-1){
							//当宏变量存在N_时，对宏变量值进行处理，如{N_YYYYP}处理为YYYYP
							metaValue = metaValue.substring(3, metaValue.length()-1);
							if(metaValue.indexOf("N")!=-1||metaValue.indexOf("P")!=-1){
								//如果处理后的宏变量还存在N或者P时，再次进行处理，如YYYYP处理为YYYY
								metaValue = metaValue.substring(0, metaValue.length()-1);
							}
						}else if(metaValue.indexOf("N")!=-1||metaValue.indexOf("P")!=-1){
							//第一种情况不成立时，如果宏变量存在N或者P时，进行处理，如{YYYYP}处理为YYYY
							metaValue = metaValue.substring(1,metaValue.length()-2);
						}else{
							//当以上两种情况都不成立时，把宏变量大括号清除。如{YYYY}处理为YYYY
							metaValue = metaValue.substring(1,metaValue.length()-1);
						}
						if(metaValue.length() == tablesValue.length()){
							//如果处理后的宏变量长度和实体宏变量值长度相等，匹配成功
							check =true;
						}
						break;
					}catch(Exception e){ 
						//进入异常表示该宏标量值不为数字，和时间格式宏变量匹配不成功
						check = false;
					}
				}
			}
		}
    	return check;
    }
    
    /**
     * 得到差异化分析信息
     * @param dataSourceID 数据源id
     * @param owner 实体表拥有者
     * @param tableName 实体表名
     * @param tableId 表类id
     * @param tableVersion 表类版本
     * @return
     */
    public List<Map<String,Object>> getDiffList(String dataSourceID, String owner, String tableName, 
    			String tableId, String tableVersion){
    	int dsId = 0;
    	if(!dataSourceID.equals("")){
    		dsId = Integer.parseInt(dataSourceID);
    	}
        //获取表类当前版本所有列信息。
        List<Map<String,Object>> tableColumnsConfig=metaTableColsDAO.queryMetaTableColsByTableId(Integer.parseInt(tableId),
        		Integer.parseInt(tableVersion),null);
        List<Map<String,Object>> instTableColumns=null;
        try{
            //根据实例表信息到对应数据源查询实例表列信息。
            instTableColumns= tableApplyDAO.queryDbTableColumns(dsId+"",owner,tableName);
        }catch(Exception e){
        }
        return DiffAnysis.diffCompare(tableColumnsConfig,instTableColumns);
    }
    
    /**
     * 判断是否存在差异
     * @param data 
     * data包含键值：dataSourceID  数据源id ，tableId 表类id ，
     * 				tableVersion 表类版本 ，owner 实体表拥有者 ，tableName 实体表名
     * @return 返回true为存在差异，返回false为不存在差异
     */
    public boolean isDiffCheck(Map<String,String> data){
    	int dataSourceID = 0;
    	if(!data.get("dataSourceID").equals("")){
    		dataSourceID = Integer.parseInt(data.get("dataSourceID"));
    	}
        int tableId = Integer.parseInt(data.get("tableId"));
        int tableVersion = Integer.parseInt(data.get("tableVersion"));
        String owner = data.get("owner").toString();
        String tableName = data.get("tableName").toString();
        //获取表类当前版本所有列信息。
        List<Map<String,Object>> tableColumnsConfig=metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion,null);
        List<Map<String,Object>> instTableColumns=null;
        try{
            //根据实例表信息到对应数据源查询实例表列信息。
            instTableColumns= tableApplyDAO.queryDbTableColumns(dataSourceID+"",owner,tableName);
        }catch(Exception e){
        }
        return DiffAnysis.isDiffCompare(tableColumnsConfig,instTableColumns);
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
		this.metaTableColsDAO = metaTableColsDAO;
	}
	public void setTableApplyDAO(TableApplyDAO tableApplyDAO) {
		this.tableApplyDAO = tableApplyDAO;
	}
	public void setImportTableDAO(ImportTableDAO importTableDAO) {
		this.importTableDAO = importTableDAO;
	}
	public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
		this.metaDataSourceDAO = metaDataSourceDAO;
	}
}