import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class MenuButton extends GameObject{
	
	private static final long serialVersionUID = -8983567354488271114L;
	
	private int width = 100;
	private int height = 50;
	private String label;

	public MenuButton(int x, int y, ID id, String label) {
		super(x, y, id);
		if (id == null || label == null) {
			throw new IllegalArgumentException();
		}
		this.label = label;
	}
	
	public MenuButton(int x, int y, ID id) {
		super(x, y, id);
		if (id == null) {
			throw new IllegalArgumentException();
		}
		label = "";
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		// Centered to the given x,y
		g.setColor(Color.white);
		g.fillRect(x-width/2, y-height/2, width, height);
		
		// Draw button label
		g.setColor(Color.pink);
		g.setFont(new Font("Monospaced", Font.BOLD, 18));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(label, x - fm.stringWidth(label)/2, 
				y + 7);
	}
	
	// Getter and Setter Methods
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getButtonLabel() {
		return label;
	}
	
	public void setButtonLabel(String label) {
		this.label = label;
	}
}
