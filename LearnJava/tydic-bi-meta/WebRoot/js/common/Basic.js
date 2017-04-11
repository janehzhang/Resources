function $(){
    var elements = new Array();
    for (var i = 0; i < arguments.length; i++){
        var element = arguments[i];
        if (typeof element == 'string')
            element = getObjectById(element);
        if (arguments.length == 1)
            return element;
        elements.push(element);
    }
    return elements;
}
function getObjectById(id){
    if (typeof(id) != "string" || id == "") return null;
    if (document.getElementById) return document.getElementById(id);
    if (document.all) return document.all(id);
    try {return eval(id);} catch(e){ return null;}
}
function length(obj){
    var l=0;
    for(var i in obj)l++;
    return l;
}
function cmpObj(obj1,obj2){
    if(typeof obj1!=typeof obj2)
        return false;
    switch(typeof obj1) {
        case "boolean":
        case "string":
        case "number":
            return obj1==obj2;
        case "object":
            if(obj1.sort){
                if(obj1.length!=obj2.length)return false;
                for(var i=0;i<obj1.length;i++)
                    if(cmpObj(obj1[i],obj2[i])==false)return false;
                return true;
            }else{
                for(var i in obj1)
                    if(cmpObj(obj1[i],obj2[i])==false)return false;
                for(var i in obj2)
                    if(cmpObj(obj1[i],obj2[i])==false)return false;
                return true;
            }
    }
}

//数据拷贝
function clone(obj){
    switch(typeof obj){
        case "boolean":
        case "string":
        case "number":
            return obj;
        case "object":
            if(obj.sort){
                var res=[];
                for(var i=0;i<obj.length;i++)
                    res[i]=clone(obj[i]);
                return res;//obj.clone();
            }else{
                var res=new Object();
                for(var i in obj){
                    res[i]=clone(obj[i]);
                }
                return res;
            }
    }
    return null;
}
function KeyDown(){  //(event.keyCode==8)  || //屏蔽退格删除键
    if((window.event.altKey) && ((window.event.keyCode == 37) || (window.event.keyCode == 39))){
        alert('不准你使用ALT+方向键前进或后退网页！');
        event.returnValue = false;
    }
    if((event.keyCode == 116) || (event.keyCode == 112) || (event.ctrlKey && event.keyCode == 82)){
        event.keyCode = 0;
        event.returnValue = false;
    }
    if ((event.ctrlKey)&&(event.keyCode==78))
        event.returnValue=false;
    if ((event.shiftKey)&&(event.keyCode==121))
        event.returnValue=false;
    if(window.event.srcElement.tagName.toUpperCase()  == 'A' && window.event.shiftKey)
        window.event.returnValue = false;
    if((window.event.altKey)&&(window.event.keyCode==115)){
        window.showModelessDialog('about:blank','','dialogWidth:1px;dialogheight:1px');
        return false;
    }
}
function contextmenu(){
    return false;
}
//尝试执行一段代码
function tryEval(){
    var al;
    var args=[];
    for(var i=0;i<arguments.length;i++)
        args[i]=arguments[i];
    try{
        var fun=args.shift();
        al=args.shift();
        fun(args);
        return true;
    }catch(ex){
        if(al && typeof ex=="string")alert(ex);
        else if(al){
            var msg="";
            for(var a in ex)
                msg+=a+":"+ex[a]+"\n";
            alert(msg);
        }
        return false;
    }
}
function SymError(e){
    window.status=(e);
    return true;
}
function attachObjEvent(obj,ev,fun){
    if(obj.attachEvent){
        obj.attachEvent(ev,fun);
        return;
    }
    if(obj.addEventListener){
        obj.addEventListener(ev.substr(2),fun,false);
        return;
    }
    obj[ev]=fun;
}
function detachObjEvent(obj,ev,fun){
    if(obj.attachEvent){
        obj.detachEvent(ev,fun);
        return;
    }
    if(obj.addEventListener){
        obj.removeEventListener(ev.substr(2),fun,false);
        return;
    }
    obj[ev]=null;
}
attachObjEvent(window,"onerror",SymError);
//attachObjEvent( window.document,"onkeydown",KeyDown);
//attachObjEvent( window.document,"oncontextmenu",contextmenu);
//

//取随机数
function rnd() {
    rnd.today=new Date();
    rnd.seed=rnd.today.getTime();
    rnd.seed = (rnd.seed*9301+49297) % 233280;
    return rnd.seed/(233280.0);
}
function rand(number) {
    return Math.ceil(rnd()*number);
}

//复制
function JM_cc(ob){
    var obj=MM_findObj(ob);
    if (obj) {
        obj.select();var js=obj.createTextRange();js.execCommand("Copy");}
}
//粘贴
function JM_pp(ob){
    var obj=MM_findObj(ob);
    if (obj) {
        obj.select();js=obj.createTextRange();
        js.execCommand("Paste");}
}
//查找元素
function MM_findObj(n, d) { //v4.0
    var p,i,x;  if(!d) d=document;
    if((p=n.indexOf("?"))>0&&parent.frames.length) {
        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
    if(!x && document.getElementById) x=document.getElementById(n); return x;
}

//光标是停在文本框文字的最后
function cc(){
    var e=event.srcElement;
    var r=e.createTextRange();
    r.movestart(character,e.value.length);
    r.collapse(true);
    r.select();
}

/**
 *  只允许用户在输入框中输入数字
 *  调用例子<input name="xmhtje_bd" type="text" onKeyDown='onlynumber()'>
 * 　键的对应数字190,110：小数点，13：回车，109,189：-,37:左箭头，38:下箭头，39:右箭头，40：上箭头，8：退格 48-57：大键盘数字，
 　　　　96-105：小键盘数字，9：TAB键，46：删除
 */
function onlynumber(){
    var event = window.event;
    var fun_iCode=event.keyCode;
    if(((fun_iCode<48)||((fun_iCode>57)&&(fun_iCode<96))||fun_iCode>105)
        &&((fun_iCode!=8)&&(fun_iCode!=109)&&(fun_iCode!=189)&&(fun_iCode!=9)&&(fun_iCode!=229)
        &&(fun_iCode!=13)&&(fun_iCode!=110)&&(fun_iCode!=190)&&(fun_iCode!=46)&&(fun_iCode!=27))
        &&((fun_iCode<37)||(fun_iCode>40))){
        event.returnValue=false;
    }
}


/////////////////////文本框编辑器///////////////////////
function insertValueQuery(txtId,text){
    var myQuery = $(txtId);
    if(text.trim()!=""){
        //IE support
        if (document.selection){
            myQuery.focus();
            sel = document.selection.createRange();
            sel.text = text;
            myQuery.focus();
        }
        //MOZILLA/NETSCAPE support
        else if (myQuery.selectionStart || myQuery.selectionStart == "0"){
            var startPos = myQuery.selectionStart;
            var endPos = myQuery.selectionEnd;
            var chaineSql = myQuery.value;
            myQuery.value = chaineSql.substring(0, startPos) + text + chaineSql.substring(endPos, chaineSql.length);
        }else{
            myQuery.value += text;
        }
    }
    event.srcElement.focus();
}

//取得控件的绝对位置
function getie(e){
    var t=e.offsetTop;
    var l=e.offsetLeft;
    while(e=e.offsetParent){
        if(e.scrollTop)t-=e.scrollTop;
        t+=e.offsetTop;
        if(e.scrollLeft)l-=e.scrollLeft;
        l+=e.offsetLeft;
    }
    return [t,l];
}

//取得控件的绝对位置
function getElPos(e){
    var t=e.offsetTop;
    var l=e.offsetLeft;
    while(e=e.offsetParent){
        if(e.scrollTop)t-=e.scrollTop;
        t+=e.offsetTop;
        if(e.scrollLeft)l-=e.scrollLeft;
        l+=e.offsetLeft;
    }
    return {y:t,x:l};
};

/**
 * 自动定位并浮动展现 (考虑到了目标元素绝对定位和滚动条)
 * @param el 被定位的对象(必须相对于BODY元素绝对定位)
 * @param target 目标对象（参照物）
 * @param mouseRangeCtrl 鼠标范围控制
 * @param autoWidth 自动随目标宽度改变
 * @return 返回map对象包含left和top，可取出来根据实际场景和框架浏览器等再次计算偏移量
 */
function autoPosition(el,target,mouseRangeCtrl,autoWidth){
    el.style.display = "block";
    if(el.beforeScroll!=null && el.beforeScroll!=undefined){
        el.scrollTop = el.beforeScroll.y + "px";
        el.scrollLeft = el.beforeScroll.x + "px";
    }
    if(autoWidth){
        el.style.width = target.offsetWidth + "px";
    }
    var pos = dhx.html.offset(target);
    var lscroll = document.documentElement.scrollLeft + document.body.scrollLeft;
    var tscroll = document.documentElement.scrollTop + document.body.scrollTop;
    var mw = document.body.offsetWidth;
    mw = Math.max(document.body.clientWidth,mw);
    mw = Math.max(document.documentElement.clientWidth,mw) + lscroll;
    var mh = document.body.offsetHeight;
    mh = Math.max(document.body.clientHeight,mh);
    mh = Math.max(document.documentElement.clientHeight,mh) + tscroll;
    var l = 0;
    var t = 0;
    if(pos.x+el.offsetWidth > mw && pos.x+target.offsetWidth > mw){   //隐藏目标一部分
        if(mw-el.offsetWidth>pos.x){
            l = pos.x;//左对齐目标el
        }else{
            l = mw - el.offsetWidth;//右对齐浏览器边框
        }
    }else{
        l = pos.x;//左对齐目标el
    }
    if(pos.y + target.offsetHeight + el.offsetHeight > mh) {//下面不够
        var dt =mh - pos.y - target.offsetHeight ;
        if(pos.y>el.offsetHeight){
            t = pos.y-el.offsetHeight + 1 ;//目标上边框对齐
        }else if(pos.y<dt){
            t = mh - el.offsetHeight;//浏览器下边框对齐
        }else{
            t = 0;//浏览器上边框对齐
        }
    }else{
        t = pos.y + target.offsetHeight - 1;//目标下边框对齐
    }
    el.style.left = l + "px";
    el.style.top = t + "px";
    if(mouseRangeCtrl){
        var ctrlFun = function (ev) {
            ev = ev || window.event;
            var mousePos = null;
            if (ev.pageX || ev.pageY) {
                mousePos =  {x:ev.pageX, y:ev.pageY};
            }else{
                mousePos = {
                    x:ev.clientX + document.body.scrollLeft+document.documentElement.scrollLeft,
                    y:ev.clientY + document.body.scrollTop+document.documentElement.scrollTop
                };
            }
            var elt = {x:l,y:t};
            var er = {startX:elt.x, endX:elt.x+el.offsetWidth,startY:elt.y, endY:elt.y+el.offsetHeight};
            var tr = {startX:pos.x, endX:pos.x + target.offsetWidth,startY:pos.y, endY:pos.y + target.offsetHeight};
            //判断鼠标坐标是否在指定区域内。
            if (((mousePos.x >= tr.startX && mousePos.y >= tr.startY) &&
                (mousePos.x <= tr.endX && mousePos.y <= tr.endY)) ||
                ((mousePos.x >= er.startX && mousePos.y >= er.startY) &&
                    (mousePos.x <= er.endX && mousePos.y <= er.endY))) {
                //鼠标在指定区域内，无动作。
            } else {
                //关闭DIV
                el.beforeScroll = {x:el.scrollLeft,y:el.scrollTop};
                el.style.display = "none";
                detachObjEvent(document,"onmousemove",ctrlFun);
            }
        };
        attachObjEvent(document,"onmousemove",ctrlFun);
    }
    return {left:l,top:t};
}

// returns the scroll left and top for the browser viewport.
function getScroll() {
    if (document.all && document.body.scrollTop != undefined) {	// IE model
        var ieBox = document.compatMode != "CSS1Compat";
        var cont = ieBox ? document.body : document.documentElement;
        return {x : cont.scrollLeft, y : cont.scrollTop};
    }
    else {
        return {x : window.pageXOffset, y : window.pageYOffset};
    }
}

/**
 * 获取一个节点下的子节点
 * @param type,节点类型  不传默认只取Element类型的
 1  ELEMENT_NODE
 2   ATTRIBUTE_NODE
 3   TEXT_NODE
 4   CDATA_SECTION_NODE
 5   ENTITY_REFERENCE_NODE
 6   ENTITY_NODE
 7   PROCESSING_INSTRUCTION_NODE
 8  COMMENT_NODE
 9  DOCUMENT_NODE
 10  DOCUMENT_TYPE_NODE
 11  DOCUMENT_FRAGMENT_NODE
 12  NOTATION_NODE
 */
try{
    if ( !window.Element ) {
        Element = function(){};
        var __createElement = document.createElement;
        document.createElement = function(tagName)         {
            var element = __createElement(tagName);
            if(element)
                for(var key in Element.prototype)
                    element[key] = Element.prototype[key];
            return element;
        };
        var __getElementById = document.getElementById
        document.getElementById = function(id)         {
            var element = __getElementById(id);
            if(element)
                for(var key in Element.prototype)
                    element[key] = Element.prototype[key];
            return element;
        }
    }
    Element.prototype.getChildNodes = function(type){
    var ns = [];
    var nodes = this.childNodes;
    if(type){
        for(var i=0;i<nodes.length;i++){
            if(nodes[i].nodeType!=type)continue;
            ns[ns.length] = nodes[i];
        }
    }else{
        for(var i=0;i<nodes.length;i++){
            if(nodes[i].nodeType!=1)continue;
            ns[ns.length] = nodes[i];
        }
    }
    return ns;
};
}catch(e){}

function isElement(o) {
    var toString = Object.prototype.toString.call(o);
    return toString.indexOf('HTML') != -1 || toString == '[object Object]' && o.nodeType === 1 && !(o instanceof Object);
}
