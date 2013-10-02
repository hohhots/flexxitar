package xitar.events
{
	import flash.events.Event;
	
	public class PreloadModuleEvent extends Event
	{
		// Define static constant.
		public static const PRELOAD_MODULE:String = "PRELOADMODULE";
		// Define private variable.
		private var _preload_url:String;
		
		public function PreloadModuleEvent(url:String)
		{
			super(PRELOAD_MODULE);
			_preload_url = url;
		}
		
		//get function
		public function get preload_url():String{
			return _preload_url;
		}
		
		// Override the inherited clone() method.
		override public function clone():Event {
			return new PreloadModuleEvent(_preload_url);
		}
	}
}