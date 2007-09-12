/*
 * Created 14.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.utils;

import org.d3s.alricg.editor.Activator;
import org.d3s.alricg.editor.editors.GegenstandEditor;
import org.d3s.alricg.editor.editors.GottheitEditor;
import org.d3s.alricg.editor.editors.KulturEditor;
import org.d3s.alricg.editor.editors.LiturgieEditor;
import org.d3s.alricg.editor.editors.NachteilEditor;
import org.d3s.alricg.editor.editors.ProfessionEditor;
import org.d3s.alricg.editor.editors.RasseEditor;
import org.d3s.alricg.editor.editors.RasseVarianteEditor;
import org.d3s.alricg.editor.editors.RegionVolkEditor;
import org.d3s.alricg.editor.editors.RepraesentationEditor;
import org.d3s.alricg.editor.editors.SchamanenRitualEditor;
import org.d3s.alricg.editor.editors.SchriftEditor;
import org.d3s.alricg.editor.editors.SonderfertigkeitEditor;
import org.d3s.alricg.editor.editors.SpracheEditor;
import org.d3s.alricg.editor.editors.TalentEditor;
import org.d3s.alricg.editor.editors.VorteilEditor;
import org.d3s.alricg.editor.editors.ZauberEditor;
import org.d3s.alricg.editor.views.charElemente.EigenschaftView;
import org.d3s.alricg.editor.views.charElemente.GegenstandView;
import org.d3s.alricg.editor.views.charElemente.GoetterView;
import org.d3s.alricg.editor.views.charElemente.KulturView;
import org.d3s.alricg.editor.views.charElemente.LiturgieView;
import org.d3s.alricg.editor.views.charElemente.MerkmalView;
import org.d3s.alricg.editor.views.charElemente.NachteilView;
import org.d3s.alricg.editor.views.charElemente.ProfessionView;
import org.d3s.alricg.editor.views.charElemente.RasseView;
import org.d3s.alricg.editor.views.charElemente.RegionVolkView;
import org.d3s.alricg.editor.views.charElemente.RepraesentationView;
import org.d3s.alricg.editor.views.charElemente.SchamanenRitualView;
import org.d3s.alricg.editor.views.charElemente.SonderfertigkeitView;
import org.d3s.alricg.editor.views.charElemente.SpracheView;
import org.d3s.alricg.editor.views.charElemente.TalentView;
import org.d3s.alricg.editor.views.charElemente.VorteilView;
import org.d3s.alricg.editor.views.charElemente.ZauberView;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Gottheit;
import org.d3s.alricg.store.charElemente.Kultur;
import org.d3s.alricg.store.charElemente.KulturVariante;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.MagieMerkmal;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Profession;
import org.d3s.alricg.store.charElemente.ProfessionVariante;
import org.d3s.alricg.store.charElemente.Rasse;
import org.d3s.alricg.store.charElemente.RasseVariante;
import org.d3s.alricg.store.charElemente.RegionVolk;
import org.d3s.alricg.store.charElemente.Repraesentation;
import org.d3s.alricg.store.charElemente.SchamanenRitual;
import org.d3s.alricg.store.charElemente.Schrift;
import org.d3s.alricg.store.charElemente.SchriftSprache;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Sprache;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.charElemente.charZusatz.DaemonenPakt;
import org.d3s.alricg.store.charElemente.charZusatz.Gegenstand;
import org.d3s.alricg.store.charElemente.charZusatz.MagierAkademie;
import org.d3s.alricg.store.charElemente.charZusatz.SchwarzeGabe;
import org.eclipse.ui.IViewPart;

/**
 * @author Vincent
 *
 */
public class ViewEditorIdManager {
	
	public static IViewPart getView(Class clazz) {
		return Activator.getDefault().getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView(getViewID(clazz));
	}
	
	public static String getEditorID(Class clazz) {
		
		if (clazz == Eigenschaft.class) {
			return "KeinEditor";
		} else if (clazz == MagieMerkmal.class) {
			return "KeinEditor";
		} else if (clazz == Talent.class) {
			return TalentEditor.ID;
		} else if (clazz == Zauber.class) {
			return ZauberEditor.ID;
		} else if (clazz ==  Repraesentation.class) {
			return RepraesentationEditor.ID;
		} else if (clazz ==  Vorteil.class) {
			return VorteilEditor.ID;
		} else if (clazz ==  Nachteil.class) {
			return NachteilEditor.ID;
		} else if (clazz ==  Sonderfertigkeit.class) {
			return SonderfertigkeitEditor.ID;
		} else if (clazz ==  Rasse.class) { //|| clazz == RasseVariante.class) {
			return RasseEditor.ID;
		} else if (clazz ==  Kultur.class) { //|| clazz == KulturVariante.class) {
			return KulturEditor.ID;
		} else if (clazz ==  Profession.class) { //|| clazz == ProfessionVariante.class) {
			return ProfessionEditor.ID;
		} else if (clazz ==  RasseVariante.class) {
			return RasseVarianteEditor.ID;
		} else if (clazz ==  KulturVariante.class) {
			
		} else if (clazz ==  ProfessionVariante.class) {
			
		} else if (clazz ==  Gottheit.class) {
			return GottheitEditor.ID;
		} else if (clazz ==  Liturgie.class) {
			return LiturgieEditor.ID;
		} else if (clazz ==  Sprache.class) {
			return SpracheEditor.ID;
		} else if (clazz == Schrift.class) {
			return SchriftEditor.ID;
		} else if (clazz == SchamanenRitual.class) {
			return SchamanenRitualEditor.ID;
		} else if (clazz ==  Gegenstand.class) {
			return GegenstandEditor.ID;
		} else if (clazz ==  RegionVolk.class) {
			return RegionVolkEditor.ID;
			
		} else if (clazz ==  DaemonenPakt.class) {
			//return "Dämonen Pakt";
		} else if (clazz ==  MagierAkademie.class) {
			//return "Magier Akademie";
		} else if (clazz ==  SchwarzeGabe.class) {
			//return "Schwarze Gabe";

		}
		
		throw new IllegalArgumentException("Keine Behandlung für ein Element des Typs " +
					clazz.toString() + " vorhanden.");

	}
	
	public static String getViewID(Class clazz) {
		if (clazz == Eigenschaft.class) {
			return EigenschaftView.ID;
		} else if (clazz == MagieMerkmal.class) {
			return MerkmalView.ID;
		} else if (clazz ==  Talent.class) {
			return TalentView.ID;
		} else if (clazz ==  Zauber.class) {
			return ZauberView.ID;
		} else if (clazz ==  Repraesentation.class) {
			return RepraesentationView.ID;
		} else if (clazz ==  Vorteil.class) {
			return VorteilView.ID;
		} else if (clazz ==  Nachteil.class) {
			return NachteilView.ID;
		} else if (clazz ==  Sonderfertigkeit.class) {
			return SonderfertigkeitView.ID;
		} else if (clazz ==  Gottheit.class) {
			return GoetterView.ID;
		} else if (clazz ==  Liturgie.class) {
			return LiturgieView.ID;
		} else if (clazz ==  RegionVolk.class) {
			return RegionVolkView.ID;
		} else if (clazz == SchriftSprache.class 
				|| clazz == Schrift.class || clazz ==  Sprache.class) {
			return SpracheView.ID;
		} else if (clazz ==  Gegenstand.class) {
			return GegenstandView.ID;
		} else if (clazz ==  SchamanenRitual.class) {
			return SchamanenRitualView.ID;
		} else if (clazz ==  Rasse.class) { //|| clazz == RasseVariante.class) {
			return RasseView.ID;
		} else if (clazz ==  Kultur.class) { //|| clazz == KulturVariante.class) {
			return KulturView.ID;
		} else if (clazz ==  Profession.class) { //|| clazz == ProfessionVariante.class) {
			return ProfessionView.ID;
			
		} else if (clazz ==  SchwarzeGabe.class) {
			//return "Schwarze Gabe";
		} else if (clazz ==  DaemonenPakt.class) {
			//return "Dämonen Pakt";
		} else if (clazz ==  MagierAkademie.class) {
			//return "Magier Akademie";
		} 
		
		throw new IllegalArgumentException("Keine Behandlung für ein Element des Typs " +
					clazz.toString() + " vorhanden.");
	}
}
