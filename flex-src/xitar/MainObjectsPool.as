package xitar
{
	import mx.collections.*;
	import mx.controls.Alert;
	
	public class MainObjectsPool  extends MainParent
	{
		private static var _allowInstantiation:Boolean = false;
		private static const LIMIT_NUM:int = 1;    //Create object limit
		private static var created_object:int = 0; //Created Object
		
		private var _mainControl:MainControl;
		private var pool:ArrayCollection = new ArrayCollection();
		
		public function MainObjectsPool(control:MainControl)
		{
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.MAINOBJECTSPOLL;
			_mainControl = control;
			
			registObject(this);
			_allowInstantiation = false;
		}
		
		//Get function ---------------------------------------------------------------------
		public static function getInstance(control:MainControl):MainObjectsPool
		{
			_allowInstantiation = true;
			if(canGetInstance(LIMIT_NUM,created_object)){
				created_object++;
				return new MainObjectsPool(control);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + " MainObjectsPool class.");
		}
		
		//regist object into objects array
		public function registObject(ob:MainParent):void
		{
			if(!ExistInPool(ob)){
				setObjectEventListener(ob);
				pool.addItem(ob);
			}else{
				throw new Error(MainLang.REGIST_DUPLICATE_OBJECT_ERROR);
			}
		}
		private function setObjectEventListener(ob:MainParent):void
		{
			if(ob != _mainControl)
			{
				_mainControl.registMainActionListener(ob);
			}
		}
		
		private function ExistInPool(ob:MainParent):Boolean
		{
			var find:Boolean = false;
			for each (var item:MainParent in pool)
			{
				if (item == ob)
				{
					find = true;
				}
			}
			return find;
		}
		
		//regist action listener---------------------------------------------------------------
		override public  function registActionListener(ob:MainControl):void{}
		
	}
}