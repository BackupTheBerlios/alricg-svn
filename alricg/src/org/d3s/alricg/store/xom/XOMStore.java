/*
 * Created on 20.06.2005 / 13:14:15
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */

package org.d3s.alricg.store.xom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import nu.xom.Document;
import nu.xom.Element;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.Gabe;
import org.d3s.alricg.charKomponenten.Gottheit;
import org.d3s.alricg.charKomponenten.Kultur;
import org.d3s.alricg.charKomponenten.KulturVariante;
import org.d3s.alricg.charKomponenten.Liturgie;
import org.d3s.alricg.charKomponenten.LiturgieRitualKenntnis;
import org.d3s.alricg.charKomponenten.Nachteil;
import org.d3s.alricg.charKomponenten.Profession;
import org.d3s.alricg.charKomponenten.ProfessionVariante;
import org.d3s.alricg.charKomponenten.Rasse;
import org.d3s.alricg.charKomponenten.RasseVariante;
import org.d3s.alricg.charKomponenten.RegionVolk;
import org.d3s.alricg.charKomponenten.Repraesentation;
import org.d3s.alricg.charKomponenten.Ritual;
import org.d3s.alricg.charKomponenten.Schrift;
import org.d3s.alricg.charKomponenten.Sonderfertigkeit;
import org.d3s.alricg.charKomponenten.Sprache;
import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.Vorteil;
import org.d3s.alricg.charKomponenten.Zauber;
import org.d3s.alricg.charKomponenten.ZusatzProfession;
import org.d3s.alricg.charKomponenten.charZusatz.Ausruestung;
import org.d3s.alricg.charKomponenten.charZusatz.DaemonenPakt;
import org.d3s.alricg.charKomponenten.charZusatz.Fahrzeug;
import org.d3s.alricg.charKomponenten.charZusatz.FkWaffe;
import org.d3s.alricg.charKomponenten.charZusatz.NahkWaffe;
import org.d3s.alricg.charKomponenten.charZusatz.Ruestung;
import org.d3s.alricg.charKomponenten.charZusatz.Schild;
import org.d3s.alricg.charKomponenten.charZusatz.SchwarzeGabe;
import org.d3s.alricg.charKomponenten.charZusatz.Tier;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.controller.ProgAdmin;
import org.d3s.alricg.prozessor.utils.FormelSammlung.KostenKlasse;
import org.d3s.alricg.store.Configuration;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;
import org.d3s.alricg.store.KeyExistsException;
import org.d3s.alricg.store.xom.map.DataToXOMMapper;
import org.d3s.alricg.store.xom.map.XOMToDataMapper;

/**
 * <code>DataStore</code> auf Basis des xom-Frameworks
 * 
 * @author <a href="mailto:msturzen@mac.com">St. Martin</a>
 */
public class XOMStore implements DataStore {

    /** <code>XOMStore</code>'s logger */
    private static final Logger LOG = Logger.getLogger(XOMStore.class.getName());

    /** Ausrüstung */
    private final Map<String, Ausruestung> ausruestungMap = new HashMap<String, Ausruestung>();

    /** Charakterkomponenten */
    private final Map<String, CharKomponente> charKompMap = new HashMap<String, CharKomponente>();

    /** Dämeonenpakte */
    private final Map<String, DaemonenPakt> daemonenPaktMap = new HashMap<String, DaemonenPakt>();

    /** Eigenschaften */
    private final Map<String, Eigenschaft> eigenschaftMap = new HashMap<String, Eigenschaft>();

    /** Fahrzeuge */
    private final Map<String, Fahrzeug> fahrzeugMap = new HashMap<String, Fahrzeug>();

    /** Gaben */
    private final Map<String, Gabe> gabeMap = new HashMap<String, Gabe>();

    /** Götter */
    private final Map<String, Gottheit> gottheitMap = new HashMap<String, Gottheit>();

    /** Kulturen */
    private final Map<String, Kultur> kulturMap = new HashMap<String, Kultur>();

    /** Kultur-Varianten */
    private final Map<String, KulturVariante> kulturVarianteMap = new HashMap<String, KulturVariante>();

    /** Liturgieb */
    private final Map<String, Liturgie> liturgieMap = new HashMap<String, Liturgie>();

    /** Nachteile */
    private final Map<String, Nachteil> nachteilMap = new HashMap<String, Nachteil>();

    /** Enthält den Ursprung jedes einzelnen Elements */
    private final Map<String, List<String>> origin = new HashMap<String, List<String>>();

    /** Professionen */
    private final Map<String, Profession> professionMap = new HashMap<String, Profession>();

    /** Professions-Varianten */
    private final Map<String, ProfessionVariante> professionVarianteMap = new HashMap<String, ProfessionVariante>();

    /** Rassen */
    private final Map<String, Rasse> rasseMap = new HashMap<String, Rasse>();

    /** Rasse-Varianten */
    private final Map<String, RasseVariante> rasseVarianteMap = new HashMap<String, RasseVariante>();

    /** Regionen */
    private final Map<String, RegionVolk> regionMap = new HashMap<String, RegionVolk>();

    /** Repräsentationen */
    private final Map<String, Repraesentation> repraesentMap = new HashMap<String, Repraesentation>();

    /** Ritual- und Liturgiekenntnisse */
    private final Map<String, LiturgieRitualKenntnis> ritLitKentMap = new HashMap<String, LiturgieRitualKenntnis>();

    /** Rituale */
    private final Map<String, Ritual> ritualMap = new HashMap<String, Ritual>();

    /** Rüstunge */
    private final Map<String, Ruestung> ruestungMap = new HashMap<String, Ruestung>();

    /** Schilde */
    private final Map<String, Schild> schildMap = new HashMap<String, Schild>();

    /** Schriften */
    private final Map<String, Schrift> schriftMap = new HashMap<String, Schrift>();

    /** Schwarze Gaben */
    private final Map<String, SchwarzeGabe> schwarzeGabeMap = new HashMap<String, SchwarzeGabe>();

    /** Sonderfertigkeiten */
    private final Map<String, Sonderfertigkeit> sonderfMap = new HashMap<String, Sonderfertigkeit>();

    /** Sprachen */
    private final Map<String, Sprache> spracheMap = new HashMap<String, Sprache>();

    /** Talente */
    private final Map<String, Talent> talentMap = new HashMap<String, Talent>();

    /** Tiere */
    private final Map<String, Tier> tierMap = new HashMap<String, Tier>();

    /** Vorteile */
    private final Map<String, Vorteil> vorteilMap = new HashMap<String, Vorteil>();

    /** Fernkampfwaffen */
    private final Map<String, FkWaffe> waffeFkMap = new HashMap<String, FkWaffe>();

    /** Nahkampfwaffen */
    private final Map<String, NahkWaffe> waffeNkMap = new HashMap<String, NahkWaffe>();

    /** Zauber */
    private final Map<String, Zauber> zauberMap = new HashMap<String, Zauber>();

    /** Zusätzliche Professionen */
    private final Map<String, ZusatzProfession> zusatzProfMap = new HashMap<String, ZusatzProfession>();
    
    /** Steigerungskostentabelle */
    private final Map<KostenKlasse, Integer[]> skt = new HashMap<KostenKlasse, Integer[]>();

    /**
     * Erzeugt einen neuen <code>XOMStore</code>.
     */
    XOMStore() {
    }

    /**
     * Fügt alle Elemente von <code>ids</code> mit dem Wert von <code>canonicalPath</code> zu <code>origin</code>
     * hinzu.
     * 
     * @param ids
     * @param canonicalPath
     */
    public void addOrigins(List<String> ids, String canonicalPath) {

        if (canonicalPath == null) {
            return;
        }

        List<String> value = origin.get(canonicalPath);
        if (value == null) {
            origin.put(canonicalPath, ids);
        } else {
            value.addAll(ids);
        }
    }

    // @see org.d3s.alricg.store.DataStore#getCharElement(java.lang.String)
    public CharElement getCharElement(String id) {
        return getCharElement(id, this.getCharKompFromId(id));
    }

    // @see org.d3s.alricg.store.DataStore#getCharElement(java.lang.String, org.d3s.alricg.controller.CharKomponente)
    public CharElement getCharElement(String id, CharKomponente charKomp) {
        final Map<String, ? extends CharElement> map = getMap(charKomp); // Die zugehörige HashMap holen
        if (map.get(id) == null) {
            LOG.warning("Id konnte nicht gefunden werden: " + id);
        }
        return map.get(id);
    }

    // @see org.d3s.alricg.store.DataStore#getCharKompFromId(java.lang.String)
    public CharKomponente getCharKompFromId(String id) {
        String prefix = "";

        try {
            prefix = id.split("-")[0]; // Spaltet den Prefix von der ID ab
        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.severe("prefix falsch aufgebaut! \n" + e.toString());
        }

        return getCharKompFromPrefix(prefix);
    }

    // @see org.d3s.alricg.store.DataStore#getCharKompFromPrefix(java.lang.String)
    public CharKomponente getCharKompFromPrefix(String prefix) {
        assert charKompMap.get(prefix) != null; // Gültigkeitsprüfung

        return charKompMap.get(prefix);
    }

    /**
     * Ermöglicht den lesenden Zugriff auf die Map mit den charKomponenten.
     * 
     * @param charKomp Die CharKomponente zu der die HashMap zurückgegeben werden soll
     * @return HashMap mit allen Elementen zu dieser CharKomponente
     */
    public Map<String, ? extends CharElement> getMap(CharKomponente charKomp) {

        switch (charKomp) {
        // >>>>>>>>>>>>>>> Herkunft
        case rasse:
            return Collections.unmodifiableMap(rasseMap);
        case kultur:
            return Collections.unmodifiableMap(kulturMap);
        case profession:
            return Collections.unmodifiableMap(professionMap);
        case rasseVariante:
            return Collections.unmodifiableMap(rasseVarianteMap);
        case kulturVariante:
            return Collections.unmodifiableMap(kulturVarianteMap);
        case professionVariante:
            return Collections.unmodifiableMap(professionVarianteMap);
        case zusatzProfession:
            return Collections.unmodifiableMap(zusatzProfMap);

        // >>>>>>>>>>>>>>> Fertigkeiten & Fähigkeiten
        case vorteil:
            return Collections.unmodifiableMap(vorteilMap);
        case gabe:
            return gabeMap;
        case nachteil:
            return Collections.unmodifiableMap(nachteilMap);
        case sonderfertigkeit:
            return Collections.unmodifiableMap(sonderfMap);
        case ritLitKenntnis:
            return Collections.unmodifiableMap(ritLitKentMap);
        case talent:
            return Collections.unmodifiableMap(talentMap);
        case zauber:
            return Collections.unmodifiableMap(zauberMap);

        // >>>>>>>>>>>>>>> Sprachen
        case sprache:
            return Collections.unmodifiableMap(spracheMap);
        case schrift:
            return Collections.unmodifiableMap(schriftMap);

        // >>>>>>>>>>>>>>> Götter
        case liturgie:
            return Collections.unmodifiableMap(liturgieMap);
        case ritual:
            return Collections.unmodifiableMap(ritualMap);

        // >>>>>>>>>>>>>>> Ausrüstung
        case ausruestung:
            return Collections.unmodifiableMap(ausruestungMap);
        case fahrzeug:
            return Collections.unmodifiableMap(fahrzeugMap);
        case waffeNk:
            return Collections.unmodifiableMap(waffeNkMap);
        case waffeFk:
            return Collections.unmodifiableMap(waffeFkMap);
        case ruestung:
            return Collections.unmodifiableMap(ruestungMap);
        case schild:
            return Collections.unmodifiableMap(schildMap);

        // >>>>>>>>>>>>>>> Zusätzliches
        case daemonenPakt:
            return Collections.unmodifiableMap(daemonenPaktMap);
        case schwarzeGabe:
            return Collections.unmodifiableMap(schwarzeGabeMap);
        case tier:
            return Collections.unmodifiableMap(tierMap);
        case region:
            return Collections.unmodifiableMap(regionMap);
        case gottheit:
            return Collections.unmodifiableMap(gottheitMap);
        case repraesentation:
            return Collections.unmodifiableMap(repraesentMap);
        case eigenschaft:
            return Collections.unmodifiableMap(eigenschaftMap);

        // >>>>>>>>>>>>>>> DEFAULT
        default:
            LOG.severe("Ein CharKomp wurde nicht gefunden: " + charKomp);
        }

        return null;
    }

    /**
     * @return origin
     */
    public Map<String, List<String>> getOrigin() {
        return origin;
    }

    // @see org.d3s.alricg.store.DataStore#getSkt()
    public Map<KostenKlasse, Integer[]> getSkt() throws ConfigurationException {
    	return Collections.unmodifiableMap(skt);
    }

    // @see org.d3s.alricg.store.DataStore#getUnmodifieableCollection(org.d3s.alricg.controller.CharKomponente)
    public Collection<CharElement> getUnmodifieableCollection(CharKomponente charKomp) {
        return Collections.unmodifiableCollection(getMap(charKomp).values());
    }

    /**
     * Initialisiert die zu <code>charKomp</code> gehörende <code>Map</code> mit "leeren" aber vom Typ korrekten
     * <code>CharElement</code>en; die <code>keys</code> sind die Einträge in <code>ids</code>.
     * <p>
     * Ein solches Vorgehen ist nötig, da <code>CharElement</code>e auf andere <code>CharElement</code>e verweisen
     * können.<br/>
     * 
     * <pre>
     *              Seien A, B CharElemente und es verweise A auf B (z.B. Modifikation: B + 3).
     *              Existiert B bei Anlage von A und der entsprechenden Modifikation noch nicht, 
     *              so sind Fehler im Programm nur eine Frage der Zeit. 
     *              Als prominentes Beispiel sei die NullPointerException genannt.
     * </pre>
     * 
     * </p>
     * 
     * @param ids Eine Liste mit ids, die zu einer Charakterkomponente hinzugefügt werden sollen.
     * @param charKomp Die Charakterkomponente zu der neue Elemente hinzugefügt werden sollen.
     */
    public void initCharKomponents(List<String> ids, CharKomponente charKomp) throws KeyExistsException {
        switch (charKomp) {
        // >>>>>>>>>>>>>>> Herkunft
        case rasse:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), rasseMap);
                rasseMap.put(ids.get(i), new Rasse(ids.get(i)));
            }
            break;
        case kultur:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), kulturMap);
                kulturMap.put(ids.get(i), new Kultur(ids.get(i)));
            }
            break;
        case profession:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), professionMap);
                professionMap.put(ids.get(i), new Profession(ids.get(i)));
            }
            break;
        case rasseVariante:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), rasseVarianteMap);
                rasseVarianteMap.put(ids.get(i), new RasseVariante(ids.get(i)));
            }
            break;
        case kulturVariante:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), kulturVarianteMap);
                kulturVarianteMap.put(ids.get(i), new KulturVariante(ids.get(i)));
            }
            break;
        case professionVariante:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), professionVarianteMap);
                professionVarianteMap.put(ids.get(i), new ProfessionVariante(ids.get(i)));
            }
            break;
        case zusatzProfession:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), zusatzProfMap);
                zusatzProfMap.put(ids.get(i), new ZusatzProfession(ids.get(i)));
            }
            break;

        // >>>>>>>>>>>>>>> Fertigkeiten & Fähigkeiten
        case vorteil:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), vorteilMap);
                vorteilMap.put(ids.get(i), new Vorteil(ids.get(i)));
            }
            break;
        case gabe:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), gabeMap);
                gabeMap.put(ids.get(i), new Gabe(ids.get(i)));
            }
            break;
        case nachteil:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), nachteilMap);
                nachteilMap.put(ids.get(i), new Nachteil(ids.get(i)));
            }
            break;
        case sonderfertigkeit:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), sonderfMap);
                sonderfMap.put(ids.get(i), new Sonderfertigkeit(ids.get(i)));
            }
            break;
        case ritLitKenntnis:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), ritLitKentMap);
                ritLitKentMap.put(ids.get(i), new LiturgieRitualKenntnis(ids.get(i)));
            }
            break;
        case talent:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), talentMap);
                talentMap.put(ids.get(i), new Talent(ids.get(i)));
            }
            break;
        case zauber:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), zauberMap);
                zauberMap.put(ids.get(i), new Zauber(ids.get(i)));
            }
            break;

        // >>>>>>>>>>>>>>> Sprachen
        case sprache:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), spracheMap);
                spracheMap.put(ids.get(i), new Sprache(ids.get(i)));
            }
            break;
        case schrift:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), schriftMap);
                schriftMap.put(ids.get(i), new Schrift(ids.get(i)));
            }
            break;

        // >>>>>>>>>>>>>>> Götter
        case liturgie:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), liturgieMap);
                liturgieMap.put(ids.get(i), new Liturgie(ids.get(i)));
            }
            break;
        case ritual:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), ritualMap);
                ritualMap.put(ids.get(i), new Ritual(ids.get(i)));
            }
            break;

        // >>>>>>>>>>>>>>> Ausrüstung
        case ausruestung:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), ausruestungMap);
                ausruestungMap.put(ids.get(i), new Ausruestung(ids.get(i)));
            }
            break;
        case fahrzeug:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), fahrzeugMap);
                fahrzeugMap.put(ids.get(i), new Fahrzeug(ids.get(i)));
            }
            break;
        case waffeNk:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), waffeNkMap);
                waffeNkMap.put(ids.get(i), new NahkWaffe(ids.get(i)));
            }
            break;
        case waffeFk:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), waffeFkMap);
                waffeFkMap.put(ids.get(i), new FkWaffe(ids.get(i)));
            }
            break;
        case ruestung:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), ruestungMap);
                ruestungMap.put(ids.get(i), new Ruestung(ids.get(i)));
            }
            break;
        case schild:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), schildMap);
                schildMap.put(ids.get(i), new Schild(ids.get(i)));
            }
            break;

        // >>>>>>>>>>>>>>> Zusätzliches
        case daemonenPakt:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), daemonenPaktMap);
                daemonenPaktMap.put(ids.get(i), new DaemonenPakt(ids.get(i)));
            }
            break;
        case schwarzeGabe:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), schwarzeGabeMap);
                schwarzeGabeMap.put(ids.get(i), new SchwarzeGabe(ids.get(i)));
            }
            break;
        case tier:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), tierMap);
                tierMap.put(ids.get(i), new Tier(ids.get(i)));
            }
            break;
        case region:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), regionMap);
                regionMap.put(ids.get(i), new RegionVolk(ids.get(i)));
            }
            break;
        case gottheit:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), gottheitMap);
                gottheitMap.put(ids.get(i), new Gottheit(ids.get(i)));
            }
            break;
        case repraesentation:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), repraesentMap);
                repraesentMap.put(ids.get(i), new Repraesentation(ids.get(i)));
            }
            break;
        case eigenschaft:
            for (int i = 0; i < ids.size(); i++) {
                keyDoppelt(ids.get(i), eigenschaftMap);
                eigenschaftMap.put(ids.get(i), new Eigenschaft(ids.get(i)));
            }
            break;
        case sonderregel:
            break; // Gibt es nicht!

        // >>>>>>>>>>>>>>> DEFAULT
        default:
            LOG.logp(Level.SEVERE, "CharKompAdmin", "initCharKomponents", "Ein CharKomp wurde nicht gefunden: "
                    + charKomp);
        }
    }
    

    // @see org.d3s.alricg.store.AbstractStoreFactory#storeData()
    public void storeData() throws ConfigurationException {
        try {

            // Menge von files, die geschrieben werden sollen, holen.
            final Set<String> fileNames = origin.keySet();
            for (Iterator<String> i = fileNames.iterator(); i.hasNext();) {
                final String fileName = i.next();

                // Die Liste mit ids holen, die in das File geschrieben werden sollen.
                final List<String> ids = origin.get(fileName);
                final Element xom = new DataToXOMMapper().transformData(ids, this);

                // xom ins file schreiben
                File output = new File(fileName);
                if (output.exists()) {
                    output.renameTo(new File(fileName + ".old")); // REVISIT [msturzen] Sollte delete sein. Nach
                    // erfolgreichem test!
                    output = new File(fileName);
                }
                final Writer writer = new BufferedWriter(new FileWriter(output));
                writer.write(new Document(xom).toXML());
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    void init(Configuration config) throws ConfigurationException {
    	
    	// Initialiserung der HashMap für schnellen Zugriff auf Komponenten über deren ID
    	for (int i = 0; i < CharKomponente.values().length; i++) {
    		charKompMap.put(CharKomponente.values()[i].getPrefix(), CharKomponente.values()[i]);
    	}
    	
    	final XOMToDataMapper mappy = new XOMToDataMapper();
    	mappy.readData(config, this);
    	skt.putAll(mappy.skt(config));
    }

    /**
     * Prüft ob ein ID Wert doppelt vorkommt! In dem Fall wird eine Warnung ausgegeben, aber nicht verhindert das der
     * alte Wert überschrieben wird!
     * 
     * @param key Der Key der überprüft werden soll
     */
    private void keyDoppelt(String key, Map<String, ? extends CharElement> hash) throws KeyExistsException {
        if (hash.containsKey(key)) {
            // Doppelte ID, dadurch wird der alte Wert überschrieben
            LOG.warning("Bei der Initialisierung ist eine ID doppelt für die HashMap: " + key);
            ProgAdmin.messenger.sendFehler(FactoryFinder.find().getLibrary().getErrorTxt("CharKomponente ID doppelt"));
            throw new KeyExistsException("Der Schlüssel " + key + " wird bereits verwendet");
        }
    }

}
