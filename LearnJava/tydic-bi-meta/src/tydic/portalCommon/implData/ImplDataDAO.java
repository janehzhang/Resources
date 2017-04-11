
  
package tydic.portalCommon.implData;

import java.util.Map;

import tydic.meta.common.MetaBaseDAO;



public class ImplDataDAO extends MetaBaseDAO
{
    
    public boolean deleteImplData(String currTime){
        String sql = "delete from tbas_int.i_qq10000_serv_num  where day_id =TO_DATE(?,'yyyy-mm-dd')"; 
		    Object[] p = new Object[1];
		    p[0] = currTime;
		return getDataAccess().execNoQuerySql(sql,p);
    }
    
    
    
    
    /**
     * 方法：执行数据导入
     * @param data
     * @return boolean
     */
    public boolean insertImportData(Map<String, Object> data) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("INSERT INTO tbas_int.i_qq10000_serv_num(DAY_ID,CITY_NAME,CITY_CODE,CONSULT_NUM,DEAL_NUM,COMPLAINT_NUM,FAULT_NUM,OTHER_NUM,QUERY_NUM,DEFINE_NUM,ACC_FRIEND_NUM,NEW_FRIEND_NUM,MAN_REQUEST_NUM,MAN_SERVICE_NUM,ANSWER_ONE_NUM,ANSWER_FIVE_NUM,MAN_ANSWER_NUM,SELF_SERVICE_NUM,ONLINE_MSG_NUM,ANSWER_24HOUR_NUM,DISP_SOLVE_NUM,REPEAT_SERV_NUM,SATISFIED_NUM,ASSESS_NUM,ONLINE_STFD_NUM,ONLINE_ASSESS_NUM)");
        buffer.append("VALUES(TO_DATE(?,'yyyy-mm-dd'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
        Object[] p = new Object[26];
     	p[0] = data.get("DAY_ID");
		p[1] = data.get("CITY_NAME");
		p[2] = data.get("CITY_CODE");
		p[3] = data.get("CONSULT_NUM");
		p[4] = data.get("DEAL_NUM");
		p[5] = data.get("COMPLAINT_NUM");
		p[6] = data.get("FAULT_NUM");
		p[7] = data.get("OTHER_NUM");
		p[8] = data.get("QUERY_NUM");
		p[9] = data.get("DEFINE_NUM");
		p[10] = data.get("ACC_FRIEND_NUM");
		p[11] = data.get("NEW_FRIEND_NUM");
		p[12] = data.get("MAN_REQUEST_NUM");
		p[13] = data.get("MAN_SERVICE_NUM");
		p[14] = data.get("ANSWER_ONE_NUM");
		p[15] = data.get("ANSWER_FIVE_NUM");
		p[16] = data.get("MAN_ANSWER_NUM");
		p[17] = data.get("SELF_SERVICE_NUM");
		p[18] = data.get("ONLINE_MSG_NUM");
		p[19] = data.get("ANSWER_24HOUR_NUM");
		p[20] = data.get("DISP_SOLVE_NUM");
		p[21] = data.get("REPEAT_SERV_NUM");
		p[22] = data.get("SATISFIED_NUM");
		p[23] = data.get("ASSESS_NUM");
		p[24] = data.get("ONLINE_STFD_NUM");
		p[25] = data.get("ONLINE_ASSESS_NUM");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
    }
}
