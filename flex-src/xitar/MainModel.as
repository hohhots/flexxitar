package xitar
{
	import flash.events.Event;
	import flash.events.ProgressEvent;
	import flash.xml.XMLDocument;
	
	import mx.collections.*;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.rpc.xml.SimpleXMLDecoder;
	import mx.utils.ObjectUtil;
	
	import xitar.events.*;

	[Event(name=SessionEvent.SESSION_CHANGED, type="xitar.events.SessionEvent")]
	[Event(name=SessionEvent.SESSION_FAILED,  type="xitar.events.sessionEvent")]
	[Event(name=InitEvent.INIT_DATA,          type="xitar.events.InitEvent")]
	[Event(name=ModelEvent.INIT_UPDATED,      type="xitar.events.ModelEvent")]
	[Event(name=ModelEvent.LINK_STATE_UPDATED,type="xitar.events.ModelEvent")]
	[Event(name=LongpullEvent.SET_LONGPULL,   type="xitar.events.LongpullEvent")]
	public class MainModel extends MainParent
	{		
		private static var _allowInstantiation:Boolean = false;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private static var _sessionStage:int = -1;
		private static var _initData:String = "";
		
		private static var _linkStartTime:Number = 0;
		private static var _linkEndTime:Number   = 0;
		
		//private properties ==========================================================
		
		private var _xitarMain:XitarMain;
		private var _error:String;
		
		public function MainModel(xm:XitarMain){
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.MAINMODEL;
			_xitarMain   = xm;
			MainControl.registObject(this);
			_allowInstantiation = true;
		}
		
		//Get function ================================================================
		public static function getInstance(xm:XitarMain):MainModel
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new MainModel(xm);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + " MainModel class.");
		}
		
		public static function get sessionStage():int{
			return _sessionStage;
		}
		
		public static function get initData():String{
			return _initData;
		}
		
		public static function get linkStartTime():Number{
			return _linkStartTime;
		}
		
		public static function get linkEndTime():Number{
			return _linkEndTime;
		}
		
		//Room ------------------------------------------------------------
		public static function getRoomsState():ArrayCollection {
			var rooms:ArrayCollection = new ArrayCollection();
			var xml:XML = new XML(new XMLDocument(_initData));
			var player:XMLList = xml.child(MainConstant.XML_PLAYER_ELEMENT);
			var building:XMLList = xml.child(MainConstant.XML_BUILDING_ELEMENT);
			var selected:Boolean = false;
			for each(var room:XML in building.room){
				for each(var proom:XML in player.room){
					if(room.@id == proom.@id){
						selected = true;
					}
				}
				rooms.addItem({id:room.@id,desks:(room.desks*2),players:room.players,selected:selected});
				selected = false;
			}
			return rooms;
		}
		
		public static function getRoomsNum():int {
			var roomsnum:int = 0;
			var xml:XML = new XML(new XMLDocument(_initData));
			var building:XMLList = xml.child(MainConstant.XML_BUILDING_ELEMENT);

			return building.children().length();
		}
		
		//private static function getMyRoom() : XML{
		//	var room:XML;
		//	var xml:XML = new XML(new XMLDocument(_initData));
		//	var player:XMLList = xml.child(MainConstant.XML_PLAYER_ELEMENT);
		//	for each(var proom:XML in player.room){
		//		room = proom;
		//	}
		//	return room;
		//}
		
		//public static function getMyRoomId() : int {
		//	var room:XML = getMyRoom();
		//	return room.@id;
		//}
		
		
		
		//other-----------------------------------------------------------
		public static function getLongpullState():String {
			var xml:XML = new XML(new XMLDocument(_initData));
			var player:XMLList = xml.child(MainConstant.XML_PLAYER_ELEMENT);
			
			var lp:String = LongpullEvent.BUILDING_LONGPULL;
			
			if(player.child(MainConstant.XML_ROOM_ELEMENT).length() == 1){
				if(player.child(MainConstant.XML_DESK_ELEMENT).length() == 1){
					if(player.child(MainConstant.XML_BOARD_ELEMENT).length() == 1){
						lp = LongpullEvent.BOARD_LONGPULL;
					}
					else{
						lp = LongpullEvent.DESK_LONGPULL;
					}
				}else{
					lp = LongpullEvent.ROOM_LONGPULL;
				}
			}
			
			return lp;
		}
				
		//Handler=========================================================================
		//Session-------------------------
		public override function setSessionHandler(ev:Event):void //SessionChangeEvent
		{
			if(MainConstant.XITAR_DEBUG){
				Alert.show("setSessionHandler" + ev.target + this.toString());
			}

			var se:SessionEvent = ev as SessionEvent;//SessionChangeEvent(ev);

			if((se.session_num == MainConstant.INIT_STAGE) || (_sessionStage != se.session_num)){
				_sessionStage = se.session_num;
				if(_sessionStage > MainConstant.LOGIN_STAGE){
					dispatchEvent(new InitEvent(InitEvent.INIT_DATA)); //start initialize data.
				}else{
					dispatchEvent(new SessionEvent(SessionEvent.SESSION_CHANGED));
				}
			}else{
				dispatchEvent(new SessionEvent(SessionEvent.SESSION_FAILED));
			}
		}
		
		public override function initDataLoadedHandler(ev:Event):void //InitEvent
		{
			var ie:InitEvent = ev as InitEvent;
			_initData = ie.data;
			//Alert.show(_initData + " - init");

			dispatchEvent(new SessionEvent(SessionEvent.SESSION_CHANGED));
		}
		
		public override function updateDataHandler(ev:Event):void{ //InitEvent
			var ie:InitEvent = ev as InitEvent;
			
			changePlayersValue(ie.data);
			changeBuildingValue(ie.data);
			
			dispatchEvent(new ModelEvent(ModelEvent.INIT_UPDATED));
			dispatchEvent(new LongpullEvent(LongpullEvent.SET_LONGPULL,0,0));
		}
		
		public override function longpullCompleteHandler(ev:Event):void //LongpullEvent
		{
			if(sessionStage > MainConstant.LOGIN_STAGE){
				dispatchEvent(new ModelEvent(ModelEvent.LINK_STATE_UPDATED));
			}
		}
		
		public override function actionCompleteHandler(ev:Event):void //ActionEvent
		{
			var ae:ActionEvent = ev as ActionEvent;
			_linkStartTime = ae.access_start_time;
			_linkEndTime   = ae.access_complete_time;
		}
		
		private function changePlayersValue(data:String):void{
			var ixml:XML = new XML(new XMLDocument(_initData));
			var xmlch:XML = new XML(new XMLDocument(data));
			
			var tp:XMLList = ixml.child(MainConstant.XML_PLAYER_ELEMENT);
			var tpch:XMLList = xmlch.child(MainConstant.XML_PLAYER_ELEMENT);
			//Alert.show(xmlch + tp.length() + " - " + tpch.length());
			for each(var playerch:XML in tpch){
				for each(var player:XML in tp){
					if((playerch.room.toXMLString() != "")){
						if(player.room.toXMLString() == ""){
							var room:XML = <room/>;
							player = player.appendChild(room);
						}
						player.room.@id = playerch.room.@id;
					}
				}
			}
			//Alert.show(ixml + tp.length() + " - " + tpch.length());
			_initData = ixml;
		}
		
		private function changeBuildingValue(data:String):void{
			var ixml:XML = new XML(new XMLDocument(_initData));
			var xmlch:XML = new XML(new XMLDocument(data));
			
			var building:XMLList = ixml.child(MainConstant.XML_BUILDING_ELEMENT);
			var buildingch:XMLList = xmlch.child(MainConstant.XML_BUILDING_ELEMENT);
			//Alert.show(ixml +" - "+buildingch.length()+" - "+ building.length());
			for each(var roomch:XML in buildingch.room){
				for each(var room:XML in building.room){
					if(roomch.@id == room.@id){
						room.players = roomch.players;
					}
				}
			}
			
			_initData = ixml;
		}
	}
}