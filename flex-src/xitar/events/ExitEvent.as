package xitar.events
{
	import flash.events.Event;
	
	public class ExitEvent extends Event
	{
		// Define static constant.
		public static const EXIT:String = "EXIT";
		// Define private variable.
		
		public function ExitEvent(typ:String)
		{
			super(typ);
		}

		// Override the inherited clone() method.
		override public function clone():Event {
			return new ExitEvent(type);
		}
	}
}