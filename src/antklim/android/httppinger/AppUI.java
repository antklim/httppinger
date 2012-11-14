package antklim.android.httppinger;

import android.content.res.Resources;
import antklim.httppinger.R;

public class AppUI {
	public static String httpPingerTabsPinger() {
		switch (AppGLOBAL.CURRENT_LANGUAGE_ID) {
			case 0:
				return Resources.getSystem().getString(R.string.tb_pngrus);
			case 1:
				return Resources.getSystem().getString(R.string.tb_pngeng);
			default :
				return "" ;
		}
	}

	public static String httpPingerTabsHistory() {
		switch (AppGLOBAL.CURRENT_LANGUAGE_ID) {
		case 0:
			return Resources.getSystem().getString(R.string.tb_hstrus);
		case 1:
			return Resources.getSystem().getString(R.string.tb_hsteng);
		default :
			return "" ;
	}
	}
}
