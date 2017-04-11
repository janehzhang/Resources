package tydic.meta.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 作用:分页PO对象
 * @date 2011-9-15
 */
public class Page {

	private static Page instance = null;
	
    /**
     * 起始行数
     */
    private int posStart=0;
    /**
     * 每页记录数
     */
    private int count=15;

    public Page() {
    
    }
   /**
    * 单例模式实例化
    */
   public synchronized static Page getInstance() {
		if (instance == null) {
			instance = new Page();
		}
		return instance;
	}
   
    public Page(int posStart, int count) {
        this.posStart = posStart;
        this.count = count;
    }

    public int getPosStart() {
        return posStart;
    }

    public void setPosStart(int posStart) {
        this.posStart = posStart;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    /**
     * @author qx
     * @description 分页
     * @param params
     */
    public Map getPageInfo(Map params){
     Map page = new HashMap() ;
     int start,end;
     //数据总数
     int totalSize =Integer.parseInt(params.get("totalSize").toString());
     //当前页面 从0开始
     int pageIndex =Integer.parseInt(params.get("pageIndex").toString());
     //页面展现数据数量
     int pageSize =Integer.parseInt(params.get("pageSize").toString());
     pageSize = (pageSize < 1) ? 20 : pageSize;
     
     int pageCount = totalSize/pageSize+1;
     if (totalSize <= 0) {
      pageCount = 0;
      start = 0;
      end = pageSize;
      pageIndex = 0;
      totalSize = 0;
     }else{
      pageCount = totalSize / pageSize;
      pageCount = (totalSize % pageSize == 0) ? pageCount : pageCount + 1;
      pageIndex = (pageIndex < 0) ? 0 : pageIndex;
      pageIndex = (pageIndex >= pageCount) ? pageIndex - 1 : pageIndex;
      // 计算开始、结束记录行号
      start = pageIndex * pageSize + 1;
      end = (pageIndex + 1) * pageSize;
     }
     page.put("pageCount", pageCount);
     page.put("start", start);
     page.put("end", end);
     page.put("pageNo", pageIndex);
     page.put("total", totalSize);
     page.put("start", start);
     page.put("end", end);
     return page;
    }
}
