package tydic.meta.common.yhd.utils;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 颜海东
 * @description 作用:分页PO对象
 * @date 2011-9-15
 */
public class Pager {

	private static Pager instance = null;

	/**
	 * 当前页
	 */
	private int currNum =1;

	/**
	 * 每页显示条数
	 */
	private int size = 15;

	/**
	 * 总记录条数
	 */
	private int totalNum = 0;
	
	/**
	 * 总页数
	 */
	private int totalPage=0;
	
	/**
	 * 开始行
	 */
    private int startRow=0;
    
    /**
     * 结束行
     */
	private int  endRow=0;


	public Pager() {

	}
	/**
	 * 单例模式实例化
	 */
	public synchronized static Pager getInstance() {
		if (instance == null) {
			instance = new Pager();
		}
		return instance;
	}
	public Pager(int currNum, int size, int totalNum) {
		this.currNum = currNum;
		this.size = size;
		this.totalNum = totalNum;
	}
	public int getCurrNum() {
		return currNum;
	}
	public void setCurrNum(int currNum) {
		this.currNum = currNum;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
    /**
     * 求总页数
     */
	public int getTotalPage() {
		return (this.totalNum/this.size) +1;
	}
	
    /**
     * 求开始行
     */
	public int getStartRow() {
	     return  (this.currNum-1)*this.size + 1;
	}
	
    /**
     * 求结束行
     */
	public int getEndRow() {
		 return  this.currNum*this.size;
	}	
}
