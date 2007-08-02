/*
 * Created 01.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.views;

import java.io.File;

/**
 * @author Vincent
 *
 */
public class ViewModel {
	private static File markedFileForNew;

	/**
	 * @return the markedFileForNew
	 */
	public static File getMarkedFileForNew() {
		return markedFileForNew;
	}

	/**
	 * @param markedFileForNew the markedFileForNew to set
	 */
	public static void setMarkedFileForNew(File markedFileForNew) {
		ViewModel.markedFileForNew = markedFileForNew;
	}
	
	
}
