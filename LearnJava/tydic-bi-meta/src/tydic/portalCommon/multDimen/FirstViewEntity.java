package tydic.portalCommon.multDimen;

public class FirstViewEntity {
	
	private String date = "";
	private String current_condition ;
	private String map_level;
	private String dr_level;
	private int type1 ;
	private int type2 ;
	private int type3 ;
	private int type4 ;
	private int type5 ;
	private String chartType ;
	private String is_top5 = "true";
	private String chartContainer = "";
	
	private int fallbackWindow;
	//暂无其他用处，此处只是为了方便日志打印的观察而设置
	private String sql_flag = "";
	
	//用于处理放大镜请求
	private int window_id = 0;
	//标题,变量命名规则：窗口名称_坐标轴_name
	private String w1_x_name = "";
	private String w1_y_name = "投诉量及投诉率";
	private String w2_x_name = "一级产品";
	private String w2_y_name = "投诉量及占比";
	private String w3_x_name = "一级表象";
	private String w3_y_name = "投诉量及占比";
	private String w5_x_name = "按省公司";
	private String w5_y_name = "投诉量及投诉率";

	private String is_link = "true";
	
	//回退操作，该参数用于决定代码执行哪个窗口的回退操作
	private int which_first = 2;
	
	//用于处理识别是哪个窗口发起的操作
	private int view_id = 0;
	
	
	public int getType1() {
		return type1;
	}
	public void setType1(int type1) {
		this.type1 = type1;
	}
	public int getType2() {
		return type2;
	}
	public void setType2(int type2) {
		this.type2 = type2;
	}
	public int getType3() {
		return type3;
	}
	public void setType3(int type3) {
		this.type3 = type3;
	}
	public int getType4() {
		return type4;
	}
	public void setType4(int type4) {
		this.type4 = type4;
	}
	public int getType5() {
		return type5;
	}
	public void setType5(int type5) {
		this.type5 = type5;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getCurrent_condition() {
		return current_condition;
	}
	public void setCurrent_condition(String current_condition) {
		this.current_condition = current_condition;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public String getIs_top5() {
		return is_top5;
	}
	public void setIs_top5(String is_top5) {
		this.is_top5 = is_top5;
	}
	public String getChartContainer() {
		return chartContainer;
	}
	public void setChartContainer(String chartContainer) {
		this.chartContainer = chartContainer;
	}
	public String getMap_level() {
		return map_level;
	}
	public void setMap_level(String map_level) {
		this.map_level = map_level;
	}
	public String getDr_level() {
		return dr_level;
	}
	public void setDr_level(String dr_level) {
		this.dr_level = dr_level;
	}
	public String getSql_flag() {
		return sql_flag;
	}
	public void setSql_flag(String sql_flag) {
		this.sql_flag = sql_flag;
	}
	public int getFallbackWindow() {
		return fallbackWindow;
	}
	public void setFallbackWindow(int fallbackWindow) {
		this.fallbackWindow = fallbackWindow;
	}
	public int getWindow_id() {
		return window_id;
	}
	public void setWindow_id(int window_id) {
		this.window_id = window_id;
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
	public String getIs_link() {
		return is_link;
	}
	public void setIs_link(String is_link) {
		this.is_link = is_link;
	}
	public int getWhich_first() {
		return which_first;
	}
	public void setWhich_first(int which_first) {
		this.which_first = which_first;
	}
	public int getView_id() {
		return view_id;
	}
	public void setView_id(int view_id) {
		this.view_id = view_id;
	}

	
	
	
}
