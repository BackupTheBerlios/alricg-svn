/*
 * Created on 29.07.2006 / 14:25:40
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorVorteil;
import org.d3s.alricg.generator.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
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
 * @author Tobias Freudenreich & Vincent
 */
public class ProzessorVorteil extends ProzessorFertigkeit<Vorteil> 
					implements GeneratorProzessor<Vorteil, GeneratorLink>, ExtendedProzessorVorteil {
	
	//private final Notepad notepade;
	protected boolean STUFE_ERHALTEN = true;

	// Die gesamtkosten für alle Vorteile
	private double gpKosten;
	
	public ProzessorVorteil(Charakter held) {
		super(held);
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
		Vorteil vorteilMerker = ((Vorteil)link.getZiel());
		return (vorteilMerker.isMitFreienText() 
			||  (vorteilMerker.getTextVorschlaege() != null && vorteilMerker.getTextVorschlaege().length > 0));
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
		if ( ((Vorteil)link.getZiel()).getBenoetigtZweitZiel() ) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateText(LINK, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		link.setText(text);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateWert(LINK, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
		// Vorteile haben entweder immer Wert 0 (dann ist der Wert nicht änderbar) oder irgendwas > 0
		link.setUserGesamtWert(wert);
		updateKosten(link);	
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.BaseLinkProzessor#updateZweitZiel(LINK, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		link.setZweitZiel(zweitZiel);
		updateKosten(link); // z.b. bei Änderung von Begabung für Schwerter auf Begabung für Töpfern
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	@Override
	public HeldenLink addModi(IdLink link) {
		// Kann eine Stufe besitzen, wie z.B. "Ausdauernd"
		GeneratorLink genLink;
		int oldWert;
		
		genLink = this.getEqualAdditionsFamilie((Vorteil) link.getZiel());
		if (genLink != null) {
			
			genLink.addLink(link);
			genLink.setZiel( ProzessorUtilities.getAdditionsFamilieErsetzung(genLink) );
			// evtl. frei werden GP werden bei update Kosten beachtet
			
		} else {
			List<GeneratorLink> list = elementBox.getEqualObjects(link);
			if (list.size() == 0) {
				genLink = new GeneratorLink(link);
				elementBox.add(genLink);
				createAutoTalent((Vorteil) link.getZiel());
			} else {
				genLink = list.get(0);
				
				if (STUFE_ERHALTEN && genLink.getWert() != 0 && genLink.getWert() != Link.KEIN_WERT) {
					oldWert = genLink.getWert(); // Alten Wert Speichern
					genLink.addLink(link); // Link hinzufügen
					genLink.setUserGesamtWert(oldWert); // Versuchen den alten Wert wiederherzustellen
				} else {
					genLink.addLink(link);
				}
			}
		}
		
		ProzessorUtilities.inspectWert(genLink, this);
		updateKosten(genLink); // Kosten Aktualisieren
		
		return genLink;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
		if (!link.getZiel().isAnzeigen()) return false;
		
		if (elementBox.getEqualObjects(link).size() > 0) {
			// So ein Element ist schon vorhanden, geht also nicht!
			return false;
		}
		
		return true;
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
		return ((Vorteil)link.getZiel()).getMaxStufe();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
		return Math.max(((Vorteil)link.getZiel()).getMinStufe(), 
							((GeneratorLink) link).getWertModis());
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(LINK, org.d3s.alricg.charKomponenten.links.IdLink)
	 */
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// Link entfernen
		heldLink.removeLink(element);
		
		// Additionsfamilie setzen
		if (heldLink.getLinkModiList().size() != 0) {
			GeneratorLink genLink = this.getEqualAdditionsFamilie((Vorteil) heldLink.getZiel());
			if (genLink != null) {
				genLink.setZiel( ProzessorUtilities.getAdditionsFamilieErsetzung(genLink) );
			}
		} 
		
		// Stufe ggf. neu setzen
		ProzessorUtilities.inspectWert(heldLink, this);
		
		// Kosten aktualisieren
		updateKosten(heldLink);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateAllKosten()
	 */
	public void updateAllKosten() {
    	final Iterator<GeneratorLink> ite = elementBox.getUnmodifiableIterator();
    	
    	while (ite.hasNext()) {
    		this.updateKosten(ite.next());
    	}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateKosten(LINK)
	 */
	@Override
	public void updateKosten(GeneratorLink genLink) {
		double kosten = 0;
		Vorteil vorteilMerker = (Vorteil)genLink.getZiel();
		
		//alte Kosten abziehen
		gpKosten -= genLink.getKosten();
		
		//neue Kosten berechnen
		kosten = vorteilMerker.getGpKosten();
		if (genLink.getLinkModiList().size() > 0 
					&& getMaxWert(genLink) == 0 
					&& getMinWert(genLink) == 0) {
			
			// Falls der aktuelle Vorteil und der eines Modis unterschiedlich sind, handelt es sich um eine
			// Additionsfamilie
			if ( ((Link) genLink.getLinkModiList().get(0)).getZiel().equals(vorteilMerker) ) {
				kosten -= vorteilMerker.getGpKosten() * genLink.getLinkModiList().size();
			} else {
				kosten = 0;
			}
		
		} else if (genLink.getUserLink() != null) {
			if (genLink.getWert() > 0) {
				kosten += vorteilMerker.getKostenProStufe()*genLink.getUserLink().getWert();
			}
		} else {
			kosten = 0;
		}
		
		// Freie GP durch doppelte automatische Vorteile
		kosten += ProzessorUtilities.getAdditionsFamilieKosten(genLink);
		
		//Kosten eintragen
		genLink.setKosten(kosten);
		gpKosten += kosten;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Vorteil ziel) {

		GeneratorLink tmpLink = createNewGenLink(ziel);
		elementBox.add(tmpLink);
		ProzessorUtilities.inspectWert(tmpLink, this);
		updateKosten(tmpLink);
		
		return tmpLink;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Vorteil ziel) {
		if (!ziel.isAnzeigen()) return false;
		
		// Es gibt noch kein solches Element...
		if (elementBox.getObjectById(ziel) == null)  return true;
		
		if (ziel.getBenoetigtZweitZiel() 
				|| ziel.isMitFreienText() 
				|| (ziel.getTextVorschlaege() != null && ziel.getTextVorschlaege().length > 0)) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(GeneratorLink element) {
		if (!this.containsLink(element)) {
			return false; // Nicht enthalten
		} else if (element.getLinkModiList().size() > 0) {
			return false; // Der Vorteil besitzt Modis, kann deswegen nicht entfernt werden!
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(GeneratorLink element) {

		//Vorteil aus der Liste herausnehmen
		elementBox.remove(element);
		
		//Kosten für dieses Element abziehen
		gpKosten -= element.getKosten();
		
		// Evtl. automatisches Talent entfernen
		removeAutoTalent((Vorteil) element.getZiel());
	}

	// -------------------- Methoden aus dem Extended Interface ---------------------------------

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public ExtendedProzessorVorteil getExtendedInterface() {
		return this;
	}
	
	/**
	 * Erstellt einen neuen GeneratorLink und fühgt diesen zur ElementBox hinzu
	 */
	private GeneratorLink createNewGenLink(Vorteil ziel) {
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
		
		if (ziel.getBenoetigtZweitZiel()) { 
			zweitZiel = this.getMoeglicheZweitZiele(ziel).get(0);
		}
		
		//Link wird erstellt und zur List hinzugefügt
		GeneratorLink tmpLink = new GeneratorLink(ziel, zweitZiel, linkText, stufe);
		createAutoTalent(ziel);
		
		return tmpLink;
	}
	
}
