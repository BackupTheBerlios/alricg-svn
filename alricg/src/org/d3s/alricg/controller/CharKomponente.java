package org.d3s.alricg.controller;

import org.d3s.alricg.store.FactoryFinder;

/**
 * <u>Beschreibung:</u><br>
 * Eine Konstante für alle Charakter Komponenten die vorkommen können. Die einzehnen Konstanten 
 * sind mit Zusatzinformationen angereichert.
 * 
 * @author V. Strelow
 */
public enum CharKomponente {
    rasse("rassen", "RAS"), kultur("kulturen", "KUL"), profession("professionen", "PRO"), 
    rasseVariante("variante", "RAV"), kulturVariante("variante", "KUV"), professionVariante("variante", "PRV"),
    zusatzProfession("zusatzProfessionen", "ZPR"), vorteil("vorteile", "VOR"), gabe("vorteileGaben", "GAB"), nachteil(
    "nachteile", "NAC"), sonderfertigkeit("sonderfertigkeiten", "SF"), ritLitKenntnis(
    "liturgieRitualKenntnise", "LRK"), talent("talente", "TAL"), sprache("sprachen", "SPR"), schrift(
    "schriften", "SFT"), zauber("zauber", "ZAU"), liturgie("liturgien", "LIT"), ritual("rituale", "RIT"), ausruestung(
    "ausruestung", "AUS"), fahrzeug("fahrzeuge", "FAH"), waffeNk("nkWaffen", "NKW"), waffeFk("fkWaffen", "FKW"), ruestung(
    "ruestungen", "RUE"), schild("schilde", "SLD"), daemonenPakt("daemonenPakte", "DAE"), schwarzeGabe(
    "schwarzeGaben", "SGA"), tier("tiere", "TIE"), region("regionen", "REG"), gottheit("gottheiten", "GOT"), repraesentation(
    "repraesentationen", "REP"), eigenschaft("x", "EIG"), sonderregel("x", "SR");

    private String kategorie; // Kategorie
    private String bezeichung; // Name der CharKomponente
    private String prefix; // Prefix der ID aller entsprechenden Elemente

    private CharKomponente(String kategorie, String prefix) {
        this.kategorie = kategorie;
        this.prefix = prefix;
        
        bezeichung = FactoryFinder.find().getLibrary().getShortTxt(prefix);
    }

    public String getKategorie() {
        return kategorie;
    }

    public String getPrefix() {
        return prefix;
    }
    
    /**
     * @return Den Namen der CharKomponente so wie er angezeigt werden kann
     */
    public String getBezeichnung() {
    	return bezeichung;
    }
}
