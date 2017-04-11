package tydic.meta.web;

import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.web.ISystemStart;
import tydic.meta.module.mag.menu.MenuDAO;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 菜单数据初始化 <br>
 * @date 2011-12-13
 */
public class MenuDataInit implements ISystemStart {

    /**
     * 以ID为键值的菜单数据全集键值
     */
    public final static String APPLIACTION_KEY_MENUDATA = "menuData";

    /**
     * 以URL为键值的菜单数据全集
     */
    public final static String APPLICATION_KEY_URL_MENUDATA = "urlMenuData";

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void init() {
        MenuDAO menuDAO = new MenuDAO();
        //查询所有菜单数据
        try {
            List<Map<String, Object>> menuDatas = menuDAO.queryAllMenu();
            //根据MENU_ID,URL设置键值，便于查找
            Map<Integer, Map<String, Object>> idMenuMapping = new HashMap<Integer, Map<String, Object>>();
            Map<String, Map<String, Object>> urlMenuMapping = new HashMap<String, Map<String, Object>>();
            for (Map<String, Object> menuData : menuDatas) {
                int menuId = Integer.parseInt(menuData.get("MENU_ID").toString());
                String url = Convert.toString(menuData.get("MENU_URL"));
                idMenuMapping.put(menuId, menuData);
                if (url != null && !url.trim().equals("")) {
                    //idMenuMapping.put(menuId, menuData);
                    urlMenuMapping.put(url, menuData);
                }
            }
            servletContext.setAttribute(APPLIACTION_KEY_MENUDATA, idMenuMapping);
            servletContext.setAttribute(APPLICATION_KEY_URL_MENUDATA, urlMenuMapping);
        } catch (NumberFormatException e) {
            Log.error(null, e);
        } finally {
            menuDAO.close();
        }
    }

    public void destory() {
    	servletContext.removeAttribute(APPLIACTION_KEY_MENUDATA);
    	servletContext.removeAttribute(APPLICATION_KEY_URL_MENUDATA);
    }
}
