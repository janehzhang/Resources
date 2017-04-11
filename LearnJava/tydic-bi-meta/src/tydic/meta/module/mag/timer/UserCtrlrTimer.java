package tydic.meta.module.mag.timer;

import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.module.mag.user.UserDAO;
import tydic.meta.sys.code.CodeManager;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description 这是一个控制用户一个月未登陆就禁用的定时器
 * @date: 12-3-8
 * @time: 下午4:55
 */

public class UserCtrlrTimer implements IMetaTimer{
    private UserDAO userDAO;
    public void init() {
        userDAO = new UserDAO();
    }

    /**
     * 定期器实现任务程序
     * @param timerName timer唯一标识
     */
    public void run(String timerName) {
        String hiddenStations = SystemVariable.getString("hidden.stations","22");
        String userLoseTimes   = SystemVariable.getString("userloselongtime","30*24*60*60*1000");
        //获取 超级用户，取消禁用
        String adminUsers = SystemVariable.getString("adminUser","457,120,353331,353332,353333,201");
        String userLoseTime[] = userLoseTimes.contains("*")?userLoseTimes.split("\\*"):new String[]{userLoseTimes};
        String hiddenStation[] = hiddenStations.contains(",")?hiddenStations.split(","):new String[]{hiddenStations};
        String adminUser[] = adminUsers.contains(",")?adminUsers.split(","):new String []{adminUsers};
        long timeLong = 1;
        for (int i=0; i<userLoseTime.length; i++){
            timeLong = timeLong*Long.parseLong(userLoseTime[i]);
        }
        //得到禁用的
        java.util.Date date = new java.util.Date(System.currentTimeMillis()-timeLong);
        List<Map<String, Object>> list = userDAO.getUserLoginLast(hiddenStation,date,adminUser);
        int[] intName = new int[list.size()];

        for(int j=0; j<list.size(); j++){
            intName[j] = Convert.toInt(list.get(j).get("USER_ID"));
        }
        int result = -1;
        try {
            result = userDAO.disableUser(intName);
            //记录日志
            userDAO.insertUserChangeLog(intName,
                    UserConstant.META_MAG_USER_CHANGE_NAME_DISABLEUSER,
                    UserConstant.META_MAG_USER_EDITOR_TYPE_AUTO,
                    null);
        } catch (Exception e) {
            Log.error("用户禁用失败",e);
        }
    }
}
