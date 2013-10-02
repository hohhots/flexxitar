package xitar
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.controls.Alert;
	
	import xitar.events.*;
	
	[Event(name=InitEvent.GET_SESSION, type="xitar.events.InitEvent")]
	[Event(name=InitEvent.INIT_DATA, type="xitar.events.InitEvent")]
	public class XitarMain extends MainParent
	{
		private static var _allowInstantiation:Boolean;
		private static const _LIMIT_NUM:int = 1;    //Create object limit
		private static var _created_object:int = 0; //Created Object
		
		private var mainApp:flexxitar;
		private var _mainModel:MainModel;
		private var _mainServerLink:MainServerLink;
		private var _mainControl:MainControl;
		
		public function XitarMain(fx:flexxitar)
		{
			if (!_allowInstantiation) {
				throw new Error(MainLang.INSTANCE_FAIL);
			}
			_class_name     = MainConstant.XITARMAIN;
			mainApp         = fx;
			
			//MainControl must instantiated first, others will regist into this.
			try {
				_mainControl     = MainControl.getInstance(this);
				_mainModel       = MainModel.getInstance(this);
				_mainServerLink  = MainServerLink.getInstance(this);
			} catch (error:Error) {
				Alert.show(error.message + this.toString());
			} finally {}
			
			MainControl.registObject(this);
			
			dispatchEvent(new InitEvent(InitEvent.GET_SESSION)); //start initialize state data.
		}
		
		//Get function ---------------------------------------------------------------------
		public static function getInstance(fx:flexxitar):XitarMain
		{
			_allowInstantiation = true;
			if(canGetInstance(_LIMIT_NUM,_created_object)){
				_created_object++;
				return new XitarMain(fx);
			}
			throw new Error(MainLang.INSTANCE_LIMIT_FAIL + " XitarMain class.");
		}
		public function get mainControl():MainControl{
			return _mainControl;
		}
		
		public function get mainModel():MainModel{
			return _mainModel;
		}
		
		public function get mainServerLink():MainServerLink{
			return _mainServerLink;
		}
		
		public function getSwfFileName():String{
			return mainControl.getSwfFileName();
		}
		
		//Handler------------------------------------------------------------------------------
		public override function sessionChangeHandler(e:Event):void //SessionChangeEvent
		{
			if(MainConstant.XITAR_DEBUG){
				Alert.show("sessionChangeHandler " + e.target+ this.toString());
			}

			if(MainModel.sessionStage == MainConstant.INIT_STAGE){
				mainApp.refreshWebPage();
			}else{
				mainApp.setSwfUrl();
			}
		}
		
		public override function actionActiveHandler(ev:Event):void
		{
			mainApp.displayMask(true);
		}
		
		public override function actionSuccessHandler(ev:Event):void
		{
			mainApp.displayMask(false);
		}
		
		public override function actionFailHandler(ev:Event):void
		{
			actionActiveHandler(ev);
		}
	}
}