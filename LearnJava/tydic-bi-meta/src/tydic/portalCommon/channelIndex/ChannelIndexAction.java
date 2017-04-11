package tydic.portalCommon.channelIndex;

import java.util.List;
import java.util.Map;

import tydic.meta.module.mag.menu.MenuCommon;
import tydic.meta.module.mag.role.RoleDAO;

/**
 * 渠道服务指引
 * @author 我爱家乡
 *
 */
public class ChannelIndexAction {
	
	private ChannelIndexDao channelIndexDao;
	
	private RoleDAO roleDao;
	
	public ChannelIndexDao getChannelIndexDao() {
		return channelIndexDao;
	}


	public void setChannelIndexDao(ChannelIndexDao channelIndexDao) {
		this.channelIndexDao = channelIndexDao;
	}

	
	public RoleDAO getRoleDao() {
		return roleDao;
	}


	public void setRoleDao(RoleDAO roleDao) {
		this.roleDao = roleDao;
	}


	/**
	 * 查询数据
	 * @param parentId
	 * @param userId
	 * @return
	 */
	public  List<Map<String, Object>> queryAllSubMenu(int parentId ,int userId) {
		int userIdStr=0;
		
		 //判断当前用户是否是管理员
		if(roleDao.isCurrentUserAdmin(""+userId+"")){
			userIdStr=1; //表示为管理员
		}else{
			userIdStr=userId;
		}
		
		return channelIndexDao.getMenuData(parentId,userIdStr);
	}
	/***
	 * 查询二级菜单
	 * @param parentId
	 * @param userId
	 * @return
	 */
	public  List<Map<String, Object>> querySubMenu(int parentId ,int userId) {
		int userIdStr=0;
		 //判断当前用户是否是管理员
		if(roleDao.isCurrentUserAdmin(""+userId+"")){
			userIdStr=1; //表示为管理员
		}else{
			userIdStr=userId;
		}
		return channelIndexDao.querySubMenuData(parentId, userIdStr);
	}
	
	/***
	 * 查询根菜单
	 * @param parentId
	 * @param userId
	 * @return
	 */
	public  List<Map<String, Object>> querySubMenu2(int parentId ,int userId) {
		int userIdStr=0;
		 //判断当前用户是否是管理员
		if(roleDao.isCurrentUserAdmin(""+userId+"")){
			userIdStr=1; //表示为管理员
		}else{
			userIdStr=userId;
		}
		return channelIndexDao.querySubMenuData2(parentId, userIdStr);
	}
	/***
	 * 查询我的收藏
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryFavoriteMenu(int userId){
		
		return channelIndexDao.queryFavoriteMenu(userId) ;
	}
}
