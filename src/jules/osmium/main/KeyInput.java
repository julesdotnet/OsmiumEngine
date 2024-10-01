package jules.osmium.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener{
	private boolean wPressed = false, aPressed = false, sPressed = false, dPressed = false;
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("hellofasdddddd");
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			wPressed = true;
			System.out.println("w pressed");
			break;
		case KeyEvent.VK_A:
			aPressed = true;
			break;
		case KeyEvent.VK_S:
			sPressed = true;
			break;
		case KeyEvent.VK_D:
			dPressed = true;
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			wPressed = false;
			System.out.println("RAAAHHH I AM FUMING");
			break;
		case KeyEvent.VK_A:
			aPressed = false;
			break;
		case KeyEvent.VK_S:
			sPressed = false;
			break;
		case KeyEvent.VK_D:
			dPressed = false;
			break;
		}
		
	}
	
	public boolean getWPressed() {
		return wPressed;
	}
	public boolean getAPressed() {
		return aPressed;
	}
	public boolean getSPressed() {
		return sPressed;
	}
	public boolean getDPressed() {
		return dPressed;
	}
	

}
