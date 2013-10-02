package xitar.events
{
	import flash.events.Event;
	
	public class LoginEvent extends Event
	{
		// Define static constant.
		public static const LOGIN:String = "LOGIN";

		// Define private variable.
		private var _pass:String;
		
		public function LoginEvent(typ:String,pw:String)
		{
			super(typ);
			_pass = pw;
		}
		
		//get function
		public function get pass():String{
			return _pass;
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			return new LoginEvent(type,_pass);
		}
	}
}