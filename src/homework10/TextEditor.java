package homework10;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/* @author Andrius Didziulis
 * Andrew ID: adidziul
 * Date: 12/09/2015
 * 
 * This the main class to setup the text editor.
 * Also it contains of setter and getter method 
 * for JLabel jl and JTextArea jta variables
 * that are used by EdMenuBar class. 
 */

public class TextEditor {
	static Path currentRelativePath = Paths.get("");
	public static String DEFAULT_PATH = currentRelativePath.toAbsolutePath().toString();
	private JFrame editorFrame = new JFrame("Text Editor");
	private static JTextArea jta;
	private static JLabel jl;
	private JPopupMenu jpm;
	private static UndoManager manager;

	public static void main(String[] args) {
		TextEditor te = new TextEditor();
		te.setupEditor();
	}

	private void setupEditor() {
		// describing the Frame
		editorFrame.setVisible(true);
		editorFrame.setPreferredSize(new Dimension(600, 600));
		editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editorFrame.setLayout(new BorderLayout());

		// creating Menu bar
		JMenuBar mmb = new EdMenuBar();
		mmb.setPreferredSize(new Dimension(600, 30));
		mmb.setVisible(true);
		editorFrame.setJMenuBar(mmb);

		// creating Text area
		jta = new JTextArea();
		jta.setLineWrap(true);
		jta.setEditable(true);
		jta.setWrapStyleWord(true);
		jta.setCaretPosition(0);
		jta.setEditable(true);
		jta.setVisible(true);
		jta.setEnabled(true);
		jta.addMouseListener(new PopupTriggerListener());
		addUndoRedoSave();

		// creating Scroll pane
		JScrollPane jsp = new JScrollPane(jta);
		jsp.setPreferredSize(new Dimension(600, 540));
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorFrame.add(BorderLayout.CENTER, jsp);

		// creating Label in the page end
		jl = new JLabel();
		jl.setPreferredSize(new Dimension(600, 30));
		editorFrame.add(BorderLayout.SOUTH, jl);

		// creating Popup Menu
		jpm = new EdPopupMenu();
		jpm.setVisible(false);
		jpm.setPreferredSize(new Dimension(100, 150));

		// finishing to build the frame
		editorFrame.pack();
	}

	// implementing Undo and Redo functionality
	@SuppressWarnings("serial")
	public static void addUndoRedoSave() {
		manager = new UndoManager();
		jta.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				manager.addEdit(e.getEdit());
			}
		});

		InputMap im = jta.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = jta.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Save");

		am.put("Undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (manager.canUndo()) {
						manager.undo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});
		am.put("Redo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (manager.canRedo()) {
						manager.redo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});

		am.put("Save", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EdMenuBar.doSave();
			}
		});
	}

	// setting Mouse Action Listeners for Right Click Menu
	private class PopupTriggerListener extends MouseAdapter {
		public void mousePressed(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				jpm.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}

		public void mouseReleased(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				jpm.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}

		public void mouseClicked(MouseEvent ev) {
		}
	}

	// a getter and a setter for manager variable
	public static UndoManager getManager() {
		return manager;
	}

	public void setManager(UndoManager manager) {
		TextEditor.manager = manager;
	}

	// a getter and a setter for jta variable
	public static JTextArea getJta() {
		return jta;
	}

	public void setJta(JTextArea jta) {
		TextEditor.jta = jta;
	}

	// a getter and a setter for jl variable
	public static JLabel getJl() {
		return jl;
	}

	public static void setJl(JLabel jl) {
		TextEditor.jl = jl;
	}

}
