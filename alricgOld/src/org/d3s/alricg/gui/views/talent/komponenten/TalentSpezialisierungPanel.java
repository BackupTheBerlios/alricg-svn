/*
 * Created on 08.07.2006 / 23:48:35
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.views.talent.komponenten;

import static org.d3s.alricg.gui.views.talent.komponenten.TalentSpezialisierungPanel.LIST_HEIGHT;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.d3s.alricg.charKomponenten.Talent;
import org.d3s.alricg.charKomponenten.links.Link;
import org.d3s.alricg.gui.komponenten.TextFieldList;

/**
 * <u>Beschreibung:</u><br> 
 * GUI-Element für den TableCellEditor für die Talent-Tabellen. 
 * Wir nur für die Talente eingesetzt.
 * @author V. Strelow
 */
public class TalentSpezialisierungPanel extends JPanel implements ActionListener {
	
	//private JScrollPane panPopList = null;
	private JPanel panPopList = null;
	private TalentEditorTextFieldList[] txtTextList;
	private JLabel lblTalentName = null; 
	protected static int LIST_HEIGHT = 75;
	private static String TEXT_SPEZIALISIERUNG = "Spez. Eintragen";
	private TalentSpezialisierungCellEditor cellEditor;
	
	public TalentSpezialisierungPanel(TalentSpezialisierungCellEditor cellEditor) {

		this.cellEditor = cellEditor;
		this.lblTalentName = new JLabel();
		this.txtTextList = new TalentEditorTextFieldList[3];
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(126, 20);
		this.setLayout(new BorderLayout());
		this.add(lblTalentName, BorderLayout.CENTER);
		
		panPopList = new JPanel();
		panPopList.setVisible(false);
		panPopList.setLayout(new BoxLayout(panPopList, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < txtTextList.length; i++) {
			txtTextList[i] = new TalentEditorTextFieldList();
			panPopList.add(txtTextList[i]);
		}

		// Listener für die Auswahl mit der Maus
		lblTalentName.addMouseListener(
				new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if (panPopList.isVisible()) {
							removeEditor();
						} else {
							showPopList();
						}
					}
				}
		);
		
		// Listener für das Hinzufüge / Bewegen / Entfernen des Elements
		this.addAncestorListener( new AncestorListener() {
			public void ancestorAdded(AncestorEvent arg0) {
				showPopList();
			}
			public void ancestorRemoved(AncestorEvent arg0) {
				removeEditor();
			}
			public void ancestorMoved(AncestorEvent arg0) {
				showPopList();
			}			
		});
	}
	
	/**
	 * Initialisiert den Editor für die Verwendung mit einem "direktem" Talent (nur für Tests)
	 * @param talent Das Talent, das gerade editiert wird
	 */
	public void initTalent(Talent talent) {
		for (int i = 0; i < talent.getSpezialisierungen().length; i++) {
			txtTextList[1].addListValue(talent.getSpezialisierungen()[i]);
		}
	}
	
	/**
	 * Initialisiert den Editor für die Verwendung mit einem Link-Talent
	 * @param talentLink Der Link, der gerade editiert wird
	 */
	public void initTalent(Link talentLink) {
		String[] spezArray = new String[0];
		
		if (talentLink.getText().length() > 0 ) {
			spezArray = talentLink.getText().split(",");
		}
		
		// Die Liste der empfohlenden Spezialisierungen initialisieren
		initTalent((Talent) talentLink.getZiel());
		
		assert spezArray.length < 4;
		
		// Vorhandene Spezialisiereungen übertragen 
		for (int i = 0; i < spezArray.length; i++) {
			txtTextList[i].setText( spezArray[i].trim() );
			txtTextList[i].addEnterKeyListener(this);
		}
		// Nicht gesetzte Felder mit Standard text belegen
		for (int i = spezArray.length; i < 3; i++) {
			txtTextList[i].setText(TEXT_SPEZIALISIERUNG);
			txtTextList[i].addEnterKeyListener(this);
		}
		
		// Label setzen mit bisher gewählten Spezialisierungen
		lblTalentName.setText( talentLink.getText() );
	}
	
	/**
	 * Entfernd den Editor 
	 */
	public void removeEditor() {
		panPopList.setVisible(false);
		
		for (int i = 0; i < txtTextList.length; i++) {
			if (txtTextList[i].isPopListVisible()) txtTextList[i].hidePopList();
		}
		
		this.getRootPane().getLayeredPane().remove(panPopList);
		panPopList.updateUI();
	}
	
	/**
	 * Zeigt die "Button-Liste" an und ermöglicht so eine Auswahl 
	 */
	public void showPopList() {
		if ( !this.getRootPane().getLayeredPane().isAncestorOf(panPopList) ) {
			this.getRootPane().getLayeredPane().add(panPopList, JLayeredPane.POPUP_LAYER);
		}
		panPopList.setVisible(true);
		
		doResizeList(); // Anpassen and die Position und Größe
	}
	
	/**
	 * @return Liefert den Inhalt aller Text-Felder mit einem Komma getrennt. Textfelder die nicht verändert 
	 * wurden oder leer gelassen wurden, werden nicht berücksichtigt. 
	 */
	public String getText() {
		String text = "";
		
		for (int i = 0; i < txtTextList.length; i++) {
			if (!txtTextList[i].getText().equals(TEXT_SPEZIALISIERUNG) && txtTextList[i].getText().trim().length() > 0) {
				
				// ersetzt alle "," (diese Trennen verschiedene Spezialisierungen und dürfen daher nicht vom
				// Benutzer gesetzt werden) und alle vor und nachstehenden Leerzeichen
				text += 
					txtTextList[i].getText()
								 .replaceAll(",", " ")
								 .trim()
					 + ", ";
			}
		}
		
		// Letztes ", " abschneiden
		if (text.length() > 0) {
			text = text.substring(0, text.length()-2);
		}
		
		return text; 
	}
	
	/**
	 * Verändert die Größe der Liste und passt diese an die Größe des 
	 * "Parent-Elements" an.
	 */
	private void doResizeList() {
		
		if (panPopList != null && panPopList.isVisible()) {
		    // Errechne die Richtigen Koordinaten auf dem LayeredPane
			Point pt = SwingUtilities.convertPoint(
					this.getParent(), 
					this.getLocation(),
					this.getRootPane().getLayeredPane()
		    );
			
			// Setzen der Größe
			panPopList.setBounds(
					pt.x, 
					pt.y + this.getHeight(),  
					this.getWidth(), LIST_HEIGHT
			);
			panPopList.updateUI(); // Updaten für Anzeige
		}
	}

	/**
	 * Wird aufgerufen, wenn "Enter" bei einem der TextListen geklickt wird.
	 * Beendet die Editierung der Zelle.
	 */
	public void actionPerformed(ActionEvent arg0) {
		cellEditor.stopCellEditing();
	}
	
	//------------------------------------------------------------------------------
	
	/**
	 * Verändert die "TextFielList"-Klasse für die Verwendung in diesem Editor. Dafür 
	 * wird die "PopList" an eine Position HINTER dem Button verschoben.
	 * @author Vincent
	 */
	class TalentEditorTextFieldList extends TextFieldList {
		/**
		 * Passt die Liste an die Position und die Größe des TextFields und des Buttons 
		 * an. Wird beim Anzeigen und bei Größenveränderungen aufgerufen. 
		 */
		protected void doResizeList() {
			
			if (this.panPopList != null && this.panPopList.isVisible()) {
			    // Errechne die Richtigen Koordinaten auf dem LayeredPane
				Point pt = SwingUtilities.convertPoint(
			    		this.getParent(), 
			    		this.getLocation(),
			    		this.getRootPane().getLayeredPane()
			    );
				
				// Setzen der Größe
				this.panPopList.setBounds(
						pt.x + this.getWidth(), 
						pt.y,  
						this.getWidth(), LIST_HEIGHT
				);
				this.panPopList.updateUI(); // Updaten für Anzeige
			}
			
		}
	}
	
}
