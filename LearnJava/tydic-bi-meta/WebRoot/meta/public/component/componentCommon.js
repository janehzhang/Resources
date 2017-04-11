/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        componentCommon.js.js
 *Description：通用组件实现
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

var meta = new Object();
meta.ui = new Object(); //组件函数命名空间
//定义组件全局变量
meta.ui.comboActiveBindID = {};//记录每个combo组件当前处于活动状态的绑定txt
meta.ui.selectTreeActiveBindID = {};
meta.ui.selectGridActiveBindID = {};

/**
 * 所有下拉选择类型的基类，包含下拉选择树，下拉选择框，下拉选择表格，代码补全等。
 */
meta.ui.baseCombo = function (cfg) {
    this.multiselect = false; // 选择模式，true多选，false单选
    this.inputEvent = true;     //输入时动态过滤，可以关闭此属性
    this.selectedValue = null;  //选择的值对象 包含至少两基本键:value,text
    this.selectedIndex = -1;     //默认选中项索引

    if(cfg){
        dhx.extend(this,cfg,true);
    }
    /**
     * BUILD核心方法,不可重写。 子类直接调用此方法进行初始化
     */
    this.baseInit = function () {
        this.selectedInputValue = {};  //记录每个组件的input选择值
        this.div = dhx.html.create("div", {style:"display;none;position:absolute;padding:0;margin:0;" +
            "overflow-x:hidden;overflow-y:auto;border:1px #8f99a2 solid;background-color:white;z-index:1000"});
        this.div.id = "D_"+this.id;
        document.body.appendChild(this.div);

        //初始化方法
        this.afterInit();
    };
    this.id = "CB_" + dhx.uid();
    var that = this;
    //绑定一个文本框
    this.bind = function(txt){
        var inp = $(txt);
        var txtid = inp.id;
        if(!txtid){
            txtid = "inp_"+this.id;
            inp.id = txtid;
        }
        var span = document.createElement("SPAN");
        span.style.position = "relative";
        span.className = "inputspan";
        span.id = txtid+"_"+this.id;
        var mw = parseInt(inp.offsetWidth <= 0 ? inp.style.width.replace("px", "") : inp.offsetWidth); //文本框宽度
        var mh = parseInt(inp.offsetHeight <= 0 ? inp.style.height.replace("px", "") : inp.offsetHeight);//文本框高度
        inp.parentNode.replaceChild(span, inp);
        inp.style.borderWidth = 0+"px";
        span.appendChild(inp);
        var im = document.createElement("SPAN"); //下拉图标
        im.style.position = "absolute";
        im.className = "chooseInp";
        im.style.right = 1+"px";
        im.style.top = 1+"px";
        span.appendChild(im);
        span.style.width = mw + "px";
        span.style.height = mh - 1 + "px";
        span.style.lineHeight = mh - 1 + "px";
        inp.style.width = mw - im.offsetWidth - 2 + "px" ;
        im.onclick = function(){that.showAll();};

        that.selectedInputValue[txtid] = {};
        Tools.addEvent(inp, "click", function () {
            if(meta.ui.comboActiveBindID[that.id]!=txtid){
                meta.ui.comboActiveBindID[that.id] = txtid;
                that.position(inp,false);
            }
        });

        if(that.inputEvent){
            //注册相应事件
            inp.old_value = inp.value;
            Tools.addEvent(inp, "keyup", function (e) {
                var event = e || window.event;
                var keyCode = event.keyCode;
                if (keyCode == 38) {
                    that.inputKeyUp(inp);
                }else if (keyCode == 40) {
                    that.inputKeyDown(inp);
                }else if (keyCode == 13) {
                    that.inputKeyEnter(inp);
                }else{
                    var value = inp.value;
                    if(value!=inp.old_value){
                        that.searchFilter(inp);
                    }
                    inp.old_value = value;
                }
            });
        }
    };
    //弹出并定位，根据input框 ，当flag为true时，显示
    this.position = function(target,flag){
        if(target.type=="text"){
            target = target.parentNode;
        }
        var div = this.div;
        if(!this.mouseRange){
            var offset = dhx.html.offset(target);
            var divHeight = parseInt(div.offsetHeight <= 0 ? div.style.height.replace("px", "") : div.offsetHeight);
            var divWidth = parseInt(div.offsetWidth <= 0 ? div.style.width.replace("px", "") : div.offsetWidth);
            //判断弹出层是否向上飘还是向下飘。
            var top = 0;
            //定义鼠标范围。
            //鼠标在target区域定义
            var offsetTargetStart = {};
            var offsetTargetEnd = {};
            //鼠标在div层中区域定义。
            var offsetDivStart = {};
            var offsetDivEnd = {};
            offsetTargetStart.x = offset.x - 5;
            offsetTargetStart.y = offset.y;
            offsetTargetEnd.x = offset.x + target.offsetWidth + 20 ;
            offsetTargetEnd.y = offset.y + target.offsetHeight + 5 ;
            offsetDivStart.x = offset.x - 5;
            offsetDivEnd.x = offset.x + divWidth + 5;
            if ((offset.y + target.offsetHeight + divHeight) > document.body.clientHeight) {
                top = offset.y - divHeight + 1;
                offsetDivStart.y = top - 5;
                offsetDivEnd.y = offset.y;
            } else {
                top = offset.y + target.offsetHeight - 1;
                offsetDivStart.y = offset.y + target.offsetHeight;
                offsetDivEnd.y = offset.y + target.offsetHeight + divHeight + 5;
            }
            div.style.left = (offset.x) + 'px';
            div.style.top = top + "px";
            div.style.width = target.offsetWidth - 2+"px";
            this.mouseRange = {
                targetS:offsetTargetStart,
                targetE:offsetTargetEnd,
                divS:offsetDivStart,
                divE:offsetDivEnd
            };
        }
        if(flag){div.style.display = "block";}
        else{div.style.display="none";}
    };
    //判断当前鼠标是否在活动文本框和层之内，在之内返回true
    this.isInMouseRange = function(ev){
        if(this.mouseRange){
            ev = ev || window.event;
            var mousePos = Tools.mousePosition(ev);
            return (((mousePos.x >= this.mouseRange.targetS.x && mousePos.y >= this.mouseRange.targetS.y) &&
                (mousePos.x <= this.mouseRange.targetE.x && mousePos.y <= this.mouseRange.targetE.y)) ||
                ((mousePos.x >= this.mouseRange.divS.x && mousePos.y >= this.mouseRange.divS.y) &&
                    (mousePos.x <= this.mouseRange.divE.x && mousePos.y <= this.mouseRange.divE.y)));
        }
        return false;
    };
    //隐藏层
    this.hide = function(){
        this.div.style.display = "none";
        this.mouseRange = null;
    };
    //展开所有
    this.showAll = function(){};

    //隐藏文本框
    this.hideInput = function(txtid){
        document.getElementById(txtid+"_"+this.id).style.display = "none";
    };
    /**
     * 初始化完成之后的操作，在这里可以注册事件，可以进行class绑定等等操作，需子类实现
     */
    this.afterInit = function () {};
    /**
     * 当Input键盘按向下键的执行方法，需实现。
     * @param input
     */
    this.inputKeyDown = function (input) {};
    /**
     * 当Input键盘按向上键的相应事件，需实现。
     * @param input
     */
    this.inputKeyUp = function (input) {};
    /**
     * 键盘按下键的相应事件，需实现
     * @param input
     */
    this.inputKeyEnter = function (input) {};

    //搜索过滤
    this.searchFilter = function(inp){
        document.getElementById("debugdiv").innerHTML = inp.value;
        this.div.innerHTML = inp.value;
        this.position(inp,true);
    };
    /**
     * 初始化值
     * @param vaule
     * @param name
     */
    this.initValue = function (value, name) {
        this.selectedValue = {
            value:value,
            name:name
        };
    };
    //获取选择的值
    this.getSelectedValue = function (txtid) {
        if(txtid){
            return this.selectedInputValue[txtid];
        }else{
            return this.selectedValue;
        }
    };
    //获取当前活动输入框
    this.getCurrentOptInput = function () {
        return document.getElementById(meta.ui.comboActiveBindID[that.id]);
    };
};
/**
 * 基础下拉输入选择框
 * @param cfg
 */
meta.ui.baseCombobox = function (cfg) {
    this.asyn = true;           //异步加载数据
    this.asynURL = null;        //异步加载URL
    this.maxDataSize = 30;       //默认最大数据量大小
    this.initData = null;        //初始数据
    this.notFoundInsertURL = null;  //为找到数据时插入Action
    this.selectedCall = null;   //选择回调
    this.beforeLoadCall = null;//加载前回调

    if(cfg){
        dhx.extend(this,cfg,true);
    }
    meta.ui.baseCombo.call(this,cfg);//继承基类的属性和方法

    /*******下面开始初始组件********/
    this.afterInit = function(){


    };
    //↓键
    this.inputKeyDown = function (input) {
        alert(1);
    };
    //↑键
    this.inputKeyUp = function (input) {

    };
    //回车
    this.inputKeyEnter = function (input) {

    };
    /****下面定义处理数据的方法****/
    //插入一条数据
    this.insertData = function(idx,value,name){

    };

    this.baseInit();
};

/**
 * 基础下拉输入选择树
 * @param cfg
 */
meta.ui.baseComboTree = function (cfg) {
    this.asyn = false;           //异步加载数据
    this.asynURL = null;        //异步过滤URL
    this.maxLevel = 2;       //默认每次展开最大层级
    this.selectedCall = null;   //选择回调
    this.beforeLoadCall = null;//加载前回调
    this.dataTree = null;        //数据树
    this.notFoundInsertURL = null;  //为找到数据时插入Action
    this.insertParent = 0;          // notFoundInsertURL生效时，插入的节点的父,可以是值，也可是函数

    this.getTree = function () {
        return this.dataTree;
    };

    /*******下面开始初始组件********/
    this.afterInit = function(){

    };
    //↓键
    this.inputKeyDown = function (input) {

    };
    //↑键
    this.inputKeyUp = function (input) {

    };
    //回车
    this.inputKeyEnter = function (input) {

    };
    this.baseInit();
};

/**
 * 基础输入选择表格
 * @param cfg
 */
meta.ui.baseComboGrid = function (cfg) {
    this.asynURL = null;        //动态过滤URL
    this.maxDataSize = 20;      //默认最大数据大小
    this.selectedCall = null;   //选择回调
    this.beforeLoadCall = null;//加载前回调

    /*******下面开始初始组件********/
    this.afterInit = function(){

    };
    //↓键
    this.inputKeyDown = function (input) {

    };
    //↑键
    this.inputKeyUp = function (input) {

    };
    //回车
    this.inputKeyEnter = function (input) {

    };
    this.baseInit();
};

/**
 * 基础弹出选择树
 * @param cfg
 */
meta.ui.baseSelectTree = function (cfg) {
    this.width = 500;
    this.height = 400;
    this.multiselect = false; // 选择模式，true多选，false单选
    this.maxLevel = 2;          //默认每次向下展开层级
    this.hideHeader = false;    //隐藏头区域
    this.hideFooter = true;    //隐藏脚区域
    this.modelState = true;     //模态窗口，默认true(透明层覆盖背景)
    this.selectedCall = null;     //选择回调
    this.showBeforeCall = null;     //弹出回调
    this.headerHTMLOBJ = null;      //头HTML对象
    this.footerHTMLOBJ = null;      //脚HTML对象

    if (cfg) {
        dhx.extend(this, cfg, true);
    }
    this.bind = function (txtid, ishidden) {
        var el = document.getElementById(txtid);
        var that = this;
        Tools.addEvent(el, "click", function () {
            meta.ui.selectTreeActiveBindID[that.id] = txtid;
            that.show();
        });
        if (!ishidden) {
            var elid = document.createElement("INPUT");
            elid.type = "hidden";
            elid.id = el.id + "_id";
            elid.value = "";
            el.parentNode.appendChild(elid);
        }
    };
    this.getSelectedValue = function (txtid) {
        return this.selectedValue;
    };
    this.getCurrentOptInput = function () {
        return document.getElementById(meta.ui.selectTreeActiveBindID[this.id]);
    };
    this.attachHeader = function (htmlid) {
        var th = document.getElementById("th_" + this.id);
        th.innerHTML = "";
        th.appendChild(document.getElementById(htmlid));
    };
    this.attachFooter = function (htmlid) {
        var tf = document.getElementById("tf_" + this.id);
        tf.innerHTML = "";
        tf.appendChild(document.getElementById(htmlid));
    };
    this.setPosition = function (left, top) {
        this.win.setPosition(left, top);
    };
    this.center = function () {
        this.win.center();
    };
    this.getWin = function () {
        return this.win;
    };
    this.getTree = function () {
        return this.dataTree;
    };
    this.show = function () {
        this.win.show();
        if (!this.dataTree) {
            var hdiv = document.getElementById("tqc_" + this.id);
            var cdiv = document.getElementById("tc_" + this.id);
            var fdiv = document.getElementById("tf_" + this.id);
            fdiv.style.height = this.footerHeight - 5 + "px";
            var bdiv = document.getElementById("tb_" + this.id);
            cdiv.style.height = (this.height - hdiv.offsetHeight - bdiv.offsetHeight - fdiv.offsetHeight - bdiv.offsetHeight - 5) + "px";
            cdiv.style.width = this.width - 20 + "px";
            fdiv.style.width = this.width - 20 + "px";
            document.getElementById("th_" + this.id).style.height = hdiv.offsetHeight + "px";
            hdiv.style.width = this.width - 70 + "px";
            this.init();
            if (!this.dataTree) {
                alert("未初始dataTree对象，弹出选择树初始失败！");
            }
        }
        if (that.showBeforeCall) {
            that.showBeforeCall();
        }
    };
    this.init = function () {};//等待子类实现

    /*******下面开始初始组件********/
    var that = this;
    this.id = "ST_" + dhx.uid();
    this.dataTree = null;

    //初始窗体
    this.win = (new dhtmlXWindows()).createWindow(this.id, 0, 0, this.width, this.height);
    this.win.stick();
    this.win.setText("弹出选择树");
    this.win.denyResize();
    this.win.denyPark();
//    this.win.denyMove();
    this.win.center();
    //关闭一些不用的按钮。
    this.win.button("minmax1").hide();
    this.win.button("park").hide();
    this.win.button("stick").hide();
    this.win.button("sticked").hide();
    this.win.attachEvent("onClose", function () {
        if (that.modelState) {
            this.setModal(false);
        }
        this.hide();
        return false;
    });
    this.win.attachEvent("onShow", function () {
        this.setModal(that.modelState);
    });

    //初始头脚区域
    var div = document.createElement("DIV");
    this.win.attachLayout("1C").cells("a").attachObject(div);
    div.innerHTML = document.getElementById("_SelectTree").innerHTML.replace(/\{id\}/g, this.id);
    if (this.hideHeader && this.hideFooter) {
        document.getElementById("th_" + this.id).style.display = "none";
        document.getElementById("tf_" + this.id).style.display = "none";
    } else if (this.hideHeader && !this.hideFooter) {
        document.getElementById("th_" + this.id).style.display = "none";
        document.getElementById("tf_" + this.id).style.display = "block";
    } else if (!this.hideHeader && this.hideFooter) {
        document.getElementById("th_" + this.id).style.display = "block";
        document.getElementById("tf_" + this.id).style.display = "none";
    } else if (!this.hideHeader && !this.hideFooter) {
        document.getElementById("th_" + this.id).style.display = "block";
        document.getElementById("tf_" + this.id).style.display = "block";
    }
    if (!this.hideHeader && this.headerHTMLOBJ) {
        if (typeof(this.headerHTMLOBJ) == "string") {
            document.getElementById("th_" + this.id).innerHTML = this.headerHTMLOBJ;
        } else {
            document.getElementById("th_" + this.id).appendChild(this.headerHTMLOBJ);
        }
    }
    if (!this.hideFooter && this.footerHTMLOBJ) {
        if (typeof(this.footerHTMLOBJ) == "string") {
            document.getElementById("tf_" + this.id).innerHTML = this.footerHTMLOBJ;
        } else {
            document.getElementById("tf_" + this.id).appendChild(this.footerHTMLOBJ);
        }
    }
    Tools.addEvent(document.getElementById("tquy_" + this.id), "click", function () {
        if (that.dataTree) {
            that.dataTree.reLoad();
        }
    });
    this.selectedValue = null;
    Tools.addEvent(document.getElementById("tsmt_" + this.id), "click", function () {
        if (that.dataTree) {
            //获取选择的记录
            //获取选择的记录
            that.selectedValue = that.returnValue();
            if (that.selectedValue) {
                var txt = document.getElementById(meta.ui.selectTreeActiveBindID[that.id]);
                if (txt != null) {
                    if (txt.type == "text") {
                        txt.value = that.selectedValue.text;
                        document.getElementById(meta.ui.selectTreeActiveBindID[that.id] + "_id").value = that.selectedValue.value;
                    } else if (txt.type == "hidden") {
                        txt.value = that.selectedValue.value;
                    }
                }
                if (that.selectedCall) {
                    that.selectedCall();
                }
                that.win.close();
            } else {
                alert("请选择！");
            }
        }
    });
    Tools.addEvent(document.getElementById("tcel_" + this.id), "click", function () {
        that.win.close();
    });

    this.win.hide();
};

/**
 * 基础弹出选择表格
 * @param cfg
 */
meta.ui.baseSelectGrid = function (cfg) {
    this.width = 500;
    this.height = 400;
    this.multiselect = false; // 选择模式，true多选，false单选
    this.pageSize = 10;
    this.hideHeader = false;    //隐藏头区域
    this.hideFooter = true;    //隐藏脚区域
    this.modelState = true;     //模态窗口，默认true(透明层覆盖背景)
    this.selectedCall = null;     //选择回调
    this.showBeforeCall = null;     //弹出回调
    this.headerHTMLOBJ = null;      //头HTML对象
    this.footerHTMLOBJ = null;      //脚HTML对象
    this.footerHeight = 100;        //脚高

    if (cfg) {
        dhx.extend(this, cfg, true);
    }
    this.bind = function (txtid, ishidden) {
        var el = document.getElementById(txtid);
        var that = this;
        Tools.addEvent(el, "click", function () {
            meta.ui.selectGridActiveBindID[that.id] = txtid;
            that.show();
        });
        if (!ishidden) {
            var elid = document.createElement("INPUT");
            elid.type = "hidden";
            elid.id = el.id + "_id";
            elid.value = "";
            el.parentNode.appendChild(elid);
        }
    };
    this.getSelectedValue = function (txtid) {
        return this.selectedValue;
    };
    this.getCurrentOptInput = function () {
        return document.getElementById(meta.ui.selectGridActiveBindID[this.id]);
    };
    this.attachHeader = function (htmlid) {
        var gh = document.getElementById("gh_" + this.id);
        gh.innerHTML = "";
        gh.appendChild(document.getElementById(htmlid));
    };
    this.attachFooter = function (htmlid) {
        var gf = document.getElementById("gf_" + this.id);
        gf.innerHTML = "";
        gf.appendChild(document.getElementById(htmlid));
    };
    this.setPosition = function (left, top) {
        this.win.setPosition(left, top);
    };
    this.center = function () {
        this.win.center();
    };
    this.getWin = function () {
        return this.win
    };
    this.getGrid = function () {
        return this.dataGrid;
    };
    this.show = function () {
        this.win.show();
        if (!this.dataGrid) {
            var hdiv = document.getElementById("gqc_" + this.id);
            var cdiv = document.getElementById("gc_" + this.id);
            var fdiv = document.getElementById("gf_" + this.id);
            fdiv.style.height = this.footerHeight - 5 + "px";
            var bdiv = document.getElementById("gb_" + this.id);
            cdiv.style.height = (this.height - hdiv.offsetHeight - bdiv.offsetHeight - fdiv.offsetHeight - bdiv.offsetHeight - 5) + "px";
            cdiv.style.width = this.width - 20 + "px";
            fdiv.style.width = this.width - 20 + "px";
            document.getElementById("gh_" + this.id).style.height = hdiv.offsetHeight + "px";
            hdiv.style.width = this.width - 70 + "px";
            this.init();
            if (!this.dataGrid) {
                alert("未初始dataGrid对象，弹出选择框初始失败！");
            }
        }
        if (that.showBeforeCall) {
            that.showBeforeCall();
        }
    };
    this.init = function () {};//等待具体业务组件子类实现

    /*******下面开始初始组件********/
    var that = this;
    this.id = "SG_" + dhx.uid();
    this.dataGrid = null;

    //初始窗体
    this.win = (new dhtmlXWindows()).createWindow(this.id, 0, 0, this.width, this.height);
    this.win.stick();
    this.win.setText("弹出选择框");
    this.win.denyResize();
    this.win.denyPark();
//    this.win.denyMove();
    this.win.center();
    //关闭一些不用的按钮。
    this.win.button("minmax1").hide();
    this.win.button("park").hide();
    this.win.button("stick").hide();
    this.win.button("sticked").hide();
    this.win.attachEvent("onClose", function () {
        if (that.modelState) {
            this.setModal(false);
        }
        this.hide();
        return false;
    });
    this.win.attachEvent("onShow", function () {
        this.setModal(that.modelState);
    });

    //初始头脚区域
    var div = document.createElement("DIV");
    var layout = this.win.attachLayout("1C");
    layout.cells("a").hideHeader();
    layout.cells("a").attachObject(div);
    div.innerHTML = document.getElementById("_SelectGrid").innerHTML.replace(/\{id\}/g, this.id);
    if (this.hideHeader && this.hideFooter) {
        document.getElementById("gh_" + this.id).style.display = "none";
        document.getElementById("gf_" + this.id).style.display = "none";
    } else if (this.hideHeader && !this.hideFooter) {
        document.getElementById("gh_" + this.id).style.display = "none";
        document.getElementById("gf_" + this.id).style.display = "block";
    } else if (!this.hideHeader && this.hideFooter) {
        document.getElementById("gh_" + this.id).style.display = "block";
        document.getElementById("gf_" + this.id).style.display = "none";
    } else if (!this.hideHeader && !this.hideFooter) {
        document.getElementById("gh_" + this.id).style.display = "block";
        document.getElementById("gf_" + this.id).style.display = "block";
    }
    if (!this.hideHeader && this.headerHTMLOBJ) {
        if (typeof(this.headerHTMLOBJ) == "string") {
            document.getElementById("gh_" + this.id).innerHTML = this.headerHTMLOBJ;
        } else {
            document.getElementById("gh_" + this.id).appendChild(this.headerHTMLOBJ);
        }
    }
    if (!this.hideFooter && this.footerHTMLOBJ) {
        if (typeof(this.footerHTMLOBJ) == "string") {
            document.getElementById("gf_" + this.id).innerHTML = this.footerHTMLOBJ;
        } else {
            document.getElementById("gf_" + this.id).appendChild(this.footerHTMLOBJ);
        }
    }
    Tools.addEvent(document.getElementById("gquy_" + this.id), "click", function () {
        if (that.dataGrid) {
            that.dataGrid.reLoad();
        }
    });
    this.selectedValue = null;
    this.returnValue = function () {
    };
    Tools.addEvent(document.getElementById("gsmt_" + this.id), "click", function () {
        if (that.dataGrid) {
            //获取选择的记录
            that.selectedValue = that.returnValue();
            if (that.selectedValue) {
                var txt = document.getElementById(meta.ui.selectGridActiveBindID[that.id]);
                if (txt != null) {
                    if (txt.type == "text") {
                        txt.value = that.selectedValue.text;
                        document.getElementById(meta.ui.selectGridActiveBindID[that.id] + "_id").value = that.selectedValue.value;
                    } else if (txt.type == "hidden") {
                        txt.value = that.selectedValue.value;
                    }
                }
                if (that.selectedCall) {
                    that.selectedCall();
                }
                that.win.close();
            } else {
                alert("请选择！");
            }
        }
    });
    Tools.addEvent(document.getElementById("gcel_" + this.id), "click", function () {
        that.win.close();
    });

    this.win.hide();
};
