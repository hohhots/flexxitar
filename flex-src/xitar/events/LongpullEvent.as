package xitar.events
{
	import flash.events.Event;
	
	public class LongpullEvent extends Event
	{
		// Define static constant.
		public static const SET_LONGPULL:String      = "SET_LONGPULL";
		public static const BUILDING_LONGPULL:String = "BUILDING_LONGPULL";
		public static const ROOM_LONGPULL:String     = "ROOM_LONGPULL";
		public static const DESK_LONGPULL:String     = "DESK_LONGPULL";
		public static const BOARD_LONGPULL:String    = "BOARD_LONGPULL";
		
		public static const ACTIVE:String            = "ACTIVE_LONGPULL";
		public static const SUCCESS:String           = "SUCCESS_LONGPULL";
		public static const COMPLETE:String          = "COMPLETE_LONGPULL";

		// Define private variable.
		private var _access_start_time : Number = 0;
		private var _access_complete_time : Number = 0;
		
		public function LongpullEvent(typ:String,start:Number,stop:Number)
		{
			super(typ);
			_access_start_time    = start;
			_access_complete_time = stop;
		}
		
		public function get access_start_time():Number {
			return _access_start_time;
		}
		
		public function get access_complete_time():Number {
			return _access_complete_time;
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			return new LongpullEvent(type,access_start_time,access_complete_time);
		}
	}
}