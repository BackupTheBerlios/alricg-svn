/*
 * Created 26.09.2005
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */

package org.d3s.alricg.controller;

/**
 * @author <a href="mailto:msturzen@mac.com>St. Martin</a>
 */
public class MessengerMock extends Messenger {

    public void register(MessageListener listener) {
    }

    public void sendFehler(String text) {
    }

    public void sendInfo(String text) {
    }

    public void sendMessage(String titel, Level level, String text) {
    }

    public int showMessage(Level level, String text) {
        return 0;
    }

    public void unregister(MessageListener listener) {
    }
}
