/*
 * Created 04.06.2008
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
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * @author Vincent
 */
public class ProzessorSonderf extends BaseProzessorElementBox<Sonderfertigkeit, GeneratorLink> 
							 implements GeneratorProzessor<Sonderfertigkeit, GeneratorLink>
{
	private Charakter charakter;
	
	public ProzessorSonderf(Charakter charakter) {
		this.charakter = charakter;
		this.elementBox = new ElementBox<GeneratorLink>();
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#addNewElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public GeneratorLink addNewElement(Sonderfertigkeit ziel) {
		final GeneratorLink tmpLink;
		
		//Link wird erstellt und zur Liste hinzugefügt
		tmpLink = new GeneratorLink(ziel, null, null, 0);
		elementBox.add(tmpLink);
		
		updateKosten(tmpLink); // Kosten Aktualisieren
		
		return tmpLink;
	}

	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#addModi(org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public HeldenLink addModi(IdLink element) {
		// TODO Auto-generated method stub
		// 
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#canAddElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canAddElement(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#removeModi(org.d3s.alricg.store.held.HeldenLink, org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canAddElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canAddElement(Sonderfertigkeit ziel) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canRemoveElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canRemoveElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateText(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateText(GeneratorLink link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateWert(GeneratorLink link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#containsLink(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean containsLink(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public Object getExtendedInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getGesamtKosten()
	 */
	@Override
	public double getGesamtKosten() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMaxWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMaxWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMinWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMinWert(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#removeElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void removeElement(GeneratorLink element) {
		// TODO Auto-generated method stub
		if (element.getZweitZiel() instanceof Repraesentation) {
			charakter.getMagieStatusAdmin().getRepraesentationen().remove(element.getZweitZiel());
		}
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateAllKosten()
	 */
	@Override
	public void updateAllKosten() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateKosten(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void updateKosten(GeneratorLink Link) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateText(org.d3s.alricg.store.charElemente.links.Link, java.lang.String)
	 */
	@Override
	public void updateText(GeneratorLink link, String text) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateWert(org.d3s.alricg.store.charElemente.links.Link, int)
	 */
	@Override
	public void updateWert(GeneratorLink link, int wert) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Status
		if (link.getZweitZiel() instanceof Repraesentation) {
			charakter.getMagieStatusAdmin().getRepraesentationen().remove(link.getZweitZiel());
		}
		if (zweitZiel instanceof Repraesentation) {
			charakter.getMagieStatusAdmin().getRepraesentationen().add((Repraesentation) zweitZiel);
		}
		
		link.setZweitZiel(zweitZiel);
		updateKosten(link); // Kosten Aktualisieren, z.B. bei ändern in eine 
							// verbilligte Repräsentation
	}
	
}
