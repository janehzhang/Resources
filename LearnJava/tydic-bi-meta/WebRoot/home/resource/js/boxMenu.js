/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        boxMenu.js
 *Description：
 *    盒子菜单
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

/**
 * 盒子菜单类
 * @param 盒子的html ID
 * @param onlyone 是否只有一个展开
 */
var BoxMenu = function(id,onlyone){
    if(!document.getElementById || !document.getElementsByTagName){return false;}
    this.menu=document.getElementById(id);
    this.submenu=this.menu.getElementsByTagName("ul");
    this.speed=20;
    this.time=10;
    this.onlyone=onlyone||false;

    //初始
    this.init=function(){
        var mainInstance = this;
        for(var i=0;i<this.submenu.length;i++){
            this.submenu[i].getElementsByTagName("span")[0].onclick=function(){
                mainInstance.toogleBox(this.parentNode);
            };
        }
    };

    //切换盒子
    this.toogleBox=function(submenu){
        if(submenu.className=="open"){
            this.closeBox(submenu);
        }else{
            this.openBox(submenu);
        }
    };

    //打开一个盒子
    this.openBox=function(submenu){
        var fullHeight=submenu.getElementsByTagName("span")[0].offsetHeight;
        var links = submenu.getElementsByTagName("a");
        for (var i = 0; i < links.length; i++){
            fullHeight += links[i].offsetHeight;
        }
        var moveBy = Math.round(this.speed * links.length);
        var mainInstance = this;
        var intId = setInterval(function() {
            var curHeight = submenu.offsetHeight;
            var newHeight = curHeight + moveBy;
            if (newHeight <fullHeight){
                submenu.style.height = newHeight + "px";
            }else {
                clearInterval(intId);
                submenu.style.height = "";
                submenu.className = "open";
            }
        }, this.time);
        this.collapseOthers(submenu);
    };

    //关闭一个盒子
    this.closeBox=function(submenu){
        var minHeight=submenu.getElementsByTagName("span")[0].offsetHeight;
        var moveBy = Math.round(this.speed * submenu.getElementsByTagName("a").length);
        var mainInstance = this;
        var intId = setInterval(function() {
            var curHeight = submenu.offsetHeight;
            var newHeight = curHeight - moveBy;
            if (newHeight > minHeight){
                submenu.style.height = newHeight + "px";
            }else {
                clearInterval(intId);
                submenu.style.height = "";
                submenu.className = "";
            }
        }, this.time);
    };

    //关闭其他盒子
    this.collapseOthers = function(submenu){
        if(this.onlyone){
            for (var i = 0; i < this.submenu.length; i++){
                if (this.submenu[i] != submenu){
                    this.closeBox(this.submenu[i]);
                }
            }
        }
    };

    /**
     * 向盒子里面插入一个菜单
     * @param idx 盒子序号
     * @param li
     */
    this.insertMenuToBox = function(idx,li){
        this.submenu[idx].appendChild(li);
    };

    /**
     * 清空一个盒子里面的菜单
     * @param idx
     */
    this.clearMenu = function(idx){
        var lis = this.submenu[idx].getElementsByTagName("li");
        if(lis && lis.length>0){
            for(var i=0;i<lis.length;i++){
                this.submenu[idx].removeChild(lis[i]);
            }
        }
    };

    /**
     * 向盒子里面插入一个元素对象
     * @param idx 盒子序号
     * @param el 元素对象
     */
    this.insertObjToBox = function(idx,el){
        this.clearMenu(idx);
        this.submenu[idx].appendChild(el);
    };


};