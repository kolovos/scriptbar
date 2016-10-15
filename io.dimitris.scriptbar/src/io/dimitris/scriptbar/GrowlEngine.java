package io.dimitris.scriptbar;


public class GrowlEngine {
	
	protected static GrowlEngine instance;
	
	public static GrowlEngine getInstance() {
		if (instance == null) {
			instance = new GrowlEngine();
		}
		return instance;
	}

	public GrowlEngine() {
		
	}
	
	public void show(Object title, Object description) {

		try {
			AppleScriptEngine.getInstance().eval(
				"display notification \"" + description + "\" with title \"" + title + "\"", "delay 1"	
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
