package tydic.meta.common.term;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.DataSourceManager;
import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.DataTable;
import tydic.meta.common.Page;
import tydic.meta.rpt.ReportDesignerAction;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 邹远贵
 * @description 条件控件封装
 * @date 12-5-14
 * @modify
 * @modifyDate
 */
public class TermControlAction {
    static String	metaSourceID	= SystemVariable.getString("currentDataSourceId", "config1");

    /**
     * 查询单个条件
     * @param termControl
     * @return
     */
    public static Object[] getTermData(Map<String, Object> termControl){
        return _getTermData(termControl, null);
    }

    /**
     * 私有方法，打包请求过程中初始单个条件
     * @param termControl
     * @param access
     * @return Object[] 可能有2至4个值
     *          Object[0] 执行标识，true或false字符串
     *          Object[1] 异常信息或具体数据(二维数组)
     *          Object[2] Map对象，需要回填改变客户端的属性值
     *          Object[3] 树动态加载时特有，返回默认值路径集，其值为map，默认值做key，路径做value
     */
    private static Object[] _getTermData(Map<String, Object> termControl, DataAccess access){
        DataAccess _access = access;
        Connection con = null;
        try{
            int termType = MapUtils.getInteger(termControl,TermConstant.KEY_termType,-1);
            int dataSrcType = MapUtils.getInteger(termControl,TermConstant.KEY_dataSrcType,0);
            int initType = MapUtils.getInteger(termControl,TermConstant.KEY_initType,0); //初始类型
            String dsql = MapUtils.getString(termControl,TermConstant.KEY_dataRule,"").split(";")[0];

            if (termType <= 0 || dataSrcType==0)
                return null;
            
            TermDataService termDataService = null;
            //后台接口数据
            if(dataSrcType==2){
                if(initType==2){//码表
                    termDataService = new CodeTermDataServiceImpl();
                }else{
                    Class<TermDataService> aa = (Class<TermDataService>) Class.forName(dsql);
                    termDataService = aa.newInstance();
                }
            }else if(initType==1 && "".equals(dsql)){
                termDataService = new DimTermDataServiceImpl();//实现维度构建
            }else{  //普通sql
                termDataService = new TermDataDefaultSerivceImpl();
            }

            final Map<String,Object> backClientAtt = new HashMap<String, Object>();//回填覆盖客户端的属性
            final Map<String,Object> appendData = new HashMap<String, Object>();//附加数据 此值不参与客户端控件绑定
            String dataSrcId = MapUtils.getString(termControl,TermConstant.KEY_dataSrcId);
            if (_access == null)
                _access = new DataAccess(con = DataSourceManager.getConnection(dataSrcId));

            TermDataCall call = new TermDataCall(){
                public void appendDataToClient(String key, Object value) {
                    appendData.put(key,value);
                }
                public void coverTermAttribute(String key, Object value) {
                    backClientAtt.put(key,value);
                }
            };
            String parentID = MapUtils.getString(termControl,TermConstant.KEY_parentID,"");
            Object[][] data = null;
            if(!"".equals(parentID)){
                data = termDataService.getChildData(_access,termControl,parentID,call);
            }else{
                data = termDataService.getData(_access,termControl,call);
            }


            Object[] ret = new Object[4];//返回数据的中间过渡对象
            ret[0]="true";//执行标识，成功
            ret[1] = data;
            ret[2] = backClientAtt;
            ret[3] = appendData;
            return ret;

        }catch (Exception ex){
            Log.error("请求单个条件数据出错!", ex);
            return new Object[] { "false", ex.getMessage() };
        }finally{
            try{
                if (con != null){
                    List<Connection> conns = new ArrayList<Connection>();
                    conns.add(con);
                    DataSourceManager.destroy(conns);
                }
            }catch (Exception ex){
                Log.error("关闭连接出错!", ex);
            }
        }
    }

    /**
     * 打包请求条件
     * @param termControls
     * @return
     */
    public static Object getTermsData(Map<String, Object>[] termControls){
        Connection con = null;
        DataAccess access = new DataAccess(con = DataSourceManager.getConnection(metaSourceID));
        try{
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> map = new HashMap<String, Object>();
            for (Map<String, Object> termControl : termControls) {
                map.put(MapUtils.getString(termControl,TermConstant.KEY_termName), termControl);
            }
            for (Map<String, Object> termCfm : termControls) {
                String parentTermName = MapUtils.getString(termCfm,TermConstant.KEY_parentTerm,"");
                String dataSrcId = MapUtils.getString(termCfm,TermConstant.KEY_dataSrcId);

                if (!parentTermName.equals("") && map.containsKey(parentTermName)) {
                    Map<String, Object> parentCfm = (Map<String, Object>) map.get(parentTermName);
                    String parentTextName = MapUtils.getString(parentCfm,TermConstant.KEY_textName,"");
                    String[] parentValue = MapUtils.getString(parentCfm,TermConstant.KEY_value,"").split(",");
                    String dsql = MapUtils.getString(termCfm,TermConstant.KEY_dataRule,"");
                    dsql = dsql.replaceAll("\\{(?i)" + parentTermName + "\\}", parentValue[0]);
                    if (!parentTextName.equals("") && parentValue.length > 1)
                        dsql = dsql.replaceAll("\\{(?i)" + parentTextName + "\\}", parentValue[1]);

                    termCfm.put(TermConstant.KEY_dataRule,dsql);
                }
                Object[] res = _getTermData(termCfm, (metaSourceID.equals(dataSrcId) ? access : null));
                if (res == null)
                    continue;
                if (res[0].equals("false")) {
                    return res;//只要有任意一个未执行成功，即返回
                }
                result.put(MapUtils.getString(termCfm,TermConstant.KEY_termName), res);
            }
            return result;
        }catch (Exception ex){
            Log.error("打包请求数据出错!", ex);
            return new Object[] { "false", ex.getMessage() };
        }finally{
            try{
                if (con != null)
                    con.close();
            }catch (Exception ex){
            }
            DataSourceManager.destroy();
        }
    }

    public static DataTable qryData(String dataSrcName, String dsql){
        DataAccess access = new DataAccess(DataSourceManager.getConnection(dataSrcName));
        DataTable table = access.queryForDataTable(dsql);
        return table;
    }
    
    public static Map<String,Object> testquery(Map<String,Object> data,Page page){
        Log.debug("page:"+page.getPosStart()+"->"+page.getCount());
        int ros = 25;
        int cos = 6;
        Object[][] a = new Object[ros][cos];
        
        for(int i=1;i<=ros;i++){
            for(int j=1;j<=cos;j++){
                a[i-1][j-1] = "行号*列号="+(i*j);
            }
        }

        Map<String,Object> ret = new HashMap<String,Object>();
        List<Object[]> array = new ArrayList<Object[]>();
        for(int i=1;i<=page.getCount();i++){
            int idx = i+page.getPosStart()-2;
            if(idx==ros)break;
            array.add(a[i+page.getPosStart()-2]);
        }
        ret.put("total",ros);
        ret.put("list",array);

        return ret;
    }
}
