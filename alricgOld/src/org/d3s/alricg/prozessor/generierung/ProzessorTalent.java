/*
 * Created on 04.06.2006 / 13:10:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Talent;
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
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <u>Beschreibung:</u><br> 
 * Prozessor für das Bearbeiten von Talenten. Alle Änderungen die an Talenten
 * durchgeführt werden, werden über diesen Prozessor durchgeführt.
 * 
 * @author V. Strelow
 */
public class ProzessorTalent extends BaseProzessorElementBox<Talent, GeneratorLink> 
							 implements LinkProzessor<Talent, GeneratorLink>, ExtendedProzessorTalent
{
	private static final Logger LOG = Logger.getLogger(ProzessorTalent.class.getName());
	
	// Texte für Notepade-Meldungen
	private final String TEXT_GESAMT_KOSTEN = "Gesamt Kosten: ";
	private final String TEXT_MODIS = "Modis auf Stufe: ";
	private final String TEXT_AKTIVIEREN = "muss aktiviert werden";
	private final String TEXT_SKT_SPALTE = "Original SKT-Spalte: ";
	private final String TEXT_KEINE_AKTIVIERUNG = "Keine Talent-aktivierungen mehr möglich";
	private final String TEXT_BESITZT_MODIS = "Talent besitzt Modifikationen";
	private final String TEXT_BASIS_TALENT = "Talent ist Basis-Talent.";
	
	protected boolean STUFE_ERHALTEN = true;
	protected int maxTalentAktivierungen = 5;
	
	private final SonderregelAdmin sonderregelAdmin;
	private final VerbilligteFertigkeitAdmin verbFertigkeitenAdmin;
	private final Notepad notepade;
	private final Held held;
	
	// Speicher alle Talente die aktiviert wurden
	private ArrayList<Talent> aktivierteTalente = new ArrayList<Talent>();
	
	/* Hält alle Talente nach den Eigenschaften sortiert, auf die die Probe
	 * Abgelegt wird (wichtig für schnellen Zugriff bei berechnung der Min-Werte
	 * bei Eigenschaften) */ 
	private HashMap<EigenschaftEnum, ArrayList<GeneratorLink>> hashMapNachEigensch;
	
	// Die gesamtkosten für alle Talente
	private int talentGpKosten = 0;

	
	public ProzessorTalent(Held held, Notepad notepade) {
		this.sonderregelAdmin = held.getSonderregelAdmin();
		this.held = held;
		this.notepade = notepade;
		this.verbFertigkeitenAdmin = held.getVerbFertigkeitenAdmin();
		this.elementBox = new ElementBoxLink<GeneratorLink>();
		
		aktivierteTalente = new ArrayList<Talent>();

		// Initialisieren der HashMap
		hashMapNachEigensch = new HashMap<EigenschaftEnum, ArrayList<GeneratorLink>>();
		
		hashMapNachEigensch.put(EigenschaftEnum.MU, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.KL, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.IN, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.CH, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.FF, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.GE, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.KO, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.KK, new ArrayList<GeneratorLink>());
	}
	
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Talent ziel) {
		final GeneratorLink tmpLink;
		
		//Link wird erstellt und zur List hinzugefügt
		tmpLink = new GeneratorLink(ziel, null, null, 0);
		elementBox.add(tmpLink);
		
		// Fügt den Link zu den entsprechenden HashMaps hinzu
		for (int i = 0; i < ziel.get3Eigenschaften().length; i++) {
			// Falls das Talente mehrmals auf die selbe Eigenschaft geprobt wird, 
			// wird es nur einmal hinzugefüght
			if ( !hashMapNachEigensch
							.get(ziel.get3Eigenschaften()[i].getEigenschaftEnum())
							.contains(tmpLink) ) 
			{
					hashMapNachEigensch
								.get(ziel.get3Eigenschaften()[i].getEigenschaftEnum())
								.add(tmpLink);
			}
		}
		
		// Prüfen ob Talent akiviert werden muß
		pruefeTalentAktivierung(tmpLink);
		
		updateKosten(tmpLink); // Kosten Aktualisieren
		
		return tmpLink;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Talent ziel) {
		if (aktivierteTalente.size() >= maxTalentAktivierungen) {
			// Muß aktiviert werden, aber dies ist nicht mehr möglich!
			notepade.writeMessage(TEXT_KEINE_AKTIVIERUNG);
			return false;
		}
		
		if (elementBox.getObjectById(ziel.getId()) != null) {
			// So ein Element ist schon vorhanden, geht also nicht!
			return false;
		} 
		
		return true;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public HeldenLink addModi(IdLink link) {
		GeneratorLink tmpLink;
		int oldWert;
		
		tmpLink = elementBox.getObjectById(link.getZiel().getId());
		
		// Das Element muß vorhanden sein!
		assert tmpLink != null;
		
		if (STUFE_ERHALTEN && tmpLink.getWert() != 0) {
			oldWert = tmpLink.getWert(); // Alten Wert Speichern
			tmpLink.addLink(link); // Link hinzufügen
			tmpLink.setUserGesamtWert(oldWert); // Versuchen den alten Wert wiederherzustellen
		} else {
			tmpLink.addLink(link);
		}
		
		ProzessorUtilities.inspectWert(tmpLink, this);
		
		// evtl. den Status als "aktiviertes Talent" entziehen
		pruefeTalentAktivierung(tmpLink);
		
		updateKosten(tmpLink); // Kosten Aktualisieren
		
		return tmpLink;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// Geprüft wird nur anhand der ID, andere Merkmale wie "text"
		// zählen bei Talenten nicht
		return (elementBox.getObjectById(link.getZiel().getId()) != null);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		return canAddElement((Talent) link.getZiel());
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canRemoveElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canRemoveElement(GeneratorLink element) {

		if (element.getLinkModiList().size() > 0) {
			notepade.writeMessage(TEXT_BESITZT_MODIS);
			return false; // Das Talent besitzt Modis, kann deswegen nicht entfernt werden!
		} else if (((Talent) element.getZiel()).getArt().equals(Talent.Art.basis) ) {
			notepade.writeMessage(TEXT_BASIS_TALENT);
			return false; // Basis Talente können nicht entfernt werden!
		}
		
		// Voraussetzungen und Sonderregeln werden übergeordnet geprüft
		return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void removeModi(GeneratorLink link, IdLink element) {
		
		// Link entfernen
		link.removeLink(element);
		
		// Stufe ggf. neu setzen
		ProzessorUtilities.inspectWert(link, this);

		// Die aktivierung des Talents ggf. neu setzen.
		pruefeTalentAktivierung(link);
		
		// Kosten aktualisieren
		updateKosten(link);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeUserElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void removeElement(GeneratorLink element) {
		final Talent tmpTalent = (Talent) element.getZiel();
		
		// evtl. den Status als "aktiviertes Talent" entziehen
		if ( aktivierteTalente.contains(tmpTalent) ) {
			aktivierteTalente.remove(tmpTalent);
		}
		
		// Entfernen aus der Liste
		elementBox.remove(element);

		// Entfernd den Link aus der entsprechenden HashMap
		for (int i = 0; i < tmpTalent.get3Eigenschaften().length; i++) {
			if ( hashMapNachEigensch
							.get(tmpTalent.get3Eigenschaften()[i].getEigenschaftEnum())
							.contains(element) ) {
					hashMapNachEigensch
							.get(tmpTalent.get3Eigenschaften()[i].getEigenschaftEnum())
							.remove(element);
			}
		}
		
		// Kosten für dieses Element von den Talent Gesamtkosten abziehen
		talentGpKosten -= element.getKosten();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateStufe(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
		// Grundsätzlich kann der Wert eines Talents geändert werden
		// Der Wert selbst wird nicht geprüft, sondern durch Max/Min vorgegeben! 
		return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateText(GeneratorLink link) {
		
		/*
		
		// Kann nur geändert werden, wenn schon Text (= Eine SF) besteht!)
		if (link.hasText()) {
			return true;
		}
		*/
		
		return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// ZweitZiel gibt es bei Talenten nicht!
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		final Eigenschaft[] eigenschaftAR;
		int maxWert = 0;
		
		// Die 3 Eigeschaften holen
		eigenschaftAR = ((Talent) link.getZiel()).get3Eigenschaften();
		
		// Höchste Eigenschaft Bestimmen
		for (int i = 0; i < eigenschaftAR.length; i++) {
			maxWert = Math.max(maxWert,
						held.getEigenschaftsWert(
							eigenschaftAR[i].getEigenschaftEnum()
						)
				);
		};
		
		// Höchste Eigenschaft +3 / Siehe MFF S. 45
		return (maxWert + 3);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		// Der niedrigeste Wert ist stehts "0", es sei den es gibt Modis (auch negativ)
		return ((GeneratorLink) link).getWertModis();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateWert(org.d3s.alricg.charKomponenten.links.Link, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
		// Updaten der Stufe
		if (wert != Link.KEIN_WERT) {
			link.setUserGesamtWert(wert);
		}
		
		pruefeTalentAktivierung(link);
		updateKosten(link);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateText(org.d3s.alricg.charKomponenten.links.Link, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		if (text == null) return;
		link.setText(text);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(org.d3s.alricg.charKomponenten.links.Link, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Noop - ZweitZiel gibt es bei Talenten nicht!
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public int getGesamtKosten() {
		return talentGpKosten;
	}
	
	/**
	 * Berechnet die Kosten die für dieses Element aufgewendet werden müssen neu.
	 * @param link Der Link zu dem Element, für das die Kosten berechnet werden
	 */
	public void updateKosten(GeneratorLink genLink) {
		final Talent tmpTalent;
		KostenKlasse kKlasse;
		int kosten, vonStufe;
		
		// Alte Kosten abziehen
		talentGpKosten -= genLink.getKosten();

		// Bestimme das Talent
		tmpTalent = (Talent) genLink.getZiel();
		
		// Bestimme die Kostenklasse
		kKlasse = tmpTalent.getKostenKlasse();
		notepade.writeMessage(TEXT_SKT_SPALTE + kKlasse.getValue());
		
		// Evtl. Änderungen der Kostenklasse (notepade wird innerhalb gesetzt)
		kKlasse = sonderregelAdmin.changeKostenKlasse(kKlasse, genLink);
		
		// Aktivieren oder nicht? Bei negativen modis muß das Talent auch aktiviert werden
		// und die "vonStufe" ist der negative Modi!
		if ( aktivierteTalente.contains(tmpTalent) && genLink.getWertModis() >= 0) {
			vonStufe = CharElement.KEIN_WERT;
			notepade.writeMessage(TEXT_AKTIVIEREN);
		} else {
			vonStufe = genLink.getWertModis();
			notepade.writeMessage(TEXT_MODIS + vonStufe);
		}
		
		// Errechne die Kosten
		kosten = FormelSammlung.berechneSktKosten(
				vonStufe, // von Stufe (KEIN_WERT falls aktiviert)
				genLink.getWert(), // bis Stufe
				kKlasse // in dieser Kostenklasse
		);
		
		notepade.writeMessage(TEXT_GESAMT_KOSTEN + kosten);
		
		// Ändere die Kosten durch Sonderregeln oder Verbilligungen durch Herkunft
		kosten = sonderregelAdmin.changeKosten(kosten, genLink);
		kosten = verbFertigkeitenAdmin.changeKostenAP(kosten, genLink);
		
		// Setze die Kosten
		genLink.setKosten(kosten);
		
		// Gesamt-Kosten für alle Talente setzen
		talentGpKosten += kosten;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateAllKosten()
	 */
	public void updateAllKosten() {
    	Iterator<GeneratorLink> ite = elementBox.getUnmodifiableIterator();
    	
    	while (ite.hasNext()) {
    		this.updateKosten(ite.next());
    	}
	}
	
// -------------- Extended Methoden -------------
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.ExtendedProzessorTalent#getAktivierteTalente()
	 */
	public List<Talent> getAktivierteTalente() {
		return Collections.unmodifiableList(aktivierteTalente);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.ExtendedProzessorTalent#isAktiviert(org.d3s.alricg.charKomponenten.Talent)
	 */
	public boolean isAktiviert(Talent tal) {
		return aktivierteTalente.contains(tal);
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.ExtendedProzessorTalent#getTalentList(org.d3s.alricg.charKomponenten.EigenschaftEnum)
	 */
	public List<GeneratorLink> getTalentList(EigenschaftEnum eigEnum) {
		List<GeneratorLink> list; 
		list = hashMapNachEigensch.get(eigEnum);
		
		if (list == null) {
			return new ArrayList<GeneratorLink>();
		}
		
		return Collections.unmodifiableList(list);
	}

	/**
	  * Aktiviert ein Talent wenn nötig, bzw. entfernd es von der Liste der 
	  * zu aktivierenden Talente.
	  */
	public void pruefeTalentAktivierung(GeneratorLink genLink) {
		/* Das Talent ist genau dann aktiviert, wenn:
		 * - Es kein Basis Talent ist
		 * - Es keine Modis gibt
		 * - Die Modis negativ sind, die Stufe aber >= 0 ist
		 */ 
		
		if ( ((Talent) genLink.getZiel()).getArt().equals(Talent.Art.basis) ) {
			// Basis Talente können nie aktiviert (und somit auch nicht deaktiviert) werden
			return;
		}
		
		// Den "aktiviert Status" des Talents setzen
		aktivierteTalente.remove((Talent) genLink.getZiel());
		if ( (genLink.getLinkModiList().size() == 0)
				|| (genLink.getWertModis() < 0 && genLink.getWert() >= 0)  ) 
		{
			aktivierteTalente.add((Talent) genLink.getZiel());
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent#setMaxTalentAktivierung(int)
	 */
	public void setMaxTalentAktivierung(int maxAktivierungen) {
		maxTalentAktivierungen = maxAktivierungen;
	}
	
}
