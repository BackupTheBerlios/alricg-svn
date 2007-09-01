/*
 * Created 02.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.utils;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.MagieMerkmal;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.SchamanenRitual;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.Gottheit.GottheitArt;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;

/**
 * @author Vincent
 *
 */
public class Regulatoren {
	/**
	 * Schnittstelle welche für Objekte benötigt wird, mit die Methode "buildViewTree"
	 * verarbeitet werden sollen.
	 * @see org.d3s.alricg.editor.common.ViewUtils.buildViewTree(Iterator, Regulator)
	 * @author Vincent
	 */
	public static interface Regulator {
		public Object[] getFirstCategory(CharElement charElement);
		public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor);
		public Class getFirstCategoryClass();
		public void setFirstCategory(CharElement charElement, Object firstCat);
	}
	
	/**
	 * Dieser Wapper umschließt CharElemente, um bei einer "instanceof" prüfung
	 * diese als "nicht-CharElement" zu maskieren. Wichtig für Trees.
	 * @author Vincent
	 */
	public static class CharElementWapper {
		private final CharElement charElement;
		private final String text;
		private final Class clazz; 
		
		public CharElementWapper(CharElement charElement) {
			this.charElement = charElement;
			this.text = null;
			this.clazz = null;
		}
		
		public CharElementWapper(Class clazz, String text) {
			this.text = text;
			this.clazz = clazz;
			this.charElement = null;
		}
		
		public Class getWappedClass()  {
			if (clazz != null) {
				return clazz;
			}
			return charElement.getClass();
		}
		
		public CharElement getCharElement() {
			return charElement;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if ( !(obj instanceof CharElementWapper) ) return false;
			
			return getWappedClass().equals(((CharElementWapper) obj).getWappedClass())  
									&& toString().equals(obj.toString());
		}

		@Override
		public String toString() {
			if (charElement != null) {
				return charElement.getName();
			}
			return text;
		}

	}
	
	public static final Regulator TalentRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] { ((Talent) charElement).getSorte() };
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getTalentList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Talent.Sorte.class;
			}

			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				((Talent) charElement).setSorte( (Talent.Sorte) firstCat);
			}
		};
		
	public static final Regulator ZauberRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				MagieMerkmal[] mm = ((Zauber) charElement).getMerkmale();
				if (mm == null) return new Object[0];
				
				CharElementWapper[] wapper = new CharElementWapper[mm.length];
				
				for (int i = 0; i < wapper.length; i++) {
					wapper[i] = new CharElementWapper(mm[i]);
				}
				return wapper;
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getZauberList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return CharElementWapper.class;
			}
			
			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				if ( !(firstCat instanceof CharElementWapper) ) {
					((Zauber) charElement).setMerkmale(null);
					return;
				}
				final CharElementWapper wapper = (CharElementWapper) firstCat; 

				((Zauber) charElement).setMerkmale(new MagieMerkmal[] { 
							(MagieMerkmal) wapper.getCharElement() });
			}
		};
		
	public static final Regulator LiturgieRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				Gottheit[] gottArray = ((Liturgie) charElement).getGottheit();
				if (gottArray == null) return new Object[0];
				CharElementWapper[] wapper = new CharElementWapper[gottArray.length];
				
				for (int i = 0; i < wapper.length; i++) {
					wapper[i] = new CharElementWapper(gottArray[i]);
				}
				
				return wapper;
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor) {
				return accessor.getLiturgieList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return CharElementWapper.class;
			}
			
			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				if ( !(firstCat instanceof CharElementWapper) ) {
					((Liturgie) charElement).setGottheit(null);
					return;
				}
				final CharElementWapper wapper = (CharElementWapper) firstCat; 

				((Liturgie) charElement).setGottheit(new Gottheit[]{
						(Gottheit) wapper.getCharElement() });
			}
		};
		
	public static final Regulator EigenschaftRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[0];
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getEigenschaftList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Object.class;
			}
			
			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				// Noop
			}
		};
		
	public static final Regulator SonderfertigkeitRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] {((Sonderfertigkeit) charElement).getArt() };
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getSonderfList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Fertigkeit.FertigkeitArt.class;
			}
			
			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				((Sonderfertigkeit) charElement).setArt((Fertigkeit.FertigkeitArt) firstCat);
			}
		};
		
	public static final Regulator VorteilRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] {((Vorteil) charElement).getArt() };
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getVorteilList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Fertigkeit.FertigkeitArt.class;
			}
			
			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				((Vorteil) charElement).setArt((Fertigkeit.FertigkeitArt) firstCat);
			}
		};
		
	public static final Regulator NachteilRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] {((Nachteil) charElement).getArt() };
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getNachteilList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Fertigkeit.FertigkeitArt.class;
			}
			
			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				((Nachteil) charElement).setArt((Fertigkeit.FertigkeitArt) firstCat);
			}
		};
		
	public static final Regulator SchriftRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[0];
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getSchriftList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Object.class;
			}
			
			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				// Noop
			}
		};
		
	public static final Regulator SpracheRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				if (charElement instanceof Sprache) {
					return new CharElementWapper[] { 
							new CharElementWapper(Sprache.class, "Sprache") };
				} else {
					return new CharElementWapper[] { 
							new CharElementWapper(Schrift.class, "Schrift") };
				}
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) 
			{
				List<CharElement> list = new ArrayList<CharElement>();
				list.addAll(accessor.getSchriftList());
				list.addAll(accessor.getSpracheList());
				return list;
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return CharElementWapper.class;
			}
			
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				// TODO wahrscheinlich die Build-Actions überschreiben (?)
			}
		};
		
	public static final Regulator RepraesentationRegulator = 
		new Regulator() {
			private static final String SCHAMANISCH = "Schamanisch";
			private static final String MAGISCH = "Magisch";
		
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				if ( ((Repraesentation) charElement).isSchamanischeRep() ) {
					return new CharElementWapper[] { 
							new CharElementWapper(Boolean.class, SCHAMANISCH) };
				} else {
					return new CharElementWapper[] { 
							new CharElementWapper(Boolean.class, MAGISCH) };
				}
			}

			@Override
			public Class getFirstCategoryClass() {
				return CharElementWapper.class;
			}

			@Override
			public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor) {
				return accessor.getRepraesentationList();
			}

			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				if (((CharElementWapper)firstCat).toString().equals(SCHAMANISCH)) {
					((Repraesentation) charElement).setSchamanischeRep(true);
				} else {
					((Repraesentation) charElement).setSchamanischeRep(false);
				}
			} 
		};
		
	public static final Regulator MerkmalRegulator = 
		new Regulator() {
		
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[0];
			}

			@Override
			public Class getFirstCategoryClass() {
				return Object.class;
			}

			@Override
			public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor) {
				return accessor.getMagieMerkmalList();
			}

			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				// Noop
			} 
		};
		
	public static final Regulator GottheitRegulator = 
		new Regulator() {
		
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] { ((Gottheit) charElement).getGottheitArt() };
			}

			@Override
			public Class getFirstCategoryClass() {
				return Gottheit.GottheitArt.class;
			}

			@Override
			public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor) {
				return accessor.getGottheitList();
			}

			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				((Gottheit) charElement).setGottheitArt((GottheitArt) firstCat);
			} 
		};
		
	public static final Regulator SchamanenRitualRegulator = 
		new Regulator() {
		
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				Repraesentation[] rep = ((SchamanenRitual) charElement).getHerkunft();
				if (rep == null) return new Object[0];
				
				CharElementWapper[] wapper = new CharElementWapper[rep.length];
				
				for (int i = 0; i < wapper.length; i++) {
					wapper[i] = new CharElementWapper(rep[i]);
				}
				return wapper;
			}

			@Override
			public Class getFirstCategoryClass() {
				return CharElementWapper.class;
			}

			@Override
			public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor) {
				return accessor.getSchamanenRitualList();
			}

			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				if ( !(firstCat instanceof CharElementWapper) ) {
					((SchamanenRitual) charElement).setHerkunft(null);
					return;
				}
				final CharElementWapper wapper = (CharElementWapper) firstCat; 

				((SchamanenRitual) charElement).setHerkunft(new Repraesentation[] { 
							(Repraesentation) wapper.getCharElement() });
			} 
		};
		
	public static final Regulator GegenstandRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] { ((Gegenstand) charElement).getArt() };
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor) {
				return accessor.getGegenstandList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Gegenstand.GegenstandArt.class;
			}

			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				((Gegenstand) charElement).setArt( (Gegenstand.GegenstandArt) firstCat);
			}
		};
		
	public static final Regulator RegionVolkRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return new Object[] { ((RegionVolk) charElement).getArt() };
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(XmlAccessor accessor) {
				return accessor.getRegionVolkList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return RegionVolk.RegionVolkArt.class;
			}

			@Override
			public void setFirstCategory(CharElement charElement, Object firstCat) {
				((RegionVolk) charElement).setArt( (RegionVolk.RegionVolkArt) firstCat);
			}
		};

}
