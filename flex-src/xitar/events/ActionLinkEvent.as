package xitar.events
{
	import flash.events.Event;
	
	public class ActionLinkEvent extends Event
	{
		// Define static constant.
		public static const ACTIVE:String = "ACTIVE";
		public static const COMPLETE:String = "COMPLETE";
		public static const FAILE:String = "FAILE";
		// Define private variable.
		
		public function ActionLinkEvent(typ:String)
		{
			super(typ);
		}

		// Override the inherited clone() method.
		override public function clone():Event {
			return new ActionLinkEvent(type);
		}
	}
}