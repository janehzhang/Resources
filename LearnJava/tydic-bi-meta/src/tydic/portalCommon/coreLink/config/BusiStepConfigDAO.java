package tydic.portalCommon.coreLink.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

public class BusiStepConfigDAO extends MetaBaseDAO {

/*	public Map<String, Object> getInitDataList(String linkName) {
		 Map<String, Object> map=new HashMap<String,Object>();
		 
		 List<BusiStepConfigBean> list1 =new ArrayList<BusiStepConfigBean>();
		 
		 List<BusiStepConfigBean> list2 =new ArrayList<BusiStepConfigBean>();
		 
		 List<BusiStepConfigBean> list3 =new ArrayList<BusiStepConfigBean>();
		 
		 List<Map<String, Object>> levelList1=getLeaf1DataByParam(linkName);
		
		 //第一层
	    for (Map<String, Object> key1 : levelList1){	 
			 BusiStepConfigBean obj1=new BusiStepConfigBean();
			 String id = (String)key1.get("BUSI_STEP_ID");
			 obj1.setId(id);
			 obj1.setName((String)key1.get("BUSI_STEP_NAME"));
			 obj1.setWeight(Double.parseDouble(key1.get("BUSI_STEP_WEIGHT").toString()));
			 obj1.setLevel(Double.parseDouble(key1.get("BUSI_STEP_LEVEL").toString()));
		     Map<String,Object> countMap=getLeafCountById(id);
			 obj1.setLeaf(Integer.parseInt(countMap.get("LEAF").toString()));
			   
			 //第二层
			  List<Map<String, Object>> levelList2=getLeafNDataByParam(id);
			  for (Map<String, Object> key2 : levelList2){	 
				  BusiStepConfigBean obj2=new BusiStepConfigBean();
				  String secondId =(String)key2.get("BUSI_STEP_ID");
				  obj2.setId(secondId);
				  obj2.setName((String)key2.get("BUSI_STEP_NAME"));
				  obj2.setParentId(id);
				  obj2.setWeight(Double.parseDouble(key2.get("BUSI_STEP_WEIGHT").toString()));
				  obj2.setPercentate(Double.parseDouble(key2.get("BUSI_STEP_PERCENTATE").toString()));
				  obj2.setLevel(Double.parseDouble(key2.get("BUSI_STEP_LEVEL").toString())); 
				  Map<String,Object> sencodCountMap=getLeafCountById(secondId);
				  obj2.setLeaf(Integer.parseInt(sencodCountMap.get("LEAF").toString()));
				  
				  //第三层
				  List<Map<String, Object>> levelList3=getLeafNDataByParam(secondId);
				  for (Map<String, Object> key3 : levelList3){	 
					  BusiStepConfigBean obj3=new BusiStepConfigBean();
					  String  threeId =(String)key3.get("BUSI_STEP_ID");
					  obj3.setId(threeId);
					  obj3.setName((String)key3.get("BUSI_STEP_NAME"));
					  obj3.setParentId(secondId);
					  obj3.setWeight(Double.parseDouble(key3.get("BUSI_STEP_WEIGHT").toString()));
					  obj3.setDirection(Double.parseDouble(key3.get("BUSI_STEP_DIRECTION").toString()));
					  obj3.setLevel(Double.parseDouble(key3.get("BUSI_STEP_LEVEL").toString())); 
			        Map<String,Object> threeCountMap=getLeafCountById(threeId);
					  obj3.setLeaf(Integer.parseInt(threeCountMap.get("LEAF").toString()));  
					  obj3.setLeaf(1);
					  
					 list3.add(obj3);
				  }
				  
				  list2.add(obj2); 
			  }
			  
			   list1.add(obj1);
		 }
		 
		 map.put("level_1", list1);
		 map.put("level_2", list2);
		 map.put("level_3", list3);
		 return map;
	}*/
	
	public Map<String, Object> getInitDataList(String linkName) {
		 Map<String, Object> map=new HashMap<String,Object>();
		 List<ObjectConfigBean> list =new ArrayList<ObjectConfigBean>();
		 
		 List<Map<String, Object>> levelList1=getLeaf1DataByParam(linkName);
		 //第一层
	    for (Map<String, Object> key1 : levelList1){	 
	    	    String step1Id   =  (String)key1.get("BUSI_STEP_ID");
	    	    String step1Name = (String)key1.get("BUSI_STEP_NAME");
	    	    Double weight1   = Double.parseDouble(key1.get("BUSI_STEP_WEIGHT").toString());
			   
			 //第二层
			  List<Map<String, Object>> levelList2=getLeafNDataByParam(step1Id);
			  if(null != levelList2 && levelList2.size() > 0){
				  for (Map<String, Object> key2 : levelList2){	 
					  String step2Id =(String)key2.get("BUSI_STEP_ID");
					  String step2Name=(String)key2.get("BUSI_STEP_NAME");
					  Double percentate2 =  Double.parseDouble(key2.get("BUSI_STEP_PERCENTATE")==null?"0":key2.get("BUSI_STEP_PERCENTATE").toString());
					  Double weight2     =  Double.parseDouble(key2.get("BUSI_STEP_WEIGHT")==null?"0":key2.get("BUSI_STEP_WEIGHT").toString());
					  //第三层
					  List<Map<String, Object>> levelList3=getLeafNDataByParam(step2Id);
					  if(null != levelList3 && levelList3.size() > 0){
						     for (Map<String, Object> key3 : levelList3){	 
						    	  String step3Id  =  (String)key3.get("BUSI_STEP_ID");
						   	      String step3Name= (String)key3.get("BUSI_STEP_NAME");  
						   	      Double direction3= Double.parseDouble(key3.get("BUSI_STEP_DIRECTION").toString());
						   	      Double weight3=Double.parseDouble(key3.get("BUSI_STEP_WEIGHT").toString());
								  
						   	      ObjectConfigBean object=new  ObjectConfigBean();
								  object.setStep1Id(step1Id);
								  object.setStep1Name(step1Name);
								  object.setWeight1(weight1);
								  object.setStep2Id(step2Id);
								  object.setStep2Name(step2Name);
								  object.setPercentate2(percentate2);
								  object.setWeight2(weight2);
								  object.setStep3Id(step3Id);
								  object.setStep3Name(step3Name);
								  object.setDirection3(direction3);
								  object.setWeight3(weight3);
								  list.add(object);
							  }
						  
					  }else{
						  ObjectConfigBean object=new  ObjectConfigBean();
						  object.setStep1Id(step1Id);
						  object.setStep1Name(step1Name);
						  object.setWeight1(weight1);
						  object.setStep2Id(step2Id);
						  object.setStep2Name(step2Name);
						  object.setPercentate2(percentate2);
						  object.setWeight2(weight2);
						  list.add(object);
						  
					  }
					  
				  }
			  }else{
						  ObjectConfigBean object=new  ObjectConfigBean();
						  object.setStep1Id(step1Id);
						  object.setStep1Name(step1Name);
						  object.setWeight1(weight1);
						  list.add(object);
				}
	     }	
		 map.put("list", list);
		 return map;
	}	
	
	
    // 第一层
	public List<Map<String, Object>> getLeaf1DataByParam(String param) {
		StringBuffer sb = new StringBuffer();

		sb.append(
				"select busi_step_id,busi_step_name,parent_busi_step_id,"
						+ "BUSI_STEP_WEIGHT*100 as BUSI_STEP_WEIGHT,BUSI_STEP_PERCENTATE*100 as BUSI_STEP_PERCENTATE,BUSI_STEP_DIRECTION,"
						+ "BUSI_STEP_LEVEL").append(" from")
				.append(" CS_BUSI_STEP_CONFIG c")
				.append(" where c.VALID_FALG = 1");
		if (param != null && !"".equals(param)) {
			sb.append(" and c.busi_step_id ='" + param + "'");
		} else {
			sb.append(" and c.parent_busi_step_id is null");
		}
		    sb.append(" order by c.busi_step_id asc");
		    
		return getDataAccess().queryForList(sb.toString());
	}
	// 第N层
	public List<Map<String, Object>> getLeafNDataByParam(String parentId) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select busi_step_id,busi_step_name,parent_busi_step_id,"
						+ "BUSI_STEP_WEIGHT*100 as BUSI_STEP_WEIGHT,BUSI_STEP_PERCENTATE*100 as BUSI_STEP_PERCENTATE,BUSI_STEP_DIRECTION,"
						+ "BUSI_STEP_LEVEL")
						.append(" from")
				.append(" CS_BUSI_STEP_CONFIG c")
				.append(" where c.VALID_FALG = 1");
		      sb.append(" and c.parent_busi_step_id ='" + parentId + "'");
		      sb.append(" order by c.busi_step_id asc");
		    
		return getDataAccess().queryForList(sb.toString());
   }
   //通过结点ID查询叶子结点个数
   public Map<String,Object> getLeafCountById(String id){
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("select count(1) as LEAF from (");
    	buffer.append(" select *");
    	buffer.append(" from CS_BUSI_STEP_CONFIG");
    	buffer.append(" where  VALID_FALG = 1");
    	buffer.append(" start with busi_step_id = ?");
    	buffer.append(" connect by parent_busi_step_id = prior busi_step_id");
    	buffer.append(" ) where busi_step_level=3");
    	return getDataAccess().queryForMap(buffer.toString(), new Object[]{id});
    }
	public boolean insert(Map<String, Object> data) {
		String sql = "insert into CS_BUSI_STEP_CONFIG "
				   +"(BUSI_STEP_ID,"
				   +"BUSI_STEP_NAME,"
				   +"PARENT_BUSI_STEP_ID,"
				   +"BUSI_STEP_WEIGHT,"
				   +"BUSI_STEP_PERCENTATE,"
				   +"BUSI_STEP_DIRECTION,"
				   +"BUSI_STEP_LEVEL,"
				   +"VALID_FALG,LOAD_DATE) "
				   +"VALUES"
                   +"((select max(c.busi_step_id)+1 from CS_BUSI_STEP_CONFIG c where c.busi_step_level=2 and c.parent_busi_step_id=?) , ?, ?, " +
                   		"(select c.busi_step_weight from CS_BUSI_STEP_CONFIG c where c.busi_step_id=?)*?/100, ?, ?,2,1,sysdate)";
		
       Object[] p = new Object[7];
     	p[0] = data.get("parentId");
		p[1] = data.get("kfname");
		p[2] = data.get("parentId");
		p[3] = data.get("parentId");
		p[4] = data.get("bz");
		p[5] = data.get("bz");
		p[6] = data.get("direction");
		return getDataAccess().execNoQuerySql(sql, p);
		
	}
	
	public boolean updateWeight(Map<String,Object> queryData) {
	      Double value = (Convert.toDouble(queryData.get("values")))/100;
	      String id = Convert.toString(queryData.get("ids"));
	      String sql = "UPDATE CS_BUSI_STEP_CONFIG SET  BUSI_STEP_WEIGHT="+value+" where BUSI_STEP_ID='"+id+"'";
		return getDataAccess().execNoQuerySql(sql);
	}
	
	public boolean updatePercentate(Map<String,Object> queryData) {
	      Double value = (Convert.toDouble(queryData.get("values")))/100;
	      String id = Convert.toString(queryData.get("ids"));
	      String sql = "UPDATE CS_BUSI_STEP_CONFIG SET  BUSI_STEP_PERCENTATE="+value+" where BUSI_STEP_ID='"+id+"'";
		return getDataAccess().execNoQuerySql(sql);
	}	
	
	public boolean updateDirection(Map<String,Object> queryData) {
	      Double value = Convert.toDouble(queryData.get("values"));
	      String id = Convert.toString(queryData.get("ids"));
	      String sql = "UPDATE CS_BUSI_STEP_CONFIG SET  BUSI_STEP_DIRECTION="+value+" where BUSI_STEP_ID='"+id+"'";
		return getDataAccess().execNoQuerySql(sql);
	}
	
	public boolean insertThree(Map<String, Object> data) {
		String sql = "insert into CS_BUSI_STEP_CONFIG "
			   +"(BUSI_STEP_ID,"
			   +"BUSI_STEP_NAME,"
			   +"PARENT_BUSI_STEP_ID,"
			   +"BUSI_STEP_WEIGHT,"
			   +"BUSI_STEP_DIRECTION,"
			   +"BUSI_STEP_LEVEL,"
			   +"VALID_FALG,LOAD_DATE) "
			   +"VALUES"
            +"((select nvl(max(c.busi_step_id)+1,?||'0000') from CS_BUSI_STEP_CONFIG c where c.busi_step_level=3 and c.parent_busi_step_id=?), ?, ?, ?, ?,3,1,sysdate)";
			   Object[] p = new Object[6];
			    p[0] = data.get("p_parentId");
				p[1] = data.get("p_parentId");
				p[2] = data.get("kfname");
				p[3] = data.get("p_parentId");
				p[4] = data.get("weight");
				p[5] = data.get("direction");
	      return getDataAccess().execNoQuerySql(sql, p);
   }
   public boolean deleteConfig(String id) {
		  String sql="delete from CS_BUSI_STEP_CONFIG c where c.busi_step_id=? or c.parent_busi_step_id=?";
		  Object[] p = new Object[2];
		    p[0] = id;
			p[1] = id;
		return getDataAccess().execNoQuerySql(sql,p);
  }
} 
