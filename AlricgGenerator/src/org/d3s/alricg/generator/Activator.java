package org.d3s.alricg.generator;

import java.util.HashMap;
import java.util.logging.Logger;

import org.d3s.alricg.common.charakter.Charakter;
import org.d3s.alricg.common.charakter.SonderregelAdmin;
import org.d3s.alricg.common.charakter.VerbilligteFertigkeitAdmin;
import org.d3s.alricg.common.charakter.VoraussetzungenAdmin;
import org.d3s.alricg.common.logic.Prozessor;
import org.d3s.alricg.generator.prozessor.GeneratorMagieStatusAdmin;
import org.d3s.alricg.generator.prozessor.ProzessorDecorator;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorEigenschaften;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorHerkunft;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorLiturgie;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorNachteil;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorSonderfertigkeit;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorTalent;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorVorteil;
import org.d3s.alricg.generator.prozessor.charElemente.ProzessorZauber;
import org.d3s.alricg.store.charElemente.Eigenschaft;
import org.d3s.alricg.store.charElemente.Herkunft;
import org.d3s.alricg.store.charElemente.Liturgie;
import org.d3s.alricg.store.charElemente.Nachteil;
import org.d3s.alricg.store.charElemente.Sonderfertigkeit;
import org.d3s.alricg.store.charElemente.Talent;
import org.d3s.alricg.store.charElemente.Vorteil;
import org.d3s.alricg.store.charElemente.Zauber;
import org.d3s.alricg.store.held.CharakterDaten;
import org.d3s.loggingService.LoggerManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.d3s.alricg.generator";
	public static Logger logger = LoggerManager.getLogger(PLUGIN_ID, null);
	private static Charakter charakter;
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		logger = LoggerManager.getLogger(PLUGIN_ID, this);
		
		// Charakter erzeugen
		final CharakterDaten charData = new CharakterDaten();
		charakter = new Charakter(charData);
		charakter.initCharakterAdmins(
				new SonderregelAdmin(charakter), 
				new VerbilligteFertigkeitAdmin(charakter), 
				new VoraussetzungenAdmin(charakter), 
				new GeneratorMagieStatusAdmin(charakter));
		
		// Alle Prozessoren erzeugen
		HashMap<Class, Prozessor> hash = new HashMap<Class, Prozessor>();
		charakter.setProzessorHash(hash);
		
		hash.put(
				Eigenschaft.class,
				new ProzessorDecorator(charakter, new ProzessorEigenschaften(charakter)));
		hash.put(
				Talent.class,
				new ProzessorDecorator(charakter, new ProzessorTalent(charakter)));
		hash.put(
				Zauber.class,
				new ProzessorDecorator(charakter, new ProzessorZauber(charakter)));
		hash.put(
				Sonderfertigkeit.class,
				new ProzessorDecorator(charakter, new ProzessorSonderfertigkeit(charakter)));
		hash.put(
				Vorteil.class,
				new ProzessorDecorator(charakter, new ProzessorVorteil(charakter)));
		hash.put(
				Herkunft.class,
				new ProzessorDecorator(charakter, new ProzessorHerkunft(charakter)));
		hash.put(
				Nachteil.class,
				new ProzessorDecorator(charakter, new ProzessorNachteil(charakter)));
		hash.put(
				Liturgie.class,
				new ProzessorDecorator(charakter, new ProzessorLiturgie(charakter)));

	}

	public static Charakter getCurrentCharakter() {
		return charakter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
