package tydic.portalCommon.multDimen;

/**
 * 回传给JSP页面的实体，用于存储需要回传的变量、结果集、JSON数据等
 * @author Jony
 *
 */
public class ResultToWebEntity {

	@Override
	public String toString() {
		return "ResultToWebEntity [path=" + path + ", json=" + json
				+ ", rateJson=" + rateJson + ", drLevel=" + drLevel
				+ ", mapLevel=" + mapLevel + "]";
	}

	//路径，即数据查询的条件先后组合顺序，按先后顺序
	private String path = "";
	//返回给页面的JSON，用于渲染报表，是报表的数据来源
	private String json = "";
	
	private String rateJson = "" ;
	
	private String drLevel;
	
	private String mapLevel;
	
	private String json1 = "";
	private String json2 = "";
	private String json3 = "";
	private String json4 = "";
	private String json5 = "";
	
	//标题,变量命名规则：窗口名称_坐标轴_name
	private String w1_x_name = "";
	private String w1_y_name = "";
	private String w2_x_name = "一级产品";
	private String w2_y_name = "投诉量及占比";
	private String w3_x_name = "一级表象";
	private String w3_y_name = "投诉量及占比";
	private String w5_x_name = "";
	private String w5_y_name = "投诉量及投诉率";
	
	//返回前台页面的数据格式是JSON还是XML？ 0：JSON；1：XML
	private String json_xml = "0";
	
	private String renderDiv = "";
	private String chartType = "";
	
	//返回给界面的错误信息
	private String err_msg = "";
	
	private String first_param_Len = "";
	private String second_param_Len = "";
	private String third_param_Len = "";
	private String fourth_param_Len = "";
	private String fifth_param_Len = "";
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getRateJson() {
		return rateJson;
	}

	public void setRateJson(String rateJson) {
		this.rateJson = rateJson;
	}

	public String getDrLevel() {
		return drLevel;
	}

	public void setDrLevel(String drLevel) {
		this.drLevel = drLevel;
	}

	public String getMapLevel() {
		return mapLevel;
	}

	public void setMapLevel(String mapLevel) {
		this.mapLevel = mapLevel;
	}

	public String getJson1() {
		return json1;
	}

	public void setJson1(String json1) {
		this.json1 = json1;
	}

	public String getJson2() {
		return json2;
	}

	public void setJson2(String json2) {
		this.json2 = json2;
	}

	public String getJson3() {
		return json3;
	}

	public void setJson3(String json3) {
		this.json3 = json3;
	}

	public String getJson4() {
		return json4;
	}

	public void setJson4(String json4) {
		this.json4 = json4;
	}

	public String getJson5() {
		return json5;
	}

	public void setJson5(String json5) {
		this.json5 = json5;
	}

	public String getW1_x_name() {
		return w1_x_name;
	}

	public void setW1_x_name(String w1_x_name) {
		this.w1_x_name = w1_x_name;
	}

	public String getW1_y_name() {
		return w1_y_name;
	}

	public void setW1_y_name(String w1_y_name) {
		this.w1_y_name = w1_y_name;
	}

	public String getW2_x_name() {
		return w2_x_name;
	}

	public void setW2_x_name(String w2_x_name) {
		this.w2_x_name = w2_x_name;
	}

	public String getW2_y_name() {
		return w2_y_name;
	}

	public void setW2_y_name(String w2_y_name) {
		this.w2_y_name = w2_y_name;
	}

	public String getW3_x_name() {
		return w3_x_name;
	}

	public void setW3_x_name(String w3_x_name) {
		this.w3_x_name = w3_x_name;
	}

	public String getW3_y_name() {
		return w3_y_name;
	}

	public void setW3_y_name(String w3_y_name) {
		this.w3_y_name = w3_y_name;
	}

	public String getW5_x_name() {
		return w5_x_name;
	}

	public void setW5_x_name(String w5_x_name) {
		this.w5_x_name = w5_x_name;
	}

	public String getW5_y_name() {
		return w5_y_name;
	}

	public void setW5_y_name(String w5_y_name) {
		this.w5_y_name = w5_y_name;
	}

	public String getRenderDiv() {
		return renderDiv;
	}

	public void setRenderDiv(String renderDiv) {
		this.renderDiv = renderDiv;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getJson_xml() {
		return json_xml;
	}

	public void setJson_xml(String json_xml) {
		this.json_xml = json_xml;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getSecond_param_Len() {
		return second_param_Len;
	}

	public void setSecond_param_Len(String second_param_Len) {
		this.second_param_Len = second_param_Len;
	}

	public String getFirst_param_Len() {
		return first_param_Len;
	}

	public void setFirst_param_Len(String first_param_Len) {
		this.first_param_Len = first_param_Len;
	}

	public String getThird_param_Len() {
		return third_param_Len;
	}

	public void setThird_param_Len(String third_param_Len) {
		this.third_param_Len = third_param_Len;
	}

	public String getFourth_param_Len() {
		return fourth_param_Len;
	}

	public void setFourth_param_Len(String fourth_param_Len) {
		this.fourth_param_Len = fourth_param_Len;
	}

	public String getFifth_param_Len() {
		return fifth_param_Len;
	}

	public void setFifth_param_Len(String fifth_param_Len) {
		this.fifth_param_Len = fifth_param_Len;
	}

	
	
}
