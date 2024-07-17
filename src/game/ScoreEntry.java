package game;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Diese Klasse stellt einen Eintrag in der Bestenliste dar.
 * Sie enthält den Namen des Spielers, das Datum, die erreichte Punktzahl sowie den Spieltypen.
 */
public class ScoreEntry implements Comparable<ScoreEntry> {

    private String name;
    private Date date;
    private int score;
    private String gameType;

    /**
     * Erzeugt ein neues ScoreEntry-Objekt
     * @param name der Name des Spielers
     * @param score die erreichte Punktzahl
     * @param date das Datum
     * @param gameGoal der Spieltyp
     */
    private ScoreEntry(String name, int score, Date date, String gameGoal) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.gameType = gameGoal;
    }

    /**
     * Erzeugt ein neues ScoreEntry-Objekt
     * @param player der Spieler
     * @param gameGoal der Spieltyp
     */
    public ScoreEntry(Player player, Goal gameGoal) {
        this.name = player.getName();
        this.score = player.getPoints();
        this.date = new Date();
        this.gameType = gameGoal.getName();
    }

    @Override
    public int compareTo(ScoreEntry scoreEntry) {
        return Integer.compare(this.score, scoreEntry.score);
    }

    /**
     * Schreibt den Eintrag als neue Zeile mit dem gegebenen {@link PrintWriter}
     * Der Eintrag sollte im richtigen Format gespeichert werden.
     * @see #read(String)
     * @see Date#getTime()
     * @param printWriter der PrintWriter, mit dem der Eintrag geschrieben wird
     */
    public void write(PrintWriter printWriter) {
        // TODO: ScoreEntry#write(PrintWriter)
    	long help = date.getTime();
    	String d = help + "";
    	String p = Integer.toString(score);
    	
    	printWriter.write(name + ";" + d + ";" + p + ";" + gameType);	
    }

    /**
     * List eine gegebene Zeile ein und wandelt dies in ein ScoreEntry-Objekt um.
     * Ist das Format der Zeile ungültig oder enthält es ungültige Daten, wird null zurückgegeben.
     * Eine gültige Zeile enthält in der Reihenfolge durch Semikolon getrennt:
     *    den Namen, das Datum als Unix-Timestamp (in Millisekunden), die erreichte Punktzahl, den Spieltypen
     * Gültig wäre beispielsweise: "Florian;1546947397000;100;Eroberung"
     *
     *
     * @see String#split(String)
     * @see Long#parseLong(String)
     * @see Integer#parseInt(String)
     * @see Date#Date(long)
     *
     * @param line Die zu lesende Zeile
     * @return Ein ScoreEntry-Objekt oder null
     */
    public static ScoreEntry read(String line) {
        // TODO: ScoreEntry#read(String)
    	String [] Format = line.split(";");
    	String Name = Format[0];
    	long help = Long.parseLong(Format[1]);
    	String MilliString = ( new Long(help).toString());
    	String aktuell = ( new Long( System.currentTimeMillis()).toString());
    	Date Datum = new Date ( help);
    	String PunkteString = Format[2];
    	int Punktzahl = Integer.parseInt(Format[2]);
    	String Mission = Format[3];
    	if ( Format.length != 4)
    		return null;
    	if ( Format [0].contains("/n") || Format[0].contains(":") || Format[0].contains(","))// Namen Pr�fung
    		return null;
    	if (MilliString.length() != aktuell.length() )// Millisekunden L�nge
    		return null;
    	for ( int i = 0; i < MilliString.length(); i++) {
    		if ( Character.isLetter(MilliString.charAt(i))) // Millisekunde darf keine Buchstaben enthalten
    			return null;}
    	if ( Punktzahl < 0 )
    		return null;
    	if ( PunkteString.charAt(PunkteString.length()-1) != '0' && PunkteString.charAt(PunkteString.length() - 1) != '5') // letzte Ziffer muss 5 oder 0 sein
    		return null;
    	for ( int i = 0; i < PunkteString.length(); i++) // Punktezahl darf keine Buchstaben enthalten 
    		{if ( Character.isLetter(PunkteString.charAt(i)))
    			return null;}
    	if ( !Mission.equals("Eroberung") && ! Mission.equals("Heerf�hrer") && ! Mission.equals("Punktelimit") && ! Mission.equals("Monopol") )
    		return null;
    	
    	else {
    	return new ScoreEntry( Name, Punktzahl , Datum , Mission );
    }}

    public Date getDate() {
        return date;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public String getMode() {
        return this.gameType;
    }
}
