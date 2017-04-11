package {
import flash.events.MouseEvent;
import flash.utils.Timer;
import flash.events.TimerEvent;
import flash.external.ExternalInterface;
import flash.geom.ColorTransform;
import flash.events.MouseEvent;
import flash.events.Event;
import flash.display.MovieClip;
import flash.display.Stage;

public class map extends MovieClip {
    private var _isOnly:Boolean = false;
    //初始化构造方法
    public function map() {
        stop();
        ready();
        frameBindEvent();
    }

    //获取鼠标位置
    public function setClew(X:Number, Y:Number, val:String):void {

    }

    public function closeClew(X:Number, Y:Number):void {

    }

    //检测js是是否加载完成
    private function ready():void {
        if (ExternalInterface.available) {
            if (Boolean(ExternalInterface.call("JsReady"))) {
                callJsWsf();
            }
            else {
                var t:Timer = new Timer(100);
                t.addEventListener(TimerEvent.TIMER, goTimer);
                t.start();
            }

        }
    }

    //定时检测
    private function goTimer(e:TimerEvent):void {
        if (Boolean(ExternalInterface.call("JsReady"))) {
            Timer(e.target).stop();
            callJsWsf();
        }
    }

    //测试装载完成后调用的方法
    public function callJsWsf():void {

        //进行js与as方法绑定
        ExternalInterface.addCallback("setMap", setMap);
        ExternalInterface.addCallback("gotoArea", gotoArea);

        ExternalInterface.call("setData()");
    }

    //调用界面js方法
    public function callJS(v:String):void {
        ExternalInterface.call("callJs", v);
        //this[v].alpha = 1;
    }

    //返回
    public function goBack():void {
        if (this._isOnly) {
            back.visible = false;
        } else {
            back.addEventListener(MouseEvent.CLICK, function (e:Event):void {
                gotoAndStop(1);
                callJS("0");
            });
        }
    }

    //获取数据
    public var Data:Array = [
        ['Area_028', '0x0068B0', true, 'A_028'],
        ['Area_0812', '0x0068B0', true, 'A_0812'],
        ['Area_0813', '0x0068B0', true, 'A_0813'],
        ['Area_0816', '0x0068B0', true, 'A_0816'],
        ['Area_0817', '0x0068B0', true, 'A_0817'],
        ['Area_0818', '0x0068B0', true, 'A_0818'],
        ['Area_0825', '0x0068B0', true, 'A_0825'],
        ['Area_0826', '0x0068B0', true, 'A_0826'],
        ['Area_0827', '0x0068B0', true, 'A_0827'],
        ['Area_0830', '0x0068B0', true, 'A_0830'],
        ['Area_0831', '0x0068B0', true, 'A_0831'],
        ['Area_0832', '0x0068B0', true, 'A_0832'],
        ['Area_0833', '0x0068B0', true, 'A_0833'],
        ['Area_0834', '0x0068B0', true, 'A_0834'],
        ['Area_0835', '0x0068B0', true, 'A_0835'],
        ['Area_0836', '0x0068B0', true, 'A_0836'],
        ['Area_0837', '0x0068B0', true, 'A_0837'],
        ['Area_0838', '0x0068B0', true, 'A_0838'],
        ['Area_0839', '0x0068B0', true, 'A_0839'],
        ['Area_0840', '0x0068B0', true, 'A_0840'],
        ['Area_0841', '0x0068B0', true, 'A_0841']
    ];

    //js调用此方法，设置地图。
    public function setMap(val:Array):Boolean {
        //ExternalInterface.call("alert('setMap')");
        for (var i:uint = 0; i < val.length; i++) {
            var obj:Array = val[i] as Array;
            for (var j:uint = 0; j < Data.length; j++) {
                var dataObj = Data[j];
                if (obj[0] == dataObj[0]) {
                    dataObj[0] = obj[0];
                    dataObj[1] = obj[1];
                    if (obj.length > 2) {
                        dataObj[2] = obj[2];
                    }
                }
            }
        }
        loadColor();
        return true;
    }

    //下转到特定的城市
    public function gotoArea(area:String, isOnly:Boolean):void {
        gotoAndStop(area);
        _isOnly = isOnly;
    }

    //帧事件绑定
    public function frameBindEvent():void {
        addFrameScript(0, bindEvent);//全省
        addFrameScript(1, abs);//阿坝
        addFrameScript(2, bzs);//巴中
        addFrameScript(3, cds);//成都
        addFrameScript(4, dzs);//达州
        addFrameScript(5, dys);//德阳
        addFrameScript(6, gzs);//甘孜
        addFrameScript(7, gas);//广安
        addFrameScript(8, gys);//广元
        addFrameScript(9, njs);//内江
        addFrameScript(10, lss);//乐山
        addFrameScript(11, lszs);//凉山州
        addFrameScript(12, lzs);//泸州
        addFrameScript(13, mss);//眉山
        addFrameScript(14, mys);//绵阳
        addFrameScript(15, nys);//南充
        addFrameScript(16, pzhs);//攀枝花
        addFrameScript(17, sns);//遂宁
        addFrameScript(18, yas);//雅安
        addFrameScript(19, ybs);//宜宾
        addFrameScript(20, zgs);//自贡
        addFrameScript(21, zys);//资阳
    }

    //鼠标进过色彩
    public function m_over(v:String):void {
        this[v].alpha = 0.5;
        /*
         var s:ColorTransform = new ColorTransform();
         var m:uint = uint(0xF0B478);
         s.color = m;

         var s1:ColorTransform = new ColorTransform();

         if(chicked !== v)
         {
         if(Data.length > 0)
         {
         for(var i=0;i<Data.length;i++)
         {

         var m1:uint = new uint( Data[i][1]);
         s1.color = m1;
         this[Data[i][0]].transform.colorTransform = s1;
         if(Data[i][0] === v)
         {
         if(Data[i][1] === true)
         {
         this[v].transform.colorTransform = s;
         }else
         {
         this[v].transform.colorTransform = s1;
         }
         }

         }
         }
         else
         {
         this[v].transform.colorTransform = s;
         }
         }*/
    }

    //鼠标移出色彩
    public function m_out(v:String):void {
        var s:ColorTransform = new ColorTransform();
        var m:uint = uint(0xE86418);
        s.color = m;

        var s1:ColorTransform = new ColorTransform();


        if (chicked !== v) {
            if (Data.length > 0) {
                for (var i = 0; i < Data.length; i++) {
                    var m1:uint = new uint(Data[i][1]);
                    s1.color = m1;
                    this[Data[i][0]].transform.colorTransform = s1;
                    /*if(Data[i][0] === v)
                     {
                     if(Data[i][1] === true)
                     {
                     this[v].transform.colorTransform = s;
                     }else
                     {
                     this[v].transform.colorTransform = s1;
                     }
                     }*/

                }
            }
            else {

                this[v].transform.colorTransform = s;
            }
        }
    }

    //鼠标点击色彩
    public var chicked:String = "";

    public function m_click(v:String, A:String, t:String):void {
        loadColor();
        var s:ColorTransform = new ColorTransform();
        var m:uint = uint(0xF8E4D0);
        s.color = m;

        var s1:ColorTransform = new ColorTransform();
        var m1:uint = uint(0xC4E8FF);
        s1.color = m1;

        if (chicked !== v) {
            if (Data.length > 0) {
                for (var i = 0; i < Data.length; i++) {
                    if (Data[i][0] === v) {
                        if (Data[i][1] === true) {
                            this[v].transform.colorTransform = s;
                        } else {
                            this[v].transform.colorTransform = s1;
                        }
                    }

                }
            }
            else {
                this[v].transform.colorTransform = s;
            }
        }

        this[A].alpha = 1;
        chicked = v;

        callJS(t);

        //判断是否下转
        if (Data.length > 0) {
            for (var u = 0; u < Data.length; u++) {
                if (Data[u][0] === v) {
                    if (Data[u][2]) {
                        gotoAndStop(Data[u][3]);
                    }
                }
            }
        }

    }

    //初始化颜色
    public function loadColor():void {
        var s:ColorTransform = new ColorTransform();
        var m:uint = uint(0xE86418);
        s.color = m;

        var s1:ColorTransform = new ColorTransform();


        if (Data.length > 0) {

            for (var i = 0; i < Data.length; i++) {
                var m1:uint = new uint(Data[i][1]);
                s1.color = m1;
                this[Data[i][0]].transform.colorTransform = s1;

                /*if(Data[i][1])
                 {
                 this[Data[i][0]].transform.colorTransform = s;
                 }
                 else
                 {
                 this[Data[i][0]].transform.colorTransform = s1;
                 }*/
            }
        }
        else {
            Area_028.transform.colorTransform = s;
            Area_0812.transform.colorTransform = s;
            Area_0813.transform.colorTransform = s;
            Area_0816.transform.colorTransform = s;
            Area_0817.transform.colorTransform = s;
            Area_0818.transform.colorTransform = s;
            Area_0825.transform.colorTransform = s;
            Area_0826.transform.colorTransform = s;
            Area_0827.transform.colorTransform = s;
            Area_0830.transform.colorTransform = s;
            Area_0831.transform.colorTransform = s;
            Area_0832.transform.colorTransform = s;
            Area_0833.transform.colorTransform = s;
            Area_0834.transform.colorTransform = s;
            Area_0835.transform.colorTransform = s;
            Area_0836.transform.colorTransform = s;
            Area_0837.transform.colorTransform = s;
            Area_0838.transform.colorTransform = s;
            Area_0839.transform.colorTransform = s;
            Area_0840.transform.colorTransform = s;
            Area_0841.transform.colorTransform = s;
        }

        T_028.alpha = 0;
        T_0812.alpha = 0;
        T_0813.alpha = 0;
        T_0816.alpha = 0;
        T_0817.alpha = 0;
        T_0818.alpha = 0;
        T_0825.alpha = 0;
        T_0826.alpha = 0;
        T_0827.alpha = 0;
        T_0830.alpha = 0;
        T_0831.alpha = 0;
        T_0832.alpha = 0;
        T_0833.alpha = 0;
        T_0834.alpha = 0;
        T_0835.alpha = 0;
        T_0836.alpha = 0;
        T_0837.alpha = 0;
        T_0838.alpha = 0;
        T_0839.alpha = 0;
        T_0840.alpha = 0;
        T_0841.alpha = 0;

    }


    //地图区域绑定事件
    public function bindEvent():void {
        loadColor();

        Area_028.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_028", "T_028", "028");
            closeClew(-40, -40);
        });
        Area_028.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_028");
            setClew(e.stageX, e.stageY, "Area_028");
        });
        Area_028.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_028");
            closeClew(-40, -40);
        });

        Area_0812.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0812", "T_0812", "0812");
            closeClew(-40, -40);
        });
        Area_0812.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0812");
            setClew(e.stageX, e.stageY, "Area_0812");
        });
        Area_0812.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0812");
            closeClew(-40, -40);
        });

        Area_0813.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0813", "T_0813", "0813");
            closeClew(-40, -40);
        });
        Area_0813.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0813");
            setClew(e.stageX, e.stageY, "Area_0813");
        });
        Area_0813.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0813");
            closeClew(-40, -40);
        });

        Area_0816.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0816", "T_0816", "0816");
            closeClew(-40, -40);
        });
        Area_0816.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0816");
            setClew(e.stageX, e.stageY, "Area_0816");
        });
        Area_0816.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0816");
            closeClew(-40, -40);
        });

        Area_0817.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0817", "T_0817", "0817");
            closeClew(-40, -40);
        });
        Area_0817.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0817");
            setClew(e.stageX, e.stageY, "Area_0817");
        });
        Area_0817.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0817");
            closeClew(-40, -40);
        });

        Area_0818.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0818", "T_0818", "0818");
            closeClew(-40, -40);
        });
        Area_0818.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0818");
            setClew(e.stageX, e.stageY, "Area_0818");
        });
        Area_0818.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0818");
            closeClew(-40, -40);
        });

        Area_0825.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0825", "T_0825", "0825");
            closeClew(-40, -40);
        });
        Area_0825.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0825");
            setClew(e.stageX, e.stageY, "Area_0825");
        });
        Area_0825.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0825");
            closeClew(-40, -40);
        });

        Area_0826.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0826", "T_0826", "0826");
            closeClew(-40, -40);
        });
        Area_0826.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0826");
            setClew(e.stageX, e.stageY, "Area_0826");
        });
        Area_0826.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0826");
            closeClew(-40, -40);
        });

        Area_0827.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0827", "T_0827", "0827");
            closeClew(-40, -40);
        });
        Area_0827.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0827");
            setClew(e.stageX, e.stageY, "Area_0827");
        });
        Area_0827.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0827");
            closeClew(-40, -40);
        });

        Area_0830.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0830", "T_0830", "0830");
            closeClew(-40, -40);
        });
        Area_0830.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0830");
            setClew(e.stageX, e.stageY, "Area_0830");
        });
        Area_0830.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0830");
            closeClew(-40, -40);
        });

        Area_0831.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0831", "T_0831", "0831");
            closeClew(-40, -40);
        });
        Area_0831.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0831");
            setClew(e.stageX, e.stageY, "Area_0831");
        });
        Area_0831.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0831");
            closeClew(-40, -40);
        });

        Area_0832.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0832", "T_0832", "0832");
            closeClew(-40, -40);
        });
        Area_0832.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0832");
            setClew(e.stageX, e.stageY, "Area_0832");
        });
        Area_0832.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0832");
            closeClew(-40, -40);
        });

        Area_0833.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0833", "T_0833", "0833");
            closeClew(-40, -40);
        });
        Area_0833.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0833");
            setClew(e.stageX, e.stageY, "Area_0833");
        });
        Area_0833.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0833");
            closeClew(-40, -40);
        });

        Area_0834.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0834", "T_0834", "0834");
            closeClew(-40, -40);
        });
        Area_0834.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0834");
            setClew(e.stageX, e.stageY, "Area_0834");
        });
        Area_0834.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0834");
            closeClew(-40, -40);
        });

        Area_0835.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0835", "T_0835", "0835");
            closeClew(-40, -40);
        });
        Area_0835.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0835");
            setClew(e.stageX, e.stageY, "Area_0835");
        });
        Area_0835.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0835");
            closeClew(-40, -40);
        });

        Area_0836.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0836", "T_0836", "0836");
            closeClew(-40, -40);
        });
        Area_0836.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0836");
            setClew(e.stageX, e.stageY, "Area_0836");
        });
        Area_0836.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0836");
            closeClew(-40, -40);
        });

        Area_0837.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0837", "T_0837", "0837");
            closeClew(-40, -40);
        });
        Area_0837.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0837");
            setClew(e.stageX, e.stageY, "Area_0837");
        });
        Area_0837.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0837");
            closeClew(-40, -40);
        });

        Area_0838.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0838", "T_0838", "0838");
            closeClew(-40, -40);
        });
        Area_0838.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0838");
            setClew(e.stageX, e.stageY, "Area_0838");
        });
        Area_0838.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0838");
            closeClew(-40, -40);
        });

        Area_0839.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0839", "T_0839", "0839");
            closeClew(-40, -40);
        });
        Area_0839.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0839");
            setClew(e.stageX, e.stageY, "Area_0839");
        });
        Area_0839.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0839");
            closeClew(-40, -40);
        });

        Area_0840.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0840", "T_0840", "0840");
            closeClew(-40, -40);
        });
        Area_0840.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0840");
            setClew(e.stageX, e.stageY, "Area_0840");
        });
        Area_0840.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0840");
            closeClew(-40, -40);
        });

        Area_0841.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            m_click("Area_0841", "T_0841", "0841");
            closeClew(-40, -40);
        });
        Area_0841.addEventListener(MouseEvent.MOUSE_OVER, function (e:MouseEvent):void {
            m_over("Area_0841");
            setClew(e.stageX, e.stageY, "Area_0841");
        });
        Area_0841.addEventListener(MouseEvent.MOUSE_OUT, function (e:MouseEvent):void {
            m_out("Area_0841");
            closeClew(-40, -40);
        });

        all.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            loadColor();
            chicked = "";
            callJS("0");
        });

    }

    //阿坝(地图上少四个标示)
    public function abs():void {
        goBack();
        Ab.A_185.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("9");
        });
        Ab.A_186.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("8");
        });
        Ab.A_187.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("3");
        });
        Ab.A_188.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("5");
        });
        Ab.A_189.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("4");
        });
        Ab.A_190.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("11");
        });
        Ab.A_191.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("12");
        });
        Ab.A_192.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("13");
        });
        Ab.A_193.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("10");
        });
        Ab.A_194.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("6");
        });
        Ab.A_195.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("7");
        });
        Ab.A_196.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("1");
        });
        Ab.A_197.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("2");
        });
    }

    //巴中 数据正确
    public function bzs():void {
        goBack();
        Bz.A_64.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("15");
        });
        Bz.A_65.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("17");
        });
        Bz.A_66.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("18");
        });
        Bz.A_67.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("16");
        });
    }

    //成都 数据正确
    public function cds():void {
        goBack();
        Cd.A_20.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("20");
        });
        Cd.A_1.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("34");
        });
        Cd.A_2.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("38");
        });
        Cd.A_3.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("31");
        });
        Cd.A_4.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("35");
        });
        Cd.A_5.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("27");
        });
        Cd.A_6.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("32");
        });
        Cd.A_7.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("39");
        });
        Cd.A_8.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("28");
        });
        Cd.A_9.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("36");
        });
        Cd.A_11.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("29");
        });
        Cd.A_12.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("40");
        });
        Cd.A_13.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("33");
        });
        Cd.A_14.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("37");
        });
        Cd.A_15.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("30");
        });
        Cd.A_225.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("21");
        });
        Cd.A_228.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("22");
        });
        Cd.A_227.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("23");
        });
        Cd.A_229.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("24");
        });
        Cd.A_222.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("25");
        });
        Cd.A_226.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("26");
        });
    }

    //达州 数据正确
    public function dzs():void {
        goBack();
        Dz.A_48.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("42");
        });//现业 验证
        Dz.A_49.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("47");
        });
        Dz.A_50.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("46");
        });
        Dz.A_51.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("45");
        });
        Dz.A_52.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("44");
        });
        Dz.A_53.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("43");
        });
    }

    //德阳 数据正确
    public function dys():void {
        goBack();
        Dy.A_128.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("209");
        });
        Dy.A_129.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("53");
        });
        Dy.A_130.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("49");
        });
        Dy.A_131.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("51");
        });
        Dy.A_132.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("52");
        });
        Dy.A_133.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("50");
        });
    }

    //甘孜(地图少1个标识)
    public function gzs():void {
        goBack();
        Gz.A_153.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("55");
        });
        Gz.A_154.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("72");
        });
        Gz.A_155.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("60");
        });
        Gz.A_156.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("61");
        });
        Gz.A_157.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("62");
        });
        Gz.A_158.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("64");
        });
        Gz.A_159.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("65");
        });
        Gz.A_160.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("66");
        });
        Gz.A_161.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("63");
        });
        Gz.A_162.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("67");
        });
        Gz.A_163.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("56");
        });
        Gz.A_164.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("58");
        });
        Gz.A_165.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("57");
        });
        Gz.A_166.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("59");
        });
        Gz.A_167.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("68");
        });
        Gz.A_168.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("69");
        });
        Gz.A_169.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("70");
        });
        Gz.A_170.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("71");
        });
    }

    //广安  数据正确
    public function gas():void {
        goBack();
        Ga.A_59.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("74");
        });
        Ga.A_60.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("76");
        });
        Ga.A_61.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("77");
        });
        Ga.A_62.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("78");
        });
        Ga.A_63.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("75");
        });
    }

    //广元
    public function gys():void {
        goBack();
        Gy.A_134.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("80");
        });
        Gy.A_135.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("84");
        });
        Gy.A_136.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("85");
        });
        Gy.A_137.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("86");
        });
        Gy.A_138.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("87");
        });
        Gy.A_139.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("82");
        });
        Gy.A_8311.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("83");
        });
        Gy.A_140.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("81");
        });


    }

    //内江 数据正确
    public function njs():void {
        goBack();
        Nj.A_86.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("155");
        });
        Nj.A_87.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("158");
        });
        Nj.A_88.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("157");
        });
        Nj.A_89.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("156");
        });
    }

    //乐山
    public function lss():void {
        goBack();
        Ls.A_90.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("89");
        });
        Ls.A_91.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("91");
        });
        Ls.A_92.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("93");
        });
        Ls.A_93.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("98");
        });
        Ls.A_94.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("90");
        });
        Ls.A_95.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("94");
        });
        Ls.A_96.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("96");
        });
        Ls.A_97.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("97");
        });
        Ls.A_98.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("92");
        });
        Ls.A_99.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("100");
        });
        Ls.A_100.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("95");
        });
        Ls.A_101.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("99");
        });

    }

    //凉山州  验证正确
    public function lszs():void {
        goBack();
        Lsz.A_102.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("102");
        });
        Lsz.A_103.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("113");
        });
        Lsz.A_104.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("112");
        });
        Lsz.A_105.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("110");
        });
        Lsz.A_106.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("111");
        });
        Lsz.A_107.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("118");
        });
        Lsz.A_108.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("108");
        });
        Lsz.A_109.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("103");
        });
        Lsz.A_110.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("104");
        });
        Lsz.A_111.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("105");
        });
        Lsz.A_112.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("107");
        });
        Lsz.A_113.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("106");
        });
        Lsz.A_114.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("114");
        });
        Lsz.A_115.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("116");
        });
        Lsz.A_116.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("117");
        });
        Lsz.A_117.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("109");
        });
        Lsz.A_118.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("115");
        });
    }

    //泸州
    public function lzs():void {
        goBack();
        Lz.A_69.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("120");
        });
        Lz.A_70.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("123");
        });
        Lz.A_71.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("125");
        });
        Lz.A_72.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("124");
        });
        Lz.A_73.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("122");
        });
        Lz.A_74.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("121");
        });

    }

    //眉山
    public function mss():void {
        goBack();
        Ms.A_142.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("127");
        });
        Ms.A_143.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("130");
        });
        Ms.A_144.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("129");
        });
        Ms.A_145.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("131");
        });
        Ms.A_146.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("132");
        });
        Ms.A_147.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("133");
        });
        Ms.A_220.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("128");
        });

    }

    //绵阳
    public function mys():void {
        goBack();
        My.A_28.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("135");
        });
        My.A_29.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("136");
        });
        My.A_30.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("141");
        });
        My.A_31.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("142");
        });
        My.A_32.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("138");
        });
        My.A_33.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("137");
        });
        My.A_34.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("140");
        });
        My.A_35.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("139");
        });
    }

    //南充
    public function nys():void {
        goBack();
        //Ny.A_36.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("144");});
        Ny.A_37.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("153");
        });
        Ny.A_173.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("150");
        });
        Ny.A_174.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("149");
        });
        Ny.A_175.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("148");
        });
        Ny.A_176.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("152");
        });
        Ny.A_177.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("151");
        });
        Ny.A_36.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("145");
        });
        Ny.A_171.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("146");
        });
        Ny.A_172.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("147");
        });
    }

    //攀枝花 正确
    public function pzhs():void {
        goBack();
        Pzh.A_16.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("160");
        });
        Pzh.A_17.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("161");
        });
        Pzh.A_18.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("162");
        });
        Pzh.A_500.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("210");
        });
        Pzh.A_501.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("211");
        });
        Pzh.A_502.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("212");
        });
        Pzh.A_503.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("213");
        });
        Pzh.A_504.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("214");
        });
    }

    //遂宁 正确
    public function sns():void {
        goBack();
        Sn.A_55.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("164");
        });
        Sn.A_56.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("167");
        });
        Sn.A_57.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("166");
        });
        Sn.A_58.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("168");
        });
        Sn.A_508.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("165");
        });
    }

    //雅安 正确
    public function yas():void {
        goBack();
        Ya.A_120.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("170");
        });
        Ya.A_121.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("171");
        });
        Ya.A_122.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("175");
        });
        Ya.A_123.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("176");
        });
        Ya.A_124.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("177");
        });
        Ya.A_125.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("172");
        });
        Ya.A_126.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("173");
        });
        Ya.A_127.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("174");
        });
    }

    //宜宾 正确
    public function ybs():void {
        goBack();
        Yb.A_76.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("179");
        });
        Yb.A_77.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("188");
        });
        Yb.A_78.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("183");
        });
        Yb.A_79.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("180");
        });
        Yb.A_80.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("186");
        });
        Yb.A_81.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("184");
        });
        Yb.A_82.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("182");
        });
        Yb.A_83.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("181");
        });
        Yb.A_84.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("187");
        });
        Yb.A_85.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("185");
        });
    }

    //自贡 正确
    public function zgs():void {
        goBack();
        Zg.A_25.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("195");
        });
        Zg.A_26.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("200");
        });
        Zg.A_27.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("199");
        });
        Zg.A_505.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("196");
        });
        Zg.A_506.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("197");
        });
        Zg.A_507.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("198");
        });
    }

    //资阳 正确
    public function zys():void {
        goBack();
        Zy.A_149.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("190");
        });
        Zy.A_150.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("191");
        });
        Zy.A_151.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("193");
        });
        Zy.A_152.addEventListener(MouseEvent.CLICK, function (e:Event):void {
            callJS("192");
        });
    }


}
}