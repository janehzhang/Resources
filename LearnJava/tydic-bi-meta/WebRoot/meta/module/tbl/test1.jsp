<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-5-10
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="tydic.meta.common.JsonUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="tydic.meta.module.mag.login.LoginConstant" %>
<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="tydic.meta.module.mag.user.UserConstant" %>
<%@ page import="tydic.meta.web.session.SessionManager" %>
<html>
<head>
    <title></title>
    <%@include file="../../public/header.jsp"%>
    <script type="text/javascript">
        var Debug = function(txt){
            $("deg").innerHTML = txt+"<br>"+$("deg").innerHTML;
        };
        dhtmlx.image_path = getDefaultImagePath();
        dhtmlx.skin = getSkin();
        window.onload = function(){
            var metaTerm = TermReqFactory.createTermReq(1);
            var term1=metaTerm.createTermControl("a","local_code");//下拉框
//            var term2=metaTerm.createTermControl("b","date_no");//日期
            var term3=metaTerm.createTermControl("c","zone_id");//下拉树
            var term4=metaTerm.createTermControl("d","area_id");//下拉框联动
            var term5=metaTerm.createTermControl("e","term5");//异步加载树
            var term6=metaTerm.createTermControl("f","term6");//下拉框联动

            term1.setListRule(1,"select z.zone_code,z.zone_name from meta_dim_zone z where z.dim_level=2 and z.dim_type_id=4","028",0);
//            term2.setDateRule(1,"select z.date_code,z.date_name from meta_dim_date z \
//        where z.dim_level=3 and z.dim_type_id=1 and z.date_code<to_char(sysdate,'yyyymmdd') \
//        order by z.date_code desc ","2012-05-12");
//            term3.setTreeRule(1,"select z.zone_code,z.zone_name,z.zone_par_code from meta_dim_zone z where  z.dim_type_id=4 order by 3,1","0000");
            term4.setTreeRule(1,"select z.zone_code,z.zone_name,z.zone_par_code from meta_dim_zone z where  z.dim_type_id=4 and z.zone_code='{LOCAL_CODE}'");
            term5.setTreeRule(1,"select z.zone_code,z.zone_name,z.zone_par_code from meta_dim_zone z where  z.dim_type_id=4 and z.zone_par_code='{AREA_ID}'");
//            term1.setDimRule(2,4,'028',2,0,"","0827");
            term6.setDimRule(2,4,"0838",[3],[],"","");
//            term6.setDimRule(2,4,'0000',[],0,'0000');
//            term6.setDimRule({
//                dimTableId:2
//            });

            term4.setParentTerm("local_code");
            term5.setParentTerm("area_id");
            dhx.showProgress("请求数据中");
            metaTerm.init(function(termVals){
                dhx.closeProgress();
                var msg="";
                for(var term in termVals){
                    if(termVals[term])
                        msg+=term+":"+termVals[term]+",";
                }
                Debug("callBack参数值："+msg);

                dataTable.setFlashCall(testfun);
                dataTable.flashData();
            });
//            term3.selTree.setCheckboxFlag(2);//up

            var testfun = function(dt,params){
                dhx.showProgress("请求数据中");
                Debug("sort:"+params.sort);
                var termVals=TermReqFactory.getTermReq(1).getKeyValue();
                termVals["COLUMN_SORT"] = params.sort;
                TermControlAction.testquery(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
                    dhx.closeProgress();
                    dt.bindData(data.list,data.total);

                });

            };

                /*dataTable.flashData(function(dt,params){
                 dhx.showProgress("请求数据中");
                 Debug("sort:"+params.sort);
                 TermControl.testquery({},{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
                 dhx.closeProgress();
                 dt.bindData(data.list,data.total);

                 });
                 });*/




            var dataTable = new meta.ui.DataTable("x");
            dataTable.setColumns({
                id:"编号",
                name:"名称",
                status:"状态",
                field1:"字段1",
                field2:"字段2",
                field3:"操作"
            });
            dataTable.setPaging(true,4,0);
            dataTable.setSorting(true,{
                id:"asc",
                name:""  ,
                field1:"desc",
                field2:""
            },2);
            dataTable.render();
            dataTable.setFormatCellCall(function(rid,cid,data){
                if(cid==5){
                    return "<a href='javascript:void(0)' onclick='alert(this.innerHTML);return false;'>"+data[cid]+"</a>";
                }

                return data[cid];
            });
            dataTable.setFormatRowCall(function(r,data){
                if(r.idd==2){
                    r.style.height = "55px";
                }

                return r;
            });

//            dataTable.grid.enableDragAndDrop(true);
            dataTable.grid.attachEvent("onRowSelect",function(id,ind){
//                Debug("id:"+id+">ind:"+ind);
                Debug("userData:"+this.MetaDataTable.getUserData(id,ind));
            });

        };

    </script>
</head>
<body style='width:100%;height:100%;'>
<div style="width:1500px;height:1000px;background-color:#f4fac9;padding-top:100px;">
    <div style="width:200px;height:20px;position:relative;top:700px;left:1400px;" id="g"></div>
</div>
<div style="position:absolute;top:10px;left:10px;">
<div style="width:150px;height: 40px;" id="a"></div>
</div>
<div style="position:absolute;top:10px;left:210px;">
    <div style="width:500px;" id="b">
        <table class="MetaTermTable" border="0" cellpadding="0" cellspacing="0">
            <colgroup>
                <col width="15%"><col width="35%">
                <col width="15%"><col width="35%">
            </colgroup>
            <tr>
                <th>字段一</th>
                <td><input type="text" name="aaa" value=""></td>
                <th>字段二</th>
                <td><select name="bbb"><option></option></select></td>
            </tr>
            <tr>
                <th>字段一</th>
                <td><input type="text" name="aaa" value=""></td>
                <th>字段二</th>
                <td><select name="bbb"><option></option></select></td>
            </tr>
            <tr>
                <td colspan="4">
                    <input type="button" value="新增">
                    <input type="button" value="保存">
                </td>
            </tr>
        </table>
        <br>
        <%--新增维护表单模板--%>
        <table class="MetaFormTable" border="0" cellpadding="0" cellspacing="1">
            <colgroup>
                <col width="15%"><col width="35%">
                <col width="15%"><col width="35%">
            </colgroup>
            <tr>
                <th>字段一</th>
                <td><input type="text" name="aaa" value=""></td>
                <th>字段二</th>
                <td><select name="bbb"><option></option></select></td>
            </tr>
            <tr>
                <th>字段一</th>
                <td><input type="text" name="aaa" value=""></td>
                <th>字段二</th>
                <td><select name="bbb"><option></option></select></td>
            </tr>
            <tr>
                <td colspan="4">
                    <input type="button" value="新增">
                    <input type="button" value="保存">
                </td>
            </tr>
        </table>
    </div>
</div>
<div style="position:absolute;top:10px;left:800px;">
    <div style="width:300px;height: 40px;" id="c"></div>
</div>
<div style="position:absolute;top:200px;left:10px;">
    <div style="width:230px;height: 40px;" id="d"></div>
</div>
<div style="position:absolute;top:400px;left:900px;">
    <div style="width:210px;height: 20px;" id="e"></div>
</div>
<div style="position:absolute;top:200px;left:800px;">
    <div style="width:200px;height: 20px;background-color:red;" id="f"></div>
</div>
<div style="position:absolute;top:300px;left:5px;">
    <div style="width:170px;height: 20px;background-color:red;" id="deg"></div>
</div>
<div style="position:absolute;top:300px;left:200px;">
    <div style="width:700px;height: 300px;background-color:#ffffff;" id="x"></div>
</div>
</body>
</html>