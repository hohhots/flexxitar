package xitar.modules.board
{
	import mx.controls.Alert;
	
	import xitar.*;
	import xitar.events.PreloadModuleEvent;
	
	public class Xboard extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private var app : board;
		
		public function Xboard(b:board)
		{
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.XBOARD;
			app = b;
			MainControl.registObject(this);
		}
		
		//Get function ---------------------------------------------------------------------
		public static function getInstance(pf:board):Xboard
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new Xboard(pf);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + "Xboard class.");
		}
	}
}