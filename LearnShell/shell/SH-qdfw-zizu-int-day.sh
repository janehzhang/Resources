#!/bin/bash
. $HOME/.bash_profile    #$HOME表示路径，可以再环境变量里面设置和查看  
						 # .bash_profile表示文件  
						 #.表示使这个文件生效

if [ $# -ne 1 ]				#参数不等于1
	then
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:请输入参数 <账期日>"
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:数据抽取失败"
		exit 1
fi 							#if语句结束

Date_No=$1					#第一个参数赋值   比如执行命令:  sh SH-qdfw-zizu-int-day.sh 20160804

echo "[`date +%Y-%m-%d\ %H:%M:%S`][SUCCESS LOG]:Date_No=$Date_No"
echo "[`date +%Y-%m-%d\ %H:%M:%S`][SUCCESS LOG]:数据抽取开始..."

/home/cust/etlstep/bin/etl_step -s14001 -d${Date_No} -f
if [ $? -ne 0 ]
	then 
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:数据抽取失败,规则号为[14001]"
		exit 1
fi
/home/cust/etlstep/bin/etl_step -s14002 -d${Date_No} -f
if [ $? -ne 0 ]
	then 
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:数据抽取失败,规则号为[14002]"
		exit 1
fi
/home/cust/etlstep/bin/etl_step -s24003 -d${Date_No} -f
if [ $? -ne 0 ]
	then 
		echo "[`date +%Y-%m-%d\ %H:%M:%S`][ERROR LOG]:数据抽取失败,规则号为[14003]"
		exit 1
fi

echo "[`date +%Y-%m-%d\ %H:%M:%S`][SUCCESS LOG]:数据抽取成功"
exit 0
