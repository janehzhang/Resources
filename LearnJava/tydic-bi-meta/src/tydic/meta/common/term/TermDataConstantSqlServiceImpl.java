package tydic.meta.common.term;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:后台常量SQL形式。如果存在后台常量SQL，取后台的常量。
 * @date 2012-06-07
 */
public class TermDataConstantSqlServiceImpl extends TermDataDefaultSerivceImpl {
    @Override
    public Object[][] getData(DataAccess access, Map<String, Object> termControl, TermDataCall call) throws Exception {
        //获取常量SQL的配置
        List<String> constantSql = (List<String>)MapUtils.getObject(termControl, TermConstant.KEY_CONSTANT_SQL);
        if (constantSql == null || constantSql.size() != 2) {
            throw new IllegalArgumentException("常量SQL配置出错");
        }
        //获取SQL
        Class constant = Class.forName(constantSql.get(0));
        //获取字段
        Field field=constant.getField(constantSql.get(1));
        termControl.put(TermConstant.KEY_dataRule,field.get(constant));
        return super.getData(access, termControl, call);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
