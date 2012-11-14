package antklim.android.httppinger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import antklim.httppinger.R;

public class HttpPingerTabs extends TabActivity {
	public static NotificationManager NM;
	private Notification N;
	private static TabSpec httppngr = null;
	private static TabSpec history = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = getTabHost();
		
		// HttpPinger
		httppngr = tabHost.newTabSpec(getResources().getString(R.string.tb_pngrus));
		httppngr.setIndicator(getResources().getString(R.string.tb_pngrus));
		Intent httppngrIntent = new Intent(this, HttpPinger.class);
		httppngr.setContent(httppngrIntent);

		// History
		history = tabHost.newTabSpec(getResources().getString(R.string.tb_hstrus));
		history.setIndicator(getResources().getString(R.string.tb_hstrus));
		Intent historyIntent = new Intent(this, History.class);
		history.setContent(historyIntent);
		
		tabHost.addTab(httppngr);
		tabHost.addTab(history);

        NM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        N = new Notification(R.drawable.ic_launcher, getResources().getText(R.string.app_name), System.currentTimeMillis());
        N.flags = Notification.FLAG_ONGOING_EVENT;
        PendingIntent pi = PendingIntent.getActivity(this, 0, this.getIntent(), 0);
        N.setLatestEventInfo(this, getResources().getText(R.string.app_name), 
        		getResources().getText(R.string.app_ntfrus), pi);
        NM.notify(0, N);        		
	}

	public static void changeLanguage() {
		httppngr.setIndicator(AppUI.httpPingerTabsPinger());
		history.setIndicator(AppUI.httpPingerTabsHistory());
	}
}
