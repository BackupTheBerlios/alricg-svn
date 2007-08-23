/*
 * Created 26. Dezember 2004 / 23:45:31
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;

import org.d3s.alricg.store.charElemente.links.IdLink;

/**
 * <b>Beschreibung:</b><br>
 * Repräsentiert eine Sprache.
 * Wenn die "komplexitaet" oder "KostenKlasse" davon abhängt ob die Sprache Muttersprache
 * ist, so ist die Variable "wennNichtMuttersprache" belegt. Wenn die Sprache nicht 
 * Muttersprach ist, und die Varibale "wennNichtMuttersprache" belegt ist, dann
 * gilt diese Variable anstelle dieser Sprache.
 *  
 * @author V.Strelow
 */
public class Sprache extends SchriftSprache {
    private Sprache wennNichtMuttersprache;
    private Schrift[] zugehoerigeSchrift;
	
    /**
     * @see org.d3s.alricg.store.charElemente.SchriftSprache
	 * @return the wennNichtMuttersprache
	 */
	public Sprache getWennNichtMuttersprache() {
		return wennNichtMuttersprache;
	}
	/**
	 * @param wennNichtMuttersprache the wennNichtMuttersprache to set
	 */
	public void setWennNichtMuttersprache(Sprache wennNichtMuttersprache) {
		this.wennNichtMuttersprache = wennNichtMuttersprache;
	}
	/**
	 * @return the zugehoerigeSchrift
	 */
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Schrift[] getZugehoerigeSchrift() {
		return zugehoerigeSchrift;
	}
	/**
	 * @param zugehoerigeSchrift the zugehoerigeSchrift to set
	 */
	public void setZugehoerigeSchrift(Schrift[] zugehoerigeSchrift) {
		this.zugehoerigeSchrift = zugehoerigeSchrift;
	}

}
