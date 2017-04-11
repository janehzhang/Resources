package tydic.meta.module.tbl;

import tydic.frame.SystemVariable;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 表类管理模块公共静态变量 <br>
 * @date 2011-10-26
 */
public class TblConstant {
    /**
     * 表类管理码表类型：TABLE_TYPE
     */
    public final static String META_SYS_CODE_TABLE_TYPE = "TABLE_TYPE";
    /**
     * 表类管理数据类型：
     */
    public final static String META_SYS_CODE_DATA_TYPE = "DATA_TYPE";
    /**
     * table 类数据源
     */
    public final static String META_DATA_SOURCE_TABLE = "TABLE";
    /**
     * 表状态，有效
     */
    public final static int META_TABLE_STATE_VAILD = 1;
    /**
     * 表状态，无效
     */
    public final static int META_TABLE_STATE_INVAILD = 0;
    /**
     * 表状态，修改。
     */
    public final static int META_TABLE_STATE_MODIFY = 2;

    /**
     * 表字段状态，正常
     */
    public final static int META_TABLE_COLS_STATE_COMMON = 1;

    /**
     * 表字段状态，修改
     */
    public final static int META_TABLE_COLS_STATE_MODIFY = 0;
    /**
     * 表类已经支持的宏变量。
     */
    public final static String[] MACRO = new String[]{"{YY}", "{YYYY}", "{MM}", "{DD}", "{HH}", "{MI}", "{SS}", "{M}", "{D}", "{YYYYMM}", "{YYYYMMN}", "{YYYYMMP}",
            "{YYYYMMDD}", "{YYYYMMDDP}", "{N_YY}", "{N_YYYY}", "{N_MM}", "{N_DD}", "{N_HH}", "{N_MI}", "{N_SS}", "{N_M}", "{N_D}",
            "{N_YYYYMM}", "{N_YYYYMMN}", "{N_YYYYMMP}", "{N_YYYYMMDD}", "{N_YYYYMMDDP}", "{LOCAL_CODE}", "{LOCAL_NAME}"};

    /**
     * 用户历史表用户与表关系类型--申请
     */
    public final static int USER_TAB_REL_TYPE_APPLY = 0;
    //建立
    public final static int USER_TAB_REL_TYPE_BUILD = 1;
    //修改
    public final static int USER_TAB_REL_TYPE_MODIFY = 2;
    //维护
    public final static int USER_TAB_REL_TYPE_MAG = 3;
    //审核通过
    public final static int USER_TAB_REL_TYPE_PASS = 4;
    //审核驳回
    public final static int USER_TAB_REL_TYPE_REJECT = 5;
    //表类上线
    public final static int USER_TAB_REL_TYPE_ONLINE = 6;
    //表类下线
    public final static int USER_TAB_REL_TYPE_OFFLINE = 7;

    //维度表类型，TYPE_ID
    public final static int META_TABLE_TYPE_ID_DIM = 2;
    //接口表类型typeId
    public final static int META_TABLE_TYPE_ID_INT = 3;

    public final static int META_DIM_TYPE_STATE_VALID = 1;

    public final static int META_DIM_TYPE_STATE_INVALID = 0;

    /**
     * 元数据数据源的ID
     */
    public final static int META_DATA_SOURCE_ID = SystemVariable.getInt("currentDataSourceId", 0);
    /**
     * 维度管理数据源ID
     */
    public final static int META_DIM_DATA_SOURCE_ID = SystemVariable.getInt("dimDataSourceId", 0);

    /**
     * 维度申请类型
     */
    public final static String DIM_APPLY_TYPE = "dim_apply";
    //列状态有效/无效
    public final static int COL_STATE_VALID = 1;
    public final static int COL_STATE_INVALID = 0;

    /**
     * 数据源有效
     */
    public static final int DATA_SOURCE_VALID = 1;
    /**
     * 数据源无效
     */
    public static final int DATA_SOURCE_INVALID = 0;
    /**
     * 列类型：维度
     */
    public static final int COL_BUS_TYPE_DIM = 0;

    /**
     * 列类型指标
     */
    public static final int COL_BUS_TYPE_INDEX = 1;

    /**
     * 列类型：标识
     */
    public static final int COL_BUS_TYPE_FLAG = 2;
    /**
     * 列是否允许为空，允许
     */
    public static final int COL_STATE_NULLABELD_YES = 1;
    /**
     * 列是否允许为空，不允许
     */
    public static final int COL_STATE_NULLABELD_NO = 0;

}

