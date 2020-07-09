package antform.test;

import javax.swing.UIManager;

import antform.gui.Control;
import antform.gui.ControlPanel;
import antform.types.Table;

/**
 * @author René Ghosh
 * 1 avr. 2005
 */
public class TableTest {
public static void main(String[] args) {
	try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	Control control = new Control(new CallbackTest(), "Table test", null, null, false);
	ControlPanel panel = control.getPanel();
	Table t = new Table();
	t.setLabel("a table");
	t.setProperty("prop");
	t.setEditable(true);
	t.setColumns("col1,col2,col3");
	t.setData("d1,d2,d3;d1,d2,d3;d1,d2,d3");
	t.setRowSeparator(";");
	t.setColumnSeparator(",");
	t.setEscapeSequence("\\");
	t.addToControlPanel(panel);
	control.show();
	System.exit(0);
}
}
