package view;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.MenusBar;



/* Use JTabbedPane to display map*/
public class MapTabPane extends DnDTabbedPane implements MouseListener, ActionListener{
	private ImageIcon icon;
	private WelcomePagePanel welcomePanel = new WelcomePagePanel();
	private JPanel displayedMapPanel = welcomePanel;
	private MenusBar menus;
	private boolean addNew = false;

	/** Construct the tab */
	public MapTabPane(){
		super();  
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		addMouseListener(this);  
		setPreferredSize(new Dimension(700, 600));
		setMinimumSize(new Dimension(350, 100));
		addTab("Welcome page", welcomePanel, icon);
	}

	/** add a map tab */
	public void addMapPane(String filename, String name){
//		Model model = new Model();
//		model.setFileName(this.model.getFileName());
//		model.setBounds(this.model.getBounds());
//		model.setWaypoints(this.model.getWaypoints());	
//		model.setActionListenerList(this.model.getActionListenerList());
//
//		//Put the new pane and its model into the hash smap
//		maps.put((MapPanel)(displayedMapPanel = new MapPanel()), model);
//		((MapPanel)displayedMapPanel).setModel(this.model);
		addTab(filename, displayedMapPanel = new MapPanel(name), icon);
		// Set the selected map as the new map tab
		setSelectedComponent(displayedMapPanel);
	}

	public void addTab(String title, JPanel component, Icon extraIcon) {  
		super.addTab(title, new CloseTabIcon(extraIcon), component);  
	}

	/** use mouse clicked method to listen click event and then close the tab pane */
	public void mouseClicked(MouseEvent e) {  
		int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());  
		if (tabNumber < 0) {  
			return;  
		}  

		Rectangle rect1 = ((CloseTabIcon) getIconAt(tabNumber)).getBounds(); 
		if (rect1.contains(e.getX(), e.getY())) {   
			
			// Save change or not when close the tab
			if (getTitleAt(getSelectedIndex()).startsWith("*") == true){
				int n = JOptionPane.showConfirmDialog(null,"Save Changes?",
						"Save Resource", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,null);

				if (n == JOptionPane.YES_OPTION)
					menus.saveAs();
				else if (n == JOptionPane.CANCEL_OPTION)
					return;			
			}
			
			//the tab is being closed 
			this.removeTabAt(tabNumber);  
//			maps.remove(displayedMapPanel);
		}  
	}  
	public void mouseEntered(MouseEvent e) {  
	}  
	public void mouseExited(MouseEvent e) {  
	}  
	public void mousePressed(MouseEvent e) {  
	}  
	public void mouseReleased(MouseEvent e) {  
	}  

	/** When model change, add "*" to before the tab pane title*/
	public void actionPerformed(ActionEvent actionEvent){
//		if (model.getFileName()== null || model.getWaypoints() == null) return;
//		if (model.getWaypoints().size() == 0 || getSelectedIndex() == -1) return;
//		if (displayedMapPanel != null && displayedMapPanel instanceof MapPanel)
//			setTitleAt(getSelectedIndex(), "*" + new File(model.getFileName()).getName());
	}



	
	public void setMenus(MenusBar menus){
		this.menus = menus;
	}
}

/** Draw a close tab icon */
class CloseTabIcon implements Icon {  
	private int x_pos;  
	private int y_pos;  
	private int width;  
	private int height;  
	private Icon fileIcon;  
	public CloseTabIcon(Icon fileIcon) {  
		this.fileIcon = fileIcon;  
		width = 16;  
		height = 16;  
	}  

	/** If no image added, draw a close icon */
	public void paintIcon(Component c, Graphics g, int x, int y) {  
		this.x_pos = x;  
		this.y_pos = y;  
		Color col = g.getColor();  
		g.setColor(new Color(250,100,100));  
		int y_p = y + 2;  
		g.drawLine(x + 1, y_p, x + 12, y_p);  
		g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);  
		g.drawLine(x, y_p + 1, x, y_p + 12);  
		g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);  
		g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);  
		g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);  
		g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);  
		g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);  
		g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);  
		g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);  
		g.setColor(col);  
		if (fileIcon != null) {  
			fileIcon.paintIcon(c, g, x + width, y_p);  
		}  
	}  
	public int getIconWidth() {  
		return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);  
	}  
	public int getIconHeight() {  
		return height;  
	}  
	public Rectangle getBounds() {  
		return new Rectangle(x_pos, y_pos, width, height);  

	}    
}  

/** A panel as welcome pange*/
class WelcomePagePanel extends JPanel {
	private ImageIcon imageIcon = new ImageIcon("image/welcomepage.jpg");
	private Image image = imageIcon.getImage();

	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		if(image != null){
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
