/*
 * Created 25.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

import org.d3s.alricg.store.charElemente.Werte.MagieMerkmalEnum;

/**
 * @author Vincent
 */
public class MagieMerkmal extends CharElement {
	private MagieMerkmalEnum merkmalEnum;

	/**
	 * @return the merkmalEnum
	 */
	public MagieMerkmalEnum getMerkmalEnum() {
		return merkmalEnum;
	}

	/**
	 * @param merkmalEnum the merkmalEnum to set
	 */
	public void setMerkmalEnum(MagieMerkmalEnum merkmalEnum) {
		this.merkmalEnum = merkmalEnum;
		merkmalEnum.setMagieMerkmal(this);
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.CharElement#getId()
	 */
	@Override
	public String getId() {
		return merkmalEnum.getId();
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.charElemente.CharElement#getName()
	 */
	@Override
	public String getName() {
		return merkmalEnum.getValue();
	}
	
	
	
}
