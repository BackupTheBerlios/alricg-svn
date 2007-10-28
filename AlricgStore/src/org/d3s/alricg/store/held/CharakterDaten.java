/*
 * Created 23. Januar 2005 / 15:30:43
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.store.held;

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Talent;

/**
 * <b>Beschreibung:</b><br>
 * Fast die Daten die einen Helden ausmachen zusammen.
 * 
 * @author V.Strelow
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CharakterDaten {
	@XmlAttribute
	private File filePath;
	
	@XmlElement(name="AllgemeineDaten")
	private AllgemeineDaten allgDaten;
	
	@XmlElementWrapper(name="Talente")
	@XmlElement(name="tal")
	private List<? extends HeldenLink<Talent>> talente;
	
	@XmlElementWrapper(name="Eigenschaften")
	@XmlElement(name="eig")
	private List<? extends HeldenLink<Eigenschaft>> eigenschaften;


	public File getFile() {
		return filePath;
	}
	
	// ---------------------------
	
	/**
	 * @return the filePath
	 */
	
	public String getFilePath() {
		if (filePath == null) return null;
		return filePath.getAbsolutePath();
	}
	
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = new File(filePath);
	}
	
	/**
	 * @return the charDaten
	 */
	public AllgemeineDaten getAllgDaten() {
		return allgDaten;
	}

	/**
	 * @param charDaten the charDaten to set
	 */
	public void setAllgDaten(AllgemeineDaten allgDaten) {
		this.allgDaten = allgDaten;
	}

	/**
	 * @return the talente
	 */
	public List<? extends HeldenLink<Talent>> getTalente() {
		return talente;
	}

	/**
	 * @param talente the talente to set
	 */
	public void setTalente(List<? extends HeldenLink<Talent>> talente) {
		this.talente = talente;
	}

	/**
	 * @return the eigenschaften
	 */
	public List<? extends HeldenLink<Eigenschaft>> getEigenschaften() {
		return eigenschaften;
	}

	/**
	 * @param eigenschaften the eigenschaften to set
	 */
	public void setEigenschaften(List<? extends HeldenLink<Eigenschaft>> eigenschaften) {
		this.eigenschaften = eigenschaften;
	}
	
}