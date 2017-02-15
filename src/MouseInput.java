import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter{

	
	private static final int SCREEN_WIDTH = 500;
	private static final int SCREEN_HEIGHT = 500;
	public static final int SCREEN_SCALE = 1;
	
	private Handler handler;
	
	public MouseInput(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < handler.objects.size(); i++) {
			GameObject temp = handler.objects.get(i);
			
			if (temp.getId() == ID.btn_start) {
				// If the point is within the button
				if ((e.getX() > temp.getX() - temp.getWidth()/2 &&
						e.getX() < temp.getX() + temp.getWidth()/2) &&
					(e.getY() > temp.getY() - temp.getHeight()/2 &&
						e.getY() < temp.getY() + temp.getHeight()/2)) {
					handler.removeObject(temp);
					// Add snake controller into the handler
					SnakeController sc = new SnakeController(handler);
					sc.addSnakeBlock();
					handler.addObject(sc);
					break;
				}
			}
			
			if (temp.getId() == ID.btn_startAgain) {
				handler.removeObject(temp);
				MenuButton start = new MenuButton((SCREEN_WIDTH * SCREEN_SCALE)/2,
						(SCREEN_HEIGHT * SCREEN_SCALE)/2, ID.btn_start, "Start");
				handler.addObject(start);
			}
			
			
		}
	}

}
