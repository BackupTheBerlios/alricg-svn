/*
 * Created 20. Januar 2005 / 15:42:20
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import java.util.ArrayList;

import javax.swing.Icon;
import javax.xml.bind.annotation.XmlEnum;

/**
 * <b>Beschreibung:</b><br>
 * Diese Klasse umfasst mehrere "Enums", die nicht Klar als eingenschaft einer
 * bestimmten Klasse angesehen werden können. Es werden außerdem Operationen auf
 * diesen Enums angeboten.
 * 
 * @author V.Strelow
 */
public class Werte {
	@XmlEnum
	public enum CharArten {
		alle("alle"),
		nichtMagisch("nichtMagisch"),
		vollZauberer("vollZauberer"),
		halbZauberer("halbZauberer"),
		viertelZauberer("viertelZauberer"),
		geweiht("geweiht"),
		borbaradianer("borbaradianer");

		private String value; // ID des Elements

		private CharArten(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@XmlEnum
	public enum Gilde {
		weiss("weiss"),
		grau("grau"),
		schwarz("schwarz"),
		unbekannt("unbekannt"),
		keine("keine");
		private String value; // IDdes Elements

		private Gilde(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

	@XmlEnum
	public enum Geschlecht {
		mann,
		frau,
		mannOderFrau
	};

	@XmlEnum
	public enum MagieMerkmal {
		// TODO MagieMerkmal korregieren
		antimagie("Antimagie"),
		beschwoerung("beschwoerung"),
		daemonisch("daemonisch"),
		daemonischBlakharaz("daemonisch (blakharaz)"),
		daemonischBelhalhar("daemonisch (belhalhar)"),
		daemonischCharyptoroth("daemonisch (charyptoroth)"),
		daemonischLolgramoth("daemonisch (lolgramoth)"),
		daemonischThargunitoth("daemonisch (thargunitoth)"),
		daemonischAmazeroth("daemonisch (amazeroth)"),
		daemonischBelshirash("daemonisch (belshirash)"),
		daemonischAsfaloth("daemonisch (asfaloth)"),
		daemonischTasfarelel("daemonisch (tasfarelel)"),
		daemonischBelzhorash("daemonisch (belzhorash)"),
		daemonischAgrimoth("daemonisch (agrimoth)"),
		daemonischBelkelel("daemonisch (belkelel)"),
		eigenschaften("eigenschaften"),
		einfluss("einfluss"),
		elementar("elementar"),
		elementarFeuer("elementar (feuer)"),
		elementarWasser("elementar (wasser)"),
		elementarLuft("elementar (luft)"),
		elementarErz("elementar (erz)"),
		elementarHumus("elementar (humus)"),
		elementarEis("elementar (eis)"),
		form("form"),
		geisterwesen("geisterwesen"),
		heilung("heilung"),
		hellsicht("hellsicht"),
		herbeirufung("herbeirufung"),
		herrschaft("herrschaft"),
		illusion("illusion"),
		kraft("kraft"),
		limbus("limbus"),
		metamagie("metamagie"),
		objekt("objekt"),
		schaden("schaden"),
		telekinese("telekinese"),
		temporal("temporal"),
		umwelt("umwelt"),
		verstaendigung("verstaendigung");

		/*
		 * Alle Merkmale sind korrekt eingetragen, jedoch sind die Bilder
		 * momentan nicht verfügbar. Frage ist auch, ob die Bilder so geladen
		 * werden sollten
		 * 
		 * antimagie("antimagie", ImageAdmin.zauberMerkmalAntimagie,
		 * ImageAdmin.zauberMerkmalAntimagie24), beschwoerung( "beschwoerung",
		 * ImageAdmin.zauberMerkmalBeschwoerung,
		 * ImageAdmin.zauberMerkmalBeschwoerung24), daemonisch( "daemonisch",
		 * ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischBlakharaz(
		 * "daemonisch (blakharaz)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelhalhar(
		 * "daemonisch (belhalhar)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischCharyptoroth(
		 * "daemonisch (charyptoroth)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischLolgramoth(
		 * "daemonisch (lolgramoth)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischThargunitoth(
		 * "daemonisch (thargunitoth)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischAmazeroth(
		 * "daemonisch (amazeroth)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelshirash(
		 * "daemonisch (belshirash)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischAsfaloth(
		 * "daemonisch (asfaloth)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischTasfarelel(
		 * "daemonisch (tasfarelel)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelzhorash(
		 * "daemonisch (belzhorash)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischAgrimoth(
		 * "daemonisch (agrimoth)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelkelel(
		 * "daemonisch (belkelel)", ImageAdmin.zauberMerkmalDaemonisch,
		 * ImageAdmin.zauberMerkmalDaemonisch24), eigenschaften(
		 * "eigenschaften", ImageAdmin.zauberMerkmalEigenschaften,
		 * ImageAdmin.zauberMerkmalEigenschaften24), einfluss("einfluss",
		 * ImageAdmin.zauberMerkmalEinfluss,
		 * ImageAdmin.zauberMerkmalEinfluss24), elementar("elementar",
		 * ImageAdmin.zauberMerkmalElementar,
		 * ImageAdmin.zauberMerkmalElementar24), elementarFeuer( "elementar
		 * (feuer)", ImageAdmin.zauberMerkmalElementarFeuer,
		 * ImageAdmin.zauberMerkmalElementarFeuer24), elementarWasser(
		 * "elementar (wasser)", ImageAdmin.zauberMerkmalElementarWasser,
		 * ImageAdmin.zauberMerkmalElementarWasser24), elementarLuft( "elementar
		 * (luft)", ImageAdmin.zauberMerkmalElementarLuft,
		 * ImageAdmin.zauberMerkmalElementarLuft24), elementarErz( "elementar
		 * (erz)", ImageAdmin.zauberMerkmalElementarErz,
		 * ImageAdmin.zauberMerkmalElementarErz24), elementarHumus( "elementar
		 * (humus)", ImageAdmin.zauberMerkmalElementarHumus,
		 * ImageAdmin.zauberMerkmalElementarHumus24), elementarEis( "elementar
		 * (eis)", ImageAdmin.zauberMerkmalElementarEis,
		 * ImageAdmin.zauberMerkmalElementarEis24), form("form",
		 * ImageAdmin.zauberMerkmalForm, ImageAdmin.zauberMerkmalForm24),
		 * geisterwesen( "geisterwesen", ImageAdmin.zauberMerkmalGeisterwesen,
		 * ImageAdmin.zauberMerkmalGeisterwesen24), heilung("heilung",
		 * ImageAdmin.zauberMerkmalHeilung, ImageAdmin.zauberMerkmalHeilung24),
		 * hellsicht("hellsicht", ImageAdmin.zauberMerkmalHellsicht,
		 * ImageAdmin.zauberMerkmalHellsicht24), herbeirufung( "herbeirufung",
		 * ImageAdmin.zauberMerkmalHerbeirufung,
		 * ImageAdmin.zauberMerkmalHerbeirufung24), herrschaft( "herrschaft",
		 * ImageAdmin.zauberMerkmalHerrschaft,
		 * ImageAdmin.zauberMerkmalHerrschaft24), illusion("illusion",
		 * ImageAdmin.zauberMerkmalIllusion,
		 * ImageAdmin.zauberMerkmalIllusion24), kraft("kraft",
		 * ImageAdmin.zauberMerkmalKraft, ImageAdmin.zauberMerkmalKraft24),
		 * limbus( "limbus", ImageAdmin.zauberMerkmalLimbus,
		 * ImageAdmin.zauberMerkmalLimbus24), metamagie("metamagie",
		 * ImageAdmin.zauberMerkmalMetamagie,
		 * ImageAdmin.zauberMerkmalMetamagie24), objekt("objekt",
		 * ImageAdmin.zauberMerkmalObjekt, ImageAdmin.zauberMerkmalObjekt24),
		 * schaden("schaden", ImageAdmin.zauberMerkmalSchaden,
		 * ImageAdmin.zauberMerkmalSchaden24), telekinese("telekinese",
		 * ImageAdmin.zauberMerkmalTelekinese,
		 * ImageAdmin.zauberMerkmalTelekinese24), temporal("temporal",
		 * ImageAdmin.zauberMerkmalTemporal,
		 * ImageAdmin.zauberMerkmalTemporal24), umwelt("umwelt",
		 * ImageAdmin.zauberMerkmalUmwelt, ImageAdmin.zauberMerkmalUmwelt24),
		 * verstaendigung( "verstaendigung",
		 * ImageAdmin.zauberMerkmalVerstaendigung,
		 * ImageAdmin.zauberMerkmalVerstaendigung24);
		 */
		private String value; // ID des Elements


		private MagieMerkmal(String value) {
			this.value = value;
		}

		/**
		 * @return Liefert den XML-Value des Merkmals
		 */
		public String getValue() {
			return value;
		}

	}

	/**
	 * <b>Beschreibung:</b><br>
	 * Hilfsklasse zum besseren Arbeiten mit Eigenschaften.
	 * 
	 * @author V.Strelow
	 */
	@XmlEnum
	public enum EigenschaftEnum {
		MU("Mut", "MU", "EIG-MU"),
		KL("Klugheit", "KL", "EIG-KL"),
		IN("Intuition", "IN", "EIG-IN"),
		CH("Charisma", "CH", "EIG-CH"),
		FF("Fingerfertigkeit", "FF", "EIG-FF"),
		GE("Gewandheit", "GE", "EIG-GE"),
		KO("Konstitution", "KO", "EIG-KO"),
		KK("Koerperkraft", "KK", "EIG-KK"),
		SO("Sozialstatus", "SO", "EIG-SO"),
		MR("Magieresistenz", "MR", "EIG-MR"),
		LEP("Lebenspunkte", "LeP", "EIG-Lep"),
		ASP("Astralpunkte", "AsP", "EIG-AsP"),
		AUP("Ausdauerpunkt", "AuP", "EIG-AuP"),
		KA("Karmaernergie", "KA", "EIG-KA"),
		GS("Geschwindigkeit", "GS", "EIG-GS"),
		INI("Initiative", "INI", "EIG-INI"),
		FK("FernkampfBasis", "FK", "EIG-FK"),
		AT("AttackeBasis", "AT", "EIG-AT"),
		PA("ParadeBasis", "PA", "EIG-PA");

		private String name; // Voller Name der Eigenschaft
		private String abk; // Abkürzung der Eigenschaft
		private String id; // ID der Eigenschaft
		private Eigenschaft eigenschaftCharElement;

		/**
		 * @param bezeichnung
		 *            Key für Library für den vollen Namen
		 * @param abkuerzung
		 *            Key für Library für die Akkürzung des Namens
		 */
		private EigenschaftEnum(String name, String abk, String id) {
			// TODO Lokalisierung der Strings
			// name =
			// FactoryFinder.find().getLibrary().getShortTxt(bezeichnung);
			// abk = FactoryFinder.find().getLibrary().getShortTxt(abkuerzung);
			this.name = name;
			this.abk = abk;
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
		public String getBezeichnung() {
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
		public String getAbk() {
			return abk;
		}

		/*
		 * (non-Javadoc) Methode überschrieben
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return abk;
		}
		
		public void setEigenschaft(Eigenschaft eigenschaftCharElement) {
			this.eigenschaftCharElement = eigenschaftCharElement;
		}
		
		public Eigenschaft getEigenschaft() {
			return eigenschaftCharElement;
		}

		/**
		 * Diese Methode wird vor allem für die initialisierung benötigt!
		 * 
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
		 * @return Gibt die Anzahl aller Eigenschaften dieser Klasse an
		 */
		public static int getAnzahlEigenschaften() {
			return EigenschaftEnum.values().length;
		}
	}

	/*
	 * public enum MagieRepraesentation { gildenmagier("gildenmagier"),
	 * elfen("elfen"), druiden("druiden"), hexen("hexen"), geoden("geoden"),
	 * schelme("schelme"), scharlatane("scharlatane"),
	 * borbaradianer("borbaradianer"), kristallomanten("kristallomanten"),
	 * alchemisten("alchemisten"), derwische("derwische"), gjalsker("gjalsker"),
	 * darna("darna"), sharisadTulamidisch("sharisad (tulamidisch)"),
	 * sharisadNovadisch("sharisad (novadisch)"), sharisadZahorisch("sharisad
	 * (zahorisch)"), sharisadAranisch("sharisad (aranisch)"),
	 * zibilja("zibilja"); private String value; // ID des Elements private
	 * MagieRepraesentation(String value) { this.value = value; } public String
	 * getValue() { return value; } }
	 */

}
