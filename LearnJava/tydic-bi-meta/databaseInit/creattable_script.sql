--------------------------------------------------------
-- Export file for user META                          --
-- Created by Administrator on 2012-3-20, ���� 06:15:50 --
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
  is '����Դ';
comment on column META_DATA_SOURCE.DATA_SOURCE_ID
  is '���';
comment on column META_DATA_SOURCE.DATA_SOURCE_NAME
  is '����Դ����';
comment on column META_DATA_SOURCE.DATA_SOURCE_ORANAME
  is '����Դoracle���ƻ���FTP������IP��ַ';
comment on column META_DATA_SOURCE.DATA_SOURCE_USER
  is 'oracle�û����ƻ���FTP�û���';
comment on column META_DATA_SOURCE.DATA_SOURCE_PASS
  is '��¼����';
comment on column META_DATA_SOURCE.DATA_SOURCE_TYPE
  is '����Դ����  TABLE��TXT��DMP��ZIP��FTP��LOCAL  ֧��FTP|ZIP|TXT   LOCAL|ZIP|TXT���';
comment on column META_DATA_SOURCE.DATA_SOURCE_RULE
  is '����Դ���� �磺�ļ�ʱ�ķָ������ TXT:�ֶηָ���+*+*+�зָ���; DMP:,����Ǳ���ΪJDBC���Ӵ�';
comment on column META_DATA_SOURCE.DATA_SOURCE_STATE
  is '״̬';
comment on column META_DATA_SOURCE.DATA_SOURCE_INTRO
  is '����Դ��;˵��';
comment on column META_DATA_SOURCE.SYS_ID
  is 'ϵͳID';
comment on column META_DATA_SOURCE.DATA_SOURCE_MIN_COUNT
  is '����Դ��С������';
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
  is '����:��Դ�Ͽ�-Ԫ����-ά��.';
comment on column META_DIM_LEVEL.DIM_LEVEL
  is 'ά�Ȳ㼶';
comment on column META_DIM_LEVEL.DIM_LEVEL_NAME
  is 'ά�Ȳ㼶����';
comment on column META_DIM_LEVEL.DIM_TYPE_ID
  is '����ID';
comment on column META_DIM_LEVEL.DIM_TABLE_ID
  is '����ID';

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
  is '����:��Դ�Ͽ�-Ԫ����-ά��.';
comment on column META_DIM_MAPP.SYS_ID
  is 'Դϵͳ����';
comment on column META_DIM_MAPP.SRC_NAME
  is 'Դϵͳ��������';
comment on column META_DIM_MAPP.DIM_TABLE_ID
  is '����ID';

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
  is '����:��Դ�Ͽ�-Ԫ����-ά��.';
comment on column META_DIM_TABLES.TABLE_NAME
  is '�����������������ʾ��ͬ�ṹ��һ���';
comment on column META_DIM_TABLES.DATA_SOURCE_ID
  is '����ԴID';
comment on column META_DIM_TABLES.TABLE_DIM_LEVEL
  is 'TABLE_TYPE_ID=2
ά�ȱ�㼶���㼶��һ��ʼ���룬��ֵԽС����Խ�󣬱���: ʡ���㼶Ϊ1�����в㼶Ϊ2��Ӫҵ��Ϊ3';
comment on column META_DIM_TABLES.TABLE_DIM_PREFIX
  is 'ά��ǰ׺
��ַ��ZONE
��Ʒ��PRD';
comment on column META_DIM_TABLES.DIM_KEY_COL_ID
  is 'ά�ȱ�������ID';
comment on column META_DIM_TABLES.DIM_TABLE_ID
  is '����ID';
comment on column META_DIM_TABLES.LAST_LEVEL_FLAG
  is 'ĩ��չ��ӳ���ʶ 0�� ����ʾ���һ�����룬ӳ��ʱʹ�õ����ڶ�����ӳ�䣬���ӳ��ʱ�Զ����ĩ�����룬����Դ��ӳ�䣬�޸�ʱֱ���޸�ĩ�����ϸ��Ĺ�ϵ��Ϊ����ӳ�� 1����Ҫ��ʾ��ӳ�����һ�����룬���һ����Ҫ�����ֹ�����ά����';
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
  is 'ϵͳID';
comment on column META_DIM_TAB_INT_REL.DATA_SOURCE_ID
  is '���';
comment on column META_DIM_TAB_INT_REL.INT_TAB_ID
  is '�ӿڱ����ID�����δ����ӿڱ���Ϊ0';
comment on column META_DIM_TAB_INT_REL.INT_TAB_NAME
  is '�ӿڱ�����';
comment on column META_DIM_TAB_INT_REL.SRC_TAB_NAME
  is 'Դϵͳ��ı���������';
comment on column META_DIM_TAB_INT_REL.DATA_MAPP_SQL
  is '����ӳ��ת��SQL';
comment on column META_DIM_TAB_INT_REL.DATA_MAPP_MARK
  is '����ת��ӳ�䱸ע';
comment on column META_DIM_TAB_INT_REL.USER_ID
  is '�û�ID';
comment on column META_DIM_TAB_INT_REL.DIM_TABLE_ID
  is '����ID';

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
  is '���������޸���ʷ��';
comment on column META_DIM_TAB_MOD_HIS.ITEM_ID
  is '�������ID';
comment on column META_DIM_TAB_MOD_HIS.DIM_TYPE_ID
  is 'ά�ȷ�������';
comment on column META_DIM_TAB_MOD_HIS.DIM_LEVEL
  is '�㼶1';
comment on column META_DIM_TAB_MOD_HIS.MOD_FLAG
  is '�޸ı�ʶ  1���� 2�޸� 3���

�޸�ʱ���޸ĵ��¼�¼ֵ����˱�
���ͨ������ϼ�¼����˱�������
';
comment on column META_DIM_TAB_MOD_HIS.MOD_MARK
  is '�޸ı�ע';
comment on column META_DIM_TAB_MOD_HIS.MOD_DATE
  is '�޸�ʱ��';
comment on column META_DIM_TAB_MOD_HIS.USER_ID
  is '�޸��û�ID';
comment on column META_DIM_TAB_MOD_HIS.DIM_TABLE_ID
  is '����ID';
comment on column META_DIM_TAB_MOD_HIS.SRC_CODE
  is 'Դϵͳ����';
comment on column META_DIM_TAB_MOD_HIS.SRC_NAME
  is 'Դϵͳ��������';
comment on column META_DIM_TAB_MOD_HIS.COL1
  is 'ά�ȱ�̬�����ֶ�';
comment on column META_DIM_TAB_MOD_HIS.COL2
  is 'ά�ȱ�̬�����ֶ�';
comment on column META_DIM_TAB_MOD_HIS.COL3
  is 'ά�ȱ�̬�����ֶ�';
comment on column META_DIM_TAB_MOD_HIS.COL4
  is 'ά�ȱ�̬�����ֶ�';
comment on column META_DIM_TAB_MOD_HIS.ITEM_DESC
  is '����';
comment on column META_DIM_TAB_MOD_HIS.STATE
  is '��Ч״̬ 0 ��Ч 1��Ч';
comment on column META_DIM_TAB_MOD_HIS.AUDIT_FLAG
  is '�Ƿ��Ѿ����';
comment on column META_DIM_TAB_MOD_HIS.AUDIT_USER_ID
  is '����û�ID';

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
  is 'ά�ȱ�������';
comment on column META_DIM_TYPE.DIM_TYPE_ID
  is '����ID';
comment on column META_DIM_TYPE.DIM_TYPE_NAME
  is '��������';
comment on column META_DIM_TYPE.DIM_TYPE_DESC
  is '��������';
comment on column META_DIM_TYPE.DIM_TABLE_ID
  is '����ID';
comment on column META_DIM_TYPE.DIM_TYPE_STATE
  is '״̬';

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
  is 'ָ��Ŀ¼��';
comment on column META_GDL_GROUP.GDL_GROUP_ID
  is 'ָ�����ID';
comment on column META_GDL_GROUP.PAR_GROUP_ID
  is '����Ŀ¼����ID';
comment on column META_GDL_GROUP.GROUP_NAME
  is '��������';
comment on column META_GDL_GROUP.ORDER_ID
  is '��ʾ����ID';
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
  is '����:��Դ�Ͽ�-Ԫ����-��֯Ȩ��.';
comment on column META_GUILD_LINE.GDL_ID
  is 'ָ��ID';
comment on column META_GUILD_LINE.PAR_GDL_ID
  is '����ָ��ID';
comment on column META_GUILD_LINE.GDL_GROUP_ID
  is 'ָ�����ID';
comment on column META_GUILD_LINE.GDL_COL_NAME
  is 'ָ���ֶ����ƣ�ͳһ���ƣ���֤�ڸ�������ͬһָ��������ͬ';
comment on column META_GUILD_LINE.GDL_NAME
  is 'ָ������';
comment on column META_GUILD_LINE.GDL_SRC_TABLE
  is '������Դ������,������, �˱����ݲ���ָ����������';
comment on column META_GUILD_LINE.GDL_SRC_TABLE_ID
  is '������Դ��ID';
comment on column META_GUILD_LINE.GDL_TYPE
  is '����:0����ָ�ꡢ1����ָ�ꡢ2����ָ��.
ָ������
0:����ָ��
1:����ָ��
2:����ָ��
';
comment on column META_GUILD_LINE.GDL_SRC_COL
  is '��Դ�ֶ�';
comment on column META_GUILD_LINE.GDL_SRC_COL_ID
  is 'Դ��ID';
comment on column META_GUILD_LINE.SRC_CALC_GROUP_ID
  is '����������ID';
comment on column META_GUILD_LINE.GDL_BUS_DESC
  is 'ָ��ҵ�����';
comment on column META_GUILD_LINE.GDL_CALC_EXPR
  is '����ָ����ʽ
ָ��ID���㣺{1}/{2} ��
Դ���ֶμ��㣺{COL1}/{COL2}
';
comment on column META_GUILD_LINE.GDL_UNIT
  is 'ָ�굥λ';
comment on column META_GUILD_LINE.USER_ID
  is '������';
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
  is '��¼��־';
comment on column META_MAG_LOGIN_LOG.USER_ID
  is '�û�ID';
comment on column META_MAG_LOGIN_LOG.LOGIN_IP
  is '��¼IP��ַ';
comment on column META_MAG_LOGIN_LOG.LOGIN_MAC
  is '��¼�����MAC��ַ';
comment on column META_MAG_LOGIN_LOG.LOGIN_DATE
  is '��¼ʱ��';
comment on column META_MAG_LOGIN_LOG.LOGOFF_DATE
  is '�ǳ�ʱ��';

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
  is '����˵�';
comment on column META_MAG_MENU.PARENT_ID
  is '�����˵�';
comment on column META_MAG_MENU.MENU_NAME
  is '�˵�����';
comment on column META_MAG_MENU.MENU_TIP
  is '�˵�ע��';
comment on column META_MAG_MENU.MENU_URL
  is '�˵���ַ';
comment on column META_MAG_MENU.PAGE_BUTTON
  is '�˵���ť�б�';
comment on column META_MAG_MENU.ORDER_ID
  is '����ID';
comment on column META_MAG_MENU.IS_SHOW
  is '����:0����ʾ��1��ʾ.
�Ƿ���ʾ';
comment on column META_MAG_MENU.ICON_URL
  is '�˵�ͼ���ַ';
comment on column META_MAG_MENU.TARGET
  is 'Ŀ�괰������
Ĭ��ѡ���� top right blank';
comment on column META_MAG_MENU.USER_ATTR
  is '����:0�����͡�1����.
�Ƿ����û�����';
comment on column META_MAG_MENU.NAV_STATE
  is '����:1 �Ƿ���󻯡�2�Ƿ��й�����4�Ƿ��в˵�����8״̬����16������.
�����״̬ 1 �Ƿ����  2�Ƿ��й��� 4�Ƿ��в˵��� 8״̬�� 16������';
comment on column META_MAG_MENU.USER_ATTR_LIST
  is '���������б� ���ŷָ�
userName={user_namecn},
userEmail={user_email},
staff={user_usernamecn}';
comment on column META_MAG_MENU.MENU_STATE
  is '����:0ͣ�á�1����';
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
  is '�˵�������־';
comment on column META_MAG_MENU_VISIT_LOG.USER_ID
  is '�û�ID';
comment on column META_MAG_MENU_VISIT_LOG.VISIT_TIME
  is '����ʱ��';
comment on column META_MAG_MENU_VISIT_LOG.YEAR_NO
  is '��ݣ����ڷ���';
comment on column META_MAG_MENU_VISIT_LOG.VISIT_ID
  is '�˵�������־ID ';

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
  is '�����û�������־��';
comment on column META_MAG_OPERATE_LOG.USER_ID
  is '�û�ID';
comment on column META_MAG_OPERATE_LOG.OPER_DATE
  is '����ʱ��';
comment on column META_MAG_OPERATE_LOG.OPER_MSG
  is '������Ϣ�����¼�¼��SQL�ȵ�';
comment on column META_MAG_OPERATE_LOG.OPER_TYPE
  is '������Ϣ�����¼�¼��SQL�ȵ�';
comment on column META_MAG_OPERATE_LOG.OPER_DETAIL
  is '������Ϣ�����¼�¼��SQL�ȵ�';
comment on column META_MAG_OPERATE_LOG.MENU_ID
  is '�����˵�';
comment on column META_MAG_OPERATE_LOG.OPER_TABLE
  is '������Ԫ���ݹ����';

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
  is '״̬��1��ʾ�ü�¼Ϊ�û�������Ϣ��2��ʾ�ü�¼Ϊ�û�������Ϣ';
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
  is '�����ɫ��';
comment on column META_MAG_ROLE.ROLE_ID
  is '��ɫID';
comment on column META_MAG_ROLE.ROLE_NAME
  is '��ɫ����';
comment on column META_MAG_ROLE.ROLE_DESC
  is '��ɫ����';
comment on column META_MAG_ROLE.ROLE_STATE
  is '����:0��Ч��1��Ч.
��ɫ״̬';
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
  is '��ɫά�ȹ�ϵ��';
comment on column META_MAG_ROLE_DIM.ROLE_ID
  is '��ɫID';
comment on column META_MAG_ROLE_DIM.DIM_TABLE_ID
  is '����ID';
comment on column META_MAG_ROLE_DIM.TABLE_DIM_LEVEL
  is '��Ȩ�޵�ά�ȱ�㼶';
comment on column META_MAG_ROLE_DIM.DIM_TYPE_ID
  is '����ID';
comment on column META_MAG_ROLE_DIM.TRANS_TYPE
  is '����:0��������1�Ӽ�����2������.
��������
0���и���Ȩ����Ĭ�����Ӽ�Ȩ��
1�����Ӽ�Ȩ����Ĭ���и���Ȩ��
2��������';
comment on column META_MAG_ROLE_DIM.ROLE_DIM_REL_ID
  is '��ɫά�ȹ���ID';
comment on column META_MAG_ROLE_DIM.USE_USER_ATTR_FLAG
  is '�Ƿ�ʹ���û����Ա�ʶֵ������򣬲��ţ���λ�����û����д��ڵ�ά�ȱ�ֵ';

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
  is '��ɫά�ȹ�ϵ����ֵ';
comment on column META_MAG_ROLE_DIM_DETAIL.DIM_TABLE_ID
  is '����ID';
comment on column META_MAG_ROLE_DIM_DETAIL.ROLE_DIM_REL_ID
  is '��ɫά�ȹ���ID';
comment on column META_MAG_ROLE_DIM_DETAIL.DIM_TYPE_ID
  is '����ID';
comment on column META_MAG_ROLE_DIM_DETAIL.DIM_CODE
  is 'ά�ȱ���ֵ';
comment on column META_MAG_ROLE_DIM_DETAIL.FLAG
  is '����:0��ӡ�1����.��Ȩ���� 0����ӣ�Ĭ��1������';

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
  is '��ɫָ���ϵ';
comment on column META_MAG_ROLE_GDL.ROLE_ID
  is '��ɫID';
comment on column META_MAG_ROLE_GDL.GDL_ID
  is 'ָ��ID';

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
  is '��ɫ�˵�Ȩ�ޱ�';
comment on column META_MAG_ROLE_MENU.ROLE_ID
  is '��ɫID';
comment on column META_MAG_ROLE_MENU.MENU_ID
  is '�˵�ID';
comment on column META_MAG_ROLE_MENU.EXCLUDE_BUTTON
  is 'û��Ȩ�޵İ�ť';
comment on column META_MAG_ROLE_MENU.MAP_TYPE
  is '����:1����(�ӷ�),0������(����)';
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
  is '����:��Դ�Ͽ�-Ԫ����-��֯Ȩ��.';
comment on column META_MAG_ROLE_ORG.ROLE_ID
  is '��ɫID';
comment on column META_MAG_ROLE_ORG.STATION_ID
  is '��λID';
comment on column META_MAG_ROLE_ORG.DEPT_ID
  is '����ID';

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
  is 'ϵͳ��ʱ�����';
comment on column META_MAG_TIMER.TIMER_ID
  is '��ʱ����ID';
comment on column META_MAG_TIMER.TIMER_TYPE
  is '��ʱ��������1����ѭ����ʱ���񣬴���������ʲôʱ��ʼ��һ���ļ��ʱ����ж�ʱ����ִ�С�
  2��ÿ�춨ʱִ�У�������������ʱ������ÿ���ʲôʱ��ʼִ�С�3��ÿ�ܶ�ʱִ�У�������������ʱ������ÿ�ܵ����ڼ�ʲôʱ���ִ�С�
4��ÿ�¶�ʱ���񣬴�����������ʱ������ÿ�µ�����ʲôʱ��㶨ʱִ�С�
5��ÿ�궨ʱ���񣬴�����������ʱ������ÿ������������Ǹ�ʱ��㶨��ִ�С�
6���̶�ʱ�䶨ʱ���񣬱�ʾ���Ǽ���ʱ��㶨��ִ��һ�Ρ�
7���Զ������Ͷ�ʱ���������������Զ���CRON���ʽ��';
comment on column META_MAG_TIMER.TIMER_RULE
  is '��ʱ������򣬶��ڲ�ͬ���������ͣ�����������£�
1����ѭ����ʱ������������´洢��
"ִ�д���,���ʱ��,��ʼʱ��,����ʱ��"����ʼʱ�������ʱ���ʽ,���ʱ��Ϊ����Ϊ:YYYYMMDDHHMISS,��������ڽ���ʱ����Բ�д���磺
3,300,20120909111211,2012090913121
��ʾ��20120909111211���300����ִ������
����
3,300,20120909111211
2��ÿ�춨ʱִ��������������´洢:
��ÿ������ִ��ʱ�䡱,ʱ���Ϊ:"HHMISS";
3��ÿ�ܶ�ʱִ��������������´洢:
��������,ִ��ʱ�䡱,ʱ���Ϊ:"HHMISS",��
3,152121��ʾÿ������15��21��21��ִ�д˶�ʱ����
4��ÿ�¶�ʱ������������´洢:
"ÿ��ִ����,ִ��ʱ��"��ʱ���Ϊ:"HHMISS",��"3,152121"��ʾÿ��3��15��21��21��ִ�д˶�ʱ����
5��ÿ�궨ʱ������������´洢:"ÿ��ִ����,ÿ��ִ����,ִ��ʱ��"��ʱ���ʽΪ:"HHMISS",,��"3,3,152121"��ʾÿ��3����3��15��21��21��ִ�д˶�ʱ����
6���̶�ʱ�䶨ʱ������洢��������:"
����ִ��ʱ��1,����ִ��ʱ��2,...",��ʾ�̶���ĳЩʱ��ִ�У�ʱ����Զ����ʱ���ʽΪ"YYYYMMDDHHMISS",��:''2012121212,2012111111''
7���Զ������Ͷ�ʱ�����ο�quartz CRON���ʽ';
comment on column META_MAG_TIMER.TIMER_STATE
  is '��ʾ�˶�ʱ�����Ƿ���Ч��1����Ч��0����Ч';
comment on column META_MAG_TIMER.TIMER_CLASS
  is '��ʾʵ���˽ӿ�IMetaTimer��ʵ��������ÿ����ʱ�������һ��ʵ����';
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
  is '�����û���';
comment on column META_MAG_USER.USER_ID
  is '�û�ID';
comment on column META_MAG_USER.USER_EMAIL
  is '�û�����,�õ��ʵ�ַ��¼�������յ�������';
comment on column META_MAG_USER.USER_PASS
  is '�û�����';
comment on column META_MAG_USER.USER_NAMECN
  is '�û���������';
comment on column META_MAG_USER.STATE
  is '�û�״̬0������(���ɵ�¼)1����Чʹ�õ�2������˵�3��������';
comment on column META_MAG_USER.USER_MOBILE
  is '�û��ֻ��ţ����ڶ�������';
comment on column META_MAG_USER.STATION_ID
  is '�û�������λ';
comment on column META_MAG_USER.ADMIN_FLAG
  is '�Ƿ��ǳ�������Ա:0���ǡ�1��.';
comment on column META_MAG_USER.HEAD_SHIP
  is 'ְ��';
comment on column META_MAG_USER.DEPT_ID
  is '����ID';
comment on column META_MAG_USER.ZONE_ID
  is '�������ID   META_DIM_ZONE.zone_id';
comment on column META_MAG_USER.VIP_FLAG
  is '����:0��1��.';

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
  is '�û�ID';
comment on column META_MAG_USER_CHANGE_LOG.CHANGE_TYPE
  is '����:1����䶯��2״̬�䶯�䶯����';
comment on column META_MAG_USER_CHANGE_LOG.CHANGE_TIME
  is '�䶯ʱ�䣬��2012-12-21 23:30:45';
comment on column META_MAG_USER_CHANGE_LOG.EDITOR_TYPE
  is '����:1ϵͳ��2��ά��������';
comment on column META_MAG_USER_CHANGE_LOG.EDITOR_ID
  is 'ά���˵�ID��������ȥ�޸����ĵ�״̬�����ֶ�Ϊ����';
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
  is '�û�����';
comment on column META_MAG_USER_DEPT.DEPT_ID
  is '����ID';
comment on column META_MAG_USER_DEPT.PARENT_ID
  is '�ϼ�����';
comment on column META_MAG_USER_DEPT.DEPT_NAME
  is '��������';
comment on column META_MAG_USER_DEPT.DEPT_STATE
  is '����:0��Ч��1��Ч.';

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
  is '�û�ָ���ϵ';
comment on column META_MAG_USER_GDL.GDL_ID
  is 'ָ��ID';
comment on column META_MAG_USER_GDL.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_GDL.MAG_TYPE
  is '����:1������2�޸�.Ȩ������1������2���޸�';

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
  is '�û��˵�Ȩ�ޱ�';
comment on column META_MAG_USER_MENU.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_MENU.MENU_ID
  is '�˵�ID';
comment on column META_MAG_USER_MENU.EXCLUDE_BUTTON
  is 'û��Ȩ�޵İ�ť';
comment on column META_MAG_USER_MENU.FLAG
  is '����:0��ӡ�1����.��Ȩ���� 0����ӣ�Ĭ��1������';

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
  is '�����û�������ϵ';
comment on column META_MAG_USER_PRO_REL.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_PRO_REL.PROGRAM_ID
  is 'Ԫ�����б�ID';
comment on column META_MAG_USER_PRO_REL.REL_TYPE
  is '����:1������2�޸ġ�3ά��.��ϵ����1������2���޸�3��ά��';
comment on column META_MAG_USER_PRO_REL.STATE_DATE
  is '״̬ʱ��';

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
  is '�û���ɫ�б�';
comment on column META_MAG_USER_ROLE.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_ROLE.ROLE_ID
  is '��ɫID';
comment on column META_MAG_USER_ROLE.GRANT_FLAG
  is '����:0��1��.�Ƿ��ܽ��˽�ɫ��������';
comment on column META_MAG_USER_ROLE.MAG_FLAG
  is '����:0�����С�1����.�Ƿ�Դ˽�ɫ�й���Ȩ�ޣ����н�ɫȨ�޲˵�����Ȩ��ʱ�Ƿ���Ȩ����˽�ɫ';

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
  is '�û�ID';

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
  is '����:��Դ�Ͽ�-Ԫ����-��֯Ȩ��.';
comment on column META_MAG_USER_STATION.STATION_ID
  is '��λID';
comment on column META_MAG_USER_STATION.STATION_NAME
  is '��λ����';
comment on column META_MAG_USER_STATION.STATION_STATE
  is '����:0��Ч��1��Ч.';

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
  is '�����û��벽���ϵ';
comment on column META_MAG_USER_STEP_REL.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_STEP_REL.STEP_ID
  is 'Ԫ�����б�ID';
comment on column META_MAG_USER_STEP_REL.REL_TYPE
  is '����:1������2�޸ġ�3ά��.��ϵ����1������2���޸�3��ά��';
comment on column META_MAG_USER_STEP_REL.STATE_DATE
  is '״̬ʱ��';

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
  is '�����û�����ϵ';
comment on column META_MAG_USER_TAB_REL.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_TAB_REL.TABLE_NAME
  is '������';
comment on column META_MAG_USER_TAB_REL.REL_TYPE
  is '����:0:���� 1������2�޸ġ�3ά��.��ϵ����0:����1������2���޸�3��ά��';
comment on column META_MAG_USER_TAB_REL.STATE_DATE
  is '״̬ʱ��';
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
  is '�Ը�Դϵͳ���б���';
comment on column META_SYS.SYS_ID
  is 'ά�ȱ�ID';
comment on column META_SYS.SYS_NAME
  is 'ά�ȱ�����';
comment on column META_SYS.SYS_DESC
  is '����';
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
  is 'Ԫ���ݱ�ı���';
comment on column META_SYS_CODE.CODE_ID
  is '����ID';
comment on column META_SYS_CODE.CODE_TYPE_ID
  is '��������ID';
comment on column META_SYS_CODE.CODE_NAME
  is '��������';
comment on column META_SYS_CODE.CODE_VALUE
  is '����ֵ';
comment on column META_SYS_CODE.ORDER_ID
  is '����ID';
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
  is '��������ID';
comment on column META_SYS_CODE_TYPE.DIR_ID
  is 'Ŀ¼ID';
comment on column META_SYS_CODE_TYPE.TYPE_CODE
  is '���ͱ���ֵ';
comment on column META_SYS_CODE_TYPE.CODE_TYPE_NAME
  is '������������';
comment on column META_SYS_CODE_TYPE.IS_EDITABLE
  is '�Ƿ�ɱ༭(0�����ɱ༭��1���ɱ༭)';
comment on column META_SYS_CODE_TYPE.DESCRIPTION
  is '����';
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
  is '���ඨ�� ����ָͬһ�û�����ͬ��ṹ��һ���';
comment on column META_TABLES.TABLE_ID
  is '����ID';
comment on column META_TABLES.TABLE_NAME
  is '�����������������ʾ��ͬ�ṹ��һ���';
comment on column META_TABLES.TABLE_NAME_CN
  is '��������';
comment on column META_TABLES.TABLE_OWNER
  is '�����û�';
comment on column META_TABLES.TABLE_BUS_COMMENT
  is '��ע�ͣ�ҵ�����';
comment on column META_TABLES.TABLE_STATE
  is '��״̬ 0:��Ч��1����Ч��2���޸�״̬';
comment on column META_TABLES.TABLE_SPACE
  is '��ռ�,Ĭ�����б��ཨ��ͬһ��ռ�';
comment on column META_TABLES.TABLE_GROUP_ID
  is '�������ID';
comment on column META_TABLES.DATA_SOURCE_ID
  is '���';
comment on column META_TABLES.TABLE_VERSION
  is '�汾��,ͬһ����ID״̬Ϊ��Ч�İ汾��ֻ����һ��';
comment on column META_TABLES.PARTITION_SQL
  is '����ķ���SQL����';

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
  is '����Ϣ';
comment on column META_TABLE_COLS.COL_ID
  is '��ID';
comment on column META_TABLE_COLS.TABLE_ID
  is '����ID';
comment on column META_TABLE_COLS.COL_NAME
  is '���ֶ�����';
comment on column META_TABLE_COLS.COL_NAME_CN
  is '��������';
comment on column META_TABLE_COLS.COL_DATATYPE
  is '���ֶζ�Ӧ��������,�����ڱ������';
comment on column META_TABLE_COLS.COL_SIZE
  is '���ֶζ�Ӧ�ֶδ�С  �ַ���ʾ���ȣ����ֱ�ʾ����λ��';
comment on column META_TABLE_COLS.COL_PREC
  is '��ʾ���־��� С��λ��';
comment on column META_TABLE_COLS.COL_BUS_COMMENT
  is '����ע��';
comment on column META_TABLE_COLS.COL_ORDER
  is '��˳��ֱ��Ӱ�����ݴ洢';
comment on column META_TABLE_COLS.COL_STATE
  is '�ֶ�״̬��0:��Ч 1:��Ч';
comment on column META_TABLE_COLS.DEFAULT_VAL
  is 'Ĭ��ֵ';
comment on column META_TABLE_COLS.DIM_TABLE_ID
  is 'ά�ȱ�ID';
comment on column META_TABLE_COLS.COL_BUS_TYPE
  is '�ֶ����ͣ���ʶ��ά����ָ��0��ά��1��ָ��';
comment on column META_TABLE_COLS.DIM_LEVEL
  is 'ά�Ȳ㼶�������ά�ȣ�����ֶα�ʾά�ȵ��ܲ㼶';
comment on column META_TABLE_COLS.DIM_COL_ID
  is '������ά����ID';
comment on column META_TABLE_COLS.IS_PRIMARY
  is '�Ƿ����������ڲ�ѯ��ҵ��Ч��';
comment on column META_TABLE_COLS.DIM_TYPE_ID
  is 'ά�ȹ鲢����ID';
comment on column META_TABLE_COLS.TABLE_VERSION
  is '�汾��,ͬһ����ID״̬Ϊ��Ч�İ汾��ֻ����һ��';

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
  is '��������ID';
comment on column META_TABLE_DATA_FLOW.DEST_TABLE_ID
  is 'Ŀ¼���ݱ�ID';
comment on column META_TABLE_DATA_FLOW.SRC_TABLE_ID
  is 'Դ����ID';

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
  is 'ʵ���';
comment on column META_TABLE_INST.TABLE_ID
  is '����ID';
comment on column META_TABLE_INST.TABLE_RECORDS
  is '��¼��';
comment on column META_TABLE_INST.TABLE_SPACE
  is '��ռ�';
comment on column META_TABLE_INST.TABLE_OWNER
  is '���û�';
comment on column META_TABLE_INST.TABLE_DATE
  is '����ˢ��ʱ��';

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
  is '��ʵ��ID';
comment on column META_TABLE_INST_DATA.TABLE_INST_DATA_STATE
  is '����״̬';
comment on column META_TABLE_INST_DATA.STATE_DATE
  is '״̬ʱ��';
comment on column META_TABLE_INST_DATA.PARTITION
  is '����';
comment on column META_TABLE_INST_DATA.SUBPARTITION
  is '�ӷ���';
comment on column META_TABLE_INST_DATA.DATA_CYCLE_NO
  is '��������ֵ';
comment on column META_TABLE_INST_DATA.DATA_LOCAL_CODE
  is '���ݵ���';
comment on column META_TABLE_INST_DATA.CREATE_DATE
  is '����ʱ��';
comment on column META_TABLE_INST_DATA.ROW_RECORDS
  is '��¼��';

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
  is '��֮��͹۵Ĺ�����ϵ��Ϣ';
comment on column META_TABLE_REL.TABLE_REL_ID
  is '�����ID';
comment on column META_TABLE_REL.TABLE_ID1_COL_IDS
  is '��һ�����ֶ�ID�б�';
comment on column META_TABLE_REL.TABLE_ID2_COL_IDS
  is '��������ֶ�ID';
comment on column META_TABLE_REL.TABLE_REL_DESC
  is '����������Ϣ';
comment on column META_TABLE_REL.TABLE_REL_TYPE
  is '���ϵ����1:һ��һ2:һ�Զ�';
comment on column META_TABLE_REL.TABLE_ID1
  is '����ID';
comment on column META_TABLE_REL.TABLE_ID2
  is '����ID  �������';
alter table META_TABLE_REL
  add primary key (TABLE_REL_ID);


--spool off
