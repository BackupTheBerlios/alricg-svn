/*
 * Created 20. Januar 2005 / 15:42:20
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.charKomponenten;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.d3s.alricg.controller.ImageAdmin;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.TextStore;

/**
 * <b>Beschreibung:</b><br>
 * Diese Klasse umfasst mehrere "Enums", die nicht Klar als eingenschaft einer
 * bestimmten Klasse angesehen werden können. Es werden außerdem Operationen auf
 * diesen Enums angeboten.
 * 
 * @author V.Strelow
 */
public class Werte {

	/** <code>Werte</code>'s logger */
	private static final Logger LOG = Logger.getLogger(Werte.class.getName());

	private final static HashMap<String, MagieMerkmal> magieMerkmale = new HashMap<String, MagieMerkmal>();

	static {
		// Zum besseren auffinden in der Enum MagieMerkmal werden die
		// Elemente in eine HashMap gelegt.

		for (int i = 0; i < MagieMerkmal.values().length; i++) {
			magieMerkmale.put(MagieMerkmal.values()[i].getValue(), MagieMerkmal
					.values()[i]);
		}
	}

	public enum CharArten {
		alle("alle"), nichtMagisch("nichtMagisch"), vollZauberer("vollZauberer"), halbZauberer(
				"halbZauberer"), viertelZauberer("viertelZauberer"), geweiht(
				"geweiht"), borbaradianer("borbaradianer");

		private String value; // ID des Elements

		private CharArten(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	public enum Gilde {
		weiss("weiss"), grau("grau"), schwarz("schwarz"), unbekannt("unbekannt"), keine(
				"keine");
		private String value; // IDdes Elements

		private Gilde(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	};

	public enum Geschlecht {
		mann, frau, mannFrau
	};

	public enum MagieMerkmal {
		antimagie("antimagie", ImageAdmin.zauberMerkmalAntimagie,
				ImageAdmin.zauberMerkmalAntimagie24), beschwoerung(
				"beschwoerung", ImageAdmin.zauberMerkmalBeschwoerung,
				ImageAdmin.zauberMerkmalBeschwoerung24), daemonisch(
				"daemonisch", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischBlakharaz(
				"daemonisch (blakharaz)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelhalhar(
				"daemonisch (belhalhar)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischCharyptoroth(
				"daemonisch (charyptoroth)",
				ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischLolgramoth(
				"daemonisch (lolgramoth)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischThargunitoth(
				"daemonisch (thargunitoth)",
				ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischAmazeroth(
				"daemonisch (amazeroth)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelshirash(
				"daemonisch (belshirash)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischAsfaloth(
				"daemonisch (asfaloth)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischTasfarelel(
				"daemonisch (tasfarelel)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelzhorash(
				"daemonisch (belzhorash)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischAgrimoth(
				"daemonisch (agrimoth)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), daemonischBelkelel(
				"daemonisch (belkelel)", ImageAdmin.zauberMerkmalDaemonisch,
				ImageAdmin.zauberMerkmalDaemonisch24), eigenschaften(
				"eigenschaften", ImageAdmin.zauberMerkmalEigenschaften,
				ImageAdmin.zauberMerkmalEigenschaften24), einfluss("einfluss",
				ImageAdmin.zauberMerkmalEinfluss,
				ImageAdmin.zauberMerkmalEinfluss24), elementar("elementar",
				ImageAdmin.zauberMerkmalElementar,
				ImageAdmin.zauberMerkmalElementar24), elementarFeuer(
				"elementar (feuer)", ImageAdmin.zauberMerkmalElementarFeuer,
				ImageAdmin.zauberMerkmalElementarFeuer24), elementarWasser(
				"elementar (wasser)", ImageAdmin.zauberMerkmalElementarWasser,
				ImageAdmin.zauberMerkmalElementarWasser24), elementarLuft(
				"elementar (luft)", ImageAdmin.zauberMerkmalElementarLuft,
				ImageAdmin.zauberMerkmalElementarLuft24), elementarErz(
				"elementar (erz)", ImageAdmin.zauberMerkmalElementarErz,
				ImageAdmin.zauberMerkmalElementarErz24), elementarHumus(
				"elementar (humus)", ImageAdmin.zauberMerkmalElementarHumus,
				ImageAdmin.zauberMerkmalElementarHumus24), elementarEis(
				"elementar (eis)", ImageAdmin.zauberMerkmalElementarEis,
				ImageAdmin.zauberMerkmalElementarEis24), form("form",
				ImageAdmin.zauberMerkmalForm, ImageAdmin.zauberMerkmalForm24), geisterwesen(
				"geisterwesen", ImageAdmin.zauberMerkmalGeisterwesen,
				ImageAdmin.zauberMerkmalGeisterwesen24), heilung("heilung",
				ImageAdmin.zauberMerkmalHeilung,
				ImageAdmin.zauberMerkmalHeilung24), hellsicht("hellsicht",
				ImageAdmin.zauberMerkmalHellsicht,
				ImageAdmin.zauberMerkmalHellsicht24), herbeirufung(
				"herbeirufung", ImageAdmin.zauberMerkmalHerbeirufung,
				ImageAdmin.zauberMerkmalHerbeirufung24), herrschaft(
				"herrschaft", ImageAdmin.zauberMerkmalHerrschaft,
				ImageAdmin.zauberMerkmalHerrschaft24), illusion("illusion",
				ImageAdmin.zauberMerkmalIllusion,
				ImageAdmin.zauberMerkmalIllusion24), kraft("kraft",
				ImageAdmin.zauberMerkmalKraft, ImageAdmin.zauberMerkmalKraft24), limbus(
				"limbus", ImageAdmin.zauberMerkmalLimbus,
				ImageAdmin.zauberMerkmalLimbus24), metamagie("metamagie",
				ImageAdmin.zauberMerkmalMetamagie,
				ImageAdmin.zauberMerkmalMetamagie24), objekt("objekt",
				ImageAdmin.zauberMerkmalObjekt,
				ImageAdmin.zauberMerkmalObjekt24), schaden("schaden",
				ImageAdmin.zauberMerkmalSchaden,
				ImageAdmin.zauberMerkmalSchaden24), telekinese("telekinese",
				ImageAdmin.zauberMerkmalTelekinese,
				ImageAdmin.zauberMerkmalTelekinese24), temporal("temporal",
				ImageAdmin.zauberMerkmalTemporal,
				ImageAdmin.zauberMerkmalTemporal24), umwelt("umwelt",
				ImageAdmin.zauberMerkmalUmwelt,
				ImageAdmin.zauberMerkmalUmwelt24), verstaendigung(
				"verstaendigung", ImageAdmin.zauberMerkmalVerstaendigung,
				ImageAdmin.zauberMerkmalVerstaendigung24);

		private String value; // ID des Elements

		private String bezeichner;

		private Icon icon16;

		private Icon icon24;

		private MagieMerkmal(String value, Icon icon16, Icon icon24) {
			this.value = value;
			this.icon16 = icon16;
			this.icon24 = icon24;
		}

		/**
		 * @return Liefert den XML-Value des Merkmals
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return Das zum Merkmal zugehörige Icon in 16x16 Pixeln
		 */
		public Icon getIconKlein() {
			return icon16;
		}

		/**
		 * @return Das zum Merkmal zugehörige Icon in 24x24 Pixeln
		 */
		public Icon getIconGross() {
			return icon24;
		}

		public String toString() {
			if (bezeichner == null) {
				TextStore lib = FactoryFinder.find().getLibrary();
				switch (this.ordinal()) {
				case 3:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Blakharaz)";
					break;
				case 4:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Belhalhar)";
					break;
				case 5:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Charyptoroth)";
					break;
				case 6:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Lolgramoth)";
					break;
				case 7:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Thargunitoth)";
					break;
				case 8:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Amazeroth)";
					break;
				case 9:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Belshirash)";
					break;
				case 10:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Asfaloth)";
					break;
				case 11:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Tasfarelel)";
					break;
				case 12:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Belzhorash)";
					break;
				case 13:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Agrimoth)";
					break;
				case 14:
					bezeichner = lib.getShortTxt(daemonisch.getValue())
							+ " (Belkelel)";
					break;
				default:
					bezeichner = lib.getShortTxt(value);
				}
			}
			return bezeichner;
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

	/**
	 * Liefert zu einem value die entsprechende Enum zurück.
	 * 
	 * @param value
	 *            Die ID der gilde
	 * @return Die Enum Gilde die zu value gehört
	 */
	public static Gilde getGildeByValue(String value) {
		Gilde[] gildeArray = Gilde.values();

		// Suchen des richtigen Elements
		for (int i = 0; i < gildeArray.length; i++) {
			if (value.equals(gildeArray[i].getValue())) {
				return gildeArray[i]; // Gefunden
			}
		}
		LOG.severe("value konnte nicht gefunden werden!");
		return null;
	}

	/**
	 * Liefert zu einem valuedie entsprechende Enum zurück.
	 * 
	 * @param value
	 *            Die ID des magieMerkmals
	 * @return Die Enum MagieMerkmal die zu value gehört
	 */
	public static MagieMerkmal getMagieMerkmalByValue(String value) {

		// Sicherstellen das auch ein Element gefunden wurde
		assert magieMerkmale.get(value) != null;

		// Suchen + zurückliefern des Elements
		return magieMerkmale.get(value);
	}

	/**
	 * Liefert zu einem value die entsprechende Enum zurück.
	 * 
	 * @param value
	 *            Die ID der CharArten
	 * @return Die Enum CharArten die zu value gehört
	 */
	public static CharArten getCharArtenByValue(String value) {
		CharArten[] charArtenArray = CharArten.values();

		// Suchen des richtigen Elements
		for (int i = 0; i < charArtenArray.length; i++) {
			if (value.equals(charArtenArray[i].getValue())) {
				return charArtenArray[i]; // Gefunden
			}
		}

		LOG.severe("XmlValue konnte nicht gefunden werden!");

		return null;
	}
}
