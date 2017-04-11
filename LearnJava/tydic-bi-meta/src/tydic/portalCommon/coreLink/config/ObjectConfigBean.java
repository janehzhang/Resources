package tydic.portalCommon.coreLink.config;

import java.io.Serializable;

public class ObjectConfigBean implements Serializable {

	private static final long serialVersionUID = -7919400023492090320L;

	private String step1Id;

	private String step1Name;
	
	private Double weight1;

	private String step2Id="";

	private String step2Name="";
	
	private Double percentate2=0d;
	
	private Double weight2=0d;
	
	private String step3Id="";

	private String step3Name="";
	
	private Double direction3=0d;
	
	private Double weight3=0d;
	
    public ObjectConfigBean(){}
    
	
    
    public String getStep1Id() {
		return step1Id;
	}

	public void setStep1Id(String step1Id) {
		this.step1Id = step1Id;
	}

	public String getStep1Name() {
		return step1Name;
	}

	public void setStep1Name(String step1Name) {
		this.step1Name = step1Name;
	}

	public Double getWeight1() {
		return weight1;
	}

	public void setWeight1(Double weight1) {
		this.weight1 = weight1;
	}

	public String getStep2Id() {
		return step2Id;
	}

	public void setStep2Id(String step2Id) {
		this.step2Id = step2Id;
	}

	public String getStep2Name() {
		return step2Name;
	}

	public void setStep2Name(String step2Name) {
		this.step2Name = step2Name;
	}

	public Double getPercentate2() {
		return percentate2;
	}

	public void setPercentate2(Double percentate2) {
		this.percentate2 = percentate2;
	}

	public Double getWeight2() {
		return weight2;
	}

	public void setWeight2(Double weight2) {
		this.weight2 = weight2;
	}

	public String getStep3Id() {
		return step3Id;
	}

	public void setStep3Id(String step3Id) {
		this.step3Id = step3Id;
	}

	public String getStep3Name() {
		return step3Name;
	}

	public void setStep3Name(String step3Name) {
		this.step3Name = step3Name;
	}

	public Double getDirection3() {
		return direction3;
	}

	public void setDirection3(Double direction3) {
		this.direction3 = direction3;
	}

	public Double getWeight3() {
		return weight3;
	}

	public void setWeight3(Double weight3) {
		this.weight3 = weight3;
	}
    

}
