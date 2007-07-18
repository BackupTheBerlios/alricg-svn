/*
 * Created 05.07.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.loggingService;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

/**
 * Provisorischer Logger.
 * Zum einen wird auf dem Java-Standard Logger aufgesetzt, zum anderen 
 * wird der Eclipse-Logger als Handler mit angemeldet und berücksichtigt.
 * 
 * @author Vincent
 */
public class LoggerManager {

	/**
	 * Liefert einen Logger.
	 * @param pluginId Id des Plugins
	 * @param plugin PlugIn Objekt
	 * @return Logger zu der pluginId
	 */
	public static Logger getLogger(String pluginId, Plugin plugin) {
		Logger jLogger = Logger.getLogger(pluginId);
		
		if (plugin != null && jLogger.getHandlers().length < 2) {
			jLogger.addHandler(new EclipseLogHandler(pluginId, plugin));
		}
		
		return jLogger;
	}
	
	/**
	 * Handler um das Loggin auch auf Eclipse umzusetzen.
	 * 
	 * @author Vincent
	 */
	public static class EclipseLogHandler extends Handler {
		private String pluginId; // The plug-in ID
		private Plugin plugin; // The shared instance

		public EclipseLogHandler(String pluginId, Plugin plugin) {
			this.pluginId = pluginId;
			this.plugin = plugin;
		}
		
		/* (non-Javadoc)
		 * @see java.util.logging.Handler#close()
		 */
		@Override
		public void close() throws SecurityException {
			// Noop
		}

		/* (non-Javadoc)
		 * @see java.util.logging.Handler#flush()
		 */
		@Override
		public void flush() {
			// Noop
		}

		/* (non-Javadoc)
		 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
		 */
		@Override
		public void publish(LogRecord logRec) {
			int level;
			
			if (logRec.getLevel() == Level.FINE
					|| logRec.getLevel() == Level.FINER
					|| logRec.getLevel() == Level.FINEST) {
				level = IStatus.OK;
			} else if (logRec.getLevel() == Level.INFO) {
				level = IStatus.INFO;
			} else if (logRec.getLevel() == Level.WARNING) {
				level = IStatus.WARNING;
			} else {
				level = IStatus.ERROR;
			}
			
			plugin.getLog().log(
					new Status(
							level, 
							pluginId, 
							level, 
							String.format( logRec.getMessage(), logRec.getParameters() ), 
							logRec.getThrown())
				);
			
		}

		
	}
}
