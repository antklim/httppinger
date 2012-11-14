package antklim.android.httppinger;

public class AppGLOBAL {
	public static String[] LANGUAGE = {"RUS", "ENG"};
	public static int CURRENT_LANGUAGE_ID = 0;
	
	public static int getCURRENT_LANGUAGE_ID() {
		return CURRENT_LANGUAGE_ID;
	}
	public static void setCURRENT_LANGUAGE_ID(int cURRENT_LANGUAGE_ID) {
		CURRENT_LANGUAGE_ID = cURRENT_LANGUAGE_ID;
	}
}
