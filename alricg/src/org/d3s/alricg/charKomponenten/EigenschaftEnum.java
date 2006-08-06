/*
 * Created 22. Dezember 2004 / 13:10:45
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import java.util.ArrayList;

import org.d3s.alricg.store.FactoryFinder;

/**
 * <b>Beschreibung:</b><br>
 * Hilfsklasse zum besseren Arbeiten mit Eigenschaften. 
 * 
 * @author V.Strelow
 */
public enum EigenschaftEnum {
    MU("Mut", 			"MU", "EIG-MU"), 
    KL("Klugheit", 		"KL", "EIG-KL"), 
    IN("Intuition", 	"IN", "EIG-IN"), 
    CH("Charisma", 		"CH", "EIG-CH"), 
    FF("Fingerfertigkeit", "FF", "EIG-FF"),  
    GE("Gewandheit", 	"GE", "EIG-GE"), 
    KO("Konstitution", 	"KO", "EIG-KO"), 
    KK("Koerperkraft", 	"KK", "EIG-KK"),
    
    SO("Sozialstatus",	"SO", "EIG-SO"),
    MR("Magieresistenz", "MR", "EIG-MR"),
    
    LEP("Lebenspunkte", "LeP", "EIG-Lep"), 
    ASP("Astralpunkte", "AsP", "EIG-AsP"), 
    AUP("Ausdauerpunkt", "AuP", "EIG-AuP"), 
    KA("Karmaernergie", "KA", "EIG-KA"), 
    
    GS("Geschwindigkeit", "GS", "EIG-GS"),
    INI("Initiative", 	"INI", "EIG-INI"),
    FK("FernkampfBasis", "FK", "EIG-FK"),
    AT("AttackeBasis", 	"AT", "EIG-AT"),
    PA("ParadeBasis", 	"PA", "EIG-PA");
    
    
    private String name; // Voller Name der Eigenschaft
    private String abk; // Abkürzung der Eigenschaft
    private String id; // ID der Eigenschaft

    /**
     * @param bezeichnung Key für Library für den vollen Namen
     * @param abkuerzung Key für Library für die Akkürzung des Namens
     */
    private EigenschaftEnum (String bezeichnung, String abkuerzung, String id) {
    	name = FactoryFinder.find().getLibrary().getShortTxt(bezeichnung);
    	abk = FactoryFinder.find().getLibrary().getShortTxt(abkuerzung);
    	this.id = id;
    }

    /** 
     * @return Die ID für diese Eigenschaft / gleichZeitig der XmlValue
     */
    public String getId() {
    	return id;
    }
    
    /**
     * @return Den vollständigen Namen der Eigenschaft
     */
    public String getBezeichnung()  {
        return name;
    }
    
    /**
     * @return Der Wert der Eigenschaft/ geichzeitig die ID
     */
    public String getValue() {
    	return id;
    }
    
    /**
     * @return Die Abkürzung des Namens der Eigenschaft
     */
    public String getAbk()  {
        return abk;
    }
    
    /* (non-Javadoc) Methode überschrieben
     * @see java.lang.Object#toString()
     */
    public String toString()  {
        return abk;
    }
    

    
    /**
     * Diese Methode wird vor allem für die initialisierung benötigt!
     * @return Eine ArrayList mit den IDs aller Eigenschaften
     */
    public static ArrayList<String> getIdArray() {
    	ArrayList<String> ids;
    	
    	ids = new ArrayList<String>(EigenschaftEnum.values().length);
    	
    	for (int i = 0; i < EigenschaftEnum.values().length; i++) {
    		ids.add(EigenschaftEnum.values()[i].getValue());
    	}
    	
    	return ids;
    }
    
    /**
     * @return Gibt die anzahl aller Eigenschaften dieser Klasse an
     */
	public static int getAnzahlEigenschaften() {
        return EigenschaftEnum.values().length;
	}
}

