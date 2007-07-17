/*
 * Created on 30.04.2005 / 23:02:43
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.store.charElemente.sonderregeln;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.elements.Held;
import org.d3s.alricg.store.elements.HeldenLink;
import org.d3s.alricg.store.elements.KostenKlasse;

/**
 * <u>Beschreibung:</u><br> 
 * Ein Adapter für Sonderregeln, die eine Standart-Implementierung aller Methoden enthält.
 * Nur ID und Beschreibung müssen sinnigerweise in jeder Sonderregel neu gesetzt werden.
 * Jede Sonderregel sollte von diesem Adapter abgeleitet werden und nur die Methoden 
 * überschreiben die für die entsprechende Sonderregel wichtig ist.
 * 
 * Für die Beschreibungen der Methoden siehe "SonderregelInterface".
 * 
 * @author V. Strelow
 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.Sonderregel
 */
public abstract class SonderregelAdapter extends CharElement implements Sonderregel {
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#canAddSelf(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddSelf(Held held, boolean ok, Link srLink) {
		return ok;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#initSonderregel(org.d3s.alricg.held.Held, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void initSonderregel(Held held, Link srLink) {
		// Noop!
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#finalizeSonderregel(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void finalizeSonderregel(Link link) {
		// Noop!
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * Diese Methode MUSS von jeder Sonderregel implementiert werden!
	 * @see org.d3s.alricg.charKomponenten.CharElement#getBeschreibung()
	 */
	public abstract String getBeschreibung();

	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeCanAddElement(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean changeCanAddElement(boolean canAdd, Link tmpLink) {
		return canAdd;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeCanRemoveElemet(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean changeCanRemoveElement(boolean canRemove, Link link) {
		return canRemove;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeCanUpdateStufe(boolean, org.d3s.alricg.held.HeldenLink)
	 */
	public boolean changeCanUpdateWert(boolean canUpdate, HeldenLink link) {
		return canUpdate;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeCanUpdateText(boolean, org.d3s.alricg.held.HeldenLink)
	 */
	public boolean changeCanUpdateText(boolean canUpdate, HeldenLink link) {
		return canUpdate;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeCanUpdateZweitZiel(boolean, org.d3s.alricg.held.HeldenLink)
	 */
	public boolean changeCanUpdateZweitZiel(boolean canUpdate, HeldenLink link, CharElement charElem) {
		return canUpdate;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeKosten(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int changeKosten(int kosten, Link link) {
		return kosten;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeKostenKlasse(org.d3s.alricg.prozessor.FormelSammlung.KostenKlasse, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public KostenKlasse changeKostenKlasse(KostenKlasse klasse, Link link) {
		return klasse;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeMaxStufe(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int changeMaxWert(int maxStufe, Link link) {
		return maxStufe;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#changeMinStufe(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int changeMinWert(int minStufe, Link link) {
		return minStufe;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.principle.BasisSonderregelInterface#processBeforAddAsNewElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void processBeforAddAsNewElement(CharElement element) {
		// Noop!
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#processAddAsNewElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void processAddAsNewElement(Link link) {
		// Noop!
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#processRemoveElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void processRemoveElement(Link link) {
		// Noop!
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#processUpdateElement(org.d3s.alricg.held.HeldenLink, int, java.lang.String, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void processUpdateElement(HeldenLink link, int stufe, String text, CharElement zweitZiel) {
		// Noop!
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#isForGenerierung()
	 */
	public boolean isForGenerierung() {
		return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#isForManagement()
	 */
	public boolean isForManagement() {
		return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * Standart implementation! Bei dieser implementation wird text zum zweitZiel 
	 * nicht beachtet, was für die meißten Sonderregel zutrifft. 
	 * Für alle anderen SR MUSS diese Methode überschrieben werden!
	 * 
	 * @see org.d3s.alricg.prozessor.sonderregeln.SonderregelInterface#isSonderregel()
	 */
	public boolean isSonderregel(String id, String text, CharElement zweitZiel) {
		return id.equals( this.getId() );
	}
}
