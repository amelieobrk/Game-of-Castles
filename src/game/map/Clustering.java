package game.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import game.map.GameMap;

/**
 * Diese Klasse teilt Burgen in Königreiche auf
 */
public class Clustering {

    private Random random;
    private final List<Castle> allCastles;
    private final int kingdomCount;
    private boolean equalCenters = false;

    /**
     * Ein neues Clustering-Objekt erzeugen.
     * @param castles Die Liste von Burgen, die aufgeteilt werden sollen
     * @param kingdomCount Die Anzahl von Königreichen die generiert werden sollen
     */
    public Clustering(List<Castle> castles, int kingdomCount) {
        if (kingdomCount < 2)
            throw new IllegalArgumentException("Ungültige Anzahl an Königreichen");

        this.random = new Random();
        this.kingdomCount = kingdomCount;
        this.allCastles = Collections.unmodifiableList(castles);
    }

    /**
     * Gibt eine Liste von Königreichen zurück.
     * Jedes Königreich sollte dabei einen Index im Bereich 0-5 bekommen, damit die Burg richtig angezeigt werden kann.
     * Siehe auch {@link Kingdom#getType()}
     */
    public List<Kingdom> getPointsClusters() {
        // TODO Clustering#getPointsClusters()
    	
    	LinkedList<Kingdom> r = new LinkedList<Kingdom>();
    	for (int i = 0; i < kingdomCount; i++) {
    		r.addLast(new Kingdom(i));
    	}
    	
    	double[] centerX = new double[kingdomCount];
    	double[] centerY = new double[kingdomCount];
    	centerX = random.doubles(kingdomCount).toArray();
    	centerY = random.doubles(kingdomCount).toArray();
    	
    	for (int i = 0; i < kingdomCount; i++) {
    		centerX[i] = centerX[i] * 50;
    		centerY[i] = centerY[i] * 50;
    	}
    	
    	LinkedList<Point> centers = new LinkedList<Point>();
    	for (int i = 0; i < kingdomCount ; i++) {
    		centers.addLast(new Point((int) centerX[i], (int) centerY[i]));
    	}
    	
    	while (!equalCenters) {
    		for (Castle c : allCastles) {
    			double[] distance = new double[kingdomCount];
    			for (int i = 0; i < kingdomCount; i++) {
    				distance[i] = c.distance(centers.get(i)); 
    			}
    			int kingdom = 0;
    			double min = distance[0];
    			for (int i = 1; i < kingdomCount; i++) {
    				if (distance[i] < min) {
    					min = distance[i];
    					kingdom = i;
    				}
    			}
    			c.setKingdom(r.get(kingdom));
    		}
    		LinkedList<Point> newCenters = new LinkedList<Point>();
    		for (int i = 0; i < kingdomCount; i++) {
    			LinkedList <Point> castlePoint = new LinkedList<Point>();
    			for (Castle c : allCastles) {
    				if (c.getKingdom() == r.get(i)) {
    					castlePoint.addLast(c.getLocationOnMap());
    				}
    			}
    			int helperX = 0;
    			int helperY = 0;
    			for (int j = 0; j < castlePoint.size(); j++) {
    				helperX += castlePoint.get(j).x;
    				helperY += castlePoint.get(j).y;	
    			}
    			double newX = (double) helperX / castlePoint.size();
    			double newY = (double) helperY / castlePoint.size();
    			newCenters.addLast(new Point((int) newX, (int) newY));
    		}
    		boolean finish = false;
    		Point[] a = new Point[kingdomCount];
			Point[] b = new Point[kingdomCount];
    		for (int i = 0; i < kingdomCount; i++) {
    			a[i] = centers.get(i);
    			b[i] = newCenters.get(i);
    			if (a[i].equals(b[i])) {
    				finish = true;
    			} else {
    				break;
    			}
    		}
    		if (finish) {
    			equalCenters = true;
    			for (int i = 0; i < kingdomCount; i++) {
    				for (Castle c : allCastles) {
    					if (c.getKingdom() == r.get(i)) {
    						r.get(i).addCastle(c);
    					}
    				}
    				r.get(i).setCentre(centers.get(i));
    			}
    		} else {
    			centers = new LinkedList<Point>();
    			for (int i = 0; i < newCenters.size(); i++) {
    				centers.addLast(newCenters.get(i));
    			}
    		}
    	}	
    	
    	return r;
    }
}
