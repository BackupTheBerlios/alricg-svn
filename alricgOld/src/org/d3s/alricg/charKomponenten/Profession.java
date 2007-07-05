/*
 * Created 22. Dezember 2004 / 13:07:41
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import org.d3s.alricg.charKomponenten.Werte.Gilde;
import org.d3s.alricg.charKomponenten.Werte.MagieMerkmal;
import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.AuswahlAusruestung;
import org.d3s.alricg.controller.CharKomponente;

/**
 * <b>Beschreibung:</b><br>
 * Repräsentiert eine Profession, speichert alle nötigen Daten.
 * 
 * @author V.Strelow
 */
public class Profession extends Herkunft {

    public enum Art {
        handwerklich("handwerklich"), kriegerisch("kriegerisch"), gesellschaftlich("gesellschaftlich"), 
        	wildnis("wildnis"), magisch("magisch"), geweiht("geweiht"), schamanisch("schamanisch");
        
        private String value; // VALUE des Elements

        private Art(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Aufwand {
        erstprof("erstprofession"), zeitaufw("zeitaufwendig"), normal("-");

        private String value; // Value des Elements

        private Aufwand(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private Aufwand aufwand;

    private Art art;

    private MagierAkademie magierAkademie;

    private Auswahl sprachen;

    private Auswahl schriften;

    private AuswahlAusruestung ausruestung;

    private AuswahlAusruestung besondererBesitz;

    private Gottheit geweihtGottheit; // Falls die Pro Geweiht ist: Der Name des Ritus
    // Modis auf LiturgieKenntnis, Geister rufen, usw.; Der Name des Ritus steht mit im Link

    private Auswahl ritusModis;

    private ProfessionVariante[] varianten;

    /*
     * (non-Javadoc) Methode überschrieben
     * 
     * @see org.d3s.alricg.charKomponenten.CharElement#getCharKomponente()
     */
    public CharKomponente getCharKomponente() {
        return CharKomponente.profession;
    }

    /**
     * Konstruktur; id beginnt mit "PRO-" für Profession
     * 
     * @param id Systemweit eindeutige id
     */
    public Profession(String id) {
        setId(id);
    }

    /**
     * @return Liefert das Attribut art.
     */
    public Art getArt() {
        return art;
    }

    /**
     * @param art Setzt das Attribut art.
     */
    public void setArt(Art art) {
        this.art = art;
    }

    /**
     * @return Liefert das Attribut aufwand.
     */
    public Aufwand getAufwand() {
        return aufwand;
    }

    /**
     * @param aufwand Setzt das Attribut aufwand.
     */
    public void setAufwand(Aufwand aufwand) {
        this.aufwand = aufwand;
    }

    /**
     * @return Liefert das Attribut ausruestung.
     */
    public AuswahlAusruestung getAusruestung() {
        return ausruestung;
    }

    /**
     * @param ausruestung Setzt das Attribut ausruestung.
     */
    public void setAusruestung(AuswahlAusruestung ausruestung) {
        this.ausruestung = ausruestung;
    }

    /**
     * @return Liefert das Attribut besondererBesitz.
     */
    public AuswahlAusruestung getBesondererBesitz() {
        return besondererBesitz;
    }

    /**
     * @param besondererBesitz Setzt das Attribut besondererBesitz.
     */
    public void setBesondererBesitz(AuswahlAusruestung besondererBesitz) {
        this.besondererBesitz = besondererBesitz;
    }

    /**
     * @return Liefert das Attribut geweihtGottheit.
     */
    public Gottheit getGeweihtGottheit() {
        return geweihtGottheit;
    }

    /**
     * @param geweihtGottheit Setzt das Attribut geweihtGottheit.
     */
    public void setGeweihtGottheit(Gottheit geweihtGottheit) {
        this.geweihtGottheit = geweihtGottheit;
    }

    /**
     * @return Liefert das Attribut magierAkademie.
     */
    public MagierAkademie getMagierAkademie() {
        return magierAkademie;
    }

    /**
     * @param magierAkademie Setzt das Attribut magierAkademie.
     */
    public void setMagierAkademie(MagierAkademie magierAkademie) {
        this.magierAkademie = magierAkademie;
    }

    /**
     * @return Liefert das Attribut ritusModis.
     */
    public Auswahl getRitusModis() {
        return ritusModis;
    }

    /**
     * @param ritusModis Setzt das Attribut ritusModis.
     */
    public void setRitusModis(Auswahl ritusModis) {
        this.ritusModis = ritusModis;
    }

    /**
     * @return Liefert das Attribut schriften.
     */
    public Auswahl getSchriften() {
        return schriften;
    }

    /**
     * @param schriften Setzt das Attribut schriften.
     */
    public void setSchriften(Auswahl schriften) {
        this.schriften = schriften;
    }

    /**
     * @return Liefert das Attribut sprachen.
     */
    public Auswahl getSprachen() {
        return sprachen;
    }

    /**
     * @param sprachen Setzt das Attribut sprachen.
     */
    public void setSprachen(Auswahl sprachen) {
        this.sprachen = sprachen;
    }
    
	/**
	 * @return Die möglichen Varianten zu dieser Profession.
	 */
	public ProfessionVariante[] getVarianten() {
		return varianten;
	}
	/**
	 * @param varianten Die möglichen Varianten zu dieser Profession.
	 */
	public void setVarianten(ProfessionVariante[] varianten) {
		this.varianten = varianten;
	}
    /**
     * <u>Beschreibung:</u><br>
     * Repräsentiert die üblichen angaben die für eine Magier-Akedemie benötigt werden, wenn diese als Profession
     * gewählt werden kann.
     * 
     * @author V. Strelow
     */
    public class MagierAkademie {
        private Gilde gilde;

        private MagieMerkmal merkmale[];

        private boolean zweitStudiumMoeglich = true;

        private boolean drittStudiumMoeglich = false;

        private String anmerkung;

        private Profession herkunft;

        /**
         * Konstruktor
         * 
         * @param herkunft Die Profession, von der diese MagierAkademie stammt.
         */
        public MagierAkademie(Profession herkunft) {
            this.herkunft = herkunft;
        }

        public String getAnmerkung() {
            return anmerkung;
        }

        public void setAnmerkung(String anmerkung) {
            this.anmerkung = anmerkung;
        }

        public boolean isDrittStudiumMoeglich() {
            return drittStudiumMoeglich;
        }

        public void setDrittStudiumMoeglich(boolean drittStudiumMoeglich) {
            this.drittStudiumMoeglich = drittStudiumMoeglich;
        }

        public Gilde getGilde() {
            return gilde;
        }

        public void setGilde(Gilde gilde) {
            this.gilde = gilde;
        }

        public Profession getHerkunft() {
            return herkunft;
        }

        public void setHerkunft(Profession herkunft) {
            this.herkunft = herkunft;
        }

        public MagieMerkmal[] getMerkmale() {
            return merkmale;
        }

        public void setMerkmale(MagieMerkmal[] merkmale) {
            this.merkmale = merkmale;
        }

        public boolean isZweitStudiumMoeglich() {
            return zweitStudiumMoeglich;
        }

        public void setZweitStudiumMoeglich(boolean zweitStudiumMoeglich) {
            this.zweitStudiumMoeglich = zweitStudiumMoeglich;
        }

    }
}
