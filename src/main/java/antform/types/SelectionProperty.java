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

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.apache.tools.ant.Task;

import antform.gui.ControlPanel;
import antform.interfaces.ActionListenerComponent;
import antform.util.CSVReader;

/**
 * Property for selecting a value among a list of proposed values.
 * 
 * @author René Ghosh
 */
public class SelectionProperty extends DefaultProperty implements ActionListenerComponent {
	private String values;
	private String separator = ",";
	private String escapeSequence = "\\";
	private String[] splitValues = null;
	private JComboBox comboBox;

	/**
	 * set the escape sequence
	 */
	public void setEscapeSequence(String escapeSequence) {
		this.escapeSequence = escapeSequence;
	}

	/**
	 * return the escape sequence
	 */
	public String getEscapeSequence() {
		return escapeSequence;
	}

	/**
	 * @return splitValues.
	 */
	public String[] getSplitValues() {
		if (splitValues == null) {
			split();
		}
		return splitValues;
	}

	/**
	 * @param splitValues.
	 */
	public void setSplitValues(String[] splitValues) {
		this.splitValues = splitValues;
	}

	/**
	 * @return separator.
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separator.
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @return values.
	 */
	public String getValues() {
		return values;
	}

	/**
	 * split the values
	 */
	private void split() {
		CSVReader reader = new CSVReader(separator, escapeSequence);
		List valueList = reader.digest(values, true);
		splitValues = (String[]) valueList.toArray(new String[valueList.size()]);
	}

	/**
	 * @param values
	 */
	public void setValues(String values) {
		this.values = values;
	}

	public void addToControlPanel(ControlPanel panel) {
		comboBox = new JComboBox(getSplitValues());
		comboBox.setEnabled(isEditable());
		panel.getStylesheetHandler().addComboBox(comboBox);
		initComponent(comboBox, panel);
	}

	public boolean validate(Task task) {
		return validate(task, "SelectionProperty");
	}

	public boolean validate(Task task, String widget) {
		boolean isValid = super.validate(task, widget);
		if (getValues() == null) {
			task.log(widget + " : attribute \"values\" missing.");
			isValid = false;
		}
		return isValid;
	}

	public void ok() {
		getProject().setProperty(getProperty(), comboBox.getSelectedItem().toString());
	}

	public void reset() {
		if (isValidValue(getCurrentProjectPropertyValue())) {
			comboBox.setSelectedItem(getCurrentProjectPropertyValue());
		} else {
			comboBox.setSelectedIndex(0);
		}
	}
	
	protected boolean isValidValue(String s) {
		boolean isValidValue = false;
		for (int i = 0 ; i < getSplitValues().length ; i++) {
			if (getSplitValues()[i].equals(s)) {
				isValidValue = true;
				break;
			}
		}
		return isValidValue;
	}

	public JComponent getFocusableComponent() {
		return comboBox;
	}
}
