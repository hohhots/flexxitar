package xitar
{
	import xitar.events.*;
	
	public class MainConstant
	{
		public function MainConstant(){}
		
		//must edit before deploy in production
		public static const GOOD_LINK_DELAY : Number    = 50;//millisecond,it will be good if result returns in this time,otherwise bad.
				
		public static const PROJECT_NAME : String     = "/flexxitar";
		
		//change with server side value
		public static const COMET_EXPIRATIONDELAY : int = 65000; //larger than server side comet expired time.
		public static const LONGPULL_CONNECTION_EXPIRED : int = 60000;//same with server side comet expired time.
		
		public static const XITAR_DEBUG : Boolean    = false;
		public static const XITAR_VERSION : String   = "1";
		public static const ACCESS_TIMES : int       = 5; //try to access server counter limit
		public static const SWF_FILE_DIR : String    = "do/swf";
		
		public static const ERROR_SWF_FILE : String     = SWF_FILE_DIR + "/error.swf";
		public static const LOGIN_SWF_FILE : String     = SWF_FILE_DIR + "/login.swf";
		public static const BUILDING_SWF_FILE : String  = SWF_FILE_DIR + "/building.swf";
		public static const BOARD_SWF_FILE : String     = SWF_FILE_DIR + "/board.swf";

		public static const CONTEXT_URL : String     = PROJECT_NAME + "/do";
		public static const SESSION_URL : String     = CONTEXT_URL + "/session";
		public static const DATA_URL : String        = CONTEXT_URL + "/data";
		public static const LOGIN_URL : String       = CONTEXT_URL + "/login";
		public static const EXIT_URL : String        = CONTEXT_URL + "/exit";
		
		public static const BUILDING : String        = "/building";
		public static const ROOM : String            = "/room";
		public static const DESK : String            = "/desk";
		
		public static const LONGPULL_URL : String          = CONTEXT_URL +  "/longpull";
		public static const LONGPULL_BUILDING_URL : String = LONGPULL_URL + BUILDING;
		public static const LONGPULL_ROOM_URL : String     = LONGPULL_URL + ROOM;
		
		public static const ACTION_URL : String            = CONTEXT_URL + "/action";
		public static const ACTION_ROOM_URL : String       = ACTION_URL +  ROOM;
		public static const ACTION_DESK_URL : String       = ACTION_URL +  DESK;
		
		//Session stage constant
		public static const REFRESH : String      = "refresh";
		public static const INIT_STAGE : int      = -1; //reference server config
		public static const ERROR_STAGE : int     = 0;
		public static const LOGIN_STAGE : int     = 1;
		public static const BUILDING_STAGE : int  = 2;
		public static const ROOM_STAGE : int      = 3;
		public static const DESK_STAGE : int      = 4;
		public static const BOARD_STAGE : int     = 5;
		
		//class name constant
		public static const XITARMAIN : int      = 1;
		public static const MAINMODEL : int      = 2;
		public static const MAINCONTROL : int    = 3;
		public static const MAINSERVERLINK : int = 4;
		public static const MAINOBJECTSPOLL : int= 5;
		public static const XLOGIN : int         = 6;
		public static const XBUILDING : int      = 7;
		public static const XBOARD : int         = 8;
		public static const XTITLEBAR : int      = 9;
		public static const XROOM : int          = 10;
		public static const XROOMS : int         = 11;
		
		public static const XML_ROOT_ELEMENT   : String = "root";
		public static const XML_PLAYER_ELEMENT   : String = "player";
		public static const XML_BUILDING_ELEMENT : String = "building";
		public static const XML_ROOM_ELEMENT     : String = "room";
		public static const XML_DESK_ELEMENT     : String = "desk";
		public static const XML_BOARD_ELEMENT    : String = "board";
		
		public static const ALL_EVENTS_TYPE : String      = "XITARGLOBALEVENT";
		
		public static const CONNECTION_FAILED : String    = "Connection to server failed!";
		//Event name constant
		//if modify this,don't forget modify MainControl dispatch & listener
		public static const EVENTS : Array  = 
			[
				[PreloadModuleEvent.PRELOAD_MODULE, "preloadModuleHandler"], //eventType,handler
				[SessionEvent.SESSION_CHANGED,      "sessionChangeHandler"],
				[SessionEvent.SET_SESSION,          "setSessionHandler"],
				[SessionEvent.SESSION_FAILED,       "sessionFailHandler"],
				[InitEvent.GET_SESSION,             "getSessionHandler"],
				[InitEvent.INIT_DATA,               "initDataHandler"],
				[InitEvent.INIT_DATA_LOADED,        "initDataLoadedHandler"],
				[InitEvent.UPDATE_DATA,             "updateDataHandler"],
				[LoginEvent.LOGIN,                  "loginHandler"],
				[ExitEvent.EXIT,                    "exitHandler"],
				[RoomEvent.CLICKED,                 "roomClickedHandler"],
				[ModelEvent.INIT_UPDATED,           "modelInitUpdateHandler"],
				[ModelEvent.LINK_STATE_UPDATED,     "modelLinkStateUpdateHandler"],
				[LongpullEvent.SET_LONGPULL,        "setLongpullHandler"],
				[LongpullEvent.BUILDING_LONGPULL,   "longpullHandler"],
				[LongpullEvent.ROOM_LONGPULL,       "longpullHandler"],
				[LongpullEvent.DESK_LONGPULL,       "longpullHandler"],
				[LongpullEvent.BOARD_LONGPULL,      "longpullHandler"],
				[LongpullEvent.ACTIVE,              "longpullActiveHandler"],
				[LongpullEvent.SUCCESS,             "longpullSuccessHandler"],
				[LongpullEvent.COMPLETE,            "longpullCompleteHandler"],
				[ActionEvent.ACTIVE,                "actionActiveHandler"],
				[ActionEvent.SUCCESS,               "actionSuccessHandler"],
				[ActionEvent.COMPLETE,              "actionCompleteHandler"],
				[ActionEvent.FAILE,                 "actionFailHandler"]
			];
		
		//Event name constant
		//if modify this,don't forget modify MainControl dispatch & listener
		public static const EVENTS_LISTENERS : Array  = //MAINCONTROL will listen all events
			[
				[SessionEvent.SESSION_CHANGED,   [XITARMAIN]], //event,listeners class Array
				[SessionEvent.SET_SESSION,       [MAINMODEL]],
				[SessionEvent.SESSION_FAILED,    [XLOGIN]],
				[InitEvent.GET_SESSION,          [MAINSERVERLINK]],
				[InitEvent.INIT_DATA,            [MAINSERVERLINK]],
				[InitEvent.INIT_DATA_LOADED,     [MAINMODEL]],
				[InitEvent.UPDATE_DATA,          [MAINMODEL]],
				[LoginEvent.LOGIN,               [MAINSERVERLINK]],
				[ExitEvent.EXIT,                 [MAINSERVERLINK]],
				[RoomEvent.CLICKED,              [XROOM,MAINSERVERLINK]],
				[ModelEvent.INIT_UPDATED,        [XROOMS]],
				[ModelEvent.LINK_STATE_UPDATED,  [XTITLEBAR]],
				[LongpullEvent.BUILDING_LONGPULL,[MAINSERVERLINK]],
				[LongpullEvent.ROOM_LONGPULL,    [MAINSERVERLINK]],
				[LongpullEvent.DESK_LONGPULL,    [MAINSERVERLINK]],
				[LongpullEvent.BOARD_LONGPULL,   [MAINSERVERLINK]],
				[LongpullEvent.ACTIVE,           [XITARMAIN]],
				[LongpullEvent.SUCCESS,          [XITARMAIN]],
				[LongpullEvent.COMPLETE,         [MAINMODEL]],
				[ActionEvent.ACTIVE,             [XITARMAIN,XTITLEBAR]],
				[ActionEvent.SUCCESS,            [XITARMAIN]],
				[ActionEvent.COMPLETE,           [MAINMODEL]],
				[ActionEvent.FAILE,              [XITARMAIN,XTITLEBAR]]
			];
		
		public static function toListenerEvents(cn:int):Array{
			var tevs:Array = new Array();
			
			for each (var lss:Array in EVENTS_LISTENERS){
				for each (var ls:int in lss[1]){
					if(ls == cn){
						tevs.push(getToListenEvents(lss[0]));
					}
				}
			}
			
			return tevs;
		}
		
		public static function getToListenEvents(evType:String):Array{
			var tev:Array = new Array();
			for each (var ev:Array in EVENTS){
				if(ev[0] == evType){
					tev = ev;
				}
			}
			
			return tev;
		}
		
	}
}