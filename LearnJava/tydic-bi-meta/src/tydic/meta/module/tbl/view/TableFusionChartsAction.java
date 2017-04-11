package tydic.meta.module.tbl.view;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.Common;
import tydic.meta.module.tbl.MetaTableInstDAO;

import java.util.*;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 表类全息视图_FusionCharts 显示Action <br>
 * @date 2011-11-09
 */
public class TableFusionChartsAction {

    /**
     * 数据处理类
     */
    private MetaTableInstDAO metaTableInstDAO;

    //测试数据
    private static String testXml = "<chart formatNumber='0' formatNumberScale='0' decimalPrecision='0' labelStep='2' canvasBorderThickness='0' showBorder='0'  baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='0' hovercapbg='FFECAA' hovercapborder='F47E00' showZeroPlane='0' showhovercap='0' showValues='0' rotateValues='1' numVDivLines='5' adjustDiv='0' yAxisValueDecimals='5' yaxisminvalue='0' chartRightMargin='20' chartLeftMargin='10' showToolTip='1'>" +
            "<categories>" +
            "<category name='10.11' />" +
            "<category name='10.12' />" +
            "<category name='10.13' />" +
            "<category name='10.14' />" +
            "<category name='10.15' />" +
            "<category name='10.16' />" +
            "<category name='10.17' />" +
            "<category name='10.18' />" +
            "<category name='10.19' />" +
            "<category name='10.20' />" +
            "<category name='10.21' />" +
            "<category name='10.22' />" +
            "<category name='10.23' />" +
            "<category name='10.24' />" +
            "<category name='10.25' />" +
            "<category name='10.26' />" +
            "<category name='10.27' />" +
            "<category name='10.28' />" +
            "<category name='10.29' />" +
            "<category name='10.30' />" +
            "<category name='10.31' />" +
            "<category name='11.01' />" +
            "<category name='11.02' />" +
            "<category name='11.03' />" +
            "<category name='11.04' />" +
            "<category name='11.05' />" +
            "<category name='11.06' />" +
            "<category name='11.07' />" +
            "<category name='11.08' />" +
            "</categories>" +
            "<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='FF2121' color='FF2121'   anchorBorderColor='FFFFFF' >" +
            "<set  value='17747' hoverText='当期值：17747'/>" +
            "<set  value='2861' hoverText='当期值：2861'/>" +
            "<set  value='9049' hoverText='当期值：9049'/>" +
            "<set  value='15912' hoverText='当期值：15912'/>" +
            "<set  value='19457' hoverText='当期值：19457'/>" +
            "<set  value='21946' hoverText='当期值：21946'/>" +
            "<set  value='27014' hoverText='当期值：27014'/>" +
            "<set  value='31533' hoverText='当期值：31533'/>" +
            "<set  value='36563' hoverText='当期值：36563'/>" +
            "<set  value='42497' hoverText='当期值：42497'/>" +
            "<set  value='52930' hoverText='当期值：52930'/>" +
            "<set  value='55727' hoverText='当期值：55727'/>" +
            "<set  value='57014' hoverText='当期值：57014'/>" +
            "<set  value='64400' hoverText='当期值：64400'/>" +
            "<set  value='72433' hoverText='当期值：72433'/>" +
            "<set  value='80545' hoverText='当期值：80545'/>" +
            "<set  value='89097' hoverText='当期值：89097'/>" +
            "<set  value='96881' hoverText='当期值：96881'/>" +
            "<set  value='102487' hoverText='当期值：102487'/>" +
            "<set  value='103021' hoverText='当期值：103021'/>" +
            "<set  value='104009' hoverText='当期值：104009'/>" +
            "<set  value='2109' hoverText='当期值：2109'/>" +
            "<set  value='6497' hoverText='当期值：6497'/>" +
            "<set  value='11810' hoverText='当期值：11810'/>" +
            "<set  value='15433' hoverText='当期值：15433'/>" +
            "<set  value='16508' hoverText='当期值：16508'/>" +
            "<set  value='13732' hoverText='当期值：13732'/>" +
            "<set  value='7330' hoverText='当期值：7330'/>" +
            "<set  value='-3179' hoverText='当期值：-3179'/>" +
            "</dataset>" +

            "</chart>";

    /**
     * 生成FusionCharts
     * @param tableInstId
     * @param tableId
     * @return
     */
    public String[] queryChartData(int tableInstId, int tableId, int tableVersion){
        String returnData[] = new String[2]; //返回前台的数据，returnData[0]： FusionCharts的XML数据，returnData[1]：Charts右侧过滤Table的HTML代码
        /**
         * FusionCharts数据结构：
         * {
         *      category:[
         *          {name:"xxxx1"},
         *          {name:"xxxx2"}
         *      ],
         *      dataset:[
         *          [
         *              {value:"v1",hoverText:"h1",    第一条线
         *              {value:"v2",hoverText:"h2"}
         *          ],
         *          [
         *              {value:"v1",hoverText:"h1",    第二条线
         *              {value:"v2",hoverText:"h2"}
         *          ]
         *      ]
         * }
         */
        Map<String,Object> chartData = new HashMap<String, Object>();
        //X轴数据点
        List<Map<String, String>> categoryList = new ArrayList<Map<String, String>>();
        //指标值
        List<Map<String, String>> datasetList[] = null;
        int minY = 99999999;//Y轴最低坐标值
        int minX = 99999999;//x轴最低坐标值
        if(tableInstId == 0){//显示FusionCharts首页信息
            List<Map<String,Object>> instList = metaTableInstDAO.queryTableInstanceByTableIdAnVersion(tableId, tableVersion, null);
            datasetList = new ArrayList[1];
            datasetList[0] = new ArrayList<Map<String, String>>();
            for(int i = 0; i < instList.size(); i++){
                Map<String, Object> itrMap = instList.get(i);
                Map<String, String> categoryMap = new HashMap<String, String>();
                categoryMap.put("name", Convert.toString(itrMap.get("TABLE_NAME")));//设置X轴坐标：实例表名称
                categoryList.add(categoryMap);
                Map<String, String> datasetMap = new HashMap<String, String>();
                datasetMap.put("value", Convert.toString(itrMap.get("TABLE_RECORDS")));//Y轴值：记录数
                //设置最低坐标值
                int records = Common.parseInt(itrMap.get("TABLE_RECORDS"))==null?0:Common.parseInt(itrMap.get("TABLE_RECORDS"));
                if(records<minY){
                    minY = records;
                }
                datasetMap.put("hoverText", Convert.toString("记录数：" + Convert.toString(itrMap.get("TABLE_RECORDS"))));//鼠标悬停记录点提示

                datasetList[0].add(datasetMap);
            }
        } else {
            List<Map<String,Object>> instDataCycleNullList = metaTableInstDAO.queryTableInstCycleNullByInstId(tableInstId);

            if(instDataCycleNullList.size()<1){//如果不存在数据周期为空的记录
                //本地网信息
                List<Map<String,Object>> localData = metaTableInstDAO.queryLocalCodeByInstId(tableInstId);
                datasetList = new ArrayList[localData.size()];
                for(int jj = 0; jj<localData.size(); jj++){
                    datasetList[jj]=new ArrayList<Map<String, String>>();
                    //点击某行，显示实例数据信息
                    List<Map<String,Object>> instDataList = metaTableInstDAO.queryTableInstDataByInstId(tableInstId, Convert.toString(localData.get(jj).get("DATA_LOCAL_CODE")));
                    for(int i = 0; i < instDataList.size(); i++){
                        Map<String, Object> itrMap = instDataList.get(i);
                        //X 轴坐标只添加一次
                        if(jj == 0){
                            Map<String, String> categoryMap = new HashMap<String, String>();
                            categoryMap.put("name", Convert.toString(itrMap.get("DATA_CYCLE_NO")));//设置X轴坐标：数据周期
                            categoryList.add(categoryMap);
                        }
                        Map<String, String> datasetMap = new HashMap<String, String>();
                        datasetMap.put("value", Convert.toString(itrMap.get("ROW_RECORDS")));//Y轴值：记录数
                        //设置最低坐标值
                        if(Integer.parseInt(itrMap.get("ROW_RECORDS").toString())<minY){
                            minY = Integer.parseInt(itrMap.get("ROW_RECORDS").toString());
                        }
                        datasetMap.put("hoverText", Convert.toString("记录数：" + Convert.toString(itrMap.get("ROW_RECORDS"))));//鼠标悬停记录点提示
                        datasetList[jj].add(datasetMap);

                    }
                }
                // 构造表格的HTML
                returnData[1] = generalNewTableHtml(localData);


            }else{
                datasetList = new ArrayList[1];
                datasetList[0]=new ArrayList<Map<String, String>>();
                //若存在数据周期为空的记录 则前台只显示数据周期为空的记录 并且X轴坐标为对应的DATA_LOCAL_CODE（本地网）
                for(int i = 0; i < instDataCycleNullList.size(); i++){
                    Map<String, Object> itrMap = instDataCycleNullList.get(i);
                    Map<String, String> categoryMap = new HashMap<String, String>();
                    categoryMap.put("name", Convert.toString(itrMap.get("ZONE_NAME")));//设置X轴坐标：数据周期
                    categoryList.add(categoryMap);
                    Map<String, String> datasetMap = new HashMap<String, String>();
                    datasetMap.put("value", Convert.toString(itrMap.get("ROW_RECORDS")));//Y轴值：记录数
                    //设置最低坐标值
                    if(Integer.parseInt(itrMap.get("ROW_RECORDS").toString())<minY){
                        minY = Integer.parseInt(itrMap.get("ROW_RECORDS").toString());
                    }
                    datasetMap.put("hoverText", Convert.toString("记录数：" + Convert.toString(itrMap.get("ROW_RECORDS"))));//鼠标悬停记录点提示
                    datasetList[0].add(datasetMap);
                }
                returnData[1]="";
            }
        }
        chartData.put("category", categoryList);
        chartData.put("dataset", datasetList);
        returnData[0]= gerenalXmlData(chartData, minY, LocalColorConstant.colors);//Charts数据XML

        return returnData;
    }

    /**
     * 点击Charts右侧CheckBox事件
     * @param data
     * @return
     */
    public String changeChartData(Map<String, Object> data){
        int tableInstId = Integer.parseInt(data.get("tableInstId").toString());
        //X轴数据点
        List<Map<String, String>> categoryList = new ArrayList<Map<String, String>>();
        //指标值
        List<Map<String, String>> datasetList[] = null;
        //data主键不固定，唯一固定的是：tableId以及tableInstId
        List<String> localIds = new ArrayList<String>();//需要显示的地域
        for(Iterator iter = data.keySet().iterator(); iter.hasNext();) {
            Object key = iter.next();
            if("tableId".equals(Convert.toString(key))||"tableInstId".equals(Convert.toString(key))){
                continue;
            }else{
                if("true".equalsIgnoreCase(Convert.toString(data.get(key)))){
                    localIds.add(Convert.toString(key));//地域ID字符串，例如028_ff00ff
                }
            }
        }
        String[] colors = new String[localIds.size()];
        datasetList = new ArrayList[localIds.size()];
        int minY=9999999;
        for(int jj=0; jj<localIds.size(); jj++){
            datasetList[jj]=new ArrayList<Map<String, String>>();
            String localIdStr = localIds.get(jj);
            String[] idAndColor = localIdStr.split("_");
            colors[jj] = idAndColor[1];
            String localCode = idAndColor[0];
            List<Map<String,Object>> instDataList = metaTableInstDAO.queryTableInstDataByInstId(tableInstId, localCode);
            for(int i = 0; i < instDataList.size(); i++){
                Map<String, Object> itrMap = instDataList.get(i);
                //X 轴坐标只添加一次
                if(jj == 0){
                    Map<String, String> categoryMap = new HashMap<String, String>();
                    categoryMap.put("name", Convert.toString(itrMap.get("DATA_CYCLE_NO")));//设置X轴坐标：数据周期
                    categoryList.add(categoryMap);
                }
                Map<String, String> datasetMap = new HashMap<String, String>();
                datasetMap.put("value", Convert.toString(itrMap.get("ROW_RECORDS")));//Y轴值：记录数
                //设置最低坐标值
                if(Integer.parseInt(itrMap.get("ROW_RECORDS").toString())<minY){
                    minY = Integer.parseInt(itrMap.get("ROW_RECORDS").toString());
                }
                datasetMap.put("hoverText", Convert.toString("记录数：" + Convert.toString(itrMap.get("ROW_RECORDS"))));//鼠标悬停记录点提示
                datasetList[jj].add(datasetMap);
            }
        }
        Map<String,Object> chartData = new HashMap<String, Object>();
        chartData.put("category", categoryList);
        chartData.put("dataset", datasetList);
        return gerenalXmlData(chartData, minY, colors);//Charts数据XML
    }

    /**
     * 生成符合FusionCharts数据格式的XML
     * @param chartData
     * @return
     */
    private String gerenalXmlData(Map<String,Object> chartData, int minY, String[] colors){
		StringBuffer sb = new StringBuffer("<chart formatNumber='0' " +
                "formatNumberScale='0' decimalPrecision='0' labelStep='1' " +
                "canvasBorderThickness='0' showBorder='0'  baseFontSize='12' " +
                "caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' " +
                "divLineAlpha='0' hovercapbg='FFECAA' hovercapborder='F47E00' " +
                "showZeroPlane='0' showhovercap='0' showValues='0'  rotateNames='0' rotateValues='1' " +
                "numVDivLines='5' adjustDiv='0' xAxisValueDecimals='0'  labelDisplay='Rotate' slantLabels='1' yAxisValueDecimals='0' chartLeftMargin='50' yaxisminvalue='"+minY/2+"' " +
                "chartRightMargin='50' chartLeftMargin='10' showToolTip='1'>");
        sb.append("<categories>");
        List<Map<String, String>> categoryList = (List<Map<String, String>>)chartData.get("category");
        for(int i=0; i<categoryList.size(); i++){
            Map<String, String> itrMap = categoryList.get(i);
            sb.append("<category name='"+itrMap.get("name")+"' />");
        }
        sb.append("</categories>");

        List<Map<String, String>>[] datasetList = (List<Map<String, String>>[])chartData.get("dataset");
        for(int jj=0; jj < datasetList.length; jj++){
            sb.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+colors[jj]+"' color='"+colors[jj]+"'   anchorBorderColor='FFFFFF' >");
            List<Map<String, String>> oneList = datasetList[jj];
            for(int i=0; i<oneList.size(); i++){
                Map<String, String> itrMap = oneList.get(i);
                sb.append("<set  value='"+itrMap.get("value")+"' hoverText='"+itrMap.get("hoverText")+"'/>");
            }
            sb.append("</dataset>");
        }

        sb.append("</chart>");
        return sb.toString();
    }

    /**
     * 根据本地网信息生成表数据HTML
     * @param localData
     * @return
     */
    private String generalNewTableHtml(List<Map<String,Object>> localData){
//        测试数据
//        String tableXML = "<table>\n" +
//                        " <tr style=\"height: auto; display: none;\">\n" +
//                        " <th style=\"height: 0px; width: 10%\"></th>\n" +
//                        " <th style=\"height: 0px; width: 10%\"></th>\n" +
//                        " <th style=\"height: 0px; width: 80%\"></th>\n" +
//                        "\n" +
//                        " </tr>\n" +
//                        " <tr>\n" +
//                        " <td style=\"width: 10%;\" align=\"left\"><div style=\"background-color: #ff3333;height: 12px;width: 12px;\"></div></td>\n" +
//                        " <td style=\"width: 10%\" align=\"right\"><input type=\"checkbox\" value=\"ck028\" checked></td>\n" +
//                        " <td style=\"width: 80%\" align=\"left\">成都</td>\n" +
//                        "\n" +
//                        " </tr>\n" +
//                        " <tr>\n" +
//                        " <td style=\"width: 10%;\" align=\"left\"><div style=\"background-color: #99ff99;height: 12px;width: 12px;\"></div></td>\n" +
//                        " <td style=\"width: 10%\" align=\"right\"><input style=\"color: #009900;\" type=\"checkbox\" value=\"ck0816\" checked></td>\n" +
//                        " <td style=\"width: 80%\" align=\"left\">乐山</td>\n" +
//                        " </tr>\n" +
//                        " </table>";

        StringBuffer sb = new StringBuffer("<table>\n" +
                        " <tr style=\"height: auto; display: none;\">\n" +
                        " <th style=\"height: 0px; width: 10%\"></th>\n" +
                        " <th style=\"height: 0px; width: 10%\"></th>\n" +
                        " <th style=\"height: 0px; width: 80%\"></th>\n" +
                        "\n" +
                        " </tr>\n");

        for(int i=0; i<localData.size(); i++){
            sb.append(" <tr>\n" +
                        " <td style=\"width: 10%;\" align=\"left\"><div style=\"background-color: #"+LocalColorConstant.colors[i]+";height: 12px;width: 12px;\"></div></td>\n" +
                        " <td style=\"width: 10%\" align=\"right\"><input type=\"checkbox\" id=\""+localData.get(i).get("DATA_LOCAL_CODE")+"_"+LocalColorConstant.colors[i]+"\" onclick=\"localSelected(this)\" checked></td>\n" +
                        " <td style=\"width: 80%\" align=\"left\">"+localData.get(i).get("ZONE_NAME")+"</td>\n" +
                        "\n" +
                        " </tr>\n");
        }
        sb.append("</table>");


        return sb.toString();
    }


    public void setMetaTableInstDAO(MetaTableInstDAO metaTableInstDAO) {
        this.metaTableInstDAO = metaTableInstDAO;
    }
}
