/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-3-7
 * Time: 上午10:22
 * To change this template use File | Settings | File Templates.
 */

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var testInit = function(){
    var userLayout = new dhtmlXLayoutObject(document.body, "3L");
    userLayout.cells("a").setText("测试");
    userLayout.cells("b").setText("表格");
    userLayout.cells("c").setText("树");
    userLayout.cells("a").setWidth(500);
    userLayout.cells("a").fixSize(true, true);
    userLayout.cells("b").fixSize(true, true);
    userLayout.hideConcentrate();
//    userLayout.hideSpliter();//移除分界边框。

    var qf = userLayout.cells("a").attachForm(
        [{
            type: "settings",
            position: "label-left",
            labelWidth: 100,
            inputWidth: 120
        }, {
            type: "fieldset",
            label: "Welcome",
            inputWidth: "auto",
            list: [{
                type: "radio",
                name: "type",
                label: "Already have account",
                labelWidth: "auto",
                position: "label-right",
                checked: true,
                list: [{
                    type: "input",
                    label: "Login",
                    name:"testinp",
                    width:150,
                    value: "p_rossi"
                }, {
                    type: "password",
                    label: "Password",
                    value: "123"
                }, {
                    type: "checkbox",
                    label: "Remember me",
                    checked: true
                }]
            }, {
                type: "radio",
                name: "type",
                label: "Not registered yet",
                labelWidth: "auto",
                position: "label-right",
                list: [{
                    type: "input",
                    label: "Full Name",
                    value: "Patricia D. Rossi"
                }, {
                    type: "input",
                    label: "E-mail Address",
                    value: "p_rossi@example.com"
                }, {
                    type: "input",
                    label: "Login",
                    value: "p_rossi"
                }, {
                    type: "password",
                    label: "Password",
                    value: "123"
                }, {
                    type: "password",
                    label: "Confirm Password",
                    value: "123"
                }, {
                    type: "checkbox",
                    label: "Subscribe on news"
                }]
            }, {
                type: "radio",
                name: "type",
                label: "Guest login",
                labelWidth: "auto",
                position: "label-right",
                list: [{
                    type: "select",
                    label: "Account type",
                    options: [{
                        text: "Admin",
                        value: "admin"
                    }, {
                        text: "Organiser",
                        value: "org"
                    }, {
                        text: "Power User",
                        value: "poweruser"
                    }, {
                        text: "User",
                        value: "user"
                    }]
                }, {
                    type: "checkbox",
                    label: "Show logs window"
                }]
            }, {
                type: "button",
                value: "Proceed"
            }]
        }]
    );
    qf.defaultValidateEvent();
    var testcb = new meta.ui.baseCombobox({});
    testcb.bind(qf.getInput("testinp"));

    var div = dhx.html.create("div", {style:"position:absolute;padding:0;margin:0;left:100px;height;100px;top:400px;" +
        "overflow-x:hidden;overflow-y:auto;border:1px #A4BED4 solid;background-color:red;z-index:1000"});
    div.id="debugdiv";
    document.body.appendChild(div);


    var treeconv = new dhtmxTreeDataConverter({
        idColumn : "id",
        pidColumn : "pid",
        textColumn : "name"
    });
    var treeDwrD = Tools.dwr({
        dwrMethod:TestWCSAction.queryTest1,
        converter:treeconv
    });
    var treeDwr2 = Tools.dwr({
        dwrMethod:TestWCSAction.queryTest2,
        converter:treeconv
    });

    var testg = new meta.ui.tablesSelectGrid({
//        multiselect:true,
        title:"选择表类(测试)" ,
        footerType:"info",
        selectedCall:function(){alert("关闭前");},
        showBeforeCall:function(){alert("弹出前");}
    });


    var t = userLayout.cells("c").attachTree();
    var x = Tree(t,{
        checkMode:"ch",
        isDynload:true,
        beforeLoad:function(tree){alert("树加载前");return true;},
        afterLoad:function(tree){alert("树加载后");},
        onDblClick:function(nodeId){
//            this.refreshItems(nodeId);
//            alert("id->"+nodeId+"====pid->"+t.getParentId(nodeId));
            testg.show();
        }
    });
    x.loadData(treeDwr2);




    //转换器参数
    var tableDataConfig = {
        idColumn:"id",pidColumn:"pid",
        filterColumns:["","name","id","des","pid"]
    };
    var tableConverter = new dhtmxGridDataConverter(tableDataConfig);
//    var tableConverter = new dhtmlxTreeGridDataConverter(tableDataConfig);

    //表格的数据展示
//    var tableGrid = $("tableGrid");//可以是来自界面一个DIV
    var tableGrid = userLayout.cells("b").attachGrid();//由dhtmlx源生控件 生成的挂靠Grid
    var mygrid = new Grid(tableGrid,{
        headNames:"{#checkBox},名称,ID,描述,操作",
//        treeFlag:true,
//        isDynload:true,
        widths:"25,*,*,*,*,*",
        multiselect:true,
        columnIds:tableConverter.filterColumns.toString(),
        beforeLoad:function(grid){
            alert("表格加载前，将colIndex:3  列头名修改");
            return  true;
        }
    });
    mygrid.genApi.setCellType(0,"ch");
    mygrid.genApi.enableClickRowChecked(true);
    var gridData = Tools.dwr(TestWCSAction.queryTest2,tableConverter);
    mygrid.loadData(gridData);
};

dhx.ready(testInit);