package tydic.meta.module.dim;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 维度常量 <br>
 * @date 2011-11-17
 */
public class DimConstant {

    //新增映射申请
    public final static int DIM_AUDIT_APPLY_ADD_MAPPING = 2;

    //未审核
    public final static int DIM_NOT_AUDIT = 0;
    //已审核并审核通过
    public final static int DIM_HAS_AUDIT_PASS = 1;
    //已审核但未审核通过
    public final static int DIM_HAS_AUDIT_NOT_PASS = 2;

    //维度末级显示
    public final static int DIM_LAST_LEVEL_FLAG_DISPLAY = 1;
    //维度末级不显示
    public final static int DIM_LAST_LEVEL_FLAG_UNDISPLAY = 0;
    //维度记录更改
    public final static int DIM_RECORD_CHANGE = 1;
    //新增映射
    public final static int DIM_MAPP_ADD = 2;
    //层级修改
    public final static int DIM_LEVEL_CHANGE = 3;
    //新增维度编码申请
    public final static int DIM_CODE_ADD = 4;
    //维度编码停用申请
    public final static int DIM_CODE_STOP = 5;
    //维度编码归并申请
    public final static int DIM_TYPE_ADD = 6;
    //维度编码上线申请
    public final static int DIM_CODE_ENABLE = 7;
    //修改映射申请
    public final static int DIM_MAPP_UPDATE = 8;
    //删除映射申请
    public final static int DIM_MAPP_DELETE = 9;
    //不显示归并类型末级树（0为非系统创建）
    public final static int DIM_MODE_FALG = 0;
    //显示归并类型末级树（1为系统创建）
    public final static int DIM_MODE_FALG_SHOW = 1;
    //维度映射编码类型
    public final static String DIM_CODE_TYPE = "dim_apply";
    //归并类型有效
    public final static int DIM_TYPE_VALID = 1;
    //归并类型无效
    public final static int DIM_TYPE_INVALID = 0;
    //批量添加code中模板的固定字段格式
    public final static int FIX_COL_COUNT = 4;
    /**
     * 日期归并类型，年月日
     */
    public final static int DIM_DATE_DIM_TYPE_MONTH = 1;
    /**
     * 日期归并类型，年周日
     */
    public final static int DIM_DATE_DIM_TYPE_WEEK = 2;
    /**
     * 日期层次年
     */
    public final static int DIM_DATE_LEVEL_YEAR = 1;
    /**
     * 日期层次月
     */
    public final static int DIM_DATE_LEVEL_MONTH = 2;
    /**
     * 日期层次天
     */
    public final static int DIM_DATE_LEVEL_DATE = 3;

    /**
     * 日期层次周
     */
    public final static int DIM_DATE_LEVEL_WEEK = 3;
}

