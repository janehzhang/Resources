/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        tablesSelectGrid.js
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

/**
 * 表类弹出选择框
 * @param cfg
 */
meta.ui.tablesSelectGrid = function(cfg){

    this.footerType = "none"; //有三种值 none/info/fieldsInfo/fieldsChk
    this.specialFilter = null;//特殊条件过滤(排除)
    this.title = "选择表类";

    if(cfg){
        dhx.extend(this,cfg,true);
    }
    dhx.extend(cfg,{
        width:880,
        height:(this.footerType=="none")?400:550,
        footerHeight:150,
        hideFooter:(this.footerType=="none")
    },false);
    meta.ui.baseSelectGrid.call(this,cfg);//继承基类的属性和方法

    //初始组件
    var that = this;
    this.init = function(){
        dwr.engine.setAsync(false);
        //初始数据源和用户和层次分类
        CmpInitAction.getTablesGridHeader(function(data){
            if(data){
                var ds = document.getElementById("ds_"+that.id);
                var tp = document.getElementById("tp_"+that.id);
                if(data.dataSource){
                    for(var i=0;i<data.dataSource.length;i++){
                        var op = document.createElement("OPTION");
                        op.value = data.dataSource[i]["DATA_SOURCE_ID"];
                        op.innerHTML = data.dataSource[i]["DATA_SOURCE_NAME"];
                        ds.appendChild(op);
                    }
                }
                if(data.tableTypes){
                    for(var i=0;i<data.tableTypes.length;i++){
                        var op = document.createElement("OPTION");
                        op.value = data.tableTypes[i]["codeValue"];
                        op.innerHTML = data.tableTypes[i]["codeName"];
                        if(that.specialFilter && that.specialFilter.TABLE_TYPE_ID){
                            var nt = that.specialFilter.TABLE_TYPE_ID;
                            if(nt==op.value || nt.indexOf(","+op.value)!=-1 || nt.indexOf(op.value+",")!=-1){
                                continue;
                            }
                        }
                        tp.appendChild(op);
                    }
                }
            }
        });


        var converter = new dhtmxGridDataConverter({
            idColumn:"tableId",
            filterColumns:["","tableName","dataSourceName","tableOwner","tableTypeName","tableGroupName","tableBusComment","tableId"]
        });
        this.dataGrid = new Grid(document.getElementById("gc_"+this.id),{
            headNames:(this.multiselect?"{#checkBox}":"")+",表类名,数据源,所属用户,层次分类,业务类型,说明",
            widths:"20,240,110,80,100,*,*",
            pageSize:this.pageSize,
            multiselect:this.multiselect,
            columnIds:converter.filterColumns.toString()
        });
        this.dataGrid.genApi.setCellType(0,(this.multiselect?"ch":"ra"));
        this.dataGrid.genApi.setCellType(4,"ro");
        this.dataGrid.genApi.enableClickRowChecked(true);
        var param = new biDwrMethodParam([{
            index:0,type: "fun",value:function(){
                var da = {};
                da["keyword"] = document.getElementById("kwd_"+that.id).value;
                da["dataSourceId"] = document.getElementById("ds_"+that.id).value;
                da["tableOwner"] = document.getElementById("us_"+that.id).value;
                da["tableTypeId"] = document.getElementById("tp_"+that.id).value;
                da["tableGroupId"] = document.getElementById("tt_id_"+that.id).value;
                if(that.specialFilter){
                    da["specialFilter"] = that.specialFilter;
                }
                return da;
            }
        }]);
        this.initEvent();
        this.dataGrid.loadData(Tools.dwr({
            dwrMethod:ComponentAction.getTablesGridData,
            param:param,
            converter:converter
        }));
        dwr.engine.setAsync(true);
    };

    //初始头区域
    this.win.setText(this.title);
    var hdiv = document.getElementById("gqc_"+this.id);
        hdiv.innerHTML = "<span class='qcond'><span>数据源</span><select id='ds_"+this.id+"'><option value=''>--请选择--</option></select></span>" +
            "<span class='qcond'><span>所属用户</span><select title='请先选择数据源' id='us_"+this.id+"'><option value=''>--请选择--</option></select></span>" +
            "<span class='qcond'><span>层次分类</span><select id='tp_"+this.id+"'><option value=''>--请选择--</option></select></span>" +
            "<span class='qcond'><span>业务类型</span><input title='请先选择层次分类' type='text' readonly='readonly' id='tt_"+this.id+"'></span>"+
            "<span class='qcond' style='width:750px'><span>关键字</span><input style='width:675px;' type='text' id='kwd_"+this.id+"'></span>"+
            "<input type='hidden' id='tt_id_"+this.id+"'>";
    Tools.addEvent(document.getElementById("kwd_"+this.id),"keyup",function(){
        var e = this.event || window.event;
        if (e.keyCode == 13){
            document.getElementById("gquy_"+that.id).click();
            return false;
        }
    });
    Tools.addEvent(document.getElementById("ds_"+this.id),"change",function(){
        var dsid = document.getElementById("ds_"+that.id).value;
        var us = document.getElementById("us_"+that.id);
        us.innerHTML = "<option value=''>--请选择--</option>";
        if(dsid && dsid!=""){
            CmpInitAction.getDSOwner(dsid,function(data){
                if(data){
                    for(var i=0;i<data.length;i++){
                        var op = document.createElement("OPTION");
                        op.value = data[i]["USERNAME"];
                        op.innerHTML = data[i]["USERNAME"];
                        us.appendChild(op);
                    }
                }
            });
        }
    });

    Tools.addEvent(document.getElementById("tp_"+this.id),"change",function(){
        var typeid = document.getElementById("tp_"+that.id).value;
        var tt = document.getElementById("tt_"+that.id);
        var tt_id_ = document.getElementById("tt_id_"+that.id);
        tt.value = "";
        tt_id_.value = "";
        if(typeid!=""){
            that.tt_tr.reLoad();
        }
    });

    //初始
    this.initEvent = function(){
        if(this.footerType!="none" && !this.hideFooter){
            this.dataGrid.attachEvent("onRowSelect", function(id){
                var tableId = that.dataGrid.getUserData(id,"tableId");
                alert("tableId= " + tableId);
            });
        }
    };

    //返回值
    this.returnValue = function(){
        var rowid = this.dataGrid.getCheckedRows(0);
        if(rowid){
            rowid = rowid.split(",");
            var val = {value:"",text:""};
            for(var i=0;i<rowid.length;i++){
                val.value += this.dataGrid.getUserData(rowid[i],"tableId")+",";
                val.text += this.dataGrid.getUserData(rowid[i],"tableName")+",";
            }
            val.value = val.value.substring(0,val.value.length-1);
            val.text = val.text.substring(0,val.text.length-1);
            return val;
        }else{
            return null;
        }
    };


    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
            "z-index:1000"
    });
    document.body.appendChild(div);
    div.style.display = "none";
    this.tt_tr = new Tree(div, {
        onClick:function (nodeId) {
            document.getElementById("tt_"+that.id).value = this.getItemText(nodeId);
            document.getElementById("tt_id_"+that.id).value = nodeId;
            //关闭树
            div.style.display = "none";
        },
        afterLoad:function (tr) {
            tr.openAllItems(0);//展开所有节点
        }
    });
    this.tt_tr.enableSingleRadioMode(true);
    this.tt_tr.actionurl = Tools.dwr({
        dwrMethod:CmpInitAction.queryTableGroup,
        param:new biDwrMethodParam([{
            index:0,type:"ui",value:"tp_"+that.id
        }]),
        converter:new dhtmxTreeDataConverter({
            idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
            isDycload:false
        }),
        showProcess:false
    });
    Tools.addEvent(document.getElementById("tt_"+that.id), "click", function () {
        var tt = document.getElementById("tt_"+that.id);
        if(document.getElementById("tp_"+that.id).value!=""){
            div.style.width = tt.offsetWidth + 'px';
            Tools.divPromptCtrl(div, tt, true);
            div.style.display = "block";
        }
    })
};