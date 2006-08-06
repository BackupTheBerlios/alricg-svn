/*
 * Created 23. Januar 2005 / 15:30:43
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.held;

import java.util.HashMap;

import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Kultur;
import org.d3s.alricg.charKomponenten.Profession;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.Sprache;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.Notepad;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.prozessor.LinkProzessorFront;
import org.d3s.alricg.prozessor.common.GeneratorLink;
import org.d3s.alricg.prozessor.common.HeldenLink;
import org.d3s.alricg.prozessor.common.SonderregelAdmin;
import org.d3s.alricg.prozessor.common.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.prozessor.common.VoraussetzungenAdmin;
import org.d3s.alricg.prozessor.elementBox.ElementBox;
import org.d3s.alricg.prozessor.generierung.ProzessorEigenschaften;
import org.d3s.alricg.prozessor.generierung.ProzessorTalent;
import org.d3s.alricg.prozessor.generierung.ProzessorZauber;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorEigenschaft;
import org.d3s.alricg.prozessor.generierung.extended.ExtendedProzessorTalent;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <b>Beschreibung:</b><br>
 * Fast die Daten die einen Helden ausmachen zusammen.
 * 
 * @author V.Strelow
 */
public class Held {

	// Die Herkunft
	private Rasse rasse;
	private Kultur kultur;
	private Profession profession;
		
	// Die Daten zu dem Char
	private CharakterDaten daten;
	
	// Alle Prozessoren und somit CharElemente des Helden, nach Komponeten sortiert
	private HashMap<CharKomponente, LinkProzessorFront> prozessorHash;
	
	// Die Eigenschaften, diese sind auch in der "prozessorHash"
	// enthalten, aber wegen ihrer besonderen Bedeutung nochmal hier
	private HashMap<EigenschaftEnum, HeldenLink> eigenschaftHash;
	
	// Die sprachen
	private Sprache[] muttersprache;  // kann mehrere geben, siehe "Golbin Festumer G"
	private Sprache[] zweitsprache; 
	private Sprache[] lehrsprache; 
	
	private int abenteuerPunkte;
	private LogBuch lnkLogBuch;
	
	// Admins zur Heldenverwaltung
	private SonderregelAdmin sonderregelAdmin;
	private VoraussetzungenAdmin voraussetzungAdmin;
	private VerbilligteFertigkeitAdmin verbFertigkeitenAdmin;
	
	// Infos ünber den Werdegang des Helden
	private boolean isAbgebrocheAusbildung;
	private boolean isKindZweiterWeltenRas;
	private boolean isKindZweiterWeltenKul;
	
	private boolean isVollzauberer;
	private boolean isHalbzauberer;
	private boolean isViertelzauberer;
	private boolean isGeweiht;
	
	private boolean isGeneriertung;
	
	//public static HeldUtilitis heldUtils; 
	//private HeldProzessor heldProzessor; // ProzessorXX mit dem der Held bearbeitet wird
	
	/**
	 * Konstruktor. Erzeugt einen neuen Helden, nur mit den Eigenschaften ausgestattet.
	 */
	public Held() {
		//heldUtils = new HeldUtilitis();
	}
	
	/**
	 * Initiiert den Helden für das Management des Helden, vor allem werden die Boxen erzeugt
	 */
	public void initManagement() {
		isGeneriertung = false;
		// TODO Boxen für das Management erzeugen
	}
	
	/**
	 * Initiiert den Helden für die Generierung, vor allem werden die Boxen erzeugt.
	 */
	public void initGenrierung() {
		LinkProzessorFront frontProzessor;
		
		// Prozessorübergreifende Objekte erzeugen
		sonderregelAdmin = new SonderregelAdmin(this);
		voraussetzungAdmin= new VoraussetzungenAdmin(this);
		verbFertigkeitenAdmin = new VerbilligteFertigkeitAdmin(this);
		
		Notepad notepad = ProgAdmin.notepad;
		
		isGeneriertung = true;
		
		// --------- Prozessoren erzeugen
		prozessorHash = new HashMap<CharKomponente, LinkProzessorFront>();
		
		// Für die Eigenschaften
		frontProzessor = new LinkProzessorFront<Eigenschaft, ExtendedProzessorEigenschaft, GeneratorLink>(
				this,
				new ProzessorEigenschaften(this, notepad));
		prozessorHash.put(CharKomponente.eigenschaft, frontProzessor);
		
		// Erzeugt alle Eigenschaften
		initEigenschaften((LinkProzessorFront) prozessorHash.get(CharKomponente.eigenschaft));
		
		// Setzt die Eigenschaften in ein zusätzliches Hash für besseren Zugriff
		initEigenschaftMap(frontProzessor);
		
		// Für die Talente
		frontProzessor = new LinkProzessorFront<Talent, ExtendedProzessorTalent, GeneratorLink>(
				this,
				new ProzessorTalent(this, notepad));
		prozessorHash.put(CharKomponente.talent, frontProzessor);
		
		// Für die Zauber
		frontProzessor = new LinkProzessorFront<Zauber, ExtendedProzessorTalent, GeneratorLink>(
				this,
				new ProzessorZauber(this, notepad));
		prozessorHash.put(CharKomponente.zauber, frontProzessor);
	}

	
	/**
	 * Erzeugt alle Eigenschaften und fügt sie zum Helden hinzu. Es werden initiale Werte
	 * gesetzt.
	 * @param Der Prozessor zu diesem Helden
	 */ 
	private void initEigenschaften(LinkProzessorFront prozessor) {
		ElementBox<HeldenLink> box;
		
		EigenschaftEnum[] enums = EigenschaftEnum.values();
		
		// Erstmal alle Eigenschaften mit "0" setzen
		for (int i = 0; i < enums.length; i++) {
			prozessor.addNewElement(
				FactoryFinder.find().getData().getCharElement(enums[i].getId(), CharKomponente.eigenschaft)
			);
		}
		
		box = prozessor.getElementBox();
		
		//  Grund-Eigenschaften auf initialwert "8" setzen
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.MU.getId()),
				8
		);
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.CH.getId()),
				8
		);
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.FF.getId()),
				8
		);
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.GE.getId()),
				8
		);
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.IN.getId()),
				8
		);
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.KK.getId()),
				8
		);
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.KL.getId()),
				8
		);
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.KO.getId()),
				8
		);
		
		prozessor.updateWert(
				box.getObjectById(EigenschaftEnum.SO.getId()),
				1
		);
		
	}
	
	/**
	 * Setzt die Eigenschaften in ein Zusätzliches Hash für besseren Zugriff, da die
	 * Eigenschaften öfter benötigt werden.
	 * @param prozessor
	 */
	private void initEigenschaftMap(LinkProzessorFront<Eigenschaft, ?, ? extends HeldenLink> prozessor) {
		
		EigenschaftEnum[] enums = EigenschaftEnum.values();
		
		eigenschaftHash = new HashMap<EigenschaftEnum, HeldenLink>();
		
		// Da Eigenschaften von größerer Bedeutung sind werden sie ZUSÄTZLICH
		// direkt in ein Hash gesichert für einfachen Zugriff
		for (int i = 0; i < enums.length; i++) {
			eigenschaftHash.put(enums[i], 
					prozessor.getElementBox().getObjectById(enums[i].getId())
			);
		}
	}
	
	/**
	 * Hiermit kann die Box der CharKomponente "komponente" abgerufen werden. Darüber 
	 * kann auf alle Elemente zu der Box zugegriffen werden. Eigenschaften (MU, Lep, usw.) 
	 * können mit dieser Methode nicht abgerufen werden, es wird "null" zurückgeliefert. 
	 * Hier für ist die Methode "getEigenschaftsWert()" gedacht.
	 * 
	 * @param komponente Die gewünschte CharKomponete
	 * @return Die LinkElementBoxzu die alle CharElemente des Helden der Art "komponente"
	 *  enthält.
	 */
	public LinkProzessorFront getProzessor(CharKomponente komponente) {
		// Eigenschaften können hier NICHT abgerufen werden, da sie errechnet werden
		//if ( komponente.equals(CharKomponente.eigenschaft) ) return null;
		
		return prozessorHash.get(komponente);
	}
	
	/**
	 * Ermöglicht einen einfachen Zugriff auf die Eigenschaften (alle die in 
	 * der enum "EigenschaftEnum" stehen). Auf die Eigenschaften kann auch mittels
	 * "getElementBox(...)" zugegriffen werden, es sollte jedoch diese Methode benutzt 
	 * werden.
	 * VORSICHT: Alle Eigenschaften die errechnet werden, werden nur über diese Methode
	 * 			den korrekten wert liefern, ansonsten nur den Wert der Modis.  
	 * 
	 * @param eigen Die gewünschte Eigenschaft
	 * @return Der Aktuelle Wert dieser Eigenschaft
	 */
	public int getEigenschaftsWert(EigenschaftEnum eigen) {
		
		switch (eigen) {
		
		case LEP: return FormelSammlung.berechneLep(
				eigenschaftHash.get(EigenschaftEnum.KO).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()		
			)
			+ eigenschaftHash.get(EigenschaftEnum.LEP).getWert();
	
		case ASP: return FormelSammlung.berechneAsp(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.CH).getWert()
			)
			+ eigenschaftHash.get(EigenschaftEnum.ASP).getWert();
			
		case AUP: return FormelSammlung.berechneAup(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KO).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert()
			)
			+ eigenschaftHash.get(EigenschaftEnum.AUP).getWert();
		
		case AT: return FormelSammlung.berechneAtBasis(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()
			)
			+ eigenschaftHash.get(EigenschaftEnum.AT).getWert();
				
		case PA: return FormelSammlung.berechnePaBasis(
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.PA).getWert();
		
		case FK: return FormelSammlung.berechneFkBasis(
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.FF).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KK).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.FK).getWert();
			
		case INI: return FormelSammlung.berechneINI(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.IN).getWert(),
				eigenschaftHash.get(EigenschaftEnum.GE).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.INI).getWert();
			
		case MR: return FormelSammlung.berechneMR(
				eigenschaftHash.get(EigenschaftEnum.MU).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KL).getWert(),
				eigenschaftHash.get(EigenschaftEnum.KO).getWert()
			) 
			+ eigenschaftHash.get(EigenschaftEnum.MR).getWert();
			
		default: // Es handelt sich um MU - KK, KE, GS oder SO
			// Da hier nicht errechnet werden muß, werden die Werte direkt geliefert
			return eigenschaftHash.get(eigen).getWert();
		}

	}

	/**
	 * @return Liefert das Attribut isAbgebrocheAusbildung.
	 */
	public boolean isAbgebrocheAusbildung() {
		return isAbgebrocheAusbildung;
	}

	/**
	 * @param isAbgebrocheAusbildung Setzt das Attribut isAbgebrocheAusbildung.
	 */
	public void setAbgebrocheAusbildung(boolean isAbgebrocheAusbildung) {
		this.isAbgebrocheAusbildung = isAbgebrocheAusbildung;
	}

	/**
	 * @return Liefert das Attribut isKindZweiterWeltenKul.
	 */
	public boolean isKindZweiterWeltenKul() {
		return isKindZweiterWeltenKul;
	}

	public boolean isVeteran() {
		// TODO implement
		return false;
	}
	
	public boolean isBreitgefaecherteBildung()  {
		// TODO implement
		return false;
	}
	
	/**
	 * @param isKindZweiterWeltenKul Setzt das Attribut isKindZweiterWeltenKul.
	 */
	public void setKindZweiterWeltenKul(boolean isKindZweiterWeltenKul) {
		this.isKindZweiterWeltenKul = isKindZweiterWeltenKul;
	}

	/**
	 * @return Liefert das Attribut isKindZweiterWeltenRas.
	 */
	public boolean isKindZweiterWeltenRas() {
		return isKindZweiterWeltenRas;
	}

	/**
	 * @param isKindZweiterWeltenRas Setzt das Attribut isKindZweiterWeltenRas.
	 */
	public void setKindZweiterWeltenRas(boolean isKindZweiterWeltenRas) {
		this.isKindZweiterWeltenRas = isKindZweiterWeltenRas;
	}

	/**
	 * @return Liefert das Attribut sonderregelAdmin.
	 */
	public SonderregelAdmin getSonderregelAdmin() {
		return sonderregelAdmin;
	}

	/**
	 * @return Liefert das Attribut verbFertigkeitenAdmin.
	 */
	public VerbilligteFertigkeitAdmin getVerbFertigkeitenAdmin() {
		return verbFertigkeitenAdmin;
	}

	/**
	 * @return Liefert das Attribut voraussetzungAdmin.
	 */
	public VoraussetzungenAdmin getVoraussetzungAdmin() {
		return voraussetzungAdmin;
	}
	
	/**
	 * @return true - Der Held befindet sich in der Generierung, ansonsten false
	 */
	public boolean isGenerierung() {
		return isGeneriertung;
	}
	
	/**
	 * @return true - Der Held befindet sich im Helden-Management, ansonsten false
	 */
	public boolean isManagement() {
		return !isGeneriertung;
	}
	
	/**
	 * @return true - Der Held besitzt den Vorteil "Vollzauberer", ansonsten false
	 */
	public boolean isVollzauberer() {
		return this.isVollzauberer;
	}
	
	/**
	 * @return true - Der Held besitzt den Vorteil "Halbzauberer", ansonsten false
	 */
	public boolean isHalbzauberer() {
		return this.isHalbzauberer;
	}
	
	/**
	 * @return true - Der Held besitzt den Vorteil "Viertelzauberer", ansonsten false
	 */
	public boolean isViertelzauberer() {
		return this.isViertelzauberer;
	}
	
	/**
	 * @return true - Der Held ist ein Geweihter, ansonsten false
	 */
	public boolean isGeweiht() {
		return this.isGeweiht;
	}
	
	/**
	 * Prüft ob der Held "normal" ist, also kein Magier oder Geweihter.
	 * @return true - Der Charakter ist nicht Magisch oder geweiht, ansonsten false
	 */
	public boolean isNormaloChar() {
		return !(isVollzauberer || isHalbzauberer || isViertelzauberer || isGeweiht);
	}
	
	/**
	 * Ermöglicht einen einfachen Zugriff auf die Links zu den Eigenschaften (alle die in 
	 * der enum "EigenschaftEnum" stehen). Auf die Eigenschaften kann auch mittels
	 * "getElementBox(...)" zugegriffen werden, es sollte jedoch diese Methode benutzt 
	 * werden.
	 * Diese Methode ist Sinnvoll wenn die Links benötigt werden, um diese zu bearbeiten.
	 * 
	 * VORSICHT: Bei alle Eigenschaften die errechnet werden, wird hier nicht der korrekte
	 * 			Wert im Link stehen, sondern nur die Modifikationen des Wertes. 
	 * 			Soll der Wert abgefragt werde, sollte "getEigenschaftsWert(..)" benutzt werden.
	 * 
	 * @param eigen Die gewünschte Eigenschaft
	 * @return Der Aktuelle Wert dieser Eigenschaft
	 *
	public HeldenLink getEigenschaftLink(EigenschaftEnum eigen) {
		return eigenschaftHash.get(eigen);
	}*/
}
