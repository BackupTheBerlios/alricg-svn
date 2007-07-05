/*
 * Created on 29.03.2005 / 01:24:12
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.gui.komponenten;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sun.java.swing.plaf.motif.MotifGraphicsUtils;

/**
 * <u>Beschreibung:</u><br> 
 * Eine neue Swing-Komponente, bestehend aus einem Text-Feld und einem Button. Wird der
 * Button gedr�ckt wird eine Auswahl-Liste "aufgeklappt" wie bei ComboBoxen. Es kann
 * dann ein Wert f�r das TextFeld ausgew�hlt werden.
 * TODO Den Button mit einem Bild ausstatten.
 * @author V. Strelow
 */
public class TextFieldList extends JPanel {
    private final ImageIcon DOWN_ICON = new ImageIcon(MotifGraphicsUtils.class
			.getResource("icons/ScrollDownArrow.gif"));
    
	private static int LIST_HEIGHT = 75;
	private JTextField txtText = null;
	private JButton butList = null;
	private JList lstPopList = null;
	protected JScrollPane panPopList = null;
	private DefaultListModel listModel = null;
	private ActionListener[] actionListener = new ActionListener[0];
	
	/**
	 * This is the default constructor
	 */
	public TextFieldList() {
		initialize();
	}
	
	/**
	 * This is the default constructor
	 */
	public TextFieldList(String text) {
		initialize();
		txtText.setText(text);
	}
	
	/**
	 * F�gt einen Listener hinzu. Der Listener reagiert nur auf "Enter" KeyEvents.
	 * @param listener Listener der zur TextList hinzugef�gt wird
	 */
	public void addEnterKeyListener(ActionListener listener) {
		ActionListener[] listenerAR = new ActionListener[actionListener.length + 1];
		
		for (int i = 0; i < actionListener.length; i++) {
			listenerAR[i] = actionListener[i];
		}
		listenerAR[listenerAR.length-1] = listener;
		
		actionListener = listenerAR;
	}
	
	/**
	 * Informiert alle Listener �ber ein dr�cken der "Enter" Taste.
	 */
	private void firePressEnter() {
		for (int i = 0; i < actionListener.length; i++) {
			actionListener[i].actionPerformed(null);
		}
	}
	
	/**
	 * Initialisiert das TextFieldList
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(126, 20);
		this.add(getTxtText(), java.awt.BorderLayout.CENTER);
		this.add(getButList(), java.awt.BorderLayout.EAST);
		
		// Listener Hinzuf�gen f�r Gr��enanpassung bei 
		this.addComponentListener(
				new ComponentAdapter() {
					public void componentMoved(ComponentEvent e) {
						doResizeList();
					}
					public void componentResized(ComponentEvent e) {
						doResizeList();
					}
				}
		);
		txtText.addKeyListener(
			new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					if (event.getKeyCode() == KeyEvent.VK_ENTER) {
		        		firePressEnter();
					}
				}
			}		
		);
		
		panPopList = getPanPopList();
		panPopList.setVisible(false);
	}
	
	/**
	 * This method initializes jTextField
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtText() {
		if (txtText == null) {
			txtText = new JTextField();
		}
		return txtText;
	}
	
	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton	
	 */    
	private JButton getButList() {
		if (butList == null) {
			butList = new JButton();
			butList.setMargin(new java.awt.Insets(0,0,0,0));
			butList.setIcon(DOWN_ICON);
			
			// Listener zum schlie�en der PopList
			butList.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!panPopList.isVisible()) {
							showPopList();
						} else {
							hidePopList();
						}
					}
				}
			);
			
		}
		return butList;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getLstPopList() {
		if (lstPopList == null) {
			listModel = new DefaultListModel();
			lstPopList = new JList(listModel);
			lstPopList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstPopList.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaptionBorder,1));

			lstPopList.addFocusListener(
					new FocusAdapter() {
						public void focusLost(FocusEvent e) {
							// Panel schlie�en, wenn nicht auf den Button geklick
							// wurde (der Button �bernimmt dann das schlie�en)
							if (!e.getOppositeComponent().equals(butList)) {
								hidePopList();
							}
						}
					}
			);
			// Listener f�r die Auswahl mit der Maus
			lstPopList.addMouseListener(
					new MouseAdapter() {
						public void mouseReleased(MouseEvent e) {
							listItemToText();
						}
				        public void mouseClicked(MouseEvent evt) {
			        		listItemToText();
			        		hidePopList();
				        }
					}
			);
			// Listener f�r die Auswahl mit "Enter"
			lstPopList.addKeyListener(
					new KeyAdapter() {
						public void keyPressed(KeyEvent event) {
							if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				        		listItemToText();
				        		hidePopList();
							}
						}
					}
			);
		}
		return lstPopList;
	}
	
	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getPanPopList() {
		if (panPopList == null) {
			panPopList = new JScrollPane();
			panPopList.setViewportView(getLstPopList());
		}
		return panPopList;
	}
	
	/**
	 * Zeigt die "Button-Liste" an und erm�glicht so eine Auswahl 
	 */
	private void showPopList() {
		this.getRootPane().getLayeredPane().add(panPopList, JLayeredPane.POPUP_LAYER);
		panPopList.setVisible(true);
		
		doResizeList(); // Anpassen and die Position und Gr��e
		lstPopList.requestFocus();
	}
	
	/**
	 * Passt die Liste an die Position und die Gr��e des TextFields und des Buttons 
	 * an. Wird beim Anzeigen und bei Gr��enver�nderungen aufgerufen. 
	 */
	protected void doResizeList() {
		
		if (panPopList != null && panPopList.isVisible()) {
		    // Errechne die Richtigen Koordinaten auf dem LayeredPane
			Point pt = SwingUtilities.convertPoint(
		    		this.getParent(), 
		    		this.getLocation(),
		    		this.getRootPane().getLayeredPane()
		    );
			
			// Setzen der Gr��e
			panPopList.setBounds(
					pt.x, 
					pt.y + this.getHeight(),  
					this.getWidth(), LIST_HEIGHT
			);
			panPopList.updateUI(); // Updaten f�r Anzeige
		}
		
	}
	
	/**
	 * Liefert zur�ck, ob die PopList sichbar ist oder nicht
	 * @return true - Die PopList ist sichtbar, ansonsten false
	 */
	public boolean isPopListVisible() {
		return panPopList.isVisible();
	}
	
	/**
	 * Schlie�t die Pop-List
	 */
	public void hidePopList() {
		panPopList.setVisible(false);
		
		if (this.getRootPane() != null) {
			this.getRootPane().getLayeredPane().remove(panPopList);
		}
	}
	
	/**
	 * Sorg daf�r, dass die Auswahl der Liste in das TextField kopiert wird.
	 */
	private void listItemToText() {
		if (panPopList.isVisible() && !lstPopList.isSelectionEmpty()) {
			txtText.setText(lstPopList.getSelectedValue().toString());
		}
	}
	
	/**
	 * @return Liefert den Text zur�ck, der in TextField steht
	 */
	public String getText() {
		return txtText.getText();
	}
	
	/**
	 * Setzt den angezeigten Text neu
	 * @param text Der neue Text des TextFields
	 */
	public void setText(String text) {
		txtText.setText(text);
	}
	
	/**
	 * F�gt einen neuen Eintrag zu der Auswahl-Liste hinzu.
	 * @param entry Neues Element f�r die Auswahlliste.
	 */
	public void addListValue(String entry) {
		listModel.addElement(entry);
	}
	
	/**
	 * F�gt einen neuen Eintrag zu der Auswahl-Liste hinzu an der stelle idx
	 * @param idx Position des neuen Eintrags
	 * @param entry Neuer Eintrga
	 */
	public void addListValueAt(int idx, String entry) {
		listModel.add(idx, entry);
	}
	
	/**
	 * Entfernt einen Eintrag aus der Auswahl-Liste
	 * @param entry Eintrag zum entfernen
	 */
	public void removeListValue(String entry) {
		listModel.removeElement(entry);
	}
	
	/**
	 * Entfernt einen Eintrag aus der Auswahl-Liste
	 * @param idx Index des Eintrags, der entfernt werden soll
	 */
	public void removeListValue(int idx) {
		listModel.remove(idx);
	}
	
	/**
	 * Entfernd alle Eintr�ge
	 */
	public void removeAllListValues() {
		listModel.removeAllElements();
	}
	
	/**
	 * Liefert den EIntrag aus der AuswahlList an der Position idx
	 * @param idx gew�nschte Position
	 *
	public String getListValue(int idx) {
		return listModel.get(idx).toString();
	}*/
	
	/**
	 * @return Die Anzahl der Eintr�ge in der Auswahl-Liste
	 */
	public int countListValues() {
		return listModel.size();
	}
	
	public void setListVisible(boolean visible) {
		panPopList.setVisible(visible);
	}

 }  //  @jve:decl-index=0:visual-constraint="10,10"
