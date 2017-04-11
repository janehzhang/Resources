package tydic.meta.common.term;

import tydic.frame.jdbc.DataAccess;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 测试后台接口方式加载条件数据，此种方式必须继承TermDataService类
 * @date 12-5-30
 * -
 * @modify
 * @modifyDate -
 */
public class TestTreeImpl extends TermDataService{

    public Object[][] getData(DataAccess access, Map<String, Object> params, TermDataCall call) throws Exception {
        Object[][] testobj = new Object[5][3];
        for(int i =1;i<=5;i++){
            testobj[i-1] = new Object[]{i,"测试"+i,0};
        }
        call.coverTermAttribute(TermConstant.KEY_dynload,true);//把树设置成异步加载
        return testobj;
    }

    /**
     * 一般的下拉框数据，此方法无用，不需要调用
     * 如果是树，并且设置成了动态加载，那么必须重写此方法，实现加载子节点数据
     * @param access
     * @param params
     * @param parentID
     * @param call
     * @return
     * @throws Exception
     */
    public Object[][] getChildData(DataAccess access,Map<String,Object> params,String parentID,TermDataCall call) throws Exception{
        Object[][] testobj = new Object[5][3];
        for(int i =1;i<=5;i++){
            String id = parentID+"_"+i;
            testobj[i-1] = new Object[]{id,"测试"+id,parentID};
        }
        return testobj;
    }

}
