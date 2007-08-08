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
import org.d3s.alricg.store.charElemente.Gabe;
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
import org.d3s.alricg.store.charElemente.RitualKenntnis;
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
 * Erzeugt, löscht und prüft Abhängigkieten zwischen CharElementen.
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
	 * Prüft ob Abhängigkeiten zwischen dem CharElement "toCheck" und den Elementen in 
	 * "accList" bestehen.
	 * @param toCheck Element das auf Abhängigkeiten Überprüft werden soll
	 * @param accList Liste mit XmlAccessorn die durchsucht werden
	 * @param monitor ProgressMonitor um den Fortschritt überwachen zu können
	 * @return Eine Liste von TableObjects die dem User angezeigt werden können 
	 */
	public List<DependencyTableObject> checkDependencies(CharElement toCheck, 
									List<XmlAccessor> accList, IProgressMonitor monitor) {
		return dependecySearcher.checkDependencies(toCheck, accList, monitor);
	}
	
	/**
	 * Prüft ob Abhängigkeiten zwischen dem CharElement "toCheck" und allen geladenen
	 * Elementen
	 * @param toCheck Element das auf Abhängigkeiten Überprüft werden soll
	 * @param monitor ProgressMonitor um den Fortschritt überwachen zu können
	 * @return Eine Liste von TableObjects die dem User angezeigt werden können 
	 */
	public List<DependencyTableObject> checkDependencies(CharElement toCheck, 
									IProgressMonitor monitor) {
		return checkDependencies(
				toCheck,
				StoreDataAccessor.getInstance().getXmlAccessors(),
				monitor);
	}
	
	/**
	 * Löscht das Element "toDelete" aus dem XmlAccessor "accList". Beim nächsten
	 * Save wird dies in die XML Datei übertragen
	 * @param toDelete Element welches gelöscht werden soll
	 * @param accessor Accessor aus dem gelöscht werden soll
	 * @return true - Löschen war erfolgreich, ansonsten false
	 */
	public boolean deleteCharElement(CharElement toDelete, XmlAccessor accessor) {
		return accessor.getMatchingList(toDelete.getClass()).remove(toDelete);
	}
	
	/**
	 * Fügt das Element zum Accessor hinzu.
	 * @param toAdd Element welches Hinzugefügt werden soll
	 * @param accessor Accessor zum Hinzufügen
	 */
	public void addCharElement(CharElement toAdd, XmlAccessor accessor) {
		((List) accessor.getMatchingList(toAdd.getClass())).add(toAdd);
	}

	
	/**
	 * Erzeugt eine neue HerkuftsVariante (RassenVariante, KulturVariante 
	 * oder ProfessionsVarianet).
	 * VORSICHT: In dieser Methode wird (noch) NICHT die Variante beim Parent 
	 * 		gesetzt.
	 * 
	 * @param clazz Die Klasse der gewünschten zu erzeugenden Variante
	 * @param parent Die Herkunft, von der die Variante abstammt
	 * @return Eine neue Variante von "parent" vom typ "clazz"
	 */
	public CharElement buildHerkunftVariante(Class clazz, Herkunft parent) {
		CharElement charElem;
		
		// TODO Auch im parent muß die variante natürlich gesetzt werden -
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
			throw new IllegalArgumentException("Keine Behandlung für ein Element des Typs " +
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
	 * Algorithmus um das "charElem" zum XmlAccessor "currentAcc" hinzuzufügen.
	 * Um den XmlAccessor korrekt anzusprechen wird ein entsprechender ListGetter benötigt
	 */ 
	private <T> void addToList(T charElem, XmlAccessor currentAcc, ListGetter<T> listGetter) {
		if (listGetter.getList(currentAcc) == null) {
			listGetter.setList(currentAcc, new ArrayList<T>());
		}
		
		listGetter.getList(currentAcc).add(charElem);
	}
	
	/**
	 * Erzeugt eine neues CharElement, außer die CharElemente: 
	 * 		RassenVariante, KulturVariante oder ProfessionsVarianet.
	 * Diese müssen über eine seperate Methode erzeugt werden.
	 * 
	 * @param clazz Die Klasse des gewünschten zu erzeugenden CharElements
	 * @return Eine neues CharElement vom typ "clazz"
	 */
	public CharElement buildCharElement(Class clazz, XmlAccessor currentAcc) {
		CharElement charElem;
		
		if (clazz == Talent.class) {
			charElem = new Talent();
			createFaehigkeit((Talent) charElem);
			((Talent) charElem).setArt(Talent.Art.basis);
			((Talent) charElem).setSorte(Talent.Sorte.gesellschaft);
			
			addToList((Talent) charElem, currentAcc, 			
				new ListGetter<Talent>() {
					@Override
					public List<Talent> getList(XmlAccessor xmlAccs) {
						return xmlAccs.getTalentList();
					}
					@Override
					public void setList(XmlAccessor xmlAccs, List<Talent> list) {
						xmlAccs.setTalentList(list);
					}
				});
			
		} else if (clazz == Zauber.class) {
			charElem = new Zauber();
			createFaehigkeit((Zauber) charElem);
			
			addToList((Zauber) charElem, currentAcc, 			
					new ListGetter<Zauber>() {
						@Override
						public List<Zauber> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getZauberList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Zauber> list) {
							xmlAccs.setZauberList(list);
						}
					});
			
		} else if (clazz == Repraesentation.class) {
			charElem = new Repraesentation();

			addToList((Repraesentation) charElem, currentAcc, 			
					new ListGetter<Repraesentation>() {
						@Override
						public List<Repraesentation> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getRepraesentationList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Repraesentation> list) {
							xmlAccs.setRepraesentationList(list);
						}
					});
			
		} else if (clazz == Gabe.class) {
			charElem = new Gabe();
			createFaehigkeit((Gabe) charElem);
			
			addToList((Gabe) charElem, currentAcc, 			
					new ListGetter<Gabe>() {
						@Override
						public List<Gabe> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getGabeList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Gabe> list) {
							xmlAccs.setGabeList(list);
						}
					});
			
		} else if (clazz == Vorteil.class) {
			charElem = new Vorteil();
			
			addToList((Vorteil) charElem, currentAcc, 			
					new ListGetter<Vorteil>() {
						@Override
						public List<Vorteil> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getVorteilList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Vorteil> list) {
							xmlAccs.setVorteilList(list);
						}
					});
			
		} else if (clazz == Nachteil.class) {
			charElem = new Nachteil();
			
			addToList((Nachteil) charElem, currentAcc, 			
					new ListGetter<Nachteil>() {
						@Override
						public List<Nachteil> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getNachteilList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Nachteil> list) {
							xmlAccs.setNachteilList(list);
						}
					});
			
		} else if (clazz == Sonderfertigkeit.class) {
			charElem = new Sonderfertigkeit();
			((Sonderfertigkeit) charElem).setArt(Sonderfertigkeit.SonderfArt.allgemein);
			
			addToList((Sonderfertigkeit) charElem, currentAcc, 			
					new ListGetter<Sonderfertigkeit>() {
						@Override
						public List<Sonderfertigkeit> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getSonderfList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Sonderfertigkeit> list) {
							xmlAccs.setSonderfList(list);
						}
					});
			
		} else if (clazz == Gottheit.class) {
			charElem = new Gottheit();
			((Gottheit) charElem).setGottheitArt(Gottheit.GottheitArt.zwoelfGoettlich);
			
			addToList((Gottheit) charElem, currentAcc, 			
					new ListGetter<Gottheit>() {
						@Override
						public List<Gottheit> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getGottheitList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Gottheit> list) {
							xmlAccs.setGottheitList(list);
						}
					});
			
		} else if (clazz == Liturgie.class) {
			charElem = new Liturgie();
			createFaehigkeit((Liturgie) charElem);
			
			addToList((Liturgie) charElem, currentAcc, 			
					new ListGetter<Liturgie>() {
						@Override
						public List<Liturgie> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getLiturgieList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Liturgie> list) {
							xmlAccs.setLiturgieList(list);
						}
					});
			
		} else if (clazz == RegionVolk.class) {
			charElem = new RegionVolk();
			
			addToList((RegionVolk) charElem, currentAcc, 			
					new ListGetter<RegionVolk>() {
						@Override
						public List<RegionVolk> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getRegionVolkList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<RegionVolk> list) {
							xmlAccs.setRegionVolkList(list);
						}
					});
			
		} else if (clazz == RitualKenntnis.class) {
			charElem = new RitualKenntnis();
			createFaehigkeit((RitualKenntnis) charElem);
			
			addToList((RitualKenntnis) charElem, currentAcc, 			
					new ListGetter<RitualKenntnis>() {
						@Override
						public List<RitualKenntnis> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getRitualkenntnisList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<RitualKenntnis> list) {
							xmlAccs.setRitualkenntnisList(list);
						}
					});
			
		} else if (clazz == Schrift.class) {
			charElem = new Schrift();
			createSchriftSprache((SchriftSprache) charElem);
			
			addToList((Schrift) charElem, currentAcc, 			
					new ListGetter<Schrift>() {
						@Override
						public List<Schrift> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getSchriftList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Schrift> list) {
							xmlAccs.setSchriftList(list);
						}
					});
			
		} else if (clazz == Sprache.class) {
			charElem = new Sprache();
			createSchriftSprache((SchriftSprache) charElem);
			
			addToList((Sprache) charElem, currentAcc, 			
					new ListGetter<Sprache>() {
						@Override
						public List<Sprache> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getSpracheList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Sprache> list) {
							xmlAccs.setSpracheList(list);
						}
					});
			
		} else if (clazz == Gegenstand.class) {
			charElem = new Gegenstand();
			
			addToList((Gegenstand) charElem, currentAcc, 			
					new ListGetter<Gegenstand>() {
						@Override
						public List<Gegenstand> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getGegenstandList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Gegenstand> list) {
							xmlAccs.setGegenstandList(list);
						}
					});
			
		} else if (clazz == DaemonenPakt.class) {
			charElem = new DaemonenPakt();
			
			addToList((DaemonenPakt) charElem, currentAcc, 			
					new ListGetter<DaemonenPakt>() {
						@Override
						public List<DaemonenPakt> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getDaemonenPaktList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<DaemonenPakt> list) {
							xmlAccs.setDaemonenPaktList(list);
						}
					});
			
		} else if (clazz == MagierAkademie.class) {
			charElem = new MagierAkademie();
			((MagierAkademie) charElem).setGilde(Gilde.unbekannt);
			
			addToList((MagierAkademie) charElem, currentAcc, 			
					new ListGetter<MagierAkademie>() {
						@Override
						public List<MagierAkademie> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getMagierAkademieList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<MagierAkademie> list) {
							xmlAccs.setMagierAkademieList(list);
						}
					});
		
		} else if (clazz == SchwarzeGabe.class) {
			charElem = new SchwarzeGabe();
			
			addToList((SchwarzeGabe) charElem, currentAcc, 			
					new ListGetter<SchwarzeGabe>() {
						@Override
						public List<SchwarzeGabe> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getSchwarzeGabeList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<SchwarzeGabe> list) {
							xmlAccs.setSchwarzeGabeList(list);
						}
					});
			
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
			
			addToList((Rasse) charElem, currentAcc, 			
					new ListGetter<Rasse>() {
						@Override
						public List<Rasse> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getRasseList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Rasse> list) {
							xmlAccs.setRasseList(list);
						}
					});
			
		} else if (clazz == Kultur.class) {
			charElem = new Kultur();
			
			addToList((Kultur) charElem, currentAcc, 			
					new ListGetter<Kultur>() {
						@Override
						public List<Kultur> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getKulturList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Kultur> list) {
							xmlAccs.setKulturList(list);
						}
					});
			
		} else if (clazz == Profession.class) {
			charElem = new Profession();
			
			((Profession) charElem).setAufwand(Profession.Aufwand.normal);
			((Profession) charElem).setArt(Profession.ProfArt.gesellschaftlich);
			
			addToList((Profession) charElem, currentAcc, 			
					new ListGetter<Profession>() {
						@Override
						public List<Profession> getList(XmlAccessor xmlAccs) {
							return xmlAccs.getProfessionList();
						}
						@Override
						public void setList(XmlAccessor xmlAccs, List<Profession> list) {
							xmlAccs.setProfessionList(list);
						}
					});
			
		} else {
			throw new IllegalArgumentException("Keine Behandlung für ein Element des Typs " +
					clazz.toString() + " vorhanden.");
		}
		
		createCharElement(charElem);
		
		return charElem;
	}
	
	/**
	 * Helper für "buildCharElement". Setzt in einem CharElement alle notwendigen Felder
	 */
	private void createCharElement(CharElement charElement) {
		charElement.setId(IdFactory.getInstance().getId(charElement.getClass(), null));
		charElement.setName("Unbenannt");
		charElement.setAnzeigen(true);
	}
	
	/**
	 * Helper für "buildCharElement". Setzt in einer Faehigkeit alle notwendigen Felder
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
	 * Helper für "buildCharElement". Setzt in einer SchriftSprache alle notwendigen Felder
	 */
	private void createSchriftSprache(SchriftSprache ssp) {
		ssp.setKomplexitaet(1);
		ssp.setKostenKlasse(KostenKlasse.A);
	}
	
	/**
	 * Speichert gefundene Abhängigkeiten und ermöglicht das Anzeigen in einer Tabelle
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
