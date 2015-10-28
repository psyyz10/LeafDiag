package controller;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import view.MapTabPane;



/** As menus, a big controller */
public class MenusBar extends JMenuBar{
	private JMenuItem jmiOpen, jmiSave, jmiSaveAs, jmiExit, jmiHelp;
	private JCheckBoxMenuItem jmiShowMap, jmiShowToolBar, jmiShowTable, jmiShowEntry;
	private ImageIcon newImageIcon =
			new ImageIcon("image/new.png");
	private ImageIcon openImageIcon =
			new ImageIcon("image/open.png");
	private ImageIcon saveImageIcon =
			new ImageIcon("image/save.png");
	private ImageIcon saveAsImageIcon =
			new ImageIcon("image/saveas.png");
	private ImageIcon exitImageIcon =
			new ImageIcon("image/exit.png");
	private ImageIcon helpImageIcon =
			new ImageIcon("image/help.png");
	private JButton jbtOpen = new JButton(openImageIcon);
	private JButton jbtSave = new JButton (saveImageIcon);
	private JButton jbtSaveAs = new JButton(saveAsImageIcon);
	private JButton jbtHelp = new JButton(helpImageIcon);
	private JButton jbtExit = new JButton (exitImageIcon);
	private JToolBar jtb = new JToolBar();
	private JFileChooser jFileChooser1 
	= new JFileChooser(new File("."));
	private MapTabPane mapTabPane =new MapTabPane();;
	private JSplitPane jSplitPane1;
	private JSplitPane jSplitPane2;
	
	/** Construct MenusBar */
	public MenusBar(){
		super();

		//Use a file filter to filt ".csv" file
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Leaf Image", "png");
		jFileChooser1.setFileFilter(filter);

		// Add menu "File" to menu bar
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		add(fileMenu);

		// Add menu "View" to menu bar
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		add(viewMenu);

		JMenu helpMenu = new JMenu("Help");
		add(helpMenu);

		//Add file menu item
		fileMenu.add(jmiOpen = new JMenuItem("Open", 'O'));
		fileMenu.add(jmiSave = new JMenuItem("Save", 'S'));
		fileMenu.add(jmiSaveAs = new JMenuItem("Save as"));
		fileMenu.addSeparator();
		fileMenu.add(jmiExit = new JMenuItem("Exit", 'E'));


		helpMenu.add(jmiHelp = new JMenuItem("Help - use cases",'H'));

		viewMenu.add(jmiShowMap = new JCheckBoxMenuItem("Show Map panel", new ImageIcon("image/earth.png")));
		viewMenu.add(jmiShowToolBar = new JCheckBoxMenuItem("Show Tool Bar", true));
		jmiShowMap.setState(true);

		//Set menu icon
		jmiOpen.setIcon(openImageIcon);
		jmiSave.setIcon(saveImageIcon);
		jmiSaveAs.setIcon(saveAsImageIcon);
		jmiExit.setIcon(exitImageIcon);
		jmiHelp.setIcon(helpImageIcon);

		//Set shortcut key
		jmiOpen.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		jmiSave.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));


		OpenListener openListener = new OpenListener();
		SaveListener saveListner = new SaveListener();
		SaveAsListener saveAsListner = new SaveAsListener();
		HelpListener helpListner = new HelpListener();
		ExitListener exitListner = new ExitListener();


		jmiOpen.addActionListener(openListener);
		jmiSave.addActionListener(saveListner);
		jmiSaveAs.addActionListener(saveAsListner );
		jmiHelp.addActionListener(helpListner);
		jmiExit.addActionListener(exitListner);
		jbtOpen.addActionListener(openListener);
		jbtSave.addActionListener(saveListner);
		jbtSaveAs.addActionListener(saveAsListner);
		jbtHelp.addActionListener(helpListner);
		jbtExit.addActionListener(exitListner);


		/** If selected, make it visible */
		jmiShowMap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mapTabPane.setVisible(jmiShowMap.isSelected());
				jSplitPane1.resetToPreferredSizes();
			}
		});


		/** If selected, make it visible */
		jmiShowToolBar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				jtb.setVisible(jmiShowToolBar.isSelected());
			}
		});
	}


	/** perform the open action when Open button is clicked */
	class OpenListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			open();
		}
	}

	/** perform the save action when Save button is clicked */
	class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
		}
	}	

	/** perform the save as action when save as button is clicked */
	class SaveAsListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
		}
	}

	/** perform the help action when help button is clicked */
	class HelpListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JTextArea jta = new JTextArea();
			jta.setEditable(false);
			try{
				Scanner input = new Scanner(new File("src/HelpText.txt"));

				while (input.hasNextLine()){
					jta.append(input.nextLine() + "\n"); 
				}

				JScrollPane jsp = new JScrollPane(jta);
				jsp.setPreferredSize(new Dimension(700, 400));
				JOptionPane.showMessageDialog(null, jsp, "Help - use cases",
						JOptionPane.INFORMATION_MESSAGE,null);
			}catch(IOException ex){
				ex.printStackTrace();
			}	
		}
	}

	/** perform the exit action when exit button is clicked */
	class ExitListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			//Ask if save the change when click the exit menu item
			int n = JOptionPane.showConfirmDialog(null,"Save Changes?",
					"Save Resource", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,null);

			if (n == JOptionPane.YES_OPTION)
				saveAs();
			else if (n == JOptionPane.NO_OPTION)
				System.exit(0);
		}
	}

	/** Open file */
	private void open() {
		if (jFileChooser1.showOpenDialog(this) ==
				JFileChooser.APPROVE_OPTION)
			open(jFileChooser1.getSelectedFile());
	}

	/** Open file with the specified File instance */
	private void open(File file) {
		String name = "";
		try {
			name = UseMatLab.runRec(file.getPath());
		} catch (MatlabConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mapTabPane.addMapPane(file.getName(),name);
		//mapTabPane.setTitleAt(mapTabPane.getSelectedIndex(), file.getName());
//		try {			
//			mapTabPane.addMapPane(file.getName());
//			mapTabPane.setTitleAt(mapTabPane.getSelectedIndex(), file.getName());
//
//		}catch (Exception ex) {
//
//			//If the file formation is not correct, show error message
//			JOptionPane.showMessageDialog(null,"This is not a leaf image",
//					"Error opening ", JOptionPane.INFORMATION_MESSAGE);
//		}
	}

	/** Save as file */
	public void saveAs() {
		
		if (jFileChooser1.showSaveDialog(this) ==
				JFileChooser.APPROVE_OPTION) {

			/** Make the file end with ".csv"  */
			File file = jFileChooser1.getSelectedFile();
			String filePath = file.getAbsolutePath();
			if(!filePath.endsWith(".png")) {
				file = new File(filePath + ".png");
			}
			save(file);
			mapTabPane.setTitleAt(mapTabPane.getSelectedIndex(), file.getName());
		}
	}
//
	/** Save file with specified File instance */
	private void save(File file) {
		try {
			copyFileUsingStream(new File("H:\\Private\\MATLAB\\identification.png"),file);
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null,file.getName() + " cannot save",
					"Error Saving ", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
//
//	public Model getModel(){
//		return model;
//	}
//
//	public void setModel(Model model){
//		this.model = model;
//	}
//
	/** Set tree view panel for manipulating show view function */
	public void setViewPanel( MapTabPane mapPanel){

		this.mapTabPane = mapPanel;
	}

	/** Set tree view panel for manipulating tool bar button function*/
	public void setToolBar(JToolBar jtb){
		this.jtb = jtb;
		jtb.add(jbtOpen);
		jtb.add(jbtSave);
		jtb.add(jbtSaveAs);
		jtb.add(jbtHelp);
		jtb.add(jbtExit);
	}

	/** Set tree view panel for manipulating show view function */
	public void setSplitPane(JSplitPane jSplitPane1, JSplitPane jSplitPane2){
		this.jSplitPane1 = jSplitPane1;
		this.jSplitPane2 = jSplitPane2;
	}
//
//	/**
//	 * A check box menu item to add or remove the dockable.
//	 */
//	private class DockableMenuItem extends JCheckBoxMenuItem
//	{
//
//		public DockableMenuItem(Dockable dockable)
//		{
//			super(dockable.getTitle(), dockable.getIcon());
//
//			setSelected(dockable.getDock() != null);
//
//			DockableMediator dockableMediator = new DockableMediator(dockable, this);
//			dockable.addDockingListener(dockableMediator);
//			addItemListener(dockableMediator);
//		}
//
//	}
//
//	/**
//	 * A listener that listens when menu items with dockables are selected and deselected.
//	 * It also listens when dockables are closed or docked.
//	 */
//	private class DockableMediator implements ItemListener, DockingListener
//	{
//
//		private Dockable dockable;
//		private Action closeAction;
//		private Action restoreAction;
//		private JMenuItem dockableMenuItem;
//
//		public DockableMediator(Dockable dockable, JMenuItem dockableMenuItem) 
//		{
//
//			this.dockable = dockable;
//			this.dockableMenuItem = dockableMenuItem;
//			closeAction = new DefaultDockableStateAction(dockable, DockableState.CLOSED);
//			restoreAction = new DefaultDockableStateAction(dockable, DockableState.NORMAL);
//		}
//
//		public void itemStateChanged(ItemEvent itemEvent)
//		{
//
//			dockable.removeDockingListener(this);
//			if (itemEvent.getStateChange() == ItemEvent.DESELECTED)
//			{
//				// Close the dockable.
//				closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
//			} 
//			else 
//			{
//				// Restore the dockable.
//				restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
//			}
//			dockable.addDockingListener(this);
//			jSplitPane2.resetToPreferredSizes();
//			jSplitPane1.resetToPreferredSizes();
//
//		}
//
//		public void dockingChanged(DockingEvent dockingEvent) {
//			if (dockingEvent.getDestinationDock() != null)
//			{
//				dockableMenuItem.removeItemListener(this);
//				dockableMenuItem.setSelected(true);
//				dockableMenuItem.addItemListener(this);	
//			}
//			else
//			{
//				dockableMenuItem.removeItemListener(this);
//				dockableMenuItem.setSelected(false);
//				dockableMenuItem.addItemListener(this);
//			}
//			jSplitPane2.resetToPreferredSizes();
//			jSplitPane1.resetToPreferredSizes();
//		}
//
//		public void dockingWillChange(DockingEvent dockingEvent) {}
//	}
}
