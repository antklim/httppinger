package antklim.android.httppinger.db;

public class Hphstory {
	private long hsorid;
	private String hisurl;
	private String histme;

	public long getHsorid() {
		return hsorid;
	}
	public void setHsorid(long hsorid) {
		this.hsorid = hsorid;
	}
	public String getHisurl() {
		return hisurl;
	}
	public void setHisurl(String hisurl) {
		this.hisurl = hisurl;
	}
	public String getHistme() {
		return histme;
	}
	public void setHistme(String histme) {
		this.histme = histme;
	}
	
	public Hphstory() {
		super();
	}
	
	public Hphstory(long hsorid, String hisurl, String histme) {
		super();
		this.hsorid = hsorid;
		this.hisurl = hisurl;
		this.histme = histme;
	}
	
	@Override
	public String toString() {
		return "Hphstory [hsorid=" + hsorid + ", hisurl=" + hisurl
				+ ", histme=" + histme + "]";
	}
}
