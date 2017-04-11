package tydic.meta.module.tbl;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-3-7
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class TestWCSAction {

    private TestWcsDAO testDAO;

    public void setTestDAO(TestWcsDAO testDAO) {
        this.testDAO = testDAO;
    }

    public static void main(String[] args){
        TestWCSAction x = new TestWCSAction();
        int p = 5;

        x.approximate(10,1,10,p);
        x.approximate(1,1,10,p);
        x.approximate(10,10,10,p);
        x.approximate(10,10,11,p);
        x.approximate(10,10,12,p);
        x.approximate(10,10,13,p);
        x.approximate(10,9,9,p);
        x.approximate(10,9,10,p);
        x.approximate(10,9,11,p);
        x.approximate(10,8,12,p);
        x.approximate(10,8,20,p);
        x.approximate(10,7,10,p);
        x.approximate(10,7,20,p);
        x.approximate(10,10,15,p);
        x.approximate(10,10,16,p);
        x.approximate(10,10,30,p);
        x.approximate(10,10,50,p);
        x.approximate(10,10,80,p);
        x.approximate(10,10,200,p);
        x.approximate(10,10,300,p);
        x.approximate(2,1,2,5);
        x.approximate(2,2,2,5);
        x.approximate(3,2,2,5);
    }

    private void approximate(int n,int x,int c,double p){
        if(n==0 || c==0 || x>n || x>c || p==0) {
            System.out.println("传入数据错误");
            return;
        }
//        double a = 1.0*x/n - (c-x)/(c*p) - (n-x)/(n*p) - (c-n)/(c*p) ;
        double a = 0 ;

        if(x==0){
            a = 0;
        }else if(c>=n*p){
            a = 1.0*x/n  - 1.0*(c-n)/c;
        }else if(n==x && x<=c){
            a = 1.0 - (c-x)/(c*p) - (n-x)/(n*p);
        }else if(n>c) {
            a = 1.0*x/n - (n-c)/(n*p);
        }else{
            a = 1.0*x/c - (c-n)/(c*p);
        }
        BigDecimal bd = new BigDecimal(a*100);
        bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
        System.out.println("选择："+n+"，匹配："+x+"，总数："+c+"，偏离"+p+" ==="+bd+"%");
    }

    public List<Map<String,Object>> queryTest(String ... par){
        return null;
    }

    public List<Map<String,Object>> queryTest1(){
        List<Map<String,Object>> a = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",1);
        map.put("pid",0);
        map.put("name","test1");
        map.put("des","asdfadf");
        map.put("children",1);
        a.add(map);

        Map<String,Object> map11 = new HashMap<String, Object>();
        map11.put("id",11);
        map11.put("pid",1);
        map11.put("name","test11");
        map11.put("des","asd11faf");
        map11.put("children",1);
        a.add(map11);

        Map<String,Object> map12 = new HashMap<String, Object>();
        map12.put("id",12);
        map12.put("pid",1);
        map12.put("name","test12");
        map12.put("des","test12121");
        map12.put("children",1);
        a.add(map12);

        Map<String,Object> map111 = new HashMap<String, Object>();
        map111.put("id",111);
        map111.put("pid",11);
        map111.put("name","test111");
        map111.put("des","test111222");
        map111.put("children",1);
        a.add(map111);

        Map<String,Object> map3 = new HashMap<String, Object>();
        map3.put("id",3);
        map3.put("pid",0);
        map3.put("name","test3");
        map3.put("des","tesdft3");
        a.add(map3);

        Map<String,Object> map31 = new HashMap<String, Object>();
        map31.put("id",31);
        map31.put("pid",3);
        map31.put("name","test31");
        map31.put("des","1test31");
        map31.put("children",1);
        a.add(map31);
        return a;
    }

    public List<Map<String,Object>> queryTest2(Map<String,Object> maps){
        Integer id= MapUtils.getInteger(maps,"id");
        List<Map<String,Object>> a = new ArrayList<Map<String,Object>>();
        if(id!=null && id>0){
            Map<String,Object> mapx = new HashMap<String, Object>();
            mapx.put("id",id+"1");
            mapx.put("pid",id);
            mapx.put("name","test"+id+"1");
            mapx.put("des","des"+id+"1");
            a.add(mapx);
            return a;
        }
        if(!"sub".equals(id)){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",1);
            map.put("pid",0);
            map.put("name","test1");
            map.put("des","asdfadf");
            a.add(map);

            Map<String,Object> map3 = new HashMap<String, Object>();
            map3.put("id",3);
            map3.put("pid",0);
            map3.put("name","test3");
            map3.put("des","tesdft3");
            a.add(map3);
        }
//        if("sub".equals(id)){
            Map<String,Object> map11 = new HashMap<String, Object>();
            map11.put("id",11);
            map11.put("pid",1);
            map11.put("name","test11");
            map11.put("des","asd11faf");
            a.add(map11);

            Map<String,Object> map12 = new HashMap<String, Object>();
            map12.put("id",12);
            map12.put("pid",1);
            map12.put("name","test12");
            map12.put("des","test12121");
            a.add(map12);

            Map<String,Object> map111 = new HashMap<String, Object>();
            map111.put("id",111);
            map111.put("pid",11);
            map111.put("name","test111");
            map111.put("des","test111222");
            a.add(map111);
//        }

        Map<String,Object> map31 = new HashMap<String, Object>();
        map31.put("id",31);
        map31.put("pid",3);
        map31.put("name","test31");
        map31.put("des","1test31");
        map31.put("children",1);
        a.add(map31);
        return a;
    }

    public List<Map<String,Object>> queryTables(Map<String,Object> data,Page page){
        if(1==1){
            return testDAO.queryMetaTables(data,page);
        }
        return null;
    }

    public Object[][] queryTableArray(Map<String,Object> data,Page page){
        if(1==0){
            return testDAO.queryMetaTableArray(data,page);
        }

        String PARENT_ID = MapUtils.getString(data,"PARENT_ID");
        if(PARENT_ID!=null){
            Object[][] objects = new Object[5][7];
            for(int i=1;i<=5;i++){
                objects[i-1] = new Object[]{"TABLE_NAME"+i,"中文名"+i,"数据源0","用户0","分类1",PARENT_ID+"_"+i,PARENT_ID};
            }
            return objects;
        }else{
            Object[][] objects = new Object[10][7];
            for(int i=1;i<=5;i++){
                objects[i-1] = new Object[]{"TABLE_NAME"+i,"中文名"+i,"数据源0","用户0","分类1",i,0};
            }
            for(int i=6;i<=10;i++){
                objects[i-1] = new Object[]{"TABLE_NAME"+i,"中文名"+i,"数据源0","用户0","分类1",i,i-5};
            }
            return objects;
        }
    }

}
