package antklim.android.httppinger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import antklim.httppinger.R;

public class Tools extends Activity {

	private Spinner splang;
	private Button btsave;
	private int li = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools);
		
		splang = (Spinner)findViewById(R.id.splang);
		btsave = (Button)findViewById(R.id.btsave);
		
        ArrayAdapter<CharSequence> adlang = ArrayAdapter.createFromResource(
        		this, R.array.splangrus, android.R.layout.simple_spinner_item);
        splang.setAdapter(adlang);
        btsave.setOnClickListener(savListener);		
	}

	private OnClickListener savListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			li = splang.getSelectedItemPosition();
			if (li != splang.INVALID_POSITION) AppGLOBAL.setCURRENT_LANGUAGE_ID(li);
			HttpPingerTabs.changeLanguage();
			finish();
		}
	};
}
