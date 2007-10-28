/*
 * Created 27.10.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.rules;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;

/**
 * 
	startGp	- Wieviele GP insgesamt zur Verfügung stehen
	maxSchlechteEingenschaftGp - Maximale GP die durch schlechte Eigenschaften gewonnen werden können 
	maxNachteilGp - Maximale GP die durch Nachteile gewonnen werden
	maxGpEigenschaften - Maximale GP die auf Eigenschaften (MU, KL, usw) verteilt werdem dürfen
	maxEigenchafWert - Maximaler Wert von Eigenschaften ohne Modis (Z.B. durch Herkunft, Vorteile)
	minEigenchafWert- Minimaler Wert von Eigenschaften ohne Modis (Z.B. durch Herkunft, Nachteile)
	maxSozialstatus - Maximaler Wert des Sozialstatus ohne Modis (Z.B. durch Herkunft, Vorteile)
	faktorTalentGp - Faktor für berechnung der TalentGP; (KL+IN) * faktorTalentGp = TalentGP
	maxTalentAktivierung- Maximale Anzahl an Talenten die aktiviert werden können
	maxZauberAktivierungVZ- Maximale Anzahl an Zaubern für VOLLzauberer die aktiviert werden können
	maxZauberAktivierungHZ - Maximale Anzahl an Zaubern für HALBzauberer die aktiviert werden können
	diffKlugheitMuttersprache - Abhängigkeit der Muttersprache von der Klugheit (KL - diffKlugheitMuttersprache = Talentwert)
	diffKlugheitZweitsprache - Abhängigkeit der Zweitsprache von der Klugheit (KL - diffKlugheitZweitsprache = Talentwert)
	maxDiffAT-PA - Maximale Differenz unterschied bei der Verteilung der Talentpunkte auf AT und PA	
	
	skt - Wenn das Attribut "useOriginal" auf "false" steht, wird kann hier eine SKT angegeben werden.
		Die SKT MUSS im selben format wie "originalSkt" vorliegen, also von z0 - z31 und 
		jeweils von A - H.
	
	Noch nicht vollständig! Nur Generierung bisher berücksichtigt!
 * 
 * 
 * @author Vincent
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegelConfig {
	
	@XmlElementWrapper(name="SKT")
	@XmlElement(name="Z")
	private List<SktZeile> sktZeilen;
	
	@XmlElement(defaultValue="110")
	private int startGp;
	
	@XmlElement(defaultValue="30")
	private int maxSchlechteEingenschaftGp;
	
	@XmlElement(defaultValue="50")
	private int maxNachteilGp;
	
	@XmlElement(defaultValue="10")
	private int maxGpEigenschaften;
	
	@XmlElement(defaultValue="14")
	private int maxEigenschafWert;
	
	@XmlElement(defaultValue="8")
	private int minEigenschafWert;
	
	@XmlElement(defaultValue="12")
	private int maxSozialstatus;
	
	@XmlElement(defaultValue="20")
	private int faktorTalentGp;
	
	@XmlElement(defaultValue="5")
	private int maxTalentAktivierung;
	
	@XmlElement(defaultValue="10")
	private int maxZauberAktivierungVZ;
	
	@XmlElement(defaultValue="5")
	private int maxZauberAktivierungHZ;
	
	@XmlElement(defaultValue="12")
	private int maxSchlechtEigenchafWert;
	
	@XmlElement(defaultValue="5")
	private int minSchelchtEigenchafWert;
	
	@XmlElement(defaultValue="-2")
	private int diffKlugheitMuttersprache;
	
	@XmlElement(defaultValue="-4")
	private int diffKlugheitZweitsprache;
	
	@XmlElement(defaultValue="5")
	private int maxDiffAT_PA;
	
	@XmlTransient
	private static RegelConfig self;

	/**
	 * Soll nicht Manuell aufgerufen werden, nur von JaxB!
	 * 
	 */
	public RegelConfig() {
		self = this;
	}
	
	/**
	 * @return Liefert die aktuelle Instanz 
	 */
	public static RegelConfig getInstance() {
		return self;
	}
	
	/**
	 * @return the sktZeilen
	 */
	public List<SktZeile> getSktZeilen() {
		return sktZeilen;
	}
	
	/**
	 * @param sktZeilen the sktZeilen to set
	 */
	public void setSktZeilen(List<SktZeile> sktZeilen) {
		this.sktZeilen = sktZeilen;
		Collections.sort(sktZeilen);
	}

	/**
	 * @return the startGp
	 */
	public int getStartGp() {
		return startGp;
	}

	/**
	 * @param startGp the startGp to set
	 */
	public void setStartGp(int startGp) {
		this.startGp = startGp;
	}

	/**
	 * @return the maxSchlechteEingenschaftGp
	 */
	public int getMaxSchlechteEingenschaftGp() {
		return maxSchlechteEingenschaftGp;
	}

	/**
	 * @param maxSchlechteEingenschaftGp the maxSchlechteEingenschaftGp to set
	 */
	public void setMaxSchlechteEingenschaftGp(int maxSchlechteEingenschaftGp) {
		this.maxSchlechteEingenschaftGp = maxSchlechteEingenschaftGp;
	}

	/**
	 * @return the maxNachteilGp
	 */
	public int getMaxNachteilGp() {
		return maxNachteilGp;
	}

	/**
	 * @param maxNachteilGp the maxNachteilGp to set
	 */
	public void setMaxNachteilGp(int maxNachteilGp) {
		this.maxNachteilGp = maxNachteilGp;
	}

	/**
	 * @return the maxGpEigenschaften
	 */
	public int getMaxGpEigenschaften() {
		return maxGpEigenschaften;
	}

	/**
	 * @param maxGpEigenschaften the maxGpEigenschaften to set
	 */
	public void setMaxGpEigenschaften(int maxGpEigenschaften) {
		this.maxGpEigenschaften = maxGpEigenschaften;
	}

	/**
	 * @return the maxEigenschafWert
	 */
	public int getMaxEigenschafWert() {
		return maxEigenschafWert;
	}

	/**
	 * @param maxEigenschafWert the maxEigenschafWert to set
	 */
	public void setMaxEigenschafWert(int maxEigenschafWert) {
		this.maxEigenschafWert = maxEigenschafWert;
	}

	/**
	 * @return the minEigenschafWert
	 */
	public int getMinEigenschafWert() {
		return minEigenschafWert;
	}

	/**
	 * @param minEigenschafWert the minEigenschafWert to set
	 */
	public void setMinEigenschafWert(int minEigenschafWert) {
		this.minEigenschafWert = minEigenschafWert;
	}

	/**
	 * @return the maxSozialstatus
	 */
	public int getMaxSozialstatus() {
		return maxSozialstatus;
	}

	/**
	 * @param maxSozialstatus the maxSozialstatus to set
	 */
	public void setMaxSozialstatus(int maxSozialstatus) {
		this.maxSozialstatus = maxSozialstatus;
	}

	/**
	 * @return the faktorTalentGp
	 */
	public int getFaktorTalentGp() {
		return faktorTalentGp;
	}

	/**
	 * @param faktorTalentGp the faktorTalentGp to set
	 */
	public void setFaktorTalentGp(int faktorTalentGp) {
		this.faktorTalentGp = faktorTalentGp;
	}

	/**
	 * @return the maxTalentAktivierung
	 */
	public int getMaxTalentAktivierung() {
		return maxTalentAktivierung;
	}

	/**
	 * @param maxTalentAktivierung the maxTalentAktivierung to set
	 */
	public void setMaxTalentAktivierung(int maxTalentAktivierung) {
		this.maxTalentAktivierung = maxTalentAktivierung;
	}

	/**
	 * @return the maxZauberAktivierungVZ
	 */
	public int getMaxZauberAktivierungVZ() {
		return maxZauberAktivierungVZ;
	}

	/**
	 * @param maxZauberAktivierungVZ the maxZauberAktivierungVZ to set
	 */
	public void setMaxZauberAktivierungVZ(int maxZauberAktivierungVZ) {
		this.maxZauberAktivierungVZ = maxZauberAktivierungVZ;
	}

	/**
	 * @return the maxZauberAktivierungHZ
	 */
	public int getMaxZauberAktivierungHZ() {
		return maxZauberAktivierungHZ;
	}

	/**
	 * @param maxZauberAktivierungHZ the maxZauberAktivierungHZ to set
	 */
	public void setMaxZauberAktivierungHZ(int maxZauberAktivierungHZ) {
		this.maxZauberAktivierungHZ = maxZauberAktivierungHZ;
	}
	
	/**
	 * @return the maxSchlechtEigenchafWert
	 */
	public int getMaxSchlechtEigenchafWert() {
		return maxSchlechtEigenchafWert;
	}
	
	/**
	 * @param maxSchlechtEigenchafWert the maxSchlechtEigenchafWert to set
	 */
	public void setMaxSchlechtEigenchafWert(int maxSchlechtEigenchafWert) {
		this.maxSchlechtEigenchafWert = maxSchlechtEigenchafWert;
	}

	/**
	 * @return the minSchelchtEigenchafWert
	 */
	public int getMinSchelchtEigenchafWert() {
		return minSchelchtEigenchafWert;
	}

	/**
	 * @param minSchelchtEigenchafWert the minSchelchtEigenchafWert to set
	 */
	public void setMinSchelchtEigenchafWert(int minSchelchtEigenchafWert) {
		this.minSchelchtEigenchafWert = minSchelchtEigenchafWert;
	}

	/**
	 * @return the diffKlugheitMuttersprache
	 */
	public int getDiffKlugheitMuttersprache() {
		return diffKlugheitMuttersprache;
	}

	/**
	 * @param diffKlugheitMuttersprache the diffKlugheitMuttersprache to set
	 */
	public void setDiffKlugheitMuttersprache(int diffKlugheitMuttersprache) {
		this.diffKlugheitMuttersprache = diffKlugheitMuttersprache;
	}

	/**
	 * @return the diffKlugheitZweitsprache
	 */
	public int getDiffKlugheitZweitsprache() {
		return diffKlugheitZweitsprache;
	}

	/**
	 * @param diffKlugheitZweitsprache the diffKlugheitZweitsprache to set
	 */
	public void setDiffKlugheitZweitsprache(int diffKlugheitZweitsprache) {
		this.diffKlugheitZweitsprache = diffKlugheitZweitsprache;
	}

	/**
	 * @return the maxDiffAT_PA
	 */
	public int getMaxDiffAT_PA() {
		return maxDiffAT_PA;
	}

	/**
	 * @param maxDiffAT_PA the maxDiffAT_PA to set
	 */
	public void setMaxDiffAT_PA(int maxDiffAT_PA) {
		this.maxDiffAT_PA = maxDiffAT_PA;
	}
	
	/**
	 * Bestreibt eine Zeile der SKT
	 * @author Vincent
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SktZeile implements Comparable<SktZeile> {
		@XmlAttribute
		private int nr, A, B, C, D, E, F, G, H;
		
		/**
		 * Konstruktor für JaxB
		 */
		public SktZeile() {}
		
		/**
		 * Konstruktor für JUnit Test
		 */
		public SktZeile(int nr, int A, int B, int C, int D, int E, int F, int G, int H) {
			this.nr = nr;
			this.A = A;
			this.B = B;
			this.C = C;
			this.D = D;
			this.E = E;
			this.F = F;
			this.G = G;
			this.H = H;	
		}
		
		/**
		 * @param kk Gewünschte KostenKlasse
		 * @return Die Kosten aus dieser Zeile für die Kostenklasse "kk"
		 */
		public int getKosten(KostenKlasse kk) {
			switch(kk) {
			case A: return A;
			case B: return B;
			case C: return C;
			case D: return D;
			case E: return E;
			case F: return F;
			case G: return G;
			case H: return H;
			}
			throw new IllegalArgumentException("KostenKlasse " + kk + " existerit nicht!");
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(SktZeile o) {
			return this.getNr() - o.getNr();
		}

		/**
		 * @return the nr
		 */
		public int getNr() {
			return nr;
		}

		/**
		 * @param nr the nr to set
		 */
		public void setNr(int nr) {
			this.nr = nr;
		}

		/**
		 * @return the a
		 */
		public int getA() {
			return A;
		}

		/**
		 * @param a the a to set
		 */
		public void setA(int a) {
			A = a;
		}

		/**
		 * @return the b
		 */
		public int getB() {
			return B;
		}

		/**
		 * @param b the b to set
		 */
		public void setB(int b) {
			B = b;
		}

		/**
		 * @return the c
		 */
		public int getC() {
			return C;
		}

		/**
		 * @param c the c to set
		 */
		public void setC(int c) {
			C = c;
		}

		/**
		 * @return the d
		 */
		public int getD() {
			return D;
		}

		/**
		 * @param d the d to set
		 */
		public void setD(int d) {
			D = d;
		}

		/**
		 * @return the e
		 */
		public int getE() {
			return E;
		}

		/**
		 * @param e the e to set
		 */
		public void setE(int e) {
			E = e;
		}

		/**
		 * @return the f
		 */
		public int getF() {
			return F;
		}

		/**
		 * @param f the f to set
		 */
		public void setF(int f) {
			F = f;
		}

		/**
		 * @return the g
		 */
		public int getG() {
			return G;
		}

		/**
		 * @param g the g to set
		 */
		public void setG(int g) {
			G = g;
		}

		/**
		 * @return the h
		 */
		public int getH() {
			return H;
		}

		/**
		 * @param h the h to set
		 */
		public void setH(int h) {
			H = h;
		}

	}
}
