package gamestate.elements;

import gamestate.BattleEntity;

public interface UsableInBattle {
	public int useInBattle(BattleEntity user, BattleEntity target);
}
