package xitar.modules.login
{
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	
	import mx.controls.Alert;
	
	import xitar.*;
	import xitar.events.LoginEvent;
	import xitar.events.PreloadModuleEvent;
	  
	[Event(name=LoginEvent.LOGIN,  type="xitar.events.LoginEvent")]
	[Event(name=PreloadModuleEvent.PRELOAD_MODULE, type="xitar.events.PreloadModuleEvent")]
	public class Xlogin extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private var app : login;
		
		public function Xlogin(pf:login){
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.XLOGIN;
			app = pf;
			MainControl.registObject(this);

			dispatchEvent(new PreloadModuleEvent(MainConstant.BUILDING_SWF_FILE));
		}
	
		//Get function ---------------------------------------------------------------------
		public static function getInstance(pf:login):Xlogin
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new Xlogin(pf);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + " Xlogin class.");
		}		
		
		public function keyUp(e:KeyboardEvent,val:String):void{
			dispatchEvent(new LoginEvent(LoginEvent.LOGIN,val));
		}
			
		//Handler-----------------------------------------------------------
		public override function sessionFailHandler(ev:Event):void //SessionEvent
		{
			app.sessionFail();
		}
	}
}