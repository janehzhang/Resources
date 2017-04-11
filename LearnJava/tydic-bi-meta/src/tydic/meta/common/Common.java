package tydic.meta.common;

import tydic.frame.common.Log;
import tydic.frame.common.utils.StringUtils;
import tydic.frame.jdbc.JdbcException;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:bi元数据系统工具包，提供公共的访问方法。
 * @date 2011-9-15
 * @modify 熊小平
 * @description 添加方法tranJavaNameToColumn，详见方法注释
 * @date 2011-11-03
 */
public class Common {

    /**
     * 按照JAVA命名规范将数据库字段名替换成JAVA风格的字段名。例如：THIS_IS_A_STR==>thisIsAStr
     *
     * @param columnName
     * @return
     */
    public static String tranColumnToJavaName(String columnName) {
        // 匹配XXX_格式的命名
        Pattern p = Pattern.compile("([A-Za-z_])([A-Za-z0-9]*)_?");
        Matcher m = p.matcher(columnName);
        int count = 0;
        StringBuffer javaName = new StringBuffer();
        while (m.find()) {
            // java变量第一个字母小写
            if (count++ == 0) {
                javaName.append(m.group(1).toLowerCase());
            } else { // 以下划线分割的第一个字母大写
                javaName.append(m.group(1).toUpperCase());
            }
            javaName.append(m.group(2).toLowerCase());
        }
        return javaName.toString();
    }

    /**
     * 按照数据库字段命名规范将JAVA风格变量替换成数据库字段名。例如：thisIsAStr/ThisIsAStr==>THIS_IS_A_STR。
     * 该方法的不足：参数只能接受形如thisIsAStr/ThisIsAStr的标准java命名，否则将不会得到预期的结果
     *
     * @param javaName
     * @return
     * @author 熊小平
     * @date 2011-11-03
     */
    public static String tranJavaNameToColumn(String javaName) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < javaName.length(); i++) {
            if (i != 0 && Character.isUpperCase(javaName.charAt(i))) {
                //如果是大写字符且不是首字母，则添加下划线
                result.append("_");
            }
            result.append(Character.toUpperCase(javaName.charAt(i)));
        }
        return result.toString();
    }

    /**
     * 将一个数组以一个指定的分隔符连接，如果未设置分隔符，直接连接
     *
     * @param array
     * @return
     */
    public static String join(Object[] array, String spliter) {
        if (array != null) {
            StringBuffer rs = new StringBuffer();
            for (int i = 0; i < array.length - 1; i++) {
                rs.append(array[i] + (spliter == null ? "" : spliter));
            }
            rs.append(array[array.length - 1]);
            return rs.toString();
        }
        return null;
    }

    public static String join(Object[] array) {
        return join(array, ",");
    }

    /**
     * MD5加密
     *
     * @param source
     * @return
     */
    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 格式化Date数据
     *
     * @param date      要格式化的Date
     * @param formatStr 格式
     * @return
     */
    public static String formatDate(Date date, String formatStr) {
        SimpleDateFormat dateToStr = new SimpleDateFormat(formatStr);
        return dateToStr.format(date);
    }

    /**
     * 从字符串解析Date
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static Date parseDate(String date, String formatStr) {
        SimpleDateFormat strToDate = new SimpleDateFormat(formatStr);
        try {
            return strToDate.parse(date);
        } catch (ParseException e) {
            Log.error(null, e);
            return null;
        }
    }

    /**
     * 删除整型数组中的某个元素，返回新的数组
     *
     * @param oldArray 原数组
     * @param index    被删除数据的序列号
     * @return
     */
    public static int[] removeArrayElement(int[] oldArray, int index) {
        try {
            if (oldArray.length > 1) {
                int[] newArray = new int[oldArray.length - 1];
                int newIndex = 0;
                for (int i = 0; i < oldArray.length; i++) {
                    if (i != index) {
                        newArray[newIndex] = oldArray[i];
                        newIndex++;
                    }
                }
                return newArray;
            } else if (index == 0) {
                return new int[0];
            } else {
                return oldArray;
            }
        } catch (Exception e) {
            return oldArray;
        }
    }

    // 汉字问题
    public static String parseChinese(String in) {

        String s = null;
        byte temp[];
        if (in == null) {
            Log.warn("Warn:Chinese null founded!");
            return new String("");
        }
        try {
            temp = in.getBytes("iso-8859-1");
            s = new String(temp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.warn("汉字编码转换出错：" + e.toString());
        }
        return s;
    }

    /**
     * 返回Integer对象
     *
     * @param obj
     * @return
     */
    public static Integer parseInt(Object obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断某个JDBCException是否SQLException
     *
     * @param e JdbcException
     * @return
     */
    public static boolean isSQLException(JdbcException e) {
        boolean rtn = false;
        if (e != null && e.getException() instanceof SQLException) {
            rtn = true;
        }
        return rtn;
    }

    /**
     * 从JdbcException中抽取SQL错误信息
     *
     * @param e JdbcException
     * @return
     */
    public static String getSQLMessage(JdbcException e) {
        String msg = null;
        if (e != null) {
            Exception exception = e.getException();
            if (exception != null && exception instanceof SQLException) {
                msg = exception.getMessage();
            }
        }

        return msg;
    }

    /**
     * 判断一个对象是否是数字 "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     *
     * @param obj
     * @return boolean
     */
    public static boolean isNumeric(Object obj) {
        Class objClass = obj.getClass();
        if (objClass.isPrimitive()) {
            //是基本类型的时候，long,short,int,double算作数字
            if (objClass == Short.TYPE || objClass == Long.TYPE
                    || objClass == Integer.TYPE || objClass == Float.TYPE || objClass == Double.TYPE || objClass == Byte.TYPE) {
                return true;
            }
        } else { //不是基本类型，判断是否是数字类型
            return Number.class.isAssignableFrom(objClass);
        }
        return false;
    }

    /**
     * 替换Map中空值
     *
     * @param map
     * @return
     */
    public static Map<String, Object> replaceNullOfMap(Map<String, Object> map) {
        Set<String> keys = map.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            if (map.get(key) == null)
                map.put(key, "");
        }
        return map;
    }

    /**
     * 获取嵌套MAP的值，比如传入参数key=a.b,就是获取键值=a的Map的键值=b的值
     * @param map
     * @param key
     * @param params
     * @return
     */
    public static Object getLinkMapVal(Map<String, Object> map, String key, String... params) {
        Object obj = null;
        String[] keys = key.split("\\.");
        if (keys.length == 1) {
            obj = map.get(key);
            if (params.length == 0)
                return obj;
            for (int i = 0; i < params.length; i++) {
                if (obj == null)
                    return null;
                obj = ((Map<String, Object>) obj).get(params[i]);
            }
        } else {
            obj = map.get(keys[0]);
            for (int i = 1; i < keys.length; i++) {
                if (obj == null)
                    return null;
                obj = ((Map<String, Object>) obj).get(keys[i]);
            }
        }
        return obj;
    }

    /**
     * 将数据维度打横的函数，此函数先支持一个维度打横
     *
     * @param header 表头，如['时间','地域','产品类型','指标1','指标2'....]
     * @param data   要进行转换的数据,二维数组。
     * @param rule   转换的规则，需要的规则如下：
     *               {
     *               dimDataIndex:要进行转换的维度列索引,数组格式
     *               dimIndex:[]--所有的维度列
     *               tranHeader:转换维度的列表头
     *               tranGdls:[],要进行转换的指标列索引，数组，如果没有，默认为所有维度列全转，
     *               }
     * @return 数据转换之后的数据，第一行为表头信息，从第二行始为对应数据信息。
     */
//    public static Object[][] tranDimData(String[] header, Object[] data, Map<String, Object> rule) {
//
//    }

    /**
     * 将维度数据打横的函数，此函数只支持一个维度打横
     *
     * @param colNames 列集合，如['时间','地域','产品类型','指标1','指标2'....]，必填
     * @param rows     要进行转换的数据，二维数组，必填
     * @param groupCol 原生数据分组的列信息。必填
     * @param transCol 要进行打横的列,必填
     * @param gdlCols  要进行打纵的指标列信息,必填
     * @return 转换之后的数据，数组第一行为表头信息，从第二行始为对应转换之后的数据,第三行为维度转换列的位置
     */
    public static Object[] tranDimData(String[] colNames, Object[][] rows, String[] groupCol, String transCol, String[] gdlCols) {
        //进行参数检查
        if (colNames == null || colNames.length == 0) {
            throw new IllegalArgumentException("所传列集合为空！");
        }
        if (groupCol == null || groupCol.length == 0) {
            throw new IllegalArgumentException("所传分组列为空，转换失败！");
        }
        if (StringUtils.isEmpty(transCol)) {
            throw new IllegalArgumentException("转换列为空！");
        }
        if (gdlCols == null || gdlCols.length == 0) {
            throw new IllegalArgumentException("转换的指标为空，转换失败！");
        }
        //所有的GROUP BY列序号
        List<Integer> groupIndexs = new ArrayList<Integer>();
        //要进行转换的列索引
        int dimDataIndex = -1;
        //要进行转换的指标索引
        Map<String, Integer> tranGdls = new HashMap<String, Integer>();
        Map<String, Integer> columnIndex = new HashMap<String, Integer>();
        for (int i = 0; i < colNames.length; i++) {
            columnIndex.put(colNames[i], i);
        }
        //计算GROUP BY 索引
        boolean isFindTranCode = false;//是否找到要分组的CODE
        for (int j = 0; j < groupCol.length; j++) {
            groupIndexs.add(columnIndex.get(groupCol[j]));
            if (groupCol[j].equals(transCol)) {
                isFindTranCode = true;
            }
        }
        if (!isFindTranCode) {
            String[] temp = new String[groupCol.length + 1];
            System.arraycopy(groupCol, 0, temp, 0, groupCol.length);
            groupCol = temp;
            groupCol[groupCol.length - 1] = transCol;
            groupIndexs.add(columnIndex.get(transCol));
        }
        //计算指标索引
        for (int j = 0; j < gdlCols.length; j++) {
            tranGdls.put(gdlCols[j], columnIndex.get(gdlCols[j]));
        }
        dimDataIndex = columnIndex.get(transCol);
        if (rows == null || rows.length == 0) {
            String[] tranColumns = new String[groupCol.length];
            int count = 0;
            //如果无数据
            for (String columnName : groupCol) {
                if (!columnName.equals(transCol)) {
                    tranColumns[count++] = columnName;
                }
            }
            tranColumns[count] = transCol;
            return new Object[]{
                    tranColumns, new Object[0][0], count
            };
        } else {
            //遍历数据，进行分析
            //所有的维度组合,缓存列
            Set<List> allDimData = new LinkedHashSet<List>();
            //要转换的维度值
            Set tranCode = new LinkedHashSet();
            //维度和指标对应的值，
            //比如维度1,维度2，维度3，指标1==Value
            Map<String, Object> tempMappingData = new HashMap<String, Object>();
            for (int i = 0; i < rows.length; i++) {
                //所有的GROUP BY的维度和标识的集合，除需要列转换的
                List list = new ArrayList();
                //所有的维度和标识的集合
                String allDims = "";
                Object tempTranCode = "";
                for (int count = 0; count < groupIndexs.size(); count++) {
                    int index = groupIndexs.get(count);
                    Object dimValue = rows[i][index];
                    if (index != dimDataIndex) {
                        list.add(dimValue);
                        allDims += dimValue.toString() + ",";
                    } else {//记录的维度值
                        tranCode.add(dimValue);
                        tempTranCode = dimValue;
                    }
                }
                allDims += tempTranCode + ",";
                allDimData.add(list);
                for (Map.Entry<String, Integer> entry : tranGdls.entrySet()) {
                    String allDimsTemp = allDims + entry.getKey();
                    Object value = rows[i][entry.getValue()];
                    if (tempMappingData.containsKey(allDimsTemp)) {
                        value = sum(value, tempMappingData.get(allDimsTemp));
                    }
                    tempMappingData.put(allDimsTemp, value);
                }
            }
            //声明一个转换后的二维数组，其行长度等于GROUP BY的长度*维度转换指标的长度
            int rowLength = allDimData.size() * gdlCols.length;
            //二维数组列的长度=保留维度长度 -1+转换的CODE长度。
            int colLength = groupIndexs.size() + tranCode.size();
            Object[][] data = new Object[rowLength][colLength];
            //分析转换后的列
            String[] tranColumns = new String[colLength];
            int count = 0;
            for (String columnName : groupCol) {
                if (!columnName.equals(transCol)) {
                    tranColumns[count++] = columnName;
                }
            }
            tranColumns[count++] = transCol;
            for (Object code : tranCode) {
                tranColumns[count++] = code.toString();
            }
            //计算转换后的数据
            count = 0;
            for (List groupByDim : allDimData) {
                int j = 0;
                for (Map.Entry<String, Integer> entry : tranGdls.entrySet()) {
                    int z = 0;
                    String dimString = "";
                    //先赋值维度和标识值
                    for (z = 0; z < groupByDim.size(); z++) {
                        String value = groupByDim.get(z).toString();
                        data[count * tranGdls.size() + j][z] = value;
                        dimString += value + ",";
                    }
                    //赋值转换后的维度列
                    data[count * tranGdls.size() + j][z] = entry.getKey();
                    //赋值转换后的维度指标
                    int k = 0;
                    for (Object code : tranCode) {
                        String key = dimString + code + "," + entry.getKey();
                        data[count * tranGdls.size() + j][z + 1 + k++] = tempMappingData.get(key);
                    }
                    j++;
                }
                count++;
            }
            Object[] result = new Object[3];
            result[0] = tranColumns;
            result[1] = data;
            result[2] = groupCol.length - 1;
            return result;
        }
    }


    /**
     * 两个数字相加
     *
     * @param data1
     * @param data2
     * @return
     */
    private static Object sum(Object data1, Object data2) {
        try {
            Number num1 = (Number) data1;
            Number num2 = (Number) data2;
            //将所有的NUMBER转换为BIGDEMAIL进行加减
            BigDecimal big1 = null;
            BigDecimal big2 = null;
            if (num1 instanceof BigDecimal) {
                big1 = (BigDecimal) num1;
            } else {
                big1 = new BigDecimal(num1.toString());
            }
            if (num2 instanceof BigDecimal) {
                big2 = (BigDecimal) num2;
            } else {
                big2 = new BigDecimal(num2.toString());
            }
            return big1.add(big2);
        } catch (Exception e) {
            if (data1 == null) {
                return data2;
            }
            if (data2 == null) {
                return data1;
            }
            return data1.toString() + data2;
        }
    }


}
