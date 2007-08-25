package org.d3s.alricg.store.access;

import java.util.List;

import org.d3s.alricg.store.charElemente.CharElement;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gabe;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.MagieMerkmal;
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


public abstract interface CharElementAccessor {

	public static final Class[] ALL_STORED_CLASSES = new Class [] {
		Eigenschaft.class, Talent.class, Zauber.class, Repraesentation.class, 
		Gabe.class, Vorteil.class, Nachteil.class, Sonderfertigkeit.class,
		Rasse.class, Kultur.class, Profession.class, Gottheit.class, 
		Liturgie.class, RegionVolk.class, RitualKenntnis.class, Schrift.class, 
		Sprache.class, SchwarzeGabe.class, Gegenstand.class, DaemonenPakt.class, 
		MagierAkademie.class, MagieMerkmal.class};
	
	
	/**
	 * @return the talentList
	 */
	public abstract List<? extends CharElement> getMatchingList(Class clazz);
	
	/**
	 * @return the eigenschaftList
	 */
	public abstract List<Eigenschaft> getEigenschaftList();
	
	
	/**
	 * @return the talentList
	 */
	public abstract List<Talent> getTalentList();

	/**
	 * @return the zauberList
	 */
	public abstract List<Zauber> getZauberList();

	/**
	 * @return the repraesentationList
	 */
	public abstract List<Repraesentation> getRepraesentationList();

	/**
	 * @return the gabeList
	 */
	public abstract List<Gabe> getGabeList();

	/**
	 * @return the vorteilList
	 */
	public abstract List<Vorteil> getVorteilList();

	/**
	 * @return the nachteilList
	 */
	public abstract List<Nachteil> getNachteilList();

	/**
	 * @return the sonderfList
	 */
	public abstract List<Sonderfertigkeit> getSonderfList();

	/**
	 * @return the rasseList
	 */
	public abstract List<Rasse> getRasseList();

	/**
	 * @return the kulturList
	 */
	public abstract List<Kultur> getKulturList();

	/**
	 * @return the professionList
	 */
	public abstract List<Profession> getProfessionList();

	/**
	 * @return the gottheitList
	 */
	public abstract List<Gottheit> getGottheitList();

	/**
	 * @return the liturgieList
	 */
	public abstract List<Liturgie> getLiturgieList();

	/**
	 * @return the regionVolkList
	 */
	public abstract List<RegionVolk> getRegionVolkList();

	/**
	 * @return the ritualkenntnisList
	 */
	public abstract List<RitualKenntnis> getRitualkenntnisList();

	/**
	 * @return the schriftList
	 */
	public abstract List<Schrift> getSchriftList();

	/**
	 * @return the spracheList
	 */
	public abstract List<Sprache> getSpracheList();

	/**
	 * @return the gegenstandList
	 */
	public abstract List<Gegenstand> getGegenstandList();

	/**
	 * @return the daemonenPaktList
	 */
	public abstract List<DaemonenPakt> getDaemonenPaktList();

	/**
	 * @return the magierAkademieList
	 */
	public abstract List<MagierAkademie> getMagierAkademieList();
	
	/**
	 * @return the SchwarzeGabeList
	 */
	public abstract List<SchwarzeGabe> getSchwarzeGabeList();
	
	/**
	 * @return the SchwarzeGabeList
	 */
	public abstract List<MagieMerkmal> getMagieMerkmalList();
	

}