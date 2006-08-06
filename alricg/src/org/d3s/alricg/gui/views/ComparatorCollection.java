/*
 * Created on 24.09.2005 / 01:05:03
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.views;

import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;

import org.d3s.alricg.charKomponenten.Faehigkeit;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.prozessor.common.HeldenLink;

/**
 * Eine Sammlung von Comaratoren für den Vergelich von Tabellen-Elementen. Die Hier
 * aufgeführten Comparatoren werden für die Tabellen von mehreren CharElementen benutzt.
 * (Comparatoren die nur für ein CharElement benutzt werden, werde bei dem entsprechenden 
 * TypSchema angegeben)
 * @author Vincent
 */
public interface ComparatorCollection {

	/**
	 * Dient dazu Links mit Comparatoren zu vergleichen, die nicht für Links
	 * sondern die CharElemente gemacht sind.
	 * @author Vincent
	 */
	class ComparatorWrapper implements Comparator<Link> {
		private Comparator comparator;
		
		/**
		 * Konstruktor
		 * @param comp Der Comparator, mit dem die Ziele der Links verglichen werden soll
		 */
		public ComparatorWrapper(Comparator comp) {
			this.comparator = comp;
		}

		public int compare(Link o1, Link o2) {
			return comparator.compare(o1.getZiel(), o2.getZiel());
		}
	}
	
	/**
	 * <u>Beschreibung:</u><br> 
	 * Dient ledeglich als Wapper damit die Objekte in einem DefaultMutableTreeNode 
	 * miteinander verglichen werden können.
	 * @author V. Strelow
	 */
	class NodeComparator implements Comparator<DefaultMutableTreeNode> {
		private Comparator realComp;
		
		public void setRealComparator(Comparator comp) {
			realComp = comp;
		}

		/* (non-Javadoc) Methode überschrieben
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(DefaultMutableTreeNode node0, DefaultMutableTreeNode node1) {
			return realComp.compare(node0.getUserObject(), node1.getUserObject());
		}
		
	}
	
	/**
	 * Comparator um Namen von CharElementen in einer TreeTable miteinander 
	 * vergleichen zu können. Es können sowohl Strings als auch Unterklassen von 
	 * "CharElement" verglichen werden.
	 * Vorteil: Es können auch Strings (wie von Ordnern in TreeTable) und CharElemente
	 * miteinander verglichen werden.
	 * @author V. Strelow
	 */
	public static Comparator compNamensComparator = 
		new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
		};
	
	/**
	 * Um zwei Faehigkeiten zu vergleichen. Vergleicht die KostenKlasse
	 */
	public static Comparator compKostenKlasse = 	
		new Comparator<Faehigkeit>() {
			public int compare(Faehigkeit arg0, Faehigkeit arg1) {
				return arg0.getKostenKlasse().toString().compareTo(arg1.getKostenKlasse().toString());
			}
		};
		
	/**
	 * Um zwei Links zu verleichen. Vergleicht das Attribut "Wert"
	 */
	public static Comparator compLinkWert =
		new Comparator<Link>() {
			public int compare(Link arg0, Link arg1) {
				return arg0.getWert() - arg1.getWert();
			}
		};
	
	/**
	 * Um zwei GeneratorLinks zu verleichen. Vergleicht das Attribut "Kosten"
	 */
	public static Comparator compLinkKosten =
		new Comparator<HeldenLink>() {
			public int compare(HeldenLink arg0, HeldenLink arg1) {
				return arg0.getKosten() - arg1.getKosten();
			}
		};
	
	/**
	 * Um zwei Links zu verleichen. Vergleicht das Attribut "Text"
	 */
	public static Comparator compLinkText =
		new Comparator<Link>() {
			public int compare(Link arg0, Link arg1) {
				return arg0.getText().compareTo( arg0.getText() );
			}
		};
		
		
}
