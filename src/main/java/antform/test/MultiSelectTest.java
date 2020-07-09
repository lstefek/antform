package antform.test;

import java.awt.BorderLayout;

import javax.swing.UIManager;

import antform.AbstractTaskWindow;
import antform.gui.Control;
import antform.gui.ControlPanel;
import antform.types.Button;
import antform.types.ButtonBar;
import antform.types.CheckSelectionProperty;
import antform.util.ActionRegistry;
import antform.util.ActionType;

/**
 * @author René Ghosh
 * 29 mars 2005
 */
public class MultiSelectTest extends AbstractTaskWindow {
	public static void main(String[] args) {
		new MultiSelectTest();
	}
	
	public MultiSelectTest() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ActionRegistry actionRegistry = new ActionRegistry(this);
		
		Control control = new Control(new CallbackTest(), "Tree component test", null, null, false);
		ControlPanel panel = control.getPanel();
		CheckSelectionProperty csh = new CheckSelectionProperty();
		csh.setLabel("a multiselect");
		csh.setProperty("prop");
		csh.setValues("element1,element2,element3,element4");
		csh.setSeparator(",");
		csh.setEscapeSequence("\\");
		csh.setEditable(true);
		csh.addToControlPanel(panel);

		ButtonBar controlBar = new ButtonBar();
		controlBar.addConfiguredButton(new Button("ok", null, ActionType.OK));
		controlBar.addConfiguredButton(new Button("reset", null, ActionType.RESET));
		controlBar.setAlign(BorderLayout.EAST);
		controlBar.setMargins(3, 3, 3, 3);
		ControlPanel controlPanel = control.getPanel();
		controlBar.applyStylesheet(controlPanel);
		controlPanel.addButtonPanel(controlBar.getPanel());
		controlBar.register(actionRegistry);

		control.show();
		System.exit(0);
	}
}
