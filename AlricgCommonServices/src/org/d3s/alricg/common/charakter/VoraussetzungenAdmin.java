/*
 * Created on 20.07.2005 / 17:44:06
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.common.charakter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.links.IdLink;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse überwacht alle Voraussetzungen die ein Held erfüllen muß. 
 * Es werden alle Voraussetungen zu einem Helden in dieser Klasse gesammelt 
 * und verwaltet, so dass folgendes abgefragt werden kann:
 * 1. Die min /max Werte aller CharElemente
 * 2. Ob ein Element entfernd oder hinzugefügt werden kann
 * 3. Ob eine Voraussetzung erfüllt ist
 * 
 * @author V. Strelow
 */
public class VoraussetzungenAdmin {

	// Speichert alle Abhängigkeiten von CharElementen aufgrund von Voraussetzungen
	private AbhaengigkeitsHash abhaengigkeitsHash;
	private Charakter held;
	
	/**
	 * Konstruktor
	 * @param CharakterDaten Held der zu dem diesem VoraussetzungenAdmin gehört
	 */
	public VoraussetzungenAdmin(Charakter held) {
		this.held = held;
		this.abhaengigkeitsHash = new AbhaengigkeitsHash();
	}
	
	/**
	 * Fügt eine Link zum Admin hinzu. Es werden alle nötigen Voraussetzungen ermittelt und
	 * gespeichert
	 * @param voraussetzung Die neue Voraussetzung die der Held nun erfüllen muß
	 */
	public void addVoraussetzungLink(Link link) {
		List<Link> linkList = new ArrayList<Link>();
		isVoraussetzungErfuellt(link, linkList, null);
		if (linkList.size() > 0) {
			abhaengigkeitsHash.addElement(link, linkList);
		}
	}
	
	/**
	 * Entfernt eine Link vom Admin. Dies NUR dann ein Element sein, das eine Voraussetzung ist,
	 * wenn es eine Alternative gibt
	 * @param voraussetzung Die Voraussetzung die von Held nun entfernt wird
	 */
	public void removeVoraussetzungLink(Link link) {
		abhaengigkeitsHash.removeElementWasVoraussetzungBenoetigt(link);
		
		if (abhaengigkeitsHash.isElementVonAnderenBenoetigt(link)) {
			List<Link> alternativenSuchenList = abhaengigkeitsHash.getElementeWelcheDiesesBenoetigen(link);
			abhaengigkeitsHash.removeElementWasVoraussetzungIst(link);
			
			// So sollte jeder Link neue Voraussetzungen bekommen
			for (Link l : alternativenSuchenList) {
				this.addVoraussetzungLink(l);
			}
		}
		
		// TODO kann wirklich nur ein "ElementWasVoraussetzungBenoetigt" entfernt werden? 
		// sollte zwar, aber wer weiß...
	}
	
	/**
	 * Prüft ob es für diesen Link Voraussetzungen gibt und liefert den entsprechenden
	 * Wert zurück.
	 * @param maxStufe Die bisher bestimmte maximale Stufe
	 * @param link Der Link für den der maximale Wert bestimmt werden soll
	 * @return Der Maximal mögliche Wert
	 */
	public int changeMaxWert(int maxStufe, Link link) {
		return changeMinMaxWert(maxStufe, link, false);
	}
	
	/**
	 * Prüft ob es für diesen Link Voraussetzungen gibt und liefert den entsprechenden
	 * Wert zurück.
	 * @param minStufe Die bisher bestimmte minimale Stufe
	 * @param link Der Link für den der minimale Wert bestimmt werden soll
	 * @return Der minimal mögliche Wert
	 */
	public int changeMinWert(int minStufe, Link link) {

		// Sind Links von diesem Link abhängig?
		List<Link> linkList = abhaengigkeitsHash.getElementeWelcheDiesesBenoetigen(link);
		if (linkList == null) return minStufe;
		
		return changeMinMaxWert(minStufe, link, true);
	}
	
	/**
	 * Ändert den Min oder Max wert, wenn dieser als Voraussetzung begrenzt ist
	 * @param stufe
	 * @param link
	 * @param isMin
	 * @return
	 */
	private int changeMinMaxWert(int stufe, Link link, boolean isMin) {

		List<Link> linkList = abhaengigkeitsHash.getElementeWelcheDiesesBenoetigen(link);
		if (linkList == null) return stufe;
		
		// Alle Links durchgehen...
		for (Link abhaengigeLinks : linkList) {
			List<OptionVoraussetzung> optVorausList;
			
			if (isMin) {
				optVorausList = abhaengigeLinks.getZiel().getVoraussetzung().getPosVoraussetzung();
			} else {
				optVorausList = abhaengigeLinks.getZiel().getVoraussetzung().getNegVoraussetzung();
			}
			
			if (optVorausList == null) continue;
			
			// alle Optionen...
			for (OptionVoraussetzung optVoraus : optVorausList) {
				List<IdLink> vorausList= optVoraus.getLinkList();
				// alle geforderten Voraussetzungen...
				for (int idx = 0; idx < vorausList.size(); idx++) {
					if (vorausList.get(idx).getZiel().equals(link.getZiel())) {
						// diese Voraussetzung braucht den Link.
						// Gibt es eine Abhängigkeit vom Wert und spielte diese überhaupt eine Rolle?
						if (vorausList.get(idx).getWert() != CharElement.KEIN_WERT 
								&& vorausList.get(idx).getWert() > 0) {
							
							if (isMin) {
								if (vorausList.get(idx).getWert() >= stufe && this.isVoraussetzungErfuellt(vorausList.get(idx), null, link)) 
									stufe = vorausList.get(idx).getWert();
							} else {
								if (vorausList.get(idx).getWert() <= stufe) 
									stufe = vorausList.get(idx).getWert();
							}
						}
					}
				}
			}
		}
		
		return stufe;
	}
	
	/**
	 * Prüft ob ein Element hinzugefügt werden kann. Dabei geht es darum, ob 
	 * andere, bereits zum Helden gehörende CharElemente mit diesem Element
	 * unvereinbar sind, es also verbieten oder anders herum ein Verbot
	 * besteht. 
	 * ACHTUNG: Es werden NICHT die Voraussetzungen des "tmpLink" geprüft! Dafür 
	 * gibt es die Methode "isVoraussetzungErfuellt(Voraussetzung voraussetzung)"
	 * @param canAdd Das Ergebnis der bisherigen Prüfung
 	 * @param tmpLink Der Link, der geprüft werden soll
	 * @return true - Das Element ist nicht unvereinbar mit bereits zum Helden
	 * 		gehörenden Elementen, ansonsten false
	 */
	public boolean changeCanAddElement(boolean canAdd, Link tmpLink) {
		if (!canAdd) return canAdd;
		
		return isVoraussetzungErfuellt(tmpLink, null, null);
	}
	
	/**
	 * Prüft ob ein Element aus dem Helden entfernd werden kann. Dabei geht 
	 * es darum, ob andere, bereits zum Helden gehörende CharElemente dieses 
	 * Element als Voraussetzung benötigen. 
	 * 
	 * @param canRemove Das Ergebnis der bisherigen Prüfung
 	 * @param tmpLink Der Link, der geprüft werden soll
	 * @return true - Das Element ist nicht Voraussetzung für andere
	 * 		zum Helden gehörende Elemnet, ansonsten false
	 */
	public boolean changeCanRemoveElement(boolean canRemove, Link link) {
		if (!canRemove) return canRemove;
		
		List<Link> linkList = abhaengigkeitsHash.getElementeWelcheDiesesBenoetigen(link);
		if (linkList == null) return canRemove;
		
		for (Link l : linkList) {
			if (!isVoraussetzungErfuellt(l, null, link)) {
				return false;
			}
		}
		
		return canRemove;
	}
	
	/**
	 * Prüft ob ein Element aus dem Helden geupdated werden kann. Dabei geht 
	 * es darum, ob andere, bereits zum Helden gehörende CharElemente dieses 
	 * Element als Voraussetzung benötigen. 
	 */
	public boolean changeCanUpdateWert(boolean canUpdate, Link link) {

		// Kann pauschal nicht gesagt werden, da der neue Wert nötig
		// wäre, um diesen geht es hier jedoch nicht.
		
		return canUpdate;
	}
	

	/**
	 * Prüft ob ein Element aus dem Helden geupdated werden kann. Dabei geht 
	 * es darum, ob andere, bereits zum Helden gehörende CharElemente dieses 
	 * Element als Voraussetzung benötigen. 
	 */
	public boolean changeCanUpdateText(boolean canUpdate, Link link) {
		if (!canUpdate) return canUpdate;
		
		String oldText = link.getText();
		
		// Welche ANDEREN Link hängen evtl. vom Wert ab?
		List<Link> linkList = abhaengigkeitsHash.getElementeWelcheDiesesBenoetigen(link);
		
		try {
			link.setText("<<tempTestValue>>");
			canUpdate = changeCanUpdateHelper(link, linkList);

		} finally {
			link.setText(oldText);
		}
		
		return canUpdate;
	}
	/**
	 * Prüft ob ein Element aus dem Helden geupdated werden kann. Dabei geht 
	 * es darum, ob andere, bereits zum Helden gehörende CharElemente dieses 
	 * Element als Voraussetzung benötigen. 
	 */
	public boolean changeCanUpdateZweitZiel(boolean canUpdate, Link link, CharElement zweitZiel) {
		if (!canUpdate) return canUpdate;
		
		CharElement oldCharElement = link.getZweitZiel();
		
		// Welche ANDEREN Link hängen evtl. vom Wert ab?
		List<Link> linkList = abhaengigkeitsHash.getElementeWelcheDiesesBenoetigen(link);
		
		try {
			Rasse tmpRasse = new Rasse();
			tmpRasse.setId("RAS-<<tmpTestWer>>");
			link.setZweitZiel(tmpRasse);
			canUpdate = changeCanUpdateHelper(link, linkList);
		} finally {
			link.setZweitZiel(oldCharElement);
		}
		
		return canUpdate;
	}
	
	/**
	 * Prüft ob ein Link, der bereits zum Helden gehört, momentan alle Voraussetzungen 
	 * erfüllt.  Der Link wurde temporär geändert.
	 * @param link Link der überprüft wird
	 * @param evtlAbhaengigeLinkList Liste von Links, die abhängig von 'link'
	 * 		sein könnten (kann nicht in der Methode ermittelt werden, da 'link'
	 * 		temporär geändert wurde)
	 * @return true - 'link' ist mit allen Voraussetzungen vereinbar, ansonsten false.
	 */
	private boolean changeCanUpdateHelper(Link link, List<Link> evtlAbhaengigeLinkList) {
		if (!this.isVoraussetzungErfuellt(link, null, null)) return false;
		
		if (evtlAbhaengigeLinkList == null) return true;
		for (Link l : evtlAbhaengigeLinkList) {
			if (!isVoraussetzungErfuellt(l, null, null)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Prüft alle Voraussetzungen zu einem Link, d.h. ob diese erfüllt werden
	 * @param link Link der Geprüft werden soll
	 * @param alsVoraussetzungBenutzteLinks ein RETURN Parameter - speichert alle als
	 * 		Voraussetzung benutzen Links. "null" erlaubt.
	 * @param ohneDiesenLink - Dieser Link wird als nicht existent behandelt. "null" erlaubt.
	 * @return true - Alle Voraussetzungen wurden erfüllt, ansonsten false
	 */
	private boolean isVoraussetzungErfuellt(Link link, List<Link> alsVoraussetzungBenutzteLinks, Link ohneDiesenLink) {
		
		Voraussetzung voraussetzung = link.getZiel().getVoraussetzung();
		
		// Diese positiven Optionen müssen erfüllt sein
		List<Link> tmpList = new ArrayList<Link>(); 
		if (voraussetzung != null && voraussetzung.getPosVoraussetzung() != null) {
			for (OptionVoraussetzung opt : voraussetzung.getPosVoraussetzung()) {
				if (!isPositiveOptionErfuellt(opt, link, ohneDiesenLink, tmpList)) return false;
				if (alsVoraussetzungBenutzteLinks != null) alsVoraussetzungBenutzteLinks.addAll(tmpList);
				tmpList.clear();
			}
		}
		
		// Diese negativen Optionen müssen erfüllt sein
		if (voraussetzung != null && voraussetzung.getNegVoraussetzung() != null) {
			for (OptionVoraussetzung opt : voraussetzung.getNegVoraussetzung()) {
				if (!isNegativeOptionErfuellt(opt, link, ohneDiesenLink, null, tmpList)) return false;
				if (alsVoraussetzungBenutzteLinks != null) alsVoraussetzungBenutzteLinks.addAll(tmpList);
				tmpList.clear();
			}
		}
		
		// Überprüfe die negativen Optionen die bereits zum Helden gehören
		List<Link> abhaengigeLinks = abhaengigkeitsHash.getElementeWelcheDiesesBenoetigen(link);
		if (abhaengigeLinks == null) return true;
		
		for (Link abHangigLink : abhaengigeLinks) {
			voraussetzung = abHangigLink.getZiel().getVoraussetzung();
			if (voraussetzung.getNegVoraussetzung() == null) continue;
			for (OptionVoraussetzung opt : voraussetzung.getNegVoraussetzung()) {
				if (!isNegativeOptionErfuellt(opt, abHangigLink, ohneDiesenLink, link, tmpList)) return false;
				tmpList.clear();
			}
		}
		
		return true;
	}
	
	/**
	 * Prüft ob eine positive Voraussetzung vom aktuellem Held erfüllt werden kann.
	 * @param option Option die aktuell geprüft wird
	 * @param addLink Link der zum Helden hinzugefügt werden soll
	 * @param ohneDiesenLink - Dieser Link wird als nicht existent behandelt (für Prüfung, ob er 
	 * 		gelöscht werden kann).
	 * @param voraussetzungErfuelltDurch Speichert die Links, welche zur erfüllung benutzt wurden
	 * @return true - Diese Option kann erfüllt werden mit den Links in "voraussetzungErfuelltDurch".
	 * 		false - Falls eine Erfüllung nicht möglich ist.
	 */
	private boolean isPositiveOptionErfuellt(OptionVoraussetzung option, Link addLink, 
			Link ohneDiesenLink, List<Link> voraussetzungErfuelltDurch) {
		
		int nochZuErfuellen = getAnzahlLinksZuFinden(option, addLink);
		if (nochZuErfuellen == 0) return true;
		
		for (int i = 0; i < option.getLinkList().size(); i++) {
			IdLink voraussetzungsLink = (IdLink) option.getLinkList().get(i);

			Prozessor prozessor = held.getProzessor(voraussetzungsLink.getZiel().getClass());
			
			// Alle Links mit gleichem Ziel, ZweitZiel & Text 
			List<Link> list = prozessor.getElementBox().getEqualObjects(voraussetzungsLink);
			
			for (Link l : list) {
				if (l.equals(ohneDiesenLink)) continue;
				
				// Ist bei mindestens einem der Wert wie gefordert?
				if ( l.getWert() >= voraussetzungsLink.getWert() ) {
					nochZuErfuellen--; // Ein erfüllte Option
					if (voraussetzungErfuelltDurch != null)	voraussetzungErfuelltDurch.add(l);
					break;
				}
			}
			
			if (nochZuErfuellen <= 0) break; // Genügent gefunden, weitere Suche unnötig.
		}
		
		if (nochZuErfuellen > 0)  {
			// NICHT genug Links gefunden, prüfen ob Alternativen möglich sind
			if (option.getAlternativOption() != null) {
				return isPositiveOptionErfuellt(
						(OptionVoraussetzung) option.getAlternativOption(), 
						addLink, 
						ohneDiesenLink,
						voraussetzungErfuelltDurch);
			}
		}
		
		if (nochZuErfuellen <= 0) return true;
		return false;
	}
	
	/**
	 * Prüft ob eine negative Voraussetzung vom aktuellem Held erfüllt werden kann.
	 * @param option Option die aktuell geprüft wird
	 * @param addLink Link der zum Helden hinzugefügt werden soll
	 * @param ohneDiesenLink - Dieser Link wird als nicht existent behandelt.
	 * @return true - Diese Option kann erfüllt werden mit den Links in "voraussetzungErfuelltDurch".
	 * 		false - Falls eine Erfüllung nicht möglich ist.
	 */
	private boolean isNegativeOptionErfuellt(OptionVoraussetzung option, Link addLink, 
			Link ohneDiesenLink, Link mitDiesemLink, List<Link> voraussetzungErfuelltDurch) {
		
		int nochZuErfuellen = getAnzahlLinksZuFinden(option, addLink);
		if (nochZuErfuellen == 0) return true;
		
		for (int i = 0; i < option.getLinkList().size(); i++) {
			IdLink voraussetzungsLink = (IdLink) option.getLinkList().get(i);
			voraussetzungErfuelltDurch.add(voraussetzungsLink);
			
			Prozessor prozessor = held.getProzessor(voraussetzungsLink.getZiel().getClass());
			
			// Alle Links mit gleichem Ziel, ZweitZiel & Text 
			List<Link> list = prozessor.getElementBox().getEqualObjects(voraussetzungsLink);
			if (mitDiesemLink != null && voraussetzungsLink.isEqualLink(mitDiesemLink)) {
				list.add(mitDiesemLink);
			}
			if (list == null || list.size() == 0) {
				nochZuErfuellen--; // Ein erfüllte Option
			}
			
			for (Link l : list) {
				// Ist bei mindestens einem der Wert wie gefordert?
				if ( l.equals(ohneDiesenLink) || l.getWert() < voraussetzungsLink.getWert() ) {
					nochZuErfuellen--; // Eine erfüllte Option
					break;
				}
			}
			
			if (nochZuErfuellen <= 0) break; // Genügent gefunden, weitere Suche unnötig.
		}
		
		if (nochZuErfuellen > 0)  {
			voraussetzungErfuelltDurch.clear();
			// NICHT genug Links gefunden, prüfen ob Alternativen möglich sind
			if (option.getAlternativOption() != null) {
				return isNegativeOptionErfuellt(
						(OptionVoraussetzung) option.getAlternativOption(), 
						addLink, 
						ohneDiesenLink,
						mitDiesemLink, 
						voraussetzungErfuelltDurch);
			}
		}
		
		return (nochZuErfuellen <= 0);
	}
	
	/**
	 * Liefert die Anzahl der Links, die als Voraussetzung gefunden werden müssen
	 * @param option Die Option 
	 * @param addLink Der Link, der hinzugefügt werden soll
	 * @return Die Anzahl der zu findenen Links. Bei "0" muss kein Link gefunden werden, die
	 * 		Option ist automatisch erfüllt.
	 */
	private int getAnzahlLinksZuFinden(OptionVoraussetzung option, Link addLink) {
		
		// Ist diese Option überhaupt gültig für den Wert?
		if (option.getWert() != CharElement.KEIN_WERT 
				&& option.getWert() != 0
				&& option.getWert() > addLink.getWert()) {
			return 0;
		}
		
		int nochZuErfuellen = option.getAnzahl(); // So viele links müssen gefunden werden
		if (nochZuErfuellen == 0 || nochZuErfuellen == CharElement.KEIN_WERT) {
			// Dann müssen alle erfüllt werden
			nochZuErfuellen = option.getLinkList().size();
		}
		
		return nochZuErfuellen;
	}
	
	/**
	 * Speichert alle Abhängigkeiten von CharElementen aufgrund von Voraussetzungen
	 * @author Vincent
	 */
	private static class AbhaengigkeitsHash {
		private Hashtable<String, List<Link>> voraussetzHash = new Hashtable<String, List<Link>>();
		// 									voraussetzHash<benoetigteVoraussetzung, abhängigeLinks>

		/**
		 * Fügt eine neue Abhängigeit hinzu
		 * @param hinzugefuegt Dieses Element...
		 * @param benoetigteVoraussetzungen ...benötigt diese Elemente
		 */
		public void addElement(Link hinzugefuegt, List<Link> benoetigteVoraussetzungen) {
			
			for(Link benoetigterLink : benoetigteVoraussetzungen) {
				List<Link> listBenoetigenDiesen = voraussetzHash.get(benoetigterLink);
				if (listBenoetigenDiesen == null) {
					listBenoetigenDiesen = new ArrayList<Link>();
					voraussetzHash.put(getLinkKey(benoetigterLink), listBenoetigenDiesen);
				}
				listBenoetigenDiesen.add(hinzugefuegt);
			}
		}
		
		/**
		 * Entfernt ein Element, welche Vorraussetzung für andere Elemente ist
		 * aus der Liste. Falls das Element nicht existiert in der Liste, passiert nichts.
		 * @param isVoraussetzung Element welches Vorraussetzung für andere ist
		 */
		public void removeElementWasVoraussetzungIst(Link voraussetzung) {
			voraussetzHash.remove(voraussetzung);
		}
		
		/**
		 * Entfernt ein Element welches Vorraussetzung benoetigt hat.
		 * @param benoetigtVoraussetzung Element welches Vorraussetzung benoetigt hat.
		 */ 
		public void removeElementWasVoraussetzungBenoetigt(Link benoetigtVoraussetzung) {
			Enumeration<String> enu = voraussetzHash.keys();

			while (enu.hasMoreElements()) {
				voraussetzHash.get(enu.nextElement()).remove(benoetigtVoraussetzung);
			}
		}
		
		/**
		 * @param isVoraussetzung Element welches geprüft werden soll
		 * @return true - dieses Element ist eine Voraussetzung für andere Elemente
		 * 			ansonsten false.
		 */
		public boolean isElementVonAnderenBenoetigt(Link isVoraussetzung) {
			return voraussetzHash.containsKey(getLinkKey(isVoraussetzung));
		}
		
		/**
		 * 
		 * @param isVoraussetzung 
		 * @return Eine Liste von Elemente welche "isVoraussetzung" als Voraussetzung 
		 * 			benötigen, oder null falls solche nicht existieren.
		 */
		public List<Link> getElementeWelcheDiesesBenoetigen(Link isVoraussetzung) {
			return voraussetzHash.get(getLinkKey(isVoraussetzung));
		}
		
		/**
		 * Erstellt aus der ID, der ID des ZweitZiels und dem Text eine kombinierten Key für den Link.
		 * @param link Link für den der Key erstellt werden soll
		 * @return Der key.
		 */
		private String getLinkKey(Link link) {
			String key;
			
			key = link.getZiel().getId();
			
			if (link.getZweitZiel() != null) {
				key += "|" + link.getZweitZiel().getId();
			}
			if (link.getText() != null) {
				key += "|" + link.getText();
			}
			
			return key;
		}
		
		/*
		private boolean isGesucht(Link benötigteVoraussetzung, Link abgefragt) {
			boolean 
			
			if (!benötigteVoraussetzung.getZiel().equals(abgefragt.getZiel())) {
				return false;
			} else if (benötigteVoraussetzung.getWert() != Link.KEIN_WERT
						&& ) {
				
			}
		}*/
		
	}
}
