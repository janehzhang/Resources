package tydic.meta.module.report.issue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.apply.TableApplyDAO;
import tydic.meta.module.tbl.diff.DiffAnysis;

/**
 * 审核数据Action
 * @author 李国民
 * Date：2012-03-07
 */
public class AuditDataAction {
	
	private AuditDataDAO auditDataDao;
    private MetaTableColsDAO metaTableColsDAO;
    private TableApplyDAO tableApplyDAO;
	private String provinceFlag = "0000";		//全省地域code
	
	/**
	 * 查询所有审核数据
	 * @param data 查询过滤条件
	 * @param page 分页
	 * @return
	 */
    public List<Map<String,Object>> getAuditData(Map<String,Object> data,Page page){
        return auditDataDao.getAuditData(data,page);
    }
    
	/**
	 * 查询某一模型下的审核日志
	 * @param data 数据日期
	 * @param issueId 模型id
	 * @param dataPeriod 按月还是按日
	 * @param page 分页
	 * @return
	 */
    public List<Map<String,Object>> getViewData(Date dataDate,String issueId,String dataPeriod,Page page){
        return auditDataDao.getViewData(dataDate,issueId,dataPeriod,page);
    }

    /**
	 * 根据模型Id，数据日期，获取审核的信息
     * @param issueId 模型id
     * @param dataDate 数据日期
     * @param localCode 地域
     * @return
     */
    public Map<String,Object> getAuditInfo(String issueId,String dataDate,String localCode){
        return auditDataDao.getAuditInfo(issueId,dataDate,localCode);
    }
    
    /**
	 * 根据模型Id，得到模型基本信息
     * @param issueId 模型id
     * @return
     */
    public Map<String,Object> getIssueInfo(String issueId){
        return auditDataDao.getIssueInfo(issueId);
    }
	
	/**
	 * 得到实体表列表
     * @param tableName 带地域宏变量的实体表名
     * @param tableId  表类id
	 * @return
	 */
	public List<Map<String,Object>> getTableInstList(String tableName,String tableId){
		List<Map<String,Object>> rs = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> list = auditDataDao.getTableInstList(tableName,tableId);
		int index = tableName.indexOf("%");
		int length = tableName.length();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			String tableInstName = map.get("TABLE_NAME").toString();
			String code = "";	//地域宏变量对应值
			if(index==0){		//宏变量在前
				String tem = tableName.substring(index+1, tableName.length());
				code = tableInstName.substring(index,tableInstName.indexOf(tem));
			}else if(index==length-1){	//宏变量在后
				code = tableInstName.substring(index,tableInstName.length());
			}else{		//宏变量在中
				String temBefore = tableName.substring(0, index);
				String temAfter = tableName.substring(index+1, tableName.length());
				code = tableInstName.replace(temBefore, "").replace(temAfter, "");
			}
			//判断宏变量对应值是否为地域code，为地域则添加返回
			if(auditDataDao.isExistByZoneCode(code)){
				map.put("DATA_AREA", code);
				rs.add(map);
			}
		}
		return rs;
	}

    /**
     * 根据实体表名tableName，表类idtableId，查询实体表是否存在，
     * 如果实体表存在且判断该实体表与表类是否存在差异
     * @param tableName 实体表名
     * @param tableId  表类id
     * @return
     */
	public int checkTableInst(String tableName,String tableId){
		int out = 1;		//验证通过标志
		Map<String,Object> tableInst = auditDataDao.getTableInst(tableName,tableId);
		if(tableInst!=null&&tableInst.size()>0){
			//如果实体表存在，判断是否存在差异
			boolean check = isDiffCheck(tableInst);
			if(check){
				//页面提示实体表存在差异
				out = 2;
			}
		}else{
			//页面提示实体表不存在
			out = 3;
		}
		return out;
	}
	
	/**
     * 判断是否存在差异
     * @param data 
     * data包含键值：dataSourceId  数据源id ，tableId 表类id ，
     * 				tableVersion 表类版本 ，tableOwner 实体表拥有者 ，tableName 实体表名
     * @return 返回true为存在差异，返回false为不存在差异
     */
    private boolean isDiffCheck(Map<String,Object> data){
    	try{
    		String dataSourceId = data.get("DATA_SOURCE_ID").toString();
	        int tableId = Integer.parseInt(data.get("TABLE_ID").toString());
	        int tableVersion = Integer.parseInt(data.get("TABLE_VERSION").toString());
	        String tableOwner = data.get("TABLE_OWNER").toString();
	        String tableName = data.get("TABLE_NAME").toString();
	        //获取表类当前版本所有列信息。
	        List<Map<String,Object>> tableColumnsConfig = metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion,null);
	        //根据实例表信息到对应数据源查询实例表列信息。
	        List<Map<String,Object>> instTableColumns = tableApplyDAO.queryDbTableColumns(dataSourceId+"",tableOwner,tableName);
	        if(instTableColumns!=null&&instTableColumns.size()>0){
	            return DiffAnysis.isDiffCompare(tableColumnsConfig,instTableColumns);
	        }else{
	        	return true;	//如果不存在实体表,存在异常
	        }
    	}catch(Exception e){
            Log.error("报表管理-审核数据模块错误",e);
    		return true;		//如果不存在实体表,存在异常
    	}
    	
    }

    /**
     * 获取已发布的字段名
     * @param issueId 模型id
     * @return
     */
    public List<Map<String,Object>> getTableInstColName(String issueId){
        return auditDataDao.getTableInstColName(issueId);
    }
    
	/**
	 * 获取实体表数据
	 * @param data  查询条件，[模型id，表名，数据源]
	 * @param page
	 * @return
	 */
    public List<Map<String,Object>> getTableInstData(Map<String,Object> data, Page page){
        if(page==null){//如果没有page，为第一页。
            page=new Page(0,20);
        }
        return auditDataDao.getTableInstData(data, page);
    }
    
    /**
     * 审核数据
     * @param data
     * @return
     */
    public boolean auditOperation(Map<String,Object> data){
    	if(data.get("localCode").toString().equals(provinceFlag)){
            BaseDAO.beginTransaction();		//创建事务
    		String codes[] = data.get("codes").toString().split(",");
    		boolean isOk = true;
    		for(int i=0;i<codes.length;i++){
    			data.put("localCode", codes[i]);	//把地域存入map中
    			//判断该审核记录是否存在
    			Map<String,Object> rs = auditDataDao.isExistAudit(data);
    			if(rs!=null&&rs.size()>0){		//如果存在，修改
    				data.put("auditLogId", rs.get("AUDIT_LOG_ID"));
    				isOk = auditDataDao.updateAudit(data);
    			}else{	//不存在，新增
    				isOk = auditDataDao.insertAudit(data);
    			}
    			if(!isOk){
    				break;
    			}
    		}
            if(isOk){  //当所有都成功是，提交事务
                BaseDAO.commit();
            }else{
                BaseDAO.rollback();
            }
        	return isOk;
    	}else{
        	if(data.get("auditLogId")!=null&&!data.get("auditLogId").toString().equals("")){
        		//如果审核记录id存在，则修改审核记录
        		return auditDataDao.updateAudit(data);
        	}else{
        		//如果审核记录id不存在，则新增审核记录
        		return auditDataDao.insertAudit(data);
        	}
    	}
    }
	
	/**
	 * 根据审核模式查询本地网分公司数据
	 * @return
	 */
	public List<Map<String,Object>> getZoneList(int auditProp){
		return auditDataDao.getZoneList(auditProp);
	}
    
	public void setAuditDataDao(AuditDataDAO auditDataDao) {
		this.auditDataDao = auditDataDao;
	}
	public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
		this.metaTableColsDAO = metaTableColsDAO;
	}
	public void setTableApplyDAO(TableApplyDAO tableApplyDAO) {
		this.tableApplyDAO = tableApplyDAO;
	}
	
}