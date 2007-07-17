/*
 * Created 17.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Vincent
 */
@XmlRootElement
public class XmlVirtualAccessor {
	@XmlTransient
	public static final String XML_TAG = "xmlVirtualAccessor";
	@XmlTransient
	public static final String XML_FILEPATH_TAG = "filePath";
	
	private XmlAccessor[] xmlAccessor;

	/**
	 * @return the xmlAccessor
	 */
	public XmlAccessor[] getXmlAccessor() {
		return xmlAccessor;
	}

	/**
	 * @param xmlAccessor the xmlAccessor to set
	 */
	public void setXmlAccessor(XmlAccessor[] xmlAccessor) {
		this.xmlAccessor = xmlAccessor;
	}
	
}
