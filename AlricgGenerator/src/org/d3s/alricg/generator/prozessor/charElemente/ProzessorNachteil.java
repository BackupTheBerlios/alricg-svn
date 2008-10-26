/*
 * Created 05.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorNachteil;
import org.d3s.alricg.generator.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;
import org.d3s.alricg.store.rules.RegelConfig;

/**
 * @author Vincent
 *
 */
public class ProzessorNachteil extends ProzessorFertigkeit<Nachteil> 
					implements GeneratorProzessor<Nachteil, GeneratorLink>, ExtendedProzessorNachteil {

	//private final Notepad notepade;
	protected boolean STUFE_ERHALTEN = true;

	// Die gesamtkosten für alle Vorteile
	private double gpKosten;
	private double gpSchlechteEigensch;
	
	public ProzessorNachteil(Charakter held) {
		super(held);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#addNewElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public GeneratorLink addNewElement(Nachteil ziel) {
		GeneratorLink tmpLink = createNewGenLink(ziel);
		elementBox.add(tmpLink);
		ProzessorUtilities.inspectWert(tmpLink, this);
		
		updateKosten(tmpLink);
		
		return tmpLink;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canAddElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canAddElement(Nachteil ziel) {
		//Genau so wenig wie GP werden die max. EigenschaftsGP geprüft
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
			return false; // Der Nachteil besitzt Modis, kann deswegen nicht entfernt werden!
		}
		
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateText(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateText(GeneratorLink link) {
		Nachteil nachteil = ((Nachteil)link.getZiel());
		return (nachteil.isMitFreienText() 
			||  (nachteil.getTextVorschlaege() != null && nachteil.getTextVorschlaege().length > 0));
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateWert(GeneratorLink link) {
		return link.getWert() > 0; // Vorteile mit Wert 0 haben immer Wert 0 (z.b. Vollzauberer)
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		if ( ((Nachteil)link.getZiel()).getBenoetigtZweitZiel() ) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getGesamtKosten()
	 */
	@Override
	public double getGesamtKosten() {
		return gpKosten;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMaxWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMaxWert(Link link) {
		Nachteil nachteil = (Nachteil) link.getZiel();
		
		if (nachteil.isSchlechteEigen()) {
			return RegelConfig.getInstance().getMaxSchlechtEigenchafWert();
		}
		return nachteil.getMaxStufe();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMinWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMinWert(Link link) {
		Nachteil nachteil = (Nachteil) link.getZiel();
		
		if (nachteil.isSchlechteEigen()) {
			return Math.max(RegelConfig.getInstance().getMinSchlechteEigenchaftWert(), 
							((GeneratorLink) link).getWertModis());
		}
		return Math.max(nachteil.getMinStufe(), ((GeneratorLink) link).getWertModis());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#removeElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void removeElement(GeneratorLink element) {
		//Element aus der Liste herausnehmen
		elementBox.remove(element);
		
		//Kosten für dieses Element abziehen
		gpKosten -= element.getKosten();
		if (((Nachteil) element.getZiel()).isSchlechteEigen()) {
			gpSchlechteEigensch -= element.getKosten();
		}
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
		Nachteil tmpNachteil = (Nachteil)genLink.getZiel();
		
		//alte Kosten abziehen
		gpKosten -= genLink.getKosten();
		if (tmpNachteil.isSchlechteEigen()) gpSchlechteEigensch -= genLink.getKosten();
		
		//neue Kosten berechnen
		kosten = tmpNachteil.getGpKosten();
		if (genLink.getLinkModiList().size() > 0 
					&& getMaxWert(genLink) == 0 
					&& getMinWert(genLink) == 0) {
			
			// Falls der aktuelle Vorteil und der eines Modis unterschiedlich sind, handelt es sich um eine
			// Additionsfamilie
			if ( ((Link) genLink.getLinkModiList().get(0)).getZiel().equals(tmpNachteil) ) {
				kosten -= tmpNachteil.getGpKosten() * genLink.getLinkModiList().size();
			} else {
				kosten = 0;
			}
		
		} else if (genLink.getUserLink() != null) {
			if (genLink.getWert() > 0) {
				kosten += tmpNachteil.getKostenProStufe()*genLink.getUserLink().getWert();
			}
		} else {
			kosten = 0;
		}
		
		// Freie GP durch doppelte automatische Vorteile
		kosten += ProzessorUtilities.getAdditionsFamilieKosten(genLink);
		
		//Kosten eintragen
		genLink.setKosten(kosten);
		gpKosten += kosten;
		if (tmpNachteil.isSchlechteEigen()) gpSchlechteEigensch += genLink.getKosten();
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
		link.setUserGesamtWert(wert);
		updateKosten(link);
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
		// Kann eine Stufe besitzen
		GeneratorLink genLink;
		int oldWert;
		
		genLink = this.getEqualAdditionsFamilie((Nachteil) link.getZiel());
		if (genLink != null) {
			
			genLink.addLink(link);
			genLink.setZiel( ProzessorUtilities.getAdditionsFamilieErsetzung(genLink) );
			// evtl. frei werden GP werden bei update Kosten beachtet
			
		} else {
			List<GeneratorLink> list = elementBox.getEqualObjects(link);
			if (list.size() == 0) {
				genLink = new GeneratorLink(link);
				elementBox.add(genLink);
				createAutoTalent((Nachteil) link.getZiel());
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
			GeneratorLink genLink = this.getEqualAdditionsFamilie((Nachteil) heldLink.getZiel());
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
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public Object getExtendedInterface() {
		return this;
	}

	/**
	 * Erstellt einen neuen GeneratorLink und fühgt diesen zur ElementBox hinzu
	 */
	private GeneratorLink createNewGenLink(Nachteil ziel) {
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
	
	/**
	 * @return Die Anzahl von GP durch schlechte Eigenschaften (negativ)
	 */
	public double getGpSchlechteEigenschaft() {
		return this.gpSchlechteEigensch;
	}
	
}
