
/**   
 * @文件名: PublicUse.java
 * @包 tydic.portalCommon
 * @描述: 
 * @author qx@tydic.com
 * @创建日期 2014-2-24 下午04:37:17
 *  
 */
  
package tydic.portalCommon;
/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：PublicUse   
 * 类描述：   
 * 创建人：qx@tydic.com
 * 创建时间：2014-2-24 下午04:37:17 
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class PublicUse {	
	/**
	 * 最大值
	 */
	public static double compareMaxValue(double maxVal, double curVal) {
		if (curVal > maxVal) {
			maxVal = curVal;
		}
		return maxVal;
	}
	/**
	 * 最小值
	 */
	public static double compareMinValue(double minVal, double curVal) {
		if (minVal == 0 && curVal != 0) {
			minVal = curVal;
		} else if (curVal < minVal) {
			minVal = curVal;
		}
		return minVal;
	}
}
