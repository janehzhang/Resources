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
var meta = meta || {};
meta.ui = meta.ui || {};
/**
 * 申明一个下拉树
 * @class meta.ui.selectTree
 * @param bindObj 为下拉框绑定元素可以是数组ID，可以是单个元素ID，被绑定对象可是DIV,SPAN 则会自动创建一个input框，也可直接是input框
 * @param width 宽度
 * @param height 高度
 */
meta.ui.selectTree = function(bindObj,width,height){
    this.width = parseInt(width)?parseInt(width):150 ;
    this.height =parseInt(height)?parseInt(height):200 ;
    this.id = "MetaSelectTree_"+dhx.uid();
    this.binds = {};
    this._autoWidth = true;
    this._dynLoadFlag = false;//控制动态加载
    this._muiltseleted = false;//控制多选
    this._dynload = 0;
    this._priFix = "tree_";

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
    this.render(); //初始box或树
    for(var k in this.binds){
        this.bind(k,true);
    }

};

/**
 * 设置弹出层宽度与高度
 * @memberOf {meta.ui.selectTree}
 * @param width
 * @param height
 */
meta.ui.selectTree.prototype.setListSize = function(width,height){
    this.width = parseInt(width)?parseInt(width):150 ;
    this.height = parseInt(height)?parseInt(height):200 ;
    this.box.style.width = this.width+"px";
    this.box.style.height = this.height+"px";
};

/**
 * 设置下拉树面板的宽度为自动匹配其input框的宽度 ，装载input的容器自适应input框框
 * @memberOf {meta.ui.selectTree}
 * @param mode
 */
meta.ui.selectTree.prototype.enableAutoSize = function(mode){
    this._autoWidth = !!mode;
};
/**
 * 设置树节点为动态加载
 * @memberOf {meta.ui.selectTree}
 * @param mode
 */
meta.ui.selectTree.prototype.setDynload = function(mode,loadFun){
    this._dynLoadFlag = !!mode;
    this._dynload = loadFun;
    if(this._dynLoadFlag && this._dynload && typeof(this._dynload)=="function"){
        this.tree.setXMLAutoLoadingBehaviour("function");
        this.tree.setXMLAutoLoading(this._dynload);
    }else{
        this.tree.setXMLAutoLoadingBehaviour(null);
        this.tree.setXMLAutoLoading(0);
    }
};
/**
 * 控制多选
 * 数据加载前置函数(只对加载后的数据有效)
 * @memberOf {meta.ui.selectTree}
 * @param mode
 * @param fun 添加数据时回调，返回true才生成checkbox框
 */
meta.ui.selectTree.prototype.enableMuiltselect = function(mode,fun){
    this._muiltseleted = !!mode;
    this.tree.enableCheckBoxes(this._muiltseleted);
    if(this._muiltseleted && fun && typeof(fun)=="function"){
        this.checkBoxCall = fun;
    }
};
/**
 * 内置方法，绘制生成dhtmlxTree对象
 * @memberOf {meta.ui.selectTree}
 */
meta.ui.selectTree.prototype.render = function(){
    if(!this.box){
        this.box = dhx.html.create("div", {style:"position:absolute;padding:0;margin:0;" +
            "overflow:auto;border:1px #8f99a2 solid;background-color:white;z-index:1000"});
        this.box.id = this.id;
        this.box.style.width = this.width+"px";
        this.box.style.height = this.height+"px";
        this.box.style.display = "none";
        this.box.style.overflow = "auto";
        document.body.appendChild(this.box);
    }
    if(!this.tree){
        this.tree = DHTMLXFactory.createTree(this.id,this.id,"100%","100%",0);
        this.tree.selTree=this;
        this.enableMuiltselect(this._muiltseleted);
        this.setCheckboxFlag(this._checkboxFlag);
        this.setDynload(this._dynLoadFlag,this._dynload);
        if(window["getDefaultImagePath"] && window["getSkin"]){
            this.tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
        }
    }
};
/**
 * 设置某绑定文本框的宽度
 * @memberOf {meta.ui.selectTree}
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
 * @memberOf {meta.ui.selectTree}
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
    autoPosition(el.selectTree.box,el,true);
    el.selectTree.tree.focusItem(el.selectTree.tree.getSelectedItemId());
};
/**
 * 删除绑定的一个元素
 * @memberOf {meta.ui.selectTree}
 * @param id
 */
meta.ui.selectTree.prototype.removeBindObj = function(id){
    var el = this.binds[id];
    if(el){
        if(el.input){
            detachObjEvent(el.input,"onclick",this._onclick);
            el.input.onkeyup = null;
            el.input.selectTree = null;
            el.removeChild(el.input);
        }else{
            detachObjEvent(el,"onclick",this._onclick);
            el.onkeyup = null;
            el.selectTree = null;
        }
        this.binds[id] = null;
        delete this.binds[id];
    }
};
/**
 * 绑定一个元素
 * @memberOf {meta.ui.selectTree}
 * @param bindObjId
 * @param type 如果为真，表示绑定时会忽略已存在，相关事件注册会重复一次
 */
meta.ui.selectTree.prototype.bind = function(bindObjId,type){
    var el = document.getElementById(bindObjId);
    if(!el)return;
    if(!type && this.binds[bindObjId])return;
    this.render();

    this.binds[bindObjId] = el;
    if(el.tagName=="INPUT"){
        el.selectTree = this;
        attachObjEvent(el,"onclick",this._onclick);
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
        attachObjEvent(el_,"onclick",this._onclick);
    }
    this.enableSearch(this._searchMode);
};
/**
 * 设置一个项是否动态加载，与节点挂钩，树被rander后，如果需要此功能也需要重新设置 (加载数据后设置)
 * @memberOf {meta.ui.selectTree}
 * @param itemId
 * @param mode
 */
meta.ui.selectTree.prototype.enableItemDynLoad = function(itemId,mode){
    var parentObject=this.tree._globalIdStorageFind(itemId);
    if (!parentObject) return;
    parentObject.XMLload = !mode;
};
/**
 * 设置 异步加载数据时，开启子节点标识(记载数据前设置)
 * @memberOf {meta.ui.selectTree}
 * @param mode
 */
meta.ui.selectTree.prototype.enableChildField = function(mode){
    this._childCnt = !!mode;
};
/**
 * 设置checkbox传递模式分四种：
 * 1，向下传递（子永远跟着父变）
 * 2，向上传递（勾选子时，依赖的父也勾选，取消子勾选父不变）
 * 3，双向传递（子永远跟着父变；勾选子时，依赖的父也勾选，取消子勾选父不变）
 * 4，父永远控制子的选中，但是子未勾全时，父为半勾选状态
 * @memberOf {meta.ui.selectTree}
 * @param mode
 */
meta.ui.selectTree.prototype.setCheckboxFlag = function(mode){
    if(this._checkeventid){
        this.tree.detachEvent(this._checkeventid);
        this._checkeventid = null;
    }
    this._checkboxFlag = mode;
    if(this._checkboxFlag==1){
        this._checkeventid = this.tree.attachEvent("onCheck",function(id,state){
            var sNode=this._globalIdStorageFind(id,0,1);
            this._setSubChecked(state,sNode);
        });
    }else if(this._checkboxFlag==2){
        this._checkeventid = this.tree.attachEvent("onCheck",function(id,state){
            if(state==1){
                var pid = this.getParentId(id);
                for(;;pid = this.getParentId(pid)){
                    if(pid==this.rootId)break;
                    this.setCheck(pid,1);
                }
            }
            if(state==0){
                var sNode=this._globalIdStorageFind(id,0,1);
                this._setSubChecked(0,sNode);
            }
        });
    }else if(this._checkboxFlag==3){
        this._checkeventid = this.tree.attachEvent("onCheck",function(id,state){
            var sNode=this._globalIdStorageFind(id,0,1);
            this._setSubChecked(state,sNode);
            if(state==1){
                var pid = this.getParentId(id);
                for(;;pid = this.getParentId(pid)){
                    if(pid==this.rootId)break;
                    this.setCheck(pid,1);
                }
            }
        });
    }else {
        if(this._checkeventid){
            this.tree.detachEvent(this._checkeventid);
            this._checkeventid = null;
        }
    }
    if(this._checkboxFlag==4)
        this.tree.enableThreeStateCheckboxes(true);
    else
        this.tree.enableThreeStateCheckboxes(false);
};
/**
 * 追加数据，data必须是二维数组，内部数组元素前三个元素必须是id，text，pid（顺序不能乱）,如果绑定成功返回true
 * @memberOf {meta.ui.selectTree}
 * @param data
 */
meta.ui.selectTree.prototype.appendData = function(data,fun){
    if(!data || !data.sort)return false;
    for(var i=0;i<data.length;i++){
        var d = data[i];
        if(d==null || d==undefined)continue;
        if(fun && typeof(fun)=="function")
            d = fun(d);
        if(!d.sort)continue;
        if(d.length<3)continue;
        if(this.checkBoxCall){
            this.tree.enableCheckBoxes(this.checkBoxCall(d));
        }
        var pid = (d[2]==null || d[2]=="" || d[2]=="null") ? this.tree.rootId : this._priFix+d[2];
        var text = (d[1]==null || d[1]=="null") ? "" : d[1];
        if(pid!=this.tree.rootId)
            pid = this.tree.getIndexById(pid)!=null?pid:this.tree.rootId;
        var flag = (this._dynLoadFlag && this._childCnt) ? d[3] : (this._dynLoadFlag?1:0);
        var ud = this.tree.insertNewChild(pid,this._priFix+d[0],text,null,0,0,0,0,flag);
        for(var j=2;j<d.length;j++){
            if(flag && this._childCnt && j==3){
                continue;
            }
            this.tree.setUserData(this._priFix+d[0],"USER_DATA_"+j,d[j]);
        }
        if(this._dynLoadFlag && this._childCnt && !d[3])
            this.tree.setUserData(this._priFix+d[0],"refresh",true);
    }
    return true;
};
/**
 * 清空数据
 * @memberOf {meta.ui.selectTree}
 */
meta.ui.selectTree.prototype.clearData = function(){
    this.tree.deleteChildItems(this.tree.rootId);
};
/**
 * 销毁回收方法
 * @memberOf {meta.ui.selectTree}
 */
meta.ui.selectTree.prototype.destructor = function(){
    Destroy.clearObj(this.tree);
    document.body.removeChild(this.box);
    this.box=null;
    for(var key in this.binds){
        if(this.binds[key].input){
            this.binds[key].input.parentNode.removeChild(this.binds[key].input);
        }else{
            this.binds[key].input=null;
            this.binds[key].selectTree = null;
            delete this.binds[key].selectTree;
            detachObjEvent(this.binds[key],"onclick",this._onclick);
            this.binds[key].onmouseover=null;
            this.binds[key].onmouseout=null;
        }
        delete this.binds[key];
    }
};
/**
 * 获取树上某项的值，返回一个数组，数据前三列为（id，text，pid）后续的为绑定了多少数据，就返回多少数据
 * @memberOf {meta.ui.selectTree}
 * @param itemid
 */
meta.ui.selectTree.prototype.getItemValue = function(itemid){
    if(!itemid)
        itemid = this.tree.getSelectedItemId();
    if(this.tree.getIndexById(itemid)==null) return null;
    var data = [];
    data[0] = itemid.replace(this._priFix,"");
    data[1] = this.tree.getItemText(itemid);
    var flag = this._dynLoadFlag&&this._childCnt;
    for(var i=2;;i++){
        if(flag && i==3)continue;
        var d = this.tree.getUserData(itemid,"USER_DATA_"+i);
        if(d==null)break;
        data[data.length] = d;
    }
    return data
};
/**
 * 获取多选值（返回二维数组）
 * @memberOf {meta.ui.selectTree}
 */
meta.ui.selectTree.prototype.getCheckedValue = function(){
    var ids = this.tree.getAllChecked();
    if(ids && ids!=""){
        ids = ids.split(",");
        var list = [];
        var flag = this._dynLoadFlag&&this._childCnt;
        for(var ii=0;ii<ids.length;ii++){
            var data = new Array();
            data[0] = ids[ii].replace(this._priFix,"");
            data[1] = this.tree.getItemText(ids[ii]);
            for(var i=2;;i++){
                if(flag && i==3)continue;
                var d = this.tree.getUserData(ids[ii],"USER_DATA_"+i);
                if(d==null)break;
                data[data.length] = d;
            }
            list[list.length] = data;
        }
        return list;
    }
    return null;
};
/**
 * 为当前活动文本框设置值
 * @memberOf {meta.ui.selectTree}
 * @param data
 */
meta.ui.selectTree.prototype.setValue = function(data){
    this.bindInput.value = data;
};
/**
 * mode 开启文本框输入时在树里面搜索
 * @memberOf {meta.ui.selectTree}
 * @param mode
 * @param flag flag为真时，一直往下搜
 */
meta.ui.selectTree.prototype.enableSearch = function(mode,flag){
    this._searchMode = !!mode;
    this._searchFlag = !!flag;
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
                if(inp.value){
                    if(e.keyCode==13 || e.keyCode==37 || e.keyCode==38 || e.keyCode==39 ||e.keyCode==40 || (inp.old_value!=inp.value)){
                        inp.selectTree.tree.findItem(inp.value.replace(/(^\s*)|(\s*$)/g,""),(e.keyCode==38 || e.keyCode==37)?1:0,inp.selectTree._searchFlag?null:1);
                    }
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

/**
 * 数据表格
 * @class {meta.ui.DataTable}
 * @param parentObj 表格容器，可以是ID，可以是容器对象
 * @param isTree 是否是表格树，当为表格树时还必须设置一些特殊参数才会生效
 */
meta.ui.DataTable = function(parentObj,isTree){
    this.tableId = "MetaDataTable_"+dhx.uid();	//	唯一标识ID
    this.userData=null;		//	绑定数据
    this.desFlag=0;			//	是否销毁绑定数据
    this.parentObj = parentObj;
    if(typeof(this.parentObj)=="string")
        this.parentObj = document.getElementById(this.parentObj);

    this.pagingFlag=true;	//	分页标识，是否显示分页条
    this.Page={
        pageSize:10,
        currPageNum:1, 		//当前页号
        allRowCount:0,		//	总记录数
        allPageCount:1,			//	总页面数
        pageSizeOptions:[10,20,30,50],
        allowType:0, 	//当前页面记录数0锁定最后，1动态表格末行数据对齐，2指定div容器
        pageType:0, //分页类型 0默认toolbar类型，后续再扩展
        pageDiv:null  //allowType=2时有效，page容器
    };

    this.sorting=false;		//	表头是否排序
    this.Sort={
        orderColMap:{},			//	排序字段方向
        orderCols:[],			//	排序字段列表
        reserveCount:3 			//	保留排序字段数
    };

    this.columnIds=[];			//	数组保持显示顺序
    this.columnNames={};         //列map

    this.isTree = !!isTree; //是否是树
};
meta.ui.DataTable.goPageSelectMax = 100;//静态变量，设置gopage模式，最大100页，即切换为输入框

/**
 * 绘制，生成各种数据容器和dhtmlxGrid
 * @memberOf {meta.ui.DataTable}
 */
meta.ui.DataTable.prototype.render = function(){
    if(!this.rendered){
        if(this.parentObj.style.display=="none")
            this.parentObj.style.display = "block";
        if(this.Page.allowType==0 || this.Page.allowType==1){//锁定最后，需要在最后留27px高度放分页条
            this.gridBox = document.createElement("DIV");
            this.parentObj.appendChild(this.gridBox);
            this.Page.pageDiv = document.createElement("DIV");
            this.parentObj.appendChild(this.Page.pageDiv);
            this.Page.pageDiv.style.width = "100%";
            this.Page.pageDiv.style.height = 27+"px";

            this.Page.pageDiv.style.overflow = "hidden";
            this.Page.pageDiv.style.margin = "0px";
            this.Page.pageDiv.style.padding = "0px";
            if(!this.pagingFlag)
                this.Page.pageDiv.style.display = "none";
            this.gridBox.style.width = "100%";
         
            this.gridBox.style.overflow = "hidden";
            if(this.Page.allowType==1){
                this.gridBox.style.minHeight = this.parentObj.offsetHeight - this.Page.pageDiv.offsetHeight + "px";
                this.parentObj.style.overflowX = "hidden";
                this.parentObj.style.overflowY = "scroll";
                this.parentObj.onscroll = function(){
                    this.getChildNodes()[0].getChildNodes()[0].style.zIndex = 1;
                    this.getChildNodes()[0].getChildNodes()[0].style.top = this.scrollTop + "px";
                };
            }else{
                this.gridBox.style.height = this.parentObj.offsetHeight - this.Page.pageDiv.offsetHeight + "px";
                this.parentObj.style.overflow = "hidden";
            }
        }else if(this.Page.allowType==2){
            if(!this.Page.pageDiv){
                this.pagingFlag = false;//如果未传入page容器，则自动变为不分页
            }
            if(typeof(this.Page.pageDiv)=="string")
                this.Page.pageDiv = document.getElementById(this.Page.pageDiv);
            this.gridBox = this.parentObj;
            this.gridBox.style.overflow = "hidden";
        }
        if(this.pagingFlag){
            this._pageRender();
        }
        if(!this.grid){
            this.grid = DHTMLXFactory.createGrid(this.tableId,{parent:this.gridBox});
            if(window["getDefaultImagePath"] && window["getSkin"]){
                this.grid.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
            }
            this.grid.entBox.onselectstart = function(e){
                (e||event).cancelBubble = false;
                return true;
            };
            if(this.headers && this.headers.length>0){
                this.grid.setHeader(this.headers);
            }
            this.grid.setHeaderAlign("center");
            this.grid.setHeaderBold(true);
            this.grid.setColumnIds(this.columnIds.join(","));
            this.grid.enableAutoHeight(this.Page.allowType==1);
            if(this.columnIds.length>0){
                var _width = parseInt(100/this.columnIds.length);
                this.grid._enbTts = [];
                for(var i=0,len=this.columnIds.length;i<len;i++){
                    this.grid.initCellWidth[i] = _width + (i<(100-_width*len)?1:0);
                    this.grid.cellType[i] = (this.isTree && i==0)?"tree":"ro";
                    this.grid.cellAlign[i] = (i==len-1?"center":"left");
                    this.grid._enbTts[i] = (i<len-1);
                }
            }
            if(this.isTree){
                this.grid.enableTreeGridLines();
                this.grid.enableTreeCellEdit(false);
                if(this._dynload){
                    this.grid.kidsXmlFile = this._dynload;
                    this.grid.expandKids = this.grid.expandKids__;
                }
                this.grid.attachEvent("onOpenStart",function(id,state){
                    //因为dhtmlx在移动过程中，会自动展开下级，且会生成一个新的ID，其ID由时间种子生成，大于Java所表示的Integer的最大值。
                    //这里返回不让其展开，以免报错
                    if(state==-1&&parseInt(id)>2147483648 && this.kidsXmlFile){
                        return false;
                    }
                    if(state==1){
                        this.setItemImage(id,getBasePath()+"/meta/resource/images/tree_icon/folderClosed.gif");
                    }else{
                        this.setItemImage(id,getBasePath()+"/meta/resource/images/tree_icon/folderOpen.gif");
                    }
                    var arr = this.getSubItems(id);
                    if(arr.length>0){
                        arr = arr.split(this.delim)
                        for(var i=0;i<arr.length;i++){
                            if(this.hasChildren(arr[i])<1 && !this._h2.get[arr[i]].has_kids){
                                this._h2.get[arr[i]].image=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/leaf.gif";
                                this.rowsAr[arr[i]].imgTag.nextSibling.src=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/leaf.gif";
                            }else{
                                this._h2.get[arr[i]].image=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/folderClosed.gif";
                                this.rowsAr[arr[i]].imgTag.nextSibling.src=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/folderClosed.gif";
                            }
                        }
                    }
                    return true;
                });
//                this.moveAfterParentId = {};//缓存移动后的父ID
                this.grid.attachEvent("onDrag", function(sId, tId, sObj, tObj, sInd, tInd){
                    //如果源ID的父节点只有一个子节点了，将其图标设置为叶子节点的图标。
                    var sparId = this.getParentId(sId);
                    var tparId = this.getParentId(tId);
                    if(sparId!=0 && tparId!=sparId && tId!=sparId && this.hasChildren(sparId)<2){ //如果源节点的父只有小于两个子，那么被拖出之后即变成叶子
                        this._h2.get[sparId].image=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/leaf.gif";
                        this.rowsAr[sparId].imgTag.nextSibling.src=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/leaf.gif";
                        this._h2.get[sparId].has_kids=false;//无子菜单表示
                    }
                    return true;
                });
                this.grid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol){
//                    this.MetaDataTable.moveAfterParentId[sId] = this.getParentId(sId);
                    if(this.hasChildren(sId)<1 && !this._h2.get[sId].has_kids){
                        this._h2.get[sId].image=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/leaf.gif";
                        this.rowsAr[sId].imgTag.nextSibling.src=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/leaf.gif";
                    }else{
                        this._h2.get[sId].image=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/folderClosed.gif";
                        this.rowsAr[sId].imgTag.nextSibling.src=dhtmlx.image_path+"csh_"+dhtmlx.skin+"/folderClosed.gif";
                    }
                });
            }
            if(this.sorting){
                this.grid.attachEvent("onHeaderClick",this._sort);
            }
            this.grid.MetaDataTable = this;
            this.grid._fillRow = meta.ui.DataTable._fillRow;
            this.grid.attachEvent("onRowIdChange",function(new_id,old_id){
                if(this.MetaDataTable && this.MetaDataTable.rowKeyIdIndex){
                    this.MetaDataTable.rowKeyIdIndex[new_id] = this.MetaDataTable.rowKeyIdIndex[old_id];
                }
                return true;
            });
//            this.grid.changeRowId = function(){alert("不支持changeRowId方法!")};
//            this.grid.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue){
//                this.MetaDataTable.setUserData(rId,cInd,nValue);
//                return true;
//            });
        }
        this.rendered = true;
    }
};
/**
 * 获取拖动过后的数据变化，主要针对排序和层级（如果是树）
 * @return 返回map对象
 *          key=id
 *          value={pid:新的父ID,order:新的顺序号}
 */
meta.ui.DataTable.prototype.getDragAfterLevelAndOrderData = function(){
    var data = {};
    var childStr = this.grid.getAllSubItems(0).split(this.grid.delim);
    for(var i=0;i<childStr.length;i++){
        var sid = childStr[i];
        var pid = this.parIdMap[sid];
        if(!pid)
            pid = this.grid.getParentId(sid);
        var order = this._getItemIndexOrder(sid);
        data[sid] = {pid:pid,order:order};
    }
    return data;
};
/**
 * 获取生成一个节点的排序索引 从1开始
 * @param id
 * @return 返回排序ID
 */
meta.ui.DataTable.prototype._getItemIndexOrder = function(id){
    var pid = this.grid.getParentId(id);
    if(pid==0){
        return this.grid._h2.get[id].index + 1;
    }else{
        return this._getItemIndexOrder(pid) + this.grid._h2.get[id].index + 1;
    }
};
/**
 * 设置某列的属性
 * @memberOf {meta.ui.DataTable}
 * @param colIndex 列索引（0开头）
 * @param cf 具体包含 type,label,width,align,valign,tip
 */
meta.ui.DataTable.prototype.setGridColumnCfg = function(colIndex,cf){
    if(colIndex>=0 && colIndex<this.columnIds.length && cf){
        if (cf.label)this.grid.setColumnLabel(colIndex, cf.label);
        if (cf.type)this.grid.cellType[colIndex] = cf.type;
        if (cf.width)this.grid.setColWidth(colIndex, cf.width);
        if (cf.align)this.grid.cellAlign[colIndex] = cf.align;
        if (cf.valign)this.grid.cellVAlign[colIndex] = cf.valign;
        this.grid._enbTts[colIndex] = !!(cf.tip);
    }
};
/**
 * 静态私有方法
 * 重写原grid的填充行方法，加入行数据格式回调
 * @param r 一行，表格的 TR对象
 * @param text 数据（一维数组）
 */
meta.ui.DataTable._fillRow = function(r,text){
    if (this.MetaDataTable.isTree && r.parentNode&&r.parentNode.tagName == "row")
        r._attrs["parent"]=r.parentNode.getAttribute("idd");

    if (this.editor)
        this.editStop();

    for (var i = 0; i < r.childNodes.length; i++){
        if ((i < text.length)||(this.defVal[i])){

            var ii=r.childNodes[i]._cellIndex;
            var val = text[ii];
            var aeditor = this.cells4(r.childNodes[i]);

            if ((this.defVal[ii])&&((val == "")||( typeof (val) == "undefined")))
                val=this.defVal[ii];

            if(this.MetaDataTable.formatCellCall){
                val = this.MetaDataTable.formatCellCall(r.idd,ii,text,this.MetaDataTable.columnIds[ii]);
            }
            if (aeditor) aeditor.setValue(val)
        } else {
            r.childNodes[i].innerHTML="&nbsp;";
            r.childNodes[i]._clearCell=true;
        }
    }

    if(this.MetaDataTable.formatRowCall){
        r = this.MetaDataTable.formatRowCall(r,text);
    }
    if(!r)return null;

    return r;
};
/**
 * 设置单元格格式化回调
 * @memberOf {meta.ui.DataTable}
 * @param fun 单元格回调函数,此函数实现支持三个参数（rid，colIndex，data）
 *          rid：行ID
 *          colIndex：列索引
 *          data：一整行的数据（一维数组）
 *          此回调函数需正常返回数据，默认返回 data[colIndex]
 */
meta.ui.DataTable.prototype.setFormatCellCall = function(fun){
    this.formatCellCall = fun;
};
/**
 * 设置行格式化回调
 * @memberOf {meta.ui.DataTable}
 * @param fun 行回调函数，此函数支持两个参数（tr，data）
 *          tr：表格TR对象
 *          data：一整行数据（一维数组）
 *          此函数需要正常返回，默认返回 TR对象
 */
meta.ui.DataTable.prototype.setFormatRowCall = function(fun){
    this.formatRowCall = fun;
};
/**
 * 设置排序模式
 * @memberOf {meta.ui.DataTable}
 * @param flag 布尔值，排序开关
 * @param sortMap 排序列数据格式为MAP对象，key表示可排序的列，value表示初始排序方式‘ASC/DESC’两种，默认'',表示初始不排
 * @param reserveCount 保留排序字段数 多列排序时最多记忆排序列数，不传默认为 3
 */
meta.ui.DataTable.prototype.setSorting=function(flag,sortMap,reserveCount){
    this.sorting=!!flag;
    if(!this.sorting)return;
    if(sortMap){
        this.Sort.orderCols = [];
        this.Sort.orderColMap = {};
        var i=0;
        for(var col in sortMap){
            this.Sort.orderColMap[col.toUpperCase()] = sortMap[col] || "";
            i++;
            if(this.Sort.orderColMap[col.toUpperCase()])
                this.Sort.orderCols[this.Sort.orderCols.length] = col.toUpperCase();
        }
        this.Sort.reserveCount = parseInt(reserveCount) ? parseInt(reserveCount) : this.Sort.reserveCount;
        if(i==0)this.sorting = false;
    }else{
        if(this.Sort.orderCols.length==0)
            this.sorting = false;
    }
};
/**
 * 私有方法，排序事件，供列头排序时调用
 * @memberOf {meta.ui.DataTable}
 *  ind - index of the column;
 *  obj - related javascript event object.
 */
meta.ui.DataTable.prototype._sort = function(ind,obj){
    var dataTable = this.MetaDataTable;
    if(!dataTable.sorting)return;
    if(dataTable.Sort.orderColMap[dataTable.columnIds[ind]]!=null && dataTable.Sort.orderColMap[dataTable.columnIds[ind]]!=undefined){
        var ascmode = dataTable.Sort.orderColMap[dataTable.columnIds[ind]];
        if(ascmode=="")dataTable.Sort.orderColMap[dataTable.columnIds[ind]] = "ASC";
        if(dataTable.Sort.orderCols[0]==dataTable.columnIds[ind]){  //说明当前列连续点击，交换方向
            if(ascmode.toUpperCase()=="DESC"){
                dataTable.Sort.orderColMap[dataTable.columnIds[ind]] = "ASC";
            }else{
                dataTable.Sort.orderColMap[dataTable.columnIds[ind]] = "DESC";
            }
        }
        var sortspan_ = document.getElementById("sortspan_"+dataTable.tableId+"_"+dataTable.Sort.orderCols[0]);
        if(sortspan_)
            sortspan_.style.display = "none";
        dataTable.Sort.orderCols.removeByValue(dataTable.columnIds[ind]);
        dataTable.Sort.orderCols = [dataTable.columnIds[ind]].concat(dataTable.Sort.orderCols);
        dataTable.Sort.orderCols.length = dataTable.Sort.reserveCount;
        dataTable.refreshData();
        sortspan_ = document.getElementById("sortspan_"+dataTable.tableId+"_"+dataTable.Sort.orderCols[0]);
        if(sortspan_){
            sortspan_.className = "sort_"+(dataTable.Sort.orderColMap[dataTable.Sort.orderCols[0]]=="DESC"?'desc':'asc');
            sortspan_.style.display = "block";
        }
    }else{
        return;
    }
};
/**
 * 设置分页数目选项
 * 设置分页条下拉框可设置的分页数目
 * @memberOf {meta.ui.DataTable}
 * @param pageSizes 可以是数组，可以是'，'分割的字符串， 默认为10,20,30,50四种值可选
 */
meta.ui.DataTable.prototype.setPageSizeOptions = function(pageSizes){
    if(pageSizes && typeof(pageSizes)!="object")
        pageSizes = (pageSizes+"").split(",");
    if(pageSizes && pageSizes.sort){
        for(var i=0;i<pageSizes.length;i++){
            if(parseInt(pageSizes[i])!=this.Page.pageSize)
                this.Page.pageSizeOptions[this.Page.pageSizeOptions.length] = parseInt(pageSizes[i]);
        }
    }
    if(!this.pagerendered)return;
    var pagesizeSel = document.getElementById("pagesize_"+this.tableId);
    if(pagesizeSel){
        if(!this.Page.pageSizeOptions.findByValue(this.Page.pageSize)){
            this.Page.pageSizeOptions = [this.Page.pageSize].concat(this.Page.pageSizeOptions);
        }
        pagesizeSel.options.length = 0;
        for(var i=0;i<this.Page.pageSizeOptions.length;i++){
            pagesizeSel.options[pagesizeSel.options.length] = new Option(this.Page.pageSizeOptions[i]+"/页",this.Page.pageSizeOptions[i]);
            if(this.Page.pageSizeOptions[i]==this.Page.pageSize)
                pagesizeSel.options[i].selected = true;
        }
    }
};
/**
 * 私有方法
 * 设置分页跳转下拉框项
 * @memberOf {meta.ui.DataTable}
 * @param count 设置分页跳转下拉框数组，count=实际分页数组
 */
meta.ui.DataTable.prototype.setPageCountOptions = function(count){
    if(count>0){
        this.Page.allPageCount = count;
    }
    if(!this.pagerendered)return;
    var gosel_ = document.getElementById("gosel_"+this.tableId);
    if(gosel_){
        gosel_.options.length = 0;
        for(var i=0;i<this.Page.allPageCount;i++){
            gosel_.options[i] = new Option((i+1)+"页",i+1);
        }
    }
};
/**
 * 设置分页模式
 * @memberOf {meta.ui.DataTable}
 * @param flag 布尔值，分页开关
 * @param pageSize 分页大小，默认10
 * @param allowType 分页条位置，默认0。  0固定在数据表格最后位置，1根据数据数目滚动，2来自外部
 * @param pageDiv 分页条容器，如果allowType=2时，需要传此参数
 * @param pageType 分页条类型，默认0，目前只支持0，即时toolbar模式
 */
meta.ui.DataTable.prototype.setPaging=function(flag,pageSize,allowType,pageDiv,pageType){
    this.pagingFlag=!!flag;
    if(!this.pagingFlag)return;
    this.Page.pageSize = parseInt(pageSize) ? parseInt(pageSize) : this.Page.pageSize;
    this.Page.allowType = allowType<=2 ? allowType : 0;
    var pd = $(pageDiv);
    if(pd){
        this.Page.pageDiv = pd;
    }
    if(this.Page.allowType==2){
        if(!pd)
            this.Page.allowType = 0;
    }
    this.Page.pageType = pageType || this.Page.pageType;
};
/**
 * 私有方法，绘制生成分页条
 * @memberOf {meta.ui.DataTable}
 * @param flag 强行重绘
 */
meta.ui.DataTable.prototype._pageRender = function(flag){
    if(!this.pagingFlag)return;
    if(this.pagerendered && !flag)return;
    if(this.Page.pageType==0){
        var inhtml = "<DIV id='pagediv_{id}' style='border:1px #A4BED4 solid;border-bottom:none;border-top:none' class='dhx_toolbar_base_dhx_skyblue'>" +
            "<DIV class=float_left style='float:right;margin-right:10px;'>" +
            "<DIV style='position:relative;margin-top:4px;margin-right:15px;' class='dhx_toolbar_text'>" +
            "<span>&nbsp;&nbsp;总记录<span id='rowtotal_{id}'>0</span>条&nbsp;<span>" +
            "<SELECT style='height:19px;width:60px;' id='pagesize_{id}'></SELECT></span>&nbsp;当前" +
            "<span id='pagenum_{id}'>1</span>/<span id='pagetotal_{id}'>1</span>页&nbsp;&nbsp;&nbsp;</span></DIV>" +
            "<DIV class='dhx_toolbar1_btn dis'><span id='leftabs_{id}' class='page_leftabs_dis'></span></DIV>" +
            "<DIV class='dhx_toolbar1_btn dis'><span id='left_{id}' class='page_left_dis'></span></DIV>" +
            "<DIV class='dhx_toolbar1_btn dis' ><span id='right_{id}' class='page_right_dis'></span></DIV>" +
            "<DIV class='dhx_toolbar1_btn dis'><span id='rightabs_{id}' class='page_rightabs_dis'></span></DIV>" +
            "<div class='dhx_toolbar_text' style='margin-top:4px;margin-left:15px;'>" +
            "<span id='gospan1_{id}' style='display: none'><span style='float: left'>转到" +
            "<input id='gotxt_{id}' style='width:18px;height:12px;line-height:12px;' type='text'>页&nbsp;</span>" +
            "<span class='gopage' id='gobtn_{id}' style='float:left;width:22px;height:18px;display:block;'></span></span>" +
            "<span id='gospan2_{id}' style='display:block;'>转到<select id='gosel_{id}' style='width:63px;height:19px;'>" +
            "</select></span></div></DIV></DIV>";
        this.Page.pageDiv.innerHTML = inhtml.replace(new RegExp("{id}","ig"),this.tableId);
        this.pagerendered = true;

        this.setPageSizeOptions();
        this.setPageCountOptions();

        this._setPager(true);

    }
};
/**
 * 私有方法，设置分页条状态
 * @memberOf {meta.ui.DataTable}
 * @param bindEvent 是否绑定事件，一般初始时才传此参数
 */
meta.ui.DataTable.prototype._setPager = function(bindEvent){
    if(!this.pagerendered)return;
    var btnLeftAbs = document.getElementById("leftabs_"+this.tableId); //首页
    var btnLeft = document.getElementById("left_"+this.tableId);        //上页
    var btnRight = document.getElementById("right_"+this.tableId);      //下页
    var btnRightAbs = document.getElementById("rightabs_"+this.tableId);  //末页
    var gospan1 = document.getElementById("gospan1_"+this.tableId);      //go 文本框 span
    var gospan2 = document.getElementById("gospan2_"+this.tableId);      //go 下拉框 span
    var gotxt = document.getElementById("gotxt_"+this.tableId);         // go 文本框
    var gobtn = document.getElementById("gobtn_"+this.tableId);         //go 按钮
    var gosel = document.getElementById("gosel_"+this.tableId);        //go下拉框
    var pagesizeOp = document.getElementById("pagesize_"+this.tableId);//改变pagesize
    if(bindEvent){
        btnLeftAbs.dataTable = this;
        btnLeft.dataTable = this;
        btnRight.dataTable = this;
        btnRightAbs.dataTable = this;
        gotxt.dataTable = this;
        gobtn.dataTable = this;
        gosel.dataTable = this;
        pagesizeOp.dataTable = this;
        btnLeftAbs.onclick = this._goFirstPage;
        btnLeft.onclick = this._goPrePage;
        btnRight.onclick = this._goNextPage;
        btnRightAbs.onclick = this._goLastPage;
        gotxt.onkeyup = function(e){
            e = e || window.event;
            var txt = e.srcElement;
            if(e.keyCode==13 && txt && txt.dataTable){
                document.getElementById("gobtn_"+txt.dataTable.tableId).onclick();
            }
        };
        gobtn.onclick = function(e){
            e = e || window.event;
            var btn = e.srcElement;
            if(btn && btn.dataTable){
                var txt = document.getElementById("gotxt_"+btn.dataTable.tableId);
                var pn = parseInt(txt.value);
                if(pn>=1 && pn<= btn.dataTable.Page.allPageCount){
                    btn.dataTable._goPage(pn);
                }else if(pn<1){
                    txt.value = 1;
                    btn.dataTable._goPage(1);
                }else if(pn > btn.dataTable.Page.allPageCount){
                    txt.value = btn.dataTable.Page.allPageCount;
                    btn.dataTable._goPage(btn.dataTable.Page.allPageCount);
                }
            }
        };
        gosel.onchange = function(e){
            e = e || window.event;
            var sel = e.srcElement;
            if(sel && sel.dataTable){
                sel.dataTable._goPage(sel.options[sel.selectedIndex].value);
            }
        };
        pagesizeOp.onchange = function(e){
            e = e || window.event;
            var sel = e.srcElement;
            if(sel && sel.dataTable){
                var curpageRow = sel.dataTable.Page.pageSize * (sel.dataTable.Page.currPageNum-1) + 1; //原分页第一条记录的数据库索引
                sel.dataTable.Page.pageSize = parseInt(sel.options[sel.selectedIndex].value);
                sel.dataTable.Page.currPageNum = parseInt((curpageRow + sel.dataTable.Page.pageSize - 1)/sel.dataTable.Page.pageSize);
                sel.dataTable.refreshData();
            }
        }
    }

    if((this.Page.currPageNum==this.Page.allPageCount && this.Page.currPageNum==1) || this.Page.allPageCount==0){
        btnLeftAbs.className = "page_leftabs_dis";
        btnLeftAbs.parentNode.className = "dhx_toolbar1_btn dis";
        btnLeft.className = "page_left_dis";
        btnLeft.parentNode.className = "dhx_toolbar1_btn dis";
        btnRight.className = "page_right_dis";
        btnRight.parentNode.className = "dhx_toolbar1_btn dis";
        btnRightAbs.className = "page_rightabs_dis";
        btnRightAbs.parentNode.className = "dhx_toolbar1_btn dis";
    }else if(this.Page.currPageNum==1){
        btnLeftAbs.className = "page_leftabs_dis";
        btnLeftAbs.parentNode.className = "dhx_toolbar1_btn dis";
        btnLeft.className = "page_left_dis";
        btnLeft.parentNode.className = "dhx_toolbar1_btn dis";
        btnRight.className = "page_right";
        btnRight.parentNode.className = "dhx_toolbar1_btn def";
        btnRightAbs.className = "page_rightabs";
        btnRightAbs.parentNode.className = "dhx_toolbar1_btn def";
    }else if(this.Page.currPageNum==this.Page.allPageCount){
        btnLeftAbs.className = "page_leftabs";
        btnLeftAbs.parentNode.className = "dhx_toolbar1_btn def";
        btnLeft.className = "page_left";
        btnLeft.parentNode.className = "dhx_toolbar1_btn def";
        btnRight.className = "page_right_dis";
        btnRight.parentNode.className = "dhx_toolbar1_btn dis";
        btnRightAbs.className = "page_rightabs_dis";
        btnRightAbs.parentNode.className = "dhx_toolbar1_btn dis";
    }else{
        btnLeftAbs.className = "page_leftabs";
        btnLeftAbs.parentNode.className = "dhx_toolbar1_btn def";
        btnLeft.className = "page_left";
        btnLeft.parentNode.className = "dhx_toolbar1_btn def";
        btnRight.className = "page_right";
        btnRight.parentNode.className = "dhx_toolbar1_btn def";
        btnRightAbs.className = "page_rightabs";
        btnRightAbs.parentNode.className = "dhx_toolbar1_btn def";
    }

    document.getElementById("rowtotal_"+this.tableId).innerHTML = this.Page.allRowCount;
    document.getElementById("pagenum_"+this.tableId).innerHTML = this.Page.currPageNum;
    document.getElementById("pagetotal_"+this.tableId).innerHTML = this.Page.allPageCount;
    if(this.Page.allPageCount>meta.ui.DataTable.goPageSelectMax){
        gospan1.style.display = "block";
        gospan2.style.display = "none";
        document.getElementById("gotxt_"+this.tableId).value = this.Page.currPageNum;
    }else{
        gospan1.style.display = "none";
        gospan2.style.display = "block";
        document.getElementById("gosel_"+this.tableId).value = this.Page.currPageNum;
    }
};
/**
 * 私有方法，首页
 * @param e
 */
meta.ui.DataTable.prototype._goFirstPage = function(e){
    e = e||window.event;
    var btn = e.srcElement;
    if(btn && btn.dataTable && btn.className.indexOf("_dis")==-1){
        btn.dataTable._goPage(1);
    }
};
/**
 * 私有方法，上页
 * @memberOf {meta.ui.DataTable}
 * @param e   页面事件对象
 */
meta.ui.DataTable.prototype._goPrePage = function(e){
    e = e||window.event;
    var btn = e.srcElement;
    if(btn && btn.dataTable && btn.className.indexOf("_dis")==-1){
        btn.dataTable._goPage(btn.dataTable.Page.currPageNum-1);
    }
};
/**
 * 私有方法，下页
 * @memberOf {meta.ui.DataTable}
 * @param e
 */
meta.ui.DataTable.prototype._goNextPage = function(e){
    e = e||window.event;
    var btn = e.srcElement;
    if(btn && btn.dataTable && btn.className.indexOf("_dis")==-1){
        btn.dataTable._goPage(btn.dataTable.Page.currPageNum+1);
    }
};
/**
 * 私有方法，末页
 * @memberOf {meta.ui.DataTable}
 * @param e
 */
meta.ui.DataTable.prototype._goLastPage = function(e){
    e = e||window.event;
    var btn = e.srcElement;
    if(btn && btn.dataTable && btn.className.indexOf("_dis")==-1){
        btn.dataTable._goPage(btn.dataTable.Page.allPageCount);
    }
};
/**
 * 私有方法，跳转到某页
 * @memberOf {meta.ui.DataTable}
 * @param num
 */
meta.ui.DataTable.prototype._goPage = function(num){
    this.Page.currPageNum = parseInt(num);
//    Debug("跳转到："+this.Page.currPageNum);
    this.refreshData();
};
/**
 * 刷新数据，第一次使用时，绑定刷新回调
 * @memberOf {meta.ui.DataTable}
 * @param refreshCall 回调函数支持两个参数，第一个是DataTable对象，第一个是即时参数对象（包括分页和排序参数）
 *          刷新回调函数内部实现，需调用bindData完成数据绑定
 */
meta.ui.DataTable.prototype.refreshData = function(refreshCall){
    this.refreshCall = refreshCall || this.refreshCall;
    this.parentObj.getChildNodes()[0].getChildNodes()[0].style.top = "0px";
    this.parentObj.scrollTop = "0px";
    if(this.Page.allowType==1){
        this.parentObj.getChildNodes()[0].getChildNodes()[1].style.minHeight = this.parentObj.offsetHeight -
            this.Page.pageDiv.offsetHeight - this.parentObj.getChildNodes()[0].getChildNodes()[0].offsetHeight - 17 + "px";
    }
    if(this.refreshCall){
        this.refreshCall(this,this.getCurrentParams());
    }else{
        alert("请传入refreshCall函数");
    }
};
/**
 * 设置行唯一索引 依据的字段  如果不设置，行索引则默认生成为 从1开始的自然数
 * 如果是表格树，则必须调用此方法显示设置出父子关系，以便形成树结构
 * @memberOf {meta.ui.DataTable}
 * @param idField 依据字段（如果绑定的是数组，则此值为绑定数组的索引）
 * @param pidField 当grid为树时才用
 */
meta.ui.DataTable.prototype.setRowIdForField = function(idField,pidField){
    this.rowKeyField = idField;
    if(this.isTree){
        this.rowParKeyField = pidField;
    }
};
/**
 * 设置表格树为动态加载函数
 * @memberOf {meta.ui.DataTable}
 * @param fun 动态加载函数，此回调函数支持3个参数：
 *          id：父ID
 *          dataTable：表格对象
 *          param：表格即时参数（包含分页和排序等属性）
 *          此回调一般用于查询后台，返回数据后调用 loadChildData 绑定子节点数据
 * @param hasChildField 设置标识是否有子节点的字段名或索引
 *          如果没此参数，那么所有节点默认都是有子的，需要点击一下动态去后台查询一次再改变状态
 */
meta.ui.DataTable.prototype.setDynload = function(fun,hasChildField){
    if(this.isTree){
        this._dynload = fun;
        this.hasChildField = hasChildField;
        if(this.grid && this._dynload){
            this.grid.kidsXmlFile = this._dynload;
        }
    }
};
/**
 * 设置列，必须在render之前调用
 * @memberOf {meta.ui.DataTable}
 * @param columns 列MAP，key一般与数据库字段对应，应为要支持排序，value为列中文名，
 *                  其载入顺序即是列的显示顺序
 * @param dataColOrder 绑定数据时一个列的对应映射，不传此参数，即按顺序来；
 *          当绑定MAP类型的数据时，此值传入每个列绑定数据对应的key
 *          当绑定为array类型时，此值传入每个列对应到绑定数据的index
 */
meta.ui.DataTable.prototype.setColumns = function(columns,dataColOrder){
    if(columns && typeof(columns)=="object"){
        dataColOrder = dataColOrder||[];
        if(typeof(dataColOrder)=='string')
            dataColOrder = dataColOrder.toUpperCase().split(",");

        this.headers = [];
        this.columnNames = {};
        for(var k in columns){
            var colid = k.toUpperCase();
            this.columnIds[this.columnIds.length] = colid;
            this.columnNames[colid] = columns[k];
            var colname = this.columnNames[colid];
            if(this.headerRenderCall)
                colname = this.headerRenderCall(k,colname);
            this.headers[this.headers.length] = colname;
        }
        if(dataColOrder.length!=0)this.colOrderIndex = true;
        if(this.colOrderIndex){
            this.colOrderIndex = [];
            this.colOrderKey={};
            for(var j=0;j<dataColOrder.length;j++)
                this.colOrderKey[dataColOrder[j].toUpperCase()]=j+1;
            var pos=0;
            for(var i=0;i<this.columnIds.length;i++){
                if(this.colOrderKey[this.columnIds[i]]) {
                    this.colOrderIndex[i] = this.colOrderKey[this.columnIds[i]]-1;
                }else{
                    if(dataColOrder[i]!=null && dataColOrder!=undefined)
                        this.colOrderIndex[i] = dataColOrder[i];
                    else{
                        if(this.columnIds.length>dataColOrder.length)
                            this.colOrderIndex[i] = dataColOrder.length + pos++  ;
                        else
                            alert("字段:"+this.columnIds[i]+" 未找到对应数据索引!");
                    }
                }
            }
        }
    }
};
/**
 * 设置列头绘制回调，可用于把某些列头绘制成特殊样式
 * @memberOf {meta.ui.DataTable}
 * @param fun 列头绘制回调函数，用于给列头加一些特殊样式修饰：支持1个参数
 *          name：列头中文名
 *          返回新绘制过后的列头html代码
 */
meta.ui.DataTable.prototype.setHeaderRenderCall = function(fun){
    this.headerRenderCall = fun;
};
/**
 * 设置刷新回调 (重要)
 * @memberOf {meta.ui.DataTable}
 * @param fun 刷新加载数据的回调接口，支持2个参数：
 *      dataTable：当前数据表格对象
 *      param：表格即时参数（分页和排序）
 *      刷新回调函数一般都是在查询后台数据，后台数据返回后调用 bindData绑定数据
 */
meta.ui.DataTable.prototype.setReFreshCall = function(fun){
    this.refreshCall = fun;
};
/**
 * 私有初始方法，在所有表格属性设置完成之后调用此方法
 * 也可直接调用 bindData() 内部判断完成初始
 * @memberOf {meta.ui.DataTable}
 */
meta.ui.DataTable.prototype._init = function(){
    if(!this.rendered)this.render();
    if(!this.inited){
        if(this.isTree){        //如果是树，则必须纠正某些参数
            this.grid.cellType[0] = "tree";
            this.grid.cellAlign[0] = "left";
        }
        if(this.sorting){   //如果有排序，则必须改变列头
            for(var i=0;i<this.grid.columnIds.length;i++){
                var colid = this.grid.columnIds[i];
                if(this.Sort.orderColMap[colid]!=null && this.Sort.orderColMap[colid]!=undefined){
                    var spn = "<span style='display:"+(colid==this.Sort.orderCols[0]?'block':'none') +
                        "' id='sortspan_"+this.tableId+"_"+colid+"' class='sort_"+
                        (this.Sort.orderColMap[colid].toUpperCase()=="DESC"?'desc':'asc')+"'></span>";
                    this.grid.hdrLabels[i] = this.grid.hdrLabels[i] + spn;
                }
            }
        }
        this.grid.init()
    }
    this.inited = true;
};
/**
 * 绑定数据，刷新回调函数内部需要主动调用此方法完成数据绑定
 * @memberOf {meta.ui.DataTable}
 * @param data 支持二维数组和Map对象的一维数组
 * @param totalNum totalNum（总记录数，开启分页时必须传此参数）
 */
meta.ui.DataTable.prototype.bindData = function(data,totalNum){
    if(!this.inited)this._init();
    if(data){
        this.grid.clearAll();
        this.userData = data;
        this.rowKeyIdIndex = {};//清空行ID索引
        if(!this.isTree){
            this.grid.parse(data,"arraymap");
        }else{
            if(this.rowParKeyField==null || this.rowParKeyField==undefined){
                alert("请使用setRowIdForField方法设置ID与父ID字段标记或索引,否则无法形成树结构!");
                return;
            }
            var pidMap = {};
            this.parIdMap = {};
            for(var i=0;i<data.length;i++){
                var _d = data[i];
                _d = this.columnOrderCall(_d);
                var id = data[i][this.rowKeyField];
                var pid = data[i][this.rowParKeyField];
                var flag = data[i][this.hasChildField];
                flag = (flag!=null && flag!=undefined) ? flag : (this._dynload?1:0);
                this.rowKeyIdIndex[id]=i+1;
                if(!(this.grid._h2.get[pid])){
                    this.parIdMap[id] = pid;//缓存ID，避免模糊查询时拖动错误
                    pid = 0;
                }
                this.grid.addRow(id,_d,null,pid,(flag?"folderClosed.gif":undefined),flag);
                pidMap[pid] = 1;
            }
            for(var _id in pidMap){
                if(_id!=0)
                    this.grid.setItemImage(_id,getBasePath()+"/meta/resource/images/tree_icon/folderClosed.gif");
            }
        }
    }
    this.buildPage(totalNum);
};
/**
 * 刷新表格树某节点
 * @param id
 */
meta.ui.DataTable.prototype.refreshNode = function(id){
    if(this.isTree && this._dynload && this.grid){
        if(id==0)
            this.userData = [];
        else{
            var strs = this.grid.getAllSubItems(id);
            var ids = strs.split(this.grid.delim);
            for(var i=0;i<ids.length;i++){
                this.userData[this.rowKeyIdIndex[ids[i]]] = null; //先删除
            }
        }
        this.grid.deleteChildItems(id);
        DWREngine.setAsync(false);
        this._dynload(id,this,this.getCurrentParams());
        DWREngine.setAsync(true);

        if(id!=0){
            if(this.grid.hasChildren(id)<1){
                this.grid._h2.change(id,"state",dhtmlXGridObject._emptyLineImg);
                this.grid._updateTGRState(this.grid._h2.get[id]);
                this.grid.setItemImage(id,getBasePath()+"/meta/resource/images/tree_icon/leaf.gif");
            }else{
                this.grid.setItemImage(id,getBasePath()+"/meta/resource/images/tree_icon/folderOpen.gif");
                this.grid.openItem(id);
            }
        }
    }
};

/**
 * 加载子项，树时有效
 * @memberOf {meta.ui.DataTable}
 * @param data 二维数组一维MAP数组
 */
meta.ui.DataTable.prototype.loadChildData = function(data){
    if(this.isTree && data){
        var pidMap = {};
        var oldLen = this.userData.length;
        this.userData = this.userData.concat(data);
        for(var i=0;i<data.length;i++){
            var _d = data[i];
            _d = this.columnOrderCall(_d);
            var id = data[i][this.rowKeyField];
            var pid = data[i][this.rowParKeyField];
            var flag = data[i][this.hasChildField];
            flag = (flag!=null && flag!=undefined) ? flag : (this._dynload?1:0);
            this.rowKeyIdIndex[id]=oldLen+i+1;
            this.grid.addRow(id,_d,null,pid,(flag?"folderClosed.gif":undefined),flag);
            pidMap[pid] = 1;
        }

        for(var _id in pidMap){
            if(_id!=0)
                this.grid.setItemImage(_id,getBasePath()+"/meta/resource/images/tree_icon/folderClosed.gif");
        }
    }
};
/**
 * 重新计算分页参数并改变状态，一般在刷新数据后调用
 * 一般都是组件内部自动调用，但是如果外面一些方法触发的分页，则需要主动调用一下，以重新设置分页条状态
 * @memberOf {meta.ui.DataTable}
 * @param totalNum 分页数目
 */
meta.ui.DataTable.prototype.buildPage = function(totalNum){
    if(!this.pagingFlag)return;
    if(totalNum>=0){
        this.Page.allRowCount = totalNum;
        var pageCount = parseInt((this.Page.allRowCount+this.Page.pageSize-1)/this.Page.pageSize);
        if(this.Page.allPageCount!=pageCount){
            this.Page.allPageCount = pageCount;
            if(this.Page.allPageCount<=meta.ui.DataTable.goPageSelectMax){
                this.setPageCountOptions();
            }
        }
    }
    if(this.Page.currPageNum>this.Page.allPageCount)
        this.Page.currPageNum = this.Page.allPageCount || 1;

    this._setPager();
};
/**
 * 私有方法
 * 主要是结合DhtmlxGrid内部填充数据是，针对数组或MAP格式的数据一个转换函数
 * @memberOf {meta.ui.DataTable}
 * @param data 一行数据（数组或MAP）
 */
meta.ui.DataTable.prototype.columnOrderCall = function(data){
    var _d = new Array(this.columnIds.length);
    if(data.sort){
        if(!this.colOrderIndex)return data;
        for(var i=0;i<this.columnIds.length;i++){
            _d[i] = data[this.colOrderIndex[i]];
        }
    }else{
        var _map = {};
        for(var k in data){
            _map[k.toUpperCase()] = data[k];
        }
        if(!this.colOrderIndex){
            for(var i=0;i<this.columnIds.length;i++){
                _d[i] = _map[this.columnIds[i].toUpperCase()];
            }
        }else{
            var i=0;
            for(var k in this.colOrderKey){
                if(this.colOrderKey[k]){
                    _d[this.colOrderKey[k]-1]=_map[k];
                }
                i++;
            }
        }
    }
    return _d;
};
/**
 * 获取用户数据
 * @memberOf {meta.ui.DataTable}
 * @param rid 行ID
 * @param idx 列索引，如果没传此参数则返回一整行数据
 */
meta.ui.DataTable.prototype.getUserData = function(rid,idx){
    if(!this.userData)return;
    if(this.rowKeyField==null ||this.rowKeyField==undefined){
        if(rid<=this.userData.length && rid>=1){
            if(idx==null || idx==undefined){
                return this.userData[rid-1];
            }else{
                if(this.colOrderIndex && this.userData[rid-1].sort)
                    return this.userData[rid-1][this.colOrderIndex[idx]];
                return this.userData[rid-1][idx];
            }
        }
    }else{
        if(this.rowKeyIdIndex[rid]==null || this.rowKeyIdIndex[rid]==undefined)return;
        if(idx==null || idx==undefined){
            return this.userData[this.rowKeyIdIndex[rid]-1];
        }else{
            if(this.colOrderIndex && this.userData[this.rowKeyIdIndex[rid]-1].sort)
                return this.userData[this.rowKeyIdIndex[rid]-1][this.colOrderIndex[idx]];
            return this.userData[this.rowKeyIdIndex[rid]-1][idx];
        }
    }
    return null;
};
/**
 * 设置用户数据
 * @memberOf {meta.ui.DataTable}
 * @param rid 行ID
 * @param idx 列索引
 * @param data 数据
 */
meta.ui.DataTable.prototype.setUserData = function(rid,idx,data){
    if(!this.userData)return;
    if(this.rowKeyField==null ||this.rowKeyField==undefined){
        if(this.colOrderIndex){
            this.userData[rid-1][this.colOrderIndex[idx]] = data;
        }else{
            this.userData[rid-1][idx] = data;
        }
    }else{
        if(this.rowKeyIdIndex[rid]==null || this.rowKeyIdIndex[rid]==undefined)return;
        if(this.colOrderIndex){
            this.userData[this.rowKeyIdIndex[rid]-1][this.colOrderIndex[idx]] = data;
        }else{
            this.userData[this.rowKeyIdIndex[rid]-1][idx] = data;
        }
    }
};
/**
 * 获取当前表格各项即时参数
 * 一般都是在需要查询后台时调用
 * @memberOf {meta.ui.DataTable}
 * @return {} 对象，包含：
 *          page:{pageSize,pageStart,rowStart}
 *          sort:"排序后的order by语句，可拿给sql直接用"
 */
meta.ui.DataTable.prototype.getCurrentParams = function(){
    var page = {
        pageSize:this.Page.pageSize,
        pageStart:this.Page.currPageNum,
        rowStart:this.Page.pageSize*((this.Page.currPageNum||1)-1)
    };
    var sort = "";
    for(var i=0,j=0;i<this.Sort.orderCols.length;i++){
        if(j>this.Sort.reserveCount-1)break;
        if(this.Sort.orderColMap[this.Sort.orderCols[i]]=="")continue;
        if(!this.Sort.orderCols[i])break;
        sort += this.Sort.orderCols[i]
            + (this.Sort.orderColMap[this.Sort.orderCols[i]].toUpperCase()=="DESC" ? " DESC," : " ASC,");
        j++;
    }
    if(sort!=""){
        sort = sort.substring(0,sort.length-1);
    }
    return {page:page,sort:sort};
};
/**
 * 销毁方法
 * @memberOf {meta.ui.DataTable}
 */
meta.ui.DataTable.prototype.destructor = function(){
    this.userData = null;
    this.rowKeyIdIndex = null;
    this.Sort= null;
    if(this.rendered){
        if(this.Page.allowType==0 || this.Page.allowType==1){
            this.parentObj.removeChild(this.gridBox);
            this.parentObj.removeChild(this.Page.pageDiv);
            this.parentObj = null;
            this.gridBox = null;
            this.Page.pageDiv = null;
            if(this.grid){
                this.grid.MetaDataTable = null;
                Destroy.destructorDHMLX(this.grid);
            }
            this.grid = null;
        }else{
            this.parentObj = null;
            this.gridBox = null;
            this.Page.pageDiv = null;
            if(this.grid){
                this.grid.MetaDataTable = null;
                Destroy.destructorDHMLX(this.grid);
            }
            this.grid = null;
        }
        delete this.gridBox;
        delete this.grid;
    }
    this.Page = null;

    delete this.userData;
    delete this.Sort;
    delete this.Page;
    delete this.parentObj;
};









