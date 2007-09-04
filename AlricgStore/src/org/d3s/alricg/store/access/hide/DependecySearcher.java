/*
 * Created 29.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access.hide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.access.CharElementFactory.DependencyTableObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.MagieMerkmal;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.charZusatz.DaemonenPakt;
import org.d3s.alricg.store.charElemente.charZusatz.MagierAkademie;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Sucht nach Abhängigkeiten zwischen CharElementen (z.B. um dies zu Prüfen bevor
 * ein Element gelöscht wird)
 * @author Vincent
 */
public class DependecySearcher {
	private List<DependencyTableObject> errorList;
	
	/**
	 * Prüft ob Abhängigkeiten zwischen dem CharElement "toCheck" und den Elementen in 
	 * "accList" bestehen.
	 * @param toCheck Element das auf Abhängigkeiten Überprüft werden soll
	 * @param monitor ProgressMonitor um den Fortschritt überwachen zu können
	 * @param accList Liste mit XmlAccessorn die durchsucht werden
	 * @return Eine Liste von TableObjects die dem User angezeigt werden können 
	 */
	public List<DependencyTableObject> checkDependencies(CharElement toCheck, 
			 List<XmlAccessor> accList, IProgressMonitor monitor) 
	{
		errorList = new ArrayList<DependencyTableObject>();
		
		monitor.beginTask("Suche nach Abhängigkeiten...", accList.size());
		for (int i = 0; i < accList.size(); i++) {
			monitor.subTask("Prüfe " + accList.get(i).getFile().getName());
			checkDependencyXmlAccessor(toCheck, accList.get(i) );
			monitor.worked(1);
			
			// Falls cancel-Button geklickt
			if (monitor.isCanceled()) {
				return null;
			}
		}
		monitor.done();
		
		return errorList;
	}
	
	/**
	 * @param toCheck Element das auf Abhängigkeiten Überprüft werden soll
	 * @param currentAcc XmlAccessor der durchsucht wird
	 */
	private void checkDependencyXmlAccessor(CharElement toCheck, XmlAccessor currentAcc) {
		for (int i = 0; i < XmlAccessor.ALL_STORED_CLASSES.length; i++) {
			if (XmlAccessor.ALL_STORED_CLASSES[i] == Eigenschaft.class) {
				continue; // Diese haben keine Abhängigkeiten zu anderen Elementen
			}
			
			checkDependencyCharElement(
					currentAcc.getMatchingList(XmlAccessor.ALL_STORED_CLASSES[i]),
					toCheck, 
					currentAcc);
		}
	}
	
	/**
	 * Überprüft ob Abhängigkeiten ziwschen dem Elementen in "charElemList" und "toCheck" 
	 * bestehen.
	 * Dies muß für jedes CharElement geprüft werden, da jedes CharElement für ein anderes
	 * als Voraussetzung gelten kann.
	 * @param proveCharElem Das CharElement welches gerade auf Abhängikeiten überprüft wird
	 * @param toCheck CharElement welches gelöscht werden soll
	 * @param currentAcc Aktueller XMLAccessor von "proveCharElem"
	 */
	private void checkDependencyCharElement(List<? extends CharElement> charElemList, CharElement toCheck, XmlAccessor currentAcc) {
		if (charElemList == null || charElemList.size() == 0) return;
		
		for (int i = 0; i < charElemList.size(); i++) {
		
			if (charElemList.get(i).getVoraussetzung() == null) continue;
			
			checkDependencyOption(
					charElemList.get(i).getVoraussetzung().getNegVoraussetzung(),
					charElemList.get(i),
					toCheck,
					currentAcc,
					"Negative Voraussetzungen");
			
			checkDependencyOption(
					charElemList.get(i).getVoraussetzung().getPosVoraussetzung(),
					charElemList.get(i),
					toCheck,
					currentAcc,
					"Positive Voraussetzungen");
		}
		
		final CharElement firstElement = charElemList.get(0);
		if (firstElement instanceof DaemonenPakt) {
			checkDependencyDaemonenPakt((List<DaemonenPakt>) charElemList, toCheck, currentAcc);
		} else if (firstElement instanceof MagierAkademie) {
			checkDependencyMagierAkademie((List<MagierAkademie>) charElemList, toCheck, currentAcc);
		} else if (firstElement instanceof Sprache) {
			checkDependencySprache((List<Sprache>) charElemList, toCheck, currentAcc);
		} else if (firstElement instanceof Liturgie) {
			checkDependencyLiturgie((List<Liturgie>) charElemList, toCheck, currentAcc);
		} else if (firstElement instanceof Zauber) {
			checkDependencyZauber((List<Zauber>) charElemList, toCheck, currentAcc);
			
		/*
		} else if (firstElement instanceof Vorteil) {
			checkDependencyVorNachteil((List<Vorteil>) charElemList, toCheck, currentAcc);
		} else if (firstElement instanceof VorNachteil) {
			checkDependencyVorNachteil((List<? extends VorNachteil>) charElemList, toCheck, currentAcc);
		*/
		} else if (firstElement instanceof Herkunft) {
			checkDependencyHerkunft((List<? extends Herkunft>) charElemList, toCheck, currentAcc);
		}
	}
	
	private void checkDependencyOption(Auswahl auswahl, CharElement proveCharElem, 
										CharElement toCheck, XmlAccessor currentAcc,
										String errorText) 
	{
		if (auswahl == null) return;
		checkDependencyOption(auswahl.getOptionen(), proveCharElem, toCheck, currentAcc, errorText);
	}
	
	private void checkDependencyOption(List<? extends Option> optionList, CharElement proveCharElem, 
			CharElement toCheck, XmlAccessor currentAcc, String errorText)  
	{
		if (optionList == null) return;
		
		for (int i = 0; i < optionList.size(); i++) {
			if (!checkDependencyOption(optionList.get(i), toCheck)) {
				errorList.add(new DependencyTableObject(proveCharElem, currentAcc, errorText));
			}
		}
	}
	
	/**
	 * Helper für die Methode "proveCharElement"
	 * @param option zu prüfende Option
	 * @param toCheck CharElement zu löschen
	 * @return true - keine Abhängeiten, ansonsten false
	 */
	private boolean checkDependencyOption(Option option, CharElement toCheck) {
		boolean flag = true;
		
		for (int i = 0; i < option.getLinkList().size(); i++) {
			flag &= checkDependencyLink((Link) option.getLinkList().get(i), toCheck);
		}
		if (flag & option.getAlternativOption() != null) {
			flag &= checkDependencyOption(option.getAlternativOption(), toCheck);
		}
		
		return flag;
	}
	
	
	private boolean checkDependencyLinkArray(Link[] linkArray, CharElement toCheck ) {
		if (linkArray == null) return true;
		boolean flag = true;
		
		for (int i = 0; i < linkArray.length; i++) {
			flag &= checkDependencyLink(linkArray[i], toCheck);
		}
		
		return flag;
	}
	
	/**
	 * Überprüft den Link "link" auf Abhängigkeiten zum CharElement "toCheck"
	 * @param link
	 * @param toCheck
	 * @return true - keine Abhängeiten, ansonsten false
	 */
	private boolean checkDependencyLink(Link link, CharElement toCheck) {
		if ( toCheck.equals(link.getZiel()) ) {
			return false;
		} else if ( toCheck.equals(link.getZweitZiel()) ) {
			return false;
		}
		return true;
	}
	
	/**
	 * Prüft den VorNachteil auf Abhängigkeiten zum CharElement "toCheck".
	 * @param vorNachList
	 * @param toCheck
	 * @param currentAcc
	 *
	private void checkDependencyVorNachteil(List<? extends VorNachteil> vorNachList, 
					CharElement toCheck,  XmlAccessor currentAcc) {
		
		if (toCheck instanceof Sonderfertigkeit) {
			for (int i = 0; i < vorNachList.size(); i++) {
				if (!checkDependencyLinkArray(vorNachList.get(i).getAendertApSf(), toCheck)) {
					errorList.add(new DependencyTableObject(vorNachList.get(i), 
							currentAcc, 
							"Verbilligte Sonderfertigkeiten"));
				}
			}
		} else if (toCheck instanceof Nachteil) {
			for (int i = 0; i < vorNachList.size(); i++) {
				if (!checkDependencyLinkArray(vorNachList.get(i).getAendertGpNachteil(), toCheck)) {
					errorList.add(new DependencyTableObject(vorNachList.get(i), 
							currentAcc, 
							"Verbilligte Nachteile"));
				}
			}
		} else if (toCheck instanceof Vorteil) {
			for (int i = 0; i < vorNachList.size(); i++) {
				if (!checkDependencyLinkArray(vorNachList.get(i).getAendertGpVorteil(), toCheck)) {
					errorList.add(new DependencyTableObject(vorNachList.get(i), 
							currentAcc, 
							"Verbilligte Vorteile"));
				}
			}
		}
	}*/
	
	private void checkDependencyZauber(List<Zauber> zauberList, CharElement toCheck, XmlAccessor currentAcc) {
		if ( !(toCheck instanceof MagieMerkmal) ) return;
		
		for (int i1 = 0; i1 < zauberList.size(); i1++) {
			MagieMerkmal[] merkmalArray = zauberList.get(i1).getMerkmale();
			
			if (merkmalArray == null) continue;
			for (int i2 = 0; i2 < merkmalArray.length; i2++) {
				if (merkmalArray[i2].equals(toCheck)) {
					errorList.add(new DependencyTableObject(zauberList.get(i1), currentAcc, "Merkmal"));
				}
			}
		}
		
	}
	
	private void checkDependencyLiturgie(List<Liturgie> liturgieList, CharElement toCheck, XmlAccessor currentAcc) {
		if ( !(toCheck instanceof Gottheit) ) return;
		
		for (int i1 = 0; i1 < liturgieList.size(); i1++) {
			Gottheit[] gottArray = liturgieList.get(i1).getGottheit();
			
			if (gottArray == null) continue;
			for (int i2 = 0; i2 < gottArray.length; i2++) {
				if (gottArray[i2].equals(toCheck)) {
					errorList.add(new DependencyTableObject(liturgieList.get(i1), currentAcc, "Gottheit"));
				}
			}
		}
		
	}
	
	
	private void checkDependencySprache(List<Sprache> spracheList, CharElement toCheck, XmlAccessor currentAcc) {

		for (int i1 = 0; i1 < spracheList.size(); i1++) {
			if( toCheck.equals(spracheList.get(i1).getWennNichtMuttersprache()) ) {
				errorList.add(new DependencyTableObject(spracheList.get(i1), currentAcc, "Falls nicht Muttersprache"));
			}
			
			// Zugehörige Schriften prüfen
			if ( toCheck instanceof Schrift &&	spracheList.get(i1).getZugehoerigeSchrift() != null) {
				for (int i2 = 0; i2 < spracheList.get(i1).getZugehoerigeSchrift().length; i2++) {
					if( toCheck.equals(spracheList.get(i1).getZugehoerigeSchrift()[i2]) ) {
						errorList.add(new DependencyTableObject(spracheList.get(i1), currentAcc, "Zugehörige Schrift"));
					}
				}
			}
		}
		
	}
	
	private void checkDependencyDaemonenPakt(List<DaemonenPakt> paktList, CharElement toCheck, XmlAccessor currentAcc) {
		
		for (int i = 0; i < paktList.size(); i++) {
			if( !checkDependencyLinkArray(paktList.get(i).getSchwarzeGaben(), toCheck)) {
				errorList.add(new DependencyTableObject(paktList.get(i), currentAcc, "Schwarze Gabe"));
			}
			if( !checkDependencyLinkArray(paktList.get(i).getVerbilligteNachteile(), toCheck)) {
				errorList.add(new DependencyTableObject(paktList.get(i), currentAcc, "Verbilligte Nachteile"));
			}
			if( !checkDependencyLinkArray(paktList.get(i).getVerbilligteSonderf(), toCheck)) {
				errorList.add(new DependencyTableObject(paktList.get(i), currentAcc, "Verbilligte Sonderfertigkeit"));
			}
			if( !checkDependencyLinkArray(paktList.get(i).getVerbilligteTalente(), toCheck)) {
				errorList.add(new DependencyTableObject(paktList.get(i), currentAcc, "Verbilligte Talente"));
			}
			if( !checkDependencyLinkArray(paktList.get(i).getVerbilligteVorteile(), toCheck)) {
				errorList.add(new DependencyTableObject(paktList.get(i), currentAcc, "Verbilligte Vorteile"));
			}
			if( !checkDependencyLinkArray(paktList.get(i).getVerbilligteZauber(), toCheck)) {
				errorList.add(new DependencyTableObject(paktList.get(i), currentAcc, "Verbilligte Zauber"));
			}
		}
	}
	
	private void checkDependencyMagierAkademie(List<MagierAkademie> akademieList, CharElement toCheck, XmlAccessor currentAcc) {
		if (toCheck.getClass() != MagierAkademie.class) return;
		
		for (int i = 0; i < akademieList.size(); i++) {
			if (akademieList.get(i).getHerkunft().equals(toCheck)) {
				errorList.add(new DependencyTableObject(akademieList.get(i), currentAcc, "Profession der Akademie"));
			}
		}
	}
	
	private void checkDependencyHerkunft(List<? extends Herkunft> herkunftList, CharElement toCheck, XmlAccessor currentAcc) {
		
		for (int i = 0; i < herkunftList.size(); i++) {
			Herkunft herkunft = herkunftList.get(i);
			
			// "normale Auswahlen"
			checkDependencyOption(
					herkunft.getVorteile(),
					herkunft,
					toCheck,
					currentAcc,
					"Vorteile");
			
			checkDependencyOption(
					herkunft.getNachteile(),
					herkunft,
					toCheck,
					currentAcc,
					"Nachteile");
			
			checkDependencyOption(
					herkunft.getSonderfertigkeiten(),
					herkunft,
					toCheck,
					currentAcc,
					"Sonderfertigkeiten");
			
			checkDependencyOption(
					herkunft.getLiturgien(),
					herkunft,
					toCheck,
					currentAcc,
					"Liturgien");
			
			checkDependencyOption(
					herkunft.getTalente(),
					herkunft,
					toCheck,
					currentAcc,
					"Talente");
			
			// magische Auswahlen
			if(herkunft.getMagieEigenschaften() != null) {
				checkDependencyOption(
						herkunft.getMagieEigenschaften().getHauszauber(),
						herkunft,
						toCheck,
						currentAcc,
						"Hauszauber");
				
				checkDependencyOption(
						herkunft.getMagieEigenschaften().getZauber(),
						herkunft,
						toCheck,
						currentAcc,
						"Modifizierte Zauber");
				
				if ( !checkDependencyLinkArray(herkunft.getMagieEigenschaften().getZusaetzlichAktivierbareZauber(), toCheck)) {
					errorList.add(new DependencyTableObject(herkunft, currentAcc, "Zusätzlich aktivierbare Zauber"));
				}
				
				if ( !checkDependencyLinkArray(herkunft.getMagieEigenschaften().getFehlendeAktivierbareZauber(), toCheck)) {
					errorList.add(new DependencyTableObject(herkunft, currentAcc, "Nicht aktivierbare Zauber"));
				}
			}
			
			// Empfohlen/ungeeignet und verbilligt
			if ( !checkDependencyLinkArray(herkunft.getEmpfVorteile(),toCheck)) {
				errorList.add(new DependencyTableObject(herkunft, currentAcc, "Empfohlene Vorteile"));
			}
			if ( !checkDependencyLinkArray(herkunft.getEmpfNachteile(),toCheck)) {
				errorList.add(new DependencyTableObject(herkunft, currentAcc, "Empfohlene Nachteile"));
			}
			if ( !checkDependencyLinkArray(herkunft.getUngeVorteile(),toCheck)) {
				errorList.add(new DependencyTableObject(herkunft, currentAcc, "Ungeeignete Vorteile"));
			}
			if ( !checkDependencyLinkArray(herkunft.getUngeNachteile(),toCheck)) {
				errorList.add(new DependencyTableObject(herkunft, currentAcc, "Ungeeignete Nachteile"));
			}
			if ( !checkDependencyLinkArray(herkunft.getVerbilligteSonderf(),toCheck)) {
				errorList.add(new DependencyTableObject(herkunft, currentAcc, "Verbilligte Sonderfertigkeiten"));
			}
			if ( !checkDependencyLinkArray(herkunft.getVerbilligteLiturgien(),toCheck)) {
				errorList.add(new DependencyTableObject(herkunft, currentAcc, "Verbilligte Liturgien"));
			}
		}
		
		final Herkunft firstElement = herkunftList.get(0);
		if (firstElement instanceof Rasse) {
			checkDependencyRasse((List<Rasse>) herkunftList, toCheck, currentAcc);
		} else if (firstElement instanceof Kultur) {
			checkDependencyKultur((List<Kultur>) herkunftList, toCheck, currentAcc);
		} else if (firstElement instanceof Profession) {
			checkDependencyProfession((List<Profession>) herkunftList, toCheck, currentAcc);
		}
		
	}
	
	
	private void checkDependencyRasse(List<Rasse> rasseList, CharElement toCheck, XmlAccessor currentAcc) {

		if (toCheck instanceof Kultur) {
			for (int i1 = 0; i1 < rasseList.size(); i1++) {
				Rasse rasse = rasseList.get(i1);
				
				for (int i2 = 0; i2 < rasse.getKulturMoeglich().length; i2++) {
					if (rasse.getKulturMoeglich()[i2].equals(toCheck)) {
						errorList.add(new DependencyTableObject(
								rasse, currentAcc, "Mögliche Kulturen"));
					}
				}
				for (int i2 = 0; i2 < rasse.getKulturUeblich().length; i2++) {
					if (rasse.getKulturUeblich()[i2].equals(toCheck)) {
						errorList.add(new DependencyTableObject(
								rasse, currentAcc, "Übliche Kulturen"));
					}
				}
			}
			
			for (int i1 = 0; i1 < rasseList.size(); i1++) {
				Rasse rasse = rasseList.get(i1);
				
				// Für Varianten
				if (rasse.getVarianten() != null) {
					checkDependencyCharElement(Arrays.asList(rasse.getVarianten()), toCheck, currentAcc);
				}
			}
			
		}
		
	}
	private void checkDependencyKultur(List<Kultur> kulturList, CharElement toCheck, XmlAccessor currentAcc) {

		if (toCheck instanceof Profession) {
			for (int i1 = 0; i1 < kulturList.size(); i1++) {
				Kultur kultur = kulturList.get(i1);
				
				for (int i2 = 0; i2 < kultur.getProfessionMoeglich().length; i2++) {
					if (kultur.getProfessionMoeglich()[i2].equals(toCheck)) {
						errorList.add(new DependencyTableObject(
								kultur.getProfessionMoeglich()[i2], 
								currentAcc, "Mögliche Profession"));
					}
				}
				for (int i2 = 0; i2 < kultur.getProfessionUeblich().length; i2++) {
					if (kultur.getProfessionUeblich()[i2].equals(toCheck)) {
						errorList.add(new DependencyTableObject(
								kultur.getProfessionMoeglich()[i2], 
								currentAcc, "Übliche Profession"));
					}
				}
			}
		}
		for (int i1 = 0; i1 < kulturList.size(); i1++) {
			Kultur kult = kulturList.get(i1);
			
			checkDependencyOption(
					kult.getMuttersprache(),
					kult,
					toCheck,
					currentAcc,
					"Muttersprache");
			
			checkDependencyOption(
					kult.getZweitsprache(),
					kult,
					toCheck,
					currentAcc,
					"Zweitsprache");
			
			checkDependencyOption(
					kult.getLehrsprache(),
					kult,
					toCheck,
					currentAcc,
					"Lehrsprache");
			
			checkDependencyOption(
					kult.getSprachen(),
					kult,
					toCheck,
					currentAcc,
					"Sprachen");
			
			checkDependencyOption(
					kult.getSchriften(),
					kult,
					toCheck,
					currentAcc,
					"Schriften");
			
			checkDependencyOption(
					kult.getAusruestung(),
					kult,
					toCheck,
					currentAcc,
					"Ausrüstung");
			
			if (toCheck.equals(kult.getRegionVolk())) {
				errorList.add(new DependencyTableObject(
						kult, currentAcc, "Region/Volk"));
			}
			
			// Für Varianten
			if (kult.getVarianten() != null) {
				checkDependencyCharElement(Arrays.asList(kult.getVarianten()), toCheck, currentAcc);
			}

		}
	}		

	private void checkDependencyProfession(List<Profession> profList, CharElement toCheck, XmlAccessor currentAcc) {
		
		for (int i1 = 0; i1 < profList.size(); i1++) {
			Profession prof = profList.get(i1);
			
			checkDependencyOption(
					prof.getSprachen(),
					prof,
					toCheck,
					currentAcc,
					"Sprachen");
			
			checkDependencyOption(
					prof.getSchriften(),
					prof,
					toCheck,
					currentAcc,
					"Schriften");
			
			checkDependencyOption(
					prof.getAusruestung(),
					prof,
					toCheck,
					currentAcc,
					"Ausrüstung");
			
			checkDependencyOption(
					prof.getBesondererBesitz(),
					prof,
					toCheck,
					currentAcc,
					"Besonderer Besitz");
			
			if ( toCheck.equals(prof.getMagierAkademie()) ) {
				errorList.add(new DependencyTableObject(toCheck, currentAcc, "Magier Akademie"));
			}
			
			// Für Varianten
			if (prof.getVarianten() != null) {
				checkDependencyCharElement(Arrays.asList(prof.getVarianten()), toCheck, currentAcc);
			}
		}
	}
	

}
