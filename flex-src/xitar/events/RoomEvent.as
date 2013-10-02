package xitar.events
{
	import flash.events.Event;
	
	public class RoomEvent extends Event
	{
		// Define static constant.
		public static const CLICKED:String = "ROOM_CLICKED";
		// Define private variable.
		private var _room_id:String;
		
		public function RoomEvent(typ:String,rid:String)
		{
			super(typ);
			_room_id = rid;
		}
		
		//get function
		public function get room_id():String{
			return _room_id;
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			return new RoomEvent(type,_room_id);
		}
	}
}