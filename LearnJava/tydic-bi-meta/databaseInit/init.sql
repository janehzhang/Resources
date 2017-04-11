-------------------------------------------------
-- Export file for user TEST                   --
-- Created by ��ΰ on 2011-12-23, ���� 01:12:17 --
-------------------------------------------------

spool dataBase.log

prompt
prompt Creating table META_DATA_SOURCE
prompt ===============================
prompt
create table META_DATA_SOURCE
(
  DATA_SOURCE_ID      NUMBER(9) not null,
  DATA_SOURCE_NAME    VARCHAR2(64),
  DATA_SOURCE_ORANAME VARCHAR2(100),
  DATA_SOURCE_USER    VARCHAR2(20),
  DATA_SOURCE_PASS    VARCHAR2(20),
  DATA_SOURCE_TYPE    VARCHAR2(256),
  DATA_SOURCE_RULE    VARCHAR2(256),
  DATA_SOURCE_STATE   NUMBER(9),
  DATA_SOURCE_INTRO   VARCHAR2(512),
  SYS_ID              NUMBER(9)
);

prompt
prompt Creating table META_DIM_LEVEL
prompt =============================
prompt
create table META_DIM_LEVEL
(
  DIM_LEVEL      NUMBER(9),
  DIM_LEVEL_NAME CHAR(18),
  DIM_TYPE_ID    NUMBER(9) not null,
  DIM_TABLE_ID   NUMBER(18) not null
)
;
comment on table META_DIM_LEVEL
  is '����:��Դ�Ͽ�-Ԫ���-ά��.';
comment on column META_DIM_LEVEL.DIM_LEVEL
  is 'ά�Ȳ㼶';
comment on column META_DIM_LEVEL.DIM_LEVEL_NAME
  is 'ά�Ȳ㼶���';
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
  is '����:��Դ�Ͽ�-Ԫ���-ά��.';
comment on column META_DIM_MAPP.SYS_ID
  is 'Դϵͳ����';
comment on column META_DIM_MAPP.SRC_NAME
  is 'Դϵͳ�������';
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
  is '����:��Դ�Ͽ�-Ԫ���-ά��.';
comment on column META_DIM_TABLES.TABLE_NAME
  is '�������������ʾ��ͬ�ṹ��һ���';
comment on column META_DIM_TABLES.DATA_SOURCE_ID
  is '���ԴID';
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
  is 'ĩ��չ��ӳ���ʶ 0�� ����ʾ���һ�����룬ӳ��ʱʹ�õ���ڶ�����ӳ�䣬���ӳ��ʱ�Զ����ĩ�����룬����Դ��ӳ�䣬�޸�ʱֱ���޸�ĩ�����ϸ�Ĺ�ϵ��Ϊ����ӳ�� 1����Ҫ��ʾ��ӳ�����һ�����룬���һ����Ҫ�����ֹ�����ά����';

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
  is '�ӿڱ����';
comment on column META_DIM_TAB_INT_REL.SRC_TAB_NAME
  is 'Դϵͳ��ı��������';
comment on column META_DIM_TAB_INT_REL.DATA_MAPP_SQL
  is '���ӳ��ת��SQL';
comment on column META_DIM_TAB_INT_REL.DATA_MAPP_MARK
  is '���ת��ӳ�䱸ע';
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
  ITEM_NAME     VARCHAR2(32),
  ITEM_PAR_ID   NUMBER(18),
  ITEM_CODE     VARCHAR2(20),
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
  HIS_ID        NUMBER(9)
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

�޸�ʱ���޸ĵ��¼�¼ֵ����˱?
���ͨ�����ϼ�¼����˱�������
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
  is 'Դϵͳ�������';
comment on column META_DIM_TAB_MOD_HIS.COL1
  is 'ά�ȱ?̬�����ֶ�';
comment on column META_DIM_TAB_MOD_HIS.COL2
  is 'ά�ȱ?̬�����ֶ�';
comment on column META_DIM_TAB_MOD_HIS.COL3
  is 'ά�ȱ?̬�����ֶ�';
comment on column META_DIM_TAB_MOD_HIS.COL4
  is 'ά�ȱ?̬�����ֶ�';
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
  is '�������';
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
  ZONE_ID     NUMBER(20) not null,
  ZONE_PAR_ID NUMBER(20) not null,
  ZONE_CODE   VARCHAR2(100) not null,
  ZONE_NAME   VARCHAR2(100) not null,
  ZONE_DESC   VARCHAR2(1000),
  DIM_TYPE_ID NUMBER(20) not null,
  STATE       NUMBER(20) not null,
  DIM_LEVEL   NUMBER(20) not null,
  MOD_FLAG    NUMBER(20) not null,
  ORDER_ID    NUMBER(20) not null
)
;
comment on column META_DIM_ZONE.ZONE_ID
  is 'ά�ȱ�ID,ΪΨһ��ֵ';
comment on column META_DIM_ZONE.ZONE_PAR_ID
  is 'ά�ȱ?ID';
comment on column META_DIM_ZONE.ZONE_CODE
  is 'ҵ������ֶ�';
comment on column META_DIM_ZONE.ZONE_NAME
  is 'ҵ����';
comment on column META_DIM_ZONE.ZONE_DESC
  is 'ҵ������';
comment on column META_DIM_ZONE.DIM_TYPE_ID
  is 'ά�ȷ�������';
comment on column META_DIM_ZONE.STATE
  is '��Ч״̬ 0 ��Ч 1��Ч';
comment on column META_DIM_ZONE.DIM_LEVEL
  is 'ά�Ȳ㼶';
comment on column META_DIM_ZONE.MOD_FLAG
  is '�޸ı�ʶ 0�� 1���� 2�޸�';
comment on column META_DIM_ZONE.ORDER_ID
  is '����ID';
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
  is '�������';
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
  GDL_CODE          VARCHAR2(12),
  GDL_UNIT          VARCHAR2(12)
)
;
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
  MENU_STATE     NUMBER(9),
  REMARK         VARCHAR2(4000)
)
;
comment on table META_MAG_MENU
  is '����˵�';
comment on column META_MAG_MENU.PARENT_ID
  is '�����˵�';
comment on column META_MAG_MENU.MENU_NAME
  is '�˵����';
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
  is 'Ŀ�괰�����
Ĭ��ѡ���� top right blank';
comment on column META_MAG_MENU.USER_ATTR
  is '����:0�����͡�1����.
�Ƿ����û�����';
comment on column META_MAG_MENU.NAV_STATE
  is '����:1 �Ƿ���󻯡�2�Ƿ��й�����4�Ƿ��в˵�����8״̬����16������.
�����״̬ 1 �Ƿ����  2�Ƿ��й��� 4�Ƿ��в˵��� 8״̬�� 16������';
comment on column META_MAG_MENU.USER_ATTR_LIST
  is '��������б� ���ŷָ�
userName={user_namecn},
userEmail={user_email},
staff={user_usernamecn}';
comment on column META_MAG_MENU.MENU_STATE
  is '����:0ͣ�á�1����';
comment on column META_MAG_MENU.REMARK
  is '�˵���ע';

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
  is '������Ԫ��ݹ����';

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
  is '��ɫ���';
comment on column META_MAG_ROLE.ROLE_DESC
  is '��ɫ����';
comment on column META_MAG_ROLE.ROLE_STATE
  is '����:0��Ч��1��Ч.
��ɫ״̬';

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
  is '����:0��ӡ�1����.
��Ȩ���� 0����ӣ�Ĭ��
1������';

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
  is '����:��Դ�Ͽ�-Ԫ���-��֯Ȩ��.';
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
  is '�û��������';
comment on column META_MAG_USER.STATE
  is '�û�״̬
0������(���ɵ�¼)
1����Чʹ�õ�
2������˵�
3�����
';
comment on column META_MAG_USER.USER_MOBILE
  is '�û��ֻ�ţ����ڶ�������';
comment on column META_MAG_USER.STATION_ID
  is '�û�������λ';
comment on column META_MAG_USER.ADMIN_FLAG
  is '�Ƿ��ǳ�������Ա:0���ǡ�1��.
';
comment on column META_MAG_USER.HEAD_SHIP
  is 'ְ��';
comment on column META_MAG_USER.DEPT_ID
  is '����ID';
comment on column META_MAG_USER.ZONE_ID
  is '�������ID   META_DIM_ZONE.zone_id';
comment on column META_MAG_USER.VIP_FLAG
  is '����:0��1��.';

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
  is '�������';
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
  is '����:1������2�޸�.
Ȩ������
1������
2���޸�';

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
  is '����:0��ӡ�1����.
��Ȩ���� 0����ӣ�Ĭ��
1������';

prompt
prompt Creating table META_MAG_USER_PROINST_REL
prompt ========================================
prompt
create table META_MAG_USER_PROINST_REL
(
  USER_ID    NUMBER(18) not null,
  PROINST_ID NUMBER(18) not null,
  REL_TYPE   NUMBER(9),
  STATE_DATE DATE
)
;
comment on table META_MAG_USER_PROINST_REL
  is '�����û������ʵ���ϵ';
comment on column META_MAG_USER_PROINST_REL.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_PROINST_REL.PROINST_ID
  is 'Ԫ����б�ID';
comment on column META_MAG_USER_PROINST_REL.REL_TYPE
  is '����:1������2�޸ġ�3ά��.
��ϵ����
1������
2���޸�
3��ά��';
comment on column META_MAG_USER_PROINST_REL.STATE_DATE
  is '״̬ʱ��';

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
  is 'Ԫ����б�ID';
comment on column META_MAG_USER_PRO_REL.REL_TYPE
  is '����:1������2�޸ġ�3ά��.
��ϵ����
1������
2���޸�
3��ά��';
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
  MAG_FALG   NUMBER(9)
)
;
comment on table META_MAG_USER_ROLE
  is '�û���ɫ�б�';
comment on column META_MAG_USER_ROLE.USER_ID
  is '�û�ID';
comment on column META_MAG_USER_ROLE.ROLE_ID
  is '��ɫID';
comment on column META_MAG_USER_ROLE.GRANT_FLAG
  is '����:0��1��.
�Ƿ��ܽ��˽�ɫ��������';
comment on column META_MAG_USER_ROLE.MAG_FALG
  is '����:0�����С�1����.
�Ƿ�Դ˽�ɫ�й���Ȩ�ޣ����н�ɫȨ�޲˵�����Ȩ��ʱ�Ƿ���Ȩ����˽�ɫ';

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
  is '����:��Դ�Ͽ�-Ԫ���-��֯Ȩ��.';
comment on column META_MAG_USER_STATION.STATION_ID
  is '��λID';
comment on column META_MAG_USER_STATION.STATION_NAME
  is '��λ���';
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
  is 'Ԫ����б�ID';
comment on column META_MAG_USER_STEP_REL.REL_TYPE
  is '����:1������2�޸ġ�3ά��.
��ϵ����
1������
2���޸�
3��ά��';
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
  is '�����';
comment on column META_MAG_USER_TAB_REL.REL_TYPE
  is '����:0:���� 1������2�޸ġ�3ά��.
��ϵ����0:����
1������
2���޸�
3��ά��';
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
comment on table META_MENU_GROUP
  is '�˵���������';
comment on column META_MENU_GROUP.GROUP_STATE
  is '����:0��Ч��1��Ч';
comment on column META_MENU_GROUP.FRAME_URL
  is '��ܵ�ַ,Ϊ����ʹ��Ĭ�ϵĿ��ҳ��';

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

prompt
prompt Creating table META_SYS_CODE
prompt ============================
prompt
create table META_SYS_CODE
(
  CODE_ID    NUMBER(18) not null,
  CODE_NAME  VARCHAR2(64),
  CODE_ITEM  VARCHAR2(20),
  CODE_TYPE  VARCHAR2(12),
  CODE_VALUE VARCHAR2(20),
  CODE_NOTE  VARCHAR2(256),
  ORDER_ID   NUMBER(9)
)
;

prompt
prompt Creating table META_TABLES
prompt ==========================
prompt
create table META_TABLES
(
  TABLE_ID          NUMBER(18) not null,
  TABLE_NAME        VARCHAR2(64),
  TABLE_NAME_CN     VARCHAR2(64),
  TABLE_OWNER        VARCHAR2(32),
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
  is '�������������ʾ��ͬ�ṹ��һ���';
comment on column META_TABLES.TABLE_NAME_CN
  is '�������';
comment on column META_TABLES.TABLE_OWNER
  is '�����û�';
comment on column META_TABLES.TABLE_BUS_COMMENT
  is '��ע�ͣ�ҵ�����';
comment on column META_TABLES.TABLE_STATE
  is '��״̬ 0:��Ч��1����Ч��2���޸�״̬
';
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
  is '���ֶ����';
comment on column META_TABLE_COLS.COL_NAME_CN
  is '�������';
comment on column META_TABLE_COLS.COL_DATATYPE
  is '���ֶζ�Ӧ�������,�����ڱ������';
comment on column META_TABLE_COLS.COL_SIZE
  is '���ֶζ�Ӧ�ֶδ�С  �ַ��ʾ���ȣ����ֱ�ʾ����λ��';
comment on column META_TABLE_COLS.COL_PREC
  is '��ʾ���־��� С��λ��';
comment on column META_TABLE_COLS.COL_BUS_COMMENT
  is '����ע��';
comment on column META_TABLE_COLS.COL_ORDER
  is '��˳��ֱ��Ӱ����ݴ洢';
comment on column META_TABLE_COLS.COL_STATE
  is '�ֶ�״̬��0:��1:�޸�';
comment on column META_TABLE_COLS.DEFAULT_VAL
  is 'Ĭ��ֵ';
comment on column META_TABLE_COLS.DIM_TABLE_ID
  is 'ά�ȱ�ID';
comment on column META_TABLE_COLS.COL_BUS_TYPE
  is '�ֶ����ͣ���ʶ��ά����ָ��
0��ά��
1��ָ��';
comment on column META_TABLE_COLS.DIM_LEVEL
  is 'ά�Ȳ㼶
������ά�ȣ�����ֶα�ʾά�ȵ��ܲ㼶';
comment on column META_TABLE_COLS.DIM_COL_ID
  is '������ά����ID';
comment on column META_TABLE_COLS.IS_PRIMARY
  is '�Ƿ��������ڲ�ѯ��ҵ��Ч��';
comment on column META_TABLE_COLS.DIM_TYPE_ID
  is '����ID';
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
  is '�������ID';
comment on column META_TABLE_DATA_FLOW.DEST_TABLE_ID
  is 'Ŀ¼��ݱ�ID';
comment on column META_TABLE_DATA_FLOW.SRC_TABLE_ID
  is 'Դ���ID';

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
  TABLE_OWNER    VARCHAR2(20),
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
  is '���ˢ��ʱ��';

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
  is '���״̬';
comment on column META_TABLE_INST_DATA.STATE_DATE
  is '״̬ʱ��';
comment on column META_TABLE_INST_DATA.DATA_CYCLE_NO
  is '�������ֵ';
comment on column META_TABLE_INST_DATA.DATA_LOCAL_CODE
  is '��ݵ���';
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
  is '���ϵ����
1:һ��һ
2:һ�Զ�';
comment on column META_TABLE_REL.TABLE_ID1
  is '����ID';
comment on column META_TABLE_REL.TABLE_ID2
  is '����ID  �������
';

prompt
prompt Creating sequence SEQ_DATA_SOURCE_ID
prompt ====================================
prompt
create sequence SEQ_DATA_SOURCE_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DIM_DATA_ID
prompt =================================
prompt
create sequence SEQ_DIM_DATA_ID
minvalue 1
maxvalue 9999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DIM_TABLES_ID
prompt ===================================
prompt
create sequence SEQ_DIM_TABLES_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DIM_TYPE_ID
prompt =================================
prompt
create sequence SEQ_DIM_TYPE_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_GUILD_LINE_ID
prompt ===================================
prompt
create sequence SEQ_GUILD_LINE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_LOGIN_LOG_ID
prompt ==================================
prompt
create sequence SEQ_LOGIN_LOG_ID
minvalue 1
maxvalue 99999999999999999999999999
start with 1
increment by 1
cache 20
cycle;

prompt
prompt Creating sequence SEQ_MAG_COMMENT
prompt =================================
prompt
create sequence SEQ_MAG_COMMENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_GROUP_ID
prompt ==================================
prompt
create sequence SEQ_MAG_GROUP_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_MENU_ID
prompt =================================
prompt
create sequence SEQ_MAG_MENU_ID
minvalue 1
maxvalue 99999999999999999999999
start with 100000
increment by 1
cache 1000
cycle;

prompt
prompt Creating sequence SEQ_MAG_MENU_VISIT_LOG_ID
prompt ===========================================
prompt
create sequence SEQ_MAG_MENU_VISIT_LOG_ID
minvalue 1
maxvalue 99999999999999999999999
start with 1
increment by 1
cache 20
cycle;

prompt
prompt Creating sequence SEQ_MAG_OPER_LOG_ID
prompt =====================================
prompt
create sequence SEQ_MAG_OPER_LOG_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_PUSH_CONFIG_ID
prompt ========================================
prompt
create sequence SEQ_MAG_PUSH_CONFIG_ID
minvalue 1
maxvalue 999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_PUSH_ID
prompt =================================
prompt
create sequence SEQ_MAG_PUSH_ID
minvalue 1
maxvalue 999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_PUSH_USER_ID
prompt ======================================
prompt
create sequence SEQ_MAG_PUSH_USER_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_ROLE_DIM_REL_ID
prompt =========================================
prompt
create sequence SEQ_MAG_ROLE_DIM_REL_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_ROLE_ID
prompt =================================
prompt
create sequence SEQ_MAG_ROLE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20
cycle;

prompt
prompt Creating sequence SEQ_MAG_SKIN_ID
prompt =================================
prompt
create sequence SEQ_MAG_SKIN_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_USER_DEPT_ID
prompt ======================================
prompt
create sequence SEQ_MAG_USER_DEPT_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_USER_ID
prompt =================================
prompt
create sequence SEQ_MAG_USER_ID
minvalue 0
maxvalue 9999999999
start with 2
increment by 1
cache 20
cycle;

prompt
prompt Creating sequence SEQ_MAG_USER_SKIN_ID
prompt ======================================
prompt
create sequence SEQ_MAG_USER_SKIN_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAG_USER_STATION_ID
prompt =========================================
prompt
create sequence SEQ_MAG_USER_STATION_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DIM_TAB_MOD_HIS_ID
prompt =============================================
prompt
create sequence SEQ_META_DIM_TAB_MOD_HIS_ID
minvalue 1
maxvalue 9999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_SYS_CODE_ID
prompt =================================
prompt
create sequence SEQ_SYS_CODE_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_SYS_ID
prompt ============================
prompt
create sequence SEQ_SYS_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TAB_COL_ID
prompt ================================
prompt
create sequence SEQ_TAB_COL_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TAB_GROUP_ID
prompt ==================================
prompt
create sequence SEQ_TAB_GROUP_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 10
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TAB_ID
prompt ============================
prompt
create sequence SEQ_TAB_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TAB_REL_ID
prompt ================================
prompt
create sequence SEQ_TAB_REL_ID
minvalue 1
maxvalue 9999999999999999999999
start with 90
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TAB_INST_DATA_ID
prompt ======================================
prompt
create sequence SEQ_TAB_INST_DATA_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TAB_INST_ID
prompt =================================
prompt
create sequence SEQ_TAB_INST_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TAB_REL_ID
prompt ================================
prompt
create sequence SEQ_TAB_REL_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_VER_TO_HOR_ID
prompt ===================================
prompt
create sequence SEQ_VER_TO_HOR_ID
minvalue 1
maxvalue 9999999999999999999999999
start with 1
increment by 1
cache 20;

--���Դ��ʼ��
insert into META_DATA_SOURCE (DATA_SOURCE_ID, DATA_SOURCE_NAME, DATA_SOURCE_ORANAME, DATA_SOURCE_USER, DATA_SOURCE_PASS, DATA_SOURCE_TYPE, DATA_SOURCE_RULE, DATA_SOURCE_STATE, DATA_SOURCE_INTRO, SYS_ID)
values (0, 'Ԫ��ݹ����', 'config', 'service', 'service', 'TABLE', 'jdbc:oracle:thin:@133.37.58.236:1521:ora10', 1, 'Ԫ��ݹ����', 1);
--ϵͳ����ʼ��
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (25, 'ά�ȱ���鲢����', '6', 'dim_apply', '6', null, 6);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (26, 'ά�ȱ�����������', '7', 'dim_apply', '8', null, 7);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (1, '����������(sum)', '1', 'group', '1', null, 1);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (2, '���������ƽ��ֵ(avg)', '2', 'group', '2', null, 2);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (3, '������������ֵ(max)', '3', 'group', '3', null, 3);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (4, '�����������Сֵ(min)', '4', 'group', '4', null, 4);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (5, '����������¼��(count)', '5', 'group', '5', null, 5);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (6, '������������һ��(last)', '6', 'group', '6', null, 6);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (7, 'ϵͳ��', '1', 'table_type', '1', null, 1);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (8, 'ά�ȱ�', '2', 'table_type', '2', null, 2);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (9, 'ά��ӳ���', '3', 'table_type', '3', null, 3);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (10, '�ӿڱ�', '4', 'table_type', '4', null, 4);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (11, 'ģ�ͱ�', '5', 'table_type', '5', null, 5);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (12, '����', '6', 'table_type', '6', null, 6);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (14, '���ܱ�', '8', 'table_type', '8', null, 8);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (15, 'Ӧ�ñ�', '9', 'table_type', '9', null, 9);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (13, '��ָ���', '7', 'table_type', '7', null, 7);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (21, 'VARCHAR2()', 'VARCHAR2()', 'DATA_TYPE', 'VARCHAR2()', null, 1);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (17, 'NUMBER', 'NUMBER', 'DATA_TYPE', 'NUMBER', null, 2);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (18, 'DATE', 'DATE', 'DATA_TYPE', 'DATE', null, 3);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (19, 'VARCHAR()', 'VARCHAR()', 'DATA_TYPE', 'VARCHAR()', null, null);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (20, 'ά�ȱ���¼����', '1', 'dim_apply', '1', null, 1);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (21, '����ӳ������', '2', 'dim_apply', '2', null, 2);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (22, '�㼶�޸�����', '3', 'dim_apply', '3', null, 3);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (23, '����ά�ȱ�������', '4', 'dim_apply', '4', null, 4);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (24, 'ά�ȱ���ͣ������', '5', 'dim_apply', '5', null, 5);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (26, '�޸�ӳ������', '8', 'dim_apply', '8', null, null);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (27, 'ɾ��ӳ������', '9', 'dim_apply', '9', null, null);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (28, 'CHAR()', 'CHAR()', 'DATA_TYPE', 'CHAR()', null, 4);
insert into META_SYS_CODE (CODE_ID, CODE_NAME, CODE_ITEM, CODE_TYPE, CODE_VALUE, CODE_NOTE, ORDER_ID)
values (29, 'LONG', 'LONG', 'DATA_TYPE', 'LONG', null, 5);
--�û����ʼ��
insert into META_MAG_USER (USER_ID, USER_EMAIL, USER_PASS, USER_NAMECN, STATE, USER_MOBILE, STATION_ID, ADMIN_FLAG, HEAD_SHIP, CREATE_DATE, USER_NAMEEN, OA_USER_NAME, DEPT_ID, ZONE_ID, USER_SN, VIP_FLAG, GROUP_ID, DEFAULT_URL)
values (1, 'admin', '96e79218965eb72c92a549dd5a330112', 'admin', 1, ' ', 22, 1, null, to_date('19-08-2011 16:01:23', 'dd-mm-yyyy hh24:mi:ss'), null, null, 3, 1, null, 1, 1, null);
--����ά�ȱ���ݳ�ʼ��
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (411, 22, '191', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (412, 22, '192', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (413, 22, '193', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (414, 22, '194', '������δ֪Ӫҵ��', '������δ֪Ӫҵ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (415, 3, '195', '�Թ��й�˾����', '�Թ�����Ͻ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (416, 3, '199', '����', '����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (417, 3, '200', '��˳', '��˳��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (418, 3, '201', '�Թ���δ֪Ӫҵ��', '�Թ���δ֪Ӫҵ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (419, 8, '83', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (420, 11, '96', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (421, 19, '109', '�ո�', '�ո���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (422, 5, '122', '����', '����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (423, 21, '134', 'üɽ��δ֪Ӫҵ��', 'üɽ��δ֪Ӫҵ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (424, 10, '155', '�ڽ��й�˾����', '�ڽ�����Ͻ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (425, 2, '41', '�ɶ���δ֪Ӫҵ��', '�ɶ���δ֪Ӫҵ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (426, 9, '165', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (427, 3, '196', '����' || chr(10) || '', '������' || chr(10) || '', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (428, 3, '197', '��', '����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (429, 3, '198', '��̲', '��̲��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (430, 4, '210', '��ɿ�', '��ɿ�', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (434, 4, '214', '����ͻ���', '����ͻ���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (433, 4, '213', '����', '����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (432, 4, '212', '�ʺ�', '�ʺ���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (431, 4, '211', '�ܵ�', '�ܵ���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (435, 2, '21', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (439, 2, '25', '�ɻ�', '�ɻ���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (438, 2, '24', '���', '�����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (437, 2, '23', '��ţ', '��ţ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (436, 2, '22', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (440, 2, '26', '������', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (410, 22, '190', '�����й�˾����', '�㽭��  -- �㽭����ҵ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (1, 0, '0000', '�Ĵ�ʡ', null, 1, 1, 1, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (2, 1, '028', '�ɶ�', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (3, 1, '0813', '�Թ�', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (4, 1, '0812', '��֦��', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (5, 1, '0830', '����', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (6, 1, '0838', '����', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (7, 1, '0816', '����', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (8, 1, '0839', '��Ԫ', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (9, 1, '0825', '����', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (10, 1, '0832', '�ڽ�', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (11, 1, '0833', '��ɽ', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (12, 1, '0817', '�ϳ�', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (19, 1, '0834', '��ɽ', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (20, 1, '0827', '����', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (21, 1, '0840', 'üɽ', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (22, 1, '0841', '����', null, 1, 1, 2, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (234, 17, '1', '�봨', '�봨��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (235, 17, '2', '����', '����', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (236, 17, '3', 'ï��', 'ï��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (237, 17, '4', '����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (238, 17, '5', '��կ��', '��կ����', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (239, 17, '6', '��', '����', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (240, 17, '7', 'С��', 'С����', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (241, 17, '8', '��ˮ', '��ˮ��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (242, 17, '9', '�����й�˾����', '�����--�����ҵ��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (243, 17, '10', '����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (244, 17, '11', '����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (245, 17, '12', '�����', '�������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (246, 17, '13', '��ԭ', '��ԭ��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (247, 17, '14', '������δ֪Ӫҵ��', '������δ֪Ӫҵ��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (248, 20, '15', '�����й�˾����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (249, 20, '16', 'ƽ��', 'ƽ����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (250, 20, '17', 'ͨ��', 'ͨ����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (251, 20, '18', '�Ͻ�', '�Ͻ���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (252, 20, '19', '������δ֪Ӫҵ��', '������δ֪Ӫҵ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (253, 2, '20', '�ɶ��й�˾����', '�ɶ�����Ͻ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (254, 2, '27', '��Ȫ��', '��Ȫ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (255, 2, '28', '��׽�', '��׽���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (256, 2, '29', '�¶�', '�¶���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (257, 2, '30', '�½�', '�½���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (258, 2, '31', '������', '��������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (259, 2, '32', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (260, 2, '33', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (261, 2, '34', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (262, 2, '35', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (263, 2, '36', '˫��', '˫����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (264, 2, '37', 'ۯ��', 'ۯ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (265, 2, '38', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (266, 2, '39', '�ѽ�', '�ֽ���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (267, 2, '40', '�½�', '�½���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (268, 15, '42', '�����й�˾����', '�ﴨ��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (269, 15, '43', '��Դ', '��Դ��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (270, 15, '44', '��', '����', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (271, 15, '45', '����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (272, 15, '46', '����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (273, 15, '47', '����', '����', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (274, 15, '48', '������δ֪Ӫҵ��', '������δ֪Ӫҵ��', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (275, 6, '209', '�����й�˾����', '��������Ͻ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (276, 6, '49', '�㺺', '�㺺��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (277, 6, '50', 'ʲ��', 'ʲ����', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (278, 6, '51', '����', '������', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (279, 6, '52', '�н�', '�н���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (280, 6, '53', '�޽�', '�޽���', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (281, 6, '54', '������δ֪Ӫҵ��', '������δ֪Ӫҵ��', 1, 1, 3, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (282, 18, '55', '�����й�˾����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (283, 18, '56', '��', '����', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (284, 18, '57', '����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (285, 18, '58', '����', '������', 1, 1, 0, 1, 0);
insert into META_DIM_ZONE (ZONE_ID, ZONE_PAR_ID, ZONE_CODE, ZONE_NAME, ZONE_DESC, DIM_TYPE_ID, STATE, DIM_LEVEL, MOD_FLAG, ORDER_ID)
values (286, 18, '59', '�Ž�', '�Ž���', 1, 1, 0, 1, 0);

--��ʼ������ά�ȱ�
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (1, 0, '���в���', 111, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (2, 1, '����', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (3, 1, '��λ�쵼', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (4, 1, '������������', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (5, 1, '�Ű�', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (6, 1, '���Ͳ�', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (7, 1, '����', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (8, 1, '���Ų�', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (9, 1, '�ͻ�����', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (10, 1, '������Դ��', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (11, 1, '�г���', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (12, 1, '��', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (13, 1, 'δȷ��', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (14, 1, '��ֵҵ��', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (15, 1, '����', null, '1');
insert into META_MAG_USER_DEPT (DEPT_ID, PARENT_ID, DEPT_NAME, DEPT_SN, DEPT_STATE)
values (16, 1, '�ۺ�֧�Ų�', null, '1');

--��ʼ����λά�ȱ�
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (1, '���и�λ', 0, 1, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (2, '�ܾ���', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (13, '�ͻ�����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (16, 'ʡ/�� �ͻ�������Ա', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (18, 'ʡ/�� Ӫ��߻���Ա', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (20, 'ʡ/�й�˾�쵼', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (22, 'IT����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (24, '���ž�������', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (26, '������', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (28, '��չ������', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (30, 'δϸ��', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (3, '�ܾ��?��ί���', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (4, '�ܼ�\���ܾ���', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (5, '�ܼ�\�ܾ���', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (6, '�ܾ�������', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (7, '�����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (8, 'ҵ����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (9, '�Ų�Ű٣���˾����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (10, '����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (11, '�ع�˾������Ա', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (12, '��Ӫ����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (14, '����', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (15, 'ʡ/�� IT֧����Ա', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (17, 'ʡ/�� ͳ�Ʒ�����Ա', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (19, 'ʡ/�в��ž���', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (21, '��ݹ���֧��', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (23, '���ž���', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (25, '�������', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (27, '���ܾ���', 1, null, '1');
insert into META_MAG_USER_STATION (STATION_ID, STATION_NAME, PAR_STATION_ID, STATION_SN, STATION_STATE)
values (29, '�����/֧�ֳ�', 1, null, '1');
--ϵͳ����ݳ�ʼ��
insert into META_MENU_GROUP (GROUP_ID, GROUP_NAME, GROUP_SN, GROUP_STATE, GROUP_LOGO, DEFAULT_SKIN, FRAME_URL)
values (1, 'Ԫ��ݹ���', 1, '1', null, null, null);
--���������ݳ�ʼ��
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (1, 'ϵͳ������', 0, 1);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (2, 'ά�ȱ�����', 0, 2);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (3, 'ά��ӳ�������', 0, 3);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (4, '�ӿڱ�����', 0, 4);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (5, 'ģ�ͱ�����', 0, 5);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (6, '��������', 0, 6);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (7, '��ָ�������', 0, 7);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (8, '���ܱ�����', 0, 8);
insert into META_TABLE_GROUP (TABLE_GROUP_ID, TABLE_GROUP_NAME, PAR_GROUP_ID, TABLE_TYPE_ID)
values (9, 'Ӧ�ñ�����', 0, 9);


--�˵���ݳ�ʼ��
insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (46002, 6001, '�ҵı�������', '', 'meta/module/tbl/apply/myApptbl.jsp', '', 1, 2, 1, to_date('16-01-2012 15:25:58', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 0, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (47001, 6001, '��������ҵ�����', '', '/meta/module/tbl/import/batchImport.jsp', '', 1, 9, 1, to_date('31-01-2012 10:49:15', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 0, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (43003, 43001, '���', '', '��˵', '', 1, 1, 1, to_date('30-12-2011 22:42:43', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 0, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (45001, 6001, '����ʵ���', '', 'meta/module/tbl/imptab/imptab.jsp', '', 1, 8, 1, to_date('09-01-2012 14:10:46', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 0, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (5003, 2002, 'Ӧ������', '', 'meta/module/mag/log/menuVisitInfo.jsp?adminFlag=true', '', 1, 11, 1, to_date('20-10-2011 11:47:00', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (1001, 0, 'ά�ȹ���', 'ά�ȹ���', 'meta/module/index/welcome.jsp', '', 1, 4, 1, to_date('13-10-2011 10:11:40', 'dd-mm-yyyy hh24:mi:ss'), '/meta/resource/images/user-mag.png                                                                  ', 'top', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (2005, 2002, '��ɫ����', '', 'meta/module/mag/role/role.jsp', 'addRole:����,modifyRole:�޸�,deleteRole:ɾ��,refUser:�����û�,refMenu:�����˵�,refDimUsers:�Ե������Ȩ', 1, 1, 1, to_date('28-09-2011 09:14:21', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 0, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (1002, 1001, '�����ѯ', '', 'meta/module/mag/zone/zone.jsp', 'refUser:�����û�', 1, 3, 1, to_date('13-10-2011 10:11:40', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (1003, 1001, '���Ų�ѯ', '', 'meta/module/mag/dept/dept.jsp', 'refUser:�����û�', 1, 4, 1, to_date('13-10-2011 10:11:40', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (1004, 1001, '��λ��ѯ', '', 'meta/module/mag/station/station.jsp', 'refUser:�����û�', 1, 5, 1, to_date('13-10-2011 10:11:40', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (5002, 2002, '��������', '', 'meta/module/mag/log/loginLog.jsp?adminFlag=true', '', 1, 12, 1, to_date('20-10-2011 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (8001, 8002, 'ά�ȱ����', '', 'meta/module/tbl/dim/addDim.jsp', '', 1, null, 1, to_date('27-10-2011 09:54:35', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (9001, 6001, '��ѯ����', '', ' ', '', 1, 4, 1, to_date('31-10-2011 09:54:32', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 0, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (2007, 2004, '�û�����', '�û�����', 'meta/public/userApply.jsp', '', 1, 1, 1, to_date('31-10-2011 15:05:18', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (2008, 2004, '�û����', '�û����', 'meta/module/mag/user/userAudit.jsp', 'auditUser:���', 1, 2, 1, to_date('31-10-2011 15:05:18', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (2009, 2004, '�û�ά��', '�û�ά��', 'meta/module/mag/user/user.jsp', 'applyUser:����,modifyUser:�޸�,deleteUser:ɾ��,disableUser:����,startUser:����,refRole:������ɫ,refMenu:�����˵�,initPwd:��������', 1, 3, 1, to_date('31-10-2011 15:05:18', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (1020, 1001, 'ά���鲢����', '', 'meta/module/dim/dimtype/dimtype.jsp', '', 1, null, 1, to_date('07-11-2011 11:47:12', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', null, null, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (1019, 1001, 'ά��ά����ͼ', 'ά��ά����ͼ', 'meta/module/dim/maintain/maintain.jsp', 'addDim:����ά�ȱ���,updateDim:�޸�ά�ȱ�,dimType:�鲢����ά��,maintainCodeData:�������ά��,codeMapping:����ӳ��ά��,intRelValue:�ӿڱ��ѯ', 1, 101, 1, to_date('07-11-2011 10:13:19', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', null, null, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (16006, 6001, 'ϵͳ��ʼ�������', '', 'meta/module/tbl/import/import.jsp', '', 1, 1, 1, to_date('09-11-2011 10:32:24', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 0, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (210012, 1001, '�鿴ά����ͼ', '', 'meta/module/dim/view/view.jsp', 'viewDim:�鿴ά�ȱ���Ϣ,dimType:�鿴�鲢����,maintainCodeData:�鿴�鲢����,codeMapping:�鿴�鲢ӳ��,intRelValue:�ӿڱ��ѯ', 1, null, 1, to_date('21-11-2011 02:17:03', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (2002, 0, 'ϵͳ����', '', 'meta/module/index/welcome.jsp', '', 1, 1, 1, to_date('28-09-2011 09:10:08', 'dd-mm-yyyy hh24:mi:ss'), '/meta/resource/images/system-mag.png                                                                ', 'blank', 0, 31, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (2003, 2002, '�˵�����', '', 'meta/module/mag/menu/menu.jsp', 'addSiblingMenu:����ͬ���˵�,addSubMenu:�����¼��˵�,deleteMenu:ɾ��˵�,updateMenu:�޸Ĳ˵�,refUser:�����û�,refRole:������ɫ,changeLevel:�����Ĳ��', 1, 13, 1, to_date('28-09-2011 09:11:58', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (2004, 2002, '�û�����', '�û�����', 'meta/module/index/welcome.jsp', '', 1, 0, 1, to_date('28-09-2011 09:13:18', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (5001, 2002, 'ҵ��ϵͳ', 'ҵ��ϵͳ', 'meta/module/mag/group/group.jsp', 'addGroup:����,modifyGroup:�޸�,deleteGroup:ɾ��,ownMenus:�����˵�', 1, 25, 1, to_date('19-10-2011 16:44:38', 'dd-mm-yyyy hh24:mi:ss'), '                                                                                                    ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (6001, 0, '�������', '', 'meta/module/index/welcome.jsp', '', 1, 2, 1, to_date('25-10-2011 14:59:36', 'dd-mm-yyyy hh24:mi:ss'), '/meta/resource/images/table.png                                                                     ', 'top', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (7001, 6001, '����������', '', '/meta/module/tbl/category/category.jsp', '', 1, 7, 1, to_date('26-10-2011 14:20:13', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (6003, 6001, '����ҵ�����', '', 'meta/module/tbl/apply/apply.jsp', '', 1, 3, 1, to_date('25-10-2011 15:03:40', 'dd-mm-yyyy hh24:mi:ss'), '                                                                                                    ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (9002, 9001, 'ȫϢ��ͼ', '', 'meta/module/tbl/view/tableView.jsp', 'basicInfo:����Ϣ,tableRef:�����ϵ,tableInstance:����ʵ��,changeHistory:�䶯��ʷ', 1, null, 1, to_date('31-10-2011 09:59:08', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (10001, 6001, '��������', '', '/meta/module/tbl/examine/examine.jsp', '', 1, 6, 1, to_date('01-11-2011 09:33:32', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (17001, 9001, 'Ԥ����Ϣ', '', '/meta/module/tbl/diff/diffWarning.jsp', '', 1, 0, 1, to_date('11-11-2011 15:36:18', 'dd-mm-yyyy hh24:mi:ss'), '[object Object]                                                                                     ', 'right', 0, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (8003, 8002, 'ά������', '', 'meta/module/tbl/maintain/maintain.jsp', '', 1, null, 1, to_date('07-11-2011', 'dd-mm-yyyy'), '', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (18001, 1001, '���ά�ȱ���', '', 'meta/module/dim/dimaudit/dimaudit.jsp', '', 1, null, 1, to_date('15-11-2011 09:13:08', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (212101, 1001, '��ѯ�ӿڱ�', '', 'meta/module/dim/intRel/intRelQuery.jsp', '', 1, 65, 1, to_date('19-11-2011 10:52:24', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', null, null, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (8002, 6001, 'ά������', '', ' ', '', 1, 5, 1, to_date('27-10-2011 10:59:33', 'dd-mm-yyyy hh24:mi:ss'), '                                                                                                    ', 'right', 0, 15, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (35001, 1001, 'ά�ȱ�ӳ�����ά��', '', '/meta/module/dim/intRelManger/intRelManger.jsp', '', 1, null, 1, to_date('23-12-2011 16:21:12', 'dd-mm-yyyy hh24:mi:ss'), '', 'right', 1, 31, '', null);

insert into meta_mag_menu (MENU_ID, PARENT_ID, MENU_NAME, MENU_TIP, MENU_URL, PAGE_BUTTON, GROUP_ID, ORDER_ID, IS_SHOW, CREATE_DATE, ICON_URL, TARGET, USER_ATTR, NAV_STATE, USER_ATTR_LIST, MENU_STATE)
values (43001, 43003, '1', '', '', '', 1, 4, 1, to_date('30-12-2011 22:36:48', 'dd-mm-yyyy hh24:mi:ss'), 'meta/public/upload/menuIcon/1325255734849.png                                                       ', 'right', 1, 0, '', null);

--META_SYS����ݳ�ʼ��
insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (1, 'Ԫ��ݹ���', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (2, 'ODS', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (3, 'EDW', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (4, '�Ʒ�', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (5, 'CRM', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (6, '���Ӫ��', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (7, '���', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (8, '1000', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (9, '����ͨ', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (10, 'ETL����193', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (11, 'ETL����170', '');

insert into meta_sys (SYS_ID, SYS_NAME, SYS_DESC)
values (100, '������ݿ�', '133.37.251.3      ');

commit;

prompt
prompt Creating procedure DIM_HOR_TO_VER
prompt =================================
prompt
create or replace procedure DIM_HOR_TO_VER(dimTableId         in Integer,
                                             dimTypeId          in integer,
                                             toTable            in varchar2,
                                             toTableCodeColumns in varchar2,
                                             toTableNameCOlumns in varchar2,
                                             toTableDescCOlumns in varchar2,
                                             toTableIdColumn    in varchar2) is
    V_DIM_TABLE_NAME   VARCHAR2(200);
    V_TABLE_DIM_PREFIX VARCHAR2(100);
    I                  INTEGER := 0;
    V_CODE_COLUMN      VARCHAR2(1000);
    V_CODE_NAME_COLUMN VARCHAR2(1000);
    V_CODE_DESC_COLUMN VARCHAR2(1000);
    V_TEMP_CHAR        VARCHAR2(1000);
    V_DIM_LEVEL        INTEGER := LENGTHB(toTableCodeColumns) -
                                  LENGTHB(REPLACE(toTableCodeColumns,
                                                  ',',
                                                  ''));
    V_LEVEL            INTEGER := 0;
    STR_SQL            VARCHAR2(2000);
    TYPE CurType IS REF CURSOR;
    CURLEAF   CurType;
    V_LEAF_ID NUMBER(38);
    v_seq_id  number(38);
    --COde ��������
    TYPE codeLengthArray IS TABLE OF NUMBER INDEX BY BINARY_INTEGER;
    V_ARRAY       codeLengthArray;
    CURLCODLENGTH CurType;
  begin
    --��ѯ������ά�ȱ��ѯά�ȱ��������Ϣ��
    SELECT A.TABLE_NAME, B.TABLE_DIM_PREFIX
      INTO V_DIM_TABLE_NAME, V_TABLE_DIM_PREFIX
      FROM META_TABLES A, META_DIM_TABLES B
      LEFT JOIN (SELECT MAX(DIM_TYPE_ID) DIM_TYPE_ID, DIM_TABLE_ID
                   FROM META_DIM_TYPE
                  GROUP BY DIM_TABLE_ID) C ON B.DIM_TABLE_ID =
                                              C.DIM_TABLE_ID
     WHERE TABLE_ID = dimTableId
       AND TABLE_STATE = 1
       AND A.TABLE_ID = B.DIM_TABLE_ID;
    --��ѯÿ���㼶��CODE����
    OPEN CURLCODLENGTH FOR 'SELECT MAX(' || V_TABLE_DIM_PREFIX || '_CODE)  FROM ' || V_DIM_TABLE_NAME || ' GROUP BY DIM_LEVEL ORDER BY DIM_LEVEL';
    LOOP
      FETCH CURLCODLENGTH
        INTO V_TEMP_CHAR;
      EXIT WHEN CURLCODLENGTH%NOTFOUND;
      V_ARRAY(I + 1) := V_TEMP_CHAR;
      I := I + 1;
    END LOOP;
    V_LEVEL := i;
    --��ʼ������
    WHILE I <= V_DIM_LEVEL LOOP
      V_ARRAY(I + 1) := V_ARRAY(V_LEVEL);
      I := I + 1;
    END LOOP;
    I := 0;
    --�򿪲�������Ҷ�ӽڵ���α�
    OPEN CURLEAF FOR 'SELECT A.' || V_TABLE_DIM_PREFIX || '_ID  from ' || V_DIM_TABLE_NAME || ' A LEFT JOIN (SELECT ' || V_TABLE_DIM_PREFIX || '_PAR_ID,COUNT(*)  CNT
  FROM  ' || V_DIM_TABLE_NAME || ' GROUP By ' || V_TABLE_DIM_PREFIX || '_PAR_ID) B ON A.' || V_TABLE_DIM_PREFIX || '_ID=B.' || V_TABLE_DIM_PREFIX || '_PAR_ID
  WHERE NVL(B.CNT,0)=0 AND DIM_TYPE_ID= ' || dimTypeId; --��

    LOOP
      FETCH CURLEAF
        INTO V_LEAF_ID;
      EXIT WHEN CURLEAF%NOTFOUND;
      STR_SQL := 'INSERT INTO ' || toTable || ' (' || toTableCodeColumns || ',' ||
                 toTableNameCOlumns || ',';
      if toTableDescCOlumns IS NOT NULL THEN
        STR_SQL := STR_SQL || toTableDescCOlumns || ',';
      END IF;
      STR_SQL := STR_SQL || toTableIdColumn || ') SELECT ';
      WHILE I <= V_DIM_LEVEL LOOP
        V_CODE_COLUMN := V_CODE_COLUMN || ' MAX(DECODE(B.DIM_LEVEL,' ||
                         (I + 1) || ',';
        if I = 0 THEN
          V_CODE_COLUMN := V_CODE_COLUMN || V_TABLE_DIM_PREFIX || '_CODE';
        ELSE
          V_CODE_COLUMN := V_CODE_COLUMN || 'SUBSTR(' || V_TABLE_DIM_PREFIX ||
                           '_CODE,' || LENGTH(V_ARRAY(I)) || ',' ||
                           (LENGTH(V_ARRAY(I + 1)) - LENGTH(V_ARRAY(I))) || ')';
        END IF;
        V_CODE_COLUMN      := V_CODE_COLUMN || ',NULL )), ';
        V_CODE_NAME_COLUMN := V_CODE_NAME_COLUMN ||
                              ' MAX(DECODE(B.DIM_LEVEL,' || (I + 1) || ',' ||
                              V_TABLE_DIM_PREFIX || '_NAME,NULL )), ';
        V_CODE_DESC_COLUMN := V_CODE_DESC_COLUMN ||
                              ' MAX(DECODE(B.DIM_LEVEL,' || (I + 1) || ',' ||
                              V_TABLE_DIM_PREFIX || '_DESC,NULL )), ';
        I                  := I + 1;
      END LOOP;
      STR_SQL := STR_SQL || V_CODE_COLUMN || V_CODE_NAME_COLUMN;
      if toTableDescCOlumns IS NOT NULL THEN
        STR_SQL := STR_SQL || V_CODE_DESC_COLUMN;
      END IF;
      --��ȡ����
      SELECT SEQ_VER_TO_HOR_ID.Nextval INTO v_seq_id FROM DUAL;

      STR_SQL := STR_SQL || v_seq_id || ' FROM ';
      STR_SQL := STR_SQL || '(SELECT A.*,LEVEL  FROM ' || V_DIM_TABLE_NAME || ' A
  CONNECT BY PRIOR ' || V_TABLE_DIM_PREFIX || '_PAR_ID=' ||
                 V_TABLE_DIM_PREFIX || '_ID  START WITH ' ||
                 V_TABLE_DIM_PREFIX || '_ID=' || V_LEAF_ID || '
  ORDER BY LEVEL DESC) B';
      --DBMS_OUTPUT.PUT_LINE(STR_SQL);
      EXECUTE IMMEDIATE STR_SQL; --��ִ̬��DDL���
    END LOOP;
    CLOSE CURLEAF;
    COMMIT;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      DBMS_OUTPUT.PUT_LINE('δ�ҵ���Ӧ��ά�ȱ���������Ϣ������ʧ�ܣ�');
    WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('ERROR:SQLCODE=' || SQLCODE || ',' || ',MSG=' ||
                           SQLERRM);
  end DIM_HOR_TO_VER;
/

prompt
prompt Creating procedure DIM_TABLE_DATA_INPUT_HOR
prompt ===========================================
prompt
CREATE OR REPLACE PROCEDURE DIM_TABLE_DATA_INPUT_HOR(V_DIM_TABLE_ID          IN INTEGER,
                                                     V_SRC_TABLE             IN VARCHAR2,
                                                     V_SRC_CODE_COLUMNS      IN VARCHAR2,
                                                     V_SRC_CODE_NAME_COLUMNS IN VARCHAR2,
                                                     V_CODE_DESC_COLUMNS     IN VARCHAR2) IS
  --V_DIM_TABLE_ID INTEGER :=441;--��ʼ��ά�ȱ����ID
  --V_SRC_CODE_COLUMNS VARCHAR2(500):='LOCAL_STATE_CODE,LEVEL2_STATE_CODE';
  --V_SRC_CODE_NAME_COLUMNS VARCHAR2(500):='LOCAL_STATE_NAME,LEVEL2_STATE_NAME';
  --V_SRC_TABLE VARCHAR2(200):='D_STATE@TBAS.REGRESS.RDBMS.DEV.US.ORACLE.COM';
  V_DIM_TABLE_NAME         VARCHAR2(200);
  V_TABLE_DIM_PREFIX       VARCHAR2(100);
  V_DIM_TYPE_ID            INTEGER := 0;
  I                        INTEGER := 0;
  V_START                  INTEGER := 0;
  V_END                    INTEGER := 0;
  V_CODE_COLUMN            VARCHAR2(100);
  V_CODE_PARENT_COLUMN     VARCHAR2(100);
  V_CODE_PARENT_ADD_COLUMN VARCHAR2(500);
  V_CODE_NAME_COLUMN       VARCHAR2(100);
  V_CODE_DESC_COLUMN       VARCHAR2(100) := 'CODE_DESC';
  V_CODE_COLUMN_ADD        VARCHAR2(500);
  --ά�Ȳ��
  V_DIM_LEVEL INTEGER := LENGTHB(V_SRC_CODE_COLUMNS) -
                         LENGTHB(REPLACE(V_SRC_CODE_COLUMNS, ',', ''));
  STR_SQL     VARCHAR2(2000);
BEGIN
  --��ѯ������ά�ȱ��ѯά�ȱ��������Ϣ��
  SELECT A.TABLE_NAME, B.TABLE_DIM_PREFIX, NVL(C.DIM_TYPE_ID, 0)
    INTO V_DIM_TABLE_NAME, V_TABLE_DIM_PREFIX, V_DIM_TYPE_ID
    FROM META_TABLES A, META_DIM_TABLES B
    LEFT JOIN (SELECT MAX(DIM_TYPE_ID) DIM_TYPE_ID, DIM_TABLE_ID
                 FROM META_DIM_TYPE
                GROUP BY DIM_TABLE_ID) C
      ON B.DIM_TABLE_ID = C.DIM_TABLE_ID
   WHERE TABLE_ID = V_DIM_TABLE_ID
     AND TABLE_STATE = 1
     AND A.TABLE_ID = B.DIM_TABLE_ID;

  --ѭ������
  WHILE I <= V_DIM_LEVEL LOOP
    STR_SQL := 'INSERT INTO ' || V_DIM_TABLE_NAME || '(' ||
               V_TABLE_DIM_PREFIX || '_ID,DIM_TYPE_ID,STATE,DIM_LEVEL,';
    STR_SQL := STR_SQL || ' MOD_FLAG,ORDER_ID,' || V_TABLE_DIM_PREFIX ||
               '_PAR_ID,' || V_TABLE_DIM_PREFIX || '_CODE,' ||
               V_TABLE_DIM_PREFIX || '_NAME,' || V_TABLE_DIM_PREFIX ||
               '_DESC)';
    --CODE�ֶδ���
    IF I != 0 THEN
      V_START := INSTR(V_SRC_CODE_COLUMNS, ',', 1, I);
    END IF;
    V_END := LENGTH(V_SRC_CODE_COLUMNS) + 1;
    IF I != V_DIM_LEVEL THEN
      V_END := INSTR(V_SRC_CODE_COLUMNS, ',', 1, I + 1);
    END IF;
    V_CODE_COLUMN     := SUBSTR(V_SRC_CODE_COLUMNS,
                                V_START + 1,
                                V_END - V_START - 1);
    V_CODE_COLUMN_ADD := 'MAX(' ||
                         REPLACE(SUBSTR(V_SRC_CODE_COLUMNS, 1, V_END - 1),
                                 ',',
                                 ')||MAX(') || ')';
    --DBMS_OUTPUT.put_line(V_END||SUBSTR(V_SRC_CODE_COLUMNS,1,V_END-1));
    --DBMS_OUTPUT.PUT_LINE(V_CODE_COLUMN_ADD);
    --CODENAME�ֶδ���
    V_START := 0;
    IF I != 0 THEN
      V_START := INSTR(V_SRC_CODE_NAME_COLUMNS, ',', 1, I);
    END IF;
    V_END := LENGTH(V_SRC_CODE_NAME_COLUMNS) + 1;
    IF I != V_DIM_LEVEL THEN
      V_END := INSTR(V_SRC_CODE_NAME_COLUMNS, ',', 1, I + 1);
    END IF;
    V_CODE_NAME_COLUMN := SUBSTR(V_SRC_CODE_NAME_COLUMNS,
                                 V_START + 1,
                                 V_END - V_START - 1);
    --CODE DESC�ֶδ���
    IF V_CODE_DESC_COLUMNS IS NOT NULL THEN
      V_START := 0;
      IF I != 0 THEN
        V_START := INSTR(V_CODE_DESC_COLUMNS, ',', 1, I);
      END IF;
      V_END := LENGTH(V_CODE_DESC_COLUMNS) + 1;
      IF I != V_DIM_LEVEL THEN
        V_END := INSTR(V_CODE_DESC_COLUMNS, ',', 1, I + 1);
      END IF;
      V_CODE_DESC_COLUMN := SUBSTR(V_CODE_DESC_COLUMNS,
                                   V_START + 1,
                                   V_END - V_START - 1);
    END IF;
  
    --SQL ��ѯ
    STR_SQL := STR_SQL || ' SELECT SEQ_DIM_DATA_ID.NEXTVAL,' ||
               V_DIM_TYPE_ID || ',1,'||(I+1)||',1,0,NVL(B.' || V_TABLE_DIM_PREFIX ||
               '_ID,0),A.' || V_CODE_COLUMN || ',NVL(A.' ||
               V_CODE_NAME_COLUMN || ','' '')' || V_CODE_NAME_COLUMN ||
               ' ,A.' || V_CODE_DESC_COLUMN || '  FROM ';
  
    --����ǵ�һ�㼶�������ԭά�ȱ��ҵ��丸ID
    IF I = 0 THEN
      STR_SQL := STR_SQL || ' (SELECT  MAX(0)  ,';
    ELSE
      STR_SQL := STR_SQL || ' (SELECT  ' || V_CODE_PARENT_ADD_COLUMN || ' ' ||
                 V_CODE_PARENT_COLUMN || ',';
    END IF;
    --�����ֶδ���
    IF V_CODE_DESC_COLUMNS IS NOT NULL AND V_CODE_DESC_COLUMNS != ' ' THEN
      STR_SQL := STR_SQL || 'MAX(' || V_CODE_DESC_COLUMN || ') ' ||
                 V_CODE_DESC_COLUMN || ',';
    ELSE
      STR_SQL := STR_SQL || '''  '' AS ' || V_CODE_DESC_COLUMN || ',';
    END IF;
    STR_SQL := STR_SQL || V_CODE_COLUMN_ADD || ' ' || V_CODE_COLUMN ||
               ',MAX(' || V_CODE_NAME_COLUMN || ')   ' ||
               V_CODE_NAME_COLUMN || ' FROM ' || V_SRC_TABLE || ' WHERE ' ||
               V_CODE_COLUMN || ' IS NOT NULL GROUP BY ' || V_CODE_COLUMN ||
               ') A LEFT JOIN ' || V_DIM_TABLE_NAME || ' B ON  ';
    IF I = 0 THEN
      STR_SQL := STR_SQL || ' 1=0  '; --��������
    ELSE
      STR_SQL := STR_SQL || ' A.' || V_CODE_PARENT_COLUMN || '=B.' ||
                 V_TABLE_DIM_PREFIX || '_CODE';
    END IF;
  
    V_CODE_PARENT_COLUMN := V_CODE_COLUMN;
    IF V_CODE_PARENT_ADD_COLUMN IS NOT NULL THEN
      V_CODE_PARENT_ADD_COLUMN := V_CODE_PARENT_ADD_COLUMN || '||';
    END IF;
    V_CODE_PARENT_ADD_COLUMN := V_CODE_PARENT_ADD_COLUMN || 'MAX(' ||
                                V_CODE_PARENT_COLUMN || ')';
    DBMS_OUTPUT.PUT_LINE(STR_SQL);
    EXECUTE IMMEDIATE STR_SQL; --��ִ̬��DDL���
    I := I + 1;
  END LOOP;
  --���?ID��
  COMMIT;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('δ�ҵ���Ӧ��ά�ȱ���������Ϣ������ʧ�ܣ�');
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('ERROR:SQLCODE=' || SQLCODE || ',' || ',MSG=' ||
                         SQLERRM);
END DIM_TABLE_DATA_INPUT_HOR;
/

prompt
prompt Creating procedure DIM_TABLE_DATA_INPUT_VER
prompt ===========================================
prompt
create or replace procedure DIM_TABLE_DATA_INPUT_VER(dimTableId    in Integer,
                                                     srcTable      in varchar2,
                                                     srcCodeColumn in varchar2,
                                                     srcNameColumn in varchar2,
                                                     srcIdColumn   in varchar2,
                                                     srcParColumn  in varchar2,
                                                     rootId        in Integer) is
  DIM_TABLE_NAME   VARCHAR2(100);
  TABLE_DIM_PREFIX VARCHAR2(100);
  V_DIM_TYPE_ID    integer := 0;
  str_sql          varchar2(2000);
  defaultRootid    integer;
  V_MAX_SQ         number(38) := 0;
  V_TABL_MAX_ID    number(38) := 0;
begin
  if rootId is null then
    defaultRootid := 0;
  else
    defaultRootid := rootId;
  end if;
  --��ѯ������ά�ȱ��ѯά�ȱ��������Ϣ��
  SELECT A.TABLE_NAME, B.TABLE_DIM_PREFIX, NVL(C.DIM_TYPE_ID, 0)
    INTO DIM_TABLE_NAME, TABLE_DIM_PREFIX, V_DIM_TYPE_ID
    FROM META_TABLES A, META_DIM_TABLES B
    LEFT JOIN (SELECT MAX(DIM_TYPE_ID) DIM_TYPE_ID, DIM_TABLE_ID
                 FROM META_DIM_TYPE
                GROUP BY DIM_TABLE_ID) C
      ON B.DIM_TABLE_ID = C.DIM_TABLE_ID
   WHERE TABLE_ID = dimTableId
     AND TABLE_STATE = 1
     AND A.TABLE_ID = B.DIM_TABLE_ID;

  str_sql := 'INSERT INTO ' || DIM_TABLE_NAME || '(' || TABLE_DIM_PREFIX ||
             '_ID,DIM_TYPE_ID,STATE,DIM_LEVEL,';
  str_sql := str_sql || ' MOD_FLAG,ORDER_ID,' || TABLE_DIM_PREFIX ||
             '_PAR_ID,' || TABLE_DIM_PREFIX || '_CODE,' || TABLE_DIM_PREFIX ||
             '_NAME)';
  str_sql := str_sql || ' SELECT ' || srcIdColumn || ',' || V_DIM_TYPE_ID ||
             ',0,0,1,0,' || srcParColumn || ',' || srcCodeColumn || ',nvl(' ||
             srcNameColumn || ','' '') FROM ' || srctable;
  execute immediate str_sql; --��ִ̬��DDL���
  commit;
  --���¸�IDΪĬ��ϵͳID
  if defaultRootid != 0 then
    str_sql := 'UPDATE ' || DIM_TABLE_NAME || ' SET ' || TABLE_DIM_PREFIX ||
               '_PAR_ID=0 WHERE ' || TABLE_DIM_PREFIX || '_PAR_ID=' ||
               defaultRootid;
    execute immediate str_sql; --��ִ̬��DDL���
  end if;
  SELECT SEQ_DIM_DATA_ID.NEXTVAL INTO V_MAX_SQ FROM DUAL;
  str_sql := 'SELECT MAX(' || TABLE_DIM_PREFIX || '_ID)   FROM ' ||
             DIM_TABLE_NAME;
  execute immediate str_sql
    INTO V_TABL_MAX_ID; --��ִ̬��DDL���
  --�ؽ�����
  if V_TABL_MAX_ID > V_MAX_SQ then
    Dbms_Output.put_line('ALTER SEQUENCE SEQ_DIM_DATA_ID  Increment By ' ||
                         TO_CHAR(V_TABL_MAX_ID - V_MAX_SQ));
    execute immediate 'ALTER SEQUENCE SEQ_DIM_DATA_ID  Increment By ' ||
                      TO_CHAR(V_TABL_MAX_ID - V_MAX_SQ);
    execute immediate 'SELECT SEQ_DIM_DATA_ID.NEXTVAL FROM DUAL';
    execute immediate 'ALTER SEQUENCE SEQ_DIM_DATA_ID  Increment By 1';
  end if;
  commit;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    Dbms_Output.put_line('δ�ҵ���Ӧ��ά�ȱ���������Ϣ������ʧ�ܣ�');
  when others then
    rollback;
    Dbms_Output.put_line('ERROR:SQLCODE=' || SQLCODE || ',' || ',MSG=' ||
                         SQLERRM);
end DIM_TABLE_DATA_INPUT_VER;
/


spool off
