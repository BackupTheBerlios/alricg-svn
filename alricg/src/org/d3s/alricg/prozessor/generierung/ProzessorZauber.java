/*
 * Created on 24.06.2006 / 22:37:42
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung;

import java.util.List;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.controller.Notepad;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.BaseProzessorElementBox;
import org.d3s.alricg.prozessor.LinkProzessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.common.SonderregelAdmin;
import org.d3s.alricg.prozessor.common.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.prozessor.elementBox.ElementBoxLink;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorZauber;

/**
 * <u>Beschreibung:</u><br> 
 *
 * @author V. Strelow
 */
public class ProzessorZauber extends BaseProzessorElementBox<Zauber, GeneratorLink> 
							 implements LinkProzessor<Zauber, GeneratorLink>, ExtendedProzessorZauber
{
	private final SonderregelAdmin sonderregelAdmin;
	private final VerbilligteFertigkeitAdmin verbFertigkeitenAdmin;
	private final Notepad notepade;
	private final Held held;
	
	public ProzessorZauber(Held held, Notepad notepade) {
		this.sonderregelAdmin = held.getSonderregelAdmin();
		this.verbFertigkeitenAdmin = held.getVerbFertigkeitenAdmin();
		this.held = held;
		this.notepade = notepade;
		this.elementBox = new ElementBoxLink<GeneratorLink>();
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public HeldenLink addModi(IdLink element) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		// TODO Auto-generated method stub
		
		/* 
		 * Es werden hier nur allgemeingültige Voraussetzungen überprüft. Also nichts
		 * was von einem Bestimmten Element abhängt. Sonderregel und Element spezifische
		 * Voraussetzungen werden vom LinkProzessorFront überprüft.
		 */
		
		/*
		 * Hier sollte mit Hilfe des "this.notepade" Objekts protokoliert werden, wie
		 * der errechnete Wert zustande kommt. Der User kann dies dann später abrufen.
		 * (Siehe z.B. im TalentProzessor zum Verständnis)
		 */
		
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(E)
	 */
	public boolean canUpdateText(GeneratorLink link) {
		// TODO Auto-generated method stub
		
		/* 
		 * Es werden hier nur allgemeingültige Voraussetzungen überprüft. Also nichts
		 * was von einem Bestimmten Element abhängt. Sonderregel und Element spezifische
		 * Voraussetzungen werden vom LinkProzessorFront überprüft.
		 */
		
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateWert(E)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
		// TODO Auto-generated method stub
		
		/* 
		 * Es werden hier nur allgemeingültige Voraussetzungen überprüft. Also nichts
		 * was von einem Bestimmten Element abhängt. Sonderregel und Element spezifische
		 * Voraussetzungen werden vom LinkProzessorFront überprüft.
		 */
		
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		
		/* 
		 * Es werden hier nur allgemeingültige Voraussetzungen überprüft. Also nichts
		 * was von einem Bestimmten Element abhängt. Sonderregel und Element spezifische
		 * Voraussetzungen werden vom LinkProzessorFront überprüft.
		 */
		
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public int getGesamtKosten() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(E, org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateKosten(E)
	 */
	public void updateKosten(GeneratorLink Link) {
		// TODO Auto-generated method stub
		
		/*
		 * Hier sollte mit Hilfe des "this.notepade" Objekts protokoliert werden, wie
		 * der errechnete Wert zustande kommt. Der User kann dies dann später abrufen.
		 * (Siehe z.B. im TalentProzessor zum Verständnis)
		 */
		
		/*
		 * Dies ist die einzige Methode (im Normalfall) wo das Objekt "this.sonderregelAdmin" 
		 * eingesetzt werden muß. Bei allen anderen Methoden übernimmt dies der "LinkProzessorFront".
		 * Bei dieser Methode ist dies jedoch nicht möglich - also aufpassen!
		 */
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateText(E, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateWert(E, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Zauber ziel) {
		// TODO Auto-generated method stub
		
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Zauber ziel) {
		// TODO Auto-generated method stub
		
		/* 
		 * Es werden hier nur allgemeingültige Voraussetzungen überprüft. Also nichts
		 * was von einem Bestimmten Element abhängt. Sonderregel und Element spezifische
		 * Voraussetzungen werden vom LinkProzessorFront überprüft.
		 */
		
		/*
		 * Hier sollte mit Hilfe des "this.notepade" Objekts protokoliert werden, wie
		 * der errechnete Wert zustande kommt. Der User kann dies dann später abrufen.
		 * (Siehe z.B. im TalentProzessor zum Verständnis)
		 */
		
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		
		/* 
		 * Es werden hier nur allgemeingültige Voraussetzungen überprüft. Also nichts
		 * was von einem Bestimmten Element abhängt. Sonderregel und Element spezifische
		 * Voraussetzungen werden vom LinkProzessorFront überprüft.
		 */
		/*
		 * Hier sollte mit Hilfe des "this.notepade" Objekts protokoliert werden, wie
		 * der errechnete Wert zustande kommt. Der User kann dies dann später abrufen.
		 * (Siehe z.B. im TalentProzessor zum Verständnis)
		 */
		
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.generierung.ExtendedProzessorZauber#getZauberList(org.d3s.alricg.charKomponenten.EigenschaftEnum)
	 */
	public List< ? extends HeldenLink> getZauberList(EigenschaftEnum eigEnum) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
