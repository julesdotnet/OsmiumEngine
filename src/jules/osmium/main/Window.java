package jules.osmium.main;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window extends JFrame{
	private static final long serialVersionUID = 1L;
	
	DrawPanel dp;
	
	private Window() {
		
		dp = new DrawPanel();
		dp.startRenderThread();
		setTitle("OsmiumEngine | early alpha");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(dp);

		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Window());
	}
}
