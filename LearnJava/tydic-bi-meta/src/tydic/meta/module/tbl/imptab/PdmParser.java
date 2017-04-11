package tydic.meta.module.tbl.imptab;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tydic.frame.common.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description pdm文件解析类，此类将PDM中的所有表、字段进行解析，未进行其关系的解析<br>
 * @date 2012-03-21
 */
public class PdmParser {
    //最大解析数
    public final static int MAX_PARSE_COUNT = 500;

    /**
     * 根据一个输入流进行pdm文件的解析
     *
     * @param inputStream
     * @return 返回的结构中包含了解析后的表和表列的所有信息。如下结构所示
     *         [
     *         {
     *         partablename:表实际名称,如META_TABLE_COLUMNS
     *         tablename:表中文名称 如表类列定义表
     *         columns:[{
     *         colName:字段名
     *         colDataType:字段类型
     *         colNullabled:是否允许为空，1：允许，0：不允许
     *         defaultVal:默认值
     *         colPrec:数字类型精度
     *         colSize: 数字类型长度
     *         isPrimary:是否为主键,0：非主键，1;主键
     *         colBusComment:字段注释
     *         }]
     *         }
     *         ]
     * @throws Exception
     */
    public static List<Map<String, Object>> parse(InputStream inputStream) throws Exception {
        long begin = System.currentTimeMillis();
        Log.info("开始解析");
        //获取XML解析的文档。
        SAXReader reader = new SAXReader();
        Document doc = reader.read(inputStream);
        //获取PDM 的父级根节点
        Node root = doc.selectSingleNode("//c:Children/o:Model");
        List<Map<String, Object>> tables = parseTables(root);
        long end = System.currentTimeMillis();
        Log.info("解析耗时：" + (end - begin));
        //解析表信息
        return tables;
    }

    /**
     * 根据PDM文档结构从父级始解析所有的表
     *
     * @param root 根级节点
     * @return
     */
    private static List<Map<String, Object>> parseTables(Node root) throws Exception {
        List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
        //查找所有的table节点
        List<Node> tablesNodes = root.selectNodes("//c:Tables");
        if (tablesNodes != null && tablesNodes.size() > 0) {
            for (Node tablesNode : tablesNodes) {
                List<Node> tableNodes = tablesNode.selectNodes("//o:Table");
                if (tableNodes != null && tableNodes.size() > 0) {
                    for (Node node : tableNodes) {
                        Map<String, Object> table = new HashMap<String, Object>();
                        try {
                            table.put("partablename", node.selectSingleNode("a:Code").getText());
                            table.put("tablename", node.selectSingleNode("a:Name").getText());
                        } catch (Exception e) {
                            continue;
                        }
                        //表注释
                        Node commentNode = node.selectSingleNode("a:Comment");
                        if (commentNode != null) {
                            table.put("tablebuscomment", commentNode.getText());
                        }
                        table.put("columns", parseColumns(node));
                        tables.add(table);
                        if (tables.size() >= MAX_PARSE_COUNT) {
                            return tables;
                        }
                    }
                }
            }
        }
        return tables;
    }

    /**
     * 由table节点解析表列信息
     *
     * @param tableNode
     * @return 格式如{
     *         colName:字段名
     *         colNameCn:字段中文名
     *         colDataType:字段类型
     *         colNullabled:是否允许为空，1：允许，0：不允许
     *         defaultVal:默认值
     *         colPrec:数字类型精度
     *         colSize: 数字类型长度
     *         isPrimary:是否为主键,0：非主键，1;主键
     *         colBusComment:字段注释
     *         }
     * @throws Exception
     */
    private static List<Map<String, Object>> parseColumns(Node tableNode) throws Exception {
        //先解析主键信息。
        List<Element> primaryKeyNodes = tableNode.selectNodes("c:PrimaryKey/o:Key");
        Set<String> priMaryKeyColums = new HashSet<String>();
        if (primaryKeyNodes != null && primaryKeyNodes.size() > 0) {
            for (Element primaryKeyNode : primaryKeyNodes) {
                String refKey = primaryKeyNode.attribute("Ref").getValue();
                //根据关联的信息获取到键值定义信息
                Element key = (Element) tableNode.selectSingleNode("//o:Key[@Id='" + refKey + "']");
                //获取其对应的列ID
                List<Element> refColumnElements = key.selectNodes("c:Key.Columns/o:Column");
                if (refColumnElements != null && refColumnElements.size() > 0) {
                    for (Element refColumn : refColumnElements) {
                        priMaryKeyColums.add(refColumn.attribute("Ref").getValue());
                    }
                }
            }
        }
        //解析column字段基本属性
        List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
        List<Element> columnNodes = tableNode.selectNodes("c:Columns/o:Column");
        if (columnNodes != null & columnNodes.size() > 0) {
            for (Element columnNode : columnNodes) {
                Map<String, Object> column = new HashMap<String, Object>();
                //获取columnId
                String colId = columnNode.attribute("Id").getValue();
                //字段名
                column.put("COL_NAME", columnNode.selectSingleNode("a:Code").getText());
                //字段中文名
                column.put("COL_NAME_CN", columnNode.selectSingleNode("a:Name").getText());
                //字段类型
                column.put("COL_DATATYPE", columnNode.selectSingleNode("a:DataType") != null ? columnNode.selectSingleNode("a:DataType").getText() : "VARCHAR2(32)");
                //字段长度
                Node lengthNode = columnNode.selectSingleNode("a:Length");
                if (lengthNode != null) {
                    column.put("COL_SIZE", lengthNode.getText());
                }
                //字段精度
                Node precisionNode = columnNode.selectSingleNode("a:Precision");
                if (precisionNode != null) {
                    column.put("COL_PREC", precisionNode.getText());
                }
                //字段默认值
                Node defaultValNode = columnNode.selectSingleNode("a:DefaultValue");
                if (defaultValNode != null) {
                    column.put("DEFAULT_VAL", defaultValNode.getText());
                }
                //字段是否允许为空
                Node nullableNode = columnNode.selectSingleNode("a:Mandatory");
                column.put("COL_NULLABLED", nullableNode == null ? 1 : (nullableNode.getText().equals("1") ? 0 : 1));
                //字段是否为主键
                column.put("IS_PRIMARY", priMaryKeyColums.contains(colId) ? 1 : 0);
                //字段注释
                Node commentNode = columnNode.selectSingleNode("a:Comment");
                if (commentNode != null) {
                    column.put("COL_BUS_COMMENT", commentNode.getText());
                }
                columns.add(column);
            }
        }
        return columns;
    }

    public static void main(String[] args) {
        try {
            long begin = System.currentTimeMillis();
            System.out.print(begin);
            List<Map<String, Object>> tables = parse(new FileInputStream("C:\\Documents and Settings\\Administrator\\桌面\\CRM2.0.pdm"));
            System.out.print(tables);
            long end = System.currentTimeMillis();
            System.out.print("解析耗时：" + (end - begin));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
