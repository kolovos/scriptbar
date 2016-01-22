package io.dimitris.scriptbar;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ScriptBar extends JDialog {
	
	protected String directory = "scripts";
	protected GridLayout gridLayout = new GridLayout(1,1);
	
	public static void main(String[] args) throws Exception {
		new ScriptBar().run();
	}

	@SuppressWarnings("unchecked")
	protected void run() throws Exception {
		
		File[] profiles = new File(directory).getCanonicalFile().listFiles();
		
		JComboBox<Object> comboBox = new JComboBox<Object>(profiles);
		getContentPane().add(comboBox);
		comboBox.setRenderer(new FileCellRenderer(comboBox.getRenderer()));
		
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					setProfile((File)e.getItem());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
		
		this.getContentPane().setLayout(gridLayout);
		this.setTitle("ScriptBar");
		this.setBounds(100, 100, 500, 500);
		this.setAlwaysOnTop(true);
		
		setProfile(profiles[0]);
		
		this.setVisible(true);
	}
	
	protected void setProfile(File profile) throws IOException {
		
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		
		// Clear old buttons
		
		for (Component component : getContentPane().getComponents()) {
			if (component instanceof JButton) buttons.add((JButton) component);
		}
		
		for (JButton button : buttons) {
			getContentPane().remove(button);
		}
		
		buttons.clear();
		
		// Add new buttons
		
		for (final File scriptFile :  profile.getCanonicalFile().listFiles()) {
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
									String script = new Scanner(scriptFile).useDelimiter("\\Z").next();
									script = "with timeout of 3600 seconds\n" + script + "\n" + "end timeout";
									scriptEngine.eval(script);
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
		
		gridLayout.setRows(buttons.size()+1);;
		this.pack();
	}
	
}
