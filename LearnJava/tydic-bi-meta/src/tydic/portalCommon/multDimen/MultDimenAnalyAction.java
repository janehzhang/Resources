package tydic.portalCommon.multDimen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author 孙浩瑞
 *
 * 2016-5-24 上午9:46:47
 */
public class MultDimenAnalyAction {
	private MultDimenAnalyDAO multDimenAnalyDAO;

	public MultDimenAnalyDAO getMultDimenAnalyDAO() {
		return multDimenAnalyDAO;
	}

	public void setMultDimenAnalyDAO(MultDimenAnalyDAO multDimenAnalyDAO) {
		this.multDimenAnalyDAO = multDimenAnalyDAO;
	}
	
	/**
	 * fusioncharts图表数据提供
	 * @return
	 */
	public String getPicXmlData(String timeType, int type,String xName,String yName){
		List<Map<String, Object>> list = multDimenAnalyDAO.getData(timeType, type, null);
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 地图联动数据提供
	 * @return
	 */
	public String getMapLDData(String timeType, int type,String xName,String yName, String screen, String cityLevel){
		List<Map<String, Object>> list = multDimenAnalyDAO.getMapLDData(timeType, type, null, screen, cityLevel);
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 考核维度 -下钻联动数据
	 * @return
	 */
	public String getLdAsseData(String timeType, int type, String screen, String xName,String yName, String cityLevel, String mapScreen){
		List<Map<String, Object>> list = multDimenAnalyDAO.getPieData(timeType, type, screen, null, cityLevel, mapScreen);
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 考核维度 -饼图联动数据
	 * @return
	 */
	public String getPieAsseData(String timeType, int type,String xName,String yName, String screen){
		List<Map<String, Object>> list = multDimenAnalyDAO.getLDData(timeType, type, screen.replace("aa", "a"));
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 客户属性-饼图联动数据
	 * @return
	 */
	public String getPicLDData(String timeType, int type,String xName,String yName, String screen){
		List<Map<String, Object>> list = multDimenAnalyDAO.getLDData(timeType, type, screen.replace("aa", "a"));
		return serPicAxis(list, type, buildTitle(type),xName,yName);
	}
	
	/**
	 * 同比跟环比的数据
	 * @param timeType
	 * @param type
	 * @return
	 */
	public Map<String, String> getPicRatioData(String timeType, int type, String ratioType,String xName,String yName, String parentId, int level){
		
		Map<String, String> map = new HashMap<String, String>();
		
		List<Map<String, Object>> list = multDimenAnalyDAO.getAllData(timeType, type, parentId, level);
		map.put("pic", serPicAxis(list, type, buildTitle(type),xName,yName));
		map.put("ratio", serRatioAxis(list, ratioType));
		return map;
	}
	
	/**
	 * fusioncharts地图数据提供
	 * @return
	 */
	public String getMapXmlData(String timeType, int type){
		List<Map<String, Object>> list = multDimenAnalyDAO.getData(timeType, type, null);
		String xml = this.serMapAxis(list);
		return xml;
	}
	
	/**
	 * fusioncharts饼图数据提供
	 * @return
	 */
	public String getPieXmlData(String timeType, int type, String screen, String ldScreen, String cityLevel, String mapScreen){
		List<Map<String, Object>> list = multDimenAnalyDAO.getPieData(timeType, type, screen, ldScreen, cityLevel ,mapScreen);
		return serPieAxis(list);
	}
	  
	/**
	 * 钻取
	 * @param list
	 * @return
	 */
	public String getDrillXmlData(String timeType, int type, String parentId, int level, String xName){
		List<Map<String, Object>> list = multDimenAnalyDAO.getDrillData(timeType, type, parentId, level);
		return serDrillAxis(list, type, parentId, level, xName);
	}

	/**
	 * 联动
	 * @param list
	 * @return
	 */
	public String getDrLDXmlData(String timeType, int type, String parentId, int level, String xName, String cityLevel, String mapScreen){
		List<Map<String, Object>> list = multDimenAnalyDAO.getDrLDData(timeType, type, parentId, level, cityLevel, mapScreen );
		return serDrillAxis(list, type, parentId, level, xName);
	}
	
	/**
	 * 查询所有数据
	 * @param list
	 * @return
	 */
	public String getAllXmlData(String timeType, int type, String xName, String yName, String parentId, int level){
		List<Map<String, Object>> list = multDimenAnalyDAO.getAllData(timeType, type, parentId, level);
		return serAllAxis(list,xName,yName);
	}
	
	/**
	 * 查询片区数据
	 * @param list
	 * @return
	 */
	public String getMap1XmlData(String timeType, String parent,String xName,String yName){
		List<Map<String, Object>> list = multDimenAnalyDAO.getMapData(timeType, parent);
		return serMap1Axis(list,xName,yName);
	}
	
	/**
	 * 查询县级数据
	 * @param list
	 * @return
	 */
	public String getMapCountyData(String timeType, String code,String xName){
		List<Map<String, Object>> list = multDimenAnalyDAO.getCountyData(timeType, code);
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
	
	private String serDrillAxis(List<Map<String, Object>> list, int type, String parentId, int level, String x_Name){
		StringBuffer setBuffer = new StringBuffer();
		String setVar =  " <set label=\"{name}\" value=\"{value}\" color=\"{color}\" link=\"JavaScript:multDimenAnaly.showLD({parent}, "+level+", '{xName}', " + type + ");\" /> ";
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
	
}
