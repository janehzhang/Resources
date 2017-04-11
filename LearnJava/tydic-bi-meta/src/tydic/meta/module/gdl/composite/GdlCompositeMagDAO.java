package tydic.meta.module.gdl.composite;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.gdl.GdlConstant;
import tydic.meta.module.gdl.GdlDAO;
import tydic.meta.sys.code.CodeManager;

import java.util.*;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description 复合指标管理DAO
 * @date 12-3-29
 * -
 * @modify
 * @modifyDate -
 */
public class GdlCompositeMagDAO extends GdlDAO {

    /**
     * 获取一个指标的支撑维度
     * @param gdlId
     * @return
     */
    public List<Map<String,Object>> getGdlSupportDimInfos(int gdlId){
        String dimTypeSql = "select dim_table_id," +
                "       dim_type_id, " +
                "       dim_type_name, " +
                "       TABLE_DIM_PREFIX, " +
                "       table_name " +
                "  from (select distinct b.dim_table_id," +
                "                        c.dim_type_id, " +
                "                        c.dim_type_name, " +
                "                        d.table_dim_prefix, " +
                "                        d.table_name " +
                "          from meta_gdl a, meta_gdl_dim_group_method b, meta_dim_type c,meta_dim_tables d " +
                "         where b.gdl_id = a.gdl_id " +
                "           and b.gdl_version = a.gdl_version " +
                "           and c.dim_table_id = b.dim_table_id " +
                "           and d.dim_table_id = b.dim_table_id " +
                "           and b.gdl_id = ? " +
                "           and a.gdl_state = 1) x";
        List<Map<String,Object>> list = getGdlSupportDims(gdlId,null);
        List<Map<String,Object>> dimTypeList = getDataAccess().queryForList(dimTypeSql,gdlId);
        for(Map<String,Object> dim : list){
            if(dimTypeList.size()>1){
                String prifixName = "";
                String dimTableName = "";
                int dimTableId_ = MapUtils.getInteger(dim,"DIM_TABLE_ID");
                List<Map<String,Object>> ls = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> mp : dimTypeList) {
                    int dimTableId = MapUtils.getInteger(mp, "DIM_TABLE_ID");
                    if (dimTableId == dimTableId_) {
                        prifixName = MapUtils.getString(mp,"TABLE_DIM_PREFIX");
                        dimTableName = MapUtils.getString(mp,"TABLE_NAME");
                        ls.add(mp);
                    }
                }
                dim.put("DIM_TABLE_NAME",dimTableName);
                dim.put("DIM_TYPE_LIST", ls);
                String sql = "select DIM_TYPE_ID,max(dim_level) MAX_LEVEL,count(1) TOTAL_COUNT from "+GdlConstant.DIM_OWNER +dimTableName +
                        " WHERE STATE=1 GROUP BY DIM_TYPE_ID ";
                Object[][] aa = getDataAccess().queryForArray(sql,false);
                for(Object arr[] : aa){
                    int totalCount = Convert.toInt(arr[2]);
                    if(totalCount==0)continue;
                    dim.put("DIM_TYPE_INFO_"+arr[0],new Object[]{arr[1],arr[2]});
                    if(totalCount<=GdlConstant.DIM_CODE_VIEWFLAG){
                        String dimValueSql = "select "+prifixName+"_CODE,"+prifixName+"_NAME from "+GdlConstant.DIM_OWNER + dimTableName +
                                " WHERE DIM_TYPE_ID=? ORDER BY ORDER_ID";
                        dim.put("DIM_TYPE_DATA_"+arr[0],getDataAccess().queryForList(dimValueSql,arr[0]));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 验证重复指标，去重规则如下：
     * 同一基础指标下，绑定维度，归并类型，维度编码完全一样
     * @param gdl
     * @param baseId 基础指标ID
     * @param exid 排除指标id（修改时传入自身)
     * @return 重复则返回true
     */
    public boolean checkCompositeReGdl(Map<String,Object> gdl,int baseId,int exid){
        Map<String,Object> bindDims = (Map<String,Object>)MapUtils.getMap(gdl,"BIND_DIMS");
        if(bindDims!=null && bindDims.size()>0){
            String sql = " SELECT GDL_ID,DIM_TYPE_ID,DIM_TABLE_ID,DIM_CODE FROM META_GDL_TBL_REL_TERM I LEFT JOIN (" +
                    "   SELECT C.GDL_TBL_REL_ID,COUNT(C.GDL_ID) CNT" +
                    "  FROM META_GDL_TBL_REL_TERM C" +
                    "  ,(SELECT distinct B.GDL_TBL_REL_ID" +
                    "               FROM META_GDL A, META_GDL_TBL_REL B" +
                    "              WHERE A.GDL_SRC_TABLE_ID = B.TABLE_ID" +
                    "                AND A.GDL_ID=?) D" +
                    "    WHERE C.GDL_TBL_REL_ID = D.GDL_TBL_REL_ID" +
                    "    GROUP BY C.GDL_TBL_REL_ID) J " +
                    "    ON I.GDL_TBL_REL_ID=J.GDL_TBL_REL_ID " +
                    "    WHERE J.CNT=? AND GDL_ID!=? ORDER BY GDL_ID "; //查询基础指标下所有与当前指标绑定维度数一样的指标
            
            Object[][] arr = getDataAccess().queryForArray(sql,false,baseId,bindDims.size(),exid);
            Map<Integer,List<Object[]>> chekDims = new HashMap<Integer,List<Object[]>>();
            for(Object[] o : arr){
                int gid = Convert.toInt(o[0]);
                if(chekDims.containsKey(gid)){
                    chekDims.get(gid).add(o);
                }else{
                    List<Object[]> ls = new ArrayList<Object[]>();
                    ls.add(o);
                    chekDims.put(gid,ls);
                }
            }
            int findGdlId = 0; //当变为有值时，表示找到有匹配的指标
            for(Map.Entry<Integer,List<Object[]>> entry : chekDims.entrySet()){

                boolean _flag = true;//默认都一样，当变false时，表示某一编码不完全一样
                for(Object[] o : entry.getValue()){
                    int dimtype = Convert.toInt(o[1]);
                    String dimcode = Convert.toString(o[3]);
                    Map<String,Object> bd = (Map<String,Object>)(bindDims.get(Convert.toString(o[2])));
                    if(bd==null){
                        _flag = false;//维度没找到，不匹配
                        break;
                    }
                    int typeid = Convert.toInt(bd.get("typeId"));
                    String codes = Convert.toString(bd.get("codes"));
                    if(typeid==dimtype && dimcode.equals(codes)){
                    }else {
                        _flag = false;
                        break;
                    }
                }
                if(_flag){//表示某个指标完全匹配
                    findGdlId = entry.getKey();
                    break;
                }
            }
            return findGdlId!=0;
        }
        return false;
    }

    /**
     * 删除原指标关系数据（复合指标维度发生修改时用）
     * @param gdlId
     * @return
     */
    public boolean deleteOldRel(int gdlId){
        //删除条件
        String termSql = "DELETE FROM META_GDL_TBL_REL_TERM A WHERE GDL_ID=?";
        //删除表关系
        String relSql = "DELETE FROM META_GDL_TBL_REL WHERE GDL_ID=?";
        //删除父指标关系
        String gdlSql = "DELETE FROM META_GDL_REL WHERE GDL_ID=?";
        getDataAccess().execNoQuerySql(termSql,gdlId);
        getDataAccess().execNoQuerySql(relSql,gdlId);
        getDataAccess().execNoQuerySql(gdlSql,gdlId);
        return true;
    }

    /**
     * 获取一个指标的最终依赖的基础指标ID
     * @param gdlId
     * @return
     */
    public int getBaseGdlIdByGdl(int gdlId){
        String sql = "SELECT x.GDL_ID " +
                "  FROM (select distinct a.PAR_GDL_ID from meta_gdl_rel a " +
                " CONNECT BY PRIOR a.PAR_GDL_ID = a.GDL_ID " +
                "  start with a.GDL_ID = ?) b " +
                "  left join meta_gdl x on x.GDL_ID = b.PAR_GDL_ID " +
                " WHERE x.GDL_STATE = 1 AND x.GDL_TYPE=0 ";//查询出基础指标
        return getDataAccess().queryForIntByNvl(sql,gdlId,gdlId); //如果未查询到数据，返回自身(自身就是基础指标)
    }

    /**
     * 分析指标父子关系
     * @param gdlId 当前指标ID
     * @param baseGdlId 最终基础指标ID
     * @param bindDims 绑定维度信息
     */
    public void analysisGdlRel(int gdlId,int baseGdlId,Map<String,Object> bindDims){
        String parBindSql = "select count(1) " +
                "from meta_gdl_tbl_rel_term b," +
                "(select i.PAR_GDL_ID from meta_gdl_rel i WHERE i.GDL_ID=? AND ROWNUM=1) c " +
                "where b.GDL_TBL_REL_ID=0 and b.GDL_ID=c.PAR_GDL_ID  "; //查询父绑定维度数量
        String bindSql = "select b.DIM_TABLE_ID," +
                " b.DIM_TYPE_ID, " +
                " b.DIM_LEVEL, " +
                " b.DIM_CODE, " +
                " c.GDL_ID, " +
                " c.lv " +
                "   from meta_gdl_tbl_rel_term b," +
                "  (SELECT j.GDL_ID,j.lv FROM (select distinct i.GDL_ID,level lv from meta_gdl_rel i " +
                "   CONNECT BY PRIOR i.GDL_ID = i.PAR_GDL_ID start with i.PAR_GDL_ID =?) j ) c  " +
                "where b.GDL_TBL_REL_ID=0 " +
                "and b.GDL_ID=c.GDL_ID  " +
                "order by c.lv desc";
        Object[][] arr = getDataAccess().queryForArray(bindSql,false,baseGdlId);
        Map<Integer,List<Object[]>> gdlBinds = new HashMap<Integer,List<Object[]>>();//把绑定维度按指标分类
        for(Object[] o : arr){
            int gid = Convert.toInt(o[4]);
            if(gdlBinds.containsKey(gid)){
                gdlBinds.get(gid).add(o);
            }else{
                List<Object[]> ls = new ArrayList<Object[]>();
                ls.add(o);
                gdlBinds.put(gid,ls);
            }
        }

        //取出每个指标开始分析
        int par_l = bindDims.size();//初始绑定距离（最终把最小的设置为父）
        List<Integer> parents = new ArrayList<Integer>();//最终确定为父的指标
        Map<Integer,Boolean> childClear = new HashMap<Integer,Boolean>();//最终确定为子的指标，value记录是否需要清楚原父
        for(Map.Entry<Integer,List<Object[]>> entry : gdlBinds.entrySet()){
            List<Object[]> binds = entry.getValue();
            if(binds.size()==bindDims.size())continue;//绑定纬度数一样，不可能成为父或子
            if(binds.size()>bindDims.size()){ //分析的指标绑定数比当前绑定数多
                //可能成为子
                int sup_num = 0;//满足条件的数目,当数目与当前绑定数相等时，才有资格成为子
                boolean flag = true;
                for(Object[] dims : binds){
                    if(bindDims.containsKey(Convert.toString(dims[0]))){
                        int dimtype = Convert.toInt(dims[1]);
                        String dimcode = Convert.toString(dims[3]);
                        Map<String,Object> bd = (Map<String,Object>)(bindDims.get(Convert.toString(dims[0])));
                        int bdtype = Convert.toInt(bd.get("typeId"));
                        String bdcodes = Convert.toString(bd.get("codes"));
                        if(dimtype==bdtype && dimcode.equals(bdcodes)){
                            sup_num ++;
                        }else{
                            flag = false;
                            break;
                        }
                    }
                }

                if(!flag || sup_num!=bindDims.size())continue;//不满足，继续下一条
                //满足继承条件，（接下来判断当前与原父谁的距离更近，近的才有资格做父）
                int a = getDataAccess().queryForInt(parBindSql,entry.getKey());//如果原父是基础指标，则会返回0
                if(bindDims.size()>=a){
                    //很好很强大，历经千难万险，终于找到失散多年的父亲
                    childClear.put(entry.getKey(),bindDims.size()!=a);
                }

            }else{
                //可能成为父
                int chayi = bindDims.size()-binds.size();
                if(chayi>par_l)continue;//比记录的最小记录还大，直接忽略（有比它更适合的父）

                boolean flag = true;
                for(Object[] dims : binds){
                    if(bindDims.containsKey(Convert.toString(dims[0]))){
                        int dimtype = Convert.toInt(dims[1]);
                        String dimcode = Convert.toString(dims[3]);
                        Map<String,Object> bd = (Map<String,Object>)(bindDims.get(Convert.toString(dims[0])));
                        int bdtype = Convert.toInt(bd.get("typeId"));
                        String bdcodes = Convert.toString(bd.get("codes"));
                        if(dimtype==bdtype && dimcode.equals(bdcodes)){
                        }else{
                            flag = false;
                            break;
                        }
                    }else{
                        flag = false;
                        break;
                    }
                }
                if(!flag)continue;//不满足条件，不能成为父

                if(chayi<par_l){
                    par_l = chayi;
                    parents.clear();//比记录的距离更短，清除原来的，此更适合父
                    parents.add(entry.getKey());
                }else if(chayi==par_l){
                    parents.add(entry.getKey());//和记录距离一样，都能成为父
                }

            }
        }

        if(parents.size()==0)
            parents.add(baseGdlId);//未找到父，因此只能直接挂到基础指标下

        String delteRelSql = "DELETE FROM META_GDL_REL WHERE GDL_ID=?";//删除关系sql
        String addRelSql = "INSERT INTO META_GDL_REL(GDL_ID,PAR_GDL_ID) VALUES(?,?)";
        for(Integer pid : parents){
            getDataAccess().execUpdate(addRelSql,gdlId,pid);
        }
        for(Map.Entry<Integer,Boolean> ent : childClear.entrySet()){
            if(ent.getValue())
                getDataAccess().execUpdate(delteRelSql,ent.getKey());//删除原关系
            getDataAccess().execUpdate(addRelSql,ent.getKey(),gdlId);
        }

    }

    /**
     * 寻找父指标，并设置关系  （添加新复合指标时，需要动态计算构建父子关系）
     * @param gdlId 当前指标
     * @param baseGdlId 基础指标（一般添加界面时选择的入口指标ID在外面计算得到）
     *          通过入口指标，可确定一个基础指标，可以确定一棵向下的树网，在向下的父关系
     *          再通过绑定维度计算出最近指标 设置出父子关系
     * @param bindDims 当前指标的绑定维度
     * @return
     */
    public boolean findSetParentGdl(int gdlId,int baseGdlId,Map<String,Object> bindDims){
        String childSql = "SELECT b.GDL_ID " +
                "  FROM (select distinct a.GDL_ID,level lv from meta_gdl_rel a " +
                " CONNECT BY PRIOR a.GDL_ID = a.PAR_GDL_ID " +
                "  start with a.PAR_GDL_ID =?) b WHERE b.LV<"+bindDims.size();
        String bindSql = "select b.DIM_TABLE_ID," +
                " b.DIM_TYPE_ID, " +
                " b.DIM_LEVEL, " +
                " b.DIM_CODE " +
                "   from meta_gdl_tbl_rel_term b " +
                "  where b.GDL_TBL_REL_ID=0 AND b.GDL_ID = ? ";
        Object[][] allpar = getDataAccess().queryForArray(childSql,false,baseGdlId); //查询基础指标下子
        List<Integer> parent = new ArrayList<Integer>();
        int ml = bindDims.size();//记录绑定差异数，最终以最小的作为父
        for(Object[] par : allpar){
            Object[][] bind = getDataAccess().queryForArray(bindSql,false,par[0]); //查询出每个父的绑定维度
            int chayi = bindDims.size()-bind.length;
            if(chayi>ml)continue;

            boolean flag = true;
            for(Object[] dims : bind){
                if(bindDims.containsKey(Convert.toString(dims[0]))){
                    int dimtype = Convert.toInt(dims[1]);
                    int dimlevel = Convert.toInt(dims[2]);
                    String dimcode = Convert.toString(dims[3]);
                    Map<String,Object> bd = (Map<String,Object>)(bindDims.get(Convert.toString(dims[0])));
                    int bdtype = Convert.toInt(bd.get("typeId"));
                    int bdlevel = Convert.toInt(bd.get("level"));
                    String bdcodes = Convert.toString(bd.get("codes"));
                    if(dimtype==bdtype && dimlevel==bdlevel && dimcode.equals(bdcodes)){
                    }else{
                        flag = false;
                        break;
                    }
                }else{
                    flag = false;
                    break;
                }
            }
            if(!flag)continue;

            if(chayi<ml){
                ml = chayi;
                parent.clear();
                parent.add(Convert.toInt(par[0]));
            }else if(chayi==ml){
                parent.add(Convert.toInt(par[0]));
            }
        }
        if(parent.size()==0)
            parent.add(baseGdlId);
        String parRelSql = "INSERT INTO META_GDL_REL(GDL_ID,PAR_GDL_ID) VALUES(?,?)";
        for(Integer pid : parent){
            getDataAccess().execUpdate(parRelSql,gdlId,pid);
        }
        return true;
    }

    /**
     * 继承父指标关系(添加新指标时，指标需要继承所有父指标关系)
     * @param gdlId 当前指标
     *              动态去父子关系表找到所有的父指标，然后把这些父指标的关系继承下来
     * @param bindDims 绑定维度信息
     * @return
     */
    public boolean extendParentGdlRel(int gdlId,Map<String,Object> bindDims){
        String bindSql = "select b.DIM_TABLE_ID from meta_gdl_tbl_rel_term b " +
                " where b.GDL_TBL_REL_ID=? AND b.GDL_ID=? ";//查询父表绑定条件数
        String parsql = "SELECT b.GDL_TBL_REL_ID,b.TABLE_ID,b.GDL_ID,b.REL_TYPE,NVL(b.REL_LEVEL,0) REL_LEVEL,b.COL_ID " +
                "FROM META_GDL_REL a LEFT JOIN META_GDL_TBL_REL b ON a.PAR_GDL_ID=b.GDL_ID " +
                "WHERE a.GDL_ID=?";  //获取所有直接父的关系
        String supSql = "SELECT A.DIM_TYPE_ID,A.COL_ID,MAX(A.DIM_LEVEL) FROM META_TABLE_COLS A " +
                "WHERE A.COL_STATE=1 AND A.TABLE_ID=? AND A.COL_BUS_TYPE=0 GROUP BY A.DIM_TYPE_ID,A.COL_ID";//查询关系表支撑的 维度

        List<Map<String,Object>> parGdls = getDataAccess().queryForList(parsql, gdlId);
        for(Map<String,Object> par : parGdls){
            List<Integer> parBinds = getDataAccess().queryForPrimitiveList(bindSql,Integer.class,par.get("GDL_TBL_REL_ID"),par.get("GDL_ID")); //父关系绑定条件
            List<Integer> parBindDims = getDataAccess().queryForPrimitiveList(bindSql,Integer.class,0,par.get("GDL_ID"));//父绑定维度
            Object[][] sups = getDataAccess().queryForArray(supSql,false,par.get("TABLE_ID"));
            Map<Integer,Integer> colMap = new HashMap<Integer,Integer>();  //绑定维度必须在此里面完全找到才可判定为继承
            for(Object[] o : sups)
                colMap.put(Convert.toInt(o[0]),Convert.toInt(o[1]));

            boolean flag = true; //当其变为false时，说明找到某个维度不支持
            for(Map.Entry<String,Object> entry : bindDims.entrySet()){
                Map<String,Object> bd = (Map<String,Object>)(entry.getValue());
                int bdtype = Convert.toInt(bd.get("typeId"));
                if(!colMap.containsKey(bdtype) && !parBinds.contains(Convert.toInt(entry.getKey()))){
                    flag = false;
                    break;
                }
            }
            if(flag){
                insertCopyParentRel(par,parBinds,parBindDims,gdlId,bindDims,colMap);
            }
        }
        //加入gdl_tbl_rel_id=0的记录，记录绑定维度
        String bindTermSql = "INSERT INTO META_GDL_TBL_REL_TERM(GDL_ID,DIM_TYPE_ID,GDL_TBL_REL_ID,DIM_TABLE_ID,DIM_LEVEL," +
                "DIM_COL_ID,DIM_CODE,ORDER_ID) VALUES(?,?,?,?,?,?,?,?) ";
        for(Map.Entry<String,Object> entry : bindDims.entrySet()){
            Map<String,Object> bd = (Map<String,Object>)(entry.getValue());
            int typeId = MapUtils.getInteger(bd,"typeId");
            int tableId = MapUtils.getInteger(bd,"tableId");
            int level = MapUtils.getInteger(bd,"level");
            String codes = MapUtils.getString(bd,"codes");
            int orderId = MapUtils.getInteger(bd,"orderId");
            getDataAccess().execUpdate(bindTermSql,gdlId,typeId,0,tableId,level,0,codes,orderId);//添加一条为0的记录
        }

        return true;
    }

    /**
     * 继承某指标维度方法(可用于复合指标创建 和 修改版本)
     * @param gdlId 当前指标
     * @param version 当前指标版本
     * @param baseId 参照点(可能是直接的父，也可能是基础指标，无影响)
     *               同一基础指标下的所有子复合指标都会继承基础指标的维度方法
     * @param baseVersion 参照点的版本
     * @return
     */
    public boolean extendGdlMethod(int gdlId,int version,int baseId,int baseVersion){
        String sql = "INSERT INTO META_GDL_DIM_GROUP_METHOD(GDL_VERSION,GDL_ID," +
                "DIM_TABLE_ID,GROUP_METHOD) " +
                "SELECT ?,?,DIM_TABLE_ID,GROUP_METHOD " +
                "FROM META_GDL_DIM_GROUP_METHOD WHERE GDL_VERSION=? AND GDL_ID=? ";
        getDataAccess().execUpdate(sql,version,gdlId,baseVersion,baseId);
        return true;
    }

    /**
     * 插入复制的父指标关系
     * @param parRelMap 父关系记录
     * @param parRelBinds 父关系绑定条件
     * @param parBindDims 父绑定维度
     * @param gdlId 新指标ID
     * @param bindDims 绑定维度数量
     * @param colMap 继承关系表的 维度列
     * @return
     */
    public boolean insertCopyParentRel(Map<String,Object> parRelMap,List<Integer> parRelBinds,List<Integer> parBindDims,int gdlId,Map<String,Object> bindDims,Map<Integer,Integer> colMap){
        int relTableId = MapUtils.getInteger(parRelMap,"TABLE_ID");
        int relLevel = MapUtils.getInteger(parRelMap,"REL_LEVEL");
        int pk = getDataAccess().queryForIntByNvl("SELECT GDL_TBL_REL_ID FROM META_GDL_TBL_REL WHERE TABLE_ID=? AND GDL_ID=?", 0, relTableId, gdlId);
        if(pk!=0)return false;//说明关系已添加

        String termSql = "INSERT INTO META_GDL_TBL_REL_TERM(GDL_ID,DIM_TYPE_ID,GDL_TBL_REL_ID,DIM_TABLE_ID,DIM_LEVEL," +
                "DIM_COL_ID,DIM_CODE,ORDER_ID) VALUES(?,?,?,?,?,?,?,?) ";
        String insertSql = "INSERT INTO META_GDL_TBL_REL(GDL_TBL_REL_ID,TABLE_ID,GDL_ID," +
                "REL_TYPE,REL_LEVEL,COL_ID) VALUES(?,?,?,?,?,?) ";
        pk = (int) queryForNextVal("SEQ_GDL_TBL_REL_ID");
        getDataAccess().execUpdate(insertSql,pk,relTableId,gdlId,1,relLevel+bindDims.size()-parBindDims.size(),parRelMap.get("COL_ID"));//继承关系\
        for(Map.Entry<String,Object> entry : bindDims.entrySet()){
            Map<String,Object> bd = (Map<String,Object>)(entry.getValue());
            int tableId = MapUtils.getInteger(bd,"tableId");
            if(!parRelBinds.contains(tableId) && parBindDims.contains(tableId)){
                //如果父关系表不包含某维度，需要判断不包含的此维度是子新复合的，还是自身直接取数已经汇总的
                //如果是自身直接取数已经汇总的，那么子继承此关系也不需要添加条件
                continue;
            }
            int typeId = MapUtils.getInteger(bd,"typeId");
            int level = MapUtils.getInteger(bd,"level");
            String codes = MapUtils.getString(bd,"codes");
            int colId = colMap.get(typeId);
            int orderId = MapUtils.getInteger(bd,"orderId");
            getDataAccess().execUpdate(termSql,gdlId,typeId,pk,tableId,level,colId,codes,orderId);//继承条件
        }

        return true;
    }

    /**
     * 分发指标关系到子指标 (父添加一个关系时，需要向子也添加关系)
     * @param gdlId 当前指标
     * @param relId 新加入的关系
     *              动态去父子关系表找到所有子指标，然后把当前指标新加入的关系分发下去(包含计算层级)
     * @return
     */
    public boolean assignGdlRelToChild(int gdlId,int relId){

        return false;
    }



}
