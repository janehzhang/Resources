package tydic.meta.rpt;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.BinaryStream;
import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.DataTable;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.Common;

public class ReportDesignerDao extends BaseDAO
{
	public long qrySeqValue(String seqName)
	{
		DataAccess access = getDataAccess();
		String sql = "SELECT " + seqName +
				".nextval seq FROM dual";
		long l = access.queryForLong(sql);
		return l;
	}

	public void deleteRptInfo(long rptId)
	{
		String sql = "DELETE META_REPORT r where r.report_id=" + rptId;
		this.getDataAccess().execUpdate(sql);
		sql = "DELETE meta_report_data r where r.report_id=" + rptId;
		this.getDataAccess().execUpdate(sql);
		sql = "DELETE meta_report_data_cols r where r.report_id=" + rptId;
		this.getDataAccess().execUpdate(sql);
		sql = "DELETE meta_report_module r where r.report_id=" + rptId;
		this.getDataAccess().execUpdate(sql);
		sql = "DELETE meta_report_module_tab r where r.report_id=" + rptId;
		this.getDataAccess().execUpdate(sql);
		sql = "DELETE meta_report_query_term r where r.report_id=" + rptId;
		this.getDataAccess().execUpdate(sql);
	}

	public void saveRpt(Hashtable<String, String> rptCfm,
			final Hashtable<String, HashMap<String, String>> termCfm,
			final Hashtable<String, HashMap<String, String>> dataCfm,
			final Hashtable<String, List<HashMap<String, String>>> dataCfmCols,
			final Hashtable<String, HashMap<String, String>> moduleCfm,
			final Hashtable<String, List<HashMap<String, String>>> tabCfm) throws Exception
	{
		String sql = "insert into META_REPORT(REPORT_ID,REPORT_NAME,SHOW_NAME_FLAG,REPORT_DESC,USER_ID," +
				" QUERY_TERM_IDS,TERM_ROW_SIZE,USER_DEFINE_CSS,STATE,CLIENT_JS,REPORT_TITLE_CFG) values(?,?,?,?,?,?,?,?,1,?,?)";
		BinaryStream js = new BinaryStream();
		String cjs = rptCfm.get("CLIENT_JS");
		byte[] bytes = cjs.getBytes();
		java.io.ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		js.setInputStream(bais, bytes.length);
		final long REPORT_ID = Convert.toLong(rptCfm.get("REPORT_ID"));
		this.getDataAccess().execUpdate(sql, REPORT_ID
				, rptCfm.get("REPORT_NAME")
				, rptCfm.get("SHOW_NAME_FLAG")
				, rptCfm.get("REPORT_DESC")
				, rptCfm.get("USER_ID")
				, rptCfm.get("QUERY_TERM_IDS")
				, rptCfm.get("TERM_ROW_SIZE")
				, rptCfm.get("USER_DEFINE_CSS")
				, js
				, rptCfm.get("REPORT_TITLE_CFG")
				);
		sql = " INSERT INTO META_REPORT_QUERY_TERM" +
				" (QUERY_TERM_ID,PARANT_TERM_ID,REPORT_ID,TERM_LABEL,TERM_TYPE" +
				",VALUE_TYPE,VALUE_COLNAME,TEXT_COLNAME,SHOW_LENGTH,SRC_TYPE" +
				",QUERY_DATA,DATA_SOURCE_ID,DEFAULT_VALUE,APPEND_VALUE,APPEND_TEXT" +
				",DIM_TABLE_NAME,DIM_TABLE_ID,DIM_TYPE_ID,DIM_DATA_LEVELS)" +
				" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		final Enumeration<String> termEnum = termCfm.keys();
		this.getDataAccess().execUpdateBatch(sql, new IParamsSetter()
		{
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException
			{
				String key = termEnum.nextElement();
				HashMap<String, String> term = termCfm.get(key);

				preparedStatement.setObject(1, term.get("termId"));
				preparedStatement.setObject(2, term.get("PARANT_TERM_ID"));
				preparedStatement.setObject(3, REPORT_ID);
				preparedStatement.setObject(4, term.get("TERM_LABEL"));
				preparedStatement.setObject(5, term.get("TERM_TYPE"));
				preparedStatement.setObject(6, term.get("VALUE_TYPE"));
				preparedStatement.setObject(7, term.get("VALUE_COLNAME"));
				preparedStatement.setObject(8, term.get("TEXT_COLNAME"));
				preparedStatement.setObject(9, term.get("SHOW_LENGTH"));
				preparedStatement.setObject(10, term.get("SRC_TYPE"));
				preparedStatement.setObject(11, term.get("QUERY_DATA"));
				preparedStatement.setObject(12, term.get("DATA_SOURCE_ID"));
				preparedStatement.setObject(13, term.get("DEFAULT_VALUE"));
				preparedStatement.setObject(14, term.get("APPEND_VALUE"));
				preparedStatement.setObject(15, term.get("APPEND_TEXT"));
				preparedStatement.setObject(16, term.get("DIM_TABLE_NAME"));
				preparedStatement.setObject(17, term.get("DIM_TABLE_ID"));
				preparedStatement.setObject(18, term.get("DIM_TYPE_ID"));
				preparedStatement.setObject(19, term.get("DIM_DATA_LEVELS"));
			}

			public int batchSize()
			{
				return termCfm.size();
			}
		});

		sql = " INSERT INTO META_REPORT_DATA" +
						" (RPT_DATA_ID,REPORT_ID,DATA_NAME,TABLE_ID,TABLE_NAME" +
						",DATA_SRC_TYPE,DATA_SOURCE_ID,DATA_QUERY_SQL,APPEND_GDLDESC)" +
						"VALUES(?,?,?,?,?,?,?,?,? )";
		final Enumeration<String> dataEnum = dataCfm.keys();
		this.getDataAccess().execUpdateBatch(sql, new IParamsSetter()
			{
				public void setValues(PreparedStatement preparedStatement, int i) throws SQLException
				{
					String key = dataEnum.nextElement();
					HashMap<String, String> data = dataCfm.get(key);

					preparedStatement.setObject(1, data.get("dataId"));
					preparedStatement.setObject(2, REPORT_ID);
					preparedStatement.setObject(3, data.get("DATA_NAME"));
					preparedStatement.setObject(4, data.get("TABLE_ID"));
					preparedStatement.setObject(5, data.get("TABLE_NAME"));
					preparedStatement.setObject(6, data.get("DATA_SRC_TYPE"));
					preparedStatement.setObject(7, data.get("DATA_SOURCE_ID"));
					preparedStatement.setObject(8, data.get("DATA_QUERY_SQL"));
					preparedStatement.setObject(9, data.get("APPEND_GDLDESC"));
				}

				public int batchSize()
				{
					return dataCfm.size();
				}
			});
		sql = "  INSERT INTO META_REPORT_DATA_COLS" +
				" (RPT_DATA_COL_ID,REPORT_ID,RPT_DATA_ID,COL_NAME,COL_NAME_CN" +
				",COL_BUS_TYPE,DIM_TABLE_ID,DIM_TYPE_ID,DIM_DATA_LEVELS,COL_BUS_COMMENT" +
				",GDL_ID,TABLE_ID,COL_ID,GROUP_METHOD)" +
				"VALUES(?,?,?,?,? ,?,?,?,?,? ,?,?,?,? )";
		final Enumeration<String> dataColEnum = dataCfmCols.keys();
		this.getDataAccess().execUpdateBatch(sql, new IParamsSetter()
		{
			public void setValues(PreparedStatement preparedStatement, int ii) throws SQLException
			{
				String key = dataColEnum.nextElement();
				List<HashMap<String, String>> dataCols = dataCfmCols.get(key);
				int len = dataCols.size();
				for (int i = 0; i < len; i++)
				{
					HashMap<String, String> col = dataCols.get(i);
					preparedStatement.setObject(1, col.get("dataColId"));
					preparedStatement.setObject(2, REPORT_ID);
					preparedStatement.setObject(3, col.get("RPT_DATA_ID"));
					preparedStatement.setObject(4, col.get("COL_NAME"));
					preparedStatement.setObject(5, col.get("COL_NAME_CN"));
					preparedStatement.setObject(6, col.get("COL_BUS_TYPE"));
					preparedStatement.setObject(7, col.get("DIM_TABLE_ID"));
					preparedStatement.setObject(8, col.get("DIM_TYPE_ID"));
					preparedStatement.setObject(9, col.get("DIM_DATA_LEVELS"));
					preparedStatement.setObject(10, col.get("COL_BUS_COMMENT"));
					preparedStatement.setObject(11, col.get("GDL_ID"));
					preparedStatement.setObject(12, col.get("TABLE_ID"));
					preparedStatement.setObject(13, col.get("COL_ID"));
					preparedStatement.setObject(14, col.get("GROUP_METHOD"));
					if (i < len - 1)
						preparedStatement.addBatch();
				}
			}

			public int batchSize()
			{
				return dataCfmCols.size();
			}
		});

		////////
		sql = "  INSERT INTO META_REPORT_MODULE (MODULE_ID,REPORT_ID,MODULE_TITLE,WINDOW_SHOW_PARAMS,STATE,MUL_PANNEL)" +
				"VALUES(?,?,?,?,1 ,?  )";
		final Enumeration<String> modEnum = moduleCfm.keys();
		this.getDataAccess().execUpdateBatch(sql, new IParamsSetter()
		{
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException
			{
				String key = modEnum.nextElement();
				HashMap<String, String> data = moduleCfm.get(key);

				preparedStatement.setObject(1, data.get("moduleId"));
				preparedStatement.setObject(2, REPORT_ID);
				preparedStatement.setObject(3, data.get("MODULE_TITLE"));
				preparedStatement.setObject(4, data.get("WINDOW_SHOW_PARAMS"));
				preparedStatement.setObject(5, data.get("MUL_PANNEL"));
			}

			public int batchSize()
			{
				return moduleCfm.size();
			}
		});
		sql = "  INSERT INTO META_REPORT_MODULE_TAB" +
				" (MODULE_TAB_ID,REPORT_ID,RPT_DATA_ID,MODULE_ID,TAB_NAME" +
				",DATA_SRC_NAME,SHOW_TYPE,SHOW_FLAG,QUERY_TERM_IDS,TERM_ROW_SIZE" +
				",DOWNLOAD_FLAG,ORDER_ID,LINK_RPT_ID,LINK_OPEN_TYPE,LINK_MODULE_IDS" +
				",LINK_TYPE,IN_PARAMS,OUT_PARAMS, TABLE_SHOW_COLS,TABLE_DATA_TRANS_RULE" +
				",TABLE_WIDTH,TABLE_HEIGHT,TABLE_COLORS,TABLE_CSSNAMES,TABLE_COL_CONFIGS" +
				",DUCK_COLNAME,ROW_UNITE_COLS,COL_UNITE_RULE,GRAPH_TYPE,GRAPH_PARAMS" +
				",GRAPH_SHOW_COLS,GRAPH_DATA_TRANS_RULE,STATE)" +
				"VALUES(?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,1)";
		final Enumeration<String> tabEnum = tabCfm.keys();
		this.getDataAccess().execUpdateBatch(sql, new IParamsSetter()
		{
			public void setValues(PreparedStatement preparedStatement, int ii) throws SQLException
			{
				String key = tabEnum.nextElement();
				List<HashMap<String, String>> dataCols = tabCfm.get(key);
				int len = dataCols.size();
				for (int i = 0; i < len; i++)
				{
					HashMap<String, String> col = dataCols.get(i);
					int c = 1;
					preparedStatement.setObject(c++, col.get("moduleTabId"));
					preparedStatement.setObject(c++, REPORT_ID);
					preparedStatement.setObject(c++, col.get("RPT_DATA_ID"));
					preparedStatement.setObject(c++, col.get("MODULE_ID"));
					preparedStatement.setObject(c++, col.get("TAB_NAME"));

					preparedStatement.setObject(c++, col.get("DATA_SRC_NAME"));
					preparedStatement.setObject(c++, col.get("SHOW_TYPE"));
					preparedStatement.setObject(c++, col.get("SHOW_FLAG"));
					preparedStatement.setObject(c++, col.get("QUERY_TERM_IDS"));
					preparedStatement.setObject(c++, col.get("TERM_ROW_SIZE"));

					preparedStatement.setObject(c++, col.get("DOWNLOAD_FLAG"));
					preparedStatement.setObject(c++, col.get("ORDER_ID"));
					preparedStatement.setObject(c++, col.get("LINK_RPT_ID"));
					preparedStatement.setObject(c++, col.get("LINK_OPEN_TYPE"));
					preparedStatement.setObject(c++, col.get("LINK_MODULE_IDS"));

					preparedStatement.setObject(c++, col.get("LINK_TYPE"));
					preparedStatement.setObject(c++, col.get("IN_PARAMS"));
					preparedStatement.setObject(c++, col.get("OUT_PARAMS"));
					preparedStatement.setObject(c++, col.get("TABLE_SHOW_COLS"));
					preparedStatement.setObject(c++, col.get("TABLE_DATA_TRANS_RULE"));

					preparedStatement.setObject(c++, col.get("TABLE_WIDTH"));
					preparedStatement.setObject(c++, col.get("TABLE_HEIGHT"));
					preparedStatement.setObject(c++, col.get("TABLE_COLORS"));
					preparedStatement.setObject(c++, col.get("TABLE_CSSNAMES"));
					String cjs = col.get("TABLE_COL_CONFIGS");
					byte[] bytes = cjs.getBytes();
					java.io.ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
					preparedStatement.setBinaryStream(c++, bais, bytes.length);

					preparedStatement.setObject(c++, col.get("DUCK_COLNAME"));
					preparedStatement.setObject(c++, col.get("ROW_UNITE_COLS"));
					preparedStatement.setObject(c++, col.get("COL_UNITE_RULE"));
					preparedStatement.setObject(c++, col.get("GRAPH_TYPE"));
					preparedStatement.setObject(c++, col.get("GRAPH_PARAMS"));
					preparedStatement.setObject(c++, col.get("GRAPH_SHOW_COLS"));
					preparedStatement.setObject(c++, col.get("GRAPH_DATA_TRANS_RULE"));

					if (i < len - 1)
						preparedStatement.addBatch();
				}
			}

			public int batchSize()
			{
				return tabCfm.size();
			}
		});

	}

	public String[] readRptJsCfg(long rptId)
	{
		try
		{
			DataAccess access = getDataAccess();
			String sql = "select t.client_js from META_REPORT t where t.report_id=" + rptId;
			ResultSet rs = access.execQuerySql(sql);
			if (rs.next() == false)
			{
				throw new Exception("未读取到报表ID为[" + rptId + "]的配置");
			}
			InputStreamReader in = new InputStreamReader(rs.getBlob(1).getBinaryStream());
			BufferedReader bin = new BufferedReader(in);
			String res = "";
			while (true)
			{
				String tmp = bin.readLine();
				if (tmp == null)
					break;
				res += tmp;
			}
			bin.close();
			rs.close();
			return new String[] { "true", res };
		}
		catch (Exception ex)
		{
			return new String[] { "false", ex.getMessage() };
		}
	}

	/**
	 * 从模型配置报表中读取配置
	 * 
	 * @param rptId
	 * @return
	 */
	public Object[] readRptInitCfgFromMod(long rptId)
	{
		try
		{
			DataAccess access = getDataAccess();
			String sql = "SELECT T.REPORT_ID,T.USER_ID,T.REPORT_NAME,T.REPORT_SQL,T.REPORT_NOTE" +
					",T.ISSUE_ID,T.IS_LISTING,T.REPORT_TRANS_CODE_SQL,T.DATA_SOURCE_ID,T.DATA_TRANS_DIMS," +
					"T.DATA_TRANS_GDLS,T.DATA_TRANS_GDL_COLNAME,M.TABLE_ID,A.TABLE_NAME,A.TABLE_NAME_CN," +
					"T.DIM_DUCK_TERM_SQL " +
					" FROM META_RPT_TAB_REPORT_CFG T,META_RPT_MODEL_ISSUE_CONFIG M,META_TABLES A" +
					" WHERE  M.ISSUE_ID=T.ISSUE_ID AND M.TABLE_ID =A.TABLE_ID" +
					" AND A.TABLE_STATE=1 AND T.REPORT_STATE IN(-1,1) AND T.REPORT_ID=?";
			Map<String, Object> rptCfm = access.queryForMap(sql, rptId);
			if (rptCfm == null)
				throw new Exception("未查询到报表ID为的" + rptId + "配置");
			Common.replaceNullOfMap(rptCfm);
			sql = "SELECT T.QUERY_ID,T.QUERY_CONTROL,T.QUERY_DEFAULT,T.DIM_LEVELS,T.DIM_CODE_FILTER," +
					"T.REPORT_ID,T.COL_ID,T.COLUMN_ID,T.DIM_TYPE_ID,T.DIM_TABLE_ID," +
					" LABEL_NAME ,A.DATA_SOURCE_ID,A.TABLE_NAME,A.TABLE_DIM_PREFIX,NVL((" +
					" SELECT 1 FROM META_TABLE_COLS C WHERE C.TABLE_ID=T.DIM_TABLE_ID" +
					" AND C.COL_STATE=1 AND C.COL_NAME LIKE '%par_code'),0) have_pAR_cODE" +
					",C.COL_NAME,C.COL_NAME_CN,C.COL_DATATYPE,nvl((select ts.table_records from" +
					" meta_table_inst ts where ts.table_name=a.table_name and ts.state=1),-1) DIM_ROW_COUNTS" +
					//" FROM META_RPT_TAB_QUERY_CFG T,META_DIM_TABLES A,META_TABLE_COLS C  WHERE A.DIM_TABLE_ID=T.DIM_TABLE_ID" +
					" FROM META_RPT_TAB_QUERY_CFG T left join META_DIM_TABLES a on a.dim_table_id = t.dim_table_id,META_TABLE_COLS C" +
					" where C.COL_ID=T.COL_ID AND C.COL_STATE=1 AND  T.REPORT_ID=? ORDER BY T.QUERY_ORDER ";
			Map<String, Object>[] rptQrys = access.queryForArray(sql, rptId);
			for (int i = 0; i < rptQrys.length; i++)
				Common.replaceNullOfMap(rptQrys[i]);
			sql = "SELECT T.OUTPUT_ID,T.REPORT_ID,T.OUTPUT_EXPRESS,T.DIM_LEVELS,T.DIM_CODE_FILTER," +
					"T.TOTAL_FLAG,T.COL_ID,T.COLUMN_ID,T.DUCK_FLAG,T.TRANS_CODE," +
					"T.COLUMN_NAME,T.DIM_TYPE_ID,T.DIM_TABLE_ID,T.COLLECT_MOTHED, A.COL_BUS_TYPE," +
					"C.COL_DATATYPE,C.COL_BUS_COMMENT,T.TOTAL_DISPLAY,T.SELECT_NAME,C.TABLE_ID," +
					"T.DUCK_FLAG" +
					" FROM META_RPT_TAB_OUTPUT_CFG T " +
                    " LEFT JOIN META_TABLE_COLS C ON T.COL_ID = C.COL_ID AND C.COL_STATE = 1 " +
                    " LEFT JOIN META_RPT_MODEL_ISSUE_COLS A ON A.COLUMN_ID = T.COLUMN_ID" +
					" WHERE T.REPORT_ID=? ORDER BY T.OUTPUT_ORDER";
			System.out.println(sql);
			Map<String, Object>[] rptOutCols = access.queryForArray(sql, rptId);
			for (int i = 0; i < rptOutCols.length; i++)
				Common.replaceNullOfMap(rptOutCols[i]);
			return new Object[] { rptCfm, rptQrys, rptOutCols };
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new String[] { "false", ex.getMessage() };
		}
	}

	//从模型配置报表中读取配置
	public Object[] readRptInitCfgFromGdl(long rptId)
	{
		try
		{

			return new Object[] { "true" };
		}
		catch (Exception ex)
		{
			return new String[] { "false", ex.getMessage() };
		}
	}

	/**
	 *读取数据库数据源列表
	 * 
	 * @return
	 */
	public Object[][] readDataSource()
	{
		DataAccess access = getDataAccess();
		String sql = "select t.data_source_id,t.data_source_name " +
				" from meta_data_source t where upper(t.data_source_type) like '%TABLE%'";
		DataTable table = access.queryForDataTable(sql);

		return table.rows;
	}

	/**
	 * 获取指定SQL的查询字段名称列表
	 * 
	 * @param dataName
	 * @param dsql
	 * @return
	 */
	public String[] qryDataCol(String dataName, String dsql)
	{
		DataAccess access = getDataAccess(dataName);
		String sql = "select * from (" + dsql + ") where rownum<1";
		DataTable table = access.queryForDataTable(sql);

		return table.colsName;
	}

	/**
	 * 通过指标字段名称列表获取指标解释
	 * 
	 * @param colNames 指标字段名称列表
	 * @return
	 */
	public DataTable qryGdlByColNames(String[] colNames)
	{
		String colNameIns = Common.join(colNames, "','").toUpperCase();
		String sql = "select t.gdl_col_name,t.gdl_name,t.gdl_bus_desc,t.gdl_unit from meta_guild_line t  " +
				" where t.gdl_col_name in('" + colNameIns + "')";
		DataAccess access = getDataAccess();
		DataTable table = access.queryForDataTable(sql);
		return table;
	}

	/**
	 * 通过指标名称列表从指标表获取指标解释
	 * 
	 * @param colNames 指标名称列表
	 * @return
	 */
	public DataTable qryGdlByGdlNames(String[] colNames)
	{
		String colNameIns = Common.join(colNames, "','");
		String sql = "select t.gdl_col_name,t.gdl_name,t.gdl_bus_desc,t.gdl_unit from meta_guild_line t  " +
				" where t.gdl_name in('" + colNameIns + "')";
		DataAccess access = getDataAccess();
		DataTable table = access.queryForDataTable(sql);
		return table;
	}

	/**
	 * 获取指定表类ID的列指标解释
	 * 
	 * @param tableId
	 * @return
	 */
	public DataTable qryMetaTableGdl(long tableId)
	{
		return qryMetaTableGdl(tableId, null);
	}

	/**
	 * 获取指定表类ID的列指标解释
	 * 
	 * @param tableId
	 * @param colName
	 * @return
	 */
	public DataTable qryMetaTableGdl(long tableId, String colName)
	{
		String sql = "select nvl(t.gdl_col_name,a.col_name) gdl_col_name," +
				" nvl(t.gdl_name,a.col_name_cn)gdl_name ," +
				" nvl(t.gdl_bus_desc,a.col_bus_comment) gdl_bus_desc ," +
				" t.gdl_unit " +
				"   from meta_guild_line t,meta_table_cols a " +
				" where t.gdl_col_name(+)=a. col_name and a.table_id=" + tableId + " and  a.col_state=1 ";
		if (colName != null && !colName.equals(""))
			sql += " and a.col_name='" + colName.toUpperCase() + "'";
		DataAccess access = getDataAccess();
		DataTable table = access.queryForDataTable(sql);
		return table;
	}

	/**
	 * 查询数据
	 * 
	 * @param dataSrcName
	 * @param dsql
	 * @return
	 */
	public DataTable qryData(String dataSrcName, String dsql, int rowCount)
	{
		DataAccess access = getDataAccess(dataSrcName);
		if (rowCount > 0)
			dsql = "select * from (" + dsql + ") where rownum<" + (rowCount + 1);
		DataTable table = access.queryForDataTable(dsql);
		return table;
	}

	public DataTable qryData(String dataSrcName, String dsql, String orderBy, int rowCount, int CurPageNum)
	{
		DataAccess access = null;
		if (dataSrcName == null || dataSrcName.trim().equals(""))
			access = getDataAccess();
		else
			access = getDataAccess(dataSrcName);
		access.setQueryTimeout(15);//报表15秒不能返回数据
		if (CurPageNum > 0 && rowCount > 0)//分页查询
		{
			String sql = dsql;
			if (!orderBy.equals(""))
				dsql = " select a.* from (" + dsql + ") a order by " + orderBy;
			dsql = "select axa.*,rownum myrownum, (select count(0) from (" + sql + ") ) MYTOTAL_ROWCOUNT" +
					" from (" + dsql + ")axa ";
			dsql = "select * from (" + dsql + ") where myrownum between " +
						((CurPageNum - 1) * rowCount + 1) + " and " + CurPageNum * rowCount;
		}
		else
		{
			if (!orderBy.equals(""))
				dsql = "select * from (" + dsql + ") order by " + orderBy;
			if (rowCount > 0)
				dsql = "select * from (" + dsql + ") where rownum<" + (rowCount + 1);
		}
		DataTable table = access.queryForDataTable(dsql);
		return table;
	}

	/**
	 * 读取报表配置
	 * 
	 * @param rptId
	 * @return
	 */
	public Map<String, Object> qryRpt(long rptId)
	{
		DataAccess access = getDataAccess();
		String sql = "select t.report_id,t.report_name,t.show_name_flag,t.report_desc,t.user_id," +
				"t.query_term_ids,t.term_row_size,t.user_define_css,report_title_cfg " +
				" from META_REPORT t where t.state=1 and t.report_id=" + rptId;
		Map<String, Object> rptMap = access.queryForMap(sql);
		Common.replaceNullOfMap(rptMap);
		return rptMap;
	}

	/**
	 * 读取报表条件配置
	 * 
	 * @param rptId
	 * @return
	 */
	public Map<String, Object>[] qryRptTerms(long rptId)
	{
		String sql = "select t.query_term_id,t.parant_term_id,t.report_id,t.term_label,t.term_type" +
				",t.value_type,t.value_colname,t.text_colname,t.show_length,t.src_type" +
				",t.query_data,t.data_source_id,t.default_value,t.append_value,t.append_text" +
				",t.dim_table_name,t.dim_table_id,t.dim_table_id,t.dim_data_levels" +
				" from META_REPORT_QUERY_TERM t where t.report_id=" + rptId;
		DataAccess access = getDataAccess();
		Map<String, Object>[] rptTerms = access.queryForArray(sql);

		for (int i = 0; i < rptTerms.length; i++)
			Common.replaceNullOfMap(rptTerms[i]);
		return rptTerms;
	}

	/**
	 * 报表数据源查询
	 * 
	 * @param rptId
	 * @return
	 */
	public Map<String, Object>[] qryRptDataSrc(long rptId)
	{
		String sql = "select t.rpt_data_id,t.report_id,t.data_name,t.table_id,t.table_name" +
				",t.data_src_type,t.data_source_id,t.data_query_sql,t.data_trans_rule,t.append_gdldesc" +
				" from META_REPORT_DATA t where t.report_id=" + rptId;
		DataAccess access = getDataAccess();
		Map<String, Object>[] rptDataSrcs = access.queryForArray(sql);
		for (int i = 0; i < rptDataSrcs.length; i++)
		{
			Common.replaceNullOfMap(rptDataSrcs[i]);
			//查询字段设置
			sql = "select t.rpt_data_col_id,t.report_id,t.rpt_data_id,t.col_name,t.col_name_cn" +
					",t.col_bus_type,t.dim_table_id,t.dim_type_id,t.dim_data_levels,t.col_bus_comment" +
					",t.gdl_id,t.table_id,t.col_id,t.group_method,t.dim_trans_sql" +
					" from META_REPORT_DATA_COLS t where t.report_id=? and rpt_data_id=?";
			Map<String, Object>[] rptDataSrcCols = access.queryForArray(sql, rptId, rptDataSrcs[i].get("RPT_DATA_ID"));
			for (int j = 0; j < rptDataSrcCols.length; j++)
				Common.replaceNullOfMap(rptDataSrcCols[i]);
			rptDataSrcs[i].put("COLUMNS", rptDataSrcCols);
		}
		return rptDataSrcs;
	}

	/**
	 * 报表模块及页面查询
	 * 
	 * @param rptId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>[] qryRptModule(long rptId) throws Exception
	{
		String sql = "select t.module_id,t.report_id,t.module_title,t.window_show_params,t.mul_pannel " +
				"from META_REPORT_MODULE t where t.state=1 and t.report_id=" + rptId;
		DataAccess access = getDataAccess();
		Map<String, Object>[] rptModules = access.queryForArray(sql);
		for (int i = 0; i < rptModules.length; i++)
		{
			Common.replaceNullOfMap(rptModules[i]);
			//查询字段设置
			sql = "select t.module_tab_id,t.report_id,t.rpt_data_id,t.module_id,t.tab_name" +
					",t.data_src_name,t.show_type,t.show_flag,t.query_term_ids,t.term_row_size" +
					",t.download_flag,t.order_id,t.link_rpt_id,t.link_open_type,t.link_module_ids" +
					",t.link_type,t.in_params,t.out_params,t.table_show_cols,t.table_width" +
					",t.table_height,t.table_colors,t.table_cssnames,t.duck_colname,t.row_unite_cols" +
					",t.col_unite_rule,t.collect_flag,t.graph_type,t.graph_params,t.table_col_configs" +
					" from META_REPORT_MODULE_TAB t where t.state=1 and t.report_id=? and module_id=?";
			Map<String, Object>[] rptModTabs = access.queryForArray(sql, rptId, rptModules[i].get("MODULE_ID"));
			for (int j = 0; j < rptModTabs.length; j++)
			{
				Set<String> ckeys = rptModTabs[j].keySet();
				for (Iterator<String> it = ckeys.iterator(); it.hasNext();)
				{
					String key = it.next();
					if (key.equals("table_col_configs".toUpperCase()))
					{
						java.sql.Blob bg = (Blob) rptModTabs[j].get(key);
						InputStreamReader in = new InputStreamReader(bg.getBinaryStream());
						BufferedReader bin = new BufferedReader(in);
						String res = "";
						while (true)
						{
							String tmp = bin.readLine();
							if (tmp == null)
								break;
							res += tmp;
						}
						bin.close();
						rptModTabs[j].put(key, res);
					}
					if (rptModTabs[j].get(key) == null)
						rptModTabs[j].put(key, "");
				}
			}
			rptModules[i].put("TABS", rptModTabs);
		}
		return rptModules;
	}
}
