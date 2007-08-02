/*
 * Created 09.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access;

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.d3s.alricg.store.access.hide.AbstractCharElementsAccessor;
import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gabe;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.RitualKenntnis;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.charZusatz.DaemonenPakt;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.charZusatz.MagierAkademie;
import org.d3s.alricg.store.charElemente.charZusatz.SchwarzeGabe;

/**
 * @author Vincent
 */
@XmlRootElement
public class XmlAccessor extends AbstractCharElementsAccessor implements CharElementAccessor {
	private File filePath;
	private String version;
	
	/**
	 * @return the filePath
	 */
	@XmlAttribute
	public String getFilePath() {
		if (filePath == null) return null;
		return filePath.getAbsolutePath();
	}
	
	@XmlTransient
	public File getFile() {
		return filePath;
	}
	
	@Override
	public List<? extends CharElement> getMatchingList(Class clazz) {
		return super.getMatchingList(clazz);
	}
	
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = new File(filePath);
	}
	
	/**
	 * @return the version
	 */
	@XmlAttribute
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	private List<Eigenschaft> eigenschaftList;
	private List<Talent> talentList;
	private List<Zauber> zauberList;
	private List<Repraesentation> repraesentationList;
	private List<Gabe> gabeList;
	private List<Vorteil> vorteilList;
	private List<Nachteil> nachteilList;
	private List<Sonderfertigkeit> sonderfList;
	private List<Rasse> rasseList;
	private List<Kultur> kulturList;
	private List<Profession> professionList;
	private List<Gottheit> gottheitList;
	private List<Liturgie> liturgieList;
	private List<RegionVolk> regionVolkList;
	private List<RitualKenntnis> ritualkenntnisList;
	private List<Schrift> schriftList;
	private List<Sprache> spracheList;
	private List<Gegenstand> gegenstandList;
	private List<DaemonenPakt> daemonenPaktList;
	private List<MagierAkademie> magierAkademieList;
	private List<SchwarzeGabe> schwarzeGabeList;
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getEigenschaftList()
	 */
	public List<Eigenschaft> getEigenschaftList() {
		return eigenschaftList;
	}
	/**
	 * @param eigenschaftList the eigenschaftList to set
	 */
	public void setEigenschaftList(List<Eigenschaft> eigenschaftList) {
		this.eigenschaftList = eigenschaftList;
	}

	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getTalentList()
	 */
	public List<Talent> getTalentList() {
		return talentList;
	}
	/**
	 * @param talentList the talentList to set
	 */
	public void setTalentList(List<Talent> talentList) {
		this.talentList = talentList;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getZauberList()
	 */
	public List<Zauber> getZauberList() {
		return zauberList;
	}
	/**
	 * @param zauberList the zauberList to set
	 */
	public void setZauberList(List<Zauber> zauberList) {
		this.zauberList = zauberList;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getRepraesentationList()
	 */
	public List<Repraesentation> getRepraesentationList() {
		return repraesentationList;
	}
	/**
	 * @param repraesentationList the repraesentationList to set
	 */
	public void setRepraesentationList(List<Repraesentation> repraesentationList) {
		this.repraesentationList = repraesentationList;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getGabeList()
	 */
	public List<Gabe> getGabeList() {
		return gabeList;
	}
	/**
	 * @param gabeList the gabeList to set
	 */
	public void setGabeList(List<Gabe> gabeList) {
		this.gabeList = gabeList;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getVorteilList()
	 */
	public List<Vorteil> getVorteilList() {
		return vorteilList;
	}
	/**
	 * @param vorteilList the vorteilList to set
	 */
	public void setVorteilList(List<Vorteil> vorteilList) {
		this.vorteilList = vorteilList;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getNachteilList()
	 */
	public List<Nachteil> getNachteilList() {
		return nachteilList;
	}
	/**
	 * @param nachteilList the nachteilList to set
	 */
	public void setNachteilList(List<Nachteil> nachteilList) {
		this.nachteilList = nachteilList;
	}
	
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getSonderfList()
	 */
	public List<Sonderfertigkeit> getSonderfList() {
		return sonderfList;
	}
	/**
	 * @param sonderfList the sonderfList to set
	 */
	public void setSonderfList(List<Sonderfertigkeit> sonderfList) {
		this.sonderfList = sonderfList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getRasseList()
	 */
	public List<Rasse> getRasseList() {
		return rasseList;
	}
	/**
	 * @param rasseList the rasseList to set
	 */
	public void setRasseList(List<Rasse> rasseList) {
		this.rasseList = rasseList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getKulturList()
	 */
	public List<Kultur> getKulturList() {
		return kulturList;
	}
	/**
	 * @param kulturList the kulturList to set
	 */
	public void setKulturList(List<Kultur> kulturList) {
		this.kulturList = kulturList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getProfessionList()
	 */
	public List<Profession> getProfessionList() {
		return professionList;
	}
	/**
	 * @param professionList the professionList to set
	 */
	public void setProfessionList(List<Profession> professionList) {
		this.professionList = professionList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getGottheitList()
	 */
	public List<Gottheit> getGottheitList() {
		return gottheitList;
	}
	/**
	 * @param gottheitList the gottheitList to set
	 */
	public void setGottheitList(List<Gottheit> gottheitList) {
		this.gottheitList = gottheitList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getLiturgieList()
	 */
	public List<Liturgie> getLiturgieList() {
		return liturgieList;
	}
	/**
	 * @param liturgieList the liturgieList to set
	 */
	public void setLiturgieList(List<Liturgie> liturgieList) {
		this.liturgieList = liturgieList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getRegionVolkList()
	 */
	public List<RegionVolk> getRegionVolkList() {
		return regionVolkList;
	}
	/**
	 * @param regionVolkList the regionVolkList to set
	 */
	public void setRegionVolkList(List<RegionVolk> regionVolkList) {
		this.regionVolkList = regionVolkList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getRitualkenntnisList()
	 */
	public List<RitualKenntnis> getRitualkenntnisList() {
		return ritualkenntnisList;
	}
	/**
	 * @param ritualkenntnisList the ritualkenntnisList to set
	 */
	public void setRitualkenntnisList(List<RitualKenntnis> ritualkenntnisList) {
		this.ritualkenntnisList = ritualkenntnisList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getSchriftList()
	 */
	public List<Schrift> getSchriftList() {
		return schriftList;
	}
	/**
	 * @param schriftList the schriftList to set
	 */
	public void setSchriftList(List<Schrift> schriftList) {
		this.schriftList = schriftList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getSpracheList()
	 */
	public List<Sprache> getSpracheList() {
		return spracheList;
	}
	/**
	 * @param spracheList the spracheList to set
	 */
	public void setSpracheList(List<Sprache> spracheList) {
		this.spracheList = spracheList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getGegenstandList()
	 */
	public List<Gegenstand> getGegenstandList() {
		return gegenstandList;
	}
	/**
	 * @param gegenstandList the gegenstandList to set
	 */
	public void setGegenstandList(List<Gegenstand> gegenstandList) {
		this.gegenstandList = gegenstandList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getDaemonenPaktList()
	 */
	public List<DaemonenPakt> getDaemonenPaktList() {
		return daemonenPaktList;
	}
	/**
	 * @param daemonenPaktList the daemonenPaktList to set
	 */
	public void setDaemonenPaktList(List<DaemonenPakt> daemonenPaktList) {
		this.daemonenPaktList = daemonenPaktList;
	}
	/* (non-Javadoc)
	 * @see org.d3s.alricg.store.access.CharElementAccessor#getMagierAkademieList()
	 */
	public List<MagierAkademie> getMagierAkademieList() {
		return magierAkademieList;
	}
	/**
	 * @param magierAkademieList the magierAkademieList to set
	 */
	public void setMagierAkademieList(List<MagierAkademie> magierAkademieList) {
		this.magierAkademieList = magierAkademieList;
	}

	/**
	 * @return the schwarzeGabeList
	 */
	public List<SchwarzeGabe> getSchwarzeGabeList() {
		return schwarzeGabeList;
	}
	/**
	 * @param schwarzeGabeList the schwarzeGabeList to set
	 */
	public void setSchwarzeGabeList(List<SchwarzeGabe> schwarzeGabeList) {
		this.schwarzeGabeList = schwarzeGabeList;
	}
	
	
}
