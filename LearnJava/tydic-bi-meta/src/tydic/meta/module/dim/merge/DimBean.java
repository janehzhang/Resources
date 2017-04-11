package tydic.meta.module.dim.merge;
/**
 * 维度bean,为适应导入时,能够选择本身维度表的数据
 * */
public class DimBean {
   private Long itemId; 
   private int level;
public Long getItemId() {
	return itemId;
}
public void setItemId(Long itemId) {
	this.itemId = itemId;
}
public int getLevel() {
	return level;
}
public void setLevel(int level) {
	this.level = level;
}
}
