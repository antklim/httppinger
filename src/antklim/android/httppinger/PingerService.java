package antklim.android.httppinger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class PingerService extends Service {
	AlarmManager am;
	private static final int SEC[] = {1, 60, 3600};
	private static final int BEG = 3000;
	private static final int MSC = 1000;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.Log("PingerService", "i", "Service created.");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.Log("PingerService", "i", "Service started.");
		
		int per = HttpPinger.getPer(); 
		int typ = HttpPinger.getTyp(); 
		if ( typ < 0 || typ > 2 ) typ = 0;
		
		per = per * SEC[ typ ] * MSC ;
		per = ( per < BEG ) ? BEG : per ;
		
		Intent rcvr = new Intent(this, PingerReceiver.class);		
		rcvr.putExtra("URL", intent.getStringExtra("URL"));
		
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, rcvr, 0);
		
		am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
				SystemClock.elapsedRealtime() + BEG, 
				per, pi);	
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (am != null) {
			Intent rcvr = new Intent(this, PingerReceiver.class);
			am.cancel(PendingIntent.getBroadcast(this, 0, rcvr, 0));
		}
		Logger.Log("PingerService", "i", "Service destroyed.");
	}
}
