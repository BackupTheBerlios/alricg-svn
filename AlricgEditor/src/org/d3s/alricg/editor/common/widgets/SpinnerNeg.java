/*
 * Created 24.08.2007
 *
 * This file is part of the project Alricg. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://www.alricg.de/".
 */
package org.d3s.alricg.editor.common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

/**
 * Spinner mit der möglichkeit auch negative Werte darzustellen
 */
public class SpinnerNeg extends Composite {
	static final int BUTTON_WIDTH = 16;

	private final Text text;
	private final Button up, down;
	private int minimum, maximum;

	private double step;
	private double pageStep;
	
	// Zeigt an ob die Maus über einem Button gedrück gehalten wird
	private boolean countUpFlag;
	private boolean countDownFlag; 
	
	public SpinnerNeg(Composite parent, int style) {

		super(parent, style);

		text = new Text(this, style | SWT.SINGLE | SWT.BORDER);
		up = new Button(this, style | SWT.ARROW | SWT.UP);
		down = new Button(this, style | SWT.ARROW | SWT.DOWN);
		text.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				try {
					e.doit = true;
					// Steuerzeichen sind erlaubt
					if (e.keyCode == SWT.DEL
							|| e.keyCode == SWT.TAB
							|| e.keyCode == SWT.BS) {
						return;
					}
					
					// ... sowie Zahlen Werte
					Double.parseDouble(e.text);
					
				} catch (NumberFormatException ex) {
					e.doit = false;
				}
			}});
		
		text.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				traverse(e);
			}
		});

		up.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				up();
			}
		});
		
		text.addKeyListener(new KeyListener() {
			@Override
			// Um die "PageUp" und "PageDown" Tasten zu implementieren 
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.PAGE_UP) {
					pageUp();
				} else if (e.keyCode == SWT.PAGE_DOWN) {
					pageDown();
				}
			}

			@Override
			// Stellt sicher, das die Zahl immer gültig bleibt
			public void keyReleased(KeyEvent e) {
				double d = Double.parseDouble(text.getText());
				if (d > maximum) {
					text.setText(Integer.toString(maximum));
				} else if (d < minimum) {
					text.setText(Integer.toString(minimum));
				} else if (d % step != 0) {
					text.setText(Double.toString((int) d));
				}
			}
			
		});
		
		up.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				countUpFlag = true;
				countUp();
			}

			@Override
			public void mouseUp(MouseEvent e) {
				countUpFlag = false;
			}
		});
		
		up.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				countUpFlag = false;
			}
		});
		
		down.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				down();
			}
		});
		
		down.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				countDownFlag = true;
				countDown();
			}

			@Override
			public void mouseUp(MouseEvent e) {
				countDownFlag = false;
			}
		});
		
		down.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				countDownFlag = false;
			}
			
		});

		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				resize();
			}
		});

		addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event e) {
				focusIn();
			}
		});

		text.setFont(getFont());
		minimum = 0;
		maximum = 9;
		step = 1d;
		pageStep = 1d;
		setSelection(minimum);
	}
	
	private void countUp() {
		getDisplay().timerExec (100, new Runnable () {
			public void run () {
				if ( countUpFlag ) {
					up();
					countUp();
				}
			}
		});
	}
	
	private void countDown() {
		getDisplay().timerExec (100, new Runnable () {
			public void run () {
				if ( countDownFlag ) {
					down();
					countDown();
				}
			}
		});
	}

	void traverse(Event e) {

		switch (e.detail) {
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			if (e.keyCode == SWT.ARROW_UP) {
				e.doit = true;
				e.detail = SWT.NULL;
				up();
			}
			break;

		case SWT.TRAVERSE_ARROW_NEXT:
			if (e.keyCode == SWT.ARROW_DOWN) {
				e.doit = true;
				e.detail = SWT.NULL;
				down();
			}
			break;
		}

	}

	void up() {
		setSelection(getSelection() + step);
		notifyListeners(SWT.Selection, new Event());
	}

	void down() {
		setSelection(getSelection() - step);
		notifyListeners(SWT.Selection, new Event());
	}
	
	void pageUp() {
		setSelection(getSelection() + pageStep);
		notifyListeners(SWT.Selection, new Event());
	}
	
	void pageDown() {
		setSelection(getSelection() - pageStep);
		notifyListeners(SWT.Selection, new Event());
	}

	void focusIn() {
		text.setFocus();
	}

	public void setFont(Font font) {
		super.setFont(font);
		text.setFont(font);
	}

	public void setSelection(double selection) {

		if (selection < minimum) {
			selection = minimum;
		} else if (selection > maximum) {
			selection = maximum;
		}

		text.setText(String.valueOf(selection));
		text.selectAll();
		text.setFocus();
	}

	public double getSelection() {
		if (text.getText().length() == 0) {
			text.setText("0");
		}
		return Double.parseDouble(text.getText());
	}

	public void setMaximum(int maximum) {
		checkWidget();
		this.maximum = maximum;
		resize();
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMinimum() {
		return minimum;
	}
	

	/**
	 * @return "Schritt" bei einfachem Klick auf Hoch/Runter
	 */
	public double getStep() {
		return step;
	}

	/**
	 * @param "Schritt" bei einfachem Klick auf Hoch/Runter
	 */
	public void setStep(double step) {
		this.step = step;
	}

	/**
	 * @return "Schritt" bei der Tase "PageUp"/"PageDown"
	 */
	public double getPageStep() {
		return pageStep;
	}

	/**
	 * @param "Schritt" bei der Tase "PageUp"/"PageDown"
	 */
	public void setPageStep(double pageStep) {
		this.pageStep = pageStep;
	}

	void resize() {
		Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int textWidth = pt.x - BUTTON_WIDTH;
		int buttonHeight = pt.y / 2;
		text.setBounds(0, 0, textWidth, pt.y);
		up.setBounds(textWidth, 0, BUTTON_WIDTH, buttonHeight);
		down.setBounds(textWidth, pt.y - buttonHeight, BUTTON_WIDTH,
				buttonHeight);
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		GC gc = new GC(text);
		Point textExtent = gc.textExtent("-" + String.valueOf(maximum)+ ".0");
		gc.dispose();
		Point pt = text.computeSize(textExtent.x, textExtent.y);

		int width = pt.x + BUTTON_WIDTH;
		int height = pt.y;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;

		return new Point(width, height);
	}

	public void addSelectionListener(SelectionListener listener) {
		if (listener == null)
			throw new SWTError(SWT.ERROR_NULL_ARGUMENT);

		addListener(SWT.Selection, new TypedListener(listener));
	}
	
	public void addModifyListener(ModifyListener listener) {
		text.addModifyListener(listener);
	}
}
