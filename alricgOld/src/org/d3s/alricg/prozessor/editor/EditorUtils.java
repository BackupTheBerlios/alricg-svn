/**
 * 
 */
package org.d3s.alricg.prozessor.editor;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.charKomponenten.links.Link;

/**
 * @author Vincent
 *
 */
public class EditorUtils {
	
	public static void addLink(IdLinkList linkList, Link link) {
		IdLink[] listArray;
		
		if ( linkList.getLinks() == null) {
			listArray = new IdLink[0];
		} else {
			listArray = linkList.getLinks();
		}
		
		IdLink[] linkArray = new IdLink[listArray.length + 1];
		for (int i = 0; i < listArray.length; i++) {
			linkArray[i] = listArray[i];
		}
		linkArray[linkArray.length - 1] = (IdLink) link;
		
		linkList.setLinks(linkArray);
	}
	
	public static void removeLink(IdLinkList linkList, Link link) {
		
		IdLink[] linkArray = new IdLink[linkList.getLinks().length - 1];
		for (int i = 0; i < linkList.getLinks().length; i++) {
			if (!link.isEqualLink(linkList.getLinks()[i])) {
				linkArray[i] = (IdLink) link;
			}
		}
		
		linkList.setLinks(linkArray);
	}
	
	public static boolean containsCharElement(IdLinkList linkList, CharElement element) {
		
		for (int i = 0; i < linkList.getLinks().length; i++) {
			if (element.equals(linkList.getLinks()[i].getZiel())) {
				return true;
			}
		}
		return false;
	}
	
	/*
	public static boolean containsCharElement(IdLinkList linkList, IdLink link) {
		
		for (int i = 0; i < linkList.getLinks().length; i++) {
			if (link.isEqualLink(linkList.getLinks()[i])) {
				return true;
			}
		}
		return false;
	}
	*/
}
