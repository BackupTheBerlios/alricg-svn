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
import org.d3s.alricg.gui.views.talent.TalentSpalten.Filter;
import org.d3s.alricg.gui.views.talent.TalentSpalten.Spalten;
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
    
    private static final String TEXT_ROOT_NODE_NAME = "Zauber";

	public enum Spalten {
		name("Name"),
		stern("*"),
		stufe("Stufe"),
		modis("Modis"),
		merkmale("Merkmale"),
		repraesentation("Repraesentation"),
		kostenKlasse("SKT"),
		kosten("Kosten"),
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
	
	/**
	 * <u>Beschreibung:</u><br> 
	 * Gibt die Möglichkeiten an, nach denen die Elemente in der Tabelle geordnet 
	 * werden können. "keine" ist immer vorhanden und bedeutet das nur eine
	 * normale Tabelle angezeigt wird, keine TreeTable. Ansonsten wird die 
	 * TreeTable nach der gewählten Ordnung angeordnet.
	 * @author V. Strelow
	 */
	public enum Ordnung {
		keine("Keine"),
		merkmale("Merkmale"), // Standard bei init
		repraesentation("Repräsentation");
		
		private Ordnung(String value) {
			this.bezeichner = value;
			//bezeichner = FactoryFinder.find().getLibrary().getShortTxt(value);
		}
		
		private String bezeichner;
		
		public String toString() {
			return bezeichner;
		}
	}
	
	/**
	 * <u>Beschreibung:</u><br> 
	 * Gibt die Möglichkeiten an, nach denen die Elemente in der Tabelle gefiltert 
	 * werden können. Es werden nur solche Elemente angezeigt, die zu dem Filter 
	 * passen.
	 * @author V. Strelow
	 */
	public enum Filter {
		keiner("Keiner"), // Standart bei init
		
		// Nur für direkte anzeige!
		nurWaehlbar("Nur Wählbare"),
		nurVerbilligt("Nur Verbilligte"),
		nurEigeneRepraesentation("Nur eigene Repräsentation"),

		// Nur für Generierung
		nurModifizierte("Nur Modifizierte"),
		nurHauszauber("Nur Hauszauber"),;
		
		private Filter(String value) {
			this.bezeichner = value;
			//bezeichner = FactoryFinder.find().getLibrary().getShortTxt(value);
		}
		
		private String bezeichner;
		
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
				return new Spalten[] {
					Spalten.name,
					Spalten.stern,
					Spalten.merkmale,
					Spalten.repraesentation,
					Spalten.kostenKlasse, 
					Spalten.probe, 
					Spalten.plus 
				};
				
			case objektLinkGen:
				return new Spalten[] {
					Spalten.name,
					Spalten.stufe,
					Spalten.modis,
					Spalten.kostenKlasse,
					Spalten.kosten,
					Spalten.merkmale,
					Spalten.repraesentation, 
					Spalten.probe, 
					Spalten.minus
				};
				
			case objektLinkHel:
				// TODO implement
				return null;
				
			case editorAuswahl:
				return new Spalten[] {
					Spalten.name,
					Spalten.merkmale,
					Spalten.repraesentation,
					Spalten.kostenKlasse, 
					Spalten.probe, 
					Spalten.plus 
				};
				
			case editorGewaehlt:
				return new Spalten[] {
					Spalten.name,
					Spalten.stufe,
					Spalten.merkmale,
					Spalten.repraesentation,
					Spalten.kostenKlasse, 
					Spalten.probe, 
					Spalten.plus 
				};
		}
		
		return null;
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
		switch (art) {
		case objektDirekt:
			return new Enum[] { Filter.keiner,  Filter.nurWaehlbar,  
					Filter.nurVerbilligt,  Filter.nurEigeneRepraesentation };
			
		case objektLinkHel:
			return new Enum[] { Filter.keiner }; // TODO
			
		case objektLinkGen:
			return new Enum[] { Filter.keiner, Filter.nurModifizierte, Filter.nurHauszauber };
			
		case editorAuswahl:
			return new Enum[] { Filter.keiner }; // TODO
			
		case editorGewaehlt:
			return new Enum[] { Filter.keiner }; // TODO
			
		default:
			LOG.warning("Case-Fall konnte nicht gefunden werden!");
			return null;
		}
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
		return Ordnung.values();
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getRootNodeName()
	 */
	public String getRootNodeName() {
		return TEXT_ROOT_NODE_NAME;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#initTable(org.d3s.alricg.prozessor.Prozessor, org.d3s.alricg.gui.komponenten.table.SortableTable, org.d3s.alricg.gui.views.SpaltenSchema.SpaltenArt)
	 */
	public void initTable(Prozessor prozessor, SortableTable table, SpaltenArt art) {
		
		switch (art) {
		case objektDirekt:
			table.setColumnMultiImage(Spalten.merkmale.toString());
			table.setColumnButton(Spalten.plus.toString(), Spalten.plus.toString());
					
		case objektLinkGen:
			table.setColumnMultiImage(Spalten.merkmale.toString());
			table.setColumnButton(Spalten.minus.toString(), Spalten.minus.toString());
				
		case objektLinkHel:
			// TODO implement
			break;
			
		case editorAuswahl:
			table.setColumnMultiImage(Spalten.merkmale.toString());
			table.setColumnButton(Spalten.plus.toString(), Spalten.plus.toString());
			break;
			
		case editorGewaehlt:
			table.setColumnMultiImage(Spalten.merkmale.toString());
			table.setColumnButton(Spalten.minus.toString(), Spalten.minus.toString());
			break;
			
		default:
			LOG.warning("Case-Fall konnte nicht gefunden werden!");
		}
		
	}


	
	
}
