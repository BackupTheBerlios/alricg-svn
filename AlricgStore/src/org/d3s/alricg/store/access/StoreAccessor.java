/*
 * Created 05.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.store.access;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * @author Vincent
 *
 */

public class StoreAccessor {
	private static String ORIGINAL_FILES_PATH; 
	private static String USER_FILES_PATH;
	private static String CHARS_PATH;
	private static StoreAccessor instance; 
	private JAXBContext ctx;
	
	private StoreAccessor() throws JAXBException {
		ctx = JAXBContext.newInstance(XmlVirtualAccessor.class);
		
		try {
			ORIGINAL_FILES_PATH = Platform.getLocation().append(File.separatorChar + "original" + File.separatorChar).toOSString();
			USER_FILES_PATH = Platform.getLocation().append(File.separatorChar + "user" + File.separatorChar).toOSString();
		} catch(org.eclipse.core.runtime.AssertionFailedException e) {
			// TEST dies ist lediglich zum testen
			File f = new File("testDir");
			ORIGINAL_FILES_PATH = f.getAbsolutePath() + File.separatorChar + "original" + File.separatorChar;
			USER_FILES_PATH = f.getAbsolutePath() + File.separatorChar + "user" + File.separatorChar;
			CHARS_PATH = f.getAbsolutePath() + File.separatorChar + "chars" + File.separatorChar;
		}
		proveDir(ORIGINAL_FILES_PATH);
		proveDir(USER_FILES_PATH);
		proveDir(CHARS_PATH);
	}
	
	private void proveDir(String dirPath) {
		File f  = new File(dirPath);
		if (!f.exists()) {
			f.mkdirs();
		}
	}
	
	public static StoreAccessor getIntance()  throws JAXBException {
		if (instance == null)  {
			instance = new StoreAccessor();
		}
		return instance;
	}
	
	public XmlVirtualAccessor loadFiles() throws JAXBException, ParserConfigurationException, SAXException, IOException {
	// Alle Files laden
		List<File> files = new ArrayList<File>();
		find( ORIGINAL_FILES_PATH, "(.*\\.xml$)", files );
		find( USER_FILES_PATH, "(.*\\.xml$)", files );
		
	// Files in einem Document vereinen
		// Document erzeugen und "Wurzel" erstellen
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		Document rootDoc = db.newDocument();
		Element rootElement = rootDoc.createElement(XmlVirtualAccessor.XML_TAG);
		rootDoc.appendChild(rootElement);
		
		for (int i = 0; i < files.size(); i++) {
			Document doc = db.parse(files.get(i));
			Node node = doc.getFirstChild();
			((Element) node).setAttribute(
					XmlVirtualAccessor.XML_FILEPATH_TAG, 
					files.get(i).getAbsolutePath());
			node = rootDoc.importNode(node, true);
			rootElement.appendChild(node);
		}
		
		
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		XmlVirtualAccessor virtuelAccessor = (XmlVirtualAccessor) unmarshaller.unmarshal(rootDoc);

		return virtuelAccessor;

	}

	public void saveFiles(XmlVirtualAccessor virtuelAccessor) throws JAXBException {
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(virtuelAccessor, System.out);
		
	}
	
	
	public List<File> find(String start, String extensionPattern, List<File> files ) 
	  { 
	    final Stack<File> dirs = new Stack<File>(); 
	    final File startdir = new File( start ); 
	    final Pattern p = Pattern.compile( extensionPattern, Pattern.CASE_INSENSITIVE ); 
	 
	    if ( startdir.isDirectory() ) 
	      dirs.push( startdir ); 
	 
	    while ( dirs.size() > 0 ) 
	    { 
	      for ( File file : dirs.pop().listFiles() ) 
	      { 
	        if ( file.isDirectory() ) 
	          dirs.push( file ); 
	        else 
	          if ( p.matcher(file.getName()).matches() ) 
	            files.add( file ); 
	      } 
	    } 
	 
	    return files; 
	  } 
	
}
