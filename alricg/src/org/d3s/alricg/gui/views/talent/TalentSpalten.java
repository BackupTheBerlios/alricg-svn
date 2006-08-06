/*
 * Created on 15.09.2005 / 17:12:45
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.views.talent;

import java.util.logging.Logger;

import org.d3s.alricg.gui.komponenten.table.SortableTable;
import org.d3s.alricg.gui.views.SpaltenSchema;
import org.d3s.alricg.gui.views.talent.komponenten.TalentSpezialisierungCellEditor;
import org.d3s.alricg.gui.views.talent.komponenten.WertComboboxCellEditor;
import org.d3s.alricg.prozessor.LinkProzessor;
import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.TextStore;

/**
 * <u>Beschreibung:</u><br> 
 * Schema für die Darstellung von Talenten in SortableTables. Hier sind alle Methoden
 * zusammengefaßt, die NICHT von dem dargestellten Objekt abhängen, sondern nur
 * von der Objek-Art und der Spalte.
 * 
 * @see org.d3s.alricg.gui.views.SpaltenSchema
 * @author V. Strelow
 */
public class TalentSpalten implements SpaltenSchema {
    /** <code>Spalten</code>'s logger */
    private static final Logger LOG = Logger.getLogger(TalentSpalten.class.getName());
	//private static TalentSpalten self; // Statischer selbst verweis
    private static final String TEXT_ROOT_NODE_NAME = "Talent";
    
	public enum Spalten {
		name("Name"),
		stern("*"),
		stufe("Stufe"),
		modis("Modis"),
		sorte("Sorte"),
		kostenKlasse("SKT"),
		kosten("Kosten"),
		art("Art"),
		spezialisierungen("Spez."),	
		auswahl("Auswahl"),
		leitTalent("L.T."),
		probe("Probe"),
		plus("+"),
		minus("-");
		private String bezeichner; // Name der Angezeigt wird
		
		/** Konstruktur
		 * @param value Der Tag um den bezeichner aus der Library zu laden
		 */
		private Spalten(String value) {
			if (value.equals("+") || value.equals("-") || value.equals("*")) {
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
		sorte("Sorte"); // Standard bei init
		
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
		nurBasisTalente("Nur Basis-Talente"),
		nurSpezialTalente("Nur Spezial-Talente"),
		nurBerufTalente("Nur Berufs-Talente"),

//		 Nur für Generierung
		nurAktivierte("Nur Aktivierte"), 
		nurModifizierte("Nur Modifizierte");
		
		private Filter(String value) {
			this.bezeichner = value;
			//bezeichner = FactoryFinder.find().getLibrary().getShortTxt(value);
		}
		
		private String bezeichner;
		
		public String toString() {
			return bezeichner;
		}
	}
	
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.views.SpaltenSchema#getSpalten()
	 */
	public Enum[] getSpalten(SpaltenArt art) {
		
		switch (art) {
		case objektDirekt:
			return new Spalten[] {
				Spalten.name,
				Spalten.stern,
				Spalten.sorte,
				Spalten.art,
				Spalten.kostenKlasse, 
				Spalten.probe, 
				Spalten.plus, 
			};
			
		case objektLinkGen:
			return new Spalten[] {
				Spalten.name, 
				Spalten.stufe, 
				Spalten.modis,
				Spalten.kostenKlasse, 
				Spalten.kosten,
				Spalten.art,
				Spalten.spezialisierungen,
				Spalten.auswahl,
				Spalten.leitTalent,
				Spalten.minus
			};
			
		case objektLinkHel:
			// TODO implement
			return null;
			
		case editorAuswahl:
			return new Spalten[] {
				Spalten.name, 
				Spalten.sorte, 
				Spalten.art, 
				Spalten.kostenKlasse, 
				Spalten.probe, 
				Spalten.plus
			};
			
		case editorGewaehlt:
			return new Spalten[] {
				Spalten.name, 
				Spalten.sorte, 
				Spalten.art, 
				Spalten.kostenKlasse, 
				Spalten.probe, 
				Spalten.minus
			};
			
		}

		LOG.warning("Case-Fall konnte nicht gefunden werden!");
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.views.SpaltenSchema#initTable()
	 */
	public void initTable(Prozessor prozessor, SortableTable table, SpaltenArt art) {
		
		switch (art) {
		case objektDirekt:
			table.setColumnMultiImage(Spalten.stern.toString());
			table.setColumnTextImage(Spalten.kostenKlasse.toString(), false);
			table.setColumnButton(Spalten.plus.toString(), "+");
			break;
		
		case objektLinkHel:
			break;
			
		case objektLinkGen:
			table.setColumnTextImage(Spalten.kostenKlasse.toString(), false);
			table.getColumn(Spalten.spezialisierungen.toString()).setCellEditor(new TalentSpezialisierungCellEditor());
			table.getColumn(Spalten.stufe.toString()).setCellEditor(new WertComboboxCellEditor((LinkProzessor) prozessor));
			table.setColumnButton(Spalten.minus.toString(), Spalten.minus.toString());
			setCellEditor(table, Spalten.stufe);
			break;
			
		case editorAuswahl:
			table.setColumnButton(Spalten.plus.toString(), Spalten.plus.toString());
			break;
			
		case editorGewaehlt:
			table.setColumnButton(Spalten.minus.toString(), Spalten.minus.toString());
			break;
			
		default:
			LOG.warning("Case-Fall konnte nicht gefunden werden!");
		}
		
	}
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.views.SpaltenSchema#isSortable(java.lang.Enum)
	 */
	public boolean isSortable(Object column) {

		switch ((Spalten) column) {
		case modis: return false;
		case auswahl: return false;
		case probe: return false;
		case plus: 	return false;
		case minus: return false;
		}
		
		return true;
	}
	
	private void setCellEditor(SortableTable table, Spalten spalte) {
		
		
		
		switch (spalte) {
		case stufe:
			
		}

		/*
		TableColumn sportColumn = sTree.getColumnModel().getColumn(2);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("Snowboarding");
		comboBox.addItem("Rowing");
		comboBox.addItem("Chasing toddlers");
		comboBox.addItem("Speed reading");
		comboBox.addItem("Teaching high school");
		comboBox.addItem("None");
		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
		*/
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getOrdnungElem()
	 */
	public Enum[] getOrdnungElem(SpaltenArt art) {
		return Ordnung.values();
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.gui.views.TypSchema#getFilterElem()
	 */
	public Enum[] getFilterElem(SpaltenArt art) {
		switch (art) {
		case objektDirekt:
			return new Enum[] { Filter.keiner,  Filter.nurWaehlbar,  
					Filter.nurVerbilligt,  Filter.nurBasisTalente,  
					Filter.nurSpezialTalente,  Filter.nurBerufTalente };
			
		case objektLinkHel:
			return new Enum[] { Filter.keiner }; // TODO
			
		case objektLinkGen:
			return new Enum[] { Filter.keiner, Filter.nurAktivierte, 
								Filter.nurModifizierte };
			
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
	 * @see org.d3s.alricg.GUI.views.SpaltenSchema#getHeaderToolTip(java.lang.Enum)
	 */
	public String getHeaderToolTip(Object column) {
        final TextStore lib = FactoryFinder.find().getLibrary();
        
		switch ((Spalten) column) {
			case name: 	return lib.getToolTipTxt("TblHeaderName");
			case stern: 	return lib.getToolTipTxt("TblHeaderStern");
			case stufe: 	return lib.getToolTipTxt("TblHeaderStufe");
			case modis: 	return lib.getToolTipTxt("TblHeaderModis");
			case kosten: 	return lib.getToolTipTxt("TblHeaderKosten");
			case spezialisierungen:	return lib.getToolTipTxt("TblHeaderSpezi");
			case auswahl:	return lib.getToolTipTxt("TblHeaderAuswahl");
			case leitTalent: return lib.getToolTipTxt("TblHeaderLeittalent");
			case sorte: return lib.getToolTipTxt("TblHeaderTalentSorte");
			case art: 	return lib.getToolTipTxt("TblHeaderTalentArt");
			case kostenKlasse: return lib.getToolTipTxt("TblHeaderKostenklasse");
			case probe: return lib.getToolTipTxt("TblHeaderProbe");
			case plus: 	return lib.getToolTipTxt("TblHeaderPlusButton");
			case minus: 	return lib.getToolTipTxt("TblHeaderMinusButton");
		}
		
		LOG.warning("Case-Fall konnte nicht gefunden werden!");
		return null;
	}

	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.gui.views.SpaltenSchema#getRootNodeName()
	 */
	public String getRootNodeName() {
		return TEXT_ROOT_NODE_NAME;
	}

}
