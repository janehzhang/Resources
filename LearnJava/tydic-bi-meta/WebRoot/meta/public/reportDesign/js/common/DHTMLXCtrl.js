//销毁DHTML对象的工具函数
function destructorDHMLX(obj)
{
    try{
        if(!obj)return;
        if(obj.DHTML_TYPE)
        {
            switch(obj.DHTML_TYPE)
            {
            case "dhtmlXCombo":
                var a=dhx_glbSelectAr;
                for(var b=0;b<a.length;b++) {
                    if(a[b]==obj)
                    {a[b]=null;a.splice(b,1);break;}
                }
            case "dhtmlXGrid":
            case "dhtmlXTree":
                tryEval(obj.destructor);
                break;
            case "dhtmlXCalendar":
                obj.i=[];
                tryEval(obj.unload);
            case "dhtmlXWindows":
                tryEval(obj[o].unload);
                break;
            }
            return;
        }
        for(var o in obj)
        {
            if(obj[o] && obj[o].DHTML_TYPE)
            {
                switch(obj[o].DHTML_TYPE)
                {
                case "dhtmlXCombo":
                case "dhtmlXGrid":
                case "dhtmlXTree":
                    if(obj[o].DHTML_TYPE=="dhtmlXCombo"){
                        var a=dhx_glbSelectAr;
                        for(var b=0;b<a.length;b++)
                            if(a[b]==obj[o])
                            {a[b]=null;a.splice(b,1);break;}
                    }
                    tryEval(obj[o].destructor);
                    break;
                case "dhtmlXCalendar":
                    obj[o].i=[];
                    tryEval(obj[o].unload);
                    break;
                case "dhtmlXWindows":
                    tryEval(obj[o].unload);
                    break;
                }
            }
            else if(obj[o] && obj[o]!=obj && typeof obj[o]=="object")
            {
                destructorDHMLX(obj[o]);
            }
        }
    }catch(ex){}
}