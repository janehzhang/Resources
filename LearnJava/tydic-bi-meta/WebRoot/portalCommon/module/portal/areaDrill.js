/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller=new biDwrCaller();
//当前系统的主页Path
var base = getBasePath();

var indexInit=function(){
  var map=new Object();
      map.tabId   =tabId;
	  map.indexCd =indexId;
	  map.zoneCode=zoneCode;	
	  map.dataDate=dataDate;
  $('chartTitle').innerHTML = tabName+'->'+indexName;
	
   PortalCommonCtrlr.getDrillTableData(map,{callback:function (res) {
		if(res==null)
		{
			dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
			return;
		}
         buildGrid(res);
	}});

  
  $("div_src").style.display = "none";
  $("div_src").style.zIndex = "-1";
  
  
}

/**
 * 构建表格
 * @param {Object} data
 */
function buildGrid(data){
	var map=new Object();
	var tableStr="";
	if(tabName=="日重点指标"){
           tableStr ="<table id='mytbl_1' width='100%' height='auto!important' border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
           tableStr+="<tr>"
					    +"<td bgcolor='#cde5fd'><strong>地域</strong></td>"
					    +"<td bgcolor='#cde5fd'><strong>当日值</strong></td>"
					    +"<td bgcolor='#cde5fd'><strong>本月累计</strong></td>"
					    +"<td bgcolor='#cde5fd'><strong>上月同期累计</strong></td>"
					    +"<td bgcolor='#cde5fd'><strong>环比</strong></td>"
					    +"<td bgcolor='#cde5fd'><strong>上年同期累计</strong></td>"
					    +"<td bgcolor='#cde5fd'><strong>同比</strong></td>"
					    +"<td bgcolor='#cde5fd'><strong>本月累计平均值</strong></td>"
					    +"<td bgcolor='#cde5fd'></td>"
					+"</tr>";
      for(var i=0;i<data.length;i++){
         tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		     +"<td>"+data[i].AREA_NAME+"</td>"
		     +"<td onclick=\"djChart('VALUE2','1','"+data[i].INDEX_CD+"')\">"+data[i].VALUE2+"</td>"
		     +"<td onclick=\"djChart('VALUE3','1','"+data[i].INDEX_CD+"')\">"+data[i].VALUE3+"</td>"
		     +"<td onclick=\"djChart('VALUE4','1','"+data[i].INDEX_CD+"')\">"+data[i].VALUE4+"</td>"
		     +"<td onclick=\"djChart('VALUE5','1','"+data[i].INDEX_CD+"')\">"+data[i].VALUE5+"</td>"
		     +"<td onclick=\"djChart('VALUE6','1','"+data[i].INDEX_CD+"')\">"+data[i].VALUE6+"</td>"
		     +"<td onclick=\"djChart('VALUE7','1','"+data[i].INDEX_CD+"')\">"+data[i].VALUE7+"</td>"
		     +"<td onclick=\"djChart('VALUE8','1','"+data[i].INDEX_CD+"')\">"+data[i].VALUE8+"</td>"
		     +"<td onclick=\"openArea(1,'"+tabName+"','"+ data[i].INDEX_CD+"','"+ data[i].INDEX_NAME+"','"+data[i].ZONE_CODE+"','"+dataDate+"');\" class='unl'>辖区</td>"
		   +"</tr>";
        
        if(i==0){
			  map.tabId   ="1";
			  map.indexCd =data[i].INDEX_CD;
			  map.zoneCode=zoneCode;	
			  map.dataDate=dataDate;
			  map.field   ="VALUE2";
          }
      }
     }else{
	      tableStr ="<table id='mytbl_2' width='100%' height='auto!important' border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
          tableStr+="<tr>"
				    +"<td bgcolor='#cde5fd'><strong>地域</strong></td>"
				    +"<td bgcolor='#cde5fd'><strong>当月值</strong></td>"
				    +"<td bgcolor='#cde5fd'><strong>上月值</strong></td>"
				    +"<td bgcolor='#cde5fd'><strong>环比</strong></td>"
				    +"<td bgcolor='#cde5fd'><strong>上年同期累计</strong></td>"
				    +"<td bgcolor='#cde5fd'><strong>同比</strong></td>"
				    +"<td bgcolor='#cde5fd'><strong>本年平均值</strong></td>"
				    +"<td bgcolor='#cde5fd'></td>"
				+"</tr>";
	       for(var i=0;i<data.length;i++){
	        tableStr =tableStr+
	           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
			     +"<td>"+data[i].AREA_NAME+"</td>"
			     +"<td onclick=\"djChart('VALUE2','2','"+data[i].INDEX_CD+"')\">"+data[i].VALUE2+"</td>"
			     +"<td onclick=\"djChart('VALUE3','2','"+data[i].INDEX_CD+"')\">"+data[i].VALUE3+"</td>"
			     +"<td onclick=\"djChart('VALUE4','2','"+data[i].INDEX_CD+"')\">"+data[i].VALUE4+"</td>"
			     +"<td onclick=\"djChart('VALUE6','2','"+data[i].INDEX_CD+"')\">"+data[i].VALUE6+"</td>"
			     +"<td onclick=\"djChart('VALUE7','2','"+data[i].INDEX_CD+"')\">"+data[i].VALUE7+"</td>"
			     +"<td onclick=\"djChart('VALUE8','2','"+data[i].INDEX_CD+"')\">"+data[i].VALUE8+"</td>"
		         +"<td onclick=\"openArea(2,'"+tabName+"','"+ data[i].INDEX_CD+"','"+ data[i].INDEX_NAME+"','"+data[i].ZONE_CODE+"','"+dataDate+"');\" class='unl'>辖区</td>"
			   +"</tr>";
	       if(i==0){
			  map.tabId   ="2";
			  map.indexCd =data[i].INDEX_CD;
			  map.zoneCode=zoneCode;	
			  map.dataDate=dataDate;
			  map.field   ="VALUE2";
             }
	      }
     }
    tableStr +="</table>"; 
    $("rpt_tabbar").innerHTML=tableStr;
    
     //加载图形
     loadChart(map);
}
/**
 * 单元格单击事件 
 * @param {Object} field
 */
function djChart(field,tabId,indexId){
	 var map=new Object();
	  map.tabId   =tabId;
	  map.indexCd =indexId;
	  map.zoneCode=zoneCode;	
	  map.dataDate=dataDate;
	  map.field   =field;
	  loadChart(map);
}

/**
 * chart图形
 * @param {Object} map
 */
function loadChart(map){
	var chart = new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId", "100%", "200","0", "0");
	PortalCommonCtrlr.getAreaChart(map, {callback:function (res) {
		    chart.setDataXML(res);
            chart.render("chartdiv");
	}});
	
}



/**
 * 钻取
 * @param {Object} tabId
 * @param {Object} indexId
 */
function openArea(tabId,tabName,indexId,indexName,zoneCode,dataDate){
	   //dhx.alert("没有营业区级数据,谢谢！");
        var url =base+"/portalCommon/module/portal/areaDrill.jsp?dataDate=" + dataDate + "&tabId=" + tabId
                   + "&indexId=" + indexId + "&indexName=" + indexName + "&zoneCode=" + zoneCode +"&tabName=" +tabName;
        //alert(url)
        url = encodeURI(url);
        
        var param="height="+screen.availHeight+"px,width="+screen.availWidth+"px,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
        window.open (url,'newwindow',param);
}
dhx.ready(indexInit);
