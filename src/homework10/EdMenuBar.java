package homework10;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

/* @author Andrius Didziulis
 * Andrew ID: adidziul
 * Date: 12/09/2015
 * 
 * This the class to describe the menu bar for text editor.
 * Also it contains menus, menu items and actions that each of
 * the item performs. 
 */

@SuppressWarnings("serial")
public class EdMenuBar extends JMenuBar {
	private JMenu fm;
	private JMenu tm;
	private JMenu hm;
	private JMenu searchMenu;
	private JMenu oldMenu;
	private JMenuItem kwItem;
	private static JFileChooser fileExplorer;
	private JFrame comboBoxFrame;
	private static BufferedWriter bw;
	private int count;
	private int pos;
	private int inc = 0;
	private LinkedList<JMenuItem> searchHistory = new LinkedList<JMenuItem>();
	private ArrayList<Bookmark> bookmarkHistory = new ArrayList<Bookmark>();
	private static JComboBox<String> jcb;

	public EdMenuBar() {

		// describing menu bar

		fm = new JMenu("File");
		tm = new JMenu("Tools");
		hm = new JMenu("Help");
		add(fm);
		add(tm);
		add(hm);

		// initializing JFileChooser that lets choose files
		fileExplorer = new JFileChooser();
		fileExplorer.setCurrentDirectory(new File(TextEditor.DEFAULT_PATH));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
		fileExplorer.setFileFilter(filter);

		// adding menu items with their actions
		addMenuItems();
	}

	// setting the content of menus
	private void addMenuItems() {
		// filling in the File Menu
		JMenuItem openItem = new JMenuItem("Open...");
		openItem.setActionCommand("Open");
		openItem.addActionListener(new MenuItemListener());
		fm.add(openItem);

		fm.addSeparator();

		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setActionCommand("Save");
		saveItem.addActionListener(new MenuItemListener());
		fm.add(saveItem);

		JMenuItem saveAsItem = new JMenuItem("Save As...");
		saveAsItem.setActionCommand("As");
		saveAsItem.addActionListener(new MenuItemListener());
		fm.add(saveAsItem);

		fm.addSeparator();

		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.setActionCommand("Close");
		closeItem.addActionListener(new MenuItemListener());
		fm.add(closeItem);

		fm.addSeparator();

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setActionCommand("Exit");
		exitItem.addActionListener(new MenuItemListener());
		fm.add(exitItem);

		// filling in the Tools -> Words menu
		JMenu wordsMenu = new JMenu("Words");
		wordsMenu.setEnabled(true);
		tm.add(wordsMenu);
		JMenuItem countItem = new JMenuItem("Count");
		countItem.setEnabled(true);
		countItem.setActionCommand("Count");
		countItem.addActionListener(new MenuItemListener());
		wordsMenu.add(countItem);
		JMenuItem uniqueItem = new JMenuItem("Unique");
		uniqueItem.setEnabled(true);
		uniqueItem.setActionCommand("Unique");
		uniqueItem.addActionListener(new MenuItemListener());
		wordsMenu.add(uniqueItem);
		searchMenu = new JMenu("Search");
		searchMenu.setEnabled(true);
		wordsMenu.add(searchMenu);
		JMenuItem searchItem = new JMenuItem("Search new...");
		searchItem.setEnabled(true);
		searchItem.setActionCommand("Search");
		searchItem.addActionListener(new MenuItemListener());
		searchMenu.add(searchItem);

		searchMenu.addSeparator();

		// filling in the Tools -> Bookmark menu
		JMenu bookmarkMenu = new JMenu("Bookmark");
		bookmarkMenu.setEnabled(true);
		tm.add(bookmarkMenu);
		JMenuItem newItem = new JMenuItem("New...");
		newItem.setEnabled(true);
		newItem.setActionCommand("New");
		newItem.addActionListener(new MenuItemListener());
		bookmarkMenu.add(newItem);
		oldMenu = new JMenu("Old");
		oldMenu.setEnabled(true);
		bookmarkMenu.add(oldMenu);
		JMenuItem deleteItem = new JMenuItem("Delete...");
		deleteItem.setEnabled(true);
		deleteItem.setActionCommand("Delete");
		deleteItem.addActionListener(new MenuItemListener());
		bookmarkMenu.add(deleteItem);

		// filling in the Tools Menu
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setActionCommand("About");
		aboutItem.addActionListener(new MenuItemListener());
		hm.add(aboutItem);

		JMenuItem featureItem = new JMenuItem("New feature..");
		featureItem.setActionCommand("Feature");
		featureItem.addActionListener(new MenuItemListener());
		hm.add(featureItem);

	}

	// describing a windows for deleting a bookmark
	private void setupComboBoxFrame() {
		comboBoxFrame = new JFrame("Delete Bookmarks");
		comboBoxFrame.setVisible(true);
		comboBoxFrame.setPreferredSize(new Dimension(350, 110));
		JPanel upperPanel = new JPanel();
		JLabel label = new JLabel("Select a bookmark to delete: ");
		jcb = new JComboBox<String>();
		jcb.setPreferredSize(new Dimension(100, 20));
		upperPanel.add(label);
		upperPanel.add(jcb);
		comboBoxFrame.add(upperPanel, BorderLayout.PAGE_START);

		JPanel buttonPanel = new JPanel();
		comboBoxFrame.add(buttonPanel, BorderLayout.PAGE_END);
		JButton deleteButton = new JButton("Delete");
		JButton cancelButton = new JButton("Cancel");
		deleteButton.addActionListener(new DeleteButtonListener());
		buttonPanel.add(deleteButton);
		cancelButton.addActionListener(new CancelButtonListener());
		buttonPanel.add(cancelButton);
		comboBoxFrame.pack();
	}

	// setting what action should be performed in case of selecting a particular
	// menu item
	private class MenuItemListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "Open":
				doOpen();
				break;
			case "Save":
				doSave();
				break;
			case "As":
				doSaveAs();
				break;
			case "Close":
				doClose();
				break;
			case "Exit":
				System.exit(0);
				break;
			case "Count":
				doCount();
				break;
			case "Unique":
				doUnique();
				break;
			case "Search":
				doSearch();
				break;
			case "New":
				doBookmark();
				break;
			case "Delete":
				deleteBookmark();
				break;
			case "About":
				showAboutWindow();
				break;
			case "Feature":
				try {
					showNewFeature();
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "We cannot find the file. Please check if it still exists.",
							"File Not Found", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
		}

	}

	// setting Actionlisteners for elements in search history
	private class SearchMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			searchKeyword(TextEditor.getJta(), e.getActionCommand());
		}
	}

	// setting Actionlisteners for elements in bookmark history
	private class BookmarkMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Iterator<Bookmark> iter = bookmarkHistory.iterator();
			while (iter.hasNext()) {
				Bookmark bm = iter.next();
				if (bm.bookmarkName.equals(e.getActionCommand())) {
					if (bm.fileName.equals(fileExplorer.getSelectedFile().getName())) {
						TextEditor.getJta().setCaretPosition(bm.bookmarkPosition + 1);
						TextEditor.getJta().moveCaretPosition(bm.bookmarkPosition);
					} else
						JOptionPane.showMessageDialog(null,
								"In order to see the bookmark, please choose the file " + bm.fileName, "Bookmark error",
								JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	// setting Actionlisteners for elements in bookmark history
	private class DeleteButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int bp = jcb.getSelectedIndex();
			bookmarkHistory.remove(bp);
			sortBookmarks();
			comboBoxFrame.dispose();
		}
	}

	// setting Actionlisteners for elements in bookmark history
	private class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			comboBoxFrame.dispose();
		}
	}

	// describing an action of searching words and displaying the search
	// history
	private void doSearch() {
		String keyword = (String) JOptionPane.showInputDialog(null, "Please enter a string to search:", "Input",
				JOptionPane.QUESTION_MESSAGE, null, null, "");
		if (keyword != null) {
			if (keyword.length() != 0) {
				searchKeyword(TextEditor.getJta(), keyword);
				if (count != 0) {
					kwItem = new JMenuItem(keyword);
					kwItem.setEnabled(true);
					kwItem.setActionCommand(keyword);
					kwItem.addActionListener(new SearchMenuListener());
					searchMenu.add(kwItem);
					searchHistory.add(kwItem);
				}
			} else
				JOptionPane.showMessageDialog(null, "Nothing was searched! The search window will be closed.",
						"Search error", JOptionPane.ERROR_MESSAGE);
		}
		if (searchHistory.size() == 6) {
			searchMenu.remove(searchHistory.get(0));
			searchHistory.remove(0);
			searchMenu.validate();
			searchMenu.repaint();
		}
	}

	// describing an action of searching a keyword in the text
	private void searchKeyword(JTextArea jta, String keyword) {
		try {
			count = 0;
			pos = 0;
			Highlighter hl = jta.getHighlighter();
			hl.removeAllHighlights();
			Document doc = jta.getDocument();
			String text = doc.getText(0, doc.getLength());

			while ((pos = text.toUpperCase().indexOf(keyword.toUpperCase(), pos)) >= 0) {
				hl.addHighlight(pos, pos + keyword.length(), DefaultHighlighter.DefaultPainter);
				if (count == 0)
					TextEditor.getJta().setCaretPosition(pos);
				pos += keyword.length();
				count++;
			}

		} catch (BadLocationException e) {
			System.out.println("Error reading the keyword.");
			System.exit(0);
		}
		TextEditor.getJl().setText(keyword + ": " + count + " occurences.");
	}

	// describing an action of counting unique words in the text
	private void doUnique() {
		if (TextEditor.getJta().getText().equals("")) {
			TextEditor.getJl().setText("Unique Words: 0");
		} else {
			Set<String> uniqueSet = new HashSet<String>();
			String cleanTextArea = TextEditor.getJta().getText().trim();
			String[] words = cleanTextArea.toLowerCase().split("[-.,:;?!~\\s]+");
			for (String word : words) {
				uniqueSet.add(word);
			}
			TextEditor.getJl().setText("Unique Words: " + uniqueSet.size());
		}
	}

	// describing an action of counting all words in the text
	private void doCount() {
		StringTokenizer myTokens = new StringTokenizer(TextEditor.getJta().getText());
		TextEditor.getJl().setText("Word Count: " + myTokens.countTokens());
	}

	// describing an action of opening the file
	private void doOpen() {
		int result = fileExplorer.showOpenDialog(TextEditor.getJta());
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedReader br = new BufferedReader(
						new FileReader(fileExplorer.getSelectedFile().getAbsolutePath()));
				TextEditor.getJta().read(br, null);
				br.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "File is not found", "File not found", JOptionPane.ERROR_MESSAGE);
			}
			TextEditor.getManager().discardAllEdits();
			TextEditor.getJl().setText(fileExplorer.getSelectedFile().getName());
			TextEditor.addUndoRedoSave();

		}

	}

	// describing an action of showing the 'About' window
	private void showAboutWindow() {
		JDialog jd = new JDialog();
		jd.setVisible(true);
		jd.setSize(400, 400);
		jd.setTitle("About Text Editor");
		
		EdMenuBar.class.getClassLoader();
		String filename = ClassLoader.getSystemResource("\\resources\\cmu.jpg").toString();
		JEditorPane aboutText = new JEditorPane("text/html",
				"<html><b>Text Editor</b><br />Created by Andrius Didziulis, MISM'16<br />Please contact me "
						+ "by email: <a href='mailto:adidziul@andrew.cmu.edu?Subject=Java%20HW7'>adidziul@andrew.cmu.edu</a>"
						+ "<br /><br />Version: 1.0<br/>" + "Released on 11/18/2015<br /><br/><br/><br/><"
						+ "<table border='0' width='370'><tr align='center'><td>"
						+ "<img src=\'"+filename+"\'></td></tr></table></html>");
		aboutText.setEditable(false);
		aboutText.setEnabled(true);
		jd.add(aboutText);

		aboutText.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent he) {
				if (HyperlinkEvent.EventType.ACTIVATED.equals(he.getEventType())) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(he.getURL().toURI());
					} catch (Exception ex) {
						System.out.println("Unable to open the hyperlink.");
					}
				}
			}
		});
	}

	// describing an action perfomed when Save As menu item is selected
	private static void doSaveAs() {
		int result = fileExplorer.showSaveDialog(TextEditor.getJta());
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				bw = new BufferedWriter(new FileWriter(fileExplorer.getSelectedFile().getAbsolutePath(), false));
				String s = TextEditor.getJta().getText();
				bw.write(s);
				bw.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "File is not found", "File not found", JOptionPane.ERROR_MESSAGE);
			}
			TextEditor.getJl().setText(fileExplorer.getSelectedFile().getName() + " successfully saved!");
		}
	}

	// describing an action perfomed when Save menu item is selected
	static void doSave() {
		if (fileExplorer.getSelectedFile() != null) {
			try {
				bw = new BufferedWriter(new FileWriter(fileExplorer.getSelectedFile().getAbsolutePath(), false));
				String s = TextEditor.getJta().getText();
				bw.write(s);
				bw.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "File is not found", "File not found", JOptionPane.ERROR_MESSAGE);
			}
			TextEditor.getJl().setText(fileExplorer.getSelectedFile().getName() + " successfully saved!");
		} else
			doSaveAs();
	}

	// describing an action of deleting a bookmark
	private void deleteBookmark() {
		if (bookmarkHistory.isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"Bookmark History does not contain any records to be deleted. Please have at least one bookmark.",
					"No deletion is possible", JOptionPane.ERROR_MESSAGE);
		} else {
			setupComboBoxFrame();
			for (Bookmark b : bookmarkHistory) {
				jcb.addItem(b.bookmarkName);
			}

		}

	}

	// describing an action of saving a bookmark
	private void doBookmark() {
		if (fileExplorer.getSelectedFile() != null) {
			String tagName = (String) JOptionPane.showInputDialog(null, "Enter tag name:", "Input",
					JOptionPane.QUESTION_MESSAGE, null, null, "");
			int bookmarkPosition = TextEditor.getJta().getCaretPosition();
			String fileName = fileExplorer.getSelectedFile().getName();
			if (tagName != null) {
				if (tagName.length() != 0) {
					bookmarkHistory.add(new Bookmark(fileName, tagName, bookmarkPosition));
					inc++;
					if (inc > 1) {
						sortBookmarks();
					} else {
						JMenuItem bookmarkItem = new JMenuItem(tagName);
						bookmarkItem.setEnabled(true);
						bookmarkItem.setActionCommand(tagName);
						bookmarkItem.addActionListener(new BookmarkMenuListener());
						oldMenu.add(bookmarkItem);
					}
				}
			}
		} else
			JOptionPane.showMessageDialog(null, "Please save a file in order to do a bookmark.", "Bookmark error",
					JOptionPane.ERROR_MESSAGE);
	}

	// sorting bookmark history in an alphabetical order
	private void sortBookmarks() {
		Collections.sort(bookmarkHistory);
		oldMenu.removeAll();
		for (Bookmark b : bookmarkHistory) {
			JMenuItem bookmarkItem = new JMenuItem(b.bookmarkName);
			bookmarkItem.setEnabled(true);
			bookmarkItem.setActionCommand(b.bookmarkName);
			bookmarkItem.addActionListener(new BookmarkMenuListener());
			oldMenu.add(bookmarkItem);
		}
	}
	
	// closing the file
	private void doClose() {
		TextEditor.getJta().setText("");
		TextEditor.getJl().setText("");
		fileExplorer.setSelectedFile(null);
		TextEditor.getManager().discardAllEdits();
	}
	
	// showing Readme file of new feature
	private void showNewFeature() throws FileNotFoundException {
		doClose();
		URL url = TextEditor.class.getResource("/resources/Readme.txt");
		try (Scanner input = new Scanner(new File(url.getFile()))) {
			while (input.hasNextLine()) {
				String line = input.nextLine();
				TextEditor.getJta().append(line + "\n");
			}
			input.close();
		}
		TextEditor.getJl().setText("Readme.txt");
		TextEditor.addUndoRedoSave();
	}
}
