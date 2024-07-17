package joker;

import game.Player;

public class Joker {
	
	private boolean reroll = false;
	private boolean used = false;
	private boolean useNow = false;
	private Player owner = null;
	
	public Joker(Player owner) {
		this.owner = owner;
	}
	
	public boolean getReroll() {
		return this.reroll;
	}
	
	public boolean getUsed() {
		return this.used;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public void setReroll(boolean reroll) {
		this.reroll = reroll;
	}
	
	public boolean getUseNow() {
		return this.useNow;
	}
	
	public void setUseNow(boolean useNow) {
		this.useNow = useNow;
	}

}
