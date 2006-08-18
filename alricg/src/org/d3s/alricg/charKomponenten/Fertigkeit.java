/*
 * Created 26. Dezember 2004 / 23:36:19
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;


/**
 * <b>Beschreibung:</b><br>
 * Fasst gemeinsamkeiten von Vor-/ Nachteilen und Sonderfertigkeiten zusammmen und bildet die Grundlage f�r diese.
 * 
 * @author V.Strelow
 */
public abstract class Fertigkeit extends CharElement {
    private Werte.CharArten[] fuerWelcheChars; // Welche Chars diese Fertigkeit w�hlen k�nnen

    private boolean hasFreienText; // Gibt es noch einen frei zu w�hlenden Text zu der Fertigkeit? (Vorurteile gegen "Orks")
    private String[] textVorschlaege; // Eine Liste M�glicher angaben f�r den Text
    // Es ist m�glich das eine Fertigkeit "hasFreienText = false" ist, aber Text Vorschl�ge besitzt
    // in diesem Fall k�nnen nur die Vorschl�ge gew�hlt werden, nichts anderes
    
    private boolean hasElementAngabe; // Gibt es noch ein Element zu dieser Fertigkeit (Unf�higkeit
                                                // "Schwerter")

    private boolean isWaehlbar = true; // Nicht w�hlbare k�nnen nur �ber die Herkunft erlangt werden

    private String additionsID; // "Familie" von Fertigkeiten, die aufaddiert werden z.B. R�stungsgew�hung I und RG II

    private int additionsWert = KEIN_WERT; // z.B. R�stungsgew�hung I = 1 und RG II = 2. Somit ergibt zwei mal RG I -->
                                            // ein mal RG II (sieht AH S.10)

    private int gpKosten;

    /**
     * Eine additionsID kennzeichent solche Fertigkeiten, die Zusammengeh�ren und bei mehrfachen erlangen durch Rasse,
     * Kultur, ... zusammengefasst werden. Zu was die Fertigkeiten zusammengefast werden wird �ber den additionsWert
     * festgelegt. Beispiel: R�stungsgew�hung I (additiosnWert: 1) und RG II (aW: 2). Somit ergibt 2x RG I --> 1x RG II
     * 
     * @return Liefert die additionsID.
     */
    public String getAdditionsID() {
        return additionsID;
    }

    /**
     * @see getAdditionsID()
     * @return Liefert das Attribut additionsWert.
     */
    public int getAdditionsWert() {
        return additionsWert;
    }

    /**
     * @return Liefert die Kosten in Generierungspunkten.
     */
    public int getGpKosten() {
        return gpKosten;
    }

    /**
     * @return Liefert das Attribut fuerWelcheChars.
     */
    public Werte.CharArten[] getFuerWelcheChars() {
        return fuerWelcheChars;
    }

    /**
     * Fertigkeiten mit Text ben�tigen noch einen Text wenn sie gew�hlt werden: z.B. Vorurteile gegen "Orks" oder
     * Verpflichtungen gegen�ber "Praioskirche".
     * 
     * @return true - Die Fertigkeit ben�tigt noch die Angabe eines Textes, ansonsten "false".
     */
    public boolean hasText() {
        return hasFreienText;
    }

    /**
     * Fertigkeiten die "Nicht w�hlbar" sind k�nnen nur �ber die Herkunft erlangt werden! D.h. diese stehen NICHT zur
     * normalen Auswahl.
     * 
     * @return true - Die Fertigkeit ist normal w�hlbar, ansonsten ist die Fertigk. NICHT w�hlbar (false)
     */
    public boolean isWaehlbar() {
        return isWaehlbar;
    }

    /**
     * @param additionsID Setzt das Attribut additionsID.
     */
    public void setAdditionsID(String additionsID) {
        this.additionsID = additionsID;
    }

    /**
     * @param additionsWert Setzt das Attribut additionsWert.
     */
    public void setAdditionsWert(int additionsWert) {
        this.additionsWert = additionsWert;
    }

    /**
     * @param fuerWelcheChars Setzt das Attribut fuerWelcheChars.
     */
    public void setFuerWelcheChars(Werte.CharArten[] fuerWelcheChars) {
        this.fuerWelcheChars = fuerWelcheChars;
    }

    /**
     * @param gpKosten Setzt das Attribut gpKosten.
     */
    public void setGpKosten(int gpKosten) {
        this.gpKosten = gpKosten;
    }

    /**
     * @param hatText Setzt das Attribut hatText.
     */
    public void setHasText(boolean hatText) {
        this.hasFreienText = hatText;
    }

    /**
     * @param isWaehlbar Setzt das Attribut isWaehlbar.
     */
    public void setWaehlbar(boolean isWaehlbar) {
        this.isWaehlbar = isWaehlbar;
    }

    /**
     * @return Liefert das Attribut hasElementAngabe.
     */
    public boolean isElementAngabe() {
        return hasElementAngabe;
    }

    /**
     * @param hasElementAngabe Setzt das Attribut hasElementAngabe.
     */
    public void setElementAngabe(boolean hasElementAngabe) {
        this.hasElementAngabe = hasElementAngabe;
    }

    public String[] getTextVorschlaege() {
        return textVorschlaege;
    }

    public void setTextVorschlaege(String[] textVorschlaege) {
        this.textVorschlaege = textVorschlaege;
    }
}
