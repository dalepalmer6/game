package battlesystem;

import canvas.MainWindow;
import font.Text;
import gamestate.BattleEntity;
import menu.MenuItem;
import menu.StartupNew;

public class PlayerStatusWindow extends MenuItem {
	private Text textName;
	private int width=64*4;
	private int height=64*4;
	private String name;
	private int HP;
	private int PP;
	private int targetHP;	//target values that the window will scroll gradually to
	private int targetPP;
	private String statusCondition;
	private double tickCount = 0;
	private double ticksPerFrame = 0.25;
	private int offsetX;
	private char[] HPdigitsArray = {'0','0','0'};
	private double counter = 0;
	private boolean animating;
	private int editing = 0; // 3 bits, 7 is all three
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void updateAnim() {
		tickCount += ticksPerFrame;
		if (tickCount % 4 == 0 && !animating) {
			if (targetHP != HP) {
				if (targetHP > HP) {
					HP++;
				} else {
					HP--;
				}
				counter = 0;
				editing = 0;
				animating = true;
			}
		}
	}
	
	public PlayerStatusWindow(BattleEntity be, int x, int y,StartupNew state) {
		super(be.getName(),x,y,0,0,state);
		textName = new Text(true,be.getName(),this.x+64,this.y+64,100,100,state.charList);
		this.name = be.getName();
		this.HP = be.getStats().getStat("CURHP");
		this.PP = be.getStats().getStat("CURPP");
		targetHP = HP;
		targetPP = PP;
		this.x = x;
		this.y = y;
	}
	
	public int getHP() {
		return HP;
		
	}
	
	public void updateStatus(int hp, int pp) {
		this.targetHP = hp;
		this.targetPP = pp;
	}
	
	public void drawWindow(MainWindow m) {
		//shouldDrawAll,s,x+TEXT_START_X,y+TEXT_START_Y,this.width,this.height,m.charLis
		
		int step = 8*4;
		int save = 0;
		int overallY = 0;
		m.renderTile(this.x, this.y, 32,32, 0,0,8,8);
		for (int x = step; x < width-step; x+=step) {
			m.renderTile(this.x + x, this.y, 32,32, 8,0,8,8);
			save = x + step;
		}
		m.renderTile(this.x + save, this.y + overallY, 32,32, 16,0,8,8);
		overallY+=step;
		
		for (int y = step; y <= height-step; y+=step) {
			m.renderTile(this.x, this.y + y,32,32,0,8,8,8);
			for (int x = step; x < width; x+=step) {
				m.renderTile(this.x+x, this.y+y, 32,32,8,8,8,8);
				save = x;
			}
			m.renderTile(this.x + save, this.y + y, 32,32, 16,8,8,8);
			overallY+=step;
		}
		
		m.renderTile(this.x,this.y + overallY,32,32,0,16,8,8);
		for (int x = step; x < width-step; x += step) {
			m.renderTile(this.x+x, this.y + overallY,32,32,8,16,8,8);
		}
		m.renderTile(this.x+save,this.y + overallY,32,32,16,16,8,8);
	}
	
	public void drawDigit(MainWindow m,int digit, int offsetX, int x, int y) {
		int coordX = 0;
		int coordY = 0;
		switch (digit) {
			case 0: coordX = 0 + offsetX; coordY = 24; break; 
			case 1: coordX = 32+ offsetX; coordY = 24; break;
			case 2: coordX = 64+ offsetX; coordY = 24; break;
			case 3: coordX = 96+ offsetX; coordY = 24; break;
			case 4: coordX = 0+ offsetX; coordY = 40; break;
			case 5: coordX = 32+ offsetX; coordY = 40; break;
			case 6: coordX = 64+ offsetX; coordY = 40; break;
			case 7: coordX = 96+ offsetX; coordY = 40; break;
			case 8: coordX = 0+ offsetX; coordY = 56; break;
			case 9: coordX = 32+ offsetX; coordY = 56; break;
		}
		m.renderTile(x,y,9*4,14*4,coordX,coordY,9,14);
	}
	
	public void drawHPIndicator(MainWindow m) {
		m.renderTile(this.x+40,this.y+96,16*4,16*4,32,0,16,16);
	}
	
	public void drawPPIndicator(MainWindow m) {
		m.renderTile(this.x+40,this.y+96+14*4,16*4,16*4,48,0,16,16);
	}
	
	
	public void drawHealth(MainWindow m) {
		int hp = HP;
		drawHPIndicator(m);
		System.out.println(tickCount);
		
		String digits = String.valueOf(HP);
		while (digits.length() < 3) {
			digits = "0" + digits;
		}
		char[] oldDigitsArray = HPdigitsArray;
		HPdigitsArray = digits.toCharArray();
		int x = this.x + 112;
		int y = this.y + 96;
		if (editing == 0) {
			for (int i = 0; i < HPdigitsArray.length; i++) {
				char c;
				if((c = HPdigitsArray[i]) !=oldDigitsArray[i]) {
					if (i == 0) {
						editing |= 1;
					}
					if (i == 1) {
						editing |= 2;
					}
					if (i == 2) {
						editing |= 4;
					}
				}
			}
		}
		
		for (int i = 0; i < HPdigitsArray.length; i++) {
			char c = HPdigitsArray[i];
			int offsetX = 0;
			if (((editing & (1 << i)) == (1 << i))) {
				if (c != oldDigitsArray[i] || animating) {
					//scroll the digit to the value
					if (counter %4 == 0) {
						offsetX = 24;
					} else if (counter%4 == 1) {
						offsetX = 16;
					} else if (counter%4== 2 ) {
						offsetX = 8;
					} else if (counter%4== 3 ) {
						offsetX = 0;
						editing = 0;
						animating = false;
					}
				}
				if (animating) {
					counter+=1;
				}
				if (targetHP > HP) {
					offsetX *= -1;
				}
			}
			
			drawDigit(m,Integer.parseInt(String.valueOf(c)),offsetX, x,y);
			x += 8*4;
		}
		
		
	}
	
	public void drawPP(MainWindow m) {
		int pp = PP;
		drawPPIndicator(m);
		String digits = String.valueOf(pp);
		while (digits.length() < 3) {
			digits = "0" + digits;
		}
		char[] digitsArray = digits.toCharArray();
		int x = this.x + 112;
		int y = this.y + 96 + 14*4;
		for (char c : digitsArray) {
			drawDigit(m,Integer.parseInt(String.valueOf(c)),0, x,y);
			x += 8*4;
		}
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img\\battlehud.png");
		drawWindow(m);
		Text.initDrawText(m);
		textName.setX(this.x+64);
		textName.setY(this.y+64);
		textName.draw(m);
		m.setTexture("img\\battlehud.png");
		drawHealth(m);
		drawPP(m);
	}
	
}
