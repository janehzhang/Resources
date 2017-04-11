/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       comboTree.js
 *Description：
 *       下拉树封装JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       张伟
 *Date：
 *       2012-04-26
 ********************************************************/
/**
 * 为一个Input框生成一颗下拉树
 * @param config
 * 配置如下：
 * {
 *   input:输入框，必填
 *   width:下拉树的宽，数字，未填则以input框的宽相同
 *   height:下拉树的高，数字，未填则默认为150，
 *   value:下拉树已经选择的值，为空表示未选择的值，如果有多个，以","号分隔
 *   name:下拉树已经选择值的名称，为空表示未选择的值，如果有多个,以"，"号分隔
 *   ansync:true/false,//异步执行 可选:默认为true,异步加载。
 *   onBeforeSelect:function(value,name),在选中之前的回调，返回TRUE代表可选，返回false不可选
 *   onValueSelect:function(values,names){}:当值被选中时的回调函数
 *   afterTreeDataLoad:function(){};//当树加载数据之后的回调函数
 *   isSingSelect:true/false:是否单选。默认单选
 *   loadDataUrl:加载树的根节点数据URL ，必选
 *   loadChildDataUrl:加载树的子节点数据URL，不填默认为loadDataUrl
 *   treeConverter:TREE数据转换器,必填
 * }
 */
var comboTree = function (config) {
    //参数检查
    var defaultValue = {

    }
    /**
     * 获取TREE对象
     */
    this.getTree = function () {

    }
    /**
     * 获取Input节点
     */
    this.getInput = function () {

    }
    /**
     * 获取选择的数据
     */
    this.getSelectValue = function () {

    }
    /**
     * 获取选择的名称
     */
    this.getSelectName = function () {

    }
    /**
     * 显示树
     */
    this.showTree = function () {

    }
    /**
     * 隐藏树
     */
    this.hideTree = function () {

    }
}
