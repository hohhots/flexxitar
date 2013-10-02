package xitar.events
{
	import flash.events.Event;
	
	import xitar.MainConstant;
	
	public class SessionEvent extends Event
	{
		// Define static constant.
		public static const SET_SESSION:String     = "SETSESSION";
		public static const SESSION_CHANGED:String = "SESSIONCHANGED";
		public static const SESSION_FAILED:String  = "SESSIONFAILED";

		// Define private variable.
		private var _session_num:int;
		
		public function SessionEvent(typ:String,sn:int = MainConstant.LOGIN_STAGE)
		{
			super(typ);
			_session_num = sn;
		}
		
		//get function
		public function get session_num():int{
			return _session_num;
		}
		
		//set function
		public function set session_num(sn:int):void{
			_session_num = sn;
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			return new SessionEvent(type,_session_num);
		}
	}
}