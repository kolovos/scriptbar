package io.dimitris.scriptbar;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.exceptions.EglRuntimeException;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.eclipse.epsilon.eol.execute.context.Variable;

public class ScriptBar extends JDialog {
	
	protected String directory = "scripts";
	protected GridLayout gridLayout = new GridLayout(1,1);
	protected File defaultDirectory = null;
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
		//this.setTitle("ScriptBar");
		this.setBounds(100, 100, 500, 500);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
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
		
		for (final File file :  profile.getCanonicalFile().listFiles()) {
			
			for (final String extension : new String[]{"applescript", "egl"}) {
				
				if (file.getName().endsWith("." + extension) && !file.getName().startsWith("_")) {
					final JButton button = new JButton(file.getName().replace("." + extension, "").
							replace("<->", " ↔ ").
							replace("->", " → ").
							replace("<-", " ← ")
							);
					
					try {
						BufferedReader br = new BufferedReader(new FileReader(file));
						String firstLine = br.readLine();
						br.close();
						String tooltip = null;
						if (extension.equalsIgnoreCase("applescript")) {
							if (firstLine.startsWith("#")) {
								tooltip = firstLine.substring(2).trim();
							}
						}
						else {
							if (firstLine.startsWith("[%//")) {
								tooltip = firstLine.substring(4).trim();
							}
						}
						
						if (tooltip != null) {
							button.setToolTipText("<html>" + WordUtils.wrap(tooltip, 40, "<br>", true) + "</html>");
						}
						
					}
					catch (Exception ex) {}
					
					this.getContentPane().add(button);
					
					button.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
					        
							new Timer().schedule(new TimerTask() {
								
								@Override
								public void run() {
									try {
										if ("applescript".equalsIgnoreCase(extension)) {
											runAppleScript(file);
										}
										else {
											
											//Create the popup menu.
									        final JPopupMenu popup = new JPopupMenu();
									        popup.add(new JMenuItem(new AbstractAction("Copy to clipboard") {
									            public void actionPerformed(ActionEvent e) {
									            	try {
										            	String result = (runEgl(file) + "").trim();
														StringSelection stringSelection = new StringSelection(result);
														Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
														clpbrd.setContents(stringSelection, null);
									            	}
									            	catch (Exception ex) {
									            		handleScriptException(file, ex);
									            	}
									            }
									        }));
									        popup.add(new JMenuItem(new AbstractAction("Save as ...") {
									            public void actionPerformed(ActionEvent e) {
									                JFileChooser chooser = new JFileChooser();
									                
									                if (defaultDirectory != null) chooser.setCurrentDirectory(defaultDirectory);
									                chooser.setFileFilter(new FileFilter() {
														
														@Override
														public String getDescription() {
															return "Files";
														}
														
														@Override
														public boolean accept(File f) {
															return !file.isDirectory();
														}
													});
									                
									                int option = chooser.showDialog(ScriptBar.this, "OK");
									                if (option == JFileChooser.APPROVE_OPTION) {
									                	try {
										                	File selectedFile = chooser.getSelectedFile();
										                	boolean save = true;
										                	if (selectedFile.exists()) {
										                		
										                		int confirmation = JOptionPane.
										                				showConfirmDialog(ScriptBar.this, "File already exists. Overwrite?", 
										                						"ScriptBar", JOptionPane.YES_NO_OPTION);
										                		
										                		if (JOptionPane.YES_OPTION != confirmation) {
										                			save = false;
										                		}
										                	}
										                	
										                	if (save) {
										                		defaultDirectory = selectedFile.getParentFile();
											                	String result = (runEgl(file) + "").trim();
											                	FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
											                	fos.write(result.getBytes());
											                	fos.close();
										                	}
									                	}
									                	catch (Exception ex) {
										            		handleScriptException(file, ex);
										            	}
									                }
									            }
									        }));
									        
									        popup.show(button, button.getWidth(), 2);
											
											
										}
									} catch (Exception ex) {
										handleScriptException(file, ex);
									}
								}
							}, 0);
							
						}
					});
					buttons.add(button);
				}
			}
		}
			
		
		gridLayout.setRows(buttons.size()+1);;
		this.pack();
	}
	
	protected void handleScriptException(File file, Exception ex) {
		Throwable t;
		
		if (ex instanceof EglRuntimeException) {
			t = ex.getCause();
		}
		else {
			t = ex;
		}
		
		GrowlEngine.getInstance().show(file.getName(), t.getMessage());
		ex.printStackTrace();
	}
	
	public Object runAppleScript(File scriptFile) throws Exception {
		ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("AppleScript");
		String script = new Scanner(scriptFile).useDelimiter("\\Z").next();
		script = "with timeout of 3600 seconds\n" + script + "\n" + "end timeout";
		return scriptEngine.eval(script);
	}
	
	public Object runEgl(File template) throws Exception {
		
		PlainXmlModel model = null;
		
		for (File file : template.getParentFile().getCanonicalFile().listFiles()) {
			if (file.getName().equals("_xml.applescript")) {
				model = new PlainXmlModel();
				String xml = runAppleScript(file) + "";
				model.setName("M");
				model.setXml("<?xml version=\"1.0\"?>" + xml.trim());
				model.setReadOnLoad(true);
				model.setStoredOnDisposal(false);
				model.load();
			}
		}
		
		EglTemplateFactoryModuleAdapter module = new EglTemplateFactoryModuleAdapter(new EglTemplateFactory());
		module.parse(template);
		if (module.getParseProblems().size() > 0) {
			throw new RuntimeException(module.getParseProblems().get(0).toString());
		}
		
		module.getContext().getFrameStack().put(Variable.createReadOnlyVariable("growl", GrowlEngine.getInstance()));
		
		if (model != null) {
			module.getContext().getModelRepository().addModel(model);
		}
		return module.execute();
		
	}
	
}
