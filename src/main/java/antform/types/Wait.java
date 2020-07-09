package antform.types;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Sequential;

import antform.AbstractTaskWindow;
import antform.gui.ControlPanel;
import antform.interfaces.ActionComponent;
import antform.util.ActionType;

public class Wait extends BaseType implements SubBuildListener {
	private String label = "Please wait ...";
	private boolean showProgress = true;
	private boolean closeWhenDone = false;
	private Sequential sequential;
	private JProgressBar progressBar;
	private JLabel labelComponent;
	private Project project;
	private Thread createdThread;
	private ControlPanel controlPanel;
	private AbstractTaskWindow task;
	
	public Wait(Project project) {
		this.project = project;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public void setCloseWhenDone(boolean closeWhenDone) {
		this.closeWhenDone = closeWhenDone;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void addConfiguredSequential(Sequential s) {
		sequential = s;
	}

	public void addToControlPanel(ControlPanel panel) {
		this.controlPanel = panel;
		controlPanel.addCentered(buildPanel());
		if (sequential != null) {
			createdThread = new Thread() {
				public void run() {
					sequential.perform();
				}
			};
			createdThread.start();
			sequential.getProject().addBuildListener(this);
		}
	}

	private void executionDone() {
		progressBar.setIndeterminate(false);
		progressBar.setValue(100);
		labelComponent.setText("Done");
	}
	
	private JPanel buildPanel() {
		JPanel widgetPanel = new JPanel(new GridLayout(0, 1));
		if (showProgress) {
			progressBar = new JProgressBar(0, 100);
			progressBar.setValue(0);
			progressBar.setStringPainted(false);
			progressBar.setIndeterminate(true);
			widgetPanel.add(progressBar);
		}
		labelComponent = new JLabel(label);
		widgetPanel.add(labelComponent);
		return widgetPanel;
	}

	public void buildStarted(BuildEvent evt) {
	}

	public void buildFinished(BuildEvent evt) {
		project.log("Received buildFinished event in Wait widget", Project.MSG_DEBUG);
		waitForThread(evt);
	}

	public void targetStarted(BuildEvent evt) {
	}

	public void targetFinished(BuildEvent evt) {
	}

	public void taskStarted(BuildEvent evt) {
	}

	public void taskFinished(BuildEvent evt) {
		project.log("Received taskFinished event in Wait", Project.MSG_DEBUG);
		if (evt.getTask().equals(sequential)) {
			executionDone();
			if (closeWhenDone) {
				HiddenButton button = new HiddenButton();
				task.getActionRegistry().register(button);
				ActionEvent event = new ActionEvent(button.getComponent(), ActionEvent.ACTION_PERFORMED, null);
				task.getActionRegistry().actionPerformed(event);
			}
		}
	}

	public void messageLogged(BuildEvent evt) {
	}

	public void subBuildStarted(BuildEvent evt) {
	}

	public void subBuildFinished(BuildEvent evt) {
		project.log("Received subbuildFinished event in Wait widget", Project.MSG_DEBUG);
		waitForThread(evt);
	}
	
	private void waitForThread(BuildEvent evt) {
		if (createdThread != null && sequential.getProject().equals(evt.getProject())) {
			if (createdThread.isAlive()) {
				project.log("Waiting for background threads completion...", Project.MSG_VERBOSE);
				try {
					createdThread.join();
				} catch (InterruptedException ie) {
					project.log("Thread " + createdThread.getName() + " got interrupted: " + ie.getMessage(), Project.MSG_DEBUG);
				}
			}
		}
	}

	public boolean validate(Task task) {
		boolean attributesAreValid = true;
		if (sequential == null) {
			task.log("Wait : Nothing to wait for.");
			attributesAreValid = false;
		}
		this.task = (AbstractTaskWindow) task;
		return attributesAreValid;
	}
	
	private class HiddenButton implements ActionComponent {
		
		JButton button = new JButton();

		public int getActionType() {
			return ActionType.CANCEL;
		}

		public String getTarget() {
			return null;
		}

		public boolean isBackground() {
			return false;
		}

		public AbstractButton getComponent() {
			return button;
		}

		public boolean isLoopExit() {
			return true; // as this button is used when closewhendone is true
		}

		public boolean isNewProject() {
			return true; // no target, so not used...
		}
		
	}
}
