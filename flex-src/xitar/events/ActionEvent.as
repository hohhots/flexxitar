package xitar.events
{
	import flash.events.Event;
	
	public class ActionEvent extends Event
	{
		// Define static constant.
		public static const ACTIVE:String   = "ACTIVE_ACTION";
		public static const SUCCESS:String  = "SUCCESS_ACTION";
		public static const COMPLETE:String = "COMPLETE_ACTION";
		public static const FAILE:String    = "FAILE_ACTION";
		// Define private variable.
		private var _access_start_time : Number = 0;
		private var _access_complete_time : Number = 0;
		
		public function ActionEvent(typ:String,start:Number,stop:Number)
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
			return new ActionEvent(type,access_start_time,access_complete_time);
		}
	}
}