package tydic.portalCommon.multDimen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author 孙浩瑞
 *
 * 2016-5-24 上午9:46:47
 */
public class MultViewAnalyAction {
	private MultViewAnalyDAO multViewAnalyDAO;

	private static Map<String, String> sqlConfMap = new HashMap<String, String>();
	private static Map<String, String> jsonConfMap = new HashMap<String, String>();
	
	private static List<Map<String, String>> firstParam = new ArrayList<Map<String, String>>();
	private static List<Map<String, String>> secondParam = new ArrayList<Map<String, String>>();
	private static List<Map<String, String>> thirdParam = new ArrayList<Map<String, String>>();
	private static List<Map<String, String>> fourthParam = new ArrayList<Map<String, String>>();
	private static List<Map<String, String>> fifthParam = new ArrayList<Map<String, String>>();
	
	private static List<String> firstHistory = new ArrayList<String>();
	private static List<String> secondHistory = new ArrayList<String>();
	private static List<String> thirdHistory = new ArrayList<String>();
	private static List<String> fourthHistory = new ArrayList<String>();
	private static List<String> fifthHistory = new ArrayList<String>();
	
	
	private static String firstSql = "";
	private static String secondSql = "";
	private static String thirdSql = "";
	private static String fourthSql = "";
	private static String fifthSql = "";
	
	private static List<Map<String, String>> dicList = new ArrayList<Map<String,String>>();
	
	
	public MultViewAnalyDAO getMultDimenAnalyDAO() {
		return multViewAnalyDAO;
	}

	public void setMultDimenAnalyDAO(MultViewAnalyDAO multViewAnalyDAO) {
		this.multViewAnalyDAO = multViewAnalyDAO;
	}
	
	/**
	 * fusioncharts图表数据提供
	 * @return
	 */
	public String getPicXmlData(String timeType, int type,String xName,String yName){
		List<Map<String, Object>> list = multViewAnalyDAO.getData(timeType, type, null);
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 考核维度 -下钻联动数据
	 * @return
	 */
	public String getLdAsseData(String timeType, int type, String parent, String xName,String yName){
		List<Map<String, Object>> list = multViewAnalyDAO.getData(timeType, type, parent);
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 考核维度 -饼图联动数据
	 * @return
	 */
	public String getPieAsseData(String timeType, int type,String xName,String yName, String screen){
		List<Map<String, Object>> list = multViewAnalyDAO.getLDData(timeType, type, screen.replace("aa", "a"));
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 客户属性-饼图联动数据
	 * @return
	 */
	public String getPicLDData(String timeType, int type,String xName,String yName, String screen){
		List<Map<String, Object>> list = multViewAnalyDAO.getLDData(timeType, type, screen.replace("aa", "a"));
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 同比跟环比的数据
	 * @param timeType
	 * @param type
	 * @return
	 */
	public Map<String, String> getPicRatioData(String timeType, int type, String ratioType,String xName,String yName){
		
		Map<String, String> map = new HashMap<String, String>();
		
		List<Map<String, Object>> list = multViewAnalyDAO.getData(timeType, type, null);
		map.put("pic", serPicAxis(list, type, buildTitle(type),xName,yName));
		map.put("ratio", serRatioAxis(list, ratioType));
		return map;
	}
	
	/**
	 * fusioncharts地图数据提供
	 * @return
	 */
	public String getMapXmlData(String timeType, int type){
		
		List<Map<String, Object>> list = multViewAnalyDAO.getData(timeType, type, null);
		String xml = this.serMapAxis(list);
		return xml;
	}
	
	/**
	 * fusioncharts饼图数据提供
	 * @return
	 */
	public String getPieXmlData(String timeType, int type, String screen){
		List<Map<String, Object>> list = multViewAnalyDAO.getPieData(timeType, type, screen);
		return serPieAxis(list);
	}
	
	/**
	 * 钻取
	 * @param list
	 * @return
	 */
	public String getDrillXmlData(String timeType, int type, String parentId, int level, String xName){
		List<Map<String, Object>> list = multViewAnalyDAO.getDrillData(timeType, type, parentId, level);
		return serDrillAxis(list, parentId, level, xName);
	}

	/**
	 * 查询所有数据
	 * @param list
	 * @return
	 */
	public String getAllXmlData(String timeType, int type,String xName,String yName){
		List<Map<String, Object>> list = multViewAnalyDAO.getData(timeType, type, null);
		return serAllAxis(list,xName,yName);
	}
	
	/**
	 * 查询片区数据
	 * @param list
	 * @return
	 */
	public String getMap1XmlData(String timeType, String parent,String xName,String yName){
		List<Map<String, Object>> list = multViewAnalyDAO.getMapData(timeType, parent);
		return serMap1Axis(list,xName,yName);
	}
	
	/**
	 * 查询县级数据
	 * @param list
	 * @return
	 */
	public String getMapCountyData(String timeType, String code,String xName){
		List<Map<String, Object>> list = multViewAnalyDAO.getCountyData(timeType, code);
		return serCountyAxis(list,xName);
	}
	
	private String serPicAxis(List<Map<String, Object>> list, int type, String title,String xName,String yName){
		StringBuffer categoryBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		StringBuffer line1Buffer = new StringBuffer();
		StringBuffer line2Buffer = new StringBuffer();
		String category = " <category label=\"{name}\" /> ";
		String value = " <set value=\"{value}\" color=\"{color}\"  link=\"JavaScript:multDimenAnaly.showLD({parent}, 1, '{column}', " +type+ ");\" /> ";
		String line = " <set value=\"{value}\" displayValue=\"{display}\" alpha=\"100\" /> ";
		String nullLine = " <set value=\"\"/> ";
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
			categoryBuffer.append(category.replace("{name}", name));
			valueBuffer.append(value.replace("{value}", _value).replace("{color}", getColor(i)).replace("{parent}", code).replace("{column}", name));
			if((i+1)%2!=0){
				if(i==0){
					line1Buffer.append(line.replace("{value}", String.valueOf(disValue)).replace("{display}", ratio));
				}else{
					line1Buffer.append(nullLine);
					line1Buffer.append(line.replace("{value}", String.valueOf(disValue)).replace("{display}", ratio));
				}
			}else{
				line2Buffer.append(nullLine);
				line2Buffer.append(line.replace("{value}", String.valueOf(disValue)).replace("{display}", ratio));
			}
			i++;
			
		}
		String reXml = xml.replace("{category}", categoryBuffer.toString())
				.replace("{set_value}", valueBuffer.toString())
				.replace("{set_line1}", line1Buffer.toString())
				.replace("{set_line2}", line2Buffer.toString())
				.replace("{xAxisName}", xName)
				.replace("{yAxisName}", yName);
		return reXml;
	}
	
	private String serRatioAxis(List<Map<String, Object>> list, String ratioType){
		StringBuffer lineBuffer = new StringBuffer();
		String set = "        <set y=\"{value}\" x=\"{x}\" toolText=\"{text}\" color=\"{color}\"   /> ";
		int i = 20;
		int i1 = 0;
		for(Map<String, Object> map : list){
			
			String _value = map.get("COMPLAIN_NUM").toString();
			String name = map.get("NAME").toString();
			String ratio = map.get("RATIO").toString();
			if(ratioType.equals("LOOP_RATIO")){
				ratio = String.valueOf(map.get("LOOP_RATIO"));
			}else if(ratioType.equals("SAME_RATIO")){
				ratio = String.valueOf(map.get("SAME_RATIO"));
			}
			_value = ratio;
			if(null == ratio || ratio.length() == 0 || ratio.equals("null") ){
				ratio = "0.01";
				_value = "0.00";
			}
			lineBuffer.append(set.replace("{value}", ratio).replace("{text}", name + "," + _value + "%").replace("{x}", String.valueOf(i)).replace("{color}", getColor(i1)));
			i+=11;
			
		}
		String reXml = this.ratioXml.replace("{set_line}", lineBuffer.toString());
		return reXml;
	}
	
	private String serPieAxis(List<Map<String, Object>> list){
		if(list.isEmpty()){
			String reXml = this.pieXml.replace("{set}", "	<set value=\"1\" label=\"无数据\" displayValue=\"没找到数据\" />");
			return reXml;
		}
		StringBuffer setBuffer = new StringBuffer();
		String setVar = "	<set value=\"{value}\" label=\"{name}\" color=\"{color}\" displayValue=\"{displayValue}\" />";
		int i = 0;
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
			setBuffer.append(setVar.replace("{value}", _value).replace("{name}", name + "," + ratio).replace("{displayValue}", name + "\n" + ratio).replace("{color}", getColor(i)));
			i++;
			
		}
		String reXml = this.pieXml.replace("{set}", setBuffer.toString());
		return reXml;
	}
	
	private String serDrillAxis(List<Map<String, Object>> list, String parentId, int level, String x_Name){
		StringBuffer setBuffer = new StringBuffer();
		String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" link=\"JavaScript:multDimenAnaly.showLD({parent}, "+level+", '{xName}');\" /> ";
		//String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" link=\"JavaScript:multDimenAnaly.show({parent}, "+level+",'qwert222');\" /> ";

		int i = 0;
		for(Map<String, Object> map : list){
			String code = String.valueOf(map.get("CODE"));
			String value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("NAME"));
			setBuffer.append(setVar.replace("{name}", name).replace("{value}", value).replace("{color}", getColor(i)).replace("{parent}", code).replace("{xName}", name));
			i++;
		}
		/////////////////////////////
		//报表中Y轴名称均为投诉量，故暂时写死为Y=投诉量，X轴根据level来判断
		String yName = "投诉量";
		String xName = "";
		if(x_Name == null){
			if(level==1){
				xName = "一级";
			}else if (level==2) {
				xName = "二级";
			}else {
				xName = "三级";
			}
		}else{
			xName = x_Name;
		}
		
		String reXml = columnXml.replace("{set}", setBuffer.toString()).replace("{xAxisName}", xName).replace("{yAxisName}", yName);
		return reXml;
	}
	
	private String serMap1Axis(List<Map<String, Object>> list,String xName,String yName){
		StringBuffer setBuffer = new StringBuffer();
		String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" link=\"JavaScript:multDimenAnaly.showMap({code}, '{name}');\" /> ";
		
		int i = 0;
		for(Map<String, Object> map : list){
			String value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("NAME"));
			String code =  String.valueOf(map.get("CODE"));
			setBuffer.append(setVar.replace("{name}", name).replace("{value}", value).replace("{color}", getColor(i)).replace("{code}", code));
			i++;
		}
		String reXml = columnXml.replace("{set}", setBuffer.toString()).replace("{xAxisName}", xName).replace("{yAxisName}", yName);
		return reXml;
	}
	
	private String serCountyAxis(List<Map<String, Object>> list,String xName){
		StringBuffer setBuffer = new StringBuffer();
		String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" /> ";
		
		int i = 0;
		for(Map<String, Object> map : list){
			String value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("NAME"));
			setBuffer.append(setVar.replace("{name}", name).replace("{value}", value).replace("{color}", getColor(i)));
			i++;
		}
		String reXml = columnXml.replace("{set}", setBuffer.toString()).replace("{xAxisName}", xName).replace("{yAxisName}", "投诉量");
		return reXml;
	}
	
	private String serMapAxis(List<Map<String, Object>> list){
//		String yuexi = "<entity id=\"0759\" value=\"1654\"  color=\"CC9900\" fontColor=\"CC9900\" toolText=\"{toolText}\"  /> "+
//				"<entity id=\"0668\" value=\"1900\"  color=\"CC9900\" displayValue=\"粤西\n{value}{br}\" fontColor =\"000000\"  toolText=\"{toolText}\"  /> "+
//				"<entity id=\"0662\" value=\"36852\" color=\"CC9900\" fontColor =\"CC9900\"  toolText=\"{toolText}\" /> "+
//				"<entity id=\"0766\" value=\"36049\" color=\"CC9900\" fontColor =\"CC9900\"  toolText=\"{toolText}\"/> ";
//		String yuebei = "<entity id=\"0758\" value=\"32013\" color=\"00FFCC\" fontColor =\"00FFCC\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0763\" value=\"34890\" color=\"00FFCC\" displayValue=\"粤北\n{value}{br}\" fontColor =\"000000\"  toolText=\"{toolText}\" /> "+
//		    	
//		    	"<entity id=\"0762\" value=\"46729\" color=\"00FFCC\" fontColor =\"00FFCC\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0751\" value=\"43724\" color=\"00FFCC\" fontColor =\"00FFCC\"  toolText=\"{toolText}\" /> ";
//		String yuedong = "<entity id=\"0754\" value=\"83129\" color=\"99CCCC\" fontColor =\"99CCCC\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0663\" value=\"74724\" color=\"99CCCC\" displayValue=\"粤东\n{value}{br} \" fontColor =\"000000\"   toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0660\" value=\"38888\" color=\"99CCCC\" fontColor =\"99CCCC\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0753\" value=\"82365\" color=\"99CCCC\" fontColor =\"99CCCC\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0768\" value=\"82365\" color=\"99CCCC\" fontColor =\"99CCCC\"  toolText=\"{toolText}\" /> ";
//		String zhuyi = "<entity id=\"020\"  value=\"69037\" color=\"FFCC66\" displayValue=\"珠1\n{value}{br}\" fontColor =\"000000\"   toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0757\" value=\"52240\" color=\"FFCC66\" fontColor =\"FFCC66\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0755\" value=\"41260\" color=\"FFCC66\" fontColor =\"FFCC66\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0769\" value=\"47109\" color=\"FFCC66\" fontColor =\"FFCC66\"  toolText=\"{toolText}\" /> ";
//		String zhuer = "<entity id=\"0760\" value=\"46371\" color=\"0099FF\" fontColor =\"0099FF\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0756\" value=\"57514\" color=\"0099FF\" fontColor =\"0099FF\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0752\" value=\"38372\" color=\"0099FF\" fontColor =\"0099FF\"  toolText=\"{toolText}\" /> "+
//		    	"<entity id=\"0750\" value=\"62713\" color=\"0099FF\" displayValue=\"珠2\n{value}{br}\" fontColor =\"000000\"  toolText=\"{toolText}\"  /> ";
		String yuexi = "<entity id=\"0759\" value=\"1654\"  color=\"ff0000\" fontColor=\"ff0000\" toolText=\"{toolText}\"  /> "+
				"<entity id=\"0668\" value=\"1900\"  color=\"ff0000\" displayValue=\"粤西\n{value}{br}\" fontColor =\"000000\"  toolText=\"{toolText}\"  /> "+
				"<entity id=\"0662\" value=\"36852\" color=\"ff0000\" fontColor =\"ff0000\"  toolText=\"{toolText}\" /> "+
				"<entity id=\"0766\" value=\"36049\" color=\"ff0000\" fontColor =\"ff0000\"  toolText=\"{toolText}\"/> ";
		String yuebei = "<entity id=\"0758\" value=\"32013\" color=\"ffff00\" fontColor =\"ffff00\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0763\" value=\"34890\" color=\"ffff00\" displayValue=\"粤北\n{value}{br}\" fontColor =\"000000\"  toolText=\"{toolText}\" /> "+
		    	
		    	"<entity id=\"0762\" value=\"46729\" color=\"ffff00\" fontColor =\"ffff00\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0751\" value=\"43724\" color=\"ffff00\" fontColor =\"ffff00\"  toolText=\"{toolText}\" /> ";
		String yuedong = "<entity id=\"0754\" value=\"83129\" color=\"00b150\" fontColor =\"00b150\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0663\" value=\"74724\" color=\"00b150\" displayValue=\"粤东\n{value}{br} \" fontColor =\"000000\"   toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0660\" value=\"38888\" color=\"00b150\" fontColor =\"00b150\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0753\" value=\"82365\" color=\"00b150\" fontColor =\"00b150\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0768\" value=\"82365\" color=\"00b150\" fontColor =\"00b150\"  toolText=\"{toolText}\" /> ";
		String zhuyi = "<entity id=\"020\"  value=\"69037\" color=\"00b1f1\" displayValue=\"珠1\n{value}{br}\" fontColor =\"000000\"   toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0757\" value=\"52240\" color=\"00b1f1\" fontColor =\"00b1f1\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0755\" value=\"41260\" color=\"00b1f1\" fontColor =\"00b1f1\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0769\" value=\"47109\" color=\"00b1f1\" fontColor =\"00b1f1\"  toolText=\"{toolText}\" /> ";
		String zhuer = "<entity id=\"0760\" value=\"46371\" color=\"7030a1\" fontColor =\"7030a1\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0756\" value=\"57514\" color=\"7030a1\" fontColor =\"7030a1\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0752\" value=\"38372\" color=\"7030a1\" fontColor =\"7030a1\"  toolText=\"{toolText}\" /> "+
		    	"<entity id=\"0750\" value=\"62713\" color=\"7030a1\" displayValue=\"珠2\n{value}{br}\" fontColor =\"000000\"  toolText=\"{toolText}\"  /> ";

		for(Map<String, Object> map : list){
			String code = String.valueOf(map.get("CODE"));
			String value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("NAME"));
			String ratio =  String.valueOf(map.get("RATIO"));
			if(null == ratio || ratio.length() == 0){
				ratio = "0";
			}
			if(code.equals("0001")){
				zhuyi = zhuyi.replace("{toolText}", name+"\n"+value+","+ratio+"{br}").replace("{value}", ratio);
			}else if(code.equals("0002")){
				zhuer = zhuer.replace("{toolText}", name+"\n"+value+","+ratio+"{br}").replace("{value}", ratio);
			}else if(code.equals("0003")){
				yuedong = yuedong.replace("{toolText}", name+"\n"+value+","+ratio+"{br}").replace("{value}", ratio);
			}else if(code.equals("0004")){
				yuexi = yuexi.replace("{toolText}", name+"\n"+value+","+ratio+"{br}").replace("{value}", ratio);
			}else if(code.equals("0005")){
				yuebei = yuebei.replace("{toolText}", name+"\n"+value+","+ratio+"{br}").replace("{value}", ratio);
			}
		}
		
		//处理无数据的情况的数据展示
		zhuyi = zhuyi.replace("{value}", "暂无数据!").replace("{toolText}", "暂无数据!");
		zhuer = zhuer.replace("{value}", "暂无数据!").replace("{toolText}", "暂无数据!");
		yuedong = yuedong.replace("{value}", "暂无数据!").replace("{toolText}", "暂无数据!");
		yuexi = yuexi.replace("{value}", "暂无数据!").replace("{toolText}", "暂无数据!");
		yuebei = yuebei.replace("{value}", "暂无数据!").replace("{toolText}", "暂无数据!");
		return this.mapXml.replace("{yuexi}", yuexi).replace("{yuebei}", yuebei).replace("{yuedong}", yuedong).replace("{zhuer}", zhuer).replace("{zhuyi}", zhuyi);	
	}
	
	private String serAllAxis(List<Map<String, Object>> list,String xName,String yName){
		StringBuffer setBuffer = new StringBuffer();
		String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\"  link=\"JavaScript:multDimenAnaly.showMapAll({code}, '{name}');\" /> ";
		
		int i = 0;
		for(Map<String, Object> map : list){
			String code = String.valueOf(map.get("CODE"));
			String value = String.valueOf(map.get("COMPLAIN_NUM"));
			String name =  String.valueOf(map.get("NAME"));
			setBuffer.append(setVar.replace("{name}", name).replace("{value}", value).replace("{color}", getColor(i)).replace("{code}", code));
			i++;
		}
		String reXml = columnXml.replace("{set}", setBuffer.toString()).replace("{xAxisName}", xName).replace("{yAxisName}", yName);
		return reXml;
	}
	
	/**
	 * 颜色
	 * RGB
	 * @param i
	 * @return
	 */
	private String getColor(int i){
		String[] colors = new String[]{
				"0033FF",
				"FF9933",
				"00FFCC",
				"FFFF00",
				"669900",
				"006666",
				"993399",
				"FF6666",
				"33CC99",
				"FF9999",
				"9900FF",
				"CC00FF",
				"6699FF",
				"33FFFF",
				"3300CC",
				"FF3300",
				"99FF00",
				"66FF00",
				"FF99FF",
				"99FF99",
				"66CCFF",
				"333333"
			};

		return "#"+colors[i];
	}
	
    private String columnXml = "<chart snumbersuffix=\"%\" syaxismaxvalue=\"50\" showcanvasborder=\"1\" showalternatehgridcolor=\"0\" "+
        	"	palettecolors=\"#0075c2,#1aaf5d,#f2c500\" basefontcolor=\"#000000\" baseFontSize=\"12\" showYAxisValues=\"0\"  "+
        	"	subcaptionfontbold=\"0\" showborder=\"0\" bgcolor=\"#d3e7ff\" showshadow=\"0\"  "+
        	"	canvasBgAlpha=\"0\" divlinealpha=\"0\" divlinecolor=\"#666666\" xAxisName=\"{xAxisName}\" yAxisName=\"{yAxisName}\" "+
        	"	divlinethickness=\"1\" divlinedashed=\"1\" divlinedashlen=\"1\" divlinegaplen=\"1\"  "+
        	"	showplotborder=\"0\" showxaxisline=\"1\" xaxislinethickness=\"1\" usePlotGradientColor=\"0\" "+
        	"	showalternatehgridcolor=\"0\" showalternatevgridcolor=\"0\" labelFontBold=\"1\" xAxisNameFontBold=\"1\" "+
        	"	legendbgalpha=\"0\" legendborderalpha=\"0\" legendshadow=\"0\" legenditemfontsize=\"12\" "+
        	"	legenditemfontcolor=\"#666666\" canvasBorderAlpha=\"0\" lineAlpha=\"50\" unescapeLinks=\"0\" formatNumberScale=\"0\" > "+
    "	 {set}		"+
//	"        <set label=\"产品1\" value=\"520\" color=\"00CC66\" link=\"JavaScript:multDimenAnaly.show();\" />"+
	"    </chart>";
    

    private String xml = "<chart snumbersuffix=\"%\" syaxismaxvalue=\"50\" showcanvasborder=\"1\" showalternatehgridcolor=\"0\" "+
        	"	palettecolors=\"#0075c2,#1aaf5d,#f2c500\" basefontcolor=\"#000000\" baseFontSize=\"12\" showYAxisValues=\"0\"  "+
        	"	subcaptionfontbold=\"0\" showborder=\"0\" bgcolor=\"#d3e7ff\" showshadow=\"0\"  "+
        	"	canvasBgAlpha=\"0\" divlinealpha=\"0\" divlinecolor=\"#666666\" xAxisName=\"{xAxisName}\" yAxisName=\"{yAxisName}\" "+
        	"	divlinethickness=\"1\" divlinedashed=\"1\" divlinedashlen=\"1\" divlinegaplen=\"1\"  "+
        	"	showplotborder=\"0\" showxaxisline=\"1\" xaxislinethickness=\"1\" usePlotGradientColor=\"0\" "+
        	"	showalternatehgridcolor=\"0\" showalternatevgridcolor=\"0\" labelFontBold=\"1\" xAxisNameFontBold=\"1\" "+
        	"	legendbgalpha=\"0\" legendborderalpha=\"0\" legendshadow=\"0\" legenditemfontsize=\"12\" "+
        	"	legenditemfontcolor=\"#666666\" canvasBorderAlpha=\"0\" lineAlpha=\"50\" unescapeLinks=\"0\" formatNumberScale=\"0\" > "+
        	"    <categories> "+
        	"        {category} "+
//        	"        <category label=\"Feb\" /> "+
        	"    </categories> "+
        	"    <dataset> "+
        	"		 {set_value}"+
//        	"        <set value=\"16000\" /> "+
        	"    </dataset> "+
        	"    <dataset renderas=\"line\" showvalues=\"1\" alpha=\"0\" > "+
        	"		 {set_line1}"+
//        	"        <set value=\"8000\" displayValue=\"123\" alpha=\"100\" /> "+
//        	"        <set value=\"\"/> "+
//        	"        <set value=\"5000\" displayValue=\"323\"  alpha=\"100\" /> "+
//        	"        <set value=\"\"/> "+
//        	"        <set value=\"11000\" displayValue=\"523\"  alpha=\"100\" /> "+
        	"    </dataset> "+
        	"    <dataset renderas=\"line\" showvalues=\"1\" alpha=\"0\" > "+
        	"		 {set_line2}"+
//        	"        <set value=\"\"/> "+
//        	"        <set value=\"2000\" displayValue=\"223\"  alpha=\"100\" /> "+
//        	"        <set value=\"\"/> "+
//        	"        <set value=\"9000\" displayValue=\"423\"  alpha=\"100\" /> "+
        	"    </dataset> "+
        	"</chart> ";
    
    private String mapXml = "<map animation=\"0\" " +
			"showbevel=\"0\" " +
			"bgcolor=\"#d3e7ff\" "+
			"usehovercolor=\"0\" " +
			"showlegend=\"0\" " +
	"	showshadow=\"0\" " +
	"	canvasBorderColor=\"#d3e7ff\" " +
	"	baseFontSize=\"12\" " +
	"	legendposition=\"RIGHT\" " +
	"	legendborderalpha=\"0\" " +
	"	legendallowdrag=\"0\" " +
	"	legendshadow=\"0\" " +
	"	showborder=\"0\"> "+
	"	showhovercap=\"0\"> "+
	" <data> "+
	"	 {yuexi}	" +
	"	 {yuebei}	" +
	"	 {yuedong}	" +
	"	 {zhuer}	" +
	"	 {zhuyi}	" +
	" </data> "+
	" </map> ";
    
    private String pieXml = " <chart showhovercap=\"1\" showToolTip=\"1\" bgcolor=\"#d3e7ff\" showBorder=\"0\" baseFontSize=\"12\" >" +
			"	{set} " +
//			"	<set value=\"17\" label=\"Item B\" color=\"F6BD0F\" />" +
		" </chart>";
    
    private String ratioXml = "<chart showlegend=\"0\" bgcolor=\"#d3e7ff\" baseFontSize=\"12\" " +
			"showborder=\"0\" showcanvasborder=\"1\" showalternatehgridcolor=\"0\" " +
			"basefontcolor=\"#666666\" basefont=\"Helvetica Neue,Arial\" captionfontsize=\"16\" " +
			"subcaptionfontsize=\"16\" subcaptionfontbold=\"0\" tooltipcolor=\"#666666\" " +
			"tooltipborderthickness=\"0\" canvasBgAlpha =\"0\" showYAxisValues=\"1\" " +
			"showhovercap=\"1\" canvasborderalpha=\"0\" yAxisMaxValue=\"100\" yAxisMinValue=\"-100\" > "+
			"    <dataset anchorradius=\"6\" anchorbgcolor=\"#1aaf5d\"> "+
			"        {set_line} "+
			"    </dataset> "+
			"	<trendlines> "+
		    "		<line startvalue=\"0\" color=\"#1aaf5d\" valueonright=\"1\" displayvalue=\"0\" /> "+
		    " 	</trendlines> "+
			"</chart> ";
    
    private String buildTitle(int type){
    	String title = "";
    	switch(type){
    		case 2:
    		case 21:
    			title ="产品维度";
				break;
    		case 3:
    		case 31:
    			title ="表象维度";
				break;
    	}
		return title;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////
    //2016-07-20  by lisn@tydic.com
    ////////////////////////////////////////////////////////////////////////////////////////
   
    //此方法用于输出全局路径日志
    private String outputPath(List<Map<String, String>> list){
    	String resultString = "";
    	if(list.size()>1){
    		Map<String, String> map = list.get(list.size()-1);
        	for(String key:map.keySet()){
        		resultString = resultString + key+":"+map.get(key)+";";
        	}
    	}
    	
    	
    	return resultString;
    }
    
    //此方法用于输出单窗口路径 
    private String outputWindowsPath(List<String> list){
    	String resultString = "";
    	if(list.size()>0){
    		for(int i=0;i<list.size();i++){
    			resultString = resultString+ ";" + list.get(i);
    		}
    		
    	}
    	
    	
    	return resultString;
    }
    
    private void getConfData(){
    	if(sqlConfMap.size()<=0){
    		sqlConfMap = multViewAnalyDAO.queryConfSql();
    		//取到产品和表象的字典
    		dicList = getConfDicMaps();
    		
    	}
    	if(jsonConfMap.size()<=0){
    		ResultToWebEntity entity = new ResultToWebEntity();
    		jsonConfMap = multViewAnalyDAO.queryConfJson(entity);    		
    	}
    }
    
    //保存最新的操作步骤，存储到对应的象限MAP中
    private Map<String, String> addOperatorMap(List<Map<String, String>> list,FirstViewEntity entity){
    	if(entity.getCurrent_condition() == null){
			Map<String, String> scMap = MapUtil.initMap();
			list.add(scMap);
		}
    	//判断list是否有元素
    	if(list.size()<=0){	
    		Map<String, String> scMap = MapUtil.initMap();
    		list.add(scMap);
    		System.out.println("list's<=0,add an element:"+outputPath(list));
    	}
    	Map<String, String> map = list.get(list.size()-1);
//		{prod_screen}:screen
		if(entity.getCurrent_condition() != null){
			String pageKey = entity.getCurrent_condition().split(":")[0].trim(); 
			String pageValue  = entity.getCurrent_condition().split(":")[1].trim();
			String screenPar = MapUtil.getValue(pageKey);
			String screen = screenPar.split(":")[0];
			String param = screenPar.split(":")[1];
			map.put(param, screen+pageValue);
			list.add(map);//历时步骤的保存
		}

		if(entity.getMap_level() != null){
			map.put("{map_level}", entity.getMap_level());
		}else{//如果参数对象层级未空 并且Map中层级为空则赋值默认值
			String mapLevel = map.get("{map_level}");
			if(mapLevel == null){
				entity.setMap_level("1");
				map.put("{map_level}", "1");
			}else{
				entity.setMap_level(mapLevel);
			}
		}
		
		if(entity.getDr_level() != null){
			map.put("{dr_level}", entity.getDr_level());
		}else{//如果参数对象层级未空 并且Map中层级为空则赋值默认值
			String drLevel = map.get("{dr_level}");
			if(drLevel == null){
				entity.setDr_level("1");
				map.put("{dr_level}", "1");
			}else{
				entity.setDr_level(drLevel);
			}
		}
		return map;
    }
    
    //第一象限默认数据加载
	public ResultToWebEntity getFirstViewMapDef(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
			entity.setChartContainer("p_1_2_2");
		}
		try{
			getConfData();
			String sql = sqlConfMap.get(String.valueOf(entity.getType1()));
			Map<String, String> map = addOperatorMap(firstParam,entity);
			if(entity.getView_id()==1){
				firstHistory.add(entity.getCurrent_condition());//記錄歷史操作				
			}
			//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
			entity.setSql_flag("####VIEW1:");
			List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map, sql);
			
			getCurrentSql(1);
			String json = jsonConfMap.get(entity.getChartType());
			if(entity.getChartContainer()!=null&&!entity.getChartContainer().equalsIgnoreCase("")){
				json = json.replace("{chartContainer}", entity.getChartContainer());
				
				resultEntity.setChartType(entity.getChartType());
				//个性化定制，针对选择按地市的时候弹窗的标题设置
				entity.setW1_x_name("地市");
				entity.setW1_y_name("投诉量");
			}else {
				json = json.replace("{chartContainer}", "p_1_2_2");
				entity.setChartContainer("p_1_2_2");
				resultEntity.setRenderDiv("p_1_2_2");
				resultEntity.setChartType("FCMap_GuangDong.swf");
			}
			//标题组装
			buildTitle(entity,map);
			FirstViewImpl impl = new FirstViewImpl();
			json = impl.createChartData(json, list, entity,resultEntity.getJson_xml());
			
			
			resultEntity.setJson(json);
			System.out.println("全局路径FIRST:"+outputPath(firstParam));
			System.out.println("窗口路径First Windows:"+outputWindowsPath(firstHistory));
			//向页面传回标题
			resultEntity.setW1_x_name(entity.getW1_x_name());
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		resultEntity = setParamLen(resultEntity);
		return resultEntity;
	}
	

	//第二象限默认数据加载
	public ResultToWebEntity getSecondViewMapDef(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
			entity.setChartContainer("p_2_2");
		}
		try{
		getConfData();
    	
		String sql = sqlConfMap.get(String.valueOf(entity.getType2()));
		Map<String, String> map = addOperatorMap(secondParam,entity);
		if(entity.getView_id()==2){
			secondHistory.add(entity.getCurrent_condition());//記錄歷史操作
		}
		//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
		entity.setSql_flag("####VIEW2:");
		List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,sql);
		
		getCurrentSql(2);
		String json = jsonConfMap.get("3");
		if(entity.getChartContainer()!=null&&!entity.getChartContainer().equalsIgnoreCase("")){
			json = json.replace("{chartContainer}", entity.getChartContainer());
		}else {
			json = json.replace("{chartContainer}", "p_2_2");
			entity.setChartContainer("p_2_2");
		}
		
		//标题组装
		buildTitle(entity,map);
		SecondViewImpl impl = new SecondViewImpl();
		json = impl.createJSON(json, list, entity);
		
		
		resultEntity = new ResultToWebEntity();
		resultEntity.setJson(json);
		System.out.println("全局路径SECOND:"+outputPath(secondParam));
		System.out.println("窗口路径Second Windows:"+outputWindowsPath(secondHistory));
		//向页面传回标题
		resultEntity.setW2_x_name(entity.getW2_x_name());
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		
		resultEntity = setParamLen(resultEntity);
		return resultEntity;
	}
	
	//第二象限同比环比
	public ResultToWebEntity getSecondViewRateDef(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		try{
		getConfData();
    	
		String sql = sqlConfMap.get(String.valueOf(entity.getType2()));
		Map<String, String> map = MapUtil.initMap();
		if(!secondParam.isEmpty()){
			map = secondParam.get(secondParam.size()-1);
		}
		if(entity.getDr_level() != null){
			map.put("{dr_level}", entity.getDr_level());
		}else{//如果参数对象层级未空 并且Map中层级为空则赋值默认值
			String drLevel = map.get("{dr_level}");
			if(drLevel == null){
				entity.setDr_level("1");
				map.put("{dr_level}", "1");
			}else{
				entity.setDr_level(drLevel);
			}
		}
		//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
		entity.setSql_flag("####VIEW2:");
		List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,sql);
		
		//标题组装
		buildTitle(entity,map);
		String json = jsonConfMap.get("5");
		if(entity.getCurrent_condition() == null){
			json = jsonConfMap.get("3");
			SecondViewImpl impl = new SecondViewImpl();
			json = impl.createJSON(json, list, entity);
		}else{
			SecondViewImpl impl = new SecondViewImpl();
			json = impl.createRateJSON(json, list, entity);
		}
		if(entity.getChartContainer()!=null&&!entity.getChartContainer().equalsIgnoreCase("")){
			json = json.replace("{chartContainer}", entity.getChartContainer());
		}else {
			json = json.replace("{chartContainer}", "p_2_2");
			entity.setChartContainer("p_2_2");
		}
		
		resultEntity = new ResultToWebEntity();
		resultEntity.setJson(json);
		System.out.println("全局路径SECOND:"+outputPath(secondParam));
		//向页面传回标题
		resultEntity.setW2_x_name(entity.getW2_x_name());
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		return resultEntity;
	}
	
	//第三象限默认数据加载
	public ResultToWebEntity getThirdViewMapDef(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		//
		entity.setChartContainer("p_3_2");
		try{
		getConfData();
    	
		String sql = sqlConfMap.get(String.valueOf(entity.getType3()));
		Map<String, String> map = addOperatorMap(thirdParam,entity);
		if(entity.getView_id()==3){
			thirdHistory.add(entity.getCurrent_condition());//記錄歷史操作
		}
		//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
		entity.setSql_flag("####VIEW3:");
		List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,sql);
		
		getCurrentSql(3);
		String json = jsonConfMap.get("3");
		json = json.replace("{chartContainer}", "p_3_2");
		
		//标题组装
		buildTitle(entity,map);
		ThirdViewImpl impl = new ThirdViewImpl();
		json = impl.createJSON(json, list, entity);
		
		resultEntity = new ResultToWebEntity();
		resultEntity.setJson(json);
		System.out.println("全局路径THIRD:"+outputPath(thirdParam));
		System.out.println("窗口路径Third Windows:"+outputWindowsPath(thirdHistory));
		//向页面传回标题
		resultEntity.setW3_x_name(entity.getW3_x_name());
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		resultEntity = setParamLen(resultEntity);
		return resultEntity;
	}
	
	//第三象限同比环比
		public ResultToWebEntity getThirdViewRateDef(FirstViewEntity entity){
			ResultToWebEntity resultEntity = new ResultToWebEntity();
			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
				entity.setChartContainer("p_3_2");
			}
			try{
			getConfData();
	    	
			String sql = sqlConfMap.get(String.valueOf(entity.getType3()));
			Map<String, String> map = MapUtil.initMap();
			if(!thirdParam.isEmpty()){
				map = thirdParam.get(thirdParam.size()-1);
			}
			if(entity.getDr_level() != null){
				map.put("{dr_level}", entity.getDr_level());
			}else{//如果参数对象层级未空 并且Map中层级为空则赋值默认值
				String drLevel = map.get("{dr_level}");
				if(drLevel == null){
					entity.setDr_level("1");
					map.put("{dr_level}", "1");
				}else{
					entity.setDr_level(drLevel);
				}
			}
			//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
			entity.setSql_flag("####VIEW3:");
			List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,sql);
			
			//标题组装
			buildTitle(entity,map);
			String json = jsonConfMap.get("5");
			if(entity.getCurrent_condition() == null){
				json = jsonConfMap.get("3");
				ThirdViewImpl impl = new ThirdViewImpl();
				json = impl.createJSON(json, list, entity);
			}else{
				ThirdViewImpl impl = new ThirdViewImpl();
				json = impl.createRateJSON(json, list, entity);
			}
			if(entity.getChartContainer()!=null&&!entity.getChartContainer().equalsIgnoreCase("")){
				json = json.replace("{chartContainer}", entity.getChartContainer());
			}else {
				json = json.replace("{chartContainer}", "p_3_2");
			}
			
			resultEntity = new ResultToWebEntity();
			resultEntity.setJson(json);
			System.out.println("全局路径THIRD:"+outputPath(thirdParam));
			//向页面传回标题
			resultEntity.setW3_x_name(entity.getW3_x_name());
			}catch (Exception e) {
				e.printStackTrace();
				return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
			}
			return resultEntity;
		}
	
	//第四象限默认数据加载
	public ResultToWebEntity getFourthViewMapDef(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
			entity.setChartContainer("p_4_2_2");
		}
		try{
		getConfData();
    	
		String sql = sqlConfMap.get(String.valueOf(entity.getType4()));
		Map<String, String> map = addOperatorMap(fourthParam,entity);
		if(entity.getView_id()==4){
			fourthHistory.add(entity.getCurrent_condition());//記錄歷史操作
		}
		if(map.get("{cust_screen}") != null){
			if(!map.get("{cust_screen}").contains("aa.")){
				map.put("{cust_screen}", map.get("{cust_screen}").replace("a.", "aa."));
			}
		}
		//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
		entity.setSql_flag("####VIEW4:");
		List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,sql);
		
		getCurrentSql(4);
		String json = jsonConfMap.get("4");
		json = json.replace("{chartContainer}", "p_4_2_2");

		//标题组装
		buildTitle(entity,map);
		FourthViewImpl impl = new FourthViewImpl();
		json = impl.createJSON(json, list, entity);
		
		resultEntity = new ResultToWebEntity();
		resultEntity.setJson(json);
		System.out.println("全局路径FOURTH:"+outputPath(fourthParam));
		System.out.println("窗口路径Fourth Windows:"+outputWindowsPath(fourthHistory));
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		resultEntity = setParamLen(resultEntity);
		return resultEntity;
	}
	
	//第五象限默认数据加载
	public ResultToWebEntity getFifthViewMapDef(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
			entity.setChartContainer("p_5_2_2");
		}
		try{
		getConfData();
    	
		String sql = sqlConfMap.get(String.valueOf(entity.getType5()));
		Map<String, String> map = addOperatorMap(fifthParam,entity);
		if(entity.getView_id()==5){
			fifthHistory.add(entity.getCurrent_condition());//記錄歷史操作
		}
		if(entity.getCurrent_condition() == null && fifthParam.size() > 1){
			fifthParam.remove(fifthParam.size()-1);
			map = fifthParam.get(fifthParam.size()-1);
		}
		//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
		entity.setSql_flag("####VIEW5:");
		List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,sql);
		
		getCurrentSql(5);
		String json = jsonConfMap.get("3");
		json = json.replace("{chartContainer}", "p_5_2_2");
		//标题组装
		buildTitle(entity,map);
		FifthViewImpl impl = new FifthViewImpl();
		json = impl.createJSON(json, list, entity);
		
		resultEntity = new ResultToWebEntity();
		resultEntity.setJson(json);
		System.out.println("全局路径FIFTH:"+outputPath(fifthParam));
		System.out.println("窗口路径Fifth Windows:"+outputWindowsPath(fifthHistory));
		//向页面传回标题
		resultEntity.setW5_x_name(entity.getW5_x_name());
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		resultEntity = setParamLen(resultEntity);
		return resultEntity;
	}
	
	//第五象限同比环比
	public ResultToWebEntity getFifthViewRateDef(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
			entity.setChartContainer("p_3_2");
		}
		try{
		getConfData();
    	
		String sql = sqlConfMap.get(String.valueOf(entity.getType5()));
		Map<String, String> map = MapUtil.initMap();
		if(!fifthParam.isEmpty()){
			map = fifthParam.get(fifthParam.size()-1);
		}
		if(entity.getDr_level() != null){
			map.put("{dr_level}", entity.getDr_level());
		}else{//如果参数对象层级未空 并且Map中层级为空则赋值默认值
			String drLevel = map.get("{dr_level}");
			if(drLevel == null){
				entity.setDr_level("1");
				map.put("{dr_level}", "1");
			}else{
				entity.setDr_level(drLevel);
			}
		}
		//设置以下entity属性值，为了方便观察输出的SQL属于哪个窗口
		entity.setSql_flag("####VIEW5:");
		List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,sql);
		
		//标题组装
		buildTitle(entity,map);
		String json = jsonConfMap.get("5");
		if(entity.getCurrent_condition() == null){
			json = jsonConfMap.get("3");
			FifthViewImpl impl = new FifthViewImpl();
			json = impl.createJSON(json, list, entity);
		}else{
			FifthViewImpl impl = new FifthViewImpl();
			json = impl.createRateJSON(json, list, entity);
		}
		if(entity.getChartContainer()!=null&&!entity.getChartContainer().equalsIgnoreCase("")){
			json = json.replace("{chartContainer}", entity.getChartContainer());
		}else {
			json = json.replace("{chartContainer}", "p_3_2");
		}
		
		resultEntity = new ResultToWebEntity();
		resultEntity.setJson(json);
		System.out.println("全局路径FIFTH:"+outputPath(fifthParam));
		//向页面传回标题
		resultEntity.setW5_x_name(entity.getW5_x_name());
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		return resultEntity;
	}

	//回退操作
	public ResultToWebEntity getFallbackCondition(FirstViewEntity entity){
		getConfData();
		String json = null; 
		String drLevel = "1";
		String mapLevel = "1";
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		try{
		//判断回退需要的窗口1-5
    	if(entity.getFallbackWindow() == 1){
    		switch (entity.getWhich_first()) {
    		case 1: 
//				if(firstHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_1_2_2");
    			}
	    		/******  图表1 联动  *****/
	    		firstHistory.remove(firstHistory.size()-1);
	    		json = firstHistory.get(firstHistory.size()-1);
	    		drLevel = firstParam.get(firstParam.size()-1).get("{dr_level}");
	    		mapLevel = firstParam.get(firstParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		int level = Integer.parseInt(mapLevel)-1;
	    		if(level < 1){
	    			level = 1;
	    		}
	    		entity.setDr_level(drLevel);
	    		entity.setMap_level(String.valueOf(level));
	    		entity.setChartContainer("p_1_2_2");
	    		if(entity.getType1()==11){
	    			entity.setChartType("3");
	    		}else{
	    			entity.setChartType("1");
	    		}	
	    		String sql = sqlConfMap.get(String.valueOf(entity.getType1()));
	    		Map<String, String> scMap = addOperatorMap(firstParam,entity);
	    		MultViewInterface impl = new FirstViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW1:");
	    		String json1 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json1);
	    		System.out.println("窗口路径First Windows:"+outputWindowsPath(firstHistory));
	    		break;
    		case 2:
	    		/******  图表2 回退  *****/
//				if(secondHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_2_2");
    			}
//	    		secondHistory.remove(secondHistory.size()-1);//删除最新的记录
//	    		json = secondHistory.get(secondHistory.size()-1);
    			json = firstHistory.get(firstHistory.size()-1);
	    		drLevel = secondParam.get(secondParam.size()-1).get("{dr_level}");
	    		mapLevel = secondParam.get(secondParam.size()-1).get("{map_level}");	    		
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		level = Integer.parseInt(mapLevel)-1;
	    		if(level < 1){
	    			level = 1;
	    		}
	    		entity.setMap_level(String.valueOf(level));
	    		entity.setChartType("3");
	    		entity.setChartContainer("p_2_2");
	    		sql = sqlConfMap.get(String.valueOf(entity.getType2()));
	    		scMap = addOperatorMap(secondParam,entity);
	    		buildTitle(entity,scMap);
	    		//对于接受到回退指令的窗口，该窗口本身的上一个条件需要清楚掉，才能达到回退的效果
	    		secondParam.get(secondParam.size()-1).put("{prod_screen}", null);
	    		impl = new SecondViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW2:");
	    		String json2 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json2);
	    		System.out.println("窗口路径Second Windows:"+outputWindowsPath(secondHistory));
				break;
			
			case 3:
//				if(thirdHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
				entity.setChartContainer("p_3_2");
//	    		/******  图表3联动  *****/
//	    		thirdHistory.remove(thirdHistory.size()-1);
//	    		json = thirdHistory.get(thirdHistory.size()-1);
				json = firstHistory.get(firstHistory.size()-1);
	    		drLevel = thirdParam.get(thirdParam.size()-1).get("{dr_level}");
	    		mapLevel = thirdParam.get(thirdParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		level = Integer.parseInt(mapLevel)-1;
	    		if(level < 1){
	    			level = 1;
	    		}
	    		entity.setMap_level(String.valueOf(level));
	    		entity.setChartContainer("p_3_2");
	    		entity.setChartType("3");
	    		sql = sqlConfMap.get(String.valueOf(entity.getType3()));
	    		scMap = addOperatorMap(thirdParam,entity);
	    		buildTitle(entity,scMap);
	    		
	    		impl = new ThirdViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW3:");
	    		String json3 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json3);
	    		System.out.println("窗口路径Third Windows:"+outputWindowsPath(thirdHistory));
	    		break;
			case 4:
//				if(fourthHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
				if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
					entity.setChartContainer("p_4_2_2");
				}
//	    		/******  图表4联动  *****/
//	    		fourthHistory.remove(fourthHistory.size()-1);
//	    		json = fourthHistory.get(fourthHistory.size()-1);
				json = firstHistory.get(firstHistory.size()-1);
	    		drLevel = fourthParam.get(fourthParam.size()-1).get("{dr_level}");
	    		mapLevel = fourthParam.get(fourthParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		level = Integer.parseInt(mapLevel)-1;
	    		if(level < 1){
	    			level = 1;
	    		}
	    		entity.setMap_level(String.valueOf(level));
	    		entity.setChartContainer("p_4_2_2");
	    		entity.setChartType("4");
	    		sql = sqlConfMap.get(String.valueOf(entity.getType4()));
	    		scMap = addOperatorMap(fourthParam,entity);
	    		if(scMap.get("{cust_screen}") != null){
	    			scMap.put("{cust_screen}", scMap.get("{cust_screen}").replace("a.", "aa."));
	    		}
	    		impl = new FourthViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW4:");
	    		String json4 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json4);
	    		System.out.println("窗口路径Fourth Windows:"+outputWindowsPath(fourthHistory));
	    		break;
			case 5:
//				if(fifthHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
				if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
					entity.setChartContainer("p_5_2_2");
				}
//	    		/******  图表5联动  *****/
//	    		fifthHistory.remove(fifthHistory.size()-1);
//	    		json = fifthHistory.get(fifthHistory.size()-1);
				json = firstHistory.get(firstHistory.size()-1);
	    		drLevel = fifthParam.get(fifthParam.size()-1).get("{dr_level}");
	    		mapLevel = fifthParam.get(fifthParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		level = Integer.parseInt(mapLevel)-1;
	    		if(level < 1){
	    			level = 1;
	    		}
	    		entity.setMap_level(String.valueOf(level));
	    		entity.setChartContainer("p_5_2_2");
	    		entity.setChartType("3");
	    		sql = sqlConfMap.get(String.valueOf(entity.getType5()));
	    		scMap = addOperatorMap(fifthParam,entity);
	    		//组装标题
	    		buildTitle(entity,scMap);
	    		
	    		impl = new FifthViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW5:");
	    		String json5 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json5);
	    		System.out.println("窗口路径Fifth Windows:"+outputWindowsPath(fifthHistory));
	    		break;
    		default:
    			break;
    		}
    	}else if(entity.getFallbackWindow() == 2){
    		switch (entity.getWhich_first()) {
			case 2:
				if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
					entity.setChartContainer("p_2_2");
				}
	    		/******  图表2 回退  *****/
//				if(secondHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
	    		secondHistory.remove(secondHistory.size()-1);//删除最新的记录
	    		json = secondHistory.get(secondHistory.size()-1);
	    		drLevel = secondParam.get(secondParam.size()-1).get("{dr_level}");
	    		mapLevel = secondParam.get(secondParam.size()-1).get("{map_level}");
	    		int level = Integer.parseInt(drLevel)-1;
	    		if(level < 1){
	    			level = 1;
	    		}
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(level));
	    		entity.setMap_level(mapLevel);
	    		entity.setChartType("3");
	    		entity.setChartContainer("p_2_2");
	    		String sql = sqlConfMap.get(String.valueOf(entity.getType2()));
	    		Map<String, String> scMap = addOperatorMap(secondParam,entity);
	    		buildTitle(entity,scMap);
	    		//对于接受到回退指令的窗口，该窗口本身的上一个条件需要清楚掉，才能达到回退的效果
	    		secondParam.get(secondParam.size()-1).put("{prod_screen}", null);
	    		MultViewInterface impl = new SecondViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW2:");
	    		String json2 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json2);
	    		System.out.println("窗口路径Second Windows:"+outputWindowsPath(secondHistory));
				break;
			case 1: 
//				if(firstHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
				if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
					entity.setChartContainer("p_1_2_2");
				}
//	    		/******  图表1 联动  *****/
//	    		firstHistory.remove(firstHistory.size()-1);
//	    		json = firstHistory.get(firstHistory.size()-1);
				json = secondHistory.get(secondHistory.size()-1);
	    		drLevel = firstParam.get(firstParam.size()-1).get("{dr_level}");
	    		mapLevel = firstParam.get(firstParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		entity.setMap_level(mapLevel);
	    		entity.setChartContainer("p_1_2_2");
	    		if(entity.getType1()==11){
	    			entity.setChartType("3");
	    		}else{
	    			entity.setChartType("1");
	    		}	
	    		sql = sqlConfMap.get(String.valueOf(entity.getType1()));
	    		scMap = addOperatorMap(firstParam,entity);
	    		impl = new FirstViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW1:");
	    		String json1 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json1);
	    		System.out.println("窗口路径First Windows:"+outputWindowsPath(firstHistory));
	    		break;
			case 3:
//				if(thirdHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
				entity.setChartContainer("p_3_2");
//	    		/******  图表3联动  *****/
//	    		thirdHistory.remove(thirdHistory.size()-1);
//	    		json = thirdHistory.get(thirdHistory.size()-1);
				json = secondHistory.get(secondHistory.size()-1);
	    		drLevel = thirdParam.get(thirdParam.size()-1).get("{dr_level}");
	    		mapLevel = thirdParam.get(thirdParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		entity.setMap_level(mapLevel);
	    		entity.setChartContainer("p_3_2");
	    		entity.setChartType("3");
	    		sql = sqlConfMap.get(String.valueOf(entity.getType3()));
	    		scMap = addOperatorMap(thirdParam,entity);
	    		buildTitle(entity,scMap);
	    		
	    		impl = new ThirdViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW3:");
	    		String json3 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json3);
	    		System.out.println("窗口路径Third Windows:"+outputWindowsPath(thirdHistory));
	    		break;
			case 4:
//				if(fourthHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
				if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
					entity.setChartContainer("p_4_2_2");
				}
//	    		/******  图表4联动  *****/
//	    		fourthHistory.remove(fourthHistory.size()-1);
//	    		json = fourthHistory.get(fourthHistory.size()-1);
				json = secondHistory.get(secondHistory.size()-1);
	    		drLevel = fourthParam.get(fourthParam.size()-1).get("{dr_level}");
	    		mapLevel = fourthParam.get(fourthParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		entity.setMap_level(mapLevel);
	    		entity.setChartContainer("p_4_2_2");
	    		entity.setChartType("4");
	    		sql = sqlConfMap.get(String.valueOf(entity.getType4()));
	    		scMap = addOperatorMap(fourthParam,entity);
	    		if(scMap.get("{cust_screen}") != null){
	    			scMap.put("{cust_screen}", scMap.get("{cust_screen}").replace("a.", "aa."));
	    		}
	    		impl = new FourthViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW4:");
	    		String json4 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json4);
	    		System.out.println("窗口路径Fourth Windows:"+outputWindowsPath(fourthHistory));
	    		break;
			case 5:
//				if(fifthHistory.size() < 2){
//					resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//	    		}
				if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
					entity.setChartContainer("p_5_2_2");
				}
//	    		/******  图表5联动  *****/
//	    		fifthHistory.remove(fifthHistory.size()-1);
//	    		json = fifthHistory.get(fifthHistory.size()-1);
				json = secondHistory.get(secondHistory.size()-1);
	    		drLevel = fifthParam.get(fifthParam.size()-1).get("{dr_level}");
	    		mapLevel = fifthParam.get(fifthParam.size()-1).get("{map_level}");
	    		entity.setCurrent_condition(json);
	    		entity.setDr_level(String.valueOf(drLevel));
	    		entity.setMap_level(mapLevel);
	    		entity.setChartContainer("p_5_2_2");
	    		entity.setChartType("3");
	    		sql = sqlConfMap.get(String.valueOf(entity.getType5()));
	    		scMap = addOperatorMap(fifthParam,entity);
	    		//组装标题
	    		buildTitle(entity,scMap);
	    		
	    		impl = new FifthViewImpl();
	    		//用于打印日志
	    		entity.setSql_flag("####VIEW5:");
	    		String json5 = getFallbackView(scMap, entity, sql, impl);
	    		resultEntity.setJson(json5);
	    		System.out.println("窗口路径Fifth Windows:"+outputWindowsPath(fifthHistory));
	    		break;
    		default:
    			break;
    		}

    	}else if(entity.getFallbackWindow() == 3){
    		switch (entity.getWhich_first()) {
			case 3:
//    		if(thirdHistory.size() < 2){
//    			resultEntity.setErr_msg("无可回退的操作");
//    			return resultEntity;
//    		}
			entity.setChartContainer("p_3_2");
    		/******  图表3 回退  *****/
    		thirdHistory.remove(thirdHistory.size()-1);//删除最新的记录
    		json = thirdHistory.get(thirdHistory.size()-1);
    		drLevel = thirdParam.get(thirdParam.size()-1).get("{dr_level}");
    		mapLevel = thirdParam.get(thirdParam.size()-1).get("{map_level}");
    		
    		int level = Integer.parseInt(drLevel)-1;
    		if(level < 1){
    			level = 1;
    		}
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(level));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_3_2");
    		entity.setChartType("3");
    		String sql = sqlConfMap.get(String.valueOf(entity.getType3()));
    		Map<String, String> scMap = addOperatorMap(thirdParam,entity);
    		buildTitle(entity,scMap);
    		//对于接受到回退指令的窗口，该窗口本身的上一个条件需要清楚掉，才能达到回退的效果
    		thirdParam.get(thirdParam.size()-1).put("{busi_screen}", null);
    		MultViewInterface impl = new ThirdViewImpl();
    		String json3 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json3);
    		System.out.println("窗口路径Third Windows:"+outputWindowsPath(thirdHistory));
    		break;
    		
    		
    		case 1:
//    			if(firstHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_1_2_2");
    			}
//    		/******  图表1 联动  *****/
//    		firstHistory.remove(firstHistory.size()-1);
//    		json = firstHistory.get(firstHistory.size()-1);
    			json = thirdHistory.get(thirdHistory.size()-1);
    		drLevel = firstParam.get(firstParam.size()-1).get("{dr_level}");
    		mapLevel = firstParam.get(firstParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_1_2_2");
    		if(entity.getType1()==11){
    			entity.setChartType("3");
    		}else{
    			entity.setChartType("1");    			
    		}
    		sql = sqlConfMap.get(String.valueOf(entity.getType1()));
    		scMap = addOperatorMap(firstParam,entity);
    		impl = new FirstViewImpl();
    		String json1 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json1);
    		System.out.println("窗口路径First Windows:"+outputWindowsPath(firstHistory));
    		break;
    		case 2:
//    			if(secondHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_2_2");
    			}
//    		/******  图表2联动  *****/
//    		secondHistory.remove(secondHistory.size()-1);
//    		json = secondHistory.get(secondHistory.size()-1);
    			json = thirdHistory.get(thirdHistory.size()-1);
    		drLevel = secondParam.get(secondParam.size()-1).get("{dr_level}");
    		mapLevel = secondParam.get(secondParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_2_2");
    		entity.setChartType("3");
    		sql = sqlConfMap.get(String.valueOf(entity.getType2()));
    		scMap = addOperatorMap(secondParam,entity);
    		impl = new SecondViewImpl();
    		String json2 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json2);
    		System.out.println("窗口路径Second Windows:"+outputWindowsPath(secondHistory));
    		break;
    		case 4:
//    			if(fourthHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_4_2_2");
    			}
//    		/******  图表4联动  *****/
//    		fourthHistory.remove(fourthHistory.size()-1);
//    		json = fourthHistory.get(fourthHistory.size()-1);
    			json = thirdHistory.get(thirdHistory.size()-1);
    		drLevel = fourthParam.get(fourthParam.size()-1).get("{dr_level}");
    		mapLevel = fourthParam.get(fourthParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_4_2_2");
    		entity.setChartType("4");
    		sql = sqlConfMap.get(String.valueOf(entity.getType4()));
    		scMap = addOperatorMap(fourthParam,entity);
    		if(scMap.get("{cust_screen}") != null){
    			scMap.put("{cust_screen}", scMap.get("{cust_screen}").replace("a.", "aa."));
    		}
    		impl = new FourthViewImpl();
    		String json4 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json4);
    		System.out.println("窗口路径Fourth Windows:"+outputWindowsPath(fourthHistory));
    		break;
    		case 5:
//    			if(fifthHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_5_2_2");
    			}
//    		/******  图表5联动  *****/
//    		fifthHistory.remove(fifthHistory.size()-1);
//    		json = fifthHistory.get(fifthHistory.size()-1);
    			json = thirdHistory.get(thirdHistory.size()-1);
    		drLevel = fifthParam.get(fifthParam.size()-1).get("{dr_level}");
    		mapLevel = fifthParam.get(fifthParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_5_2_2");
    		entity.setChartType("3");
    		sql = sqlConfMap.get(String.valueOf(entity.getType5()));
    		scMap = addOperatorMap(fifthParam,entity);
    		impl = new FifthViewImpl();
    		String json5 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json5);
    		System.out.println("窗口路径Fifth Windows:"+outputWindowsPath(fifthHistory));
    		break;
    		default:
    			break;
    		}
    		
    		
//    		resultEntity.setJson1(json1);
//    		resultEntity.setJson2(json2);
//    		resultEntity.setJson3(json3);
//    		resultEntity.setJson4(json4);
//    		resultEntity.setJson5(json5);
    	}else if(entity.getFallbackWindow() == 4){
    		
    	}else if(entity.getFallbackWindow() == 5){
    		switch (entity.getWhich_first()) {
    		case 5:
//    			if(fifthHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_5_2_2");
    			}
//    		/******  图表5联动  *****/
    		fifthHistory.remove(fifthHistory.size()-1);
    		json = fifthHistory.get(fifthHistory.size()-1);
    		drLevel = fifthParam.get(fifthParam.size()-1).get("{dr_level}");
    		mapLevel = fifthParam.get(fifthParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_5_2_2");
    		entity.setChartType("3");
    		String sql = sqlConfMap.get(String.valueOf(entity.getType5()));
    		Map<String, String>  scMap = addOperatorMap(fifthParam,entity);
    		MultViewInterface impl = new FifthViewImpl();
    		String json5 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json5);
    		System.out.println("窗口路径Fifth Windows:"+outputWindowsPath(fifthHistory));
    		break;
    		
    		case 1:
//    			if(firstHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_1_2_2");
    			}
//    		/******  图表1 联动  *****/
//    		firstHistory.remove(firstHistory.size()-1);
//    		json = firstHistory.get(firstHistory.size()-1);
    			json = fifthHistory.get(fifthHistory.size()-1);
    		drLevel = firstParam.get(firstParam.size()-1).get("{dr_level}");
    		mapLevel = firstParam.get(firstParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_1_2_2");
    		if(entity.getType1()==11){
    			entity.setChartType("3");
    		}else{
    			entity.setChartType("1");    			
    		}
    		sql = sqlConfMap.get(String.valueOf(entity.getType1()));
    		scMap = addOperatorMap(firstParam,entity);
    		impl = new FirstViewImpl();
    		String json1 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json1);
    		System.out.println("窗口路径First Windows:"+outputWindowsPath(firstHistory));
    		break;
    		case 2:
//    			if(secondHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_2_2");
    			}
//    		/******  图表2联动  *****/
//    		secondHistory.remove(secondHistory.size()-1);
//    		json = secondHistory.get(secondHistory.size()-1);
    			json = fifthHistory.get(fifthHistory.size()-1);
    		drLevel = secondParam.get(secondParam.size()-1).get("{dr_level}");
    		mapLevel = secondParam.get(secondParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_2_2");
    		entity.setChartType("3");
    		sql = sqlConfMap.get(String.valueOf(entity.getType2()));
    		scMap = addOperatorMap(secondParam,entity);
    		impl = new SecondViewImpl();
    		String json2 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json2);
    		System.out.println("窗口路径Second Windows:"+outputWindowsPath(secondHistory));
    		break;
    		
    		case 3:
//        		if(thirdHistory.size() < 2){
//        			resultEntity.setErr_msg("无可回退的操作");
//        			return resultEntity;
//        		}
    			entity.setChartContainer("p_3_2");
        		/******  图表3 回退  *****/
//        		thirdHistory.remove(thirdHistory.size()-1);//删除最新的记录
    			json = fifthHistory.get(fifthHistory.size()-1);
        		drLevel = thirdParam.get(thirdParam.size()-1).get("{dr_level}");
        		mapLevel = thirdParam.get(thirdParam.size()-1).get("{map_level}");
        		
        		int level = Integer.parseInt(drLevel)-1;
        		if(level < 1){
        			level = 1;
        		}
        		entity.setCurrent_condition(json);
        		entity.setDr_level(String.valueOf(level));
        		entity.setMap_level(mapLevel);
        		entity.setChartContainer("p_3_2");
        		entity.setChartType("3");
        		sql = sqlConfMap.get(String.valueOf(entity.getType3()));
        		scMap = addOperatorMap(thirdParam,entity);
        		buildTitle(entity,scMap);
        		//对于接受到回退指令的窗口，该窗口本身的上一个条件需要清楚掉，才能达到回退的效果
        		thirdParam.get(thirdParam.size()-1).put("{busi_screen}", null);
        		impl = new ThirdViewImpl();
        		String json3 = getFallbackView(scMap, entity, sql, impl);
        		resultEntity.setJson(json3);
        		System.out.println("窗口路径Third Windows:"+outputWindowsPath(thirdHistory));
        		break;
        		
    		case 4:
//    			if(fourthHistory.size() < 2){
//    				resultEntity.setErr_msg("无可回退的操作");
//	    			return resultEntity;
//        		}
    			if(entity.getChartContainer()==null||entity.getChartContainer().equalsIgnoreCase("")){
    				entity.setChartContainer("p_4_2_2");
    			}
//    		/******  图表4联动  *****/
//    		fourthHistory.remove(fourthHistory.size()-1);
//    		json = fourthHistory.get(fourthHistory.size()-1);
    			json = fifthHistory.get(fifthHistory.size()-1);
    		drLevel = fourthParam.get(fourthParam.size()-1).get("{dr_level}");
    		mapLevel = fourthParam.get(fourthParam.size()-1).get("{map_level}");
    		entity.setCurrent_condition(json);
    		entity.setDr_level(String.valueOf(drLevel));
    		entity.setMap_level(mapLevel);
    		entity.setChartContainer("p_4_2_2");
    		entity.setChartType("4");
    		sql = sqlConfMap.get(String.valueOf(entity.getType4()));
    		scMap = addOperatorMap(fourthParam,entity);
    		if(scMap.get("{cust_screen}") != null){
    			scMap.put("{cust_screen}", scMap.get("{cust_screen}").replace("a.", "aa."));
    		}
    		impl = new FourthViewImpl();
    		String json4 = getFallbackView(scMap, entity, sql, impl);
    		resultEntity.setJson(json4);
    		System.out.println("窗口路径Fourth Windows:"+outputWindowsPath(fourthHistory));
    		break;
    		default:
    			break;
    		}
    	}
    	System.out.println("FALLBACK:"+resultEntity);
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
    	
		resultEntity = setParamLen(resultEntity);
		return resultEntity;
	}
	
	//回退json
	public String getFallbackView(Map<String, String> map, FirstViewEntity entity, String sql, MultViewInterface impl) throws Exception{
		
		List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map, sql);
		
		String json = jsonConfMap.get(entity.getChartType());
		if(entity.getChartContainer()!=null&&!entity.getChartContainer().equalsIgnoreCase("")){
			json = json.replace("{chartContainer}", entity.getChartContainer());
		}else {
			json = json.replace("{chartContainer}", "p_1_2_2");
		}
		
		json = impl.createJSON(json, list, entity);
		return json;
	}
	
	
	
	public void clear(){
		firstParam = new ArrayList<Map<String, String>>();
		secondParam = new ArrayList<Map<String, String>>();
		thirdParam = new ArrayList<Map<String, String>>();
		fourthParam = new ArrayList<Map<String, String>>();
		fifthParam = new ArrayList<Map<String, String>>();
		firstHistory = new ArrayList<String>();
		secondHistory = new ArrayList<String>();
		thirdHistory = new ArrayList<String>();
		fourthHistory = new ArrayList<String>();
		fifthHistory = new ArrayList<String>();
	}
	
	private void getCurrentSql(int type){
		String sql = multViewAnalyDAO.getCurrentSql(type);
		switch (type) {
		case 1:
			this.firstSql = sql;
			break;
		case 2:
			this.secondSql = sql;
			break;
		case 3:
			this.thirdSql = sql;
			break;
		case 4:
			this.fourthSql = sql;
			break;
		case 5:
			this.fifthSql = sql;
			break;
		default:
			sql = "";
			break;
		}
	}
	
	
	public ResultToWebEntity getAllDataFromWindows(FirstViewEntity entity){
		ResultToWebEntity resultEntity = new ResultToWebEntity();
		Map<String, String> map = new HashMap<String, String>();
		entity.setChartContainer("detail");
		try{
		switch (entity.getWindow_id()) {
		case 1:{
			entity.setSql_flag("####VIEW1:");
			List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,this.firstSql);
			
			String json = jsonConfMap.get("3");
			json = json.replace("{chartContainer}", "detail");
			buildTitle(entity, this.firstParam.get(firstParam.size()-1));
			FirstViewImpl impl = new FirstViewImpl();
			json = impl.createJSON(json, list, entity);
			
			resultEntity.setJson(json);
			resultEntity.setW1_x_name(entity.getW1_x_name());
			System.out.println("FIRST:"+outputPath(firstParam));
			break;}
		case 2:{
			entity.setSql_flag("####VIEW2:");
			List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,this.secondSql);
			
			String json = jsonConfMap.get("3");
			json = json.replace("{chartContainer}", "detail");
			buildTitle(entity, this.secondParam.get(secondParam.size()-1));
			SecondViewImpl impl = new SecondViewImpl();
			json = impl.createJSON(json, list, entity);
			
			resultEntity.setJson(json);
			resultEntity.setW2_x_name(entity.getW2_x_name());
			System.out.println("FIRST:"+outputPath(firstParam));
			break;}
		case 3:{
			entity.setSql_flag("####VIEW3:");
			List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,this.thirdSql);
			
			String json = jsonConfMap.get("3");
			json = json.replace("{chartContainer}", "detail");
			buildTitle(entity, thirdParam.get(thirdParam.size()-1));
			ThirdViewImpl impl = new ThirdViewImpl();
			json = impl.createJSON(json, list, entity);
			
			resultEntity.setJson(json);
			resultEntity.setW3_x_name(entity.getW3_x_name());
			System.out.println("THIRD:"+outputPath(firstParam));
			break;}
		case 4:{
			entity.setSql_flag("####VIEW4:");
			List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,this.fourthSql);
			
			String json = jsonConfMap.get("4");
			json = json.replace("{chartContainer}", "detail");

			FourthViewImpl impl = new FourthViewImpl();
			json = impl.createJSON(json, list, entity);
			
			resultEntity.setJson(json);
			System.out.println("FOURTH:"+outputPath(firstParam));
			break;}
		case 5:{
			entity.setSql_flag("####VIEW5:");
			List<Map<String, Object>> list = multViewAnalyDAO.getFirstViewData(entity, map,this.fifthSql);
			
			String json = jsonConfMap.get("3");
			json = json.replace("{chartContainer}", "detail");
			buildTitle(entity, fifthParam.get(fifthParam.size()-1));
			FifthViewImpl impl = new FifthViewImpl();
			json = impl.createJSON(json, list, entity);
			
			resultEntity.setJson(json);
			resultEntity.setW5_x_name(entity.getW5_x_name());
			System.out.println("FIFTH:"+outputPath(firstParam));
			break;}
		default:
			break;
		}
		}catch (Exception e) {
			e.printStackTrace();
			return loadChartError(jsonConfMap.get("2"),entity,e.getMessage());
		}
		
		return resultEntity;
	}
	
	/**
	 * 组装标题
	 * @param entity
	 * @param map
	 * @return
	 */
	private FirstViewEntity buildTitle(FirstViewEntity entity,Map<String, String> map){
		
		for(String key:map.keySet()){
			String value = map.get(key);
			
			if(key.equalsIgnoreCase("{map_screen}")){
				if(value!=null&&!value.equalsIgnoreCase("")){
					value = value.split("'")[1];
					System.out.println("title-{map_screen}"+value);
					value = value.replace("%", "");
					
					Map<String, String> areaCodeMap_1 =  dicList.get(4);
					Map<String, String> parentId_mapMap =  dicList.get(5);
					
					String title_seq1 = areaCodeMap_1.get(value);
					String parentId = parentId_mapMap.get(value);
					//循环获取标题名称
					while(!parentId.equalsIgnoreCase("-1")){
						title_seq1 = areaCodeMap_1.get(parentId) +"-"+ title_seq1;
						
						parentId = parentId_mapMap.get(parentId);
					}
					
					entity.setW1_x_name(entity.getW1_x_name()+title_seq1);
				}
			}else if(key.equalsIgnoreCase("{prod_screen}")){
				if(value!=null&&!value.equalsIgnoreCase("")){
					value = value.split("'")[1];
					System.out.println("title-{prod_screen}"+value);
					value = value.replace("%", "");
					
					Map<String, String> name_mapMap =  dicList.get(0);
					Map<String, String> parentId_mapMap =  dicList.get(1);
					
					String title_seq1 = name_mapMap.get(value);
					String parentId = parentId_mapMap.get(value);
					if(parentId!=null&&!parentId.equalsIgnoreCase("-1")){
						title_seq1 = name_mapMap.get(parentId)+"-"+title_seq1;
					}
					if(title_seq1!=null&&!title_seq1.equalsIgnoreCase("")){
						entity.setW2_x_name("产品");
					}
					entity.setW2_x_name(entity.getW2_x_name()+"-"+title_seq1);
				}
			}else if(key.equalsIgnoreCase("{busi_screen}")){
				if(value!=null&&!value.equalsIgnoreCase("")){
					value = value.split("'")[1];
					System.out.println("title-{busi_screen}"+value);
					value = value.replace("%", "");
					
					Map<String, String> name_mapMap =  dicList.get(2);
					Map<String, String> parentId_mapMap =  dicList.get(3);
					
					String title_seq1 = name_mapMap.get(value);
					String parentId = parentId_mapMap.get(value);
					if(parentId!=null&&!parentId.equalsIgnoreCase("1")){
						title_seq1 = name_mapMap.get(parentId)+"-"+title_seq1;
					}
					if(title_seq1!=null&&!title_seq1.equalsIgnoreCase("")){
						entity.setW3_x_name("表象");
					}
					entity.setW3_x_name(entity.getW3_x_name()+"-"+title_seq1);
				}
			}else if(key.equalsIgnoreCase("{cust_screen}")){
				if(value!=null&&!value.equalsIgnoreCase("")){
					value = value.split("'")[1];
					System.out.println("title-{cust_screen}"+value);
				}
			}else if(key.equalsIgnoreCase("{accs_screen}")){
				if(value!=null&&!value.equalsIgnoreCase("")){
					value = value.split("'")[1];
					System.out.println("title-{accs_screen}"+value);
				}
				
				//根据传入的SQL类型来判断x轴名称
				int type = entity.getType5();
				if(type==51){
					entity.setW5_x_name("按省公司");
				}else {
					entity.setW5_x_name("按地市");
				}
			}else {
				
			}
		}
		
		return null;
	}
	
	/**
	 * 获取字典信息
	 * @return
	 */
	private List<Map<String, String>> getConfDicMaps(){
		List<Map<String, String>> list = multViewAnalyDAO.getConfDicMaps(sqlConfMap);
	
		return list;
	}
	
	//出现异常则返回界面异常信息
	private ResultToWebEntity loadChartError(String json,
			FirstViewEntity entity,String errMsg) {
		System.out.println("exception throw out...");
		System.out.println("error msg:"+errMsg);
		if(errMsg==null){
			errMsg = "null";
		}
		if(json==null||json.equalsIgnoreCase("")){
			json = this.errJson;
		}
		ResultToWebEntity resultToWebEntity = new ResultToWebEntity();
		String data_content = "{ \"label\":\"系统出现异常,请反馈给管理员处理!!!{br}错误信息:{error_msg}\",\"value\":\"\",\"color\":\"ff0000\"}";
		json = json.replace("{chartContainer}", entity.getChartContainer())
				.replace("{xAxisName}", "")
				.replace("{yAxisName}", "")
				.replace("{data_content}", data_content)
				.replace("{error_msg}", errMsg.replace("\n", ""));
		
		resultToWebEntity.setJson(json);
		System.out.println(json);
		return resultToWebEntity;
	}
	
	private ResultToWebEntity setParamLen(ResultToWebEntity entity){
		entity.setFirst_param_Len(String.valueOf(firstHistory.size()));
		entity.setSecond_param_Len(String.valueOf(secondHistory.size()));
		entity.setThird_param_Len(String.valueOf(thirdHistory.size()));
		entity.setFourth_param_Len(String.valueOf(fourthHistory.size()));
		entity.setFifth_param_Len(String.valueOf(fifthHistory.size()));
		
		return entity;
	}
	
	private String errJson= " { " +
							"          \"type\": \"column2d\", " +
							"          \"renderAt\": \"{chartContainer}\", " +
							"          \"width\": \"100%\", " +
							"          \"height\": \"100%\", " +
							"          \"dataFormat\": \"json\", " +
							"          \"dataSource\": { " +
							"                  \"chart\":{ " +
							"              \"snumbersuffix\":\"%\", " +
							"             \"syaxismaxvalue\":\"50\", " +
							"             \"showcanvasborder\":\"1\", " +
							"             \"showalternatehgridcolor\":\"0\", " +
							"             \"palettecolors\":\"#0075c2,#1aaf5d,#f2c500\", " +
							"             \"basefontcolor\":\"#000000\", " +
							"             \"baseFontSize\":\"12\", " +
							"             \"showYAxisValues\":\"0\", " +
							"             \"subcaptionfontbold\":\"0\", " +
							"             \"showborder\":\"0\", " +
							"             \"bgcolor\":\"#d3e7ff\", " +
							"             \"showshadow\":\"0\", " +
							"             \"canvasBgAlpha\":\"0\", " +
							"             \"divlinealpha\":\"0\", " +
							"             \"divlinecolor\":\"#666666\", " +
							"             \"xAxisName\":\"{xAxisName}\", " +
							"             \"yAxisName\":\"{yAxisName}\", " +
							"             \"divlinethickness\":\"1\", " +
							"             \"divlinedashed\":\"1\", " +
							"             \"divlinedashlen\":\"1\", " +
							"             \"divlinegaplen\":\"1\", " +
							"             \"showplotborder\":\"0\", " +
							"             \"showxaxisline\":\"1\", " +
							"             \"xaxislinethickness\":\"1\", " +
							"             \"usePlotGradientColor\":\"0\", " +
							"             \"showalternatehgridcolor\":\"0\", " +
							"             \"showalternatevgridcolor\":\"0\", " +
							"             \"labelFontBold\":\"1\", " +
							"             \"xAxisNameFontBold\":\"1\", " +
							"             \"legendbgalpha\":\"0\", " +
							"             \"legendborderalpha\":\"0\", " +
							"             \"legendshadow\":\"0\", " +
							"             \"legenditemfontsize\":\"12\", " +
							"             \"legenditemfontcolor\":\"#666666\", " +
							"             \"canvasBorderAlpha\":\"0\", " +
							"             \"lineAlpha\":\"50\", " +
							"             \"unescapeLinks\":\"0\", " +
							"             \"formatNumberScale\":\"0\" " +
							"                  }, " +
							"                  \"data\":[ " +
							"                      {data_content} " +
							"                  ] " +
							"          } " +
							"      } ";
}
