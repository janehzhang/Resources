/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        metaui.js
 *Description：组件封装
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/
var meta = meta || new Object();
meta.ui = meta.ui || new Object();
/**
 * 申明一个下拉树
 * @param bindObj 为下拉框绑定元素可以是数组ID，可以是单个元素ID，被绑定对象可是DIV,SPAN 则会自动创建一个input框，也可直接是input框
 * @param width 宽度
 * @param height 高度
 */
meta.ui.selectTree = function(bindObj,width,height){
    this.width = width || parseInt(width)?parseInt(width):150 ;
    this.height = height || parseInt(height)?parseInt(height):200 ;
    this.id = "MetaSelectTree_"+dhx.uid();
    this.binds = {};
    this._autoWidth = true;
    if(typeof(bindObj)=="string"){
        var a = document.getElementById(bindObj);
        if(a)this.binds[bindObj]=a;
    }

    if(typeof(bindObj)=="object" && bindObj.sort){
        for(var i=0;i<bindObj.length;i++){
            var obj = bindObj[i];
            if(!obj)
                continue;
            if(typeof(obj)=="string"){
                var b = document.getElementById(obj);
                if(b)this.binds[obj]=b;
            }else{
                if(!obj.tagName)continue;
                if(obj.id)
                    obj.id = "SelTree_"+dhx.uid();
                this.binds[obj.id] = obj;
            }
        }
    }
    this.box = dhx.html.create("div", {style:"position:absolute;padding:0;margin:0;" +
        "overflow:auto;border:1px #8f99a2 solid;background-color:white;z-index:1000"});
    this.box.id = this.id;
    this.box.style.width = this.width+"px";
    this.box.style.height = this.height+"px";
    this.box.style.display = "none";
    document.body.appendChild(this.box);
    var bx = this.box;
    this.box.onmouseover = function(e){
        bx.style.display = "block";
    };
    this.box.onmouseout = function(e){
        bx.style.display = "none";
    };

    this.tree = DhtmlxObjFactory.createTree(this.id,this.id,"100%","100%",0);

    for(var k in this.binds){
        this.bind(k,true);
    }

};
/**
 * 设置下拉树面板的宽度为自动匹配其input框的宽度 ，装载input的容器自适应input框框
 * @param mode
 */
meta.ui.selectTree.prototype.enableAutoSize = function(mode){
    this._autoWidth = mode;
};
/**
 * 设置某绑定文本框的宽度
 * @param bindId
 * @param width
 */
meta.ui.selectTree.prototype.setBindObjWidth = function(bindId,width){
    var el = this.binds[bindId];
    if(el && width && parseInt(width)){
        el.style.width = parseInt(width) + "px";
        if(el.input)
            el.input.style.width = parseInt(width) + "px";
    }
};
/**
 * 文本框单击事件 (私有方法)
 * @param e
 */
meta.ui.selectTree.prototype._onclick = function(e){
    e = e || window.event;
    var el = e.srcElement;
    if(!el.selectTree) return;
    el.selectTree.bindInput=el;
    if(el.selectTree._autoWidth){      //设置了自动宽度
        el.selectTree.box.style.width = el.offsetWidth -2+ "px";
    }
    if(el.selectTree.box.style.display=="block")return;
    autoPosition(el.selectTree.box,el);
};
/**
 * 删除绑定的一个元素
 * @param id
 */
meta.ui.selectTree.prototype.removeBindObj = function(id){
    var el = this.binds[id];
    if(el){
        if(el.input){
            Tools.removeEvent(el.input,"click",this._onclick);
            el.input.onmouseover = null;
            el.input.onmouseout = null;
            el.input.onkeyup = null;
            el.input.selectTree = null;
            el.removeChild(el.input);
        }else{
            Tools.removeEvent(el,"click",this._onclick);
            el.onmouseover = null;
            el.onmouseout = null;
            el.onkeyup = null;
            el.selectTree = null;
        }
        this.binds[id] = null;
        delete this.binds[id];
    }
};
/**
 * 绑定一个元素
 * @param bindObjId
 * @param type 如果为真，表示绑定时会忽略已存在，相关事件注册会重复一次
 */
meta.ui.selectTree.prototype.bind = function(bindObjId,type){
    var el = document.getElementById(bindObjId);
    if(!el)return;
    if(!type && this.binds[bindObjId])return;
    if(!this.tree)    
    	this.tree = DhtmlxObjFactory.createTree(this.id,this.id,"100%","100%",0);

    this.binds[bindObjId] = el;
    if(el.tagName=="INPUT"){
        el.selectTree = this;
        Tools.addEvent(el,"click",this._onclick);
        el.onmouseover = function(e){
            e = e || window.event;
            var de = e.srcElement;
            if(!de.selectTree || !de.selectTree.bindInput) return;
            if(de==de.selectTree.bindInput)
                de.click();
        };
        el.onmouseout = function(e){
            e = e || window.event;
            var de = e.srcElement;
            if(!de.selectTree || !de.selectTree.bindInput) return;
            de.selectTree.box.style.display = "none";
        };
    }else{
    	
        var el_ =document.getElementById(bindObjId+"_input");
        if(!el_)el_= document.createElement("INPUT");
        el_.selectTree = this;
        el_.type = "text";
        el_.id = bindObjId+"_input";
        el_.style.width = this.width+"px";
        el_.style.margin = 0+"px";
        this.binds[bindObjId].appendChild(el_);
        this.binds[bindObjId].input = el_;
        Tools.addEvent(el_,"click",this._onclick);
        el_.onmouseover = function(e){
            e = e || window.event;
            var de = e.srcElement;
            if(!de.selectTree || !de.selectTree.bindInput) return;
            if(de==de.selectTree.bindInput)
                de.click();
        };
        el_.onmouseout = function(e){
            e = e || window.event;
            var de = e.srcElement;
            if(!de.selectTree || !de.selectTree.bindInput) return;
            de.selectTree.box.style.display = "none";
        };
    }
    this.enableSearch(this._searchMode);
};
/**
 * 追加数据，data必须是二维数组，内部数组元素前三个元素必须是id，text，pid（顺序不能乱）,如果绑定成功返回true
 * @param data
 */
meta.ui.selectTree.prototype.appendData = function(data){
    if(!data || !data.sort)return false;
    for(var i=0;i<data.length;i++){
        var d = data[i];
        if(!d.sort)continue;
        if(d.length<3)continue;
        var pid = (d[2]==null || d[2]=="" || d[2]=="null") ? this.tree.rootId : d[2];
        var text = (d[1]==null || d[1]=="null") ? "" : d[1];
        if(pid!=this.tree.rootId)
            pid = this.tree.getIndexById(pid)!=null?pid:this.tree.rootId;
        var ud = this.tree.insertNewChild(pid,d[0],text);
        for(var j=2;j<d.length;j++){
            this.tree.setUserData(d[0],"USER_DATA_"+j,d[j]);
        }
    }
    return true;
};
/**
 * 清空数据
 */
meta.ui.selectTree.prototype.clearData = function(){
    this.tree.clearAll();
};
meta.ui.selectTree.prototype.destructor = function(){
    this.tree.destructor();
    this.tree=null;
    document.body.removeChild(this.box);
    this.box=null;
    for(var key in this.binds)
    {
    	if(this.binds[key].input)
    	{
    		this.binds[key].input.parentNode.removeChild(this.binds[key].input);
    	}
    	else
    	{
    		this.binds[key].input=null;
	    	this.binds[key].selectTree = null;
	    	delete this.binds[key].selectTree;
	    	Tools.removeEvent(this.binds[key],"click",this._onclick);
	    	this.binds[key].onmouseover=null;
	    	this.binds[key].onmouseout=null;
    	}
    	
    }
};
/**
 * 获取树上某项的值，返回一个数组，数据前三列为（id，text，pid）后续的为绑定了多少数据，就返回多少数据
 * @param itemid
 */
meta.ui.selectTree.prototype.getItemValue = function(itemid){
    if(this.tree.getIndexById(itemid)==null) return null;
    var data = new Array();
    data[0] = itemid;
    data[1] = this.tree.getItemText(itemid);
    for(var i=2;;i++){
        var d = this.tree.getUserData(itemid,"USER_DATA_"+i);
        if(d==null)break;
        data[i] = d;
    }
    return data
};
/**
 * 为当前活动文本框设置值
 * @param data
 */
meta.ui.selectTree.prototype.setValue = function(data){
    this.bindInput.value = data;
};
/**
 * mode 开启文本框输入时在树里面搜索
 * @param mode
 * @param flag flag为真时，一直往下搜
 */
meta.ui.selectTree.prototype.enableSearch = function(mode,flag){
    this._searchMode = mode;
    this._searchFlag = flag;
    if(this._searchMode){
        this.tree.enableKeySearch(false);
        for(var k in this.binds){
            var el = this.binds[k].input || this.binds[k];
            el.readonly = false;
            if(el.onkeyup)continue;
            el.onkeyup = function(e){
                e = e||window.event;
                var inp = e.srcElement;
                if(!inp.selectTree)return;
                if(inp.old_value!=inp.value){
                    inp.selectTree.tree.findItem(inp.value.replace(/(^\s*)|(\s*$)/g,""),0,inp.selectTree._searchFlag?null:1);
                }
                inp.old_value = inp.value;
            }
        }
    }else{
        for(var k in this.binds){
            var el = this.binds[k].input || this.binds[k];
            el.onkeyup = null;
            el.readonly = true;
        }
    }
};

/**
 * 下拉选择表格
 * @param bindObj
 * @param width
 * @param height
 */
meta.ui.selectTable = function(bindObj,width,height){

};

