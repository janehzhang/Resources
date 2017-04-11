package tydic.meta.common.yhd.sql;

/**
 * @author：李春刚
 * 
 * 关闭数据库联接类
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
 * @exception java.sql.SQLException 异常说明。
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
 * @exception java.sql.SQLException 异常说明。
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
 * 功能说明：将传入的resultSet调用n次next()方法。跳过这些row。注意，假设原先resultSet停在第start条row上<br>
 *			(一次next都未调用过定义为第0条)，则执行完此方法后，应停留在第start+n条row上。<br>
 * 输入参数：resultSet ResultSet：用于跳转的resultSet<br>
 *			n int:要跳过的row的条数<br>
 * 返回值：boolean 是否成功。true表示成功，false表示失败。当resultSet为null，返回false,当抛出SQLException，<br>
 *			返回false，当resultSet剩余的条数小于n时，返回false<br>
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
 * 功能说明：根据当前页数和每页条数，将resultset停留在要取的那一页的第一条。页数从1开始计算<br>
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
