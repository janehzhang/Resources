package tydic.meta.module.mag.favorite.gdl;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description 收藏指标DAO
 * @date 12-3-29
 * -
 * @modify
 * @modifyDate -
 */
public class FavGdlDAO extends MetaBaseDAO{

    /**
     * 根据收藏夹ID查询所有收藏指标
     * @param favId
     * @return
     */
    public List<Map<String,Object>> queryFavGuidlineByFavId(int favId){
        String sql = "SELECT A.GDL_FAVORITE_ID,A.GDL_ID,A.FAVORITE_ID,A.USER_ID," +
                "to_char(A.FAVORITE_TIME,'yyyy-mm-dd hh24:mi:ss') AS FAVORITE_TIME,A.FAVORITE_ORDER," +
                "B.GDL_NAME " +
                "FROM META_GDL_USER_FAVORITE A LEFT JOIN META_GUILD_LINE B ON A.GDL_ID=B.GDL_ID WHERE A.FAVORITE_ID=? " +
                "ORDER BY A.FAVORITE_REPORT_ORDER ASC";
        return getDataAccess().queryForList(sql,favId);
    }

    /**
     * 根据收藏夹ID查询所有收藏指标
     * @param favIds
     * @return
     */
    public List<Map<String,Object>> queryFavGuidlineByFavIds(int[] favIds){
        String sql = "SELECT A.GDL_FAVORITE_ID,A.GDL_ID,A.FAVORITE_ID,A.USER_ID," +
                "to_char(A.FAVORITE_TIME,'yyyy-mm-dd hh24:mi:ss') AS FAVORITE_TIME,A.FAVORITE_ORDER," +
                "B.GDL_NAME " +
                "FROM META_GDL_USER_FAVORITE A LEFT JOIN META_GUILD_LINE B ON A.GDL_ID=B.GDL_ID " +
                "WHERE A.FAVORITE_ID IN" + SqlUtils.inParamDeal(favIds) +
                " ORDER BY A.FAVORITE_REPORT_ORDER ASC";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 收藏指标
     * @param data
     * @return
     */
    public int insertFavoriteGuidline(Map<String,Object> data){
        String sql = "INSERT INTO META_GDL_USER_FAVORITE "
                + "(GDL_FAVORITE_ID,GDL_ID,FAVORITE_ID,USER_ID,FAVORITE_TIME,FAVORITE_ORDER) "
                + "SELECT ?,?,?,?,TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'),NVL(MAX(FAVORITE_ORDER),1)+1 FROM META_GDL_USER_FAVORITE";
        List<Object> proParams = new ArrayList<Object>();
        long pk = queryForNextVal("SEQ_GDL_USER_FAVORITE_ID");
        proParams.add(pk);
        if(data.containsKey("objId")) proParams.add(Integer.parseInt(String.valueOf(data.get("objId"))));
        else proParams.add(0);
        if(data.containsKey("favoriteType")) proParams.add(Integer.parseInt(String.valueOf(data.get("favoriteType"))));
        else proParams.add(0);
        if(data.containsKey("userId")) proParams.add(Integer.parseInt(String.valueOf(data.get("userId"))));
        else proParams.add(0);
        proParams.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//时间
        getDataAccess().execUpdate(sql, proParams.toArray());
        return (int)pk;
    }

    /**
     * 修改收藏指标的分类（主要是收藏分类树 拖动）
     * @param id
     * @param type
     * @return
     */
    public int updateFavoriteGuidlineType(int id,int type){
        String sql = "UPDATE META_GDL_USER_FAVORITE SET FAVORITE_ID=? WHERE GDL_FAVORITE_ID=?";
        return getDataAccess().execUpdate(sql,type,id);
    }

    /**
     * 删除收藏指标
     * @param id
     * @return
     */
    public int deleteFavoriteGuidline(int id){
        String sql = "DELETE FROM META_GDL_USER_FAVORITE WHERE GDL_FAVORITE_ID=?";
        return getDataAccess().execUpdate(sql,id);
    }

    /**
     * 返回某收藏夹某指标数量。用于验证
     * @param reportId
     * @param userId
     * @return
     */
    public int countFavoriteGuidlineNum(int reportId,int userId){
        String sql = "SELECT COUNT(1) FROM META_GDL_USER_FAVORITE WHERE USER_ID=? AND GDL_ID=?";
        return getDataAccess().queryForInt(sql,userId,reportId);
    }

    /**
     * 修改收藏指标的排序值
     * @param id
     * @param ot true升，false降
     * @return
     */
    public int updateFavoriteGuidlineOrder(int id,int favId,boolean ot){
        String sql = "SELECT A.GDL_FAVORITE_ID,A.FAVORITE_ORDER FROM META_GDL_USER_FAVORITE A WHERE A.GDL_FAVORITE_ID=? " +
                "OR (A.FAVORITE_ID=? AND A.FAVORITE_ORDER=(SELECT "+(ot?"MAX":"MIN")+"(FAVORITE_ORDER) " +
                "FROM META_GDL_USER_FAVORITE WHERE FAVORITE_ID=? " +
                "AND FAVORITE_ORDER"+(ot?"<":">")+"(SELECT FAVORITE_ORDER FROM META_GDL_USER_FAVORITE WHERE GDL_FAVORITE_ID=?)))";
        List<Map<String,Object>> datas = getDataAccess().queryForList(sql,id,favId,favId,id);
        String updateSql = "UPDATE META_GDL_USER_FAVORITE SET FAVORITE_ORDER=? WHERE GDL_FAVORITE_ID=?";
        if(datas.size()>1){
            int oid = 0;  //原ID
            int order = 0;//原排序ID
            int _order = 0;//交换排序ID
            List<Integer> _ids = new ArrayList<Integer>();//交换ID集
            for(Map<String,Object> map : datas){
                int mid = Integer.parseInt(String.valueOf(map.get("GDL_FAVORITE_ID")));
                int morder = Integer.parseInt(String.valueOf(map.get("FAVORITE_ORDER")));
                if(mid==id){
                    oid = mid;
                    _order = morder;
                }else{
                    _ids.add(mid);
                    order = morder;
                }
            }
            Object[][] proParamses=new Object[_ids.size()+1][2];
            proParamses[0][0] = order;
            proParamses[0][1] = oid;
            for(int x=0;x<_ids.size();x++){
                proParamses[x+1][0] = _order;
                proParamses[x+1][1] = _ids.get(x);
            }
            getDataAccess().execUpdateBatch(updateSql, proParamses);
            return 1;
        }
        return 0;
    }

}
