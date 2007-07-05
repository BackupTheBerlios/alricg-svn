/*
 * Created on 30.04.2005 / 14:22:03
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.sonderregeln;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Kultur;
import org.d3s.alricg.charKomponenten.Profession;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.LinkProzessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;

/**
 * <u>Beschreibung:</u><br> 
 * Implementiert die Sonderregel des Vorteils "Herausragende Eigenschaft", AH S. 108.
 * 
 * TODO unvereinbar mit "Misserable EIgenschaft"
 * 
 * Erfüllt:
 * Stubenhocker ist mit GE, KK, KO unvereinbar!
 * 
 * @author V. Strelow
 */
public class HerausragendeEigenschaft extends SonderregelAdapter {
	private Eigenschaft eigen;
	private LinkProzessor<Eigenschaft, HeldenLink> prozessor;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getId()
	 */
	public String getId() {
		return "SR-HerausragendeEigenschaft";
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.CharElement#getBeschreibung()
	 */
	public String getBeschreibung() {
		// TODO Muß noch lokalisiert werden
		return "Siehe AH S. 108";
	}
	
	/**
	 * Konstruktor
	 */
	public HerausragendeEigenschaft() {
		
		// Erstellen der wählbaren Eigenschaften
		/*if (moeglicheZweitZiele == null) {
			moeglicheZweitZiele = new CharElement[] {
					ProgAdmin.data.getCharElement(EigenschaftEnum.MU.getId(), 
															CharKomponente.eigenschaft),
					ProgAdmin.data.getCharElement(EigenschaftEnum.KL.getId(), 
															CharKomponente.eigenschaft),
					ProgAdmin.data.getCharElement(EigenschaftEnum.IN.getId(), 
															CharKomponente.eigenschaft),
					ProgAdmin.data.getCharElement(EigenschaftEnum.CH.getId(), 
															CharKomponente.eigenschaft),
					ProgAdmin.data.getCharElement(EigenschaftEnum.FF.getId(), 
															CharKomponente.eigenschaft),
					ProgAdmin.data.getCharElement(EigenschaftEnum.GE.getId(), 
															CharKomponente.eigenschaft),
					ProgAdmin.data.getCharElement(EigenschaftEnum.KO.getId(), 
															CharKomponente.eigenschaft),																
					ProgAdmin.data.getCharElement(EigenschaftEnum.KK.getId(), 
															CharKomponente.eigenschaft),
			};
		}*/

	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#canAddSelf(org.d3s.alricg.held.box.HeldProzessor, boolean, org.d3s.alricg.charKomponenten.links.Link)
	 * 
	 * - Das Ziel dieser Sonderregel (also die Eigenschaft) wird aus dem 
	 * 	 srLink ausgelesen und ist das "zweitZiel".
	 * - Es darf keine Eigenschaft die SF "Herausragende Eigenschaft" bekommen, die 
	 *   durch die Herkunft gesenkt wurde.
	 */
	public boolean canAddSelf(Held held, boolean ok, Link srlink) {
		Eigenschaft element;
		Link tmpLink;
		
		// Auslesen des gewünschten Links
		element = (Eigenschaft) srlink.getZweitZiel();
		tmpLink = ((LinkProzessor<Eigenschaft, HeldenLink>) held
						.getProzessor(CharKomponente.eigenschaft))
							.getElementBox()
							.getObjectById(element);
		
		// Überprüfung ob die Modis durch die Herkunft u.ä. negativ sind,
		// dann dürfte diese SR nicht gewählt werden
		if ( ((GeneratorLink) tmpLink).getWertModis() < 0 ) {
			return false;
		}
		
		// Die Sonderregel darf nur einmal zu einem ELement existieren
		if (held.getSonderregelAdmin().hasSonderregel(this.getId(), null, element)) {
			return false;
		}
		
		// Prüfen ob "Stubenhocker" --> Nicht erlaubt mit KK, KO und GE!
		if ( element.getEigenschaftEnum().equals(EigenschaftEnum.GE)
				|| element.getEigenschaftEnum().equals(EigenschaftEnum.KK)
				|| element.getEigenschaftEnum().equals(EigenschaftEnum.KO)
		) {
			if ( held.getSonderregelAdmin().hasSonderregel("SR-Stubenhocker", null, null) ) {
				return false;
			}
		}
		
		return ok;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#initSonderregel(org.d3s.alricg.held.box.HeldProzessor, org.d3s.alricg.charKomponenten.links.Link)
	 * 
	 * - Das Ziel dieser Sonderregel (also die Eigenschaft) wird aus dem 
	 * 	 srElementlink ausgelesen und ist das "zweitZiel".
	 *  - Die betreffende Eigenschaft wird auf das Maximum gesetzt und um den
	 *    Wert in "link" erhöht. 
	 */
	public void initSonderregel(Held held, Link srLink) {
		GeneratorLink tmpLink;
		IdLink modiLink;
		
		assert srLink.getZweitZiel() != null;
		
		this.prozessor = held.getProzessor(CharKomponente.eigenschaft);
		
		// Auslesen des gewünschten Links
		eigen = (Eigenschaft) srLink.getZweitZiel();
		tmpLink = (GeneratorLink) prozessor.getElementBox().getObjectById(eigen);
		
		// Link erstellen der die Eigenschaft um den gewünschten Wert erhöht  
		modiLink = new IdLink(this, null);
		modiLink.setWert(srLink.getWert());
		
		// Neuen Link hinzufügen,
		tmpLink.addLink(modiLink);
		
		// Eigenschaft auf den Maximalen Wert setzen
		tmpLink.setUserGesamtWert(prozessor.getMaxWert(tmpLink));
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#finalizeSonderregel(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void finalizeSonderregel(Link link) {
		CharElement element;
		GeneratorLink tmpLink, modiLink;
		
		assert link.getZweitZiel() != null;
		
		// Auslesen des gewünschten Links
		element = link.getZweitZiel();
		tmpLink = (GeneratorLink) prozessor.getElementBox().getObjectById(element);

		// Suche und entferne den Link aus dem Element
		tmpLink.removeLinkByQuelle(this);
		eigen = null;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeCanAddElement(boolean, org.d3s.alricg.charKomponenten.links.Link)
	 * 
	 * - Es kann keine Rasse oder Kultur hinzugefügt werden, die einen Eigenschaft 
	 *  mit "Herausragender Eigenschaft" erniedrigt
	 */
	public boolean changeCanAddElement(boolean ok, Link tmpLink) {
		CharElement element = tmpLink.getZiel();
		
		if (element instanceof Rasse) {
//			TODO Es darf keine Eigenschaft gesenkt werden, die "Herausragende Eigenschaft" ist
		} else if (element instanceof Kultur) {
//			TODO Es darf keine Eigenschaft gesenkt werden, die "Herausragende Eigenschaft" ist
		} else if (element instanceof Profession) {
//			TODO Es darf keine Eigenschaft gesenkt werden, die "Herausragende Eigenschaft" ist			
		} 
		
		return ok;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeMaxStufe(int, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int changeMaxWert(int maxStufe, Link link) {
		// Muß wahrscheinlich nicht Implementiert werden!
		return maxStufe;
	}
	

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeMinStufe(int, org.d3s.alricg.charKomponenten.links.Link)
	 * - Da eine Herausragende Eigenschaft nicht gesenkt werden kann gilt:
	 *  Minimale Stufe = Maximale Stufe.
	 */
	public int changeMinWert(int minStufe, Link link) {
		
		if ( eigen.equals( link.getZiel()) ) {
			// Der Wert kann nicht mehr gesenkt werden!
			return prozessor.getMaxWert(link);
		}
		
		return minStufe;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelInterface#changeCanUpdateStufe(boolean, org.d3s.alricg.held.HeldenLink)
	 * - Ein "H. Eigenschaft" kann nicht mehr geändert werden
	 */
	public boolean changeCanUpdateWert(boolean canUpdate, HeldenLink link) {
		// Die Stufe kann (auf normalem Weg) nicht mehr geändert werden
		if ( eigen.equals( link.getZiel() ) ) {
			return false;
		}
		return canUpdate;
	}
	
    /* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#isSonderregel(java.lang.String, java.lang.String, org.d3s.alricg.charKomponenten.CharElement)
	 */
	@Override
	public boolean isSonderregel(String id, String text, CharElement zweitZiel) {
		
		if ( id.equals(this.getId()) && zweitZiel.equals(eigen)	) {
			// Die ID und die Eigenschaft stimmen, somit gleiche SR
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.charKomponenten.sonderregeln.SonderregelAdapter#isForManagement()
	 */
	@Override
	public boolean isForManagement() {
		// Ist nach der Generierung nicht mehr wichtig!
		return false;
	}

	// ------------------------------------------------------------------------------
	/**
	 * @return Liefert die Eigenschaft, füe die diese SR gilt
	 */
	public Eigenschaft getZweitZiel() {
		return eigen;
	}
	
	/**
	 * Diese Regel ist nicht im Interface enthalten, da sie NUR diese Sonderregel betrifft
	 * @return Liste der möglichen Zweitziele
	 *
	public CharElement[] getMoeglicheZweitZiele() {
		return moeglicheZweitZiele;
	}*/


}
