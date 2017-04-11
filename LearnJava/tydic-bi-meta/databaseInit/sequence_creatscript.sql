--维表公用序列
create sequence SEQ_DIM_DATA_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 维度类型  META_DIM_TYPE  (DIM_TYPE_ID）
create sequence SEQ_DIM_TYPE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 数据源  META_DATA_SOURCE （DATA_SOURCE_ID）
create sequence SEQ_DATA_SOURCE_ID minvalue 1 maxvalue 99999999999999999999999 start with 2  increment by 1 cache 20 ;
-- 维度表类  META_DIM_TABLES  （DIM_TABLE_ID）
create sequence SEQ_DIM_TABLES_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_STEP_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_STEP_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_STEP_GROUP_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_STEP_CHECK_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_STEP_CHECK_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_STEP_CHECK_EXEC_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_SRC_REL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_COL_REL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_COL_CALC_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_TAB_COL_CALC_GROUP_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_PROINST_GROUP_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_PROGRAM_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_PRO_INST_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_HOST_RES_PREF_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_HOST_PRO_RES_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_HOST_PRO_RES_CTR_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_ATTEMPER_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_ATTEMPER_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_ETL_ATT_MONITOR_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_GDL_USER_REL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_GDL_TAB_REL_TERM_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_GDL_TAB_REL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_GDL_GROUP_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_GDL_GROUP_REL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_GUILD_LINE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--登陆日志   META_MAG_LOGIN_LOG    ( LOG_ID ）
create sequence SEQ_LOGIN_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--维度修改记录表  META_DIM_TAB_MOD_HIS  （HIS_ID）
create sequence SEQ_DIM_TAB_MOD_HIS_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_MAG_PUSH_USER_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--用戶修改状态记录 META_MAG_USER_CHANGE_LOG   （LOG_ID）
create sequence SEQ_MAG_USER_CHANGE_ID minvalue 1 maxvalue 99999999999999999999999 start with 10000  increment by 1 cache 20 ;
create sequence SEQ_MAG_PUSH_CONFIG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_MAG_PUSH_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--用户关联界面风格 META_MAG_USER_SKIN
create sequence SEQ_MAG_USER_SKIN_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--岗位表  META_MAG_USER_STATION  (STATION_ID)
create sequence SEQ_MAG_USER_STATION_ID minvalue 1 maxvalue 99999999999999999999999 start with 100  increment by 1 cache 20 ;
create sequence SEQ_MAG_USER_SEARCH_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--用户表   META_MAG_USER  （USER_ID）
create sequence SEQ_MAG_USER_ID minvalue 1 maxvalue 99999999999999999999999 start with 2  increment by 1 cache 20 ;
--皮肤管理表  META_MAG_SKIN  (SKIN_ID)
create sequence SEQ_MAG_SKIN_ID minvalue 1 maxvalue 99999999999999999999999 start with 10  increment by 1 cache 20 ;
-- 部门表   META_MAG_USER_DEPT (DEPT_ID)
create sequence SEQ_MAG_USER_DEPT_ID minvalue 1 maxvalue 99999999999999999999999 start with 20  increment by 1 cache 20 ;
-- 角色表   META_MAG_ROLE  (ROLE_ID)
create sequence SEQ_MAG_ROLE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1000  increment by 1 cache 20 ;
create sequence SEQ_MAG_ROLE_DIM_REL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 操作日志表 META_MAG_OPERATE_LOG
create sequence SEQ_MAG_OPER_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--  菜单访问日志表  META_MAG_MENU_VISIT_LOG (VISIT_ID)
create sequence SEQ_MAG_MENU_VISIT_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--  菜单ID META_MAG_MENU(MENU_ID)
create sequence SEQ_MAG_MENU_ID minvalue 1 maxvalue 99999999999999999999999 start with 300000  increment by 1 cache 20 ;
--   搜藏菜单  META_MAG_MENU_USER_FAVORITE   (MENU_FAVORITE_ID）
create sequence SEQ_MAG_MENU_FAVORITE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--   新增系统   META_MENU_GROUP  (GROUP_ID  ）
create sequence SEQ_MAG_GROUP_ID minvalue 1 maxvalue 99999999999999999999999 start with 50  increment by 1 cache 20 ;
--收藏夹序列  META_MAG_FAVORITE_DIR  （FAVORITE_ID）
create sequence SEQ_MAG_FAVORITE_DIR_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--收藏夹序列  meta_mag_notice  （notice_id）
create sequence SEQ_MAG_NOTICE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;

--发布数据日志表 META_RPT_MODEL_ISSUE_LOG （LOG_ID）
create sequence SEQ_RRPORT_MANAGE_ISSUE_PUB_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 搜藏报表   META_RPT_USER_FAVORITE (REPORT_FAVORITE_ID）
create sequence SEQ_RPT_USER_FAVORITE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_TYPE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 搜藏报表日志   META_RPT_USE_LOG(USE_LOG_ID)
create sequence SEQ_RPT_USE_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_TAB_OUTPUT_CFG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_TAB_FILTER_CFG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_PUSH_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_PUSH_CONFIG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--  审核记录  META_RPT_DATA_AUDIT_LOG (AUDIT_LOG_ID）
create sequence SEQ_RPT_DATA_AUDIT_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_DATA_AUDIT_CFG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_CONFIG_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_RPT_REPORT_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_SYS_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_SYS_CODE_TYPE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
create sequence SEQ_SYS_I18N_ITEM_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 新增系统编码  META_SYS_CODE   (CODE_ID）
create sequence SEQ_SYS_CODE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1000  increment by 1 cache 20 ;
--  编码树信息 meta_sys_code_type_dir   (dir_id）
create sequence SEQ_SYS_CODE_TYPE_DIR_ID minvalue 1 maxvalue 99999999999999999999999 start with 100  increment by 1 cache 20 ;
--预警信息  META_TABLE_DIFF   （DIFF_ID）
create sequence SEQ_TABLE_DIFF_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
--表类审核数据 META_MAG_USER_TAB_REL  （REL_ID）
create sequence SEQ_TAB_REL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 实体表映射关系  META_TABLE_INST   （TABLE_INST_ID）
create sequence SEQ_TAB_INST_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 表类管理 META_TABLES    （TABLE_ID）
create sequence SEQ_TAB_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 表类批量新增记录数据 META_TABLE_INST_DATA(TABLE_INST_DATA_ID）
create sequence SEQ_TAB_INST_DATA_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- META_TABLE_REL      ( TABLE_REL_ID ）
create sequence SEQ_TAB_COL_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 表类列公用ID
create sequence SEQ_TAB_GROUP_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- 公告序列  META_MAG_NOTICE  (NOTICE_ID)
create sequence SEQ_MAG_NOTICE_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;
-- meta_rpt_config_log （log_id）
create sequence SEQ_RPT_CONFIG_LOG_ID minvalue 1 maxvalue 99999999999999999999999 start with 1  increment by 1 cache 20 ;





