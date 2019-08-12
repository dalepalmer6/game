package battlesystem;

import canvas.MainWindow;
import font.Text;
import gamestate.BattleEntity;
import gamestate.Entity;
import gamestate.PCBattleEntity;
import menu.MenuItem;
import menu.StartupNew;

public class PlayerStatusWindow extends MenuItem {
	private Text textName;
//	private int width=64*4;
//	private int height=64*4;
	private String name;
	private int HP;
	private int PP;
	private int targetHP;	//target values that the window will scroll gradually to
	private int targetPP;
	private String statusCondition;
	private double tickCount = 0;
	private double ticksPerFrame = 0.25;
	private int[] HPoffsetX = {0,0,0};
	private int[] PPoffsetX = {0,0,0};
	private char[] HPdigitsArray = {'0','0','0'};
	private char[] PPdigitsArray = {'0','0','0'};
	private double[] countersHP = {0,0,0};
	private double[] countersPP = {0,0,0};
	private boolean animatingHP;
	private boolean animatingPP;
	private int editingHP = 0; // 3 bits, 7 is all three
	private int editingPP = 0;
	private PCBattleEntity battleEntity;
	private Entity avatar;
	
	public void updateAnim() {
		super.updateAnim();
		if (targetY < y) {
			y-=8;
			avatar.moveOnScreen(0,-32);
		} else if (targetY > y) {
			y+=8;
			avatar.moveOnScreen(0,32);
		}
		tickCount += ticksPerFrame;
		if (tickCount % 1 == 0 && !animatingHP) {
			if (targetHP != HP) {
				if (targetHP > HP) {
					HP++;
				} else {
					HP--;
				}
				countersHP[2] = 0;
				editingHP = 0;
				animatingHP = true;
			}
		}
		if (tickCount % 1 == 0 && !animatingPP) {
			if (targetPP != PP) {
				if (targetPP > PP) {
					PP++;
				} else {
					PP--;
				}
				countersPP[2] = 0;
				editingPP = 0;
				animatingPP = true;
			}
		}
	}
	
	public PlayerStatusWindow(BattleEntity be, int x, int y,StartupNew state) {
		super(be.getName(),x,y,0,0,state);
		battleEntity = (PCBattleEntity) be;
		avatar = state.allEntities.get(battleEntity.getId().toLowerCase()).createCopy(x+96,y-15,96,128,"copy");
		avatar.setXYOnScreen(x+80,y);
		textName = new Text(true,be.getName(),((int)this.x+64),((int)this.y+64),100,100,state.charList);
		this.name = be.getName();
		this.HP = be.getStats().getStat("CURHP");
		this.PP = be.getStats().getStat("CURPP");
		String digits = HP + "";
		while (digits.length() < 3) {
			digits = "0" + digits;
		}
		HPdigitsArray = digits.toCharArray();
		digits = PP + "";
		while (digits.length() < 3) {
			digits = "0" + digits;
		}
		PPdigitsArray = digits.toCharArray();
		targetHP = HP;
		targetPP = PP;
		this.x = x;
		this.y = y;
	}
	
	public Entity getEntity() {
		return avatar;
	}
	
	public int getHP() {
		return HP;
		
	}
	
	public void updateStatus(int hp, int pp) {
		this.targetHP = hp;
		this.targetPP = pp;
	}
	
	public void drawWindow(MainWindow m) {
		m.renderTile(x,y,60*4,59*4,224,0,60,59);
	}
	
//	public void drawWindow(MainWindow m) {
//		//shouldDrawAll,s,x+TEXT_START_X,y+TEXT_START_Y,this.width,this.height,m.charLis
//		
//		int step = 8*4;
//		int save = 0;
//		int overallY = 0;
//		m.renderTile(this.x, drawingY, 32,32, 0,0,8,8);
//		for (int x = step; x < width-step; x+=step) {
//			m.renderTile(this.x + x, drawingY, 32,32, 8,0,8,8);
//			save = x + step;
//		}
//		m.renderTile(this.x + save, drawingY + overallY, 32,32, 16,0,8,8);
//		overallY+=step;
//		
//		for (int y = step; y <= height-step; y+=step) {
//			m.renderTile(this.x, drawingY + y,32,32,0,8,8,8);
//			for (int x = step; x < width; x+=step) {
//				m.renderTile(this.x+x, drawingY+y, 32,32,8,8,8,8);
//				save = x;
//			}
//			m.renderTile(this.x + save, drawingY + y, 32,32, 16,8,8,8);
//			overallY+=step;
//		}
//		
//		m.renderTile(this.x,drawingY + overallY,32,32,0,16,8,8);
//		for (int x = step; x < width-step; x += step) {
//			m.renderTile(this.x+x, drawingY + overallY,32,32,8,16,8,8);
//		}
//		m.renderTile(this.x+save,drawingY + overallY,32,32,16,16,8,8);
//	}
	
	public void drawDigit(MainWindow m,int digit, int PPoffsetX, double x, double y) {
		int coordX = 0;
		int coordY = 0;
		switch (digit) {
			case 0: coordX = 0 + PPoffsetX; coordY = 72; break; 
			case 1: coordX = 72+ PPoffsetX; coordY = 72; break;
			case 2: coordX = 144+ PPoffsetX; coordY = 72; break;
			case 3: coordX = 216+ PPoffsetX; coordY = 72; break;
			case 4: coordX = 288+ PPoffsetX; coordY = 72; break;
			case 5: coordX = 360+ PPoffsetX; coordY = 72; break;
			case 6: coordX = 432+ PPoffsetX; coordY = 72; break;
			case 7: coordX = 504+ PPoffsetX; coordY = 72; break;
			case 8: coordX = 576+ PPoffsetX; coordY = 72; break;
			case 9: coordX = 648+ PPoffsetX; coordY = 72; break;
		}
		m.renderTile(x,y,8*4,8*4,coordX,coordY,8,8);
	}
	
//	public void drawHPIndicator(MainWindow m) {
//		m.renderTile(this.x+40,this.drawingY+96,16*4,16*4,32,0,16,15);
//	}
//	
//	public void drawPPIndicator(MainWindow m) {
//		m.renderTile(this.x+40,this.drawingY+96+14*4,16*4,16*4,48,0,16,15);
//	}
	
	
	public void drawHealth(MainWindow m) {	
		String digits = String.valueOf(HP);
		while (digits.length() < 3) {
			digits = "0" + digits;
		}
		char[] oldDigitsArray = HPdigitsArray;
		HPdigitsArray = digits.toCharArray();
		double x = this.x + (29*4);
		double y = this.drawingY + (24*4);
		if (editingHP == 0) {
			for (int i = 0; i < HPdigitsArray.length; i++) {
				char c;
				if((c = HPdigitsArray[i]) != oldDigitsArray[i]) {
					if (i == 0) {
						editingHP |= 1;
					}
					if (i == 1) {
						editingHP |= 2;
					}
					if (i == 2) {
						editingHP |= 4;
					}
				}
			}
		}
		
		for (int i = 0; i < HPdigitsArray.length; i++) {
			char c = HPdigitsArray[i];
			if (((editingHP & (1 << i)) == (1 << i))) {
				if (c != oldDigitsArray[i] || animatingHP) {
					//scroll the digit to the value
					if (countersHP[i] % 8 == 0) {
						HPoffsetX[i] = 63;
					} else if (countersHP[i] % 8 == 1 ) {
						HPoffsetX[i] = 54;
					} else if (countersHP[i] % 8 == 2 ) {
						HPoffsetX[i] = 45;
					} else if (countersHP[i] % 8 == 3 ) {
						HPoffsetX[i] = 36;
					} else if (countersHP[i] % 8 == 4) {
						HPoffsetX[i] = 27;
					} else if (countersHP[i] % 8 == 5) {
						HPoffsetX[i] = 18;
					} else if (countersHP[i] % 8 == 6) {
						HPoffsetX[i] = 9;
					} else if (countersHP[i] % 8 == 7) {
						HPoffsetX[i] = 0;
						editingHP = 0;
						animatingHP = false;
					}
					if (battleEntity.getDefending()) {
						countersHP[i]+=0.5;
					} else {
						countersHP[i]+=1;
					}
				}
				if (animatingHP) {
//					if (battleEntity.getDefending()) {
//						countersHP[0] += 0.5;
//						countersHP[1] += 0.5;
//						countersHP[2] += 0.5;
//					} else {
//						countersHP[0]+=1;
//						countersHP[1]+=1;
//						countersHP[2]+=1;
//					}
				}
				if (targetHP > HP) {
					HPoffsetX[i] *= -1;
				}
			}
			
			drawDigit(m,Integer.parseInt(String.valueOf(c)),HPoffsetX[i], x,y);
			x += 8*4;
		}
	}
	
	public void drawPP(MainWindow m) {	
		String digits = String.valueOf(PP);
		while (digits.length() < 3) {
			digits = "0" + digits;
		}
		char[] oldDigitsArray = PPdigitsArray;
		PPdigitsArray = digits.toCharArray();
		double x = this.x + (29*4);
		double y = this.drawingY + (34*4);
		if (editingPP == 0) {
			for (int i = 0; i < PPdigitsArray.length; i++) {
				char c;
				if((c = PPdigitsArray[i]) != oldDigitsArray[i]) {
					if (i == 0) {
						editingPP |= 1;
					}
					if (i == 1) {
						editingPP |= 2;
					}
					if (i == 2) {
						editingPP |= 4;
					}
				}
			}
		}
		
		for (int i = 0; i < PPdigitsArray.length; i++) {
			char c = PPdigitsArray[i];
			if (((editingPP & (1 << i)) == (1 << i))) {
				if (c != oldDigitsArray[i] || animatingPP) {
					//scroll the digit to the value
					if (countersPP[i] % 8 == 0) {
						PPoffsetX[i] = 63;
					} else if (countersPP[i] % 8 == 1 ) {
						PPoffsetX[i] = 54;
					} else if (countersPP[i] % 8 == 2 ) {
						PPoffsetX[i] = 45;
					} else if (countersPP[i] % 8 == 3 ) {
						PPoffsetX[i] = 36;
					} else if (countersPP[i] % 8 == 4) {
						PPoffsetX[i] = 27;
					} else if (countersPP[i] % 8 == 5) {
						PPoffsetX[i] = 18;
					} else if (countersPP[i] % 8 == 6) {
						PPoffsetX[i] = 9;
					} else if (countersPP[i] % 8 == 7) {
						PPoffsetX[i] = 0;
						editingPP = 0;
						animatingPP = false;
					}
					if (battleEntity.getDefending()) {
						countersPP[i]+=0.5;
					} else {
						countersPP[i]+=1;
					}
				}
				if (animatingPP) {
//					if (battleEntity.getDefending()) {
//						countersPP[0] += 0.5;
//						countersPP[1] += 0.5;
//						countersPP[2] += 0.5;
//					} else {
//						countersPP[0]+=1;
//						countersPP[1]+=1;
//						countersPP[2]+=1;
//					}
				}
				if (targetPP > PP) {
					PPoffsetX[i] *= -1;
				}
			}
			
			drawDigit(m,Integer.parseInt(String.valueOf(c)),PPoffsetX[i], x,y);
			x += 8*4;
		}
	}
	
//	public void drawPP(MainWindow m) {
//		int pp = PP;
////		drawPPIndicator(m);
//		String digits = String.valueOf(pp);
//		while (digits.length() < 3) {
//			digits = "0" + digits;
//		}
//		char[] digitsArray = digits.toCharArray();
//		double x = this.x + 29*4;
//		double y = this.drawingY + 34*4;
//		for (char c : digitsArray) {
//			drawDigit(m,Integer.parseInt(String.valueOf(c)),0, x,y);
//			x += 8*4;
//		}
//	}
	
	public void draw(MainWindow m) {
		avatar.draw(m);
		m.setTexture("img\\battlehud.png");
		drawWindow(m);
		Text.initDrawText(m);
		textName.setX(this.x+64);
		textName.setY(this.y+32);
		textName.draw(m);
		m.setTexture("img\\battlehud.png");
		drawHealth(m);
		drawPP(m);
	}

	public void stopScrolling() {
		// TODO Auto-generated method stub
		targetHP = Math.max(HP,1);
		targetPP = PP;
	}
	
}
