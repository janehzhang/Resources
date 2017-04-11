<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-5-28
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title></title>
    <%@include file="../../public/header.jsp"%>
    <script type="text/javascript">
        var getSkin = function () {
            // 根据session中的skin配置获取用户的皮肤设置。
            return "dhx_skyblue";
        }
        var getDefaultImagePath = function () {
            return "<%=request.getContextPath()%>/meta/resource/dhtmlx/imgs/";
        }
        dhtmlx.image_path = getDefaultImagePath();
        dhtmlx.skin = getSkin();
        window.onload = function(){
//            var combo = new dhtmlXCombo("a","aaa",200);
//            var op = [{value:"0",text:""},{value:"1",text:"abcab前树加载前aa"},{value:"2",text:"xyz"}];
//            combo.addOption(op);
//            combo.enableFilteringMode(true);
//            combo.enableOptionAutoPositioning(true);
////            combo.setAutoOpenListWidth(true);
////            combo.readonly(true,true);
//

            var selTree = new meta.ui.selectTree(["b","c","d","e"]);
            selTree.bind("a");
            var data = new Array();
            for(var i=0;i<10;i++){
                var d = new Array();
                d[d.length] = "id_"+i;
                d[d.length] = "text_"+i;
                d[d.length] = 0;
                for(var j=0;j<5;j++){
                    d[d.length] = "附加_"+j;
                }
                data[data.length] = d;
            }
            selTree.appendData(data);

            selTree.tree.attachEvent("onDblClick",function(itemid){
                selTree.setValue(selTree.getItemValue(itemid));
            });


            selTree.enableSearch(true,true);
            selTree.enableAutoSize(true);
            selTree.setBindObjWidth("d",250);
            selTree.removeBindObj("b");
        };
    </script>
</head>
<body style='width:100%;height:100%;overflow:inherit;background-color:#f4fac9;'>
<div style="position:absolute;top:10px;left:10px;">
    <div style="width:150px;height: 40px;" id="a"></div>
</div>
<div style="position:absolute;top:10px;left:600px;">
    <div style="width:200px;height: 40px;" id="b"></div>
</div>
<div style="position:absolute;top:500px;left:20px;">
    <div style="width:300px;height: 40px;" id="c"></div>
</div>
<div style="position:absolute;top:500px;left:600px;">
    <div style="width:230px;height: 40px;" id="d"></div>
</div>
</body>
</html>