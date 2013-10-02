package xitar.controls.bars
{
	import flash.events.Event;
	
	import mx.controls.Alert;
	
	import xitar.*;
	import xitar.events.ExitEvent;
	
	[Event(name=ExitEvent.EXIT, type="xitar.events.ExitEvent")]
	public class XtitleBar extends MainParent
	{
		private static var _allowInstantiation:Boolean = false;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private var app : titleBar;
			
		public function XtitleBar(pf:titleBar){ 
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name = MainConstant.XTITLEBAR;
			app = pf;
			setCurrentState();
			MainControl.registObject(this);
			_allowInstantiation = false;
		}
		
		//Get function ---------------------------------------------------------------------
		public static function getInstance(pf:titleBar):XtitleBar
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new XtitleBar(pf);
			}
			
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + " XtitleBar class.");
		}
		
		//handler
		public override function exitHandler(e:Event):void
		{
			dispatchEvent(new ExitEvent(ExitEvent.EXIT));
		}
		
		public override function modelLinkStateUpdateHandler(ev:Event):void
		{
			setCurrentState();
		}
		
		private function setCurrentState():void{
			var delay:Number = (MainModel.linkEndTime - MainModel.linkStartTime);
			if(delay < MainConstant.GOOD_LINK_DELAY){
				app.currentState = "linkGood";
			}else{
				app.currentState = "linkBad";
			}
		}
		
		public override function actionActiveHandler(ev:Event):void
		{
			app.currentState = "linkActive";
		}
				
		public override function actionFailHandler(ev:Event):void  //ActionEvent
		{
			app.currentState = "linkDown";
		}
		
	}
}