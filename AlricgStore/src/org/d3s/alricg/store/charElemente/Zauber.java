/*
 * Created 23. Dezember 2004 / 14:53:55
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;


/**
 * <b>Beschreibung:</b><br> TODO Beschreibung einfügen
 * @author V.Strelow
 */
public class Zauber extends Faehigkeit {
	private MagieMerkmal[] merkmale;
	private Verbreitung[] verbreitung; // Welche Repräsentationen den Zauber können
	private String probenModi; // Modis auf die Probe "+MR" / "+Modi"
	private String zauberdauer;
	private String aspKosten;
	private String ziel;
	private String reichweite;
	private String wirkungsdauer;
	
	/**
	 * @return Liefert das Attribut merkmale.
	 */
	@XmlElement(required = true)
	@XmlList
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public MagieMerkmal[] getMerkmale() {
		return merkmale;
	}
	
	/**
	 * @return Liefert einen kompletten String mit allen Verbreitungen 
	 * 				als Abkürzungen.
	 */
	public String getVerbreitungText(boolean alsAbk) {
		StringBuffer buffer;
		
		if (alsAbk) {
			buffer = verbreitung[0].getAbkuerzungText();
			
			for (int i = 1; i < verbreitung.length; i++) {
				buffer.append(", ").append(verbreitung[i].getAbkuerzungText());
			}
		} else {
			buffer = verbreitung[0].getVollenText();
			
			for (int i = 1; i < verbreitung.length; i++) {
				buffer.append(", ").append(verbreitung[i].getVollenText());
			}	
		}
		return buffer.toString();
	}
	
	/**
	 * @return Liefert die Verbreitungen in der der Zauber verhanden ist
	 */
	@XmlElement(required = true)
	public Verbreitung[] getVerbreitung() {
		return verbreitung;
	}	
	
	/**
	 * @return Liefert das Attribut aspKosten.
	 */
	public String getAspKosten() {
		return aspKosten;
	}
	/**
	 * @param aspKosten Setzt das Attribut aspKosten.
	 */
	public void setAspKosten(String aspKosten) {
		this.aspKosten = aspKosten;
	}
	/**
	 * @return Liefert das Attribut probenModi.
	 */
	public String getProbenModi() {
		return probenModi;
	}
	/**
	 * @param probenModi Setzt das Attribut probenModi.
	 */
	public void setProbenModi(String probenModi) {
		this.probenModi = probenModi;
	}
	/**
	 * @return Liefert das Attribut reichweite.
	 */
	public String getReichweite() {
		return reichweite;
	}
	/**
	 * @param reichweite Setzt das Attribut reichweite.
	 */
	public void setReichweite(String reichweite) {
		this.reichweite = reichweite;
	}
	/**
	 * @return Liefert das Attribut wirkungsdauer.
	 */
	public String getWirkungsdauer() {
		return wirkungsdauer;
	}
	/**
	 * @param wirkungsdauer Setzt das Attribut wirkungsdauer.
	 */
	public void setWirkungsdauer(String wirkungsdauer) {
		this.wirkungsdauer = wirkungsdauer;
	}
	/**
	 * @return Liefert das Attribut zauberdauer.
	 */
	public String getZauberdauer() {
		return zauberdauer;
	}
	/**
	 * @param zauberdauer Setzt das Attribut zauberdauer.
	 */
	public void setZauberdauer(String zauberdauer) {
		this.zauberdauer = zauberdauer;
	}
	/**
	 * @return Liefert das Attribut ziel.
	 */
	public String getZiel() {
		return ziel;
	}
	/**
	 * @param ziel Setzt das Attribut ziel.
	 */
	public void setZiel(String ziel) {
		this.ziel = ziel;
	}
	/**
	 * @param merkmale Setzt das Attribut merkmale.
	 */
	public void setMerkmale(MagieMerkmal[] merkmale) {
		this.merkmale = merkmale;
		Arrays.sort(merkmale);
	}
	/**
	 * @param verbreitung Setzt das Attribut verbreitung.
	 */
	public void setVerbreitung(Verbreitung[] verbreitung) {
		this.verbreitung = verbreitung;
	}
        
    /**
     * <u>Beschreibung:</u><br> 
     * In welchen Repräsentationen welchen Gruppierungen dieser Zauber bekannt ist.
     * @author V. Strelow
     */
    public static class Verbreitung {
    	private Repraesentation bekanntBei; // Bei welcher Gruppe der Zauber bekannt ist
    	private Repraesentation repraesentation; // In welcher Repräsentation der Zauber bekannt ist
    	private int wert; // Der Wert des bekanntheit
       
    	/* So kann z.N. "Hex(Mag)2" nachgebildet werden - Hexen bekannt in der 
    	 * Repräsentation der Magier mit dem Wert 2 
    	 */
    	
		/**
		 * @return Liefert das Attribut bekanntBei.
		 */
        @XmlAttribute
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
		public Repraesentation getBekanntBei() {
			return bekanntBei;
		}
        
        public void setBekanntBei(Repraesentation newBekanntBei) {
            bekanntBei = newBekanntBei;
        }
        
		/**
		 * @return Liefert das Attribut repraesentation.
		 */
        @XmlAttribute(required = true)
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
		public Repraesentation getRepraesentation() {
			return repraesentation;
		}
        
        public void setRepraesentation(Repraesentation newRepraesentation) {
            repraesentation = newRepraesentation;
        }
        
		/**
		 * @return Liefert das Attribut wert.
		 */
        @XmlAttribute(required = true)
		public int getWert() {
			return wert;
		}
        
        public void setWert(int newWert) {
            wert = newWert;
        }
		
		/**
		 * @return Den kompleten Text mit den Abkürzungen für die Repräsentationen
		 */
		public StringBuffer getVollenText() {
			StringBuffer buffer = new StringBuffer(repraesentation.getName());
			
			if (bekanntBei != null) {
				buffer.append("(")
					.append(bekanntBei.getName())
					.append(")");
			}
			buffer.append(" ");
			buffer.append(wert);
			
			return buffer;			
		}	
        
		/**
		 * @return Den kompleten Text mit den Abkürzungen für die Repräsentationen
		 */
		public StringBuffer getAbkuerzungText() {
			StringBuffer buffer = new StringBuffer(repraesentation.getAbkuerzung());
			
			if (bekanntBei != null) {
				buffer.append("(")
					.append(bekanntBei.getAbkuerzung())
					.append(")");
			}
			buffer.append(" ");
			buffer.append(wert);
			
			return buffer;			
		}		        
    }
}
