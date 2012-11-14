package antklim.android.httppinger.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PingerDataBase extends SQLiteOpenHelper {
	
	public static final String TABLE_HPHSTORY = "hphstory";
	public static final String COLUMN_HSORID = "hsorid";
	public static final String COLUMN_HISURL = "hisurl";
	public static final String COLUMN_HISTME = "histme";

	private static final String DATABASE_NAME = "httppngr.db";
	private static final int DATABASE_VERSION = 1;

	public static final String DATABASE_CREATE = 
			"create table " + TABLE_HPHSTORY 
			+ "(" 
			+ COLUMN_HSORID + " integer primary key autoincrement, "
			+ COLUMN_HISURL + " text not null, " 
			+ COLUMN_HISTME + " text not null " 
			+ ");" ;
	
	public PingerDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(PingerDataBase.class.getName(), 
				"Upgrading database from verison " + oldVersion + 
				" to " + newVersion + " which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HPHSTORY);
		onCreate(db);
	}
}
