package tydic.portalCommon.implData.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DocHelper {

    /**
     * 描述：解析XML为Document。
     * 
     * @param xmlString XML字符串
     * @return
     * @throws DocumentException
     */
    public static Document parseXML(String xmlString) throws DocumentException {
        Document document = DocumentHelper.parseText(xmlString);
        return document;
    }

    /**
     * 描述：根据Document生成XML字符串。
     * 
     * @param document XML文档对象
     * @param encoding 编码方式
     * @param isPrettyPrint 格式化输出
     * @return
     * @throws IOException
     */
    public static String genXML(Document document, String encoding, boolean isPrettyPrint) throws IOException {
        if (document == null) {
            return "";
        }

        OutputFormat format = null;
        if (isPrettyPrint) {
            format = OutputFormat.createPrettyPrint();
        }
        else {
            format = OutputFormat.createCompactFormat();
        }
        if (encoding != null && encoding.length() > 0) {
            // 默认为UTF-8
            format.setEncoding(encoding);
        }
        // format.setSuppressDeclaration(true); // 设置XML声明
        // format.setOmitEncoding(true); // 设置XML声明中的编码格式声明
        format.setNewLineAfterDeclaration(false);
        // format.setLineSeparator("\r\n"); // 设置换行符，默认为\n
        // format.setExpandEmptyElements(true); // 设置展开空结点
        // 是否删除（多余）空格字符。即删除行首和行尾的空格字符，而且行中的连续多个空格字符合并为一个空格。
        // 参照Element.getTextTrim()
        format.setTrimText(false);
        // 是否（在元素前后）补空格字符。即行首和行尾有空格字符而且指定删除多余空格字符时，补一个空格。
        // 参照setTrimText(true)
        format.setPadText(false);

        StringWriter sw = new StringWriter();
        XMLWriter writer = new XMLWriter(sw, format);
        // writer.setEscapeText(false); // 是否转义特殊字符
        writer.write(document);
        writer.flush();
        String xmlString = sw.toString();
        writer.close();

        return xmlString;
    }

    public static Element addElement(Element p, String e, String v) {
        return addElement(p, e, v, false);
    }

    public static Element addElement(Element p, String e, String v, boolean canOmit) {
        Element el = null;
        if (v == null && canOmit) {
            el = p;
        }
        else {
            el = p.addElement(e);
            if (v != null) {
                el.setText(v);
            }
        }
        return el;
    }

    public static Element addElement(Element p, String e, int v) {
        return addElement(p, e, String.valueOf(v));
    }

    public static Element addElement(Element p, String e, int v, boolean canOmit) {
        return addElement(p, e, String.valueOf(v), canOmit);
    }

    public final static String trimNull(String value) {
        return value == null ? "" : value;
    }

    public static void main(String[] args) throws Exception {
        Document document = parseXML(FileUtil.readContent(new FileInputStream("xmlContent.xml"), "GBK"));
        String xmlString = document.selectSingleNode("//eml").getStringValue();
        System.out.println(xmlString);
    }

}
