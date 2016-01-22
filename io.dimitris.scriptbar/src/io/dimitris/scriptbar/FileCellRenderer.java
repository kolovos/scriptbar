package io.dimitris.scriptbar;

import java.awt.Component;
import java.io.File;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.apache.commons.lang3.text.WordUtils;

public class FileCellRenderer implements ListCellRenderer<Object> {
	
	protected ListCellRenderer<Object> renderer;
	
	public FileCellRenderer(ListCellRenderer<Object> renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return renderer.getListCellRendererComponent(list, WordUtils.capitalize(((File)value).getName()), index, isSelected, cellHasFocus);
	}

}
