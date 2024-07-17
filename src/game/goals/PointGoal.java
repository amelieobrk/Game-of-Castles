package game.goals;

import game.Game;
import game.Goal;
import game.Player;

public class PointGoal extends Goal {
	
	private final int winPoints = 500;
	
	public PointGoal() {
		super("Punktelimit", "Derjenige Spieler gewinnt, der zuerst 500 Punkte erreicht!");
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
			 if (p.getPoints() >= winPoints)
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
