package xitar
{
	public class MainLang extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const _LIMIT_NUM:int = 0;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		public static const SessionIsEmpty:String      = "Session is empty!";
		public static const INSTANCE_FAIL:String       = "Error: Instantiation failed: Use getInstance() instead of new.";
		public static const INSTANCE_LIMIT_FAIL:String = "Error: Instantiation failed: Up to creation limit num of.";
		public static const LOAD_MODULE_ERROR:String   = "Error in load module from server!";
		public static const REGIST_DUPLICATE_OBJECT_ERROR:String = "Can't regist object twice into control!";
		public static const NOT_EXIST_THIS_FUNCTION:String = "In this class,dosn't exist this function! - ";
		
		public function MainLang()
		{
			if (!_allowInstantiation) {
				throw new Error(INSTANCE_FAIL + " MainLang class.");
			}
		}
	}
}