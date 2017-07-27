import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 500;
	public static final int HEIGHT = WIDTH;
	public static final int SCALE = 1;
	public static final String title = "Snake";
	
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	
	public Game() {
		setWindow();
		handler = new Handler();
		this.addMouseListener(new MouseInput(handler));
		this.addKeyListener(new KeyInput(handler));
		handler.addObject(new MenuButton(WIDTH/2, HEIGHT/2, ID.btn_start, "Start"));
		start();
	}
	
	@Override
	public void run() { // Controls tick and render
		long lastTime = System.nanoTime();
		double fps = 10D;
		double tickRate = 1000000000D / fps;
		double delta = 0D;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / tickRate;
			lastTime = now;
			while (delta >= 1) {
				tick();
				render();
				delta--;
			}
		}
		stop();
	}
	
	private void tick() {
		handler.tick();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) { // First tick
			this.createBufferStrategy(3);
			return;
		}
		
		// Set game background color
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.PINK);
		g.fillRect(10, 10, WIDTH * SCALE - 20, HEIGHT * SCALE - 20);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	private void setWindow() {
		JFrame frame = new JFrame(title);
		frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE + 22));
		frame.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE + 22));
		frame.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE + 22));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.add(this);
		
		frame.setVisible(true);
	}
	
	private synchronized void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}
	
	private synchronized void stop() {
		try {
			running = false;
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new Game();
	}
	
}
