/*
 * Created on 15.09.2005 / 17:35:10
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.views;

import java.util.Comparator;
import java.util.List;

import org.d3s.alricg.prozessor.Prozessor;
import org.d3s.alricg.prozessor.elementBox.ElementBox;

/**
 * <u>Beschreibung:</u><br> 
 * Dient den SortableTreeTable und SortableTables als Vorlage f�r die Darstellung.
 * 
 * In diesen Schemas werden spezielle Methoden f�r die Anzeige von Elementen zusammengefasst, 
 * und zwar nur solche Methoden die von dem Typ der Elementen (Link, CharElement) in der Tabelle abh�ngen.
 * Andere, nicht von Typ abh�ngige Methoden sind im "SpaltenSchema" zu finden.  Da jedes CharElement 
 * andere Funktionalit�ten ben�tigt, wird f�r jedesCharElemnt ein TypSchema ben�tigt.
 * 
 * @author V. Strelow
 */
public interface TypSchema {
	
	/**
	 * Um Tablen nach verschiedenen Spalten sortieren zu k�nnen, mu� f�r 
	 * jede Spalte ein Comparator verf�gbar sein. Dieser Comparator
	 * wird hiermit geliefert
	 * @param column Die Spalte, �ber die sortiert werden soll
	 * @return Ein Comparator, mit dem Elemente nach der Spalte sortiert werden
	 * 		k�nnen
	 */
	public Comparator getComparator(Object column);
	
	/**
	 * Dies ist die eigentliche Methode, mit der Werte abgerufen werden! 
	 * @param object Das Objekt welches eigentlich dargestellt wird (Talent,
	 * 		Rasse, Zauber, Link, usw.)
	 * @param column Die "Spalte" welche aus diesem Objekt gefragt ist (Talent.sorte,
	 * 		Rasse.gp, Zauber.merkmale, usw.)
	 * @return Ein Objekt mit dem Entsprechendem Wert
	 */
	public Object getCellValue(Object object, Object column);
	

	/**
	 * Setzt einen Wert neu an einer bestimmten Position der Tabelle.
	 * @param newValue Der ver�nderte Wert an der stelle "column"
	 * @param object Das Objekt welches eigentlich dargestellt wird (Talent,
	 * 		Rasse, Zauber, Link, usw.)
	 * @param column Die "Spalte" welche ge�ndert wurde (Talent.sorte,
	 * 		Rasse.gp, Zauber.merkmale, usw.)
	 */
	public void setCellValue(Object newValue, Object object, Object column);
	
	/**
	 * Pr�ft ob eine Tabellen Zelle editiert werden kann.
	 * @param object Das Objekt welches eigentlich dargestellt wird (Talent,
	 * 		Rasse, Zauber, Link, usw.)
	 * @param column Die "Spalte" welche aus diesem Objekt gefragt ist (Talent.sorte,
	 * 		Rasse.gp, Zauber.merkmale, usw.)
	 */
	public boolean isCellEditable(Object object, Object column);
	
	/**
	 * Liefert den ToolTipText f�r ein Element in der Tabelle
	 * @param object Das Object, �ber dem der Mauszeiger steht
	 * @param column Die Spalte, �ber dem der Mauszeiger steht
	 * @return Liefert den ToolTip Text f�r das Objekt und die Spalte
	 */
	public String getToolTip(Object object, Object column);
	
	/**
	 * Liefert zu einer Ordnung (die aus der Methode "getOrdnungElem()" stammt)
	 * die Enums(=Ordner), nach dem die Elemente geordnet werden k�nnen.
	 * Nach diesen Enums wird eine TreeTable also geordnet, wenn die "ordnung"
	 * gew�hlt ist.
	 * 
	 * BEISPIEL: 
	 * - Eine Ordnung bei Talenten ist "Sorte"; Die Enums sind die einzelen Sorten 
	 * 		"Kampf", "Natur", "Gesellschaft", ...
	 * - Eine Ordnung bei Zauber ist "Merkmal"; Die Enums sind die einzelnen Merkmale 
	 * 		"Illusion", "Metamagie", "Verst�ndigung", "Heilung", "Form"....
	 * 
	 * @param ordnung Die gew�nscht Ordnung der Elemente ( entspring getOrdnungElem() )
	 * @return Ein Array von Objekten nach denen der Tree geordnent wird, oder null
	 * 		wenn nach NICHT geordnet wird
	 */
	public Enum[] getOrdnerForOrdnung(Enum ordnung);
	
	/**
	 * Liefert zu den �bergebenen "element" in Verbindung mit einer "ordnung"
	 * alles Enums zu dieser "ordnung" zur�ck, in die das "element" eingeordnet
	 * ist. Wenn nicht geordnet werden soll, so gibt diese Methode c
	 * 
	 * BEISPIEL: 
	 * - Element Talent "Schwerter" mit Ordnung "sorte" ergibt die Enums "Kampf"
	 * - Der Zauber "Balsam" mit Ordnung "Merkmak" ergibt die Enums "Heilung" und "Form"
	 * 
	 * @param element Das Element, zu dem die Enums, in die dieses Element eingeordnet 
	 * 		ist, zur�ckgeliefert werden soll
	 * @param ordnung Die "Ordnung" in die das Element eingeordnt wird. Jede Ordnung besitzt
	 * 		andere Enums
	 * @return Die Enums in die das "element" innerhalb der "ordnung" eingeordent wird.
	 */
	public Enum[] getEnumsFromElement(Object element, Enum ordnung);
	
	/**
	 * Nimmt eine Liste von Elementen und sortiert alle nicht zum Filter passenden 
	 * Elemente aus. Die �brigen sollen dann angezeigt werden.
	 * @param aList Liste von Elementen aus der Tabelle
	 * @return Liste von Elementen die gem�� des aktuellen Filters "bereinigt" wurde
	 */
	public List doFilterElements(Enum filter, List aList);
	
	/**
	 * Liefert zur�ck um welche Art von Elementen die mit dem Schema verarbeitet werden
	 * es sich handelt.
	 * 
	 * @return true - Dieses Schema behandelt als Elemente Links, andernfalls sind
	 * 	die Elemente CharElemente
	 */
	public boolean hasLinksAsElements();
	
	/**
	 * Liefert den aktuellen Prozessor zur�ck, der von dem Schema zur Verarbeitung 
	 * der Werte eingesetzt wird.
	 * 
	 * @return Aktueller Prozessor
	 */
	public Prozessor getProzessor();
	
	/**
	 * Setzt den Prozessor neu, mit dem das Schema die Daten verarbeitet.
	 * @param prozessor neuer Prozessor
	 */
	public void setProzessor(Prozessor prozessor);

	
	public ElementBox getElementBox();

	
	public void setElementBox(ElementBox box);
}
