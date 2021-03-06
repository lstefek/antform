/***************************************************************************\*
 *                                                                            *
 *    AntForm form-based interaction for Ant scripts                          *
 *    Copyright (C) 2005 René Ghosh                                           *
 *                                                                            *
 *   This library is free software; you can redistribute it and/or modify it  *
 *   under the terms of the GNU Lesser General Public License as published by *
 *   the Free Software Foundation; either version 2.1 of the License, or (at  *
 *   your option) any later version.                                          *
 *                                                                            *
 *   This library is distributed in the hope that it will be useful, but      *
 *   WITHOUT ANY WARRANTY; without even the implied warranty of               *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser  *
 *   General Public License for more details.                                 *
 *                                                                            *
 *   You should have received a copy of the GNU Lesser General Public License *
 *   along with this library; if not, write to the Free Software Foundation,  *
 *   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA              *
 \****************************************************************************/
package antform.types;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.tools.ant.Task;

import antform.gui.ControlPanel;
import antform.interfaces.ActionListenerComponent;
import antform.interfaces.Requirable;
import antform.util.FontStyleAttribute;

/**
 * Text property edited over multiple lines.
 * 
 * @author René Ghosh
 */
public class MultilineTextProperty extends DefaultProperty implements Requirable,
		ActionListenerComponent {
	private int columns = 40, rows = 5;
	private boolean required;
	private JTextArea textArea;
	private String fontName;
	private int fontSize = -1;
	private int fontStyle = Font.PLAIN;

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return columns.
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @param columns.
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * @return rows.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyleAttribute fsa) {
		this.fontStyle = fsa.getFontStyle();
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void addToControlPanel(ControlPanel panel) {
		textArea = new JTextArea(rows, columns);
		textArea.setEditable(isEditable());
		if (fontName != null || fontSize == -1 ) {
			Font f = textArea.getFont();
			String name = fontName == null ? f.getFontName() : fontName;
			int size = fontSize > 0 ? fontSize : f.getSize();
			textArea.setFont(new Font(name, fontStyle, size));
		}
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.getStylesheetHandler().addMultiLineTextArea(textArea);
		panel.getStylesheetHandler().addScrollPane(scrollPane);
		initComponent(scrollPane, panel);
	}

	public boolean validate(Task task) {
		return super.validate(task, "MultiLineTextProperty");
	}

	public void ok() {
		getProject().setProperty(getProperty(), textArea.getText());
	}

	public void reset() {
		textArea.setText(getCurrentProjectPropertyValue());
	}

	public boolean requiredStatusOk() {
		boolean ok = true;
		if (isRequired() && "".equals(textArea.getText())) {
			ok = false;
			textArea.requestFocus();
		}
		return ok;
	}

	public JComponent getFocusableComponent() {
		return textArea;
	}
}
