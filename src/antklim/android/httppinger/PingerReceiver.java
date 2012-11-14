package antklim.android.httppinger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PingerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Pinger pinger = new Pinger(HttpPinger.getUrl());
//		pinger.dummyRqst();
		if (pinger.isNetworkAvailable()) {
			pinger.sendRqst();
		} else {
			Logger.Log("PingerService", "e", "Network is unavailable.");
		}
	}
}
