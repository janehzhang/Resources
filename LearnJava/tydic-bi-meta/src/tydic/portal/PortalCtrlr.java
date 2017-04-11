package tydic.portal;

import tydic.frame.common.Log;
import tydic.frame.jdbc.DataTable;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 移植记录：
 * 1，为保证编译通过，能注销的代码已经注销而不是删除
 * 2，为结合现有框架，PortalDAO的静态方法调用都改为portalDAO对象调用
 * 3，部分static方法必须去掉static修饰
 * 4，日志打印用新框架实现
 * 5，去掉DWR注解
 *
 * --刘斌
 */

public class PortalCtrlr {
    private PortalDAO portalDAO;

    public void setPortalDAO(PortalDAO portalDAO) {
        this.portalDAO = portalDAO;
    }

    /**
     * 获取显示表单栏
     *
     * @return
     */

    public Object[] getAreaName() {
    	Object[] object = portalDAO.getAreaName();
        return portalDAO.getAreaName();
    }

    public Object[] getDataAudit(int menu_id, int tab_id, int index_type_id, String dateNo, String areaCode) {
        return portalDAO.getDataAudit(menu_id, tab_id, index_type_id, dateNo, areaCode);
    }

    public List<Map<String, Object>> getNotice() {
        return portalDAO.getNotice();
    }

    /**
     * 获取首页菜单子报表项及属性 时间
     *
     * @param menu_id
     * @return
     */

    public Object[] getViewTabs(int menu_id, int define_id, int report_level_id, String indexTypeId) {
        try {
            int indexTPD = 0;
            if (indexTypeId != null && !indexTypeId.equals(""))
                indexTPD = Integer.parseInt(indexTypeId);
            Object[] res = new Object[4];
            List<Map<String, Object>> tb = portalDAO.getViewTabs(menu_id, define_id, indexTPD); //tab项
            Object[] rpt_index = new Object[tb.size()];
            Object[] rpt_index_EXP = new Object[tb.size()];
            for (int i = 0; i < tb.size(); i++) {
                int index_type_id =Integer.parseInt(tb.get(i).get("INDEX_TYPE_ID").toString());
                int tab_id = Integer.parseInt(tb.get(i).get("TAB_ID").toString());
                rpt_index[i] = portalDAO.getViewTabIndex(index_type_id, report_level_id); //报表指标配置
                rpt_index_EXP[i] = portalDAO.getViewTabIndexExp(index_type_id + "", report_level_id + "");//指标解释
                List<Map<String, Object>> tmp = portalDAO.getViewTabDataAudit(menu_id, tab_id, index_type_id);//数据审核标识
                if (tmp.size() > 0) {
                    tb.get(i).put("MIN_DATENO", tmp.get(0).get("MIN_DATENO"));
                    tb.get(i).put("MAX_DATENO", tmp.get(0).get("MAX_DATENO"));
                    tb.get(i).put("EFFECT_DATENO", tmp.get(0).get("EFFECT_DATENO"));
                }
            }
            res[0] = tb;
            res[1] = rpt_index;
            res[2] = rpt_index_EXP;
            return res;
        } catch (Exception e) {
            Log.error(null, e);
        }

        return null;
    }

    /**
     * 获取表Data
     *
     * @return
     */

    public Object[][] getTableData(ReportPO po, String vals[], int type, HttpServletRequest request) {
        String indexTypeId = po.getIndexTypeId();
        String indexCd = po.getIndexCd();
        String reportLevelId = po.getReportLevelId();
        String dateNo = po.getDateNo();
        String localCode = po.getLocalCode();
        String areaId = po.getAreaId();

        return portalDAO.getData(indexTypeId, reportLevelId, dateNo, indexCd, localCode, areaId, vals, type);
    }

    public String getAreaData(String fieldName, String indexTypeId, String REPORT_LEVEL_ID, String dateNo,
                              String indexCd, String LOCAL_CODE) {
        DataTable dt = portalDAO.getAreaData(fieldName, indexTypeId, REPORT_LEVEL_ID, dateNo, indexCd, LOCAL_CODE);
        if (dt == null)
            return null;

        StringBuffer str = new StringBuffer();
        str.append("<chart useRoundEdges='1' slantLabels='1' rotateNames='0' bgColor='FFFFFF' showAlternateHGridColor='0'");
        str.append("baseFontSize='14' baseFontColor='000000' divLineColor='FFFFFF' lineThickness='1' showValues='0'");
        str.append("formatNumber='0' formatNumberScale='0' numvdivlines='5' chartLeftMargin='5' chartRightMargin='5' showToolTip='1'>");
        str.append("<categories>");
        for (int i = 0; i < dt.rowsCount; i++) {
            str.append("<category label='" + dt.rows[i][0] + "' />");
        }
        str.append("</categories>");
        str.append("<dataset seriesName='' color='AFD8F8' showValues='0'> ");

        for (int i = 0; i < dt.rowsCount; i++) {
            str.append("<set value='" + dt.rows[i][1] + "' />");
        }
        str.append("</dataset> </chart>");
        return str.toString();
    }

    /**
     * 获取chart的Data
     *
     * @return
     */
    public Object[] getTableChart(String indexTypeId, String reportLevelId, String indexCd,
                                  String localCode, String areaId, String fieldName, int radio, String dateNo) {
        //定义一个boolean值，用来保存是否是新oa方法调用，这里对显示做一些调整
    	boolean isNewOaPara = false;
    	if(dateNo.endsWith("$")){
    		isNewOaPara = true;
    		dateNo = dateNo.replace("$", "");
    	}
    	int cunt = 28;
        int jPoint = 2;
        if (radio == 2) {
            cunt = 63;
            jPoint = 3;
        } else if (radio == 3) {
            cunt = 91;
            jPoint = 7;
        } else {
        }

        if (fieldName == null || fieldName.equals("")) {
            fieldName = "Value2";
        }

        if (dateNo == null || dateNo.equals("")) {
            dateNo = getYesterday();
        }
        try {
        	//查询最大有效期
//        	dateNo = "20120612";
            String maxDate = portalDAO.queryMaxDate(indexTypeId);
            if(Integer.parseInt(maxDate) > Integer.parseInt(dateNo)){
            	maxDate = dateNo;
            }else if(!maxDate.equals(dateNo)){
            	dateNo = getBeforeDay(maxDate, -1);
            }

            String startDayNo = getBeforeDay(dateNo, cunt);

            String lsDayNo = getBeforeDay(startDayNo, cunt);

            Integer year = (Integer.parseInt(maxDate.substring(0, 4)) - 1);
            String yesYear = year.toString() + maxDate.substring(4);
            String lsyesYearNo = year.toString() + startDayNo.substring(4);
            fieldName = fieldName.toUpperCase();
            //用于处理累计数据的时间
            String lastMoth = getMiniTime(dateNo,1);
            //这里加一个数字的原因是因为，比如3。4月的前一个月 是2.4日，但是前28天只能查到2.7日的情况，所以要把数据查出来，但是日期不会影响，因为日期格式已固定
            String isIastMoth = getBeforeDay(lastMoth, cunt+3);
            
            //获取当期
            List<Map<String, Object>> list1 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
                    startDayNo, maxDate, fieldName);
            //是否是累计数
            boolean isArive = false;
            //获取上月同期
            //将累计数据做特殊处理
            List<Map<String, Object>> list2=null;
            if(indexCd.equals("YHFZ_IND_5") || indexCd.equals("YHFZ_IND_6") || indexCd.equals("YHFZ_IND_7")||
            		indexCd.equals("YHFZ_IND_8") || indexCd.equals("YWLL_IND_3") ||indexCd.equals("YWLL_IND_4")||
            		indexCd.equals("SRYC_IND_2")||indexCd.equals("SRYC_IND_5")||indexCd.equals("GWYH_IND_2")||
            		indexCd.equals("GWYH_IND_3")||indexCd.equals("GWYH_IND_4") || indexCd.equals("GWYH_IND_5") || indexCd.equals("GWYH_IND_6")){
                isArive = true;
            	list2 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
            			isIastMoth, lastMoth, fieldName);
            }else{
            	list2 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
                        lsDayNo, startDayNo, fieldName);
            }
            
            //获取上年同期
            List<Map<String, Object>> list3 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
                    lsyesYearNo, yesYear, fieldName);
            //anchorRadius设置线点半径，lineThickness折线厚度 showhovercap='0'
            //String  data ="<chart yAxisValuesStep='2'  formatNumber='1' labelStep='"+jPoint+"' lineThickness='1' canvasBorderThickness='0' showBorder='0'  baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='0' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' decimalPrecision='2' showhovercap='0' showValues='1' rotateValues='1' valuePosition='auto' numdivlines='5' numVdivlines='6' yaxisminvalue='10' yaxismaxvalue='1' rotateNames='0' chartRightMargin='20' chartLeftMargin='10'>";
            //			String data = "<graph caption='' subcaption=''  hovercapbg='FFECAA' hovercapborder='F47E00' "
            //					+ "formatNumber='0' labelStep='"
            //					+ jPoint
            //					+ "'  formatNumberScale='0' decimalPrecision='0' baseFontSize='12' showvalues='0' canvasBorderThickness='1' "
            //					+
            //					"numdivlines='3' numVdivlines='0'   yaxisminvalue='10' yaxismaxvalue='1'  rotateNames='1' chartRightMargin='20' "
            //					+
            //					"chartLeftMargin='10'>\n"; yAxisValuesStep='2' yaxismaxvalue='1'
            BigDecimal minValue = new BigDecimal(999999999);
            BigDecimal maxValue = new BigDecimal(-888888888);
            BigDecimal maXvalue = new BigDecimal(-88888888);
            boolean bool = false;
            for (int i = 0; i < list1.size(); i++) {
                BigDecimal val = ((BigDecimal) (list1.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list1.get(i).remove(fieldName);
                    list1.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if(val.toString().indexOf(".") > 0) bool = true;
            }
            for (int i = 0; i < list2.size(); i++) {
                BigDecimal val = ((BigDecimal) (list2.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list2.get(i).remove(fieldName);
                    list2.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if(val.toString().indexOf(".") > 0) bool = true;
            }
            for (int i = 0; i < list3.size(); i++) {
                BigDecimal val = ((BigDecimal) (list3.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list3.get(i).remove(fieldName);
                    list3.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if(val.toString().indexOf(".") > 0) bool = true;
            }
            int minValueInt = minValue.intValue();
			if(!bool || isNewOaPara){
				int step = maxValue.intValue()-minValue.intValue();
                if(step>5){
                    maxValue = BigDecimal.valueOf(maxValue.intValue() + (5-step%5));
                }
			}
			String data = "<chart   formatNumber='1' labelStep='"
				+ jPoint
				//"+minValueInt+"  divLineDecimalPrecision='0'
				+ "' lineThickness='1'  canvasBorderThickness='0' showBorder='0'  baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF'" +
						" alternateHGridColor='FFFFFF' divLineAlpha='0' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' " +
						"showToolTip='1' showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto' numdivlines='4' adjustDiv='0' numVdivlines='6' " +
						" yaxisminvalue='"+minValueInt+"' yaxismaxvalue='"+maxValue.intValue()+"' rotateNames='0' chartRightMargin='20' chartLeftMargin='10'>";
			//String data = "<chart caption='' subcaption='' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' decimalPrecision='0' baseFontSize='12' showvalues='0' canvasBorderThickness='1' numdivlines='3' numVdivlines='0'  yaxisminvalue='10' yaxismaxvalue='1' useRoundEdges='1' rotateNames='1' chartRightMargin='20' chartLeftMargin='10'>\n";
            String timedata = "<categories>\n";
            String data_u = "<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='FF2121' seriesName='" + "当期值"
                    + "' color='FF2121'   anchorBorderColor='FFFFFF' >\n";
            String data_m = "<dataset  anchorRadius='4' anchorBgColor='0000FF' seriesName='" + "上月同期"
                    + "' color='0000FF' anchorBorderColor='FFFFFF'>\n";
            String data_v;
            if (list3.size() > 0 && !isNewOaPara) {
                data_v = "<dataset anchorSides='3' anchorRadius='5' anchorBgColor='00FF00' seriesName='" + "上年同期"
                        + "' color='00FF00' anchorBorderColor='FFFFFF'>\n";
            } else {
                data_v = null;
            }


            //			for (int i = 0; i < list1.size(); i++)
            //			{
            //				Map<String, Object> map1 = (Map<String, Object>) list1.get(i);
            //				data_u += "<set  value='" + map1.get(fieldName.toUpperCase()).toString() + "' hoverText='当期值 "
            //						+ map1.get(fieldName.toUpperCase()).toString() + "'/>\n";
            //
            //			}

            List lsArrMon = getDayArrayInMonth(dateNo, cunt);
            List lsArrMon1 = null;
            if(isArive){
            	lsArrMon1 = getDayLastMo(lsArrMon);
            	//lsArrMon1 = getDayArrayInMonth(lastMoth,cunt);
            }else{
            	lsArrMon1 = getDayArrayInMonth(lsArrMon.get(0).toString(), cunt);
            }
            for (int i = 0; i < lsArrMon.size(); i++) {//必须显示30个日期，没有数据显示0

                Boolean v1Flag = true;
                Boolean v2Flag = true;
                Boolean v3Flag = true;
                Boolean v2pFlag = true;
                String fieldValue = "";
                //判定是是否少一天
                //当期趋势线
                for (int m = 0; m < list1.size(); m++) {
                    Map<String, Object> map1 = (Map<String, Object>) list1.get(m);
                    //String temp1 = lsArrMon.get(i).toString();
                    if (map1.containsKey(fieldName) && map1.get("DATENO").equals(lsArrMon.get(i).toString())) {//如果此日期存在
                        try {
                            if((indexTypeId.toString().equals("12") ||indexTypeId.toString().equals("11") )  && isNewOaPara && lsArrMon.get(i).toString().equals(dateNo)){
                                data_u += "<set  value='-' hoverText='当期值：-'/>\n";
                            }else{
                                data_u += "<set  value='" + map1.get(fieldName).toString() + "' hoverText='当期值："
                                        + map1.get(fieldName).toString() + "'/>\n";
                            }

                        } catch (Exception e) {
                            Log.error(null, e);
                        }
                        v1Flag = false;
                    }
                }
                if (v1Flag) {
                    if(isNewOaPara && lsArrMon.get(i).toString().equals(dateNo)){
                        //新OA传过来的参数判断
                        data_u += "<set  value='-' hoverText='当期值：-'/>\n";
                    }else{
                        data_u += "<set  value='0' hoverText='当期值：0'/>\n";
                    }
                }
                //上月同期趋势线
                for (int m = 0; m < list2.size(); m++) {
                    Map<String, Object> map2 = (Map<String, Object>) list2.get(m);
                    if(map2.containsKey(fieldName) && map2.containsKey("DATENO") && (map2.get("DATENO").toString().compareTo(lsArrMon1.get(i).toString())<0) ){
                        fieldValue = map2.get(fieldName).toString();
                    }
                    if (map2.containsKey(fieldName) && map2.get("DATENO").equals(lsArrMon1.get(i).toString()))
					{//如果此日期存在
						data_m += "<set  value='" + map2.get(fieldName).toString() + "' hoverText='上月同期： "
								+ map2.get(fieldName).toString() + "'/>\n";
						v2Flag = false;
					}
                }
                if (v2Flag) {
                    if(!this.stringToDate(lsArrMon1.get(i).toString())){
                        if(i==0 && fieldValue!=null && !fieldValue.toString().equals("") ){
                            Map<String, Object> map = new HashMap<String, Object>();
                             map.put("DATENO",lsArrMon1.get(i).toString());
                             map.put(fieldName,fieldValue);
                             list2.add(map);
                             data_m += "<set  value='" + fieldValue + "' hoverText='上月同期： "
                                            + fieldValue + "'/>\n";
                             v2pFlag=false;
                        }else{
                            Map<String, Object> map = new HashMap<String, Object>();
                            for(int p=0; p<list2.size(); p++){
                                Map<String, Object> map21 = (Map<String, Object>) list2.get(p);
                                if (map21.containsKey(fieldName) && map21.get("DATENO").equals(lsArrMon1.get(i-1).toString()))
                                {
                                    map.put("DATENO",lsArrMon1.get(i).toString());
                                    map.put(fieldName,map21.get(fieldName).toString());
                                    //如果此日期存在
                                    data_m += "<set  value='" + map21.get(fieldName).toString() + "' hoverText='上月同期： "
                                            + map21.get(fieldName).toString() + "'/>\n";
                                    v2pFlag=false;
                                }
                            }
                            if(v2pFlag){
                                data_m += "<set  value='0' hoverText='上月同期：0'/>\n";
                            }
                            list2.add(map);
                        }
                    }else{
                        data_m += "<set  value='0' hoverText='上月同期：0'/>\n";
                    }
                }
                //上年同期趋势线
                if (data_v != null) {
                    for (int m = 0; m < list3.size(); m++) {
                        Map<String, Object> map3 = (Map<String, Object>) list3.get(m);
                        if (map3.containsKey(fieldName)
                                && map3.get("DATENO").toString().substring(4).equals(lsArrMon.get(i).toString().substring(4))) {//如果此日期存在
                            data_v += "<set  value='" + map3.get(fieldName).toString() + "' hoverText='上年同期："
                                    + map3.get(fieldName).toString() + "'/>\n";
                            v3Flag = false;
                        }
                    }
                    list3.clear();
                    if (v3Flag) {
                        data_v += "<set  value='" + "0" + "' hoverText='上年同期：0'/>\n";
                    }
                }
                //线条节点
                if(isNewOaPara){
                	timedata += "<category name='" + lsArrMon.get(i).toString().substring(6, 8) + "' />\n";
                }else {
                	timedata += "<category name='" + lsArrMon.get(i).toString().substring(4, 6) + "."
                    	+ lsArrMon.get(i).toString().substring(6, 8) + "' />\n";
				}
                
                //timedata += lsArrMon.get(i).toString().substring(4, 6)+"."+lsArrMon.get(i).toString().substring(6, 8)+"|";
            }
            timedata += "</categories>\n";
            data_u += "</dataset>\n";
            data_m += "</dataset>\n";
            if (data_v != null) {
                data_v += "</dataset>\n";
                data += data_v;
            }
            data += timedata;
            data += data_u;
            data += data_m;
            data += "</chart>"; 
            Object[] rtnObj = new Object[2];
            rtnObj[0] = data;
            if ((reportLevelId != null && reportLevelId.equals("1"))
                    && (areaId == null || areaId.equals("") || areaId.equals("0"))) {
                rtnObj[1] = portalDAO.getMapData(dateNo, indexTypeId, indexCd, fieldName);
            } else {
                rtnObj[1] = null;
            }
            return rtnObj;
        } catch (Exception e) {
            Log.error(null, e);
            return null;
        }
    }
    /**
     * 辅助类，用于辅助新OA 展示
     */
    public Object[] getTableChart1(String type,String dragon_channel_type, String bt_code, String localCode, String dateNo) {
        //定义一个boolean值，用来保存是否是新oa方法调用，这里对显示做一些调整
    	boolean isNewOaPara = false;
    	boolean hasDataNo = false;//数据日期是否完整
    	if(dateNo.endsWith("$")){
    		isNewOaPara = true;
    		dateNo = dateNo.replace("$", "");
    	}
    	int cunt = 28;
        int jPoint = 2;
        int radio = 1;
        if (radio == 2) {
            cunt = 63;
            jPoint = 3;
        } else if (radio == 3) {
            cunt = 91;
            jPoint = 7;
        } else {
        }
        String fieldName = "Value2";
        if (fieldName == null || fieldName.equals("")) {
            fieldName = "Value2";
        }

        if (dateNo == null || dateNo.equals("")) {
            dateNo = getYesterday();
        }
        try {

            String startDayNo = getBeforeDay(dateNo, cunt);

            String lsDayNo = getBeforeDay(startDayNo, cunt);
            fieldName = fieldName.toUpperCase();
            //用于处理累计数据的时间
            String lastMoth = getMiniTime(dateNo,1);
            String isIastMoth = getBeforeDay(lastMoth, cunt+3);
            List<Map<String, Object>> list1 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,startDayNo,dateNo);
            List<Map<String, Object>> list2;
            if(type.toString().equals("1")){
                list2 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,isIastMoth,lastMoth);
            }else{
                list2 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,lsDayNo,startDayNo);
            }
            BigDecimal minValue = new BigDecimal(999999999);
            BigDecimal maxValue = new BigDecimal(-888888888);
            BigDecimal maXvalue = new BigDecimal(-88888888);
            boolean bool = false;
            for (int i = 0; i < list1.size(); i++) {
                BigDecimal val = ((BigDecimal) (list1.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list1.get(i).remove(fieldName);
                    list1.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if(val.toString().indexOf(".") > 0) bool = true;
            }
            for (int i = 0; i < list2.size(); i++) {
                BigDecimal val = ((BigDecimal) (list2.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list2.get(i).remove(fieldName);
                    list2.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if(val.toString().indexOf(".") > 0) bool = true;
            }
            if(list1.size()<28 || list2.size()<28){
                minValue = BigDecimal.valueOf(0);
            }
            int minValueInt = minValue.intValue();
			if(!bool || isNewOaPara){
				int step = maxValue.intValue()-minValue.intValue();
                if(step>5){
                    maxValue = BigDecimal.valueOf(maxValue.intValue() + (5-step%5));
                }
			}
			String data = "<chart   formatNumber='1' labelStep='"
				+ jPoint
				+ "' lineThickness='1' divLineDecimalPrecision='0' canvasBorderThickness='0' showBorder='0'  baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF'" +
						" alternateHGridColor='FFFFFF' divLineAlpha='0' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' " +
						"showToolTip='1' showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto' numdivlines='4' adjustDiv='0' numVdivlines='6' " +
						" yaxisminvalue='"+minValueInt+"' yaxismaxvalue='"+maxValue.intValue()+"' rotateNames='0' chartRightMargin='20' chartLeftMargin='10'>";
			//String data = "<chart caption='' subcaption='' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' decimalPrecision='0' baseFontSize='12' showvalues='0' canvasBorderThickness='1' numdivlines='3' numVdivlines='0'  yaxisminvalue='10' yaxismaxvalue='1' useRoundEdges='1' rotateNames='1' chartRightMargin='20' chartLeftMargin='10'>\n";
            String timedata = "<categories>\n";
            String data_u = "<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='FF2121' seriesName='" + "当期值"
                    + "' color='FF2121'   anchorBorderColor='FFFFFF' >\n";
            String data_m = "<dataset  anchorRadius='4' anchorBgColor='0000FF' seriesName='" + "上月同期"
                    + "' color='0000FF' anchorBorderColor='FFFFFF'>\n";

            List lsArrMon = getDayArrayInMonth(dateNo, cunt);
            List lsArrMon1 = null;
            if(type.toString().equals("1")){
            	lsArrMon1 = getDayLastMo(lsArrMon);
            	for (int j = 0;j<lsArrMon.size();j ++){
                	for(int k = 0; k < list1.size(); k++){
                		if(lsArrMon.get(j).toString().equals(list1.get(k).get("DATENO").toString())){
                			hasDataNo = true;
                		}
                	}
                	if(!hasDataNo){
                  		 String dataNo = getDayArrayInMonth(lsArrMon.get(j).toString(), 1).get(0); 
                        List<Map<String, Object>> list3 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,dataNo,dataNo);
                        	int t = 1;
                        	while(list3.size() == 0){
                        		dataNo = getDayArrayInMonth(lsArrMon.get(j).toString(), t).get(0);
                        		list3 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,dataNo,dataNo);
                        		t++;
                        	}
                        	Map mapdata = new HashMap();
                        	mapdata.put("DATENO",lsArrMon.get(j).toString());
                        	mapdata.put(fieldName,list3.get(0).get(fieldName).toString());
                        	list1.add(mapdata);
               		}
               		hasDataNo = false;
                	
                }
            	for (int j = 0;j<lsArrMon1.size();j ++){
                	for(int k = 0; k < list2.size(); k++){
                		if(lsArrMon1.get(j).toString().equals(list2.get(k).get("DATENO").toString())){
                			hasDataNo = true;
                		}
                	}
                	if(!hasDataNo && Integer.parseInt(lsArrMon1.get(j).toString())  > 20120309){
                  		 String dataNo = getDayArrayInMonth(lsArrMon1.get(j).toString(), 1).get(0); 
                        List<Map<String, Object>> list3 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,dataNo,dataNo);
                        	int t = 1;
                        	while(list3.size() == 0){
                        		dataNo = getDayArrayInMonth(lsArrMon1.get(j).toString(), t).get(0);
                        		list3 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,dataNo,dataNo);
                        		t++;
                        	}
                        	Map mapdata = new HashMap();
                        	mapdata.put("DATENO",lsArrMon1.get(j).toString());
                        	mapdata.put(fieldName,list3.get(0).get(fieldName).toString());
                        	list2.add(mapdata);
               		}
               		hasDataNo = false;
                	
                }
            }else{
            	lsArrMon1 = getDayArrayInMonth(lsArrMon.get(0).toString(), cunt);
            }
            
            
            for (int i = 0; i < lsArrMon.size(); i++) {//必须显示30个日期，没有数据显示0

                Boolean v1Flag = true;
                Boolean v2Flag = true;
                Boolean v2pFlag = true;
                //用来保存如果日期不存在，最近一天的值
                String fieldValue = "";
                //判定是是否少一天
                //当期趋势线
                for (int m = 0; m < list1.size(); m++) {
                    Map<String, Object> map1 = (Map<String, Object>) list1.get(m);
                    //String temp1 = lsArrMon.get(i).toString();
                    if (map1.containsKey(fieldName) && map1.get("DATENO").toString().trim().equals(lsArrMon.get(i).toString().trim())) {//如果此日期存在
                        try {
                            data_u += "<set  value='" + map1.get(fieldName).toString() + "' hoverText='当期值："
                                    + map1.get(fieldName).toString() + "'/>\n";
                        } catch (Exception e) {
                            Log.error(null, e);
                        }
                        v1Flag = false;
                    }
                }
                if (v1Flag) {
                    if(type.toString().trim().equals("1")){
                        Map<String,Object> map = new HashMap<String,Object>();
                        for(int n=0;n<list1.size(); n++){
                             Map<String, Object> map1 = (Map<String, Object>) list1.get(n);
                            if(map1.containsKey(fieldName) && map1.get("DATENO").toString().trim().equals(lsArrMon.get(i).toString().trim())){
//                             for(int j = 0;j< list1.size();j++){
//                            	 if(list1.get(j).get("DATENO").equals(lsArrMon.get(i))){
//                            		 hasDataNo = true;
//                            	 }
//                             } 
//                            	 if(!hasDataNo){
//                            		 String dataNo = getDayArrayInMonth(lsArrMon.get(i).toString(), 1).get(0); 
//                                 	List<Map<String, Object>> list3 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,dataNo,dataNo);
//                                 	int k = 1;
//                                 	while(list3.size() == 0){
//                                 		dataNo = getDayArrayInMonth(lsArrMon.get(i).toString(), k).get(0);
//                                 		list3 = portalDAO.getChart1(type,dragon_channel_type,bt_code,localCode,dataNo,dataNo);
//                                 		k++;
//                                 	}
//                                 	data_u += "<set  value='" + list3.get(0).get(fieldName).toString() + "' hoverText='当期值："
//                                     + list3.get(0).get(fieldName).toString() + "'/>\n";
//                                     map.put("DATENO",lsArrMon.get(i).toString());
//                                    map.put(fieldName,list3.get(0).get(fieldName).toString());
//                                     v1Flag = false;
//                                     hasDataNo = false;
//                            	 }
                                data_u += "<set  value='" + map1.get(fieldName).toString() + "' hoverText='当期值："
                                    + map1.get(fieldName).toString() + "'/>\n";
                                map.put("DATENO",lsArrMon.get(i));
                                map.put(fieldName,map1.get(fieldName).toString());
                                v1Flag = false;
                            }
                        }
                        if(v1Flag){
                            data_u += "<set  value='0' hoverText='当期值：0'/>\n";
                        }
                        list1.add(map);
                	}else{
                        data_u += "<set  value='0' hoverText='当期值：0'/>\n";
                    }
                }
                //上月同期趋势线
                for (int m = 0; m < list2.size(); m++) {
                    Map<String, Object> map2 = (Map<String, Object>) list2.get(m);
                    if(map2.containsKey(fieldName) && map2.containsKey("DATENO") && (map2.get("DATENO").toString().compareTo(lsArrMon1.get(i).toString())<0) ){
                        fieldValue = map2.get(fieldName).toString();
                    }
                    if (map2.containsKey(fieldName) && map2.get("DATENO").equals(lsArrMon1.get(i).toString()))
					{//如果此日期存在
						data_m += "<set  value='" + map2.get(fieldName).toString() + "' hoverText='上月同期： "
								+ map2.get(fieldName).toString() + "'/>\n";
						v2Flag = false;
					}
                }
                if (v2Flag) {
                    if(!this.stringToDate(lsArrMon1.get(i).toString()) && type.toString().trim().equals("1")){
                        if(i==0 && fieldValue!=null && !fieldValue.toString().equals("") ){
                            Map<String, Object> map = new HashMap<String, Object>();
                             map.put("DATENO",lsArrMon1.get(i).toString());
                             map.put(fieldName,fieldValue);
                             list2.add(map);
                             data_m += "<set  value='" + fieldValue + "' hoverText='上月同期： "
                                            + fieldValue + "'/>\n";
                             v2pFlag=false;
                        }else{
                            Map<String, Object> map = new HashMap<String, Object>();
                            for(int p=0; p<list2.size(); p++){
                                Map<String, Object> map21 = (Map<String, Object>) list2.get(p);
                                if (map21.containsKey(fieldName) && map21.get("DATENO").equals(lsArrMon1.get(i-1).toString()))
                                {
                                    map.put("DATENO",lsArrMon1.get(i).toString());
                                    map.put(fieldName,map21.get(fieldName).toString());
                                    //如果此日期存在
                                    data_m += "<set  value='" + map21.get(fieldName).toString() + "' hoverText='上月同期： "
                                            + map21.get(fieldName).toString() + "'/>\n";
                                    v2pFlag=false;
                                }
                            }
                            if(v2pFlag){
                                data_m += "<set  value='0' hoverText='上月同期：0'/>\n";
                            }
                            list2.add(map);
                        }
                    }else{
                		data_m += "<set  value='0' hoverText='上月同期：0'/>\n";
                	}

                }

                //线条节点
                if(isNewOaPara){
                	timedata += "<category name='" + lsArrMon.get(i).toString().substring(6, 8) + "' />\n";
                }else {
                	timedata += "<category name='" + lsArrMon.get(i).toString().substring(4, 6) + "."
                    	+ lsArrMon.get(i).toString().substring(6, 8) + "' />\n";
				}

                //timedata += lsArrMon.get(i).toString().substring(4, 6)+"."+lsArrMon.get(i).toString().substring(6, 8)+"|";
            }
            timedata += "</categories>\n";
            data_u += "</dataset>\n";
            data_m += "</dataset>\n";
            data += timedata;
            data += data_u;
            data += data_m;
            data += "</chart>";
            Object[] rtnObj = new Object[2];
            rtnObj[0] = data;
//            if ((reportLevelId != null && reportLevelId.equals("1"))
//                    && (areaId == null || areaId.equals("") || areaId.equals("0"))) {
//                rtnObj[1] = portalDAO.getMapData(dateNo, indexTypeId, indexCd, fieldName);
//            } else {
//                rtnObj[1] = null;
//            }
            return rtnObj;
        } catch (Exception e) {
            Log.error(null, e);
            return null;
        }
    }
    /**
     * 获取值班人员
     *
     * @return
     */
    public String getDuty() {
        StringBuffer rtn = new StringBuffer();
        List list = portalDAO.getDuty();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Map record = (Map) iterator.next();
            rtn.append(record.get("PRINCIPAL_MAN"));
            rtn.append("&nbsp;");
            rtn.append(record.get("MOBILE"));
            rtn.append("&nbsp;&nbsp;");
        }
        return rtn.toString();
    }

    /**
     * 获取前1月的日期
     *
     * @param date
     * @param monNum
     * @return
     */
    private static String getMiniTime(String date, Integer monNum) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month_no = Integer.parseInt(date.substring(4, 6)) - monNum;
        int day = Integer.parseInt(date.substring(6, 8));
        Calendar cal = new GregorianCalendar(year, month_no - 1, day);

        String preDate = cal.get(Calendar.YEAR) + String.format("%1$02d", cal.get(Calendar.MONTH) + 1)
                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        return preDate;
    }

    /**
     * 获取前多少天的日期
     *
     * @param date
     * @param monNum
     * @return
     */
    private static String getBeforeDay(String date, int monNum) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month_no = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8)) - monNum;
        Calendar cal = new GregorianCalendar(year, month_no - 1, day);

        String preDate = cal.get(Calendar.YEAR) + String.format("%1$02d", cal.get(Calendar.MONTH) + 1)
                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        return preDate;
    }

    //取得昨天的日期
    private static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        return yesterday;
    }
    //取得前一天的上一个月的时间
    private static String getYesLastMouth(String date, int monNum){
    	int year = Integer.parseInt(date.substring(0, 4));
        int month_no = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        Calendar cal = new GregorianCalendar(year, month_no - 1, day);

        String preDate = cal.get(Calendar.YEAR) + String.format("%1$02d", cal.get(Calendar.MONTH) + 1-monNum)
                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        return preDate;
    }
    /**
     * <p/>
     * 获取指定月份内的日期序列 曲线使用日期显示周期为30天
     *
     * @param date dayArray in month
     * @return 趋势图显示页面URL
     */
    private static List<String> getDayArrayInMonth(String date, int num) {
        List<String> days = new ArrayList<String>();
        if (date != null && !"".equals(date.trim())) {
            int year = Integer.parseInt(date.substring(0, 4));
            int month_no = Integer.parseInt(date.substring(4, 6)) - 1;
            int day = Integer.parseInt(date.substring(6, 8));
            Calendar calendar = new GregorianCalendar(year, month_no, day);
            Calendar cal = new GregorianCalendar(year, month_no, day - num);
            String[] dateArray = new String[((int) ((calendar.getTimeInMillis() - cal
                    .getTimeInMillis()) / 1000 / 3600 / 24)) + 1];
            cal.add(Calendar.DAY_OF_MONTH, -1);
            for (String aDateArray : dateArray) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                days.add(cal.get(Calendar.YEAR)
                                + String.format("%1$02d", (cal.get(Calendar.MONTH) + 1))
                                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH)));
            }
        }
        return days;
    }
    //获取上一个月的当前天，没有值也映射出来。
    private static List<String> getDayLastMo(List list){
    	List<String> days = new ArrayList<String>();
        if (list != null) {
            for(int i=0;i<list.size();i++){
            	String day = "";
            	day += list.get(i).toString().substring(0, 4);
                int month_no = Integer.parseInt(list.get(i).toString().substring(4, 6)) - 1;
                if((month_no+"").length() ==1 && month_no!=0){
                	day += "0";
                	day += month_no;
                }else if(month_no == 0){
                	day = "";
                	day += Integer.parseInt(list.get(i).toString().substring(0,4))-1+"";
                	day +="12";
                }else{
                	day += month_no;
                }
                int moth_day = Integer.parseInt(list.get(i).toString().substring(6,8));
                if((moth_day+"").length() ==1){
                	day += "0";
                }
                day += moth_day+"";
                days.add(day);
            }
//            //判定是否为第一天或者为第一月
//            if(){
//            	list.get(0).toString().substring(4, 6)
//            }
        }
        return days;
    }
    /**
     * 转换一个字符串，能够转换成时间格式，就返回true，否知返回false
     */
    public boolean stringToDate(String str){
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        try{
            Date date = format.parse(str);
            String str1 = format.format(date);
            if(str.trim().equals(str1.trim())){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
}
