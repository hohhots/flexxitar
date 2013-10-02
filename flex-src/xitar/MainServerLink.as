package xitar
{
	import flash.events.*;
	import flash.net.URLRequestHeader;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.utils.Timer;
	
	import mx.controls.Alert;
	import mx.rpc.http.HTTPService;
	
	import xitar.MainConstant;
	import xitar.events.*;

	[Event(name=SessionEvent.SET_SESSION,   type="xitar.events.SessionEvent")]
	[Event(name=InitEvent.INIT_DATA_LOADED, type="xitar.events.InitEvent")]
	[Event(name=LongpullEvent.ACTIVE,       type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.SUCCESS,      type="xitar.events.LongpullEvent")]
	[Event(name=LongpullEvent.COMPLETE,     type="xitar.events.LongpullEvent")]
	[Event(name=ActionEvent.ACTIVE,         type="xitar.events.ActionEvent")]
	[Event(name=ActionEvent.SUCCESS,        type="xitar.events.ActionEvent")]
	[Event(name=ActionEvent.COMPLETE,       type="xitar.events.ActionEvent")]
	[Event(name=ActionEvent.FAILE,          type="xitar.events.ActionEvent")]

	public class MainServerLink extends MainParent
	{
		private static var _allowInstantiation:Boolean = false;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		//private properties ----------------------------------------------------------------
		private var _receivedActionEvent:Event; //for recall event handler function
		private var _receivedLongpullEvent:Event; 
		
		private var _accessLongpullTimes:int  = 0; //number of try to connect server times
		private var _accessActionTimes:int  = 0;

		private var _xitarMain:XitarMain;
		
		private var _req_Action: URLRequest;
		private var _loader_Action : URLLoader;
		private var _loader_Action_start_time : Number = 0;
		private var _loader_Action_end_time : Number   = 0;
		
		private var _req_Longpull : URLRequest;
		private var _loader_Longpull : URLLoader;
		private var _loader_Longpull_start_time : Number = 0;
		private var _loader_Longpull_end_time : Number   = 0;
		
		//constructor-------------------------------------------------------------------------
		public function MainServerLink(xm:XitarMain){
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.MAINSERVERLINK;
			_xitarMain = xm;
			MainControl.registObject(this);
			_allowInstantiation = false;
		}
		
		//Get / Set function ---------------------------------------------------------------------
		public static function getInstance(xm:XitarMain):MainServerLink
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new MainServerLink(xm);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + " MainServerLink class.");
		}
		
		public function get loader_Longpull_start_time():Number {
			return _loader_Longpull_start_time;
		}
		public function set loader_Longpull_start_time(val:Number):void {
			_loader_Longpull_start_time = val;
		}
		
		public function get loader_Longpull_end_time():Number {
			return _loader_Longpull_end_time;
		}
		public function set loader_Longpull_end_time(val:Number):void {
			_loader_Longpull_end_time = val;
			dispatchEvent(new LongpullEvent(LongpullEvent.COMPLETE,
				_loader_Longpull_start_time,
				_loader_Longpull_end_time));
		}
		
		public function get loader_Action_start_time():Number {
			return _loader_Action_start_time;
		}
		public function set loader_Action_start_time(val:Number):void {
			_loader_Action_start_time = val;
		}
		
		public function get loader_Action_end_time():Number {
			return _loader_Action_end_time;
		}
		public function set loader_Action_end_time(val:Number):void {
			_loader_Action_end_time = val;
			dispatchEvent(new ActionEvent(ActionEvent.COMPLETE,
				_loader_Action_start_time,
				_loader_Action_end_time));
		}
			
		private function loader_Longpull_load():void{
			// creates a new five-second Timer
			var longpullTimer:Timer = new Timer(MainConstant.COMET_EXPIRATIONDELAY, 1); //every x seconds,repeat 1 time.
			longpullTimer.addEventListener(TimerEvent.TIMER_COMPLETE, onLongpullTimerComplete);
			longpullTimer.start();

			_loader_Longpull.load(_req_Longpull);
			_loader_Longpull_start_time = MainUtil.getCurrentTime();
		}
		private function loader_Action_load():void{
			_loader_Action.load(_req_Action);
			_loader_Action_start_time = MainUtil.getCurrentTime();
		}
		private function onLongpullTimerComplete(event:TimerEvent):void	{
			//Alert.show("time out!");
			var c_time : Number = MainUtil.getCurrentTime();
			//Alert.show((c_time - _loader_Longpull_start_time).toString());
			if((c_time - _loader_Longpull_start_time) > MainConstant.LONGPULL_CONNECTION_EXPIRED){
				Alert.show("Too long connection.Connection failed!");
				dispatchEvent(new ActionEvent(ActionEvent.FAILE,0,0));
				try{
					_loader_Longpull.close();
				}catch(err:Error){}
			}
		}
		
		//Link to server common functions-----------------------------------------------------------
		private function longpullToServer(url:String,
									 meth:String,
									 vars:URLVariables,
									 comple:String):void{

			try{ //reset pre condition
				_accessLongpullTimes = 0;
				_loader_Longpull.close();
			}catch(err:Error){}
			
			_req_Longpull = new URLRequest();
			_loader_Longpull = new URLLoader();
			
			_req_Longpull.url = url  + "/" + MainUtil.generateRandomString();
			_req_Longpull.method = meth;
			_req_Longpull.data = vars;
						
			_loader_Longpull.dataFormat = URLLoaderDataFormat.TEXT;

			_loader_Longpull.addEventListener(IOErrorEvent.IO_ERROR, longpull_ErrorHandler);
			_loader_Longpull.addEventListener(HTTPStatusEvent.HTTP_STATUS, longpull_HttpStatusHandler);
			_loader_Longpull.addEventListener(Event.COMPLETE, this[comple]);
			
			loader_Longpull_load();
		}
		private function longpull_ErrorHandler():Boolean{
			if(_accessLongpullTimes < MainConstant.ACCESS_TIMES){
				_accessLongpullTimes++;
				loader_Longpull_load();
				return true;
			}
			dispatchEvent(new ActionEvent(ActionEvent.FAILE,0,0));
			
			return false;
		}
		private function longpull_HttpStatusHandler(s:HTTPStatusEvent):void{
			//Alert.show(s.status.toString());

			if(s.status != 200){
				longpull_ErrorHandler();
			}else{
				loader_Longpull_end_time = MainUtil.getCurrentTime();
				dispatchEvent(new ActionEvent(ActionEvent.SUCCESS,0,0));
			}
		}
		
		private function actionToServer(url:String,
										  meth:String,
										  _vars:URLVariables,
										  comple:String,
										  xdispatchEvent:Boolean):void{
			
			if(xdispatchEvent){
				dispatchEvent(new ActionEvent(ActionEvent.ACTIVE,0,0));
			}
			
			try{
				_loader_Action.close(); //first,to close connection
			}catch(err:Error){}
			
			_req_Action = new URLRequest();
			_loader_Action = new URLLoader();
			
			_req_Action.url = url  + "/" + MainUtil.generateRandomString();
			_req_Action.method = meth;
			_req_Action.data = _vars;
			
			_loader_Action.dataFormat = URLLoaderDataFormat.TEXT;
			_loader_Action.addEventListener(IOErrorEvent.IO_ERROR, action_ErrorHandler);
			_loader_Action.addEventListener(HTTPStatusEvent.HTTP_STATUS, action_HttpStatusHandler);
			_loader_Action.addEventListener(Event.COMPLETE, this[comple]);
			loader_Action_load();
		}
		private function action_ErrorHandler():Boolean{
			if(_accessActionTimes < MainConstant.ACCESS_TIMES){
				_accessActionTimes++;
				loader_Action_load();
				return true;
			}
			_accessActionTimes = 0;
			dispatchEvent(new ActionEvent(ActionEvent.FAILE,0,0));
			
			return false;
		}
		private function action_HttpStatusHandler(s:HTTPStatusEvent):void{
			//Alert.show(s.status.toString());
			if(s.status != 200){
				action_ErrorHandler();
			}else{
				_accessActionTimes = 0;
				loader_Action_end_time = MainUtil.getCurrentTime();
			}
		}
		
		//dispatch commmon link event
		private function dispatchCommonLinkEvent(data:String):Boolean{
			var ifContinue:Boolean = true;
			
			if(data == MainConstant.REFRESH){ //lost session
				Alert.show(data);
				dispatchEvent(new ActionEvent(ActionEvent.FAILE,0,0));
				
				ifContinue = false;
			}
			
			return ifContinue;
		}
		
		//Login to server functions-----------------------------------------------------------
		public override function loginHandler(e:Event):void{ //LoginEvent
			_receivedActionEvent = e;
			var params : URLVariables = new URLVariables();
			
			var le:LoginEvent = (e as LoginEvent);
			params.passwd = le.pass;
			actionToServer(MainConstant.LOGIN_URL,
						URLRequestMethod.POST,
						params,
						"login_CompleteHandler",
						false);

		}
		private function login_CompleteHandler(evt:Event):void{
			session_CommonHandler();
		}
		
		//Exit from server functions-----------------------------------------------------------
		public override function exitHandler(e:Event):void{ //ExitEvent
			_receivedActionEvent = e;
			actionToServer(MainConstant.EXIT_URL,
				URLRequestMethod.GET,
				new URLVariables(),
				"exit_CompleteHandler",
				false);
		}
		private function exit_CompleteHandler(evt:Event):void{
			session_CommonHandler();
		}
		
		//handler -----------------------------------------------------------------------------
		public override function getSessionHandler(ev:Event):void //InitEvent
		{
			_receivedActionEvent = ev;
			if(MainConstant.XITAR_DEBUG){
				Alert.show("getSessionHandler " + ev.target+ this.toString());
			}
			
			//var ie:InitEvent = (ev as InitEvent);
			getSession();
		}
		
		//link to server function-------------------------------------------------------------
		private function getSession():void{
			actionToServer(MainConstant.SESSION_URL,
				URLRequestMethod.GET,
				new URLVariables(),
				"session_CompleteHandler",
				false);
		}
		private function session_CompleteHandler(evt:Event):void{
			session_CommonHandler();
		}
		private function session_CommonHandler():void{
			var eventObj:SessionEvent = new SessionEvent(SessionEvent.SET_SESSION);
			var data : int;
			if(MainConstant.REFRESH == _loader_Action.data){
				data = MainConstant.INIT_STAGE;
			}else{
				data = _loader_Action.data;
			}
			eventObj.session_num = data;
			dispatchEvent(eventObj);
			dispatchCommonLinkEvent(_loader_Action.data);
		}
		
		//Init data function ----------------------------------------------------------------------
		public override function initDataHandler(ev:Event):void  //InitEvent
		{
			if(MainConstant.XITAR_DEBUG){
				Alert.show("initDataHandler " + ev.target+ this.toString());
			}
			
			_receivedActionEvent = ev;
			
			actionToServer(MainConstant.DATA_URL,
				URLRequestMethod.GET,
				new URLVariables(),
				"data_CompleteHandler",
				false);
		}		
		
		private function data_CompleteHandler(evt:Event):void{
			dispatchCommonLinkEvent(_loader_Action.data);
			dispatchEvent(new InitEvent(InitEvent.INIT_DATA_LOADED,_loader_Action.data));
		}
		
		//room click functions-------------------------------------------------------
		public override function roomClickedHandler(ev:Event):void  //clickEvent
		{
			if(MainConstant.XITAR_DEBUG){
				Alert.show("roomClickedHandler " + ev.target+ this.toString());
			}
			
			_receivedActionEvent = ev;
			
			var re:RoomEvent = ev as RoomEvent;
			var url:String = MainConstant.ACTION_ROOM_URL + "/" + re.room_id;
			actionToServer(url,
				URLRequestMethod.GET,
				new URLVariables(),
				"roomClickedComplete",
				true);
		}
		
		private function roomClickedComplete(evt:Event):void{
			//Alert.show(_loader_Action.data + " - roomClickedComplete");
			dispatchCommonLinkEvent(_loader_Action.data);
		}
		
		//long pull functions-------------------------------------------------------
		public override function longpullHandler(ev:Event):void  //LongpullEvent
		{
			_receivedLongpullEvent = ev;
			
			resetCurrentLongpullHandler();
		}
		
		private function resetCurrentLongpullHandler():void
		{
			var pe:LongpullEvent = _receivedLongpullEvent as LongpullEvent
			switch(pe.type){
				case LongpullEvent.BUILDING_LONGPULL:
					setBuildingLongpull();
					break;
				case LongpullEvent.ROOM_LONGPULL:
					setRoomLongpull();
					break;
				case LongpullEvent.DESK_LONGPULL:
					setDeskLongpull();
					break;
				case LongpullEvent.BOARD_LONGPULL:
					setBoardLongpull();
					break;
				default:
					Alert.show("MainServerLink - LongPullHandler error");
					break;
			}
		}
		
		private function longpull_CompleteHandler(evt:Event):void{
			Alert.show("longpullComplete 1 - " + _loader_Longpull.data);
			if(_loader_Longpull.data == "i"){
				resetCurrentLongpullHandler();
			}else{
				if(dispatchCommonLinkEvent(_loader_Longpull.data)){
					dispatchEvent(new InitEvent(InitEvent.UPDATE_DATA,_loader_Longpull.data));
				}
			}
		}
		
		private function setBuildingLongpull():void
		{
			longpullToServer(MainConstant.LONGPULL_BUILDING_URL,
				URLRequestMethod.GET,
				new URLVariables(),
				"longpull_CompleteHandler");
		}
		
		private function setRoomLongpull():void
		{
			//var url:String = MainConstant.LONGPULL_ROOM_URL + "/" + MainModel.getMyRoomId();
			longpullToServer(MainConstant.LONGPULL_ROOM_URL,
				URLRequestMethod.GET,
				new URLVariables(),
				"longpull_CompleteHandler");
		}
		
		private function setDeskLongpull():void
		{}
		
		private function setBoardLongpull():void
		{}
		
	}
}