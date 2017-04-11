package tydic.portalCommon.multDimen;

import java.util.List;
import java.util.Map;

public interface MultViewInterface {

	//将JSON模板从模板转换成JSON数据流
	public String createJSON(String jsonModel,List<Map<String, Object>> list,Object obj);
	
	//将xml模板转换成xml格式数据
	public String createXML(String xmlModel,List<Map<String, Object>> list,Object obj);
}
