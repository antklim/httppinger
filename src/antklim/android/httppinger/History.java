package antklim.android.httppinger;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import antklim.android.httppinger.db.Hphstory;
import antklim.android.httppinger.db.HphstoryDataSource;
import antklim.httppinger.R;

public class History extends ListActivity {
	private HphstoryDataSource ds = null;
	private List<Hphstory> vl = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		try {
			ds = new HphstoryDataSource(this);
			ds.open();
			
			vl = ds.getAllHphstory();
			ArrayAdapter<Hphstory> adhist = new ArrayAdapter<Hphstory>(this, 
					android.R.layout.simple_list_item_1, vl);
			setListAdapter(adhist);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();			
		}
		registerForContextMenu(getListView());
	}

	@Override
	protected void onPause() {
		ds.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		ds.open();
		refresh();
		super.onResume();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.layout.cntm_history, menu);
		menu.setHeaderTitle(R.string.cmtitlrus);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.cm_del:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			long lid = info.id;
			int id = -1;
			
			if (lid > Integer.MIN_VALUE && lid < Integer.MAX_VALUE ) id = (int)lid;
			delHistory(vl.get(id));
			refresh();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.layout.menu_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnrefr:
			refresh();
			return true;
		case R.id.mncler:
			clrHistory();
			refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void clrHistory() {
		try {
			ds.delAllHphstory();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Can't delete all rows " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void delHistory(Hphstory hphstory) {
		try {
			ds.delHphstory(hphstory);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Can't row " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void refresh() {
		vl = ds.getAllHphstory();
		ArrayAdapter<Hphstory> adhist = new ArrayAdapter<Hphstory>(this, 
				android.R.layout.simple_list_item_1, vl);
		setListAdapter(adhist);	
	}
}
