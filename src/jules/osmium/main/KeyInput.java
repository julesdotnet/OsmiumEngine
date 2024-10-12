package jules.osmium.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyInput implements KeyListener {
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    private static boolean stopMouseMovement = true;
    
    private Map<Integer, Runnable> bindings = new HashMap<>();

    // Enum to represent direction, including diagonals
    private enum Direction {
        NONE,
        FORWARD,
        LEFT,
        BACK,
        RIGHT,
        FORWARD_LEFT,
        FORWARD_RIGHT,
        BACK_LEFT,
        BACK_RIGHT
    }

    private Direction current = Direction.NONE;

    public KeyInput() {
        bindKey(KeyEvent.VK_W, () -> {
            wPressed = true;
            updateDirection();
        });

        bindKey(KeyEvent.VK_A, () -> {
            aPressed = true;
            updateDirection();
        });

        bindKey(KeyEvent.VK_S, () -> {
            sPressed = true;
            updateDirection();
        });

        bindKey(KeyEvent.VK_D, () -> {
            dPressed = true;
            updateDirection();
        });

        bindKey(KeyEvent.VK_ESCAPE, () -> {
        });
    }

    public void bindKey(int keyCode, Runnable function) {
        bindings.put(keyCode, function);
    }

    private void execute(int keyCode) {
        if (bindings.containsKey(keyCode)) {
            bindings.get(keyCode).run();
        }
    }
    private void updateDirection() {
        if (wPressed && aPressed) {
            current = Direction.FORWARD_LEFT;
        } else if (wPressed && dPressed) {
            current = Direction.FORWARD_RIGHT;
        } else if (sPressed && aPressed) {
            current = Direction.BACK_LEFT;
        } else if (sPressed && dPressed) {
            current = Direction.BACK_RIGHT;
        } else if (wPressed) {
            current = Direction.FORWARD;
        } else if (aPressed) {
            current = Direction.LEFT;
        } else if (sPressed) {
            current = Direction.BACK;
        } else if (dPressed) {
            current = Direction.RIGHT;
        } else {
            current = Direction.NONE;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        execute(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) wPressed = false;
        if (keyCode == KeyEvent.VK_A) aPressed = false;
        if (keyCode == KeyEvent.VK_S) sPressed = false;
        if (keyCode == KeyEvent.VK_D) dPressed = false;

        updateDirection();

        if (keyCode == KeyEvent.VK_ESCAPE) {
            stopMouseMovement = !stopMouseMovement;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
  
    }

    public String getDirectionAsString() {
        return current.toString();
    }

    public static boolean getEscapeReleased() {
        return stopMouseMovement;
    }
}
