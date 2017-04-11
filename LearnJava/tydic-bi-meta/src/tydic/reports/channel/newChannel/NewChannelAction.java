package tydic.reports.channel.newChannel;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.yhd.utils.Pager;
import tydic.portalCommon.DateUtil;

/***
 * 渠道偏好视图 功能模块
 * @author 我爱家乡
 *
 */
public class NewChannelAction {
	
	public NewChannelDao newChannelDao ;

	public NewChannelDao getNewChannelDao() {
		return newChannelDao;
	}

	public void setNewChannelDao(NewChannelDao newChannelDao) {
		this.newChannelDao = newChannelDao;
	}
	
	/***
	 *  查询报表的展现 通过存储过程实现
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelAll_pg(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryChannelAll_pg(queryData));//查询报表的展现
		
		return map;
		
	}
	
	/***
	 * 系统后台表监控实现
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> systemApply(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.systemApply(queryData));
		return map;
		
	}
	
	
	/***
	 * 系统接口监控实现
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> systemInterface(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.systemInterface(queryData));
		return map;
		
	}
	
	
	
	/***
	 * 偏好应用-服务偏好 整体
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServicePreferencr(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelServicePreferencr(queryData));
		return map;
		
	}
	
	
	
	
	
	/***
	 * 偏好应用-渠道偏好 整体
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelPreferencr(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelSum(queryData));
		return map;
		
	}
	
	
	/***
	 * 偏好应用-服务偏好 六大细分市场&在网时长
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServiceSix(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelServiceSix(queryData));
		return map;
		
	}
	
	
	
	/***
	 * 偏好应用-渠道偏好 六大细分市场&在网时长&用户价值
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelSix(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelSix(queryData));
		return map;
		
	}
	
	/***
	 * 偏好应用-服务偏好  产品类型
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServiceTERMINAL(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelServiceTERMINAL(queryData));
		return map;
		
	}
	
	/***
	 * 偏好应用-渠道偏好  产品类型&客户等级
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelTERMINAL(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelTERMINAL(queryData));
		return map;
		
	}
	
	/***
	 * 偏好应用-服务偏好  客户等级
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServiceCUSTLEVEL(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelServiceCUSTLEVEL(queryData));
		return map;
		
	}
	
	/***
	 * 偏好应用-服务偏好  付费方式
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServicePAYMENT(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelServicePAYMENT(queryData));
		return map;
		
	}
	
	/***
	 * 偏好应用-渠道偏好  付费方式
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelPAYMENT(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.channelPAYMENT(queryData));
		return map;
		
	}
	
	/***
	 * 查询渠道服务 用户级
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelViewUser_pg(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryChannelViewUser_pg(queryData));//查询报表的展现
		
		return map;
		
	}
	/***
	 * 渠道服务 一级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerFisrt_pg(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryChannelServerFisrt_pg(queryData));//查询报表的展现
		return map;
	}
	
	/***
	 * 区域渠道服务二级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerSecond_pg(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryChannelServerSecond_pg(queryData));//查询报表的展现
		return map;
	}
	/***
	 * 区域渠道服务三级报表查询
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerThrid_pg(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryChannelServerThrid_pg(queryData));//查询报表的展现
		return map;
	}
	
	/***
	 * 区域渠道服务三级报表办理
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerThridTransaction_pg(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryChannelServerThridTransaction_pg(queryData));//查询报表的展现
		return map;
	}
	
	/***
	 * 渠道服务偏好视图
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelCustView_pg(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryChannelCustView_pg(queryData));//查询报表的展现
		
		return map;
	}
	
	/***
	 * 关键指标的展现
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryKeyAll_pg(Map<String, Object> queryData){
		
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.queryKeyAll_pg(queryData));//查询报表的展现
		
		return map;
		
	}
	
	public Map<String, Object> queryKeyAll_pg1(Map<String, Object> queryData){
			
			Map<String, Object> map=new HashMap<String,Object>(); 
			map.putAll(newChannelDao.queryKeyAll_pg1(queryData));//查询报表的展现
			
			return map;
			
	}

	/***
	 * 加载渠道类型树
	 * @param beginId
	 * @param endId
	 * @return
	 */
	public List<Map<String,Object>> queryChannelTypePathPart(String beginId,String endId){
	        return newChannelDao.queryChannelTypePathPart(beginId,endId);
	 }
    
	public List<Map<String,Object>> querySubChannelTypePart_new(String parentId){
	        return newChannelDao.querySubChannelTypePart_new(parentId);
	  }
	
	public List<Map<String,Object>> querySubChannelTypePart_new2(String parentId){
        return newChannelDao.querySubChannelTypePart_new2(parentId);
  }
	
	
	/***
	 * 查询渠道服务分类树
	 * @param beginId
	 * @param endId
	 * @return
	 */
	
	public List<Map<String,Object>> queryChannelNewServById(String beginId,String endId){
	        return newChannelDao.queryChannelNewServById(beginId, endId);
	  }
	
	/***
	 * 根据渠道服务分类树的父节点 查询子节点数据
	 * @param parentCode
	 * @return
	 */
	public List<Map<String,Object>> querySubChannelNewServId(String parentCode){
	        return newChannelDao.querySubChannelNewServId(parentCode);
	}
	
	/***
	 * 查询线上线下新渠道一级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getChannelSerNewFirst_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelSerNewFirst_Pg(queryData));
	    return map; 
	}
	//10000人工与新媒体&自助渠道的偏好系数
	public Map<String, Object> getChannelPreferIndex_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelPreferIndex_Pg(queryData));
	    return map; 
	}


	//渠道接触数
	public Map<String, Object> getChannelPreferSummary_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelPreferSummary_Pg(queryData));
	    return map; 
	}
	//渠道偏好选择
	public Map<String, Object> getChannelPreferChoose_Pg(Map<String, Object> queryData) {
	    Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
		page.setSize(Convert.toInt(queryData.get("pageCount")));
		return newChannelDao.getChannelPreferChoose_Pg(queryData, page);
	}

	public Map<String, Object> getChannelSer_Pg(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelSer_Pg(queryData));
		map.put("lineChartMap1", getChannelSumLine1(queryData));//折线图1
		map.put("lineChartMap12", getChannelSumLine12(queryData));//折线图12
		map.put("lineChartMap2", getChannelSumLine2(queryData));//折线图2
		map.put("lineChartMap22", getChannelSumLine22(queryData));//折线图22
		map.put("lineChartMap3", getChannelSumLine3(queryData));//折线图3
		map.put("lineChartMap4", getChannelSumLine4(queryData));//折线图4
	    return map; 
	}
	public Map<String, Object> changeFirstChart(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("lineChartMap1", getChannelSumLine1(queryData));//折线图1
		map.put("lineChartMap12", getChannelSumLine12(queryData));//折线图12
		map.put("lineChartMap2", getChannelSumLine2(queryData));//折线图2
		map.put("lineChartMap22", getChannelSumLine22(queryData));//折线图22
		map.put("lineChartMap3", getChannelSumLine3(queryData));//折线图3
		map.put("lineChartMap4", getChannelSumLine4(queryData));//折线图4
	    return map; 
	}
	
	public Map<String, Object> getChannelSerOther_Pg(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelSer_Pg(queryData));
	    return map; 
	}
	public Map<String, Object> getChannelSerChart_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelSerChart_Pg(queryData));
		map.put("lineChartMap", getChannelSerLine(queryData));//折线图
		map.put("barChartMap",getChannelSerBar(queryData) );//柱状图
	    return map; 
	}
	
	//按区域统计渠道服务一级、二级、三级报表
	public Map<String, Object> getAreaServerSum_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getAreaServerSum_Pg(queryData));
		//map.put("lineChartMap", getChannelSerLine(queryData));//折线图
		//map.put("barChartMap",getChannelSerBar(queryData) );//柱状图
	    return map; 
	}
	
	//渠道服务构建折线图、柱状图
	public Map getChannelSerChart(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("barChartMap", getChannelSerBar(queryData));//柱状图
		map.put("lineChartMap", getChannelSerLine(queryData));//折线图
        return map; 
	}
	
	/***
	 * 查询线上线下新渠道二级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getChannelSerNewSecond_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelSerNewSecond_Pg(queryData));
		//map.put("lineChartMap", getChannelLineChart_FirstSecThi(queryData));//折线图
		//map.put("pieChartMap",getChannelPieChart_FirstSecThi(queryData) );//饼图
	    return map; 
	}
	/***
	 * 渠道服务三级
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getChannelSerNewThird_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getChannelSerNewThird_Pg(queryData));
		//map.put("lineChartMap", getChannelLineChart_FirstSecThi(queryData));//折线图
		//map.put("pieChartMap",getChannelPieChart_FirstSecThi(queryData) );//饼图
	    return map; 
	}
	//折线图1
	public String getChannelSumLine1(Map<String,Object>queryData) throws ParseException{
			String field = MapUtils.getString(queryData, "field",null);
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			int numVdivlines =-1;
			StringBuffer  temp1=new StringBuffer();
			temp1.append("<categories>\n");
			StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	StringBuffer  temp4=new StringBuffer();
	    	
	      	String color1= "FF0000";
	      	String color2= "0000FF";
	    	String color3= "44BB74";
	    	
	    	double maxVal=0;
	        double minVal=0;
	    	
	    	String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	
	    	if("1".equals(dateType)){//周报
	    		String inWeek=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前周
	    		String inWeekArr=newChannelDao.getCurWeekDate(inWeek);//当前周的数组
	    		String inWeekStart=inWeekArr.substring(inWeekArr.length()-17,inWeekArr.length());//开始周
	    		String inWeekEnd =newChannelDao.getCurWeekDate(inWeek).substring(0,17);//结束周
	    		
	    		temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='网厅' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='WAP厅'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='客户端'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");

			    String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100001",queryData);
		  		currentList2 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100013",queryData);
		  		currentList3 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100014",queryData);

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			  	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			       	  temp1.append("<category name='第"+date1+"周' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='第"+date1+"周："+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			          temp3.append("<set value='"+currentValue2+"' hoverText='第"+date2+"周："+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='第"+date3+"周："+currentValue3+"' />\n");
			    	  date3++;
			       }
	    	}if("0".equals(dateType)){//日报
	    		String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
				String inYearMon=inEndDate.substring(0,6);//月
				String inStartDate=inYearMon+"01";
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='网厅' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='WAP厅'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='客户端'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");

			    String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100001",queryData);
		  		currentList2 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100013",queryData);
		  		currentList3 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100014",queryData);

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(inYearMon+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(inYearMon+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(inYearMon+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
	    	}
	    	if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//结束月
				String year=inMonth.substring(0,4);
				String inMonthStart=year+"01";
				
			    temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='网厅' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='WAP厅'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='客户端'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
		  		
			    String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100001",queryData);//网厅
		  		currentList2 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100013",queryData);//WAP厅
		  		currentList3 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100014",queryData);//客户端

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"月' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(year+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(year+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(year+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
	    	}
  	    	temp1.append("</categories>\n");
	        temp2.append("</dataset>\n");
	        temp3.append("</dataset>\n");
	        temp4.append("</dataset>\n");
	    lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='8' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                    " exportFileName='linechart1' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        lineChartMap.append(temp1).append(temp2).append(temp3).append(temp4);
        lineChartMap.append("</chart>");
    return lineChartMap.toString();
	}
	//折线图12
	public String getChannelSumLine12(Map<String,Object>queryData) throws ParseException{
			String field = MapUtils.getString(queryData, "field",null);
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			int numVdivlines =-1;
			StringBuffer  temp1=new StringBuffer();
			temp1.append("<categories>\n");
	      	StringBuffer  temp5=new StringBuffer();
	      	StringBuffer  temp6=new StringBuffer();
	      	StringBuffer  temp7=new StringBuffer();
	
	      	String color4= "FF0000";
	      	String color5= "0000FF";
	      	String color6= "44BB74";
	    	
	    	double maxVal=0;
	        double minVal=0;
	    	
	    	String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	
	    	if("1".equals(dateType)){//周报
	    		String inWeek=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前周
	    		String inWeekArr=newChannelDao.getCurWeekDate(inWeek);//当前周的数组
	    		String inWeekStart=inWeekArr.substring(inWeekArr.length()-17,inWeekArr.length());//开始周
	    		String inWeekEnd =newChannelDao.getCurWeekDate(inWeek).substring(0,17);//结束周
	    		
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='短厅'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
			    temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color5+"' seriesName='自助终端'  color='"+color5+"' anchorBorderColor='FFFFFF'>\n");
			    temp7.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color6+"' seriesName='电商渠道'  color='"+color6+"' anchorBorderColor='FFFFFF'>\n");
		  		String currentValue4="0";
		  		String currentValue5="0";
		  		String currentValue6="0";
		
		  		List<Map<String, Object>> currentList4=null;
		  		List<Map<String, Object>> currentList5=null;
		  		List<Map<String, Object>> currentList6=null;
		  		
		  		currentList4 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100007",queryData);
		  		currentList5 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100012",queryData);
		  		currentList6 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100018",queryData);//电商渠道

		        int date4=1;
		        int date5=1;
		        int date6=1;
		        
		        for (Map<String, Object> key4 : currentList4){
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			  	      temp1.append("<category name='第"+date4+"周' />\n");
			          temp5.append("<set value='"+currentValue4+"' hoverText='第"+date4+"周："+currentValue4+"' />\n");
			    	  date4++;
			    	  numVdivlines++;
			       }
		        for (Map<String, Object> key5 : currentList5){
		        	  currentValue5=String.valueOf(key5.get(field) == null ? "0": key5.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue5));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue5));
			          temp6.append("<set value='"+currentValue5+"' hoverText='第"+date5+"周："+currentValue5+"' />\n");
			    	  date5++;
			       }
		        for (Map<String, Object> key6 : currentList6){
		        	  currentValue6=String.valueOf(key6.get(field) == null ? "0": key6.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue6));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue6));
			          temp7.append("<set value='"+currentValue6+"' hoverText='第"+date6+"周："+currentValue6+"' />\n");
			    	  date6++;
			       }
	    	}if("0".equals(dateType)){//日报
	    		String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
				String inYearMon=inEndDate.substring(0,6);//月
				String inStartDate=inYearMon+"01";
				
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='短厅'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
			    temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color5+"' seriesName='自助终端'  color='"+color5+"' anchorBorderColor='FFFFFF'>\n");
			    temp7.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color6+"' seriesName='电商渠道'  color='"+color6+"' anchorBorderColor='FFFFFF'>\n");
			    
		  		String currentValue4="0";
		  		String currentValue5="0";
		  		String currentValue6="0";
		
		  		List<Map<String, Object>> currentList4=null;
		  		List<Map<String, Object>> currentList5=null;
		  		List<Map<String, Object>> currentList6=null;
		  		
		  		currentList4 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100007",queryData);
		  		currentList5 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100012",queryData);
		  		currentList6 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100018",queryData);//电商渠道

		        int date4=1;
		        int date5=1;
		        int date6=1;
		        
		        for (Map<String, Object> key4 : currentList4){
		        	  String show="";
		    	      if(date4<10){
		    	    	  show="0"+date4;
		    	      }else{
		    	    	  show=date4+"";
		    	      }
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			  	      temp1.append("<category name='"+date4+"' />\n");
			          temp5.append("<set value='"+currentValue4+"' hoverText='"+(inYearMon+show)+"： "+currentValue4+"' />\n");
			    	  date4++;
			    	  numVdivlines++;
			       }
		        for (Map<String, Object> key5 : currentList5){
		        	  String show="";
		    	      if(date5<10){
		    	    	  show="0"+date5;
		    	      }else{
		    	    	  show=date5+"";
		    	      }
		        	  currentValue5=String.valueOf(key5.get(field) == null ? "0": key5.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue5));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue5));
			          temp6.append("<set value='"+currentValue5+"' hoverText='"+(inYearMon+show)+"： "+currentValue5+"' />\n");
			    	  date5++;
			       }
		        for (Map<String, Object> key6 : currentList6){
		        	  String show="";
		    	      if(date6<10){
		    	    	  show="0"+date6;
		    	      }else{
		    	    	  show=date6+"";
		    	      }
		        	  currentValue6=String.valueOf(key6.get(field) == null ? "0": key6.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue6));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue6));
			  	      temp7.append("<set value='"+currentValue6+"' hoverText='"+(inYearMon+show)+"： "+currentValue6+"' />\n");
			    	  date6++;
			       }
	    	}
	    	if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//结束月
				String year=inMonth.substring(0,4);
				String inMonthStart=year+"01";
				
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='短厅'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
			    temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color5+"' seriesName='自助终端'  color='"+color5+"' anchorBorderColor='FFFFFF'>\n");
			    temp7.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color6+"' seriesName='电商渠道'  color='"+color6+"' anchorBorderColor='FFFFFF'>\n");
			    
		  		String currentValue4="0";
		  		String currentValue5="0";
		  		String currentValue6="0";
		
		  		List<Map<String, Object>> currentList4=null;
		  		List<Map<String, Object>> currentList5=null;
		  		List<Map<String, Object>> currentList6=null;
		  		
		  		currentList4 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100007",queryData);//短厅
		  		currentList5 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100012",queryData);//自助终端
		  		currentList6 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100018",queryData);//电商渠道

		        int date4=1;
		        int date5=1;
		        int date6=1;
		        
		        for (Map<String, Object> key4 : currentList4){
		        	  String show="";
		    	      if(date4<10){
		    	    	  show="0"+date4;
		    	      }else{
		    	    	  show=date4+"";
		    	      }
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			          temp5.append("<set value='"+currentValue4+"' hoverText='"+(year+show)+"： "+currentValue4+"' />\n");
			    	  date4++;
			    	  numVdivlines++;
			       }
		        for (Map<String, Object> key5 : currentList5){
		        	  String show="";
		    	      if(date5<10){
		    	    	  show="0"+date5;
		    	      }else{
		    	    	  show=date5+"";
		    	      }
		        	  currentValue5=String.valueOf(key5.get(field) == null ? "0": key5.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue5));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue5));
			  	      temp1.append("<category name='"+date5+"月' />\n");
			          temp6.append("<set value='"+currentValue5+"' hoverText='"+(year+show)+"： "+currentValue5+"' />\n");
			    	  date5++;
			       }
		        for (Map<String, Object> key6 : currentList6){
		        	  String show="";
		    	      if(date6<10){
		    	    	  show="0"+date6;
		    	      }else{
		    	    	  show=date6+"";
		    	      }
		        	  currentValue6=String.valueOf(key6.get(field) == null ? "0": key6.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue6));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue6));
			  	      temp7.append("<set value='"+currentValue6+"' hoverText='"+(year+show)+"： "+currentValue6+"' />\n");
			    	  date6++;
			       }
	    	}
  	    	temp1.append("</categories>\n");
	        temp5.append("</dataset>\n");
	        temp6.append("</dataset>\n");
	        temp7.append("</dataset>\n");
	    lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='8' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                    " exportFileName='linechart2' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        lineChartMap.append(temp1).append(temp5).append(temp6).append(temp7);
        lineChartMap.append("</chart>");
    return lineChartMap.toString();
	}
	
	//折线图2
	public String getChannelSumLine2(Map<String,Object>queryData) throws ParseException{
			String field = MapUtils.getString(queryData, "field",null);
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			int numVdivlines =-1;
			StringBuffer  temp1=new StringBuffer();
			temp1.append("<categories>\n");
			StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	StringBuffer  temp4=new StringBuffer();
	      	StringBuffer  temp5=new StringBuffer();
	
	      	String color1= "FF0000";
	      	String color2= "0000FF";
	    	String color3= "44BB74";
	    	String color4= "CDAD00";
	    	
	    	double maxVal=0;
	        double minVal=0;
	    	
	    	String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	
	    	if("1".equals(dateType)){//周报
	    		String inWeek=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前周
	    		String inWeekArr=newChannelDao.getCurWeekDate(inWeek);//当前周的数组
	    		String inWeekStart=inWeekArr.substring(inWeekArr.length()-17,inWeekArr.length());//开始周
	    		String inWeekEnd =newChannelDao.getCurWeekDate(inWeek).substring(0,17);//结束周
	    		
	    		temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='10000号自助' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='10001号自助'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='速拨干线'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='10000号人工'    color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
		  		String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		  		String currentValue4="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		List<Map<String, Object>> currentList4=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100004",queryData);//10000号自助
		  		currentList2 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100005",queryData);//10001号自助
		  		currentList3 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100006",queryData);//速播
		  		currentList4 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200005",queryData);//10000号人工
		  		
		        int date1=1;
		        int date2=1;
		        int date3=1;
		        int date4=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			  	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			       	  temp1.append("<category name='第"+date1+"周' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='第"+date1+"周："+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			          temp3.append("<set value='"+currentValue2+"' hoverText='第"+date2+"周："+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='第"+date3+"周："+currentValue3+"' />\n");
			    	  date3++;
			       }
		        for (Map<String, Object> key4 : currentList4){
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			          temp5.append("<set value='"+currentValue4+"' hoverText='第"+date4+"周："+currentValue4+"' />\n");
			    	  date4++;
			       }
	    	}if("0".equals(dateType)){//日报
	    		String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
				String inYearMon=inEndDate.substring(0,6);//月
				String inStartDate=inYearMon+"01";
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='10000号自助' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='10001号自助'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='速拨干线'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='10000号人工'    color='"+color4+"' anchorBorderColor='FFFFFF'>\n");

			    String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		  		String currentValue4="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		List<Map<String, Object>> currentList4=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100004",queryData);//10000号自助
		  		currentList2 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100005",queryData);//10001号自助
		  		currentList3 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100006",queryData);//速播
		  		currentList4 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200005",queryData);//10000号人工

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        int date4=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(inYearMon+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(inYearMon+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(inYearMon+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
		        for (Map<String, Object> key4 : currentList4){
			        	 String show="";
			    	      if(date4<10){
			    	    	  show="0"+date4;
			    	      }else{
			    	    	  show=date4+"";
			    	      }
			        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
					      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
				  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
				          temp5.append("<set value='"+currentValue4+"' hoverText='"+(inYearMon+show)+"： "+currentValue4+"' />\n");
				    	  date4++;
				 }
	    	}
	    	
	    	if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//结束月
				String year=inMonth.substring(0,4);
				String inMonthStart=year+"01";
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='10000号自助' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='10001号自助'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='速拨干线'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='10000号人工'    color='"+color4+"' anchorBorderColor='FFFFFF'>\n");

			    String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		  		String currentValue4="0";
				
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		List<Map<String, Object>> currentList4=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100004",queryData);//10000号自助
		  		currentList2 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100005",queryData);//10001号自助
		  		currentList3 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100006",queryData);//速播
		  		currentList4 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200005",queryData);//10000号人工

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        int date4=1;
		        
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"月' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(year+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(year+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(year+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
		        for (Map<String, Object> key4 : currentList4){
		        	String show="";
		    	      if(date4<10){
		    	    	  show="0"+date4;
		    	      }else{
		    	    	  show=date4+"";
		    	      }
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			          temp5.append("<set value='"+currentValue4+"' hoverText='"+(year+show)+"："+currentValue4+"' />\n");
			    	  date4++;
			 }
	    	}
  	    	temp1.append("</categories>\n");
	        temp2.append("</dataset>\n");
	        temp3.append("</dataset>\n");
	        temp4.append("</dataset>\n");
	        temp5.append("</dataset>\n");
	    lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0' yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                    " exportFileName='linechart3' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        lineChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5);
        lineChartMap.append("</chart>");
    return lineChartMap.toString();
	}
	
	//折线图22
	public String getChannelSumLine22(Map<String,Object>queryData) throws ParseException{
			String field = MapUtils.getString(queryData, "field",null);
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			int numVdivlines =-1;
			StringBuffer  temp1=new StringBuffer();
			temp1.append("<categories>\n");
	      	StringBuffer  temp5=new StringBuffer();
	      	StringBuffer  temp6=new StringBuffer();
	      	StringBuffer  temp7=new StringBuffer();
	
	      	String color4= "FF0000";
	      	String color5= "0000FF";
	    	String color6= "44BB74";
	    	
	    	double maxVal=0;
	        double minVal=0;
	    	
	    	String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	
	    	if("1".equals(dateType)){//周报
	    		String inWeek=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前周
	    		String inWeekArr=newChannelDao.getCurWeekDate(inWeek);//当前周的数组
	    		String inWeekStart=inWeekArr.substring(inWeekArr.length()-17,inWeekArr.length());//开始周
	    		String inWeekEnd =newChannelDao.getCurWeekDate(inWeek).substring(0,17);//结束周
	    		
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='QQ客服自助'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
			    temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color5+"' seriesName='微信客服自助'  color='"+color5+"' anchorBorderColor='FFFFFF'>\n");
			    temp7.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color6+"' seriesName='易信客服自助'  color='"+color6+"' anchorBorderColor='FFFFFF'>\n");

		  		String currentValue4="0";
		  		String currentValue5="0";
		  		String currentValue6="0";
		
		  		List<Map<String, Object>> currentList4=null;
		  		List<Map<String, Object>> currentList5=null;
		  		List<Map<String, Object>> currentList6=null;
		  		
		  		currentList4 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100015",queryData);//qq自助
		  		currentList5 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100016",queryData);//微信自助
		  		currentList6 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"100017",queryData);//易信自助

		        int date4=1;
		        int date5=1;
		        int date6=1;
		        
		        for (Map<String, Object> key4 : currentList4){
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			  	      temp1.append("<category name='第"+date4+"周' />\n");
			          temp5.append("<set value='"+currentValue4+"' hoverText='第"+date4+"周："+currentValue4+"' />\n");
			    	  date4++;
			    	  numVdivlines++;
			       }
		        for (Map<String, Object> key5 : currentList5){
		        	  currentValue5=String.valueOf(key5.get(field) == null ? "0": key5.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue5));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue5));
			          temp6.append("<set value='"+currentValue5+"' hoverText='第"+date5+"周："+currentValue5+"' />\n");
			    	  date5++;
			       }
		        for (Map<String, Object> key6 : currentList6){
		        	  currentValue6=String.valueOf(key6.get(field) == null ? "0": key6.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue6));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue6));
			          temp7.append("<set value='"+currentValue6+"' hoverText='第"+date6+"周："+currentValue6+"' />\n");
			    	  date6++;
			    	  numVdivlines++;
			       }
	    	}if("0".equals(dateType)){//日报
	    		String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
				String inYearMon=inEndDate.substring(0,6);//月
				String inStartDate=inYearMon+"01";
				
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='QQ客服自助'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
			    temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color5+"' seriesName='微信客服自助'  color='"+color5+"' anchorBorderColor='FFFFFF'>\n");
			    temp7.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color6+"' seriesName='易信客服自助'  color='"+color6+"' anchorBorderColor='FFFFFF'>\n");
			    
		  		String currentValue4="0";
		  		String currentValue5="0";
		  		String currentValue6="0";
		
		  		List<Map<String, Object>> currentList4=null;
		  		List<Map<String, Object>> currentList5=null;
		  		List<Map<String, Object>> currentList6=null;
		  		
		  		currentList4 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100015",queryData);//qq自助
		  		currentList5 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100016",queryData);//微信自助
		  		currentList6 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"100017",queryData);//易信自助

		        int date4=1;
		        int date5=1;
		        int date6=1;
		       
		        for (Map<String, Object> key4 : currentList4){
		        	  String show="";
		    	      if(date4<10){
		    	    	  show="0"+date4;
		    	      }else{
		    	    	  show=date4+"";
		    	      }
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			          temp5.append("<set value='"+currentValue4+"' hoverText='"+(inYearMon+show)+"： "+currentValue4+"' />\n");
			    	  date4++;
			       }
		        for (Map<String, Object> key5 : currentList5){
		        	  String show="";
		    	      if(date5<10){
		    	    	  show="0"+date5;
		    	      }else{
		    	    	  show=date5+"";
		    	      }
		        	  currentValue5=String.valueOf(key5.get(field) == null ? "0": key5.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue5));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue5));
			  	      temp1.append("<category name='"+date5+"' />\n");
			          temp6.append("<set value='"+currentValue5+"' hoverText='"+(inYearMon+show)+"： "+currentValue5+"' />\n");
			    	  date5++;
			       }
		        for (Map<String, Object> key6 : currentList6){
		        	  String show="";
		    	      if(date6<10){
		    	    	  show="0"+date6;
		    	      }else{
		    	    	  show=date6+"";
		    	      }
		        	  currentValue6=String.valueOf(key6.get(field) == null ? "0": key6.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue6));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue6));
			          temp7.append("<set value='"+currentValue6+"' hoverText='"+(inYearMon+show)+"： "+currentValue6+"' />\n");
			    	  date6++;
			       }
	    	}
	    	if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//结束月
				String year=inMonth.substring(0,4);
				String inMonthStart=year+"01";
				
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='QQ客服自助'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
			    temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color5+"' seriesName='微信客服自助'  color='"+color5+"' anchorBorderColor='FFFFFF'>\n");
			    temp7.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color6+"' seriesName='易信客服自助'  color='"+color6+"' anchorBorderColor='FFFFFF'>\n");
			    
		  		String currentValue4="0";
		  		String currentValue5="0";
		  		String currentValue6="0";
		
		  		List<Map<String, Object>> currentList4=null;
		  		List<Map<String, Object>> currentList5=null;
		  		List<Map<String, Object>> currentList6=null;
		  		
		  		currentList4 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100015",queryData);//qq自助
		  		currentList5 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100016",queryData);//微信自助
		  		currentList6 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"100017",queryData);//易信自助

		        int date4=1;
		        int date5=1;
		        int date6=1;
		       
		        for (Map<String, Object> key4 : currentList4){
		        	  String show="";
		    	      if(date4<10){
		    	    	  show="0"+date4;
		    	      }else{
		    	    	  show=date4+"";
		    	      }
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			  	      temp1.append("<category name='"+date4+"月' />\n");
			          temp5.append("<set value='"+currentValue4+"' hoverText='"+(year+show)+"： "+currentValue4+"' />\n");
			    	  date4++;
			       }
		        for (Map<String, Object> key5 : currentList5){
		        	  String show="";
		    	      if(date5<10){
		    	    	  show="0"+date5;
		    	      }else{
		    	    	  show=date5+"";
		    	      }
		        	  currentValue5=String.valueOf(key5.get(field) == null ? "0": key5.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue5));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue5));
			          temp6.append("<set value='"+currentValue5+"' hoverText='"+(year+show)+"： "+currentValue5+"' />\n");
			    	  date5++;
			       }
		        for (Map<String, Object> key6 : currentList6){
		        	  String show="";
		    	      if(date6<10){
		    	    	  show="0"+date6;
		    	      }else{
		    	    	  show=date6+"";
		    	      }
		        	  currentValue6=String.valueOf(key6.get(field) == null ? "0": key6.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue6));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue6));
			          temp7.append("<set value='"+currentValue6+"' hoverText='"+(year+show)+"： "+currentValue6+"' />\n");
			    	  date6++;
			       }
	    	}
  	    	temp1.append("</categories>\n");
	        temp5.append("</dataset>\n");
	        temp6.append("</dataset>\n");
	        temp7.append("</dataset>\n");
	    lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0' yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                    " exportFileName='linechart4' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        lineChartMap.append(temp1).append(temp5).append(temp6).append(temp7);
        lineChartMap.append("</chart>");
    return lineChartMap.toString();
	}
	//折线图3
	public String getChannelSumLine3(Map<String,Object>queryData) throws ParseException{
			String field = MapUtils.getString(queryData, "field",null);
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			int numVdivlines =-1;
			StringBuffer  temp1=new StringBuffer();
			temp1.append("<categories>\n");
			StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	StringBuffer  temp4=new StringBuffer();
	      	StringBuffer  temp5=new StringBuffer();
	      	
	    	String color1= "FF0000";
	      	String color2= "0000FF";
	    	String color3= "44BB74";
	    	String color4= "CDAD00";
	    	
	    	double maxVal=0;
	        double minVal=0;
	    	
	    	String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	
	    	if("1".equals(dateType)){//周报
	    		String inWeek=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前周
	    		String inWeekArr=newChannelDao.getCurWeekDate(inWeek);//当前周的数组
	    		String inWeekStart=inWeekArr.substring(inWeekArr.length()-17,inWeekArr.length());//开始周
	    		String inWeekEnd =newChannelDao.getCurWeekDate(inWeek).substring(0,17);//结束周
	    		
	    		temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='自有厅' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='专营店'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='开放渠道'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='直销渠道'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
		  		String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		  		String currentValue4="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		List<Map<String, Object>> currentList4=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200001",queryData);
		  		currentList2 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200002",queryData);
		  		currentList3 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200003",queryData);
		  		currentList4 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200004",queryData);

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        int date4=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			  	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			       	  temp1.append("<category name='第"+date1+"周' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='第"+date1+"周："+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			          temp3.append("<set value='"+currentValue2+"' hoverText='第"+date2+"周："+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='第"+date3+"周："+currentValue3+"' />\n");
			    	  date3++;
			       }
		        for (Map<String, Object> key4 : currentList4){
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			          temp5.append("<set value='"+currentValue4+"' hoverText='第"+date4+"周："+currentValue4+"' />\n");
			    	  date4++;
			       }
	    	}if("0".equals(dateType)){//日报
	    		String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
				String inYearMon=inEndDate.substring(0,6);//月
				String inStartDate=inYearMon+"01";
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='自有厅' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='专营店'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='开放渠道'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='直销渠道'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
		  		String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		  		String currentValue4="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		List<Map<String, Object>> currentList4=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200001",queryData);
		  		currentList2 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200002",queryData);
		  		currentList3 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200003",queryData);
		  		currentList4 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200004",queryData);

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        int date4=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(inYearMon+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(inYearMon+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(inYearMon+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
		        for (Map<String, Object> key4 : currentList4){
		        	  String show="";
		    	      if(date4<10){
		    	    	  show="0"+date4;
		    	      }else{
		    	    	  show=date4+"";
		    	      }
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			          temp5.append("<set value='"+currentValue4+"' hoverText='"+(inYearMon+show)+"： "+currentValue4+"' />\n");
			    	  date4++;
			       }
	    	}
	    	if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//结束月
				String year=inMonth.substring(0,4);
				String inMonthStart=year+"01";
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='自有厅' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='专营店'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='开放渠道'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
			    temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='直销渠道'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
		  		
			    String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		  		String currentValue4="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		List<Map<String, Object>> currentList4=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200001",queryData);//网厅
		  		currentList2 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200002",queryData);//WAP厅
		  		currentList3 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200003",queryData);//客户端
		  		currentList4 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200004",queryData);//短厅

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        int date4=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"月' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(year+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(year+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(year+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
		        for (Map<String, Object> key4 : currentList4){
		        	  String show="";
		    	      if(date4<10){
		    	    	  show="0"+date4;
		    	      }else{
		    	    	  show=date4+"";
		    	      }
		        	  currentValue4=String.valueOf(key4.get(field) == null ? "0": key4.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue4));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue4));
			          temp5.append("<set value='"+currentValue4+"' hoverText='"+(year+show)+"： "+currentValue4+"' />\n");
			    	  date4++;
			       }
	    	}
  	    	temp1.append("</categories>\n");
	        temp2.append("</dataset>\n");
	        temp3.append("</dataset>\n");
	        temp4.append("</dataset>\n");
	        temp5.append("</dataset>\n");
	    lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0' yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                    " exportFileName='linechart5' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        lineChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5);
        lineChartMap.append("</chart>");
    return lineChartMap.toString();
	}
	
	//折线图4
	public String getChannelSumLine4(Map<String,Object>queryData) throws ParseException{
			String field = MapUtils.getString(queryData, "field",null);
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			int numVdivlines =-1;
			StringBuffer  temp1=new StringBuffer();
			temp1.append("<categories>\n");
			StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	StringBuffer  temp4=new StringBuffer();
	
	      	String color1= "FF0000";
	      	String color2= "0000FF";
	    	String color3= "44BB74";
	    	
	    	double maxVal=0;
	        double minVal=0;
	    	
	    	String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	
	    	if("1".equals(dateType)){//周报
	    		String inWeek=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前周
	    		String inWeekArr=newChannelDao.getCurWeekDate(inWeek);//当前周的数组
	    		String inWeekStart=inWeekArr.substring(inWeekArr.length()-17,inWeekArr.length());//开始周
	    		String inWeekEnd =newChannelDao.getCurWeekDate(inWeek).substring(0,17);//结束周
	    		
	    		temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='QQ客服人工' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='微信客服人工'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='易信客服人工'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
		  		String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200008",queryData);
		  		currentList2 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200009",queryData);
		  		currentList3 = newChannelDao.getChannelSerListData(inWeekStart,inWeekEnd,"200010",queryData);

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			  	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			       	  temp1.append("<category name='第"+date1+"周' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='第"+date1+"周："+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			          temp3.append("<set value='"+currentValue2+"' hoverText='第"+date2+"周："+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='第"+date3+"周："+currentValue3+"' />\n");
			    	  date3++;
			       }
	    	}if("0".equals(dateType)){//日报
	    		String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
				String inYearMon=inEndDate.substring(0,6);//月
				String inStartDate=inYearMon+"01";
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='QQ客服人工' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='微信客服人工'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='易信客服人工'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
		  		String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200008",queryData);//网厅
		  		currentList2 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200009",queryData);//WAP厅
		  		currentList3 = newChannelDao.getChannelSerListData(inStartDate,inEndDate,"200010",queryData);//客户端

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(inYearMon+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(inYearMon+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(inYearMon+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
	    	}
	    	if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//结束月
				String year=inMonth.substring(0,4);
				String inMonthStart=year+"01";
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='QQ客服人工' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
			    temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='微信客服人工'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
			    temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='易信客服人工'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
		  		
			    String currentValue1="0";
		  		String currentValue2="0";
		  		String currentValue3="0";
		
		  		List<Map<String, Object>> currentList1=null;
		  		List<Map<String, Object>> currentList2=null;
		  		List<Map<String, Object>> currentList3=null;
		  		
		  		currentList1 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200008",queryData);//网厅
		  		currentList2 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200009",queryData);//WAP厅
		  		currentList3 = newChannelDao.getChannelSerListData(inMonthStart,inMonth,"200010",queryData);//客户端

		        int date1=1;
		        int date2=1;
		        int date3=1;
		        for (Map<String, Object> key1 : currentList1){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
		        	  currentValue1=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue1));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue1));
			  	      temp1.append("<category name='"+date1+"月' />\n");
			          temp2.append("<set value='"+currentValue1+"' hoverText='"+(year+show)+"： "+currentValue1+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : currentList2){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
		        	  currentValue2=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue2));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue2));
			  	      temp3.append("<set value='"+currentValue2+"' hoverText='"+(year+show)+"： "+currentValue2+"' />\n");
			    	  date2++;
			       }
		        for (Map<String, Object> key3 : currentList3){
		        	  String show="";
		    	      if(date3<10){
		    	    	  show="0"+date3;
		    	      }else{
		    	    	  show=date3+"";
		    	      }
		        	  currentValue3=String.valueOf(key3.get(field) == null ? "0": key3.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue3));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue3));
			          temp4.append("<set value='"+currentValue3+"' hoverText='"+(year+show)+"： "+currentValue3+"' />\n");
			    	  date3++;
			       }
	    	}
  	    	temp1.append("</categories>\n");
	        temp2.append("</dataset>\n");
	        temp3.append("</dataset>\n");
	        temp4.append("</dataset>\n");
	    lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                    " exportFileName='linechart6' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        lineChartMap.append(temp1).append(temp2).append(temp3).append(temp4);
        lineChartMap.append("</chart>");
    return lineChartMap.toString();
	}
	//折线图
	private String getChannelSerLine(Map<String, Object> queryData) {
		StringBuffer  lineChartMap=new StringBuffer();
		//折线展示
    	String field =       MapUtils.getString(queryData, "field",null);
		String currentColor= "FBC62A";//当月
    	String lastColor=    "71B359";//上月
    	
    	double maxVal=0;
        double minVal=0;
        
        int numVdivlines =-1;
    	
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();

		String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	if("1".equals(dateType)){//周报
	    		String inWeek=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
	    		String inWeekArr=newChannelDao.getCurWeekDate(inWeek);
	    		String inWeekStart=inWeekArr.substring(inWeekArr.length()-17,inWeekArr.length());
	    		String inWeekEnd =newChannelDao.getCurWeekDate(inWeek).substring(0,17);
	    		
	    		String lastWeekArr=newChannelDao.getLastWeekDate(inWeek);
	    		String lastWeekEnd=newChannelDao.getLastWeekDate(inWeek).substring(0,17);
	    		String lastWeekStart =lastWeekArr.substring(lastWeekArr.length()-17,lastWeekArr.length());
	    		
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='当月'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='上月'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		        
		        List<Map<String, Object>> currentList=null;
		        List<Map<String, Object>> lastList=null;
		        currentList = newChannelDao.getChannelSerLineData(inWeekStart,inWeekEnd,queryData);
		        lastList =    newChannelDao.getChannelSerLineData(lastWeekStart,lastWeekEnd,queryData);
		        int date1=1;
		        int date2=1;
		        for (Map<String, Object> key1 : currentList){
			       	  String curValue=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
			       	  temp1.append("<category name='第"+date1+"周' />\n");
			          temp2.append("<set value='"+curValue+"' hoverText='第"+date1+"周："+curValue+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : lastList){
			       	  String lastValue=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
			          temp3.append("<set value='"+lastValue+"' hoverText='第"+date2+"周："+lastValue+"' />\n");
			    	  numVdivlines++;
			    	  date2++;
			       }
	    	}
	    	if("2".equals(dateType)){//月报
	    		String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
				if (inMonth.length()==0) {
					inMonth="201507";
				}
				
				String month=inMonth.substring(4,6);
				String year=inMonth.substring(0,4);
				String inMonthStart=year+"01";
				String lastMonth=(Integer.parseInt(year)-1)+month;//去年月
				String lastMonthStart=(Integer.parseInt(year)-1)+"01";
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inMonth+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastMonth+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		        
		        List<Map<String, Object>> currentList=null;
		        List<Map<String, Object>> lastList=null;
		        currentList = newChannelDao.getChannelSerLineData(inMonthStart,inMonth,queryData);
		        lastList =    newChannelDao.getChannelSerLineData(lastMonthStart,lastMonth,queryData);
		        int date1=1;
		        int date2=1;
		        for (Map<String, Object> key1 : currentList){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
			       	  String curValue=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	    maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			  	        minVal=compareMinValue(minVal,Double.parseDouble(curValue));
			       	  temp1.append("<category name='"+date1+"月' />\n");
			          temp2.append("<set value='"+curValue+"' hoverText='"+(year+show)+"： "+curValue+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : lastList){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
			       	  String lastValue=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
			          temp3.append("<set value='"+lastValue+"' hoverText='"+(year+show)+"： "+lastValue+"' />\n");
			    	  numVdivlines++;
			    	  date2++;
			       }
	    	}
	    	if("0".equals(dateType)){//日报
	    		String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
				String inYearMon=inEndDate.substring(0,6);//月
				String inStartDate=inYearMon+"01";
				String inEndDay=inEndDate.substring(6,8);//结束日
				String lastYearMon=DateUtil.getLastMon(inYearMon);//上月
				String lastStartDate=lastYearMon+"01";
				String lastEndDate=lastYearMon+inEndDay;

				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");

				List<Map<String, Object>> currentList=null;
		        List<Map<String, Object>> lastList=null;
				currentList=newChannelDao.getChannelSerLineData(inStartDate,inEndDate,queryData);//本月
				lastList=newChannelDao.getChannelSerLineData(lastStartDate,lastEndDate,queryData);//上月
				int date1=1;
		        int date2=1;
				
		        for (Map<String, Object> key1 : currentList){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
			       	  String curValue=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
			       	  temp1.append("<category name='"+date1+"' />\n");
			          temp2.append("<set value='"+curValue+"' hoverText='"+(inYearMon+show)+"： "+curValue+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : lastList){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
			       	  String lastValue=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
			          temp3.append("<set value='"+lastValue+"' hoverText='"+(lastYearMon+show)+"： "+lastValue+"' />\n");
			    	  numVdivlines++;
			    	  date2++;
			       }
	    	}
	    	if(maxVal==minVal){
				minVal=0;
			}
		         temp1.append("</categories>\n");
		         temp2.append("</dataset>\n");
		         temp3.append("</dataset>\n");//yaxisminvalue='10' yaxismaxvalue='1'double maxVal=0; double minVal=0;
		         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
		     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
		     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
		     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
		     	        " numdivlines='4' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
		     	        " chartRightMargin='20' chartLeftMargin='10'"+
		     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	                    " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
		         lineChartMap.append(temp1).append(temp2).append(temp3);
		         lineChartMap.append("</chart>");
		return lineChartMap.toString(); 
  }
	
	private String getChannelSerBar(Map<String, Object> queryData) {
		StringBuffer  barChartMap=new StringBuffer();
		//柱状图展示
    	String field =       MapUtils.getString(queryData, "field",null);
		String currentColor= "FBC62A";//当月
    	String lastColor=    "71B359";//上月
    	
    	double maxVal=0;
        double minVal=0;
        
        int numVdivlines =-1;
    	
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();

		String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
	    	if("1".equals(dateType)){//周报
				//宽带新装
	            String inWeek=MapUtils.getString(queryData, "startDate", null);//当前周
				
				String curDateStart=inWeek.substring(0,8);//当前周开始日期
				String curDateYear=curDateStart.substring(0,4);
				String curDateMon=curDateStart.substring(4,6);
				String curDateDay=curDateStart.substring(6,8);
				String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
				
				String curDateEnd=inWeek.substring(9,17);//当前周结束日期
				String dateYear=curDateEnd.substring(0,4);
				String dateMon=curDateEnd.substring(4,6);
				String dateDay=curDateEnd.substring(6,8);
				String dateTime=dateYear+"-"+dateMon+"-"+dateDay;
				
				String lastDateStart=DateUtil.lastDay(curDateTime, -7);
				String lastDateEnd=DateUtil.lastDay(dateTime, -7);
				
				String lastWeek=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

				temp2.append("<dataset  seriesName='" + inWeek + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
				temp3.append("<dataset  seriesName='" + lastWeek + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

				List<Map<String,Object>>barList1=newChannelDao.getChannelSerBarData(inWeek,inWeek,queryData);//本月
				List<Map<String,Object>>barList2=newChannelDao.getChannelSerBarData(lastWeek,lastWeek,queryData);//上月
				
			        //柱状图_周报表
				for (Map<String, Object> key1 : barList1) {
					String curValue = "0";
					curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
					maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(curValue));  
					temp1.append("<category label='" + key1.get("CHANNEL_TYPE_NAME_NEW")+ "'/>\n");
					temp2.append("<set      value='" + curValue + "' />\n");
				}for (Map<String, Object> key2 : barList2) {
					String lastValue = "0";
				    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
		    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));  
					temp3.append("<set      value='" + lastValue + "' />\n");
					numVdivlines=numVdivlines+1;
				}
	    	}
	    	if("2".equals(dateType)){//月报
	    		String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
	    		if(inMonth.length()==0){
	    			inMonth="201507";
	    		}
				String lastMonth=DateUtil.getLastMon(inMonth);

				temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
				temp3.append("<dataset  seriesName='" + lastMonth + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
				
				List<Map<String,Object>>barList1=newChannelDao.getChannelSerBarData(inMonth,inMonth,queryData);//本月
				List<Map<String,Object>>barList2=newChannelDao.getChannelSerBarData(lastMonth,lastMonth,queryData);//上月
				
			        //柱状图_周报表
				for (Map<String, Object> key1 : barList1) {
					String curValue = "0";
					curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
					maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(curValue));  
					temp1.append("<category label='" + key1.get("CHANNEL_TYPE_NAME_NEW")+ "'/>\n");
					temp2.append("<set      value='" + curValue + "' />\n");
				}for (Map<String, Object> key2 : barList2) {
					String lastValue = "0";
				    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));  
					temp3.append("<set      value='" + lastValue + "' />\n");
					numVdivlines=numVdivlines+1;
				}
	    	}
	    	if("0".equals(dateType)){//日报
	    		String curDateStart=MapUtils.getString(queryData, "startDate", null);//当前日开始
				String curStart=curDateStart.replaceAll("-", "");
				String curDateEnd=MapUtils.getString(queryData, "endDate", null);//当前日结束
				String curEnd=curDateEnd.replaceAll("-", "");
				String curDate=curDateStart.replaceAll("-", "")+"~"+curDateEnd.replaceAll("-", "");
				
				int interval=0;
				long interval1=DateUtil.getInterval(curEnd,curStart);
				interval=(int)interval1-1;

				String lastDateStart=DateUtil.lastDay(curDateStart, interval);
				String lastStart=lastDateStart.replaceAll("-", "");
				String lastDateEnd=DateUtil.lastDay(curDateEnd, interval);
				String lastEnd=lastDateEnd.replaceAll("-", "");
				String lastDate=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

				temp2.append("<dataset  seriesName='" + curDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
				temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

				List<Map<String,Object>>barList1=newChannelDao.getChannelSerBarData(curStart,curEnd,queryData);//本月
				List<Map<String,Object>>barList2=newChannelDao.getChannelSerBarData(lastStart,lastEnd,queryData);//上月
				
			        //柱状图_周报表
				for (Map<String, Object> key1 : barList1) {
					String curValue = "0";
					curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
					maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(curValue));  
					temp1.append("<category label='" + key1.get("CHANNEL_TYPE_NAME_NEW")+ "'/>\n");
					temp2.append("<set      value='" + curValue + "' />\n");
				}for (Map<String, Object> key2 : barList2) {
					String lastValue = "0";
				    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
				    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));  
					temp3.append("<set      value='" + lastValue + "' />\n");
					numVdivlines=numVdivlines+1;
				}
	    	}
	   if(maxVal==minVal){
		 minVal=0;
	   } 
       temp1.append("</categories>\n");
       temp2.append("</dataset>\n");
       temp3.append("</dataset>\n");
       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
               " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' "+
               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
       barChartMap.append(temp1).append(temp2).append(temp3);
       barChartMap.append("</chart>");
      return barChartMap.toString();	
  }
	/****
	 * 查询 集团渠道 新模板报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getGroupChannelReport_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getGroupChannelReport_Pg(queryData));
		 
	    return map; 
	}

	/****
	 * 查询 集团新口径-集团上报监测月报
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getGroupChannelSc_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(newChannelDao.getGroupChannelSc_Pg(queryData));
		 
	    return map; 
	}
	
	/****
	 * 查询 集团新口径-集团上报清单
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getGroupChannelList_Pg(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return newChannelDao.getGroupChannelList_Pg(paramMap,page);
	}
	
	public Map<String, Object> expTxtDataJt(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		NewChannelDao newChannelDao = new NewChannelDao();
		return newChannelDao.getGroupChannelList_Pg(paramMap, page);// 记录
	}
	
	
	
	/***
	 *  渠道服务清单
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getChannelNewIndexTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return newChannelDao.getChannelNewIndexTableData(paramMap, page);
	}
	/**
	 * 导出数据
	 */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		NewChannelDao newChannelDao = new NewChannelDao();
		return newChannelDao.getChannelNewIndexTableData(paramMap, page);// 记录
	}
	/**
	 * 最大值
	 */
	private double compareMaxValue(double maxVal, double curVal) {
		if (curVal > maxVal) {
			maxVal = curVal;
		}
		return maxVal;
	}
	/**
	 * 最小值
	 */
	private double compareMinValue(double minVal, double curVal) {
		if (minVal == 0 && curVal != 0) {
			minVal = curVal;
		} else if (curVal < minVal) {
			minVal = curVal;
		}
		return minVal;
	}
	
	/***
	 * 移动后付费用户信用管理月报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getCreditManageMonthData(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.getCreditManageMonthData(queryData));//查询报表的展现
		return map;
	}
	
	/***
	 * 移动后付费用户信用投诉表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getCreditComplaintData(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
		page.setSize(Convert.toInt(queryData.get("pageCount")));
		map.putAll(newChannelDao.getCreditComplaintData(queryData, page));//查询报表的展现
		return map;
	}
	
	/***
	 * 移动后付费用户信用管理宽表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getCreditManageKBData(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
		page.setSize(Convert.toInt(queryData.get("pageCount")));
		map.putAll(newChannelDao.getCreditManageKBData(queryData, page));//查询报表的展现
		return map;
	}
	
	/***
	 * 移动后付费用户临时授信月报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getTemporaryCreditMonthData(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.getTemporaryCreditMonthData(queryData));//查询报表的展现
		return map;
	}
	
	/***
	 * 移动后付费用户节假日无限信用效果监控报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getHolidayCreditResultData(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		map.putAll(newChannelDao.getHolidayCreditResultData(queryData));//查询报表的展现
		return map;
	}
	
	/***
	 * 移动后付费用户临时授信监控管理宽表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getLSSXMonitorKBData(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>(); 
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
		page.setSize(Convert.toInt(queryData.get("pageCount")));
		map.putAll(newChannelDao.getLSSXMonitorKBData(queryData, page));//查询报表的展现
		return map;
	}
	
}
