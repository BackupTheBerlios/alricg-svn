/*
 * Created 07.03.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.VoraussetzungenAdmin;
import org.d3s.alricg.store.charElemente.Herkunft;

/**
 * @author Vincent
 *
 */
public class VoraussetzungenGeneratorAdmin extends VoraussetzungenAdmin {

	public VoraussetzungenGeneratorAdmin(Charakter held) {
		super(held);
	}
	
	/**
	 * Gilt an ob die neueHerkunft zum Helden hinzugef�gt werden, wenn die 
	 * alteHerkunft gleichzeitig entfernd wird. Die Herkunft soll also ausgetauscht
	 * werden.
	 * Es m�ssen dabei vor allem negative Abh�ngigkeiten gepr�ft werden. Also
	 * wenn der Held "Gutaussehen" besitzt, aber die neue Herkunft automatisch 
	 * "Unansehnlich" hinzuf�gen w�rde.
	 * @param neueHerkunft
	 * @param alteHerkunft
	 * @return
	 */
	public boolean canSwitchHerkunft(Herkunft neueHerkunft, Herkunft alteHerkunft) {
		// TODO implement
		return false;
	}

}
