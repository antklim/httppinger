package antklim.android.httppinger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Pinger {
	private String url ;
	private String httpCont = "";
	private String errmsg = "";
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHttpCont() {
		return httpCont;
	}
	public void setHttpCont(String httpCont) {
		this.httpCont = httpCont;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public Pinger(String url) {
		super();
		this.url = url;
	}
	public Pinger() {
		super();
	}

	public void sendRqst() {
		HttpURLConnection con = null;
		errmsg = "" ;
		httpCont = "";
		try {
			URL url = new URL(this.url);
			con = (HttpURLConnection)url.openConnection();
			readStream(con.getInputStream());
			Logger.Log("PingerService", "i", "URL " + this.url + " has been requested.");
		} catch (Exception e) {
			errmsg += "1001 " + e.getMessage();
			Logger.Log("PingerService", "e", "REQUEST ERROR " + errmsg);
		} finally {
			if (con != null) con.disconnect();
		}
	}

	private void readStream(InputStream in) {
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(in));
			String line = "";
			StringBuffer sb = new StringBuffer("");
			String NL = System.getProperty("line.separator");
			while ((line = rd.readLine()) != null) sb.append(line + NL);
			httpCont = sb.toString();
		} catch (IOException e) {
			errmsg += "2001 " + e.getMessage();
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException ie) {
					errmsg += "2002 " + ie.getMessage();
				}			
			}
		}		
	}
	
	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager)HttpPinger.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		
		if (ni != null && ni.isConnected())	return true;
		return false;
	}
	
	public void dummyRqst () {
		Logger.Log("PingerService", "i", "Dummy request.");
	}
}
