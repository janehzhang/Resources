#!/bin/bash
. $HOME/.bash_profile    #$HOME��ʾ·���������ٻ��������������úͲ鿴  
						 # .bash_profile��ʾ�ļ�  
						 #.��ʾʹ����ļ���Ч

if [ $# -ne 1 ]				#����������1
	then
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:��������� <������>"
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:���ݳ�ȡʧ��"
		exit 1
fi 							#if������

Date_No=$1					#��һ��������ֵ   ����ִ������:  sh SH-qdfw-zizu-int-day.sh 20160804

echo "[`date +%Y-%m-%d\ %H:%M:%S`][SUCCESS LOG]:Date_No=$Date_No"
echo "[`date +%Y-%m-%d\ %H:%M:%S`][SUCCESS LOG]:���ݳ�ȡ��ʼ..."

/home/cust/etlstep/bin/etl_step -s14001 -d${Date_No} -f
if [ $? -ne 0 ]
	then 
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:���ݳ�ȡʧ��,�����Ϊ[14001]"
		exit 1
fi
/home/cust/etlstep/bin/etl_step -s14002 -d${Date_No} -f
if [ $? -ne 0 ]
	then 
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:���ݳ�ȡʧ��,�����Ϊ[14002]"
		exit 1
fi
/home/cust/etlstep/bin/etl_step -s24003 -d${Date_No} -f
if [ $? -ne 0 ]
	then 
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:���ݳ�ȡʧ��,�����Ϊ[14003]"
		exit 1
fi

echo "[`date +%Y-%m-%d\ %H:%M:%S`][SUCCESS LOG]:���ݳ�ȡ�ɹ�"
exit 0
