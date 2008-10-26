/*
 * Created 12.07.2008
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.charakter.CharStatusAdmin;
import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;

/**
 * @author Vincent
 *
 */
public class ProzessorLiturgie extends BaseProzessorElementBox<Liturgie, GeneratorLink> 
											implements GeneratorProzessor<Liturgie, GeneratorLink> {

	private final Charakter charakter;
	
	private final int KOSTEN_PRO_GRAD = 50;
	private final String LITURGIEKENNTNIS_ID = "SFK-Liturgiekenntnis";
	protected boolean STUFE_ERHALTEN = true; 
	// Sollte hier immer "True" sein, da nur bestimmte Stufen erlaubt sind. 
	// Z.B. 2,3,5 User 2 + Modi 2 = 4 wäre zwar in den Grenzen, aber nicht erlaubt!
	
	private int liturgieApKosten;
    
    
	public ProzessorLiturgie(Charakter charakter) {
		this.charakter = charakter;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#addNewElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public GeneratorLink addNewElement(Liturgie ziel) {
		
		// Gottheit bestimmen
		Gottheit zweitziel = this.findGottheit(ziel);
		if (zweitziel == null) zweitziel = charakter.getStatusAdmin().getGottheitenGeweiht().get(0);
		
		GeneratorLink tmpLink = new GeneratorLink(ziel, zweitziel, null, this.getMinWert(ziel));
		elementBox.add(tmpLink);
		ProzessorUtilities.inspectWert(tmpLink, this);
		updateKosten(tmpLink);
		
		return tmpLink;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#addModi(org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public HeldenLink addModi(IdLink link) {
		// Kann eine Stufe besitzen, wie z.B. "Ausdauernd"
		GeneratorLink genLink;
		int oldWert;
		
		List<GeneratorLink> list = elementBox.getEqualObjects(link);
		if (list.size() == 0) {
			genLink = new GeneratorLink(link);
			elementBox.add(genLink);
			
		} else {
			genLink = list.get(0);
			// Sollte hier immer "True" sein, da nur bestimmte Stufen erlaubt sind. 
			// Z.B. 2,3,5 User 2 + Modi 2 = 4 wäre zwar in den Grenzen, aber nicht erlaubt!
			
			if (STUFE_ERHALTEN && genLink.getWert() != 0 && genLink.getWert() != Link.KEIN_WERT) {
				oldWert = genLink.getWert(); // Alten Wert Speichern
				genLink.addLink(link); // Link hinzufügen
				genLink.setUserGesamtWert(oldWert); // Versuchen den alten Wert wiederherzustellen
			} else {
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
		return canAddElement((Liturgie) link.getZiel());
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canAddElement(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canAddElement(Liturgie liturgie) {
		// Vorteil Geweiht oder SF Spätweihe
		// Liturgiekentnis-Wert muss mindestens Liturgie-Wert x 3 entsprechen
		// SF "Kontakt zum großem Geist
		
		if (!liturgie.isAnzeigen()) return false;
		
		// Ist Char überhaupt Geweiht...
		if (charakter.getStatusAdmin().getGeweihtStatus() == CharStatusAdmin.GeweihtStatus.nichtGeweiht) {
			return false;
		}
		
		// Es gibt noch bereits ein solches Element
		if (elementBox.getObjectById(liturgie) != null)  return false;
		
		// Die Liturgiekenntnis des chars ist nicht groß genug
		if (this.getMinWert(liturgie) == 0) return false;
		
		// Ist Litugrie allgeimein? Oder stimmt die Gottheit von Liturgie/Char überein?
		if (liturgie.getArt().equals(Liturgie.LiturgieArt.allgemein)) {
			return true;
		} else if (liturgie.getArt().equals(Liturgie.LiturgieArt.hochschamanen) 
				&& charakter.getStatusAdmin().getGeweihtStatus() == CharStatusAdmin.GeweihtStatus.hochschamane) {
			return true;
		} else if (this.findGottheit(liturgie) != null) {
			return true;
		}
		
		// Liturgie passt nicht zum Gott
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.GeneratorProzessor#removeModi(org.d3s.alricg.store.held.HeldenLink, org.d3s.alricg.store.charElemente.links.IdLink)
	 */
	@Override
	public void removeModi(GeneratorLink heldLink, IdLink element) {
		int oldValue = heldLink.getWert();
		
		// Link entfernen
		heldLink.removeLink(element);
		
		// auf alten Wert setzen, weil sonst ungültige Stufen entstehen können
		heldLink.setUserGesamtWert(oldValue);
		
		// Stufe ggf. neu setzen
		ProzessorUtilities.inspectWert(heldLink, this);
		
		// Kosten aktualisieren
		updateKosten(heldLink);
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canRemoveElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canRemoveElement(GeneratorLink element) {
		
		if (!this.containsLink(element)) {
			return false; // Nicht enthalten
		} else if (element.getLinkModiList().size() > 0) {
			return false; // Das Liturgie besitzt Modis, kann deswegen nicht entfernt werden!
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateText(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateText(GeneratorLink link) {
		// Gibt keinen Text
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean canUpdateWert(GeneratorLink link) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#canUpdateZweitZiel(org.d3s.alricg.store.charElemente.links.Link, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Gibt kein ZweitZeil
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#containsLink(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public boolean containsLink(Link link) {
		// Geprüft wird nur anhand der ID, andere Merkmale wie "text"
		// zählen bei Liturgien nicht
		return (elementBox.getObjectById(link.getZiel().getId()) != null);
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
		return liturgieApKosten;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMaxWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMaxWert(Link link) {
		// Liturgiekentnis-Wert muss mindestens Liturgie-Wert x 3 entsprechen
		
		final Liturgie liturgie = (Liturgie) link.getZiel();
		final int litMaxWert = getMaxWertLiturgiekenntnis();
		
		int max = 0;
		for (int x : liturgie.getGrad()) {
			if (x > litMaxWert) continue; // Nicht höher als LitKenntnis
			max = Math.max(max, x);
		}
		
		return max;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getMinWert(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public int getMinWert(Link link) {
		// Liturgiekentnis-Wert muss mindestens Liturgie-Wert x 3 entsprechen

		// Wenn Modis existieren, dann sind diese das Minimum
		if ( ((GeneratorLink) link).getWertModis() > 0) {
			return ((GeneratorLink) link).getWertModis();
		}
		
		// Ansonsten der niedrigste Wert der Litrugie...
		final Liturgie liturgie = (Liturgie) link.getZiel();
		int min = 0;
		for (int x : liturgie.getGrad()) {
			min = Math.min(min, x);
		}
		
		// ... oder "0" fall keine 
		if (min > getMaxWertLiturgiekenntnis()) return 0;
		
		return min;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#removeElement(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void removeElement(GeneratorLink element) {
		// Entfernen aus der Liste
		elementBox.remove(element);
		
		// Kosten für dieses Element von den Talent Gesamtkosten abziehen
		liturgieApKosten -= element.getKosten();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateAllKosten()
	 */
	@Override
	public void updateAllKosten() {
    	Iterator<GeneratorLink> ite = elementBox.getUnmodifiableIterator();
    	
    	while (ite.hasNext()) {
    		this.updateKosten(ite.next());
    	}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateKosten(org.d3s.alricg.store.charElemente.links.Link)
	 */
	@Override
	public void updateKosten(GeneratorLink link) {
		liturgieApKosten -= link.getKosten();
		
		Liturgie liturgie = (Liturgie) link.getZiel();
		int kosten = 0;
		
		kosten += link.getUserLink().getWert() * this.KOSTEN_PRO_GRAD;
		
		// Falls nicht zu der Gottheit gehörig, dann Kosten wie einen Grad höher
		if (charakter.getStatusAdmin().getGottheitenGeweiht().contains((Gottheit) link.getZweitZiel())) {
			kosten += this.KOSTEN_PRO_GRAD;
		}

		link.setKosten( kosten );
		liturgieApKosten += link.getKosten();
		
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#updateText(org.d3s.alricg.store.charElemente.links.Link, java.lang.String)
	 */
	@Override
	public void updateText(GeneratorLink link, String text) {
		// Noop
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
		link.setZweitZiel((Gottheit) zweitZiel);
	}
	
	// ------------------------------------------------------------------------------------------
	
	/**
	 * Prüft ob der Charakter einem Gott zugewand ist, ebenfalls der Liturgie zugewand ist. 
	 * @param liturgie Zu prüfende Liturgie
	 * @return Gottheit des Charakters und der Liturgie. Gibt es mehrere, die erste gefundene.
	 * 		Wird keine solche Gottheit gefunden --> null 
	 */
	private Gottheit findGottheit(Liturgie liturgie) {
		for (Gottheit gott : liturgie.getGottheit()) {
			if (charakter.getStatusAdmin().getGottheitenGeweiht().contains(gott)) {
				return gott;
			}
		}
		return null;
	}
	/**
	 * @return Liturgiekenntnis x3 (= Maximum aller Liturgien) oder "0" falls 
	 */
	private int getMaxWertLiturgiekenntnis() {
		final Link litKenntnisLink = charakter.getProzessor(Sonderfertigkeit.class).getElementBox().getObjectById(LITURGIEKENNTNIS_ID);
		if (litKenntnisLink == null) return 0;
		
		final Talent talent = ((Sonderfertigkeit) litKenntnisLink.getZiel()).getAutomatischesTalent();
		final Link talentLink = charakter.getProzessor(Talent.class).getElementBox().getObjectById(talent);
		
		return talentLink.getWert() * 3;
	}
	
	/**
	 * @return Den niedrigsten möglichen Liturgie-Wert diese Liturgie unter einbeziehung des Liturgiewertes
	 */
	private int getMinWert(Liturgie liturgie) {
		// Ansonsten der niedrigste Wert der Litrugie...
		int min = 0;
		for (int x : liturgie.getGrad()) {
			min = Math.min(min, x);
		}
		
		// ... oder "0" fall keine 
		if (min > getMaxWertLiturgiekenntnis()) return 0;
		
		return min;
	}
}
