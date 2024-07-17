package game.goals;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;
import game.map.Kingdom;

public class MonopolGoal extends Goal  {
	
	public MonopolGoal () {
		super("Monopol", "In dieser Mission gewinnt der Spieler, der als erstes ein bestimmtes Königreich mit mindestens 2 Gruppen pro Burg besetzt.");
	}
	
	@Override
	public boolean isCompleted() {
        return this.getWinner() != null;
    }
	
	@Override
	public Player getWinner() {
		Game game = this.getGame();
		if (game.getRound() < 2)
			return null;
		
		Player p = null;
		for (Kingdom k : game.getMap().getKingdoms()) {
			p = k.getOwner();
			for (Castle c : k.getCastles()) {
				if (c.getTroopCount() < 2)
					break;
			}
			return p;
		}
		return null;
	}
	
	public boolean hasLost(Player player) {
		if (getGame().getRound() < 2)
			return false;
		
		return (getWinner() != player && getWinner() != null);
	}
	

}
