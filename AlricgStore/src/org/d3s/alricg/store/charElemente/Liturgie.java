/*
 * Created 14.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.charElemente;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 * 
 * @author Vincent
 */
public class Liturgie extends CharElement {
	
	@XmlEnum
	public enum LiturgieArt {
		allgemein("allgemein"), // Z.B. Angrosch
		speziell("speziell"),
		hochschamanen("allgemein, nur Hochschamanen");
		private String value; // ID des Elements

		private LiturgieArt(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	private Gottheit[] gottheit;
	private int[] grad;
	private LiturgieArt art;
	
	/**
	 * Gibt an, zu welchen Gottheiten diese Liturgie ursprünglch gehört.
	 * @return the gottheit
	 */
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	public Gottheit[] getGottheit() {
		return gottheit;
	}

	/**
	 * @param gottheit the gottheit to set
	 */
	public void setGottheit(Gottheit[] gottheit) {
		this.gottheit = gottheit;
	}

	/**
	 * Die Stufen die erreicht werden kann
	 * @return Die möglichen Grade
	 */
	@XmlList
	@XmlAttribute
	@XmlSchemaType(name = "nonNegativeInteger")
	public int[] getGrad() {
		return grad;
	}

	/**
	 * @param Die möglichen Grade die erreicht werden können
	 */
	public void setGrad(int[] grad) {
		this.grad = grad;
	}

	/**
	 * @return the art
	 */
	public LiturgieArt getArt() {
		return art;
	}

	/**
	 * @param art the art to set
	 */
	public void setArt(LiturgieArt art) {
		this.art = art;
	}
	
}
