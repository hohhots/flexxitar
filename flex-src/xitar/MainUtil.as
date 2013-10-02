package xitar
{
	import mx.controls.Alert;
	
	public class MainUtil
	{
		
		public function MainUtil(){}
		
		//Static function -----------------------------------------------------------------------------------
		public static function generateRandomString(newLength:uint = 6, userAlphabet:String = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789"):String{
			var alphabet:Array = userAlphabet.split("");
			var alphabetLength:int = alphabet.length;
			var randomLetters:String = "";
			for (var i:uint = 0; i < newLength; i++){
				randomLetters += alphabet[int(Math.floor(Math.random() * alphabetLength))];
			}
			return randomLetters;
		}
		
		public static function removeSpaces(str:String):String{
			var text_arr:Array=str.split(' ');
			return(text_arr.join(''));
		}
		
		public static function getCurrentTime():Number{
			return new Date().getTime();
		}
	}
}