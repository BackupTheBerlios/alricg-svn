/*
 * Created 27.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.d3s.alricg.editor.common.ViewUtils.TreeObject;
import org.d3s.alricg.editor.common.ViewUtils.TreeViewContentProvider;
import org.d3s.alricg.editor.editors.EditorMessages;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.links.Link;
import org.d3s.alricg.store.charElemente.links.Option;
import org.d3s.alricg.store.charElemente.links.OptionAnzahl;
import org.d3s.alricg.store.charElemente.links.OptionListe;
import org.d3s.alricg.store.charElemente.links.OptionVerteilung;
import org.d3s.alricg.store.charElemente.links.OptionVoraussetzung;
import org.d3s.alricg.store.charElemente.links.Voraussetzung;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Diese Klasse ermöglicht das Darstellen und bearbeiten von Voraussetzungen!
 * @author Vincent
 */
public class VoraussetzungPart extends AbstractAuswahlPart {
    private TreeObject invisibleRoot;
    
	private final static String POSITIVE_VORAUS = EditorMessages.VoraussetzungPart_PositiveVoraussetzungen;
	private final static String NEGATIVE_VORAUS = EditorMessages.VoraussetzungPart_NegativeVoraussetzungen;
	
	/**
	 * Erstellt die TreeTable
	 * @param top Parent Composite
	 * @param partSite 
	 */
	public VoraussetzungPart(Composite top, IWorkbenchPartSite partSite) {
		super(top, partSite);
	}
	


	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#dispose()
	 */
	@Override
	public void dispose() {
		treeViewer.getTree().dispose();

	}

	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(CharElement charElem) {
		final Voraussetzung currentVor = buildVoraussetzung();
		
		if (charElem.getVoraussetzung() == null && currentVor == null) {
			return false;
		} else if (charElem.getVoraussetzung() != null && currentVor != null) {
			// Nope
		} else {
			return true;
		}
		
		boolean isEqual = true;
		isEqual &= compareOptionList(
				charElem.getVoraussetzung().getNegVoraussetzung(), 
				currentVor.getNegVoraussetzung());

		isEqual &= compareOptionList(
				charElem.getVoraussetzung().getPosVoraussetzung(), 
				currentVor.getPosVoraussetzung());
		
		return !isEqual;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(CharElement charElem) {
		// "Grund-Tree" aufbauen
		invisibleRoot = new TreeObject("invisibleRoot", null); //$NON-NLS-1$

		final TreeObject positivTree = new TreeObject(POSITIVE_VORAUS, invisibleRoot);
		invisibleRoot.addChildren(positivTree);
		final TreeObject negativTree = new TreeObject(NEGATIVE_VORAUS, invisibleRoot);
		invisibleRoot.addChildren(negativTree);

		treeViewer.setContentProvider(new TreeViewContentProvider(invisibleRoot));
		treeViewer.setInput(invisibleRoot);
		
		// Lade Daten
		if (charElem.getVoraussetzung() == null) return;
		
		// Lade Voraussetzungen
		if (charElem.getVoraussetzung().getPosVoraussetzung() != null) {
			loadAuswahlList(charElem.getVoraussetzung().getPosVoraussetzung(), positivTree);
		}
		
		// Lade Nachteile
		if (charElem.getVoraussetzung().getNegVoraussetzung() != null) {
			loadAuswahlList(charElem.getVoraussetzung().getNegVoraussetzung(), negativTree);
		}
		
		auswahlDrop.setQuelle(charElem); // Setzt die Quelle für alle neuen Links in der Auswahl
		auswahlDrop.setAcceptGlobalDropClazz(CharElement.class); // Alle CharElemente sind möglich
		auswahlDrop.addAcceptColumnClazz(
				CharElement.class,
				auswahlDrop.getColumnsToListen()[0]); // Alle CharElemente sind möglich
		treeViewer.refresh();
	}
	
	/**
	 * Erstellt aus dem aktuellen Tree eine Voraussetzung 
	 * @return Die Voraussetzung, die dem aktuellen Tree entspricht.
	 */
	private Voraussetzung buildVoraussetzung() {
		final Voraussetzung voraus = new Voraussetzung();
		
		// Die beiden Kinder "positiv" und "negativ"
		for (int i1 = 0; i1 < invisibleRoot.getChildren().length; i1++) {
			final List<OptionVoraussetzung> optList = new ArrayList<OptionVoraussetzung>();
			
			// Entweder die Kinder von "positiv" oder "negativ"
			if (invisibleRoot.getChildren()[i1].getChildren() == null) continue;
			for (int i2 = 0; i2 < invisibleRoot.getChildren()[i1].getChildren().length; i2++) {
				final Option tmpOp = buildAuswahl(invisibleRoot.getChildren()[i1].getChildren()[i2]);
				if (tmpOp != null) {
					optList.add( (OptionVoraussetzung) tmpOp );
				}
			}
			
			if (optList.size() == 0) continue;
			
			if (invisibleRoot.getChildren()[i1].getValue().equals(POSITIVE_VORAUS)) {
				voraus.setPosVoraussetzung(optList);
			} else {
				voraus.setNegVoraussetzung(optList);
			}
		}
		
		if (voraus.getPosVoraussetzung() != null || voraus.getNegVoraussetzung() != null) {
			return voraus;
		} else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstarctElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, CharElement charElem) {
		charElem.setVoraussetzung(buildVoraussetzung());
	}
	
	@Override
	protected void makeActions() {
		super.makeActions();

		// Neues alternativ Element Action 
		buildNewAlternative.setToolTipText(EditorMessages.VoraussetzungPart_AlternativeVoraussetzung_TT);
		
		// Neues Element Action 
		buildNewAuswahl.setText(EditorMessages.VoraussetzungPart_NewVoraussetzung);
		buildNewAuswahl.setToolTipText(EditorMessages.VoraussetzungPart_NewVoraussetzung_TT);	
		
		// Texte für Editieren setzen
		editSelected.setText(EditorMessages.VoraussetzungPart_EditVoraussetzung);
		editSelected.setToolTipText(EditorMessages.VoraussetzungPart_EditVoraussetzung_TT);
	}
	
	/**
	 * Definiert den Typ der Option, der standartmäßig vorgeschlagen wird
	 * @return Classe der Default Option
	 */
	@Override
	protected Class getOptionClass() {
		return OptionVoraussetzung.class;
	}

}
