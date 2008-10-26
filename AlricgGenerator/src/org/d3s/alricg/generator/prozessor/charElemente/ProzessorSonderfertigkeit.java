/*
 * Created 05.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit;
import org.d3s.alricg.generator.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * @author Vincent
 *
 */
public class ProzessorSonderfertigkeit extends ProzessorFertigkeit<Sonderfertigkeit> 
					implements GeneratorProzessor<Sonderfertigkeit, GeneratorLink>, ExtendedProzessorSonderfertigkeit {

	// Die gesamtkosten für alle SF
	private double gpKosten;
	private int apKosten;
	
	private ArrayList<GeneratorLink> linksMitApBezahlt = new ArrayList<GeneratorLink>();
	private boolean isApKosten = false; // true = die Kosten werden aktuell in AP angegeben, ansonsten in GP
	private ElementBox<GeneratorLink>.LinkLinkComparator<GeneratorLink> comparator;
	private GeneratorProzessor<Eigenschaft, HeldenLink> eigenschaftProzessor;
	
	public ProzessorSonderfertigkeit(Charakter held) {
		super(held);
		comparator = elementBox.new LinkLinkComparator<GeneratorLink>();
		eigenschaftProzessor = (GeneratorProzessor<Eigenschaft, HeldenLink>) this.held.getProzessor(Eigenschaft.class);
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#addNewElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public GeneratorLink addNewElement(Sonderfertigkeit ziel) {
		GeneratorLink tmpLink = createNewGenLink(ziel);
		elementBox.add(tmpLink);
		ProzessorUtilities.inspectWert(tmpLink, this);
		addToApBezahltListe(tmpLink);
		addPermanenteKosten(ziel);
		updateKosten(tmpLink);
		
		return tmpLink;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canAddElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canAddElement(Sonderfertigkeit ziel) {
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
	 * @see org.d3s.alricg.common.logic.Prozessor#canRemoveElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canRemoveElement(GeneratorLink element) {
		if (!this.containsLink(element)) {
			return false; // Nicht enthalten
		} else if (element.getLinkModiList().size() > 0) {
			return false; // Der Vorteil besitzt Modis, kann deswegen nicht entfernt werden!
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateText(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateText(GeneratorLink link) {	
		Sonderfertigkeit sf = ((Sonderfertigkeit)link.getZiel());
		return (sf.isMitFreienText() 
			||  (sf.getTextVorschlaege() != null && sf.getTextVorschlaege().length > 0));
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateWert(GeneratorLink link) {
		return false; // Gibt keinen Wert
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		if ( ((Sonderfertigkeit)link.getZiel()).getBenoetigtZweitZiel() ) {
			return true;
		}
		return false;
	}

	/** 
	 * Die Gesamt-Kosten, umgerechnet zu GP
	 * @see org.d3s.alricg.common.logic.Prozessor#getGesamtKosten()
	 */
	@Override
	public double getGesamtKosten() {
		return this.gpKosten + FormelSammlung.getGpFromAp(apKosten);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMaxWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMaxWert(Link link) {
		return 0; // Kein Wert für SF
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMinWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMinWert(Link link) {
		return 0; // Kein Wert für SF
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#removeElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void removeElement(GeneratorLink element) {
		//Vorteil aus der Liste herausnehmen
		elementBox.remove(element);
		
		//Kosten für dieses Element abziehen
		int idx = Collections.binarySearch(linksMitApBezahlt, element, comparator);
		if (Collections.binarySearch(linksMitApBezahlt, element, comparator) >= 0) {
			apKosten -= element.getKosten();
			linksMitApBezahlt.remove(idx);
		} else {
			gpKosten -= element.getKosten();
		}
		
		// Evtl. automatisches Talent entfernen
		removeAutoTalent((Sonderfertigkeit) element.getZiel());
		removePermanenteKosten((Sonderfertigkeit) element.getZiel());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateAllKosten()
	 */
	@Override
	public void updateAllKosten() {
    	final Iterator<GeneratorLink> ite = elementBox.getUnmodifiableIterator();
    	
    	while (ite.hasNext()) {
    		this.updateKosten(ite.next());
    	}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateKosten(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void updateKosten(GeneratorLink genLink) {
		double kosten = 0;
		boolean isApKosten = Collections.binarySearch(linksMitApBezahlt, genLink, comparator) >= 0;
		
		//alte Kosten abziehen
		if (isApKosten) {
			apKosten -= genLink.getKosten();
		} else {
			gpKosten -= genLink.getKosten();
		}
		
		//neue Kosten berechnen
		if (genLink.getLinkModiList().size() > 0) {
			// Kostenlos bzw. freie GP/AP durch Modis
			
			// Falls die aktuelle SF und die eines Modis unterschiedlich sind, handelt es sich um eine
			// Additionsfamilie
			if ( ((Link) genLink.getLinkModiList().get(0)).getZiel().equals(genLink.getZiel()) ) {
				kosten -= (genLink.getLinkModiList().size() -1) 
								* ((Sonderfertigkeit) genLink.getZiel()).getGpKosten() ;
			} else {
				kosten = 0;
			}

		} else if (genLink.getUserLink() != null) {
			kosten = ((Sonderfertigkeit) genLink.getZiel()).getGpKosten();
		}
		
		// Freie GP durch doppelte automatische Vorteile
		kosten += ProzessorUtilities.getAdditionsFamilieKosten(genLink);
		
		//Kosten eintragen
		if (isApKosten) {
			kosten = FormelSammlung.getApFromGp(kosten);
			apKosten += kosten;
		} else {
			gpKosten += kosten;
		}
		
		genLink.setKosten(kosten);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateText(org.d3s.alricg.store.charElemente.links.Link, java.lang.String)
	 */
	@Override
	public void updateText(GeneratorLink link, String text) {
		link.setText(text);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateWert(org.d3s.alricg.store.charElemente.links.Link, int)
	 */
	@Override
	public void updateWert(GeneratorLink link, int wert) {
		// Nope - gibt keinen Wert bei SF
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		link.setZweitZiel(zweitZiel);
		updateKosten(link); // z.b. bei Änderung von Begabung für Schwerter auf Begabung für Töpfern
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#addModi(org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public HeldenLink addModi(IdLink link) {
		// Kann eine Stufe besitzen, wie z.B. "Ausdauernd"
		GeneratorLink genLink;
		int oldWert;
		
		genLink = this.getEqualAdditionsFamilie((Sonderfertigkeit) link.getZiel());
		if (genLink != null) {
			
			genLink.addLink(link);
			genLink.setZiel( ProzessorUtilities.getAdditionsFamilieErsetzung(genLink) );
			// evtl. frei werden GP werden bei update Kosten beachtet
			
		} else {
			List<GeneratorLink> list = elementBox.getEqualObjects(link);
			if (list.size() == 0) {
				genLink = new GeneratorLink(link);
				elementBox.add(genLink);
				addToApBezahltListe(genLink);
				addPermanenteKosten((Sonderfertigkeit) link.getZiel());
				createAutoTalent((Sonderfertigkeit) link.getZiel());
				
			} else {
				genLink = list.get(0);
				genLink.addLink(link);
			}
		}
		
		ProzessorUtilities.inspectWert(genLink, this);
		updateKosten(genLink); // Kosten Aktualisieren
		
		return genLink;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#canAddElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canAddElement(Link link) {
		if (!link.getZiel().isAnzeigen()) return false;
		
		if (elementBox.getEqualObjects(link).size() > 0) {
			// So ein Element ist schon vorhanden, geht also nicht!
			return false;
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#removeModi(org.d3s.alricg.store.held.HeldenLink, org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// Link entfernen
		heldLink.removeLink(element);
		
		// Additionsfamilie setzen
		if (heldLink.getLinkModiList().size() != 0) {
			GeneratorLink genLink = this.getEqualAdditionsFamilie((Sonderfertigkeit) heldLink.getZiel());
			if (genLink != null) {
				genLink.setZiel( ProzessorUtilities.getAdditionsFamilieErsetzung(genLink) );
			}
		} 
		
		// Stufe ggf. neu setzen
		ProzessorUtilities.inspectWert(heldLink, this);
		
		// Kosten aktualisieren
		updateKosten(heldLink);
	}
	
	// -------------------- Methoden aus dem Extended Interface ---------------------------------
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public ExtendedProzessorSonderfertigkeit getExtendedInterface() {
		return this;
	}
		
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit#changeMitAPBezahlt(org.d3s.alricg.generator.prozessor.GeneratorLink, boolean)
	 */
	@Override
	public void changeMitAPBezahlt(GeneratorLink link, boolean mitAP) {
		if (this.getElementBox().getObjectById(link.getZiel()) == null) {
			throw new IllegalArgumentException("Der Link ist nicht im Prozessor vorhanden.");
		}
		
		int idx = Collections.binarySearch(linksMitApBezahlt, link, comparator);
		if (idx >= 0) {
			linksMitApBezahlt.remove(idx);
			apKosten -= link.getKosten();
			link.setKosten(FormelSammlung.getGpFromAp( (int)link.getKosten() ));
			gpKosten += link.getKosten();
		} else {
			addToApBezahltListe(link);
			gpKosten -= link.getKosten();
			link.setKosten(FormelSammlung.getApFromGp(link.getKosten()));
			apKosten += (int)link.getKosten();
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit#getApGesamtKosten()
	 */
	@Override
	public int getApGesamtKosten() {
		return this.apKosten;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit#getGpGesamtKosten()
	 */
	@Override
	public double getGpGesamtKosten() {
		return this.gpKosten;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit#isLinkMitApBezahlt(org.d3s.alricg.generator.prozessor.GeneratorLink)
	 */
	@Override
	public boolean isLinkMitApBezahlt(GeneratorLink link) {
		return (Collections.binarySearch(linksMitApBezahlt, link, comparator) >= 0);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit#setMitApBezahlt(boolean)
	 */
	@Override
	public void setMitApBezahlt(boolean mitAP) {
		isApKosten = mitAP;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorSonderfertigkeit#getMitApBezahlt()
	 */
	@Override
	public boolean getMitApBezahlt() {
		return isApKosten;
	}
	
	// ------------------------------- Hilfmethoden ------------------------------------
	
	/**
	 * Erstellt einen neuen GeneratorLink und fühgt diesen zur ElementBox hinzu
	 */
	private GeneratorLink createNewGenLink(Sonderfertigkeit ziel) {
		String linkText = null;
		int stufe = 0;
		CharElement zweitZiel = null;
		
		if (ziel.isMitFreienText()) {
			linkText = "Bitte Text eingeben";
		} else if (!ziel.isMitFreienText() && (ziel.getTextVorschlaege() != null)) {
			linkText = ziel.getTextVorschlaege()[0];
		}
		
		if (ziel.getBenoetigtZweitZiel()) { 
			zweitZiel = this.getMoeglicheZweitZiele(ziel).get(0);
		}
		
		//Link wird erstellt und zur List hinzugefügt
		GeneratorLink tmpLink = new GeneratorLink(ziel, zweitZiel, linkText, stufe);
		createAutoTalent(ziel);
		
		return tmpLink;
	}
	
	/**
	 * Entfernt, fall nötig, die permaneten Kosten vom Helden
	 * @param link Link mit evtl. permaneten Kosten
	 */
	private void removePermanenteKosten(Sonderfertigkeit sf) {

		if (sf.getPermAsp() > 0) {
			GeneratorLink tmpLink = (GeneratorLink) eigenschaftProzessor.getElementBox()
										.getObjectById(EigenschaftEnum.ASP.getEigenschaft());	
			eigenschaftProzessor.removeModi(
						tmpLink,
						tmpLink.getModiLinkByQuelle(sf));
			
		} 
		if (sf.getPermKep() > 0) {
			GeneratorLink tmpLink = (GeneratorLink) eigenschaftProzessor.getElementBox()
										.getObjectById(EigenschaftEnum.KA.getEigenschaft());	
			eigenschaftProzessor.removeModi(
							tmpLink,
							tmpLink.getModiLinkByQuelle(sf));
			
		} 
		if (sf.getPermLep() > 0) {
			GeneratorLink tmpLink = (GeneratorLink) eigenschaftProzessor.getElementBox()
										.getObjectById(EigenschaftEnum.LEP.getEigenschaft());	
			eigenschaftProzessor.removeModi(
							tmpLink,
							tmpLink.getModiLinkByQuelle(sf));
		} 
		
	}
	
	/**
	 * Fügt, fall nötig, die permaneten Kosten zum Helden hinzu.
	 * @param link Link mit evtl. permaneten Kosten
	 */
	private void addPermanenteKosten(Sonderfertigkeit sf) {
		if (sf.getPermAsp() > 0) {
			eigenschaftProzessor.addModi(
					new IdLink<Eigenschaft>(
						sf, 
						EigenschaftEnum.ASP.getEigenschaft(),
						null,
						-sf.getPermAsp(),
						null)
					);
		} 
		if (sf.getPermKep() > 0) {
			eigenschaftProzessor.addModi(
					new IdLink<Eigenschaft>(
						sf, 
						EigenschaftEnum.KA.getEigenschaft(),
						null,
						-sf.getPermKep(),
						null)
					);

		} 
		if (sf.getPermLep() > 0) {
			eigenschaftProzessor.addModi(
					new IdLink<Eigenschaft>(
						sf, 
						EigenschaftEnum.LEP.getEigenschaft(),
						null,
						-sf.getPermLep(),
						null)
					);
		}
		

	}
	
	private void addToApBezahltListe(GeneratorLink link) {
		// AP oder GP als Bezahlung?
		if (isApKosten) {
			// Falls die Kosten mit AP bezahlt werden, diesen Link in die AP Liste einordnen
			// Korrekte Position bestimmen (wichtig für spätere zugriffe)
			int index = Collections.binarySearch(linksMitApBezahlt, link, comparator);
			if (index < 0) {
				index = Math.abs(index + 1);
			}
			// Element dort einfügen (sortierung erleichtert das suchen)
			linksMitApBezahlt.add(index, link);
		}
		
	}
	
}
