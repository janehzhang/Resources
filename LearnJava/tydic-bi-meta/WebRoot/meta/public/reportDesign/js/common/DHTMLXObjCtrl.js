/**
 * 获取DHTMLX对象工厂，系统所有DHTMLX对象都通过此类创建，以便最后统一销毁
 */
var DhtmlxObjFactory={
	windws:{},	//	窗口对象集合
    combos:{},//combo
    trees:{},
    grids:{},
    calendars:{},
	createWindows:function(dhtmlxWindowsId)
	{ 
		if(this.windws[dhtmlxWindowsId])
			return this.windws[dhtmlxWindowsId];
        this.windws[dhtmlxWindowsId]=new dhtmlXWindows();
        this.windws[dhtmlxWindowsId].DHTML_TYPE="dhtmlXWindows";
        this.windws[dhtmlxWindowsId].dhtmlxWindowsId=dhtmlxWindowsId;
		return this.windws[dhtmlxWindowsId];
	},
	createWindow:function(dhtmlxWindowsId,windowId,left, top, width, height)
	{ 
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
        }
        return this.calendars[calendarId];
    }
};
addDestroyVar(DhtmlxObjFactory);

