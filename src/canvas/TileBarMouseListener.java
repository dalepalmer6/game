package canvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import global.GlobalVars;
import mapeditor.MapPreview;
import mapeditor.TileBar;

public class TileBarMouseListener implements MouseListener {
	private int t_id;
	
	public TileBarMouseListener(TileBar d) {
		// TODO Auto-generated constructor stub
		this.t_id = d.getHoveredTile();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Clicked a tiles");
		//set the tile selected as the tool to use
		GlobalVars.mapPreviewEditTool = this.t_id;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

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
