package canvas;

public interface Hoverable {
	public boolean hovered(double mousex, double mousey);
	
	public void hoveredAction();
	public void unhoveredAction();
}
