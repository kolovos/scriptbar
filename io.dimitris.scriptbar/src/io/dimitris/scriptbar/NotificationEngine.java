package io.dimitris.scriptbar;


public class NotificationEngine {
	
	protected static NotificationEngine instance;
	
	public static NotificationEngine getInstance() {
		if (instance == null) {
			instance = new NotificationEngine();
		}
		return instance;
	}

	public NotificationEngine() {
		
	}
	
	public void show(Object title, Object description) {

		try {
			String applescript = "display notification \"" + description + "\" with title \"" + title + "\"";
			String[] args = new String[]{"osascript", "-e", applescript};
			Runtime.getRuntime().exec(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
