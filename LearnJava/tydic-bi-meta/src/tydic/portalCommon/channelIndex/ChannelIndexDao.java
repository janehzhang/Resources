package tydic.portalCommon.channelIndex;

import java.util.List;
import java.util.Map;

import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.module.mag.user.UserConstant;

public class ChannelIndexDao extends MetaBaseDAO{
	
	public List<Map<String, Object>> getMenuData(int parentId, int userId){
		String sql = "SELECT  A.MENU_ID,A.PARENT_ID,A.MENU_NAME,A.MENU_TIP,A.MENU_URL, "
				+ "A.PAGE_BUTTON,A.GROUP_ID,A.ORDER_ID,A.IS_SHOW,A.CREATE_DATE, "
				+ "A.ICON_URL,A.TARGET,A.USER_ATTR,A.NAV_STATE,A.USER_ATTR_LIST, "
				+ "A.MENU_STATE,DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN, "
				+ "D.FLAG ROLE_FLAG,A.IMG_URL FROM META_MAG_MENU A "
				+ "LEFT JOIN (SELECT PARENT_ID,COUNT(1) CNT FROM META_MAG_MENU GROUP BY PARENT_ID) C "
				+ "ON A.MENU_ID=C.PARENT_ID "
				//关联出没有权限的按钮
				//关联菜单用户表中的数据
				+ "LEFT JOIN (SELECT MENU_ID,EXCLUDE_BUTTON,FLAG FROM  META_MAG_USER_MENU  WHERE USER_ID="
				+ userId
				+ ") D ON A.MENU_ID=D.MENU_ID "
				//所有子节点条件
				//+ " WHERE A.MENU_ID IN (SELECT MENU_ID FROM META_MAG_MENU CONNECT BY PRIOR MENU_ID= PARENT_ID START WITH PARENT_ID="
				+" WHERE A.MENU_ID IN (SELECT MENU_ID FROM META_MAG_MENU where PARENT_ID="+ parentId + ") ";
		if (userId != UserConstant.ADMIN_USERID) {//如果不是管理员。
			sql += "AND EXISTS (SELECT 1 FROM "
					+ "(SELECT MENU_ID FROM  META_MAG_USER_MENU B WHERE USER_ID="
					+ userId
					+ " "
					+ "UNION "
					+ "SELECT B.MENU_ID FROM META_MAG_USER_ROLE A,META_MAG_ROLE_MENU B WHERE A.USER_ID="
					+ userId
					+ " AND B.ROLE_ID=A.ROLE_ID) C WHERE A.MENU_ID=C.MENU_ID) ";
		}
		sql += "ORDER BY A.PARENT_ID,A.ORDER_ID ASC";
		
		return getDataAccess().queryForList(sql);
		 
	}
	/***
	 * 查询子菜单
	 * @param parentId
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> querySubMenuData(int parentId, int userId){
		String sql = "SELECT  A.MENU_ID,A.PARENT_ID,A.MENU_NAME,A.MENU_TIP,A.MENU_URL, "
				+ "A.PAGE_BUTTON,A.GROUP_ID,A.ORDER_ID,A.IS_SHOW,A.CREATE_DATE, "
				+ "A.ICON_URL,A.TARGET,A.USER_ATTR,A.NAV_STATE,A.USER_ATTR_LIST, "
				+ "A.MENU_STATE,DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN, "
				+ "D.FLAG ROLE_FLAG,A.IMG_URL FROM META_MAG_MENU A "
				+ "LEFT JOIN (SELECT PARENT_ID,COUNT(1) CNT FROM META_MAG_MENU GROUP BY PARENT_ID) C "
				+ "ON A.MENU_ID=C.PARENT_ID "
				//关联出没有权限的按钮
				//关联菜单用户表中的数据
				+ "LEFT JOIN (SELECT MENU_ID,EXCLUDE_BUTTON,FLAG FROM  META_MAG_USER_MENU  WHERE USER_ID="
				+ userId
				+ ") D ON A.MENU_ID=D.MENU_ID "
				//所有子节点条件
				//+ " WHERE A.MENU_ID IN (SELECT MENU_ID FROM META_MAG_MENU CONNECT BY PRIOR MENU_ID= PARENT_ID START WITH PARENT_ID="
				+" WHERE A.MENU_ID IN (SELECT MENU_ID FROM META_MAG_MENU where PARENT_ID="+ parentId + " and menu_url is null) ";
		if (userId != UserConstant.ADMIN_USERID) {//如果不是管理员。
			sql += "AND EXISTS (SELECT 1 FROM "
					+ "(SELECT MENU_ID FROM  META_MAG_USER_MENU B WHERE USER_ID="
					+ userId
					+ " "
					+ "UNION "
					+ "SELECT B.MENU_ID FROM META_MAG_USER_ROLE A,META_MAG_ROLE_MENU B WHERE A.USER_ID="
					+ userId
					+ " AND B.ROLE_ID=A.ROLE_ID) C WHERE A.MENU_ID=C.MENU_ID) ";
		}
		sql += "ORDER BY A.PARENT_ID,A.ORDER_ID ASC";
		
		return getDataAccess().queryForList(sql);
		 
	}
	
	/***
	 * 查询最后一级菜单数据
	 * @param parentId
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> querySubMenuData2(int parentId, int userId){
		String sql = "SELECT  A.MENU_ID,A.PARENT_ID,A.MENU_NAME,A.MENU_TIP,A.MENU_URL, "
				+ "A.PAGE_BUTTON,A.GROUP_ID,A.ORDER_ID,A.IS_SHOW,A.CREATE_DATE, "
				+ "A.ICON_URL,A.TARGET,A.USER_ATTR,A.NAV_STATE,A.USER_ATTR_LIST, "
				+ "A.MENU_STATE,DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN, "
				+ "D.FLAG ROLE_FLAG,A.IMG_URL,A.ARRT FROM META_MAG_MENU A "
				+ "LEFT JOIN (SELECT PARENT_ID,COUNT(1) CNT FROM META_MAG_MENU GROUP BY PARENT_ID) C "
				+ "ON A.MENU_ID=C.PARENT_ID "
				//关联出没有权限的按钮
				//关联菜单用户表中的数据
				+ "LEFT JOIN (SELECT MENU_ID,EXCLUDE_BUTTON,FLAG FROM  META_MAG_USER_MENU  WHERE USER_ID="
				+ userId
				+ ") D ON A.MENU_ID=D.MENU_ID "
				//所有子节点条件
				//+ " WHERE A.MENU_ID IN (SELECT MENU_ID FROM META_MAG_MENU CONNECT BY PRIOR MENU_ID= PARENT_ID START WITH PARENT_ID="
				+" WHERE A.MENU_ID IN (SELECT MENU_ID FROM META_MAG_MENU where PARENT_ID="+ parentId + " and menu_url is not null) ";
		if (userId != UserConstant.ADMIN_USERID) {//如果不是管理员。
			sql += "AND EXISTS (SELECT 1 FROM "
					+ "(SELECT MENU_ID FROM  META_MAG_USER_MENU B WHERE USER_ID="
					+ userId
					+ " "
					+ "UNION "
					+ "SELECT B.MENU_ID FROM META_MAG_USER_ROLE A,META_MAG_ROLE_MENU B WHERE A.USER_ID="
					+ userId
					+ " AND B.ROLE_ID=A.ROLE_ID) C WHERE A.MENU_ID=C.MENU_ID) ";
		}
		sql += "ORDER BY A.PARENT_ID,A.ORDER_ID ASC";
		
		return getDataAccess().queryForList(sql);
		 
	}
	/***
	 * 查询我的收藏夹菜单
	 * @param parentId
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryFavoriteMenu(int parentId, int userId) {
        String sql = "SELECT  A.MENU_ID,A.PARENT_ID,A.MENU_NAME,A.MENU_TIP,A.MENU_URL, " +
                "A.PAGE_BUTTON,A.GROUP_ID,A.ORDER_ID,A.IS_SHOW,A.CREATE_DATE, " +
                "A.ICON_URL,A.TARGET,A.USER_ATTR,A.NAV_STATE,A.USER_ATTR_LIST, " +
                "A.MENU_STATE,DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN, " +
                "D.FLAG ROLE_FLAG  FROM META_MAG_MENU A "
                + "LEFT JOIN (SELECT PARENT_ID,COUNT(1) CNT FROM META_MAG_MENU GROUP BY PARENT_ID) C "
                + "ON A.MENU_ID=C.PARENT_ID "
                //关联出没有权限的按钮
                //关联菜单用户表中的数据
                + "LEFT JOIN (SELECT MENU_ID,EXCLUDE_BUTTON,FLAG FROM  META_MAG_USER_MENU  WHERE USER_ID=" + userId + ") D ON A.MENU_ID=D.MENU_ID "
                //所有子节点条件
                + "WHERE A.MENU_ID IN (SELECT MENU_ID FROM META_MAG_MENU CONNECT BY PRIOR MENU_ID= PARENT_ID START WITH PARENT_ID=" + parentId + ") ";
        if (userId != UserConstant.ADMIN_USERID) {//如果不是管理员。
            sql += "AND EXISTS (SELECT 1 FROM " +
                    "(SELECT MENU_ID FROM  META_MAG_USER_MENU B WHERE USER_ID=" + userId + " " +
                    "UNION " +
                    "SELECT B.MENU_ID FROM META_MAG_USER_ROLE A,META_MAG_ROLE_MENU B WHERE A.USER_ID=" + userId
                    + " AND B.ROLE_ID=A.ROLE_ID) C WHERE A.MENU_ID=C.MENU_ID) ";
        }
        sql += "ORDER BY A.PARENT_ID,A.ORDER_ID ASC";
        return getDataAccess().queryForList(sql);
    }
	
	/***
	 * 查询收藏夹
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryFavoriteMenu(int userId){
		String sql="select a.*,rownum r from (select l.user_id,l.menu_id,m.menu_name,m.parent_id,count(1) cnt,m.lv,m.menu_url from META_MAG_MENU_VISIT_LOG l,(select menu_id,parent_id,"
                    +"menu_name ,level lv,menu_url  from meta_mag_menu t where connect_by_isleaf = 1 start with parent_id = 0 "
            +" connect by prior menu_id = parent_id) m where l.menu_id=m.menu_id and l.menu_id not in(84002)  and m.parent_id not in(160009) and m.lv>2 and l.visit_time >(sysdate-30)"
            +" and l.user_id="+userId +""
		    +" group by l.user_id,l.menu_id,m.menu_name,m.parent_id,m.lv,m.menu_url order by cnt desc) a where rownum <=8";
		return getDataAccess().queryForList(sql);
	}
	
	
}
