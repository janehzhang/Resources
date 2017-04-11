
/**   
 * @文件名: Pagination.java
 * @包 tydic.portalCommon
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-6-11 下午03:10:41
 *  
 */
  
package tydic.portalCommon;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：Pagination   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-6-11 下午03:10:41   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class Pagination {
	/**
	 * 
			* getPagesCount 获取总页数
			* @param pageSize 每页显示记录数
			* @param countSum 记录总数
			* @param @return 设定文件
			* @return int
			* @Exception 异常对象
			* @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public int getPagesCount(int countSum, int pageSize){
		int pagescount = (int) Math.ceil((double) countSum / pageSize);//求总页数，ceil（num）取整不小于num
		return pagescount;
	}
	/**
	 * 
			* getPageCodeNum 获取页码数
			* @param pagescount 总页数
			* @param @return 设定文件
			* @return int
			* @Exception 异常对象
			* @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public int getPageCodeNum(int pagescount, int pages){
		if(pagescount < pages){
            pages = pagescount;//如果分页变量大总页数，则将分页变量设计为总页数
        }
        if(pages < 1){
            pages = 1;//如果分页变量小于１,则将分页变量设为１
        }
		return pages;
	}
	/**
	 * 
			* getBeginDisTag 获取页码标签（1 2 3 4 ...）的开始标签数
			* @param pages 页码数
			* @param maxTagNum 最大标签数
			* @param @return 设定文件
			* @return int
			* @Exception 异常对象
			* @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public int getBeginDisTag(int pages, int maxTagNum){
		int listbegin = (pages - (int) Math.ceil((double) maxTagNum / 2));//从第几页开始显示分页信息
        if(listbegin < 1){
            listbegin = 1;
        }
		return listbegin;
	}
	/**
	 * 
			* getEndDisTag 获取页码标签（1 2 3 4 ...）的结束标签数
			* @param pages 页码数
			* @param maxTagNum 最大标签数
			* @param pagescount 总页数
			* @param @return 设定文件
			* @return int
			* @Exception 异常对象
			* @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public int getEndDisTag(int pages, int maxTagNum, int pagescount){
		 int listend = pages + maxTagNum/2;//分页信息显示到第几页
         if(listend > pagescount){
             listend = pagescount + 1;
         }
         return listend;
	}
	/**
	 * 
			* getDisDataBeginPage 数据从第几页展示
			* @param pages 页码数
			* @param pageSize 页面展示数据的条数
			* @param @return 设定文件
			* @return int
			* @Exception 异常对象
			* @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public int getDisDataBeginPage(int pages, int pageSize){
		int recordbegin = (pages - 1) * pageSize;//起始记录
		return recordbegin;
	}
	/**
	 * 
			* getDisDataEndPage 数据展示最后一页数
			* @param pages 页码数
			* @param pageSize 页面展示数据的条数
			* @param countSum 数据总条数
			* @param pagescount 总页数
			* @param @return 设定文件
			* @return int
			* @Exception 异常对象
			* @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public int getDisDataEndPage(int recordbegin, int pageSize, int countSum, int pages, int pagescount){
		int recordend = 0;
        recordend = recordbegin + pageSize;
        //最后一页记录显示处理
        if (pages == pagescount) {
            recordend = (int) (recordbegin + pageSize * (countSum % pageSize) * 0.1);
        }
        return recordend;
	}
}
