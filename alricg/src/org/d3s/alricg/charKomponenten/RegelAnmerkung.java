/*
 * Created 22. Dezember 2004 / 01:49:46
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Beschreibung:</b><br>
 * Zu jedem "CharElement" k�nnen besondere Regeln existieren, die NICHT
 * durch ALRICG automatisch beachtet werden, sondern von user beachtet
 * werden m�ssen. (Typischer weise Beschr�nkungen)
 * Diese werden durch ein Objekt von RegelAnmerkung repr�sentiert.
 * @author V.Strelow
 */
public class RegelAnmerkung {
    /**
     * REGEL - Etwas was der Benutzer beachten sollte, aber das Programm nicht automatisch machen kann
     * TOD0 - Etwas was der Benutzer beachten sollte, aber nicht mu� (z.B. sich einen Titel geben bei dem 
     *      Vorteil "Adlige Abstammung" 
     */
    public enum Modus { 
    	regel("regel"), 
    	todo("toDo");
    	
		private String value;
		
		private Modus(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
}
    
	private List<String> anmerkungen; // Array von Anmerkungen, gleicher index bei modus => Zugeh�rig
	private List<Modus> modi;

	/**
	 * Konstruktor
	 */
	public RegelAnmerkung() {
		anmerkungen = new ArrayList<String>(0);
		modi = new ArrayList<Modus>(0);
	}
	
	/**
	 * F�gt eine weitere Anmerkung hinzu!
	 * @param anmerkungIn Der Text der Regelanmerkung
	 * @param modusIn Der Modus, entweder "todo" oder "regel"
	 */
	public void add(String anmerkungIn, String modusIn) {
		
		// Pr�fen ob der Modus g�ltig ist:
		assert modusIn.equals(Modus.regel.getValue()) || modusIn.equals(Modus.todo.getValue());
		
		// Hinzuf�gen der Anmerkung
		anmerkungen.add(anmerkungIn);
		
		// Hinzuf�gen des Modus
		if (modusIn.equals("regel")) {
			modi.add(Modus.regel);
		} else { // modusIn.equals("toDo")
			modi.add(Modus.todo);
		}
	}
    
    public Modus[] getModi() {
        return modi.toArray(new Modus[0]);
    }
    
    public String[] getAnmerkungen() {
        return anmerkungen.toArray(new String[0]);
    }
	
}
