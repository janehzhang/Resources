package tydic.meta.rpt;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.common.Common;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 邹远贵
 * @description 报表模块页面下载 <br>
 * @date 2011-12-27
 */
public class ReportDownloadAction
{
	/**
	 * 
	 */
	private ReportDesignerDao				rptDao			= null;
	private static java.util.regex.Pattern	macroReg		= java.util.regex.Pattern.compile("\\{(\\w*)\\}");
	private static ObjectMapper				mapper			= new ObjectMapper();
	private static int						duckInitLevel	= 0;

	private static Map<String, Object> getModule(ArrayList<Map<String, Object>> rptModules, String modId)
	{
		ArrayList<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < rptModules.size(); i++)
		{
			if (rptModules.get(i).get("modId").equals(modId))
				return rptModules.get(i);
		}
		return null;
	}

	private static ArrayList<Map<String, Object>> getAllTabs(ArrayList<Map<String, Object>> rptModules)
	{
		ArrayList<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < rptModules.size(); i++)
		{
			String modId = (String) rptModules.get(i).get("modId");
			Map<String, Object> tabs = (Map<String, Object>) rptModules.get(i).get("tabs");
			Set<String> ckeys = tabs.keySet();
			for (Iterator<String> it = ckeys.iterator(); it.hasNext();)
			{
				String key = it.next();
				if (key.startsWith(modId) == false)
					continue;
				obj.add((Map<String, Object>) tabs.get(key));
			}
		}
		return obj;
	}

	private static Map<String, Object> getRptTab(ArrayList<Map<String, Object>> rptTabs, String tabId)
	{
		for (int i = 0; i < rptTabs.size(); i++)
		{
			if (rptTabs.get(i).get("tabId").equals(tabId))
				return rptTabs.get(i);
		}
		return null;
	}

	private static Map<String, Object> getRptTab(Map<String, Object> rptMod, String tabId)
	{
		return (Map<String, Object>) Common.getLinkMapVal(rptMod, "tabs." + tabId);
	}

	/**
	 * @param reportId
	 * @param reportConfig
	 * @param reportParams
	 * @param reportDownTab
	 * @param response
	 * @return 文件名称,文件路径
	 * @throws Exception
	 */
	public String[] reportDownLoad(String reportId, String reportConfig, String reportParams
			, String reportDownTab, final HttpServletResponse response) throws Exception
	{
		if (rptDao == null)
			rptDao = new ReportDesignerDao();
		//		int resultStep = 0;
		ResultSet rs = null;
		try
		{
			int outType = 0;
			Object obj = mapper.readValue(reportConfig, Object.class);
			Map<String, Object> rptCfm = (Map<String, Object>) obj;
			String rptTitle = Common.getLinkMapVal(rptCfm, "rptTitle.title.value").toString();
			Map<String, Object> rptDatas = (Map<String, Object>) Common.getLinkMapVal(rptCfm, "dataCfms", "value");
			Map<String, Object> rptTerms = (Map<String, Object>) Common.getLinkMapVal(rptCfm, "termCfms", "value");
			ArrayList<Map<String, Object>> rptModules = (ArrayList<Map<String, Object>>)
					Common.getLinkMapVal(rptCfm, "modules", "value");
			ArrayList<Map<String, Object>> rptAllTabs = getAllTabs(rptModules);
			String tabId = reportDownTab;
			Map<String, Object> rptTab = getRptTab(rptAllTabs, tabId);
			obj = Common.getLinkMapVal(rptTab, "dowLoadType");
			if (obj != null)
			{
				outType = Convert.toInt(obj.toString());
			}

			Map<String, Object> downSrcData = (Map<String, Object>) rptDatas.get(
					Common.getLinkMapVal(rptTab, "dataSrcName", "value").toString());
			Map<String, Object> rptMod = getModule(rptModules, rptTab.get("modId").toString());
			if (rptAllTabs.size() > 1)
			{
				rptTitle += "_" + Common.getLinkMapVal(rptMod, "moduleName", "value").toString();
				if (((Map<String, Object>) rptMod.get("tabs")).size() > 2)
					rptTitle += "_" + Common.getLinkMapVal(rptTab, "tabName", "value").toString();
			}
			String dataQuerySql = Common.getLinkMapVal(downSrcData, "dataSql", "value").toString();
			String dataSrcName = Common.getLinkMapVal(downSrcData, "dataSrcId", "value").toString();

			Hashtable<String, String> params = new Hashtable<String, String>();
			params = mapper.readValue(reportParams, params.getClass());
			String dsql = dataQuerySql;
			ReportDesignerAction.addDateParams(params);
			boolean duckFlag = false;
			int duckLevel = 1;
			String duckSQL = "";
			if (dsql.indexOf("{DIM_DUCK_TERM_SQL}") > 0)
			{
				Object duckPartSql = Common.getLinkMapVal(rptTab, "table.duckPartSql.value");
				if (duckPartSql == null)
					throw new Exception("配置了钻取SQL，却未配置钻取条件,SQL=" + dsql);
				String[] duckTermSqls = duckPartSql.toString().split(";");
				dsql = dsql.replace("{DIM_DUCK_TERM_SQL}", duckTermSqls[0]);
				if (duckTermSqls.length == 2)
				{
					duckSQL = dataQuerySql.replace("{DIM_DUCK_TERM_SQL}", duckTermSqls[1]);
					duckFlag = true;
				}
			}
			dsql = replaceMacro(dsql, params);
			ArrayList<String> _colNames = (ArrayList<String>) Common.getLinkMapVal(rptTab, "table.colNames");
			String[] colNames = new String[_colNames.size()];
			_colNames.toArray(colNames);
			HashMap<String, HashMap<String, Object>> selColumns = (HashMap<String, HashMap<String, Object>>) Common
					.getLinkMapVal(rptTab, "table.columns.value");
			ArrayList<Map<String, Object>> dataColAttrs = (ArrayList<Map<String, Object>>) Common.getLinkMapVal(downSrcData,
					"colAttrs.value");
			HashMap<String, Map<String, Object>> colMap = new HashMap<String, Map<String, Object>>();
			boolean specifyCol = true;
			if (colNames == null || colNames.length == 0)
			{
				specifyCol = false;
				colNames = new String[dataColAttrs.size()];
			}
			for (int i = 0; i < dataColAttrs.size(); i++)
			{
				Map<String, Object> colAttr = dataColAttrs.get(i);
				String colName = Common.getLinkMapVal(colAttr, "name.value").toString();
				if (specifyCol == false)
					colNames[i] = colName;
				colMap.put(colName, colAttr);
			}

			PrintWriter out = response.getWriter();
			String tabHeadStr = "";
			String csvHeadStr = "";

			//输出表头
			tabHeadStr = ("<table   cellpadding='1' cellspacing='1' border='1' " +
					"  align=center  id='" + tabId + "_privTable_head  style='background-color:" +
					Common.getLinkMapVal(rptTab, "table.backGroundColor.value") + ";'>"); //表,表头,行,交替行
			tabHeadStr += ("<tr id='" + tabId + "_ShowTableHead'  style='position:relative;background-color:" +
					Common.getLinkMapVal(rptTab, "table.tabHeadBackColor.value") + ";color:"
					+ Common.getLinkMapVal(rptTab, "table.tabHeadForeColor.value") + ";'>");
			String colNameCn[] = new String[colNames.length];
			//数据源字段属性
			Map<String, Object>[] colAttrs = (Map<String, Object>[]) Array.newInstance(rptTab.getClass(), colNames.length);
			Map<String, Object>[] colPros = (Map<String, Object>[]) Array.newInstance(rptTab.getClass(), colNames.length);
			String colDisplayAttrs[] = new String[colNames.length];
			boolean colCodeFlag[] = new boolean[colNames.length];//预先查询出编码转换的SQL值到HashMap中
			Hashtable[] transCodes = new Hashtable[colNames.length];
			int srcIndex[] = new int[colNames.length];
			ReportDesignerAction rptDesignerAction = new ReportDesignerAction();
			DataAccess access = new DataAccess(rptDao.getConnection(dataSrcName));
			access.setQueryTimeout(600);
			String transCodeSql[] = new String[colNames.length];
			for (int i = 0; i < colNames.length; i++)
			{
				String colName = colNames[i];
				colAttrs[i] = colMap.get(colName);
				colPros[i] = selColumns.get(colName);
				colDisplayAttrs[i] = "";
				String width = Common.getLinkMapVal(colPros[i], "width.value").toString();
				if (!width.equals("") && !width.equals("0"))
					colDisplayAttrs[i] += "width:" + width + "px;";
				String backColor = Common.getLinkMapVal(colPros[i], "backColor.value").toString();
				if (!backColor.equals(""))
					colDisplayAttrs[i] += "background-color:" + backColor;
				String nameCn = Common.getLinkMapVal(colAttrs[i], "nameCn.value").toString();
				tabHeadStr += ("<td nowrap align=center id='" + tabId + "_headTd_" + i
						+ "' style='position:relative;padding-left:0px;padding-right:0px;" + colDisplayAttrs[i] + "'>" +
						(!nameCn.equals("") ? nameCn : colName) + "</td>");

				csvHeadStr += (!nameCn.equals("") ? nameCn : colName) + ",";
				srcIndex[i] = Convert.toInt(Common.getLinkMapVal(colAttrs[i], "srcIndex"));

				transCodeSql[i] = Common.getLinkMapVal(colAttrs[i], "dimCodeTransSql.value").toString();
				if (transCodeSql == null || transCodeSql[i].equals(""))
					colCodeFlag[i] = false;
				else
					colCodeFlag[i] = true;
				if (colCodeFlag[i])
				{
					transCodes[i] = new Hashtable();
					if (transCodeSql[i].trim().substring(0, 6).toUpperCase().equals("SELECT"))
					{
						String _transCodeSql = replaceMacro(transCodeSql[i], params);
						Object[][] codes = new DataAccess(rptDao.getConnection()).queryForArray(_transCodeSql, false);
						for (int n = 0; n < codes.length; n++)
						{
							if (codes[n][0] != null && codes[n][1] != null)
								transCodes[i].put(codes[n][0].toString(), codes[n][1].toString());
						}
					}
					else
					{
						String[] codes = transCodeSql[i].split(";");
						for (int n = 0; n < codes.length; n++)
						{
							String[] codeTmp = codes[i].split(":");
							if (codeTmp.length == 2)
								transCodes[i].put(codeTmp[0], codeTmp[1]);
						}
					}
				}
			}
			tabHeadStr += ("</tr>");
			csvHeadStr += "\n";
			Integer rowCount = 0;
			String rowBackColor[] = new String[2];
			rowBackColor[0] = Common.getLinkMapVal(rptTab, "table.rowBackColor.value").toString();
			rowBackColor[1] = Common.getLinkMapVal(rptTab, "table.rowAlternantBackColor.value").toString();
			System.out.println(dsql);
			rs = access.execQuerySql(dsql);

			if (outType == 0)
				out.print(csvHeadStr);
			else if (outType == 1)
				out.println(tabHeadStr);
			int DUCK_LEVEL = 0;
			String lastDimCode = "";
			Map<String, Object> duckDimColAttr = colAttrs[0];
			Object duckObj = duckDimColAttr.get("DIM_DATA_LEVELS");
			String[] duckDimLevels = duckObj == null ? new String[0] : duckObj.toString().split(",");
			while (rs.next())
			{
				String dimCode = "";
				if (duckFlag)
				{
					dimCode = rs.getObject(srcIndex[0] + 1).toString();
					if (!lastDimCode.equals(dimCode) && !lastDimCode.equals("")) //多维度下载 取上一条钻取
					{
						boolean haveDuck = true;
						if (duckInitLevel == 1)
							DUCK_LEVEL = 1;

						if (1 == 0)//获取当前查询层级  通过条件与查询字段组合判断
						{
							DUCK_LEVEL = 1;//获取当前查询层级
							if (duckDimLevels.length > 0 && DUCK_LEVEL >= Convert.toInt(duckDimLevels[duckDimLevels.length - 1]))
								haveDuck = false;
						}
						else
						{
							if (DUCK_LEVEL >= duckDimLevels.length)
								haveDuck = false;
						}
						//判断维度编码权限
						if (haveDuck)
						{
							params.put(colNames[0], lastDimCode);
							//					rowCount +=;
							this.readDuckData(access, duckSQL, params, duckLevel, DUCK_LEVEL, duckDimLevels, out, outType,
									rowCount,
									colNames, srcIndex, colCodeFlag, transCodes, transCodeSql, rowBackColor, colDisplayAttrs);

						}
					}
					lastDimCode = dimCode;
				}
				if (outType == 1)
					out.println("<tr  style='background-color:" + rowBackColor[rowCount % 2] + "'>"); //tabHeadBackColor
				for (int i = 0; i < colNames.length; i++)
				{
					int si = srcIndex[i];
					Object data = rs.getObject(si + 1);
					if (data == null)
						data = "";
					if (colCodeFlag[i])
					{
						Object name = transCodes[i].get(data);
						if (name != null && !name.equals(""))
							data = name.toString();
					}
					if (outType != 0)
						out.println("<td    style='" + colDisplayAttrs[i] + "'> " + data + " </td>");
					else
						out.print(data + ",");
				}

				if (outType == 1)
					out.println("</tr>");
				else if (outType == 0)
					out.print("\n");
				rowCount++;
				if (rowCount % 100 == 0)
					out.flush();
			}
			if (outType == 1)
				out.println("</table>");
			access.close(rs);
			rs = null;
			if (duckFlag)
			{
				if (!lastDimCode.equals("")) //多维度下载 取上一条钻取
				{
					boolean haveDuck = true;
					if (duckInitLevel == 1)
						DUCK_LEVEL = 1;
					if (1 == 0)//获取当前查询层级  通过条件与查询字段组合判断
					{
						DUCK_LEVEL = 1;//获取当前查询层级
						if (duckDimLevels.length > 0 && DUCK_LEVEL >= Convert.toInt(duckDimLevels[duckDimLevels.length - 1]))
							haveDuck = false;
					}
					else
					{
						if (DUCK_LEVEL >= duckDimLevels.length)
							haveDuck = false;
					}
					//判断维度编码权限
					if (haveDuck)
					{
						params.put(colNames[0], lastDimCode);
						//					rowCount +=;
						this.readDuckData(access, duckSQL, params, duckLevel, DUCK_LEVEL, duckDimLevels, out, outType, rowCount,
								colNames, srcIndex, colCodeFlag, transCodes, transCodeSql, rowBackColor, colDisplayAttrs);
					}
				}
			}
			return new String[] { rptTitle, "" };
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			throw ex;
		}
		finally
		{
			if (rs != null)
			{
				Statement statement = rs.getStatement();
				if (statement != null)
					statement.close();
				rs.close();
			}
			rptDao.close();
		}
	}

	private void readDuckData(DataAccess access, String duckSQL, Hashtable<String, String> params, int duckLevel, int DUCK_LEVEL,
			String[] duckDimLevels, PrintWriter out, int outType, Integer rowCount, String[] colNames, int[] srcIndex,
			boolean[] colCodeFlag,
			Hashtable[] _transCodes, String[] transCodeSql, String[] rowBackColor, String[] colDisplayAttrs)
			throws Exception
	{
		ResultSet rs = null;
		try
		{
			duckLevel++;
			DUCK_LEVEL++;
			Hashtable[] transCodes = new Hashtable[colNames.length];
			for (int i = 0; i < colNames.length; i++)
			{
				if (colCodeFlag[i])
				{
					transCodes[i] = _transCodes[i];
					if (transCodeSql[i].trim().substring(0, 6).toUpperCase().equals("SELECT")
							&& transCodeSql[i].indexOf("{" + colNames[0] + "}") > 0)
					{
						transCodes[i] = new Hashtable();
						String _transCodeSql = replaceMacro(transCodeSql[i], params);
						Object[][] codes = new DataAccess(rptDao.getConnection()).queryForArray(_transCodeSql, false);
						for (int n = 0; n < codes.length; n++)
						{
							if (codes[n][0] != null && codes[n][1] != null)
								transCodes[i].put(codes[n][0].toString(), codes[n][1].toString());
						}
					}
				}
			}
			String indentStr = "";
			for (int i = 1; i < duckLevel; i++)
				indentStr += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			String dsql = replaceMacro(duckSQL, params);
			rs = access.execQuerySql(dsql);
			//		int rowLen = 0;
			String lastDimCode = "";
			while (rs.next())
			{
				String dimCode = "";
				dimCode = rs.getObject(srcIndex[0] + 1).toString();
				if (!lastDimCode.equals(dimCode) && !lastDimCode.equals("")) //多维度下载 取上一条钻取
				{
					boolean haveDuck = true;
					if (duckInitLevel == 1)
						DUCK_LEVEL = 1;
					if (1 == 0)//获取当前查询层级  通过条件与查询字段组合判断
					{
						DUCK_LEVEL = 1;//获取当前查询层级
						if (duckDimLevels.length > 0 && DUCK_LEVEL >= Convert.toInt(duckDimLevels[duckDimLevels.length - 1]))
							haveDuck = false;
					}
					else
					{
						if (DUCK_LEVEL >= duckDimLevels.length)
							haveDuck = false;
					}
					//判断维度编码权限
					if (haveDuck)
					{
						params.put(colNames[0], lastDimCode);
						this.readDuckData(access, duckSQL, params, duckLevel, DUCK_LEVEL, duckDimLevels, out, outType, rowCount,
								colNames, srcIndex, colCodeFlag, transCodes, transCodeSql, rowBackColor, colDisplayAttrs);
					}
				}
				lastDimCode = dimCode;
				if (outType == 1)
					out.println("<tr  style='background-color:" + rowBackColor[rowCount % 2] + "'>"); //tabHeadBackColor
				for (int i = 0; i < colNames.length; i++)
				{
					int si = srcIndex[i];
					Object data = rs.getObject(si + 1);
					if (data == null)
						data = "";
					if (colCodeFlag[i])
					{
						Object name = transCodes[i].get(data);
						if (name != null && !name.equals(""))
							data = name.toString();
					}
					if (outType != 0)
						out.println("<td    style='" + colDisplayAttrs[i] + "'> " + (i == 0 ? indentStr : "") + data + " </td>");
					else
						out.print(data + ",");
				}

				if (outType == 1)
					out.println("</tr>");
				else if (outType == 0)
					out.print("\n");
				rowCount++;
				if (rowCount % 100 == 0)
					out.flush();
			}
			access.close(rs);
			rs = null;
			if (!lastDimCode.equals("")) //多维度下载 取上一条钻取
			{
				boolean haveDuck = true;
				if (duckInitLevel == 1)
					DUCK_LEVEL = 1;
				if (1 == 0)//获取当前查询层级  通过条件与查询字段组合判断
				{
					DUCK_LEVEL = 1;//获取当前查询层级
					if (duckDimLevels.length > 0 && DUCK_LEVEL >= Convert.toInt(duckDimLevels[duckDimLevels.length - 1]))
						haveDuck = false;
				}
				else
				{
					if (DUCK_LEVEL >= duckDimLevels.length)
						haveDuck = false;
				}
				//判断维度编码权限
				if (haveDuck)
				{
					params.put(colNames[0], lastDimCode);
					this.readDuckData(access, duckSQL, params, duckLevel, DUCK_LEVEL, duckDimLevels, out, outType, rowCount,
							colNames, srcIndex, colCodeFlag, transCodes, transCodeSql, rowBackColor, colDisplayAttrs);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			if (rs != null)
			{
				access.close(rs);
			}
		}
		//		return rowLen;
	}

	public String replaceMacro(String dsql, Hashtable<String, String> params) throws Exception
	{
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
				dsql = ReportDesignerAction.delSQLTermFromKey(dsql, key);
			}
		}
		if (dsql.indexOf("{") > 0)
		{
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
					dsql = ReportDesignerAction.delSQLTermFromKey(dsql, varName);
				}
			}
		}
		return dsql;
	}

	public ReportDesignerDao getRptDao()
	{
		return rptDao;
	}

	public void setRptDao(ReportDesignerDao rptDao)
	{
		this.rptDao = rptDao;
	}
}
