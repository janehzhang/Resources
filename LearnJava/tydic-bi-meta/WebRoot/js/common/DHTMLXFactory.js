/**
 * DHTML对象工厂(目前只提供，窗体，下拉框，树，表格，日历控件）
 * 各种创建函数，传入唯一标识ID，可避免重复创建。
 * 通过此工厂创建的DHTML对象会在页面关闭时回收内存（代码过程中如果未销毁对象）
 */
var DHTMLXFactory={
	windws:{},	//
    combos:{},//
    trees:{},
    grids:{},
    calendars:{},
	createWindows:function(dhtmlxWindowsId){
		if(this.windws[dhtmlxWindowsId])
			return this.windws[dhtmlxWindowsId];
        this.windws[dhtmlxWindowsId]=new dhtmlXWindows();
        this.windws[dhtmlxWindowsId].DHTML_TYPE="dhtmlXWindows";
        this.windws[dhtmlxWindowsId].dhtmlxWindowsId=dhtmlxWindowsId;
		return this.windws[dhtmlxWindowsId];
	},
	createWindow:function(dhtmlxWindowsId,windowId,left, top, width, height){
		if(!this.windws[dhtmlxWindowsId]){
            this.windws[dhtmlxWindowsId] = this.createWindows(dhtmlxWindowsId);
		}
        if(!this.windws[dhtmlxWindowsId][windowId]){
            this.windws[dhtmlxWindowsId][windowId] = this.windws[dhtmlxWindowsId].createWindow(windowId,left, top, width, height);
            this.windws[dhtmlxWindowsId][windowId].windowId = windowId;
        }
        return this.windws[dhtmlxWindowsId][windowId];
	},
    createCombo:function(comboId,parent,name,width,optionType,tabIndex){
        if(!this.combos[comboId]){
            this.combos[comboId] = new dhtmlXCombo(parent,name,width,optionType,tabIndex);
            this.combos[comboId].DHTML_TYPE = "dhtmlXCombo";
            this.combos[comboId].enableOptionAutoWidth(true);
			this.combos[comboId].enableFilteringMode(true);
		    this.combos[comboId].enableOptionAutoPositioning(true);
		    if(this.combos[comboId].setAutoOpenListWidth)
				this.combos[comboId].setAutoOpenListWidth(true);
        }
        return this.combos[comboId];
    },
    createTree:function(treeId,htmlObject, width, height, rootId){
        if(!this.trees[treeId]){
            this.trees[treeId] = new dhtmlXTreeObject(htmlObject, width, height, rootId);
            this.trees[treeId].DHTML_TYPE = "dhtmlXTree";
        }
        return this.trees[treeId];
    },
    createGrid:function(gridId,cfg){
        if(!this.grids[gridId]){
            this.grids[gridId] = new dhtmlXGridObject(cfg);
            this.grids[gridId].DHTML_TYPE = "dhtmlXGrid";
        }
        return this.grids[gridId];
    },
    createCalendar:function(calendarId,a,b){
        if(!this.calendars[calendarId]){
            this.calendars[calendarId] = new dhtmlXCalendarObject(a,b);
            this.calendars[calendarId].DHTML_TYPE = "dhtmlXCalendar";
            if(this.calendars[calendarId].langData["zh"])
            	this.calendars[calendarId].loadUserLanguage('zh');
			this.calendars[calendarId].hideTime();
        }
        return this.calendars[calendarId];
    }
};
Destroy.addDestroyVar(DHTMLXFactory);