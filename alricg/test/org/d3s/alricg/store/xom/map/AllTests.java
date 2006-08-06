/*
 * Created on 27.09.2005 / 00:52:11
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.store.xom.map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestSuite für alle org.d3s.alricg.store.xom.map Tests.
 * 
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class AllTests extends TestCase {
    
    public static Test suite() {
        TestSuite testSuite = new TestSuite("All Tests for org.d3s.alricg.store.xom.map");
        //testSuite.addTestSuite(DataToXOMMapper_Test.class);
        //testSuite.addTestSuite(XOMToDataMapper_Test.class);
        //testSuite.addTestSuite(XOMToConfigMapper_Test.class);
        //testSuite.addTestSuite(XOMToLibraryMapper_Test.class);
        testSuite.addTestSuite(XM_Ausruestung_Test.class);
        //testSuite.addTestSuite(XM_Auswahl_Test.class);
        //testSuite.addTestSuite(XM_AuswahlAusruestung_Test.class);
        testSuite.addTestSuite(XM_CharElement_Test.class);
        //testSuite.addTestSuite(XM_DaemonenPakt_Test.class);
        testSuite.addTestSuite(XM_Faehigkeit_Test.class);
        testSuite.addTestSuite(XM_Fahrzeug_Test.class);
        testSuite.addTestSuite(XM_Fertigkeit_Test.class);
        testSuite.addTestSuite(XM_FkWaffe_Test.class);
        testSuite.addTestSuite(XM_Gabe_Test.class);
        testSuite.addTestSuite(XM_Gegenstand_Test.class);
        testSuite.addTestSuite(XM_Gottheit_Test.class);
        //testSuite.addTestSuite(XM_Herkunft_Test.class);
        //testSuite.addTestSuite(XM_HerkunftVariante_Test.class);
        testSuite.addTestSuite(XM_IdLink_Test.class);
        testSuite.addTestSuite(XM_IdLinkList_Test.class);
        //testSuite.addTestSuite(XM_IdLinkVoraussetzung_Test.class);
        //testSuite.addTestSuite(XM_Kultur_Test.class);
        //testSuite.addTestSuite(XM_Liturgue_Test.class); --> unnötig; wie Ritus.
        testSuite.addTestSuite(XM_LiturgieRitualKenntnis_Test.class);
        testSuite.addTestSuite(XM_Nachteil_Test.class);
        testSuite.addTestSuite(XM_NahkWaffe_Test.class);
        //testSuite.addTestSuite(XM_Profession_Test.class);
        //testSuite.addTestSuite(XM_Rasse_Test.class);
        testSuite.addTestSuite(XM_RegionVolk_Test.class);        
        testSuite.addTestSuite(XM_Repraesentation_Test.class);
        //testSuite.addTestSuite(XM_Ritual_Test.class); --> unnötig; wie Ritus.
        testSuite.addTestSuite(XM_Ritus_Test.class);
        testSuite.addTestSuite(XM_Ruestung_Test.class);
        testSuite.addTestSuite(XM_Schild_Test.class);
        //testSuite.addTestSuite(XM_Schrift_Test.class); --> unnötig; wie SchriftSprache
        testSuite.addTestSuite(XM_SchriftSprache_Test.class);
        testSuite.addTestSuite(XM_SchwarzeGabe_Test.class);
        testSuite.addTestSuite(XM_SimpelGegenstand_Test.class);
        testSuite.addTestSuite(XM_SonderFertigkeit_Test.class);
        testSuite.addTestSuite(XM_Sprache_Test.class);
        testSuite.addTestSuite(XM_Talent_Test.class);
        //testSuite.addTestSuite(XM_Tier_Test.class); --> unnötig; wie CharElement
        //testSuite.addTestSuite(XM_Voraussetzung_Test.class);
        testSuite.addTestSuite(XM_VorNachteil_Test.class);
        //testSuite.addTestSuite(XM_Vorteil_Test.class); --> unnötig; wie VorNachteil
        testSuite.addTestSuite(XM_Waffe_Test.class);
        testSuite.addTestSuite(XM_Zauber_Test.class);
        //testSuite.addTestSuite(XM_ZusatzProfession_Test.class);
        testSuite.addTestSuite(XOMMappingHelper_Test.class);
        return testSuite;
    }
}
