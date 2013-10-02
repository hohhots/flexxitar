package xitar.events
{
	import flash.events.Event;
	
	public class ModelEvent extends Event
	{
		// Define static constant.
		public static const INIT_UPDATED:String       = "MODEL_INIT_UPDATED";
		public static const LINK_STATE_UPDATED:String = "MODEL_LINK_STATE_UPDATED";
		// Define private variable.
		
		public function ModelEvent(typ:String)
		{
			super(typ);
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			return new ModelEvent(type);
		}
	}
}