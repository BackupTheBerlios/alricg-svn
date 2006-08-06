/*
 * Created on 20.09.2005
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.views.zauber;

import java.util.Comparator;
import java.util.logging.Logger;

import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.gui.komponenten.table.SortableTable;
import org.d3s.alricg.gui.views.ComparatorCollection;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <u>Beschreibung:</u><br> 
 * Schema für die Darstellung von Zaubern in SortableTables. Hier sind alle Methoden
 * zusammengefaßt, die NICHT von dem dargestellten Objekt abhängen, sondern nur
 * von der Objek-Art und der Spalte.
 * 
 * @see org.d3s.alricg.gui.views.SpaltenSchema
 * @author V. Strelow
 */
public class ZauberSpalten implements SpaltenSchema {
    
    /** <code>ZauberSpalten</code>'s logger */
    private static final Logger LOG = Logger.getLogger(ZauberSpalten.class.getName());

	public enum Spalten {
		name("Name"),
		merkmale("Merkmale"),
		repraesentation("Repraesentation"),
		kostenKlasse("SKT"),
		probe("Probe"),
		plus("+"),
		minus("-");
		private String bezeichner; // Name der Angezeigt wird
		
		private Spalten(String value) {
			if (value.equals("+") || value.equals("-")) {
				bezeichner = value;
			} else {
				bezeichner = FactoryFinder.find().getLibrary().getShortTxt(value);
			}
		}
		
		public String toString() {
			return bezeichner;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getSpalten(org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public Enum[] getSpalten(SpaltenArt art) {
		switch (art) {
			case objektDirekt:
			case objektLinkGen:
			case editorAuswahl:
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#initTable(org.d3s.alricg.gui.komponenten.table.SortableTable, org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public void initTable(SortableTable table, SpaltenArt art) {
		if ( art.equals(SpaltenArt.objektDirekt) ) {
			table.setColumnMultiImage(Spalten.merkmale.toString());
			table.setColumnButton(Spalten.plus.toString(), "+");
			table.setColumnButton(Spalten.minus.toString(), "-");
		} else if ( art.equals(SpaltenArt.objektLinkGen) ) {
			
		} else if ( art.equals(SpaltenArt.editorAuswahl) ) {
		
		}

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getComparator(java.lang.Object)
	 */
	public Comparator getComparator(Object column) {
		switch ((Spalten) column) {
		case name: 	return ComparatorCollection.compNamensComparator;
		case merkmale: return new Comparator<Zauber>() {
						public int compare(Zauber arg0, Zauber arg1) {
							return (arg0.getMerkmale().length - arg1.getMerkmale().length);
						}
					};
		case repraesentation: 	return new Comparator<Zauber>() {
						public int compare(Zauber arg0, Zauber arg1) {
							return (arg0.getVerbreitung().length - arg1.getVerbreitung().length);
						}
					};
		case kostenKlasse: return new Comparator<Zauber>() {
						public int compare(Zauber arg0, Zauber arg1) {
							return arg0.getKostenKlasse().toString().compareTo(arg1.getKostenKlasse().toString());
						}
					};
		}
		
		LOG.severe("Case-Fall konnte nicht gefunden werden!");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#isSortable(java.lang.Object)
	 */
	public boolean isSortable(Object column) {
		switch ((Spalten) column) {
			case probe: return false;
			case plus: return false;
			case minus: return false;
			default: return true;
		}
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getFilterElem(org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public Enum[] getFilterElem(SpaltenArt art) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getHeaderToolTip(java.lang.Object)
	 */
	public String getHeaderToolTip(Object column) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getOrdnungElem(org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public Enum[] getOrdnungElem(SpaltenArt art) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getRootNodeName()
	 */
	public String getRootNodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#initTable(org.d3s.alricg.prozessor.Prozessor, org.d3s.alricg.gui.komponenten.table.SortableTable, org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public void initTable(Prozessor prozessor, SortableTable table, SpaltenArt art) {
		// TODO Auto-generated method stub
		
	}


	
	
}
