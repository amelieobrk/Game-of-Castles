package game.map;

import base.*;
import game.GameConstants;
import gui.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Diese Klasse representiert das Spielfeld. Sie beinhaltet das Hintergrundbild, welches mit Perlin noise erzeugt wurde,
 * eine Liste mit Königreichen und alle Burgen und deren Verbindungen als Graphen.
 *
 * Die Karte wird in mehreren Schritten generiert, siehe dazu {@link #generateRandomMap(int, int, int, int, int)}
 */
public class GameMap {

    private BufferedImage backgroundImage;
    private Graph<Castle> castleGraph;
    private List<Kingdom> kingdoms;

    // Map Generation
    private double[][] noiseValues;
    private int width, height, scale;  

    /**
     * Erzeugt eine neue leere Karte. Der Konstruktor sollte niemals direkt aufgerufen werden.
     * Um eine neue Karte zu erstellen, muss {@link #generateRandomMap(int, int, int, int, int)} verwendet werden
     * @param width die Breite der Karte
     * @param height die Höhe der Karte
     * @param scale der Skalierungsfaktor
     */
    private GameMap(int width, int height, int scale) {
        this.castleGraph = new Graph<>();
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    /**
     * Wandelt einen Noise-Wert in eine Farbe um. Die Methode kann nach belieben angepasst werden
     * @param value der Perlin-Noise-Wert
     * @return die resultierende Farbe
     */
    private Color doubleToColor(double value) {
        if (value <= 0.40)
            return GameConstants.COLOR_WATER;
        else if (value <= 0.5)
            return GameConstants.COLOR_SAND;
        else if (value <= 0.7)
            return GameConstants.COLOR_GRASS;
        else if (value <= 0.8)
            return GameConstants.COLOR_STONE;
        else
            return GameConstants.COLOR_SNOW;
    }

    /**
     * Hier wird das Hintergrund-Bild mittels Perlin-Noise erzeugt.
     * Siehe auch: {@link PerlinNoise}
     */
    private void generateBackground() {
        PerlinNoise perlinNoise = new PerlinNoise(width, height, scale);
        Dimension realSize = perlinNoise.getRealSize();

        noiseValues = new double[realSize.width][realSize.height];
        backgroundImage = new BufferedImage(realSize.width, realSize.height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < realSize.width; x++) {
            for (int y = 0; y < realSize.height; y++) {
                double noiseValue = perlinNoise.getNoise(x, y);
                noiseValues[x][y] = noiseValue;
                backgroundImage.setRGB(x, y, doubleToColor(noiseValue).getRGB());
            }
        }
    }

    /**
     * Hier werden die Burgen erzeugt.
     * Dabei wir die Karte in Felder unterteilt, sodass auf jedes Fals maximal eine Burg kommt.
     * Sollte auf einem Feld keine Position für eine Burg existieren (z.B. aufgrund von Wasser oder angrenzenden Burgen), wird dieses übersprungen.
     * Dadurch kann es vorkommen, dass nicht alle Burgen generiert werden
     * @param castleCount die maximale Anzahl der zu generierenden Burgen
     */
    private void generateCastles(int castleCount) {
        double square = Math.ceil(Math.sqrt(castleCount));
        double length = width + height;

        int tilesX = (int) Math.max(1, (width / length + 0.5) * square) + 5;
        int tilesY = (int) Math.max(1, (height / length + 0.5) * square) + 5;
        int tileW = (width * scale / tilesX);
        int tileH = (height * scale / tilesY);

        if (tilesX * tilesY < castleCount) {
            throw new IllegalArgumentException(String.format("CALCULATION Error: tilesX=%d * tilesY=%d < castles=%d", tilesX, tilesY, castleCount));
        }

        // Add possible tiles
        List<Point> possibleFields = new ArrayList<>(tilesX * tilesY);
        for (int x = 0; x < tilesX - 1; x++) {
            for (int y = 0; y < tilesY - 1; y++) {
                possibleFields.add(new Point(x, y));
            }
        }

        // Generate castles
        List<String> possibleNames = generateCastleNames();
        int castlesGenerated = 0;
        while (possibleFields.size() > 0 && castlesGenerated < castleCount) {
            Point randomField = possibleFields.remove((int) (Math.random() * possibleFields.size()));
            int x0 = (int) ((randomField.x + 0.5) * tileW);
            int y0 = (int) ((randomField.y + 0.5) * tileH);

            for (int x = (int) (0.5 * tileW); x >= 0; x--) {
                boolean positionFound = false;
                for (int y = (int) (0.5 * tileH); y >= 0; y--) {
                    int x_mid = (int) (x0 + x + 0.5 * tileW);
                    int y_mid = (int) (y0 + y + 0.5 * tileH);
                    if (noiseValues[x_mid][y_mid] >= 0.6) {
                        String name = possibleNames.isEmpty() ? "Burg " + (castlesGenerated + 1) :
                            possibleNames.get((int) (Math.random() * possibleNames.size()));
                        Castle newCastle = new Castle(new Point(x0 + x, y0 + y), name);
                        boolean doesIntersect = false;

                        for (Castle r : castleGraph.getAllValues()) {
                            if (r.distance(newCastle) < Math.max(tileW, tileH)) {
                                doesIntersect = true;
                                break;
                            }
                        }

                        if (!doesIntersect) {
                            possibleNames.remove(name);
                            castleGraph.addNode(newCastle);
                            castlesGenerated++;
                            positionFound = true;
                            break;
                        }
                    }
                }

                if (positionFound)
                    break;
            }
        }
    }

    /**
     * Hier werden die Kanten erzeugt. Dazu werden zunächst alle Burgen durch eine Linie verbunden und anschließend
     * jede Burg mit allen anderen in einem bestimmten Radius nochmals verbunden
     */
    private void generateEdges() {
    	 // TODO: GameMap#generateEdges()
    	List<Node<Castle>> nodes = castleGraph.getNodes();
    	
    	int maxDistance = 0;
    	if (width == 15) 
    		maxDistance = 150;
    	else if (width == 27) 
    		maxDistance = 200;
    	else if (width == 48) 
    		maxDistance = 300;
    	
    	for (int i = 0; i < nodes.size(); i++) {
    		Castle a = nodes.get(i).getValue();
    		for (int j = i + 1; j < nodes.size() ; j++) {
    			Castle b = nodes.get(j).getValue();
    			if ( a.distance(b) < maxDistance && castleGraph.getEdges(nodes.get(i)).size()<3) {
    				castleGraph.addEdge(nodes.get(i), nodes.get(j));
    			}
    		}
    	}
    	for (int i = 0; i < nodes.size(); i++) {
    		if (castleGraph.getEdges(nodes.get(i)).size() == 0) {	
                    Castle a=nodes.get(i).getValue();
                    Node<Castle> b=nodes.get(i);
                    double min= -1;
                    for (int j=0; j<nodes.size();j++){
                        if (i !=j){
                             if (min == -1){
                             b=nodes.get(j);
                             min=a.distance(b.getValue());
                             }
                             else{
                                 if (min>a.distance(nodes.get(j).getValue())){
                                       min=a.distance(nodes.get(j).getValue());
                                       b=nodes.get(j);
                        }
                    }
                  }
               }
	            castleGraph.addEdge(nodes.get(i), b);
    		}
    	}
 
    	       for (int i=0; i<nodes.size();i++) {
                    if (castleGraph.getEdges(nodes.get(i)).size() ==1 ) {
                        Edge<Castle> E1= castleGraph.getEdges(nodes.get(i)).get(0);
                        Node<Castle> otherNode = E1.getOtherNode(nodes.get(i));
                        if (castleGraph.getEdges(otherNode).size() ==1) {
                           Castle a=nodes.get(i).getValue();
                           Node<Castle> b=nodes.get(i);
                           double min= -1;
                           for (int j=0; j<nodes.size();j++){
                               if (i !=j && nodes.get(j) !=otherNode ){
                                    if (min == -1){
                                    b=nodes.get(j);
                                     min=a.distance(b.getValue());
                                    }
                                    else{
                                        if (min>a.distance(nodes.get(j).getValue()) ){
                                              min=a.distance(nodes.get(j).getValue());
                   
                                              b=nodes.get(j);
                                        }
                                    }
                               }
                         }
                   castleGraph.addEdge(nodes.get(i), b);
                     }
              }
   }
                
                List<Node<Castle>> res = new ArrayList<Node<Castle>>();
                List<Edge<Castle>> edgeRes = new ArrayList<Edge<Castle>>();
                for (int i = 0; i < nodes.size(); i++) {
                	if (!res.contains(nodes.get(i))) {
                	boolean boucle=false;
                	List<Node<Castle>> newRes = new ArrayList<Node<Castle>>();
            		List<Edge<Castle>> a = castleGraph.getEdges(nodes.get(i));
            		int prev=0;
            		while (!boucle) {
            			prev =newRes.size();
            		for (Edge<Castle> e : a) {
            			if (!newRes.contains(e.getNodeA()))
            				newRes.add(e.getNodeA());
            			if(!newRes.contains(e.getNodeB()))
            				newRes.add(e.getNodeB());
            				}
            		for (int j=0; j<newRes.size();j++) {
            			List<Edge<Castle>> b = castleGraph.getEdges(newRes.get(j));
            			for (Edge<Castle> e1 : b) {
            				if (!a.contains(e1)) {
            					a.add(e1);
            				}
            			}
            		}
            		if ( prev==newRes.size() ) {
            			boucle=true;
                	}
            	}
            		for (Edge<Castle> e : a )
            			edgeRes.add(e);
            		for (Node<Castle> n :newRes)
            			res.add(n);
            		if (boucle && newRes.size()<nodes.size()) {
        		    int randomNum2=ThreadLocalRandom.current().nextInt(0, newRes.size() );
        		    Castle cas=nodes.get(randomNum2).getValue();
        		    Node<Castle> b=nodes.get(randomNum2);
        		    double min= -1;
        		    for (int k=0; k<nodes.size();k++) {
        		              if (!newRes.contains(nodes.get(k)) ){
                                  if (min == -1){
                                  b=nodes.get(k);
                                   min=cas.distance(b.getValue());
                                  }
                                  else{
                                      if (min>cas.distance(nodes.get(k).getValue()) ){
                                            min=cas.distance(nodes.get(k).getValue());
                 
                                            b=nodes.get(k);
                             }
        		            }
                          }
        		             
                	}
        		    castleGraph.addEdge(newRes.get(randomNum2), b);
                }
             }
          }
     }


    /**
     * Hier werden die Burgen in Königreiche unterteilt. Dazu wird der {@link Clustering} Algorithmus aufgerufen.
     * @param kingdomCount die Anzahl der zu generierenden Königreiche
     */
    private void generateKingdoms(int kingdomCount) {
        if(kingdomCount > 0 && kingdomCount < castleGraph.getAllValues().size()) {
            Clustering clustering = new Clustering(castleGraph.getAllValues(), kingdomCount);
            kingdoms = clustering.getPointsClusters();
        } else {
            kingdoms = new ArrayList<>();
        }
    }

    /**
     * Eine neue Spielfeldkarte generieren.
     * Dazu werden folgende Schritte abgearbeitet:
     *   1. Das Hintergrundbild generieren
     *   2. Burgen generieren
     *   3. Kanten hinzufügen
     *   4. Burgen in Köngireiche unterteilen
     * @param width die Breite des Spielfelds
     * @param height die Höhe des Spielfelds
     * @param scale die Skalierung
     * @param castleCount die maximale Anzahl an Burgen
     * @param kingdomCount die Anzahl der Königreiche
     * @return eine neue GameMap-Instanz
     */
    public static GameMap generateRandomMap(int width, int height, int scale, int castleCount, int kingdomCount) {

        width = Math.max(width, 15);
        height = Math.max(height, 10);

        if (scale <= 0 || castleCount <= 0)
            throw new IllegalArgumentException();

        System.out.println(String.format("Generating new map, castles=%d, width=%d, height=%d, kingdoms=%d", castleCount, width, height, kingdomCount));
        GameMap gameMap = new GameMap(width, height, scale);
        gameMap.generateBackground();
        gameMap.generateCastles(castleCount);
        gameMap.generateEdges();
        gameMap.generateKingdoms(kingdomCount);

        if(!gameMap.getGraph().allNodesConnected()) {
            System.out.println("Fehler bei der Verifikation: Es sind nicht alle Knoten miteinander verbunden!");
            return null;
        }

        return gameMap;
    }

    /**
     * Generiert eine Liste von Zufallsnamen für Burgen. Dabei wird ein Prefix (Schloss, Burg oder Festung) an einen
     * vorhandenen Namen aus den Resourcen angefügt. Siehe auch: {@link Resources#getcastleNames()}
     * @return eine Liste mit Zufallsnamen
     */
    private List<String> generateCastleNames() {
        String[] prefixes = {"Schloss", "Burg", "Festung"};
        List<String> names = Resources.getInstance().getCastleNames();
        List<String> nameList = new ArrayList<>(names.size());

        for (String name : names) {
            String prefix = prefixes[(int) (Math.random() * prefixes.length)];
            nameList.add(prefix + " " + name);
        }

        return nameList;
    }

    public int getWidth() {
        return this.backgroundImage.getWidth();
    }

    public int getHeight() {
        return this.backgroundImage.getHeight();
    }

    public BufferedImage getBackgroundImage() {
        return this.backgroundImage;
    }

    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public List<Castle> getCastles() {
        return castleGraph.getAllValues();
    }

    public Graph<Castle> getGraph() {
        return this.castleGraph;
    }

    public List<Edge<Castle>> getEdges() {
        return this.castleGraph.getEdges();
    }

    public List<Kingdom> getKingdoms() {
        return this.kingdoms;
    }
}
