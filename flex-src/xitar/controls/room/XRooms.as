package xitar.controls.room
{
	import flash.events.Event;
	
	import mx.controls.Alert;
	
	import xitar.*;
	import xitar.events.ModelEvent;
	
	[Event(name=ModelEvent.UPDATED, type="xitar.events.ModelEvent")]
	public class XRooms extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private var _app : Rooms;
		
		public function XRooms(pf:Rooms){ 
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.XROOMS;
			_app = pf;
			MainControl.registObject(this);

		}
		
		//Get function ---------------------------------------------------------------------
		public static function getInstance(rooms:Rooms):XRooms
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new XRooms(rooms);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + "XRooms class.");
		}
		
	
		public override function modelInitUpdateHandler(ev:Event):void  //ModelEvent
		{
			_app.rooms = MainModel.getRoomsState();
		}
	}
}