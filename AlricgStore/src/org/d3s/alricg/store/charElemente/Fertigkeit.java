/*
 * Created 26. Dezember 2004 / 23:36:19
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlSchemaType;


/**
 * <b>Beschreibung:</b><br>
 * Fasst gemeinsamkeiten von Vor-/ Nachteilen und Sonderfertigkeiten zusammmen und bildet 
 * die Grundlage für diese.
 * 
 * Fertigkeiten können ein "automatischesTalent" besitzen. Wenn eine Fertigkeit zum mit
 * "automatischesTalent" zum Helden hinzugefügt wird, wird das Talent ebenfalls hinzugefügt.
 * (analog natürlich entfernen). 
 * Dies ist nötig für Gaben, Ritualkenntis(Magie), Ritualkenntnis(Schamanen), Liturgiekenntnis
 * @author V.Strelow
 */
public abstract class Fertigkeit extends CharElement {
	@XmlEnum
	public enum FertigkeitArt {
		allgemein("allgemein"),
		waffenloskampf("waffenlosKampf"), 
		bewaffnetkampf("bewaffnetKampf"), 
		magisch("magisch"), 
		geweiht("geweiht"), 
		schamanisch("schamanisch"), 
		sonstige("sonstige");
		private String value; // Value des Elements
		
		private FertigkeitArt(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	private Talent automatischesTalent;
    private Werte.CharArten[] fuerWelcheChars; // Welche Chars diese Fertigkeit wählen können
	private FertigkeitArt art;
	
    private boolean isMitFreienText = false; // Gibt es noch einen frei zu wählenden Text zu der Fertigkeit? (Vorurteile gegen "Orks")
    private String[] textVorschlaege; // Eine Liste Möglicher angaben für den Text
    // Es ist möglich das eine Fertigkeit "hasFreienText = false" ist, aber Text Vorschläge besitzt
    // in diesem Fall können nur die Vorschläge gewählt werden, nichts anderes
    
    private boolean benoetigtZweitZiel = false; // Gibt es noch ein Element zu dieser Fertigkeit (Unfähigkeit "Schwerter")
    private boolean isWaehlbar = true; // Nicht wählbare können nur über die Herkunft erlangt werden

    private AdditionsFamilie additionsFamilie; // "Familie" von Fertigkeiten, die aufaddiert werden z.B. Rüstungsgewöhung I und RG II

    private int gpKosten;

     
    /**
     * Eine AdditionsFamilie kennzeichent solche Fertigkeiten, die Zusammengehören und bei mehrfachen erlangen durch Rasse,
     * Kultur, ... zusammengefasst werden. Zu was die Fertigkeiten zusammengefast werden wird über den additionsWert
     * festgelegt. Beispiel: Rüstungsgewöhung I (additiosnWert: 1) und RG II (aW: 2). Somit ergibt 2x RG I --> 1x RG II
     * 
     * @return Liefert die AdditionsFamilie
     */
    public AdditionsFamilie getAdditionsFamilie() {
        return additionsFamilie;
    }

    /**
     * Fertigkeiten mit Text benötigen noch einen Text wenn sie gewählt werden: z.B. Vorurteile gegen "Orks" oder
     * Verpflichtungen gegenüber "Praioskirche".
     * 
     * @return true - Die Fertigkeit benötigt noch die Angabe eines Textes, ansonsten "false".
     */
    @XmlAttribute
    public boolean isMitFreienText() {
        return isMitFreienText;
    }

    /**
     * Fertigkeiten die "Nicht wählbar" sind können nur über die Herkunft erlangt werden! D.h. diese stehen NICHT zur
     * normalen Auswahl.
     * 
     * @return true - Die Fertigkeit ist normal wählbar, ansonsten ist die Fertigk. NICHT wählbar (false)
     */
    @XmlAttribute
    public boolean isWaehlbar() {
        return isWaehlbar;
    }

	/**
	 * @return the fuerWelcheChars
	 */
	public Werte.CharArten[] getFuerWelcheChars() {
		return fuerWelcheChars;
	}

	/**
	 * @param fuerWelcheChars the fuerWelcheChars to set
	 */
	public void setFuerWelcheChars(Werte.CharArten[] fuerWelcheChars) {
		this.fuerWelcheChars = fuerWelcheChars;
	}

	/**
	 * @return the textVorschlaege
	 */
	public String[] getTextVorschlaege() {
		return textVorschlaege;
	}

	/**
	 * @param textVorschlaege the textVorschlaege to set
	 */
	public void setTextVorschlaege(String[] textVorschlaege) {
		this.textVorschlaege = textVorschlaege;
	}

	/**
	 * @return the benoetigtZweitZiel
	 */
	@XmlAttribute
	public boolean getBenoetigtZweitZiel() {
		return benoetigtZweitZiel;
	}

	/**
	 * @param benoetigtZweitZiel the benoetigtZweitZiel to set
	 */
	public void setBenoetigtZweitZiel(boolean benoetigtZweitZiel) {
		this.benoetigtZweitZiel = benoetigtZweitZiel;
	}

	/**
	 * @return the gpKosten
	 */
	@XmlAttribute
	public int getGpKosten() {
		return gpKosten;
	}

	/**
	 * @return Liefert das Attribut art.
	 */
	public FertigkeitArt getArt() {
		return art;
	}
	/**
	 * @param art Setzt das Attribut art.
	 */
	public void setArt(FertigkeitArt art) {
		this.art = art;
	}
	
	/**
	 * @param gpKosten the gpKosten to set
	 */
	public void setGpKosten(int gpKosten) {
		this.gpKosten = gpKosten;
	}

	/**
	 * @param isMitFreienText the isMitFreienText to set
	 */
	public void setMitFreienText(boolean isMitFreienText) {
		this.isMitFreienText = isMitFreienText;
	}

	/**
	 * @param isWaehlbar the isWaehlbar to set
	 */
	public void setWaehlbar(boolean isWaehlbar) {
		this.isWaehlbar = isWaehlbar;
	}

	/**
	 * @param additionsFamilie the additionsFamilie to set
	 */
	public void setAdditionsFamilie(AdditionsFamilie additionsFamilie) {
		this.additionsFamilie = additionsFamilie;
	}

	/**
	 * @return the automatischeTalent
	 */
	public Talent getAutomatischesTalent() {
		return automatischesTalent;
	}

	/**
	 * @param automatischeTalent the automatischeTalent to set
	 */
	public void setAutomatischesTalent(Talent automatischesTalent) {
		this.automatischesTalent = automatischesTalent;
	}
	
    /**
     * Eine additionsID kennzeichent solche Fertigkeiten, die Zusammengehören und bei mehrfachen erlangen durch Rasse,
     * Kultur, ... zusammengefasst werden. Zu was die Fertigkeiten zusammengefast werden wird über den additionsWert
     * festgelegt. Beispiel: Rüstungsgewöhung I (additiosnWert: 1) und RG II (aW: 2). Somit ergibt 2x RG I --> 1x RG II
     */
    public static class AdditionsFamilie {
        private String additionsID; 
    	private int additionsWert = KEIN_WERT; 
		/**
		 * Id einer "Familie" von Fertigkeiten, die aufaddiert werden z.B. 
		 * Rüstungsgewöhung I und RG II
		 * 
		 * @return the additionsID
		 */
    	@XmlAttribute
		public String getAdditionsID() {
			return additionsID;
		}
		/**
		 * @param additionsID the additionsID to set
		 */
		public void setAdditionsID(String additionsID) {
			this.additionsID = additionsID;
		}
		/**
		 * z.B. Rüstungsgewöhung I = 1 und RG II = 2. Somit ergibt zwei mal RG I -->
         * ein mal RG II (sieht AH S.10)
		 * @return the additionsWert
		 */
		@XmlAttribute
		@XmlSchemaType(name = "nonNegativeInteger")
		public int getAdditionsWert() {
			return additionsWert;
		}
		/**
		 * @param additionsWert the additionsWert to set
		 */
		public void setAdditionsWert(int additionsWert) {
			this.additionsWert = additionsWert;
		}
    }
}
