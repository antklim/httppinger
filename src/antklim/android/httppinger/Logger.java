package antklim.android.httppinger;

import android.os.Message;
import android.os.Messenger;

public class Logger {
	public static void Log(String src, String typ, String mesg) {
//		if (typ.equalsIgnoreCase("d")) Log.d(src, mesg);
//		else if (typ.equalsIgnoreCase("e")) Log.e(src, mesg);
//		else if (typ.equalsIgnoreCase("i")) Log.i(src, mesg);
//		else if (typ.equalsIgnoreCase("v")) Log.v(src, mesg);
//		else if (typ.equalsIgnoreCase("w")) Log.w(src, mesg);
		
		if (HttpPinger.getLog()) {
			Messenger msg = HttpPinger.getMsgr();
			Message mes = Message.obtain();
			mes.arg1 = android.app.Activity.RESULT_OK;
			mes.obj = mesg;
			try {
				msg.send(mes);
			} catch (Exception e) {
				
			}
		}
	}
}
