/**
 * 
 */
package org.d3s.alricg.prozessor.editor;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.prozessor.BaseLinkProzessor;

/**
 * @author Vincent
 *
 */
public interface BaseProzessorEditorLink<ZIEL extends CharElement, LINK extends Link> 
																					extends BaseLinkProzessor<ZIEL, LINK> 
{

	public abstract void setTarget(CharElement ce);
	
	public abstract CharElement getTarget();

}
