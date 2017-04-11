package tydic.portalCommon.multDimen;

import java.util.List;
import java.util.Map;

public class FifthViewImpl implements MultViewInterface{

	@Override
	public String createJSON(String jsonModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		
		String type = "2";
		StringBuffer categoryBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		StringBuffer line1Buffer = new StringBuffer();
		StringBuffer line2Buffer = new StringBuffer();
		String category = "{\"label\":\"{value}\"} ";
		String value = " {\"value\":\"{category_value}\",\"color\":\"{category_color}\",\"link\":\"JavaScript:multDimenAnaly.showLD({parent}, 1, '{column}', " +type+ ");\"} ";
		String line = " {\"value\":\"{line_value}\",\"displayValue\":\"{display_value}\"} ";
		String nullLine = " {\"value\":\"\"} ";
		
		//判断是否需要下钻，不需要下钻的从界面传回is_link=false参数，同比环比不需要下钻
		//同比环比不需要下钻
		if(entity.getIs_link().equalsIgnoreCase("false")){
			value = " {\"value\":\"{category_value}\",\"color\":\"{category_color}\"} ";
		}
		
		int i = 0;
		for(Map<String, Object> map : list){
			String code = String.valueOf(map.get("CODE"));
			String _value = String.valueOf(map.get("COMPLAIN_NUM"));
			int disValue = (int) ((Integer.parseInt(_value)/2) * 0.5);
			
			String name = String.valueOf(map.get("NAME"));
			String ratio = String.valueOf(map.get("RATIO"));
			if(null == ratio || ratio.length() == 0){
				ratio = "0";
			}
			categoryBuffer.append(category.replace("{value}", name)+",");
			valueBuffer.append(value.replace("{category_value}", _value).replace("{category_color}", ColorBox.getColor(i)).replace("{parent}", code).replace("{column}", name)+",");
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
					.replace("{xAxisName}", entity.getW5_x_name())
					.replace("{yAxisName}", entity.getW5_y_name());
		}else {
			jsonModel = jsonModel.replace("{category_label_content}", "{\"label\":\"未查询到相关数据\"}")
					.replace("{dataset_data_content}", "{\"value\":\"\"}")
					.replace("{dataset_line1_content}", " {\"value\":\"\"} ")
					.replace("{dataset_line2_content}", " {\"value\":\"\"} ")
					.replace("{xAxisName}", entity.getW5_x_name())
					.replace("{yAxisName}", "");
		}


		
		return jsonModel;
	}

	public String createRateJSON(String jsonModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		StringBuffer dataBuffer = new StringBuffer();
		String dataSet = "{ \"x\":\"{x}\", \"y\":\"{y}\", \"toolText\":\"{text}\", \"color\":\"{color}\"{plotColor} }";
		String plotColor = ",\"anchorBgColor\":\"ff0000\",\"anchorBorderColor\":\"ff0000\"";
		int i = 4;
		for(Map<String, Object> map : list){
			String _value = map.get("COMPLAIN_NUM").toString();
			String name = map.get("NAME").toString();
			String ratio = map.get("RATIO").toString();
			if(entity.getCurrent_condition().equals("LOOP_RATIO")){
				ratio = String.valueOf(map.get("LOOP_RATIO"));
			}else if(entity.getCurrent_condition().equals("SAME_RATIO")){
				ratio = String.valueOf(map.get("SAME_RATIO"));
			}
			_value = ratio;
			if(null == ratio || ratio.length() == 0 || ratio.equals("null") ){
				ratio = "0.01";
				_value = "0.00";
			}
			
			//判断同比环比的数值，如果超过15%，则改点用红色表示
			String tag_color = ColorBox.getColor(1);
			int flag = 0;
			if(Math.abs(Double.valueOf(ratio))>15){
				tag_color = "#ff0000";
				flag = 1;
			}
			if(flag==1){
				dataBuffer.append(dataSet.replace("{x}", String.valueOf(i))
						.replace("{y}", ratio).replace("{text}", name + "," + _value + "%")
						.replace("{color}", tag_color).replace("{plotColor}", plotColor)).append(",");
			}else {
				dataBuffer.append(dataSet.replace("{x}", String.valueOf(i))
						.replace("{y}", ratio).replace("{text}", name + "," + _value + "%")
						.replace("{color}", tag_color).replace("{plotColor}", "")).append(",");			
			}
			
			
			
			i+=8;
		}
		
		if(!list.isEmpty()){
			jsonModel = jsonModel.replace("{data_content}", dataBuffer.substring(0, dataBuffer.length()-1));
			jsonModel = jsonModel.replace("{xAxisName}", entity.getW5_x_name()).replace("{yAxisName}", entity.getW5_y_name());
			
		}else {
			jsonModel = jsonModel.replace("{data_content}", "{\"label\":\"未查询到相关数据\"}")
					.replace("{xAxisName}", entity.getW5_x_name())
					.replace("{yAxisName}", "");
		}
		
		
		
		
		return jsonModel;
	}

	@Override
	public String createXML(String xmlModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		
		String type = "2";
		StringBuffer categoryBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		StringBuffer line1Buffer = new StringBuffer();
		StringBuffer line2Buffer = new StringBuffer();
		String category = "<category label=\"{value}\" /> ";
		String value = "<set value=\"{category_value}\" color=\"{category_color}\" link=\"JavaScript:multDimenAnaly.showLD({parent}, 1, '{column}', " +type+ ");\" />";
		String line = " <set value=\"{line_value}\" displayValue=\"{display_value}\" /> ";
		String nullLine = " <set value=\"\" /> ";
		
		//判断是否需要下钻，不需要下钻的从界面传回is_link=false参数，同比环比不需要下钻
		//同比环比不需要下钻
		if(entity.getIs_link().equalsIgnoreCase("false")){
			value = " <set value=\"{category_value}\" color=\"{category_color}\" /> ";
		}
		
		int i = 0;
		for(Map<String, Object> map : list){
			String code = String.valueOf(map.get("CODE"));
			String _value = String.valueOf(map.get("COMPLAIN_NUM"));
			int disValue = (int) ((Integer.parseInt(_value)/2) * 0.5);
			
			String name = String.valueOf(map.get("NAME"));
			String ratio = String.valueOf(map.get("RATIO"));
			if(null == ratio || ratio.length() == 0){
				ratio = "0";
			}
			categoryBuffer.append(category.replace("{value}", name));
			valueBuffer.append(value.replace("{category_value}", _value).replace("{category_color}", ColorBox.getColor(i)).replace("{parent}", code).replace("{column}", name));
			if((i+1)%2!=0){
				if(i==0){
					line1Buffer.append(line.replace("{line_value}", String.valueOf(disValue)).replace("{display_value}", ratio));
				}else{
					line1Buffer.append(nullLine);
					line1Buffer.append(line.replace("{line_value}", String.valueOf(disValue)).replace("{display_value}", ratio));
				}
			}else{
				line2Buffer.append(nullLine);
				line2Buffer.append(line.replace("{line_value}", String.valueOf(disValue)).replace("{display_value}", ratio));
			}
			i++;
			
		}
		
		String result = categoryBuffer.toString();
		if(result!=null&&!result.equalsIgnoreCase("")){
			xmlModel = xmlModel.replace("{category_label_content}", categoryBuffer.toString())
					.replace("{dataset_data_content}", valueBuffer.toString())
					.replace("{dataset_line1_content}", line1Buffer.toString())
					.replace("{dataset_line2_content}", line2Buffer.toString())
					.replace("{xAxisName}", entity.getW5_x_name())
					.replace("{yAxisName}", entity.getW5_y_name());
		}else {
			xmlModel = xmlModel.replace("{category_label_content}", "<category label=\"未查询到相关数据\" />")
					.replace("{dataset_data_content}", "<set value=\"\" />")
					.replace("{dataset_line1_content}", " <set value=\"\" /> ")
					.replace("{dataset_line2_content}", " <set value=\"\" /> ")
					.replace("{xAxisName}", entity.getW5_x_name())
					.replace("{yAxisName}", "");
		}


		
		return xmlModel;
	}
	
	public String createRateXML(String xmlModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		StringBuffer dataBuffer = new StringBuffer();
		String dataSet = "<set x=\"{x}\" y=\"{y}\" toolText=\"{text}\" color=\"{color}\"{plotColor} />";
		String plotColor = ",\"anchorBgColor\":\"ff0000\",\"anchorBorderColor\":\"ff0000\"";
		int i = 4;
		for(Map<String, Object> map : list){
			String _value = map.get("COMPLAIN_NUM").toString();
			String name = map.get("NAME").toString();
			String ratio = map.get("RATIO").toString();
			if(entity.getCurrent_condition().equals("LOOP_RATIO")){
				ratio = String.valueOf(map.get("LOOP_RATIO"));
			}else if(entity.getCurrent_condition().equals("SAME_RATIO")){
				ratio = String.valueOf(map.get("SAME_RATIO"));
			}
			_value = ratio;
			if(null == ratio || ratio.length() == 0 || ratio.equals("null") ){
				ratio = "0.01";
				_value = "0.00";
			}
			//判断同比环比的数值，如果超过15%，则改点用红色表示
			String tag_color = ColorBox.getColor(1);
			int flag = 0;
			if(Math.abs(Double.valueOf(ratio))>15){
				tag_color = "#ff0000";
				flag = 1;
			}
			if(flag==1){
				dataBuffer.append(dataSet.replace("{x}", String.valueOf(i))
						.replace("{y}", ratio).replace("{text}", name + "," + _value + "%")
						.replace("{color}", tag_color).replace("{plotColor}", plotColor));	
			}else {
				dataBuffer.append(dataSet.replace("{x}", String.valueOf(i))
						.replace("{y}", ratio).replace("{text}", name + "," + _value + "%")
						.replace("{color}", tag_color).replace("{plotColor}", ""));				
			}
			
			
			i+=8;
		}
		
		if(!list.isEmpty()){
			xmlModel = xmlModel.replace("{data_content}", dataBuffer.toString());
			xmlModel = xmlModel.replace("{xAxisName}", entity.getW5_x_name()).replace("{yAxisName}", entity.getW5_y_name());
			
		}else {
			xmlModel = xmlModel.replace("{data_content}", "<set label=\"未查询到相关数据\" />")
					.replace("{xAxisName}", entity.getW5_x_name())
					.replace("{yAxisName}", "");
		}
		
		
		
		
		return xmlModel;
	}
}
