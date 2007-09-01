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
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.links.Auswahl;
import org.d3s.alricg.store.charElemente.links.Option;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Auswahl der Talente für eine Herkunft
 * @author Vincent
 */
public class AuswahlPart extends AbstractAuswahlPart<Herkunft> {
	private TreeObject invisibleRoot;
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
		final Auswahl currentAuswahl = buildAuswahl();
		
		if (regulator.getAuswahl(herkunft) == null && currentAuswahl == null) {
			return false;
		} else if (regulator.getAuswahl(herkunft) != null && currentAuswahl != null) {
			// Nope
		} else {
			return true;
		}
		
		return !compareOptionList(
				regulator.getAuswahl(herkunft).getOptionen(), 
				currentAuswahl.getOptionen());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(Herkunft herkunft) {
		// "Grund-Tree" aufbauen
		invisibleRoot = new TreeObject("invisibleRoot", null);

		final TreeObject node = new TreeObject(regulator.getRootNoteName(), invisibleRoot);
		invisibleRoot.addChildren(node);
		
		treeViewer.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		treeViewer.setInput(invisibleRoot);
		
		// Setzt die Quelle für alle neuen Links in der Auswahl
		auswahlDrop.setQuelle(herkunft); 
		// Setzt die CharElemente, die in der Auswahl als LinkZiel stehen dürfen
		auswahlDrop.setAcceptGlobalDropClazz(regulator.getCharElementClazz());
		treeViewer.refresh();
		
		// Lade Daten
		if (regulator.getAuswahl(herkunft) == null 
				|| regulator.getAuswahl(herkunft).getOptionen() == null) {
			return;
		}
		
		loadAuswahlList(regulator.getAuswahl(herkunft).getOptionen(), node);

	}

	/**
	 * Erstellt aus dem aktuellen Tree eine Voraussetzung 
	 * @return Die Voraussetzung, die dem aktuellen Tree entspricht.
	 */
	private Auswahl buildAuswahl() {
		TreeObject root = invisibleRoot.getChildren()[0];
	
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
		regulator.setAuswahl(herkunft, buildAuswahl());

	}
	
	/**
	 * Über dieses Interface werden alle Daten angegeben, die nötig sind um
	 * verschiedene Auswahlen einer Herkunft (für Talente, Zauber usw.) per 
	 * "AuswahlPart" zu Editieren
	 * @author Vincent
	 */
	public static interface HerkunftAuswahlRegulator {
		/**
		 * @return Name des Root-Knotens der zu sehen ist. Z.B. "Talente" oder "Zauber"
		 */
		public String getRootNoteName();
		
		/**
		 * @param herkunft Die Herkunft, die editiert wird
		 * @return Die Auswahl, die Editiert wird (z.B. "herkunft.getTalente()"
		 */
		public Auswahl getAuswahl(Herkunft herkunft);
		
		/**
		 * Trägt die "auswahl" an die richtige stelle der "herkunft" ein. 
		 * Z.B. "herkunft.setTalente(auswahl)"
		 * @param herkunft Die Herkunft, die editiert wird
		 * @param auswahl Die Auswahl, die in der herkunft eingetragen werden soll
		 */
		public void setAuswahl(Herkunft herkunft, Auswahl auswahl);
		
		/**
		 * @return Die Klasse des CharElements, welches mit in der Auswahl 
		 * verwaltete wird.
		 * Z.B. "Talent.getClass()" für Talente oder "CharElement.getClass()" 
		 * für alle CharElemente. 
		 */
		public Class getCharElementClazz();
		
	}

}
