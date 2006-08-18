/*
 * Created 26. April 2005 / 00:13:05
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.prozessor.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.BasisSonderregelInterface;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.Sonderregel;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
/**
 * <b>Beschreibung:</b><br> 
 * Dient der Verwaltung der Sonderregel. Registriert alle aktiven Sonderregel und 
 * dient als eine zentrale Schnittstelle die die Methodenaufrufe an alle aktiven
 * Sonderregeln weiterleitet.
 * 
 * @author V.Strelow
 */
public class SonderregelAdmin implements BasisSonderregelInterface  {
    
    /** <code>SonderregelAdmin</code>'s logger */
    private static final Logger LOG = Logger.getLogger(SonderregelAdmin.class.getName());
    
    private Held held;
	private final HashMap<Link, Sonderregel> sonderRegelMap;
	private Iterator<Sonderregel> iterator;
	//private ArrayList<SonderregelAdapter> sonderregeln;
	
	public SonderregelAdmin(Held held) {
		this.held = held;
		sonderRegelMap = new HashMap<Link, Sonderregel>();
	}
	
	
	/**
	 * Fügt eine Sonderregel zu Admin hinzu. Diese Sonderregel wird nach 
	 * dem Hinzufügen bei jedem Methodenaufruf des Admins mit beachtet
	 * 
	 * (Wenn ein neues Element zu Held hinzugefügt wurde, dass eine Sonderregel 
	 * besitzt, dann wir die Sonderregel mit dieser Methode hinzugefügt.)
	 * 
	 * @param sRegel Die neue Sonderregel
	 * @param link Der Link zu dem die Sonderregel gehört
	 */
	public void addSonderregel(Link link) {
		// TODO implement
		// Es sollte eine Sortierung vorgenommen werden, damit überprüft werden kann
		// in welcher Reichenfolge Sonderregeln ausgeführt wurden / werden!
		// ACHTUNG Es können mehrer Links die gleiche Sonderregel besitzen!
		
		if ( sonderRegelMap.containsKey(link) ) {
			LOG.warning("Dieser Link ist bereits im SonderregelAmdin registriert!");
		}
		
		if (link.getZiel().hasSonderregel()) {
			Sonderregel sr = link.getZiel().createSonderregel();
			sonderRegelMap.put(link, sr ); // SR zum Admin hinzufügen
			sr.initSonderregel(held, link); // SR initialisieren
		}
	}
	
	/**
	 * @return Liefert die Anzahl der aktiven Sonderregeln des Helden
	 */
	public int countSonderregeln() {
		return sonderRegelMap.size();
	}
	
	/**
	 * Überprüft ob diese Sonderregel bereits vorhanden ist. Text und zweitZiel sind
	 * bei bestimmten SR nötig, da es diese SR in verschiedenen Varianten gibt 
	 * (z.B. "Herausragende Eigenschaft" mit "KK" und mit "MU"). Ansonsten 
	 * können diese Parameter null sein.
	 * @param id  Die Id der SR die überprüft wird
	 * @param text Der Text der SR die überprüft wird (kann null sein)
	 * @param zweitZiel Das ZweitZiel der SR, die überprüft wird (kann null sein)
	 * @return true - Die SR ist im SR-Admin enthalten, ansonsten false
	 */
	public boolean hasSonderregel(String id, String text, CharElement zweitZiel) {
		iterator = sonderRegelMap.values().iterator();
		
		while (iterator.hasNext()) {
			if ( iterator.next().isSonderregel(id, text, zweitZiel) ) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Liefert die aktuelle Sonderegel zurück mit der entsprechenden ID, oder 
	 * null falls keine entsprechnende SR vorhanden ist. Text und zweitZiel sind
	 * bei bestimmten SR nötig, da es diese SR in verschiedenen Varianten gibt 
	 * (z.B. "Herausragende Eigenschaft" mit "KK" und mit "MU"). Ansonsten 
	 * können diese Parameter null sein.
	 * @param id ID der gesuchten SR
	 * @param text Der Text der SR die gesucht wird (kann null sein)
	 * @param zweitZiel Das ZweitZiel der SR, die gesucht wird (kann null sein)
	 * @return Die SR aus dem SrAdmin mit der entsprechenden ID, oder null
	 * 		falls es eine solche SR nicht gibt.
	 */
	public Sonderregel getSonderregel(String id, String text, CharElement zweitZiel) {
		iterator = sonderRegelMap.values().iterator();
		Sonderregel sonderR;
		
		while (iterator.hasNext()) {
			sonderR = iterator.next();
			
			if ( sonderR.isSonderregel(id, text, zweitZiel) ) { 
				return sonderR;
			}
		}
		
		return null;
	}
	
	/**
	 * Entfernd eine Sonderregel aus dem Admin. Diese Sonderregel wird nicht
	 * mehr bei Methodenaufrufen mit beachtet.
	 * 
	 * (Wenn ein Element vom Held entfernd wird, dann wird die Sonderregel 
	 * mit dieser Methode entfernd)
	 * 
	 * @param sRegel Die Sonderregel die entfernd wird
	 * @param link Der Link zu dem die Sonderregel gehört
	 */
	public void removeSonderregel(Link link) {
		final Sonderregel sRegel = sonderRegelMap.get(link); 
		
		sonderRegelMap.remove(link); // SR entfernen
		sRegel.finalizeSonderregel(link); // SR finalizer aufrufen
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#processAddAsNewElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void processAddAsNewElement(Link link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			iterator.next().processAddAsNewElement(link);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.BasisSonderregelInterface#processBeforAddAsNewElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void processBeforAddAsNewElement(CharElement element) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			iterator.next().processBeforAddAsNewElement(element);
		}	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#processRemoveElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void processRemoveElement(Link link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			iterator.next().processRemoveElement(link);
		}
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeKostenKlasse(org.d3s.alricg.prozessor.FormelSammlung.KostenKlasse, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public KostenKlasse changeKostenKlasse(KostenKlasse klasse, Link link)  {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			klasse = iterator.next().changeKostenKlasse(klasse, link);
		}
		return klasse;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeKosten(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int changeKosten(int kosten, Link link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			kosten = iterator.next().changeKosten(kosten, link);
		}
		return kosten;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeMaxStufe(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int changeMaxWert(int maxStufe, Link link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			maxStufe = iterator.next().changeMaxWert(maxStufe, link);
		}
		return maxStufe;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeMinStufe(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int changeMinWert(int minStufe, Link link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			minStufe = iterator.next().changeMinWert(minStufe, link);
		}
		return minStufe;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeCanAddElement(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean changeCanAddElement(boolean ok, Link link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			ok = iterator.next().changeCanAddElement(ok, link);
		}
		return ok;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeCanRemoveElemet(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean changeCanRemoveElement(boolean canRemove, Link link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			canRemove = iterator.next().changeCanRemoveElement(canRemove, link);
		}
		return canRemove;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#processUpdateElement(org.d3s.alricg.held.HeldenLink, int, java.lang.String, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void processUpdateElement(HeldenLink link, int stufe, String text, CharElement zweitZiel) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			iterator.next().processUpdateElement(link, stufe, text, zweitZiel);
		}
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeCanUpdateStufe(boolean, org.d3s.alricg.held.HeldenLink)
	 */
	public boolean changeCanUpdateWert(boolean canUpdate, HeldenLink link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			canUpdate = iterator.next().changeCanUpdateWert(canUpdate, link);
		}
		return canUpdate;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeCanUpdateText(boolean, org.d3s.alricg.held.HeldenLink)
	 */
	public boolean changeCanUpdateText(boolean canUpdate, HeldenLink link) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			canUpdate = iterator.next().changeCanUpdateText(canUpdate, link);
		}
		return canUpdate;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeCanUpdateZweitZiel(boolean, org.d3s.alricg.held.HeldenLink)
	 */
	public boolean changeCanUpdateZweitZiel(boolean canUpdate, HeldenLink link, CharElement charElem) {
		iterator = sonderRegelMap.values().iterator();
		
		// Alle Sonderregeln durchlaufen und entsprechend aufrufen
		while (iterator.hasNext()) {
			canUpdate = iterator.next().changeCanUpdateZweitZiel(canUpdate, link, charElem);
		}
		return canUpdate;
	}
	
}
