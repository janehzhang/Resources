package tydic.meta.common.yhd.sql;

/**
 * @author�����
 * 
 * �ر����ݿ�������
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;

public class SQLUtil {
/**
 * @param conn java.sql.Connection
 */
public static void safelyCloseConnection(Connection conn) {
	if (conn != null) {
		try {
			conn.close();
		} catch(SQLException err) {
		}
	}
}
/**
 * @param rs java.sql.ResultSet
 */
public static void safelyCloseResultSet(ResultSet rs) {
	if (rs != null) {
		try {
			rs.close();
		} catch (SQLException err) {
		}
	}
}
/**
 * @param pstmt java.sql.PreparedStatement
 */
public static void safelyCloseStatement(PreparedStatement pstmt) {
	if (pstmt != null) {
		try {
			pstmt.close();
		} catch (SQLException err) {
		}
	}
}
/**
 * @param stmt java.sql.Statement
 */
public static void safelyCloseStatement(Statement stmt) {
	if (stmt != null) {
		try {
			stmt.close();
		} catch(SQLException err) {
		}
	}
}
/**
 * @param stmt java.sql.CallableStatement
 */
public static void safelyCloseStatement(CallableStatement stmt) {
	if (stmt != null) {
		try {
			stmt.close();
		} catch(SQLException err) {
		}
	}
}
/**
 * @param conn java.sql.Connection
 */
public static void safelyRollBack(Connection conn) {
	if (conn != null) {
		try {
			conn.rollback();
		} catch (SQLException err) {
		}
	}
}

/**
 * @exception java.sql.SQLException �쳣˵����
 */
public static String safelyGetString(ResultSet rs, int index) throws java.sql.SQLException {
	String result = rs.getString(index);
	return (result==null)?"":result;
}

/**
 * 
 * @return java.lang.String
 * @param rs java.sql.ResultSet
 * @param index int
 * @exception java.sql.SQLException �쳣˵����
 */
public static String safelyGetString(ResultSet rs, String col) throws java.sql.SQLException {
	String result = rs.getString(col);
	if (result == null) {
		return "";
	} else {
		return result.trim();
	}
}

/**
 * ����˵�����������resultSet����n��next()������������Щrow��ע�⣬����ԭ��resultSetͣ�ڵ�start��row��<br>
 *			(һ��next��δ���ù�����Ϊ��0��)����ִ����˷�����Ӧͣ���ڵ�start+n��row�ϡ�<br>
 * ���������resultSet ResultSet��������ת��resultSet<br>
 *			n int:Ҫ������row������<br>
 * ����ֵ��boolean �Ƿ�ɹ���true��ʾ�ɹ���false��ʾʧ�ܡ���resultSetΪnull������false,���׳�SQLException��<br>
 *			����false����resultSetʣ�������С��nʱ������false<br>
 * 
 * @return boolean
 * @param resultSet java.sql.ResultSet
 */
public static boolean moveToRow(ResultSet resultSet, int n) {
	if (resultSet == null) {
		return false;
	}
	try{
		for (int i=0; i<n; i++) {
			if (!resultSet.next()) {
				return false;
			}
		}
	} catch (SQLException e) {
		return false;
	}
	return true;
}

/**
 * ����˵�������ݵ�ǰҳ����ÿҳ��������resultsetͣ����Ҫȡ����һҳ�ĵ�һ����ҳ����1��ʼ����<br>
 * 
 * @return boolean
 * @param resultSet java.sql.ResultSet
 * @param currentPage int
 * @param pageSize int
 */
public static boolean pagingResultSet(ResultSet resultSet, int currentPage, int pageSize) {
	if (currentPage < 1 || pageSize <= 0) {
		return false;
	}
	int start = (currentPage - 1) * pageSize + 1;
	return moveToRow(resultSet, start);
}
}
