package gui;

import game.Game;
import game.Player;
import game.map.Castle;

public class AttackThread extends Thread {

    private Castle attackerCastle, defenderCastle;
    private Player attacker, defender;
    private int troopAttackCount;
    private Game game;
    private boolean fastForward;
    private Player winner;

    public AttackThread(Game game, Castle attackerCastle, Castle defenderCastle, int troopAttackCount) {
        this.attackerCastle = attackerCastle;
        this.defenderCastle = defenderCastle;
        this.attacker = attackerCastle.getOwner();
        this.defender = defenderCastle.getOwner();
        this.winner = defender;
        this.troopAttackCount = troopAttackCount;
        this.game = game;
        this.fastForward = false;
    }

    public void fastForward() {
        fastForward = true;
    }

    private void sleep(int ms) throws InterruptedException {
        long end = System.currentTimeMillis() + ms;
        while(System.currentTimeMillis() < end && !fastForward) {
            Thread.sleep(10);
        }
    }

    @Override
    public void run() {

        int attackUntil = Math.max(1, attackerCastle.getTroopCount() - troopAttackCount);

        try {
            sleep(1500);

            while(attackerCastle.getTroopCount() > attackUntil) {

                // Attacker dices: at maximum 3 and not more than actual troop count
                int attackerCount =  Math.min(troopAttackCount, Math.min(attackerCastle.getTroopCount() - 1, 3));
                int attackerDice[] = game.roll(attacker, attackerCount, fastForward);

                sleep(1500);

                // Defender dices: at maximum 2
                int defenderCount = Math.min(2, defenderCastle.getTroopCount());
                int defenderDice[] = game.roll(defender, defenderCount, fastForward);
                
                int helpDefender = defenderCastle.getTroopCount();
                int helpAttacker = attackerCastle.getTroopCount();

                game.doAttack(attackerCastle, defenderCastle, attackerDice, defenderDice);
                
                sleep(5000);
                
                // Modernisierung für Einsatz des Jokers!
                if(defenderCastle.getOwner() == attacker) {
                	if (defender.getJoker().getUseNow()) {
                		if (!defender.getJoker().getUsed()) {
                			
                			defenderCastle.setOwner(defender);
                			defenderCastle.addTroops(helpDefender);
                			attackerCastle.addTroops(helpAttacker - attackerCastle.getTroopCount());
                			
                			defenderDice = game.roll(defender, defenderCount, fastForward);
                			
                			game.doAttack(attackerCastle, defenderCastle, attackerDice, defenderDice);
                			
                			defender.getJoker().setUsed(true);
                		}
                	}
                	
                    winner = attacker;
                    break;
                } else {
                	if (attacker.getJoker().getUseNow()) {
                		if (!attacker.getJoker().getUsed()) {
                			
                			defenderCastle.addTroops(helpDefender - defenderCastle.getTroopCount());
                			attackerCastle.addTroops(helpAttacker - attackerCastle.getTroopCount());
                			
                			attackerDice = game.roll(defender, defenderCount, fastForward);
                			
                			game.doAttack(attackerCastle, defenderCastle, attackerDice, defenderDice);	
                			
                			attacker.getJoker().setUsed(true);
                		}
                	}
                	if (defenderCastle.getOwner() == attacker) {
                		winner = attacker;
                		break;
                		
                	}
                }

                sleep(1500);
            }
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }

        game.stopAttack();
    }

    public Player getWinner() {
        return winner;
    }
}
