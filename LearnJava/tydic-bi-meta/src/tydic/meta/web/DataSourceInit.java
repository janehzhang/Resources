package tydic.meta.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import tydic.frame.DataSourceManager;
import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataSourceImpl;
import tydic.frame.web.ISystemStart;
import tydic.meta.common.Constant;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.TblConstant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 *
 * 数据源初始化类
 *
 * @author 谭红涛
 * @date 2012-2-15
 */
public class DataSourceInit implements ISystemStart {
	/**
	 * 数据源默认大小
	 */
	public static int DEFAULT_MIN_COUNT = SystemVariable.getInt("db.dynamic.minSize", 5);
	private static List<String> dataSourceIds = new ArrayList<String>();
	
	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#destory()
	 */
	public void destory() {
		Collection<DataSource> dataSources = DataSourceManager.getAllDataSource().values();
		for(DataSource dataSource:dataSources){
			if(dataSource instanceof DataSourceImpl){
				((DataSourceImpl)dataSource).destroy();
			}
		}
		DataSourceManager.getAllDataSource().clear();
	}

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#init()
	 */
	public void init() {
		String metaDataSourceID = Convert.toString(TblConstant.META_DATA_SOURCE_ID);
		//将默认数据源的ID指向默认数据源
		DataSourceManager.addDataSource(metaDataSourceID, DataSourceManager.getAllDataSource().get(SystemVariable.getString("db.default", "config1")));
		loadAll();
	}
	
	/**
	 * 载入指定ID的数据源
	 * @param id 数据源ID
	 */
	public synchronized static void load(int id){
		MetaDataSourceDAO dataSourceDAO  = new MetaDataSourceDAO();
		load(dataSourceDAO.queryDataSourceById(id));
	}
	
	/**
	 * 重新载入所有数据源
	 */
	public synchronized static void loadAll(){
		MetaDataSourceDAO dataSourceDAO  = new MetaDataSourceDAO();
		for(String id : dataSourceIds){
			DataSourceManager.removeDataSource(id);
		}
		List<Map<String,Object>> list = dataSourceDAO.queryDataSourceByType(TblConstant.META_DATA_SOURCE_TABLE);
		for(Map<String,Object> map : list){
			load(map);
		}
		dataSourceDAO.close();
	}
	
	/**
	 * 根据map中的信息动态载入一个数据源
	 * @param map 数据源信息
	 */
	private synchronized static void load(Map<String,Object> map){
		int id = MapUtils.getIntValue(map, "DATA_SOURCE_ID", 0);
		if(id!=TblConstant.META_DATA_SOURCE_ID){
			try{
				String user = MapUtils.getString(map, "DATA_SOURCE_USER");
				String passwd = MapUtils.getString(map, "DATA_SOURCE_PASS");
				String url = MapUtils.getString(map, "DATA_SOURCE_RULE");
				int minCount = MapUtils.getIntValue(map,"DATA_SOURCE_MIN_COUNT", DEFAULT_MIN_COUNT);
				DataSourceImpl dataSource = new DataSourceImpl(Constant.ORACLE_DRIVER_STRING, url, user, passwd);
				dataSource.setMinCount(minCount);
				DataSourceManager.addDataSource(Convert.toString(id), dataSource);
			}catch(Exception e){
				Log.error("初始化数据源出错,数据源ID为"+id, e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#setServletContext(javax.servlet.ServletContext)
	 */
	public void setServletContext(ServletContext servletContext) {
	}

}
