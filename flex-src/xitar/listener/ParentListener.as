package xitar.listener
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import xitar.events.*;
		
	//Parent abstract adapter class for all listener
	public class ParentListener extends EventDispatcher
	{
		public function ParentListener(self:ParentListener)
		{
			if(self != this)
			{
				//only a subclass can pass a valid reference to self
				throw new Error("Abstract class did not receive reference to self. " + 
						"ParentListener cannot be instantiated directly.");
			}
		}
		
		//Handlers---------------------------------------------------------------------------------------------
		public function preloadModuleHandler(ev:Event):void{}       //PreloadModuleEvent
		public function sessionChangeHandler(ev:Event):void{}       //SessionEvent
		public function setSessionHandler(ev:Event):void{}          //SessionEvent
		public function sessionFailHandler(ev:Event):void{}         //SessionEvent
		
		public function initDataHandler(ev:Event):void{}            //InitEvent
		public function initDataLoadedHandler(ev:Event):void{}      //InitEvent
		public function updateDataHandler(ev:Event):void{}          //InitEvent
		public function getSessionHandler(ev:Event):void{}          //initEvent
		
		public function loginHandler(ev:Event):void{}               //LoginEvent
		public function exitHandler(ev:Event):void{}                //ExitEvent
		public function roomClickedHandler(ev:Event):void{}         //RoomEvent
		public function modelInitUpdateHandler(ev:Event):void{}     //ModelEvent
		public function modelLinkStateUpdateHandler(ev:Event):void{}//ModelEvent
		
		public function setLongpullHandler(ev:Event):void{}         //LongpullEvent
		public function longpullHandler(ev:Event):void{}            //LongpullEvent
		public function longpullActiveHandler(ev:Event):void{}      //LongpullEvent
		public function longpullSuccessHandler(ev:Event):void{}     //LongpullEvent
		public function longpullCompleteHandler(ev:Event):void{}    //LongpullEvent
		
		public function actionActiveHandler(ev:Event):void{}        //ActionEvent
		public function actionSuccessHandler(ev:Event):void{}       //ActionEvent
		public function actionCompleteHandler(ev:Event):void{}      //ActionEvent
		public function actionFailHandler(ev:Event):void{}          //ActionEvent
	}
}