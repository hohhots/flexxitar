package xitar
{
	import flash.events.EventDispatcher;
	import mx.controls.Alert;
	
	import xitar.listener.ParentListener;
	import xitar.events.SessionEvent;
	
	public class MainParent extends ParentListener
	{
		protected var _class_name:int;
		
		public function MainParent()
		{
			super(this);
		}
		
		protected static function canGetInstance(limit:int,create:int):Boolean
		{
			if(create < limit){
				return true;
			}
			return false;
		}
		
		public function get class_name():int
		{
			return _class_name;
		}
		
		public function registActionListener(ob:MainControl):void
		{
			var evs:Array = MainConstant.toListenerEvents(_class_name);
			for each (var ev:Array in evs){
				ob.addEventListener(ev[0],this[ev[1]])
			}
		}
	}
}