package controller;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.MenuBar;

import javax.swing.*;

import view.MapTabPane;


/** Note:
 *  
 *  The project use javadocking.jar to implement dock and undock functions
 *  Please add this archive to the project, it is in the lib folder of 
 *  the project, thanks very much.
 *
 */

public class MainFrameController extends JFrame{
	private MapTabPane mapTabPane = new MapTabPane();
	private JSplitPane jSplitPane2;
	private JSplitPane jSplitPane1;
	private JToolBar jtb = new JToolBar();
	private MenusBar jmb = new MenusBar();

	public MainFrameController(){
		// Create the dock model for the docks.
		

		setJMenuBar(jmb);


		jSplitPane1 = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, jSplitPane2 , 
				mapTabPane);
		
		//jSplitPane2.setOneTouchExpandable(true);

		add(jSplitPane1, BorderLayout.CENTER);
		add(jtb, BorderLayout.NORTH);

		jmb.setToolBar(jtb); 
		jmb.setSplitPane(jSplitPane1, jSplitPane2); 
		jmb.setViewPanel(mapTabPane);
		mapTabPane.setMenus(jmb);
	}
	
	// create and shows GUI
	public static void createAndShowGUI(){
		MainFrameController frame = new MainFrameController();
		frame.setDefaultCloseOperation(3);
		frame.setTitle("LeafDiag");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); 
	}

	public static void main(String[] args) {

		//Set the software UI look and feel and fonts
		try {  
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
			UIManager.put( "Button.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "Label.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "MenuItem.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "TabbedPane.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "Table.font", new Font("Verdana",Font.PLAIN, 11) );
		}  
		catch (Exception e) {  
			e.printStackTrace();  
		}  

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}
}
