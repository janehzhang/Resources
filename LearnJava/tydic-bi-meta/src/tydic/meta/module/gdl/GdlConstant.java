package tydic.meta.module.gdl;

import tydic.frame.SystemVariable;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description 指标常量
 * @date 12-4-1
 * -
 * @modify
 * @modifyDate -
 */
public class GdlConstant {

	//指标审核状态度
    public static final String GDL_AUDIT_STATE_NAME = "GDL_AUDIT_STATE";
    
   //指标操作类型
    public static final String GDL_ALTER_TYPE_NAME = "GDL_ALERT_TYPE";
    
   //指标维度分组计算方式
    public static final String  GDL_GROUP_METHOD_NAME = "GDL_GROUP_METHOD";
    
    //指标类型 码表名称
    public static final String GDL_TYPE_CODE_NAME = "GDL_TYPE";

    //指标状态 码表名称
    public static final String GDL_STATE_CODE_NAME = "GDL_STATE";

    public static final int GDL_STATE_VALID = 1;//有效指标
    public static final int GDL_STATE_INVALID = 0;//无效指标
    public static final int GDL_STATE_DOWNLINE = 3;//下线指标

    //维度表用户前缀
    public static final String DIM_OWNER = SystemVariable.getString("dimTableOwner", "DIM")+".";

    //维度编码显示为checkbox伐值
    public static final int DIM_CODE_VIEWFLAG = SystemVariable.getInt("dimCodeViewFlag",5);//
    
    public static final int GDL_TYPE_BASIC = 0;  //基础指标
    public static final int GDL_TYPE_COMPOSITE = 1;//复合指标
    public static final int GDL_TYPE_EXPR = 2;//计算指标

    public static final int GDL_ALTER_TYPE_CREATE = 1;		//建立
    public static final int GDL_ALTER_TYPE_UPDATE = 2;		//修改
    public static final int GDL_ALTER_TYPE_DOWN = 3;		//下线
    public static final int GDL_ALTER_TYPE_UP = 4;		//上线

    public static final int GDL_NO_AUDIT = 0;		//指标未审核
    public static final int GDL_AUDIT_PASS = 0;		//指标审核通过

    /**
     * 在创建复合指标的时候，储存绑定维度到表META_GDL_TBL_REL_TERM中，GDL_TBL_REL_ID字段设为0
     */
    public static final int BASE_GDL_TBL_REL_ID = 0;

    public static final String GDL_NUMFORMAT_CODENAME = "GDL_NUMFORMAT";//指标格式码表名
}
