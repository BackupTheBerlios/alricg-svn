/*
 * Created on 04.04.2005 / 10:04:35
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 */
package org.d3s.alricg.gui.komponenten.table;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.d3s.alricg.gui.komponenten.table.renderer.ButtonEditor;
import org.d3s.alricg.gui.komponenten.table.renderer.ButtonRenderer;
import org.d3s.alricg.gui.komponenten.table.renderer.ImageRenderer;
import org.d3s.alricg.gui.komponenten.table.renderer.ImageTextRenderer;

import com.sun.java.swing.plaf.motif.MotifGraphicsUtils;

/**
 * <u>Beschreibung:</u><br>
 * Diese JTable Arbeitet mit dem AbstractSortableTableModel zusammen.
 * Es Ermöglicht 
 * - Sortierung nach Spalten
 * - Anzeigen von ToolTips für einzelne Zellen und Tablellenköpfe
 * - Leichter Einbau von Buttons als Tabellen Elemente
 * 
 * @author V. Strelow
 */
public class SortableTable extends JTable {
	SortableTableModelInterface model;
	
	// Pfeile für die Sortierung
    private final ImageIcon UP_ICON = new ImageIcon(MotifGraphicsUtils.class
            		.getResource("icons/ScrollUpArrow.gif"));
    private final ImageIcon DOWN_ICON = new ImageIcon(MotifGraphicsUtils.class
    				.getResource("icons/ScrollDownArrow.gif"));
    
    
    /* (non-Javadoc) Methode überschrieben
	 * @see javax.swing.JTable#setModel(javax.swing.table.TableModel)
	 */
	public void setModel(SortableTableModelInterface model) {
		super.setModel(model);
		this.model = model;
		init();
		
	}
    
    private void init() {
    	
    	// Anzeige der Pfeile für die Sortierung der Spalten
    	this.getTableHeader().setDefaultRenderer(
    		new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {

                    JTableHeader header = table.getTableHeader();
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());

                    setText(value == null ? "" : value.toString());
                    setBorder(UIManager.getBorder("TableHeader.cellBorder"));

                    // Wenn die Spalte nicht sortierbar ist, wird auch 
                    // kein Pfeil angezeigt
                    if (!model.isSortable(column)) {
                    	setIcon(null);
                    	return this;
                    }
                    
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setHorizontalTextPosition(SwingConstants.LEFT);
                    
                    // Setzen des Pfeils abhängig von der Sortierung
                    if (model.isSortColumnDesc(column)) {
                        setIcon(UP_ICON);
                    } else {
                        setIcon(DOWN_ICON);
                    }

                    return this;
                }
            }
        );
        
        // Listener für die Sortierung der Spalten
        this.getTableHeader().addMouseListener(
        	new MouseAdapter() {
	            public void mousePressed(MouseEvent evt) {
	            	int column = columnAtPoint(evt.getPoint());
	            	column = convertColumnIndexToModel(column);
	            	
	            	// Sortieren wenn sortierbar
	            	if (model.isSortable(column)) {
	            		model.sortTableByColumn(column);
	            		updateUI();
	            	}
	            }
        	}
        );
    }

    /* (non-Javadoc) Methode überschrieben.
     * Ermöglicht ToolTips für jede Zelle Extra
     * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
     */
    public String getToolTipText(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        
        // Bestimmen der Zeile/ Spalte um umrechnen auf das Modell
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        int realColumnIndex = convertColumnIndexToModel(colIndex);

        return model.getToolTip(rowIndex, colIndex);
    }
    

    /* (non-Javadoc) Methode überschrieben
     * Ermöglicht ToolTips für die TableHeader
     * @see javax.swing.JTable#createDefaultTableHeader()
     */
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
            public String getToolTipText(MouseEvent e) {
            	
            	// Bestimmen der Spalte
                int index = columnModel.getColumnIndexAtX(e.getPoint().x);
                index = convertColumnIndexToModel(index);
                
                return model.getHeaderToolTip(index);
            }
        };
    }
    
    /**
     * Hiermit wird eine Spalte so eingestellt, das sie einen Button anzeigt.
     * @param column Die Spalte die per Button bedient werden soll
     * @param buttonText Der text auf dem Button
     */
    public void setColumnButton(String colName, String buttonText) {
    	
    	// Der Renderer wird mit einem neuen Button erstellt, der 
    	// den selben Text trägt wie der gegebende Text
		this.getColumn(colName).setCellRenderer(
				new ButtonRenderer(new JButton(buttonText)));
		// Der Editor wird mit dem Button erstellt
		this.getColumn(colName).setCellEditor(
				new ButtonEditor(new JButton(buttonText)));
    }
    
    /**
     * Hiermait wird eine Spalte so eingestellt, das sie einen Text mit 
     * einem Bild darstellen kann.
     * @param column Die Nummer der Spalte die per Text + Icon dargestellt werden soll
     * @param isIconVorText true - Erst kommt das Icon (=links), danach der Text, 
     * 						false - Erst kommt der Text (=links), danach das Icon
     */
    public void setColumnTextImage(String colName, boolean isIconLinks) {
    	this.getColumn(colName).setCellRenderer(new ImageTextRenderer(isIconLinks));
	}
    
    /**
     * Hiermit wird eine Spalte so eingestellt, das sie ein oder mehrer Icons 
     * anzeigt.
     * @param column Die Spalte die ein Bild anzeigen soll
     * @param buttonText Der text auf dem Button
     */
    public void setColumnMultiImage(String colName) {    	
    	this.getColumn(colName).setCellRenderer(new ImageRenderer());
    }
    
}