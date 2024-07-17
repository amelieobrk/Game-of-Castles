package game.goals;

import game.Game;
import game.Goal;
import game.Player;

public class GeneralGoal extends Goal  {
	
	private final int maxTroops = 75; 
	
	public GeneralGoal() {
		super("Heerführer", "In dieser Mission gewinnt der Spieler, der zuerst eine Truppenstärke von 75 erreicht!");
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
		
		for (Player p : game.getPlayers()) {
			if (p.getRemainingTroops() >= maxTroops)
				return p;
		}
		
		return null;
	}
	
	@Override
	public boolean hasLost(Player player) {
		if (getGame().getRound() < 2)
			return false;
		
		return (getWinner() != player && getWinner() != null);
	}

}
