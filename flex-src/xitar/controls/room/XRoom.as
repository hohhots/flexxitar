package xitar.controls.room
{
	import flash.events.Event;
	
	import mx.controls.Alert;
	
	import xitar.*;
	import xitar.events.RoomEvent;
	
	[Event(name=RoomEvent.CLICKED, type="xitar.events.RoomEvent")]
	public class XRoom extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const _LIMIT_NUM:int = MainModel.getRoomsNum();    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private static var _clickedState:Boolean = false; //if any room is clicked
		
		private var app : Room;
		
		public function XRoom(pf:Room){ 
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.XROOM;
			app = pf;
			MainControl.registObject(this);
			_allowInstantiation = false;
		}
		
		//Get function ---------------------------------------------------------------------
		public static function getInstance(room:Room):XRoom
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new XRoom(room);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + "XRoom class.");
		}
		
		public static function get clickedState():Boolean {
			return _clickedState;
		}
		public static function set clickedState(val:Boolean):void {
			_clickedState = val;
		}
		
		public function roomClicked():void
		{
			clickedState = true;
			var eventObj:RoomEvent = new RoomEvent(RoomEvent.CLICKED,app.xid);
			this.dispatchEvent(eventObj);
		}
		
		public override function roomClickedHandler(ev:Event):void  //clickEvent
		{
			var re:RoomEvent = ev as RoomEvent;

			if(app.id == re.room_id){
				app.xselected=true;
			}else{
				app.xselected=false;
			}
		}
	}
}