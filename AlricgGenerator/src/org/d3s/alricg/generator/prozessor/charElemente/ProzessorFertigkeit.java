/*
 * Created 05.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
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
}
