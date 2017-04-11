
/**   
 * @文件名: ReportParseDAO.java
 * @包 tydic.meta.module.report.parse
 * @描述:
 * @author wuxl@tydic.com
 * @创建日期 2012-4-10 下午05:10:33
 *  
 */
  
package tydic.meta.module.report.parse;

import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportParseDAO   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-10 下午05:10:33   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportParseDAO extends MetaBaseDAO {
	/**
     * 获取包含报表转换编码的数据
     * @param sql Object[][]
     * @return
     */
	public Object[][] getReportDataByTranCodeComplete(String sql,long dataSourceId){
		return getDataAccess(dataSourceId+"").queryForDataTable(sql).rows;
	}
	/**
     * 获取不包含报表转换编码的数据
     * @param sql Object[][]
     * @return
     */
	public Object[][] getReportData(String sql,long dataSourceId){
		return getDataAccess(dataSourceId+"").queryForDataTable(sql).rows;
	}
}
