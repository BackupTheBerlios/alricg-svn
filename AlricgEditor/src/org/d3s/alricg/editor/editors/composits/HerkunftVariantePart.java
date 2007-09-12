/*
 * Created 04.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import java.util.Arrays;

import org.d3s.alricg.editor.common.widgets.ComboTextList;
import org.d3s.alricg.editor.utils.EditorViewUtils.EditorTreeOrTableObject;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * @author Vincent
 */
public abstract class HerkunftVariantePart extends AbstractElementPart<HerkunftVariante> {

	private final ComboTextList cobTextList;
	private final Button cbxIsErsetztOriginal;
	private final Button cbxIsMultible;
	private final Label lblParent;
	private Herkunft parent;
	private XmlAccessor parentXmlAccessor;
	
	public HerkunftVariantePart(Composite top, String[] comboItems) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
	// Sollen Tag "überschrieben" werden?
		GridData tmpGData = new GridData(GridData.GRAB_HORIZONTAL);
		tmpGData.widthHint = 600;
		tmpGData.horizontalSpan = 2; // nimm 2 Spalten Platz ein
		
		final Group groupEntferne = new Group(top, SWT.SHADOW_IN);
		groupEntferne.setText("Elemente aus Original übernehmen");
		groupEntferne.setLayout(gridLayout);
		groupEntferne.setLayoutData(tmpGData);
		
		final Label lblFiller = new Label(groupEntferne, SWT.NONE);
		cbxIsErsetztOriginal = new Button(groupEntferne, SWT.CHECK);
		cbxIsErsetztOriginal.setText("Varianten erstezt Original komplett");
		cbxIsErsetztOriginal.setToolTipText("Es werden keine Elemente aus dem Original übernommen," +
									" sondern nur solche aus der Variante."); 
		cbxIsErsetztOriginal.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Noop
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				cobTextList.setEnabled(!cbxIsErsetztOriginal.getSelection());
			}
		});
		
		final Label lblErsetze = new Label(groupEntferne, SWT.NONE);
		lblErsetze.setText("Ersetze Tags:");
		lblErsetze.setToolTipText("Diese Elemente werden nicht vom Original übernommen," +
									" sondern durch die Variante komplett ersetzt.");
		tmpGData = new GridData(); 
		tmpGData.verticalAlignment = GridData.BEGINNING;
		tmpGData.verticalIndent = 10;
		lblErsetze.setLayoutData(tmpGData);
		
		cobTextList = new ComboTextList(groupEntferne, SWT.NONE, comboItems);
		
		final Label lblFiller2 = new Label(top, SWT.NONE);
		cbxIsMultible = new Button(top, SWT.CHECK);
		cbxIsMultible.setText("Ist Zusatzvariante");
		cbxIsMultible.setToolTipText("Zusatzvarianten können zusätzlich zu anderen Zusatzvarianten " +
									"gewählt werden. Die Varianten addieren sich dann."); 
		
		lblParent = new Label(top, SWT.BORDER);
		lblParent.setText("Per Drag and Drop wählen");
		lblParent.setSize(120, lblParent.getSize().y);
		tmpGData = new GridData(); 
		tmpGData.widthHint = 125;
		lblParent.setLayoutData(tmpGData);
		
	    final DropTarget target = new DropTarget(
	    		lblParent,
	    		DND.DROP_COPY | DND.DROP_MOVE);
	    target.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer() });
	    target.addDropListener(new DropTargetAdapter() {
	      public void drop(DropTargetEvent event) {
	        if (event.data == null) {
	          return;
	        }
	        final EditorTreeOrTableObject treeObj = (EditorTreeOrTableObject) ((IStructuredSelection) event.data).getFirstElement();
	        final CharElement charElem = ((CharElement) treeObj.getValue());
	        
	        if ( !canDropCharElement(charElem) ) return;
	        
	        lblParent.setText(charElem.getName());
	        parent = (Herkunft) treeObj.getValue();
	        parentXmlAccessor = treeObj.getAccessor();
	      }
	    });

	}
	
	public Herkunft getParten() {
		return this.parent;
	}
	
	public XmlAccessor getParentXmlAccessor () {
		return this.parentXmlAccessor;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#isDirty(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public boolean isDirty(HerkunftVariante charElem) {
		boolean isNotDirty = true;
		
		if (charElem.getEntferneXmlTag() == null) {
			isNotDirty &= cobTextList.getValueList().length == 0;
		} else {
			isNotDirty &= Arrays.equals(charElem.getEntferneXmlTag(), cobTextList.getValueList());
		}
		isNotDirty &= charElem.isAdditionsVariante() == !cbxIsErsetztOriginal.getSelection();
		isNotDirty &= charElem.isMultibel() == cbxIsMultible.getSelection();
		isNotDirty &= lblParent.getData().equals(charElem.getVarianteVon());
		
		return !isNotDirty;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#loadData(org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void loadData(HerkunftVariante charElem) {
		cobTextList.setValues(charElem.getEntferneXmlTag());
		cbxIsErsetztOriginal.setSelection(!charElem.isAdditionsVariante());
		cobTextList.setEnabled(charElem.isAdditionsVariante());
		cbxIsMultible.setSelection(charElem.isMultibel());
		
        lblParent.setText(charElem.getVarianteVon().getName());
        lblParent.setData(charElem.getVarianteVon());
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.editor.editors.composits.AbstractElementPart#saveData(org.eclipse.core.runtime.IProgressMonitor, org.d3s.alricg.store.charElemente.CharElement)
	 */
	@Override
	public void saveData(IProgressMonitor monitor, HerkunftVariante charElem) {
		
		if (cobTextList.getEnabled() == false || cobTextList.getValueList().length == 0) {
			charElem.setEntferneXmlTag(null);
		} else {
			charElem.setEntferneXmlTag(cobTextList.getValueList());
		}
		charElem.setAdditionsVariante(!cbxIsErsetztOriginal.getSelection());
		charElem.setMultibel(cbxIsMultible.getSelection());
		
		charElem.setVarianteVon((Herkunft) lblParent.getData());
	}
	
	public abstract boolean canDropCharElement(CharElement charElem);

}
