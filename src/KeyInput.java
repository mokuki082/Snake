import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	
	private static final int SCREEN_WIDTH = 500;
	private static final int SCREEN_HEIGHT = 500;
	public static final int SCREEN_SCALE = 1;
	
	private Handler handler;
	
	public KeyInput(Handler handler) {
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		for (int i = 0; i < handler.objects.size(); i++) {
			
			GameObject temp = handler.objects.get(i);
			
			if (temp.getId() == ID.obj_snakeController) {
				if (key == KeyEvent.VK_UP)
					((SnakeController) temp).moveUp();
				if (key == KeyEvent.VK_RIGHT)
					((SnakeController) temp).moveRight();
				if (key == KeyEvent.VK_DOWN)
					((SnakeController) temp).moveDown();
				if (key == KeyEvent.VK_LEFT)
					((SnakeController) temp).moveLeft();
				if (key == KeyEvent.VK_SPACE)
					((SnakeController) temp).addSnakeBlock();
			}
			
			if (temp.getId() == ID.btn_start) {
				if (key == KeyEvent.VK_ENTER) {
					handler.removeObject(temp);
					// Add snake controller into the handler
					SnakeController sc = new SnakeController(handler);
					sc.addSnakeBlock();
					handler.addObject(sc);
					break;
				}
			}
			
			if (temp.getId() == ID.btn_startAgain) {
				if (key == KeyEvent.VK_ENTER) {
					handler.removeObject(temp);
					MenuButton start = new MenuButton((SCREEN_WIDTH * SCREEN_SCALE)/2,
							(SCREEN_HEIGHT * SCREEN_SCALE)/2, ID.btn_start, "Start");
					handler.addObject(start);
				}
			}
		}
	}
	
}
