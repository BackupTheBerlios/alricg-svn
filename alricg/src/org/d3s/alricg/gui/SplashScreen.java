/*
 * Created on 14.02.2005 / 09:55:58
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

import org.d3s.alricg.controller.MessageListener;
import org.d3s.alricg.controller.Messenger;
import org.d3s.alricg.controller.Nachricht;
import org.d3s.alricg.controller.ProgAdmin;

/**
 * <u>Beschreibung:</u><br> 
 * Fenster was beim Start des Programms angezeigt wird, um die Ladezeit zu "überbrücken".
 * Meldungen können an den Screen mittels Messenger übermittel werden, diese werden in einem 
 * Label angezeigt.
 * @author V. Strelow
 */
public class SplashScreen extends JWindow implements MessageListener {
	private static final String IMAGE_PATH = "ressourcen" + File.separator 
											+ "img" + File.separator 
											+ "SplashScreen.jpg";
	private javax.swing.JPanel jContentPane = null;
	private JLabel lblText = null;
	private JLabel lblImage = null;
	private ImageIcon imgBild;
	
	/* (non-Javadoc) Methode überschrieben
	 * @see org.d3s.alricg.GUI.MessageListener#neueNachricht(org.d3s.alricg.GUI.Messenger.Nachricht)
	 */
	public void neueNachricht(Nachricht neueNachricht) {
		
		// Textfarbe wählen, je nach Level 
		if ( neueNachricht.getLevel().equals(Messenger.Level.info) ) {
			this.lblText.setForeground(Color.black);
		} else if ( neueNachricht.getLevel().equals(Messenger.Level.warnung) ) {
			this.lblText.setForeground(Color.orange);
		} else if ( neueNachricht.getLevel().equals(Messenger.Level.fehler) ||
				    neueNachricht.getLevel().equals(Messenger.Level.fehlerSchwer) ) {
			this.lblText.setForeground(Color.red);
		}
				
		lblText.setText(neueNachricht.getText()); // Text anzeigen
	}

	/**
	 * Kunstruktor für SplashScreen.
	 */
	public SplashScreen() {
		super();
		initialize();
	}
	/**
	 * Initialisiert den SplashScreen
	 */
	private void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		
		ProgAdmin.messenger.register(this); // Um Nachrichten empfangen zu können
		this.lblText = new JLabel("SplashScreen initialisiert...");
		this.lblImage = new JLabel();
		this.imgBild = new ImageIcon(IMAGE_PATH); // Bild laden
		lblImage.setIcon(imgBild); // Bild anzeigen
		
		//Position in Bildmitte bestimmen sowie die größe des Fensters
		this.setSize(imgBild.getIconWidth(), imgBild.getIconHeight() + lblText.getHeight());
		setLocation(d.width / 2 - this.getWidth() / 2, d.height / 2 - this.getHeight() / 2);
		
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			
			jContentPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
			
			jContentPane.add(lblText, java.awt.BorderLayout.SOUTH);
			jContentPane.add(lblImage, java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	/**
	 * Bereitet den SplashScreen auf das schließen vor und "De-Registert" den SplashScreen 
	 * wieder vom Messenger.
	 */
	public void prepareDispose() {
		ProgAdmin.messenger.unregister(this);
	}
}
