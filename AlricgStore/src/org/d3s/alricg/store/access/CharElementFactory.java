/*
 * Created 30.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.store.access.hide.DependecySearcher;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.KulturVariante;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.ProfessionVariante;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RasseVariante;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.SchamanenRitual;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.SchriftSprache;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Rasse.FarbenAngabe;
import org.d3s.alricg.store.charElemente.Werte.EigenschaftEnum;
import org.d3s.alricg.store.charElemente.Werte.Geschlecht;
import org.d3s.alricg.store.charElemente.Werte.Gilde;
import org.d3s.alricg.store.charElemente.charZusatz.DaemonenPakt;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.charZusatz.KostenKlasse;
import org.d3s.alricg.store.charElemente.charZusatz.MagierAkademie;
import org.d3s.alricg.store.charElemente.charZusatz.SchwarzeGabe;
import org.d3s.alricg.store.charElemente.charZusatz.WuerfelSammlung;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Erzeugt, l�scht und pr�ft Abh�ngigkieten zwischen CharElementen.
 * @author Vincent
 */
public class CharElementFactory {
	private DependecySearcher dependecySearcher;
	private static CharElementFactory self = new CharElementFactory();
	
	private CharElementFactory() {
		dependecySearcher = new DependecySearcher();
	}
	
	public static CharElementFactory getInstance() {
		return self;
	}
	
	/**
	 * Pr�ft ob Abh�ngigkeiten zwischen dem CharElement "toCheck" und den Elementen in 
	 * "accList" bestehen.
	 * @param toCheck Element das auf Abh�ngigkeiten �berpr�ft werden soll
	 * @param accList Liste mit XmlAccessorn die durchsucht werden
	 * @param monitor ProgressMonitor um den Fortschritt �berwachen zu k�nnen
	 * @return Eine Liste von TableObjects die dem User angezeigt werden k�nnen 
	 */
	public List<DependencyTableObject> checkDependencies(CharElement toCheck, 
									List<XmlAccessor> accList, IProgressMonitor monitor) {
		return dependecySearcher.checkDependencies(toCheck, accList, monitor);
	}
	
	/**
	 * Pr�ft ob Abh�ngigkeiten zwischen dem CharElement "toCheck" und allen geladenen
	 * Elementen
	 * @param toCheck Element das auf Abh�ngigkeiten �berpr�ft werden soll
	 * @param monitor ProgressMonitor um den Fortschritt �berwachen zu k�nnen
	 * @return Eine Liste von TableObjects die dem User angezeigt werden k�nnen 
	 */
	public List<DependencyTableObject> checkDependencies(CharElement toCheck, 
									IProgressMonitor monitor) {
		return checkDependencies(
				toCheck,
				StoreDataAccessor.getInstance().getXmlAccessors(),
				monitor);
	}
	
	/**
	 * L�scht das Element "toDelete" aus dem XmlAccessor "accList". Beim n�chsten
	 * Save wird dies in die XML Datei �bertragen
	 * @param toDelete Element welches gel�scht werden soll
	 * @param accessor Accessor aus dem gel�scht werden soll
	 * @return true - L�schen war erfolgreich, ansonsten false
	 */
	public boolean deleteCharElement(CharElement toDelete, XmlAccessor accessor) {
		return accessor.getMatchingList(toDelete.getClass()).remove(toDelete);
	}
	
	/**
	 * F�gt das Element zum Accessor hinzu.
	 * @param toAdd Element welches Hinzugef�gt werden soll
	 * @param accessor Accessor zum Hinzuf�gen
	 */
	public void addCharElement(CharElement toAdd, XmlAccessor accessor) {
		// List anlegen, falls diese leer ist
		if (accessor.getMatchingList(toAdd.getClass()) == null) {
			accessor.setMatchingList(toAdd.getClass(), new ArrayList());
		}
		
		((List) accessor.getMatchingList(toAdd.getClass())).add(toAdd);
	}

	
	/**
	 * Erzeugt eine neue HerkuftsVariante (RassenVariante, KulturVariante 
	 * oder ProfessionsVarianet).
	 * VORSICHT: In dieser Methode wird (noch) NICHT die Variante beim Parent 
	 * 		gesetzt.
	 * 
	 * @param clazz Die Klasse der gew�nschten zu erzeugenden Variante
	 * @param parent Die Herkunft, von der die Variante abstammt
	 * @return Eine neue Variante von "parent" vom typ "clazz"
	 */
	public CharElement buildHerkunftVariante(Class clazz, Herkunft parent) {
		CharElement charElem;
		
		// TODO Auch im parent mu� die variante nat�rlich gesetzt werden -
		// entweder hier ober je nach GUI auch im Editor
		if (clazz == RasseVariante.class) {
			charElem = new RasseVariante();
			((RasseVariante) charElem).setVarianteVon(parent);
			
		} else if (clazz == KulturVariante.class) {
			charElem = new KulturVariante();
			((KulturVariante) charElem).setVarianteVon(parent);
		} else if (clazz == ProfessionVariante.class) {
			charElem = new ProfessionVariante();
			((ProfessionVariante) charElem).setVarianteVon(parent);
		} else {
			throw new IllegalArgumentException("Keine Behandlung f�r ein Element des Typs " +
				clazz.toString() + " vorhanden.");
		}
		
		createCharElement(charElem);
		
		return charElem;
	}
	
	// Interface um zugriff zu vereinheitlichen
	private static interface ListGetter<Ziel> {
		public List<Ziel> getList(XmlAccessor xmlAccs);
		public void setList(XmlAccessor xmlAccs, List<Ziel> list);
	}
	
	/**
	 * Algorithmus um das "charElem" zum XmlAccessor "currentAcc" hinzuzuf�gen.
	 * Um den XmlAccessor korrekt anzusprechen wird ein entsprechender ListGetter ben�tigt
	 */ 
	private <T> void addToList(T charElem, XmlAccessor currentAcc, ListGetter<T> listGetter) {
		if (listGetter.getList(currentAcc) == null) {
			listGetter.setList(currentAcc, new ArrayList<T>());
		}
		
		listGetter.getList(currentAcc).add(charElem);
	}
	
	/**
	 * Erzeugt eine neues CharElement, au�er die CharElemente: 
	 * 		RassenVariante, KulturVariante oder ProfessionsVarianet.
	 * Diese m�ssen �ber eine seperate Methode erzeugt werden.
	 * 
	 * @param clazz Die Klasse des gew�nschten zu erzeugenden CharElements
	 * @return Eine neues CharElement vom typ "clazz"
	 */
	public CharElement buildCharElement(Class clazz) {
		CharElement charElem;
		
		if (clazz == Talent.class) {
			charElem = new Talent();
			createFaehigkeit((Talent) charElem);
			((Talent) charElem).setArt(Talent.Art.basis);
			((Talent) charElem).setSorte(Talent.Sorte.gesellschaft);
			
		} else if (clazz == Zauber.class) {
			charElem = new Zauber();
			createFaehigkeit((Zauber) charElem);
			
		} else if (clazz == Repraesentation.class) {
			charElem = new Repraesentation();
			
		} else if (clazz == Vorteil.class) {
			charElem = new Vorteil();
			((Fertigkeit) charElem).setArt(Fertigkeit.FertigkeitArt.allgemein);
			
		} else if (clazz == Nachteil.class) {
			charElem = new Nachteil();
			((Fertigkeit) charElem).setArt(Fertigkeit.FertigkeitArt.allgemein);
			
		} else if (clazz == Sonderfertigkeit.class) {
			charElem = new Sonderfertigkeit();
			((Fertigkeit) charElem).setArt(Fertigkeit.FertigkeitArt.allgemein);
			
		} else if (clazz == Gottheit.class) {
			charElem = new Gottheit();
			((Gottheit) charElem).setGottheitArt(Gottheit.GottheitArt.zwoelfGoettlich);
			
		} else if (clazz == Liturgie.class) {
			charElem = new Liturgie();
			createFaehigkeit((Liturgie) charElem);
			
		} else if (clazz == RegionVolk.class) {
			charElem = new RegionVolk();
			((RegionVolk) charElem).setArt(RegionVolk.RegionVolkArt.menschlich);
			
		} else if (clazz == Schrift.class) {
			charElem = new Schrift();
			createSchriftSprache((SchriftSprache) charElem);
			
		} else if (clazz == Sprache.class) {
			charElem = new Sprache();
			createSchriftSprache((SchriftSprache) charElem);
			
		} else if (clazz == Gegenstand.class) {
			charElem = new Gegenstand();
			((Gegenstand) charElem).setArt(Gegenstand.GegenstandArt.sonstiges);
			
		} else if (clazz == DaemonenPakt.class) {
			charElem = new DaemonenPakt();
			
		} else if (clazz == MagierAkademie.class) {
			charElem = new MagierAkademie();
			((MagierAkademie) charElem).setGilde(Gilde.unbekannt);
		
		} else if (clazz == SchwarzeGabe.class) {
			charElem = new SchwarzeGabe();
			
		} else if (clazz == SchamanenRitual.class) {
			charElem = new SchamanenRitual();
			((SchamanenRitual) charElem).setGrad(1);
			
		} else if (clazz == Rasse.class) {
			charElem = new Rasse();
			
			((Rasse) charElem).setAlterWuerfel(new WuerfelSammlung(new int[] {1}, new int[] {2}, 15));
			((Rasse) charElem).setHaarfarbe(
					new FarbenAngabe[]{
						new FarbenAngabe("Schwarz", 20),
					});
			((Rasse) charElem).setAugenfarbe(					
					new FarbenAngabe[]{
						new FarbenAngabe("Schwarz", 20),
					});
			((Rasse) charElem).setGeschlecht(Geschlecht.mannOderFrau);
			((Rasse) charElem).setKannGewaehltWerden(true);
			((Rasse) charElem).setGeschwindigk(8);
			((Rasse) charElem).setGewichtModi(-100);
			((Rasse) charElem).setGroesseWuerfel(new WuerfelSammlung(new int[] {2}, new int[] {20}, 160));
			
		} else if (clazz == Kultur.class) {
			charElem = new Kultur();
			
		} else if (clazz == Profession.class) {
			charElem = new Profession();
			
			((Profession) charElem).setAufwand(Profession.Aufwand.normal);
			((Profession) charElem).setArt(Profession.ProfArt.gesellschaftlich);
			
		} else {
			throw new IllegalArgumentException("Keine Behandlung f�r ein Element des Typs " +
					clazz.toString() + " vorhanden.");
		}
		
		createCharElement(charElem);
		
		return charElem;
	}
	
	/**
	 * Helper f�r "buildCharElement". Setzt in einem CharElement alle notwendigen Felder
	 */
	private void createCharElement(CharElement charElement) {
		charElement.setId(IdFactory.getInstance().getId(charElement.getClass(), null));
		charElement.setName("Unbenannt");
		charElement.setAnzeigen(true);
	}
	
	/**
	 * Helper f�r "buildCharElement". Setzt in einer Faehigkeit alle notwendigen Felder
	 */
	private void createFaehigkeit(Faehigkeit faehigkeit) {
		faehigkeit.setDreiEigenschaften(new Eigenschaft[]{
				EigenschaftEnum.MU.getEigenschaft(),
				EigenschaftEnum.MU.getEigenschaft(),
				EigenschaftEnum.MU.getEigenschaft()
			});
		faehigkeit.setKostenKlasse(KostenKlasse.A);
	}
	
	/**
	 * Helper f�r "buildCharElement". Setzt in einer SchriftSprache alle notwendigen Felder
	 */
	private void createSchriftSprache(SchriftSprache ssp) {
		ssp.setKomplexitaet(1);
		ssp.setKostenKlasse(KostenKlasse.A);
	}
	
	/**
	 * Speichert gefundene Abh�ngigkeiten und erm�glicht das Anzeigen in einer Tabelle
	 * @author Vincent
	 */
	public static class DependencyTableObject {
		private String text;
		private XmlAccessor accessor;
		private CharElement charElement;
		
		public DependencyTableObject(CharElement value, XmlAccessor accessor, String text) {
			this.text = text;
			this.accessor = accessor;
			this.charElement = value;
		}

		public String getText() {
			return text;
		}
		
		public XmlAccessor getAccessor() {
			return accessor;
		}
		
		public CharElement getCharElement() {
			return charElement;
		}
	}
}
