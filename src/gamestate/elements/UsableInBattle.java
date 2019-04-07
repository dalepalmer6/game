package gamestate.elements;

import gamestate.BattleEntity;

public interface UsableInBattle {
	public String useInBattle(BattleEntity user, BattleEntity target);
}
