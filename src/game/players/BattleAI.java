package game.players;

import java.awt.Color;

import game.AI;
import game.Game;

public class BattleAI extends AI {
	
	public BattleAI(String name, Color color) {
		super(name,color);
	}
	
	@Override
	protected void actions(Game game) throws InterruptedException {
		if (game.getRound() == 1) {
			
		} else {
			
		}
	}

}
