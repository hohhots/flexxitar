package xitar.events
{
	import flash.events.Event;
	
	public class InitEvent extends Event
	{
		// Define static constant.
		public static const GET_SESSION:String        = "getSessionStage";
		public static const INIT_DATA:String          = "initData";
		public static const INIT_DATA_LOADED:String   = "initDataLoaded";
		public static const UPDATE_DATA:String        = "UpdateData";
		public static const UPDATE_DATA_LOADED:String = "UpdateDataLoaded";
		private var _data:String;
		// Define private variable.
		
		public function InitEvent(typ:String,dat:String="default")
		{
			super(typ);
			_data = dat;
		}

		public function get data():String{
			return _data;
		}
		
		public function set data(d:String):void{
			_data = d;
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			return new InitEvent(type,data);
		}
	}
}