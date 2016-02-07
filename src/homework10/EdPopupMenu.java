package homework10;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

/* @author Andrius Didziulis
 * Andrew ID: adidziul
 * Date: 12/09/2015
 * 
 * This the right click menu class that is used by TextEditor.
 * It contains has six features: undo, redo, cut, copy, paste, 
 * and save (same as a feature menu bar's Save). 
 * All of these features assist in editing the text in Text Editor. 
 */

@SuppressWarnings("serial")
public class EdPopupMenu extends JPopupMenu {

	public EdPopupMenu() {
		// creating pop menu
		
		JMenuItem undoItem = new JMenuItem("Undo");
		undoItem.addActionListener(new PMItemListener());
		undoItem.setActionCommand("Redo");
		undoItem.setToolTipText("Undo Action");
		undoItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
		add(undoItem);
		
		JMenuItem redoItem = new JMenuItem("Redo");
		redoItem.addActionListener(new PMItemListener());
		redoItem.setActionCommand("Redo");
		redoItem.setToolTipText("Redo Action");
		redoItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
		add(redoItem);
		
		addSeparator();
		
		JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
		cutItem.setText("Cut");
		cutItem.setToolTipText("Cut");
		cutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
		add(cutItem);

		JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
		copyItem.setText("Copy");
		copyItem.setToolTipText("Copy");
		copyItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		add(copyItem);

		JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
		pasteItem.setText("Paste");
		pasteItem.setToolTipText("Paste");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
		add(pasteItem);
		
		addSeparator();
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new PMItemListener());
		saveItem.setActionCommand("Save");
		saveItem.setToolTipText("Save Text");
		saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		add(saveItem);
		
	}

	// setting Actionlisteners for elements in Popup Menu
	private class PMItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Robot rb;
		
			switch (e.getActionCommand()) {
			case "Save":
				EdMenuBar.doSave();
				break;
			case "Redo":
				rb = new Robot();
				rb.keyPress(KeyEvent.VK_CONTROL);
				rb.keyPress(KeyEvent.VK_Y);
				rb.keyRelease(KeyEvent.VK_Y);
				rb.keyRelease(KeyEvent.VK_CONTROL);
				break;
			case "Undo":
				rb = new Robot();
				rb.keyPress(KeyEvent.VK_CONTROL);
				rb.keyPress(KeyEvent.VK_Z);
				rb.keyRelease(KeyEvent.VK_Z);
				rb.keyRelease(KeyEvent.VK_CONTROL);
				break;
			}
			} catch (AWTException e1) {
				JOptionPane.showMessageDialog(null,
						"The button did not work correctly. Please try again.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}


	}
}
