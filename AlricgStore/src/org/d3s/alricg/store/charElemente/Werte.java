/*
 * Created 20. Januar 2005 / 15:42:20
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import java.util.ArrayList;

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
	public enum MagieMerkmalEnum {
		// TODO MagieMerkmal korregieren
		antimagie("antimagie"),
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

		private String value;
		private MagieMerkmal magieMerkmal;
		
		private MagieMerkmalEnum(String value) {
			this.value = value;
		}

		/**
		 * @return Liefert den XML-Value des Merkmals
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return the magieMerkmal
		 */
		public MagieMerkmal getMagieMerkmal() {
			return magieMerkmal;
		}

		/**
		 * @param magieMerkmal the magieMerkmal to set
		 */
		public void setMagieMerkmal(MagieMerkmal magieMerkmal) {
			this.magieMerkmal = magieMerkmal;
		}
		
		public String getId() {
			return "MER-" + super.toString(); 
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
		MU("Mut", "MU", "Basis", "EIG-MU"),
		KL("Klugheit", "KL", "Basis", "EIG-KL"),
		IN("Intuition", "IN", "Basis","EIG-IN"),
		CH("Charisma", "CH", "Basis","EIG-CH"),
		FF("Fingerfertigkeit", "FF", "Basis", "EIG-FF"),
		GE("Gewandheit", "GE", "Basis","EIG-GE"),
		KO("Konstitution", "KO", "Basis","EIG-KO"),
		KK("Koerperkraft", "KK", "Basis","EIG-KK"),
		SO("Sozialstatus", "SO", "Erweitert", "EIG-SO"),
		MR("Magieresistenz", "MR", "Errechnet", "EIG-MR", "(MU+KL+KO)/5"),
		LEP("Lebenspunkte", "LeP", "Errechnet", "EIG-LeP", "(2xKO+KK)/2"),
		ASP("Astralpunkte", "AsP", "Errechnet", "EIG-AsP", "(MU+IN+CH)/2"),
		AUP("Ausdauerpunkt", "AuP", "Errechnet", "EIG-AuP", "(MU+KO+GE)/2"),
		KA("Karmaernergie", "KaP", "Errechnet", "EIG-KaP"), 
		GS("Geschwindigkeit", "GS", "Erweitert", "EIG-GS", "8, ab GE>15=9, ab GE<11=7"),
		INI("Initiative", "INI", "Errechnet", "EIG-INI", "(2xMU+IN+GE)/5"),
		FK("FernkampfBasis", "FK", "Errechnet", "EIG-FK", "(IN+FF+KK)/5"),
		AT("AttackeBasis", "AT", "Errechnet", "EIG-AT", "(MU+GE+KK)/5"),
		PA("ParadeBasis", "PA", "Errechnet", "EIG-PA", "(IN+GE+KK)/5"),
		WSW("Wundschwelle", "WSW", "Errechnet", "EIG-WSW", "KO/2"),
		ESW("Erschöpfungsschwelle", "ESW", "Errechnet", "EIG-ESW", "KO");

		
		private final String name; // Voller Name der Eigenschaft
		private final String abk; // Abkürzung der Eigenschaft
		private final String id; // ID der Eigenschaft
		private final String sammelbegriff; // 
		private String formel;
		private Eigenschaft eigenschaftCharElement;
		

		/**
		 * @param bezeichnung
		 *            Key für Library für den vollen Namen
		 * @param abkuerzung
		 *            Key für Library für die Akkürzung des Namens
		 */
		private EigenschaftEnum(String name, String abk, String sammelbegriff, String id) {
			// TODO Lokalisierung der Strings
			// name =
			// FactoryFinder.find().getLibrary().getShortTxt(bezeichnung);
			// abk = FactoryFinder.find().getLibrary().getShortTxt(abkuerzung);
			this.name = name;
			this.abk = abk;
			this.id = id;
			this.sammelbegriff = sammelbegriff;
			this.formel = "";
		}

		private EigenschaftEnum(String name, String abk, String sammelbegriff, String id, String formel) {
			this(name, abk, sammelbegriff, id);
			
			this.formel = formel;
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
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return abk;
		}
		
		
		public String getFormel() {
			return formel;
		}
		public void setEigenschaft(Eigenschaft eigenschaftCharElement) {
			this.eigenschaftCharElement = eigenschaftCharElement;
		}
		
		public Eigenschaft getEigenschaft() {
			return eigenschaftCharElement;
		}

		public String getSammelbegriff() {
			return sammelbegriff;
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
