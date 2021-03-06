package tydic.portalCommon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.org.apache.commons.beanutils.BeanUtils;

/**   
 * CSV操作(导出和导入)
 *
 * @author 全霞
 * @version 1.0 Aug 7, 2014 10:30:58 AM   
 */

public class CSVUtils {
	 /**
     * 导出
     * 
     * @param file csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
    public static boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;
        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }
        
        return isSucess;
    }
    
    /**
     * 导入
     * 
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return dataList;
    }
    /**
     * CSV导出测试
     * 
     * @throws Exception
     */
    public void exportCsv() {
        List<String> dataList=new ArrayList<String>();
        dataList.add("1,张三,男");
        dataList.add("2,李四,男");
        dataList.add("3,小红,女");
        boolean isSuccess=CSVUtils.exportCsv(new File("D:/test/ljq.csv"), dataList);
        System.out.println(isSuccess);
    }
    
    /**
     * CSV导入测试
     * 
     * @throws Exception
     */
    public void importCsv()  {
        List<String> dataList=CSVUtils.importCsv(new File("D:/test/ljq.csv"));
        if(dataList!=null && !dataList.isEmpty()){
            for(String data : dataList){
                System.out.println(data);
            }
        }
    }
    
    public static File createCSVFile(List exportData, LinkedHashMap rowMapper,
            String outPutPath) {
        BufferedWriter csvFileOutputStream = null;
        File csvFile = new File(outPutPath);
        try {
        	csvFile.createNewFile();
           // csvFile.createNewFile();
            // GB2312使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "GB2312"), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator
                    .hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                        .next();
                csvFileOutputStream.write("\""
                        + propertyEntry.getValue().toString() + "\"");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {  
                Object row = (Object) iterator.next();               
                for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {  
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();  
                    csvFileOutputStream.write("\""  
                            +  BeanUtils.getProperty(row, propertyEntry.getKey().toString()) + "\"");  
                   if (propertyIterator.hasNext()) {  
                       csvFileOutputStream.write(",");  
                    }  
               }  
                if (iterator.hasNext()) {  
                   csvFileOutputStream.newLine();  
                }  
           }  
            csvFileOutputStream.flush();  
        } catch (Exception e) {  
           e.printStackTrace();  
        } finally {  
           try {  
                csvFileOutputStream.close();  
            } catch (IOException e) {  
               e.printStackTrace();
           }  
       }  
        return csvFile;
    }
}
