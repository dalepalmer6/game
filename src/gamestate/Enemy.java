package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;

public class Enemy extends BattleEntity {
	private int width;
	private int height;
	
	public Enemy(String texture, String name, int width, int height, int hp, int pp) {
		super(texture,name,hp,pp);
		this.width = width*4;
		this.height = height*4;
	}
	
//	public TextWindowWithPrompt performBattleAction(BattleMenu bm, ArrayList<PCBattleEntity> party, ArrayList<Enemy> enemies) {
////		bm.addMenuItem(new TextWindowWithPrompt(name + " managed to deal " + 4 + " points of damage to " + party.get(0).name + "!!",0,0,10,10,bm.getState()));
//		System.out.println(name + " managed to deal " + 4 + " points of damage to " + party.get(0).name + "!!");
//		party.get(0).dealDamage(4);
////		bm.setPrompt(name + " managed to deal " + 4 + " points of damage to" + party.get(0).name + "!!");
//		return new TextWindowWithPrompt(name + " managed to deal " + 4 + " points of damage to" + party.get(0).name + "!!",bm.getState().getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,bm.getState());
//	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}
}
