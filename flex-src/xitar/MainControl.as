package xitar
{
	import flash.events.Event;
	import flash.utils.getQualifiedClassName;
	
	import mx.controls.Alert;
	import mx.events.ModuleEvent;
	import mx.modules.IModuleInfo;
	import mx.modules.ModuleManager;
	
	import xitar.events.*;
	
	[Event(name=InitEvent.INIT_DATA,            type="xitar.events.InitEvent")]
	[Event(name=InitEvent.INIT_DATA_LOADED,     type="xitar.events.InitEvent")]
	[Event(name=InitEvent.INIT_DATA_UPDATED,    type="xitar.events.InitEvent")]
	[Event(name=SessionEvent.SESSION_CHANGED,   type="xitar.events.sessionEvent")]
	[Event(name=SessionEvent.SET_SESSION,       type="xitar.events.sessionEvent")]
	[Event(name=SessionEvent.SESSION_FAILED,    type="xitar.events.sessionEvent")]
	[Event(name=LoginEvent.LOGIN,               type="xitar.events.LoginEvent")]
	[Event(name=ExitEvent.EXIT,                 type="xitar.events.ExitEvent")]
	[Event(name=RoomEvent.CLICK,                type="xitar.events.RoomEvent")]
	[Event(name=ModelEvent.INIT_UPDATED,        type="xitar.events.ModelEvent")]
	[Event(name=ModelEvent.LINK_STATE_UPDATED,  type="xitar.events.ModelEvent")]
	[Event(name=LongpullEvent.BUILDING_LONGPULL,type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.ROOM_LONGPULL,    type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.DESK_LONGPULL,    type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.BOARD_LONGPULL,   type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.ACTIVE,           type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.SUCCESS,          type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.COMPLETE,         type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.FAILE,            type="xitar.events.LongpullEvent")]
	[Event(name=ActionEvent.ACTIVE,             type="xitar.events.ActionEvent")]
	[Event(name=ActionEvent.SUCCESS,            type="xitar.events.ActionEvent")]
	[Event(name=ActionEvent.COMPLETE,           type="xitar.events.ActionEvent")]
	[Event(name=ActionEvent.FAILE,              type="xitar.events.ActionEvent")]
	public class MainControl extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const LIMIT_NUM:int = 1;    //Create object limit
		private static var created_object:int = 0; //Created Object
		private static var _mainObjectsPool:MainObjectsPool;
		
		//private propertied ---------------------------------------------------------------
		private var xitarMain:XitarMain;
		private var info:IModuleInfo; //For preload module,must be a global variable
		
		public function MainControl(xm:XitarMain){
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.MAINCONTROL;
			xitarMain = xm;
			try {
				_mainObjectsPool = MainObjectsPool.getInstance(this);
			} catch (error:Error) {
				Alert.show(error.message);
			} finally {}

			registObject(this);
			_allowInstantiation = false;
		}

		//Get function ---------------------------------------------------------------------
		public static function getInstance(xm:XitarMain):MainControl
		{
			_allowInstantiation = true;
			if(canGetInstance(LIMIT_NUM,created_object)){
				created_object++;
				return new MainControl(xm);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + " MainControl Class.");
		}
		
		public function getSwfFileName():String{
			var url:String = MainConstant.LOGIN_SWF_FILE;
			switch(MainModel.sessionStage){
				case MainConstant.ERROR_STAGE:
					url = MainConstant.ERROR_SWF_FILE;
					break;
				case MainConstant.LOGIN_STAGE:
					url = MainConstant.LOGIN_SWF_FILE;
					break;
				case MainConstant.BUILDING_STAGE:
					url = MainConstant.BUILDING_SWF_FILE;
					break;
				case MainConstant.ROOM_STAGE:
					url = MainConstant.BUILDING_SWF_FILE;
					break;
				case MainConstant.DESK_STAGE:
					url = MainConstant.BUILDING_SWF_FILE;
					break;
				case MainConstant.BOARD_STAGE:
					url = MainConstant.BOARD_SWF_FILE;
					break;
				default:
					Alert.show("MainControl  - getSwfFileName() - Out of range");
					break;
			}
			return url;
		}
		
		//regist object into control-----------------------------------------------------------------------------
		public static function registObject(ob:MainParent):void{
			_mainObjectsPool.registObject(ob);
		}
		
		//regist action listener---------------------------------------------------------------------------------
		public function registMainActionListener(ob:MainParent):void
		{
			if(ob != this) //can't listene self
			{
				var evs:Array = MainConstant.EVENTS;
				for each (var ev:Array in evs)
				{
					ob.addEventListener(ev[0],mainEventsHandler);
				}
				ob.registActionListener(this);
			}
		}
		public function mainEventsHandler(event:Event):void{
			if(event.target != this){ //can't listene self
				var evs:Array = MainConstant.EVENTS;
				for each (var ev:Array in evs)
				{
					if(event.type == ev[0])
					{
						if(this.hasOwnProperty(ev[1])){
							this[ev[1]](event);
						}else{
							Alert.show(this.toString() + " - " + MainLang.NOT_EXIST_THIS_FUNCTION + ev[1]);
						}
					}
				}
			}
		}
	
		//preload module functions ------------------------------------------------------------------------------
		private var preloadModuleReady : Boolean = false;
		private var loadTimes:int  = 0; //number of try to load modules num.
		public override function preloadModuleHandler(ev:Event):void 
		{
			var e:PreloadModuleEvent = ev as PreloadModuleEvent;
			preloadModuleReady = false;
            info = ModuleManager.getModule(e.preload_url);
			info.addEventListener(ModuleEvent.ERROR, preloadModuleErrorHandler);
            info.addEventListener(ModuleEvent.READY, preloadModuleReadyHandler);
            info.load();
        }
        private function preloadModuleReadyHandler(e:ModuleEvent):void {
        	if(MainConstant.XITAR_DEBUG){
        		Alert.show("Preload module " + e.type + " " + info.url);
        	}
            preloadModuleReady = true; // "ready"
			loadTimes = 0;
        }
		private function preloadModuleErrorHandler(e:ModuleEvent):void{
			if(loadTimes < MainConstant.ACCESS_TIMES){
				loadTimes++;
				info.load();
			}else{
				loadTimes = 0;
				throw new Error(MainLang.LOAD_MODULE_ERROR);
			}
		}
		
		//LongpullLinkEvent handler --------------------------------------------------------------
		public override function longpullActiveHandler(ev:Event):void
		{
			dispatchEvent(ev as LongpullEvent);
		}
		
		public override function longpullSuccessHandler(ev:Event):void
		{
			dispatchEvent(ev as LongpullEvent);
		}
		
		public override function longpullCompleteHandler(ev:Event):void
		{
			dispatchEvent(ev as LongpullEvent);
		}
		
		//ActionLinkEvent handler --------------------------------------------------------------
		public override function actionActiveHandler(ev:Event):void
		{
			dispatchEvent(ev as ActionEvent);
		}
		
		public override function actionSuccessHandler(ev:Event):void
		{
			dispatchEvent(ev as ActionEvent);
		}
		
		public override function actionCompleteHandler(ev:Event):void
		{
			dispatchEvent(ev as ActionEvent);
		}
		
		public override function actionFailHandler(ev:Event):void
		{
			dispatchEvent(ev as ActionEvent);
		}
		
		//session Change Handler --------------------------------------------------------------------------------
		public override function sessionChangeHandler(ev:Event):void
		{
			dispatchEvent(ev as SessionEvent);
		}
		//setSession handler
		public override function setSessionHandler(ev:Event):void
		{
			sessionChangeHandler(ev);
		}
		
		public override function sessionFailHandler(ev:Event):void
		{
			sessionChangeHandler(ev);
		}
		
		//initData handler
		public override function initDataHandler(ev:Event):void
		{
			dispatchEvent(ev as InitEvent);
		}
		
		public override function initDataLoadedHandler(ev:Event):void //InitEvent
		{
			initDataHandler(ev);
		}
			
		public override function updateDataHandler(ev:Event):void  //initEvent
		{
			initDataHandler(ev);
		}
		
		public override function modelInitUpdateHandler(ev:Event):void  //ModelEvent
		{
			dispatchEvent(ev as ModelEvent);
		}
		
		public override function modelLinkStateUpdateHandler(ev:Event):void  //ModelEvent
		{
			dispatchEvent(ev as ModelEvent);
		}
		
		//Login handler
		public override function loginHandler(ev:Event):void
		{
			dispatchEvent(ev as LoginEvent);
		}
		
		//exit handler------------------------------------------------------------------------------------------
		public override function exitHandler(ev:Event):void
		{
			dispatchEvent(ev as ExitEvent);
		}
		
		//roomClick event Handler
		public override function roomClickedHandler(ev:Event):void
		{
			dispatchEvent(ev as RoomEvent);
		}
		
		//InitEvent handler
		public override function getSessionHandler(ev:Event):void
		{
			dispatchEvent(ev as InitEvent);
		}
		
		//roomClick event Handler
		public override function setLongpullHandler(ev:Event):void
		{	//Alert.show(MainModel.getLongpullState());
			dispatchEvent(new LongpullEvent(MainModel.getLongpullState(),0,0));
		}
	}
}