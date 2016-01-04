package io.dimitris.scriptbar;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JButton;
import javax.swing.JDialog;

public class ScriptBar extends JDialog {
	
	protected String directory = "/Library/Application Support/Skim/Scripts";
	
	public static void main(String[] args) throws Exception {
		new ScriptBar().run();
	}

	protected void run() throws Exception {
		
		this.getContentPane().setLayout(new FlowLayout());
		
		for (final File scriptFile : new File(directory).listFiles()) {
			if (scriptFile.getName().endsWith("applescript")) {
				final JButton button = new JButton(scriptFile.getName().replace(".applescript", ""));
				this.getContentPane().add(button);
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("AppleScript");
						try {
							scriptEngine.eval(new FileReader(scriptFile));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			}
		}
		
		this.setBounds(100, 100, 500, 500);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
}
