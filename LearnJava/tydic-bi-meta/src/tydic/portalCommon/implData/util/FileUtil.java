package tydic.portalCommon.implData.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * 描述：文件IO操作工具类。
 * 
 * @author xiaopingchun, 2011-8-24
 * @version
 * @see
 * @since
 */
public class FileUtil {

    public static final String DEFAULT_FILEENCODING = "UTF-8";

    /**
     * 描述：以字节方式读取文件。
     * 
     * @param fileName
     *            文件名称
     * @param encoding
     *            文件编码
     * @return
     * @throws Exception
     */
    public static String readFile(String fileName, String encoding) throws Exception {
        return readContent(new FileInputStream(fileName), encoding);
    }

    /**
     * 描述：按行的方式读文件，丢弃换行符。
     * 
     * @param fileName
     *            文件名称
     * @param encoding
     *            文件编码
     * @return
     * @throws Exception
     */
    public static String readFileByLine(String fileName, String encoding) throws Exception {
        BufferedReader br = null;
        InputStream is = null;
        if (encoding == null || encoding.length() == 0) {
            encoding = DEFAULT_FILEENCODING;
        }
        StringBuffer buffer = new StringBuffer(8192);
        String lineData = null;
        try {
            is = new FileInputStream(fileName);
            br = new BufferedReader(new InputStreamReader(is, encoding));
            while ((lineData = br.readLine()) != null) {
                buffer.append(lineData);
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                    // 忽略此异常
                }
            }
            else if (is != null) {
                try {
                    is.close();
                }
                catch (Exception e) {
                    // 忽略此异常
                }
            }
        }

        return buffer.toString();
    }

    /**
     * 描述：从输入流中读取字符串数据。并关闭流。
     * 
     * @param is
     *            输入流
     * @param encoding
     *            编码方式
     * @return
     * @throws IOException
     */
    public static String readContent(InputStream is, String encoding) throws IOException {
        byte[] bytes = readContent(is);
        if (encoding == null || encoding.length() == 0) {
            encoding = DEFAULT_FILEENCODING;
        }
        return new String(bytes, encoding);
    }

    /**
     * 描述：从输入流中读取字节数据。并关闭流。
     * 
     * @param is
     *            输入流
     * @return
     * @throws IOException
     */
    public static byte[] readContent(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        BufferedInputStream bis = null;
        int readCount = 0;
        byte[] midBuffer = new byte[1024];
        try {
            bis = new BufferedInputStream(is);
            while ((readCount = bis.read(midBuffer)) > -1) {
                baos.write(midBuffer, 0, readCount);
            }
            baos.flush();
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (bis != null) {
                try {
                    bis.close();
                }
                catch (Exception e) {
                    // 忽略此异常
                }
            }
            else if (is != null) {
                try {
                    is.close();
                }
                catch (Exception e) {
                    // 忽略此异常
                }
            }
        }

        return baos.toByteArray();
    }

    /**
     * 描述：往输出流中写入字符串。并关闭流。
     * 
     * @param os
     *            输出流
     * @param content
     *            字符串
     * @param encoding
     *            编码方式
     * @throws IOException
     */
    public static void writeContent(OutputStream os, String content, String encoding) throws IOException {
        if (content == null || content.length() == 0) {
            return;
        }
        if (encoding == null || encoding.length() == 0) {
            encoding = DEFAULT_FILEENCODING;
        }

        byte[] bytes = content.getBytes(encoding);
        writeContent(os, bytes);
    }

    /**
     * 描述：往输出流中写入字节数组。并关闭流。
     * 
     * @param os
     *            输出流
     * @param buff
     *            缓冲区，字节数组
     * @throws IOException
     */
    public static void writeContent(OutputStream os, byte[] buff) throws IOException {
        if (buff == null || buff.length == 0) {
            return;
        }

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(os);
            bos.write(buff);
            bos.flush();
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (Exception e) {
                    // 忽略此异常
                }
            }
            else if (os != null) {
                try {
                    os.close();
                }
                catch (Exception e) {
                    // 忽略此异常
                }
            }
        }
    }

    public static void writeFile(String file, String content, String encoding) throws IOException {
        creatDirs(file);
        writeContent(new FileOutputStream(file), content, encoding);
    }

    /**
     * 文件拷贝
     * 
     * @param sourceFilePath
     *            源文件路径
     * @param destFilePath
     *            目标文件路径，如果目标路径不存在会一级级创建目录
     * @throws IOException
     */
    public static void copy(String sourceFilePath, String destFilePath) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            creatDirs(destFilePath);
            is = new FileInputStream(sourceFilePath);
            os = new FileOutputStream(destFilePath);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > -1) {
                os.write(buffer, 0, count);
            }
            os.flush();
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e1) {
                    //
                }
            }
            if (os != null) {
                try {
                    os.close();
                }
                catch (Exception e1) {
                    //
                }
            }
        }
    }

    /**
     * @description 迭代生成多级目录
     * @param
     * @return
     * @exception
     */
    public static boolean creatDirs(String filePath) {
        File dir = new File(filePath).getParentFile();
        return dir != null && dir.mkdirs();
    }

    /**
     * @description 根据文件路径获取文件名
     * @param
     * @return
     * @exception
     */
    public static String getFileName(String filePath) {
        return new File(filePath).getName();
    }

    /**
     * 清空文件
     * 
     * @param filePath
     * @throws Exception
     */
    public static void cleanFile(String filePath) throws Exception {
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        }
        finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getFileName("logs/test.txt"));
        System.out.println(getFileName("logs/test2.txt"));
        System.out.println(getFileName("logs\\test.txt"));
        System.out.println(getFileName("logs\\test2.txt"));

        cleanFile("logs/test.txt");
    }

}
