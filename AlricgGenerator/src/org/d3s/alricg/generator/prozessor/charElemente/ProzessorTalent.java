/*
 * Created on 04.06.2006 / 13:10:17
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.Notepad;
import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorTalent;
import org.d3s.alricg.generator.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Talent.Art;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;
import org.d3s.alricg.store.rules.RegelConfig;

/**
 * <u>Beschreibung:</u><br> 
 * Prozessor f�r das Bearbeiten von Talenten. Alle �nderungen die an Talenten
 * durchgef�hrt werden, werden �ber diesen Prozessor durchgef�hrt.
 * 
 * @author V. Strelow
 */
public class ProzessorTalent extends BaseProzessorElementBox<Talent, GeneratorLink> 
							 implements GeneratorProzessor<Talent, GeneratorLink>, ExtendedProzessorTalent
{
	
	// Texte f�r Notepade-Meldungen
	private final String TEXT_GESAMT_KOSTEN = "Gesamt Kosten: ";
	private final String TEXT_MODIS = "Modis auf Stufe: ";
	private final String TEXT_AKTIVIEREN = "muss aktiviert werden";
	private final String TEXT_SKT_SPALTE = "Original SKT-Spalte: ";
	private final String TEXT_KEINE_AKTIVIERUNG = "Keine Talent-aktivierungen mehr m�glich";
	private final String TEXT_BESITZT_MODIS = "Talent besitzt Modifikationen";
	private final String TEXT_BASIS_TALENT = "Talent ist Basis-Talent.";
	
	protected boolean STUFE_ERHALTEN = true;
	private int maxTalentAktivierungen = RegelConfig.getInstance().getMaxTalentAktivierung();
	
	private final Charakter held;
	
	// Speicher alle Talente die aktiviert wurden
	private ArrayList<Talent> aktivierteTalente = new ArrayList<Talent>();
	
	/* H�lt alle Talente nach den Eigenschaften sortiert, auf die die Probe
	 * Abgelegt wird (wichtig f�r schnellen Zugriff bei berechnung der Min-Werte
	 * bei Eigenschaften) */ 
	private HashMap<EigenschaftEnum, ArrayList<GeneratorLink>> hashMapNachEigensch;
	
	// Die gesamtkosten f�r alle Talente
	private int talentGpKosten = 0;
	
	public ProzessorTalent(Charakter held) {
		this.held = held;
		this.elementBox = new ElementBox<GeneratorLink>();
		
		aktivierteTalente = new ArrayList<Talent>();
		hashMapNachEigensch = new HashMap<EigenschaftEnum, ArrayList<GeneratorLink>>();
		
		// Initialisieren der HashMap
		hashMapNachEigensch.put(EigenschaftEnum.MU, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.KL, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.IN, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.CH, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.FF, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.GE, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.KO, new ArrayList<GeneratorLink>());
		hashMapNachEigensch.put(EigenschaftEnum.KK, new ArrayList<GeneratorLink>());
		
		// Basis Talente laden
	   	final Iterator<Talent> ite =  StoreDataAccessor.getInstance().getTalentList().iterator();
    	while(ite.hasNext()) {
    		final Talent tmpTal = ite.next();
    		if (tmpTal.getArt() == Art.basis) {
    			elementBox.add(new GeneratorLink(tmpTal, null, null, 0));
    		}
    	}
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Talent ziel) {
		//Link wird erstellt und zur Liste hinzugef�gt
		GeneratorLink tmpLink = new GeneratorLink(ziel, null, null, 0);
		registerTalent(tmpLink);
		
		// Pr�fen ob Talent akiviert werden mu�
		pruefeTalentAktivierung(tmpLink);
		updateKosten(tmpLink); // Kosten Aktualisieren
		
		return tmpLink;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public HeldenLink addModi(IdLink link) {
		GeneratorLink tmpLink;
		int oldWert;
		
		tmpLink = elementBox.getObjectById(link.getZiel());
		if (tmpLink == null) {
			tmpLink = new GeneratorLink(link);
			registerTalent(tmpLink);
		} else {
			if (STUFE_ERHALTEN && tmpLink.getWert() != 0) {
				oldWert = tmpLink.getWert(); // Alten Wert Speichern
				tmpLink.addLink(link); // Link hinzuf�gen
				tmpLink.setUserGesamtWert(oldWert); // Versuchen den alten Wert wiederherzustellen
			} else {
				tmpLink.addLink(link);
			}
		}
		
		ProzessorUtilities.inspectWert(tmpLink, this);
		
		// evtl. den Status als "aktiviertes Talent" entziehen
		pruefeTalentAktivierung(tmpLink);
		updateKosten(tmpLink); // Kosten Aktualisieren
		
		return tmpLink;
	}
	
	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Talent ziel) {
		if (aktivierteTalente.size() >= maxTalentAktivierungen) {
			// Mu� aktiviert werden, aber dies ist nicht mehr m�glich!
			Notepad.writeMessage(TEXT_KEINE_AKTIVIERUNG);
			return false;
		}
		
		if (elementBox.getObjectById(ziel.getId()) != null) {
			// So ein Element ist schon vorhanden, geht also nicht!
			return false;
		} 
		
		return true;
	}
	


	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		// Gepr�ft wird nur anhand der ID, andere Merkmale wie "text"
		// z�hlen bei Talenten nicht
		return (elementBox.getObjectById(link.getZiel().getId()) != null);
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		return canAddElement((Talent) link.getZiel());
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canRemoveElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canRemoveElement(GeneratorLink element) {

		if (element.getLinkModiList().size() > 0) {
			Notepad.writeMessage(TEXT_BESITZT_MODIS);
			return false; // Das Talent besitzt Modis, kann deswegen nicht entfernt werden!
		} else if (((Talent) element.getZiel()).getArt().equals(Talent.Art.basis) ) {
			Notepad.writeMessage(TEXT_BASIS_TALENT);
			return false; // Basis Talente k�nnen nicht entfernt werden!
		}
		
		// Voraussetzungen und Sonderregeln werden �bergeordnet gepr�ft
		return true;
	}

	/* (non-Javadoc) Methode �berschrieben
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

	/* (non-Javadoc) Methode �berschrieben
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
		for (int i = 0; i < tmpTalent.getDreiEigenschaften().length; i++) {
			if ( hashMapNachEigensch
							.get(tmpTalent.getDreiEigenschaften()[i].getEigenschaftEnum())
							.contains(element) ) {
					hashMapNachEigensch
							.get(tmpTalent.getDreiEigenschaften()[i].getEigenschaftEnum())
							.remove(element);
			}
		}
		
		// Kosten f�r dieses Element von den Talent Gesamtkosten abziehen
		talentGpKosten -= element.getKosten();
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateStufe(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
		// Grunds�tzlich kann der Wert eines Talents ge�ndert werden
		// Der Wert selbst wird nicht gepr�ft, sondern durch Max/Min vorgegeben! 
		return true;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateText(GeneratorLink link) {
		
		if (link.getLinkModiList() == null) return true;
		
		// Pr�fen ob automatisches Talent
		for (IdLink modiLink : ((List<IdLink>) link.getLinkModiList())) {
			if (modiLink.getQuelle() instanceof Fertigkeit) {
				// Dies ist eine automatisches Talent, welches �ber eine Fertigkeit 
				// hinzugef�gt wurde. => Keine Spezialisierung m�glich
				return false;
			}
		}
		
		return true;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// ZweitZiel gibt es bei Talenten nicht!
		return false;
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
		final Eigenschaft[] eigenschaftAR;
		int maxWert = 0;
		
		// Die 3 Eigeschaften holen
		eigenschaftAR = ((Talent) link.getZiel()).getDreiEigenschaften();
		
		// H�chste Eigenschaft Bestimmen
		for (int i = 0; i < eigenschaftAR.length; i++) {
			maxWert = Math.max(maxWert,
						held.getEigenschaftsWert(
							eigenschaftAR[i].getEigenschaftEnum()
						)
				);
		};
		
		// H�chste Eigenschaft +3 / Siehe MFF S. 45
		return (maxWert + 3);
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		// Der niedrigeste Wert ist stehts "0", es sei den es gibt Modis (auch negativ)
		return ((GeneratorLink) link).getWertModis();
	}

	/* (non-Javadoc) Methode �berschrieben
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

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateText(org.d3s.alricg.charKomponenten.links.Link, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		//if (text == null) return;
		// TODO Kosten f�r Sonderf berechnen?
		link.setText(text);
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(org.d3s.alricg.charKomponenten.links.Link, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Noop - ZweitZiel gibt es bei Talenten nicht!
	}
	
	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public double getGesamtKosten() {
		return talentGpKosten;
	}
	
	/**
	 * Berechnet die Kosten die f�r dieses Element aufgewendet werden m�ssen neu.
	 * @param link Der Link zu dem Element, f�r das die Kosten berechnet werden
	 */
	public void updateKosten(GeneratorLink genLink) {
		final Talent tmpTalent;
		KostenKlasse kKlasse;
		int kosten;
		int vonStufe;
		
		// Alte Kosten abziehen
		talentGpKosten -= genLink.getKosten();

		// Bestimme das Talent
		tmpTalent = (Talent) genLink.getZiel();
		
		// Bestimme die Kostenklasse
		kKlasse = tmpTalent.getKostenKlasse();
		Notepad.writeMessage(TEXT_SKT_SPALTE + kKlasse.getValue());
		
		// Evtl. �nderungen der Kostenklasse (notepade wird innerhalb gesetzt)
		kKlasse = held.getSonderregelAdmin().changeKostenKlasse(kKlasse, genLink);
		
		// Aktivieren oder nicht? Bei negativen modis mu� das Talent auch aktiviert werden
		// und die "vonStufe" ist der negative Modi!
		if ( aktivierteTalente.contains(tmpTalent) && genLink.getWertModis() >= 0) {
			vonStufe = CharElement.KEIN_WERT;
			Notepad.writeMessage(TEXT_AKTIVIEREN);
		} else {
			vonStufe = genLink.getWertModis();
			Notepad.writeMessage(TEXT_MODIS + vonStufe);
		}
		
		// Errechne die Kosten
		kosten = FormelSammlung.berechneSktKosten(
				vonStufe, // von Stufe (KEIN_WERT falls aktiviert)
				genLink.getWert(), // bis Stufe
				kKlasse // in dieser Kostenklasse
		);
		
		Notepad.writeMessage(TEXT_GESAMT_KOSTEN + kosten);
		
		// �ndere die Kosten durch Sonderregeln oder Verbilligungen durch Herkunft
		kosten = (int) held.getSonderregelAdmin().changeKosten(new Double(kosten), genLink);
		kosten = held.getVerbFertigkeitenAdmin().changeKostenAP(kosten, genLink);
		
		// Setze die Kosten
		genLink.setKosten(kosten);
		
		// Gesamt-Kosten f�r alle Talente setzen
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
	
	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.ExtendedProzessorTalent#getAktivierteTalente()
	 */
	public List<Talent> getAktivierteTalente() {
		return Collections.unmodifiableList(aktivierteTalente);
	}

	/* (non-Javadoc) Methode �berschrieben
	 * @see org.d3s.alricg.prozessor.ExtendedProzessorTalent#isAktiviert(org.d3s.alricg.charKomponenten.Talent)
	 */
	public boolean isAktiviert(Talent tal) {
		return aktivierteTalente.contains(tal);
	}
	
	/* (non-Javadoc) Methode �berschrieben
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
	  * Aktiviert ein Talent wenn n�tig, bzw. entfernd es von der Liste der 
	  * zu aktivierenden Talente.
	  */
	public void pruefeTalentAktivierung(GeneratorLink genLink) {
		/* Das Talent ist genau dann aktiviert, wenn:
		 * - Es kein Basis Talent ist
		 * - Es keine Modis gibt
		 * - Die Modis negativ sind, die Stufe aber >= 0 ist
		 */ 
		
		if ( ((Talent) genLink.getZiel()).getArt().equals(Talent.Art.basis) ) {
			// Basis Talente k�nnen nie aktiviert (und somit auch nicht deaktiviert) werden
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

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorTalent#getMaxTalentAktivierung()
	 */
	@Override
	public int getMaxTalentAktivierung() {
		return maxTalentAktivierungen;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public ExtendedProzessorTalent getExtendedInterface() {
		return this;
	}
	
	/**
	 * Erstellt einen neuen GeneratorLink und f�hgt diesen zur ElementBox hinzu
	 */
	private void registerTalent(GeneratorLink tmpLink) {
		Talent ziel = (Talent) tmpLink.getZiel();

		elementBox.add(tmpLink);
		
		// F�gt den Link zu den entsprechenden HashMaps hinzu
		for (int i = 0; i < ziel.getDreiEigenschaften().length; i++) {
			// Falls das Talente mehrmals auf die selbe Eigenschaft geprobt wird, 
			// wird es nur einmal hinzugef�ght
			if ( !hashMapNachEigensch
							.get(ziel.getDreiEigenschaften()[i].getEigenschaftEnum())
							.contains(tmpLink) ) 
			{
					hashMapNachEigensch
								.get(ziel.getDreiEigenschaften()[i].getEigenschaftEnum())
								.add(tmpLink);
			}
		}
	}
}
