package tydic.meta.module.gdl.examine;

import org.directwebremoting.util.Logger;
import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.meta.module.mag.timer.IMetaTimer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标到达生效时间自动生效 <br>
 * @date 2012-6-15
 */
public class ValidateTimer implements IMetaTimer {

    private ValidateTimerDAO validateTimerDAO;

    public void init() {
    }

    public void run(String timerName) {
        try{
            validateTimerDAO = new ValidateTimerDAO();
            BaseDAO.beginTransaction();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            List<Map<String, Object>> gdlIds = validateTimerDAO.querySuitGdlIds(time);
            for(Map<String, Object> m : gdlIds){
                validateTimerDAO.invalidGdl(Convert.toInt(m.get("GDL_ID")));
            }
            validateTimerDAO.validSuitGdls(time);
            BaseDAO.commit();
        }catch (Exception e){
            e.printStackTrace();
            BaseDAO.rollback();
        }finally {
            validateTimerDAO.close();
        }
    }

    public void setValidateTimerDAO(ValidateTimerDAO validateTimerDAO) {
        this.validateTimerDAO = validateTimerDAO;
    }

    public static void main(String args[]){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.print(sdf.format(d));


    }
}
