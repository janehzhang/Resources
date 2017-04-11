package tydic.meta.module.dim;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王晶
 *         csv导入文件解析工具类
 */
public class CsvUtil {
    /**
     * 读取csv文件
     */
    public List<Map<String, Object>> readFile(InputStream inputStream) throws Exception {
        List<Map<String, Object>> dataList = null;
        byte[] b = new byte[1024];
        String fileContent = new String();
        try {
            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
            int i = -1;
            while ((i = inputStream.read(b)) > 0) {
                outputstream.write(b, 0, i);
            }
            fileContent = outputstream.toString();
            dataList = read(fileContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataList;
    }

    /**
     * 将上传的文件读取并封装成List,里面包含数据的map
     * 数据的key为第一行的数据.在编码这边前4项是固定必须有的(itemCode,itemName,parCode,itemDesc,其中itemDesc是描述,数据库可以为空,那解析可以为空)
     * 在映射这边都是必须的(itemCode,itemName,srcCode,srcName)
     *
     * @param fileContent 上传的内容
     */
    public List<Map<String, Object>> read(String fileContent) {
        List<Map<String, Object>> dataList = null;
        if (fileContent != null) {
            dataList = new ArrayList<Map<String, Object>>();
            String[] rowArr = fileContent.split("\r");  //取得行数据
            if (rowArr != null && rowArr.length != 0) {
                String[] keyArr = rowArr[0].split(","); //取得第一行的数据,作为key,以后每一行的数据作为一个map,key为此行的数据
                for (int i = 1; i < rowArr.length; i++) {
                    if (i != rowArr.length - 1) {
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        //初始化Map
                        for (int j = 0; j < keyArr.length; j++) {
                              dataMap.put(keyArr[j],null);
                        }
                        String rowStr = rowArr[i];
                        if (rowStr != null && rowStr.length() != 0) {
                            String[] cellArr = rowStr.split(","); //分割某一行的数据,去掉最前面的\n
                            if (cellArr != null && cellArr.length != 0) {
                                for (int j = 0; j < cellArr.length; j++) {
                                    if (cellArr[j] != null && cellArr[j] != "") {
                                        if (j == 0) {
                                            int len = cellArr[j].toString().length();
                                            dataMap.put(keyArr[j], cellArr[j].toString().substring(1, len));//去掉最前面的\n
                                        } else {
                                            dataMap.put(keyArr[j], cellArr[j]); //有值的情况下放本来的值
                                        }
                                    } else {
                                        dataMap.put(keyArr[j], null); //没有值的情况下放null
                                    }
                                }
                            }
                        }
                        dataList.add(dataMap);
                    } else {//处理最后一行的数据,由于csv的\n符号在一行的记录的最前面,去掉后判断该行是否有数据,避免导入数据库中有null的情况
                        String rowStr = rowArr[i];
                        if (rowStr != null && rowStr.length() != 0) {
                            String[] cellArr = rowStr.split(",");
                            if (cellArr[0].toString().trim().length() > 2) { //确定最后的一行去掉\n之后有数据才确定将此行数据放map中
                                Map<String, Object> dataMap = new HashMap<String, Object>();
                                for (int j = 0; j < cellArr.length; j++) {
                                    if (cellArr[j] != null && cellArr[j] != "") {
                                        if (j == 0) {
                                            int len = cellArr[j].toString().length();
                                            dataMap.put(keyArr[j], cellArr[j].toString().substring(1, len));
                                        } else {
                                            dataMap.put(keyArr[j], cellArr[j]);
                                        }
                                    } else {
                                        dataMap.put(keyArr[j], null);
                                    }
                                }
                                dataList.add(dataMap);
                            }
                        }
                    }
                }
            }
        }
        return dataList;
    }
}
