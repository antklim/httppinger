package antklim.android.httppinger.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class HphstoryDataSource {
	private SQLiteDatabase db;
	private PingerDataBase dbh;
	private String[] allCol = {PingerDataBase.COLUMN_HSORID, 
			PingerDataBase.COLUMN_HISURL, PingerDataBase.COLUMN_HISTME};

	public HphstoryDataSource(Context context) {
		dbh = new PingerDataBase(context);
	}
	
	public void open() throws SQLException {
		db = dbh.getWritableDatabase();
	}
	
	public void close() {
		dbh.close();
	}
	
	public Hphstory addHphstory(String hisurl, String histme) {
		ContentValues cv = new ContentValues();
		cv.put(PingerDataBase.COLUMN_HISURL, hisurl);
		cv.put(PingerDataBase.COLUMN_HISTME, histme);
		
		long _ID = db.insert(PingerDataBase.TABLE_HPHSTORY, null, cv);
		Cursor cursor = db.query(PingerDataBase.TABLE_HPHSTORY, allCol, 
				PingerDataBase.COLUMN_HSORID + " = " + _ID, 
				null, null, null, null);
		cursor.moveToFirst();
		
		Hphstory newHphstory = cursorToHphstory(cursor);
		cursor.close();
		return newHphstory;
	}

	public void delHphstory(Hphstory hphstory) {
		long _ID = hphstory.getHsorid();
		System.out.println("Hphstory deleted with id: " + _ID);
		db.delete(PingerDataBase.TABLE_HPHSTORY, 
				PingerDataBase.COLUMN_HSORID + " = " + _ID, null);
	}
	
	public void delAllHphstory() {
		System.out.println("Deleting all rows");
		db.delete(PingerDataBase.TABLE_HPHSTORY, null, null);
	}
	
	public List<Hphstory> getAllHphstory() {
		List<Hphstory> hphstories = new ArrayList<Hphstory>();
		String sort = PingerDataBase.COLUMN_HSORID + " DESC" ; 
		Cursor cursor = db.query(PingerDataBase.TABLE_HPHSTORY, 
				allCol, null, null, null, null, sort );
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Hphstory hphstory = cursorToHphstory(cursor);
			hphstories.add(hphstory);
			cursor.moveToNext();
		}
		cursor.close();
		
		return hphstories;
	}
	
	private Hphstory cursorToHphstory(Cursor cursor) {
		Hphstory hphstory = new Hphstory();
		hphstory.setHsorid(cursor.getLong(0));
		hphstory.setHisurl(cursor.getString(1));
		hphstory.setHistme(cursor.getString(2));
		return hphstory;
	}

	public Hphstory getLastHistory() {
		Hphstory hphstory = null;
		String sort = PingerDataBase.COLUMN_HSORID + " DESC" ; 
		Cursor cursor = db.query(PingerDataBase.TABLE_HPHSTORY, 
				allCol, null, null, null, null, sort );
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			hphstory = cursorToHphstory(cursor);
		} else {
			hphstory = new Hphstory(-1, "", "");
		}
		cursor.close();
		return hphstory;
	}
}
