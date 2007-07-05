/*
 * Created 23. Januar 2005 / 15:26:21
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.d3s.alricg.gui.SplashScreen;
import org.d3s.alricg.prozessor.utils.FormelSammlung;
import org.d3s.alricg.store.ConfigurationException;
import org.d3s.alricg.store.FactoryFinder;

/**
 * <b>Beschreibung:</b><br>
 * Verwaltet Progammweite Einstellgrößen wie Pfade zu Dateien. Steuert den
 * Progammstart.
 * 
 * @author V.Strelow
 * @stereotype singelton
 */
public class ProgAdmin {

	// Singeltons
	private static final Logger LOG = Logger.getLogger(ProgAdmin.class
			.getName()); // Für Nachrichten aller Art

	public static Messenger messenger; // Für Nachrichten die Angezeigt werden sollen

	//public static HeldenAdmin heldenAdmin; // Verwaltung der Helden

	public static Notepad notepad;

	/**
	 * 
	 * @param args
	 *            An der Stelle "0" ist der Parameter "noScreen" möglich, um
	 *            eine anzeige des Splash-Screen zu unterdrücken (for allem für
	 *            Test-Zwecke)
	 */
	public static void main(String[] args) {
		final boolean showSplash;

		// Auswerten der Parameter
		if (args == null || args.length == 0) {
			showSplash = true;
		} else if (args[0].equals("noScreen")) {
			showSplash = false;
		} else {
			showSplash = true;
		}

		// Logger & Messenger
		try {
			final File f = new File("ressourcen/logging.properties");
			LogManager.getLogManager()
					.readConfiguration(new FileInputStream(f));
		} catch (IOException ioe) {
			LOG.severe("Cannot stup logging correctly!");
			LOG.throwing(ProgAdmin.class.getName(), "main", ioe);
		}

		messenger = new Messenger();
		notepad = new Notepad();
		
		// SplashScreen
		final SplashScreen splash = new SplashScreen();
		splash.setVisible(showSplash);

		// init Programm
		init();

		// Cleanup
		splash.setVisible(false);
		splash.prepareDispose(); // Vom Messenger abmelden
		splash.dispose();
		System.gc();
	}

	private static final void init() {

		try {
			// Initialize store & factory
			FactoryFinder.init();
			LOG.info("Data Store Factory initialisiert...");

			FormelSammlung.initFormelSammlung(notepad);

		} catch (ConfigurationException ce) {
			LOG
					.log(
							Level.SEVERE,
							"Config Datei konnte nicht geladen werden. Programm beendet.",
							ce);
			messenger
					.showMessage(
							Messenger.Level.fehler,
							"Die Config-Datei konnte nicht geladen werden! Bitte überprüfen sie ob die Datei \n"
									+ "zugriffsbereit ist und im Orginalzustand vorliegt. \n"
									+ "\n"
									+ "Das Programm konnte ohne diese Datei nicht gestartet werden \n"
									+ "und wird nun wieder geschlossen!");

			System.exit(1); // Programm Beenden
		}
	}
}
