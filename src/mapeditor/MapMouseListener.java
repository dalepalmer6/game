package mapeditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import canvas.Drawable;
import global.GlobalVars;

public class MapMouseListener implements MouseListener {
	private Drawable mp;
	
	public MapMouseListener(Drawable mp) {
		this.mp = mp;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//int t_id = ((MapPreview) mp).getTileAtPos(((MapPreview) mp).getTilePosition());
		int t_id = 2;
		System.out.println("placing " + GlobalVars.mapPreviewEditTool);
		((MapPreview) mp).toggleDragging(true);
		((MapPreview) mp).editMap(GlobalVars.mapPreviewEditTool);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		((MapPreview) mp).toggleDragging(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
