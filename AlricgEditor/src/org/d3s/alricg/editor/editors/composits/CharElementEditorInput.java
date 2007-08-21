/*
 * Created 26.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.editor.views.charElemente.RefreshableViewPart;
import org.d3s.alricg.store.access.IdFactory;
import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * Ermöglicht das öffnen von CharElementen in Editoren
 * @author Vincent
 */
public class CharElementEditorInput implements IEditorInput {
	private final CharElement charElement;
	private final XmlAccessor xmlAccessor;
	private final String id;
	private final boolean isNew;
	
	public CharElementEditorInput(CharElement charElement, XmlAccessor xmlAccessor, boolean isNew) {
		this.charElement = charElement;
		this.xmlAccessor = xmlAccessor;
		this.id = charElement.getId();
		this.isNew = isNew;
	}
	
	@Override
	public boolean exists() {
		if (charElement != null) {
			return true;
		} 
		return IdFactory.getInstance().isIdExist(id);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public String getToolTipText() {
		return id;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (CharElement.class.isAssignableFrom(adapter)) {
			return this.charElement;
		} else if (XmlAccessor.class.isAssignableFrom(adapter)) {
			return this.xmlAccessor;
		} else if (Boolean.class.isAssignableFrom(adapter)) {
			return this.isNew;
		}

		return null;
	}
	
	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	// Überschrieben um den die Entscheidung zu ermöglichen ob ein neuer
	// Editor für ein Object gestartet werden soll oder nicht.
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} if (obj instanceof CharElementEditorInput 
				&& ((CharElementEditorInput) obj).getName() == id ) {
			return true;
		}
		return false;
	}
}
