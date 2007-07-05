/*
 * Created on 12.02.2005 / 17:43:44
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected an under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.controller;


/**
 * <u>Beschreibung:</u><br> 
 * Wird von allen Klassen implementiert, die die Nachrichten der GUI anzeigen oder
 * überwachen wollen. Dafür registrieren sich die MessageListener beim Messenger mit 
 * der "register(MessageListener)" Methode und werden bis zum deregistrieren mitteles
 * "neueNachricht(Nachricht)" über neu eintreffende Nachrichten informiert.
 * @author V. Strelow
 * @see org.d3s.alricg.GUI.Messenger
 * @see org.d3s.alricg.GUI.Messenger.Nachricht
 */
public interface MessageListener {

	/**
	 * Ist der MessageListener bei einem Messenger registriert, so ruft der 
	 * Messenger beim eintreffen einer neuen Nachricht diese Methode auf mit der
	 * neuen Nachricht als Parameter.
	 * @param neueNachricht Neue, kurz zuvor eingetroffene Nachricht.
	 */
	public void neueNachricht(Nachricht neueNachricht);

}
