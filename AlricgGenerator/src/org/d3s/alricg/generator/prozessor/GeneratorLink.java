/*
 * Created 24. April 2005 / 10:44:27
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.generator.prozessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * <b>Beschreibung:</b><br>
 * Wird nur für Helden benutzt, die gerade generiert werden. Ein Generatorlink ist
 * eine Verbindung zwischen einem Held und einem CharElemente (bzw. mehrerer). 
 * Ein GeneratorLink kann mehrere IdLinks enthalten, und so wiederspiegel das ein 
 * Element sich aus mehreren Modifiationen durch Herkunft (Rasse, Kultur, Profession) und 
 * Vor-/Nachteilen (z.B. Herausragende Eigenschaft), sowie der Auswahl des Benutzers 
 * zusammensetzt. 
 * 
 * Es ist absichtlich wenig Logik enthalten, diese soll von der überliegenden Schicht
 * überprüft werden.
 * 
 * @author V.Strelow
 */
public class GeneratorLink<ZIEL extends CharElement> extends HeldenLink  {
       
	/* Überlegungen:
	 * - Es kann durch eine Herkunft auch mehrmalig ein Link hinzugefügt werden (z.B. regulär und
	 * 	durch eine Auswahl nochmals)
	 * - Es kann mehrer Rassen, Kulturen und Professionen geben (Kind zweier Welten, Breitgefächerte Bildung)
	 * - Durch den User kann nur einmal ein Wert dazukommen
	 */
	
	/* - Jeder IdLink enthält die Info von welcher Herkunft / Auswahl, diese muß hier dahern 
	 *   nicht nocheinmal gespeichert werden.
	 * - Die Grundwerte (zielId, linkID, text) aller IdLinks in einem Generatorlink sind
	 *   stehts gleich, leitwert und wert können  sich unterscheiden
	 */
	
	// Alle Links die der User "dazubekommen" hat. Also durch Herkunft, Vorteile, Nachteile, usw.
	private ArrayList<IdLink<ZIEL>> linkModiArray = new ArrayList<IdLink<ZIEL>>(3);
	
	// Vom Benutzer selbst hinzugefügter wert!
	private IdLink userLink;
	
	private double kosten; // Aktuelle Kosten
	
	/**
	 * Konstruktor. Erzeugt einen Generatorlink. An der Quelle des Links wird entschieden
	 * ob der hinzugefügt Link als Modi (link.quelle != null) oder als vom User gesetzt
	 * behandet wird (link.quelle == null).
	 * @param link Link auf dem dieser GeneratorLink basiert.
	 */
	public GeneratorLink(IdLink link) {
		// Setzen der Grundwerte
		this.setZiel(link.getZiel());
		this.setText(link.getText());
		this.setZweitZiel(link.getZweitZiel());
		super.setWert(link.getWert());

		addLink(link);
	}
	
	/**
	 * Konstruktor. Erzeugt einen Generatorlink. Aus den angegeben Werten wird ein
	 * Link erzeugt, der als von User gesetzt behandet wird.
	 * @param link Link auf dem dieser GeneratorLink basiert.
	 */
	public GeneratorLink(CharElement ziel, CharElement zweitZiel, String text, int wert) {
		final IdLink link;
		
		// Setzen der Grundwerte
		this.setZiel(ziel);
		this.setText(text);
		this.setZweitZiel(zweitZiel);
		super.setWert(wert);

		// Neuen Link erzeugen
		link = new IdLink(null, ziel, zweitZiel, wert, text);
		addLink(link);
	}
	
	/**
	 * Fügt einen Link zu diesem GeneratorLink hinzu.  Ist die Quelle = null, so
	 * wird davon ausgegagen das der User Link neu gesetzt wird, ansonsten (quelle != null)
	 * wird der Link als Modifikation zu diesem Link angesehen.
	 * 
	 * @param link Der Link der hinzugefügt werden soll.
	 */
	public void addLink(IdLink link) {
		if (link.getQuelle() != null) {
			linkModiArray.add(link);
		} else {
			userLink = link;
		}
		
		updateWert();
	}
	
	/**
	 * @param link Der Link der entfernt werden soll.
	 */
	public void removeLink(IdLink link) {
		
		if ( link.equals(userLink) ) {
			userLink = null;
		} else {
			linkModiArray.remove(link);
		}
		
		updateWert();
	}
	
	/**
	 * Liefert den (ersten) ModiLink, dessen Quelle gleich "quelle" ist.
	 * @param quelle Die Quelle des Links der gesucht wird.
	 * @return Den ModiLink oder "null", falls es keinen solchen gibt.
	 */
	public IdLink<ZIEL> getModiLinkByQuelle(CharElement quelle) {
		for (int i = 0; i < linkModiArray.size(); i++) {
			if ( linkModiArray.get(i).getQuelle().equals(quelle) ) {
				return linkModiArray.get(i);
			}
		}
		return null;
	}
	
    /**
     * @return Die aktuellen kosten für dieses Element (Od GP oder TalentGP ergibt 
     * sich aus dem Kontext)
     */
    public double getKosten() {
    	return this.kosten;
    }
    
    /**
     * @param kosten Die neuen gesamtkosten für dieses Element
     */
    public void setKosten(double kosten) {
    	this.kosten = kosten;
    }
    
    /**
     * Liefert eine Liste aller Links, AUSSER dem was der User selbst eingestellt hat
     * (dieses kann mit "getUserLink()" abgerufen werden. Es werde also alle links
     * durch Herkunft, Vorteilen, Nachteilen usw. abgerufen.
     *  
     * VORSICHT! Der Wert des GeneratorLinks kann sich von der Summe der hier
     * angegebenden Werte der IdLinks unterscheiden! (wegen "Breitgefächerte Bildung", 
     * "Veteran", usw.)
     * 
     * @return Eine Liste mit allen Links, durch die der Generatorlink modifiziert wird,
     * 	außer dem, was der User selbst gewählt hat!
     */
    public List<IdLink<ZIEL>> getLinkModiList() {
    	return Collections.unmodifiableList(linkModiArray);
    }
    
    /**
     * @return Der Teil dieses GeneratorLinks, der vom User hinzugefügt wurde (also nicht von
     * 		der Herkunft stammt). Gibt es keinen solchen Teil, wird "null" zurückgeliefert 
     * 		(was heißt das der gesamte Link nur aus Modis durch Herkunft besteht) 
     */
    public IdLink getUserLink() {
    	
    	return userLink;
    }
    
    /**
     * @return Die Stufe ohne die änderungen durch den User, nur die Modifikationen
     */
    public int getWertModis() {
    	
    	// Es gibt keinen Wert für diesen Link!
    	if (this.getWert() == KEIN_WERT) {
    		return KEIN_WERT;
    	}
    	
    	// Es gibt keinen vom User gewählten wert, Modis = gesamt-Stufe
    	if (userLink == null || userLink.getWert() == KEIN_WERT) {
    		return this.getWert();
    	}
    	
    	// Gesamt-Stufe abzüglich dessen, was der User gewählt hat 
    	return (this.getWert() - userLink.getWert());
    }
    
	/**
	 * Wird auf "setUserGesamtWert" umgemappt.
	 * @param wert Setzt das Attribut wert ("-100" bedeutet, das es keinen Wert gibt).
	 */
	public void setWert(int wert) {
		setUserGesamtWert(wert);
		/* throw new UnsupportedOperationException("setWert() kann nicht direkt " +
				"auf einen Generatorlink angewand werden!"); */
	}
    
	/**
	 * Hiermit wird der von User "gekaufte" Anteil eines links gesetzt, ohne rücksicht auf
	 * den Gesamtwert.
	 * 
	 * @param wert Der neue Wert der von User 
	 */
	public void setUserWert(int wert) {
		// Guard
		if (wert == KEIN_WERT) {
			// Der gewünschte Wert hat "keinen Wert" oder ist kleiner als möglich
			if (userLink != null) {
				removeLink(userLink);
			}
			return;
		}
		
		if (userLink == null) {
			// userLink existiert noch nicht und wird neu angelegt 
			userLink = new IdLink(null, getZiel(), getZweitZiel(), 0, getText());
		}
		
		// Wert setzen
		userLink.setWert(wert);
		
		updateWert(); // Damit der neue Wert auch übernommen wird
	}
	
	/**
	 * Hiermit wird der Wert des Elements neu gesetzt. Modifikationen
	 * durch die Herkunft u.ä. werden NICHT verändert, sondern nur der Teil, der
	 * durch den User gewählt wird. Falls ein Wert kleiner ist, als der durch die
	 * Modis vorgegebene Minimalwert, so wird der Minimalwert beibehalten.
	 * 
	 * @param wert Der gewünschte gesamtWert des Elements
	 */
	public void setUserGesamtWert(int wert) {
		
		// Guard
		if (wert == KEIN_WERT || (wert < this.getWertModis()) ) {
			// Der gewünschte Wert hat "keinen Wert" oder ist kleiner als möglich
			if (userLink != null) {
				removeLink(userLink);
			}
			updateWert(); // Damit der neue Wert auch übernommen wird
			return;
		}
		
		if (userLink == null) {
			// userLink existiert noch nicht und wird neu angelegt 
			userLink = new IdLink(null, getZiel(), getZweitZiel(), 0, getText());
		}
		
		// Wert setzen, so das der gewünschte Wert erreicht wird
		userLink.setWert(wert - this.getWertModis());
		
		updateWert(); // Damit der neue Wert auch übernommen wird
	}

	/**
	 * Bestimmt den Wert des Generator-Links neu, indem die Werte der einzelnen Teil-Links
	 * Addiert werden. Hat auch nur ein Wert den Wert "KEIN_WERT", dann hat gesamte
	 * GeneratorLink keinen Wert (Warum? : Weil es entweder alle Links einen Wert haben
	 * oder keiner)
	 */
	public void updateWert() {
		int tmpWert = 0;
		
		// Prüfung ob es überhaupt einen Wert git
		if (this.getWert() == KEIN_WERT) {
			return;
		}
		
		//	Prüfung des vom User gewählten Wertes
		if (userLink != null) {
			tmpWert = userLink.getWert();
		}
		
		// Addition der Modis durch Herkunft u.ä.
		for (int i = 0; i < linkModiArray.size(); i++) {
			tmpWert += linkModiArray.get(i).getWert();
    	}
		
		super.setWert(tmpWert); // Setzen des neuen Wertes
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.links.Link#isLeitwert()
	 */
	public boolean isLeitwert() {
		// Wenn auch nur einer der Links Leitwert ist, so ist der gesamte
		// GeneratorLink ein Leitwert!
		for (int i = 0; i < linkModiArray.size(); i++) {
    		if (linkModiArray.get(i).isLeitwert()) {
    			return true;
    		}
    	}
		if (userLink != null && userLink.isLeitwert()) {
			return true;
		}
		
		return false;
	}

}


