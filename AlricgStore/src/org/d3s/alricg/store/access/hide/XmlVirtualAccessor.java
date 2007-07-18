/*
 * Created 17.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access.hide;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.d3s.alricg.store.access.XmlAccessor;

/**
 * Dieser Accessor wird nur für den Unmarshaller benötigt, um alle XML-File
 * XmlAccessors zu einem einzigen JaxB kompatiblem Objekt zusammenzufassen.
 * 
 * @author Vincent
 */
@XmlRootElement
public class XmlVirtualAccessor {
	@XmlTransient
	public static final String XML_TAG = "xmlVirtualAccessor";
	@XmlTransient
	public static final String XML_FILEPATH_TAG = "filePath";
	
	private List<XmlAccessor> xmlAccessor;

	/**
	 * @return the xmlAccessor
	 */
	public List<XmlAccessor> getXmlAccessor() {
		return xmlAccessor;
	}

	/**
	 * @param xmlAccessor the xmlAccessor to set
	 */
	public void setXmlAccessor(List<XmlAccessor> xmlAccessor) {
		this.xmlAccessor = xmlAccessor;
	}
	
}
