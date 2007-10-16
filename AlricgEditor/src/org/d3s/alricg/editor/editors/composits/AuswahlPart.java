/*
 * Created 17.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.ArrayList;
import java.util.List;

import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.utils.EditorViewUtils.AuswahlTreeObject;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.Option;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Auswahl der CharElemente für eine Herkunft
 * @author Vincent
 */
public class AuswahlPart extends AbstractAuswahlPart<Herkunft> {
	protected TreeObject invisibleRoot;
	private final HerkunftAuswahlRegulator regulator;
	
	public AuswahlPart(Composite top, 
			IWorkbenchPartSite partSite, 
			HerkunftAuswahlRegulator regulator) 
	{
		super(top, partSite);
		this.regulator = regulator;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(Herkunft herkunft) {
		
		// Alle verschiedenen Auswahlen vergleichen 
		for (int i = 0; i < invisibleRoot.getChildren().length; i++) {
			final Auswahl buildedAuswahl = 
				buildAuswahl((AuswahlTreeObject) invisibleRoot.getChildren()[i]);
			final Auswahl gettetAuswahl = regulator.getAuswahl(
						herkunft, 
						((AuswahlTreeObject) invisibleRoot.getChildren()[i]).getIndex());
			
			if ( buildedAuswahl == null && gettetAuswahl == null) {
				// nope
			} else if (buildedAuswahl == null || gettetAuswahl == null) {
				return true;
			} else {
				if ( !compareOptionList(
						buildedAuswahl.getOptionen(), 
						gettetAuswahl.getOptionen() )) {
					return true;
				}
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(Herkunft herkunft) {
		// "Grund-Tree" aufbauen
		invisibleRoot = new TreeObject("invisibleRoot", null);
		
		// Setzt die Quelle für alle neuen Links in der Auswahl
		auswahlDrop.setQuelle(herkunft); 
		// Setzt die CharElemente, die in der Auswahl als LinkZiel stehen dürfen
		auswahlDrop.setAcceptGlobalDropClazz(regulator.getCharElementClazz());

		// Erzeugt die einzelnen Nodes für Talente, Voraussetzungen usw.
		for (int i = 0; i < regulator.getCharElementClazz().length; i++) {
			AuswahlTreeObject treeObj = new AuswahlTreeObject(
					regulator.getCharElementClazz()[i], 
					invisibleRoot,
					regulator.getCharElementText()[i],
					i);
			invisibleRoot.addChildren(treeObj);
			
			if (regulator.getAuswahl(herkunft, i) == null
					|| regulator.getAuswahl(herkunft, i).getOptionen() == null ) {
				continue;
			}
			loadAuswahlList(
					regulator.getAuswahl(herkunft, i).getOptionen(),
					treeObj);
		}
		
		treeViewer.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		treeViewer.setInput(invisibleRoot);
		treeViewer.refresh();
	}

	/**
	 * Erstellt aus dem aktuellen Tree eine Voraussetzung 
	 * @return Die Voraussetzung, die dem aktuellen Tree entspricht.
	 */
	private Auswahl buildAuswahl(AuswahlTreeObject root) {
		//TreeObject root = invisibleRoot.getChildren()[0];
	
		if (root.getChildren() == null) return null;
		
		// Alle TreeObjects durchgehen und hinzufügen
		final List<Option> optList = new ArrayList<Option>();
		for (int i2 = 0; i2 < root.getChildren().length; i2++) {
			final Option tmpOp = buildAuswahl(root.getChildren()[i2]);
			if (tmpOp != null) {
				optList.add( (Option) tmpOp );
			}
		}
		
		if (optList.size() == 0) return null;
		
		final Auswahl auswahl = new Auswahl();
		auswahl.setOptionen(optList);
	
		return auswahl;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, Herkunft herkunft) {
		for (int i = 0; i < invisibleRoot.getChildren().length; i++) {
			regulator.setAuswahl(
					herkunft, 
					buildAuswahl((AuswahlTreeObject) invisibleRoot.getChildren()[i]),
					((AuswahlTreeObject) invisibleRoot.getChildren()[i]).getIndex());
		}
	}
	
	/**
	 * Über dieses Interface werden alle Daten angegeben, die nötig sind um
	 * verschiedene Auswahlen einer Herkunft (für Talente, Zauber usw.) per 
	 * "AuswahlPart" zu Editieren
	 * @author Vincent
	 */
	public static interface HerkunftAuswahlRegulator {
		
		/**
		 * Liefert die Auswahl von "herkunft" mit dem index "index"
		 * @param herkunft Die Herkunft, die editiert wird
		 * @param Der Index der zu liefernden Auswahl. Der Index ist identisch
		 * 		mit dem Index des Elements von getCharElementClazz & getCharElementText
		 * @return Die Auswahl mit dem index "index"
		 */
		public abstract Auswahl getAuswahl(Herkunft herkunft, int index);
		
		/**
		 * Trägt die "auswahl" an die richtige stelle der "herkunft" ein. 
		 * Z.B. "herkunft.setTalente(auswahl)"
		 * @param herkunft Die Herkunft, die editiert wird
		 * @param auswahl Die Auswahl, die in der herkunft eingetragen werden soll
		 */
		/**
		 * Trägt die Auswahl die "herkunft" an der Stelle "index" ein. 
		 * @param herkunft Die Herkunft, die editiert wird
		 * @param auswahl Die Auswahl welche eingetragen werden soll
		 * @param index Der Index der Auswahl, gibt an wo in der Herkunft die Auswahl
		 * 		eingetragen werden soll. Der Index ist identisch mit dem Index des
		 * 		Elements von getCharElementClazz & getCharElementText
		 */
		public abstract void setAuswahl(Herkunft herkunft, Auswahl auswahl, int index);
		
		/**
		 * Gibt an welche Klassen angezeigt werden sollen. Für jede Klasse wird
		 * im Tree ein "ordner" angelegt. Es können in diesen Ordner nur
		 * Elemente vom entsprechenden Typ hinzugefügt werden.
		 * Der Text der Ordner wird mit "getCharElementText()" angegeben. 
		 * Zugehörigkeiten werden über den Index identifiziert.
		 * @return Die Klasse für welche Ordner erstellt werden sollen.
		 */
		public abstract Class[] getCharElementClazz();
		
		/**
		 * Gibt den Tex an, der auf den Ordner von "getCharElementClazz()" 
		 * stehen soll. Zugehörigkeiten werden über den Index identifiziert.
		 * @return Der Anteigetext für die Knoten
		 */
		public abstract String[] getCharElementText();
		
	}

}
