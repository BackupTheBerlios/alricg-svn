/*
 * Created 06.09.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors.composits;

import org.d3s.alricg.store.access.XmlAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.HerkunftVariante;
import org.eclipse.ui.IEditorInput;

/**
 * @author Vincent
 *
 */
public class HerkunftVarianteEditorInput extends CharElementEditorInput implements IEditorInput {
	HerkunftVariante variante;
	
	public HerkunftVarianteEditorInput(HerkunftVariante variante, Herkunft parent,  
			XmlAccessor xmlAccessor,  boolean isNew) {
		super(parent, xmlAccessor, isNew);
	}
	
	@Override
	public String getName() {
		return ((CharElement) variante).getId();
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == HerkunftVariante.class) {
			return variante;
		}
		return super.getAdapter(adapter);
	}
}
