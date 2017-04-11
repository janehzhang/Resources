package tydic.meta.module.tbl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tydic.frame.BaseDAO;
import tydic.frame.DataSourceManager;
import tydic.meta.common.Common;
import tydic.meta.common.Constant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 表类常用工具类。 <br>
 * @date 2011-10-31
 */
public class TblCommon extends BaseDAO{
    /**
     * 宏变量正则表达式，预先编译。
     */
    private  static Pattern marcoPattern=null;
    static {
        String marcoStr= Common.join(TblConstant.MACRO, "|").replaceAll("\\{","\\\\{").replaceAll("\\}","\\\\}");
        marcoPattern= Pattern.compile(marcoStr);
    }
    /**
     * 宏变量替换
     * @param marco
     * @return
     */
    public  static String marcoFormat(String marco){
        //替换括号。
        marco=marco.replaceAll("\\{","").replaceAll("\\}","");
        //如果是本地网或者本地网名称，指定一个特殊值。
        marco=marco.replaceAll("LOCAL_CODE","028").replaceAll("LOCAL_NAME","成都");
        //日期类宏变量处理。将前缀"N_"替换掉。
        marco=marco.replaceAll("N_(\\w*)","$1");
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        //如果以"N"或者"P"结尾表示上月或者下月或者上一日
        if(marco.equals("YYYYMMN")){//下一月。
            //加一月
            calendar.add(Calendar.MONTH,1);
            date=calendar.getTime();
            marco="YYYYMM";
        }else if(marco.equals("YYYYMMP")){//上一月
            calendar.add(Calendar.MONTH,-1);
            date=calendar.getTime();
            marco="YYYYMM";
        }else if(marco.equals("YYYYMMDDP")){//上一日
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            date=calendar.getTime();
            marco="YYYYMMDD";
        }
        marco=marco.replaceAll("Y","y").replaceAll("D","d").replaceAll("MI","mm").replaceAll("S","s");
        return new SimpleDateFormat(marco).format(date);
    }

    /**
     * 替换SQL中的宏变量
     * @param sql
     * @return
     */
    public static String sqlMarcoReplace(String sql){
        Matcher matcher=marcoPattern.matcher(sql);
        String finalSql=sql;
        while(matcher.find()){
            finalSql=matcher.replaceFirst(TblCommon.marcoFormat(matcher.group(0)));
            matcher = marcoPattern.matcher(finalSql);
        }
        return  finalSql;
    }

    /**
     * 判断是否是Oracle数据类型。
     * @param dataType
     * @return
     */
    public static boolean  isNumber(String dataType){
        if(dataType.equalsIgnoreCase("NUMBER")
           ||dataType.equalsIgnoreCase("DECIMAL")||dataType.equalsIgnoreCase("FLOAT")
           || dataType.equalsIgnoreCase("INTEGER")) {
            return true;
        }
        return false;
    }

    /**
     * 组装完整的ORACLE数据类型。
     * @param dataType
     * @param colSize
     * @param colPrec
     * @return
     */
    public static String buildDataType(String dataType,int colSize,int colPrec){
        if(dataType.equalsIgnoreCase("DATE")){//date类型不关心长度
            return dataType;
        }
        String rDataType=dataType;
        if(colSize>0){
            rDataType+="("+colSize;
        }else{
            return  rDataType;
        }
        if(colPrec>0){
            rDataType+=","+colPrec;
        }
        rDataType+=")";
        return  rDataType;
    }

    /**
     * 判断指定环境数据库是否存在对应的表
     * @param url 数据库连接URL
     * @param dbUser  数据库连接用户名
     * @param dbPwd    数据库连接用户密码
     * @param tableName  数据库表名
     * @return
     */
    public static boolean isExitsTable(String dataSourceId,String tableName,String tableOwner) throws Exception{
        boolean flag = true;
        Statement statement=null;
        try{
            Connection connection = DataSourceManager.getConnection(dataSourceId);
            statement = connection.createStatement();
            statement.executeQuery("SELECT 1 FROM "+tableOwner+"."+tableName);
        } catch(Exception e){
            if(e instanceof SQLException){
                if(((SQLException) e).getErrorCode()==942){
                    flag=false;
                }
            }else{
                throw e;
            }
        }finally{
            DataSourceManager.destroy();
            if(statement!=null){
                statement.close();
            }
        }
        return flag;
    }

}
