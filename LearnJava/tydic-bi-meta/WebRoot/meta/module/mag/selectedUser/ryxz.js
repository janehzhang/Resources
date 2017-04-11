
/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var loadUserParam = new biDwrMethodParam();//loadZone Action参数设置。
var loadZoneParam = new biDwrMethodParam();//loadZone Action参数设置。
var dwrCaller = new biDwrCaller();

/**
 * User表Data转换器
 */
var userConvertConfig = {
    idColumnName:"userId",
    filterColumns:["","userNamecn","userEmail","deptName","stationName","deptId"],
    cellDataFormat:function( rowIndex,  columnIndex,columnName,  cellValue,rowData) {
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName,cellValue, rowData);
    }
}
var userDataConverter=new dhtmxGridDataConverter(userConvertConfig);
/**
 * 声明dwrCaller
 */

dwrCaller.addAutoAction("queryZoneTree","ZoneAction.queryZoneTree");

var zoneTreeConvert = new dhtmxTreeDataConverter({
    idColumn:"zoneId",pidColumn:"zoneParId",textColumn:"zoneName",
    isDycload:false
});
dwrCaller.addDataConverter("queryZoneTree", zoneTreeConvert);
dwrCaller.isShowProcess("queryZoneTree", false);


dwrCaller.addAutoAction("loadUser", "UserAction.queryUserList",loadUserParam, function(data){});
dwrCaller.addDataConverter("loadUser",userDataConverter);

/**
 * 界面初始化
 */
var zoneInit = function(){
    var codeLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "2U");
    codeLayout.hideConcentrate();
    codeLayout.hideSpliter();
    codeLayout.cells("a").hideHeader();
    codeLayout.cells("b").hideHeader();
    codeLayout.cells("a").setWidth(260);
    var zoneLayout = new dhtmlXLayoutObject(codeLayout.cells("a"), "2E");
    zoneLayout.cells("a").setText("地域查询");
    zoneLayout.cells("b").hideHeader();
    zoneLayout.cells("a").setHeight(80);
    zoneLayout.cells("a").fixSize(false,true);
    zoneLayout.hideSpliter();//移除分界边框。
    zoneLayout.hideConcentrate();
    
   
    //加载查询表单
    var queryform = zoneLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"input",label:"名称：",name:"zoneName",inputHeight : 17,inputWidth:100} ,
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询",offsetLeft : 0,offsetTop : 0},
        {type:"hidden",name:"template"}
    ]);
    //定义loadRole Action的参数来源于表单queryform数据。
    loadZoneParam.setParamConfig([{
        index:0,type:"fun",value:function(){
    	var formData=queryform.getFormData();
            formData.zoneName=Tools.trim(queryform.getInput("zoneName").value);
            return formData;
        }
    }]);
    //加载树
    tree = zoneLayout.cells("b").attachTree();
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.enableDragAndDrop(true,true);
    tree.setDataMode("json");
    dwrCaller.executeAction("queryZoneTree", loadZoneParam ,function(data){
        tree.loadJSONObject(data);
    });
    //查询表单事件处理
   queryform.attachEvent("onButtonClick", function(id){
        if(id=="query"){
        	 tree.clearAll(); 
             dwrCaller.executeAction("queryZoneTree", loadZoneParam ,function(data){
             tree.loadJSONObject(data);
           });
        }
    });  
   
   var userLayout = new dhtmlXLayoutObject(codeLayout.cells("b"), "3E");
    userLayout.cells("a").setText("用户查询");
    userLayout.cells("b").hideHeader();
    userLayout.cells("c").hideHeader();
    userLayout.cells("a").setHeight(100);
    userLayout.cells("a").fixSize(false,true);
    userLayout.cells("b").setHeight(500);
    userLayout.hideSpliter();//移除分界边框。
    userLayout.hideConcentrate();
   
      //加载查询表单
    var queryUserform = userLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"input",label:"用户名称：",name:"userName",inputHeight : 17},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询",offsetLeft : 0,offsetTop : 0},
        {type:"hidden",name:"zoneId"}
    ]);

   loadUserParam.setParamConfig([{
	    index:0,type:"fun",value:function(){
	        var data = {};
	        var formData=queryUserform.getFormData();
	        data.formData = formData;
	        return data;
	    }
	}]);
    
/**    var buttonToolBar=userLayout.cells("b").attachToolbar();
        buttonToolBar.addButton("addUser",1, "选择人员",base + "/meta/resource/images/auditUser.png", 
        	base + "/meta/resource/images/auditUser.png");**/
    
   
    mygrid = userLayout.cells("b").attachGrid();
    mygrid.setHeader("{#checkBox},姓名,邮箱,部门,岗位");
    mygrid.setInitWidthsP("3,20,37,20,20");
    mygrid.setColAlign("center,left,left,left,left");
    mygrid.setHeaderAlign("center,center,center,center,center");
    mygrid.setColTypes("ch,ro,ro,ro,ro");
    mygrid.setColSorting("na,na,na,na,na");
    mygrid.enableMultiselect(false);
    mygrid.enableCtrlC();
    mygrid.setColumnIds("'',userNamecn,userEmail,deptName,stationName,deptId");
    mygrid.enableTooltips("true,true,true,true,true");
    mygrid.enableSelectCheckedBoxCheck(1);
    mygrid.init();
    mygrid.defaultPaging(10);
    mygrid.load(dwrCaller.loadUser,"json");
      //查询表单事件处理
    queryUserform.attachEvent("onButtonClick", function(id){
        if(id=="query"){
            //进行数据查询。
            if(queryUserform.validate()){
                 mygrid.clearAll();
                 mygrid.load(dwrCaller.loadUser,"json");
            }
        }
    });
    
   tree.attachEvent("onClick", function(id){
	      queryUserform.setFormData({zoneId:id});
          mygrid.clearAll();
          mygrid.load(dwrCaller.loadUser,"json");
    });
    //添加buttonToolBar事件
/**    buttonToolBar.attachEvent("onclick", function(id) {
        if (id == "addUser") {
        	addUser();
        }
    });**/

   var tempForm=userLayout.cells("c").attachForm([
      {type:"block",offsetLeft:200 ,list:[	
	        {type:"button",name:"confirm",value:"确 定",offsetLeft : 0,offsetTop : 0},
	        {type:"newcolumn"},
	        {type:"button",name:"close",  value:"关 闭",offsetLeft : 20,offsetTop : 0}
           ]}
    ]);
   tempForm.attachEvent("onButtonClick", function (id) {
    	 if (id == "confirm") {
    		  addUser();
    	 }
    	 if (id == "close") {
    		 window.close();
    	 }
    });
    
    
    
};

var addUser=function(){
  var selecteddRowId= mygrid.getCheckedRows(0);
  var selecteddRowName="";
  var deptIds="";
  var deptNames="";
  if(selecteddRowId == null || selecteddRowId==""){
        dhx.alert("请选择至少一行数据!");
        return ;
    }else{
        for(var i=0; i<selecteddRowId.split(",").length; i++){
            var rowData=mygrid.getRowData(selecteddRowId.split(",")[i]);
            if(i==0){
            	  selecteddRowName=rowData.data[1];
            	  deptIds=rowData.data[5];
                  deptNames=rowData.data[3];
            } else{
            	selecteddRowName +=","+rowData.data[1];
            	deptIds +=","+rowData.data[5];
                deptNames +=","+rowData.data[3];
            }
            
        }
             var obj =new Object(); 
             obj.ids= selecteddRowId;
	         obj.names = selecteddRowName;
	         obj.deptIds=deptIds;
	         obj.deptNames=deptNames;
	         window.returnValue=obj;
             window.close(); 
	         
    };
}
dhx.ready(zoneInit);