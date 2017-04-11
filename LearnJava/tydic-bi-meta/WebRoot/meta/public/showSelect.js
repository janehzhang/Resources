/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description  弹出选择JS 提供系统跨模块选择如选择表类，选择维度，选择地域 ……等一切弹出选择层
 * @date 12-3-31
 * -
 * @modify
 * @modifyDate
 * -
 */

//导入DWRJS，本JS提供的跨模块选择，为了保证本JS一些DWR访问方法能被顺利执行，提供一个导入DWRJS的方法
var importDWRJS = function(dwrAction){
    if(!window[dwrAction]){
        dhx.require(getBasePath()+"/dwr/interface/" + dwrAction + ".js");
    }
};

var ShowSelect = new Object();//选择函数命名空间
ShowSelect.EmptyObject = {};
ShowSelect.EmptyList = [];
ShowSelect.EmptyFunction=new Function();

//选择表类
ShowSelect.selectTable = function () {

};
