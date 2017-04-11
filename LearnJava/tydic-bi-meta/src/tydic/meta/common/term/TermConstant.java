package tydic.meta.common.term;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 条件常量
 * @date 12-5-29
 * -
 * @modify
 * @modifyDate -
 */
public class TermConstant {

    public static final String KEY_termName = "termName"; //条件控件名
    public static final String KEY_textName = "textName"; //文本宏变量
    public static final String KEY_termType = "termType"; //条件类型0:文本框 1:下拉框 2:下拉树 3:日期控件 时间选择器
    public static final String KEY_parentTerm = "parentTerm"; //依赖父条件(联动）
    public static final String KEY_valueType = "valueType";//条件值数值类型	0:数字   1字符串  2:日期对象
    public static final String KEY_termWidth = "termWidth";//条件宽度
    public static final String KEY_dataSrcType = "dataSrcType"; //数据源类型  1:SQL查询语句 0:固定值列表 ，2后台接口
    public static final String KEY_dataRule = "dataRule"; //数据规则
    public static final String KEY_dataSrcId = "dataSrcId";//数据源ID
    public static final String KEY_defaultValue = "defaultValue";//默认值列表，以 ‘,’分割的字符串
    public static final String KEY_value = "value";//值 条件不同，具体结构不同
    public static final String KEY_initType = "initType";//1 维度表设置 2码表
    public static final String KEY_codeType = "codeType";//码表类型
    public static final String KEY_codeInited = "codeInited";//码表是否在客户端被初始  布尔值
    public static final String KEY_classRuleParams = "classRuleParams";//接口数据 参数
    public static final String KEY_dimTableId = "dimTableId";//
    public static final String KEY_dimTypeId = "dimTypeId";
    public static final String KEY_dimValueType = "dimValueType";//维度数据类型 0:维度编码，1:维度ID
    public static final String KEY_dimDataLevels = "dimDataLevels"; //层级，','分割的字符串
    public static final String KEY_excludeValues = "excludeValues"; //要排除的值，','分割的字符串
    public static final String KEY_dimInitValues = "dimInitValues"; //初始值，','分割的字符串
    public static final String KEY_mulSelect = "mulSelect";//是否多选 true/false （树和下拉框独有）
    public static final String KEY_dynload = "dynload";//是否异步 true/false (树独有)
    public static final String KEY_defaultValuePath = "defaultValuePath";//默认值路径 （树独有，从根到叶子方向的数组）
    public static final String KEY_defValPathInited= "defValPathInited";//布尔值，默认值路径是否已被初始
    public static final String KEY_treeChildFlag= "treeChildFlag";//布尔值，树节点是否多一个标记是否有子的字段
    public static final String KEY_parentID = "parentID";           //class规则，树异步加载数据时，通过此key可获取父节点ID
    public static final String KEY_CONSTANT_SQL="constantSql";//后台常量SQL键值。

}
