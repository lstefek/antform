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

import javax.swing.JComponent;

import org.apache.tools.ant.Task;

import antform.gui.ControlPanel;
import antform.gui.FileChooser;
import antform.interfaces.ActionListenerComponent;
import antform.interfaces.Requirable;

/**
 * File selection property
 * @author René Ghosh
 * 4 mars 2005
 */
public class FileSelectionProperty extends DefaultProperty implements Requirable, ActionListenerComponent { 
	private int columns = 34;
	private boolean directoryChooser;
	private boolean required;
	private FileChooser chooser;

	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public boolean isDirectoryChooser() {
		return directoryChooser;
	}
	public void setDirectoryChooser(boolean directoryChooser) {
		this.directoryChooser = directoryChooser;
	}

	public void addToControlPanel(ControlPanel panel) {
		chooser = new FileChooser(columns, directoryChooser);
		panel.getStylesheetHandler().addFileChooser(chooser);
		initComponent(chooser, panel);
	}
	
	public boolean validate(Task task) {
		return super.validate(task, "FileSelectionProperty");
	}

	public void ok() {
		getProject().setProperty(getProperty(), chooser.getText());
	}

	public void reset() {
		chooser.setText(getCurrentProjectPropertyValue());
	}

	public boolean requiredStatusOk() {
		boolean ok = true;
		if (isRequired() && "".equals(chooser.getText())) {
			ok = false;
			chooser.requestFocus();
		}
		return ok;
	}

	public JComponent getFocusableComponent() {
		return chooser;
	}
}
