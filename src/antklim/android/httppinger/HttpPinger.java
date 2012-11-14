package antklim.android.httppinger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import antklim.android.httppinger.db.Hphstory;
import antklim.android.httppinger.db.HphstoryDataSource;
import antklim.httppinger.R;

public class HttpPinger extends Activity {
    
	private EditText in_url;
	private EditText in_per;
	private EditText in_rsp;
	private Button bt_run;
	private Button bt_stp;
	private Button btexit;
	private Spinner sp_per;
	private CheckBox ch_log;
	private CheckBox ch_mac;
	private static Context context;
	private static String url;
	private static String urlwmac; // URL without MAC
	private static int per = 0;
	private static int typ = 0;
	private static boolean log = false;
	private static boolean run = false;
	
	private static Messenger msgr;
	
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ArrayList<String> loglst = new ArrayList<String>();
	private static final int LSTLIM = 5;
	
	private WifiManager wfm;
	private WifiInfo wfi;
	String mac = null;
	String macE = null;
	
	public static Context getContext() {
		return context;
	}

    public static String getUrl() {
		return url;
	}
    
    public static int getPer() {
		return per;
	}

	public static int getTyp() {
		return typ;
	}

	public static boolean getLog() {
		return log;
	}

    public static Messenger getMsgr() {
		return msgr;
	}

	private Handler msgrHndl = new Handler() {
		public void handleMessage(Message message) {
			StringBuilder sb = new StringBuilder(df.format(new Date()));
			Object mesg = message.obj;
			if (message.arg1 == RESULT_OK && mesg != null) {
				if (loglst.size() >= LSTLIM) loglst.clear();
				loglst.add(0, sb + " " + mesg.toString());
				showLog();
			} else {
				in_rsp.getText().clear();
				in_rsp.setText(sb + " " + "ERROR HAPPEND. CAN'T GET THE MESSAGE");
				loglst.clear();
			}
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httppngr);
        setElements();
	}
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setElmn();
	}
			
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.layout.menu_pinger, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.mntool:
//			Intent toolsIntent = new Intent(this, Tools.class);
//			startActivity(toolsIntent);
//			return true;
		case R.id.mnexit:
			extAction();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setElements() {
        context = this;
        in_url = (EditText)findViewById(R.id.in_url);
        in_per = (EditText)findViewById(R.id.in_per);
        in_rsp = (EditText)findViewById(R.id.in_rsp);
        bt_run = (Button)findViewById(R.id.bt_run);
        bt_stp = (Button)findViewById(R.id.bt_stp);
        btexit = (Button)findViewById(R.id.btexit);
        sp_per = (Spinner)findViewById(R.id.sp_per);
        ch_log = (CheckBox)findViewById(R.id.ch_log);
        ch_mac = (CheckBox)findViewById(R.id.ch_mac);
        msgr = new Messenger(msgrHndl);
                               
        ArrayAdapter<CharSequence> ad_per = ArrayAdapter.createFromResource(
        		this, R.array.sp_perrus, android.R.layout.simple_spinner_item);
        sp_per.setAdapter(ad_per);

        in_url.setOnKeyListener(urlListener);
        in_per.setOnKeyListener(perListener);
        bt_run.setOnClickListener(runListener);
        bt_stp.setOnClickListener(stpListener);
        btexit.setOnClickListener(extListener);
        ch_log.setOnCheckedChangeListener(chkListener);
        ch_mac.setOnCheckedChangeListener(chkListener);
        
        ch_log.setChecked(false);
        ch_mac.setChecked(false);
        bt_stp.setEnabled(false);
        in_rsp.setEnabled(false);
        in_rsp.setScrollbarFadingEnabled(true);
        in_rsp.setKeyListener(null);

        try {
	        wfm = (WifiManager)getSystemService(WIFI_SERVICE);
	        wfi = wfm.getConnectionInfo();
	        mac = wfi.getMacAddress();
        } catch (Exception e) {
        	macE = "Error " + e.getMessage() ;
        }
        
        setUrl(in_url);
	}

	protected void showLog() {
		String buffer = ""; 
		int id = 0;
		Iterator<String> it = loglst.iterator();
		while (it.hasNext()) {
			buffer += it.next();
			if (loglst.size() > 1 && id != loglst.size() - 1) buffer += "\n";	
			id++ ;
		}
		in_rsp.getText().clear();
		in_rsp.setText(buffer);
	}

	private OnClickListener runListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (isUrlValid() && isPerValid()) {
				url = in_url.getText().toString();
				per = Integer.parseInt(in_per.getText().toString());
				typ = Integer.parseInt(String.valueOf(sp_per.getSelectedItemPosition()));
				
				Intent intent = new Intent(HttpPinger.this, PingerService.class);
				switchBtns();
				startService(intent);
				addHistory(url);
			}
		}
	};
	
    private OnClickListener stpListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switchBtns();
			stopService(new Intent(HttpPinger.this, PingerService.class));
		}
	};
	
    private OnClickListener extListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			extAction();
		}
	};
	
	private OnKeyListener urlListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (in_url.getText().toString().length() == 0) in_url.setError(getResources().getText(R.string.rq_urlrus));
			return false;
		}
	};
	
	private OnKeyListener perListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (in_per.getText().toString().length() == 0) in_per.setError(getResources().getText(R.string.rq_perrus));
			return false;
		}
	};

	private OnCheckedChangeListener chkListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.ch_log:
				if (!isChecked) in_rsp.getText().clear();
				in_rsp.setEnabled(isChecked);	
				log = isChecked;				
				break;
			case R.id.ch_mac:
				if (isChecked) {
					urlwmac = in_url.getText().toString();
					if (macE != null) {
						Toast.makeText(getApplicationContext(), "MAC ERROR: " + macE, Toast.LENGTH_LONG).show();
						ch_mac.setChecked(false);
					} else {
						if (mac != null) {
							String u = in_url.getText().toString();
							u += ":" + mac;
							in_url.getText().clear();
							in_url.setText(u);
						} else { 
							Toast.makeText(getApplicationContext(), "MAC is not available", Toast.LENGTH_LONG).show();
							ch_mac.setChecked(false); 
						}
					}
				} else {  
					in_url.getText().clear();
					in_url.setText(urlwmac);
				}
				break;
			default:
				break;
			}
		}
	};
	
	private boolean isUrlValid() {
		if (in_url.getText().toString().length() == 0) return false;
		Pattern p = Pattern.compile("(((f|ht){1}(tp|tps)://)[\\w\\d\\S]+)");
		Matcher m = p.matcher(in_url.getText().toString());
		boolean b = m.matches();
		
		if (b == false) in_url.setError(getResources().getText(R.string.rq_urlrus));
		
		return b;	
	}

	private boolean isPerValid() {
		return in_per.getText().toString().length() != 0;
	}

	private void switchBtns () {
		run = !bt_run.isEnabled();
		setElmn();
	}

	private void setElmn() {
		bt_stp.setEnabled(!run);
		bt_run.setEnabled(run);
		in_url.setEnabled(run);
		in_per.setEnabled(run);
		sp_per.setEnabled(run);
	}

	protected void addHistory(String url) {
		try {
			HphstoryDataSource ds = new HphstoryDataSource(this);
			ds.open();
			StringBuilder sb = new StringBuilder(df.format(new Date()));
			ds.addHphstory(url, sb.toString());
			ds.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Can't insert into base " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void extAction() {
		stopService(new Intent(HttpPinger.this, PingerService.class));
		antklim.android.httppinger.HttpPingerTabs.NM.cancel(0);
		HttpPinger.this.finish();		
	}
	
	private void setUrl(EditText _inurl) {
		try {
			HphstoryDataSource ds = new HphstoryDataSource(this);
			ds.open();
			Hphstory hphstory = ds.getLastHistory();
			if (hphstory.getHsorid() > 0) _inurl.setText(hphstory.getHisurl());
			ds.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Can't insert into base " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}