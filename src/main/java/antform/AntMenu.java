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
package antform;

import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import antform.gui.CallBack;
import antform.types.Button;
import antform.types.Separator;
import antform.util.TargetInvoker;

/**
 * AntMenu task definition class.
 * @author René Ghosh
 * 12 janv. 2005
 */
public class AntMenu extends AbstractTaskWindow implements CallBack{
	private boolean built = false;
	/**
	 * Initialisation
	 */
	public void init() throws BuildException {
		super.init();
		widgets = new ArrayList();		
	}
	/**
	 * build the visual components
	 */
	protected void build(){
		super.build();		
		control.getPanel().addBlankSouthPanel();
		built = true;
	}
	
	/**
	 * add a configured separator
	 * @param textProperty
	 */
	public void addConfiguredSeparator(Separator separator){		
		widgets.add(separator);
	}
	
	/**
	 * execute method
	 */
	public void execute() throws BuildException {
		do {
			preliminaries();
			if (!built) {
				build();
			}
			if (shouldShow()) {
				super.execute();		
				control.show();
				if (dynamic) {
					built = false;
					control = null;
				}
				if (getActionSource() != null && getActionSource().getTarget() != null && findTargetByName(getActionSource().getTarget()) != null) {
					TargetInvoker invoker = new TargetInvoker(this, getActionSource());
					invoker.perform();
				}
			}
		} while (isLoop() && getActionSource() != null && !getActionSource().isLoopExit() && shouldShow());
	}
	
	/**
	 * add a configured link
	 */
	public void addConfiguredLink(Button button) {
		log("<link> nested elements are deprecated. Use <button> or <buttonbar> instead.");
		addConfiguredButton(button);
	}
}
