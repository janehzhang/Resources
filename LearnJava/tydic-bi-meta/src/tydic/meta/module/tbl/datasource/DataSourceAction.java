package tydic.meta.module.tbl.datasource;

import tydic.frame.DataSourceManager;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.web.DataSourceInit;

import java.util.List;
import java.util.Map;

/**
 *
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 *
 * @author 刘斌
 * @date 2012-02-13
 * @description 管理Action
 *
 */
public class DataSourceAction {
    /**
     * 数据库操作类
     */
    private MetaDataSourceDAO metaDataSourceDAO;

    /**
     * 根据关键字查询数据源信息
     * @param key
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryDataSource(String key,Page page){
        if(page==null){//如果没有page，为第一页。
           page=new Page(0,20);
        }
        return metaDataSourceDAO.queryDataSource(key, page);
    }

    /**
     * 查询当前系统信息
     * @return
     */
    public List<Map<String,Object>> querySysInfo(){
        return metaDataSourceDAO.querySysInfo();
    }

    /**
     * 新增或者修改数据源信息
     * @param data
     * @return
     */
    public Object saveOrupdate(Map<String, Object> data){
        try{
            OprResult<Integer,Object> result = null;
            int dataSourceId = Convert.toInt(data.get("dataSourceId"), -1);
            if(dataSourceId == -1){//新增
                dataSourceId = (int)metaDataSourceDAO.queryForNextVal("SEQ_DATA_SOURCE_ID");
                data.put("dataSourceId", dataSourceId);
                metaDataSourceDAO.insertDataSource(data);
                Map<String, Object> newData = metaDataSourceDAO.queryDetailById(dataSourceId);
                result = new OprResult<Integer,Object>(null, dataSourceId, OprResult.OprResultType.insert);
                result.setSuccessData(newData);
                //启用
                DataSourceInit.load(dataSourceId);
                return result;
            }else{//修改
                result = new OprResult<Integer,Object>(null, metaDataSourceDAO.updateDataSource(data), OprResult.OprResultType.update);
                result.setSuccessData(metaDataSourceDAO.queryDetailById(dataSourceId));
                //启用
                DataSourceInit.load(dataSourceId);
                return result;
            }
        }catch (Exception e){
            Log.error("", e);
            return new OprResult<Integer,Object>(null, null, OprResult.OprResultType.error);
        }
    }

    /**
     * 数据源启用
     * @param sourceId
     * @return
     */
    public OprResult<Integer,Object> onlineDataSource(int sourceId){
        try{
            OprResult<Integer,Object> result= new OprResult<Integer,Object>(null, metaDataSourceDAO.onlineDataSource(sourceId), OprResult.OprResultType.update);
            result.setSuccessData(metaDataSourceDAO.queryDetailById(sourceId));
            DataSourceInit.load(sourceId);
            return result;
        }catch (Exception e){
            Log.error("", e);
            return new OprResult<Integer,Object>(null, null, OprResult.OprResultType.error);
        }

    }

    /**
     * 数据源停用
     * @param sourceId
     * @return
     */
    public OprResult<?,?> offlineDataSource(int sourceId){
        try{
            // 若有表类引用改数据源 则不能停用
            OprResult<Integer,Object> result;
            if(metaDataSourceDAO.checkUsing(sourceId)){
                result = new OprResult<Integer,Object>();
                result.setSid(0);
                return result;
            }
            result = new OprResult<Integer,Object>(null, metaDataSourceDAO.offlineDataSource(sourceId), OprResult.OprResultType.update);
            result.setSuccessData(metaDataSourceDAO.queryDetailById(sourceId));
            DataSourceManager.removeDataSource(String.valueOf(sourceId));
            return result;
        }catch (Exception e){
            Log.error("", e);
            return new OprResult<Integer,Object>(null, null, OprResult.OprResultType.error);
        }
    }

    /**
     * 测试数据源
     * @param data
     * @return
     */
    public String testDataSource(Map<?,?> data){
        String user = Convert.toString(data.get("dataSourceUser"));
        String url = Convert.toString(data.get("dataSourceRule"));
        String pwd = Convert.toString(data.get("dataSourcePass"));
        try{
            metaDataSourceDAO.testDataSource(url, user, pwd);
            return null;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    /**
     * 重载
     * @param dataSourceId
     * @return
     */
    public String reloadDataSource(int dataSourceId){
        try{
            DataSourceInit.load(dataSourceId);
            metaDataSourceDAO.getConnection(Convert.toString(dataSourceId));
            return null;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
        this.metaDataSourceDAO = metaDataSourceDAO;
    }

}
