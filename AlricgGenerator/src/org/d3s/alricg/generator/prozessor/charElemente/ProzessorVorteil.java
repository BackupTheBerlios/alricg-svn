/*
 * Created on 29.07.2006 / 14:25:40
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.Notepad;
import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.charakter.SonderregelAdmin;
import org.d3s.alricg.common.charakter.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorVorteil;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Talent.Art;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 * Prozessor für das Bearbeiten von Vorteilen. Alle Änderungen die an Vorteilen
 * durchgeführt werden, werden über diesen Prozessor durchgeführt.
 * 
 * Zu beachtene Unterschiede zum Talent-, Zauber-, und EigenschaftProzessor:
 * 	- Ein Vorteil kann ggf. auch mehrfach zum Charakter hinzugefügt werden. (z.B. "herausragender Sinn...")
 * 	- Vorteile können, müssen aber nicht: einen Wert, Text oder ein ZweitZiel besitzen.
 *  - Bestimmte Vorteile können nur von bestimmten CharArten gewählt werden (z.B. Zauberer, geweihte)
 *  - Die Änderungen der Kosten, auch durch Vorteile, wird im Prozessor nicht berechnet. Dies
 *  	übernimmt der "VerbilligteKostenAdmin"
 *   
 * @author Tobias Freudenreich
 */
public class ProzessorVorteil extends BaseProzessorElementBox<Vorteil, GeneratorLink> 
					implements GeneratorProzessor<Vorteil, GeneratorLink>, ExtendedProzessorVorteil {
	
	private final SonderregelAdmin sonderregelAdmin;
	private final VerbilligteFertigkeitAdmin verbFertigkeitenAdmin;
	private final Notepad notepade;
	private final Charakter held;
	
	// Texte für Notepade-Meldungen
	private final String TEXT_GESAMT_KOSTEN = "Gesamt Kosten: ";
	
	// Speichert die gewählten Vorteile
	private ArrayList<Vorteil> gewaehlteVorteile;
	
	// Die gesamtkosten für alle Vorteile
	private double gpKosten;
	
	public ProzessorVorteil(Charakter held, Notepad notepade) {
		this.held = held;
		this.notepade = notepade;
		this.sonderregelAdmin = held.getSonderregelAdmin();
		this.verbFertigkeitenAdmin = held.getVerbFertigkeitenAdmin();
		this.elementBox = new ElementBox<GeneratorLink>();
		
		gewaehlteVorteile = new ArrayList<Vorteil>();
		gpKosten = 0;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateText(LINK)
	 */
	public boolean canUpdateText(GeneratorLink link) {
		/*
		 * Ein Text kann dann gesetzt (und geupdated) werden, wenn 
		 * - Vorteil.hasFreienText = true 
		 * - oder Vorteil.textVorschlaege.length > 0
		 * (bzw. wenn der Link bereits einen text besitzt)
		 */
		Vorteil vorteilMerker = (Vorteil)link.getZiel();
		return (vorteilMerker.isMitFreienText()) || (vorteilMerker.getTextVorschlaege().length > 0);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateWert(LINK)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
		return link.getWert() > 0; // Vorteile mit Wert 0 haben immer Wert 0 (z.b. Vollzauberer)
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#canUpdateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO hängt noch davon ab wie ein link der Liste hinzugefügt wird
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// Hier reicht es nicht die ID zu überprüfen wie bei Talenten. 
		// Es muß mit "elementBox.contiansEqualObject" geprüft werden, um auch Text und ZweitZiel zu überprüfen
		
		return elementBox.contiansEqualObject(link);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateText(LINK, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		if (text != null) {
			link.setText(text);
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateWert(LINK, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
		// Vorteile haben entweder immer Wert 0 (dann ist der Wert nicht änderbar) oder irgendwas > 0
		if (wert > 0) { 
			link.setWert(wert);
			updateKosten(link);
		}		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		if (zweitZiel != null) {
			link.setZweitZiel(zweitZiel);
			//TODO evtl. noch updateKosten(link); aufrufen (z.b. bei Änderung von Begabung für Schwerter auf Begabung für Töpfern
		}
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public HeldenLink addModi(IdLink element) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		// TODO finish
		Vorteil vorteilMerker = (Vorteil)link.getZiel();
		
		//für den Char überhaupt erlaubt?
		//TODO enum anschauen
		/*vorteilMerker.getFuerWelcheChars().
		held.getCharArt();*/
		
		//Kollision mit anderen Vorteilen? (evtl. nicht notwendig, wenn z.B. gutaussehend/herausragendes Ausshen mit Stufen gemacht werden)
		
		//Kollision mit Nachteilen?
		//TODO nachfragen wie Nachteile abgerufen werden
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public double getGesamtKosten() {
		return gpKosten;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		// hier evtl. noch Überprüfung, ob wirklich ein Vorteil übergeben wurde
		return ((Vorteil)link.getZiel()).getMaxStufe();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		// hier evtl. noch Überprüfung, ob wirklich ein Vorteil übergeben wurde
		return ((Vorteil)link.getZiel()).getMinStufe();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(LINK, org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateAllKosten()
	 */
	public void updateAllKosten() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateKosten(LINK)
	 */
	public void updateKosten(GeneratorLink genLink) {
		double kosten = 0;
		Vorteil vorteilMerker = (Vorteil)genLink.getZiel();
		
		//alte Kosten abziehen
		gpKosten -= genLink.getKosten();
		
		//neue Kosten berechnen
		if (genLink.getWert() == 0) { //Vorteil hat keine Stufe
			kosten = vorteilMerker.getGpKosten();
		} else { //Vorteil hat eine Stufe
			kosten = vorteilMerker.getGpKosten()*genLink.getWert();
		}
		
		//Kosten eintragen
		genLink.setKosten(kosten);
		gpKosten += kosten;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Vorteil ziel) {
		/*
		 * Beim hinzufügen eines neuen Vorteils muß ein Link erzeugt werden. Hierfür müssen
		 * auch alle notwendigen Angaben des Links gemacht werden. Hierbei gilt folgendes:
		 * - ist "Vorteil.hasFreienText == true", wird der Text auf etwas wie "Bitte Text eingeben" gesetzt
		 * - ist "Vorteil.hasFreienText == false" und "textVorschlaege" ist gesetzt, wird der erste Eintrag 
		 * 		als Standard gesetzt
		 * - ist "Vorteil.getBenoetigtZweitZiel == true" wird ebenfalls das erste Element der Liste gesetzt
		 * - Die Stufe wird auf den minimalWert gesetzt
		 */
		
		String linkText = null;
		int stufe = 0;
		CharElement zweitZiel = null;
		
		if (ziel.isMitFreienText()) {
			linkText = "Bitte Text eingeben";
		} else if (!ziel.isMitFreienText() && (ziel.getTextVorschlaege() != null)) {
			linkText = ziel.getTextVorschlaege()[0];
		}
		
		if (ziel.getMinStufe() > 0) {
			stufe = ziel.getMinStufe();
		}
		
		//TODO zweitziel?? Bsp. Begabung für Schwerter
		
		if (ziel.getBenoetigtZweitZiel()) { 
			// TODO mail schreiben wo das erste Element der Liste steht
		}
		
		//Link wird erstellt und zur List hinzugefügt
		GeneratorLink tmpLink = new GeneratorLink(ziel, linkText, zweitZiel, stufe);
		elementBox.add(tmpLink);
		
		updateKosten(tmpLink);
		
		return tmpLink;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Vorteil ziel) {
		// Kosten sind hierbei egal. 
		// Elemente die sich gegenseitig ausschließen ("Gutaussehend", "Widerwärtiges Aussehen") werden
		// ebenfalls nicht überprüft
		
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(GeneratorLink element) {
		final Vorteil vorteilMerker = (Vorteil)element.getZiel();
		
		//Vorteil aus der Liste herausnehmen
		elementBox.remove(vorteilMerker);
		
		//Kosten für dieses Element abziehen
		gpKosten -= element.getKosten();
		
	}

	// -------------------- Methoden aus dem Extended Interface ---------------------------------
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil#addMoeglicheZweitZiele(org.d3s.alricg.charKomponenten.Vorteil, java.util.List)
	 */
	public void addMoeglicheZweitZiele(Vorteil vorteil, List<CharElement> zweitZiele) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil#getMoeglicheZweitZiele(org.d3s.alricg.charKomponenten.Vorteil)
	 */
	public List<CharElement> getMoeglicheZweitZiele(Vorteil vorteil) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil#removeMoeglicheZweitZiele(org.d3s.alricg.charKomponenten.Vorteil)
	 */
	public void removeMoeglicheZweitZiele(Vorteil vorteil) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public ExtendedProzessorVorteil getExtendedInterface() {
		return this;
	}
}
