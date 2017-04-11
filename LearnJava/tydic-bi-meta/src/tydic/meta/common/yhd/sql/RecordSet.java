/*
 * Created on 2005-9-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tydic.meta.common.yhd.sql;

/**
 * @author ���
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;


public class RecordSet {

	private final int QUERY_STORE_TYPE = 1;
	private final int UPDATE_STORE_TYPE = 2;
	private int rowCount;		
	private static RecordSet recordSet = null;
	private Connection conn = null;
    private CallableStatement cst = null;
	
	public RecordSet(){
		
	}
	
	public static RecordSet getInstance(){
		if(recordSet==null){
			recordSet = new RecordSet();
		}
		return recordSet;
	}
	
	
	
	private void setRowCount(int count){
		rowCount = count;
	}
	
	public int getRowCount(){
		return rowCount;
	}

	
	/*	
	 *ִ�в�ѯ�������
	 */	
	public ResultSet executeQueryStore(String storeName,Vector params)
				throws Exception {
		
        int len = params.size();
        String storeProc = convertStore(QUERY_STORE_TYPE,storeName,len);
        ResultSet rs = null;
        try{
        	conn = ServicePool.getDB2Connection();
			cst = conn.prepareCall(storeProc);
			for(int i=0;i<len;i++){
				cst.setString(i+1,(String)params.get(i));
			}
			boolean Flag = cst.execute();
			if (Flag){
				rs = cst.getResultSet();
			}else{
				throw new Exception("No Result Return");
			}
        }catch(Exception ce){
        	ce.printStackTrace();
        }finally{
        }
        return rs;
	}
	
	
	
	/*
	 * ִ�в�ѯ�洢���̣����ҷ��ذ�������������CallableStatement����
	 */
	public CallableStatement getCallableStatement (String storeName,Vector params)
									throws Exception {
        int len = params.size();
        String storeProc = convertStore(QUERY_STORE_TYPE,storeName,len);
        Connection connection =null;
        try{
        	
        	conn = ServicePool.getDB2Connection();
        	connection =DriverManager.getConnection("proxool.config2");	
			cst = connection.prepareCall(storeProc);
			for(int i=0;i<len;i++){
				cst.setString(i+1,(String)params.get(i));
			}
			boolean Flag = cst.execute();
			if (!Flag){
				throw new Exception("No Result Return");
			}
        }catch(SQLException se){  
        	throw new Exception(se.getMessage());
        }
        return cst;
	}
	
	
	
	/*
	 * ִ�в�ѯ�洢���̣����ҷ��ذ�������������Vector����(����һ��Vector�����Ӧһ�������)
	 */
	public Vector getMoreResultSetOjbect (String storeName,Vector params)
												throws Exception {
		
        int len = params.size();
        ResultSet rs = null;
        Vector vect = new Vector();
        String storeProc = convertStore(QUERY_STORE_TYPE,storeName,len);
        try{
        	
        	conn = ServicePool.getDB2Connection();
			cst = conn.prepareCall(storeProc);
			for(int i=0;i<len;i++){
				cst.setString(i+1,(String)params.get(i));
			}
			boolean Flag = cst.execute();
			if (Flag){
				rs = cst.getResultSet();
				while (true){
					//��ÿһ����������������е�ÿһ����¼�������뵽Vector������
					Vector temp = new Vector();
					while(rs.next()){
						temp.add(this.getCreditObject(rs));
					}
					vect.add(temp);
					if (cst.getMoreResults()){
						rs = cst.getResultSet();
					}else{
						break;
					}
				}
			}else{
				throw new Exception("No Result Return");
			}
        }catch(SQLException se){ 
        	throw new Exception(se.getMessage());
        }finally{
        	SQLUtil.safelyCloseResultSet(rs);
        	close();
        }
        return vect;
	}
	
	
	
	/*
	 *ִ�з�ҳ��ѯ�������
	 */	
	private ResultSet executeQueryStore(String storeName,Vector params,int offset)
				throws Exception {
		
        int len = params.size();
        String storeProc = convertStore(QUERY_STORE_TYPE,storeName,len);
        ResultSet rs = null;
        ResultSet rsTemp = null;
        try{
        	conn = ServicePool.getDB2Connection();
			cst = conn.prepareCall(storeProc,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			for(int i=0;i<len;i++){
				cst.setString(i+1,(String)params.get(i));
			}
			boolean Flag = cst.execute();
			if (Flag){
				rsTemp = cst.getResultSet();
				int count = 0;
				while (rsTemp.next()){
					count++;
				}
				this.setRowCount(count);
				count = 0;
				cst.execute();
				rs = cst.getResultSet();
				while(rs.next() && count < offset){
					count++;
				}
			}else{
				throw new Exception("No Result Return");
			}
        }catch(Exception ce){
        	throw new Exception(ce.getMessage());
        }finally{
        }
        return rs;
	}

	
	
	/*
	 * ִ�в�ѯ�Ĵ洢���̣�����ȡ��Ӧ�ļ�¼�����з�ҳ
	 */
	public Vector executeQueryStore(String storeName,Vector params,int offset,int length)
					throws Exception {
		Vector vt = new Vector();
        ResultSet rs = null;
        try{
        	rs = executeQueryStore(storeName,params);
			int count = 0;	
			while(rs.next()){
				if (count >= offset && count < offset + length){
					vt.add(getCreditObject(rs));
				}
				count++;
			}
			this.setRowCount(count);
        }catch(Exception ce){
        	throw new Exception(ce.getMessage());
        }finally{
        	SQLUtil.safelyCloseResultSet(rs);
        	close();
        }
        return vt;
	}

	
	
	/*	
	 *ִ���޸ĺ�ɾ���������
	 */
	public boolean executeStore(String storeName,Vector params){
        int len = params.size();
        boolean Flag = true;
        String storeProc = convertStore(UPDATE_STORE_TYPE,storeName,len);
        try{
        	conn = ServicePool.getDB2Connection();
			cst = conn.prepareCall(storeProc);
			cst.registerOutParameter(1,Types.INTEGER);
			for(int i=0;i<len;i++){
				cst.setString(i+2,(String)params.get(i));
			}
			int count = cst.executeUpdate();
			if (cst.getInt(1) != 1){
				Flag = false;
			}
        }catch(Exception ce){
        	ce.printStackTrace();
        	Flag = false;
        }finally{
        }
        return Flag;
	}

	
	/*
	 * �ر����Ӷ���
	 */
	public void close(){
    	SQLUtil.safelyCloseStatement(cst);
    	SQLUtil.safelyCloseConnection(conn);		
	}
	
	
	
	/*	
	 *ȡ�ý�����е�ÿһ����¼�������һ��������������
	 */	
	private CreditObject getCreditObject(ResultSet rs){
		CreditObject object = new CreditObject();
		try{ 
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int count = rsMetaData.getColumnCount();
			for (int i = 1;i <= count;i++){
				//DECIMAL�������͵�����IDΪ3��INTEGER�������͵�����IDΪ4��VARCHAR�������͵�����IDΪ12
				//TIMESTAMP�������͵�����IDΪ93��CHAR�������͵�����IDΪ1
				object.setColumnName(rsMetaData.getColumnName(i));
				if ((rsMetaData.getColumnType(i) == 3 || rsMetaData.getColumnType(i) == 4)&& rs.getString(i) == null){
					object.setColumnValue(String.valueOf(0));
				}else{
					object.setColumnValue(rs.getString(i));
				}
			}
		}catch (SQLException se){ 
			se.printStackTrace();
		}
		return object;
	} 
	
	
	
	/*	
	 *��ʽ���洢�����ַ���
	 */
	private String convertStore(int type,String storeName,int paramNum) {
		String preStoreString = "";
		String sufStoreString = "";
		String storeString = "";
		switch(type){
			case QUERY_STORE_TYPE:
				preStoreString = "{call ";
				break;
			case UPDATE_STORE_TYPE:
				preStoreString = "{?=call ";
				break;
			default:
				break;
		}
		for(int i=0;i<paramNum;i++){
			sufStoreString = sufStoreString + ",?";
		}
		if (paramNum>0){
			sufStoreString = sufStoreString.substring(1);
		}
		storeString = preStoreString + storeName + "(" + sufStoreString + ")}";
		return storeString;
	}

}
