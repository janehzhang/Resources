package tydic.portalCommon.multDimen;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FourthViewImpl implements MultViewInterface{

	@Override
	public String createJSON(String jsonModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		
		if(list.isEmpty()){
			String reXml = jsonModel.replace("{pie_data_content}", "{\"label\":\"无数据\",\"value\":\"1\",\"displayValue\":\"没找到数据\"} ");
			return reXml;
		}
		StringBuffer setBuffer = new StringBuffer();
//		String setVar = "	<set value=\"{value}\" label=\"{name}\" color=\"{color}\" displayValue=\"{displayValue}\" />";
		String setVar = "{\"label\":\"{name}\",\"value\":\"{value}\",\"displayValue\":\"{displayValue}\",\"color\":\"{color}\",\"toolText\":\"{toolText}\"}";
		int i = 0;
		
		//由于需要自定义tooltip,为了计算占比，需要记录值到list里保留，方便后续计算
		List<String> valueList = new ArrayList<String>();
		//用于存储总数据量
		double allsum = 0;
		for(Map<String, Object> map : list){
			
			String _value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("Y_OR_N"));
			if(name.equals("0")){
				name = "否";
			}else if(name.equals("1")){
				name = "是";
			}
			String ratio = String.valueOf(map.get("RATIO"));
			if(null == ratio || ratio.length() == 0){
				ratio = "0";
			}
			setBuffer.append(setVar.replace("{value}", _value).replace("{name}", name + "," + ratio)
					.replace("{displayValue}", name + "{br}" + ratio)
					.replace("{color}", ColorBox.getColor(i))
					.replace("{toolText}", "{toolText}"+i)+",");
			//记录数据，计算tooltip数据
			valueList.add(name+","+_value+",");
			allsum = allsum + Double.valueOf(_value);
			
			i++;
			
		}
		String reXml = jsonModel.replace("{pie_data_content}", setBuffer.toString().substring(0, setBuffer.toString().length()-1));

		//构造tooltip,自定义tooltip
		for(int j=0;j<valueList.size();j++){
			//取到名称和值，content的格式为name,value,
			String content = valueList.get(j);
			//分割取出值
			String value = content.split(",")[1];
			
			//计算百分比
			double proportion = Double.valueOf(value)/allsum*100;
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			String resultValue = df.format(proportion);
			//拼接tooltip内容
			content = content + resultValue+"%";
			//替换到JSON中
			reXml = reXml.replace("{toolText}"+j, content);
		}
		
		return reXml;
	}

	@Override
	public String createXML(String xmlModel, List<Map<String, Object>> list,
			Object obj) {
		FirstViewEntity entity = (FirstViewEntity)obj;
		
		if(list.isEmpty()){
			String reXml = xmlModel.replace("{pie_data_content}", "<set label=\"无数据\" value=\"1\" displayValue=\"没找到数据\" /> ");
			return reXml;
		}
		StringBuffer setBuffer = new StringBuffer();
//		String setVar = "	<set value=\"{value}\" label=\"{name}\" color=\"{color}\" displayValue=\"{displayValue}\" />";
		String setVar = "<set label=\"{name}\" value=\"{value}\" displayValue=\"{displayValue}\" color=\"{color}\" toolText=\"{toolText}\" />";
		int i = 0;
		
		//由于需要自定义tooltip,为了计算占比，需要记录值到list里保留，方便后续计算
		List<String> valueList = new ArrayList<String>();
		//用于存储总数据量
		double allsum = 0;
		for(Map<String, Object> map : list){
			
			String _value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("Y_OR_N"));
			if(name.equals("0")){
				name = "否";
			}else if(name.equals("1")){
				name = "是";
			}
			String ratio = String.valueOf(map.get("RATIO"));
			if(null == ratio || ratio.length() == 0){
				ratio = "0";
			}
			setBuffer.append(setVar.replace("{value}", _value).replace("{name}", name + "," + ratio)
					.replace("{displayValue}", name + "{br}" + ratio)
					.replace("{color}", ColorBox.getColor(i))
					.replace("{toolText}", "{toolText}"+i));
			//记录数据，计算tooltip数据
			valueList.add(name+","+_value+",");
			allsum = allsum + Double.valueOf(_value);
			
			i++;
			
		}
		String reXml = xmlModel.replace("{pie_data_content}", setBuffer.toString());

		//构造tooltip,自定义tooltip
		for(int j=0;j<valueList.size();j++){
			//取到名称和值，content的格式为name,value,
			String content = valueList.get(j);
			//分割取出值
			String value = content.split(",")[1];
			
			//计算百分比
			double proportion = Double.valueOf(value)/allsum*100;
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			String resultValue = df.format(proportion);
			//拼接tooltip内容
			content = content + resultValue+"%";
			//替换到JSON中
			reXml = reXml.replace("{toolText}"+j, content);
		}
		
		return reXml;
	}

}
