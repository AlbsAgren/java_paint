package se.miun.alag1901.dt062g.jpaint;

import se.miun.alag1901.dt062g.jpaint.client.Client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.util.ArrayList;
import java.util.LinkedList;

/**
* <h1>JPaintInterface</h1>
* Class for managing GUI to control the program
*
* @author  Albin Ågren (alag1901)
* @version 1.3
* @since   2021-01-10
*/
public class JPaintInterface extends JFrame {
	private static final long serialVersionUID = 1;
	private JPaintInterface window = this;
	private DrawingPanel drawingPanel;
	private LinkedList<Integer> nrToUndo;
	
	private final Client client;
	
	/**
	 * Default constructor
	 */
	public JPaintInterface(Client client) {
		super("JPaint");

		this.client = client;
		this.nrToUndo = new LinkedList<Integer>();

		// set window icon image
		ImageIcon icon = new ImageIcon("jpaint_icon.png");
		this.setIconImage(icon.getImage());

		// set size and set the default window location to the middle of the screen
		this.setSize(800, 400);
		this.setLocationRelativeTo(null);
		
		// exit program on window close
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.drawingPanel = new DrawingPanel();
		
		this.buildMenuBar();
		this.buildDrawingInterface();
	}
	
	/**
	 * Method for building the components of the menu bar as well as setting their
	 * associated event listeners
	 */
	private void buildMenuBar() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		menu = new JMenu("File");

		// add option for adding new drawing
		menuItem = new JMenuItem("New...");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menu.add(menuItem);

		// add listener to New.. option
		menuItem.addActionListener(e -> {
			// get user input
			Drawing newDrawing = new Drawing();	
			
			newDrawing.setName(JOptionPane.showInputDialog(null, "Enter drawing name:"));
			newDrawing.setAuthor(JOptionPane.showInputDialog(null, "Enter author name:"));
			
			// name or author being null means user has pressed 'cancel'
			if(newDrawing.getName() == null || newDrawing.getAuthor() == null) {
				return;
			}
			
			// set new drawing and update window title
			drawingPanel.setDrawing(newDrawing);
			window.updateTitle();
		});
		
		// add option to save current drawing
		menuItem = new JMenuItem("Save as...");
		menuItem.setMnemonic(KeyEvent.VK_S);
		menu.add(menuItem);
		
		// add Save as functionality
		menuItem.addActionListener(e -> {
			String suggestedName = drawingPanel.getDrawing().getByline() + ".xml";
			String fileName = JOptionPane.showInputDialog(null, 
														  "Save drawing to:",
														  suggestedName);
			
			if(fileName != null) {
				// save drawing to file
				FileHandler.saveToXML(drawingPanel.getDrawing(), fileName);
			}			
		});
		
		menuItem = new JMenuItem("Load...");
		menuItem.setMnemonic(KeyEvent.VK_L);
		menu.add(menuItem);
		
		// load drawing from user-specified file
		menuItem.addActionListener(e -> {
			String fileName = JOptionPane.showInputDialog(null, "Enter file name:", ".xml");
			
			if(fileName != null) {
				Drawing loadedDrawing = FileHandler.loadFromXML(fileName);
				
				if(loadedDrawing != null) {
					// set the loaded drawing if no previous drawing exists, otherwise add it
					if(drawingPanel.getDrawing().isEmpty()) {
						drawingPanel.setDrawing(loadedDrawing);
					} else {
						drawingPanel.addDrawing(loadedDrawing);
					}
					updateTitle();
				} else {
					JOptionPane.showMessageDialog(null, "File not read, check filename");
				}
			}
		});
		
		menuItem = new JMenuItem("Info");
		menuItem.setMnemonic(KeyEvent.VK_I);
		menu.add(menuItem);
		
		// display painting information
		menuItem.addActionListener(e -> {
			Drawing drawing = drawingPanel.getDrawing();
			String name = drawing.getName(), author = drawing.getAuthor();

			String info = (name == null || name.isBlank() ? "[unnamed painting]" : name) + " by "
						+ (author == null || author.isBlank() ? "[unnamned author]" : author)
					 	+ "\nNumber of shapes: " + drawing.getShapes().size()
					 	+ "\nTotal area: " + drawing.getTotalArea()
					 	+ "\nTotal circumference: " + drawing.getTotalCircumference(); 
			JOptionPane.showMessageDialog(null, info, "Info", JOptionPane.INFORMATION_MESSAGE);
		});
		
		// add a separating line
		menu.addSeparator();
		menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic(KeyEvent.VK_E);
		menu.add(menuItem);
		
		menuItem.addActionListener(e -> {
			System.exit(0);
		});

		menuBar.add(menu);
		
		// build Edit menu
		menu = new JMenu("Edit");
		
		// undo last added shape
		menuItem = new JMenuItem("Undo");
		menuItem.setMnemonic(KeyEvent.VK_U);
		menu.add(menuItem);
		
		menuItem.addActionListener(e -> {
			if(!nrToUndo.isEmpty()) {
				// a brushstroke is considered a single shape
				drawingPanel.undo(nrToUndo.pop());
				drawingPanel.repaint();
			}
		});
		
		menuItem = new JMenuItem("Name...");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menu.add(menuItem);

		// change painting name on click
		menuItem.addActionListener(e -> {
			// get user input
			drawingPanel.getDrawing().setName(JOptionPane.showInputDialog(null, "Enter drawing name:"));

			// update window title
			window.updateTitle();
		});
		
		menuItem = new JMenuItem("Author...");
		menuItem.setMnemonic(KeyEvent.VK_A);
		menu.add(menuItem);

		// change painting author on click
		menuItem.addActionListener(e -> {
			// get user input
			drawingPanel.getDrawing().setAuthor(JOptionPane.showInputDialog(null, "Enter author name:"));

			// update window title
			window.updateTitle();
		});	
		menuBar.add(menu);

		// build Server menu
		menu = new JMenu("Server");
		
		menuItem = new JMenuItem("Load...");
		menuItem.setMnemonic(KeyEvent.VK_L);
		menu.add(menuItem);		

		// use an implementation of SwingWorker interact with server
		menuItem.addActionListener(e -> {
		    new ListFetchWorker().execute();
		});			

		menuItem = new JMenuItem("Save as...");
		menuItem.setMnemonic(KeyEvent.VK_L);
		menu.add(menuItem);
		
		menuItem.addActionListener(e -> {
			// prompt user for filename
			String suggestedName = drawingPanel.getDrawing().getByline() + ".xml";
			String fileName = JOptionPane.showInputDialog(null, 
														  "Save drawing to:",
														  suggestedName);
			
			if(fileName != null) {
				// save drawing locally before sending to server
				FileHandler.saveToXML(drawingPanel.getDrawing(), fileName);
				new SaveWorker(fileName).execute();
			}
		});					
		menuBar.add(menu);
	}

	/**
	 * Method for building the components of the main area and implementing the functionality 
	 * of the various menu items
	 */
	private void buildDrawingInterface() {	
		JPanel colorPicker = new JPanel();	
		ArrayList<JPanel> colorPanels = new ArrayList<JPanel>();
	
		// use gridbaglayout to create grid in color picker panel
		colorPicker.setLayout(new GridBagLayout());		
		GridBagConstraints c = new GridBagConstraints();
		
		// set color boxes to resize
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;

		// create color panels for different colors
		JPanel colorPanel = new JPanel();
		colorPanel.setBackground(Color.red);
		colorPicker.add(colorPanel, c);
		colorPanels.add(colorPanel);

		colorPanel = new JPanel();
		colorPanel.setBackground(Color.blue);
		colorPicker.add(colorPanel, c);
		colorPanels.add(colorPanel);

		colorPanel = new JPanel();
		colorPanel.setBackground(Color.green);
		colorPicker.add(colorPanel, c);
		colorPanels.add(colorPanel);

		colorPanel = new JPanel();
		colorPanel.setBackground(Color.yellow);
		colorPicker.add(colorPanel, c);
		colorPanels.add(colorPanel);
		
		colorPanel = new JPanel();
		colorPanel.setBackground(Color.orange);
		colorPicker.add(colorPanel,c );
		colorPanels.add(colorPanel);
			
		// add a JComboBox to allow shape selection
		String shapes[] = { "Rectangle", "Circle", "Paintbrush" };
		JComboBox<String> shapeSelector = new JComboBox<String>(shapes);
		shapeSelector.setPreferredSize(new Dimension(125, 50));
		
		// set JComboBox not to resize and add to color picker
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;			
		colorPicker.add(shapeSelector, c);

		// create a status bar at the bottom of the window
		JPanel statusBar = new JPanel();
		statusBar.setBackground(Color.lightGray);
		statusBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		statusBar.setPreferredSize(new Dimension(window.getWidth(), 24));
		statusBar.setLayout(new BorderLayout());

		// default coordinate display before mouse movements
		JLabel coordinateDisplay = new JLabel("Coordinates:");
		statusBar.add(coordinateDisplay, BorderLayout.LINE_START);

		// panel for displaying user selected color
		JPanel selectedColor = new JPanel();
		selectedColor.setLayout(new BorderLayout());
		selectedColor.setOpaque(false);
		
		JLabel colorLabel = new JLabel("Selected color:");
		selectedColor.add(colorLabel, BorderLayout.LINE_START);
		
		JPanel colorSquare = new JPanel();
		// set red as default color choice
		colorSquare.setBackground(Color.red);
		colorSquare.setPreferredSize(new Dimension(20, 20));
		
		selectedColor.add(colorSquare, BorderLayout.LINE_END);
		
		statusBar.add(selectedColor, BorderLayout.LINE_END);
		
		// add a listener to update the coordinates upon mouse movement in the draw area
		drawingPanel.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent me) {
				coordinateDisplay.setText("Coordinates: " + me.getX() + ", " + me.getY());
			}
			public void mouseDragged(MouseEvent me) {
				coordinateDisplay.setText("Coordinates: " + me.getX() + ", " + me.getY());
			}
		});

		// add listeners to update the chosen color when color panel clicked
		for(JPanel panel : colorPanels) {
			panel.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					colorSquare.setBackground(me.getComponent().getBackground());
				}
			});
		}
		
		// inner class to listen to mouse events in drawingPanel to allow drawing shapes
		class DrawListener extends MouseAdapter {
			private Drawing drawing;
			private Shape shape;
			private String color;
			private int shapesAdded = 0;
			
			public void mousePressed(MouseEvent me) {
				// get string representation of color
				color = "#" + Integer.toHexString(colorSquare.getBackground().getRGB()).substring(2);
				drawing = drawingPanel.getDrawing();
				
				// create the shape selected by the jcombobox
				if(shapeSelector.getSelectedItem().toString() == "Circle") {
					shape = new Circle(me.getX(), me.getY(), color);
				} else if(shapeSelector.getSelectedItem().toString() == "Rectangle") {
					shape = new Rectangle(me.getX(), me.getY(), color);
				} else {
					// since this bit was voluntary i took the freedom to play around and add a
					// crude "paintbrush" feature
					shape = new Circle(me.getX(), me.getY(), color);
					shape.addPoint(me.getX() + 8, me.getY() + 8);
				}
				++shapesAdded;
				drawing.addShape(shape);
			}
			
			// update shape as user drags the mouse
			public void mouseDragged(MouseEvent me) {
				if(shapeSelector.getSelectedItem().toString() != "Paintbrush") {
					shape.addPoint(me.getX(), me.getY());
					drawingPanel.repaint();	
				} else {
					shape = new Circle(me.getX(), me.getY(), color);
					shape.addPoint(me.getX() + 8, me.getY() + 8);
					
					// add circle and repaint on every mouse drag
					drawing.addShape(shape);
					++shapesAdded;
					drawingPanel.repaint();
				}
			}
			
			public void mouseReleased(MouseEvent me) {
				if(shapeSelector.getSelectedItem().toString() != "Paintbrush") {
					shape.addPoint(me.getX(), me.getY());
					drawingPanel.repaint();	
				} else {
					shape = new Circle(me.getX(), me.getY(), color);
					shape.addPoint(me.getX() + 8, me.getY() + 8);
					
					drawing.addShape(shape);
					++shapesAdded;
					drawingPanel.repaint();
					
				}				
				nrToUndo.addFirst(shapesAdded);
				shapesAdded = 0;
			}
		}
		
		// add listeners to drawingPanel
		DrawListener dl = new DrawListener();
		drawingPanel.addMouseListener(dl);
		drawingPanel.addMouseMotionListener(dl);
		
		// add the components to the window
		window.add(colorPicker, BorderLayout.PAGE_START);
		window.add(drawingPanel, BorderLayout.CENTER);
		window.add(statusBar, BorderLayout.SOUTH);
	}
	
	/**
	 * Method for updating the window title according to the drawing name and author
	 * Catches null values and changes them to blank strings
	 */
	private void updateTitle() {
		if(drawingPanel.getDrawing().getName() == null) {
			drawingPanel.getDrawing().setName("");
		}
		if(drawingPanel.getDrawing().getAuthor() == null) {
			drawingPanel.getDrawing().setAuthor("");
		}

		if(!drawingPanel.getDrawing().getName().isBlank()) {
			if(!drawingPanel.getDrawing().getAuthor().isBlank()) {
				this.setTitle("JPaint - " + drawingPanel.getDrawing().getName() + " by " + drawingPanel.getDrawing().getAuthor());
				return;
			}
			this.setTitle("JPaint - " + drawingPanel.getDrawing().getName());
			return;
		} else if(!drawingPanel.getDrawing().getAuthor().isBlank()) {
			this.setTitle("JPaint - [unnamed drawing] by " + drawingPanel.getDrawing().getAuthor());
			return;
		}
		this.setTitle("JPaint");
	}

	/**
	* <h2>ListFetchWorker</h2>
	* Implementation of SwingWorker for fetching list of available files from server
	*
	* @author  Albin Ågren (alag1901)
	* @version 1.0
	* @since   2020-12-30
	*/
	public class ListFetchWorker extends SwingWorker<String[], Void> {
		protected String[] doInBackground() {
			return client.getFilenamesFromServer();
		}
		
		protected void done() {
			try {
				String[] fileList = get();
				
			    String input = (String)JOptionPane.showInputDialog(null, "Choose file",
				        "Available drawings", JOptionPane.QUESTION_MESSAGE, null, fileList, fileList[0]);
				    
			    if(input != null) {
				    new FileFetchWorker(input).execute();
			    }
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	* <h2>FileFetchWorker</h2>
	* Implementation of SwingWorker for fetching an xml-file from server
	*
	* @author  Albin Ågren (alag1901)
	* @version 1.0
	* @since   2020-12-30
	*/
	public class FileFetchWorker extends SwingWorker<String, Void> {
		private String fileName;
		
		public FileFetchWorker(String fileName) {
			this.fileName = fileName;
		}
		
		protected String doInBackground() {
			return client.getFileFromServer(fileName);
		}
		
		protected void done() {
			Drawing loadedDrawing;
			try {
				loadedDrawing = FileHandler.loadFromXML(get());
				drawingPanel.setDrawing(loadedDrawing);
				updateTitle();
			} catch(Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Failed to get file from server.");
			}
		}
	}

	/**
	* <h2>SaveWorker</h2>
	* Implementation of SwingWorker for saving a xml-file to server
	*
	* @author  Albin Ågren (alag1901)
	* @version 1.0
	* @since   2020-12-30
	*/
	public class SaveWorker extends SwingWorker<Boolean, Void> {
		private String fileName;
		
		public SaveWorker(String fileName) {
			this.fileName = fileName;
		}
		
		protected Boolean doInBackground() {
			return client.saveFileToServer(fileName);
		}
		
		protected void done() {
			try {
				if(get()) {
					JOptionPane.showMessageDialog(null, "File saved to server.");
				} else {
					JOptionPane.showMessageDialog(null, "Failed to save file to server.");
				}
			} catch(Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Failed to save file to server.");
			}
		}
	}
}
