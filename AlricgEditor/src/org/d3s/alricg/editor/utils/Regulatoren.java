/*
 * Created 02.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.utils;

import java.util.List;

import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Werte;
import org.d3s.alricg.store.charElemente.Zauber;

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
		};
		
	public static final Regulator ZauberRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return ((Zauber) charElement).getMerkmale();
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getZauberList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Werte.MagieMerkmal.class;
			}
		};
		
	public static final Regulator LiturgieRegulator = 
		new Regulator() {
			@Override
			public Object[] getFirstCategory(CharElement charElement) {
				return ((Liturgie) charElement).getGottheit();
			}
	
			@Override
			public List<? extends CharElement> getListFromAccessor(
					XmlAccessor accessor) {
				return accessor.getZauberList();
			}
			
			@Override
			public Class getFirstCategoryClass() {
				return Gottheit.class;
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
		};
}
