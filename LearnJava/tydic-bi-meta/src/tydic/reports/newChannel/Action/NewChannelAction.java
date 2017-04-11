package tydic.reports.newChannel.Action;

import java.util.HashMap;
import java.util.Map;

import tydic.reports.newChannel.Dao.NewChannelDao;

/**
 * 政企报表的新改动
 * @author 我爱家乡
 *
 */
public class NewChannelAction {
	
	public NewChannelDao newChannelDao;

	public NewChannelDao getNewChannelDao() {
		return newChannelDao;
	}

	public void setNewChannelDao(NewChannelDao newChannelDao) {
		this.newChannelDao = newChannelDao;
	}
	
	/***
	 * 查询数据的展现 政企
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getZqData(Map<String, Object> queryData){
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.putAll(newChannelDao.exeCallReplaceProcedure(queryData)); //执行存储过程
		return map;
	}
	
	
}
