/*
 * Created on 19.06.2006 / 12:15:32
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.generator.prozessor.charElemente;

import java.util.Iterator;
import java.util.List;

import org.d3s.alricg.common.Notepad;
import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.ElementBox;
import org.d3s.alricg.common.charakter.ExtendedProzessorEigenschaftCommon;
import org.d3s.alricg.common.logic.BaseProzessorElementBox;
import org.d3s.alricg.common.logic.FormelSammlung;
import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.generator.prozessor.GeneratorProzessor;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorTalent;
import org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorZauber;
import org.d3s.alricg.generator.prozessor.utils.ProzessorUtilities;
import org.d3s.alricg.store.access.StoreDataAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.held.HeldenLink;
import org.d3s.alricg.store.rules.RegelConfig;

/**
 * <u>Beschreibung:</u><br> 
 * Prozessor für das Bearbeiten von Eigenschaften. Alle Änderungen die an Eigenschaften
 * durchgeführt werden, werden über diesen Prozessor durchgeführt.
 *  
 * @author V. Strelow
 */
public class ProzessorEigenschaften extends BaseProzessorElementBox<Eigenschaft, GeneratorLink> 
									implements GeneratorProzessor<Eigenschaft, GeneratorLink>, 
												ExtendedProzessorEigenschaft, 
												ExtendedProzessorEigenschaftCommon {
	
    private static final boolean STUFE_ERHALTEN = true;
    
	private final String TEXT_SKT_SPALTE = "Original SKT-Spalte: ";
	private final String TEXT_GESAMT_KOSTEN = "Gesamt Kosten: ";
	
    private int eigenschaftGpKosten = 0;
    private int eigenschaftTalentGpKosten = 0;

    private final Charakter held;
    
    /**
     * Konstruktor, setzt die initialen Werte aller Eigenschaften
     * @param held Held zu dem dieser Prozessor gehört
     */
    public ProzessorEigenschaften(Charakter held) {
		this.held = held;
		this.elementBox = new ElementBox<GeneratorLink>();
		
    	// Alle Eigenschaften laden
    	final Iterator<Eigenschaft> ite =  StoreDataAccessor.getInstance().getEigenschaftList().iterator();
    	while(ite.hasNext()) {
    		final Eigenschaft tmpEig = ite.next();
    		int wert = 0;
    		
    		// Die "Basis-Eigenschaften" auf 8 setzen
    		if (tmpEig.getEigenschaftEnum() == EigenschaftEnum.MU
    				|| tmpEig.getEigenschaftEnum() == EigenschaftEnum.KL
    				|| tmpEig.getEigenschaftEnum() == EigenschaftEnum.IN
    				|| tmpEig.getEigenschaftEnum() == EigenschaftEnum.CH
    				|| tmpEig.getEigenschaftEnum() == EigenschaftEnum.FF
    				|| tmpEig.getEigenschaftEnum() == EigenschaftEnum.GE
    				|| tmpEig.getEigenschaftEnum() == EigenschaftEnum.KO
    				|| tmpEig.getEigenschaftEnum() == EigenschaftEnum.KK ) {
    			wert = 8;
    		} else if (tmpEig.getEigenschaftEnum() == EigenschaftEnum.SO) {
    			wert = 1;
    		}
    		
    		elementBox.add(new GeneratorLink(tmpEig, null, null, wert));
    	}
    	
    	updateAllKosten();
    }
    
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#addModi(org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
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
	@Override
	public boolean canAddElement(Link link) {
        // Zu Eigenschaften können keine neuen Elemente hinzugefügt werden
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateText(E)
	 */
	@Override
	public boolean canUpdateText(GeneratorLink link) {
        // Es gibt keinen Text bei Eigenschaften
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateWert(E)
	 */
	@Override
	public boolean canUpdateWert(GeneratorLink link) {
        // Grundsätzlich können Werte bei Eigenschaften geändert werden
        final EigenschaftEnum eigen = ((Eigenschaft) link.getZiel()).getEigenschaftEnum();

        if (eigen.equals(EigenschaftEnum.MR) || eigen.equals(EigenschaftEnum.AT) || eigen.equals(EigenschaftEnum.FK)
                || eigen.equals(EigenschaftEnum.INI) || eigen.equals(EigenschaftEnum.PA)
                || eigen.equals(EigenschaftEnum.KA) || eigen.equals(EigenschaftEnum.GS) 
                || eigen.equals(EigenschaftEnum.WSW) || eigen.equals(EigenschaftEnum.ESW)) {
            // Diese Eigenschaften können nicht direkt geändert werden
            return false;
        }

        return true;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#canUpdateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	@Override
	public boolean canUpdateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
        // Es gibt kein Zweitziel bei Eigenschaften
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#containsLink(org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public boolean containsLink(Link link) {
		return ( elementBox.getObjectById(link.getZiel()) != null );
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getGesamtKosten()
	 */
	@Override
	public double getGesamtKosten() {
		return eigenschaftGpKosten;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#getMaxWert(org.d3s.alricg.charKomponenten.links.Link)
	 */
	@Override
	public int getMaxWert(Link link) {
        final EigenschaftEnum eigen = ((Eigenschaft) link.getZiel()).getEigenschaftEnum();
        final GeneratorLink genLink = (GeneratorLink) link;
        int tmpInt;
        
        if (eigen.equals(EigenschaftEnum.MU) || eigen.equals(EigenschaftEnum.KL) || eigen.equals(EigenschaftEnum.IN)
                || eigen.equals(EigenschaftEnum.CH) || eigen.equals(EigenschaftEnum.FF)
                || eigen.equals(EigenschaftEnum.GE) || eigen.equals(EigenschaftEnum.KO)
                || eigen.equals(EigenschaftEnum.KK)) {

            // Der Maximale Wert plus die Modis aus Herkunft o.ä.
            return genLink.getWertModis() + RegelConfig.getInstance().getMaxEigenschafWert();

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
	@Override
	public int getMinWert(Link link) {
        final EigenschaftEnum eigen = ((Eigenschaft) link.getZiel()).getEigenschaftEnum();
        int minMoeglicherWert;

        if (eigen.equals(EigenschaftEnum.MU) || eigen.equals(EigenschaftEnum.KL) 
        		|| eigen.equals(EigenschaftEnum.IN) || eigen.equals(EigenschaftEnum.CH) 
        		|| eigen.equals(EigenschaftEnum.FF) || eigen.equals(EigenschaftEnum.GE) 
        		|| eigen.equals(EigenschaftEnum.KO) || eigen.equals(EigenschaftEnum.KK)) {
        	
            // Der Mininale Wert plus die Modis aus Herkunft
            minMoeglicherWert = ((GeneratorLink) link).getWertModis()  + RegelConfig.getInstance().getMinEigenschafWert();
            ExtendedProzessorTalent ept = (ExtendedProzessorTalent) held.getProzessor(Talent.class).getExtendedInterface();
            ExtendedProzessorZauber epz = (ExtendedProzessorZauber) held.getProzessor(Zauber.class).getExtendedInterface();
            	
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
	@Override
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
	@Override
	public void updateText(GeneratorLink link, String text) {
		// Noop - Kann nicht geändert werden
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateWert(E, int)
	 */
	@Override
	public void updateWert(GeneratorLink link, int wert) {
	       // Test das die Stufe nicht negativ wird
        assert (wert > 0);
        // Bestimmte Eigenschaften können nicht direkt gesetzt werden
        assert (!(((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.MR)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.AT)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.FK)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.INI)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.PA)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.KA) 
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.GS)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.WSW)
                || ((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.ESW)));
        
        link.setUserGesamtWert(wert);

        // Kosten neu berechnen
        updateKosten(link);
        
        // Automatisches Update abhängiger Werte, soweit nötig
        // Gekaufte Werte werden gesenkt, wenn sie das zulässige Maximum überschreiten
        
        if (((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.KO)) {
        	// Prüfen ob zugekaufte Lep noch gültig sind
        	if (elementBox.getObjectById(EigenschaftEnum.LEP.getId()).getUserLink().getWert()
        			> this.getMaxZukauf(EigenschaftEnum.LEP)) {
        		
        		held.getProzessor(Eigenschaft.class).updateWert(
        				elementBox.getObjectById(EigenschaftEnum.LEP.getId()),
        				this.getMaxZukauf(EigenschaftEnum.LEP)
    				 		+ elementBox.getObjectById(EigenschaftEnum.LEP.getId()).getWertModis()
        			);
        	}
        	
        	// Prüfen ob zugekaufte Aup noch gültig sind
        	if (elementBox.getObjectById(EigenschaftEnum.AUP.getId()).getUserLink().getWert()
        			> this.getMaxZukauf(EigenschaftEnum.AUP)) {
        		
        		held.getProzessor(Eigenschaft.class).updateWert(
        				elementBox.getObjectById(EigenschaftEnum.AUP.getId()),
        				this.getMaxZukauf(EigenschaftEnum.AUP)
        				 	+ elementBox.getObjectById(EigenschaftEnum.AUP.getId()).getWertModis()
        			);
        	}
        	
        } else if (((Eigenschaft) link.getZiel()).getEigenschaftEnum().equals(EigenschaftEnum.CH)) {
        	
        	// Prüfen ob zugekaufte Asp noch gültig sind
        	if (elementBox.getObjectById(EigenschaftEnum.ASP.getId()).getUserLink().getWert()
        			> this.getMaxZukauf(EigenschaftEnum.ASP)) {
        		
        		held.getProzessor(Eigenschaft.class).updateWert(
        				elementBox.getObjectById(EigenschaftEnum.ASP.getId()),
        				this.getMaxZukauf(EigenschaftEnum.ASP)
				 			+ elementBox.getObjectById(EigenschaftEnum.ASP.getId()).getWertModis()
        			);
        	}
        }
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateZweitZiel(E, org.d3s.alricg.charKomponenten.CharElement)
	 */
	@Override
	public void updateZweitZiel(GeneratorLink link, CharElement zweitZiel) {
		// Noop - Kann nicht geändert werden
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#addNewElement(ZIEL)
	 */
	@Override
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
	@Override
	public boolean canAddElement(Eigenschaft ziel) {
        // Nach der Initialisierung können keine neuen Eigenschaften hinzugefügt werden
        return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#canRemoveElement(ELEM)
	 */
	@Override
	public boolean canRemoveElement(GeneratorLink element) {
		// Eigenschaften können nicht entfernt werden
		return false;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.prozessor.Prozessor#removeElement(ELEM)
	 */
	@Override
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
        tmpInt = this.getEigenschaftsWert(eigen);
        
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
    @Override
    public void updateKosten(GeneratorLink genLink) {
        final EigenschaftEnum eigen = ((Eigenschaft) genLink.getZiel()).getEigenschaftEnum();
        double kosten = 0;
        final double alteKosten;
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
        	Notepad.writeMessage(TEXT_SKT_SPALTE + KK.getValue());

            // Kostenklasse mit Sonderregeln überprüfen
            KK = held.getSonderregelAdmin().changeKostenKlasse(KK, genLink);

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
        Notepad.writeMessage(TEXT_GESAMT_KOSTEN + kosten);
        
        // Kosten mit Sonderregeln überprüfen
        kosten = held.getSonderregelAdmin().changeKosten(kosten, genLink);

        genLink.setKosten(kosten);

        if (KK != null) {
            // TalentGp
            eigenschaftTalentGpKosten += kosten - alteKosten; // Gesamtkosten setzen
        } else {
            // "echte" GP
            eigenschaftGpKosten += kosten - alteKosten; // Gesamtkosten setzen
        }

    }
    
	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.prozessor.LinkProzessor#updateAllKosten()
	 */
    @Override
    public void updateAllKosten() {
    	Iterator<GeneratorLink> ite = elementBox.getUnmodifiableIterator();
    	
    	while (ite.hasNext()) {
    		this.updateKosten(ite.next());
    	}
	}
    
    // Extendet Methoden

	/**
     * Die Kosten für die Talent-GP
     * 
     * @return Die Kosten die mit Talent GP bezahlt werden (ASP, LEP, AUP)
     * @see getGesamtKosten()
     */
    @Override
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
        return link.getWertModis() + RegelConfig.getInstance().getMaxSozialstatus();
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Lebensenergie
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertLep(GeneratorLink link) {
        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!

        // Der errechnete Wert + die Modis = das Maximum was hinzugekauft werden darf
        return FormelSammlung.berechneLep(
        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.KK.getId()).getWert())
	                + link.getWertModis() 
	                + getMaxZukauf(EigenschaftEnum.LEP);
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Ausdauer
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertAup(GeneratorLink link) {
        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!

        // Der errechnete Wert + die Modis + das Maximum was hinzugekauft werden darf
        return FormelSammlung.berechneAup(
        		elementBox.getObjectById(EigenschaftEnum.MU.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert(), 
        		elementBox.getObjectById(EigenschaftEnum.GE.getId()).getWert())
                	+ link.getWertModis() 
                	+ getMaxZukauf(EigenschaftEnum.AUP);
    }

    /**
     * Berechnet den Maximalwert für die Eigenschaft Astralenergie
     * 
     * @param link Der Link mit der Eigenschaft
     * @return Der maximale Wert der Eigenschaft
     */
    private int getMaxWertAsp(GeneratorLink link) {
        // Selbst nicht beschränkt, nur die Anzahl die Hinzugekauft werden kann!

        // Der errechnete Wert + die Modis + das Maximum was hinzugekauft werden darf
        return FormelSammlung.berechneAsp(
        		elementBox.getObjectById(EigenschaftEnum.MU.getId()).getWert(),
        		elementBox.getObjectById(EigenschaftEnum.IN.getId()).getWert(), 
        		elementBox.getObjectById(EigenschaftEnum.CH.getId()).getWert())
                	+ link.getWertModis() 
                	+ getMaxZukauf(EigenschaftEnum.ASP);
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

    
    
	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorEigenschaft#getEigenschaftsLink(org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum)
	 */
	@Override
	public GeneratorLink<Eigenschaft> getEigenschaftsLink(EigenschaftEnum eigen) {
		return this.elementBox.getObjectById(eigen.getEigenschaft());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorEigenschaft#getEigenschaftsWert(org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum)
	 */
	@Override
	public int getEigenschaftsWert(EigenschaftEnum eigen) {
		switch (eigen) {
		
		case LEP: return FormelSammlung.berechneLep(
				elementBox.getObjectById(EigenschaftEnum.KO.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.KK.getEigenschaft()).getWert()		
			)
			+ elementBox.getObjectById(EigenschaftEnum.LEP.getEigenschaft()).getWert();
	
		case ASP: return FormelSammlung.berechneAsp(
				elementBox.getObjectById(EigenschaftEnum.MU.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.IN.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.CH.getEigenschaft()).getWert()
			)
			+ elementBox.getObjectById(EigenschaftEnum.ASP.getEigenschaft()).getWert();
			
		case AUP: return FormelSammlung.berechneAup(
				elementBox.getObjectById(EigenschaftEnum.MU.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.KO.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.GE.getEigenschaft()).getWert()
			)
			+ elementBox.getObjectById(EigenschaftEnum.AUP.getEigenschaft()).getWert();
		
		case AT: return FormelSammlung.berechneAtBasis(
				elementBox.getObjectById(EigenschaftEnum.MU.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.GE.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.KK.getEigenschaft()).getWert()
			)
			+ elementBox.getObjectById(EigenschaftEnum.AT.getEigenschaft()).getWert();
				
		case PA: return FormelSammlung.berechnePaBasis(
				elementBox.getObjectById(EigenschaftEnum.IN.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.GE.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.KK.getEigenschaft()).getWert()
			) 
			+ elementBox.getObjectById(EigenschaftEnum.PA.getEigenschaft()).getWert();
		
		case FK: return FormelSammlung.berechneFkBasis(
				elementBox.getObjectById(EigenschaftEnum.IN.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.FF.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.KK.getEigenschaft()).getWert()
			) 
			+ elementBox.getObjectById(EigenschaftEnum.FK.getEigenschaft()).getWert();
			
		case INI: return FormelSammlung.berechneINI(
				elementBox.getObjectById(EigenschaftEnum.MU.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.IN.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.GE.getEigenschaft()).getWert()
			) 
			+ elementBox.getObjectById(EigenschaftEnum.INI.getEigenschaft()).getWert();
			
		case MR: return FormelSammlung.berechneMR(
				elementBox.getObjectById(EigenschaftEnum.MU.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.KL.getEigenschaft()).getWert(),
				elementBox.getObjectById(EigenschaftEnum.KO.getEigenschaft()).getWert()
			) 
			+ elementBox.getObjectById(EigenschaftEnum.MR.getEigenschaft()).getWert();
		
		case GS: return FormelSammlung.berechneGS(
				elementBox.getObjectById(EigenschaftEnum.GE.getEigenschaft()).getWert()
			) 
			+ elementBox.getObjectById(EigenschaftEnum.GS.getEigenschaft()).getWert();
		
		case WSW: return FormelSammlung.berechneWundSW(
				elementBox.getObjectById(EigenschaftEnum.KO.getEigenschaft()).getWert()
			) 
			+ elementBox.getObjectById(EigenschaftEnum.WSW.getEigenschaft()).getWert();

		case ESW: return FormelSammlung.berechneErschoepfungsSW(
				elementBox.getObjectById(EigenschaftEnum.KO.getEigenschaft()).getWert()
			) 
			+ elementBox.getObjectById(EigenschaftEnum.ESW.getEigenschaft()).getWert();

		default: // Es handelt sich um MU - KK, KE oder SO
			// Da hier nicht errechnet werden muß, werden die Werte direkt geliefert
			return elementBox.getObjectById(eigen.getEigenschaft()).getWert();
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.common.logic.Prozessor#getExtendedInterface()
	 */
	@Override
	public ExtendedProzessorEigenschaft getExtendedInterface() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorEigenschaft#getMaxZukauf(org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum)
	 */
	@Override
	public int getMaxZukauf(EigenschaftEnum eigen) {
		switch (eigen) {
		case LEP:
			return FormelSammlung.berechneMaxLepHinzuKauf( 
	        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert());
		case AUP:
			return FormelSammlung.berechneMaxAupHinzuKauf( 
	        		elementBox.getObjectById(EigenschaftEnum.KO.getId()).getWert());
		case ASP:
			return FormelSammlung.berechneMaxAspHinzuKauf(
	        		elementBox.getObjectById(EigenschaftEnum.CH.getId()).getWert());
		default:
			throw new IllegalArgumentException("Konnte keine behandlung für " + eigen + " finden!");	
		}
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.generator.prozessor.extended.ExtendedProzessorEigenschaft#getMinZukauf(org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum)
	 */
	@Override
	public int getMinZukauf(EigenschaftEnum eigen) {
		return 0;
	}
	
}
