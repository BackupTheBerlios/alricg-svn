/*
 * Created 18.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.d3s.alricg.store.access.hide.XmlVirtualAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gabe;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.RitualKenntnis;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.charZusatz.DaemonenPakt;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.charZusatz.MagierAkademie;

/**
 * Diese Klasse bietet zugriff auf alle aktuell geladenen Elemente.
 * Alle Methoden liefern nicht modifizierbare Listen von CharElementen.
 * Bei jedem Zugriff werden die Listen neu zusammengestellt, somit 
 * sparsam zu benutzen!
 * 
 * Au�nahme ist "getXmlAccessors()". 
 * Diese Methode liefert die aktuell geladenen XmlAccessors. Jeder XmlAccessor steht 
 * f�r eine geladene  XML-Datei. Alle geladenen CharElemente geh�ren zu einem XmlAccessor.
 * Sollen CharElemente hinzugef�gt/gel�scht werden, so muss dies �ber einen XmlAccessor
 * geschehen. 
 * 
 * @author Vincent
 */
public class StoreDataAccessor implements CharElementAccessor {
	private XmlVirtualAccessor virtualAccessor;
	private static StoreDataAccessor self;
	
	protected StoreDataAccessor(XmlVirtualAccessor virtualAccessor) {
		this.virtualAccessor = virtualAccessor;
		self = this;
	}
	
	public static StoreDataAccessor getInstance() {
		return self;
	}
	
	/**
	 * @return the virtualAccessor
	 */
	public List<XmlAccessor> getXmlAccessors() {
		return virtualAccessor.getXmlAccessor();
	}
	
	public List<? extends CharElement> getMatchingList(CharElement element, CharElementAccessor accessor) {
		
		if (element instanceof Eigenschaft) {
			return accessor.getEigenschaftList();
		} else if (element instanceof Talent) {
			return accessor.getTalentList();
		} else if (element instanceof Zauber) {
			return accessor.getZauberList();
		} else if (element instanceof Repraesentation) {
			return accessor.getRepraesentationList();
		} else if (element instanceof Gabe) {
			return accessor.getGabeList();
		} else if (element instanceof Vorteil) {
			return accessor.getVorteilList();
		} else if (element instanceof Nachteil) {
			return accessor.getNachteilList();
		} else if (element instanceof Sonderfertigkeit) {
			return accessor.getSonderfList();
		} else if (element instanceof Rasse) {
			return accessor.getRasseList();
		} else if (element instanceof Kultur) {
			return accessor.getKulturList();
		} else if (element instanceof Profession) {
			return accessor.getProfessionList();
		} else if (element instanceof Gottheit) {
			return accessor.getGottheitList();
		} else if (element instanceof Liturgie) {
			return accessor.getLiturgieList();
		} else if (element instanceof RegionVolk) {
			return accessor.getRasseList();
		} else if (element instanceof RitualKenntnis) {
			return accessor.getRitualkenntnisList();
		} else if (element instanceof Schrift) {
			return accessor.getSchriftList();
		} else if (element instanceof Sprache) {
			return accessor.getSpracheList();
		} else if (element instanceof Gegenstand) {
			return accessor.getGegenstandList();
		} else if (element instanceof DaemonenPakt) {
			return accessor.getDaemonenPaktList();
		} else if (element instanceof MagierAkademie) {
			return accessor.getMagierAkademieList();
		} else {
			throw new IllegalArgumentException("Keine Behandlung f�r ein Element des Typs " +
					element.getClass() + " vorhanden.");
		}
	}
	/**
	 * Interface um den Algortmus zur Erstellung der nicht Modifizierbaren Listen
	 * von dem Datenlesen zu trennen.
	 * @author Vincent
	 *
	 * @param <Ziel> Typ der CharElemente in der Liste
	 */
	private static interface ListGetter<Ziel> {
		public List<Ziel> getList(CharElementAccessor xmlAccs);
	}
	
	/**
	 * Algoritmus zum erstellen der nicht Modifizierbaren Listen
	 * @param <Ziel> Typ der CharElemente in der Liste
	 * @param listGetter Objekt um auf die richtigen Listen zuzugreifen
	 * @return Nicht Modifizerbare Liste mit allen Objekten von Typ "Ziel" im Accessor
	 */
	private <Ziel> List<Ziel> getList(ListGetter<Ziel> listGetter) {
		final List<XmlAccessor> xmlAccs = virtualAccessor.getXmlAccessor();
		final List<Ziel> list = new ArrayList<Ziel>();
		
		for (int i = 0; i < xmlAccs.size(); i++) {
			if (listGetter.getList(xmlAccs.get(i)) == null) continue;
			list.addAll(listGetter.getList(xmlAccs.get(i)));
		}
		
		return Collections.unmodifiableList(list);
	}
	
	/**
	 * @return the eigenschaftList
	 */
	public List<Eigenschaft> getEigenschaftList() {
		ListGetter<Eigenschaft> listGetter = new ListGetter<Eigenschaft>(){
			@Override
			public List<Eigenschaft> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getEigenschaftList();
			}
		};
		
		return getList(listGetter);
	}
	
	/**
	 * @return the talentList
	 */
	public List<Talent> getTalentList() {
		ListGetter<Talent> listGetter = new ListGetter<Talent>(){
			@Override
			public List<Talent> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getTalentList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the zauberList
	 */
	public List<Zauber> getZauberList() {
		ListGetter<Zauber> listGetter = new ListGetter<Zauber>(){
			@Override
			public List<Zauber> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getZauberList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the repraesentationList
	 */
	public List<Repraesentation> getRepraesentationList() {
		ListGetter<Repraesentation> listGetter = new ListGetter<Repraesentation>(){
			@Override
			public List<Repraesentation> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getRepraesentationList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the gabeList
	 */
	public List<Gabe> getGabeList() {
		ListGetter<Gabe> listGetter = new ListGetter<Gabe>(){
			@Override
			public List<Gabe> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getGabeList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the vorteilList
	 */
	public List<Vorteil> getVorteilList() {
		ListGetter<Vorteil> listGetter = new ListGetter<Vorteil>(){
			@Override
			public List<Vorteil> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getVorteilList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the nachteilList
	 */
	public List<Nachteil> getNachteilList() {
		ListGetter<Nachteil> listGetter = new ListGetter<Nachteil>(){
			@Override
			public List<Nachteil> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getNachteilList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the sonderfList
	 */
	public List<Sonderfertigkeit> getSonderfList() {
		ListGetter<Sonderfertigkeit> listGetter = new ListGetter<Sonderfertigkeit>(){
			@Override
			public List<Sonderfertigkeit> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getSonderfList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the rasseList
	 */
	public List<Rasse> getRasseList() {
		ListGetter<Rasse> listGetter = new ListGetter<Rasse>(){
			@Override
			public List<Rasse> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getRasseList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the kulturList
	 */
	public List<Kultur> getKulturList() {
		ListGetter<Kultur> listGetter = new ListGetter<Kultur>(){
			@Override
			public List<Kultur> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getKulturList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the professionList
	 */
	public List<Profession> getProfessionList() {
		ListGetter<Profession> listGetter = new ListGetter<Profession>(){
			@Override
			public List<Profession> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getProfessionList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the gottheitList
	 */
	public List<Gottheit> getGottheitList() {
		ListGetter<Gottheit> listGetter = new ListGetter<Gottheit>(){
			@Override
			public List<Gottheit> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getGottheitList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the liturgieList
	 */
	public List<Liturgie> getLiturgieList() {
		ListGetter<Liturgie> listGetter = new ListGetter<Liturgie>(){
			@Override
			public List<Liturgie> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getLiturgieList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the regionVolkList
	 */
	public List<RegionVolk> getRegionVolkList() {
		ListGetter<RegionVolk> listGetter = new ListGetter<RegionVolk>(){
			@Override
			public List<RegionVolk> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getRegionVolkList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the ritualkenntnisList
	 */
	public List<RitualKenntnis> getRitualkenntnisList() {
		ListGetter<RitualKenntnis> listGetter = new ListGetter<RitualKenntnis>(){
			@Override
			public List<RitualKenntnis> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getRitualkenntnisList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the schriftList
	 */
	public List<Schrift> getSchriftList() {
		ListGetter<Schrift> listGetter = new ListGetter<Schrift>(){
			@Override
			public List<Schrift> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getSchriftList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the spracheList
	 */
	public List<Sprache> getSpracheList() {
		ListGetter<Sprache> listGetter = new ListGetter<Sprache>(){
			@Override
			public List<Sprache> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getSpracheList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the gegenstandList
	 */
	public List<Gegenstand> getGegenstandList() {
		ListGetter<Gegenstand> listGetter = new ListGetter<Gegenstand>(){
			@Override
			public List<Gegenstand> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getGegenstandList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the daemonenPaktList
	 */
	public List<DaemonenPakt> getDaemonenPaktList() {
		ListGetter<DaemonenPakt> listGetter = new ListGetter<DaemonenPakt>(){
			@Override
			public List<DaemonenPakt> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getDaemonenPaktList();
			}
		};
		
		return getList(listGetter);
	}
	/**
	 * @return the magierAkademieList
	 */
	public List<MagierAkademie> getMagierAkademieList() {
		ListGetter<MagierAkademie> listGetter = new ListGetter<MagierAkademie>(){
			@Override
			public List<MagierAkademie> getList(CharElementAccessor xmlAccs) {
				return xmlAccs.getMagierAkademieList();
			}
		};
		
		return getList(listGetter);
	}
	
	
}