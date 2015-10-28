package view;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.*;

import controller.UseMatLab;


/** A panel display the map embedded in the map tab */
public class MapPanel extends JPanel{
	private double[] bounds;
	private int x, y, xDiff, yDiff, width, height, mouseX, mouseY, 
	mouseX2, mouseY2,mapWidth, mapHeight;
	private BufferedImage bufImage; 
	private BufferedImage originalBufImage; 
	private Graphics2D bufImageG; 	
	private double scale = 1.0;  
	private double scaleDiff;
	private Image image;
	private DrawPanel drawPanel;
	private JLabel label;
	private Point position;
	private double minLat, minLon, maxLat, maxLon;
	String path;

	//construct the MapPanel
	public MapPanel(String path){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.path = path;
		add(drawPanel= new DrawPanel());
		
		//Align the left edges of the components.
		drawPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		//label.setAlignmentX(Component.LEFT_ALIGNMENT); //redundant
	}

	// This panel is for drawing and displaying the map
	class DrawPanel extends JPanel  {	
		private JPopupMenu popMenus = new JPopupMenu();
		private JMenuItem jmiZoomIn = new JMenuItem("Zoom in", new ImageIcon("image/zoomin.png"));
		private JMenuItem jmiZoomOut = new JMenuItem("Zoom out", new ImageIcon("image/zoomout.png"));
		private boolean start = true;
		private boolean zoom, center;
		private JSlider jsld = new JSlider(JSlider.VERTICAL);
		private Timer timer = new Timer(300, new TimerListener());

		public DrawPanel(){
			setLayout(new BorderLayout(5, 5));

			loadImage(path);
			
			setBackground(Color.BLACK);
			//new Color(230,230,250)
			setPreferredSize(new Dimension(700, 600));
			setMinimumSize(new Dimension(350, 100));

			jsld.setInverted(true);
			jsld.setMinimum(0);
			jsld.setMaximum(50);
			jsld.setPaintLabels(true);
			jsld.setPaintTicks(true);
			jsld.setMajorTickSpacing(5);
			jsld.setMinorTickSpacing(1);

			popMenus.add(jmiZoomIn);
			popMenus.add(jmiZoomOut);

			//add(jsld, BorderLayout.EAST);
			addMouseMotionListener(new MouseMotionHandler());
			addMouseWheelListener(new MouseWheelListener1());
			addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					xDiff = x - e.getX();
					yDiff = y - e.getY();
					showPopup(e);
				}

				public void mouseReleased(MouseEvent e){
					mouseX2 = e.getX();
					mouseY2 = e.getY();
					showPopup(e);
				}
			});

			jmiZoomIn.addActionListener(new ButtonActionListener());
			jmiZoomOut.addActionListener(new ButtonActionListener());
		}

		//load map image to the panel
		public void loadImage(String fileName) {
			image = this.getToolkit().getImage(fileName); 
			MediaTracker mt = new MediaTracker(this); 
			mt.addImage(image, (int) (Math.random() * 100)); 
			try {
				mt.waitForAll(); 
			} catch (Exception ex) {
				ex.printStackTrace();  
			}
			mapWidth = image.getWidth(null);
			mapHeight = image.getHeight(null);
			originalBufImage =	new BufferedImage(image.getWidth(this),image.getHeight(this),BufferedImage.TYPE_INT_ARGB); 	
			bufImage = originalBufImage;
			bufImageG = bufImage.createGraphics(); 
			bufImageG.drawImage(image, 0, 0, this); 
			start = true;
			repaint(); 
		}

		// apply filter for zoom in and zoom out
		public synchronized void applyFilter() {
			if (bufImage == null)
				return; 
			BufferedImage filteredBufImage =new BufferedImage((int) (image.getWidth(this) * scale),(int) (image.getHeight(this) * scale),BufferedImage.TYPE_INT_ARGB); 
			AffineTransform transform = new AffineTransform(); 
			transform.setToScale(scale, scale); 
			AffineTransformOp imageOp = new AffineTransformOp(transform, null);		
			imageOp.filter(originalBufImage, filteredBufImage);
			bufImage = filteredBufImage; 
			repaint();
		}

		// paint the map
		public void paint(Graphics g) {
			super.paintComponent(g);

			if (start){
				x = (getWidth() - bufImage.getWidth()) / 2;
				y = (getHeight() - bufImage.getHeight()) / 2;			
			}
			if (center){
				x -= (mouseX2 - width / 2);
				y -= (mouseY2 - height / 2);
				center = false;
			}
			if (bufImage != null) {
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(bufImage,x,y,this);

				//drawing the red rectangle arcs, when zoom in or zoom out
				if (zoom){
					g2.setStroke(new BasicStroke(3));
					g2.setColor(Color.RED);
					g2.drawLine(mouseX - 20, mouseY - 10, mouseX - 20, mouseY - 15);
					g2.drawLine(mouseX - 15, mouseY - 15, mouseX - 20, mouseY - 15);
					g2.drawLine(mouseX + 15, mouseY - 15, mouseX + 20, mouseY - 15);
					g2.drawLine(mouseX + 20, mouseY - 10, mouseX + 20, mouseY - 15);
					g2.drawLine(mouseX - 15, mouseY + 15, mouseX - 20, mouseY + 15);
					g2.drawLine(mouseX - 20, mouseY + 10, mouseX - 20, mouseY + 15);
					g2.drawLine(mouseX + 20, mouseY + 10, mouseX + 20, mouseY + 15);
					g2.drawLine(mouseX + 15, mouseY + 15, mouseX + 20, mouseY + 15);
				}
				start = false;
				zoom = false;
				height = getHeight();
				width = getWidth();
			}
		}

		private void showPopup(MouseEvent evt){
			if(evt.isPopupTrigger())
				popMenus.show(evt.getComponent(), evt.getX(), evt.getY());
		}


		// Popup menu item's listener
		class ButtonActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JMenuItem jmi = (JMenuItem)e.getSource();

				//Zoom in 
				if(jmi == jmiZoomIn) {
					if (scale > 5) return;
					scaleDiff = 0.25;
					scale *= 1.25; 		
					x -= (mouseX - x) * scaleDiff;
					y -= (mouseY - y) * scaleDiff;
					scaleDiff = 0;
					zoom = true;
					timer.start();
					applyFilter(); 

					//Zoom out
				} else if(jmi == jmiZoomOut) {
					if (scale < 0.2) return;
					scaleDiff = - 0.2;
					scale *= 0.8; 
					x -= (mouseX - x) * scaleDiff;
					y -= (mouseY - y) * scaleDiff;
					scaleDiff = 0;
					zoom = true;
					timer.start();
					applyFilter(); 

					//Center the map	
				}
			}
		}

		class MouseWheelListener1 implements MouseWheelListener {

			// When mouse wheel rotates, zoom in or zoom out
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation();
				if (notches < 0) {
					if (scale > 5) return;
					scaleDiff = 0.25;
					scale *= 1.25; 
				} else {
					if (scale < 0.2) return;
					scaleDiff = - 0.2;
					scale *= 0.8; 
				}
				x -= (mouseX - x) * scaleDiff;
				y -= (mouseY - y) * scaleDiff;
				scaleDiff = 0;
				zoom = true;
				timer.start();
				applyFilter(); 
			}

		}

		// Use timer to add red rectangle arcs when zoom in or zoom out
		class TimerListener implements ActionListener {
			public void actionPerformed(ActionEvent arg0) {
				repaint();
				timer.stop();
			}

		}


		// implements drag map function and display mouse location function
		class MouseMotionHandler extends MouseMotionAdapter {
			public void mouseDragged(MouseEvent e) {
				x = e.getX() + xDiff;
				y = e.getY() + yDiff;
				repaint();
			}
			public void mouseMoved(MouseEvent e){
				mouseX = e.getX();
				mouseY = e.getY();
			}
		}
	}

}


