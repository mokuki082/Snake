import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;
/**
 * Draw the snake blocks in position
 * and manage their speed and directions
 */
public class SnakeController extends GameObject{
	
	private static final long serialVersionUID = -4112745058021438657L;
	private static final int SCREEN_WIDTH = 500;
	private static final int SCREEN_HEIGHT = 500;
	public static final int SCREEN_SCALE = 1;
	
	private int score = -1;
	
	private class SnakeBlock extends GameObject{

		private static final long serialVersionUID = -317217179211353865L;
		private static final int WIDTH = 10;
		private static final int HEIGHT = 10;
		
		private SnakeBlock next;
		private int nextVelX = 0;
		private int nextVelY = 0;
		
		public SnakeBlock(int x, int y, ID id) {
			super(x, y, id);
			next = null;
		}

		@Override
		public void tick() {
			x += velX;
			y += velY;
		}

		@Override
		public void render(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(x,y,WIDTH, HEIGHT);
		}
		
		public void setNext(SnakeBlock sb) {
			next = sb;
		}
		
		public SnakeBlock getNext() {
			return next;
		}
		
		public int getWidth() {
			return WIDTH;
		}
		
		public int getHeight() {
			return HEIGHT;
		}

	}
	
	private class Food extends GameObject {
		private static final int WIDTH = 10;
		private static final int HEIGHT = 10;
		
		public Food(int x, int y) {
			super(x, y, ID.spr_food);
		}

		@Override
		public void tick() {
			
		}

		@Override
		public void render(Graphics g) {
			g.setColor(Color.red);
			g.fillRect(x,y,WIDTH, HEIGHT);
		}
		
	}
	
	private class Pair {
		
		private int x;
		private int y;
		
		private Pair(int x, int y) {
			
			this.x = x;
			this.y = y;
		}
	}
	
	
	private Handler handler;
	private int length;
	private SnakeBlock head;
	private SnakeBlock tail;
	private boolean[][] snakeMap;
	private Food food;
	
	public SnakeController(Handler handler) {
		super(0,0,ID.obj_snakeController);
		this.handler = handler;
		length = 0;
		head = null;
		int x = (SCREEN_WIDTH - 20)/SnakeBlock.WIDTH;
		int y = (SCREEN_HEIGHT - 20)/SnakeBlock.HEIGHT;
		snakeMap = new boolean[x][y];
		newFood();
	}
	
	
	private void newFood() {
		
		LinkedList<Pair> avaliableSpots = new LinkedList<>();
		
		for (int x = 0; x < snakeMap.length; x++) {
			for (int y = 0; y < snakeMap[0].length; y++) {
				if (!snakeMap[x][y]) {
					Pair coor = indexToCoor(new Pair(x,y));
					avaliableSpots.add(new Pair(coor.x,coor.y));
				}
			}
		}
		
		Random rnd = new Random();
		Pair newSpot = avaliableSpots.get(rnd.nextInt(avaliableSpots.size()));
		food = new Food(newSpot.x, newSpot.y);
		
	}
	
	private void gameOver() {
		MenuButton startAgain = new MenuButton(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, ID.btn_startAgain);
		startAgain.setWidth(300);
		startAgain.setButtonLabel("Start Again \n "
				+ "Score: " + Integer.toString(this.score));
		handler.addObject(startAgain);
		handler.removeObject(this);
	}

	@Override
	public void tick() {
		
		// Check if head has collided into its body
		if (checkCollision(head.getX(),head.getY())) {
			gameOver();
		}
		
		// Check if head has collided into a food
		if (food.x == head.getX() && food.y == head.getY()) {
			newFood();
			addSnakeBlock();
		}
		
		// Update the velocity of each snake block
		SnakeBlock prev = head;
		head.tick();
		
		// Clear snakeMap
		int x = snakeMap.length;
		int y = snakeMap[0].length;
		snakeMap = new boolean[x][y];
		
		while (prev.getNext() != null) {
			SnakeBlock curr = prev.getNext();
			curr.setVelX(curr.nextVelX);
			curr.setVelY(curr.nextVelY);
			
			curr.nextVelX = prev.getVelX();
			curr.nextVelY = prev.getVelY();
			curr.tick();
			prev = curr;
			
			// Update snakeMap
			Pair indexes = coorToIndex(new Pair(curr.getX(), curr.getY()));
			snakeMap[indexes.x][indexes.y] = true;
			
			curr = curr.next;
		}
		
		
		// If the snake Head is off the screen, you lose
		if (head.getX() <= 0 || head.getX() >= SCREEN_WIDTH - head.getWidth()||
				head.getY() <= 0 || head.getY() >= SCREEN_WIDTH - head.getHeight()) {
			gameOver();
		}
	}

	@Override
	public void render(Graphics g) {
		
		// Render snake body
		SnakeBlock curr = head;
		while (curr != null) {
			curr.render(g);
			curr = curr.getNext();
		}
		
		// Render food
		food.render(g);
		
		// Render score
		String score = "Score: " + Integer.toString(this.score);
		g.setColor(Color.white);
		g.setFont(new Font("Monospaced", Font.BOLD, 18));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(score, SCREEN_WIDTH - (fm.stringWidth(score) + 20), 
				30);
	}
	
	public void addSnakeBlock() {
		
		SnakeBlock newSnake = null;
		
		if (length == 0) { // create the head of the snake
			
			newSnake = new SnakeBlock(24 * SnakeBlock.WIDTH, 24 * SnakeBlock.HEIGHT, ID.spr_snakeBlock);
			newSnake.setVelX(10);
			head = newSnake;
		} else {
			
			int newX = tail.getX();
			int newY = tail.getY();
			
			newSnake = new SnakeBlock(newX, newY, ID.spr_snakeBlock);
			newSnake.setVelX(tail.getVelX());
			newSnake.setVelY(tail.getVelY());
			tail.setNext(newSnake);
		}
		
		tail = newSnake;
		length++;
		score++;
	}
	
	private boolean checkCollision(int x, int y) {
		Pair indexes = coorToIndex(new Pair(x,y));
		return snakeMap[indexes.x][indexes.y];
	}
	
	private Pair coorToIndex(Pair coor) {
		int x = (coor.x - SnakeBlock.WIDTH) / SnakeBlock.WIDTH;
		int y = (coor.y - SnakeBlock.HEIGHT) / SnakeBlock.HEIGHT;
		return new Pair(x,y);
	}
	
	private Pair indexToCoor(Pair indexes) {
		int x = indexes.x * SnakeBlock.WIDTH + SnakeBlock.WIDTH;
		int y = indexes.y * SnakeBlock.HEIGHT + SnakeBlock.HEIGHT;
		return new Pair(x,y);
	}
	
	public void moveUp() {
		if (length == 1 || head.getVelY() != 10) { // Avoids going back
			head.setVelY(-10);
			head.setVelX(0);
		}
	}
	
	public void moveDown() {
		if (length == 1 || head.getVelY() != -10) {
			head.setVelY(10);
			head.setVelX(0);
		}
	}
	
	public void moveRight() {
		if (length == 1 || head.getVelX() != -10) {
			head.setVelX(10);
			head.setVelY(0);
		}
	}
	
	public void moveLeft() {
		if (length == 1 || head.getVelX() != 10) {
			head.setVelX(-10);
			head.setVelY(0);
		}
	}
	
}
