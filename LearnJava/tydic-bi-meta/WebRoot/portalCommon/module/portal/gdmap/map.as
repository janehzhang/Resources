package 
{
  import flash.events.MouseEvent;
  import flash.utils.Timer;
  import flash.events.TimerEvent;
  import flash.external.ExternalInterface;
  import flash.geom.ColorTransform;
  import flash.events.MouseEvent;
  import flash.events.Event;
  import flash.display.MovieClip;
  import flash.display.Stage;
	
  public class map extends MovieClip
  {
	  	private var _isOnly:Boolean = false;
		
		//初始化构造方法
		public function map()
		{
			stop();
			ready();
			frameBindEvent();
		}
		
		//获取鼠标位置
		public function setClew(X:Number,Y:Number,val:String):void
		{
			/*
				for(var o=0;o<Data.length;o++)
				{
					if(Data[o][0] === val)
					{
						clew.Ctitle.text = Data[o][2].split(":")[0];
						clew.Cnumber.text = Data[o][2].split(":")[1];
					}
				}
			
				if(X>120)
				{
					clew.x = X-125;
					if(Y>170)
					{
						clew.y = Y-45;
					}else
					{
						clew.y = Y+5;
					}
				}
				else
				{
					clew.x = X+5
					if(Y>170)
					{
						clew.y = Y-45;
					}else
					{
						clew.y = Y+5;
					}
				}

			*/
		}
		
		public function closeClew(X:Number,Y:Number):void
		{
			//callJS(X+"--->"+Y);
			//clew.x = X;
			//clew.y = Y;
		}
		
		//检测js是是否加载完成
		private function ready():void
		{
			if(ExternalInterface.available)
			{
				if(Boolean(ExternalInterface.call("JsReady")))
				{
					callJsWsf();
				} 
				else
				{
					var t:Timer = new Timer(100);
					t.addEventListener(TimerEvent.TIMER,goTimer);
					t.start();
				}

			}
		}
		
		//定时检测
		private function goTimer(e:TimerEvent):void
		{
			if(Boolean(ExternalInterface.call("JsReady")))
			{
				Timer(e.target).stop();
				callJsWsf();
			}
		}
		
		//测试装载完成后调用的方法
		public function callJsWsf():void
		{
			//进行js与as方法绑定
			ExternalInterface.addCallback("setMap",setMap);
			ExternalInterface.addCallback("gotoArea",gotoArea);
		}
		
		//下转到特定的城市
		public function gotoArea(area:String,isOnly:Boolean):void{
			//ExternalInterface.call("debug","设置为"+back);
			//back.visible=!isOnly;
			_isOnly = isOnly;
			gotoAndStop(area);
		}
		
		//调用界面js方法
		public function callJS(v:String):void
		{
			ExternalInterface.call("callJs",v);
			//this[v].alpha = 1;
		}
		
		//返回
		public function goBack():void
		{
			//ExternalInterface.call("debug","跳转为"+_isOnly);
			if(this._isOnly){
				back.visible=false;
			}else{
				back.addEventListener( MouseEvent.CLICK,function(e:Event):void{gotoAndStop(1);callJS("0");});
			}
		}
		
		//获取数据
		public var Data:Array = [];

		//js调用此方法，设置地图。
		public function setMap(val:Array):void
		{
			Data = val;
			if(!this._isOnly){
				loadColor();
			}
		}
		
		//帧事件绑定
		public function frameBindEvent():void
		{			
			addFrameScript(0,bindEvent);//全省
			addFrameScript(1,sgs);//韶关
			addFrameScript(2,mzs);//梅州
			addFrameScript(3,hys);//河源
			addFrameScript(4,hzs);//惠州
			addFrameScript(5,sws);//汕尾
			addFrameScript(6,jys);//揭阳
			addFrameScript(7,sts);//汕头
			addFrameScript(8,czs);//潮州
			addFrameScript(9,szs);//深圳
			addFrameScript(10,dgs);//东莞
			addFrameScript(11,gzs);//广州
			addFrameScript(12,fss);//佛山
			addFrameScript(13,zhs);//珠海
			addFrameScript(14,zss);//中山
			addFrameScript(15,jms);//江门
			addFrameScript(16,yjs);//阳江
			addFrameScript(17,zjs);//湛江
			addFrameScript(18,mms);//茂名
			addFrameScript(19,yfs);//云浮
			addFrameScript(20,zqs);//肇庆
			addFrameScript(21,qys);//清远
		}
		
		//鼠标进过色彩
		public function m_over(v:String):void
		{this[v].alpha = 0.5;
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
		public function m_out(v:String):void
		{
			var s:ColorTransform = new ColorTransform();
            var m:uint = uint(0xE86418);
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
				else
				{
					
            		this[v].transform.colorTransform = s;
				}
			}
		}
		
		//鼠标点击色彩
		public var chicked:String = "";
		public function m_click(v:String,A:String,t:String):void
		{
			loadColor();
			var s:ColorTransform = new ColorTransform();
            var m:uint = uint(0xF8E4D0);
            s.color = m;
			
			var s1:ColorTransform = new ColorTransform();
            var m1:uint = uint(0xC4E8FF);
            s1.color = m1;
			
			if(chicked !== v)
			{
				if(Data.length > 0)
				{
					for(var i=0;i<Data.length;i++)
					{
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
			}
			
			this[A].alpha = 1;
			chicked = v;
			
			callJS(t);
			
			//判断是否下转
			if(Data.length>0)
			{
				for(var u=0;u<Data.length;u++)
				{
					if(Data[u][0] === v)
					{
						if(Data[u][3] != "")
						{
							gotoAndStop(Data[u][3]);
						}
					}
				}
			}
			
		}
		
		//初始化颜色
		public function loadColor():void
		{
			
			
		}
		
		
		//地图区域绑定事件
		public function bindEvent():void
		{
			loadColor();
			
			Area_200.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_200","T_200","200");closeClew(-40,-40);});
			Area_200.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_200");setClew(e.stageX,e.stageY,"Area_200");});
			Area_200.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_200");closeClew(-40,-40);});
			
			Area_755.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_755","T_755","755");closeClew(-40,-40);});
			Area_755.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_755");setClew(e.stageX,e.stageY,"Area_755");});
			Area_755.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_755");closeClew(-40,-40);});
			
			Area_769.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_769","T_769","769");closeClew(-40,-40);});
			Area_769.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_769");setClew(e.stageX,e.stageY,"Area_769");});
			Area_769.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_769");closeClew(-40,-40);});
			
			Area_757.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_757","T_757","757");closeClew(-40,-40);});
			Area_757.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_757");setClew(e.stageX,e.stageY,"Area_757");});
			Area_757.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_757");closeClew(-40,-40);});
			
			Area_754.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_754","T_754","754");closeClew(-40,-40);});
			Area_754.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_754");setClew(e.stageX,e.stageY,"Area_754");});
			Area_754.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_754");closeClew(-40,-40);});
			
			Area_760.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_760","T_760","760");closeClew(-40,-40);});
			Area_760.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_760");setClew(e.stageX,e.stageY,"Area_760");});
			Area_760.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_760");closeClew(-40,-40);});
			
			Area_752.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_752","T_752","752");closeClew(-40,-40);});
			Area_752.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_752");setClew(e.stageX,e.stageY,"Area_752");});
			Area_752.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_752");closeClew(-40,-40);});
			
			Area_750.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_750","T_750","750");closeClew(-40,-40);});
			Area_750.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_750");setClew(e.stageX,e.stageY,"Area_750");});
			Area_750.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_750");closeClew(-40,-40);});
			
			Area_759.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_759","T_759","759");closeClew(-40,-40);});
			Area_759.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_759");setClew(e.stageX,e.stageY,"Area_759");});
			Area_759.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_759");closeClew(-40,-40);});
			
			Area_663.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_663","T_663","663");closeClew(-40,-40);});
			Area_663.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_663");setClew(e.stageX,e.stageY,"Area_663");});
			Area_663.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_663");closeClew(-40,-40);});
			
			Area_756.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_756","T_756","756");closeClew(-40,-40);});
			Area_756.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_756");setClew(e.stageX,e.stageY,"Area_756");});
			Area_756.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_756");closeClew(-40,-40);});
			
			Area_668.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_668","T_668","668");closeClew(-40,-40);});
			Area_668.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_668");setClew(e.stageX,e.stageY,"Area_668");});
			Area_668.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_668");closeClew(-40,-40);});
			
			Area_758.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_758","T_758","758");closeClew(-40,-40);});
			Area_758.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_758");setClew(e.stageX,e.stageY,"Area_758");});
			Area_758.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_758");closeClew(-40,-40);});
			
			Area_753.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_753","T_753","753");closeClew(-40,-40);});
			Area_753.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_753");setClew(e.stageX,e.stageY,"Area_753");});
			Area_753.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_753");closeClew(-40,-40);});
			
			Area_763.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_763","T_763","763");closeClew(-40,-40);});
			Area_763.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_763");setClew(e.stageX,e.stageY,"Area_763");});
			Area_763.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_763");closeClew(-40,-40);});
			
			Area_768.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_768","T_768","768");closeClew(-40,-40);});
			Area_768.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_768");setClew(e.stageX,e.stageY,"Area_768");});
			Area_768.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_768");closeClew(-40,-40);});
			
			Area_660.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_660","T_660","660");closeClew(-40,-40);});
			Area_660.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_660");setClew(e.stageX,e.stageY,"Area_660");});
			Area_660.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_660");closeClew(-40,-40);});
			
			Area_762.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_762","T_762","762");closeClew(-40,-40);});
			Area_762.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_762");setClew(e.stageX,e.stageY,"Area_762");});
			Area_762.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_762");closeClew(-40,-40);});
			
			Area_751.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_751","T_751","751");closeClew(-40,-40);});
			Area_751.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_751");setClew(e.stageX,e.stageY,"Area_751");});
			Area_751.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_751");closeClew(-40,-40);});
			
			Area_662.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_662","T_662","662");closeClew(-40,-40);});
			Area_662.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_662");setClew(e.stageX,e.stageY,"Area_662");});
			Area_662.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_662");closeClew(-40,-40);});
			
			Area_766.addEventListener( MouseEvent.CLICK,function(e:Event):void{m_click("Area_766","T_766","766");closeClew(-40,-40);});
			Area_766.addEventListener( MouseEvent.MOUSE_OVER,function(e:MouseEvent):void{m_over("Area_766");setClew(e.stageX,e.stageY,"Area_766");});
			Area_766.addEventListener( MouseEvent.MOUSE_OUT,function(e:MouseEvent):void{m_out("Area_766");closeClew(-40,-40);});
			
			all.addEventListener( MouseEvent.CLICK,function(e:Event):void{loadColor();chicked="";callJS("0");});

		}
		
		//韶关
		public function sgs():void
		{
			goBack();
			SG.A_751.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("751");});
			SG.A_17004310.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004310");});
			SG.A_17004019.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004019");});
			SG.A_17004017.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004017");});
		    SG.A_17004022.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004022");});
			SG.A_17004021.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004021");});
			SG.A_17004020.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004020");});
			SG.A_17004016.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004016");});
			SG.A_17004015.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004015");});
			SG.A_17004014.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("17004014");});
		}
		
		//梅州 数据正确
		public function mzs():void
		{
			goBack();
			MZ.A_753.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("753");}); 
			MZ.A_13123006.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123006");});
			MZ.A_13123005.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123005");});
			MZ.A_13123007.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123007");});
			MZ.A_13123001.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123001");});
			MZ.A_13123002.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123002");});
			MZ.A_13123008.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123008");});
			MZ.A_13123004.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123004");});
			MZ.A_13123003.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("13123003");});
		}
		
		//河源 数据正确
		public function hys():void
		{
			goBack();
			HY.A_762.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("762");});
			HY.A_5000101.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("5000101");});
			HY.A_5000601.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("5000601");});
			HY.A_5000401.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("5000401");});
			HY.A_5000501.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("5000501");});
		    HY.A_5000201.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("5000201");});
			HY.A_5000301.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("5000301");});
		}
		
	    //惠州 数据正确
		public function hzs():void
		{
			goBack();
			HZ.A_752.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("752");});//现业 验证
			HZ.A_7004444.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("7004444");});
			HZ.A_7004446.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("7004446");});
		    HZ.A_7004438.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("7004438");});
			HZ.A_7004443.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("7004443");});
			HZ.A_7004439.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("7004439");});
			HZ.A_7004445.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("7004445");});
			HZ.A_7991167.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("7991167");});
		}
		
		//汕尾 数据正确
		public function sws():void
		{
			goBack();
			SW.A_660.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("660");});
			SW.A_16000002.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("16000002");});
			SW.A_16000065.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("16000065");});
			SW.A_16000172.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("16000172");});
		    SW.A_16000280.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("16000280");});
		}
		
		//揭阳
		public function jys():void
		{	
			goBack();
			JY.A_663.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("663");});
			JY.A_11000002.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("11000002");});
			JY.A_11000006.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("11000006");});
			JY.A_11000004.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("11000004");});
		    JY.A_11000005.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("11000005");});
			JY.A_11000003.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("11000003");});
		}
		
		//汕头  数据正确 
		public function sts():void
		{
			goBack();
			ST.A_754.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("754");});
			ST.A_15318914.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("15318914");});
			ST.A_15318913.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("15318913");});
			ST.A_15318910.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("15318910");});
		    ST.A_15318911.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("15318911");});
			ST.A_15318912.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("15318912");});
		}
		
		//潮州 
		public function czs():void
		{
			goBack();
			CZ.A_768.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("768");});
			CZ.A_18100003.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("18100003");});
			CZ.A_18100001.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("18100001");});
			CZ.A_18100004.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("18100004");});
		}
		
		//深圳 数据正确
		public function szs():void
		{
			goBack();
			SZ.A_755.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("755");});
			SZ.A_1000401.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000401");});
			SZ.A_1000105.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000105");});
			SZ.A_1000102.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000102");});
			SZ.A_1000501.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000501");});
			SZ.A_1000305.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000305");});
			SZ.A_1000101.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000101");});
			SZ.A_1000103.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000103");});
			SZ.A_1000201.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000201");});
			SZ.A_1000104.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("1000104");});
		}
		
		//东莞
		public function dgs():void
		{
			goBack();
			DG.A_769.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("769");});
			DG.A_2120004.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120004");});
			DG.A_2120009.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120009");});
			DG.A_2120003.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120003");});
			DG.A_2120006.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120006");});
			DG.A_2120001.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120001");});
			DG.A_2120011.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120011");});
			DG.A_2120010.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120010");});
			DG.A_2120033.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120033");});
			DG.A_2120007.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120007");});
			DG.A_2120020.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120020");});
			DG.A_2120016.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("2120016");});
			
		}
		
		//广州  验证正确
		public function gzs():void
		{
			goBack();
			GZ.A_200.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("200");});
			GZ.A_11149.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("11149");});
			GZ.A_10005.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10005");});
			GZ.A_10006.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10006");});
		    GZ.A_10002.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10002");});
			GZ.A_10307.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10307");});
			GZ.A_10003.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10003");});
			GZ.A_10922.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10922");});
			GZ.A_10317.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10317");});
			GZ.A_4174.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4174");});
			GZ.A_4050.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4050");});
			GZ.A_10061.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10061");});
			GZ.A_10004.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10004");});
		}
        
       //佛山
		public function fss():void
		{
			goBack();
			FS.A_757.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("757");});
			FS.A_3120391.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("3120391");});
			FS.A_3120521.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("3120521");});
		    FS.A_3121148.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("3121148");});
			FS.A_3121618.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("3121618");});
			FS.A_3120996.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("3120996");});
			
		}
		
        //珠海
		public function zhs():void
		{
			goBack();
			ZH.A_756.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("756");});
			ZH.A_19010080.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("19010080");});
			ZH.A_19010067.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("19010067");});
			ZH.A_19010088.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("19010088");});
			ZH.A_19010078.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("19010078");});
			ZH.A_19010063.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("19010063");});
			
		}
		
		//中山
		public function zss():void
		{
			goBack();
			ZS.A_760.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("760");});
			ZS.A_4451389.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451389");});
			ZS.A_4451394.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451394");});
			ZS.A_4451393.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451393");});
			ZS.A_4451396.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451396");});
			ZS.A_4451402.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451402");});
			ZS.A_4451398.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451398");});
			ZS.A_4451403.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451403");});
			ZS.A_4451391.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("4451391");});

		}

       //江门 
	   public function jms():void
		{
			goBack();
			JM.A_750.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("750");});
			JM.A_10005229.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10005229");});			
			JM.A_10004051.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10004051");});
			JM.A_10004034.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10004034");});
			JM.A_10004063.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10004063");});
			JM.A_10004803.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10004803");});
			JM.A_10004085.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("10004085");});
		}
        
		//阳江 正确 
		public function yjs():void
		{
			goBack();
			YJ.A_662.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("662");});
			YJ.A_100057287.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("100057287");});
			YJ.A_21100001.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("21100001");});
			YJ.A_21100003.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("21100003");});
			YJ.A_21100004.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("21100004");});
			YJ.A_21100005.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("21100005");});
		}
        
		//湛江 
		public function zjs():void
		{
			goBack();
			ZJ.A_759.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("759");});
			ZJ.A_100261743.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("100261743");});
			ZJ.A_9000393.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("9000393");});
			ZJ.A_9000276.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("9000276");});
			ZJ.A_9000058.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("9000058");});
			ZJ.A_9000453.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("9000453");});
			ZJ.A_100261742.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("100261742");});
			ZJ.A_9000282.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("9000282");});
			ZJ.A_9000394.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("9000394");});
			ZJ.A_9000395.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("9000395");});
		}
		
        //茂名
		public function mms():void
		{
			goBack();
			MM.A_668.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("668");});
			MM.A_12123163.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("12123163");});
			MM.A_12123165.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("12123165");});
		    MM.A_12123166.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("12123166");});
			MM.A_12123162.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("12123162");});
			MM.A_12123161.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("12123161");});
			MM.A_12123164.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("12123164");});
		}
		
        //云浮
		public function yfs():void
		{
			goBack();
			YF.A_766.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("766");});
			YF.A_20123157.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("20123157");});
			YF.A_20123158.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("20123158");});
		    YF.A_20123159.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("20123159");});
			YF.A_20123160.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("20123160");});
			YF.A_20123156.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("20123156");});
		}
		
        //肇庆
		public function zqs():void
		{
			goBack();
			ZQ.A_758.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("758");});
			ZQ.A_8006547.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006547");});
			ZQ.A_8006548.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006548");});
			ZQ.A_8006550.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006550");});
			ZQ.A_8006551.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006551");});
			ZQ.A_8006552.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006552");});
			ZQ.A_8006553.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006553");});
			ZQ.A_8006554.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006554");});
			ZQ.A_8006555.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("8006555");});
			
		}
		
        //清远
		public function qys():void
		{
			goBack();
			QY.A_763.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("763");});
			QY.A_14004031.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004031");});
			QY.A_14004034.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004034");});
			QY.A_14004037.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004037");});
			QY.A_14004038.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004038");});
			QY.A_14004032.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004032");});
			QY.A_14004036.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004036");});
			QY.A_14004035.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004035");});
			QY.A_14004033.addEventListener( MouseEvent.CLICK,function(e:Event):void{callJS("14004033");});
		}


  }
}