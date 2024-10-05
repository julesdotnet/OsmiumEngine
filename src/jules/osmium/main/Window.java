package jules.osmium.main;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jules.osmium.ui.MainMenu;

public class Window extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public static DrawPanel dp = new DrawPanel();
	BufferedImage logo;
	MainMenu mainMenu;
	
	private Window() {
		try {
			logo = ImageIO.read(this.getClass().getResource("/startscreen_res/iconimage.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		mainMenu = new MainMenu(this, dp);
		setTitle("OsmiumEngine | early alpha");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setIconImage(logo);
		
		add(mainMenu);

		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Window());
	}
}
