/*
 * Created 22.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common;

import org.d3s.alricg.common.CharElementTextService;
import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTableObject;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeObject;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Faehigkeit;
import org.d3s.alricg.store.charElemente.Fertigkeit;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.SchriftSprache;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.VorNachteil;
import org.d3s.alricg.store.charElemente.Fertigkeit.AdditionsFamilie;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.links.Option;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

/**
 * @author Vincent
 *
 */
public class CustomColumnViewerSorter {
	
	/**
	 * Liefert zu einem TreeObject/TableObject mit CharElement oder Link,
	 * oder einem  CharElement oder Link direkt das zugehörige CharElement. 
	 * Gibt es KEIN zugehöriges CharElement, so wird "null" zurückgeliefert.
	 * 
	 * @param element TreeObject/TableObject/CharElement oder Link
	 * @return Das zugehörige CharElement oder "null" wenn keines existiert
	 *
	private static CharElement getCharElement(Object element) {
		return ViewUtils.getCharElement(element);
	}*/
	
	private static int getMultiplikator(Viewer viewer) {
		
		if (viewer instanceof TreeViewer) {
			if ( ((TreeViewer) viewer).getTree().getSortDirection() == SWT.DOWN) {
				return -1;
			}
		} else if (viewer instanceof TableViewer) {
			if ( ((TableViewer) viewer).getTable().getSortDirection() == SWT.DOWN) {
				return -1;
			}
		}
		
		return 1;
	}
	
	public static abstract class CreatableViewerSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (getCharElement(e1) == null || getCharElement(e2) == null) {
				return e1.toString().compareTo(e2.toString()) * getMultiplikator(viewer);
			}
			
			return getComparable(e1).compareTo(getComparable(e2))
						* getMultiplikator(viewer);
		}
		
		public ViewerSorter getNewInstance() {
			try {
				return this.getClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Liefert zu einem TreeObject/TableObject mit CharElement oder Link,
		 * oder einem  CharElement oder Link direkt das zugehörige CharElement. 
		 * Gibt es KEIN zugehöriges CharElement, so wird "null" zurückgeliefert.
		 * 
		 * @param element TreeObject/TableObject/CharElement oder Link
		 * @return Das zugehörige CharElement oder "null" wenn keines existiert
		 */
		protected static CharElement getCharElement(Object element) {
			return ViewUtils.getCharElement(element);
		}
		
		/**
		 * Liefert das Element, nach welchen verglichen werden soll
		 */
		public abstract Comparable getComparable(Object obj);
	}
	
	public static class NameSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return getCharElement(obj).getName();
		}
	}
	
	public static class CharElementKlasseSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return CharElementTextService.getCharElementName(
					getCharElement(obj).getClass());
		}
	}
	
	/**
	 * Testet lediglich ob eine Voraussetzung existiert oder nicht
	 */
	public static class CharElementVoraussetzungSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return 	getCharElement(obj).getVoraussetzung() == null;
		}
	}
	
	public static class AdditionsFamilieSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			AdditionsFamilie addFam = ((Fertigkeit) getCharElement(obj)).getAdditionsFamilie();
			if (addFam == null) return "-";
			return addFam.getAdditionsID() + " " + addFam.getAdditionsWert();
		}
	}
	
	public static class OptionNameSorter extends CreatableViewerSorter {
		@Override
		public int category(Object element) {
			if (((TreeObject) element).getValue() instanceof Option) {
				return 1;
			}
			return 2;
		}
		@Override
		public Comparable getComparable(Object obj) {
			return getCharElement(obj).getName();
		}
	}
	
	public static class SktSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			CharElement charElement = getCharElement(obj);
			
			if(charElement instanceof SchriftSprache) {
				return ((SchriftSprache) getCharElement(obj)).getKostenKlasse().getValue();
			} else {
				return ((Faehigkeit) getCharElement(obj)).getKostenKlasse().getValue();
			}
		}
	}
	
	public static class FertigkeitGpSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Fertigkeit) getCharElement(obj)).getGpKosten();
		}	
	}
	
	public static class VorNachteilGpSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			final VorNachteil vorNachteil = ((VorNachteil) getCharElement(obj));
			int kosten = 0;
			
			if (vorNachteil.getGpKosten() != CharElement.KEIN_WERT) {
				kosten += vorNachteil.getGpKosten();
			}
			if (vorNachteil.getKostenProStufe() != CharElement.KEIN_WERT) {
				kosten += (vorNachteil.getKostenProStufe());
			}
			return kosten;
		}
	}
	
	public static class SonderFertigkeitApSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return (((Fertigkeit) getCharElement(obj)).getGpKosten());
		}	
	}
	
	public static class FertigkeitBenoetigtZweitZielSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Fertigkeit) getCharElement(obj)).getBenoetigtZweitZiel();
		}	
	}
	
	public static class FertigkeitBenoetigtTextSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			Fertigkeit charElem = (Fertigkeit) getCharElement(obj);
			if ( ((Fertigkeit) charElem).isMitFreienText() || 
					( ((Fertigkeit) charElem).getTextVorschlaege() != null 
							&& ((Fertigkeit) charElem).getTextVorschlaege().length > 0) )  {
				return true;
			}
			return false;
		}	
	}
	public static class FertigkeitArtSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Sonderfertigkeit) getCharElement(obj)).getArt().getValue();
		}
	}
	
	public static class SchriftSpracheKomplexitaetSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((SchriftSprache) getCharElement(obj)).getKomplexitaet();
		}
	}
	
	public static class GegenstandWertSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Gegenstand) getCharElement(obj)).getWertInKreuzern();
		}
	}
	
	public static class GegenstandHerkunftSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			if ( ((Gegenstand) getCharElement(obj)).getHerkunft() == null) {
				return 0;
			}
			return ((Gegenstand) getCharElement(obj)).getHerkunft().length;
		}
	}
	
	public static class GegenstandArtSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((Gegenstand) getCharElement(obj)).getArt().toString();
		}
	}
	
	public static class RegionVolkArtSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((RegionVolk) getCharElement(obj)).getArt().toString();
		}
	}
	
	public static class RegionVolkAbkSorter extends CreatableViewerSorter {
		@Override
		public Comparable getComparable(Object obj) {
			return ((RegionVolk) getCharElement(obj)).getAbk();
		}
	}
	
	public static class DateiSorter extends CreatableViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			String s1, s2;
			
			if (e1 instanceof EditorTreeObject) {
				s1 = ((EditorTreeObject) e1).getFile().getName();
				s2 = ((EditorTreeObject) e2).getFile().getName();
			} else if(e1 instanceof EditorTableObject) {
				s1 = ((EditorTableObject) e1).getFile().getName();
				s2 = ((EditorTableObject) e2).getFile().getName();
			} else {
				return e1.toString().compareTo(e2.toString());
			}
			
			return s1.compareTo(s2) * getMultiplikator(viewer);
		}

		@Override
		public Comparable getComparable(Object obj) {
			// Noop
			return null;
		}
		
	}
	

}
