/*
 * Created 01.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.editors;

/**
 * @author Vincent
 */
public interface ISavableEditor {
	public void addSaveListener(IEditorSaveListener listener);
	public void removeSaveListener(IEditorSaveListener listener);
}
