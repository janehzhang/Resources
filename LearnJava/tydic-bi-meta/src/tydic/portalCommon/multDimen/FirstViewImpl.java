package tydic.portalCommon.multDimen;

import java.util.List;
import java.util.Map;

public class FirstViewImpl implements MultViewInterface{

	public String createChartData(String jsonModel, List<Map<String, Object>> list,
			Object obj,String dataType){
		String data = "";
		if(dataType.equalsIgnoreCase("0")){
			data = createJSON(jsonModel,list,obj);
		}else {
			data = createXML(jsonModel,list,obj);
		}
		
		return data;
	}
	
	
	@Override
	public String createJSON(String jsonModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		String chartType = entity.getChartType();
		//存在一个BUG，有时候chartType会为null，目前暂时找不到问题，当发现为null时暂时设置为2
		if(chartType==null||chartType.equalsIgnoreCase("")){
			chartType = "3";
		}
		//2D柱状图
		if(chartType.equalsIgnoreCase("3")){
//			jsonModel = createSingleColumnChartJson(jsonModel,list,obj);
			jsonModel = createCombiJSON(jsonModel,list,obj);
		}else if (chartType.equalsIgnoreCase("1")) {//地图
			jsonModel = createMscombi2dChartJson(jsonModel,list,obj);
		}else {
			return "ERROR JSON!!";
		}
		
		return jsonModel;
	}
	
	
	public String createXML(String jsonModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		String chartType = entity.getChartType();
		//存在一个BUG，有时候chartType会为null，目前暂时找不到问题，当发现为null时暂时设置为2
		if(chartType==null){
			chartType = "2";
		}
		//2D柱状图
		if(chartType.equalsIgnoreCase("2")){
			jsonModel = createSingleColumnChartXml(jsonModel,list,obj);
		}else if (chartType.equalsIgnoreCase("1")) {//地图
			jsonModel = createMscombi2dChartXml(jsonModel,list,obj);
		}else {
			return "ERROR JSON!!";
		}
		
		return jsonModel;
	}
	
	//2D柱状图，配置SQL:select t.*,rowid from TB_CONF_MULTVIEW_JSON t where id= 2;
	private String createSingleColumnChartJson(String jsonModel, List<Map<String, Object>> list,
			Object obj){
		FirstViewEntity entity = (FirstViewEntity)obj;
		
		StringBuffer setBuffer = new StringBuffer();
//		String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" link=\"JavaScript:multDimenAnaly.showMap({code}, '{name}');\" /> ";
		String setVar = " { \"label\":\"{name}\",\"value\":\"{value}\",\"color\":\"{color}\",\"link\":\"JavaScript:multDimenAnaly.getSubAreData({parent}, {level});\"}";
		
		//处理放大镜请求，设置某些参数为默认值
		if(entity.getWindow_id()==1){
			setVar = " { \"label\":\"{name}\",\"value\":\"{value}\",\"color\":\"{color}\"}";
			entity.setMap_level("1");
		}
				
		int i = 0;
		for(Map<String, Object> map : list){
			String value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("NAME"));
			String code =  String.valueOf(map.get("CODE"));
			setBuffer.append(setVar.replace("{name}", name).replace("{value}", value)
					.replace("{color}", ColorBox.getColor(i))
					.replace("{parent}", code)
					.replace("{level}", entity.getMap_level())+",");
			i++;
		}
		String result = setBuffer.toString();
		if(result!=null&&!result.equalsIgnoreCase("")){
			result = result.substring(0, result.length()-1);
			jsonModel = jsonModel.replace("{data_content}", result).replace("{xAxisName}", entity.getW1_x_name()).replace("{yAxisName}", entity.getW1_y_name());			
		}else {
			jsonModel = jsonModel.replace("{data_content}", "{ \"label\":\"未查询到相关数据\",\"value\":\"\"}");
			jsonModel = jsonModel.replace("{xAxisName}", entity.getW1_x_name()).replace("{yAxisName}", "");
		}
		
		return jsonModel;
	}
	
	//地图，配置SQL:select t.*,rowid from TB_CONF_MULTVIEW_JSON t where id= 1;
	private String createMscombi2dChartJson(String jsonModel, List<Map<String, Object>> list,
			Object obj){
		FirstViewEntity entity = (FirstViewEntity)obj;
		
		for(Map<String, Object> map : list){
			String code = String.valueOf(map.get("CODE"));
			String value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("NAME"));
			String ratio =  String.valueOf(map.get("RATIO"));
			if(null == ratio || ratio.length() == 0){
				ratio = "0";
			}
			if(code.equals("0001")){
				jsonModel = jsonModel.replace("{toolText0001}", name+"{br}"+value+","+ratio).replace("{value0001}", ratio);
			}else if(code.equals("0002")){
				jsonModel = jsonModel.replace("{toolText0002}", name+"{br}"+value+","+ratio).replace("{value0002}", ratio);
			}else if(code.equals("0003")){
				jsonModel = jsonModel.replace("{toolText0003}", name+"{br}"+value+","+ratio).replace("{value0003}", ratio);
			}else if(code.equals("0004")){
				jsonModel = jsonModel.replace("{toolText0004}", name+"{br}"+value+","+ratio).replace("{value0004}", ratio);
			}else if(code.equals("0005")){
				jsonModel = jsonModel.replace("{toolText0005}", name+"{br}"+value+","+ratio).replace("{value0005}", ratio);
			}
		}
		
		jsonModel = jsonModel.replace("{toolText0001}", "无数据").replace("{value0001}", "无数据");
		jsonModel = jsonModel.replace("{toolText0002}", "无数据").replace("{value0002}", "无数据");
		jsonModel = jsonModel.replace("{toolText0003}", "无数据").replace("{value0003}", "无数据");
		jsonModel = jsonModel.replace("{toolText0004}", "无数据").replace("{value0004}", "无数据");
		jsonModel = jsonModel.replace("{toolText0005}", "无数据").replace("{value0005}", "无数据");
		
		return jsonModel;

	}
	
	
	public String createCombiJSON(String jsonModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		
		String type = "2";
		StringBuffer categoryBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		StringBuffer line1Buffer = new StringBuffer();
		StringBuffer line2Buffer = new StringBuffer();
		String category = "{\"label\":\"{value}\"} ";
		String value = " {\"value\":\"{category_value}\",\"color\":\"{category_color}\",\"link\":\"JavaScript:multDimenAnaly.getSubAreData({parent}, {level});\"} ";
		String line = " {\"value\":\"{line_value}\",\"displayValue\":\"{display_value}\"} ";
		String nullLine = " {\"value\":\"\"} ";
		
		//判断是否需要下钻，不需要下钻的从界面传回is_link=false参数，同比环比不需要下钻
		//同比环比不需要下钻
		if(entity.getIs_link().equalsIgnoreCase("false")){
			value = " {\"value\":\"{category_value}\",\"color\":\"{category_color}\"} ";
		}
		//处理放大镜请求，设置某些参数为默认值
		if(entity.getWindow_id()==1){
			value = " {\"value\":\"{category_value}\",\"color\":\"{category_color}\"} ";
			entity.setDr_level("1");
			entity.setMap_level("1");
		}
		
		int i = 0;
		for(Map<String, Object> map : list){
			String code = String.valueOf(map.get("CODE"));
			String _value = String.valueOf(map.get("COMPLAIN_NUM"));
			int disValue = (int) ((Integer.parseInt(_value)/2) * 0.5);
			
			String name = String.valueOf(map.get("NAME"));
			String ratio = String.valueOf(map.get("RATIO")).replace("‰", "");
			if(null == ratio || ratio.length() == 0){
				ratio = "0";
			}
			categoryBuffer.append(category.replace("{value}", name)+",");
			valueBuffer.append(value.replace("{category_value}", _value).replace("{category_color}", ColorBox.getColor(i)).replace("{parent}", code).replace("{level}", entity.getMap_level())+",");
			if((i+1)%2!=0){
				if(i==0){
					line1Buffer.append(line.replace("{line_value}", String.valueOf(disValue)).replace("{display_value}", ratio)+",");
				}else{
					line1Buffer.append(nullLine+",");
					line1Buffer.append(line.replace("{line_value}", String.valueOf(disValue)).replace("{display_value}", ratio)+",");
				}
			}else{
				line2Buffer.append(nullLine+",");
				line2Buffer.append(line.replace("{line_value}", String.valueOf(disValue)).replace("{display_value}", ratio)+",");
			}
			i++;
			
		}
		
		String result = categoryBuffer.toString();
		if(result!=null&&!result.equalsIgnoreCase("")){
			jsonModel = jsonModel.replace("{category_label_content}", categoryBuffer.toString().substring(0, categoryBuffer.toString().length()-1))
					.replace("{dataset_data_content}", valueBuffer.toString().substring(0, valueBuffer.length()>0?valueBuffer.toString().length()-1:0))
					.replace("{dataset_line1_content}", line1Buffer.toString().substring(0, line1Buffer.length()>0?line1Buffer.toString().length()-1:0))
					.replace("{dataset_line2_content}", line2Buffer.toString().substring(0, line2Buffer.length()>0?line2Buffer.toString().length()-1:0))
					.replace("{xAxisName}", entity.getW1_x_name())
					.replace("{yAxisName}", entity.getW1_y_name());
		}else {
			jsonModel = jsonModel.replace("{category_label_content}", "{\"label\":\"未查询到相关数据\"}")
					.replace("{dataset_data_content}", "{\"value\":\"\"}")
					.replace("{dataset_line1_content}", " {\"value\":\"\"} ")
					.replace("{dataset_line2_content}", " {\"value\":\"\"} ")
					.replace("{xAxisName}", entity.getW1_x_name())
					.replace("{yAxisName}", "");
		}
		
		
		
		
		return jsonModel;
	}

	
	//2D柱状图，配置SQL:select t.*,rowid from TB_CONF_MULTVIEW_JSON t where id= 2;
		private String createSingleColumnChartXml(String xmlModel, List<Map<String, Object>> list,
				Object obj){
			FirstViewEntity entity = (FirstViewEntity)obj;
			
			StringBuffer setBuffer = new StringBuffer();
//			String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" link=\"JavaScript:multDimenAnaly.showMap({code}, '{name}');\" /> ";
			String setVar = " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" link=\"JavaScript:multDimenAnaly.getSubAreData({parent}, {level});\" />";
			
			//处理放大镜请求，设置某些参数为默认值
			if(entity.getWindow_id()==1){
				setVar = " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" />";
				entity.setMap_level("1");
			}
					
			int i = 0;
			for(Map<String, Object> map : list){
				String value = String.valueOf(map.get("COMPLAIN_NUM"));
				String name =  String.valueOf(map.get("NAME"));
				String code =  String.valueOf(map.get("CODE"));
				setBuffer.append(setVar.replace("{name}", name).replace("{value}", value)
						.replace("{color}", "#"+ColorBox.getColor(i))
						.replace("{parent}", code)
						.replace("{level}", entity.getMap_level()));
				i++;
			}
			String result = setBuffer.toString();
			if(result!=null&&!result.equalsIgnoreCase("")){
				result = result.substring(0, result.length()-1);
				xmlModel = xmlModel.replace("{data_content}", result).replace("{xAxisName}", entity.getW1_x_name()).replace("{yAxisName}", entity.getW1_y_name());			
			}else {
				xmlModel = xmlModel.replace("{data_content}", "<set label=\"未查询到相关数据\" value=\"\" />");
				xmlModel = xmlModel.replace("{xAxisName}", entity.getW1_x_name()).replace("{yAxisName}", "");
			}
			
			return xmlModel;
		}
		
		
		//地图，配置SQL:select t.*,rowid from TB_CONF_MULTVIEW_JSON t where id= 1;
		private String createMscombi2dChartXml(String xmlModel, List<Map<String, Object>> list,
				Object obj){
			FirstViewEntity entity = (FirstViewEntity)obj;
			
			for(Map<String, Object> map : list){
				String code = String.valueOf(map.get("CODE"));
				String value = String.valueOf(map.get("COMPLAIN_NUM"));
				String name =  String.valueOf(map.get("NAME"));
				String ratio =  String.valueOf(map.get("RATIO"));
				if(null == ratio || ratio.length() == 0){
					ratio = "0";
				}
				if(code.equals("0001")){
					xmlModel = xmlModel.replace("{toolText0001}", name+"\n"+value+","+ratio).replace("{value0001}", ratio);
				}else if(code.equals("0002")){
					xmlModel = xmlModel.replace("{toolText0002}", name+"\n"+value+","+ratio).replace("{value0002}", ratio);
				}else if(code.equals("0003")){
					xmlModel = xmlModel.replace("{toolText0003}", name+"\n"+value+","+ratio).replace("{value0003}", ratio);
				}else if(code.equals("0004")){
					xmlModel = xmlModel.replace("{toolText0004}", name+"\n"+value+","+ratio).replace("{value0004}", ratio);
				}else if(code.equals("0005")){
					xmlModel = xmlModel.replace("{toolText0005}", name+"\n"+value+","+ratio).replace("{value0005}", ratio);
				}
			}
			
			xmlModel = xmlModel.replace("{toolText0001}", "无数据").replace("{value0001}", "无数据");
			xmlModel = xmlModel.replace("{toolText0002}", "无数据").replace("{value0002}", "无数据");
			xmlModel = xmlModel.replace("{toolText0003}", "无数据").replace("{value0003}", "无数据");
			xmlModel = xmlModel.replace("{toolText0004}", "无数据").replace("{value0004}", "无数据");
			xmlModel = xmlModel.replace("{toolText0005}", "无数据").replace("{value0005}", "无数据");
			
			return xmlModel;

		}

}
