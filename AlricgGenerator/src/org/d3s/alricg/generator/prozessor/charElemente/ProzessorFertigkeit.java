/*
 * Created 05.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;

/**
 * @author Vincent
 *
 */
public abstract class ProzessorFertigkeit<ZIEL extends CharElement> extends BaseProzessorElementBox<ZIEL, GeneratorLink> {
	protected final Charakter held;
	protected int AUTOMATISCHE_TALENT_STUFE = 3;
	
	public ProzessorFertigkeit(Charakter held) {
		this.held = held;
		this.elementBox = new ElementBox<GeneratorLink>();
	}
	
	/**
	 * Prüft ob die Fertigkeit ein automatisches Talent besitzt und entfernd dieses
	 * ggf. komplett vom Helden.
	 * @param ziel Fertigkeit die entfernd wird
	 */
	protected void removeAutoTalent(Fertigkeit ziel) {
		if (ziel.getAutomatischesTalent() == null) return;
		
		Link link = ((GeneratorProzessor) held.getProzessor(Talent.class)).getElementBox().getObjectById(ziel.getAutomatischesTalent());
		((GeneratorProzessor) held.getProzessor(Talent.class)).removeElement(link);
	}
	
	/**
	 * Prüft ob die Fertigkeit ein automatisches Talent besitzt und fügt dieses
	 * ggf. mit dem entsprechendem Startwert zu Helden hinzu!
	 * @param ziel Fertigkeit die hinzugefügt wird
	 */
	protected void createAutoTalent(Fertigkeit ziel) {
		if (ziel.getAutomatischesTalent() == null) return;

		IdLink<Talent> autoLink = new IdLink<Talent>(
				ziel,
				ziel.getAutomatischesTalent(),
				null,
				AUTOMATISCHE_TALENT_STUFE,
				null);
		
		((GeneratorProzessor) held.getProzessor(Talent.class)).addModi(autoLink);
	}
	
	
	/**
	 * Such ein Element mit der gleichen AdditionsID wie "fertigkeit". Bei mehreren Vorkommen
	 * wird das erste gefundene geliefert.
	 * @param fertigkeit Fertigkeit nach dessen AdditionsID gesucht wird
	 * @return Link mit der Fertigkeit als Ziel, oder null fall kein solches Element existiert
	 */
	public GeneratorLink getEqualAdditionsFamilie(Fertigkeit fertigkeit) {
		if (fertigkeit.getAdditionsFamilie() == null) return null;
		
		List<GeneratorLink> elementList = elementBox.getUnmodifiableList();
		
		for(GeneratorLink element : elementList) {
			if ( !element.getZiel().getClass().equals(fertigkeit.getClass()) ) continue;
			if ( ((Fertigkeit)element.getZiel()).getAdditionsFamilie() == null) continue;
			
			if ( ((Fertigkeit)element.getZiel()).getAdditionsFamilie().getAdditionsID().equals(
					fertigkeit.getAdditionsFamilie().getAdditionsID()) ) {
				return element;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorVorteil#getMoeglicheZweitZiele(org.d3s.alricg.charKomponenten.Vorteil)
	 */
	public List<CharElement> getMoeglicheZweitZiele(ZIEL ziel) {
		// TODO Auto-generated method stub
		
		// Nur zum Testen
		ArrayList list = new ArrayList();
		list.add(null);
		
		return list;
	}
}
