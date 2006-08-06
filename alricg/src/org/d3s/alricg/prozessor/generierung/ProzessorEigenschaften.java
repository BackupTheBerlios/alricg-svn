/*
 * Created on 19.06.2006 / 12:15:32
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.prozessor.generierung;

import java.util.List;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.Notepad;
import org.d3s.alricg.held.Held;
import org.d3s.alricg.prozessor.BaseProzessorElementBox;
import org.d3s.alricg.prozessor.LinkProzessor;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.common.SonderregelAdmin;
import org.d3s.alricg.prozessor.elementBox.ElementBoxLink;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorZauber;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.prozessor.utils.Konstanten;
import org.d3s.alricg.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;

/**
 * <u>Beschreibung:</u><br> 
 * Prozessor für das Bearbeiten von Eigenschaften. Alle Änderungen die an Eigenschaften
 * durchgeführt werden, werden über diesen Prozessor durchgeführt.
 *  
 * @author V. Strelow
 */
public class ProzessorEigenschaften extends BaseProzessorElementBox<Eigenschaft, GeneratorLink> 
									implements LinkProzessor<Eigenschaft, GeneratorLink>, ExtendedProzessorEigenschaft {
    
    /** <code>ProzessorEigenschaften</code>'s logger */
    private static final Logger LOG = Logger.getLogger(ProzessorEigenschaften.class.getName());
    private static final boolean STUFE_ERHALTEN = true;
    
	private static final String TEXT_SKT_SPALTE = "Original SKT-Spalte: ";
	private static final String TEXT_GESAMT_KOSTEN = "Gesamt Kosten: ";
	
    private int eigenschaftGpKosten = 0;
    private int eigenschaftTalentGpKosten = 0;

    private final Held held;
    private final SonderregelAdmin sonderregelAdmin;
    private final Notepad notepad;
    
    
    public ProzessorEigenschaften(Held held, Notepad notepad) {
		this.sonderregelAdmin = held.getSonderregelAdmin();
		this.held = held;
		this.notepad = notepad;
		this.elementBox = new ElementBoxLink<GeneratorLink>();
    }
    
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public HeldenLink addModi(IdLink element) {
        final GeneratorLink tmpLink;
        final int tmpInt;
        
        tmpLink = this.elementBox.getObjectById(element.getZiel().getId());

        if (STUFE_ERHALTEN) {
            tmpInt = tmpLink.getWert(); // Alten Wert Speichern
            tmpLink.addLink(element); // Link hinzufügen
            tmpLink.setUserGesamtWert(tmpInt); // Versuchen den alten Wert wiederherzustellen
        } else {
            tmpLink.addLink(element);
        }

        // Überprüfen ob die Stufen OK sind
        inspectEigenschaftWert(tmpLink);

        updateKosten(tmpLink); // Kosten Aktualisieren

        return tmpLink;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canAddElement(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean canAddElement(Link link) {
        // Zu Eigenschaften können keine neuen Elemente hinzugefügt werden
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(E)
	 */
	public boolean canUpdateText(GeneratorLink link) {
        // Es gibt keinen Text bei Eigenschaften
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateWert(E)
	 */
	public boolean canUpdateWert(GeneratorLink link) {
        // Grundsätzlich können Werte bei Eigenschaften geändert werden
        final EigenschaftEnum eigen = ((Eigenschaft) link.getZiel()).getEigenschaftEnum();

        if (eigen.equals(EigenschaftEnum.MR) || eigen.equals(EigenschaftEnum.AT) || eigen.equals(EigenschaftEnum.FK)
                || eigen.equals(EigenschaftEnum.INI) || eigen.equals(EigenschaftEnum.PA)
                || eigen.equals(EigenschaftEnum.KA) || eigen.equals(EigenschaftEnum.GS)) {
            // Diese Eigenschaften können nicht direkt geändert werden
            return false;
        }

        return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
        // Es gibt kein Zweitziel bei Eigenschaften
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public boolean containsLink(Link link) {
		return ( elementBox.getObjectById(link.getZiel().getId()) != null );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	public int getGesamtKosten() {
		return eigenschaftGpKosten;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMaxWert(Link link) {
        final EigenschaftEnum eigen = ((Eigenschaft) link.getZiel()).getEigenschaftEnum();
        final GeneratorLink genLink = (GeneratorLink) link;
        int tmpInt;
        
        if (eigen.equals(EigenschaftEnum.MU) || eigen.equals(EigenschaftEnum.KL) || eigen.equals(EigenschaftEnum.IN)
                || eigen.equals(EigenschaftEnum.CH) || eigen.equals(EigenschaftEnum.FF)
                || eigen.equals(EigenschaftEnum.GE) || eigen.equals(EigenschaftEnum.KO)
                || eigen.equals(EigenschaftEnum.KK)) {

            // Der Maximale Wert plus die Modis aus Herkunft o.ä.
            return genLink.getWertModis() + Konstanten.getGenKonstanten().MAX_EIGENSCHAFT_WERT;

        } else if (eigen.equals(EigenschaftEnum.SO)) {

            return getMaxWertSO((GeneratorLink) link);
            
        } else if (eigen.equals(EigenschaftEnum.LEP)) {

            return getMaxWertLep((GeneratorLink) link);

        } else if (eigen.equals(EigenschaftEnum.ASP)) {

            return getMaxWertAsp((GeneratorLink) link);

        } else if (eigen.equals(EigenschaftEnum.AUP)) {

            return getMaxWertAup((GeneratorLink) link);

        } else if (eigen.equals(EigenschaftEnum.KA)) {

            return getMaxWertKA((GeneratorLink) link);

        } else {
            // Alle anderen Werte können nicht von User gesetzt werden, ein Maximum ist unnötig
            return 100;
        }
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMinWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	public int getMinWert(Link link) {
        final EigenschaftEnum eigen = ((Eigenschaft) link.getZiel()).getEigenschaftEnum();
        int minMoeglicherWert;

        if (eigen.equals(EigenschaftEnum.MU) || eigen.equals(EigenschaftEnum.KL) || eigen.equals(EigenschaftEnum.IN)
                || eigen.equals(EigenschaftEnum.CH) || eigen.equals(EigenschaftEnum.FF)
                || eigen.equals(EigenschaftEnum.GE) || eigen.equals(EigenschaftEnum.KO)
                || eigen.equals(EigenschaftEnum.KK)) {
        	
            // Der Mininale Wert plus die Modis aus Herkunft
            minMoeglicherWert = ((GeneratorLink) link).getWertModis()  + Konstanten.getGenKonstanten().MIN_EIGENSCHAFT_WERT;
            ExtendedProzessorTalent ept = (ExtendedProzessorTalent) held.getProzessor(CharKomponente.talent).getExtendedFunctions();
            ExtendedProzessorZauber epz = (ExtendedProzessorZauber) held.getProzessor(CharKomponente.zauber).getExtendedFunctions();
            	
            // Prüfung des minimalen Wertes durch die Talente
            minMoeglicherWert = 
            		ProzessorUtilities.getMinEigenschaftWert(
            				(List<HeldenLink>) ept.getTalentList(eigen), 
            				(Eigenschaft) link.getZiel(), 
            				held,
            				minMoeglicherWert);
            
            // Prüfung des minimalen Wertes durch die Zauber
            minMoeglicherWert = 
            		ProzessorUtilities.getMinEigenschaftWert(
            				(List<HeldenLink>) epz.getZauberList(eigen), 
            				(Eigenschaft) link.getZiel(), 
            				held,
            				minMoeglicherWert);

            return minMoeglicherWert;

        } else if (eigen.equals(EigenschaftEnum.SO)) {

        	// Voraussetzungen sollten durch den VoraussetzungsAdmin geprüft werden
            return 1;

        } else if (eigen.equals(EigenschaftEnum.LEP)) {
        	
        	return getMinWertLep((GeneratorLink) link);

        } else if (eigen.equals(EigenschaftEnum.ASP)) {

        	return getMinWertAsp((GeneratorLink) link);
        	
        } else if (eigen.equals(EigenschaftEnum.AUP)) {
        	
        	return getMinWertAup((GeneratorLink) link);

        } else if (eigen.equals(EigenschaftEnum.KA)) {
            // Siehe S. 26 im Aventurische Götterdiener
            return 0;

        } else {
            // Alle anderen Werte können nicht von User gesetzt werden, ein Minimum ist unnötig
            return 0;
        }
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#removeModi(E, org.d3s.alricg.charKomponenten.links.Link)
	 */
	public void removeModi(GeneratorLink heldLink, IdLink element) {

        // Link entfernen
        heldLink.removeLink(element);

        // Stufe ggf. neu setzen
        inspectEigenschaftWert(heldLink);

        // Kosten aktualisieren
        updateKosten(heldLink);
		
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateText(E, java.lang.String)
	 */
	public void updateText(GeneratorLink link, String text) {
		// Noop - Kann nicht geändert werden
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateWert(E, int)
	 */
	public void updateWert(GeneratorLink link, int wert) {
	       // Test das die Stufe nicht negativ wird
        assert (wert > 0);
        // Bestimmte Eigenschaften können nicht direkt gesetzt werden
        assert (!(((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.MR)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.AT)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.FK)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.INI)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.PA)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.KA) || ((Eigenschaft) link
                .getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.GS)));

        link.setUserGesamtWert(wert);

        // Kosten neu berechnen
        updateKosten(link);
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Noop - Kann nicht geändert werden
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	public GeneratorLink addNewElement(Eigenschaft ziel) {
		final GeneratorLink tmpLink;
		
		//Link wird erstellt und zur List hinzugefügt
		tmpLink = new GeneratorLink(ziel, null, null, 0);
		elementBox.add(tmpLink);
		
		updateKosten(tmpLink); // Kosten Aktualisieren
		
		return tmpLink;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canAddElement(ZIEL)
	 */
	public boolean canAddElement(Eigenschaft ziel) {
        // Nach der Initialisierung können keine neuen Eigenschaften hinzugefügt werden
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	public boolean canRemoveElement(GeneratorLink element) {
		// Eigenschaften können nicht entfernt werden
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	public void removeElement(GeneratorLink element) {
		// Noop - Eigenschaften können nicht entfernt werden
	}
	
    /**
     * Versucht überprüft ob der Wert des Elements "link" innerhalb der möglichen Grenzen ist. Wenn nicht wird versucht
     * den Wert entsprechend zu setzten. Diese Methode wird beim Ändern der Herkunft benötigt.
     * 
     * @param link Der Link der überprüft werden soll
     * @param prozessor Der ProzessorXX mit dem der Link überprüft wird
     */
    private void inspectEigenschaftWert(GeneratorLink tmpLink) {
        final EigenschaftEnum eigen = ((Eigenschaft) tmpLink.getZiel()).getEigenschaftEnum();
        int tmpInt = 0;

        // Aktuellen Wert bestimmen. (Errechnte Werte können nur über den Held bestimmt werden)
        tmpInt = held.getEigenschaftsWert(eigen);
        
        if (tmpInt > 0) {
            // Die Errechneten Werte müssen anderes behandelt werden
            if (tmpInt > getMaxWert(tmpLink)) {
                tmpLink.setUserGesamtWert(getMaxWert(tmpLink));
            } else if (tmpInt < getMinWert(tmpLink)) {
                tmpLink.setUserGesamtWert(getMinWert(tmpLink));
            }
        } else {
            // Nicht errechnete Werte
            ProzessorUtilities.inspectWert(tmpLink, this);
        }
    }
    
    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.prozessor.generierung.AbstractBoxGen#updateKosten(org.d3s.alricg.held.GeneratorLink)
     */
    public void updateKosten(GeneratorLink genLink) {
        final EigenschaftEnum eigen = ((Eigenschaft) genLink.getZiel()).getEigenschaftEnum();
        int kosten = 0;
        final int alteKosten;
        KostenKlasse KK = null;

        // Alte Kosten merken
        alteKosten = genLink.getKosten();

        // KostenKlasse festlegen, wenn diese nach SKT berechent wird
        if (eigen.equals(EigenschaftEnum.LEP)) {
            KK = KostenKlasse.H;
        } else if (eigen.equals(EigenschaftEnum.ASP)) {
            KK = KostenKlasse.G;
        } else if (eigen.equals(EigenschaftEnum.AUP)) {
            KK = KostenKlasse.E;
        } else if (eigen.equals(EigenschaftEnum.KA)) {
            KK = KostenKlasse.F;
        }

        if (KK != null) {
            // Berechnung mit SKT

            // Die Kosten-Kategorie als Nachricht absenden
        	notepad.writeMessage(TEXT_SKT_SPALTE + KK.getValue());

            // Kostenklasse mit Sonderregeln überprüfen
            KK = sonderregelAdmin.changeKostenKlasse(KK, genLink);

            // Kosten berechnen
            if (genLink.getUserLink() != null) {
                kosten = FormelSammlung.berechneSktKosten(0, genLink.getUserLink().getWert(), KK);
            } else {
                kosten = 0;
            }

        } else if (eigen.equals(EigenschaftEnum.MU) || eigen.equals(EigenschaftEnum.KL)
                || eigen.equals(EigenschaftEnum.IN) || eigen.equals(EigenschaftEnum.CH)
                || eigen.equals(EigenschaftEnum.FF) || eigen.equals(EigenschaftEnum.GE)
                || eigen.equals(EigenschaftEnum.KO) || eigen.equals(EigenschaftEnum.KK)
                || eigen.equals(EigenschaftEnum.SO)) {
            // Berechnung ohne SKT

            // Die Kosten entsprechen hier dem Gewählten Wert
            if (genLink.getUserLink() != null) {
                kosten = genLink.getUserLink().getWert();
            }

        } else {
            // keine Kosten, da dies errechnete Werte sind oder KA (durch Vorteile)
            return;
        }

        // Kosten als Nachricht
        notepad.writeMessage(TEXT_GESAMT_KOSTEN + kosten);
        
        // Kosten mit Sonderregeln überprüfen
        kosten = sonderregelAdmin.changeKosten(kosten, genLink);

        genLink.setKosten(kosten);

        if (KK != null) {
            // TalentGp
            eigenschaftTalentGpKosten += kosten - alteKosten; // Gesamtkosten setzen
        } else {
            // "echte" GP
            eigenschaftGpKosten += kosten - alteKosten; // Gesamtkosten setzen
        }

    }
    
    // Extendet Methoden
    
    /**
     * Die Kosten für die Talent-GP
     * 
     * @return Die Kosten die mit Talent GP bezahlt werden (ASP, LEP, AUP)
     * @see getGesamtKosten()
     */
    public int getGesamtTalentGpKosten() {
        return eigenschaftTalentGpKosten;
    }
    
    /**
     * Berechnet den Maximalwert für die Eigenschaft Sozialstatus
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertSO(GeneratorLink link) {
        final List<IdLink> linkList;

        // Der Maximale Wert plus die Modis aus Herkunft o.ä.
        return link.getWertModis() + Konstanten.getGenKonstanten().MAX_SOZIALSTATUS;
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Lebensenergie
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertLep(GeneratorLink link) {
        int tmpInt;

        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!
        // tmpInt = Der Wert ger Maximal hinzugekauft werden darf ( = KO /2)
        tmpInt = Math.round(elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert() / 2);

        // Der errechnete Wert + die Modis = das Maximum was hinzugekauft werden darf
        return FormelSammlung.berechneLep(
        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.KK.getId()).getWert())
	                + link.getWertModis() 
	                + tmpInt;
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Ausdauer
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertAup(GeneratorLink link) {
        int tmpInt;

        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!
        // tmpInt = Der Wert ger Maximal hinzugekauft werden darf ( = KO * 2)
        tmpInt = elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert() * 2;

        // Der errechnete Wert + die Modis + das Maximum was hinzugekauft werden darf
        return FormelSammlung.berechneAup(
        		elementBox.getObjectById(EigenschaftEnum.MU.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert(), 
        		elementBox.getObjectById(EigenschaftEnum.GE.getId()).getWert())
                	+ link.getWertModis() + tmpInt;
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Astralenergie
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertAsp(GeneratorLink link) {
        int tmpInt;

        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!
        // tmpInt = Der Wert ger Maximal hinzugekauft werden darf ( = CH x 1,5 ) Siehe unten
        tmpInt = (int) Math.round( elementBox.getObjectById(EigenschaftEnum.CH.getId()).getWert() * 1.5d );

        // Der errechnete Wert + die Modis + das Maximum was hinzugekauft werden darf
        return FormelSammlung.berechneAsp(
        		elementBox.getObjectById(EigenschaftEnum.MU.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.IN.getId()).getWert(), 
        		elementBox.getObjectById(EigenschaftEnum.CH.getId()).getWert())
                	+ link.getWertModis() 
                	+ tmpInt;
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Karmaenergie
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertKA(GeneratorLink link) {
        final List<IdLink> linkList;

        // Siehe S. 26 im Aventurische Götterdiener
        return elementBox.getObjectById(EigenschaftEnum.IN.getId()).getWert();
    }
    
    
    
    
    /**
     * Berechnet den Maximalwert für die Eigenschaft Lebensenergie
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMinWertLep(GeneratorLink link) {
        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!

        // Minimal ist der Wert ohne zukäufe durch den User
        return FormelSammlung.berechneLep(
        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.KK.getId()).getWert())
                	+ ((GeneratorLink) link).getWertModis();
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Ausdauer
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMinWertAup(GeneratorLink link) {
        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!

    	// Minimal ist der Wert ohne zukäufe durch den User
        return FormelSammlung.berechneAup(
        		elementBox.getObjectById(EigenschaftEnum.MU.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.GE.getId()).getWert())
                	+ ((GeneratorLink) link).getWertModis();
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Astralenergie
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMinWertAsp(GeneratorLink link) {
        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!

        // Minimal ist der Wert ohne zukäufe durch den User
        return FormelSammlung.berechneAsp(
        		elementBox.getObjectById(EigenschaftEnum.MU.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.IN.getId()).getWert(), 
        		elementBox.getObjectById(EigenschaftEnum.CH.getId()).getWert())
                	+ ((GeneratorLink) link).getWertModis();
    }

}
