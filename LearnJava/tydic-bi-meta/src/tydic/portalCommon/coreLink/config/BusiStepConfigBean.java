package tydic.portalCommon.coreLink.config;

import java.io.Serializable;

public class BusiStepConfigBean implements Serializable {

	private static final long serialVersionUID = 2370516293458038954L;

	private String id;
	
	private String name;
	
	private String parentId;
	
	private Double weight;// 环节权重
	
	private Double percentate;// 环节比重
	
	private Double direction;// 指标指向
	
	private Double level;
	
	private String flag;

    private int    leaf;
  
    public BusiStepConfigBean(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getPercentate() {
		return percentate;
	}

	public void setPercentate(Double percentate) {
		this.percentate = percentate;
	}

	public Double getDirection() {
		return direction;
	}

	public void setDirection(Double direction) {
		this.direction = direction;
	}

	public Double getLevel() {
		return level;
	}

	public void setLevel(Double level) {
		this.level = level;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getLeaf() {
		return leaf;
	}

	public void setLeaf(int leaf) {
		this.leaf = leaf;
	}   
	
}
