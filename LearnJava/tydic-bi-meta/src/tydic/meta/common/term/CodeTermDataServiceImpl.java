package tydic.meta.common.term;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.sys.code.CodePO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 码表条件数据系统的一个默认实现
 * @date 12-5-29
 * -
 * @modify
 * @modifyDate -
 */
public class CodeTermDataServiceImpl extends TermDataService{

    public Object[][] getData(DataAccess access, Map<String,Object> params,TermDataCall call) {
        String codeType = MapUtils.getString(params,TermConstant.KEY_codeType,"");
        String notValueRange = MapUtils.getString(params,TermConstant.KEY_excludeValues,"")+",";
        if(!"".equals(codeType)){
            CodePO[] pos = CodeManager.getCodes(codeType);
            List<Object[]> list = new ArrayList<Object[]>();
            if(pos==null){
                CodeManager.load();
                pos = CodeManager.getCodes(codeType);
            }
            for(CodePO po : pos){
                if(notValueRange.contains(po.getCodeValue()+","))
                    continue;
                list.add(new Object[]{po.getCodeValue(),po.getCodeName()});
            }
            Object[][] objects = new Object[list.size()][2];
            for(int i=0;i<list.size();i++)
                objects[i] = list.get(i);
            return objects;
        }
        return null;
    }

}
