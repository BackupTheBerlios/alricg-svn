/*
 * Created 05.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.d3s.alricg.store.Activator;
import org.d3s.alricg.store.access.hide.XmlVirtualAccessor;
import org.d3s.alricg.store.held.CharakterDaten;
import org.d3s.alricg.store.rules.RegelConfig;
import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * Diese Klasse bietet Funktionen zum Speichern und Laden von Daten an.
 * 
 * @author Vincent
 */
public class StoreAccessor {
	private static String ORIGINAL_FILES_PATH; // Ordner mit den Original-Dateien
	private static String USER_FILES_PATH; // Ordner mit erweiterungen vom User
	private static String CHARS_PATH; // Ordner mit den gespeicherten Charakteren
	private static String RULES_PATH; // Datei in der die Regeln gesichert sind
	
	private static StoreAccessor instance;
	
	private StoreAccessor() {
		try {
			ORIGINAL_FILES_PATH = Platform.getLocation().append(File.separatorChar + "original").toOSString();
			USER_FILES_PATH = Platform.getLocation().append(File.separatorChar + "user").toOSString();
			CHARS_PATH = Platform.getLocation().append(File.separatorChar + "chars").toOSString();
			RULES_PATH = Platform.getLocation().append(File.separatorChar + "rulesConfig.xml").toOSString();
		} catch(org.eclipse.core.runtime.AssertionFailedException e) {
			// TEST dies ist lediglich zum testen, auch für Unit-Tests
			File f = new File("testDir");
			ORIGINAL_FILES_PATH = f.getAbsolutePath() + File.separatorChar + File.separatorChar + "original" + File.separatorChar;
			USER_FILES_PATH = f.getAbsolutePath() + File.separatorChar + File.separatorChar + "user" + File.separatorChar;
			CHARS_PATH = f.getAbsolutePath() + File.separatorChar + File.separatorChar + "chars" + File.separatorChar;
			RULES_PATH = f.getAbsolutePath() + File.separatorChar + File.separatorChar + "rulesConfig.xml";
		}
		proveDir(ORIGINAL_FILES_PATH);
		proveDir(USER_FILES_PATH);
		proveDir(CHARS_PATH);
	}
	
	/**
	 * Setzt alle Pfad-Angaben neu. Ist vor allem für UnitTest wichtig, sollte an keiner anderen
	 * Stelle benutzt werden.
	 * @param originalFolder Ordner mit den Original-Dateien
	 * @param userFolder Ordner mit erweiterungen vom User
	 * @param charFolder Ordner mit den gespeicherten Charakteren
	 * @param ruleFile Datei in der die Regeln gesichert sind
	 */
	public void setPaths(String originalFolder, String userFolder, String charFolder, String ruleFile) {
		ORIGINAL_FILES_PATH = originalFolder;
		USER_FILES_PATH = userFolder;
		CHARS_PATH = charFolder;
		RULES_PATH = ruleFile;
	}
	
	/**
	 * Überprüft ob ein Verzeichnis vorhanden ist, wenn nicht wird es angelegt
	 * @param dirPath Zu prüfendes Verzeichnis
	 */
	private void proveDir(String dirPath) {
		File f  = new File(dirPath);
		if (!f.exists()) {
			f.mkdirs();
		}
	}
	
	/**
	 * Sucht den zu einem File gehörigen XML-Accessor heraus
	 * @param file Der Name des Files
	 * @return Der zugehörige XmlAccessor oder "null" falls keiner existiert
	 */
	public XmlAccessor getXmlAccessor(File file) {
		List<XmlAccessor> accList = StoreDataAccessor.getInstance().getXmlAccessors();
		for (int i = 0; i< accList.size(); i++) {
			if (accList.get(i).getFile().equals(file)) {
				return accList.get(i);
			}
		}
		return null;
	}
	/**
	 * @return Liefert die Instanz des Singeltons StoreAccessor
	 * @throws JAXBException
	 */
	public static StoreAccessor getInstance() {
		if (instance == null)  {
			instance = new StoreAccessor();
		}
		return instance;
	}
	
	/**
	 * Läd alle XML-Files aus den Verzeichnisen ORIGINAL_FILES_PATH und USER_FILES_PATH
	 * und speichert das Ergebnis im XmlVirtualAccessor.
	 * @return
	 * @throws JAXBException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public StoreDataAccessor loadFiles() throws JAXBException, ParserConfigurationException, SAXException, IOException {
	// Alle Files laden
		List<File> files = new ArrayList<File>();
		find( ORIGINAL_FILES_PATH, "(.*\\.xml$)", files );
		find( USER_FILES_PATH, "(.*\\.xml$)", files );
		find( CHARS_PATH, "(.*\\.xml$)", files );
		
	// DefaultCreateAccessor festlegen
		
	// Files in einem Document vereinen
		// Document erzeugen und "Wurzel" erstellen
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		final DocumentBuilder db = factory.newDocumentBuilder();
		Document rootDoc = db.newDocument();
		Element rootElement = rootDoc.createElement(XmlVirtualAccessor.XML_TAG);
		rootDoc.appendChild(rootElement);
		
		// Alle XML-Dateien einlesen und zur Wurzel hinzufügen
		for (int i = 0; i < files.size(); i++) {
			Document doc = db.parse(files.get(i));
			Node node = doc.getFirstChild();
			
			Activator.logger.log(
					Level.INFO,
					"Laden vom Datei: {0} ", 
					files.get(i).getAbsolutePath());
			
			((Element) node).setAttribute(
					XmlVirtualAccessor.XML_FILEPATH_TAG, 
					files.get(i).getAbsolutePath());
			node = rootDoc.importNode(node, true);
			rootElement.appendChild(node);
		}
		
		// Unmarshal
		final JAXBContext ctx = JAXBContext.newInstance(XmlVirtualAccessor.class);
		final Unmarshaller unmarshaller = ctx.createUnmarshaller();
		XmlVirtualAccessor virtuelAccessor = (XmlVirtualAccessor) unmarshaller.unmarshal(rootDoc);

		Activator.logger.log(
				Level.INFO, 
				"Alle XML-Dateien geladen.");
		
		return new StoreDataAccessor(virtuelAccessor);
	}

	/**
	 * Läd alle XML-Files aus den Verzeichnisen ORIGINAL_FILES_PATH und USER_FILES_PATH
	 * und speichert das Ergebnis im XmlVirtualAccessor.
	 * @return
	 * @throws JAXBException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public RegelConfig loadRegelConfig() throws JAXBException, ParserConfigurationException, SAXException, IOException {
		
		// Unmarshal
		final JAXBContext ctx = JAXBContext.newInstance(RegelConfig.class);
		final Unmarshaller unmarshaller = ctx.createUnmarshaller();
		final RegelConfig regelConfig = (RegelConfig) unmarshaller.unmarshal(new File(RULES_PATH));

		Activator.logger.log(
				Level.INFO, 
				"RegelConfig geladen.");
		
		return regelConfig;
	}
	
	/**
	 * Fügt einen neues File, zum Datenbestand hinzu. (Die Datei wird erst mit "save" abgelegt")
	 * @param file File welches hinzugefügt werden soll
	 * @return Der neue XmlAccessor zu dem File
	 */
	public XmlAccessor addNewFile(File file ) {
		XmlAccessor newOne = new XmlAccessor();
		newOne.setFilePath(file.getAbsolutePath());
		newOne.setVersion("0.1");
		
		StoreDataAccessor.getInstance().getXmlAccessors().add(newOne);
		return newOne;
	}
	
	/**
	 * Speichert den XmlAccessor in ein File wie im "filePath" Attribut des 
	 * XmlAccessors angegeben.
	 * @param virtuelAccessor Accessor der gespeichert werden soll
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void saveFile(XmlAccessor xmlAccessor) throws JAXBException, IOException {
		final JAXBContext ctx = JAXBContext.newInstance(XmlAccessor.class);
		final Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		if (xmlAccessor.getFilePath() == null) {
				throw new IOException("Es wurde für mindestes ein XML-File kein " +
						"Speicherort (konkreter FileName als filePath im XmlAccessor) " +
						"angegeben! Entsprechende Files wurden nicht gespeichert.");
		}
		
		marshaller.marshal(
				xmlAccessor, 
				new FileWriter(xmlAccessor.getFilePath())
			);
	}
	
	/**
	 * Speichert den Helden in ein File wie im "filePath" Attribut des 
	 * Helden angegeben.
	 * @param virtuelAccessor Held der gespeichert werden soll
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void saveHeld(CharakterDaten held) throws JAXBException, IOException {
		final JAXBContext ctx = JAXBContext.newInstance(XmlAccessor.class);
		final Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		if (held.getFilePath() == null) {
				throw new IOException("Es wurde für mindestes ein XML-File kein " +
						"Speicherort (konkreter FileName als filePath im XmlAccessor) " +
						"angegeben! Entsprechende Files wurden nicht gespeichert.");
		}
		
		marshaller.marshal(
				held, 
				new FileWriter(held.getFilePath())
			);
	}
	
	/**
	 * Speichert alle Dateien aus dem "XmlVirtualAccessor" in XML-Files. Jeder 
	 * XmlAccessor aus dem "XmlVirtualAccessor" wird in ein File gespeichert 
	 * wie im "filePath" Attribut des XmlAccessors angegeben.
	 * @param virtuelAccessor Accessor der gespeichert werden soll
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void saveFiles(List<XmlAccessor> xmlAccessor) throws JAXBException, IOException {
		
		for (int i = 0; i < xmlAccessor.size(); i++) {
			Activator.logger.log(
					Level.INFO,
					"Speichern von Datei: " + xmlAccessor.get(i).getFilePath());
			saveFile(xmlAccessor.get(i));
		}

	}
	
	/**
	 * Sucht rekursiv von dem Start-Ordner aus nach Dateien die dem Pattern 
	 * entsprechen und speichert sie in der Liste.
	 * @param start Ordner von wo die rekursive Suche beginnt
	 * @param extensionPattern Pattern welche Dateien gesucht werden
	 * @param files Liste aller gefundenen Dateien
	 */
	private void find(String start, String extensionPattern, List<File> files) {
		final Stack<File> dirs = new Stack<File>();
		final File startdir = new File(start);
		final Pattern p = Pattern.compile(extensionPattern,
				Pattern.CASE_INSENSITIVE);

		if (startdir.isDirectory())
			dirs.push(startdir);

		while (dirs.size() > 0) {
			for (File file : dirs.pop().listFiles()) {
				if (file.isDirectory())
					dirs.push(file);
				else if (p.matcher(file.getName()).matches())
					files.add(file);
			}
		}
	}

	/**
	 * @return the oRIGINAL_FILES_PATH
	 */
	public String getOriginalFilesPath() {
		return ORIGINAL_FILES_PATH;
	}

	/**
	 * @return the uSER_FILES_PATH
	 */
	public String getUserFilesPath() {
		return USER_FILES_PATH;
	}

	/**
	 * @return the cHARS_PATH
	 */
	public String getCharsFilesPath() {
		return CHARS_PATH;
	}
	
	
	
}
