package homework10;

/* @author Andrius Didziulis
 * Andrew ID: adidziul
 * Date: 12/09/2015
 * 
 * This a class of bookmark object that uses a comparator to sort
 * bookmarks alphabetically. The class is used by EdMenuBar class
 */

public class Bookmark implements Comparable <Bookmark> {
	String fileName;
	String bookmarkName;
	int bookmarkPosition;
	
	public Bookmark(String fileName, String bookmarkName, int bookmarkPosition) {
		this.fileName = fileName;
		this.bookmarkName = bookmarkName;
		this.bookmarkPosition = bookmarkPosition;
		}
	// comparing bookmark names with each other
	@Override
	public int compareTo(Bookmark b) {
		return this.bookmarkName.compareTo(b.bookmarkName);
	}
}
