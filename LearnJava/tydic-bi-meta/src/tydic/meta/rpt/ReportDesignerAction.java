package tydic.meta.rpt;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.DataTable;
import tydic.meta.common.Common;
import tydic.meta.module.tbl.TblConstant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 邹远贵
 * @description 报表配置基本数据读取 <br>
 * @date 2011-12-27
 */
public class ReportDesignerAction
{
	/**
	 * 
	 */
	private ReportDesignerDao				rptDao;
	private static java.util.regex.Pattern	macroReg	= java.util.regex.Pattern.compile("\\{(\\w*)\\}");
	private static java.util.regex.Pattern	termReg		= java.util.regex.Pattern.compile("(\\w*\\$*[<>!=]*\\$*'?\\{\\w*\\}'?)");

	public Object[] readInitData()
	{
		return rptDao.readDataSource();
	}

	//执行SQl查询列字段信息
	public Object[] qryDataCol(String dataName, String dsql, Hashtable<String, String> params)
	{
		try
		{
			if (dsql.length() < 5 || !dsql.trim().substring(0, 6).toUpperCase().startsWith("SELECT"))
			{
				return new Object[] { "false", "不允许执行的SQL" };
			}

			Enumeration<String> em = params.keys();
			while (em.hasMoreElements())
			{
				String key = em.nextElement();
				String val = params.get(key);
				if (val != null && !val.equals(""))
					dsql = dsql.replaceAll("\\{(?i)" + key + "\\}", params.get(key));
				else
				{
					dsql = dsql.replaceAll("\\{(?i)" + key + "\\}", "{" + key + "}");
					dsql = delSQLTermFromKey(dsql, key);
				}
			}
			java.util.regex.Matcher m = macroReg.matcher(dsql);
			while (m.find())
			{
				String varName = m.group(1).toUpperCase();
				if (params.containsKey(varName))
				{
					dsql = dsql.replaceAll("\\{(?i)" + varName + "\\}", params.get(varName));
				}
				else
				{
					dsql = dsql.replaceAll("\\{(?i)" + varName + "\\}", "{" + varName + "}");
					dsql = delSQLTermFromKey(dsql, varName);
				}
			}
			//读取查询字段
			String[] obj = rptDao.qryDataCol(dataName, dsql);
			//	读取字段指标解释
			return new Object[] { obj, rptDao.qryGdlByColNames(obj).rows };
		}
		catch (Exception ex)
		{
			return new Object[] { "false", ex.getMessage() + " sql=" + dsql };
		}
	}

	/**
	 * 条件数据读取 dimParams 维度附加属性字段， 关联维度ID 层级等，用于编码权限过虑
	 * 
	 * @param dataSrcName
	 * @param dsql
	 * @param params
	 * @param type
	 * @param dimParams 维度信息，用于权限和编码过滤
	 * @return
	 */
	public Object[] qryDimTermData(String dataSrcName, String dsql, Hashtable<String, String> params, int type,
			HashMap<String, Object> termAttrs)
	{
		Object[] dimRows = qryDataParam(dataSrcName, dsql, params, type, new String[] { "0", "0" });// int rowNUm,int curPage,String orderBy
		//编码权限过滤
		return dimRows;
	}

	/**
	 * 报表钻取数据读取 columns 查询字段的维度属性 指标属性，用于维度编码和指标权限判断 钻取标识等
	 * 
	 * @param dataSrcName
	 * @param dsqls
	 * @param params
	 * @param dataSrcAttrs 数据源信息 包括字段信息 columns 数据源字段属性
	 *            HashMap<colName,HashMap<attrName,attrValue>>
	 * @param termAttrs 条件属性
	 * @param dataTransRule 0:选择的字段列表 1:转换维度2:转换指标 3:指标列名称
	 * @param pageCfm 分页设置 pageSize,currPage,orderby
	 * @param type 是否初始化查询数据 0:初始化 1:二次查询 有些数据不需要重复查询，比如：指标解释
	 * @return数组: 查询数据列列表 二维数组数据 每一个字段数据层级 钻取维度下一级的权限 横纵转换数据 [字段列表,转换后数据]
	 */
	public Object[] qryDataParam(String dataSrcName, String[] dsqls, Hashtable<String, String> params,
			HashMap<String, Object> termAttrs, HashMap<String, Object> dataSrcAttrs, List<String> dataTransRule,
			String[] pageCfm)
	{
		try
		{
			addLocalCode(params, termAttrs);
			HashMap<String, Object> colDimTrans = new HashMap<String, Object>();
			ArrayList<Object> res = new ArrayList<Object>();
			for (int i = 0; i < dsqls.length; i++)
			{
				String dsql = dsqls[i];
				String colName = "";
				if (i > 0)
				{
					colName = dsql.split(":")[0];
					dsql = dsql.split(":")[1];
				}
				if (i == 0)
				{
					Object[] obj = qryDataParam(dataSrcName, dsql, params, 1, pageCfm);
					if (obj[0].equals("false"))
						throw new Exception(obj[1].toString());
					res.add(obj[0]);//列名
					res.add(obj[1]);//数据
				}
				else
				{
					Object[] obj = qryDataParam(TblConstant.META_DIM_DATA_SOURCE_ID+"", dsql, params, 1, new String[] {});
					if (!obj[0].equals("false"))
					{
						HashMap<String, String> dimCodTrans = buildDimTransCodeMap((Object[][]) obj[1], 0, 1);
						colDimTrans.put(colName, dimCodTrans);
					}
				}
			}
			res.add(colDimTrans);
			//维度及指标权限过滤数据 
			res.add(0);//3: 每一字段数据层级 map<colName,dim_level>
			res.add(0);//4: 根据钻取维度判断下一级的权限 map<code,true>
			res.add(0);//5：横纵转换数据  [字段列表,转换后数据]
			res.add(0);//6：指标解释集合map<name,valueText>
			return res.toArray();
		}
		catch (Exception ex)
		{
			return new Object[] { "false", ex.getMessage() };
		}
	}

	/**
	 * 数组转换成MAP
	 * 
	 * @param obj
	 * @param code
	 * @param name
	 * @return
	 */
	public HashMap<String, String> buildDimTransCodeMap(Object[][] obj, int code, int name)
	{
		HashMap<String, String> dimCodTrans = new HashMap<String, String>();
		for (int i = 0; i < obj.length; i++)
		{
			if (obj[i][code] != null && obj[i][name] != null)
				dimCodTrans.put(obj[i][code].toString(), obj[i][name].toString());
		}
		return dimCodTrans;
	}

	/**
	 * 通过动态参数查询数据
	 * 
	 * @param dataSrcName 数据源ID
	 * @param dsql 数据查询SQl，包含宏变量
	 * @param params 宏变量参数
	 * @param type 参数替换类型
	 * @return
	 */
	private Object[] qryDataParam(String dataSrcName, String dsql, Hashtable<String, String> params, int type, String[] pageCfm)// int rowNUm,int curPage,String orderBy
	{
		try
		{
			if (dsql.length() < 5 || !dsql.trim().substring(0, 6).toUpperCase().startsWith("SELECT"))
			{
				return new Object[] { "false", "不允许执行的SQL" };
			}
			addDateParams(params);
			Enumeration<String> em = params.keys();
			while (em.hasMoreElements())
			{
				String key = em.nextElement();
				String val = params.get(key);
				if (val != null && !val.equals(""))
					dsql = dsql.replaceAll("\\{(?i)" + key + "\\}", params.get(key));
				else
				{
					dsql = dsql.replaceAll("\\{(?i)" + key + "\\}", "{" + key + "}");
					dsql = delSQLTermFromKey(dsql, key);
				}
			}
			if (dsql.indexOf("{") > 0)
			{
				java.util.regex.Matcher m = macroReg.matcher(dsql);
				if (type == 0)
				{
					if (m.find())
					{
						String varName = m.group(1).toUpperCase();
						throw new Exception("参数错误, 宏变量 {" + varName + "}不存在");
					}
				}
				else
				{
					while (m.find())
					{
						String varName = m.group(1).toUpperCase();
						if (params.containsKey(varName))
						{
							dsql = dsql.replaceAll("\\{(?i)" + varName + "\\}", params.get(varName));
						}
						else
						{
							dsql = dsql.replaceAll("\\{(?i)" + varName + "\\}", "{" + varName + "}");
							dsql = delSQLTermFromKey(dsql, varName);
						}
					}
				}
			}
			System.out.println(dsql);
			int rowNUm = 0, curPage = 0;
			String orderBy = "";
			if (pageCfm != null)
			{
				if (pageCfm.length > 0)
					rowNUm = Convert.toInt(pageCfm[0], 0);
				if (pageCfm.length > 1)
					curPage = Convert.toInt(pageCfm[1], 1);
				if (pageCfm.length > 2)
					orderBy = pageCfm[2];
			}
			DataTable tb = rptDao.qryData(dataSrcName, dsql, orderBy, rowNUm, curPage);
			return new Object[] { tb.colsName, tb.rows };
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new Object[] { "false", ex.getMessage() + " sql=" + dsql };
		}
	}

	/**
	 * 根据关键字删除SQL中的条件
	 * 
	 * @param tsql
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String delSQLTermFromKey(String tsql, String key) throws Exception
	{

		//if(debug)System.out.println("删除前：" + tsql);
		int index = tsql.indexOf("{" + key + "}");
		if (index <= 0)
			return tsql;
		int len = tsql.length();
		//select l.area_id,l.area_name from area l where l.local_code=  '{local_code}' order by l.area_id_new 
		index--;
		int startIndex = index;
		if (tsql.charAt(startIndex) == '\'')
			startIndex--;
		char c = tsql.charAt(startIndex);
		while (c == ' ' || c == '\t' || c == '\n' || c == '\r')
		{
			startIndex--;
			c = tsql.charAt(startIndex);
		}

		if (c == '<' || c == '>' || c == '=')
		{
			while (c == '<' || c == '>' || c == '=' || c == '!')
			{
				startIndex--;
				c = tsql.charAt(startIndex);
			}
			while (c == ' ' || c == '\t' || c == '\n' || c == '\r')
			{
				startIndex--;
				c = tsql.charAt(startIndex);
			}
		}
		while (c != ' ' && c != '\t' && c != '\n' && c != '\r')
		{
			startIndex--;
			c = tsql.charAt(startIndex);
		}
		while (c == ' ' || c == '\t' || c == '\n' || c == '\r')
		{
			startIndex--;
			c = tsql.charAt(startIndex);
		}
		int endIndex = index + key.length() + 3;
		if (endIndex < len && (c = tsql.charAt(endIndex)) == '\'')
			endIndex++;
		if (endIndex >= len)
			endIndex = len;
		else
			c = tsql.charAt(endIndex);
		while (endIndex < len && (c == ' ' || c == '\t' || c == '\n' || c == '\r'))
		{
			endIndex++;
			if (endIndex >= len)
				break;
			c = tsql.charAt(endIndex);
		}
		if (endIndex > len)
			endIndex = len;
		String lastWord = lastWord(tsql, startIndex);
		String nextWord = "";

		String sp = "";
		if (endIndex > startIndex)
			sp = tsql.substring(startIndex + 1, endIndex);

		if (lastWord.toLowerCase().equals("and") || lastWord.toLowerCase().equals("or"))
		{
			tsql = tsql.substring(0, startIndex - (lastWord.toLowerCase().equals("or") ? 2 : 3)) + " " + tsql.substring(endIndex);
		}
		else
		{
			nextWord = nextWord(tsql, endIndex);
			if (nextWord.toLowerCase().equals("and") || nextWord.toLowerCase().equals("or"))
			{
				tsql = tsql.substring(0, startIndex + 1) + " "
						+ tsql.substring(endIndex + (lastWord.toLowerCase().equals("or") ? 2 : 3));
			}
			else if (lastWord.toLowerCase().equals("where"))
			{
				tsql = tsql.substring(0, startIndex - 5) + " " + tsql.substring(endIndex);
			}
			else
				throw new Exception("删除宏变量失败：{" + key + "} " + tsql + "");
		}
		//		if (tsql.indexOf("{" + key + "}") == index)
		return delSQLTermFromKey(tsql, key);
	}

	private static String lastWord(String tsql, int index)
	{
		//if(debug)System.out.println("输入：" + index);
		char c = tsql.charAt(index);
		while (c == ' ' || c == '\t' || c == '\n' || c == '\r')
		{
			index--;
			c = tsql.charAt(index);
		}
		int end = index;
		while (c != ' ' && c != '\t' && c != '\n' && c != '\r')
		{
			index--;
			c = tsql.charAt(index);
		}
		String sp = tsql.substring(index + 1, end + 1);
		//if(debug)System.out.println("返回：" + sp);
		return sp.trim();
	}

	private static String nextWord(String tsql, int index)
	{
		char c = tsql.charAt(index);
		int len = tsql.length();
		while (index < len && (c == ' ' || c == '\t' || c == '\n' || c == '\r'))
		{
			index++;
			c = tsql.charAt(index);
		}
		int end = index;
		while (index < len && (c != ' ' && c != '\t' && c != '\n' && c != '\r'))
		{
			index++;
			c = tsql.charAt(index);
		}
		String sp = tsql.substring(end, index);
		return sp.trim();
	}

	/**
	 * 处理参数中的时间
	 * 
	 * @param params
	 */
	public static void addDateParams(Hashtable<String, String> params)
	{
		for (Enumeration<String> e = params.keys(); e.hasMoreElements();)
		{
			String key = e.nextElement();
			String val = params.get(key).toString();
			if (val == null || val.equals(""))
				continue;//				params.remove(key);
			int type = 0;
			if (key.toUpperCase().equals("YYYYMMDD") || key.toUpperCase().equals("DAY_NO") || key.toUpperCase().equals("DATE_NO"))
				type = 8;
			if (key.toUpperCase().equals("YYYYMM") || key.toUpperCase().equals("MONTH_NO"))
				type = 6;
			if (key.toUpperCase().equals("YYYY") || key.toUpperCase().equals("YEAR_NO"))
				type = 4;
			switch (type)
			{
			case 8:
				if (!params.containsKey("YYYYMMDD") && val.length() >= 8)
					params.put("YYYYMMDD", val.substring(0, 8));
				if (!params.containsKey("DD") && val.length() >= 8)
					params.put("DD", val.substring(6, 8));
				if (!params.containsKey("D") && val.length() >= 8)
					params.put("D", Integer.parseInt(val.substring(6, 8)) + "");
			case 6:
				if (!params.containsKey("YYYYMM") && val.length() >= 6)
					params.put("YYYYMM", val.substring(0, 6));
				if (!params.containsKey("MM") && val.length() >= 6)
					params.put("MM", params.get(key).substring(4, 6));
				if (!params.containsKey("M") && val.length() >= 6)
					params.put("M", Integer.parseInt(val.substring(4, 6)) + "");
			case 4:
				if (!params.containsKey("YYYY") && val.length() >= 4)
					params.put("YYYY", params.get(key).substring(0, 4));
				if (!params.containsKey("YY") && val.length() >= 4)
					params.put("YY", val.substring(2, 4));
				break;
			}
			if (type == 8)
				break;
		}
	}

	/**
	 * 添加本地网宏变量LocalCode，主要用于按本地网分区的表钻取 条件钻取，表格钻取
	 * 
	 * @param params
	 * @param termAttrs
	 */
	public static void addLocalCode(Hashtable<String, String> params, HashMap<String, Object> termAttrs)
	{
		if (params.containsKey("LOCAL_CODE"))
			return;
	}

	/**
	 * 保存修改报表配置
	 * 
	 * @param rptCfm
	 * @param termCfm
	 * @param dataCfm
	 * @param dataCfmCols
	 * @param moduleCfm
	 * @param tabCfm
	 * @return
	 */
	public Object[] saveRpt(Hashtable<String, String> rptCfm,
			Hashtable<String, HashMap<String, String>> termCfm,
			Hashtable<String, HashMap<String, String>> dataCfm,
			Hashtable<String, List<HashMap<String, String>>> dataCfmCols,
			Hashtable<String, HashMap<String, String>> moduleCfm,
			Hashtable<String, List<HashMap<String, String>>> tabCfm)
	{
		try
		{
			BaseDAO.beginTransaction();
			long rptId = Convert.toLong(rptCfm.get("REPORT_ID"));
			int type = 0; //	0:新增或另存为
			if (rptId != 0)
				type = 1; //	修改
			if (type != 0)//新增，先查询各表对象的序列号
				rptDao.deleteRptInfo(rptId);//删除原有信息
			if (type == 0)
			{
				rptCfm.remove("REPORT_ID");
				rptId = rptDao.qrySeqValue("SEQ_RPT_REPORT_ID");
				rptCfm.put("REPORT_ID", rptId + "");
			}
			//全部使用新序列
			{
				for (Enumeration<String> e = termCfm.keys(); e.hasMoreElements();)
				{
					String key = e.nextElement();
					HashMap<String, String> term = termCfm.get(key);
					term.remove("termId");
					term.put("termId", rptDao.qrySeqValue("SEQ_RPT_QRY_TERM_ID") + "");
				}
				for (Enumeration<String> e = dataCfm.keys(); e.hasMoreElements();)
				{
					String key = e.nextElement();
					HashMap<String, String> data = dataCfm.get(key);
					data.remove("dataId");
					data.put("dataId", rptDao.qrySeqValue("SEQ_RPT_DATA_ID") + "");
				}
				for (Enumeration<String> e = dataCfmCols.keys(); e.hasMoreElements();)
				{
					String key = e.nextElement();
					List<HashMap<String, String>> datas = dataCfmCols.get(key);
					for (int i = 0; i < datas.size(); i++)
					{
						HashMap<String, String> data = datas.get(i);
						data.remove("dataColId");
						data.put("dataColId", rptDao.qrySeqValue("SEQ_RPT_DATA_COL_ID") + "");
					}
				}
				for (Enumeration<String> e = moduleCfm.keys(); e.hasMoreElements();)
				{
					String key = e.nextElement();
					HashMap<String, String> data = moduleCfm.get(key);
					data.remove("moduleId");
					data.put("moduleId", rptDao.qrySeqValue("SEQ_RPT_MODULE_ID") + "");
				}
				for (Enumeration<String> e = tabCfm.keys(); e.hasMoreElements();)
				{
					String key = e.nextElement();
					List<HashMap<String, String>> datas = tabCfm.get(key);
					for (int i = 0; i < datas.size(); i++)
					{
						HashMap<String, String> data = datas.get(i);
						data.remove("moduleTabId");
						data.put("moduleTabId", rptDao.qrySeqValue("SEQ_RPT_MODULE_TAB_ID") + "");
					}
				}
			}
			//PARANT_TERM_ID
			for (Enumeration<String> e = termCfm.keys(); e.hasMoreElements();)
			{
				String key = e.nextElement();
				HashMap<String, String> term = termCfm.get(key);
				String PARANT_TERM_ID = term.get("PARANT_TERM_ID");
				if (PARANT_TERM_ID != null && !PARANT_TERM_ID.equals(""))
				{
					term.remove("PARANT_TERM_ID");
					term.put("PARANT_TERM_ID", termCfm.get(PARANT_TERM_ID).get("termId"));
				}
			}
			//关联数据源列
			for (Enumeration<String> e = dataCfmCols.keys(); e.hasMoreElements();)
			{
				String key = e.nextElement();
				List<HashMap<String, String>> cols = dataCfmCols.get(key);
				for (int i = 0; i < cols.size(); i++)
				{
					HashMap<String, String> col = cols.get(i);
					col.remove("RPT_DATA_ID");
					col.put("RPT_DATA_ID", dataCfm.get(key).get("dataId"));
				}
			}

			//关联报表条件
			String qryTermIds = rptCfm.get("QUERY_TERM_IDS");
			if (qryTermIds != null && !qryTermIds.trim().equals(""))
			{
				rptCfm.remove("QUERY_TERM_IDS");
				String ids[] = qryTermIds.split(",");
				for (int i = 0; i < ids.length; i++)
				{
					if (termCfm.containsKey(ids[i]))
						ids[i] = termCfm.get(ids[i]).get("termId");
					else
						throw new Exception("条件Id： " + ids[i] + "关联错误，无法保存");
				}
				rptCfm.put("QUERY_TERM_IDS", Common.join(ids, ","));
			}
			//QUERY_TERM_IDS
			for (Enumeration<String> e = tabCfm.keys(); e.hasMoreElements();)
			{
				String key = e.nextElement();
				List<HashMap<String, String>> modTabs = tabCfm.get(key);
				for (int l = 0; l < modTabs.size(); l++)
				{
					HashMap<String, String> tab = modTabs.get(l);
					tab.remove("MODULE_ID");//关联页模块ID
					tab.put("MODULE_ID", moduleCfm.get(key).get("moduleId"));
					qryTermIds = tab.get("QUERY_TERM_IDS");
					if (qryTermIds != null && !qryTermIds.trim().equals(""))
					{
						tab.remove("QUERY_TERM_IDS");
						String ids[] = qryTermIds.split(",");
						for (int i = 0; i < ids.length; i++)
						{
							if (termCfm.containsKey(ids[i]))
								ids[i] = termCfm.get(ids[i]).get("termId");
							else
								throw new Exception("条件Id： " + ids[i] + "关联错误，无法保存");
						}
						tab.put("QUERY_TERM_IDS", Common.join(ids, ","));
					}
					String RPT_DATA_ID = tab.get("RPT_DATA_ID");
					if (RPT_DATA_ID != null && !RPT_DATA_ID.equals(""))
					{
						if (dataCfm.containsKey(RPT_DATA_ID))
						{
							tab.remove("RPT_DATA_ID");
							RPT_DATA_ID = dataCfm.get(RPT_DATA_ID).get("dataId");
							tab.put("RPT_DATA_ID", RPT_DATA_ID);
						}
						else
							throw new Exception("数据源 ID： " + RPT_DATA_ID + "关联错误，无法保存");
					}
					//					LINK_MODULE_IDS
					String LINK_MODULE_IDS = tab.get("LINK_MODULE_IDS");
					if (LINK_MODULE_IDS != null && !LINK_MODULE_IDS.trim().equals(""))
					{
						tab.remove("LINK_MODULE_IDS");
						String ids[] = LINK_MODULE_IDS.split(",");
						for (int i = 0; i < ids.length; i++)
						{
							if (moduleCfm.containsKey(ids[i]))
								ids[i] = moduleCfm.get(ids[i]).get("moduleId");
							else
								throw new Exception("模块Id： " + ids[i] + "关联错误，无法保存");
						}
						tab.put("LINK_MODULE_IDS", Common.join(ids, ","));
					}
				}
			}
			rptDao.saveRpt(rptCfm, termCfm, dataCfm, dataCfmCols, moduleCfm, tabCfm);
			BaseDAO.commit();
			return new Object[] { "true", "保存成功", rptId };
		}
		catch (Exception ex)
		{
			BaseDAO.rollback();
			ex.printStackTrace();
			return new Object[] { "false", ex.getMessage() };
		}
	}

	/**
	 * 读取报表配置，用于设计器初始化
	 * 
	 * @param initType
	 * @param rptId
	 * @return
	 */
	public Object[] readRptInitCfg(int initType, long rptId)
	{
		if (initType == 1)//指标
			return rptDao.readRptInitCfgFromGdl(rptId);
		else if (initType == 2) //模型SQL
		{
			Object[] rptCfm = rptDao.readRptInitCfgFromMod(rptId);
			if (rptCfm[0].equals("false"))
				return rptCfm;
			Map<String, Object> rptCfg = (Map<String, Object>) rptCfm[0];
			Map<String, Object>[] rptQrys = (Map<String, Object>[]) rptCfm[1];
			Map<String, Object>[] rptOutCols = (Map<String, Object>[]) rptCfm[2];
			//从维度表中查询记录数大小，以决定是否异步加载数据
			//读取审核信息 有效时间区间  标识编码转换  
			return new Object[] { "true", rptCfm };
		}
		else if (rptId > 0)
			return rptDao.readRptJsCfg(rptId);
		return null;
	}

	/**
	 * 读取报表配置用于展现
	 * 
	 * @param rptId
	 * @return
	 */
	public Object[] readRptCfg(long rptId)
	{
		try
		{
			long l = System.currentTimeMillis();
			Map<String, Object> rptMap = rptDao.qryRpt(rptId);
			Map<String, Object>[] rptTerms = rptDao.qryRptTerms(rptId);
			Map<String, Object>[] rptDataSrcs = rptDao.qryRptDataSrc(rptId);
			Map<String, Object>[] rptModules = rptDao.qryRptModule(rptId);
			System.out.println(System.currentTimeMillis() - l + " ms");

			Object[] res = new Object[] {
					rptMap, rptTerms, rptDataSrcs, rptModules };

			return new Object[] { "true", res };
		}
		catch (Exception ex)
		{
			return new Object[] { "false", ex.getMessage() };
		}
	}

	public ReportDesignerDao getRptDao()
	{
		return rptDao;
	}

	public void setRptDao(ReportDesignerDao rptDao)
	{
		this.rptDao = rptDao;
	}

	/**
	 * 将维度数据打横的函数，此函数只支持一个维度打横
	 * 
	 * @param colNames 列集合，如['时间','地域','产品类型','指标1','指标2'....]，必填
	 * @param rows 要进行转换的数据，二维数组，必填
	 * @param groupCol 原生数据分组的列信息。必填
	 * @param transCol 要进行打横的列,必填
	 * @param gdlCols 要进行打纵的指标列信息,必填
	 * @return 转换之后的数据，数组第一个为表头信息，从第二个为对应转换之后的数据,第三个为维度转换列的位置
	 */
	public Object[] transData(String[] colNames, String[][] rows, String[] groupCol, String[] transCol, String[] gdlCols)
	{
		try
		{
			if (transCol.length == 1)
				return Common.tranDimData(colNames, rows, groupCol, transCol[0], gdlCols);
			else
				return null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new Object[] { "false", ex.getMessage() };
		}

	}

	public static void main(String[] args)
	{
		String sql = "select l.area_id,l.area_name from area l where l.local_code<>'{local_code}' and l.local_code2!='{local_code}' order by l.area_id_new";
		String key = "local_code";
		System.out.println(sql);
		long st = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++)
		{
			sql = "select l.area_id,l.area_name from area l where l.local_code<>   '{local_code}' and l.local_code2   !='{local_code}' order by l.area_id_new";
			//			sql = delSQLTermKey(sql, key);
		}
		System.out.println(System.currentTimeMillis() - st);
		System.out.println(sql);

		st = System.currentTimeMillis();
		try
		{
			for (int i = 0; i < 10000; i++)
			{
				sql = "select l.area_id,l.area_name from area l where l.local_code<>'{local_code}' and l.local_code2!='{local_code}' order by l.area_id_new";
				sql = delSQLTermFromKey(sql, key);
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		System.out.println(System.currentTimeMillis() - st);
		System.out.println(sql);

	}

}
