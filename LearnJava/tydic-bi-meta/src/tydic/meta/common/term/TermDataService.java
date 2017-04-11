package tydic.meta.common.term;

import tydic.frame.jdbc.DataAccess;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 条件请求数据抽象类
 * @date 12-5-29
 * -
 * @modify
 * @modifyDate -
 */
public abstract class TermDataService {

    /**
     * 条件实现类必须实现此方法
     * @param access 传入数据访问接口
     * @param params 条件参数 可通过此参数取得客户端条件控件的所有状态信息 取值的key参考TermControlAction的常量
     * @return
     */
    public abstract Object[][] getData(DataAccess access,Map<String,Object> params,TermDataCall call) throws Exception;

    /**
     * 默认实现一般的下拉框不需要实现此方法，异步加载树时则需要重写此方法
     * 异步下拉树加载时 在getData的实现里面调用call.coverTermAttribute(TermControl.KEY_dynload,true)  实现异步加载
     * @param access
     * @param params
     * @param call
     * @return
     * @throws Exception
     */
    public Object[][] getChildData(DataAccess access,Map<String,Object> params,String parentID,TermDataCall call) throws Exception{
        return null;
    }

}
