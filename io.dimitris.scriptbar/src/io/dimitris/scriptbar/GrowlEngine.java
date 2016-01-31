package io.dimitris.scriptbar;

import javax.script.ScriptException;

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
	
	public void show(String title, String description) {
		
		String applicationName = "ScriptBar";
		
		try {
			AppleScriptEngine.getInstance().eval(
				"tell application id \"com.Growl.GrowlHelperApp\"",
				"	set the allNotificationsList to {\"Notification\"}",	
				"	set the enabledNotificationsList to {\"Notification\"}",
				"	register as application \""+ applicationName + "\" all notifications allNotificationsList default notifications allNotificationsList",
				"	notify with name \"Notification\" title \"" + title + "\" description \"" + description + "\" application name \"" + applicationName + "\"",
				"end tell"	
			);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
