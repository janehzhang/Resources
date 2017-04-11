﻿/**
  * 文件名：ConstantStoreProc.java
  * 版本信息：Version 1.0
  * 日期：2013-5-20
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.meta.common.yhd.constant;

import java.io.Serializable;

/***
 * 存储过程的配置
 * @author 我爱家乡
 *
 */
public class ConstantStoreProc implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static final String RPT_VISIT_EWAM_FB_LOG = "RPT_VISIT_EWAM_FB_LOG";
	public static final String RPT_VISIT_EWAM_TS_DETAIL = "RPT_VISIT_EWAM_TS_DETAIL";
	/*
	 * 综调障碍数据清单报表 --存储过程
	 */
	public static final String RPT_FAULT__LIST = "RPT_FAULT_DETAIL";
	/*
	 * 综调开通装移机清单报表 --存储过程
	 */
	public static final String RPT_OP_LIST    ="RPT_OP_DETAIL";
	/*
	 * 客户满意率监测周报表--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_WEEK    ="RPT_VISIT_SATISFY_WEEK";
	/*
	 * 宽带新装--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_WEEK_KD_ADD    ="RPT_VISIT_SATISFY_WEEK_KD_ADD";
	/*
	 * 宽带新装不满意原因TOP周报--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_WEEK_KD_REAS    ="RPT_VISIT_SATISFY_WEEK_KD_REAS";
	/*
	 * 宽带修障服务满意率周报--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_WEEK_KD_UP    ="RPT_VISIT_SATISFY_WEEK_KD_UP";
	/*
	 * 宽带修障不满意原因TOP周报--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_WEEK_KD_UP_R    ="RPT_VISIT_SATISFY_WEEK_KD_UP_R";
	/*
	 * 综调日监测报表
	 */
	public static final String RPT_ZW_DAY    ="RPT_ZW_DAY";
	
	/*
	 * 综调月监测报表
	 */
	public static final String RPT_ZW_MONTH    ="RPT_ZW_MONTH";
	
	/*
	 * 宽带新装即时回访清单
	 */
	public static final String RPT_VISIT_SATISFY_KD_DETALL ="RPT_VISIT_SATISFY_KD_DETALL";
	public static final String RPT_VISIT_IVRZYJ_GK_DETALL ="RPT_VISIT_IVRZYJ_GK_DETALL";
	public static final String RPT_VISIT_IVRZYJ_KD_DETALL ="RPT_VISIT_IVRZYJ_KD_DETALL";
	public static final String RPT_VISIT_IVRZYJ_KD ="RPT_VISIT_IVRZYJ_KD";
	
	public static final String RPT_VISIT_IVRZYJ_KD_DETALL_NEW ="RPT_VISIT_IVRZYJ_KD_DETALL_NEW";
	
	/*
	 * 宽带修障即时回访清单
	 */
	public static final String RPT_VISIT_SATISFY_KD_UP_DETALL ="RPT_VISIT_SATISFY_KD_UP_DETALL";
	
	public static final String RPT_VISIT_IVRXZ_KD_UP_DETALL ="RPT_VISIT_IVRXZ_KD_UP_DETALL";
	
	public static final String RPT_VISIT_IVRXZ_KD_UP_DETALL_N ="RPT_VISIT_IVRXZ_KD_UP_DETALL_N";
	public static final String RPT_VISIT_IVRXZ_GK_DETALL ="RPT_VISIT_IVRXZ_GK_DETALL";
	public static final String RPT_VISIT_IVRXZ_KD_UP ="RPT_VISIT_IVRXZ_KD_UP";
	/***
	 * 新的营业厅及时回防清单
	 */	
	public static final String RPT_VISIT_SATISFY_BUSI_DET_O ="RPT_VISIT_SATISFY_BUSI_DET_O";
	
	
	/*
	 * 集团上报清单
	 */
	public static final String PRT_CS_GROUP_REPORT_LIST_MONTH ="PRT_CS_GROUP_REPORT_LIST_MONTH";
	
	/**
	 * 新的营业厅非实体查看
	 */
	public static final String RPT_VISIT_SATISFY_DIS_REA_0 ="RPT_VISIT_SATISFY_DIS_REA_0";
	
	/*
	 * 投诉清单--存储过程
	 */
	public static final String RPT_CMPL_DETAIL ="RPT_CMPL_DETAIL";
	public static final String RPT_CMPL_DETAIL_NEW ="RPT_CMPL_DETAIL_new";
	public static final String RPT_CMPL_DETAIL_N ="RPT_CMPL_DETAIL_N";
	/*
	 * 客户满意率--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_RATIO ="RPT_VISIT_SATISFY_RATIO";
	//满意度评测总体情况
	public static final String RPT_VISIT_SATISFY_ALL ="RPT_VISIT_SATISFY_ALL";
	public static final String RPT_VISIT_SATISFY_ALL_TT ="RPT_VISIT_SATISFY_ALL_TT";
	//满意度测评详情总体报表
	public static final String RPT_VISIT_SATISFY_AREA ="RPT_VISIT_SATISFY_AREA";
	public static final String RPT_VISIT_SATISFY_AREA_TT ="RPT_VISIT_SATISFY_AREA_TT";
	//满意度测评详情总体报表-东莞
	public static final String RPT_VISIT_SATISFY_AREA_DG ="RPT_VISIT_SATISFY_AREA_DG";
	public static final String RPT_VISIT_SATISFY_AREA_DG_TT ="RPT_VISIT_SATISFY_AREA_DG_TT";
	
	//满意度测评详情总体报表--非实体
	public static final String RPT_VISIT_SATISFY_AREA_OTHER ="RPT_VISIT_SATISFY_AREA_OTHER";
	
	/*
	 * 投诉类指标日监测--存储过程
	 */
	public static final String RPT_CMPL_MONIT_DAY ="RPT_CMPL_MONIT_DAY";
	/*
	 * 投诉类指标月监测--存储过程
	 */
	public static final String RPT_CMPL_MONIT_MON ="RPT_CMPL_MONIT_MON";
	/*
	 * 客户满意率各触点报表--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_LEVEL ="RPT_VISIT_SATISFY_LEVEL";
	/*
	 * 客户满意率各触点报表--存储过程_修改版
	 */
	public static final String RPT_VISIT_SATISFY_AREA_ZW ="RPT_VISIT_SATISFY_AREA_ZW";//装维
	public static final String RPT_VISIT_SATISFY_AREA_BUSI ="RPT_VISIT_SATISFY_AREA_BUSI";//营业厅
	/*
	 * 不满意原因TOP报表--存储过程
	 */
	public static final String RPT_VISIT_SATISFY_REASON ="RPT_VISIT_SATISFY_REASON";
	//不满意原因top_最新
	public static final String RPT_VISIT_SATISFY_DIS_REASON ="RPT_VISIT_SATISFY_DIS_REASON";
	
	/*
	 * 投诉--存储过程
	 */
	public static final String P_COMMON_RPT_4J ="P_COMMON_RPT_4J";
	
	/*
	 * 投诉日报表--存储过程
	 */
	public static final String RPT_LOCAL_CMPL_DAY ="RPT_LOCAL_CMPL_DAY";
	
	/*
	 * 投诉现象分析日报表--存储过程
	 */
	public static final String RPT_LOCAL_CMPLPHE_DAY ="RPT_LOCAL_CMPLPHE_DAY";
	
	/*
	 * 投诉--越级投诉清单
	 */
	public static final String RPT_SKIP_CMPL_DETAIL="RPT_SKIP_CMPL_DETAIL";
	
	/**
	 * 营业厅回访清单过程
	 */
	public static final String RPT_VISIT_SATISFY_BUSI_DETALL="RPT_VISIT_SATISFY_BUSI_DETALL";
	public static final String RPT_VISIT_SATISFY_ISHOP_DETALL="RPT_VISIT_SATISFY_ISHOP_DETALL";
	public static final String RPT_VISIT_SATISFY_DX_DETALL="RPT_VISIT_SATISFY_DX_DETALL";
	/**
	 * 投诉回访清单过程
	 */
	public static final String RPT_VISIT_SATISFY_CMPL_DETALL="RPT_VISIT_SATISFY_CMPL_DETALL";
	//vip客戶回访清单过程
	public static final String RPT_VISIT_SATISFY_VIP_DETALL="RPT_VISIT_SATISFY_VIP_DETALL";
	//宽带新装、修障
	public static final String RPT_VISIT_SATISFY_ON_TIME_ZW="RPT_VISIT_SATISFY_ON_TIME_ZW";
	public static final String RPT_VISIT_SATISFY_CHANNEL_ZW="RPT_VISIT_SATISFY_CHANNEL_ZW";
	public static final String RPT_VISIT_SATISFY_GROUP_ZW="RPT_VISIT_SATISFY_GROUP_ZW";
	public static final String RPT_VISIT_SATISFY_CUSTLEVEL_ZW="RPT_VISIT_SATISFY_CUSTLEVEL_ZW";
	public static final String RPT_VISIT_SATISFY_ARPU_ZW="RPT_VISIT_SATISFY_ARPU_ZW";
	public static final String RPT_VISIT_SATISFY_CDMATYPE_ZW="RPT_VISIT_SATISFY_CDMATYPE_ZW";
	public static final String RPT_VISIT_SATISFY_DUTYOP_ZW="RPT_VISIT_SATISFY_DUTYOP_ZW";
	//按维度区分满意度
	public static final String RPT_VISIT_SATISFY_ON_TIME="RPT_VISIT_SATISFY_ON_TIME";
	public static final String RPT_VISIT_SATISFY_CDMATYPE="RPT_VISIT_SATISFY_CDMATYPE";
	public static final String RPT_VISIT_SATISFY_CHANNEL="RPT_VISIT_SATISFY_CHANNEL";
	public static final String RPT_VISIT_SATISFY_CUSTLEVEL="RPT_VISIT_SATISFY_CUSTLEVEL";
	public static final String RPT_VISIT_SATISFY_DUTYOR="RPT_VISIT_SATISFY_DUTYOR";
	public static final String RPT_VISIT_SATISFY_CUSTGROUP="RPT_VISIT_SATISFY_CUSTGROUP";
	public static final String RPT_VISIT_SATISFY_ARPU="RPT_VISIT_SATISFY_ARPU";
	public static final String RPT_VISIT_SATISFY_BUSINESS="RPT_VISIT_SATISFY_BUSINESS";
	public static final String RPT_VISIT_SATISFY_TMETHOD="RPT_VISIT_SATISFY_TMETHOD";
	public static final String RPT_VISIT_SATISFY_PRODTYPE="RPT_VISIT_SATISFY_PRODTYPE";
	public static final String RPT_VISIT_SATISFY_TERMINAL="RPT_VISIT_SATISFY_TERMINAL";
	
	public static final String RPT_VISIT_NO_SATISFY_DETALL ="RPT_VISIT_NO_SATISFY_DETALL";
	//延迟测评
	public static final String RPT_VISIT_SMSDELAY_AREA ="RPT_VISIT_SMSDELAY_AREA";
	public static final String RPT_VISIT_SMSDELAY_DETALL ="RPT_VISIT_SMSDELAY_DETALL";
	//政企满意度
	public static final String RPT_VISIT_ZQMYD_DETALL ="RPT_VISIT_ZQMYD_DETALL";
	public static final String RPT_VISIT_ZQMYD_AREA ="RPT_VISIT_ZQMYD_AREA";
	public static final String RPT_VISIT_ZQMYD_AREA_WEEK ="RPT_VISIT_ZQMYD_AREA_WEEK";
	public static final String RPT_VISIT_ZQMYD_UNSATISFY ="RPT_VISIT_ZQMYD_UNSATISFY";
	//投诉
	public static final String RPT_VISIT_SATISFY_TS_DETALL ="RPT_VISIT_SATISFY_TS_DETALL";
	//营业厅
	public static final String RPT_VISIT_SATISFY_ON_TIME_BUSI="RPT_VISIT_SATISFY_ON_TIME_BUSI";
	public static final String RPT_VISIT_SATISFY_CHANNEL_BUSI="RPT_VISIT_SATISFY_CHANNEL_BUSI";
	public static final String RPT_VISIT_SATISFY_GROUP_BUSI="RPT_VISIT_SATISFY_GROUP_BUSI";
	public static final String RPT_VISIT_SATISFY_CUSTLEV_BUSI="RPT_VISIT_SATISFY_CUSTLEV_BUSI";
	public static final String RPT_VISIT_SATISFY_ARPU_BUSI="RPT_VISIT_SATISFY_ARPU_BUSI";
	public static final String RPT_VISIT_SATISFY_CDMATYP_BUSI="RPT_VISIT_SATISFY_CDMATYP_BUSI";
	public static final String RPT_VISIT_SATISFY_DUTYOP_BUSI="RPT_VISIT_SATISFY_DUTYOP_BUSI";
	//10000号
	public static final String RPT_VISIT_SATISFY_AREA_10000="RPT_VISIT_SATISFY_AREA_10000";
	public static final String RPT_VISIT_SATISFY_DUTYOP_10000="RPT_VISIT_SATISFY_DUTYOP_10000";
	public static final String RPT_VISIT_SATISFY_BUSINESS_1W="RPT_VISIT_SATISFY_BUSINESS_1W";
	//网厅
	public static final String RPT_VISIT_SATISFY_AREA_WT="RPT_VISIT_SATISFY_AREA_WT";
	public static final String RPT_VISIT_SATISFY_DUTYOP_WT="RPT_VISIT_SATISFY_DUTYOP_WT";
	//掌厅
	public static final String RPT_VISIT_SATISFY_AREA_ZT="RPT_VISIT_SATISFY_AREA_ZT";
	public static final String RPT_VISIT_SATISFY_DUTYOP_ZT="RPT_VISIT_SATISFY_DUTYOP_ZT";
	//号百
	public static final String RPT_VISIT_SATISFY_AREA_HB="RPT_VISIT_SATISFY_AREA_HB";
	public static final String RPT_VISIT_SATISFY_DUTYOP_HB="RPT_VISIT_SATISFY_DUTYOP_HB";
	public static final String RPT_VISIT_SATISFY_TMETHOD_HB="RPT_VISIT_SATISFY_TMETHOD_HB";
	public static final String RPT_VISIT_SATISFY_BUSINESS_HB="RPT_VISIT_SATISFY_BUSINESS_HB";
	//满意度考核总表
	public static final String RPT_VISIT_SATISFY_MAIN="RPT_VISIT_SATISFY_MAIN";
	public static final String RPT_VISIT_SATISFY_MAIN_T="RPT_VISIT_SATISFY_MAIN_T";
	public static final String RPT_VISIT_SATISFY_DIS_REA_WEEK="RPT_VISIT_SATISFY_DIS_REA_WEEK";
	public static final String RPT_VISIT_SATISFY_DIS_RES_WEEK="RPT_VISIT_SATISFY_DIS_RES_WEEK";
	public static final String RPT_VISIT_SATISFY_DIS_WEEK_TT="RPT_VISIT_SATISFY_DIS_WEEK_TT";
	public static final String RPT_VISIT_NO_SATISFY_WEEK="RPT_VISIT_NO_SATISFY_WEEK";
	public static final String RPT_VISIT_DIS_SATISFY_WEEK="RPT_VISIT_DIS_SATISFY_WEEK";
	/*
	 *  越级投诉月监测 
	 */
	public static final String RPT_CMPL_SPAN_MON ="RPT_CMPL_SPAN_MON";
	/**
	 *  工信部申诉月监测
	 */
	public static final String RPT_CMPL_GXB_MON ="RPT_CMPL_GXB_MON";
	
	/**
	 *  越级投诉清单
	 */
	public static final String RPT_CMPL_SPAN_DETALL ="RPT_CMPL_SPAN_DETALL";
	/**
	 *  10000 号回访满意度清单
	 */
	public static final String RPT_VISIT_SATISFY_10000_DETALL ="RPT_VISIT_SATISFY_10000_DETALL";
	/**
	 *  号百 回访满意度清单
	 */
	public static final String RPT_VISIT_SATISFY_HB_DETALL ="RPT_VISIT_SATISFY_HB_DETALL";
	/**
	 *  网厅 回访满意度清单表
	 */
	public static final String RPT_VISIT_SATISFY_WT_DETALL ="RPT_VISIT_SATISFY_WT_DETALL";
	
	/**
	 *  掌厅 回访满意度清单表
	 */
	public static final String RPT_VISIT_SATISFY_ZT_DETALL ="RPT_VISIT_SATISFY_ZT_DETALL";
	//客户密码管理模块
	public static final String RPT_CUST_PWD_ACT ="RPT_CUST_PWD_ACT";
	public static final String RPT_CUST_PWD_MONITORING ="RPT_CUST_PWD_MONITORING";
	public static final String RPT_CUST_PWD_ACT_LEVEL ="RPT_CUST_PWD_ACT_LEVEL";
	public static final String RPT_CUST_PWD_ACT_LIKE ="RPT_CUST_PWD_ACT_LIKE";
	public static final String RPT_CS_CHANNEL_FUALT_DISTRI ="RPT_CS_CHANNEL_FUALT_DISTRI";
	public static final String RPT_TFJ_INTIME_DAY ="RPT_TFJ_INTIME_DAY";	
	public static final String RPT_TFJ_ORDER_DETAIL ="RPT_TFJ_ORDER_DETAIL";
   //健康度模型
	public static final String RPT_CS_HEALTHY_IDENTIFY ="RPT_CS_HEALTHY_IDENTIFY";//评分架构
	public static final String RPT_CS_HEALTHY_MAIN ="RPT_CS_HEALTHY_MAIN";//总体情况
	public static final String RPT_CS_HEALTHY_AREA ="RPT_CS_HEALTHY_AREA";//业务项
	public static final String RPT_CS_HEALTHY_ITEM ="RPT_CS_HEALTHY_ITEM";//业务指标
	
	public static final String RPT_CS_CHANNEL_SERVICE_AREA ="RPT_CS_CHANNEL_SERVICE_AREA";//全渠道服务区域
	public static final String RPT_KEY_DATA_LISTING ="RPT_KEY_DATA_LISTING";//客服部关键指标满意率
	public static final String PRO_CS_TB_ITANT_ASS_NTQLITY_OT ="PRO_CS_TB_ITANT_ASS_NTQLITY_OT";//客服部关键指标满意率报表
	public static final String RPT_CS_CHANNEL_SERVICE ="RPT_CS_CHANNEL_SERVICE";//全渠道服务
	public static final String RPT_CS_CHANNEL_SERVICE_FIRST ="RPT_CS_CHANNEL_SERVICE_FIRST";//全渠道服务
	public static final String RPT_CS_CHANNEL_SERVICE_SECOND ="RPT_CS_CHANNEL_SERVICE_SECOND";//全渠道服务
	public static final String RPT_CS_CHANNEL_SERVICE_THIRD ="RPT_CS_CHANNEL_SERVICE_THIRD";//全渠道服务
	public static final String RPT_CHANNEL_SERVICE_DETAIL ="RPT_CHANNEL_SERVICE_DETAIL";//全渠道服务清单
	public static final String RPT_CHANNEL_1WLAB_DETAIL ="RPT_CHANNEL_1WLAB_DETAIL";//10000号人工服务清单
	public static final String RPT_CHANNEL_1WAUTO_DETAIL ="RPT_CHANNEL_1WAUTO_DETAIL";//10000号、10001号自助、速播干线
	public static final String RPT_CHANNEL_BUSI_DETAIL ="RPT_CHANNEL_BUSI_DETAIL";//自营厅
	public static final String RPT_CHANNEL_DT_DETAIL ="RPT_CHANNEL_DT_DETAIL";//短厅
	public static final String RPT_CHANNEL_WT_DETAIL ="RPT_CHANNEL_WT_DETAIL";//网厅
	public static final String RPT_CHANNEL_WAP_DETAIL ="RPT_CHANNEL_WAP_DETAIL";//WAP厅
	public static final String RPT_CHANNEL_KHD_DETAIL ="RPT_CHANNEL_KHD_DETAIL";//客户端
	public static final String RPT_CHANNEL_ZZZD_DETAIL ="RPT_CHANNEL_ZZZD_DETAIL";//自助中终端
	public static final String RPT_CHANNEL_IMLAB_DETAIL ="RPT_CHANNEL_IMLAB_DETAIL";//qq、微信、易信人工
	public static final String RPT_CHANNEL_QQAUTO_DETAIL ="RPT_CHANNEL_QQAUTO_DETAIL";//qq、微信、易信人工
	public static final String RPT_CHANNEL_WXAUTO_DETAIL ="RPT_CHANNEL_WXAUTO_DETAIL";//微信自助
	public static final String RPT_CHANNEL_YXAUTO_DETAIL ="RPT_CHANNEL_YXAUTO_DETAIL";//易信自助
	
	
	
	public static final String RPT_CUST_PWD_ACT_SUM="RPT_CUST_PWD_ACT_SUM";//客户密码激活率总体报表 存储过程
	public static final String RPT_CS_CHANNEL_SERV_AREA_KEY="RPT_CS_CHANNEL_SERV_AREA_KEY";//渠道服务关键指标
	public static final String RPT_CHANNEL_INDEX_DETAIL="RPT_CHANNEL_INDEX_DETAIL";//渠道服务业务项清单
	public static final String RPT_CS_CHANNEL_OVER_GOALS="RPT_CS_CHANNEL_OVER_GOALS";//渠道服务总体目标
	public static final String RPT_CS_CHANNEL_SAT_PREFER="RPT_CS_CHANNEL_SAT_PREFER";//偏好-满意度
	public static final String RPT_CS_CHANNEL_COMP_PREFER="RPT_CS_CHANNEL_COMP_PREFER";//偏好-抱怨率
	
	public static final String RPT_CS_COMPLAIN_MAIN1="RPT_CS_COMPLAIN_MAIN1";//细分市场
	public static final String RPT_CS_COMPLAIN_MAIN2="RPT_CS_COMPLAIN_MAIN2";//细分市场
	public static final String RPT_CS_COMPLAIN_MAIN3="RPT_CS_COMPLAIN_MAIN3";//细分市场
	public static final String RPT_CS_COMPLAIN_MAIN4="RPT_CS_COMPLAIN_MAIN4";//细分市场
	public static final String RPT_CS_COMPLAIN_MAIN5="RPT_CS_COMPLAIN_MAIN5";//细分市场
	public static final String RPT_CS_COMPLAIN_MAIN6="RPT_CS_COMPLAIN_MAIN6";//细分市场
	public static final String RPT_CS_COMPLAIN_MAIN7="RPT_CS_COMPLAIN_MAIN7";//细分市场
	public static final String RPT_CS_COMPLAIN_MAIN8="RPT_CS_COMPLAIN_MAIN8";//细分市场
	
	
	public static final String RPT_CS_CHANNEL_SER_NEW_FIRST ="RPT_CS_CHANNEL_SER_NEW_FIRST";//新渠道服务一级报表
	public static final String RPT_CS_CHANNEL_SER_FIRST_NEW ="RPT_CS_CHANNEL_SER_FIRST_NEW";//新渠道服务一级报表(新无图)
	public static final String RPT_CS_CHANNEL_SER_FIRST_SUM ="RPT_CS_CHANNEL_SER_FIRST_SUM";//新渠道服务一级报表(图表)
	public static final String RPT_CS_CHANNEL_SER_SECOND_SUM ="RPT_CS_CHANNEL_SER_SECOND_SUM";//新渠道服务二级报表(图表)
	public static final String RPT_CS_CHANNEL_SER_THRID_SUM ="RPT_CS_CHANNEL_SER_THRID_SUM";//新渠道服务二级报表(图表)
	public static final String RPT_CS_CHANNEL_SER_NEW_SECOND ="RPT_CS_CHANNEL_SER_NEW_SECOND";//新渠道服务二级报表
	public static final String RPT_CS_CHANNEL_SER_SECOND_NEW ="RPT_CS_CHANNEL_SER_SECOND_NEW";//新渠道服务二级报表(新无图)

	public static final String RPT_CS_CHANNEL_SER_NEW_THRID ="RPT_CS_CHANNEL_SER_NEW_THRID";//新渠道服务三级报表
	public static final String RPT_CS_CHANNEL_SER_THRID_NEW ="RPT_CS_CHANNEL_SER_THRID_NEW";//新渠道服务三级报表(新无图)
	
	
	public static final String PRT_CS_GROUP_CHANNEL_VIEW   ="PRT_CS_GROUP_CHANNEL_VIEW";//集团模板报表
	
	public static final String PRT_CS_GROUP_REPORT_MONTH   ="PRT_CS_GROUP_REPORT_MONTH";//集团上报监测月报

	public static final String RPT_CHANNEL_NEW_INDEX_DETAIL ="RPT_CHANNEL_NEW_INDEX_DETAIL";//渠道服务新 清单
	public static final String RPT_CHANNEL_NEW_INDEX_DETAIL2 ="RPT_CHANNEL_NEW_INDEX_DETAIL2";//渠道服务新 清单2
	
	public static final String RPT_CHANNEL_PREFER_10000 ="RPT_CHANNEL_PREFER_10000";//10000人工与新媒体&自助渠道的偏好系数
	public static final String RPT_CHANNEL_PREFER_ZYT ="RPT_CHANNEL_PREFER_ZYT";//自营厅与新媒体&自助渠道的偏好系数
	public static final String RPT_CHANNEL_PREFER_DT ="RPT_CHANNEL_PREFER_DT";//短厅与新媒体&自助渠道的偏好系数
	public static final String RPT_CHANNEL_PREFER_SUMMARY ="RPT_CHANNEL_PREFER_SUMMARY";//接触渠道数
	public static final String RPT_CHANNEL_PREFER_CHOOSE ="RPT_CHANNEL_PREFER_CHOOSE";//偏好选择
	
	public static final String RPT_CS_CHANNEL_ONE_PRO_NEW ="RPT_CS_CHANNEL_ONE_PRO_NEW";//配置按区域统计渠道服务一级报表
	public static final String RPT_CS_CHANNEL_SECOND_PRO_NEW ="RPT_CS_CHANNEL_SECOND_PRO_NEW";//配置按区域统计渠道服务一级报表
	//public static final String RPT_CS_CHANNEL_THRID_PRO ="RPT_CS_CHANNEL_THRID_PRO";//配置按区域统计渠道服务二级级报表
	public static final String RPT_CS_CHANNEL_THRID_PRO_NEW ="RPT_CS_CHANNEL_THRID_PRO_NEW";//配置按区域统计渠道服务三级报表
	public static final String RPT_VISIT_SATISFY_WX_DETALL ="RPT_VISIT_SATISFY_WX_DETALL";//微信即时回访满意度清单
	public static final String RPT_LOCAL_CMPLREASON_DAY ="RPT_LOCAL_CMPLREASON_DAY";//微信即时回访满意度清单

	public static final String RPT_VISIT_EWAM_FAULTDATA ="RPT_VISIT_EWAM_FAULTDATA";//满意度预警监控异常数据报表
	public static final String RPT_VISIT_EWAM_DETALL ="RPT_VISIT_EWAM_DETALL";//满意度预警监控异常数据清单

}