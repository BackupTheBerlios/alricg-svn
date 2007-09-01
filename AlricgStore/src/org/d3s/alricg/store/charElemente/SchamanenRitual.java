/*
 * Created 30.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 * @author Vincent
 *
 */
public class SchamanenRitual extends CharElement {
	private Repraesentation[] herkunft;
	private int grad;
	/**
	 * @return the herkunft
	 */
	@XmlList
	@XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Repraesentation[] getHerkunft() {
		return herkunft;
	}
	/**
	 * @param herkunft the herkunft to set
	 */
	public void setHerkunft(Repraesentation[] herkunft) {
		this.herkunft = herkunft;
	}
	/**
	 * @return the grad
	 */
	public int getGrad() {
		return grad;
	}
	/**
	 * @param grad the grad to set
	 */
	public void setGrad(int grad) {
		this.grad = grad;
	}
	
	/**
	 * @return Liefert einen kompletten String mit allen Verbreitungen 
	 * 				als Abkürzungen.
	 */
	public String getHerkunftText(boolean alsAbk) {
		StringBuffer buffer = new StringBuffer();
		
		if (alsAbk) {			
			for (int i = 0; i < herkunft.length; i++) {
				buffer.append(herkunft[i].getAbkuerzung());
				if (i+1 < herkunft.length) buffer.append(", ");
			}
		} else {
			for (int i = 0; i < herkunft.length; i++) {
				buffer.append(herkunft[i].getName());
				if (i+1 < herkunft.length) buffer.append(", ");
			}
		}
		return buffer.toString();
	}	
	
	
}
