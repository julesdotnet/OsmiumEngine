package jules.osmium.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import jules.osmium.main.DrawPanel;
import jules.osmium.main.Window;

public class MainMenu extends JPanel{
	BufferedImage logo;
	JButton startButton;

	private static final long serialVersionUID = 1L;
	public MainMenu(Window window, DrawPanel dp) {
		int compWidth = 900;
		setPreferredSize(new Dimension(compWidth, 600));
		setBackground(Color.black);
		setLayout(null);
		
		startButton = new JButton();
		startButton.addActionListener(e -> {
			window.remove(this);  
		    dp.startRenderThread();
		    window.add(dp);
		    window.revalidate();
		    window.setResizable(true);
		    dp.requestFocusInWindow();
		});
		int buttonWidth = 300;
		
		startButton.setSize(buttonWidth, 60);
		startButton.setLocation(compWidth / 2 - buttonWidth / 2, 200);
		startButton.setText("Start");
		startButton.setBackground(Color.gray);
		add(startButton);
		
		try {
			logo = ImageIO.read(this.getClass().getResource("/startscreen_res/osmiumlogo.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	 @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        int size = 200;
	        
	        if (logo != null) {
	            g.drawImage(logo, getWidth()/2 - size / 2, -40, size, size, this);
	        }
	    }
}
