/*
 * Created 18.01.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorHerkunft;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.spezial.AbgebrochenProf;
import org.d3s.alricg.store.charElemente.spezial.BreitgefaechertProf;
import org.d3s.alricg.store.charElemente.spezial.VeteranProf;
import org.d3s.alricg.store.charElemente.spezial.ZweiWeltenKultur;
import org.d3s.alricg.store.charElemente.spezial.ZweiWeltenRasse;
import org.d3s.alricg.store.held.HeldenLink;
import org.d3s.alricg.store.rules.RegelConfig;
import org.d3s.alricg.store.rules.RegelConfig.RegelStrenge;

/**
 * @author Vincent
 *
 */
public class ProzessorHerkunft extends BaseProzessorElementBox<Herkunft, GeneratorLink> implements GeneratorProzessor<Herkunft, GeneratorLink>, ExtendedProzessorHerkunft {
	private final Charakter held;
	
	private GeneratorLink<Rasse> rasse;
	private GeneratorLink<Kultur> kultur;
	private GeneratorLink<Profession> profession;
	
	public ProzessorHerkunft(Charakter held) {
		this.held = held;
		this.elementBox = new ElementBox<GeneratorLink>();
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#removeElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void removeElement(GeneratorLink element) {
		// TODO Implement
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateAllKosten()
	 */
	@Override
	public void updateAllKosten() {
		// TODO Implement
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateKosten(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void updateKosten(GeneratorLink Link) {
		// TODO Implement
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateText(org.d3s.alricg.store.charElemente.links.Link, java.lang.String)
	 */
	@Override
	public void updateText(GeneratorLink link, String text) {
		// TODO Implement
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#addNewElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public GeneratorLink addNewElement(Herkunft ziel) {
		GeneratorLink tmp = new GeneratorLink(ziel, null, null, Link.KEIN_WERT);
		GeneratorLink old;
		
		// Abhängigkeiten überprüfen und evtl. automatisch kaufen??
		if (ziel instanceof Rasse) {
			old = this.rasse;
			this.rasse = tmp;
		} else if (ziel instanceof Kultur) {
			old = this.kultur;
			this.kultur = tmp;
		} else if (ziel instanceof Profession) {
			old = this.profession;
			this.profession = tmp;
		} else {
			throw new IllegalArgumentException();
		}
		
		
		
		// Entferne alte Herkunft und füge neue hinzu
		elementBox.remove(old);
		elementBox.add(tmp);
		
		return tmp;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canAddElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canAddElement(Herkunft ziel) {
		// 1. Ist es mit den anderen "Herkünften" vereinbar?
		// 2. Gibt es Unverträglichkeiten mit Voraussetzungen?
		// 3. Gibt es Abhängigkeiten?
		
		if (RegelConfig.getInstance().getHerkunftAuswahl() == RegelStrenge.keineBeschraenkung) {
			return true; // TODO oder sollten hier noch andere Voraussetzungen geprüft werden?
		}
		
		boolean isUeblich = false;
		boolean isMoeglich = false;
		boolean isNegativlisten = false;
		
		if (ziel instanceof Rasse) {
			if (kultur == null) return true;

			isUeblich = canFind( ((Rasse) ziel).getKulturMoeglich(), kultur.getZiel() );
			isMoeglich = canFind( ((Rasse) ziel).getKulturUeblich(), kultur.getZiel() );
			isNegativlisten = ((Rasse) ziel).isKulturenSindNegativListe();

		} else if (ziel instanceof Kultur) {
			if (rasse != null) {
				isUeblich = canFind( ((Rasse) rasse.getZiel()).getKulturUeblich(), ziel);
				isMoeglich = canFind( ((Rasse) rasse.getZiel()).getKulturMoeglich(), ziel);
				isNegativlisten = ((Rasse) rasse.getZiel()).isKulturenSindNegativListe();
			} else {
				isUeblich = true;
				isMoeglich = false;
				isNegativlisten = false;
			}
			// Erste Prüfung von zwei nötigen
			if ( !canAddElementHelper(isUeblich, isMoeglich, isNegativlisten) ) {
				return false;
			}
			
			if (profession == null) return true;
			
			isUeblich = canFind( ((Kultur) ziel).getProfessionUeblich(), profession.getZiel());
			isMoeglich = canFind( ((Kultur) ziel).getProfessionMoeglich(), profession.getZiel());
			isNegativlisten = ((Kultur) ziel).isProfessionenSindNegativListe();
			
		} else if (ziel instanceof Profession) {
			if (kultur == null) return true;
			
			isUeblich = canFind( ((Kultur) kultur.getZiel()).getProfessionUeblich(), ziel);
			isMoeglich = canFind( ((Kultur) kultur.getZiel()).getProfessionMoeglich(), ziel);
			isNegativlisten = ((Kultur) kultur.getZiel()).isProfessionenSindNegativListe();
			
		} else {
			throw new IllegalArgumentException();
		}
		
		return canAddElementHelper(isUeblich, isMoeglich, isNegativlisten);
	}
	
	/**
	 * Durchsucht das "array" nach dem "target"
	 * @param array Array welches durchsucht wird
	 * @param target Element welches gesucht wird
	 * @return true - "target" ist in "array" enthalten, ansonsten false
	 */
	private boolean canFind(Object[] array, Object target) {
		if (array == null) return false;
		return 0 <= Arrays.binarySearch(array, target);
	}
	
	private boolean canAddElementHelper(boolean isUeblich, boolean isMoeglich, boolean isNegativlisten)  {
		// Je nach eingestellter "strenge" der Regeln 
		if (RegelConfig.getInstance().getHerkunftAuswahl() == RegelStrenge.nurEmpfohlen) {
			return (!isNegativlisten && isUeblich) || (isNegativlisten && !isUeblich);
			
		} else {
			return (!isNegativlisten && isUeblich) || (isNegativlisten && !isUeblich)
						|| (!isNegativlisten && isMoeglich) || (isNegativlisten && !isMoeglich);
		} 
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canRemoveElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canRemoveElement(GeneratorLink element) {
		// TODO Implement
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateText(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateText(GeneratorLink link) {
		// TODO Implement
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#containsLink(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean containsLink(Link link) {
		// TODO Implement ??
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#addModi(org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public HeldenLink addModi(IdLink element) {
		// Noop
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#canAddElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canAddElement(Link link) {
		// Noop
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#removeModi(org.d3s.alricg.store.held.HeldenLink, org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// Noop
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateWert(GeneratorLink link) {
		// Noop
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Noop
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public ExtendedProzessorHerkunft getExtendedInterface() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getGesamtKosten()
	 */
	@Override
	public double getGesamtKosten() {
		// TODO Implement
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMaxWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMaxWert(Link link) {
		// Noop
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMinWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMinWert(Link link) {
		// Noop
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateWert(org.d3s.alricg.store.charElemente.links.Link, int)
	 */
	@Override
	public void updateWert(GeneratorLink link, int wert) {
		// Noop
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Noop
	}


	// ----------------------------------------------------------------------
	// 				ExtendedProzessorHerkunft
	// ----------------------------------------------------------------------
	
	/**
	 * @return the rasse
	 */
	public GeneratorLink<Rasse> getRasse() {
		return rasse;
	}

	/**
	 * @return the kultur
	 */
	public GeneratorLink<Kultur> getKultur() {
		return kultur;
	}

	/**
	 * @return the profession
	 */
	public GeneratorLink<Profession> getProfession() {
		return profession;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorHerkunft#isRasseKindZweierWelten()
	 */
	@Override
	public boolean isRasseKindZweierWelten() {
		return (rasse != null && rasse.getZiel() instanceof ZweiWeltenRasse);
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorHerkunft#isKulturKindZweierWelten()
	 */
	@Override
	public boolean isKulturKindZweierWelten() {
		return (kultur != null && kultur.getZiel() instanceof ZweiWeltenKultur);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorHerkunft#isProfessionAbgebrochen()
	 */
	@Override
	public boolean isProfessionAbgebrochen() {
		return (profession != null && profession.getZiel() instanceof AbgebrochenProf);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorHerkunft#isProfessionBreitgefächert()
	 */
	@Override
	public boolean isProfessionBreitgefächert() {
		return (profession != null && profession.getZiel() instanceof BreitgefaechertProf);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorHerkunft#isProfessionVeteran()
	 */
	@Override
	public boolean isProfessionVeteran() {
		return (profession != null && profession.getZiel() instanceof VeteranProf);
	}
	
}
