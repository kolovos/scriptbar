package io.dimitris.scriptbar;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JButton;
import javax.swing.JDialog;

public class ScriptBar extends JDialog {
	
	protected String directory = "scripts";
	
	public static void main(String[] args) throws Exception {
		new ScriptBar().run();
	}

	protected void run() throws Exception {
		
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		
		/*
		Image defaultIcon = new ImageIcon("resources/application.gif").getImage();
		
		if (SystemTray.isSupported()) {
			SystemTray systemTray = SystemTray.getSystemTray();
			PopupMenu popupMenu = new PopupMenu();
			
			TrayIcon trayIcon = new TrayIcon(defaultIcon, "Newsgroup Watcher", popupMenu);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					ScriptBar.this.setVisible(true);
				}
				
			});
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e) {
				System.exit(-1);
			}
			
		}*/
		
		//JOptionPane.showMessageDialog(this, new File(directory).getCanonicalFile().exists());
		
		for (final File scriptFile :  new File(directory).getCanonicalFile().listFiles()) {
			if (scriptFile.getName().endsWith("applescript")) {
				final JButton button = new JButton(scriptFile.getName().replace(".applescript", ""));
				this.getContentPane().add(button);
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						new Timer().schedule(new TimerTask() {
							
							@Override
							public void run() {
								ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("AppleScript");
								try {
									scriptEngine.eval(new FileReader(scriptFile));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}, 0);
						
					}
				});
				buttons.add(button);
			}
		}
		
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
		
		this.getContentPane().setLayout(new FlowLayout());
		this.setTitle("ScriptBar");
		this.setBounds(100, 100, 500, 500);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		
		int maxWidth = 0;
		int totalHeight = 0;
		for (JButton button : buttons) {
			if (button.getWidth() > maxWidth) {
				maxWidth = button.getWidth();
			}
			totalHeight = totalHeight + button.getHeight() + 10;
		}
		
		this.setBounds(100, 100, maxWidth, totalHeight);
		
	}
	
}
