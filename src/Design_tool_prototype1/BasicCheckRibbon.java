package Design_tool_prototype1;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import org.jvnet.flamingo.common.*;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.popup.*;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;

public class BasicCheckRibbon extends JRibbonFrame {

	public BasicCheckRibbon() {
		super("Ribbon test");
	}

	public void configureRibbon() {
		configureApplicationMenu();
	}

	protected void configureApplicationMenu() {
		RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(
				new EmptyResizableIcon(32), "New", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Invoked creating new document");
					}
				}, CommandButtonKind.ACTION_ONLY);

		RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
		applicationMenu.addMenuEntry(amEntryNew);

		applicationMenu.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
					@Override
					public void menuEntryActivated(JPanel targetPanel) {
						targetPanel.removeAll();
						JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(
								CommandButtonDisplayState.MEDIUM);
						String groupName = "Default Documents";
						openHistoryPanel.addButtonGroup(groupName);
						for (int i = 0; i < 5; i++) {
							JCommandButton historyButton = new JCommandButton(i
									+ "    " + "default" + i + ".html",
									new EmptyResizableIcon(32));
							historyButton
									.setHorizontalAlignment(SwingUtilities.LEFT);
							openHistoryPanel
									.addButtonToLastGroup(historyButton);
						}
						openHistoryPanel.setMaxButtonColumns(1);
						targetPanel.setLayout(new BorderLayout());
						targetPanel.add(openHistoryPanel, BorderLayout.CENTER);
					}
				});

		this.getRibbon().setApplicationMenu(applicationMenu);

	}

//	protected void configureControlPanel(JPanel controlPanel) {}

	public static void main(String[] args) {

		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
                catch (Exception exc) {}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BasicCheckRibbon c = new BasicCheckRibbon();
				c.configureRibbon();
                                c.setBounds(0, 0, 800, 400);
				c.setVisible(true);
				c.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}
}
