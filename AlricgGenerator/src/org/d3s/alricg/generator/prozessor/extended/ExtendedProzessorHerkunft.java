/*
 * Created 21.03.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.extended;

import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;

/**
 * @author Vincent
 *
 */
public interface ExtendedProzessorHerkunft {

	public GeneratorLink<Rasse> getRasse();	
	public GeneratorLink<Kultur> getKultur();
	public GeneratorLink<Profession> getProfession();
	
	public boolean isRasseKindZweierWelten();
	public boolean isKulturKindZweierWelten();
	public boolean isProfessionAbgebrochen();
	public boolean isProfessionVeteran();
	public boolean isProfessionBreitgefächert();
}
