package tydic.meta.module.report;

import tydic.frame.SystemVariable;
import tydic.meta.common.ClassContextUtil;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用 :报表常量定义类
 * @date 2012-04-05
 */
public class ReportConstant {

    /**
     * 报表查询类型：按日期
     */
    public final static int REPORT_QUERY_CONTROL_DAY = 1;

    /**
     * 报表查询类型：日期介于
     */
    public final static int REPORT_QUERY_CONTROL_DAY_BETWEEN = 2;

    /**
     * 报表查询类型：按地域维度查询
     */
    public final static int REPORT_QUERY_CONTROL_ZONE = 3;

    /**
     * 报表查询类型，按照其他维度
     */
    public final static int REPORT_QUERY_CONTROL_OTHER_DIM = 4;

    /**
     * 报表有效时间：1年
     */
    public final static int REPORT_EFFECT_TIME_ONE_YEAR = 1;

    /**
     * 报表有效时间:长久
     */
    public final static int REPORT_EFFECT_TIME_ALL = 2;

    /**
     * 报表操作类型新增
     */
    public final static int PEPORT_OPERATE_TYPE_ADD = 1;
    /**
     * 报表操作类型修改
     */
    public final static int PEPORT_OPERATE_TYPE_UPDATE = 2;
    /**
     * 报表操作类型下线
     */
    public final static int PEPORT_OPERATE_TYPE_OFFLINE = 3;
    /**
     * 报表管理码表类型：PRT_AGREED 应用约定
     */
    public final static String REPORT_PRT_AGREED = "PRT_AGREED";
    /**
     * 报表管理码表类型：IS_SHOW 是否展现
     */
    public final static String REPORT_IS_SHOW = "IS_SHOW";
    /**
     * 报表管理码表类型：DATA_CYCLE 数据周期
     */
    public final static String REPORT_DATA_CYCLE = "DATA_CYCLE";
    /**
     * 报表管理码表类型：REPORT_TIME 报表有效时间
     */
    public final static String REPORT_TIME = "REPORT_TIME";
    /**
     * 数据模型日志，新增数据模型
     */
    public final static int REPORT_ISSUE_LOG_INSERT_TYPE = 11;
    /**
     * 数据模型日志，修改数据模型
     */
    public final static int REPORT_ISSUE_LOG_UPDATE_TYPE = 12;
    /**
     * 数据模型日志，下线数据模型
     */
    public final static int REPORT_ISSUE_LOG_OFF_TYPE = 21;
    /**
     * 报表权限等级，私有
     */
    public final static int REPORT_RIGHT_LEVEL_PRIVATE = 1;

    /**
     * 报表权限等级，共有
     */
    public final static int REPORT_RIGHT_LEVEL_PUBLIC = 0;
    /**
     * 地域默认参数
     */
    public final static String USER_ZONE = "{USER_ZONE}";
    /**
     * 年报表（DIM_LEVEL+DIM_TYPE_ID+DIM_TABLE_ID）
     */
    public final static String REP_YEAR = "#111#,#121#";
    /**
     * 月报表（DIM_LEVEL+DIM_TYPE_ID+DIM_TABLE_ID）
     */
    public final static String REP_MONTH = "#211#";
    /**
     * 周报表（DIM_LEVEL+DIM_TYPE_ID+DIM_TABLE_ID）
     */
    public final static String REP_WEEK = "#221#";
    /**
     * 日报表（DIM_LEVEL+DIM_TYPE_ID+DIM_TABLE_ID）
     */
    public final static String REP_DAY = "#311#,#321#";
    /**
     * sql列别名（后面跟序号）
     */
    public final static String SQLASCOL = "COL";
    /**
     * 订阅周期类型：一次性
     */
    public final static int SEND_SEQUNCE_ONEOFF = 0;
    /**
     * 订阅周期类型：年
     */
    public final static int SEND_SEQUNCE_YEAR = 1;
    /**
     * 订阅周期类型：半年
     */
    public final static int SEND_SEQUNCE_HALFYEAR = 2;
    /**
     * 订阅周期类型：月
     */
    public final static int SEND_SEQUNCE_MONTH = 3;
    /**
     * 订阅周期类型：周
     */
    public final static int SEND_SEQUNCE_WEEK = 4;
    /**
     * 订阅周期类型：日
     */
    public final static int SEND_SEQUNCE_DAY = 5;
    /**
     * 订阅周期类型：时
     */
    public final static int SEND_SEQUNCE_HOUR = 6;
    /**
     * 邮件发送人邮箱地址
     */
    public final static String FROMADDR = SystemVariable.getString("mail.fromAddr", "wuxl@tydic.com");
    /**
     * 邮件发送人邮箱名称
     */
    public final static String FNAME = SystemVariable.getString("mail.fname", "wuxl@tydic.com");
    /**
     * 邮件发送人邮箱密码
     */
    public final static String FPWD = SystemVariable.getString("mail.fpwd", "fbd*:via$99");
    /**
     * 发送人邮件服务器端口
     */
    public final static String MAILPORT = SystemVariable.getString("mail.port", "25");
    /**
     * 发送人邮件主机名
     */
    public final static String HOSTNAME = SystemVariable.getString("mail.hostName", "tydic.com");
    /**
     * 邮件附件根目录
     */
    public final static String MAILFILEPATH = ClassContextUtil.getInstance().getWebAppRootPath() + SystemVariable.getString("mail.filePath", "/meta/module/reportManage/reportFiles/");
    /**
     * 发送邮件时，允许出错次数
     */
    public final static int TIMERPUSHERRORNUMLOG = SystemVariable.getInt("timer.push.errer.num.log", 3);
    /**
     * 发送邮件，定时器扫描周期（单位：毫秒）
     */
    public final static long TIMERDELAY = SystemVariable.getLong("timer.delay", 300000);
    /**
     * 是否清单：是
     */
    public final static int IS_LISTING_YES = 1;

    /**
     * 是否清单:否
     */
    public final static int IS_LISTING_NO = 0;
    /**
     * 訂閱類型--郵件
     */
    public final static int PUSHTYPEMAIL = 1;
    /**
     * 訂閱類型--彩信
     */
    public final static int PUSHTYPCX = 2;
    /**
     * 訂閱類型--短信
     */
    public final static int PUSHTYPDX = 3;
    /**
     * 报表钻取标识--钻取
     */
    public final static int REPORT_DUCK_FALG_YES = 1;

    /**
     * 报表钻取标识--不钻取
     */
    public final static int REPORT_DUCK_FALG_NO = 0;
    /**
     * 发送邮件时，连接池最大值
     */
    public final static int TIMERTHREADPOOLSIZE = SystemVariable.getInt("timer.threadPoolSize", 5);
}
