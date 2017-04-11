--------------------------------------------------------
-- Export file for user META                          --
-- Created by Administrator on 2012-3-20, 下午 06:15:50 --
--------------------------------------------------------

--spool alltable1.log

prompt
prompt Creating table META_DATA_SOURCE
prompt ===============================
prompt
create table META_DATA_SOURCE
(
  DATA_SOURCE_ID        NUMBER(9) not null,
  DATA_SOURCE_NAME      VARCHAR2(64),
  DATA_SOURCE_ORANAME   VARCHAR2(100),
  DATA_SOURCE_USER      VARCHAR2(20),
  DATA_SOURCE_PASS      VARCHAR2(20),
  DATA_SOURCE_TYPE      VARCHAR2(256),
  DATA_SOURCE_RULE      VARCHAR2(256),
  DATA_SOURCE_STATE     NUMBER(9),
  DATA_SOURCE_INTRO     VARCHAR2(512),
  SYS_ID                NUMBER(9),
  DATA_SOURCE_MIN_COUNT NUMBER(9)
)
;
comment on table META_DATA_SOURCE
  is '数据源';
comment on column META_DATA_SOURCE.DATA_SOURCE_ID
  is '序号';
comment on column META_DATA_SOURCE.DATA_SOURCE_NAME
  is '数据源名称';
comment on column META_DATA_SOURCE.DATA_SOURCE_ORANAME
  is '数据源oracle名称或者FTP主机的IP地址';
comment on column META_DATA_SOURCE.DATA_SOURCE_USER
  is 'oracle用户名称或者FTP用户名';
comment on column META_DATA_SOURCE.DATA_SOURCE_PASS
  is '登录密码';
comment on column META_DATA_SOURCE.DATA_SOURCE_TYPE
  is '数据源类型  TABLE、TXT、DMP、ZIP、FTP、LOCAL  支持FTP|ZIP|TXT   LOCAL|ZIP|TXT组合';
comment on column META_DATA_SOURCE.DATA_SOURCE_RULE
  is '数据源规则 如：文件时的分隔规则等 TXT:字段分隔符+*+*+行分隔符; DMP:,如果是表，则为JDBC连接串';
comment on column META_DATA_SOURCE.DATA_SOURCE_STATE
  is '状态';
comment on column META_DATA_SOURCE.DATA_SOURCE_INTRO
  is '数据源用途说明';
comment on column META_DATA_SOURCE.SYS_ID
  is '系统ID';
comment on column META_DATA_SOURCE.DATA_SOURCE_MIN_COUNT
  is '数据源最小连接数';
alter table META_DATA_SOURCE
  add primary key (DATA_SOURCE_ID);

prompt
prompt Creating table META_DIM_LEVEL
prompt =============================
prompt
create table META_DIM_LEVEL
(
  DIM_LEVEL      NUMBER(9),
  DIM_LEVEL_NAME VARCHAR2(18),
  DIM_TYPE_ID    NUMBER(9) not null,
  DIM_TABLE_ID   NUMBER(18) not null
)
;
comment on table META_DIM_LEVEL
  is '分类:天源迪科-元数据-维度.';
comment on column META_DIM_LEVEL.DIM_LEVEL
  is '维度层级';
comment on column META_DIM_LEVEL.DIM_LEVEL_NAME
  is '维度层级名称';
comment on column META_DIM_LEVEL.DIM_TYPE_ID
  is '类型ID';
comment on column META_DIM_LEVEL.DIM_TABLE_ID
  is '表类ID';

prompt
prompt Creating table META_DIM_MAPP
prompt ============================
prompt
create table META_DIM_MAPP
(
  ITEM_CODE    VARCHAR2(32) not null,
  SRC_CODE     VARCHAR2(20),
  SYS_ID       NUMBER(9) not null,
  ITEM_NAME    VARCHAR2(32),
  SRC_NAME     VARCHAR2(64),
  DIM_TABLE_ID NUMBER(18) not null
)
;
comment on table META_DIM_MAPP
  is '分类:天源迪科-元数据-维度.';
comment on column META_DIM_MAPP.SYS_ID
  is '源系统编码';
comment on column META_DIM_MAPP.SRC_NAME
  is '源系统编码名称';
comment on column META_DIM_MAPP.DIM_TABLE_ID
  is '表类ID';

prompt
prompt Creating table META_DIM_TABLES
prompt ==============================
prompt
create table META_DIM_TABLES
(
  TABLE_NAME         VARCHAR2(64),
  DATA_SOURCE_ID     NUMBER,
  TABLE_DIM_LEVEL    NUMBER(18) default 0,
  TABLE_DIM_PREFIX   VARCHAR2(12),
  DIM_KEY_COL_ID     NUMBER(18),
  DIM_PAR_KEY_COL_ID NUMBER(18),
  DIM_TABLE_ID       NUMBER(18) not null,
  LAST_LEVEL_FLAG    NUMBER(9)
)
;
comment on table META_DIM_TABLES
  is '分类:天源迪科-元数据-维度.';
comment on column META_DIM_TABLES.TABLE_NAME
  is '表类名，带宏变量标示相同结构的一组表';
comment on column META_DIM_TABLES.DATA_SOURCE_ID
  is '数据源ID';
comment on column META_DIM_TABLES.TABLE_DIM_LEVEL
  is 'TABLE_TYPE_ID=2
维度表层级，层级从一开始编码，数值越小级别越大，比如: 省级层级为1，地市层级为2，营业区为3';
comment on column META_DIM_TABLES.TABLE_DIM_PREFIX
  is '维度前缀
地址：ZONE
产品：PRD';
comment on column META_DIM_TABLES.DIM_KEY_COL_ID
  is '维度表主键列ID';
comment on column META_DIM_TABLES.DIM_TABLE_ID
  is '表类ID';
comment on column META_DIM_TABLES.LAST_LEVEL_FLAG
  is '末级展现映射标识 0： 不显示最后一级编码，映射时使用倒数第二级做映射，添加映射时自动添加末级编码，并与源做映射，修改时直接修改末级与上给的关系视为编码映射 1：需要显示和映射最后一级编码，最后一级需要纳入手工管理维护中';
alter table META_DIM_TABLES
  add primary key (DIM_TABLE_ID);

prompt
prompt Creating table META_DIM_TAB_INT_REL
prompt ===================================
prompt
create table META_DIM_TAB_INT_REL
(
  SYS_ID         NUMBER(9) not null,
  DATA_SOURCE_ID NUMBER(9),
  INT_TAB_ID     NUMBER(18),
  INT_TAB_NAME   VARCHAR2(100),
  SRC_TAB_NAME   VARCHAR2(100),
  DATA_MAPP_SQL  VARCHAR2(4000),
  DATA_MAPP_MARK VARCHAR2(1000),
  USER_ID        NUMBER(18),
  DIM_TABLE_ID   NUMBER(18) not null
)
;
comment on column META_DIM_TAB_INT_REL.SYS_ID
  is '系统ID';
comment on column META_DIM_TAB_INT_REL.DATA_SOURCE_ID
  is '序号';
comment on column META_DIM_TAB_INT_REL.INT_TAB_ID
  is '接口表表类ID，如果未管理接口表则为0';
comment on column META_DIM_TAB_INT_REL.INT_TAB_NAME
  is '接口表名称';
comment on column META_DIM_TAB_INT_REL.SRC_TAB_NAME
  is '源系统表的编码表表名称';
comment on column META_DIM_TAB_INT_REL.DATA_MAPP_SQL
  is '数据映射转换SQL';
comment on column META_DIM_TAB_INT_REL.DATA_MAPP_MARK
  is '数据转换映射备注';
comment on column META_DIM_TAB_INT_REL.USER_ID
  is '用户ID';
comment on column META_DIM_TAB_INT_REL.DIM_TABLE_ID
  is '表类ID';

prompt
prompt Creating table META_DIM_TAB_MOD_HIS
prompt ===================================
prompt
create table META_DIM_TAB_MOD_HIS
(
  ITEM_ID       NUMBER(18) not null,
  ITEM_NAME     VARCHAR2(200),
  ITEM_PAR_ID   NUMBER(18),
  ITEM_CODE     VARCHAR2(100),
  DIM_TYPE_ID   NUMBER(9) not null,
  DIM_LEVEL     CHAR(18),
  MOD_FLAG      VARCHAR2(64),
  MOD_MARK      VARCHAR2(1000),
  MOD_DATE      DATE,
  USER_ID       NUMBER(18),
  DIM_TABLE_ID  NUMBER(18) not null,
  SRC_CODE      VARCHAR2(20),
  SRC_NAME      VARCHAR2(64),
  COL1          VARCHAR2(64),
  COL2          VARCHAR2(64),
  COL3          VARCHAR2(64),
  COL4          VARCHAR2(64),
  ITEM_DESC     VARCHAR2(256),
  ORDER_ID      NUMBER(9),
  STATE         NUMBER(9),
  AUDIT_FLAG    NUMBER(9),
  AUDIT_USER_ID NUMBER(9),
  SRC_SYS_ID    NUMBER(9),
  HIS_ID        NUMBER(9),
  BATCH_ID      VARCHAR2(20),
  COL5          VARCHAR2(64)
)
;
comment on table META_DIM_TAB_MOD_HIS
  is '地域编码表修改历史表';
comment on column META_DIM_TAB_MOD_HIS.ITEM_ID
  is '地域编码ID';
comment on column META_DIM_TAB_MOD_HIS.DIM_TYPE_ID
  is '维度分组类型';
comment on column META_DIM_TAB_MOD_HIS.DIM_LEVEL
  is '层级1';
comment on column META_DIM_TAB_MOD_HIS.MOD_FLAG
  is '修改标识  1新增 2修改 3审核

修改时把修改的新记录值插入此表，
审核通过后把老记录插入此表做备份
';
comment on column META_DIM_TAB_MOD_HIS.MOD_MARK
  is '修改备注';
comment on column META_DIM_TAB_MOD_HIS.MOD_DATE
  is '修改时间';
comment on column META_DIM_TAB_MOD_HIS.USER_ID
  is '修改用户ID';
comment on column META_DIM_TAB_MOD_HIS.DIM_TABLE_ID
  is '表类ID';
comment on column META_DIM_TAB_MOD_HIS.SRC_CODE
  is '源系统编码';
comment on column META_DIM_TAB_MOD_HIS.SRC_NAME
  is '源系统编码名称';
comment on column META_DIM_TAB_MOD_HIS.COL1
  is '维度表动态属性字段';
comment on column META_DIM_TAB_MOD_HIS.COL2
  is '维度表动态属性字段';
comment on column META_DIM_TAB_MOD_HIS.COL3
  is '维度表动态属性字段';
comment on column META_DIM_TAB_MOD_HIS.COL4
  is '维度表动态属性字段';
comment on column META_DIM_TAB_MOD_HIS.ITEM_DESC
  is '描述';
comment on column META_DIM_TAB_MOD_HIS.STATE
  is '有效状态 0 无效 1有效';
comment on column META_DIM_TAB_MOD_HIS.AUDIT_FLAG
  is '是否已经审核';
comment on column META_DIM_TAB_MOD_HIS.AUDIT_USER_ID
  is '审核用户ID';

prompt
prompt Creating table META_DIM_TYPE
prompt ============================
prompt
create table META_DIM_TYPE
(
  DIM_TYPE_ID    NUMBER(9) not null,
  DIM_TYPE_NAME  VARCHAR2(32),
  DIM_TYPE_DESC  VARCHAR2(512),
  DIM_TYPE_CODE  VARCHAR2(12),
  DIM_TABLE_ID   NUMBER(18) not null,
  DIM_TYPE_STATE NUMBER(9)
)
;
comment on table META_DIM_TYPE
  is '维度编码类型';
comment on column META_DIM_TYPE.DIM_TYPE_ID
  is '类型ID';
comment on column META_DIM_TYPE.DIM_TYPE_NAME
  is '类型名称';
comment on column META_DIM_TYPE.DIM_TYPE_DESC
  is '类型描述';
comment on column META_DIM_TYPE.DIM_TABLE_ID
  is '表类ID';
comment on column META_DIM_TYPE.DIM_TYPE_STATE
  is '状态';

prompt
prompt Creating table META_DIM_ZONE
prompt ============================
prompt
create table META_DIM_ZONE
(
  ZONE_ID       NUMBER(20) not null,
  ZONE_PAR_ID   NUMBER(20) not null,
  ZONE_CODE     VARCHAR2(100) not null,
  ZONE_NAME     VARCHAR2(100) not null,
  ZONE_DESC     VARCHAR2(1000),
  DIM_TYPE_ID   NUMBER(20) not null,
  STATE         NUMBER(20) default 1,
  DIM_LEVEL     NUMBER(20) default 0,
  MOD_FLAG      NUMBER(20) default 0,
  ORDER_ID      NUMBER(20) default 0,
  ODER_H        NUMBER(38,7),
  TEST_ADD_COL3 DATE,
  TEST_ADD_COL4 CHAR(10),
  TEST_ADD_COL1 NUMBER(38,7),
  TEST_ADD_COL2 VARCHAR2(1000),
  TEST_ADD_COL5 NUMBER(38,7)
)
;
comment on column META_DIM_ZONE.ZONE_ID
  is 'null';
comment on column META_DIM_ZONE.ZONE_PAR_ID
  is 'null';
comment on column META_DIM_ZONE.ZONE_CODE
  is 'null';
comment on column META_DIM_ZONE.ZONE_NAME
  is 'null';
comment on column META_DIM_ZONE.ZONE_DESC
  is 'null';
comment on column META_DIM_ZONE.DIM_TYPE_ID
  is 'null';
comment on column META_DIM_ZONE.STATE
  is 'null';
comment on column META_DIM_ZONE.DIM_LEVEL
  is 'null';
comment on column META_DIM_ZONE.MOD_FLAG
  is 'null';
comment on column META_DIM_ZONE.ORDER_ID
  is 'null';
comment on column META_DIM_ZONE.ODER_H
  is 'null';
comment on column META_DIM_ZONE.TEST_ADD_COL3
  is 'null';
comment on column META_DIM_ZONE.TEST_ADD_COL4
  is 'null';
comment on column META_DIM_ZONE.TEST_ADD_COL1
  is 'null';
comment on column META_DIM_ZONE.TEST_ADD_COL2
  is 'null';
comment on column META_DIM_ZONE.TEST_ADD_COL5
  is 'null';
alter table META_DIM_ZONE
  add primary key (ZONE_ID);

prompt
prompt Creating table META_GDL_GROUP
prompt =============================
prompt
create table META_GDL_GROUP
(
  GDL_GROUP_ID NUMBER(18) not null,
  PAR_GROUP_ID NUMBER(18) default 0,
  GROUP_NAME   VARCHAR2(64),
  ORDER_ID     NUMBER(9)
)
;
comment on table META_GDL_GROUP
  is '指标目录表';
comment on column META_GDL_GROUP.GDL_GROUP_ID
  is '指标分组ID';
comment on column META_GDL_GROUP.PAR_GROUP_ID
  is '父级目录分组ID';
comment on column META_GDL_GROUP.GROUP_NAME
  is '分组名称';
comment on column META_GDL_GROUP.ORDER_ID
  is '显示排序ID';
alter table META_GDL_GROUP
  add primary key (GDL_GROUP_ID);

prompt
prompt Creating table META_GUILD_LINE
prompt ==============================
prompt
create table META_GUILD_LINE
(
  GDL_ID            NUMBER(18) not null,
  PAR_GDL_ID        NUMBER(18) default 0,
  GDL_CODE          VARCHAR2(12),
  GDL_GROUP_ID      NUMBER(18),
  GDL_COL_NAME      VARCHAR2(32),
  GDL_NAME          VARCHAR2(64),
  GDL_SRC_TABLE     VARCHAR2(64),
  GDL_SRC_TABLE_ID  NUMBER(18),
  GDL_TYPE          NUMBER(9),
  GDL_SRC_COL       VARCHAR2(64),
  GDL_SRC_COL_ID    NUMBER(18),
  SRC_CALC_GROUP_ID NUMBER(18),
  GDL_BUS_DESC      VARCHAR2(1000),
  GDL_CALC_EXPR     VARCHAR2(256),
  GDL_UNIT          VARCHAR2(12),
  USER_ID           NUMBER(18)
)
;
comment on table META_GUILD_LINE
  is '分类:天源迪科-元数据-组织权限.';
comment on column META_GUILD_LINE.GDL_ID
  is '指标ID';
comment on column META_GUILD_LINE.PAR_GDL_ID
  is '父级指标ID';
comment on column META_GUILD_LINE.GDL_GROUP_ID
  is '指标分组ID';
comment on column META_GUILD_LINE.GDL_COL_NAME
  is '指标字段名称，统一名称，保证在各个表中同一指标名称相同';
comment on column META_GUILD_LINE.GDL_NAME
  is '指标名称';
comment on column META_GUILD_LINE.GDL_SRC_TABLE
  is '数据来源表名称,基础表, 此表数据不由指标配置生成';
comment on column META_GUILD_LINE.GDL_SRC_TABLE_ID
  is '数据来源表ID';
comment on column META_GUILD_LINE.GDL_TYPE
  is '代码:0基本指标、1复合指标、2计算指标.
指标类型
0:基本指标
1:复合指标
2:计算指标
';
comment on column META_GUILD_LINE.GDL_SRC_COL
  is '来源字段';
comment on column META_GUILD_LINE.GDL_SRC_COL_ID
  is '源列ID';
comment on column META_GUILD_LINE.SRC_CALC_GROUP_ID
  is '计算规则分组ID';
comment on column META_GUILD_LINE.GDL_BUS_DESC
  is '指标业务解释';
comment on column META_GUILD_LINE.GDL_CALC_EXPR
  is '计算指标表达式
指标ID计算：{1}/{2} 等
源表字段计算：{COL1}/{COL2}
';
comment on column META_GUILD_LINE.GDL_UNIT
  is '指标单位';
comment on column META_GUILD_LINE.USER_ID
  is '创建人';
alter table META_GUILD_LINE
  add primary key (GDL_ID);

prompt
prompt Creating table META_MAG_COMMENT
prompt ===============================
prompt
create table META_MAG_COMMENT
(
  COMMENT_ID      NUMBER(18) not null,
  PARENT_ID       NUMBER(18),
  SYS_ID          NUMBER(18),
  MENU_ID         NUMBER(18),
  COMMENT_DEPT    NUMBER(18),
  COMMENT_USER    NUMBER(18),
  COMMENT_TIME    DATE,
  COMMENT_CONTENT VARCHAR2(4000)
)
;
alter table META_MAG_COMMENT
  add primary key (COMMENT_ID);

prompt
prompt Creating table META_MAG_LOGIN_LOG
prompt =================================
prompt
create table META_MAG_LOGIN_LOG
(
  LOG_ID      NUMBER(18) not null,
  USER_ID     NUMBER(18),
  LOGIN_IP    VARCHAR2(20),
  LOGIN_MAC   VARCHAR2(32),
  LOGIN_DATE  DATE,
  LOGOFF_DATE DATE,
  GROUP_ID    NUMBER(18)
)
;
comment on table META_MAG_LOGIN_LOG
  is '登录日志';
comment on column META_MAG_LOGIN_LOG.USER_ID
  is '用户ID';
comment on column META_MAG_LOGIN_LOG.LOGIN_IP
  is '登录IP地址';
comment on column META_MAG_LOGIN_LOG.LOGIN_MAC
  is '登录计算机MAC地址';
comment on column META_MAG_LOGIN_LOG.LOGIN_DATE
  is '登录时间';
comment on column META_MAG_LOGIN_LOG.LOGOFF_DATE
  is '登出时间';

prompt
prompt Creating table META_MAG_MENU
prompt ============================
prompt
create table META_MAG_MENU
(
  MENU_ID        NUMBER(18) not null,
  PARENT_ID      NUMBER(18) default 0,
  MENU_NAME      VARCHAR2(64),
  MENU_TIP       VARCHAR2(100),
  MENU_URL       VARCHAR2(512),
  PAGE_BUTTON    VARCHAR2(256),
  GROUP_ID       NUMBER(9),
  ORDER_ID       NUMBER(9),
  IS_SHOW        NUMBER(9),
  CREATE_DATE    DATE default sysdate,
  ICON_URL       CHAR(100),
  TARGET         VARCHAR2(32),
  USER_ATTR      NUMBER(9),
  NAV_STATE      NUMBER(9),
  USER_ATTR_LIST VARCHAR2(100),
  MENU_STATE     NUMBER(9)
)
;
comment on table META_MAG_MENU
  is '管理菜单';
comment on column META_MAG_MENU.PARENT_ID
  is '父级菜单';
comment on column META_MAG_MENU.MENU_NAME
  is '菜单名称';
comment on column META_MAG_MENU.MENU_TIP
  is '菜单注释';
comment on column META_MAG_MENU.MENU_URL
  is '菜单地址';
comment on column META_MAG_MENU.PAGE_BUTTON
  is '菜单按钮列表';
comment on column META_MAG_MENU.ORDER_ID
  is '排序ID';
comment on column META_MAG_MENU.IS_SHOW
  is '代码:0不显示、1显示.
是否显示';
comment on column META_MAG_MENU.ICON_URL
  is '菜单图标地址';
comment on column META_MAG_MENU.TARGET
  is '目标窗口名称
默认选择项 top right blank';
comment on column META_MAG_MENU.USER_ATTR
  is '代码:0不传送、1传送.
是否传送用户属性';
comment on column META_MAG_MENU.NAV_STATE
  is '代码:1 是否最大化、2是否有滚动、4是否有菜单栏、8状态栏、16链接栏.
浏览器状态 1 是否最大化  2是否有滚动 4是否有菜单栏 8状态栏 16链接栏';
comment on column META_MAG_MENU.USER_ATTR_LIST
  is '数据属性列表 逗号分隔
userName={user_namecn},
userEmail={user_email},
staff={user_usernamecn}';
comment on column META_MAG_MENU.MENU_STATE
  is '代码:0停用、1启用';
alter table META_MAG_MENU
  add primary key (MENU_ID);

prompt
prompt Creating table META_MAG_MENU_VISIT_LOG
prompt ======================================
prompt
create table META_MAG_MENU_VISIT_LOG
(
  MENU_ID    NUMBER(18) not null,
  USER_ID    NUMBER(18) not null,
  VISIT_TIME DATE not null,
  YEAR_NO    NUMBER(9),
  LOG_ID     NUMBER(18) not null,
  VISIT_ID   NUMBER(18) not null
)
;
comment on table META_MAG_MENU_VISIT_LOG
  is '菜单访问日志';
comment on column META_MAG_MENU_VISIT_LOG.USER_ID
  is '用户ID';
comment on column META_MAG_MENU_VISIT_LOG.VISIT_TIME
  is '访问时间';
comment on column META_MAG_MENU_VISIT_LOG.YEAR_NO
  is '年份，用于分区';
comment on column META_MAG_MENU_VISIT_LOG.VISIT_ID
  is '菜单访问日志ID ';

prompt
prompt Creating table META_MAG_OPERATE_LOG
prompt ===================================
prompt
create table META_MAG_OPERATE_LOG
(
  LOG_ID      NUMBER(18) not null,
  USER_ID     NUMBER(18),
  OPER_DATE   DATE,
  OPER_MSG    VARCHAR2(4000),
  OPER_TYPE   NUMBER(9),
  OPER_DETAIL BLOB,
  MENU_ID     NUMBER(18),
  OPER_TABLE  VARCHAR2(64)
)
;
comment on table META_MAG_OPERATE_LOG
  is '管理用户操作日志表';
comment on column META_MAG_OPERATE_LOG.USER_ID
  is '用户ID';
comment on column META_MAG_OPERATE_LOG.OPER_DATE
  is '操作时间';
comment on column META_MAG_OPERATE_LOG.OPER_MSG
  is '操作信息，更新记录的SQL等等';
comment on column META_MAG_OPERATE_LOG.OPER_TYPE
  is '操作信息，更新记录的SQL等等';
comment on column META_MAG_OPERATE_LOG.OPER_DETAIL
  is '操作信息，更新记录的SQL等等';
comment on column META_MAG_OPERATE_LOG.MENU_ID
  is '操作菜单';
comment on column META_MAG_OPERATE_LOG.OPER_TABLE
  is '操作的元数据管理表';

prompt
prompt Creating table META_MAG_PHSH_LOG
prompt ================================
prompt
create table META_MAG_PHSH_LOG
(
  PUSH_LOG_ID  NUMBER(18) not null,
  RECEIVE_DEPT NUMBER(18),
  RECEIVE_USER NUMBER(18),
  SEND_TIME    DATE,
  FACT_CONTENT VARCHAR2(2000),
  DATA_ANNEX   VARCHAR2(100),
  ORDER_ID     NUMBER(18),
  SEND_FLAG    NUMBER(9),
  DATA_ID      NUMBER(18)
)
;
alter table META_MAG_PHSH_LOG
  add primary key (PUSH_LOG_ID);

prompt
prompt Creating table META_MAG_PUSH
prompt ============================
prompt
create table META_MAG_PUSH
(
  PUSH_ID      CHAR(18) not null,
  SYS_ID       CHAR(18),
  MENU_ID      CHAR(18),
  MODULE_ID    CHAR(18),
  PUSH_NOTE    VARCHAR2(1000),
  CREATE_DEPT  NUMBER(18),
  CREATE_USER  NUMBER(18),
  CREATE_TIME  DATE,
  PUSH_NAME    VARCHAR2(30),
  PUSH_STATE   NUMBER(18) default 0,
  PUSH_PATTERN VARCHAR2(1000)
)
;
alter table META_MAG_PUSH
  add primary key (PUSH_ID);

prompt
prompt Creating table META_MAG_PUSH_CONFIG
prompt ===================================
prompt
create table META_MAG_PUSH_CONFIG
(
  ORDER_ID            NUMBER(18) not null,
  PUSH_TYPE           NUMBER(9),
  PREV_SEND_TIME      DATE,
  SEND_SEQUNCE        VARCHAR2(20),
  SEND_SEQUENCE_VALUE DATE,
  PUSH_ID             CHAR(18),
  PUSH_CHANNEL        INTEGER,
  STATE               INTEGER default 1,
  PUSH_PATTERN        VARCHAR2(2000)
)
;
comment on column META_MAG_PUSH_CONFIG.STATE
  is '状态：1表示该记录为用户推送信息，2表示该记录为用户订阅信息';
alter table META_MAG_PUSH_CONFIG
  add primary key (ORDER_ID);

prompt
prompt Creating table META_MAG_PUSH_DATA
prompt =================================
prompt
create table META_MAG_PUSH_DATA
(
  DATA_ID      NUMBER(18) not null,
  DATA_CONTENT VARCHAR2(2000),
  DATA_ANNEX   VARCHAR2(100),
  PUSH_ID      CHAR(18)
)
;
alter table META_MAG_PUSH_DATA
  add primary key (DATA_ID);

prompt
prompt Creating table META_MAG_PUSH_USER
prompt =================================
prompt
create table META_MAG_PUSH_USER
(
  PUSH_USER_ID NUMBER(18) not null,
  RECEIVE_USER NUMBER(18),
  RECEIVE_DEPT NUMBER(18),
  ORDER_ID     NUMBER(18)
)
;
alter table META_MAG_PUSH_USER
  add primary key (PUSH_USER_ID);

prompt
prompt Creating table META_MAG_ROLE
prompt ============================
prompt
create table META_MAG_ROLE
(
  ROLE_ID     NUMBER(18) not null,
  ROLE_NAME   VARCHAR2(64),
  ROLE_DESC   VARCHAR2(256),
  ROLE_STATE  NUMBER(9),
  CREATE_DATE DATE default sysdate
)
;
comment on table META_MAG_ROLE
  is '管理角色表';
comment on column META_MAG_ROLE.ROLE_ID
  is '角色ID';
comment on column META_MAG_ROLE.ROLE_NAME
  is '角色名称';
comment on column META_MAG_ROLE.ROLE_DESC
  is '角色描述';
comment on column META_MAG_ROLE.ROLE_STATE
  is '代码:0无效、1有效.
角色状态';
alter table META_MAG_ROLE
  add primary key (ROLE_ID);

prompt
prompt Creating table META_MAG_ROLE_DIM
prompt ================================
prompt
create table META_MAG_ROLE_DIM
(
  ROLE_ID            NUMBER(18) not null,
  DIM_TABLE_ID       NUMBER(18) not null,
  TABLE_DIM_LEVEL    NUMBER(18) default 0,
  DIM_TYPE_ID        NUMBER(9) not null,
  TRANS_TYPE         NUMBER(9),
  ROLE_DIM_REL_ID    NUMBER(18) not null,
  USE_USER_ATTR_FLAG NUMBER(9)
)
;
comment on table META_MAG_ROLE_DIM
  is '角色维度关系表';
comment on column META_MAG_ROLE_DIM.ROLE_ID
  is '角色ID';
comment on column META_MAG_ROLE_DIM.DIM_TABLE_ID
  is '表类ID';
comment on column META_MAG_ROLE_DIM.TABLE_DIM_LEVEL
  is '有权限的维度表层级';
comment on column META_MAG_ROLE_DIM.DIM_TYPE_ID
  is '类型ID';
comment on column META_MAG_ROLE_DIM.TRANS_TYPE
  is '代码:0父级规则、1子级规则、2不传递.
传递类型
0：有父级权限则默认有子级权限
1：有子级权限则默认有父级权限
2：不传递';
comment on column META_MAG_ROLE_DIM.ROLE_DIM_REL_ID
  is '角色维度关联ID';
comment on column META_MAG_ROLE_DIM.USE_USER_ATTR_FLAG
  is '是否使用用户属性标识值，如地域，部门，岗位等在用户表中存在的维度表值';

prompt
prompt Creating table META_MAG_ROLE_DIM_DETAIL
prompt =======================================
prompt
create table META_MAG_ROLE_DIM_DETAIL
(
  DIM_TABLE_ID    NUMBER(18) not null,
  ROLE_DIM_REL_ID NUMBER(18) not null,
  DIM_TYPE_ID     NUMBER(9) not null,
  DIM_CODE        VARCHAR2(20),
  FLAG            NUMBER(9)
)
;
comment on table META_MAG_ROLE_DIM_DETAIL
  is '角色维度关系具体值';
comment on column META_MAG_ROLE_DIM_DETAIL.DIM_TABLE_ID
  is '表类ID';
comment on column META_MAG_ROLE_DIM_DETAIL.ROLE_DIM_REL_ID
  is '角色维度关联ID';
comment on column META_MAG_ROLE_DIM_DETAIL.DIM_TYPE_ID
  is '类型ID';
comment on column META_MAG_ROLE_DIM_DETAIL.DIM_CODE
  is '维度编码值';
comment on column META_MAG_ROLE_DIM_DETAIL.FLAG
  is '代码:0添加、1减少.赋权类型 0：添加，默认1：减少';

prompt
prompt Creating table META_MAG_ROLE_GDL
prompt ================================
prompt
create table META_MAG_ROLE_GDL
(
  ROLE_ID NUMBER(18) not null,
  GDL_ID  NUMBER(18) not null
)
;
comment on table META_MAG_ROLE_GDL
  is '角色指标关系';
comment on column META_MAG_ROLE_GDL.ROLE_ID
  is '角色ID';
comment on column META_MAG_ROLE_GDL.GDL_ID
  is '指标ID';

prompt
prompt Creating table META_MAG_ROLE_MENU
prompt =================================
prompt
create table META_MAG_ROLE_MENU
(
  ROLE_ID        NUMBER(18) not null,
  MENU_ID        NUMBER(18) not null,
  EXCLUDE_BUTTON VARCHAR2(100),
  MAP_TYPE       NUMBER(9)
)
;
comment on table META_MAG_ROLE_MENU
  is '角色菜单权限表';
comment on column META_MAG_ROLE_MENU.ROLE_ID
  is '角色ID';
comment on column META_MAG_ROLE_MENU.MENU_ID
  is '菜单ID';
comment on column META_MAG_ROLE_MENU.EXCLUDE_BUTTON
  is '没有权限的按钮';
comment on column META_MAG_ROLE_MENU.MAP_TYPE
  is '代码:1具有(加法),0不具有(减法)';
alter table META_MAG_ROLE_MENU
  add primary key (ROLE_ID, MENU_ID);

prompt
prompt Creating table META_MAG_ROLE_ORG
prompt ================================
prompt
create table META_MAG_ROLE_ORG
(
  ROLE_ID    NUMBER(18) not null,
  STATION_ID NUMBER(9) not null,
  DEPT_ID    NUMBER(9) not null
)
;
comment on table META_MAG_ROLE_ORG
  is '分类:天源迪科-元数据-组织权限.';
comment on column META_MAG_ROLE_ORG.ROLE_ID
  is '角色ID';
comment on column META_MAG_ROLE_ORG.STATION_ID
  is '岗位ID';
comment on column META_MAG_ROLE_ORG.DEPT_ID
  is '部门ID';

prompt
prompt Creating table META_MAG_SKIN
prompt ============================
prompt
create table META_MAG_SKIN
(
  SKIN_ID     NUMBER(18) not null,
  IMAGES_PATH VARCHAR2(100),
  CSS_PATH    VARCHAR2(100),
  SKIN_NAME   VARCHAR2(50),
  SKIN_STATE  NUMBER(9),
  SKIN_SN     NUMBER(9),
  GROUP_ID    NUMBER(9)
)
;

prompt
prompt Creating table META_MAG_SYS_CONFIG
prompt ==================================
prompt
create table META_MAG_SYS_CONFIG
(
  CONFIG_ID         NUMBER(18) not null,
  GROUP_ID          NUMBER(9),
  CONFIG_ITEM_TYPE  NUMBER(9),
  CONFIG_ITEM_STATE NUMBER(9),
  CONFIG_ITEM_NAME  VARCHAR2(32),
  CONFIG_ITEM_VALUE VARCHAR2(100),
  CONFIG_ITEM_NOTE  VARCHAR2(100)
)
;

prompt
prompt Creating table META_MAG_TIMER
prompt =============================
prompt
create table META_MAG_TIMER
(
  TIMER_ID    NUMBER(18) not null,
  TIMER_TYPE  NUMBER(9) not null,
  TIMER_RULE  VARCHAR2(100) not null,
  TIMER_STATE NUMBER(9) not null,
  TIMER_CLASS VARCHAR2(100) not null,
  TIMER_DESC  VARCHAR2(512)
)
;
comment on table META_MAG_TIMER
  is '系统定时任务表';
comment on column META_MAG_TIMER.TIMER_ID
  is '定时任务ID';
comment on column META_MAG_TIMER.TIMER_TYPE
  is '定时任务类型1、简单循环定时任务，此类型描述什么时候开始以一定的间隔时间进行定时任务执行。
  2、每天定时执行，此类型描述定时任务在每天的什么时候开始执行。3、每周定时执行，此类型描述定时任务在每周的星期几什么时间点执行。
4、每月定时任务，此类型描述定时任务在每月的那天什么时间点定时执行。
5、每年定时任务，此类型描述定时任务在每年的那月那天那个时间点定点执行。
6、固定时间定时任务，表示在那几个时间点定点执行一次。
7、自定义类型定时器，此类型用于自定义CRON表达式。';
comment on column META_MAG_TIMER.TIMER_RULE
  is '定时任务规则，对于不同的任务类型，其规则定义如下：
1、简单循环定时任务，其规则如下存储：
"执行次数,间隔时间,开始时间,结束时间"。开始时间与结束时间格式,间隔时间为毫秒为:YYYYMMDDHHMISS,如果不存在结束时间可以不写，如：
3,300,20120909111211,2012090913121
表示从20120909111211间隔300毫秒执行三次
或者
3,300,20120909111211
2、每天定时执行任务，其规则如下存储:
“每天任务执行时间”,时间格为:"HHMISS";
3、每周定时执行任务，其规则如下存储:
“星期数,执行时间”,时间格为:"HHMISS",如
3,152121表示每周三，15点21分21秒执行此定时任务
4、每月定时任务，其规则如下存储:
"每月执行天,执行时间"，时间格为:"HHMISS",如"3,152121"表示每月3号15点21分21秒执行此定时任务
5、每年定时任务，其规则如下存储:"每年执行月,每年执行天,执行时间"，时间格式为:"HHMISS",,如"3,3,152121"表示每年3月月3号15点21分21秒执行此定时任务
6、固定时间定时任务，其存储规则如下:"
具体执行时间1,具体执行时间2,...",表示固定点某些时间执行，时间可以多个，时间格式为"YYYYMMDDHHMISS",如:''2012121212,2012111111''
7、自定义类型定时器，参考quartz CRON表达式';
comment on column META_MAG_TIMER.TIMER_STATE
  is '表示此定时任务是否有效，1：有效，0：无效';
comment on column META_MAG_TIMER.TIMER_CLASS
  is '表示实现了接口IMetaTimer的实现类名，每个定时任务必有一个实现类';
alter table META_MAG_TIMER
  add primary key (TIMER_ID);

prompt
prompt Creating table META_MAG_USER
prompt ============================
prompt
create table META_MAG_USER
(
  USER_ID      NUMBER(18) not null,
  USER_EMAIL   VARCHAR2(64),
  USER_PASS    VARCHAR2(32),
  USER_NAMECN  VARCHAR2(20),
  STATE        NUMBER(9) default 1,
  USER_MOBILE  VARCHAR2(12),
  STATION_ID   NUMBER(9),
  ADMIN_FLAG   NUMBER(9),
  HEAD_SHIP    VARCHAR2(32),
  CREATE_DATE  DATE default sysdate,
  USER_NAMEEN  VARCHAR2(50),
  OA_USER_NAME VARCHAR2(64),
  DEPT_ID      NUMBER(9),
  ZONE_ID      NUMBER(9),
  USER_SN      NUMBER(9),
  VIP_FLAG     NUMBER(9),
  GROUP_ID     NUMBER(18),
  DEFAULT_URL  VARCHAR2(512)
)
;
comment on table META_MAG_USER
  is '管理用户表';
comment on column META_MAG_USER.USER_ID
  is '用户ID';
comment on column META_MAG_USER.USER_EMAIL
  is '用户电邮,用电邮地址登录，并接收电邮提醒';
comment on column META_MAG_USER.USER_PASS
  is '用户密码';
comment on column META_MAG_USER.USER_NAMECN
  is '用户中文名称';
comment on column META_MAG_USER.STATE
  is '用户状态0：禁用(不可登录)1：有效使用的2：待审核的3：锁定的';
comment on column META_MAG_USER.USER_MOBILE
  is '用户手机号，用于短信提醒';
comment on column META_MAG_USER.STATION_ID
  is '用户所属岗位';
comment on column META_MAG_USER.ADMIN_FLAG
  is '是否是超级管理员:0不是、1是.';
comment on column META_MAG_USER.HEAD_SHIP
  is '职务';
comment on column META_MAG_USER.DEPT_ID
  is '部门ID';
comment on column META_MAG_USER.ZONE_ID
  is '地域编码ID   META_DIM_ZONE.zone_id';
comment on column META_MAG_USER.VIP_FLAG
  is '代码:0否、1是.';

prompt
prompt Creating table META_MAG_USER_CHANGE_LOG
prompt =======================================
prompt
create table META_MAG_USER_CHANGE_LOG
(
  LOG_ID      NUMBER(18) not null,
  USER_ID     NUMBER(18),
  CHANGE_TYPE NUMBER(9),
  CHANGE_TIME DATE,
  EDITOR_TYPE NUMBER(9),
  EDITOR_ID   NUMBER(18)
)
;
comment on column META_MAG_USER_CHANGE_LOG.USER_ID
  is '用户ID';
comment on column META_MAG_USER_CHANGE_LOG.CHANGE_TYPE
  is '代码:1密码变动、2状态变动变动类型';
comment on column META_MAG_USER_CHANGE_LOG.CHANGE_TIME
  is '变动时间，如2012-12-21 23:30:45';
comment on column META_MAG_USER_CHANGE_LOG.EDITOR_TYPE
  is '代码:1系统、2人维护人类型';
comment on column META_MAG_USER_CHANGE_LOG.EDITOR_ID
  is '维护人的ID，如张三去修改李四的状态，则本字段为张三';
alter table META_MAG_USER_CHANGE_LOG
  add primary key (LOG_ID);

prompt
prompt Creating table META_MAG_USER_DEPT
prompt =================================
prompt
create table META_MAG_USER_DEPT
(
  DEPT_ID    NUMBER(9) not null,
  PARENT_ID  NUMBER(9),
  DEPT_NAME  VARCHAR2(32),
  DEPT_SN    NUMBER(9),
  DEPT_STATE VARCHAR2(12)
)
;
comment on table META_MAG_USER_DEPT
  is '用户部门';
comment on column META_MAG_USER_DEPT.DEPT_ID
  is '部门ID';
comment on column META_MAG_USER_DEPT.PARENT_ID
  is '上级部门';
comment on column META_MAG_USER_DEPT.DEPT_NAME
  is '部门名称';
comment on column META_MAG_USER_DEPT.DEPT_STATE
  is '代码:0无效、1有效.';

prompt
prompt Creating table META_MAG_USER_GDL
prompt ================================
prompt
create table META_MAG_USER_GDL
(
  GDL_ID     NUMBER(18) not null,
  USER_ID    NUMBER(18) not null,
  MAG_TYPE   NUMBER(9),
  STATE_DATE DATE
)
;
comment on table META_MAG_USER_GDL
  is '用户指标关系';
comment on column META_MAG_USER_GDL.GDL_ID
  is '指标ID';
comment on column META_MAG_USER_GDL.USER_ID
  is '用户ID';
comment on column META_MAG_USER_GDL.MAG_TYPE
  is '代码:1建立、2修改.权限类型1：建立2：修改';

prompt
prompt Creating table META_MAG_USER_MENU
prompt =================================
prompt
create table META_MAG_USER_MENU
(
  USER_ID        NUMBER(18) not null,
  MENU_ID        NUMBER(18) not null,
  EXCLUDE_BUTTON VARCHAR2(100),
  FLAG           NUMBER(9)
)
;
comment on table META_MAG_USER_MENU
  is '用户菜单权限表';
comment on column META_MAG_USER_MENU.USER_ID
  is '用户ID';
comment on column META_MAG_USER_MENU.MENU_ID
  is '菜单ID';
comment on column META_MAG_USER_MENU.EXCLUDE_BUTTON
  is '没有权限的按钮';
comment on column META_MAG_USER_MENU.FLAG
  is '代码:0添加、1减少.赋权类型 0：添加，默认1：减少';

prompt
prompt Creating table META_MAG_USER_PRO_REL
prompt ====================================
prompt
create table META_MAG_USER_PRO_REL
(
  USER_ID    NUMBER(18) not null,
  PROGRAM_ID NUMBER(18) not null,
  REL_TYPE   NUMBER(9),
  STATE_DATE DATE
)
;
comment on table META_MAG_USER_PRO_REL
  is '描述用户与程序关系';
comment on column META_MAG_USER_PRO_REL.USER_ID
  is '用户ID';
comment on column META_MAG_USER_PRO_REL.PROGRAM_ID
  is '元数据中表ID';
comment on column META_MAG_USER_PRO_REL.REL_TYPE
  is '代码:1建立、2修改、3维护.关系类型1：建立2：修改3：维护';
comment on column META_MAG_USER_PRO_REL.STATE_DATE
  is '状态时间';

prompt
prompt Creating table META_MAG_USER_ROLE
prompt =================================
prompt
create table META_MAG_USER_ROLE
(
  USER_ID    NUMBER(18) not null,
  ROLE_ID    NUMBER(18) not null,
  GRANT_FLAG NUMBER(9),
  MAG_FLAG   NUMBER(9)
)
;
comment on table META_MAG_USER_ROLE
  is '用户角色列表';
comment on column META_MAG_USER_ROLE.USER_ID
  is '用户ID';
comment on column META_MAG_USER_ROLE.ROLE_ID
  is '角色ID';
comment on column META_MAG_USER_ROLE.GRANT_FLAG
  is '代码:0否、1是.是否能将此角色授与他人';
comment on column META_MAG_USER_ROLE.MAG_FLAG
  is '代码:0不具有、1具有.是否对此角色有管理权限，在有角色权限菜单管理权限时是否有权管理此角色';

prompt
prompt Creating table META_MAG_USER_SKIN
prompt =================================
prompt
create table META_MAG_USER_SKIN
(
  SKIN_ID      NUMBER(18),
  USER_ID      NUMBER(18),
  USER_SKIN_ID CHAR(18) not null
)
;
comment on column META_MAG_USER_SKIN.USER_ID
  is '用户ID';

prompt
prompt Creating table META_MAG_USER_STATION
prompt ====================================
prompt
create table META_MAG_USER_STATION
(
  STATION_ID     NUMBER(9) not null,
  STATION_NAME   VARCHAR2(64),
  PAR_STATION_ID NUMBER(9),
  STATION_SN     NUMBER(9),
  STATION_STATE  VARCHAR2(12)
)
;
comment on table META_MAG_USER_STATION
  is '分类:天源迪科-元数据-组织权限.';
comment on column META_MAG_USER_STATION.STATION_ID
  is '岗位ID';
comment on column META_MAG_USER_STATION.STATION_NAME
  is '岗位名称';
comment on column META_MAG_USER_STATION.STATION_STATE
  is '代码:0有效、1无效.';

prompt
prompt Creating table META_MAG_USER_STEP_REL
prompt =====================================
prompt
create table META_MAG_USER_STEP_REL
(
  USER_ID    NUMBER(18) not null,
  STEP_ID    NUMBER(18) not null,
  REL_TYPE   NUMBER(9) not null,
  STATE_DATE DATE
)
;
comment on table META_MAG_USER_STEP_REL
  is '描述用户与步骤关系';
comment on column META_MAG_USER_STEP_REL.USER_ID
  is '用户ID';
comment on column META_MAG_USER_STEP_REL.STEP_ID
  is '元数据中表ID';
comment on column META_MAG_USER_STEP_REL.REL_TYPE
  is '代码:1建立、2修改、3维护.关系类型1：建立2：修改3：维护';
comment on column META_MAG_USER_STEP_REL.STATE_DATE
  is '状态时间';

prompt
prompt Creating table META_MAG_USER_TAB_REL
prompt ====================================
prompt
create table META_MAG_USER_TAB_REL
(
  USER_ID       NUMBER(18) not null,
  TABLE_NAME    VARCHAR2(64),
  REL_TYPE      INTEGER not null,
  STATE_DATE    DATE,
  TABLE_ID      NUMBER(18) not null,
  TABLE_STATE   INTEGER,
  TABLE_VERSION NUMBER not null,
  STATE_MARK    VARCHAR2(128),
  REL_ID        NUMBER(18) not null,
  LAST_REL_ID   NUMBER(18)
)
;
comment on table META_MAG_USER_TAB_REL
  is '描述用户与表关系';
comment on column META_MAG_USER_TAB_REL.USER_ID
  is '用户ID';
comment on column META_MAG_USER_TAB_REL.TABLE_NAME
  is '表名称';
comment on column META_MAG_USER_TAB_REL.REL_TYPE
  is '代码:0:申请 1建立、2修改、3维护.关系类型0:申请1：建立2：修改3：维护';
comment on column META_MAG_USER_TAB_REL.STATE_DATE
  is '状态时间';
alter table META_MAG_USER_TAB_REL
  add constraint PRIMARY_REL_ID primary key (REL_ID);

prompt
prompt Creating table META_MENU_GROUP
prompt ==============================
prompt
create table META_MENU_GROUP
(
  GROUP_ID     NUMBER(9) not null,
  GROUP_NAME   VARCHAR2(64),
  GROUP_SN     NUMBER(9),
  GROUP_STATE  VARCHAR2(12),
  GROUP_LOGO   CHAR(18),
  DEFAULT_SKIN NUMBER(9),
  FRAME_URL    VARCHAR2(256)
)
;
alter table META_MENU_GROUP
  add constraint PK_GROUP_ID primary key (GROUP_ID);

prompt
prompt Creating table META_SYS
prompt =======================
prompt
create table META_SYS
(
  SYS_ID   NUMBER(9) not null,
  SYS_NAME VARCHAR2(32),
  SYS_DESC CHAR(18)
)
;
comment on table META_SYS
  is '对各源系统进行编码';
comment on column META_SYS.SYS_ID
  is '维度表ID';
comment on column META_SYS.SYS_NAME
  is '维度表名称';
comment on column META_SYS.SYS_DESC
  is '描述';
alter table META_SYS
  add primary key (SYS_ID);

prompt
prompt Creating table META_SYS_CODE
prompt ============================
prompt
create table META_SYS_CODE
(
  CODE_ID      NUMBER(9) not null,
  CODE_TYPE_ID NUMBER(9) not null,
  CODE_NAME    VARCHAR2(32) not null,
  CODE_VALUE   VARCHAR2(32) not null,
  ORDER_ID     NUMBER(9) not null
)
;
comment on table META_SYS_CODE
  is '元数据表的编码';
comment on column META_SYS_CODE.CODE_ID
  is '编码ID';
comment on column META_SYS_CODE.CODE_TYPE_ID
  is '编码类型ID';
comment on column META_SYS_CODE.CODE_NAME
  is '编码名称';
comment on column META_SYS_CODE.CODE_VALUE
  is '编码值';
comment on column META_SYS_CODE.ORDER_ID
  is '排序ID';
alter table META_SYS_CODE
  add primary key (CODE_ID);

prompt
prompt Creating table META_SYS_CODE_TYPE
prompt =================================
prompt
create table META_SYS_CODE_TYPE
(
  CODE_TYPE_ID   NUMBER(9) not null,
  DIR_ID         NUMBER(9) not null,
  TYPE_CODE      VARCHAR2(20) not null,
  CODE_TYPE_NAME VARCHAR2(32) not null,
  IS_EDITABLE    NUMBER(9) not null,
  DESCRIPTION    VARCHAR2(512)
)
;
comment on column META_SYS_CODE_TYPE.CODE_TYPE_ID
  is '编码类型ID';
comment on column META_SYS_CODE_TYPE.DIR_ID
  is '目录ID';
comment on column META_SYS_CODE_TYPE.TYPE_CODE
  is '类型编码值';
comment on column META_SYS_CODE_TYPE.CODE_TYPE_NAME
  is '编码类型名称';
comment on column META_SYS_CODE_TYPE.IS_EDITABLE
  is '是否可编辑(0：不可编辑，1：可编辑)';
comment on column META_SYS_CODE_TYPE.DESCRIPTION
  is '描述';
alter table META_SYS_CODE_TYPE
  add primary key (CODE_TYPE_ID);

prompt
prompt Creating table META_TABLES
prompt ==========================
prompt
create table META_TABLES
(
  TABLE_ID          NUMBER(18) not null,
  TABLE_NAME        VARCHAR2(64),
  TABLE_NAME_CN     VARCHAR2(64),
  TABLE_OWNER       VARCHAR2(32),
  TABLE_BUS_COMMENT VARCHAR2(1000),
  TABLE_STATE       NUMBER(9),
  TABLE_SPACE       VARCHAR2(64),
  TABLE_GROUP_ID    NUMBER(9),
  DATA_SOURCE_ID    NUMBER(9),
  TABLE_TYPE_ID     NUMBER(9),
  TABLE_VERSION     NUMBER(9) default 0,
  PARTITION_SQL     VARCHAR2(1000)
)
;
comment on table META_TABLES
  is '表类定义 表类指同一用户下相同表结构的一组表';
comment on column META_TABLES.TABLE_ID
  is '表类ID';
comment on column META_TABLES.TABLE_NAME
  is '表类名，带宏变量标示相同结构的一组表';
comment on column META_TABLES.TABLE_NAME_CN
  is '中文名称';
comment on column META_TABLES.TABLE_OWNER
  is '所属用户';
comment on column META_TABLES.TABLE_BUS_COMMENT
  is '表注释，业务解释';
comment on column META_TABLES.TABLE_STATE
  is '表状态 0:无效，1：有效，2：修改状态';
comment on column META_TABLES.TABLE_SPACE
  is '表空间,默认所有表类建在同一表空间';
comment on column META_TABLES.TABLE_GROUP_ID
  is '表类分类ID';
comment on column META_TABLES.DATA_SOURCE_ID
  is '序号';
comment on column META_TABLES.TABLE_VERSION
  is '版本号,同一表类ID状态为有效的版本号只能有一个';
comment on column META_TABLES.PARTITION_SQL
  is '建表的分区SQL部分';

prompt
prompt Creating table META_TABLE_COLS
prompt ==============================
prompt
create table META_TABLE_COLS
(
  COL_ID          NUMBER(18) not null,
  TABLE_ID        NUMBER(18) default 0 not null,
  COL_NAME        VARCHAR2(32),
  COL_NAME_CN     VARCHAR2(32),
  COL_DATATYPE    VARCHAR2(20),
  COL_SIZE        NUMBER(9),
  COL_PREC        NUMBER(9),
  COL_BUS_COMMENT VARCHAR2(1000),
  COL_ORDER       NUMBER(9),
  COL_NULLABLED   NUMBER(9),
  COL_STATE       NUMBER(9),
  DEFAULT_VAL     VARCHAR2(64),
  DIM_TABLE_ID    NUMBER(18),
  COL_BUS_TYPE    NUMBER(9),
  DIM_LEVEL       NUMBER(9) default 0,
  DIM_COL_ID      NUMBER(18),
  IS_PRIMARY      NUMBER(9),
  DIM_TYPE_ID     NUMBER(9),
  TABLE_VERSION   NUMBER(9) default 0
)
;
comment on table META_TABLE_COLS
  is '列信息';
comment on column META_TABLE_COLS.COL_ID
  is '列ID';
comment on column META_TABLE_COLS.TABLE_ID
  is '表类ID';
comment on column META_TABLE_COLS.COL_NAME
  is '表字段名称';
comment on column META_TABLE_COLS.COL_NAME_CN
  is '中文名称';
comment on column META_TABLE_COLS.COL_DATATYPE
  is '表字段对应数据类型,编码在编码表中';
comment on column META_TABLE_COLS.COL_SIZE
  is '表字段对应字段大小  字符表示长度，数字表示数字位数';
comment on column META_TABLE_COLS.COL_PREC
  is '表示数字精度 小数位数';
comment on column META_TABLE_COLS.COL_BUS_COMMENT
  is '描述注释';
comment on column META_TABLE_COLS.COL_ORDER
  is '列顺序，直接影响数据存储';
comment on column META_TABLE_COLS.COL_STATE
  is '字段状态：0:无效 1:有效';
comment on column META_TABLE_COLS.DEFAULT_VAL
  is '默认值';
comment on column META_TABLE_COLS.DIM_TABLE_ID
  is '维度表ID';
comment on column META_TABLE_COLS.COL_BUS_TYPE
  is '字段类型，标识是维度与指标0：维度1：指标';
comment on column META_TABLE_COLS.DIM_LEVEL
  is '维度层级如果表是维度，则此字段表示维度的总层级';
comment on column META_TABLE_COLS.DIM_COL_ID
  is '关联的维度列ID';
comment on column META_TABLE_COLS.IS_PRIMARY
  is '是否主键，用于查询和业务效验';
comment on column META_TABLE_COLS.DIM_TYPE_ID
  is '维度归并类型ID';
comment on column META_TABLE_COLS.TABLE_VERSION
  is '版本号,同一表类ID状态为有效的版本号只能有一个';

prompt
prompt Creating table META_TABLE_DATA_FLOW
prompt ===================================
prompt
create table META_TABLE_DATA_FLOW
(
  FLOW_ID       NUMBER(18) not null,
  DEST_TABLE_ID NUMBER(18),
  SRC_TABLE_ID  NUMBER(18)
)
;
comment on column META_TABLE_DATA_FLOW.FLOW_ID
  is '数据流向ID';
comment on column META_TABLE_DATA_FLOW.DEST_TABLE_ID
  is '目录数据表ID';
comment on column META_TABLE_DATA_FLOW.SRC_TABLE_ID
  is '源数据ID';

prompt
prompt Creating table META_TABLE_DIFF
prompt ==============================
prompt
create table META_TABLE_DIFF
(
  DIFF_ID       NUMBER(18) not null,
  CREATE_DATE   DATE,
  TABLE_ID      NUMBER(18),
  TABLE_VERSION NUMBER(9),
  STATE         NUMBER(9)
)
;

prompt
prompt Creating table META_TABLE_DIFF_ITEM
prompt ===================================
prompt
create table META_TABLE_DIFF_ITEM
(
  DIFF_ID    NUMBER(18),
  ITEM_ID    NUMBER(18) not null,
  ITEM_TYPE  NUMBER(9),
  DIFF_TABLE VARCHAR2(100),
  DIFF_COL   VARCHAR2(100)
)
;

prompt
prompt Creating table META_TABLE_GROUP
prompt ===============================
prompt
create table META_TABLE_GROUP
(
  TABLE_GROUP_ID   NUMBER(9) not null,
  TABLE_GROUP_NAME VARCHAR2(32),
  PAR_GROUP_ID     NUMBER(9),
  TABLE_TYPE_ID    NUMBER(9)
)
;
alter table META_TABLE_GROUP
  add primary key (TABLE_GROUP_ID);

prompt
prompt Creating table META_TABLE_INST
prompt ==============================
prompt
create table META_TABLE_INST
(
  TABLE_NAME    VARCHAR2(64) not null,
  TABLE_ID      NUMBER(18) not null,
  TABLE_RECORDS NUMBER(9),
  TABLE_SPACE   VARCHAR2(64),
  TABLE_OWNER   VARCHAR2(20),
  TABLE_DATE    DATE,
  STATE         NUMBER(9),
  TABLE_INST_ID NUMBER(18) not null,
  TABLE_VERSION NUMBER(9)
)
;
comment on table META_TABLE_INST
  is '实体表';
comment on column META_TABLE_INST.TABLE_ID
  is '表类ID';
comment on column META_TABLE_INST.TABLE_RECORDS
  is '记录数';
comment on column META_TABLE_INST.TABLE_SPACE
  is '表空间';
comment on column META_TABLE_INST.TABLE_OWNER
  is '表用户';
comment on column META_TABLE_INST.TABLE_DATE
  is '数据刷新时间';

prompt
prompt Creating table META_TABLE_INST_DATA
prompt ===================================
prompt
create table META_TABLE_INST_DATA
(
  TABLE_INST_ID         NUMBER(18),
  TABLE_INST_DATA_ID    NUMBER(18) not null,
  TABLE_INST_DATA_STATE NUMBER(9),
  STATE_DATE            DATE,
  PARTITION             VARCHAR2(20),
  SUBPARTITION          VARCHAR2(20),
  DATA_CYCLE_NO         NUMBER(9),
  DATA_LOCAL_CODE       VARCHAR2(20),
  CREATE_DATE           DATE,
  ROW_RECORDS           NUMBER(9)
)
;
comment on column META_TABLE_INST_DATA.TABLE_INST_ID
  is '表实例ID';
comment on column META_TABLE_INST_DATA.TABLE_INST_DATA_STATE
  is '数据状态';
comment on column META_TABLE_INST_DATA.STATE_DATE
  is '状态时间';
comment on column META_TABLE_INST_DATA.PARTITION
  is '分区';
comment on column META_TABLE_INST_DATA.SUBPARTITION
  is '子分区';
comment on column META_TABLE_INST_DATA.DATA_CYCLE_NO
  is '数据周期值';
comment on column META_TABLE_INST_DATA.DATA_LOCAL_CODE
  is '数据地域';
comment on column META_TABLE_INST_DATA.CREATE_DATE
  is '创建时间';
comment on column META_TABLE_INST_DATA.ROW_RECORDS
  is '记录数';

prompt
prompt Creating table META_TABLE_REL
prompt =============================
prompt
create table META_TABLE_REL
(
  TABLE_REL_ID      NUMBER(18) not null,
  TABLE_ID1_COL_IDS VARCHAR2(100),
  TABLE_ID2_COL_IDS VARCHAR2(100),
  TABLE_REL_DESC    VARCHAR2(256),
  TABLE_REL_TYPE    NUMBER(9),
  TABLE_ID1         NUMBER(18),
  TABLE_ID2         NUMBER(18)
)
;
comment on table META_TABLE_REL
  is '表之间客观的关联关系信息';
comment on column META_TABLE_REL.TABLE_REL_ID
  is '表关联ID';
comment on column META_TABLE_REL.TABLE_ID1_COL_IDS
  is '表一关联字段ID列表';
comment on column META_TABLE_REL.TABLE_ID2_COL_IDS
  is '表二关联字段ID';
comment on column META_TABLE_REL.TABLE_REL_DESC
  is '关联描述信息';
comment on column META_TABLE_REL.TABLE_REL_TYPE
  is '表关系类型1:一对一2:一对多';
comment on column META_TABLE_REL.TABLE_ID1
  is '表类ID';
comment on column META_TABLE_REL.TABLE_ID2
  is '表类ID  关联表二';
alter table META_TABLE_REL
  add primary key (TABLE_REL_ID);


--spool off
