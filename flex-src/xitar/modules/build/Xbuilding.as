package xitar.modules.build
{
	import flash.events.Event;
	import mx.controls.Alert;
	
	import xitar.*;
	import xitar.events.*;

	[Event(name=PreloadModuleEvent.PRELOAD_MODULE, type="xitar.events.PreloadModuleEvent")]
	[Event(name=LongpullEvent.SET_LONGPULL,        type="xitar.events.LongpullEvent")]
	public class Xbuilding extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private var _app : building;
			
		public function Xbuilding(pf:building){ 
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.XBUILDING;
			_app = pf;
			MainControl.registObject(this);
			XdispatchEvent();
		}
		
		//Get function ---------------------------------------------------------------------
		public static function getInstance(pf:building):Xbuilding
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new Xbuilding(pf);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + "Xbuilding class.");
		}
		
		private function XdispatchEvent():void{
			dispatchEvent(new PreloadModuleEvent(MainConstant.BOARD_SWF_FILE));
			dispatchEvent(new LongpullEvent(LongpullEvent.SET_LONGPULL,0,0));
		}
	}
}